package com.ly.base.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import com.ly.base.common.model.lambda.DateOperator;

/**
 * 日期操作类
 * 
 * @author LeiYong
 *
 */
public class DateUtil {

	/** 存放不同的日期模板格式的sdf的Map */
	private static Map<String, DateFormat> sdfMap = new Hashtable<>();
	public static final String YMD = "yyyy-MM-dd";
	public static final String YMD_HM = "yyyy-MM-dd HH:mm";
	public static final String YMD_HMS = "yyyy-MM-dd HH:mm:ss";

	static {
		sdfMap.put(YMD_HM, new SimpleDateFormat(YMD_HM));
		sdfMap.put(YMD_HMS, new SimpleDateFormat(YMD_HMS));
		sdfMap.put(YMD, new SimpleDateFormat(YMD));
		sdfMap.put("yyyy/MM/dd HH:mm", new SimpleDateFormat("yyyy/MM/dd HH:mm"));
		sdfMap.put("yyyy/MM/dd HH:mm:ss", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
		sdfMap.put("yyyy/MM/dd", new SimpleDateFormat("yyyy/MM/dd"));
	}

	/**
	 * 获取DateFormat对象
	 * 
	 * @param format
	 * @return
	 */
	public static DateFormat getDateFormat(String format) {
		if (sdfMap.get(format) == null) {
			DateFormat sf = new SimpleDateFormat(format);
			sdfMap.put(format, sf);
		}
		return sdfMap.get(format);
	}

	/**
	 * 用给定的样式格式化给定的日期（字符串）
	 * 
	 * @param format
	 *            日期样式
	 * @param dateStr
	 *            目标日期字符串
	 * @return
	 * @throws java.text.ParseException
	 */
	public static Date parse(String dateStr, String... format) {
		Date date = null;
		for (int i = 0; i < format.length; i++) {
			try {
				date = getDateFormat(format[i]).parse(dateStr);
				if (date != null) {
					return date;
				}
			} catch (Exception e) {
			}
		}
		return date;
	}

	/**
	 * 将给定的日期格式化为默认的样式:yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateStr
	 * @return
	 * @throws java.text.ParseException
	 */
	public static Date parse(String dateStr) {
		return parse(dateStr, YMD_HM, YMD_HMS, YMD);
	}
	/**
	 * 扩展parse,支持季度qq,半年hy
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static Date parseExtend(String dateStr,String format){
		int mm = 0;
		if(format.indexOf("qq")>=0){
			format = format.replace("qq", "MM");
			mm=3;
		}else if(format.indexOf("hy")>=0){
			format = format.replace("hy", "MM");
			mm=6;
		}
		Date date = parse(dateStr, format);
		if (mm>0) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int month = calendar.get(Calendar.MONTH);
			calendar.set(Calendar.MONTH, month*mm);
			date = calendar.getTime();
		}
		return date;
	}
	/**
	 * 用给定的样式格式化给定的日期（日期格式）
	 * 
	 * @param format
	 *            樣式
	 * @param date
	 *            日期
	 * @return
	 */
	public static String format(String format, Date date) {
		synchronized (LockKeyUtil.getLockKey(format)) {
			return getDateFormat(format).format(date);
		}
	}
	/**
	 * 扩展format,支持季度qq,半年hy
	 * @param format
	 * @param date
	 * @return
	 */
	public static String formatExtend(String format, Date date) {
		int mm = 0;
		if(format.indexOf("qq")>=0){
			format = format.replace("qq", "MM");
			mm=3;
		}else if(format.indexOf("hy")>=0){
			format = format.replace("hy", "MM");
			mm=6;
		}
		if (mm>0) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int month = calendar.get(Calendar.MONTH);
			calendar.set(Calendar.MONTH, month/mm);
			date = calendar.getTime();
		}
		synchronized (LockKeyUtil.getLockKey(format)) {
			return getDateFormat(format).format(date);
		}
	}

	/**
	 * 用给定的样式格式化给定的日期
	 * 
	 * @param c
	 * @param format
	 * @return
	 */
	public static String format(String format, Calendar c) {
		synchronized (LockKeyUtil.getLockKey(format)) {
			return getDateFormat(format).format(c.getTime());
		}
	}

	/**
	 * 格式化给定日期 为"yyyy-MM-dd HH:mm"
	 * 
	 * @return
	 */
	public static String format(Date date) {
		return format(YMD_HM, date);
	}

