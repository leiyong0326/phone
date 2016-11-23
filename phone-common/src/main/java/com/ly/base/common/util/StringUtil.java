package com.ly.base.common.util;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串处理工具类
 * 
 * @author Leiyong
 * 
 */
public class StringUtil {

	/**
	 * 功能描述：判断是否为整数
	 * 
	 * @param str
	 *            传入的字符串
	 * @return 是整数返回true,否则返回false
	 */
	public static boolean isInteger(String str) {
		return str.matches("^[+-]?\\d+$");
	}

	/**
	 * 判断是否为浮点数，包括double和float
	 * 
	 * @param str
	 *            传入的字符串
	 * @return 是浮点数返回true,否则返回false
	 */
	public static boolean isDouble(String str) {
		return str.matches("^[+-]?\\d+(\\.\\d+$)?");
	}

	/**
	 * 字母数字下划线,并以字母开头
	 */
	public static boolean isSafe(String str) {
		if (str == null || str.length() == 0) {
			return false;
		}
		return str.matches("[\\w]{4,}");
	}

	/**
	 * 功能描述：判断输入的字符串是否符合Email样式.
	 * 
	 * @param str
	 *            传入的字符串
	 * @return 是Email样式返回true,否则返回false
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.length() < 1 || email.length() > 256) {
			return false;
		}
		return email.matches("^[-+.\\w]+@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
	}

	/**
	 * 功能描述：判断输入的字符串是否为纯汉字
	 * 
	 * @param str
	 *            传入的字符窜
	 * @return 如果是纯汉字返回true,否则返回false
	 */
	public static boolean isChinese(String str) {
		return str.matches("^[\u0391-\uFFE5]+$");
	}

	/**
	 * 功能描述：判断是不是合法的手机号码
	 * 
	 * @param pn
	 *            手机号
	 * @return boolean
	 */
	public static boolean isPhoneNumber(String pn) {
		try {
			return pn.matches("^1[\\d]{10}$");

		} catch (RuntimeException e) {
			return false;
		}
	}

	/**
	 * 获取UUID的String值
	 * 
	 * @return UUID
	 */
	public synchronized static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 获取count位随机十六进制字符串
	 * 
	 * @return
	 */
	public static String getNonceStr(int count) {
		return getHexString(NumberUtil.random((int) Math.pow(16, count)), count);
	}

	/**
	 * 获取十六进制数据
	 * 
	 * @param number
	 *            被转化的数据
	 * @param length
	 *            需转化为的长度,为0则不强制长度
	 * @return
	 */
	public static String getHexString(int number, int length) {
		String value = Integer.toHexString(number);
		return fullStringBefore(value, '0', length);
	}

	/**
	 * 在字符串前填充字符
	 * 
	 * @param src
	 *            原字符
	 * @param full
	 *            填充的字符
	 * @param length
	 *            总长度,为0则不强制长度
	 * @return 如("ff",'0',5),返回000ff,如("fffff",'0',3),返回fff
	 */
	public static String fullStringBefore(String src,char full,int length){
		if (length>0) {
			if (src.length()>length) {
				return src.substring(src.length()-length);
			}else if(src.length()<length){
				return appendStringNotNull(null, getNString(full, length-src.length()),src);
			}
		}
		return src;
	}

