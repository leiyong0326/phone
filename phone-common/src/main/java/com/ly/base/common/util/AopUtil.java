package com.ly.base.common.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class AopUtil {
	private static ExpressionParser parser;
	/**
	 * 获取被拦截方法对象
	 * 
	 * MethodSignature.getMethod() 获取的是顶层接口或者父类的方法对象 而缓存的注解在实现类的方法上
	 * 所以应该使用反射获取当前对象的方法对象
	 */
	public static Method getMethod(ProceedingJoinPoint pjp) {
		// 获取参数的类型
		Object[] args = pjp.getArgs();
		Class<?>[] parameterTypes = ((MethodSignature)pjp.getSignature()).getMethod().getParameterTypes();
//		Class[] argTypes = new Class[pjp.getArgs().length];
//		for (int i = 0; i < args.length; i++) {
//			if (args[i]!=null) {
//				argTypes[i] = args[i].getClass();
//			}
//		}
		Method method = null;
		try {
			method = pjp.getTarget().getClass().getMethod(pjp.getSignature().getName(), parameterTypes);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return method;
	}
	/**
	 * 通过spring spel解析参数获取redis缓存key
	 * 
	 * @param keys
	 *            缓存keys
	 * @param paramNames
	 *            参数名
	 * @param args
	 *            参数列表
	 * @return
	 */
	public static String parseKeyByParam(String keys, String[] paramNames, Object[] args) {
		if (StringUtils.isBlank(keys)) {
			return "";
		}
		ExpressionParser parser = getParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		// 把方法参数放入SPEL上下文中
		for (int i = 0; i < paramNames.length; i++) {
			context.setVariable(paramNames[i], args[i]);
		}
		// 获取参数key
		StringBuffer sb = new StringBuffer();
		// for (int i = 0; i < keys.length; i++) {
		sb.append(parser.parseExpression(keys).getValue(context, String.class));
		// }
		return sb.toString();
	}

	/**
	 * 获取参数名列表
	 * 
	 * @param method
	 * @return
	 */
	public static String[] getMethodParamNames(Method method) {
		// 获取被拦截方法参数名列表(使用Spring支持类库)
		LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
		String[] paraNameArr = u.getParameterNames(method);
		return paraNameArr;
	}

	/**
	 * 获取缓存主键
	 * @param jp
	 * @param clazz 
	 * @return
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
//	public static <T extends Annotation> String[] getCachedKey(T t,String[] paramNames, Class<T> clazz) throws NoSuchFieldException, SecurityException {
////		Method method = getMethod(jp);
////		T t = getMethodAnnotation(method, clazz);
//		// String fieldKey = null;
//		// if (cache.isParam()) {
//		String fieldKey = parseKeyByParam(ReflectionUtil.getField(t.getClass().getField("cachekey"), t).toString(), paramNames, jp.getArgs());
//		// }
//		return fieldKey.split(",");
//	}
	
	/**
	 * 获取方法注解
	 * @param method
	 * @param clazz 注解类型
	 * @return
	 */
	public static <T extends Annotation> T getMethodAnnotation(Method method,Class<T> clazz){
		T t = method.getAnnotation(clazz);
		return t;
	}
	/**
	 * 获取redis缓存key通过集合参数
	 * 
	 * @param keys
	 *            缓存keys
	 * @param params
	 *            参数名
	 * @param returnObj
	 *            参数列表
	 * @return
	 */
	private String parseKeyByReturn(String keys, Object returnObj) {
		ExpressionParser parser = getParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		// 把方法参数放入SPEL上下文中
		context.setVariable("obj", returnObj);
		// 获取参数key
		StringBuffer sb = new StringBuffer();
		sb.append(parser.parseExpression(keys).getValue(context, String.class));
		return sb.toString();
	}
	/**
	 * 使用SPEL进行key的解析
	 * 
	 * @return
	 */
	public synchronized static ExpressionParser getParser() {
		if (parser == null) {
			parser = new SpelExpressionParser();
		}
		return parser;
	}
	/**
	 * 执行切入点方法
	 * @param jp
	 * @param params 方法参数
	 * @return
	 */
	public static Object executeJoinPointMethod(ProceedingJoinPoint jp,Object[] params){
		Object obj = null;
		try {
			if (params==null||params.length==0) {
				obj = jp.proceed();
			}else{
				obj = jp.proceed(params);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return obj;
	}
}