	/**
	 * 某個月份的天數可能拥有的最大值
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getDayOfMonth(int year, int month) {
		Calendar a = Calendar.getInstance();
		int maxDate = 0;
		if (year != 0 && month != 0) {
			a.clear();
			a.set(1, year);
			a.set(2, month - 1);
			maxDate = a.getActualMaximum(5);
		}
		return maxDate;
	}

	/**
	 * 查询两时间间隔
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static String getInterval(String startTime, String endTime) { // 传入的时间格式必须类似于2012-8-21
																			// 17:53:20这样的格式
		String interval = null;
		SimpleDateFormat sd = new SimpleDateFormat(YMD_HMS);
		ParsePosition pos1 = new ParsePosition(0);
		ParsePosition pos2 = new ParsePosition(0);
		Date d1 = (Date) sd.parse(startTime, pos1);
		Date d2 = (Date) sd.parse(endTime, pos2);

		long time = d2.getTime() - d1.getTime();// 得出的时间间隔是毫秒

		if (time / 1000 < 20 && time / 1000 >= 0) {
			// 如果时间间隔小于20秒则显示“刚刚”
			interval = "刚刚";

		} else if (time / 3600000 < 24 && time / 3600000 > 0) {
			// 如果时间间隔小于24小时则显示多少小时前
			int h = (int) (time / 3600000);// 得出的时间间隔的单位是小时
			interval = h + "小时前";

		} else if (time / 60000 < 60 && time / 60000 > 0) {
			// 如果时间间隔小于60分钟则显示多少分钟前
			int m = (int) ((time % 3600000) / 60000);// 得出的时间间隔的单位是分钟
			interval = m + "分钟前";

		} else if (time / 1000 < 60 && time / 1000 > 0) {
			// 如果时间间隔小于60秒则显示多少秒前
			int se = (int) ((time % 60000) / 1000);
			interval = se + "秒前";

		} else {
			// 大于24小时，则显示天
			int d = (int) (time / (3600000 * 24));// 得出的时间间隔的单位是天
			interval = d + "天前";
		}
		return interval;
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(YMD);
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 获取当前时间到23.59分59秒剩余毫秒数
	 * 
	 * @return
	 */
	public static long getTimeToNight() {
		Calendar calendar = Calendar.getInstance();
		long currentTime = calendar.getTimeInMillis();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		long nightTime = calendar.getTimeInMillis();
		return nightTime - currentTime;
	}

	/**
	 * 获取当前时间到指定时间剩余毫秒数
	 * 
	 * @return
	 */
	public static long getSecondToNight() {
		return (long) Math.ceil(getTimeToNight() / 1000);
	}

	/**
	 * 获取当前时间到指定时间剩余毫秒数
	 * 
	 * @return
	 */
	public static long getTimeToDate(Date date) {
		long nightTime = date.getTime();
		return nightTime - System.currentTimeMillis();
	}

	/**
	 * 获取当前时间到指定时间剩余秒数
	 * 
	 * @return
	 */
	public static long getSecondToDate(Date date) {
		return (long) Math.ceil(getTimeToDate(date) / 1000);
	}

	/**
	 * 获取当前时间日期最后时间
	 * 
	 * @return
	 */
	public static Date dateToNight(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}

	/**
	 * 获取当前日期0点时间
	 * 
	 * @return
	 */
	public static Date dateToMorining(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取给定时间所处星期的第一时间
	 * @param date
	 * @return
	 */
	public static Calendar getWeekFirstDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}
	
	/**
	 * 获取第一时间点 当前年,不可变更
	 * 
	 * @param field
	 *            Calendar.field
	 * @return
	 */
	public static Calendar getFirstDate(int field, Date date) {
		if (field == Calendar.DAY_OF_WEEK) {
			return getWeekFirstDate(date);
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		switch (field) {
		case Calendar.YEAR:
			calendar.set(Calendar.MONTH, 0);
		case Calendar.MONTH:
			calendar.set(Calendar.DAY_OF_MONTH, 1);
		case Calendar.DAY_OF_MONTH:
			calendar.set(Calendar.HOUR_OF_DAY, 0);
		case Calendar.HOUR_OF_DAY:
			calendar.set(Calendar.MINUTE, 0);
		case Calendar.MINUTE:
			calendar.set(Calendar.SECOND, 0);
		case Calendar.SECOND:
			calendar.set(Calendar.MILLISECOND, 0);
		default:
			break;
		}
		return calendar;
	}

	/**
	 * 获取第一时间点 当前年,不可变更
	 * 
	 * @param field
	 *            Calendar.field
	 * @return
	 */
	public static Calendar getLastDate(int field, Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		switch (field) {
		case Calendar.YEAR:
			calendar.set(Calendar.MONTH, 11);
		case Calendar.MONTH:
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DATE));
		case Calendar.DAY_OF_MONTH:
			calendar.set(Calendar.HOUR_OF_DAY, 23);
		case Calendar.HOUR_OF_DAY:
			calendar.set(Calendar.MINUTE, 59);
		case Calendar.MINUTE:
			calendar.set(Calendar.SECOND, 59);
		case Calendar.SECOND:
			calendar.set(Calendar.MILLISECOND, 999);
		default:
			break;
		}
		return calendar;
	}

	/**
	 * 获取次时间点,如次月,次日
	 * 
	 * @param field
	 *            Calendar.field
	 * @param calendar为:getFirstDate()
	 * @return
	 */
	public static Calendar addDate(Date date, int field, int add) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, add);
		// switch (field) {
		// case Calendar.YEAR:
		// case Calendar.MONTH:
		// case Calendar.DAY_OF_MONTH:
		// case Calendar.HOUR_OF_DAY:
		// case Calendar.MINUTE:
		// case Calendar.SECOND:
		// case Calendar.MILLISECOND:
		// calendar.set(field, calendar.get(field)+1);
		// default:
		// break;
		// }
		return calendar;
	}

	/**
	 * 遍历开始日期到结束日期的所有中间时段
	 * 
	 * @param startDate
	 * @param endDate
	 * @param field
	 * @return
	 */
	public static boolean foreach(Date startDate, Date endDate, int field, int interval, DateOperator operator) {
		if (startDate == null || endDate == null) {
			return true;
		}
		while (startDate.getTime() <= endDate.getTime()) {
			boolean res = operator.operator(startDate);
			if (!res) {
				return res;
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			calendar.add(field, interval);
			startDate = calendar.getTime();
		}
		return true;
	}
}
