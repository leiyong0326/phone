package com.base.generate.pub;



import com.base.generate.BaseGenerate;
import com.ly.base.common.util.StringUtil;

public class ImportUtil extends BaseGenerate{

	/**
	 * ###get###
	 */
	public static String getOverride(){
		return code("@Override", 1, 1);
	}
	public static String getAutowired(){
		return code("@Autowired", 1, 1);
	}
	public static String getRequestMapping(String value){
		StringBuffer sb = new StringBuffer();
		sb.append(code(String.format("@RequestMapping(value = \"/%s\", method = RequestMethod.POST)", value), 1, 1));
		sb.append(code(String.format("@ResponseBody", value), 1, 1));
		return sb.toString();
	}
	public static String getDubboService(){
		return code("@Service(version = \"1.0\"", 0, 1);
	}
	public static String getSpringService(){
		return code("@Service", 0, 1);
	}
	public static String getController(){
		return code("@Controller", 0, 1);
	}
	public static String getComponent(){
		return code("@Component", 0, 1);
	}
	public static String getRequestMappingClass(String value){
		return code(String.format("@RequestMapping(value = \"/%s\")", value), 0, 1);
	}
	public static String getLogger(String logEnum,String logOperateEnum,String title){
		return code(String.format("@Logger(model = LogEnum.%s, type = LogOperateEnum.%s, title = \"%s\")", logEnum,logOperateEnum,title), 1, 1);
	}
	/**
	 * ###import###
	 */
	public static String importAutowired(){
		return importBase("org.springframework.beans.factory.annotation.Autowired");
	}
	public static String importMybatisParam(){
		return importBase("org.apache.ibatis.annotations.Param");
	}
	public static String importSpringService(){
		return importBase("org.springframework.stereotype.Service");
	}
	public static String importDubboService(){
		return importBase("com.alibaba.dubbo.config.annotation.Service");
	}
	public static String importComponent(){
		return importBase("org.springframework.stereotype.Component");
	}
	public static String importController(){
		return importBase("org.springframework.stereotype.Controller");
	}
	public static String importRequestMapping(){
		StringBuffer sb = new StringBuffer();
		sb.append(importBase("org.springframework.web.bind.annotation.RequestMapping"));
		sb.append(importBase("org.springframework.web.bind.annotation.RequestMethod"));
		sb.append(importBase("org.springframework.web.bind.annotation.ResponseBody"));
		sb.append(importBase("org.springframework.web.bind.annotation.RequestParam"));
		return sb.toString();
	}
	public static String importList(){
		return importBase("java.util.List");
	}
	public static String importArrayList(){
		return importBase("java.util.ArrayList");
	}
	public static String importMap(){
		return importBase("java.util.Map");
	}
	public static String importHashMap(){
		return importBase("java.util.HashMap");
	}
	public static String importMyBatisUtil(){
		return importBase("com.ly.base.common.util.MyBatisUtil");
	}
	public static String importBeanUtil(){
		return importBase("com.ly.base.common.util.BeanUtil");
	}
	public static String importDateUtil(){
		return importBase("com.ly.base.common.util.DateUtil");
	}
	public static String importJson(){
		return importBase("com.ly.base.common.model.Json");
	}
	public static String importModel(){
		return importBase("com.ly.base.common.model.Model");
	}
	public static String importErrorConfig(){
		return importBase("com.ly.base.common.system.ErrorConfig");
	}
	public static String importPage(){
		return importBase("com.github.pagehelper.Page");
	}
	public static String importSysUser(){
		return importBase("com.ly.base.core.model.sys.SysUser");
	}
	public static String importWebUtils(){
		return importBase("com.ly.base.core.util.WebUtils");
	}
	public static String importRequest(){
		return importBase("javax.servlet.http.HttpServletRequest");
	}
	public static String importResponse(){
		return importBase("javax.servlet.http.HttpServletResponse");
	}
	public static String importLogger(){
		StringBuffer sb = new StringBuffer();
		sb.append(importBase("com.ly.base.common.annotation.Logger"));
		sb.append(importBase("com.ly.base.common.em.ext.LogEnum"));
		sb.append(importBase("com.ly.base.common.em.ext.LogOperateEnum"));
		return sb.toString();
	}
	public static String importExport(String className){
		return importBase("com.ly.base.core.excel.export.ext."+className+"ExcelExport");
	}
	public static String importExportUtil(){
		return importBase("com.ly.base.core.util.excel.ExportUtil");
	}

	/**
	 * ###base###
	 */
	public static String importBase(String beanUrl){
		return code(StringUtil.appendStringNotNull(null,  "import ", beanUrl , ";") , 0,1);
	}
	public static String packages(String packages){
		return code(StringUtil.appendStringNotNull(null,  "package ", packages , ";"), 0,2);
	}
	public static String code(String code,int t,int rn){
		return StringUtil.appendString(null,"", StringUtil.getNString(BLANK_1, t),code,StringUtil.getNString(RT_1, rn));
	}
	public static String getComment(String title,boolean isReturn ,String... params){
		StringBuffer sb = new StringBuffer();
		sb.append(code("/**", 1, 1));
		sb.append(code(" * "+title, 1, 1));
		sb.append(code(" * ", 1, 1));
		if (params!=null) {
			for (String p : params) {
				sb.append(code(" * @param "+p,1,1));
			}
		}
		if (isReturn) {
			sb.append(code(" * @return",1,1));
		}
		sb.append(code(" */ ",1,1));
		return sb.toString();
	}
}
