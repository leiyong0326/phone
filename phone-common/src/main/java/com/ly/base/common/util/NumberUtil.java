package com.ly.base.common.util;

import java.math.BigDecimal;

/**
 * 数字工具类
 * 
 * @author LeiYong
 *
 */
public class NumberUtil {
	/**
	 * 获取一个不大于max的整数
	 * 
	 * @param max
	 * @return
	 */
	public static long random(long max) {
		return (long) (Math.random() * max);
	}

	public static int random(int max) {
		return (int) (Math.random() * max);
	}

	/**
	 * 格式化金额 两位小数
	 * 
	 * @param money
	 * @param scale
	 * @return
	 */
	public static BigDecimal round(BigDecimal money, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
		}
		BigDecimal one = new BigDecimal("1");
		return money.divide(one, scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 
	 * @param number
	 *            被转换的String
	 * @param digits
	 *            小数位数
	 * @param fullZero
	 *            是否在小数位补0
	 * @return
	 */
	public static String decimal(String number, int digits, boolean fullZero) {
		int index = number.indexOf(".");
		int endWith = 0;
		String value = number;
		if (index > 0) {
			if (digits == 0) {
				endWith = index;
			} else {
				endWith = index + 1 + digits;
			}
			if (endWith != value.length()) {
				value = StringUtil.subString(number, 0, endWith);
			}
			if (fullZero) {
				value = StringUtil.fullStringAfter(value, '0', endWith);
			}
		} else if (fullZero) {
			if (digits > 0) {
				value = StringUtil.appendStringNotNull(".", value, StringUtil.getNString('0', digits));
			}
		}
		return value;
	}

	public static void main(String[] args) {
		// System.out.println(decimal("5", 2, true));
		// System.out.println(decimal("5.1", 2, true));
		// System.out.println(decimal("5.0", 2, true));
		// System.out.println(decimal("5.01111", 2, true));
		// System.out.println(decimal("5.22112", 2, false));
		// System.out.println(decimal("5", 2, false));
		// System.out.println(decimal("5.1", 2, false));
		System.out.println(decimal("5", 0, true));
		System.out.println(decimal("5.1", 0, true));
		System.out.println(decimal("5.0", 0, true));
		System.out.println(decimal("5.01111", 0, true));
		System.out.println(decimal("5.22112", 0, false));
		System.out.println(decimal("5", 0, false));
		System.out.println(decimal("5.1", 0, false));
	}
}
