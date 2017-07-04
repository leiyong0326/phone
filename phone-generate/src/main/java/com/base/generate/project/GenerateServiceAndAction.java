package com.base.generate.project;

import static com.base.generate.pub.ImportUtil.*;

import java.io.File;
import java.lang.reflect.Field;

import org.apache.commons.io.FilenameUtils;

import com.base.generate.BaseScanBeanGenerate;
import com.ly.base.common.util.ReflectionUtil;
import com.ly.base.common.util.StringUtil;

/**
 * 
 * <p>
 * 生成各个bean的service以及action
 * </p>
 * <p>
 * 支持任意级目录扫描,使用时注意配置各个路径
 * </p>
 * 
 * @author Ray
 * @version V1.0, 2015年8月3日
 * @since 2015年8月3日
 */
@SuppressWarnings("rawtypes")
public class GenerateServiceAndAction extends BaseScanBeanGenerate{
	
	private static final String FILTER_NAME = "\\w+\\.java";// 扫描的文件名,正则方式

	public static final boolean PRINT_INFO_FLAG = true;// 打印信息开关
	public static final boolean PRINT_ERROR_FLAG = true;// 打印错误开关
	public static final boolean WRITE_FILE_FLAG = true;// 生成文件开关,false则不创建文件只打印目录信息
	public static final boolean FORCE_FLAG = false;// 强制生成开关,若为true,即使文件已存在也重新生成
	public static final boolean PRINT_ENUM = false;// 打印枚举,为true后不再生成文件,优先级最高
	public static final boolean PRINT_DUBBO_CONSUMER_CONFIG = false;// 打印Dubbo配置,为true后不再生成文件,优先级其次
	public static final boolean IS_DUBBO = false;// 打印Dubbo配置,为true后不再生成文件,优先级其次
	
	//生成类开关
	public static final boolean GENERATE_MAPPER = true;// 生成Mapper文件开关
	public static final boolean GENERATE_SERVICE = true;// 生成Service文件开关
	public static final boolean GENERATE_SERVICE_IMPL = true;// 生成ServiceImpl文件开关
	public static final boolean GENERATE_CONSUMER = true;// 生成ConsumerService文件开关
	public static final boolean GENERATE_CONSUMER_IMPL = true;// 生成ConsumerServiceImpl文件开关
	public static final boolean GENERATE_PROXY = true;// 生成Proxy文件开关
	public static final boolean GENERATE_CONTROLLER = true;// 生成Controller文件开关
	public static final boolean GENERATE_EXPORT = true;// 生成导出文件开关
	
	

