package com.ly.base.common.model.lambda;

@FunctionalInterface
public interface BeanOperator<T> {
	/**
	 * 适用于普通对象
	 * @param t
	 * return 当返回false时终止遍历
	 */
	public boolean operator(T t);
}
