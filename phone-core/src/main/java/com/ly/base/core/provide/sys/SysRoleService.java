package com.ly.base.core.provide.sys;

import java.util.List;

import com.ly.base.core.model.sys.SysRole;
import com.ly.base.core.provide.BaseService;

/**
 * 业务处理
 * @author LeiYong
 * @date 2016年10月02日
 */
public interface SysRoleService extends BaseService<SysRole> {
	/**
	 * 通过主键查询
	 * 
	 * @param pk
	 * @return
	 */ 
	public SysRole selectByPrimaryKey(Integer pk);
	/**
	 * 通过主键删除
	 * 
	 * @param pk
	 * @return
	 */ 
	public int deleteByPrimaryKey(Integer pk);
	/**
	 * 通过主键批量删除
	 * 
	 * @param pks
	 * @return
	 */ 
	public int deleteByBatch(Integer[] pks);
	/**
	 * 通过主键启用
	 * 
	 * @param pk
	 * @return
	 */ 
	public int enable(String updateBy,Integer pk);
	/**
	 * 通过主键禁用
	 * 
	 * @param pk
	 * @return
	 */ 
	public int disable(String updateBy,Integer pk);
	/**
	 * 通过主键批量启用
	 * 
	 * @param pks
	 * @return
	 */ 
	public int enables(String updateBy,Integer... pks);
	/**
	 * 通过主键批量禁用
	 * 
	 * @param pks
	 * @return
	 */ 
	public int disables(String updateBy,Integer... pks);
	/**
	 * 为角色批量插入菜单权限
	 * @param list 菜单列表
	 * @param rolePk
	 */
	public int grantRole(List<String> list, Integer rolePk);
}