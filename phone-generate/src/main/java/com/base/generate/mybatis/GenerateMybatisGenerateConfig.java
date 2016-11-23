package com.base.generate.mybatis;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.base.generate.BaseGenerate;

public class GenerateMybatisGenerateConfig extends BaseGenerate {
	private static final String TABLE_NAME = "information_schema.columns";
	private static final String DB_NAME = "phone_distribution";
	private static final String TABLE_COLUMN = "table_name";
	private static final String DB_COLUMN = "table_schema";
	private static final String PREFIX = "		";

	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost:3306/" + DB_NAME
			+ "?useUnicode=true&characterEncoding=UTF-8";
	private static final String USER_NAME = "root";
	private static final String PASSWORD = "root";

	private static final boolean IS_TO_FILE = true;
	private static final boolean IS_TO_CONSOLE = true;
	private static final String FILE_PATH = "D:/tools/mybatis/config.txt";

	private static final String[] IGORE_TABLE_REGEX = {};
	private static final String[] ACCESS_TABLE_REGEX = {};

	public static void main(String[] args) {
		executeSql();
		// 查询主外键关系
		// select
		// TABLE_NAME,COLUMN_NAME,CONSTRAINT_NAME,
		// REFERENCED_TABLE_NAME,REFERENCED_COLUMN_NAME
		// from INFORMATION_SCHEMA.KEY_COLUMN_USAGE;
	}

	private static String getFindTablesSql() {
		return "select " + TABLE_COLUMN + " from " + TABLE_NAME + " where " + DB_COLUMN + "='" + DB_NAME + "' group by "
				+ TABLE_COLUMN;
	}

	/**
	 * 生成mybatis generator配置文件
	 */
	private static void executeSql() {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		try {
			Class.forName(DRIVER);
			Connection conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(getFindTablesSql());
			while (rs.next()) {
				int columnCount = rs.getMetaData().getColumnCount();
				if (columnCount == 1) {
					String tableName = rs.getString(1);
					// 匹配忽略的表
					if (IGORE_TABLE_REGEX.length > 0) {
						boolean flag = false;
						for (int i = 0; i < IGORE_TABLE_REGEX.length; i++) {
							String regex = IGORE_TABLE_REGEX[i];
							if (tableName.matches(regex)) {
								flag = true;
								break;
							}
						}
						if (flag) {
							continue;
						}
					}
					// 匹配仅通过的表
					if (ACCESS_TABLE_REGEX.length > 0) {
						boolean flag = false;
						for (int i = 0; i < ACCESS_TABLE_REGEX.length; i++) {
							String regex = IGORE_TABLE_REGEX[i];
							if (tableName.matches(regex)) {
								flag = true;
								break;
							}
						}
						if (!flag) {
							continue;
						}
					}
					sb.append(PREFIX+"<!-- " + tableName + " -->\n");
					sb.append(PREFIX+"<table tableName=\"" + tableName
							+ "\" enableCountByExample=\"false\" enableDeleteByExample=\"false\" enableSelectByExample=\"false\" enableUpdateByExample=\"false\">");
					// sb.append("\n");
					// sb.append("\t<!-- 忽略的字段 -->\n");
					// sb.append("\t<ignoreColumn column=\"FRED\" />\n");
					// sb.append("\t<!-- 指定列的java数据类型 -->\n");
					// sb.append("\t<columnOverride
					// column=\"LONG_VARCHAR_FIELD\" jdbcType=\"VARCHAR\"
					// />\n");
					sb.append("</table>");
					sb.append("\n");
				}
			}
		} catch (ClassNotFoundException e) {
			System.out.println("找不到驱动jar");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("数据库连接失败");
			e.printStackTrace();
		}
		// JDK1.8可自动回收try内的资源,不再finally关闭,作为工具类不关闭也没事...
		if (IS_TO_CONSOLE) {
			System.out.println(sb.toString());
		}
		if (IS_TO_FILE) {
			try {
				writeFile(new File(FILE_PATH), sb);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
