package com.base.generate.sql;

import java.util.Date;

import com.ly.base.common.util.DateUtil;
import com.ly.base.common.util.NumberUtil;
import com.ly.base.common.util.ReflectionUtil;
import com.ly.base.common.util.StringUtil;
import com.ly.base.core.model.sys.SysUser;

/**
 * 生成插入测试sql语句
 * 
 * @author LeiYong
 *
 */
public class GenerateTestSql {
	private static String tableName = "test";
	private static String columns = ReflectionUtil.getFields(SysUser.class);//"pk,name,age...."
	private static long datetime = 3l * 365l * 24l * 3600l * 1000l;// 随机产生一个3年内的日期

	public static void main(String[] args) {
		System.out.println(getInsert(3));
	}

	/**
	 * 生成count条insert语句
	 * 
	 * @param count
	 * @return
	 */
	private static String getInsert(int count) {
		StringBuffer values = new StringBuffer();
		values.append("INSERT INTO " + tableName + "(" + getColumnName(columns) + ") values");
		values.append(getValues(count));
		return values.toString();
	}

	private static String getValues(int count) {
		StringBuffer values = new StringBuffer();
		for (int i = 0; i < count;) {
			values.append("(");
			// 组装插入值
			String val = String.join(",", getPK(), StringUtil.getNonceStr(5), "" + NumberUtil.random(2),
					"" + NumberUtil.random(100), getDate());
			values.append("'" + val.replace(",", "','") + "'");
			values.append(")");
			if (++i < count) {
				values.append(",");
			}
		}
		return values.toString();
	}

	private static String getPK() {
		return StringUtil.getUUID();
		// return StringUtil.getUUID()+StringUtil.getNonceStr(5);
	}

	private static String getDate() {
		return DateUtil.format(new Date(System.currentTimeMillis() - NumberUtil.random(datetime)));
	}

	private static String getColumnName(String column){
		return column.replaceAll("([A-Z])", "_$1").toUpperCase();
	}
}
