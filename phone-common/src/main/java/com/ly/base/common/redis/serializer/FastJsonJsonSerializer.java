package com.ly.base.common.redis.serializer;

import java.nio.charset.Charset;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class FastJsonJsonSerializer<T> implements RedisSerializer<T> {

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	private final static byte[] EMPTY_ARRAY = new byte[0];

	private final JavaType javaType;

	/**
	 * Creates a new {@link Jackson2JsonRedisSerializer} for the given target {@link Class}.
	 * 
	 * @param type
	 */
	public FastJsonJsonSerializer(Class<T> type) {
		this.javaType = getJavaType(type);
	}

	/**
	 * Creates a new {@link Jackson2JsonRedisSerializer} for the given target {@link JavaType}.
	 * 
	 * @param javaType
	 */
	public FastJsonJsonSerializer(JavaType javaType) {
		this.javaType = javaType;
	}

	public T deserialize(byte[] bytes) throws SerializationException {
		if (ArrayUtils.isEmpty(bytes)) {
			return null;
		}
		try {
			return JSON.parseObject(bytes, javaType);
		} catch (Exception ex) {
			throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
		}
	}

	public byte[] serialize(Object t) throws SerializationException {

		if (t == null) {
			return EMPTY_ARRAY;
		}
		try {
			return JSON.toJSONBytes(t);
		} catch (Exception ex) {
			throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
		}
	}

	/**
	 * Returns the Jackson {@link JavaType} for the specific class.
	 * <p>
	 * Default implementation returns {@link TypeFactory#constructType(java.lang.reflect.Type)}, but this can be
	 * overridden in subclasses, to allow for custom generic collection handling. For instance:
	 * 
	 * <pre class="code">
	 * protected JavaType getJavaType(Class&lt;?&gt; clazz) {
	 * 	if (List.class.isAssignableFrom(clazz)) {
	 * 		return TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, MyBean.class);
	 * 	} else {
	 * 		return super.getJavaType(clazz);
	 * 	}
	 * }
	 * </pre>
	 * 
	 * @param clazz the class to return the java type for
	 * @return the java type
	 */
	protected JavaType getJavaType(Class<?> clazz) {
		return TypeFactory.defaultInstance().constructType(clazz);
	}


}
