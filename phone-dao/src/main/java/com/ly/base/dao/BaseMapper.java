package com.ly.base.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ly.base.common.model.Model;
import com.github.pagehelper.Page;

public interface BaseMapper<T> {

	Page<T> selectAllByCondition(@Param("queryInfo") T queryInfo, @Param("order") String order);

	/**
	 * 根据自定义条件进行查询
	 * 
	 * @param conditions
	 *            where条件
	 * @param columns
	 *            要查询的列
	 * @param order
	 *            排序列,如"name",反序可写为"name desc"
	 * @return
	 */
	Page<T> selectExtend(@Param("conditions") List<Model> conditions, @Param("columns") List<String> columns,
			@Param("order") String order);
	/**
	 * 单笔插入
	 * @param record
	 * @return
	 */
	int insertSelective(T record);
	/**
	 * 批量插入
	 * @param list
	 * @return
	 */
	int insertBatch(List<T> list);

	/**
	 * 更新通过主键
	 * @param record
	 * @return
	 */
	int updateByPrimaryKey(T record);


}
