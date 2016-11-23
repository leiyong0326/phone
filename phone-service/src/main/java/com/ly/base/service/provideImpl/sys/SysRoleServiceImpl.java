package com.ly.base.service.provideImpl.sys;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ly.base.core.model.sys.SysRole;
import com.ly.base.core.provide.sys.SysRoleService;
import com.ly.base.dao.sys.SysRoleMapper;
import com.ly.base.service.provideImpl.BaseServiceImpl;

/**
 * 业务处理,数据缓存
 * @author LeiYong
 * @date 2016年10月02日
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRole> implements SysRoleService {

	@Autowired
	public void setBaseMapper(SysRoleMapper baseMapper) {
		super.setBaseMapper(baseMapper);
	}
	public SysRoleMapper getBaseMapper() {
		return (SysRoleMapper) super.getBaseMapper();
	}
	/**
	 * 通过主键查询
	 * 
	 * @param pk
	 * @return
	 */ 
	@Override
	public SysRole selectByPrimaryKey(Integer pk){
		return getBaseMapper().selectByPrimaryKey(pk);
	}
	/**
	 * 为角色批量插入菜单权限
	 * @param list 菜单列表
	 * @param rolePk
	 * @throws SQLException
	 */
	@Override	
	public int grantRole(List<String> list,Integer rolePk) {
		getBaseMapper().deleteGrantRole(rolePk);
		if (list.size()>0) {
			int result = getBaseMapper().grantRole(list, rolePk);
			if(result!=list.size()){
				return -1;
			}
			return result;
		}
		return 0;
	};
	
	/**
	 * 通过主键删除
	 * 
	 * @param pk
	 * @return
	 */ 
	@Override
	public int deleteByPrimaryKey(Integer pk){
		return getBaseMapper().deleteByPrimaryKey(pk);
	}
	/**
	 * 通过主键批量删除
	 * 
	 * @param pks
	 * @return
	 */ 
	@Override
	public int deleteByBatch(Integer[] pks){
		return getBaseMapper().deleteByBatch(pks);
	}
	/**
	 * 通过主键启用
	 * 
	 * @param pk
	 * @return
	 */ 
	@Override
	public int enable(String updateBy,Integer pk){
		return getBaseMapper().updateState(updateBy,"enable","1",pk);
	}
	/**
	 * 通过主键禁用
	 * 
	 * @param pk
	 * @return
	 */ 
	@Override
	public int disable(String updateBy,Integer pk){
		return getBaseMapper().updateState(updateBy,"enable","0",pk);
	}
	/**
	 * 通过主键批量启用
	 * 
	 * @param pks
	 * @return
	 */ 
	@Override
	public int enables(String updateBy,Integer... pks){
		return getBaseMapper().updateStates(updateBy,"enable","1",pks);
	}
	/**
	 * 通过主键批量禁用
	 * 
	 * @param pks
	 * @return
	 */ 
	@Override
	public int disables(String updateBy,Integer... pks){
		return getBaseMapper().updateStates(updateBy,"enable","0",pks);
	}
}