package com.ly.base.core.excel.export;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ly.base.common.model.PropertiesModel;
import com.ly.base.common.util.DateUtil;
import com.ly.base.common.util.PropertiesCacheUtil;
import com.ly.base.common.util.ReflectionUtil;
import com.ly.base.common.util.StringUtil;

public abstract class ExcelExportSuper<T> {
	private List<T> data;
	private T conditions;
	private LinkedHashMap<String, Object> columnJson;
	private Map<String, Integer> columnMaxValue;
	private int rowNumber;
	protected static final String UNDEFINED = "未知";
	protected static final String YES = "是";
	protected static final String NO = "否";
	protected static final String NO_PAY = "未支付";
	protected static final String PAY = "已支付";
	protected static final String BANK = "    ";
	protected static final String ENABLED = "启用";
	protected static final String DISABLED = "禁用";
	protected static final String NORMAL = "正常";
	protected static final String MAN = "男";
	protected static final String WOMAN = "女";
	
	private static final int MAX_CELL_WIDTH = 100;
	private static final Charset CHARSET = Charset.forName("gbk");
	
	public ExcelExportSuper(List<T> data,T conditions) {
		super();
		this.data = data;
		this.conditions = conditions;
		columnMaxValue = new HashMap<>();
	}
	/**
	 * 获取properties对应的key
	 * @return
	 */
	protected String getPropertiesKey(){
		return ReflectionUtil.getGenericSuperclass(this.getClass()).getSimpleName();
	}
	/**
	 * 获取properties文件路径
	 * @return
	 */
	protected String getExportProperties(){
		return "properties/excelExport.properties";
	}
	/**
	 * 将数据写入workbook
	 * @param workbook 不传递则创建一个workbook
	 * @param sheetName 为null则默认为"自动导出"
	 * @return
	 */
	public HSSFWorkbook dataToWorkbook(HSSFWorkbook workbook, String sheetName) {
		if (init()) {
			HSSFWorkbook wbook = workbook == null ? new HSSFWorkbook() : workbook;
			// 创建一个sheet
			sheetName = (sheetName == null ? "自动导出" : sheetName);
			HSSFSheet sheet = wbook.createSheet(sheetName);
			//写入表头条件
			writeCondtions(sheet);
			// 写入标题
			writeHead(sheet);
			// 写入内容
			writeBody(sheet);
			autoSheetWidth(sheet);
			completeAfter(sheet);
			return wbook;
		}
		return null;
	}
	/**
	 * 格式化condition
	 * @param t
	 * @return
	 */
	protected abstract String formatCondition(T t);
	protected abstract String getReportName();
	public String getExportName(){
		return StringUtil.appendStringNotNull(null, getReportName(),DateUtil.format("yyyy-MM-dd", Calendar.getInstance()));
	}
	/**
	 * 在excel表格生成完成后执行的方法
	 * 默认是锁定表头
	 * @param sheet
	 */
	protected void completeAfter(HSSFSheet sheet){
		sheet.createFreezePane(3, 2);
	}
	/**
	 * 初始化
	 * @return
	 */
	boolean init(){
		setRowNumber(0);
		String propertiesPath = getExportProperties();
		PropertiesModel properties = PropertiesCacheUtil.loadProjectProperties(propertiesPath);
		if (properties!=null) {
			//从properties中获取导出对应的配置json
			String columnJsonString = properties.get(getPropertiesKey());
			columnJson = JSONObject.parseObject(columnJsonString,new TypeReference<LinkedHashMap<String, Object>>(){});
//			LinkedHashMap<String, String> jsonMap2 = JSONObject.parseObject(columnJsonString);;
			return true;
		}
		return false;
	}
	/**
	 * 表头条件
	 * @param sheet
	 * @param t
	 * @param cellCount
	 * @return
	 */
	void writeCondtions(HSSFSheet sheet){
		T t = getConditions();
		if (t!=null) {
			HSSFRow row = sheet.createRow(getRowNumber());
			row.setHeight((short) 500);
			CellRangeAddress cra = new CellRangeAddress(getRowNumber(), getRowNumber(), 0, getColumnJson().size());
			sheet.addMergedRegion(cra);
			HSSFCell cell = row.createCell(0);
			HSSFCellStyle style = cell.getCellStyle();
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			style.setWrapText(true);
			cell.setCellStyle(style);
			setCellValue(cell, formatCondition(t));
			addRowNumber();
		}
	}
	/**
	 * 写入表头
	 * @param sheet
	 */
	void writeHead(HSSFSheet sheet){
		LinkedHashMap<String, Object> columnJson = getColumnJson();
		Set<String> keySet = columnJson.keySet();
		int cellNumber = 0;
		HSSFRow row = sheet.createRow(addRowNumber());
		for (String k : keySet) {
			Object name = columnJson.get(k);//品项编码
			sheet.autoSizeColumn(cellNumber);
			HSSFCell cell = row.createCell(cellNumber++);
			setCellValue(cell, name);
			pubMaxValue(k,name);
		}
	}
	void writeBody(HSSFSheet sheet){
		Set<String> keySet = getColumnJson().keySet();
		List<T> ts = getData();
		for (T t:ts) {
//			Class cls = t.getClass();
			int cellNumber = 0;//将cellNumber从0开始
			HSSFRow row = sheet.createRow(addRowNumber());//创建新的一行
			for(String key:keySet){
				try {
					HSSFCell cell = row.createCell(cellNumber++);
					Object value = getValueByKey(t, key);
					setCellValue(cell, value);
					pubMaxValue(key, value);
				} catch (Exception e) {
					throw new RuntimeException("writeBody", e);
				}
			}
		}
	}
	/**
	 * 为单位格设置值
	 * @param cell
	 * @param value
	 */
	void setCellValue(HSSFCell cell,Object value){
		if (value==null) {
			cell.setCellValue(getNullValue());
		}else{
//			if (org.springframework.beans.BeanUtils..isNotCollection(value)) {
				cell.setCellValue(value.toString());
//			}else{
//				logger.warn("initSheetBody#is not collection:"+key);
//			}
		}
	}
	/**
	 * 为单位格设置值
	 * @param cell
	 * @param value
	 */
	private void pubMaxValue(String key,Object value){
		Integer maxSize = columnMaxValue.get(key);
		Integer valueSize = 0;
		if (value==null) {
			valueSize = getValueSize(null);
		}else{
			valueSize = getValueSize(value.toString());
		}
		//缓存最大长度
		if (maxSize==null||valueSize>maxSize) {
			if (valueSize>MAX_CELL_WIDTH) {
				columnMaxValue.put(key, MAX_CELL_WIDTH);
			}else{
				columnMaxValue.put(key, valueSize);
			}
		}
	}
	/**
	 * 空值时默认使用"null"
	 * @return
	 */
	String getNullValue(){
		return "无";
	}
	Object getValueByKey(T t,String key) throws NoSuchFieldException, SecurityException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Object formatValue = formatValue(t, key);
		if (formatValue!=null) {
			return formatValue;
		}
		if(t instanceof Map){
			@SuppressWarnings("rawtypes")
			Object returnObject = ((Map) t).get(key);
    		return returnObject;
    	}else{
    		PropertyDescriptor pd = ReflectionUtil.getPropertyDescriptor(key, t);
    		Method rm = pd.getReadMethod();
    		return rm.invoke(t);
    	}
    }
	protected abstract Object formatValue(T t,String key);
	/**
	 * 自动重置列宽
	 * @param sheet
	 * @param columnJson
	 */
	void autoSheetWidth(HSSFSheet sheet){
		Set<String> keySet = getColumnJson().keySet();
		int i = 0;
		for (String key : keySet) {
			Integer width = columnMaxValue.get(key);
			if (width!=null&&width>0) {
				width+=6;
				width*=256;
				sheet.setColumnWidth(i, width);
			}
			i++;
			
		}
    }
	protected List<T> getData() {
		return data;
	}
	protected LinkedHashMap<String, Object> getColumnJson() {
		return columnJson;
	}
	protected int getRowNumber() {
		return rowNumber;
	}
	protected int addRowNumber() {
		return rowNumber++;
	}
	protected void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}
	protected T getConditions() {
		return conditions;
	}
	
	private static int getValueSize(String value){
		return StringUtils.isEmpty(value)?2:value.getBytes(CHARSET).length;
	}
}
