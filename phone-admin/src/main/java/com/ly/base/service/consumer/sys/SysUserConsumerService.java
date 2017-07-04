package com.ly.base.service.consumer.sys;

import com.ly.base.core.model.sys.SysUser;
import com.ly.base.common.model.Json;

import java.math.BigDecimal;
import java.util.List;
import com.ly.base.common.model.Model;

/**
 * 日志记录,处理返回结果,缓存特殊数据
 * @author LeiYong
 * @date 2016年10月04日
 */
public interface SysUserConsumerService {

	/**
	 * 通过主键查询
	 * 
	 * @param pk
	 * @return
	 */ 
	public Json getByPk(String pk);
	/**
	 * 通过主键删除
	 * 
	 * @param pk
	 * @return
	 */ 
	public Json deleteByPk(String pk);
	/**
	 * 通过主键批量删除
	 * 
	 * @param pks
	 * @return
	 */ 
	public Json deleteByBatch(String... pks);
	/**
	 * 通过主键启用
	 * 
	 * @param pk
	 * @return
	 */ 
	public Json enable(String updateBy,String pk);
	/**
	 * 通过主键禁用
	 * 
	 * @param pk
	 * @return
	 */ 
	public Json disable(String updateBy,String pk);
	/**
	 * 通过主键批量启用
	 * 
	 * @param pks
	 * @return
	 */ 
	public Json enables(String updateBy,String... pks);
	/**
	 * 通过主键批量禁用
	 * 
	 * @param pks
	 * @return
	 */ 
	public Json disables(String updateBy,String... pks);
	/**
	 * 新增记录
	 * 
	 * @param data
	 * @return
	 */ 
	public Json insert(SysUser data);
	/**
	 * 批量新增
	 * 
	 * @param list
	 * @return
	 */ 
	public Json insertBatch(List<SysUser> list);
	/**
	 * 更新记录
	 * 
	 * @param data
	 * @return
	 */ 
	public Json update(SysUser data);
	/**
	 * 分页查询
	 * 
	 * @param queryInfo
	 * @param pageNum
	 * @param pageSize
	 * @param orderBy
	 * @return
	 */ 
	public Json findByPage(SysUser queryInfo, int pageNum, int pageSize,String orderBy);
	/**
	 * 查询所有
	 * 
	 * @param queryInfo
	 * @param orderBy
	 * @return
	 */ 
	public Json findAll(SysUser queryInfo, String orderBy);
	/**
	 * 分页查询
	 * 
	 * @param conditions
	 * @param pageNum
	 * @param pageSize
	 * @param orderBy
	 * @return
	 */ 
	public Json findByPage(List<Model> conditions, int pageNum, int pageSize, String order);
	/**
	 * 查询所有
	 * 
	 * @param conditions
	 * @param orderBy
	 * @return
	 */ 
	public Json findAll(List<Model> conditions, String orderBy);
	/**
	 * 导出
	 * 
	 * @param conditions
	 * @param orderBy
	 * @return
	 */ 
	public Json findByExport(List<Model> conditions, String orderBy);
	/**
	 * 登录
	 * 
	 * @param userName
	 * @param password(明文)
	 * @return
	 */ 
	public Json loginSystem(String userName, String password);
	/**
	 * 修改密码
	 * 
	 * @param updateBy
	 * @param pk
	 * @param password(明文)
	 * @return
	 */ 
	public Json changePwd(String updateBy, String pk, String password);

	/**
	 * 验证登录名
	 * 不记录日志
	 * @param userName
	 * @return
	 */ 
	public Json checkLoginName(String userName);
	/**
	 * 添加积分
	 * @param ratio
	 * @param pk
	 * @return
	 */
	public Json addRatio(BigDecimal ratio, String pk);
}