package com.ly.base.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.ly.base.common.filter.util.HTMLFilterStrategy;
import com.ly.base.common.model.fastjson.BigDecimalCustomSerializer;
import com.ly.base.common.model.fastjson.DoubleCustomSerializer;

public class JsonUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

	private static SerializeConfig serializeConfig = SerializeConfig.getGlobalInstance();
//	private static ParserConfig parserConfig = ParserConfig.getGlobalInstance();
	private static BigDecimalCustomSerializer bigDecimalCustomSerializer;
	private static DoubleCustomSerializer doubleCustomSerializer;
	
	static {
		initSerializer();
		initParser();
	}
	
	private static void initSerializer(){
		//由于要自定义属性,故而此处实例化
		bigDecimalCustomSerializer = new BigDecimalCustomSerializer(2,true);
		doubleCustomSerializer = new DoubleCustomSerializer(2, true);
//		mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
		serializeConfig.put(BigDecimal.class, bigDecimalCustomSerializer);
		serializeConfig.put(Double.class, doubleCustomSerializer);
//		serializeConfig.put(Page.class, PageSerializer.instance);
	
	}
	private static void initParser(){
		//由于要自定义属性,故而此处实例化
//		IdentityHashMap<Type, ObjectDeserializer> derializers =  parserConfig.getDerializers();
//		derializers.put(Page.class, PageDeserializer.instance);
	}
	public static String toJsonString(Object obj){
		if (logger.isDebugEnabled()) {
			logger.debug("toJsonString(Object) - start"); //$NON-NLS-1$
		}
		String json = JSON.toJSONString(obj,serializeConfig);
		if (logger.isDebugEnabled()) {
			logger.debug("#JSON:"+json);
			logger.debug("toJsonString(Object) - end"); //$NON-NLS-1$
		}
		return json;
	}
}
