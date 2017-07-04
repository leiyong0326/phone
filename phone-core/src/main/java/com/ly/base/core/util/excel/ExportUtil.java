package com.ly.base.core.util.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ly.base.common.model.Json;
import com.ly.base.common.system.SystemConfig;
import com.ly.base.common.util.ReflectionUtil;
import com.ly.base.core.excel.export.ExcelExportSuper;

public class ExportUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(ExportUtil.class);

	@SuppressWarnings("unchecked")
	public static<T> void export(Json ret,T model,HttpServletResponse response,Class<? extends ExcelExportSuper<T>> clazz){
		if (logger.isDebugEnabled()) {
			logger.debug("export(String, Json, T, HttpServletResponse, Class<? extends ExcelExportSuper<T>>) - start"); //$NON-NLS-1$
		}

		OutputStream fOut = null;
		try {
			fOut = response.getOutputStream();
			if (ret.isSuccess()) {
				List<T> list = ReflectionUtil.convertObjectToBean(ret.getObj(), List.class);
				ExcelExportSuper<?> schedule = clazz.getConstructor(List.class,model.getClass()).newInstance(list,model);
				String codedFileName = java.net.URLEncoder.encode(schedule.getExportName(), "UTF-8"); 
				HSSFWorkbook workBook = schedule.dataToWorkbook(null, null);
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
				workBook.write(fOut);
			}else{
				response.setCharacterEncoding(SystemConfig.CHARSET);
				response.setContentType("text/html;charset=utf-8");
				fOut.write(ret.getMsg().getBytes());
			}
		} catch (IOException e) {
			//往往是因为连接已断开,不做处理
//			if (fOut!=null) {
//				try {
//					ret.setMsg("导出异常");
//					fOut.write(ret.toJsonString().getBytes());
//				} catch (IOException e1) {
//					logger.warn("export(String, Json, T, HttpServletResponse, Class<? extends ExcelExportSuper<T>>) - exception ignored", e1); //$NON-NLS-1$
//				}
//			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e){
			//构造函数或对象没找到
			logger.error("导出异常",e);
		}finally {
			if (fOut!=null) {
				try {
					fOut.flush();
					fOut.close();
				} catch (IOException e) {
					logger.error("export(String, Json, T, HttpServletResponse, Class<? extends ExcelExportSuper<T>>) - exception ignored", e); //$NON-NLS-1$
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("export(String, Json, T, HttpServletResponse, Class<? extends ExcelExportSuper<T>>) - end"); //$NON-NLS-1$
		}
	}
}
