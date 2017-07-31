package com.ly.base.proxy.sys;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ly.base.common.model.AddressBean;
import com.ly.base.common.model.Json;
import com.ly.base.common.model.Model;
import com.ly.base.common.redis.CacheAccessException;
import com.ly.base.common.redis.RedisClientSupport;
import com.ly.base.common.system.ErrorConfig;
import com.ly.base.common.system.SystemConfig;
import com.ly.base.common.system.cookies.AdminCookieConfig;
import com.ly.base.common.system.redis.RedisConfig;
import com.ly.base.common.system.redis.RedisKeyConfig;
import com.ly.base.common.util.BeanUtil;
import com.ly.base.common.util.IpUtil;
import com.ly.base.common.util.MD5Util;
import com.ly.base.common.util.ReflectionUtil;
import com.ly.base.common.util.SpringBeanUtil;
import com.ly.base.common.util.StringUtil;
import com.ly.base.core.model.log.LogLogin;
import com.ly.base.core.model.sys.SysUser;
import com.ly.base.core.util.WebUtils;
import com.ly.base.proxy.log.LogLoginProxy;
import com.ly.base.service.consumer.sys.SysUserConsumerService;
import com.ly.base.shiro.exception.SysDepartmentDisabledException;
import com.ly.base.shiro.exception.SysOrganizationDisabledException;

/**
 * 参数验证,参数处理,缓存session、cookie
 * @author LeiYong
 * @date 2016年10月04日
 */
