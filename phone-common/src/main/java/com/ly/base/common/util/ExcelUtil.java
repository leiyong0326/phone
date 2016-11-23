package com.ly.base.common.util;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ly.base.common.model.PropertiesModel;
/**
 * Excel报表使用工具类
 * @author Ray
 *
 */
public class ExcelUtil {
    private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
    private static String reportPath;
    //private static BufferedInputStream ib;
    private static BufferedOutputStream ob;
    public static final String EXCEL_ORDER_EXPORT_CONFIG = "excelOrderExportConfig.properties";
    public static final String EXCEL_ORDER_IMPORT_CONFIG = "excelOrderImportConfig.properties";
    
    /**
     * 根据workbook生成excel文件,目录为:reportPath + filePath + fileName
     * 
     * @param workbook
     * @param filePath
     * @param fileName
     *            ,需要带后缀
     * @return true/false
     */
    public static boolean generateExcel4WeebWork(HSSFWorkbook workbook,
            String filePath, String fileName) {
		if (logger.isDebugEnabled()) {
			logger.debug("generateExcel4WeebWork(HSSFWorkbook, String, String) - start"); //$NON-NLS-1$
		}

        if (filePath == null || fileName == null) {
            logger.warn("文件路径/文件名为空为空");
        } else if (reportPath == null) {
        	logger.warn("reportPath==null");
        }
        String targetFilePath = reportPath + filePath + fileName;
        try {
            File directory = new File(reportPath + filePath);
            if (!directory.isDirectory()) {
                if (!directory.mkdirs()) {
                	logger.warn("Craete Directory Error:"+directory);
                    return false;
                }
            }
            File file = new File(targetFilePath);
            if (file.exists()) {
                if (!file.delete()) {
                    logger.warn("Delete File Error:"+targetFilePath);
                    return false;
                }
            }
            ob = new BufferedOutputStream(new FileOutputStream(targetFilePath));
            workbook.write(ob);

			if (logger.isDebugEnabled()) {
				logger.debug("generateExcel4WeebWork(HSSFWorkbook, String, String) - end"); //$NON-NLS-1$
			}
            return true;
        } catch (FileNotFoundException e) {
			logger.error("generateExcel4WeebWork(HSSFWorkbook, String, String)", e); //$NON-NLS-1$

            e.printStackTrace();
        } catch (IOException e) {
			logger.error("generateExcel4WeebWork(HSSFWorkbook, String, String)", e); //$NON-NLS-1$

            e.printStackTrace();
        } finally {
            try {
                ob.flush();
                ob.close();
            } catch (IOException e) {
                logger.error("generateExcel4WeebWork",e);
            }
        }

		if (logger.isDebugEnabled()) {
			logger.debug("generateExcel4WeebWork(HSSFWorkbook, String, String) - end"); //$NON-NLS-1$
		}
        return false;
    }
 
