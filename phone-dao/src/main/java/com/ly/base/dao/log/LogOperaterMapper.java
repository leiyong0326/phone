package com.ly.base.dao.log;

import com.ly.base.core.model.log.LogOperater;
import com.ly.base.dao.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 数据库处理
 * @author LeiYong
 * @date 2016年10月02日
 */
public interface LogOperaterMapper extends BaseMapper<LogOperater> {

	/**
	 * 通过主键查询
	 * 
	 * @param pk
	 * @return
	 */ 
	LogOperater selectByPrimaryKey(@Param("pk") Integer pk);

	/**
	 * 通过主键删除
	 * 
	 * @param pk
	 * @return
	 */ 
	int deleteByPrimaryKey(@Param("pk") Integer pk);

	/**
	 * 通过主键批量删除
	 * 
	 * @param pks
	 * @return
	 */ 
	int deleteByBatch(@Param("pks") Integer[] pks);

	/**
	 * 通过主键更新单列
	 * 
	 * @param pk
	 * @return
	 */ 
	int updateState(@Param("updateBy") String updateBy,@Param("column") String column, @Param("status") String status,@Param("pk") Integer pk);

	/**
	 * 通过主键批量更新单列
	 * 
	 * @param pks
	 * @return
	 */ 
	int updateStates(@Param("updateBy") String updateBy,@Param("column") String column, @Param("status") String status,@Param("pks") Integer... pks);

}