@Component
public class SysUserProxy {
	private static final Map<String, String> fieldNameMap = new HashMap<>();
	static {
		fieldNameMap.put("loginName", "登录名");
		fieldNameMap.put("accountPk", "会员级别");
		fieldNameMap.put("name", "姓名");
		fieldNameMap.put("phone", "手机号");
		fieldNameMap.put("sex", "性别");
		fieldNameMap.put("rolePk", "所属角色");
		fieldNameMap.put("alipay", "支付宝");
	}
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SysUserProxy.class);

	@Autowired
	private SysUserConsumerService service;

	/**
	 * 添加用户金额
	 * @param request
	 * @param response
	 * @param ratio
	 * @param pk
	 * @return
	 */
	public Json addRatio(HttpServletRequest request, HttpServletResponse response, BigDecimal ratio, String pk) {
		if(StringUtils.isEmpty(pk)||ratio==null) {return ErrorConfig.getSystemParamErrorJson();};
		return checkResult(service.addRatio(ratio,pk));
	}
	/**
	 * 获取个人信息
	 * @param request
	 * @return
	 */
	public Json myInfo(HttpServletRequest request) {
		SysUser su = WebUtils.getLoginUser(request);
		return getByPk(request, su.getPk());
	}
	/**
	 * 修改个人信息
	 * @param request
	 * @param phone
	 * @param birthday
	 * @param alipay
	 * @param wechat
	 * @param qq
	 * @return
	 */
	public Json changeMyInfo(HttpServletRequest request, String wechat,
			String qq) {
		SysUser su = WebUtils.getLoginUser(request);
		su.setWechat(wechat);
		su.setQq(qq);
		return update(request, su);
	}
	/**
	 * 通过主键查询
	 * 
	 * @param pk
	 * @return
	 */ 
	public Json getByPk(HttpServletRequest request,String pk) {
		if(pk == null) {return ErrorConfig.getSystemParamErrorJson();};
		return checkResult(service.getByPk(pk));
	}
	/**
	 * 通过主键删除
	 * 
	 * @param pk
	 * @return
	 */ 
	public Json deleteByPk(HttpServletRequest request,String pk) {
		if(pk == null) {return ErrorConfig.getSystemParamErrorJson();};
		return checkResult(service.deleteByPk(pk));
	}
	/**
	 * 通过主键批量删除
	 * 
	 * @param pks
	 * @return
	 */ 
	public Json deleteByBatch(HttpServletRequest request,String... pks) {
		if(pks == null) {return ErrorConfig.getSystemParamErrorJson();};
		return checkResult(service.deleteByBatch(pks));
	}
	/**
	 * 通过主键启用
	 * 
	 * @param pk
	 * @return
	 */ 
	public Json enable(HttpServletRequest request,String pk) {
		if(pk == null) {return ErrorConfig.getSystemParamErrorJson();};
		SysUser user = WebUtils.getLoginUser(request);
		return checkResult(service.enable(user.getPk(),pk));
	}
	/**
	 * 通过主键禁用
	 * 
	 * @param pk
	 * @return
	 */ 
	public Json disable(HttpServletRequest request,String pk) {
		if(pk == null) {return ErrorConfig.getSystemParamErrorJson();};
		SysUser user = WebUtils.getLoginUser(request);
		return checkResult(service.disable(user.getPk(),pk));
	}
	/**
	 * 通过主键批量启用
	 * 
	 * @param pks
	 * @return
	 */ 
	public Json enables(HttpServletRequest request,String... pks) {
		if(pks == null) {return ErrorConfig.getSystemParamErrorJson();};
		SysUser user = WebUtils.getLoginUser(request);
		return checkResult(service.enables(user.getPk(),pks));
	}
	/**
	 * 通过主键批量禁用
	 * 
	 * @param pks
	 * @return
	 */ 
	public Json disables(HttpServletRequest request,String... pks) {
		if(pks == null) {return ErrorConfig.getSystemParamErrorJson();};
		SysUser user = WebUtils.getLoginUser(request);
		return checkResult(service.disables(user.getPk(),pks));
	}
	/**
	 * 新增记录
	 * 
	 * @param data
	 * @return
	 */ 
	public Json insert(HttpServletRequest request,SysUser data) {
		String res = checkPasword(data.getPassword());
		if (res!=null) {
			return new Json(res);
		}
		SysUser user = WebUtils.getLoginUser(request);
		String checkRes = checkData(data,user);
		if (checkRes != null) {
			return new Json(checkRes);
		}
		data.setCreateTime(data.getUpdateTime());
		Json checkLoginName = service.checkLoginName(data.getLoginName());
		if(checkLoginName.isSuccess()){
			return checkResult(service.insert(data));
		}else{
			return checkLoginName;
		}
	}
	/**
	 * 批量新增
	 * 
	 * @param list
	 * @return
	 */ 
	public Json insertBatch(HttpServletRequest request,List<SysUser> list) {
		if(list == null) {return ErrorConfig.getSystemParamErrorJson();};
		SysUser user = WebUtils.getLoginUser(request);
		String checkResult = null;
		for(SysUser data : list) {
			checkResult = checkData(data,user);
			if(checkResult != null){
				return new Json().setMsg(checkResult);
			}
		}
		return checkResult(service.insertBatch(list));
	}
	/**
	 * 更新记录
	 * 
	 * @param data
	 * @return
	 */ 
	public Json update(HttpServletRequest request,SysUser data) {
		if (StringUtils.isNotBlank(data.getPassword())) {
			String res = checkPasword(data.getPassword());
			if (res!=null) {
				return new Json(res);
			}
		}
		SysUser user = WebUtils.getLoginUser(request);
		String checkRes = checkData(data,user);
		if (checkRes != null) {
			return new Json().setMsg(checkRes);
		}
		return checkResult(service.update(data));
	}
	/**
	 * 分页查询
	 * 
	 * @param conditions
	 * @param pageNum
	 * @param pageSize
	 * @param orderBy
	 * @return
	 */ 
	public Json findByPage(HttpServletRequest request,SysUser queryInfo, int pageNum, int pageSize,String orderBy) {
		if (pageNum < 1 || pageSize < 1 ) {
			return ErrorConfig.getSystemErrorJson();
		}
		//queryInfo->conditions
		List<Model> conditions = getConditions(queryInfo,request);
		return checkResult(service.findByPage(conditions,pageNum,pageSize,orderBy));
	}
	/**
	 * 查询所有
	 * 
	 * @param conditions
	 * @param orderBy
	 * @return
	 */ 
	public Json findAll(HttpServletRequest request,SysUser queryInfo, String orderBy) {
		//queryInfo->conditions
		List<Model> conditions = getConditions(queryInfo,request);
		return checkResult(service.findAll(conditions,orderBy));
	}
	/**
	 * 导出
	 * 
	 * @param queryInfo
	 * @param orderBy
	 * @return
	 */ 
	public Json findByExport(HttpServletRequest request, SysUser queryInfo, String orderBy) {
		//queryInfo->conditions
		List<Model> conditions = getConditions(queryInfo,request);
		return checkResult(service.findByExport(conditions,orderBy));
	}
	/**
	 * 验证登录名
	 * @param request
	 * @param userName
	 * @return
	 */
	public Json checkLoginName(HttpServletRequest request,String userName) {
		return checkResult(service.checkLoginName(userName));
	}
	/**
	 * 修改个人密码
	 * @param request
	 * @param password
	 * @param newPassword
	 * @return
	 */
	public Json changePassword(HttpServletRequest request,String password,String newPassword) {
		if (!StringUtil.checkNotEmpty(password,newPassword)) {
			return ErrorConfig.getSystemParamErrorJson();
		}
		SysUser su = WebUtils.getLoginUser(request);
		return updatePassword(request, su.getPk(), password, newPassword);
	}
	/**
	 * 修改用户密码
	 * @param request
	 * @param pk
	 * @param password
	 * @param newPassword
	 * @return
	 */
	public Json updatePassword(HttpServletRequest request, String pk, String password, String newPassword) {
		if (!StringUtil.checkNotEmpty(pk,newPassword)) {
			return ErrorConfig.getSystemParamErrorJson();
		}
		SysUser su = WebUtils.getLoginUser(request);
		if (password!=null) {
			Object pwd = SecurityUtils.getSubject().getSession().getAttribute("password");
			if (!MD5Util.md5(password).equals(pwd)) {
				return new Json("密码错误");
			}
		}
		String newPwd = MD5Util.md5(newPassword);
		Json result = checkResult(service.changePwd(su.getPk(), pk, newPwd));
		if (result.isSuccess()) {
			//如果是修改本人的密码
			if (su.getPk().equals(pk)) {
				//更新缓存信息
				SecurityUtils.getSubject().getSession().setAttribute("password",newPwd);
			}
		}
		return result;
	}
	/**
	 * 登录
	 * @param request
	 * @param response
	 * @param loginName
	 * @param password
	 * @param rememberMe
	 * @return
	 */
	public Json loginSystem(HttpServletRequest request, HttpServletResponse response,String loginName,String password,String rememberme) {
		Json loginResult = new Json();
		if (!StringUtil.checkNotEmpty(loginName,password,rememberme)) {
			return ErrorConfig.getSystemParamErrorJson();
		}
		boolean remember = "1".equals(rememberme);// 记住我
		//如果自动登录则不校验验证码
		if (request.getAttribute("autoLoginUser")==null) {
			Json captchaResult = WebUtils.checkKaptcha(request);// 验证码校验
			// 如果校验失败
			if (!captchaResult.isSuccess()) {
				return captchaResult;
			}
		}else{
			request.removeAttribute("autoLoginUser");
		}
		UsernamePasswordToken token = new UsernamePasswordToken(loginName,
				password);
		// 获取当前的Subject
		Subject currentUser = SecurityUtils.getSubject();
		// 如果已校验成功,直接跳转到登录页面
		try {
			// 在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
			// 每个Realm都能在必要时对提交的AuthenticationTokens作出反应
			// 所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
			currentUser.login(token);
			if (currentUser.isAuthenticated()) {
				Object currentSu = currentUser.getPrincipal();
				SysUser sysUser = ReflectionUtil.convertObjectToBean(currentSu, SysUser.class);
				if (sysUser != null) {
					sysUser.setIp(IpUtil.getIp(request));
					//缓存密码
					SecurityUtils.getSubject().getSession().setAttribute("password", sysUser.getPassword());
					sysUser.setPassword(null);
				}
				loginResult.setSuccess(true).setObj(sysUser);
				// 将帐号密码+ip作为缓存,用于记住我功能,用户帐号密码使用base64等多重加密后存储到数据库中
				rememberMe(response, token, sysUser.getIp(), remember);
				// 记录登录日志
				logLogin(request, sysUser);
			} else {
				token.clear();
			}
			// resultPageURL = "main";
		} catch (SysOrganizationDisabledException uae) {
			logger.error("loginSystem(HttpServletRequest, HttpServletResponse, String, String, boolean)", uae); //$NON-NLS-1$
			loginResult.setSuccess(false).setMsg(uae.getMessage());
		} catch (SysDepartmentDisabledException uae) {
			logger.error("loginSystem(HttpServletRequest, HttpServletResponse, String, String, boolean)", uae); //$NON-NLS-1$
			loginResult.setSuccess(false).setMsg(uae.getMessage());
		} catch (IncorrectCredentialsException ice) {
			logger.error("loginSystem(HttpServletRequest, HttpServletResponse, String, String, boolean)", ice); //$NON-NLS-1$
			loginResult.setSuccess(false).setMsg("密码不正确");
		} catch (LockedAccountException lae) {
			logger.error("loginSystem(HttpServletRequest, HttpServletResponse, String, String, boolean)", lae); //$NON-NLS-1$
			loginResult.setSuccess(false).setMsg("账户已锁定");
		} catch (ExcessiveAttemptsException eae) {
			logger.error("loginSystem(HttpServletRequest, HttpServletResponse, String, String, boolean)", eae); //$NON-NLS-1$
			loginResult.setSuccess(false).setMsg("用户名或密码错误次数过多,已冻结" + SystemConfig.LOGIN_COUNT_TIME + "分钟");
		} catch (AuthenticationException ae) {
			logger.error("loginSystem(HttpServletRequest, HttpServletResponse, String, String, boolean)", ae); //$NON-NLS-1$
			loginResult.setSuccess(false).setMsg(ae.getMessage());
			ae.printStackTrace();
		} catch (UnsupportedOperationException e) {
			logger.error("loginSystem(HttpServletRequest, HttpServletResponse, String, String, boolean)", e); //$NON-NLS-1$
			return ErrorConfig.getSystemErrorJson();
		}
		return loginResult;
	}
	/**
	 * 注销
	 * @param request
	 * @param response
	 * @return
	 */
	public Json logout(HttpServletRequest request, HttpServletResponse response) {
		Json json = new Json();
		Subject subject = SecurityUtils.getSubject();
		SysUser sysUser = ReflectionUtil.convertObjectToBean(subject.getPrincipal(), SysUser.class);
		if (sysUser!=null) {
			//移除记住我缓存
			removeRememberMe(request,response);
		}
		subject.logout();
		return json.setSuccess(true);
	}
	/**
	 * 记录登录日志
	 * @param request
	 * @param sysUser
	 */
	private void logLogin(HttpServletRequest request, SysUser sysUser) {
		String[] agent = IpUtil.getOsAndBrowser(request);
		String ip = sysUser.getIp();
		String os = agent[0];
		String browser = agent[1];
		String createBy = sysUser.getPk();
		String createName = sysUser.getName();
		String loginName = sysUser.getLoginName();
		String phone = sysUser.getPhone();
		Date createTime = new Date();
		String country = null;
		String province = null;
		String city = null;
		String area = null;
		String adsl = null;
		if (SystemConfig.LOG_IP_ENABLE&&ip!=null&&!ip.startsWith("192.168")) {
			AddressBean address = IpUtil.getAddress(ip);
			if (address!=null) {
				country = address.getCountry();
				province = address.getRegion();
				city = address.getCity();
				area = address.getArea();
				adsl = address.getIsp();
			}
		}
		LogLoginProxy logLoginProxy = SpringBeanUtil.getBean(LogLoginProxy.class);
		LogLogin data = new LogLogin(createBy,createName,loginName,phone, createTime, ip, country, province, city, area, adsl, "0", browser, os);
		logLoginProxy.insert(request, data);
		
	}
	/**
	 * 利用redis实现记住我功能
	 * @param response
	 * @param token
	 * @param ip
	 * @param rememberMe
	 */
	private void rememberMe(HttpServletResponse response, UsernamePasswordToken token,String ip, boolean rememberMe) {
		if (rememberMe) {
			String un = MD5Util.encryptBlend(String.valueOf(token.getUsername()));
			String pwd = MD5Util.encryptBlend(String.valueOf(token.getPassword()));
			String cacheKey = WebUtils.getCacheKey(un, pwd, ip);
			if (cacheKey!=null) {
				try {
					//将预先生成好的key计入redis中及cookie中,并且设置有效时间30天,访问页面前先判断是否存在该key,存在则直接调用登录方法
					RedisClientSupport redisClientSupport = SpringBeanUtil.getRedisClientSupport();
					String redisKey = RedisKeyConfig.getRememberCacheKey(cacheKey);
					redisClientSupport.putValue(redisKey, un+"#"+pwd);
					redisClientSupport.expire(redisKey, RedisConfig.REMEMBER_REDIS_CACHE_TIME);
					WebUtils.writeCookie(response, AdminCookieConfig.REMEMBER_ME_KEY, cacheKey, RedisConfig.REMEMBER_REDIS_CACHE_TIME);
				} catch (CacheAccessException e) {
					logger.error("rememberMe(HttpServletResponse, UsernamePasswordToken, String, boolean)", e); //$NON-NLS-1$
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 移除记住我功能(用户登出或其它原因)
	 * @param response
	 * @param token
	 * @param ip
	 * @param rememberMe
	 */
	private void removeRememberMe(HttpServletRequest request, HttpServletResponse response) {
			String cacheKey = WebUtils.readCookie(request, AdminCookieConfig.REMEMBER_ME_KEY);
			try {
				if (StringUtils.isNotBlank(cacheKey)) {
					//通过获取到的key删除cookie及redis缓存
					RedisClientSupport redisClientSupport = SpringBeanUtil.getRedisClientSupport();
					String redisKey = RedisKeyConfig.getRememberCacheKey(cacheKey);
					redisClientSupport.delete(redisKey);
					WebUtils.writeCookie(response, AdminCookieConfig.REMEMBER_ME_KEY, null, 0);
				}
			} catch (CacheAccessException e) {
				e.printStackTrace();
			}
	}
	private String checkPasword(String password){
		if (StringUtils.isBlank(password)) {
			return "密码不能为空";
		}
		if (!StringUtil.isSafe(password)) {
			return "密码至少4位,只能使用字母数字和_";
		}
		return null;
	}
	/**
	 * 数据校验及数据填充,如更新时间,更新人等
	 * 
	 * @param data
	 * @param user
	 * @return
	 */ 
	private String checkData(SysUser data,SysUser user) {
		String result = BeanUtil.checkEntity(data, fieldNameMap);
		if (result!=null) {
			return result;
		}
		boolean isPhone = StringUtil.isPhoneNumber(data.getPhone());
		if (!isPhone) {
			return "手机号不合法";
		}
		data.setUpdateTime(new Date());
		if (StringUtils.isNotEmpty(data.getPassword())) {
			data.setPassword(MD5Util.md5(data.getPassword()));
			data.setUpdatePassword(data.getUpdateTime());
		}
		return result;
	}
	/**
	 * 验证返回结果
	 * 
	 * @param json
	 * @return
	 */ 
	private Json checkResult(Json json) {
		if(json == null){
			return ErrorConfig.getSystemErrorJson();
		}
		return json;
	}
	/**
	 * 数据校验及数据填充
	 * 
	 * @param json
	 * @return
	 */ 
	private List<Model> getConditions(SysUser queryInfo,HttpServletRequest request) {
		if(queryInfo == null){
			return new ArrayList<Model>();
		}
		List<Model> conditions = new ArrayList<>();
		if (StringUtils.isNotBlank(queryInfo.getName())) {
			conditions.add(new Model("NAME", "LIKE", StringUtil.appendStringNotNull(queryInfo.getName(), "%","%")));
		}
		if (StringUtils.isNotBlank(queryInfo.getPhone())) {
			conditions.add(new Model("PHONE", "LIKE", StringUtil.appendStringNotNull(queryInfo.getPhone(), "%","%")));
		}
		if (StringUtils.isNotBlank(queryInfo.getSex())) {
			conditions.add(new Model("SEX", "=", queryInfo.getSex()));
		}
		if (StringUtils.isNotBlank(queryInfo.getUpName())) {
			conditions.add(new Model("UP_NAME", "LIKE", StringUtil.appendStringNotNull(queryInfo.getUpName(), "%","%")));
		}
		return conditions;
	}
}