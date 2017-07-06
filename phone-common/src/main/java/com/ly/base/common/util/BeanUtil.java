package com.ly.base.common.util;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * 对象转换工具类
 * @author TangFangguo
 * @date 2016年8月11日
 * @Description
 */
public class BeanUtil {
	/**
	 * 验证对象属性是否为空,不支持子属性
	 * @param obj
	 * @param notNullFields 属性名集合,不支持子属性
	 * @return
	 */
	public static String checkEntity(final Object obj,String... notNullFields){
		if (obj==null) {
			return "信息异常";
		}else {
			if (ArrayUtils.isNotEmpty(notNullFields)) {
				Class<?> clazz = obj.getClass();
				for (int i = 0; i < notNullFields.length; i++) {
					String f = notNullFields[i];
					if (StringUtils.isNotBlank(f)) {
						Field field = ReflectionUtil.findField(clazz, f);
						Object value = ReflectionUtil.getFieldAndSetAccessible(field, obj);
						boolean res = checkFieldNotNull(value);
						if (!res) {
							return f+"不能为空";
						}
					}
				}
			}
		}
		return null;
	}
	/**
	 * 验证对象属性是否为空,不支持子属性
	 * @param obj
	 * @param notNullFieldErrorMap key:属性名,value:属性中文名
	 * @return
	 */
	public static String checkEntity(final Object obj,Map<String, String> notNullFieldErrorMap){
		if (obj==null) {
			return "信息异常";
		}else {
			if (notNullFieldErrorMap != null) {
				Set<String> keys = notNullFieldErrorMap.keySet();
				Class<?> clazz = obj.getClass();
				for (String key : keys) {
					if (StringUtils.isNotBlank(key)) {
						Field field = ReflectionUtil.findField(clazz, key);
						if (field == null) {
							return "信息异常";
						}
						Object value = ReflectionUtil.getFieldAndSetAccessible(field, obj);
						boolean res = checkFieldNotNull(value);
						if (!res) {
							return notNullFieldErrorMap.get(key) + "不能为空";
						}
					}
				}
			}
		}
		return null;
	}
	/**
	 * 验证对象属性是否为空,为空则返回spel表达式
	 * @param obj 被验证的对象
	 * @param notNullFields spel表达式集合
	 * @return 如全部不为空,返回null
	 * "#pk","#sysUsers?.![!this]"
	 */
	public static String checkEntityBySpel(final Object obj,String... notNullFields){
		if (obj==null) {
			return "信息异常";
		}else {
			ExpressionParser parser = new SpelExpressionParser();
	        //SPEL上下文
	        StandardEvaluationContext context = new StandardEvaluationContext();
	        context.setVariable("obj", obj);
			if (ArrayUtils.isNotEmpty(notNullFields)) {
				for (int i = 0; i < notNullFields.length; i++) {
					String f = notNullFields[i];
					if (StringUtils.isNotBlank(f)) {
						Object value = parser.parseExpression(f).getValue();
						boolean res = checkFieldNotNull(value);
						if (!res) {
							return f;
						}
					}
				}
			}
		}
		return null;
	}
	/**
	 * 支持子属性,用.分割
	 * @param fieldName
	 * @param obj
	 * @param parser 
	 * @param context 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static boolean checkFieldNotNull(Object value) {
		if (value == null) {
			return false;
		}
		if (value instanceof String) {
			return value.toString().length() > 0;
		}
		if (value instanceof Number || value instanceof Date || value instanceof Boolean) {
			return true;
		}
		if (value instanceof Map) {
			return !((Map) value).isEmpty();
		}
		if (value instanceof List) {
			return !((List) value).isEmpty();
		}
		if (value instanceof Set) {
			return !((List) value).isEmpty();
		}
		if (value instanceof Collection) {
			return !((Collection) value).isEmpty();
		}
		Class<?> clazz = value.getClass();
		if (clazz.isArray()) {
			return ((Object[]) value).length > 0;
		}
		return true;
	}
}
