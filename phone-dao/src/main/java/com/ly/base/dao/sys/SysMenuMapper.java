package com.ly.base.dao.sys;

import com.ly.base.core.model.sys.SysMenu;
import com.ly.base.dao.BaseMapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 数据库处理
 * @author LeiYong
 * @date 2016年10月02日
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

	/**
	 * 查询名称是否存在
	 * @param name
	 * @return
	 */
	SysMenu selectByName(String name);
	
	/**
	 * 查询用户的菜单
	 * @param rolePk
	 * @param menuType 
	 * @return
	 */
	List<SysMenu> findAllMenuByRole(@Param("rolePk")Integer rolePk,@Param("menuType")String menuType);
	/**
	 * 查询用户的功能按钮归组为String
	 * 如:[{01:show,edit,del},{02:show,edit,del}]
	 * @param rolePk
	 * @param menuType //取消参数,避免只有访问权限的情况
	 * @return
	 */
	List<SysMenu> findAllMenuStringByRole(@Param("rolePk")Integer rolePk);

	/**
	 * 通过主键查询
	 * 
	 * @param pk
	 * @return
	 */ 
	SysMenu selectByPrimaryKey(@Param("pk") String pk);

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

}