package com.ly.base.dao.sys;

import com.ly.base.core.model.sys.SysRole;
import com.ly.base.dao.BaseMapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 数据库处理
 * @author LeiYong
 * @date 2016年10月02日
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

	/**
	 * 通过主键查询
	 * 
	 * @param pk
	 * @return
	 */ 
	SysRole selectByPrimaryKey(@Param("pk") Integer pk);

	/**
	 * 为角色批量插入菜单权限
	 * @param list 菜单列表
	 * @param rolePk
	 * @throws SQLException
	 */
	public int grantRole(@Param("list")List<String> list,@Param("rolePk") Integer rolePk);
	/**
	 * 为角色删除菜单权限
	 * @param rolePk
	 * @throws SQLException
	 */
	public int deleteGrantRole(@Param("rolePk") Integer rolePk);
	
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