package com.ly.base.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RedisEvict {
	/**
	 * 缓存key,默认为#record?.pk
	 * 如"#obj?.orgPk+#obj?.loginName"
	 * 集合操作如:"#objs?.![column+operate]"
	 * 数组操作:"#objs?.![#this]"
	 * 语法请参考SpringSpelTest.java
	 * @return
	 */
	String cacheKey() default "#record?.pk";
	/**
	 * 是否多笔操作
	 * @return
	 */
	boolean batch() default false;
	/**
	 * 类型
	 * @return
	 */
	String clazz();
}
