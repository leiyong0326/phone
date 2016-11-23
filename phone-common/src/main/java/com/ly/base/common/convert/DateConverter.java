package com.ly.base.common.convert;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.expression.ParseException;

import com.ly.base.common.util.DateUtil;

public class DateConverter implements Converter<String, Date> {
	@Override
	public Date convert(String source) {
		try {
			return DateUtil.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}