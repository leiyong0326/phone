package com.base.generate.sql;

import java.io.File;
import java.lang.reflect.Field;

import com.base.generate.BaseScanBeanGenerate;
import com.ly.base.common.util.FileUtil;
import com.ly.base.common.util.ReflectionUtil;
import com.ly.base.common.util.StringUtil;

/**
 * 生成sql语句,如updateOrg
 * @author LeiYong
 *
 */
public class GnerateSqlByBean extends BaseScanBeanGenerate{
	private static final String FILTER_NAME = "\\w+\\.java";// 扫描的文件名,正则方式
	public static void main(String[] args) {
		generateUpdateOrganizationSql();
	}
	private static void generateUpdateOrganizationSql(){
//		generateUpdateSql();//更新所有表机构编号
//		generatePrimaryKeySql();//修改所有机构字段表为联合主键
	}

	/**
	 * 生成替换联合主键sql
	 */
	public static void generatePrimaryKeySql(){
		// D:\workspace\base\base-common
		String fileName = getProjectPath(MODEL_PROJECT) + "/"+BEAN_BASE_PATH+"/" + BEAN_URL.replace(".", "/");
		File file = new File(fileName);
		beanScan(file,FILTER_NAME,(f)->{
			String path = f.getPath();
			String clazzName = getBeanPackage(path, BEAN_BASE_PATH);
			try {
				Class<?> clazz = Class.forName(clazzName);
				Field pkfield = ReflectionUtil.findField(clazz, "orgPk");
				if (pkfield!=null) {
					Field pkf = ReflectionUtil.findField(clazz, "pk");
					Field idf = ReflectionUtil.findField(clazz, "id");
					String pkColumn = null;
					if (pkf!=null) {
						pkColumn = "PK";
					}else if(idf!=null){
						pkColumn = "ID";
					}else{
						return true;
					}
					String name = FileUtil.getFileName(path);
					String tableName = StringUtil.beanNameToTablleName(name);
					System.out.println("ALTER TABLE `"+tableName+"` MODIFY COLUMN `ORG_PK`  varchar(40) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '组织' AFTER `"+pkColumn+"`,DROP PRIMARY KEY,ADD PRIMARY KEY (`"+pkColumn+"`, `ORG_PK`);");
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (FORCE_FLAG) {
				
			}
			return true;
		});
	
	}
	/**
	 * 更新机构编号
	 * @param sql
	 */
	public static void generateUpdateSql() {
		String srcOrg = "0101";
		String tarOrg = "0100001";
		// D:\workspace\base\base-common
		String fileName = getProjectPath(MODEL_PROJECT) + "/"+BEAN_BASE_PATH+"/" + BEAN_URL.replace(".", "/");
		File file = new File(fileName);
		System.out.println("update sys_organization set PK='"+tarOrg+"' where PK='"+srcOrg+"';");
		beanScan(file,FILTER_NAME,(f)->{
			String path = f.getPath();
			String pathIndex = "src\\main\\java\\";
			String clazzName = path.substring(path.indexOf(pathIndex)+pathIndex.length(),path.lastIndexOf(".")).replace("\\", ".");
			try {
				Class<?> clazz = Class.forName(clazzName);
				Field field = ReflectionUtil.findField(clazz, "orgPk");
				if (field!=null) {
					String name = FileUtil.getFileName(path);
					String tableName = StringUtil.beanNameToTablleName(name);
					System.out.println("update "+tableName+" set ORG_PK='"+tarOrg+"' where ORG_PK='"+srcOrg+"';");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (FORCE_FLAG) {
				
			}
			return true;
		});
	}
	
}
