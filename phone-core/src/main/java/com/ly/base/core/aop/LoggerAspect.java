package com.ly.base.core.aop;

import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ly.base.common.annotation.Logger;
import com.ly.base.common.system.SystemConfig;
import com.ly.base.common.system.properties.AdminPropertiesConfig;
import com.ly.base.common.util.PropertiesCacheUtil;
import com.ly.base.common.util.StringUtil;
import com.ly.base.core.model.log.LogOperater;
import com.ly.base.core.model.sys.SysUser;
import com.ly.base.core.provide.log.LogOperaterService;
import com.ly.base.core.util.AopUtil;
import com.ly.base.core.util.WebUtils;

/**
 * 操作日志
 * @author LeiYong
 *
 */
@Aspect
@Component
public class LoggerAspect {
	/**
	 * Logger for this class
	 */
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(LoggerAspect.class);
	
	@Autowired
	private LogOperaterService logOperaterService;
	@Pointcut("@annotation(com.ly.base.common.annotation.Logger)")  
    private void loggerMethod(){}//定义一个切入点  
	/**
	 * 记录操作日志
	 * 
	 * @param jp
	 * @return
	 * @throws Throwable
	 */
	@Around("loggerMethod()")
	public Object logger(ProceedingJoinPoint jp) throws Throwable {
		
		Object result = null;
		Boolean logEnable = SystemConfig.LOG_ENABLE;
		long curTime = System.currentTimeMillis();
		result = AopUtil.executeJoinPointMethod(jp, jp.getArgs());
		log.debug(String.format("#%s->%s执行耗时%s", jp.getThis().getClass().getName(),AopUtil.getMethod(jp).getName(),curTime-System.currentTimeMillis()));
		if (logEnable&&result!=null) {
			LogOperater lo = null;
			SysUser su = getLoginSysUser();
			if (su!=null) {
				lo = getLogBySysUser(su, jp);
			}
			if (lo!=null) {
				logOperaterService.insertSelective(lo);
			}
		}
		return result;
	}

	/**
	 * 记录系统用户操作日志
	 * @param user
	 * @param jp
	 * @return
	 */
	private LogOperater getLogBySysUser(SysUser user,ProceedingJoinPoint jp){
		LogOperater logOperater = getLogger(jp);
		if (logOperater!=null) {
			logOperater.setCreateBy(user.getPk());
			logOperater.setFrom("0");
		}
		return logOperater;
	}
//	/**
//	 * 记录微信用户操作日志
//	 * @param user
//	 * @param jp
//	 * @return
//	 */
//	private LogOperater getLogByUsrUser(UsrUser user,ProceedingJoinPoint jp){
//		LogOperater logOperater = getLogger(jp);
//		if (logOperater!=null) {
//			logOperater.setCreateBy(user.getPk());
//			logOperater.setFrom("1");
////			logOperater.setOrgPk(user.getOrgPk());
//		}
//		return logOperater;
//	}

	private LogOperater getLogger(ProceedingJoinPoint jp){
		//获取切入点方法及参数
		Method method = AopUtil.getMethod(jp);
		Logger t = AopUtil.getMethodAnnotation(method, Logger.class);
		try {
			Boolean flag = PropertiesCacheUtil.loadProjectProperties(AdminPropertiesConfig.LOGGER_CONFIG).getBoolean(t.type().toString());
			//未配置或true都记录日志,仅为false时不记录
			if (flag!=null&&!flag) {
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] paramNames = AopUtil.getMethodParamNames(method);
		String pk = AopUtil.parseKeyByParam(t.title(),paramNames, jp.getArgs());
		//主键,机构pk,创建人,创建时间,操作类型,模块,处理方法,主键,操作内容
		//自增长主键
		LogOperater logOperater = new LogOperater();
		logOperater.setCreateTime(new Date());
		logOperater.setType(t.type().getValue());
		logOperater.setModel(t.model().getValue());
		logOperater.setFunc(t.model().getDiscription());
		logOperater.setMethod(method.getName());
		if (StringUtils.isNotBlank(pk)) {
			int index = pk.indexOf(",");
			if (index<0) {
				logOperater.setTitle(pk);
			}else{
				logOperater.setTitle(StringUtil.subString(pk, 0, index));
				int count = StringUtils.countMatches(pk, ",");
				String content = SystemConfig.LOG_CONTENT_LENGTH>pk.length()?pk:StringUtil.subString(pk, 0, SystemConfig.LOG_CONTENT_LENGTH)+"...";
				logOperater.setContent(count+"笔:"+content);
			}
		}
		return logOperater;
	}
	/**
	 * 获取系统登录用户
	 * @return
	 */
	private SysUser getLoginSysUser(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();    
		return WebUtils.getLoginUser(request);
	}
	/**
	 * 获取微信登录用户(未实现)
	 * @return
	 */
//	private UsrUserSlave getLoginUsrUser(){
//		return null;
//	}
	
}