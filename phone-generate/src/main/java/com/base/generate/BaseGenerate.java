package com.base.generate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ly.base.common.util.StringUtil;
/**
 * 基础的生成类
 * 定义所有项目目录及结构
 * 提供打印log,写入文件等方法
 * @author LeiYong
 *
 */
public class BaseGenerate {
	public static final String MODEL_PROJECT = "phone-core";// 项目名
	public static final String SERVICE_PROJECT = "phone-core";// 项目名
	public static final String SERVICE_IMPL_PROJECT = "phone-service";// 项目名
	public static final String MAPPER_PROJECT = "phone-dao";// 项目名
	public static final String CONSUMER_PROJECT = "phone-admin";// 项目名
	public static final String PROXY_PROJECT = "phone-admin";// 项目名
	public static final String CONTROLLER_PROJECT = "phone-admin";// 项目名
	public static final String RT_1 = "\r\n";// 换行
	public static final String RT_2 = RT_1 + RT_1;// 换两行
	public static final String BLANK_1 = "	";// ≈TAB
	public static final String BLANK_2 = BLANK_1 + BLANK_1;
	public static final String BLANK_3 = BLANK_2 + BLANK_1;
	public static final String BLANK_4 = BLANK_2 + BLANK_2;
	public static final boolean PRINT_INFO_FLAG = true;// 打印信息开关
	public static final boolean PRINT_ERROR_FLAG = true;// 打印错误开关
	public static final boolean WRITE_FILE_FLAG = true;// 生成文件开关,false则不创建文件只打印目录信息
	public static final boolean FORCE_FLAG = false;// 强制生成开关,若为true,即使文件已存在也重新生成

	public static final String DATE_STRING = new SimpleDateFormat("yyyy年MM月dd日").format(new Date());
	// 注释部分
	private static final String ANNOTATION_AUTHOR_PARAMTER = "@author ";
	private static final String ANNOTATION_AUTHOR_NAME = "LeiYong";
	private static final String ANNOTATION_AUTHOR = ANNOTATION_AUTHOR_PARAMTER + ANNOTATION_AUTHOR_NAME;
	private static final String ANNOTATION_DATE = "@date ";
	
	public static final String ANNOTATION = StringUtil.appendString(null, null, RT_1,"/**",RT_1," * %s",RT_1 ," * " , ANNOTATION_AUTHOR ,
			RT_1 , " * " , ANNOTATION_DATE+ DATE_STRING , RT_1 , " */" , RT_1); 

	public static String getProjectPath(String project) {
		return System.getProperty("user.dir") + "/../" + project;
	}

	public static void writeFile(File f,StringBuffer sb) throws IOException{
		if(WRITE_FILE_FLAG){
			FileWriter fw = new FileWriter(f, false);
			fw.write(sb.toString());
			fw.flush();
			fw.close();
		}else{
			System.out.println(f.getName());
			System.out.println(sb);
		}
	}
	public static void printLog(String info) {
		if (PRINT_INFO_FLAG) {
			System.out.println("generate file：" + info + " success！");
		}
	}

	public static void printError(String info, Exception e) {
		if (PRINT_ERROR_FLAG) {
			System.err.println("generate file：" + info + " error！Exception:" + e.getMessage());
			e.printStackTrace();
		}
	}
}
