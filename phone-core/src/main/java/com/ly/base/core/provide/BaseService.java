package com.ly.base.core.provide;

import java.util.List;

import com.ly.base.common.model.Model;
import com.github.pagehelper.Page;


public interface BaseService<T> {

	/**
	 * 插入数据
	 * @param data
	 * @return
	 */
	T insertSelective(T data);
	/**
	 * 批量插入
	 * @param list
	 * @return
	 */
	int insertBatch(List<T> list);
	
	/**
	 * 更新对象
	 * @param data
	 * @return
	 */
	int updateByPK(T data);

	/**
	 * 查询分页通过条件
	 * @param queryInfo
	 * @param pageNum
	 * @param pageSize
	 * @param order
	 * @return
	 */
	Page<T> findByPage(T queryInfo,int pageNum,int pageSize,String orderBy);
	/**
	 * 查询所有通过条件
	 * @param queryInfo
	 * @param order
	 * @return
	 */
	Page<T> findAll(T queryInfo,String orderBy);
	/**
	 * 查询分页通过条件
	 * @param queryInfo
	 * @param pageNum
	 * @param pageSize
	 * @param order
	 * @return
	 */
	Page<T> findByPage(List<Model> conditions,int pageNum,int pageSize,String order);
	/**
	 * 查询所有通过条件
	 * @param queryInfo
	 * @param order
	 * @return
	 */
	Page<T> findAll(List<Model> conditions,String order);
}
