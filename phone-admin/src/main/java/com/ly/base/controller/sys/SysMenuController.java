package com.ly.base.controller.sys;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ly.base.common.em.ext.security.AdminSecurityEnum;
import com.ly.base.common.model.Json;
import com.ly.base.common.system.page.AdminPageConfig;
import com.ly.base.common.util.StringUtil;
import com.ly.base.core.model.sys.SysMenu;
import com.ly.base.proxy.sys.SysMenuProxy;
import com.ly.base.util.AdminSecurityUtil;

/**
 * 业务暴露,请将不需要的方法删除
 * @author LeiYong
 * @date 2016年10月04日
 */
@Controller
@RequestMapping(value = "/sysMenu")
public class SysMenuController {

	@Autowired
	private SysMenuProxy proxy;
	@RequestMapping("/findMenuList")
	public @ResponseBody String findMenuList(HttpServletRequest request){
		Json json=proxy.findAllMenuByRole(request);
		String returnString = json.toJsonString();
		return returnString;
	}

	@RequestMapping("/findMenuFunc")
	public @ResponseBody String findMenuFunc(HttpServletRequest request,String menuPk){
		Json json=proxy.findFuncStringByMenu(request,menuPk);
		String returnString = json.toJsonString();
		return returnString;
	}

	@RequestMapping("/findFuncList")
	public @ResponseBody String findFuncList(HttpServletRequest request, HttpServletResponse response, Integer rolePk) {
		boolean permitted = AdminSecurityUtil.isPermitted(AdminSecurityEnum.SysRole, "edit");
		Json json = null;
		// 如果查询其它角色且无角色编辑权限则跳转无权访问
		if (permitted) {
			json=proxy.findFuncStringByRole(request,rolePk);
			String returnString = json.toJsonString();
			return returnString;
		}
		try {
			response.sendRedirect(
					StringUtil.appendStringNotNull(null, AdminPageConfig.PROJECT_NAME, AdminPageConfig.NOPERMS_PAGE));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
	public Json insert(HttpServletRequest request,SysMenu data) {
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
	public Json insertBatch(HttpServletRequest request,List<SysMenu> list) {
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
	public Json update(HttpServletRequest request,SysMenu data) {
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
	public Json findByPage(HttpServletRequest request,SysMenu queryInfo, int pageNum, int pageSize,String orderBy) {
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
	public Json findAll(HttpServletRequest request,SysMenu queryInfo, String orderBy) {
		return proxy.findAll(request,queryInfo,orderBy);
	}
}