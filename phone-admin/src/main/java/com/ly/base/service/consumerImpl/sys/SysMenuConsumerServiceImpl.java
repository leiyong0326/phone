package com.ly.base.service.consumerImpl.sys;

import com.ly.base.core.model.sys.SysMenu;
import com.ly.base.core.provide.sys.SysMenuService;
import com.ly.base.service.consumer.sys.SysMenuConsumerService;
import com.ly.base.common.model.Json;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.ly.base.common.model.Model;
import com.github.pagehelper.Page;
import com.ly.base.common.annotation.Logger;
import com.ly.base.common.em.ext.LogEnum;
import com.ly.base.common.em.ext.LogOperateEnum;
import com.ly.base.common.system.ErrorConfig;
import org.springframework.stereotype.Component;

/**
 * 日志记录,处理返回结果,缓存特殊数据
 * @author LeiYong
 * @date 2016年10月04日
 */
@Component
public class SysMenuConsumerServiceImpl implements SysMenuConsumerService {

	@Autowired
	private SysMenuService service;

	/**
	 * 查询用户的菜单
	 * @param rolePk
	 * @return
	 */
	@Override
	@Logger(model = LogEnum.SysMenu, type = LogOperateEnum.Select, title = "#rolePk")
	public Json findAllMenuByRole(Integer rolePk) {
		Json json = new Json();
		List<SysMenu> list = service.findAllMenuByRole(rolePk);
		if (list==null) {
			json.setMsg("获取用户菜单失败,请重新登录");
		}else{
			json.setObj(list).setSuccess(true);
		}
		return json;
	}

	/**
	 * 查询用户的功能
	 * @param rolePk
	 * @return
	 */
	@Override
	@Logger(model = LogEnum.SysMenu, type = LogOperateEnum.Select, title = "#rolePk")
	public Json findAllFuncByRole(Integer rolePk) {
		Json json = new Json();
		List<SysMenu> list = service.findAllFuncByRole(rolePk);
		if (list==null) {
			json.setMsg("获取用户功能权限失败,请重新登录");
		}else{
			json.setObj(list).setSuccess(true);
		}
		return json;
	}

	/**
	 * 查询用户的功能按钮归组为String
	 * 如:[{01:show,edit,del},{02:show,edit,del}]
	 * @param rolePk
	 * @param menuType //取消参数,避免只有访问权限的情况
	 * @return
	 */
	@Override
	@Logger(model = LogEnum.SysMenu, type = LogOperateEnum.Select, title = "#rolePk")
	public Json findAllFuncStringByRole(Integer rolePk) {
		Json json = new Json();
		List<SysMenu> list = service.findAllFuncStringByRole(rolePk);
		if (list==null) {
			json.setMsg("获取用户功能权限失败,请重新登录");
		}else{
			json.setObj(list).setSuccess(true);
		}
		return json;
	}
	/**
	 * 通过主键查询
	 * 
	 * @param pk
	 * @return
	 */ 
	@Override
	@Logger(model = LogEnum.SysMenu, type = LogOperateEnum.Select, title = "#pk")
	public Json getByPk(String pk) {
		Json json = new Json();
		SysMenu result = service.selectByPrimaryKey(pk);
		if (result == null) {
			return ErrorConfig.getSystemErrorJson();
		}
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
	@Logger(model = LogEnum.SysMenu, type = LogOperateEnum.Delete, title = "#pk")
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
	@Logger(model = LogEnum.SysMenu, type = LogOperateEnum.Delete, title = "#pks?.![#this]")
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
	@Logger(model = LogEnum.SysMenu, type = LogOperateEnum.Update, title = "#pk")
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
	@Logger(model = LogEnum.SysMenu, type = LogOperateEnum.Update, title = "#pk")
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
	@Logger(model = LogEnum.SysMenu, type = LogOperateEnum.Update, title = "#pks?.![#this]")
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
	@Logger(model = LogEnum.SysMenu, type = LogOperateEnum.Update, title = "#pks?.![#this]")
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
	@Logger(model = LogEnum.SysMenu, type = LogOperateEnum.Save, title = "#data?.pk")
	public Json insert(SysMenu data) {
		Json json = new Json();
		SysMenu result = service.insertSelective(data);
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
	@Logger(model = LogEnum.SysMenu, type = LogOperateEnum.Save, title = "#objs?.!['1']")
	public Json insertBatch(List<SysMenu> list) {
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
	@Logger(model = LogEnum.SysMenu, type = LogOperateEnum.Update, title = "#data?.pk")
	public Json update(SysMenu data) {
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
	@Logger(model = LogEnum.SysMenu, type = LogOperateEnum.Select, title = "")
	public Json findByPage(SysMenu queryInfo, int pageNum, int pageSize,String orderBy) {
		Json json = new Json();
		Page<SysMenu> result = service.findByPage(queryInfo,pageNum,pageSize,orderBy);
		if (result == null) {
			return ErrorConfig.getSystemErrorJson();
		}
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
	@Logger(model = LogEnum.SysMenu, type = LogOperateEnum.Select, title = "")
	public Json findAll(SysMenu queryInfo, String orderBy) {
		Json json = new Json();
		Page<SysMenu> result = service.findAll(queryInfo,orderBy);
		if (result == null) {
			return ErrorConfig.getSystemErrorJson();
		}
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
	@Logger(model = LogEnum.SysMenu, type = LogOperateEnum.Select, title = "")
	public Json findByPage(List<Model> conditions, int pageNum, int pageSize,String orderBy) {
		Json json = new Json();
		Page<SysMenu> result = service.findByPage(conditions,pageNum,pageSize,orderBy);
		if (result == null) {
			return ErrorConfig.getSystemErrorJson();
		}
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
	@Logger(model = LogEnum.SysMenu, type = LogOperateEnum.Select, title = "")
	public Json findAll(List<Model> conditions, String orderBy) {
		Json json = new Json();
		Page<SysMenu> result = service.findAll(conditions,orderBy);
		if (result == null) {
			return ErrorConfig.getSystemErrorJson();
		}
		json.setSuccess(true).setObj(result);
		return json;
	}
}