package com.ly.base.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;

import com.alibaba.fastjson.JSON;
import com.ly.base.common.model.Model;
import com.ly.base.common.model.WechatPayModel;

/**
 * MyBatis工具类
 * @author LeiYong
 *
 */
public class MyBatisUtil {
	private static final String splitCharacter = ",";
//	private static final String defaultFilterRegex = ".*([d|D]ate|[t|T]ime).*";
	private static final String EQUALS = "=";
	private static final FieldFilter defaultFilter = new FieldFilter() {
		@Override
		public boolean matches(Field field) {
//			if(field.getName().matches(defaultFilterRegex)){
//				return false;
//			}
			if(Boolean.class.isAssignableFrom(field.getType())){
				return false;
			}
			if(Date.class.isAssignableFrom(field.getType())){
				return false;
			}
			return true;
		}
	};
	/**
	 * 解析String为Model,切割符默认为","
	 * @param texts 如"pk,=,111","age,>,15"
	 * @return 
	 */
	public static List<Model> parseBase(String... texts){
		List<Model> list = new ArrayList<>();
		if (texts!=null&&texts.length>0) {
			for (String text : texts) {
				if (StringUtils.isNotBlank(text)) {
					String[] arr = StringUtil.indexOf(text, splitCharacter, 0);
					String[] arr2 = StringUtil.indexOf(arr[1], splitCharacter, 0);
					list.add(new Model(arr[0],arr2[0], arr2[1]));
				}
			}
		}
		return list;
	}
	/**
	 * 解析对象为Model
	 * 默认过滤date和boolean类型字段
	 * @param obj
	 * @param parseSuper 是否解析父类
	 * @return
	 */
	public static List<Model> parseByObject(Object obj,boolean parseSuper){
		return parseByObject(obj, parseSuper, defaultFilter);
	}
	/**
	 * 解析对象为Model
	 * 自定义filter
	 * @param obj
	 * @param parseSuper 是否解析父类
	 * @return
	 */
	public static List<Model> parseByObject(Object obj,boolean parseSuper,FieldFilter ff){
		List<Model> list = new ArrayList<>();
		if (obj==null) {
			return list;
		}
		//解析Field
		FieldCallback fc = new FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				if (ff != null && !ff.matches(field)) {
					return;
				}
				Model m = parseByField(obj, field);
				if (m!=null) {
					list.add(m);
				}
			}
		};
		if (parseSuper) {
			ReflectionUtil.doWithFields(obj.getClass(),fc);
		}else{
			ReflectionUtil.doWithLocalFields(obj.getClass(),fc);
		}
		return list;
	}
	public static void main(String[] args) {
		WechatPayModel model = new WechatPayModel();
		model.setAppId("123");
		model.setAppKey("456");
		model.setCommodityName("thank");
		System.out.println(JSON.toJSONString(parseByObject(model, true)));
	}
	/**
	 * 将指定对象的field转为Model
	 * @param obj
	 * @param f
	 * @return
	 */
	public static Model parseByField(Object obj, Field f) {
		if (f == null || obj == null) {
			return null;
		}
		if (FieldUtil.isFinalOrStatic(f)) {
			return null;
		}
		Object value = ReflectionUtil.getFieldAndSetAccessible(f, obj);
		if (value==null||value.toString().isEmpty()) {
			return null;
		}
		return new Model(FieldUtil.fieldNameToDbName(f.getName()), EQUALS, value);
	}
	/**
	 * 解析String为Model,默认使用,分割
	 * 例如:"name","in","张三,李四,王五"
	 * @param column 列
	 * @param operator 操作符
	 * @param values 值集
	 * @return
	 */
	public static Model parseList(String column,String operator,String values){
		return parseList(column, operator, values, splitCharacter);
	}
	/**
	 * 解析String为Model
	 * 例如:"name","in","张三#李四#王五","#"
	 * @param column 列
	 * @param operator 操作符
	 * @param values 值集
	 * @param split 切割符,支持正则
	 * @return
	 */
	public static Model parseList(String column,String operator,String values,String split){
		if (StringUtils.isNotBlank(values)) {
			List<String> list = Arrays.asList(values.split(split));
			return new Model(column,operator,list);
		}
		return null;
	}
}