	/**
	 * 在字符串后填充字符
	 * @param src 原字符
	 * @param full 填充的字符
	 * @param length 总长度,为0则不强制长度
	 * @return 如("ff",'0',5),返回ff000,如("fffff",'0',3),返回fff
	 */
	public static String fullStringAfter(String src,char full,int length){
		if (length>0) {
			if (src.length()>length) {
				return src.substring(src.length()-length);
			}else if(src.length()<length){
				return appendStringNotNull(null,src, getNString(full, length-src.length()));
			}
		}
		return src;
	}
	/**
	 * 获取N个相同的String组合的字符串
	 * 
	 * @param src
	 * @param count
	 * @return
	 */
	public static String getNString(char src, int count) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < count; i++) {
			sb.append(src);
		}
		return sb.toString();
	}
	/**
	 * 获取N个相同的String组合的字符串
	 * 
	 * @param src
	 * @param count
	 * @return
	 */
	public static String getNString(String src, int count) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < count; i++) {
			sb.append(src);
		}
		return sb.toString();
	}

	/**
	 * 寻找指定字符串并截取
	 * 
	 * @param str
	 * @param tar
	 * @param startIndex
	 *            开始寻找下标
	 * @return 返回{tar前的字符串,tar后的字符串},如str=offott,tar=o,startIndex=1,{"ff","tt"},
	 *         若未找到则返回{"",str}
	 */
	public static String[] indexOf(String str, String tar, int startIndex) {
		if (str != null) {
			int index = str.indexOf(tar, startIndex);
			if (index > 0) {
				return new String[] { str.substring(startIndex, index), str.substring(index + tar.length()) };
			}
		}
		return new String[] { "", str };
	}

	/**
	 * 判断字符串是否存在于content中
	 * 
	 * @param str
	 * @param content
	 * @return
	 */
	public static boolean inStrings(String str, String content) {
		StringBuffer sb = new StringBuffer();
		sb.append(".+,");
		sb.append(str);
		sb.append(",.+|^");
		sb.append(str);
		sb.append(",.+|^");
		sb.append(str);
		sb.append("$|,.+");
		sb.append(str);
		sb.append("$");
		return content.matches(sb.toString());
	}

	/**
	 * 执行substring方法,不会因为长度错误而异常
	 * 
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static String subString(String str, int start, int end) {
		if (StringUtils.isNotBlank(str) && start < end) {
			if (str.length() > end) {
				return str.substring(start, end);
			} else {
				if (str.length() > start) {
					return str.substring(start);
				} else {
					return "";
				}
			}
		}
		return str;
	}

	/**
	 * 批量验证字符串是否不为空及空字符串
	 * 
	 * @param strings
	 * @return
	 */
	public static boolean checkNotEmpty(String... strings) {
		for (int i = 0; i < strings.length; i++) {
			if (StringUtils.isBlank(strings[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 拼接字符串
	 * 
	 * @param split
	 *            分隔符,如无则不分割
	 * @param strings
	 * @return
	 */
	public static String appendStringByObject(String split, Object... strings) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strings.length;) {
			sb.append(strings[i]);
			if (++i < strings.length && StringUtils.isNotEmpty(split)) {
				sb.append(split);
			}
		}
		return sb.toString();
	}
	/**
	 * 拼接字符串
	 * 
	 * @param split
	 *            分隔符,如无则不分割
	 * @param strings
	 * @return
	 */
	public static String appendStringNotNull(String split, String... strings) {
		if (checkNotEmpty(strings)) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < strings.length;) {
				sb.append(strings[i]);
				if (++i < strings.length && StringUtils.isNotEmpty(split)) {
					sb.append(split);
				}
			}
			return sb.toString();
		}
		return "";
	}

	/**
	 * 拼接字符串
	 * 
	 * @param split
	 *            分隔符,如无则不分割
	 * @param nullCharpter
	 *            当为null时,用于替换的字符
	 * @param strings
	 * @return
	 */
	public static String appendString(String split, String nullCharpter, String... strings) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strings.length;) {
			String str = strings[i];
			if (str != null) {
				sb.append(str);
			} else {
				sb.append(nullCharpter);
			}
			if (++i < strings.length && StringUtils.isNotEmpty(split)) {
				sb.append(split);
			}
		}
		return sb.toString();
	}

	/**
	 * 生成订单号
	 * 
	 * @return
	 */
	public static String getOrderNum() {
		String now = DateUtil.format("yyyyMMddHHmmssSSS", new Date());
		int num = NumberUtil.random(6);
		return now + num;
	}
	
	/**
	 * BeanName转为表名
	 * 即驼峰转表名
	 * @param beanName
	 * @return
	 */
	public static String beanNameToTablleName(String beanName) {
		StringBuffer sb = new StringBuffer();
		for (char c : beanName.toCharArray()) {
			int code = (int) c;
			if (code >= 65 && code <= 90) {
				if (sb.length() > 0) {
					sb.append('_');
				}
				sb.append((char) (code + 32));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 校验字符串长度是否在 min 与 max 之间
	 * @param str
	 * @param minLength
	 * @param maxLength
	 * @return
	 */
	public static boolean checkLength(String str,int minLength,int maxLength){
		if (str == null) {
			return false;
		}
		if (str.length() >= minLength && str.length() <= maxLength) {
			return true;
		}
		return false;
	}
	
	/**
	 * 校验字符是否为 数字字母下划线组成的 6-20位字符串
	 * @param str
	 * @return
	 */
	public static boolean isLoginName(String str){
		if (StringUtils.isBlank(str)) {
			return false;
		}
		return str.matches("^[A-Za-z0-9_]{6,20}$");
	}
	
}

