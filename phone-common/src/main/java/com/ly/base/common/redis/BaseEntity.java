package com.ly.base.common.redis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.ly.base.common.util.DateConvertUtils;

public class BaseEntity implements java.io.Serializable {

	public static final String CACHE_APP_NAME="ucenter-";
	
	private static final long serialVersionUID = -7200095849148417467L;

	public static final String ENTITY_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	protected static final String TIME_FORMAT = "HH:mm:ss";
	
	protected static final String DATE_FORMAT = "yyyy-MM-dd";
	
	protected static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	protected static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.S";
	
	public static final String TIMESTAMP_FORMAT_WEBSERVICE = "yyyyMMddHHmmssSSS";
	
	public static final String THE_DAY_VALUE = "yyyyMMdd";
	
	public static final String UNIX_TIME_FORMAT="yyyyMMddHHmmss";


	public static String date2String(java.util.Date date, String dateFormat) {
		return DateConvertUtils.format(date, dateFormat);
	}

	public static <T extends java.util.Date> Date string2Date(String dateString,
			String dateFormat, Class<T> targetResultType) {
		return DateConvertUtils.parse(dateString, dateFormat, targetResultType);
	}
	
	public static String getWsTime(){
		return date2String(new Date(), TIMESTAMP_FORMAT_WEBSERVICE);
	}
	
	public static int getTheDate(){
		return Integer.parseInt(date2String(new Date(), THE_DAY_VALUE));
	}
	
	public static String getTheDateString(){
		return date2String(new Date(), THE_DAY_VALUE);
	}
	
	public static String getEntityTime(){
		return date2String(new Date(), DATE_TIME_FORMAT);
	}
	
	/**
	 * 获得当前时间,timestamp
	 * 
	 * @return
	 */
	public static java.sql.Timestamp getTimestamp() {
		return new java.sql.Timestamp(System.currentTimeMillis());
	}
	
	public static long getLongTime(){
		return new Date().getTime();
	}
	
	
	public static String changeToDate(long mills){
	        Date d = new Date((long)mills*1000);
	        SimpleDateFormat sdf= new SimpleDateFormat(DATE_TIME_FORMAT);
	        String date = sdf.format(d);
	        return date;
	    }
	public static Date returnToDate(long mills){
		return new Date((long)mills*1000);
	}
	public static Integer getUnixTime(){
		return (int) (System.currentTimeMillis()/1000);
	}
	/**
	 * sdk端时间yyyyMMddHHmmsss转换为unixTime
	 * @param time
	 * @return
	 */
	public static Integer getDateFormtForSDK(String time) throws Exception{
		try{
		 SimpleDateFormat sdf= new SimpleDateFormat(UNIX_TIME_FORMAT);
		 return (int)(sdf.parse(time).getTime()/1000);
		}catch(Exception e){
			throw e;
		}
	}
	public static long timeStrToUnixTimestamp(String timeStr){
		try {
			return new java.text.SimpleDateFormat (DATE_TIME_FORMAT).parse(timeStr).getTime()/1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public static long getUnixTimeByDate(Date date){
		return date.getTime()/1000;
	}
	
	/****
	 * uinx时间戳转换为标准日期格式
	 * 格式为 yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String fromUinxTime(Integer date) {
		if (date == null || date.intValue() == 0) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
		try {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(date * 1000L);
			return sdf.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static Integer strToUnixTime(String timeStr,String format){
		if(StringUtils.isEmpty(timeStr)){
			return null;
		}
		try {
			return (int) (new java.text.SimpleDateFormat (format).parse(timeStr).getTime()/1000);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
}
