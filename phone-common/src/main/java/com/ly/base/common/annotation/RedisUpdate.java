package com.ly.base.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RedisUpdate {
	/**
	 * 缓存key,默认为#record?.pk
	 * 如"#obj?.orgPk+#obj?.loginName"
	 * 集合操作如:"#objs?.![column+operate]"
	 * 语法请参考SpringSpelTest.java
	 * 默认为#record?.pk
	 * @return
	 */
	String cacheKey() default "#record?.pk";
	/**
	 * -1表示永不过期
	 * @return
	 */
	long expireTime() default -1;
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
	/**
	 * 更新列名
	 * 格式为{参数名:列名},支持多笔
	 * 例如更新手机号,参数为String p,手机号字段为phone,则:{"p:phone"}
	 * 如参数为对象:则{"p.sysuser.phone:phone"}
	 * @return
	 */
	String[] updateKeys();
}
