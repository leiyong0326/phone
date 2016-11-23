package com.ly.base.common.model;

import java.util.List;
/**
 * 返回结果集
 * @author LeiYong
 *
 */
public class Grid {
	private int total;
	private List<Object> rows;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<Object> getRows() {
		return rows;
	}
	public void setRows(List<Object> rows) {
		this.rows = rows;
	}
	
	
}
