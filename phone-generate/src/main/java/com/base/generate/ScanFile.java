package com.base.generate;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import com.ly.base.common.util.StringUtil;
/**
 * 扫描目录下所有html
 * @author LeiYong
 *
 */
public class ScanFile extends BaseScanBeanGenerate{
	private static final String BASE_URL = "webapp";
	private static final String REPLACE_URL = "";
	private static final String FILE_TYPE = ".html";
	private static final String FILTER_NAME = ".+\\.html";// 扫描的文件名,正则方式
	private static final boolean REPLACE_URL_FLAG = false;
	private static final boolean ABSOLUTE_URL_FALG = true;//true绝对路径,false相对路径
	private static final boolean PRINT_FALG = true;//打印信息开关
	private static int seq = 1;//生成sql序列开始
	
	public static void main(String[] args) {
		// D:\workspace\base\base-common
		System.out.println("INSERT INTO `cykj`.`sys_menu` (`PK`, `UP_PK`, `NAME`, `URL`, `ICON`, `SEQ`, `MENU_TYPE`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `ENABLE`, `DSC`, `PUB`) VALUES ");
		String fileName = getProjectPath(CONTROLLER_PROJECT) + "/"+BASE_URL+"/";
		File file = new File(fileName);
		beanScan(file,FILTER_NAME,(f)->{
			printByBean(f, getBeanPackage(f.getPath(), BASE_URL).replace(".", "/")+".html");// 创建相关类
			return true;
		});
	}
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
//	public static void beanScan(File file, String parentPack, boolean isFirst) {
//		if (file.exists()) {
//			if (file.isDirectory()) {
//				File[] fs = file.listFiles();
//				for (int i = 0; i < fs.length; i++) {
//					File f = fs[i];
//					String parent = "";
//					if (!isFirst) {
//						parent = parentPack + "/" + file.getName();
//					}
//					beanScan(f, parent, false);
//				}
//			} else if (file.isFile() && file.getName().endsWith(FILE_TYPE)) {
//				try {
//					printByBean(file, parentPack);// 创建相关类
//				} catch (Exception e) {
//					printError(file.getAbsolutePath(), e);
//				}
//			}
//		}
//	}
	private static void printByBean(File file, String parentPack) {
//		String url = parentPack+"/"+file.getName();
		String name = FilenameUtils.getName(file.getName());
		System.out.println(getInsertSql(name,parentPack));
	}
	/**
	 * 获取数据库插入sql
	 * @param url
	 * @return
	 */
	private static String getInsertSql(String name,String url){
		String s = StringUtil.fullStringBefore(seq++ +"", '0', 4);
		String sql = "('"+s+"', '', '查询组织', '"+url+"', NULL, "+s+", 0, NULL, NULL, NULL, NULL, '1', NULL, 1),";
		return sql;
	}
}
