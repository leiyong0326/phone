// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DateConvertUtils.java

package com.ly.base.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class DateConvertUtils {

	public DateConvertUtils() {
	}

	public static Date parse(String dateString, String dateFormat) {
		return parse(dateString, dateFormat, java.util.Date.class);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" }) 
	public static Date parse(String dateString, String dateFormat, Class targetResultType) {
		DateFormat df;
		try {
			if (StringUtils.isEmpty(dateString))
				return null;
			df = new SimpleDateFormat(dateFormat);
			Date t;
			long time = df.parse(dateString).getTime();
			t = (Date) targetResultType.getConstructor(new Class[] { Long.TYPE })
					.newInstance(new Object[] { Long.valueOf(time) });
			return t;
		} catch (Exception e) {
			String errorInfo = (new StringBuilder("cannot use dateformat:")).append(dateFormat)
					.append(" parse datestring:").append(dateString).toString();
			throw new IllegalArgumentException(errorInfo, e);
		}
	}

	public static String format(Date date, String dateFormat) {
		if (date == null)
			return null;
		else
			return (new SimpleDateFormat(dateFormat)).format(date);
	}
}
