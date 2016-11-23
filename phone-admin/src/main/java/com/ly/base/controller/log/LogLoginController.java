package com.ly.base.controller.log;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ly.base.common.model.Json;
import com.ly.base.core.model.log.LogLogin;
import com.ly.base.proxy.log.LogLoginProxy;

/**
 * 业务暴露,请将不需要的方法删除
 * @author LeiYong
 * @date 2016年10月16日
 */
@Controller
@RequestMapping(value = "/logLogin")
public class LogLoginController {

	@Autowired
	private LogLoginProxy proxy;
	/**
	 * 通过主键查询
	 * 
	 * @param pk
	 * @return
	 */ 
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	@ResponseBody
	public Json getByPk(HttpServletRequest request,Integer pk) {
		return proxy.getByPk(request,pk);
	}
	/**
	 * 通过主键删除
	 * 不提供删除功能
	 * @param pk
	 * @return
	 */ 
//	@RequestMapping(value = "/delete", method = RequestMethod.POST)
//	@ResponseBody
//	public Json deleteByPk(HttpServletRequest request,Integer pk) {
//		return proxy.deleteByPk(request,pk);
//	}
	/**
	 * 通过主键批量删除
	 * 不提供删除功能
	 * @param pks
	 * @return
	 */ 
//	@RequestMapping(value = "/deleteByBatch", method = RequestMethod.POST)
//	@ResponseBody
//	public Json deleteByBatch(HttpServletRequest request,@RequestParam("pks[]")Integer[] pks) {
//		return proxy.deleteByBatch(request,pks);
//	}
	/**
	 * 通过主键启用
	 * 
	 * @param pk
	 * @return
	 */ 
	@RequestMapping(value = "/enable", method = RequestMethod.POST)
	@ResponseBody
	public Json enable(HttpServletRequest request,Integer pk) {
		return proxy.enable(request,pk);
	}
	/**
	 * 通过主键禁用
	 * 
	 * @param pk
	 * @return
	 */ 
	@RequestMapping(value = "/disable", method = RequestMethod.POST)
	@ResponseBody
	public Json disable(HttpServletRequest request,Integer pk) {
		return proxy.disable(request,pk);
	}
	/**
	 * 通过主键批量启用
	 * 
	 * @param pks
	 * @return
	 */ 
	@RequestMapping(value = "/enables", method = RequestMethod.POST)
	@ResponseBody
	public Json enables(HttpServletRequest request,Integer... pks) {
		return proxy.enables(request,pks);
	}
	/**
	 * 通过主键批量禁用
	 * 
	 * @param pks
	 * @return
	 */ 
	@RequestMapping(value = "/disables", method = RequestMethod.POST)
	@ResponseBody
	public Json disables(HttpServletRequest request,Integer... pks) {
		return proxy.disables(request,pks);
	}
	/**
	 * 新增记录
	 * 
	 * @param data
	 * @return
	 */ 
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	@ResponseBody
	public Json insert(HttpServletRequest request,LogLogin data) {
		if (data.getPk()==null) {
			return proxy.insert(request,data);
		}
		return proxy.update(request,data);
	}
	/**
	 * 批量新增
	 * 
	 * @param list
	 * @return
	 */ 
	@RequestMapping(value = "/insertBatch", method = RequestMethod.POST)
	@ResponseBody
	public Json insertBatch(HttpServletRequest request,List<LogLogin> list) {
		return proxy.insertBatch(request,list);
	}
	/**
	 * 更新记录
	 * 
	 * @param data
	 * @return
	 */ 
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Json update(HttpServletRequest request,LogLogin data) {
		return proxy.update(request,data);
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
	@RequestMapping(value = "/findByPage", method = RequestMethod.POST)
	@ResponseBody
	public Json findByPage(HttpServletRequest request,LogLogin queryInfo, int pageNum, int pageSize,String orderBy) {
		return proxy.findByPage(request,queryInfo,pageNum,pageSize,orderBy);
	}
	/**
	 * 查询所有
	 * 
	 * @param queryInfo
	 * @param orderBy
	 * @return
	 */ 
	@RequestMapping(value = "/findAll", method = RequestMethod.POST)
	@ResponseBody
	public Json findAll(HttpServletRequest request,LogLogin queryInfo, String orderBy) {
		return proxy.findAll(request,queryInfo,orderBy);
	}
}