    /**
     * 将文件转为workbook对象
     * @param file excel文件,仅支持xls
     * @param noCheckFileType 是否验证文件类型,true为不验证
     * @return HSSFWorkbook
     * @throws Exception
     */
    public static HSSFWorkbook getWorkbookByFile(File file,boolean noCheckFileType) throws Exception{
		if (logger.isDebugEnabled()) {
			logger.debug("getWorkbookByFile(File, boolean) - start"); //$NON-NLS-1$
		}

    	HSSFWorkbook workbook = null;
    	if (file!=null&&file.isFile()) {
			if (noCheckFileType||file.getName().endsWith(".xls")||file.getName().endsWith(".xlsx")) {
				try {
					InputStream is = new BufferedInputStream(new FileInputStream(file));
					workbook = new HSSFWorkbook(is);
				} catch (FileNotFoundException e) {
					logger.error("getWorkbookByFile(File)", e); //$NON-NLS-1$
				} catch (IOException e) {
					logger.error("getWorkbookByFile(File)", e); //$NON-NLS-1$
				}
			}else{
				throw new Exception("文件类型错误,应为xls文件");
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getWorkbookByFile(File, boolean) - end"); //$NON-NLS-1$
		}
		return workbook;
    }
    /**
     * workbook转数据对象
     * @param workbook excel表格对象
     * @param rowNumber 开始行
     * @return List
     */
    public static JSONArray workbookToData(String propertiesPath,String key,String sheetName,HSSFWorkbook workbook,int rowNumber){
		if (logger.isDebugEnabled()) {
			logger.debug("workbookToData(String, String, String, HSSFWorkbook, int) - start"); //$NON-NLS-1$
		}

    	JSONArray array = new JSONArray();
		//将写入excel的数据不能为空
		if (workbook!=null) {
			//读取properties配置文件
			PropertiesModel properties = PropertiesCacheUtil.loadProperties(propertiesPath);
			if (properties!=null) {
				//从properties中获取导出对应的配置json
				String columnJsonString = properties.get(key);
				try {
					//创建一个sheet
					HSSFSheet sheet = null;
					if (StringUtils.isNotBlank(sheetName)) {
						sheet = workbook.getSheet(sheetName);
					}
					//如果未获取到sheet则获取第一个sheet
					if (sheet==null) {
						sheet = workbook.getSheetAt(0);
					}
					if (sheet!=null) {
						//定义sheet的行码,并保存列信息
						//List<String> columnList = new ArrayList<String>();
						JSONObject columnJson = JSONObject.parseObject(columnJsonString);
						//读取每列对应的列号通过标题
						Map<Integer, String> map = readSheetHead(sheet, columnJson, rowNumber++);
						//读取内容
						rowNumber = readSheetBody(sheet, map, rowNumber,array);
					}else{
						logger.error("workbookToData#sheet is null");
					}
					
				} catch (Exception e) {
					logger.error("workbookToData#parse json error",e);
				}
				
			}else{
				logger.warn("workbookToData#properties is null");
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("workbookToData(String, String, String, HSSFWorkbook, int) - end"); //$NON-NLS-1$
		}
    	return array;
    }

	/**
     * 记录每一列对应的column
     * @param sheet
     * @param columnJson
     * @param rowNumber
     * @return
     */
	private static Map<Integer, String> readSheetHead(HSSFSheet sheet, JSONObject columnJson, int rowNumber) {
		if (logger.isDebugEnabled()) {
			logger.debug("readSheetHead(HSSFSheet, JSONObject, int) - start"); //$NON-NLS-1$
		}

		Map<Integer, String> map = new Hashtable<Integer, String>();
		if(sheet!=null){
			HSSFRow row = sheet.getRow(rowNumber++);
			if (row!=null) {
				Iterator<Cell> iterator = row.cellIterator();
				while (iterator.hasNext()) {
					Cell cell = iterator.next();
					if (cell!=null) {
						String cellValue = cell.getStringCellValue();
						if (StringUtils.isNotBlank(cellValue) && columnJson.containsKey(cellValue)) {
							map.put(cell.getColumnIndex(), columnJson.getString(cellValue));// 记录cell每一列对应的columnName
						}
					}
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("readSheetHead(HSSFSheet, JSONObject, int) - end"); //$NON-NLS-1$
		}
		return map;
	}
	/**
	 * 读取excel到JsonArray
	 * @param sheet
	 * @param map 列对应的column
	 * @param rowNumber
	 * @param array
	 * @return 当前的rowNumber
	 */
    private static int readSheetBody(HSSFSheet sheet, Map<Integer, String> map, int rowNumber, JSONArray array) {
		if (logger.isDebugEnabled()) {
			logger.debug("readSheetBody(HSSFSheet, Map<Integer,String>, int, JSONArray) - start"); //$NON-NLS-1$
		}

    	if(sheet!=null){
			int end = sheet.getLastRowNum();//获取最后一行
			for (; rowNumber<=end;rowNumber++) {
				HSSFRow row = sheet.getRow(rowNumber);
				if (row!=null) {
					JSONObject jsonObject = new JSONObject();
					Iterator<Cell> iterator = row.cellIterator();
					while (iterator.hasNext()) {
						Cell cell = iterator.next();
						if (cell!=null) {
							int cellIndex = cell.getColumnIndex();
							String key = map.get(cellIndex);
							String cellValue = getStringValue(cell);
							if (key!=null&&cellValue!=null&&!cellValue.equals("null")) {
								readSheetCell(jsonObject,key,cellValue);
							}
							
						}
					}
					array.add(jsonObject);
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("readSheetBody(HSSFSheet, Map<Integer,String>, int, JSONArray) - end"); //$NON-NLS-1$
		}
		return rowNumber;
	}
    
	public static String getStringValue(Cell cell) {
		switch (cell.getCellTypeEnum()) {
		case BOOLEAN:
			return cell.getBooleanCellValue() ? "1" : "0";
		case FORMULA:
			return cell.getCellFormula();
		case NUMERIC:
			cell.setCellType(CellType.STRING);
			return cell.getStringCellValue();
		case STRING:
			return cell.getStringCellValue();
		default:
			return "";
		}

	}
    /**
     * 将cellValue写入JsonObject对应的key
     * @param jsonObject
     * @param key
     * @param cellValue
     */
    private static void readSheetCell(JSONObject jsonObject, String key, String cellValue){
		if (logger.isDebugEnabled()) {
			logger.debug("readSheetCell(JSONObject, String, String) - start"); //$NON-NLS-1$
		}

    	if (key.indexOf("#")>0) {
			String prefix = key.substring(0,key.indexOf("#"));
			String suffix = key.substring(key.indexOf("#")+1);
			JSONObject jo = null;
			//如果
			if (!jsonObject.containsKey(prefix)) {
				jo = jsonObject.getJSONObject(prefix);
			}else{
				jo = new JSONObject();
			}
			readSheetCell(jo, suffix, cellValue);
			jsonObject.put(prefix, jo);
		}else{
			jsonObject.put(key, cellValue==null?"null":cellValue);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("readSheetCell(JSONObject, String, String) - end"); //$NON-NLS-1$
		}
    }
    /**
     * 将数据表对象转换为workbook
     * @param key 配置文件key
     * @param sheetName sheet页名称,不填默认为自动导出
     * @param ts 数据集合
     * @param rowNumber 开始行
     * @return HSSFWorkbook
     */
    public static <T> HSSFWorkbook dataToWorkbook(String propertiesPath,String key,String sheetName,List<T> ts,int rowNumber){
    	logger.info("dataToWorkbook#key="+key);
    	HSSFWorkbook workbook = new HSSFWorkbook();
    	if (StringUtils.isNotEmpty(key)) {
    		//将写入excel的数据不能为空
    		if (ts!=null&&!ts.isEmpty()) {
				//读取properties配置文件
    			PropertiesModel properties = PropertiesCacheUtil.loadProperties(propertiesPath);
    			
    			if (properties!=null) {
					//从properties中获取导出对应的配置json
    				String columnJsonString = properties.get(key);
    				try {
    					//创建一个sheet
    					sheetName=(sheetName==null?"自动导出":sheetName);
    					HSSFSheet sheet = workbook.createSheet(sheetName);
    					//定义sheet的行码,并保存列信息
    					//List<String> columnList = new ArrayList<String>();
						JSONObject columnJson = JSONObject.parseObject(columnJsonString);
						//写入标题
    					writeSheetHead(sheet, columnJson, rowNumber++);
    					//写入内容
    					rowNumber = writeSheetBody(sheet, columnJson, rowNumber,ts);
    					
    					autoSheetWidth(sheet, columnJson);
    					
					} catch (Exception e) {
						logger.error("dataToWorkbook#parse json error",e);
					}
    				
				}else{
					logger.warn("dataToWorkbook#properties is null");
				}
			}else{
				logger.warn("dataToWorkbook#ts is null");
			}
		}else{
			logger.warn("dataToWorkbook#key is null");
		}
    	
    	logger.info("dataToWorkbook#end.");
    	return workbook;
    }
    /**
     * 初始化表头
     * @param sheet
     * @param columnJson
     * @param rowNumber
     */
    private static void writeSheetHead(HSSFSheet sheet,JSONObject columnJson,int rowNumber){
		if (logger.isDebugEnabled()) {
			logger.debug("writeSheetHead(HSSFSheet, JSONObject, int) - start"); //$NON-NLS-1$
		}

		Set<String> keySet = columnJson.keySet();
		int cellNumber = 0;
		HSSFRow row = sheet.createRow(rowNumber);
		for (String k : keySet) {//k:GOODS_NO
			String name = columnJson.getString(k);//品项编码
			sheet.autoSizeColumn(cellNumber);
			HSSFCell cell = row.createCell(cellNumber++);
			cell.setCellValue(name);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("writeSheetHead(HSSFSheet, JSONObject, int) - end"); //$NON-NLS-1$
		}
    }
    /**
     * 初始化sheet的内容
     * @param sheet
     * @param columnJson
     * @param rowNumber
     * @param ts 数据
     * @return 当前操作的行号
     */
	private static <T>int writeSheetBody(HSSFSheet sheet,JSONObject columnJson,int rowNumber,List<T> ts){
		if (logger.isDebugEnabled()) {
			logger.debug("writeSheetBody(HSSFSheet, JSONObject, int, List<T>) - start"); //$NON-NLS-1$
		}

		Set<String> keySet = columnJson.keySet();
		for (T t:ts) {
//			Class cls = t.getClass();
			int cellNumber = 0;//将cellNumber从0开始
			HSSFRow row = sheet.createRow(rowNumber++);//创建新的一行
			for(String key:keySet){
				try {
					HSSFCell cell = row.createCell(cellNumber++);
					Object value = getValueByKey(t, key);
					if (value==null) {
						cell.setCellValue("null");
					}else{
//						if (org.springframework.beans.BeanUtils..isNotCollection(value)) {
							cell.setCellValue(value.toString());
//						}else{
//							logger.warn("initSheetBody#is not collection:"+key);
//						}
					}
				} catch (NoSuchFieldException e) {
					logger.error("initSheetBody",e);
				} catch (SecurityException e) {
					logger.error("initSheetBody",e);
				} catch (IllegalAccessException e) {
					logger.error("initSheetBody",e);
				} catch (IllegalArgumentException e) {
					logger.error("initSheetBody",e);
				} catch (InvocationTargetException e) {
					logger.error("initSheetBody",e);
				} catch (IntrospectionException e) {
					logger.error("initSheetBody",e);
				}
			}
			
			/**
			Field[] fields = cls.getDeclaredFields();
			for(Field field:fields){
				PropertyDescriptor pd = new PropertyDescriptor(field.getName(), cls);
				//getMethod
				Method rm = pd.getReadMethod();
				if (!Modifier.isPublic(rm.getDeclaringClass().getModifiers())) {
					rm.setAccessible(true);
				}
				//获取值
				Object value = rm.invoke(t);
				//如果值为集合则不处理
				if(!BeanUtils.isCollection(value)){
					
				}
			}
			*/
		}

		if (logger.isDebugEnabled()) {
			logger.debug("writeSheetBody(HSSFSheet, JSONObject, int, List<T>) - end"); //$NON-NLS-1$
		}
		return rowNumber;
    }
    /**
     * 获取对象的值
     * @param t
     * @param key
     * @return
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @SuppressWarnings("rawtypes")
	private static <T>Object getValueByKey(T t,String key) throws NoSuchFieldException, SecurityException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		if (logger.isDebugEnabled()) {//t==PurOrderD,k==GOODS_NO
				//t:{"GOODS_NO":"0230100","GOODS_BARCODE":"1231238174921"}
			logger.debug("getValueByKey(T, String) - start"); //$NON-NLS-1$
		}

    	if(t instanceof Map){
			Object returnObject = ((Map) t).get(key);
			if (logger.isDebugEnabled()) {
				logger.debug("getValueByKey(T, String) - end"); //$NON-NLS-1$
			}
    		return returnObject;
    	}else{
    		Class cls = t.getClass();
//        	String k = null;
//        	if(key.indexOf("#")>0){
//    			k = key.substring(0,key.indexOf("#"));
//    			key = key.substring(key.indexOf("#")+1);
//    		}else{
//    			k=key;
//    			key=null;
//    		}
    		//t==PurOrderD,k==GOODS_NO
			//t:{"GOODS_NO":"0230100","GOODS_BARCODE":"1231238174921"} 
    		Field f = cls.getDeclaredField(key);//"GOODS_NO":"0230100"
    		PropertyDescriptor pd = new PropertyDescriptor(f.getName(), cls);//"GOODS_NO":"0230100"
    		Method rm = pd.getReadMethod();//类似getGoodsNo
    		Object value = rm.invoke(t);//执行getGoodsNo返回"0230100"
//    		if (key!=null&&key.length()>0&&value!=null) {
//    			return getValueByKey(value, key);
//    		}

			if (logger.isDebugEnabled()) {
				logger.debug("getValueByKey(T, String) - end"); //$NON-NLS-1$
			}
    		return value;
    	}
    }
    /**
     * 初始化表单的列宽
     * @param sheet
     * @param columnJson
     */
    private static void autoSheetWidth(HSSFSheet sheet,JSONObject columnJson){
		if (logger.isDebugEnabled()) {
			logger.debug("autoSheetWidth(HSSFSheet, JSONObject) - start"); //$NON-NLS-1$
		}

		Set<String> keySet = columnJson.keySet();
		for (int i = 0;i<keySet.size();i++) {
			sheet.autoSizeColumn(i);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("autoSheetWidth(HSSFSheet, JSONObject) - end"); //$NON-NLS-1$
		}
    }
}