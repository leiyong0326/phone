package com.ly.base.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class FieldUtil {

	public static String fieldNameToDbName(String column){
		if (StringUtils.isEmpty(column)) {
			return column;
		}
		return column.replaceAll("([A-Z])", "_$1").toUpperCase();
	}

	public static boolean isFinalOrStatic(Field field){
		int modifier = field.getModifiers();
		return Modifier.isFinal(modifier) || Modifier.isStatic(modifier);
	}
	public static String dbNameToFieldName(String column){
		if (StringUtils.isEmpty(column)) {
			return column;
		}
		Pattern pattern = Pattern.compile("_([a-z])");
		Matcher matcher = pattern.matcher(column.toLowerCase());
		StringBuffer sb = new StringBuffer();  
		while (matcher.find()) {
		    String g = matcher.group(1);  
		    matcher.appendReplacement(sb, g.toUpperCase());  
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	public static void main(String[] args) {
		System.out.println(dbNameToFieldName("TEST_DB_FIELD"));
		System.out.println(fieldNameToDbName("testDbField"));
	}
}
