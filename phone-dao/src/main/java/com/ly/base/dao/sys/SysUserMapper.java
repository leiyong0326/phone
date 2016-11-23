package com.ly.base.dao.sys;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.ly.base.core.model.sys.SysUser;
import com.ly.base.dao.BaseMapper;

/**
 * 数据库处理
 * @author LeiYong
 * @date 2016年10月02日
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

	/**
	 * 通过主键查询
	 * 
	 * @param pk
	 * @return
	 */ 
	SysUser selectByPrimaryKey(@Param("pk") String pk);

	/**
	 * 通过主键删除
	 * 
	 * @param pk
	 * @return
	 */ 
	int deleteByPrimaryKey(@Param("pk") String pk);

	/**
	 * 通过主键批量删除
	 * 
	 * @param pks
	 * @return
	 */ 
	int deleteByBatch(@Param("pks") String[] pks);

	/**
	 * 通过主键更新单列
	 * 
	 * @param pk
	 * @return
	 */ 
	int updateState(@Param("updateBy") String updateBy,@Param("column") String column, @Param("status") String status,@Param("pk") String pk);

	/**
	 * 通过主键批量更新单列
	 * 
	 * @param pks
	 * @return
	 */ 
	int updateStates(@Param("updateBy") String updateBy,@Param("column") String column, @Param("status") String status,@Param("pks") String... pks);

	int updatePassword(@Param("updateBy")String updateBy,@Param("password") String password, @Param("updatePassword")Date updatePassword,@Param("pk") String pk);

	/**
	 * 更新通过主键
	 * @param record
	 * @return
	 */
	int updateScore(SysUser record);

}