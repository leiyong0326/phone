package com.ly.base.common.model;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Properties;

import com.alibaba.fastjson.util.TypeUtils;
/**
 * Properties缓存工具类
 * @author Ray
 *
 */
public class PropertiesModel extends Hashtable<String, String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -475301370338185257L;
	private Long visionDate;
	private Properties properties;
	private String charsetFrom;
	private String charsetTo;

	public PropertiesModel(Long vision, Properties properties,
			String charsetFrom, String charsetTo) {
		super();
		this.visionDate = vision;
		this.properties = properties;
		this.charsetFrom = charsetFrom;
		this.charsetTo = charsetTo;
	}

	/**
	 * 获取一个property属性
	 * 
	 * @param properties
	 * @param key
	 * @return key.value
	 * @throws UnsupportedEncodingException
	 */
	private void getProperty(String key) throws UnsupportedEncodingException {
		String value = properties.getProperty(key);
		if (value == null) {
		}else if (charsetFrom == null||charsetFrom.isEmpty() || charsetTo == null||charsetTo.isEmpty()) {
			put(key, new String(value));
		} else {
			put(key, new String(value.getBytes(charsetFrom), charsetTo));
		}
	}
	@Override
	public synchronized String put(String key, String value) {
		properties.put(key, value);
		String returnString = super.put(key, value);
		return returnString;
	}
	/**
	 * 获取一个property属性
	 * @param key
	 * @return value
	 */
	public String get(Object key){
		if (super.get(key.toString()) == null) {
			try {
				getProperty(key.toString());
			} catch (UnsupportedEncodingException e) {
			}
		}
		return super.get(key.toString());
	}
	public Integer getInteger(Object key){
		return TypeUtils.castToInt(get(key));
	}
	public Double getDouble(Object key){
		return TypeUtils.castToDouble(get(key));
	}
	public Boolean getBoolean(Object key){
		return TypeUtils.castToBoolean(get(key));
	}
	/**
	 * 验证版本号是否一致
	 * @param vision
	 * @return  版本号是否匹配
	 */
	public boolean equalsVision(Long vision) {
		if (this.visionDate==null) {
			boolean returnboolean = this.visionDate == vision;
			return returnboolean;
		}
		boolean returnboolean = this.visionDate.equals(vision);
		return returnboolean;
	}

	/**
	 * 设置版本号
	 * @param vision
	 */
	public void setVision(Long vision) {
		this.visionDate = vision;
	}

	/**
	 * 获取properties对象
	 * @return Properties
	 */
	public Properties getProperties() {
		return properties;
	}
	
}