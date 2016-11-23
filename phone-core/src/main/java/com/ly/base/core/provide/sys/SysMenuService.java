package com.ly.base.core.provide.sys;

import java.util.List;

import com.ly.base.core.model.sys.SysMenu;
import com.ly.base.core.provide.BaseService;

/**
 * 业务处理
 * @author LeiYong
 * @date 2016年10月02日
 */
public interface SysMenuService extends BaseService<SysMenu> {
	/**
	 * 通过主键查询
	 * 
	 * @param pk
	 * @return
	 */ 
	public SysMenu selectByPrimaryKey(String pk);
	/**
	 * 通过主键删除
	 * 
	 * @param pk
	 * @return
	 */ 
	public int deleteByPrimaryKey(String pk);
	/**
	 * 通过主键批量删除
	 * 
	 * @param pks
	 * @return
	 */ 
	public int deleteByBatch(String[] pks);
	/**
	 * 通过主键启用
	 * 
	 * @param pk
	 * @return
	 */ 
	public int enable(String updateBy,String pk);
	/**
	 * 通过主键禁用
	 * 
	 * @param pk
	 * @return
	 */ 
	public int disable(String updateBy,String pk);
	/**
	 * 通过主键批量启用
	 * 
	 * @param pks
	 * @return
	 */ 
	public int enables(String updateBy,String... pks);
	/**
	 * 通过主键批量禁用
	 * 
	 * @param pks
	 * @return
	 */ 
	public int disables(String updateBy,String... pks);
	/**
	 * 查询用户的菜单
	 * @param rolePk
	 * @param menuType 
	 * @return
	 */
	public List<SysMenu> findAllMenuByRole(Integer rolePk, String menuType);
	/**
	 * 查询用户的菜单
	 * @param rolePk
	 * @return
	 */
	List<SysMenu> findAllMenuByRole(Integer rolePk);
	/**
	 * 查询用户的功能
	 * @param rolePk
	 * @return
	 */
	List<SysMenu> findAllFuncByRole(Integer rolePk);
	/**
	 * 查询用户的功能按钮归组为String
	 * 如:[{01:show,edit,del},{02:show,edit,del}]
	 * @param rolePk
	 * @param menuType //取消参数,避免只有访问权限的情况
	 * @return
	 */
	List<SysMenu> findAllFuncStringByRole(Integer rolePk);
}