package com.ly.base.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ly.base.common.em.ext.LogEnum;
import com.ly.base.common.em.ext.LogOperateEnum;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Logger {
	/**
	 * 操作类型 0-查询 1-新增 2-修改 3-删除
	 * @return
	 */
	LogOperateEnum type();
	/**
	 * 所属模块
	 * @return
	 */
	LogEnum model();
	/**
	 * 业务主键
	 * @return
	 */
	String title();
}
