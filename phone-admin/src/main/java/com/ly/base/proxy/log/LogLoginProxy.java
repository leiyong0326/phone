package com.ly.base.proxy.log;

import com.ly.base.core.model.log.LogLogin;
import com.ly.base.service.consumer.log.LogLoginConsumerService;
import com.ly.base.common.model.Json;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.ly.base.common.model.Model;
import javax.servlet.http.HttpServletRequest;
import com.ly.base.common.system.ErrorConfig;
import com.ly.base.core.util.WebUtils;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import com.ly.base.common.util.MyBatisUtil;
import com.ly.base.core.model.sys.SysUser;

/**
 * 参数验证,参数处理,缓存session、cookie
 * @author LeiYong
 * @date 2016年10月04日
 */
@Component
public class LogLoginProxy {

	@Autowired
	private LogLoginConsumerService service;
	/**
	 * 通过主键查询
	 * 
	 * @param pk
	 * @return
	 */ 
	public Json getByPk(HttpServletRequest request,Integer pk) {
		if(pk == null) {return ErrorConfig.getSystemParamErrorJson();};
		return checkResult(service.getByPk(pk));
	}
	/**
	 * 通过主键删除
	 * 
	 * @param pk
	 * @return
	 */ 
	public Json deleteByPk(HttpServletRequest request,Integer pk) {
		if(pk == null) {return ErrorConfig.getSystemParamErrorJson();};
		return checkResult(service.deleteByPk(pk));
	}
	/**
	 * 通过主键批量删除
	 * 
	 * @param pks
	 * @return
	 */ 
	public Json deleteByBatch(HttpServletRequest request,Integer... pks) {
		if(pks == null) {return ErrorConfig.getSystemParamErrorJson();};
		return checkResult(service.deleteByBatch(pks));
	}
	/**
	 * 通过主键启用
	 * 
	 * @param pk
	 * @return
	 */ 
	public Json enable(HttpServletRequest request,Integer pk) {
		if(pk == null) {return ErrorConfig.getSystemParamErrorJson();};
		SysUser user = WebUtils.getLoginUser(request);
		return checkResult(service.enable(user.getPk(),pk));
	}
	/**
	 * 通过主键禁用
	 * 
	 * @param pk
	 * @return
	 */ 
	public Json disable(HttpServletRequest request,Integer pk) {
		if(pk == null) {return ErrorConfig.getSystemParamErrorJson();};
		SysUser user = WebUtils.getLoginUser(request);
		return checkResult(service.disable(user.getPk(),pk));
	}
	/**
	 * 通过主键批量启用
	 * 
	 * @param pks
	 * @return
	 */ 
	public Json enables(HttpServletRequest request,Integer... pks) {
		if(pks == null) {return ErrorConfig.getSystemParamErrorJson();};
		SysUser user = WebUtils.getLoginUser(request);
		return checkResult(service.enables(user.getPk(),pks));
	}
	/**
	 * 通过主键批量禁用
	 * 
	 * @param pks
	 * @return
	 */ 
	public Json disables(HttpServletRequest request,Integer... pks) {
		if(pks == null) {return ErrorConfig.getSystemParamErrorJson();};
		SysUser user = WebUtils.getLoginUser(request);
		return checkResult(service.disables(user.getPk(),pks));
	}
	/**
	 * 新增记录
	 * 
	 * @param data
	 * @return
	 */ 
	public Json insert(HttpServletRequest request,LogLogin data) {
		SysUser user = WebUtils.getLoginUser(request);
		String checkRes = checkData(data,user);
		if (checkRes != null) {
			return new Json().setMsg(checkRes);
		}
		return checkResult(service.insert(data));
	}
	/**
	 * 批量新增
	 * 
	 * @param list
	 * @return
	 */ 
	public Json insertBatch(HttpServletRequest request,List<LogLogin> list) {
		if(list == null) {return ErrorConfig.getSystemParamErrorJson();};
		SysUser user = WebUtils.getLoginUser(request);
		String checkResult = null;
		for(LogLogin data : list) {
			checkResult = checkData(data,user);
			if(checkResult != null){
				return new Json().setMsg(checkResult);
			}
		}
		return checkResult(service.insertBatch(list));
	}
	/**
	 * 更新记录
	 * 
	 * @param data
	 * @return
	 */ 
	public Json update(HttpServletRequest request,LogLogin data) {
		SysUser user = WebUtils.getLoginUser(request);
		String checkRes = checkData(data,user);
		if (checkRes != null) {
			return new Json().setMsg(checkRes);
		}
		return checkResult(service.update(data));
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
	public Json findByPage(HttpServletRequest request,LogLogin queryInfo, int pageNum, int pageSize,String orderBy) {
		if (pageNum < 1 || pageSize < 1 ) {
			return ErrorConfig.getSystemErrorJson();
		}
		//queryInfo->conditions
		List<Model> conditions = getConditions(queryInfo,request);
		return checkResult(service.findByPage(conditions,pageNum,pageSize,orderBy));
	}
	/**
	 * 查询所有
	 * 
	 * @param conditions
	 * @param orderBy
	 * @return
	 */ 
	public Json findAll(HttpServletRequest request,LogLogin queryInfo, String orderBy) {
		//queryInfo->conditions
		List<Model> conditions = getConditions(queryInfo,request);
		return checkResult(service.findAll(conditions,orderBy));
	}
	/**
	 * 数据校验及数据填充,如更新时间,更新人等
	 * 
	 * @param data
	 * @param user
	 * @return
	 */ 
	private String checkData(LogLogin data,SysUser user) {
		//TODO 请完善数据校验及填充,如更新时间,更新人等
		return null;
	}
	/**
	 * 验证返回结果
	 * 
	 * @param json
	 * @return
	 */ 
	private Json checkResult(Json json) {
		if(json == null){
			return ErrorConfig.getSystemErrorJson();
		}
		return json;
	}
	/**
	 * 数据校验及数据填充
	 * 
	 * @param json
	 * @return
	 */ 
	private List<Model> getConditions(LogLogin queryInfo,HttpServletRequest request) {
		if(queryInfo == null){
			return new ArrayList<Model>();
		}
		List<Model> conditions = MyBatisUtil.parseByObject(queryInfo, true);
		return conditions;
	}
}