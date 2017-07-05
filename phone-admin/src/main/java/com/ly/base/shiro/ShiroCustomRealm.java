package com.ly.base.shiro;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ly.base.common.model.Json;
import com.ly.base.common.redis.CacheAccessException;
import com.ly.base.common.redis.RedisClientSupport;
import com.ly.base.common.system.SystemConfig;
import com.ly.base.common.util.ArrayUtil;
import com.ly.base.common.util.MD5Util;
import com.ly.base.common.util.ReflectionUtil;
import com.ly.base.common.util.StringUtil;
import com.ly.base.core.model.sys.SysMenu;
import com.ly.base.core.model.sys.SysUser;
import com.ly.base.proxy.sys.SysMenuProxy;
import com.ly.base.service.consumer.sys.SysUserConsumerService;

@Component
public class ShiroCustomRealm extends AuthorizingRealm {
	private static Logger logger = LoggerFactory.getLogger(ShiroCustomRealm.class);
	private static final String LOGIN_COUNT_NAME = "LOGIN_COUNT";

	@Autowired
	private SysUserConsumerService sysUserConsumerService;
	@Autowired
	private SysMenuProxy sysMenuProxy;
	@Autowired
	@Qualifier("redisClientSupport")
	private RedisClientSupport redisClientSupport;

	public ShiroCustomRealm() {
		super();
	}
	
	/**
	 * 认证回调函数, 登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		if (logger.isDebugEnabled()) {
			logger.debug("doGetAuthenticationInfo(AuthenticationToken) - start"); //$NON-NLS-1$
		}

		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		System.out.println(token.getUsername());
		String tokenName = token.getUsername();
//		int splitIndex = tokenName.indexOf(SystemConfig.SPLIT_CHARPTER);
//		String second = tokenName.substring(0, splitIndex);
//		Json orgInfo = sysOrganizationConsumerService.getPkByShortUrl(second);
//		if (!orgInfo.isSuccess()||orgInfo.getObj()==null) {
//			throw new SysOrganizationDisabledException(orgInfo.getMsg());
//		}
//		SysOrganization org = ReflectionUtil.convertObjectToBean(orgInfo.getObj(), SysOrganization.class);
//		if (org==null) {
//			throw new SysOrganizationDisabledException(orgInfo.getMsg());
//		}
//		String orgPK=org.getPk();//根据用户访问的地址中解析出 该地址所对应的组织主键
		Integer count = null;
		String loginCountCacheKey = StringUtil.appendStringByObject(":",LOGIN_COUNT_NAME,tokenName);
		try {
			//如果密码错误次数过多,冻结5分钟
			Object obj = redisClientSupport.getValue(loginCountCacheKey);
			count = obj==null?null:ReflectionUtil.convertObjectToBean(obj, Integer.class);
			if (count!=null&&count>=SystemConfig.LOGIN_COUNT) {
				Long expire = redisClientSupport.getRedisTemplate().getExpire(loginCountCacheKey);
				//如果已配置超时时间则不再重设
				if (expire!=null&&expire<=0) {
					redisClientSupport.expire(loginCountCacheKey, SystemConfig.LOGIN_COUNT_TIME*60);
				}
				//提示登录次数超过限制
				throw new ExcessiveAttemptsException(StringUtil.appendStringByObject(null, "密码错误",count,"次,请",SystemConfig.LOGIN_COUNT_TIME,"分钟后再试"));
			}
		} catch (CacheAccessException e1) {
			logger.warn("doGetAuthenticationInfo(AuthenticationToken) - exception ignored", e1); //$NON-NLS-1$
		}
		//执行登录
		String loginName = tokenName;
		String password = String.valueOf(token.getPassword());
		Json json = sysUserConsumerService.loginSystem(loginName, MD5Util.md5(password));//(token.getUsername(),token.getPassword().toString());
		if (json != null&&json.isSuccess()) {
			SysUser su = ReflectionUtil.convertObjectToBean(json.getObj(), SysUser.class);
			//状态为0则表示账户已冻结
			if (su==null) {
				throw new UnsupportedOperationException();
			}
			if (!su.getEnable().equals("1")) {
				throw new LockedAccountException();
			}
//			if (!su.getSysDepartment().getEnable().equals("1")) {
//				throw new SysDepartmentDisabledException();
//			}
			try {
				//登录成功后删除错误登录计数器
				redisClientSupport.delete(loginCountCacheKey);
			} catch (CacheAccessException e) {
				logger.warn("doGetAuthenticationInfo(AuthenticationToken) - exception ignored", e); //$NON-NLS-1$
			}
			AuthenticationInfo returnAuthenticationInfo = new SimpleAuthenticationInfo(su, password, getName());
			if (logger.isDebugEnabled()) {
				logger.debug("doGetAuthenticationInfo(AuthenticationToken) - end"); //$NON-NLS-1$
			}
			return returnAuthenticationInfo;
		}else{
			//密码错误则计数器加1
			Long errorCount = null;
			try {
				errorCount = redisClientSupport.incrementValue(loginCountCacheKey, 1);
				redisClientSupport.expire(loginCountCacheKey, SystemConfig.LOGIN_ERROR_TIME*60);
			} catch (CacheAccessException e) {
				logger.warn("doGetAuthenticationInfo(AuthenticationToken) - exception ignored", e); //$NON-NLS-1$
			}
			String message = errorCount==null?"密码错误,请重试":StringUtil.appendStringByObject(null,"密码错误",errorCount,"次,错误",SystemConfig.LOGIN_COUNT,"次后将冻结",SystemConfig.LOGIN_COUNT_TIME,"分钟");
			logger.warn("LoginError(userName:{},password:{})",loginName,password); //$NON-NLS-1$
			throw new AuthenticationException(message);
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		if (logger.isDebugEnabled()) {
			logger.debug("doGetAuthorizationInfo(PrincipalCollection) - start"); //$NON-NLS-1$
		}

		Json json = sysMenuProxy.findAllFuncStringByRole(null);
		if (json.isSuccess()) {
			Object obj = json.getObj();
			if (obj!=null) {
				/* 这里编写授权代码 */
				Set<String> roleNames = new HashSet<String>();
			    Set<String> permissions = new HashSet<String>();
				List<?> list = ReflectionUtil.convertObjectToBean(obj, List.class);
				boolean res = ArrayUtil.foreach(list, (v,i)->{
					SysMenu sm = ReflectionUtil.convertObjectToBean(v,SysMenu.class);
					if (sm!=null) {
						String pk = sm.getPk();
						String names = sm.getName();
						if (StringUtils.isNotEmpty(names)) {
							ArrayUtil.foreach(names.split(","), (n,j)->{
								permissions.add(StringUtil.appendStringNotNull(null, pk,n));
								return true;
							});
						}
						return true;
					}
					return false;
				});
				if(!res){
					if (logger.isDebugEnabled()) {
						logger.debug("doGetAuthorizationInfo(PrincipalCollection) - end"); //$NON-NLS-1$
					}
					return null;
				}
				SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
			    info.setStringPermissions(permissions);