	public static void main(String[] args) {
		// D:\workspace\base\base-common
		String fileName = StringUtil.appendStringNotNull(File.separator, getProjectPath(MODEL_PROJECT) ,BEAN_BASE_PATH, BEAN_URL.replace(".", File.separator));
		File file = new File(fileName);
		beanScan(file, FILTER_NAME, (f)->{
			try {
				String parentPackage = getBeanPackage(f.getPath(), BEAN_BASE_PATH+File.separator+BEAN_URL.replace(".", File.separator));
				createByBean(f,parentPackage.substring(0, parentPackage.lastIndexOf(".")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		});
	}


	/**
	 * 
	 * <p>
	 * 创建对应的对象
	 * </p>
	 * <p>
	 * 创建serviceI,serviceImpl,Action
	 * </p>
	 * 
	 * @param f
	 *            被创建的对象
	 * @param parentPack
	 *            上级包名:若无传递"",否则传递目录,如"com.log"
	 * @throws Exception
	 */
	public static void createByBean(File f, String parentPack) throws Exception {
		String baseName = FilenameUtils.getBaseName(f.getName());
		Class c = Class.forName(StringUtil.appendStringNotNull(".", BEAN_URL , parentPack,baseName));
		if (PRINT_ENUM) {
			createBeanEnum(baseName, parentPack);
		}else if(PRINT_DUBBO_CONSUMER_CONFIG){
			createBeanDubboConfig(baseName, parentPack);
		}else{
			if (GENERATE_MAPPER) {
				createBeanMapper(c, parentPack);
			}
			if (GENERATE_SERVICE) {
				createBeanService(c, parentPack);
			}
			if (GENERATE_SERVICE_IMPL) {
				createBeanServiceImpl(c, parentPack);
			}
			if (GENERATE_CONSUMER) {
				createConsumerService(c, parentPack);
			}
			if (GENERATE_CONSUMER_IMPL) {
				createConsumerServiceImpl(c, parentPack);
			}
			if (GENERATE_PROXY) {
				createProxy(c, parentPack);
			}
			if (GENERATE_CONTROLLER) {
				createController(c, parentPack);
			}
			if (GENERATE_EXPORT) {
				createExport(c, parentPack);
			}
		}

	}

	
	/**
	 * 创建bean的service
	 * 
	 * @param c
	 *            需要生成的modelBean
	 * @param parentPack
	 *            上级包名:若无传递"",否则传递目录,如"com.log"
	 * @throws Exception
	 */
	public static void createBeanMapper(Class c, String parentPack) throws Exception {
		String cName = c.getSimpleName();
		String fileName = getFileName(MAPPER_PROJECT, MAPPER_URL, parentPack,cName, "Mapper.java");
		File f = new File(fileName);
		if (checkFile(f)) {
			//判断pk类型
			String[] pkTypes = getPkAndType(c);
			String pk = pkTypes[1];
			String pkType = pkTypes[0];
			StringBuffer sb = new StringBuffer();
			sb.append(packages(StringUtil.appendStringNotNull(".", MAPPER_URL,parentPack)));
			sb.append(importBase(StringUtil.appendStringNotNull(".", BEAN_URL,parentPack,cName)));// 导入实体类
			sb.append(importBase(StringUtil.appendStringNotNull(".", BASE_MAPPER_URL,BASE_MAPPER_NAME)));// 导入接口
			sb.append(importMybatisParam());
			sb.append(code(String.format(ANNOTATION, "数据库处理"), 0, 0));
			sb.append(code(String.format("public interface %sMapper extends %s<%s> {", cName,BASE_MAPPER_NAME,cName),0,2));
			sb.append(getComment("通过主键查询", true,pk));
			sb.append(code(String.format("%s selectByPrimaryKey(@Param(\"%s\") %s %s);",cName,pk,pkType,pk),1,2));
			sb.append(getComment("通过主键删除", true,pk));
			sb.append(code(String.format("int deleteByPrimaryKey(@Param(\"%s\") %s %s);",pk,pkType,pk),1,2));
			sb.append(getComment("通过主键批量删除", true,pk+"s"));
			sb.append(code(String.format("int deleteByBatch(@Param(\"%ss\") %s[] %ss);",pk,pkType,pk),1,2));
			sb.append(getComment("通过主键更新单列", true,pk));
			sb.append(code(String.format("int updateState(@Param(\"updateBy\") String updateBy,@Param(\"column\") String column, @Param(\"status\") String status,@Param(\"%s\") %s %s);",pk,pkType,pk),1,2));
			sb.append(getComment("通过主键批量更新单列", true,pk+"s"));
			sb.append(code(String.format("int updateStates(@Param(\"updateBy\") String updateBy,@Param(\"column\") String column, @Param(\"status\") String status,@Param(\"%ss\") %s... %ss);",pk,pkType,pk),1,2));
			sb.append(code("}",0,0));
			writeFile(f, sb);
			printLog(fileName);
		}
	}
	/**
	 * 创建bean的service
	 * 
	 * @param c
	 *            需要生成的modelBean
	 * @param parentPack
	 *            上级包名:若无传递"",否则传递目录,如"com.log"
	 * @throws Exception
	 */
	public static void createBeanService(Class c, String parentPack) throws Exception {
		String cName = c.getSimpleName();
		String fileName = getFileName(SERVICE_PROJECT, SERVICE_URL, parentPack,cName, "Service.java");
		File f = new File(fileName);
		if (checkFile(f)) {
			//判断pk类型
			String[] pkTypes = getPkAndType(c);
			String pk = pkTypes[1];
			String pkType = pkTypes[0];
			StringBuffer sb = new StringBuffer();
			sb.append(packages(StringUtil.appendStringNotNull(".", SERVICE_URL,parentPack)));
			sb.append(importBase(StringUtil.appendStringNotNull(".", BEAN_URL,parentPack,cName)));// 导入实体类
			sb.append(importBase(StringUtil.appendStringNotNull(".", BASE_SERVICE_URL,BASE_SERVICE_NAME)));// 导入接口
			sb.append(code(String.format(ANNOTATION, "业务处理"), 0, 0));
			sb.append(code(String.format("public interface %sService extends %s<%s> {", cName, BASE_SERVICE_NAME,cName),0,1));
			sb.append(getComment("通过主键查询", true,pk));
			sb.append(code(String.format("public %s selectByPrimaryKey(%s %s);",cName,pkType,pk),1,1));
			sb.append(getComment("通过主键删除", true,pk));
			sb.append(code(String.format("public int deleteByPrimaryKey(%s %s);",pkType,pk),1,1));
			sb.append(getComment("通过主键批量删除", true,pk+"s"));
			sb.append(code(String.format("public int deleteByBatch(%s[] %ss);",pkType,pk),1,1));
			sb.append(getComment("通过主键启用", true,pk));
			sb.append(code(String.format("public int enable(String updateBy,%s %s);",pkType,pk),1,1));
			sb.append(getComment("通过主键禁用", true,pk));
			sb.append(code(String.format("public int disable(String updateBy,%s %s);",pkType,pk),1,1));
			sb.append(getComment("通过主键批量启用", true,pk+"s"));
			sb.append(code(String.format("public int enables(String updateBy,%s... %ss);",pkType,pk),1,1));
			sb.append(getComment("通过主键批量禁用", true,pk+"s"));
			sb.append(code(String.format("public int disables(String updateBy,%s... %ss);",pkType,pk),1,1));
			sb.append(code("}",0,0));
			writeFile(f, sb);
			printLog(fileName);
		}
	}

	/**
	 * 创建bean的service的实现类
	 * 
	 * @param c
	 *            需要生成的modelBean
	 * @param parentPack
	 *            上级包名:若无传递"",否则传递目录,如"com.log"
	 * @throws Exception
	 */
	public static void createBeanServiceImpl(Class c, String parentPack) throws Exception {
		String cName = c.getSimpleName();
		String fileName = getFileName(SERVICE_IMPL_PROJECT, IMPL_SERVICE_URL, parentPack,cName, "ServiceImpl.java");
		File f = new File(fileName);
		if (checkFile(f)) {
			//判断pk类型
			String[] pkTypes = getPkAndType(c);
			String pk = pkTypes[1];
			String pkType = pkTypes[0];
			StringBuffer sb = new StringBuffer();
			sb.append(packages(StringUtil.appendStringNotNull(".", IMPL_SERVICE_URL,parentPack)));
			if (IS_DUBBO) {
				sb.append(importDubboService());
			}else {
				sb.append(importSpringService());
			}
			sb.append(importAutowired());// 导入自动注入注解
			sb.append(importBase(StringUtil.appendStringNotNull(".",BEAN_URL,parentPack,cName)));// 导入实体类
			sb.append(importBase(StringUtil.appendStringNotNull(".",BASE_IMPL_SERVICE_URL , IMPL_SERVICE_NAME)));// 导入父impl
			sb.append(importBase(StringUtil.appendStringNotNull(".",SERVICE_URL , parentPack , cName+"Service")));// 导入接口
			sb.append(importBase(StringUtil.appendStringNotNull(".",MAPPER_URL , parentPack , cName+"Mapper")));// 导入Mapper
			
			
			sb.append(code(String.format(ANNOTATION, "业务处理,数据缓存"), 0, 0));
			if (IS_DUBBO) {
				sb.append(getDubboService());
			}else {
				sb.append(getSpringService());
			}
			sb.append(code(String.format("public class %sServiceImpl extends %s<%s> implements %sService {",cName,IMPL_SERVICE_NAME,cName,cName), 0, 2));
			sb.append(getAutowired());
			sb.append(code(String.format("public void setBaseMapper(%sMapper baseMapper) {%s%s%s}", cName,RT_1+BLANK_2,"super.setBaseMapper(baseMapper);",RT_1+BLANK_1),1,1));
			sb.append(code(String.format("public %sMapper getBaseMapper() {%s%s%s}", cName,RT_1+BLANK_2,"return ("+cName+"Mapper) super.getBaseMapper();",RT_1+BLANK_1),1,1));
			sb.append(getComment("通过主键查询", true,pk));
			sb.append(getOverride());
			sb.append(code(String.format("public %s selectByPrimaryKey(%s %s){%s%s%s}",cName,pkType,pk,RT_1+BLANK_2,"return getBaseMapper().selectByPrimaryKey("+pk+");",RT_1+BLANK_1),1,1));
			sb.append(getComment("通过主键删除", true,pk));
			sb.append(getOverride());
			sb.append(code(String.format("public int deleteByPrimaryKey(%s %s){%s%s%s}",pkType,pk,RT_1+BLANK_2,"return getBaseMapper().deleteByPrimaryKey("+pk+");",RT_1+BLANK_1),1,1));
			sb.append(getComment("通过主键批量删除", true,pk+"s"));
			sb.append(getOverride());
			sb.append(code(String.format("public int deleteByBatch(%s[] %ss){%s%s%s}",pkType,pk,RT_1+BLANK_2,"return getBaseMapper().deleteByBatch("+pk+"s);",RT_1+BLANK_1),1,1));
			sb.append(getComment("通过主键启用", true,pk));
			sb.append(getOverride());
			sb.append(code(String.format("public int enable(String updateBy,%s %s){%s%s%s}",pkType,pk,RT_1+BLANK_2,"return getBaseMapper().updateState(updateBy,\"enable\",\"1\","+pk+");",RT_1+BLANK_1),1,1));
			sb.append(getComment("通过主键禁用", true,pk));
			sb.append(getOverride());
			sb.append(code(String.format("public int disable(String updateBy,%s %s){%s%s%s}",pkType,pk,RT_1+BLANK_2,"return getBaseMapper().updateState(updateBy,\"enable\",\"0\","+pk+");",RT_1+BLANK_1),1,1));
			sb.append(getComment("通过主键批量启用", true,pk+"s"));
			sb.append(getOverride());
			sb.append(code(String.format("public int enables(String updateBy,%s... %ss){%s%s%s}",pkType,pk,RT_1+BLANK_2,"return getBaseMapper().updateStates(updateBy,\"enable\",\"1\","+pk+"s);",RT_1+BLANK_1),1,1));
			sb.append(getComment("通过主键批量禁用", true,pk+"s"));
			sb.append(getOverride());
			sb.append(code(String.format("public int disables(String updateBy,%s... %ss){%s%s%s}",pkType,pk,RT_1+BLANK_2,"return getBaseMapper().updateStates(updateBy,\"enable\",\"0\","+pk+"s);",RT_1+BLANK_1),1,1));
			sb.append(code("}",0,0));
			writeFile(f, sb);
			printLog(fileName);
		}

	}
	/**
	 * 创建消费者service接口
	 * 
	 * @param c
	 *            需要生成的modelBean
	 * @param parentPack
	 *            上级包名:若无传递"",否则传递目录,如"com.log"
	 * @throws Exception
	 */
	public static void createConsumerService(Class c, String parentPack) throws Exception {
		String cName = c.getSimpleName();
		String fileName = getFileName(CONSUMER_PROJECT, CONSUMER_URL, parentPack,cName, "ConsumerService.java");
		File f = new File(fileName);
		if (checkFile(f)) {
			//判断pk类型
			String[] pkTypes = getPkAndType(c);
			String pk = pkTypes[1];
			String pkType = pkTypes[0];
			//开始拼接类
			StringBuffer sb = new StringBuffer();
			sb.append(packages(StringUtil.appendStringNotNull(".", CONSUMER_URL,parentPack)));
			sb.append(importBase(StringUtil.appendStringNotNull(".",BEAN_URL,parentPack,cName)));// 导入实体类
			sb.append(importJson());// 导入接口
			sb.append(importList());
			sb.append(importModel());
			//注释
			sb.append(code(String.format(ANNOTATION, "日志记录,处理返回结果,缓存特殊数据"), 0, 0));
			sb.append(code(String.format("public interface %sConsumerService {", cName),0,2));
			if (pk!=null) {
				sb.append(getComment("通过主键查询", true,pk));
				sb.append(code(String.format("public Json getByPk(%s %s);",pkType,pk),1,1));
				sb.append(getComment("通过主键删除", true,pk));
				sb.append(code(String.format("public Json deleteByPk(%s %s);",pkType,pk),1,1));
				sb.append(getComment("通过主键批量删除", true,pk+"s"));
				sb.append(code(String.format("public Json deleteByBatch(%s... %ss);",pkType,pk),1,1));
				sb.append(getComment("通过主键启用", true,pk));
				sb.append(code(String.format("public Json enable(String updateBy,%s %s);",pkType,pk),1,1));
				sb.append(getComment("通过主键禁用", true,pk));
				sb.append(code(String.format("public Json disable(String updateBy,%s %s);",pkType,pk),1,1));
				sb.append(getComment("通过主键批量启用", true,pk+"s"));
				sb.append(code(String.format("public Json enables(String updateBy,%s... %ss);",pkType,pk),1,1));
				sb.append(getComment("通过主键批量禁用", true,pk+"s"));
				sb.append(code(String.format("public Json disables(String updateBy,%s... %ss);",pkType,pk),1,1));
				sb.append(getComment("新增记录", true,"data"));
				sb.append(code(String.format("public Json insert(%s data);",cName),1,1));
				sb.append(getComment("批量新增", true,"list"));
				sb.append(code(String.format("public Json insertBatch(List<%s> list);",cName),1,1));
				sb.append(getComment("更新记录", true,"data"));
				sb.append(code(String.format("public Json update(%s data);",cName),1,1));
				sb.append(getComment("分页查询", true,"queryInfo","pageNum","pageSize","orderBy"));
				sb.append(code(String.format("public Json findByPage(%s queryInfo, int pageNum, int pageSize,String orderBy);",cName),1,1));
				sb.append(getComment("查询所有", true,"queryInfo","orderBy"));
				sb.append(code(String.format("public Json findAll(%s queryInfo, String orderBy);",cName),1,1));
				sb.append(getComment("分页查询", true,"conditions","pageNum","pageSize","orderBy"));
				sb.append(code(String.format("public Json findByPage(List<Model> conditions, int pageNum, int pageSize, String order);",cName),1,1));
				sb.append(getComment("查询所有", true,"conditions","orderBy"));
				sb.append(code(String.format("public Json findAll(List<Model> conditions, String orderBy);"),1,1));
				if (GENERATE_EXPORT) {
					sb.append(getComment("导出", true,"conditions","orderBy"));
					sb.append(code(String.format("public Json findByExport(List<Model> conditions, String orderBy);"),1,1));
				}
			}
			sb.append(code("}",0,0));
			writeFile(f, sb);
			printLog(fileName);
		}
	}
	
	/**
	 * 创建消费者service实现类
	 * 
	 * @param c
	 *            需要生成的modelBean
	 * @param parentPack
	 *            上级包名:若无传递"",否则传递目录,如"com.log"
	 * @throws Exception
	 */
	public static void createConsumerServiceImpl(Class c, String parentPack) throws Exception {
		String cName = c.getSimpleName();
		String fileName = getFileName(CONSUMER_PROJECT, CONSUMER_IMPL_URL, parentPack,cName, "ConsumerServiceImpl.java");
		String serviceName = cName+"Service";
		String consumerServiceName = cName+"ConsumerService";
		File f = new File(fileName);
		if (checkFile(f)) {
			//判断pk类型
			String[] pkTypes = getPkAndType(c);
			String pk = pkTypes[1];
			String pkType = pkTypes[0];
			String upperPk = getGetOneUpper(pk);
			//开始拼接类
			StringBuffer sb = new StringBuffer();
			sb.append(packages(StringUtil.appendStringNotNull(".", CONSUMER_IMPL_URL,parentPack)));
			sb.append(importBase(StringUtil.appendStringNotNull(".",BEAN_URL,parentPack,cName)));// 导入实体类
			sb.append(importBase(StringUtil.appendStringNotNull(".",SERVICE_URL,parentPack,serviceName)));// 导入实体类
			sb.append(importBase(StringUtil.appendStringNotNull(".",CONSUMER_URL,parentPack,consumerServiceName)));// 导入实体类
			sb.append(importJson());// 导入接口
			sb.append(importList());
			sb.append(importAutowired());
			sb.append(importModel());
			sb.append(importPage());
			sb.append(importLogger());
			sb.append(importErrorConfig());
			sb.append(importComponent());
			//注释
			sb.append(code(String.format(ANNOTATION, "日志记录,处理返回结果,缓存特殊数据"), 0, 0));
			sb.append(getComponent());
			sb.append(code(String.format("public class %sConsumerServiceImpl implements %s {", cName,consumerServiceName),0,2));
			sb.append(getAutowired());
			sb.append(code(String.format("private %sService service;", cName), 1, 1));
//			sb.append(code(String.format("public void set%s(%s service) {%s%s%s}",serviceName, serviceName,RT_1+BLANK_2,"this.service=service;",RT_1+BLANK_1),1,1));
			if (pk!=null) {
				//getByPk
				sb.append(getComment("通过主键查询", true,pk));
				sb.append(getOverride());
				sb.append(getLogger(cName,"Select", "#"+pk));
				sb.append(code(String.format("public Json getByPk(%s %s) {",pkType,pk),1,1));
				sb.append(code(String.format("Json json = new Json();"),2,1));
				sb.append(code(String.format("%s result = service.selectByPrimaryKey(%s);",cName,pk),2,1));
				sb.append(code(String.format("if (result == null) {"),2,1));
				sb.append(code(String.format("return ErrorConfig.getSystemErrorJson();"),3,1));
				sb.append(code(String.format("}"),2,1));
				sb.append(code(String.format("json.setSuccess(true).setObj(result);"),2,1));
				sb.append(code(String.format("return json;"),2,1));
				sb.append(code(String.format("}"),1,1));
				//deleteByPk
				sb.append(getComment("通过主键删除", true,pk));
				sb.append(getOverride());
				sb.append(getLogger(cName,"Delete", "#"+pk));
				sb.append(code(String.format("public Json deleteByPk(%s %s) {",pkType,pk),1,1));
				sb.append(code(String.format("Json json = new Json();"),2,1));
				sb.append(code(String.format("int result = service.deleteByPrimaryKey(%s);",pk),2,1));
				sb.append(code(String.format("if (result == 0) {"),2,1));
				sb.append(code(String.format("return ErrorConfig.getSystemDeleteErrorJson();"),3,1));
				sb.append(code(String.format("}"),2,1));
				sb.append(code(String.format("json.setSuccess(true);"),2,1));
				sb.append(code(String.format("return json;"),2,1));
				sb.append(code(String.format("}"),1,1));
				//deleteByBatch
				sb.append(getComment("通过主键批量删除", true,pk+"s"));
				sb.append(getOverride());
				sb.append(getLogger(cName, "Delete","#"+pk+"s?.![#this]"));
				sb.append(code(String.format("public Json deleteByBatch(%s... %ss) {",pkType,pk),1,1));
				sb.append(code(String.format("Json json = new Json();"),2,1));
				sb.append(code(String.format("int result = service.deleteByBatch(%ss);",pk),2,1));
				sb.append(code(String.format("if (result == 0) {"),2,1));
				sb.append(code(String.format("return ErrorConfig.getSystemDeleteErrorJson();"),3,1));
				sb.append(code(String.format("}"),2,1));
				sb.append(code(String.format("json.setSuccess(true);"),2,1));
				sb.append(code(String.format("return json;"),2,1));
				sb.append(code(String.format("}"),1,1));
				//enable
				sb.append(getComment("通过主键启用", true,pk));
				sb.append(getOverride());
				sb.append(getLogger(cName,"Update", "#"+pk));
				sb.append(code(String.format("public Json enable(String updateBy,%s %s) {",pkType,pk),1,1));
				sb.append(code(String.format("Json json = new Json();"),2,1));
				sb.append(code(String.format("int result = service.enable(updateBy,%s);",pk),2,1));
				sb.append(code(String.format("if (result == 0) {"),2,1));
				sb.append(code(String.format("return ErrorConfig.getSystemEnableErrorJson();"),3,1));
				sb.append(code(String.format("}"),2,1));
				sb.append(code(String.format("json.setSuccess(true);"),2,1));
				sb.append(code(String.format("return json;"),2,1));
				sb.append(code(String.format("}"),1,1));
				//disable
				sb.append(getComment("通过主键禁用", true,pk));
				sb.append(getOverride());
				sb.append(getLogger(cName,"Update", "#"+pk));
				sb.append(code(String.format("public Json disable(String updateBy,%s %s) {",pkType,pk),1,1));
				sb.append(code(String.format("Json json = new Json();"),2,1));
				sb.append(code(String.format("int result = service.disable(updateBy,%s);",pk),2,1));
				sb.append(code(String.format("if (result == 0) {"),2,1));
				sb.append(code(String.format("return ErrorConfig.getSystemDisableErrorJson();"),3,1));
				sb.append(code(String.format("}"),2,1));
				sb.append(code(String.format("json.setSuccess(true);"),2,1));
				sb.append(code(String.format("return json;"),2,1));
				sb.append(code(String.format("}"),1,1));
				//enables
				sb.append(getComment("通过主键批量启用", true,pk+"s"));
				sb.append(getOverride());
				sb.append(getLogger(cName,"Update", "#"+pk+"s?.![#this]"));
				sb.append(code(String.format("public Json enables(String updateBy,%s... %ss) {",pkType,pk),1,1));
				sb.append(code(String.format("Json json = new Json();"),2,1));
				sb.append(code(String.format("int result = service.enables(updateBy,%ss);",pk),2,1));
				sb.append(code(String.format("if (result == 0) {"),2,1));
				sb.append(code(String.format("return ErrorConfig.getSystemEnableErrorJson();"),3,1));
				sb.append(code(String.format("}"),2,1));
				sb.append(code(String.format("json.setSuccess(true);"),2,1));
				sb.append(code(String.format("return json;"),2,1));
				sb.append(code(String.format("}"),1,1));
				//disables
				sb.append(getComment("通过主键批量禁用", true,pk+"s"));
				sb.append(getOverride());
				sb.append(getLogger(cName,"Update", "#"+pk+"s?.![#this]"));
				sb.append(code(String.format("public Json disables(String updateBy,%s... %ss) {",pkType,pk),1,1));
				sb.append(code(String.format("Json json = new Json();"),2,1));
				sb.append(code(String.format("int result = service.disables(updateBy,%ss);",pk),2,1));
				sb.append(code(String.format("if (result == 0) {"),2,1));
				sb.append(code(String.format("return ErrorConfig.getSystemDisableErrorJson();"),3,1));
				sb.append(code(String.format("}"),2,1));
				sb.append(code(String.format("json.setSuccess(true);"),2,1));
				sb.append(code(String.format("return json;"),2,1));
				sb.append(code(String.format("}"),1,1));
				//insert
				sb.append(getComment("新增记录", true,"data"));
				sb.append(getOverride());
				sb.append(getLogger(cName,"Save", "#data?."+pk));
				sb.append(code(String.format("public Json insert(%s data) {",cName),1,1));
				sb.append(code(String.format("Json json = new Json();"),2,1));
				sb.append(code(String.format("%s result = service.insertSelective(data);",cName),2,1));
				sb.append(code(String.format("if (result == null) {"),2,1));
				sb.append(code(String.format("return ErrorConfig.getSystemAddErrorJson();"),3,1));
				sb.append(code(String.format("}"),2,1));
				sb.append(code(String.format("//回写主键参数用于记录日志"),2,1));
				sb.append(code(String.format("data.set%s(result.get%s());",upperPk,upperPk),2,1));
				sb.append(code(String.format("json.setSuccess(true);"),2,1));
				sb.append(code(String.format("return json;"),2,1));
				sb.append(code(String.format("}"),1,1));
				//insertBatch
				sb.append(getComment("批量新增(不记录详细插入信息,如要记录pk参数,需service调用后回写主键)", true,"list"));
				sb.append(getOverride());
				//批量新增不记录日志
				sb.append(getLogger(cName, "Save","#objs?.!['1']"));
				sb.append(code(String.format("public Json insertBatch(List<%s> list) {",cName),1,1));
				sb.append(code(String.format("Json json = new Json();"),2,1));
				sb.append(code(String.format("int result = service.insertBatch(list);"),2,1));
				sb.append(code(String.format("if (result == 0) {"),2,1));
				sb.append(code(String.format("return ErrorConfig.getSystemAddErrorJson();"),3,1));
				sb.append(code(String.format("}"),2,1));
				sb.append(code(String.format("json.setSuccess(true);"),2,1));
				sb.append(code(String.format("return json;"),2,1));
				sb.append(code(String.format("}"),1,1));
				//update
				sb.append(getComment("更新记录", true,"data"));
				sb.append(getOverride());
				sb.append(getLogger(cName,"Update", "#data?."+pk));
				sb.append(code(String.format("public Json update(%s data) {",cName),1,1));
				sb.append(code(String.format("Json json = new Json();"),2,1));
				sb.append(code(String.format("int result = service.updateByPK(data);"),2,1));
				sb.append(code(String.format("if (result == 0) {"),2,1));
				sb.append(code(String.format("return ErrorConfig.getSystemUpdateErrorJson();"),3,1));
				sb.append(code(String.format("}"),2,1));
				sb.append(code(String.format("json.setSuccess(true);"),2,1));
				sb.append(code(String.format("return json;"),2,1));
				sb.append(code(String.format("}"),1,1));
				//findByPage
				sb.append(getComment("分页查询", true,"queryInfo","pageNum","pageSize","orderBy"));
				sb.append(getOverride());
				sb.append(getLogger(cName,"Select", ""));
				sb.append(code(String.format("public Json findByPage(%s queryInfo, int pageNum, int pageSize,String orderBy) {",cName),1,1));
				sb.append(code(String.format("Json json = new Json();"),2,1));
				sb.append(code(String.format("Page<%s> result = service.findByPage(queryInfo,pageNum,pageSize,orderBy);",cName),2,1));
				sb.append(code(String.format("if (result == null) {"),2,1));
				sb.append(code(String.format("return ErrorConfig.getSystemErrorJson();"),3,1));
				sb.append(code(String.format("}"),2,1));
				sb.append(code(String.format("json.setSuccess(true).setObj(result).setTotal(result.getPages());"),2,1));
				sb.append(code(String.format("return json;"),2,1));
				sb.append(code(String.format("}"),1,1));
				//findAll
				sb.append(getComment("查询所有", true,"queryInfo","orderBy"));
				sb.append(getOverride());
				sb.append(getLogger(cName,"Select", ""));
				sb.append(code(String.format("public Json findAll(%s queryInfo, String orderBy) {",cName),1,1));
				sb.append(code(String.format("Json json = new Json();"),2,1));
				sb.append(code(String.format("Page<%s> result = service.findAll(queryInfo,orderBy);",cName),2,1));
				sb.append(code(String.format("if (result == null) {"),2,1));
				sb.append(code(String.format("return ErrorConfig.getSystemErrorJson();"),3,1));
				sb.append(code(String.format("}"),2,1));
				sb.append(code(String.format("json.setSuccess(true).setObj(result);"),2,1));
				sb.append(code(String.format("return json;"),2,1));
				sb.append(code(String.format("}"),1,1));
				//findByPage
				sb.append(getComment("分页查询", true,"conditions","pageNum","pageSize","orderBy"));
				sb.append(getOverride());
				sb.append(getLogger(cName,"Select", ""));
				sb.append(code(String.format("public Json findByPage(List<Model> conditions, int pageNum, int pageSize,String orderBy) {"),1,1));
				sb.append(code(String.format("Json json = new Json();"),2,1));
				sb.append(code(String.format("Page<%s> result = service.findByPage(conditions,pageNum,pageSize,orderBy);",cName),2,1));
				sb.append(code(String.format("if (result == null) {"),2,1));
				sb.append(code(String.format("return ErrorConfig.getSystemErrorJson();"),3,1));
				sb.append(code(String.format("}"),2,1));
				sb.append(code(String.format("json.setSuccess(true).setObj(result).setTotal(result.getPages());"),2,1));
				sb.append(code(String.format("return json;"),2,1));
				sb.append(code(String.format("}"),1,1));
				//findAll
				sb.append(getComment("查询所有", true,"conditions","orderBy"));
				sb.append(getOverride());
				sb.append(getLogger(cName,"Select",""));
				sb.append(code(String.format("public Json findAll(List<Model> conditions, String orderBy) {",cName),1,1));
				sb.append(code(String.format("Json json = new Json();"),2,1));
				sb.append(code(String.format("Page<%s> result = service.findAll(conditions,orderBy);",cName),2,1));
				sb.append(code(String.format("if (result == null) {"),2,1));
				sb.append(code(String.format("return ErrorConfig.getSystemErrorJson();"),3,1));
				sb.append(code(String.format("}"),2,1));
				sb.append(code(String.format("json.setSuccess(true).setObj(result);"),2,1));
				sb.append(code(String.format("return json;"),2,1));
				sb.append(code(String.format("}"),1,1));
				if (GENERATE_EXPORT) {
					//export
					sb.append(getComment("导出", true,"conditions","orderBy"));
					sb.append(getOverride());
					sb.append(getLogger(cName,"Export",""));
					sb.append(code(String.format("public Json findByExport(List<Model> conditions, String orderBy) {",cName),1,1));
					sb.append(code(String.format("Json json = new Json();"),2,1));
					sb.append(code(String.format("Page<%s> result = service.findAll(conditions,orderBy);",cName),2,1));
					sb.append(code(String.format("if (result == null) {"),2,1));
					sb.append(code(String.format("return ErrorConfig.getSystemErrorJson();"),3,1));
					sb.append(code(String.format("}"),2,1));
					sb.append(code(String.format("json.setSuccess(true).setObj(result);"),2,1));
					sb.append(code(String.format("return json;"),2,1));
					sb.append(code(String.format("}"),1,1));
				}
			}
			sb.append(code("}",0,0));
			writeFile(f, sb);
			printLog(fileName);
		}
	}
	
	/**
	 * 创建消费者service代理类
	 * 
	 * @param c
	 *            需要生成的modelBean
	 * @param parentPack
	 *            上级包名:若无传递"",否则传递目录,如"com.log"
	 * @throws Exception
	 */
	public static void createProxy(Class c, String parentPack) throws Exception {
		String cName = c.getSimpleName();
		String fileName = getFileName(PROXY_PROJECT, PROXY_URL, parentPack,cName, "Proxy.java");
		String consumerServiceName = cName+"ConsumerService";
		File f = new File(fileName);
		if (checkFile(f)) {
			//判断pk类型
			String[] pkTypes = getPkAndType(c);
			String pk = pkTypes[1];
			String pkType = pkTypes[0];
			//开始拼接类
			StringBuffer sb = new StringBuffer();
			sb.append(packages(StringUtil.appendStringNotNull(".", PROXY_URL,parentPack)));
			sb.append(importBase(StringUtil.appendStringNotNull(".",BEAN_URL,parentPack,cName)));// 导入实体类
			sb.append(importBase(StringUtil.appendStringNotNull(".",CONSUMER_URL,parentPack,consumerServiceName)));// 导入实体类
			sb.append(importJson());// 导入接口
			sb.append(importList());
			sb.append(importAutowired());
			sb.append(importModel());
			sb.append(importBeanUtil());
			sb.append(importRequest());
			sb.append(importErrorConfig());
			sb.append(importWebUtils());
			sb.append(importComponent());
			sb.append(importArrayList());
			sb.append(importMyBatisUtil());
			if (!cName.equals("SysUser")) {
				sb.append(importSysUser());
			}
			//注释
			sb.append(code(String.format(ANNOTATION, "参数验证,参数处理,缓存session、cookie"), 0, 0));
			sb.append(getComponent());
			sb.append(code(String.format("public class %sProxy {", cName),0,2));
			sb.append(getAutowired());
			sb.append(code(String.format("private %s service;", consumerServiceName), 1, 1));
//			sb.append(code(String.format("public void set%s(%s service) {%s%s%s}",consumerServiceName, consumerServiceName,RT_1+BLANK_2,"this.service=service;",RT_1+BLANK_1),1,1));
			if (pk!=null) {
				//getByPk
				sb.append(getComment("通过主键查询", true,pk));
				sb.append(code(String.format("public Json getByPk(HttpServletRequest request,%s %s) {",pkType,pk),1,1));
				sb.append(code(String.format("if(%s == null) {return ErrorConfig.getSystemParamErrorJson();};",pk),2,1));
				sb.append(code(String.format("return checkResult(service.getByPk(%s));",pk),2,1));
				sb.append(code(String.format("}"),1,1));
				//deleteByPk
				sb.append(getComment("通过主键删除", true,pk));
				sb.append(code(String.format("public Json deleteByPk(HttpServletRequest request,%s %s) {",pkType,pk),1,1));
				sb.append(code(String.format("if(%s == null) {return ErrorConfig.getSystemParamErrorJson();};",pk),2,1));
				sb.append(code(String.format("return checkResult(service.deleteByPk(%s));",pk),2,1));
				sb.append(code(String.format("}"),1,1));
				//deleteByBatch
				sb.append(getComment("通过主键批量删除", true,pk+"s"));
				sb.append(code(String.format("public Json deleteByBatch(HttpServletRequest request,%s... %ss) {",pkType,pk),1,1));
				sb.append(code(String.format("if(%ss == null) {return ErrorConfig.getSystemParamErrorJson();};",pk),2,1));
				sb.append(code(String.format("return checkResult(service.deleteByBatch(%ss));",pk),2,1));
				sb.append(code(String.format("}"),1,1));
				//enable
				sb.append(getComment("通过主键启用", true,pk));
				sb.append(code(String.format("public Json enable(HttpServletRequest request,%s %s) {",pkType,pk),1,1));
				sb.append(code(String.format("if(%s == null) {return ErrorConfig.getSystemParamErrorJson();};",pk),2,1));
				sb.append(code(String.format("SysUser user = WebUtils.getLoginUser(request);"),2,1));
				sb.append(code(String.format("return checkResult(service.enable(user.getPk(),%s));",pk),2,1));
				sb.append(code(String.format("}"),1,1));
				//disable
				sb.append(getComment("通过主键禁用", true,pk));
				sb.append(code(String.format("public Json disable(HttpServletRequest request,%s %s) {",pkType,pk),1,1));
				sb.append(code(String.format("if(%s == null) {return ErrorConfig.getSystemParamErrorJson();};",pk),2,1));
				sb.append(code(String.format("SysUser user = WebUtils.getLoginUser(request);"),2,1));
				sb.append(code(String.format("return checkResult(service.disable(user.getPk(),%s));",pk),2,1));
				sb.append(code(String.format("}"),1,1));
				//enables
				sb.append(getComment("通过主键批量启用", true,pk+"s"));
				sb.append(code(String.format("public Json enables(HttpServletRequest request,%s... %ss) {",pkType,pk),1,1));
				sb.append(code(String.format("if(%ss == null) {return ErrorConfig.getSystemParamErrorJson();};",pk),2,1));
				sb.append(code(String.format("SysUser user = WebUtils.getLoginUser(request);"),2,1));
				sb.append(code(String.format("return checkResult(service.enables(user.getPk(),%ss));",pk),2,1));
				sb.append(code(String.format("}"),1,1));
				//disables
				sb.append(getComment("通过主键批量禁用", true,pk+"s"));
				sb.append(code(String.format("public Json disables(HttpServletRequest request,%s... %ss) {",pkType,pk),1,1));
				sb.append(code(String.format("if(%ss == null) {return ErrorConfig.getSystemParamErrorJson();};",pk),2,1));
				sb.append(code(String.format("SysUser user = WebUtils.getLoginUser(request);"),2,1));
				sb.append(code(String.format("return checkResult(service.disables(user.getPk(),%ss));",pk),2,1));
				sb.append(code(String.format("}"),1,1));
				//insert
				sb.append(getComment("新增记录", true,"data"));
				sb.append(code(String.format("public Json insert(HttpServletRequest request,%s data) {",cName),1,1));
				sb.append(code(String.format("SysUser user = WebUtils.getLoginUser(request);"),2,1));
				sb.append(code(String.format("String checkRes = checkData(data,user);"),2,1));
				sb.append(code(String.format("if (checkRes != null) {"),2,1));
				sb.append(code(String.format("return new Json().setMsg(checkRes);"),3,1));
				sb.append(code(String.format("}"),2,1));
				sb.append(code(String.format("return checkResult(service.insert(data));"),2,1));
				sb.append(code(String.format("}"),1,1));
				//insertBatch
				sb.append(getComment("批量新增", true,"list"));
				sb.append(code(String.format("public Json insertBatch(HttpServletRequest request,List<%s> list) {",cName),1,1));
				sb.append(code(String.format("if(list == null) {return ErrorConfig.getSystemParamErrorJson();};"),2,1));
				sb.append(code(String.format("SysUser user = WebUtils.getLoginUser(request);"),2,1));
				sb.append(code(String.format("String checkResult = null;"),2,1));
				sb.append(code(String.format("for(%s data : list) {",cName),2,1));
				sb.append(code(String.format("checkResult = checkData(data,user);"),3,1));
				sb.append(code(String.format("if(checkResult != null){"),3,1));
				sb.append(code(String.format("return new Json().setMsg(checkResult);"),4,1));
				sb.append(code(String.format("}"),3,1));
				sb.append(code(String.format("}"),2,1));
				sb.append(code(String.format("return checkResult(service.insertBatch(list));",pk),2,1));
				sb.append(code(String.format("}"),1,1));
				//update
				sb.append(getComment("更新记录", true,"data"));
				sb.append(code(String.format("public Json update(HttpServletRequest request,%s data) {",cName),1,1));
				sb.append(code(String.format("SysUser user = WebUtils.getLoginUser(request);"),2,1));
				sb.append(code(String.format("String checkRes = checkData(data,user);"),2,1));
				sb.append(code(String.format("if (checkRes != null) {"),2,1));
				sb.append(code(String.format("return new Json().setMsg(checkRes);"),3,1));
				sb.append(code(String.format("}"),2,1));
				sb.append(code(String.format("return checkResult(service.update(data));"),2,1));
				sb.append(code(String.format("}"),1,1));
//				//findByPage
//				sb.append(getComment("分页查询", true,"queryInfo","pageNum","pageSize","orderBy"));
//				sb.append(code(String.format("public Json findByPage(HttpServletRequest request,%s queryInfo, int pageNum, int pageSize,String orderBy) {",cName),1,1));
//				sb.append(code(String.format("if (pageNum < 1 || pageSize < 1 ) {"),2,1));
//				sb.append(code(String.format("return ErrorConfig.getSystemErrorJson();"),3,1));
//				sb.append(code(String.format("}"),2,1));
//				sb.append(code(String.format("return checkResult(service.findByPage(queryInfo,pageNum,pageSize,orderBy));"),2,1));
//				sb.append(code(String.format("}"),1,1));
//				//findAll
//				sb.append(getComment("查询所有", true,"queryInfo","orderBy"));
//				sb.append(code(String.format("public Json findAll(HttpServletRequest request,%s queryInfo, String orderBy) {",cName),1,1));
//				sb.append(code(String.format("return checkResult(service.findAll(queryInfo,orderBy));"),2,1));
//				sb.append(code(String.format("}"),1,1));
				//findByPage
				sb.append(getComment("分页查询", true,"conditions","pageNum","pageSize","orderBy"));
				sb.append(code(String.format("public Json findByPage(HttpServletRequest request,%s queryInfo, int pageNum, int pageSize,String orderBy) {",cName),1,1));
				sb.append(code(String.format("if (pageNum < 1 || pageSize < 1 ) {"),2,1));
				sb.append(code(String.format("return ErrorConfig.getSystemErrorJson();"),3,1));
				sb.append(code(String.format("}"),2,1));
				sb.append(code("//queryInfo->conditions",2,1));
				sb.append(code("List<Model> conditions = getConditions(queryInfo,request);",2,1));
				sb.append(code(String.format("return checkResult(service.findByPage(conditions,pageNum,pageSize,orderBy));"),2,1));
				sb.append(code(String.format("}"),1,1));
				//findAll
				sb.append(getComment("查询所有", true,"conditions","orderBy"));
				sb.append(code(String.format("public Json findAll(HttpServletRequest request,%s queryInfo, String orderBy) {",cName),1,1));
				sb.append(code("//queryInfo->conditions",2,1));
				sb.append(code("List<Model> conditions = getConditions(queryInfo,request);",2,1));
				sb.append(code(String.format("return checkResult(service.findAll(conditions,orderBy));"),2,1));
				sb.append(code(String.format("}"),1,1));
				if (GENERATE_EXPORT) {
					//export
					sb.append(getComment("导出", true,"conditions","orderBy"));
					sb.append(code(String.format("public Json findByExport(HttpServletRequest request,%s queryInfo, String orderBy) {",cName),1,1));
					sb.append(code("//queryInfo->conditions",2,1));
					sb.append(code("List<Model> conditions = getConditions(queryInfo,request);",2,1));
					sb.append(code(String.format("return checkResult(service.findByExport(conditions,orderBy));"),2,1));
					sb.append(code(String.format("}"),1,1));
				}
				//checkData
				sb.append(getComment("数据校验及数据填充,如更新时间,更新人等", true,"data","user"));
				sb.append(code(String.format("private String checkData(%s data,SysUser user) {",cName),1,1));
				sb.append(code(String.format("//TODO 请完善数据校验及填充,如更新时间,更新人等"),2,1));
				sb.append(code(String.format("return BeanUtil.checkEntity(data,null,null);"),2,1));
				sb.append(code(String.format("}"),1,1));
				//checkResult
				sb.append(getComment("验证返回结果", true,"json"));
				sb.append(code(String.format("private Json checkResult(Json json) {"),1,1));
				sb.append(code(String.format("if(json == null){"),2,1));
				sb.append(code(String.format("return ErrorConfig.getSystemErrorJson();"),3,1));
				sb.append(code(String.format("}"),2,1));
				sb.append(code(String.format("return json;"),2,1));
				sb.append(code(String.format("}"),1,1));
				//转换Conditions
				//private List<Model> getConditions(GoodsComboLevel queryInfo, HttpServletRequest request) {
				sb.append(getComment("数据校验及数据填充", true,"json"));
				sb.append(code(String.format("private List<Model> getConditions(%s queryInfo,HttpServletRequest request) {",cName),1,1));
				sb.append(code(String.format("if(queryInfo == null){"),2,1));
				sb.append(code(String.format("return new ArrayList<Model>();"),3,1));
				sb.append(code(String.format("}"),2,1));
				sb.append(code(String.format("List<Model> conditions = MyBatisUtil.parseByObject(queryInfo, true);"),2,1));
				sb.append(code(String.format("return conditions;"),2,1));
				sb.append(code(String.format("}"),1,1));
				
			}
			sb.append(code("}",0,0));
			writeFile(f, sb);
			printLog(fileName);
		}
	}
	/**
	 * 创建业务暴露Controller
	 * 
	 * @param c
	 *            需要生成的modelBean
	 * @param parentPack
	 *            上级包名:若无传递"",否则传递目录,如"com.log"
	 * @throws Exception
	 */
	public static void createController(Class c, String parentPack) throws Exception {
		String cName = c.getSimpleName();
		String fileName = getFileName(CONTROLLER_PROJECT, CONTROLLER_URL, parentPack,cName, "Controller.java");
		String proxyName = cName+"Proxy";
		File f = new File(fileName);
		if (checkFile(f)) {
			//判断pk类型
			String[] pkTypes = getPkAndType(c);
			String pk = pkTypes[1];
			String pkType = pkTypes[0];
			//开始拼接类
			StringBuffer sb = new StringBuffer();
			sb.append(packages(StringUtil.appendStringNotNull(".", CONTROLLER_URL,parentPack)));
			sb.append(importBase(StringUtil.appendStringNotNull(".",BEAN_URL,parentPack,cName)));// 导入实体类
			sb.append(importBase(StringUtil.appendStringNotNull(".",PROXY_URL,parentPack,proxyName)));// 导入实体类
			sb.append(importJson());// 导入接口
			sb.append(importList());
			sb.append(importAutowired());
			sb.append(importRequest());
			sb.append(importRequestMapping());
			sb.append(importController());
			if (GENERATE_EXPORT) {
				sb.append(importExport(cName));
			}
			//注释
			sb.append(code(String.format(ANNOTATION, "业务暴露,请将不需要的方法删除"), 0, 0));
			sb.append(getController());
			sb.append(getRequestMappingClass(getPropertieName(cName).trim()));
			sb.append(code(String.format("public class %sController {", cName),0,2));
			sb.append(getAutowired());
			sb.append(code(String.format("private %s proxy;", proxyName), 1, 1));
//			sb.append(code(String.format("public void set%s(%s service) {%s%s%s}",consumerServiceName, consumerServiceName,RT_1+BLANK_2,"this.service=service;",RT_1+BLANK_1),1,1));
			if (pk!=null) {
				//getByPk
				sb.append(getComment("通过主键查询", true,pk));
				sb.append(getRequestMapping("get"));
				sb.append(code(String.format("public Json getByPk(HttpServletRequest request,%s %s) {",pkType,pk),1,1));
				sb.append(code(String.format("return proxy.getByPk(request,%s);",pk),2,1));
				sb.append(code(String.format("}"),1,1));
				//deleteByPk
				sb.append(getComment("通过主键删除", true,pk));
				sb.append(getRequestMapping("delete"));
				sb.append(code(String.format("public Json deleteByPk(HttpServletRequest request,%s %s) {",pkType,pk),1,1));
				sb.append(code(String.format("return proxy.deleteByPk(request,%s);",pk),2,1));
				sb.append(code(String.format("}"),1,1));
				//deleteByBatch
				sb.append(getComment("通过主键批量删除", true,pk+"s"));
				sb.append(getRequestMapping("deleteByBatch"));
				sb.append(code(String.format("public Json deleteByBatch(HttpServletRequest request,@RequestParam(\"%ss[]\")%s[] %ss) {",pk,pkType,pk),1,1));
				sb.append(code(String.format("return proxy.deleteByBatch(request,%ss);",pk),2,1));
				sb.append(code(String.format("}"),1,1));
				//enable
				sb.append(getComment("通过主键启用", true,pk));
				sb.append(getRequestMapping("enable"));
				sb.append(code(String.format("public Json enable(HttpServletRequest request,%s %s) {",pkType,pk),1,1));
				sb.append(code(String.format("return proxy.enable(request,%s);",pk),2,1));
				sb.append(code(String.format("}"),1,1));
				//disable
				sb.append(getComment("通过主键禁用", true,pk));
				sb.append(getRequestMapping("disable"));
				sb.append(code(String.format("public Json disable(HttpServletRequest request,%s %s) {",pkType,pk),1,1));
				sb.append(code(String.format("return proxy.disable(request,%s);",pk),2,1));
				sb.append(code(String.format("}"),1,1));
				//enables
				sb.append(getComment("通过主键批量启用", true,pk+"s"));
				sb.append(getRequestMapping("enables"));
				sb.append(code(String.format("public Json enables(HttpServletRequest request,%s... %ss) {",pkType,pk),1,1));
				sb.append(code(String.format("return proxy.enables(request,%ss);",pk),2,1));
				sb.append(code(String.format("}"),1,1));
				//disables
				sb.append(getComment("通过主键批量禁用", true,pk+"s"));
				sb.append(getRequestMapping("disables"));
				sb.append(code(String.format("public Json disables(HttpServletRequest request,%s... %ss) {",pkType,pk),1,1));
				sb.append(code(String.format("return proxy.disables(request,%ss);",pk),2,1));
				sb.append(code(String.format("}"),1,1));
				//insert
				sb.append(getComment("新增记录", true,"data"));
				sb.append(getRequestMapping("insert"));
				sb.append(code(String.format("public Json insert(HttpServletRequest request,%s data) {",cName),1,1));
				sb.append(code(String.format("if (data.getPk()==null) {"),2,1));
				sb.append(code(String.format("return proxy.insert(request,data);"),3,1));
				sb.append(code(String.format("}"),2,1));
				sb.append(code(String.format("return proxy.update(request,data);"),2,1));
				sb.append(code(String.format("}"),1,1));
				//insertBatch
				sb.append(getComment("批量新增", true,"list"));
				sb.append(getRequestMapping("insertBatch"));
				sb.append(code(String.format("public Json insertBatch(HttpServletRequest request,List<%s> list) {",cName),1,1));
				sb.append(code(String.format("return proxy.insertBatch(request,list);"),2,1));
				sb.append(code(String.format("}"),1,1));
				//update
				sb.append(getComment("更新记录", true,"data"));
				sb.append(getRequestMapping("update"));
				sb.append(code(String.format("public Json update(HttpServletRequest request,%s data) {",cName),1,1));
				sb.append(code(String.format("return proxy.update(request,data);"),2,1));
				sb.append(code(String.format("}"),1,1));
				//findByPage
				sb.append(getComment("分页查询", true,"queryInfo","pageNum","pageSize","orderBy"));
				sb.append(getRequestMapping("findByPage"));
				sb.append(code(String.format("public Json findByPage(HttpServletRequest request,%s queryInfo, int pageNum, int pageSize,String orderBy) {",cName),1,1));
				sb.append(code(String.format("return proxy.findByPage(request,queryInfo,pageNum,pageSize,orderBy);"),2,1));
				sb.append(code(String.format("}"),1,1));
				//findAll
				sb.append(getComment("查询所有", true,"queryInfo","orderBy"));
				sb.append(getRequestMapping("findAll"));
				sb.append(code(String.format("public Json findAll(HttpServletRequest request,%s queryInfo, String orderBy) {",cName),1,1));
				sb.append(code(String.format("return proxy.findAll(request,queryInfo,orderBy);"),2,1));
				sb.append(code(String.format("}"),1,1));
				if (GENERATE_EXPORT) {
					//export
					sb.append(getComment("导出", true,"queryInfo","orderBy"));
					sb.append(getRequestMapping("export"));
					sb.append(code(String.format("public void export(HttpServletRequest request,HttpServletResponse response,%s queryInfo, String orderBy) {",cName),1,1));
					sb.append(code(String.format("Json ret = proxy.findByExport(request,queryInfo,orderBy);"),2,1));
					sb.append(code(String.format("ExportUtil.export(ret, queryInfo, response, %sExcelExport.class);",cName),2,1));
					sb.append(code(String.format("}"),1,1));
				}
			}
			sb.append(code("}",0,0));
			writeFile(f, sb);
			printLog(fileName);
		}
	}
	/**
	 * 创建bean的Action类
	 * 
	 * @param c
	 *            需要生成的modelBean
	 * @param parentPack
	 *            上级包名:若无传递"",否则传递目录,如"com.log"
	 * @throws Exception
	 */
	public static void createBeanAction(Class c, String parentPack) throws Exception {
		String cName = c.getSimpleName();
		String fileName = getFileName(CONTROLLER_PROJECT, CONTROLLER_URL, parentPack,c.getSimpleName(), "Constroller.java");
		File f = new File(fileName);
		if (checkFile(f)) {
			StringBuffer sb = new StringBuffer();
			sb.append(packages(CONTROLLER_URL + parentPack));
			sb.append(importBase(c.getName()));// 导入实体类
			sb.append("import org.apache.struts2.convention.annotation.Action;" + RT_1);
			sb.append("import org.apache.struts2.convention.annotation.Namespace;" + RT_1);
			sb.append("import org.springframework.beans.factory.annotation.Autowired;" + RT_1);
			sb.append("import " + SERVICE_URL + parentPack + "." + cName + "ServiceI;" + RT_1);// 导入接口
			sb.append("import " + BASE_ACTION_URL + "." + BASE_ACTION_NAME + ";");// 导入BaseAction
			sb.append(code(String.format(ANNOTATION, ""), 0, 0));// 注释
			sb.append(RT_1 + "@Namespace(\"/" + f.getParentFile().getName() + "\")");
			sb.append(RT_1 + "@Action" + RT_1);
			sb.append("public class " + cName + "Action ");
			sb.append("extends " + BASE_ACTION_NAME + "<" + cName + "> ");
			sb.append("{" + RT_2);
			sb.append(BLANK_1 + "/**");
			sb.append(RT_1 + BLANK_1 + " * 注入业务逻辑，使当前action调用service.xxx的时候，直接是调用基础业务逻辑");
			sb.append(RT_1 + BLANK_1 + " * ");
			sb.append(RT_1 + BLANK_1 + " * 如果想调用自己特有的服务方法时，请使用((TServiceI) service).methodName()这种形式强转类型调用");
			sb.append(RT_1 + BLANK_1 + " * ");
			sb.append(RT_1 + BLANK_1 + " * @param service");
			sb.append(RT_1 + BLANK_1 + " */");
			sb.append(RT_1 + BLANK_1 + "@Autowired");
			sb.append(RT_1 + BLANK_1 + "public void setService(" + cName + "ServiceI service) {");
			sb.append(RT_1 + BLANK_2 + "this.service = service;");
			sb.append(RT_1 + BLANK_1 + "}");
			sb.append(RT_2 + "}");
			writeFile(f, sb);
			printLog(fileName);
		}
	}
	/**
	 * 创建bean的导出
	 * 
	 * @param c
	 *            需要生成的modelBean
	 * @param parentPack
	 *            上级包名:若无传递"",否则传递目录,如"com.log"
	 * @throws Exception
	 */
	public static void createExport(Class c, String parentPack) throws Exception {
		String cName = c.getSimpleName();
		String fileName = getFileName(SERVICE_PROJECT, EXPORT_URL, "excel.export.ext",cName, "ExcelExport.java");
		File f = new File(fileName);
		if (checkFile(f)) {
			StringBuffer sb = new StringBuffer();
			sb.append(packages(StringUtil.appendStringNotNull(".", EXPORT_URL,"excel.export.ext")));
			sb.append(importBase(StringUtil.appendStringNotNull(".", BEAN_URL,parentPack,cName)));// 导入实体类
			sb.append(importBase(StringUtil.appendStringNotNull(".", EXPORT_URL,"excel.export",BASE_EXPORT_NAME)));// 导入接口
			sb.append(importList());// 导入List
			sb.append(code(String.format(ANNOTATION, "导出"), 0, 0));
			sb.append(code(String.format("public class %sExcelExport extends %s<%s> {", cName, BASE_EXPORT_NAME,cName),0,1));
			sb.append(code(String.format("public %sExcelExport(List<%s> data, %s conditions) {",cName,cName,cName),1,1));
			sb.append(code(String.format("super(data, conditions);"),2,1));
			sb.append(code(String.format("}"),1,1));
			sb.append(getOverride());
			sb.append(code(String.format("protected Object formatValue(%s t, String key) {",cName),1,1));
			sb.append(code(String.format("switch (key) {"),2,1));
			sb.append(code(String.format("case \"field\":"),2,1));
			sb.append(code(String.format("return \"fieldName\";"),3,1));
			sb.append(code(String.format("}"),2,1));
			sb.append(code(String.format("return null;"),2,1));
			sb.append(code(String.format("}"),1,1));
			sb.append(getOverride());
			sb.append(code(String.format("protected String formatCondition(%s t) {",cName),1,1));
			sb.append(code(String.format("StringBuffer sb = new StringBuffer();"),2,1));
			sb.append(code(String.format("if (t.getField()!=null) {"),2,1));
			sb.append(code(String.format("sb.append(\"FieldName:\");"),3,1));
			sb.append(code(String.format("sb.append(t.getField());"),3,1));
			sb.append(code(String.format("sb.append(BANK);"),3,1));
			sb.append(code(String.format("}"),2,1));
			sb.append(code(String.format("return sb.toString();"),2,1));
			sb.append(code(String.format("}"),1,1));
			sb.append(getOverride());
			sb.append(code(String.format("protected String getReportName() {"),1,1));
			sb.append(code(String.format("return \"ExcelTitle\";"),2,1));
			sb.append(code(String.format("}"),1,1));
			sb.append(code("}",0,0));
			writeFile(f, sb);
			printLog(fileName);
		}
	}
	/**
	 * 创建bean的枚举信息
	 * 
	 * @param c
	 *            需要生成的modelBean
	 * @param parentPack
	 *            上级包名:若无传递"",否则传递目录,如"com.log"
	 * @throws Exception
	 */
	public static void createBeanEnum(String cName, String parentPack) throws Exception {
		System.out.print(cName+"(\"\",\"\"),");
	}

	/**
	 * 生成bean的dubbo配置service代码
	 * @param cName
	 * @param parentPack
	 */
	private static void createBeanDubboConfig(String cName, String parentPack) {
		String interfaze = SERVICE_URL + parentPack + "." + cName + "Service";
		String config = "<dubbo:reference id=\""+cName+"Service\" interface=\""+interfaze+"\" timeout=\"6000\" check=\"false\" />";
		System.out.println(config);
	}

	/**
	 * 验证文件及目录
	 * @param f
	 * @return
	 */
	private static boolean checkFile(File f){
		if (!WRITE_FILE_FLAG) {
			return true;
		}
		//文件不存|强制生成
		if (!f.isFile() || FORCE_FLAG) {
			File pf = f.getParentFile();
			if (!pf.isDirectory()) {
				if (!pf.isFile()) {
					pf.mkdirs();
				}else{
					System.out.println("存在同目录名文件,请删除"+pf.getPath());
					return false;
				}
			}
			return true;
		}
		return false;
	}
	/**
	 * 获取项目文件名
	 * @param project 项目常量
	 * @param pkg 包目录
	 * @param parentPack 上级名称
	 * @param suffix 后缀
	 * @param cName 
	 * @return
	 */
	private static String getFileName(String project,String pkg,String parentPack, String cName,String suffix){
		String fileName = StringUtil.appendStringNotNull(File.separator, getProjectPath(project),BEAN_BASE_PATH,pkg.replace(".", "/"),parentPack.replace(".", "/"),cName+suffix);
		return fileName;
	}
	/**
	 * 获取属性名
	 * @param cName 如
	 * LogLogin -> logLogin
	 * @return
	 */
	private static String getPropertieName(String cName){
		return " "+Character.toLowerCase(cName.charAt(0))+cName.substring(1);
	}
	/**
	 * 获取属性名
	 * @param cName 如
	 * LogLogin -> logLogin
	 * @return
	 */
	private static String getGetOneUpper(String propertie){
		return Character.toUpperCase(propertie.charAt(0))+propertie.substring(1);
	}
	
	/**
	 * 获取主键及类型
	 * @param c
	 * @return [type,pk]
	 */
	private static String[] getPkAndType(Class c){
		String[] pks = new String[2];
		Field f = ReflectionUtil.findField(c, "pk");
		if (f!=null) {
			pks[1] = "pk";
			pks[0] = f.getType().getSimpleName();
		}
//		if (ReflectionUtil.findField(c, "pk")!=null) {
//			pks[1] = "pk";
//			pks[0] = "String ";
//		}else if(ReflectionUtil.findField(c, "id")!=null){
//			pks[1] = "id";
//			pks[0] = "Long ";
//		}
		return pks;
	}
}