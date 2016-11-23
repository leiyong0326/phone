package com.base.generate.sql;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import com.base.generate.BaseGenerate;
import com.base.generate.BaseScanBeanGenerate;
import com.ly.base.common.util.ArrayUtil;
import com.ly.base.common.util.StringUtil;

/**
 * 生成分区代码
 * @author LeiYong
 *
 */
public class GeneratePartitionSql extends BaseScanBeanGenerate{

	private static final String BEAN_BASE_PATH = "src/main/java";
	private static final String BEAN_URL = "com.ly.base.core.model";// model基础目录
	private static final String FILTER_NAME = "\\w+\\.java";// 扫描的文件名,正则方式
	private static final String PARTITION_COUNT = "16";
	private static final Class<?>[] filter_bean = {
//			SysOrganization.class,MtPeccancyDetail.class,
//			ScdType.class,ShopCarBrand.class,ShopCarImg.class,ShopCarOldImg.class,ShopCarPartsImg.class,
//			ShopCarTag.class,ShopPartsClass.class,ShopPartsClassSlave.class,ShopPartsOrderDetail.class,
//			SysMenu.class,SysOrgScore.class,SysRole.class,SysRoleMenu.class,SysScoreType.class,
//			SysServerFee.class,SysTradeSlave.class,UsrUserCar.class
			};//UsrScoreDetail//UsrPhoneHistory
	public static void main(String[] args) {
		// D:\workspace\base\base-common
		String fileName = getProjectPath(MODEL_PROJECT) + "/"+BEAN_BASE_PATH+"/" + BEAN_URL.replace(".", "/");
		File file = new File(fileName);
		beanScan(file, "", true);
	}
	public static void printSql(String beanName){
		boolean res = ArrayUtil.foreach(filter_bean, (c,i)->{
			if (c.getSimpleName().equals(beanName)) {
				return false;
			}
			return true;
		});
		if(!res){
			return; 
		}
//		System.out.print(beanName+"\t");
		String name = StringUtil.beanNameToTablleName(beanName);
		System.out.println("SELECT '"+name+"';");
		System.out.println(StringUtil.appendStringByObject(null, "ALTER TABLE ",name," PARTITION BY KEY(ORG_PK) PARTITIONS ",PARTITION_COUNT,";"));
		
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
	public static void beanScan(File file, String parentPack, boolean isFirst) {
		beanScan(file, FILTER_NAME, (f)->{
			printSql(FilenameUtils.getBaseName(f.getName()));
			return true;
		});
//		if (file.exists()) {
//			if (file.isDirectory()) {
//				File[] fs = file.listFiles();
//				for (int i = 0; i < fs.length; i++) {
//					File f = fs[i];
//					String parent = "";
//					if (!isFirst) {
//						parent = parentPack + "." + file.getName();
//					}
//					beanScan(f, parent, false);
//				}
//			} else if (file.isFile() && file.getName().matches(FILTER_NAME)) {
//				try {
//					printSql(FilenameUtils.getBaseName(file.getName()));// 创建相关类
//				} catch (Exception e) {
//					printError(file.getAbsolutePath(), e);
//				}
//			}
//		}
	}
}
