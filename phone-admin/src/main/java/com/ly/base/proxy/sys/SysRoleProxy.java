package com.ly.base.proxy.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ly.base.common.model.Json;
import com.ly.base.common.model.Model;
import com.ly.base.common.system.ErrorConfig;
import com.ly.base.common.system.SystemConfig;
import com.ly.base.common.system.cookies.AdminCookieConfig;
import com.ly.base.common.util.ArrayUtil;
import com.ly.base.common.util.BeanUtil;
import com.ly.base.common.util.MyBatisUtil;
import com.ly.base.common.util.ReflectionUtil;
import com.ly.base.common.util.StringUtil;
import com.ly.base.core.model.sys.SysRole;
import com.ly.base.core.model.sys.SysUser;
import com.ly.base.core.util.WebUtils;
import com.ly.base.service.consumer.sys.SysRoleConsumerService;

/**
 * 参数验证,参数处理,缓存session、cookie
 * @author LeiYong
 * @date 2016年10月04日
 */
@Component
public class SysRoleProxy {

	private static final Map<String, String> fieldNameMap = new HashMap<>();
	static {
		fieldNameMap.put("name", "角色名");
	}
	@Autowired
	private SysRoleConsumerService service;
	/**
	 * 角色授权
	 * @param menus
	 * @param pk
	 * @return
	 */
	public Json grantRole(HttpServletRequest request,String menuJsonString,Integer rolePk){
		if(rolePk == null) {return ErrorConfig.getSystemParamErrorJson();};
		SysUser su = WebUtils.getLoginUser(request);;
		if (menuJsonString.length()>0) {
			if (rolePk.equals(su.getRolePk())) {
				return ErrorConfig.getSysGrantOperateJson();
			}
			//解析menuPks为json,并转为数组对象
			JSONArray menuArrary = JSON.parseArray(menuJsonString);
			List<String> menuList = new ArrayList<>(menuArrary.size());
			for (int i = 0; i < menuArrary.size(); i++) {
				JSONObject jo = menuArrary.getJSONObject(i);
				String pk = jo.getString("pk");
				String name = jo.getString("name");//权限信息
				menuList.add(pk);
				if (StringUtils.isNotBlank(name)) {
					//分割每个权限信息
					String[] permissions = name.split(",");
					for (String permission : permissions) {
						if (StringUtils.isNotEmpty(permission)) {
							menuList.add(pk+permission);
						}
					}
				}
			}
			//将List转为数组
			Object obj = SecurityUtils.getSubject().getSession().getAttribute(AdminCookieConfig.SYS_FUNC_KEY);
			//先获取权限缓存,访问授权必定提前缓存,不会为空
			if (obj != null) {
				boolean result = checkGrant(obj, menuList);
				if (result) {
					return checkResult(service.grantRole(menuList, rolePk));
				}
			}
		}
		return ErrorConfig.getSystemParamErrorJson();
	}
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
	public Json insert(HttpServletRequest request,SysRole data) {
		SysUser user = WebUtils.getLoginUser(request);
		String checkRes = checkData(data,user);
		if (checkRes != null) {
			return new Json().setMsg(checkRes);
		}
		data.setEnable(SystemConfig.ENABLE);
		return checkResult(service.insert(data));
	}
	/**
	 * 批量新增
	 * 
	 * @param list
	 * @return
	 */ 
	public Json insertBatch(HttpServletRequest request,List<SysRole> list) {
		if(list == null) {return ErrorConfig.getSystemParamErrorJson();};
		SysUser user = WebUtils.getLoginUser(request);
		String checkResult = null;
		for(SysRole data : list) {
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
	public Json update(HttpServletRequest request,SysRole data) {
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
	public Json findByPage(HttpServletRequest request,SysRole queryInfo, int pageNum, int pageSize,String orderBy) {
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
	public Json findAll(HttpServletRequest request,SysRole queryInfo, String orderBy) {
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
	private String checkData(SysRole data,SysUser user) {
		return BeanUtil.checkEntity(data, fieldNameMap);
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
	private List<Model> getConditions(SysRole queryInfo,HttpServletRequest request) {
		if(queryInfo == null){
			return new ArrayList<Model>();
		}
		List<Model> conditions = MyBatisUtil.parseByObject(queryInfo, true);
		return conditions;
	}
	/**
	 * 验证用户权限
	 * @param obj
	 * @param menuList
	 * @return
	 */
	private boolean checkGrant(Object obj,List<String> menuList){

		boolean result = false;
		Map<?, ?> map = ReflectionUtil.convertObjectToBean(obj, Map.class);
		if (map != null) {
			List<String> ms = menuList;
			result = ArrayUtil.foreach(ms, (m,i)->{
				//遍历校验授权菜单是否异常
				if (StringUtils.isNotBlank(m)) {
					if (StringUtil.isInteger(m)) {
						return map.containsKey(m);
					}
					String[] kv = m.replaceAll("^(\\d+)(\\w+)$", "$1,$2").split(",");
					String vs = map.get(kv[0]).toString();
					//如果自己都没权限的,不允许授权给其他人
					return vs.contains(kv[1]);
				}
				return true;
			});
		}
		return result;
	}
}