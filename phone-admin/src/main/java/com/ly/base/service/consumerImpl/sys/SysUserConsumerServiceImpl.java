package com.ly.base.service.consumerImpl.sys;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.Page;
import com.ly.base.common.annotation.Logger;
import com.ly.base.common.em.ext.LogEnum;
import com.ly.base.common.em.ext.LogOperateEnum;
import com.ly.base.common.model.Json;
import com.ly.base.common.model.Model;
import com.ly.base.common.system.ErrorConfig;
import com.ly.base.common.util.SpringBeanUtil;
import com.ly.base.core.model.sys.SysAccount;
import com.ly.base.core.model.sys.SysUser;
import com.ly.base.core.provide.sys.SysAccountService;
import com.ly.base.core.provide.sys.SysUserService;
import com.ly.base.service.consumer.sys.SysUserConsumerService;

/**
 * 日志记录,处理返回结果,缓存特殊数据
 * @author LeiYong
 * @date 2016年10月04日
 */
@Component
public class SysUserConsumerServiceImpl implements SysUserConsumerService {

	@Autowired
	private SysUserService service;

	@Override
	@Logger(model = LogEnum.SysUser, type = LogOperateEnum.Update, title = "#pk")
	public Json addRatio(BigDecimal ratio, String pk) {
		Json json = new Json();
		int result = service.addRatio(ratio,pk);
		if (result == 0) {
			return ErrorConfig.getSystemEnableErrorJson();
		}
		json.setSuccess(true);
		return json;
	}
	/**
	 * 系统登录
	 * 不记录日志,登录会记录登录日志
	 * @param userName
	 * @param password
	 * @return
	 */ 
	@Override
	public Json loginSystem(String userName,String password) {
		Json json = new Json();
		SysUser result = service.loginSystem(userName,password);
		if (result == null) {
			return ErrorConfig.getSystemErrorJson();
		}
		json.setSuccess(true).setObj(result);
		return json;
	}
	/**
	 * 验证登录名
	 * 不记录日志
	 * @param userName
	 * @return
	 */ 
	@Override
	public Json checkLoginName(String userName) {
		Json json = new Json();
		int result = service.checkLoginName(userName);
		if (result == 0) {
			return json.setSuccess(true);
		}
		json.setMsg("用户名已存在");
		return json;
	}
	/**
	 * 修改密码
	 * @param updateBy
	 * @param pk
	 * @param newPwd
	 * @return
	 */
	@Override
	@Logger(model = LogEnum.SysUser, type = LogOperateEnum.Update, title = "#pk")
	public Json changePwd(String updateBy,String pk,String password){
		Json json = new Json();
		int result = service.changePwd(updateBy, pk, password);
		if (result == 0) {
			return ErrorConfig.getSystemErrorJson();
		}
		json.setSuccess(true).setObj(result);
		return json;
	}
	/**
	 * 通过主键查询
	 * 
	 * @param pk
	 * @return
	 */ 
	@Override
	@Logger(model = LogEnum.SysUser, type = LogOperateEnum.Select, title = "#pk")
	public Json getByPk(String pk) {
		Json json = new Json();
		SysUser result = service.selectByPrimaryKey(pk);
		if (result == null) {
			return ErrorConfig.getSystemErrorJson();
		}
		SysAccountService sysAccountService = SpringBeanUtil.getBean(SysAccountService.class);
		SysAccount sysAccount = sysAccountService.selectByPrimaryKey(result.getAccountPk());
		if (sysAccount==null) {
			return ErrorConfig.getSystemErrorJson();
		}
		result.setAccountName(sysAccount.getName());
		result.setPassword(null);
		json.setSuccess(true).setObj(result);
		return json;
	}
	/**
	 * 通过主键删除
	 * 
	 * @param pk
	 * @return
	 */ 
	@Override
	@Logger(model = LogEnum.SysUser, type = LogOperateEnum.Delete, title = "#pk")
	public Json deleteByPk(String pk) {
		Json json = new Json();
		int result = service.deleteByPrimaryKey(pk);
		if (result == 0) {
			return ErrorConfig.getSystemDeleteErrorJson();
		}
		json.setSuccess(true);
		return json;
	}
	/**
	 * 通过主键批量删除
	 * 
	 * @param pks
	 * @return
	 */ 
	@Override
	@Logger(model = LogEnum.SysUser, type = LogOperateEnum.Delete, title = "#pks?.![#this]")
	public Json deleteByBatch(String... pks) {
		Json json = new Json();
		int result = service.deleteByBatch(pks);
		if (result == 0) {
			return ErrorConfig.getSystemDeleteErrorJson();
		}
		json.setSuccess(true);
		return json;
	}
	/**
	 * 通过主键启用
	 * 
	 * @param pk
	 * @return
	 */ 
	@Override
	@Logger(model = LogEnum.SysUser, type = LogOperateEnum.Update, title = "#pk")
	public Json enable(String updateBy,String pk) {
		Json json = new Json();
		int result = service.enable(updateBy,pk);
		if (result == 0) {
			return ErrorConfig.getSystemEnableErrorJson();
		}
		json.setSuccess(true);
		return json;
	}
	/**
	 * 通过主键禁用
	 * 
	 * @param pk
	 * @return
	 */ 
	@Override
	@Logger(model = LogEnum.SysUser, type = LogOperateEnum.Update, title = "#pk")
	public Json disable(String updateBy,String pk) {
		Json json = new Json();
		int result = service.disable(updateBy,pk);
		if (result == 0) {
			return ErrorConfig.getSystemDisableErrorJson();
		}
		json.setSuccess(true);
		return json;
	}
	/**
	 * 通过主键批量启用
	 * 
	 * @param pks
	 * @return
	 */ 
	@Override
	@Logger(model = LogEnum.SysUser, type = LogOperateEnum.Update, title = "#pks?.![#this]")
	public Json enables(String updateBy,String... pks) {
		Json json = new Json();
		int result = service.enables(updateBy,pks);
		if (result == 0) {
			return ErrorConfig.getSystemEnableErrorJson();
		}
		json.setSuccess(true);
		return json;
	}
	/**
	 * 通过主键批量禁用
	 * 
	 * @param pks
	 * @return
	 */ 
	@Override
	@Logger(model = LogEnum.SysUser, type = LogOperateEnum.Update, title = "#pks?.![#this]")
	public Json disables(String updateBy,String... pks) {
		Json json = new Json();
		int result = service.disables(updateBy,pks);
		if (result == 0) {
			return ErrorConfig.getSystemDisableErrorJson();
		}
		json.setSuccess(true);
		return json;
	}
	/**
	 * 新增记录
	 * 
	 * @param data
	 * @return
	 */ 
	@Override
	@Logger(model = LogEnum.SysUser, type = LogOperateEnum.Save, title = "#data?.pk")
	public Json insert(SysUser data) {
		Json json = new Json();
		SysUser result = service.insertSelective(data);
		if (result == null) {
			return ErrorConfig.getSystemAddErrorJson();
		}
		//回写主键参数用于记录日志
		data.setPk(result.getPk());
		json.setSuccess(true);
		return json;
	}
	/**
	 * 批量新增(不记录详细插入信息,如要记录pk参数,需service调用后回写主键)
	 * 
	 * @param list
	 * @return
	 */ 
	@Override
	@Logger(model = LogEnum.SysUser, type = LogOperateEnum.Save, title = "#objs?.!['1']")
	public Json insertBatch(List<SysUser> list) {
		Json json = new Json();
		int result = service.insertBatch(list);
		if (result == 0) {
			return ErrorConfig.getSystemAddErrorJson();
		}
		json.setSuccess(true);
		return json;
	}
	/**
	 * 更新记录
	 * 
	 * @param data
	 * @return
	 */ 
	@Override
	@Logger(model = LogEnum.SysUser, type = LogOperateEnum.Update, title = "#data?.pk")
	public Json update(SysUser data) {
		Json json = new Json();
		int result = service.updateByPK(data);
		if (result == 0) {
			return ErrorConfig.getSystemUpdateErrorJson();
		}
		json.setSuccess(true);
		return json;
	}
	/**
	 * 分页查询
	 * 
	 * @param queryInfo
	 * @param pageNum
	 * @param pageSize
	 * @param orderBy
	 * @return
	 */ 
	@Override
	@Logger(model = LogEnum.SysUser, type = LogOperateEnum.Select, title = "")
	public Json findByPage(SysUser queryInfo, int pageNum, int pageSize,String orderBy) {
		Json json = new Json();
		Page<SysUser> result = service.findByPage(queryInfo,pageNum,pageSize,orderBy);
		if (result == null) {
			return ErrorConfig.getSystemErrorJson();
		}
		result.forEach((su)->{
			su.setPassword(null);
		});
		json.setSuccess(true).setObj(result).setTotal(result.getPages());
		return json;
	}
	/**
	 * 查询所有
	 * 
	 * @param queryInfo
	 * @param orderBy
	 * @return
	 */ 
	@Override
	@Logger(model = LogEnum.SysUser, type = LogOperateEnum.Select, title = "")
	public Json findAll(SysUser queryInfo, String orderBy) {
		Json json = new Json();
		Page<SysUser> result = service.findAll(queryInfo,orderBy);
		if (result == null) {
			return ErrorConfig.getSystemErrorJson();
		}
		result.forEach((su)->{
			su.setPassword(null);
		});
		json.setSuccess(true).setObj(result);
		return json;
	}
	/**
	 * 分页查询
	 * 
	 * @param conditions
	 * @param pageNum
	 * @param pageSize
	 * @param orderBy
	 * @return
	 */ 
	@Override
	@Logger(model = LogEnum.SysUser, type = LogOperateEnum.Select, title = "")
	public Json findByPage(List<Model> conditions, int pageNum, int pageSize,String orderBy) {
		Json json = new Json();
		Page<SysUser> result = service.findByPage(conditions,pageNum,pageSize,orderBy);
		if (result == null) {
			return ErrorConfig.getSystemErrorJson();
		}
		result.forEach((su)->{
			su.setPassword(null);
		});
		json.setSuccess(true).setObj(result).setTotal(result.getPages());
		return json;
	}
	/**
	 * 查询所有
	 * 
	 * @param conditions
	 * @param orderBy
	 * @return
	 */ 
	@Override
	@Logger(model = LogEnum.SysUser, type = LogOperateEnum.Select, title = "")
	public Json findAll(List<Model> conditions, String orderBy) {
		Json json = new Json();
		Page<SysUser> result = service.findAll(conditions,orderBy);
		if (result == null) {
			return ErrorConfig.getSystemErrorJson();
		}
		result.forEach((su)->{
			su.setPassword(null);
		});
		json.setSuccess(true).setObj(result);
		return json;
	}
	/**
	 * 导出
	 * 
	 * @param conditions
	 * @param orderBy
	 * @return
	 */ 
	@Override
	@Logger(model = LogEnum.SysUser, type = LogOperateEnum.Export, title = "")
	public Json findByExport(List<Model> conditions, String orderBy) {
		Json json = new Json();
		Page<SysUser> result = service.findAll(conditions,orderBy);
		if (result == null) {
			return ErrorConfig.getSystemErrorJson();
		}
		result.forEach((su)->{
			su.setPassword(null);
		});
		json.setSuccess(true).setObj(result);
		return json;
	}
}