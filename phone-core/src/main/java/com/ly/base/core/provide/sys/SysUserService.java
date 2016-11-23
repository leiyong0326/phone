package com.ly.base.core.provide.sys;

import java.math.BigDecimal;

import com.ly.base.core.model.sys.SysUser;
import com.ly.base.core.provide.BaseService;

/**
 * 业务处理
 * @author LeiYong
 * @date 2016年10月02日
 */
public interface SysUserService extends BaseService<SysUser> {
	/**
	 * 通过主键查询
	 * 
	 * @param pk
	 * @return
	 */ 
	public SysUser selectByPrimaryKey(String pk);
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
	 * @param updateBy
	 * @param pks
	 * @return
	 */ 
	public int deleteByBatch(String[] pks);
	/**
	 * 通过主键启用
	 * 
	 * @param updateBy
	 * @param pk
	 * @return
	 */ 
	public int enable(String updateBy,String pk);
	/**
	 * 通过主键禁用
	 * 
	 * @param updateBy
	 * @param pk
	 * @return
	 */ 
	public int disable(String updateBy,String pk);
	/**
	 * 通过主键批量启用
	 * 
	 * @param updateBy
	 * @param pks
	 * @return
	 */ 
	public int enables(String updateBy,String... pks);
	/**
	 * 通过主键批量禁用
	 * @param updateBy
	 * @param pks
	 * @return
	 */ 
	public int disables(String updateBy,String... pks);
	/**
	 * 通过主键修改密码
	 * @param updateBy
	 * @param pk
	 * @param password(密文)
	 * @return
	 */
	public int changePwd(String updateBy, String pk, String password);
	/**
	 * 登录
	 * @param userName
	 * @param pwd(密文)
	 * @return
	 */
	public SysUser loginSystem(String userName, String password);
	int checkLoginName(String userName);
	/**
	 * 添加积分
	 * @param ratio
	 * @param pk
	 * @return
	 */
	public int addRatio(BigDecimal ratio, String pk);
	/**
	 * 更新积分,销量,积分
	 */
	public int updateScore(SysUser su);
}