				if (logger.isDebugEnabled()) {
					logger.debug("doGetAuthorizationInfo(PrincipalCollection) - end"); //$NON-NLS-1$
				}
			    return info;
			}
//		    roleNames.add("admin");
//		    roleNames.add("zhangsan");
//		    permissions.add("user.do?myjsp");
//		    permissions.add("login.do?main");
//		    permissions.add("login.do?logout");
//		    SecurityUtils.getSubject().checkPermission(permission);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("doGetAuthorizationInfo(PrincipalCollection) - end"); //$NON-NLS-1$
		}
		return null;
	}

	/**
	 * 更新用户授权信息缓存.
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		if (logger.isDebugEnabled()) {
			logger.debug("clearCachedAuthorizationInfo(String) - start"); //$NON-NLS-1$
		}

		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);

		if (logger.isDebugEnabled()) {
			logger.debug("clearCachedAuthorizationInfo(String) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 清除所有用户授权信息缓存.
	 */
	public void clearAllCachedAuthorizationInfo() {
		if (logger.isDebugEnabled()) {
			logger.debug("clearAllCachedAuthorizationInfo() - start"); //$NON-NLS-1$
		}

		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("clearAllCachedAuthorizationInfo() - end"); //$NON-NLS-1$
		}
	}

//	@PostConstruct
//	public void initCredentialsMatcher() {//MD5加密
//		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(ALGORITHM);
//		setCredentialsMatcher(matcher);
//	}
}
