package com.ly.base.common.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 反射工具类
 * @author LeiYong
 *
 */
public class ReflectionUtil extends org.springframework.util.ReflectionUtils{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);

	/**
	 * 获取类的实体,通过调用getInstance
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(Class<T> clazz){
		Method m = findMethod(clazz, "getInstance");
		if (m!=null) {
			try {
				T returnT = (T) m.invoke(null);
				return returnT;
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				logger.error("getInstance(Class<T>) - exception ignored", e); //$NON-NLS-1$
			}
		}
		return null;
	}
	/**
	 * 通过字符串获取Class
	 * @param clazz
	 * @return
	 */
	public static Class<?> getClass(String clazz){
		try {
			return Class.forName(clazz);
		} catch (ClassNotFoundException e) {
			logger.error("getClass(String)", e); //$NON-NLS-1$
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取类中所有元素
	 * @param cls
	 * @return
	 */
	public static String getFields(Class<?> cls){
		Field[] fields = cls.getDeclaredFields();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < fields.length; ) {
			sb.append(fields[i].getName());
			if (++i<fields.length) {
				sb.append(",");
			}
		}
		return sb.toString();
	}
	/**
	 * 获取类中所有元素
	 * @param cls
	 * @return
	 */
	public static String getFieldsAndSupper(Class<?> cls){
		Field[] fields = cls.getDeclaredFields();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < fields.length; ) {
			sb.append(fields[i].getName());
			if (++i<fields.length) {
				sb.append(",");
			}
		}
		return sb.toString();
	}
	/**
	 * 执行方法
	 * @param obj
	 * @param MethodName
	 * @param force 强制执行,即使方法为私有
	 * @param params
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Object executeMethod(Object obj,String MethodName,boolean force,Class<?>... params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		Method method = obj.getClass().getMethod(MethodName,params);
		if (force&&!method.isAccessible()) {
			method.setAccessible(true);
		}
		return method.invoke(obj);
	}
	/**
	 * 获取泛型类型
	 * @param cls
	 * @param index
	 * @return
	 */
	public static Class<?> getGenericSuperclass(Class<?> cls){  
		ParameterizedType parameterizedType = (ParameterizedType)cls.getGenericSuperclass(); 
		return (Class<?>)(parameterizedType.getActualTypeArguments()[0]); 
    }
	
	/**
	 * 将类转换为指定对象,如果无法转换则返回null
	 * @param obj
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convertObjectToBean(Object obj,Class<T> clazz){
		if (obj!=null&&clazz.isInstance(obj)) {
			return (T) obj;
		}
		return null;
	}
	public static Object getFieldAndSetAccessible(Field field, Object target){
		if (!field.isAccessible()) {
			field.setAccessible(true);
		}
		return getField(field, target);
	}
	public static PropertyDescriptor getPropertyDescriptor(String field, Object target) throws NoSuchFieldException, SecurityException, IntrospectionException{
		Class<?> cls = target.getClass();
		return new PropertyDescriptor(field, cls);
	}
}
