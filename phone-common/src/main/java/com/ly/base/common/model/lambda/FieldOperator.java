package com.ly.base.common.model.lambda;

import java.lang.reflect.Field;

@FunctionalInterface
public interface FieldOperator {
	/**
	 * 适用于普通对象
	 * @param field
	 * @param value
	 * return 当返回false时终止遍历
	 */
	public boolean operator(Field field,Object value);
}
