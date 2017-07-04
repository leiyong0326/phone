package com.ly.base.controller.sys;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ly.base.common.model.Json;
import com.ly.base.core.excel.export.ext.SysUserExcelExport;
import com.ly.base.core.model.sys.SysUser;
import com.ly.base.core.util.excel.ExportUtil;
import com.ly.base.proxy.sys.SysUserProxy;

/**
 * 业务暴露,请将不需要的方法删除
 * @author LeiYong
 * @date 2016年10月04日
 */
@Controller
@RequestMapping(value = "/sysUser")
public class SysUserController {

	@Autowired
	private SysUserProxy proxy;
	/**
	 * 后台管理用户登录接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody String login(HttpServletRequest request, HttpServletResponse response,String loginName,String password,String rememberme) {
		String returnString = proxy.loginSystem(request, response,loginName,password,rememberme).toJsonString();
		return returnString;
	}
	@RequestMapping(value = "/checkLoginName", method = RequestMethod.POST)
	public @ResponseBody String checkLoginName(HttpServletRequest request, HttpServletResponse response,String userName) {
		String returnString = proxy.checkLoginName(request, userName).toJsonString();
		return returnString;
	}
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public @ResponseBody String changePassword(HttpServletRequest request, HttpServletResponse response,String password,String newPassword) {
		String returnString = proxy.changePassword(request, password, newPassword).toJsonString();
		return returnString;
	}
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	public @ResponseBody String updatePassword(HttpServletRequest request, HttpServletResponse response,String pk,String newPassword) {
		String returnString = proxy.updatePassword(request,pk, null, newPassword).toJsonString();
		return returnString;
	}

	@RequestMapping(value = "/logout")
	public @ResponseBody String logout(HttpServletRequest request, HttpServletResponse response) {
		Json json = proxy.logout(request,response);
		return json.toJsonString();
	}
	@RequestMapping(value = "/addRatio", method = RequestMethod.POST)
	public @ResponseBody String addRatio(HttpServletRequest request, HttpServletResponse response,BigDecimal ratio,String pk) {
		Json json = proxy.addRatio(request,response,ratio,pk);
		return json.toJsonString();
	}
	/**
	 * 通过主键查询
	 * 
	 * @param pk
	 * @return
	 */ 
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	@ResponseBody
	public Json getByPk(HttpServletRequest request,String pk) {
		return proxy.getByPk(request,pk);
	}
	/**
	 * 个人信息查询
	 * @return
	 */ 
	@RequestMapping(value = "/myInfo", method = RequestMethod.POST)
	@ResponseBody
	public Json myInfo(HttpServletRequest request) {
		return proxy.myInfo(request);
	}
	/**
	 * 个人信息修改
	 * @return
	 */ 
	@RequestMapping(value = "/changeMyInfo", method = RequestMethod.POST)
	@ResponseBody
	public Json changeMyInfo(HttpServletRequest request,String wechat,String qq) {
		return proxy.changeMyInfo(request,wechat,qq);
	}
	/**
	 * 通过主键删除
	 * 
	 * @param pk
	 * @return
	 */ 
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public Json deleteByPk(HttpServletRequest request,String pk) {
		return proxy.deleteByPk(request,pk);
	}
	/**
	 * 通过主键批量删除
	 * 
	 * @param pks
	 * @return
	 */ 
	@RequestMapping(value = "/deleteByBatch", method = RequestMethod.POST)
	@ResponseBody
	public Json deleteByBatch(HttpServletRequest request,@RequestParam("pks[]")String[] pks) {
		return proxy.deleteByBatch(request,pks);
	}
	/**
	 * 通过主键启用
	 * 
	 * @param pk
	 * @return
	 */ 
	@RequestMapping(value = "/enable", method = RequestMethod.POST)
	@ResponseBody
	public Json enable(HttpServletRequest request,String pk) {
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
	public Json disable(HttpServletRequest request,String pk) {
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
	public Json enables(HttpServletRequest request,String... pks) {
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
	public Json disables(HttpServletRequest request,String... pks) {
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
	public Json insert(HttpServletRequest request,SysUser data) {
		if (data.getPk()==null) {
			return proxy.insert(request,data);
		}else{
			return proxy.update(request,data);
		}
	}
	/**
	 * 批量新增
	 * 
	 * @param list
	 * @return
	 */ 
	@RequestMapping(value = "/insertBatch", method = RequestMethod.POST)
	@ResponseBody
	public Json insertBatch(HttpServletRequest request,List<SysUser> list) {
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
	public Json update(HttpServletRequest request,SysUser data) {
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
	public Json findByPage(HttpServletRequest request,SysUser queryInfo, int pageNum, int pageSize,String orderBy) {
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
	public Json findAll(HttpServletRequest request,SysUser queryInfo, String orderBy) {
		return proxy.findAll(request,queryInfo,orderBy);
	}
	/**
	 * 导出
	 * 
	 * @param queryInfo
	 * @param orderBy
	 * @return
	 */ 
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public void export(HttpServletRequest request, HttpServletResponse response,SysUser queryInfo, String orderBy) {
		Json ret = proxy.findByExport(request,queryInfo,orderBy);
		ExportUtil.export(ret, queryInfo, response, SysUserExcelExport.class);
	}
}