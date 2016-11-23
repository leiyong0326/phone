package com.ly.base.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ly.base.common.model.PropertiesModel;
import com.ly.base.common.system.SystemConfig;

/**
 * properties缓存工具类
 * @author Ray
 *
 */
public class PropertiesCacheUtil {
	private static Logger logger = LoggerFactory.getLogger(PropertiesCacheUtil.class);
	private static final String BASEPATH = PropertiesCacheUtil.class.getResource("/").getFile();
	private static final String USERCONFIG = "userConfig.properties";
	private static Map<String, PropertiesModel> propertiesMap = new HashMap<String, PropertiesModel>();
	
	/**
	 * 加载项目中的配置文件
	 * @param propertiesFilePath
	 * @return
	 */
	public static PropertiesModel loadProjectProperties(String propertiesFilePath) {
		String path = StringUtil.appendStringByObject(null,FileUtil.checkDirectory(BASEPATH), propertiesFilePath);
		return loadProperties(path);
	}
	/**
	 * 加载一个properties,如果文件不存在返回null
	 * 
	 * @param propertiesFilePath
	 * @return XNProperties
	 */
	public static PropertiesModel loadProperties(String propertiesFilePath) {
		if (logger.isDebugEnabled()) {
			logger.debug("loadProperties(String) - start"); //$NON-NLS-1$
		}

		File file = new File(propertiesFilePath);
		if (!file.isFile()) {
			logger.warn("loadProperties#file is not found:"+propertiesFilePath);
			return null;
		}
		try {
			//匹配文件是否做过修改
			Long date = file.lastModified();
			if (propertiesMap.get(propertiesFilePath) == null
					|| !propertiesMap.get(propertiesFilePath).equalsVision(
							date)) {
//				BufferedReader fis = new BufferedReader(new    InputStreamReader(is));  
				Reader fis = new InputStreamReader(new FileInputStream(propertiesFilePath),SystemConfig.CHARSET);
//				BufferedReader fis = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				Properties properties = new Properties();
				properties.load(fis);
				fis.close();
				String charsetFrom = properties.getProperty("charsetFrom", SystemConfig.CHARSET);
				String charsetTo = properties.getProperty("charsetTo", SystemConfig.CHARSET);
				PropertiesModel xnProperties = new PropertiesModel(date,
						properties, charsetFrom, charsetTo);
				propertiesMap.put(propertiesFilePath, xnProperties);
			}
		} catch (IOException e) {
			logger.error("loadProperties#",e);
		}
		PropertiesModel returnUtilModelProperties = propertiesMap.get(propertiesFilePath);
		if (logger.isDebugEnabled()) {
			logger.debug("loadProperties(String) - end"); //$NON-NLS-1$
		}
		return returnUtilModelProperties;
	}
	
	/**
	 * 临时缓存
	 * @param propertiesFilePath
	 * @param utilModelProperties
	 */
	public static void setProperties(String propertiesFilePath,PropertiesModel utilModelProperties){
		if (logger.isDebugEnabled()) {
			logger.debug("setProperties(String, UtilModelProperties) - start"); //$NON-NLS-1$
		}

		if (propertiesFilePath!=null&&utilModelProperties!=null) {
			propertiesMap.put(propertiesFilePath, utilModelProperties);
		}else{
			logger.warn("setProperties#file is not found or utilModelProperties is null");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("setProperties(String, UtilModelProperties) - end"); //$NON-NLS-1$
		}
	}

	public static String getBasepath()
	{
		return BASEPATH;
	}

	public static String getUserconfig() {
		return USERCONFIG;
	}

}