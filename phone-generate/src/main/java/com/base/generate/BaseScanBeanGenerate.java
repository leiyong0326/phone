package com.base.generate;

import java.io.File;

import com.ly.base.common.model.lambda.BeanOperator;
import com.ly.base.common.util.StringUtil;

/**
 * 基础的生成类
 * 提供扫描目录的方法
 * @author LeiYong
 *
 */
public class BaseScanBeanGenerate extends BaseGenerate{
	protected static final String BASE_BEAN_URL = "com.ly.base";
	protected static final String BEAN_URL = BASE_BEAN_URL+".core.model";// model基础目录
	protected static final String CONTROLLER_URL = BASE_BEAN_URL+".controller";// action基础目录
	protected static final String PROXY_URL = BASE_BEAN_URL+".proxy";// action基础目录
	protected static final String CONSUMER_URL = BASE_BEAN_URL+".service.consumer";// action基础目录
	protected static final String CONSUMER_IMPL_URL = BASE_BEAN_URL+".service.consumerImpl";// action基础目录
	protected static final String SERVICE_URL = BASE_BEAN_URL+".core.provide";// service基础目录
	protected static final String EXPORT_URL = BASE_BEAN_URL+".core";// export基础目录
	protected static final String IMPL_SERVICE_URL = BASE_BEAN_URL+".service.provideImpl";// serviceImpl基础目录
	protected static final String MAPPER_URL = BASE_BEAN_URL+".dao";// service基础目录
	// 用于被继承对象的包名
	protected static final String BASE_ACTION_URL = BASE_BEAN_URL+".pub.action";// 被继承action的基础目录
	protected static final String BASE_MAPPER_URL = BASE_BEAN_URL+".dao";// 被继承mapper的基础目录
	protected static final String BASE_SERVICE_URL = BASE_BEAN_URL+".core.provide";// 被继承service的基础目录
	protected static final String BASE_CONSUMER_URL = BASE_BEAN_URL+".service.consumer";// 被继承service的基础目录
	protected static final String BASE_IMPL_SERVICE_URL = BASE_BEAN_URL+".service.provideImpl";// 被继承serviceImpl的基础目录

	// 用于被继承对象的类名
	protected static final String BASE_ACTION_NAME = "BaseAction";// BaseAction.java
	protected static final String BASE_SERVICE_NAME = "BaseService";
	protected static final String BASE_MAPPER_NAME = "BaseMapper";
	protected static final String IMPL_SERVICE_NAME = "BaseServiceImpl";
	protected static final String BASE_CONSUMER_NAME = "BaseConsumerService";
	protected static final String BASE_EXPORT_NAME = "ExcelExportSuper";
	// 公共部分
	protected static final String BEAN_BASE_PATH = getParentDir("src","main","java");
	
	/**
	 * 
	 * <p>
	 * bean文件扫描
	 * </p>
	 * <p>
	 * 扫描指定目录下的所有java文件,生成对应的service和action,与bean同目录结构
	 * </p>
	 * 
	 * @param file
	 *            需要扫描的文件
	 * @param parentPack
	 *            上级包名:若无传递"",若有使用.开头,如".log"
	 */
	public static void beanScan(File file,String filterName,BeanOperator<File> operator) {
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] fs = file.listFiles();
				for (int i = 0; i < fs.length; i++) {
					File f = fs[i];
					beanScan(f,filterName,operator);
				}
			} else if (file.isFile() && file.getName().matches(filterName)) {
				try {
					operator.operator(file);
				} catch (Exception e) {
					printError(file.getAbsolutePath(), e);
				}
			}
		}
	}
	
	/**
	 * 获取bean包名
	 * @param filePath
	 * @param parentDir 包名上级目录,如"src","main","java"
	 * @return
	 */
	public static String getBeanPackage(String filePath,String parentPath){
		String clazzName = filePath.substring(filePath.indexOf(parentPath)+parentPath.length()+1,filePath.lastIndexOf(".")).replace(File.separator, ".");
		return clazzName;
	}
	public static String getParentDir(String... parentDir){
		return StringUtil.appendStringNotNull(File.separator, parentDir);
	}
	public static void main(String[] args) {
		String parentPath = getParentDir("src","main","java");
		String clazz = getBeanPackage(StringUtil.appendStringNotNull(File.separator, "d:","test","src","main","java","com","test","abc.java"),parentPath);
		System.out.println(clazz);
	}
}
