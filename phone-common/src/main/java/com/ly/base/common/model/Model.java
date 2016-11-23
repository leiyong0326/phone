package com.ly.base.common.model;

import java.util.List;

/**
 * 操作类
 * 考虑添加一组columns列来自定义查询的列
 * @author LeiYong
 *
 */
public class Model {
	private String column;
	private String operate;
	private Object value;
	private List<Object> values;
	
	public Model(String column, String operate, List<Object> values) {
		super();
		this.column = column;
		this.operate = operate;
		this.values = values;
	}

	public Model(String column, String operate, Object value) {
		super();
		this.column = column;
		this.operate = operate;
		this.value = value;
	}
	
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(column);
		sb.append(operate);
		sb.append(value==null?values.toString():value);
		return sb.toString();
	}
	
}
