package com.ly.base.proxy.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ly.base.common.em.ext.security.AdminSecurityEnum;
import com.ly.base.common.model.Json;
import com.ly.base.common.model.Model;
import com.ly.base.common.system.ErrorConfig;
import com.ly.base.common.system.cookies.AdminCookieConfig;
import com.ly.base.common.util.ArrayUtil;
import com.ly.base.common.util.MyBatisUtil;
import com.ly.base.common.util.ReflectionUtil;
import com.ly.base.core.model.sys.SysMenu;
import com.ly.base.core.model.sys.SysUser;
import com.ly.base.core.util.WebUtils;
import com.ly.base.service.consumer.sys.SysMenuConsumerService;
import com.ly.base.util.AdminSecurityUtil;

/**
 * 参数验证,参数处理,缓存session、cookie
 * @author LeiYong
 * @date 2016年10月04日
 */
@Component
public class SysMenuProxy {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SysMenuProxy.class);

	@Autowired
	private SysMenuConsumerService service;
	/**
	 * 查询用户的菜单
	 * 
	 * @param rolePk
	 * @param orgPk
	 * @return
	 */
	public Json findAllMenuByRole(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("findAllMenuByRole(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		// 从session中读取菜单数据
		Object obj = SecurityUtils.getSubject().getSession().getAttribute(AdminCookieConfig.SYS_MENU_KEY);
		if (obj != null) {
			Json json = new Json();
			json.setSuccess(true).setObj(obj);

			if (logger.isDebugEnabled()) {
				logger.debug("findAllMenuByRole(HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return json;
		}
		// 从数据库中查询menu数据
		SysUser su = WebUtils.getLoginUser(request);
		if (su != null) {
			Integer rolePk = su.getRolePk();
			if (rolePk!=null) {
				Json json = service.findAllMenuByRole(rolePk);
				if (json.isSuccess()) {
					SecurityUtils.getSubject().getSession().setAttribute(AdminCookieConfig.SYS_MENU_KEY, json.getObj());

					if (logger.isDebugEnabled()) {
						logger.debug("findAllMenuByRole(HttpServletRequest) - end"); //$NON-NLS-1$
					}
					return json;
				}
			}
		}
		Json returnJson = ErrorConfig.getSystemParamErrorJson();
		if (logger.isDebugEnabled()) {
			logger.debug("findAllMenuByRole(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnJson;
	}

	/**
	 * 查询用户的菜单对应的功能
	 * 
	 * @param rolePk
	 * @param orgPk
	 * @return
	 */
	public Json findAllFuncByRole(HttpServletRequest request, Integer rolePk) {
		if (logger.isDebugEnabled()) {
			logger.debug("findAllFuncByRole(HttpServletRequest, String) - start"); //$NON-NLS-1$
		}

		SysUser su = WebUtils.getLoginUser(request);
		if (rolePk==null) {
			rolePk = su.getRolePk();
		}
		if (rolePk!=null) {
			Json returnJson = service.findAllFuncByRole(rolePk);
			if (logger.isDebugEnabled()) {
				logger.debug("findAllFuncByRole(HttpServletRequest, String) - end"); //$NON-NLS-1$
			}
			return returnJson;
		}
		Json returnJson = ErrorConfig.getSystemParamErrorJson();
		if (logger.isDebugEnabled()) {
			logger.debug("findAllFuncByRole(HttpServletRequest, String) - end"); //$NON-NLS-1$
		}
		return returnJson;
	}

	/**
	 * 查询用户的菜单对应的功能为String 如:[{01:show,edit,del},{02:show,edit,del}]
	 * 
	 * @param rolePk
	 * @param orgPk
	 * @return
	 */
	public Json findAllFuncStringByRole(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("findAllFuncStringByRole(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		SysUser su = WebUtils.getLoginUser(request);
		Integer rolePk = su.getRolePk();
		if (rolePk!=null) {
			Json res = service.findAllFuncStringByRole(rolePk);
			if (res.isSuccess() && request != null) {
				Object obj = SecurityUtils.getSubject().getSession().getAttribute(AdminCookieConfig.SYS_FUNC_KEY);
				// 缓存功能权限到session
				if (obj == null) {
					try {
						Object resObj = res.getObj();
						List<?> list = ReflectionUtil.convertObjectToBean(resObj, List.class);
						Map<String, String> menuMap = new HashMap<>();
						if (list != null) {
							boolean cacheRes = ArrayUtil.foreach(list, (m, i) -> {
								SysMenu sm = (SysMenu) m;
								if (sm != null) {
									menuMap.put(sm.getPk(), sm.getName());
									return true;
								}
								return false;
							});
							if (cacheRes) {
								SecurityUtils.getSubject().getSession().setAttribute(AdminCookieConfig.SYS_FUNC_KEY,
										menuMap);
							}
						}
					} catch (Exception e) {
						logger.error("findAllFuncStringByRole(HttpServletRequest)", e); //$NON-NLS-1$

						// 即使有异常不影响之前查询的结果,仅仅放弃缓存
						System.out.println("findAllFuncStringByRole(HttpServletRequest request)缓存异常");
					}
				}
			}

			if (logger.isDebugEnabled()) {
				logger.debug("findAllFuncStringByRole(HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return res;
		}
		Json returnJson = ErrorConfig.getSystemParamErrorJson();
		if (logger.isDebugEnabled()) {
			logger.debug("findAllFuncStringByRole(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnJson;
	}

	/**
	 * 查询用户的菜单对应的功能为String 如:[{01:show,edit,del},{02:show,edit,del}]
	 * 
	 * @param rolePk
	 * @param menu @see AdminSecurityEnum
	 * @return
	 */
	public Json findFuncStringByMenu(HttpServletRequest request, String menu) {
		if (logger.isDebugEnabled()) {
			logger.debug("findFuncStringByMenu(HttpServletRequest, String) - start"); //$NON-NLS-1$
		}

		// 存储的功能JsonObject对象/Map对象,后从Map对象中读取对应的功能按钮
		Json resultJson = new Json();
		AdminSecurityEnum menuEnum = AdminSecurityUtil.convertAdminSecurityEnum(menu);
		if (menuEnum == null) {
			resultJson.setMsg("参数异常");

			if (logger.isDebugEnabled()) {
				logger.debug("findFuncStringByMenu(HttpServletRequest, String) - end"); //$NON-NLS-1$
			}
			return resultJson;
		}
		Object obj = SecurityUtils.getSubject().getSession().getAttribute(AdminCookieConfig.SYS_FUNC_KEY);
		if (obj != null) {
			Map<?, ?> map = ReflectionUtil.convertObjectToBean(obj, Map.class);
			if (map != null) {
				Object value = map.get(menuEnum.getDiscription());
				if (value != null) {
					resultJson.setObj(value).setSuccess(true);

					if (logger.isDebugEnabled()) {
						logger.debug("findFuncStringByMenu(HttpServletRequest, String) - end"); //$NON-NLS-1$
					}
					return resultJson;
				} else {
					return resultJson.setObj("").setSuccess(true);
				}
			}
		}
		SysUser su = WebUtils.getLoginUser(request);
		Integer rolePk = su.getRolePk();
		if (rolePk!=null) {
			//查询角色功能String
			Json res = service.findAllFuncStringByRole(rolePk);
			if (res.isSuccess()) {
				Object resObj = res.getObj();
				List<?> list = ReflectionUtil.convertObjectToBean(resObj, List.class);
				Map<String, String> menuMap = new HashMap<>();
				if (list != null) {
					boolean cacheRes = ArrayUtil.foreach(list, (m, i) -> {
						SysMenu sm = (SysMenu) m;
						if (sm != null) {
							menuMap.put(sm.getPk(), sm.getName());
							return true;
						}
						return false;
					});
					if (cacheRes) {
						SecurityUtils.getSubject().getSession().setAttribute(AdminCookieConfig.SYS_FUNC_KEY, menuMap);
						Object value = menuMap.get(menuEnum.getDiscription());
						if (value != null) {
							resultJson.setObj(value).setSuccess(true);

							if (logger.isDebugEnabled()) {
								logger.debug("findFuncStringByMenu(HttpServletRequest, String) - end"); //$NON-NLS-1$
							}
							return resultJson;
						}else{
							resultJson.setObj("").setSuccess(true);
						}
					}
				}
			}
		}
		Json returnJson = resultJson.setMsg(ErrorConfig.SYSTEM_PARAM_ERROR);
		if (logger.isDebugEnabled()) {
			logger.debug("findFuncStringByMenu(HttpServletRequest, String) - end"); //$NON-NLS-1$
		}
		return returnJson;
	}

	/**
	 * 查询用户的功能按钮归组为String
	 * 如:[{01:show,edit,del},{02:show,edit,del}]
	 * @param rolePk
	 * @param menuType //取消参数,避免只有访问权限的情况
	 * @return
	 */
	public Json findFuncStringByRole(HttpServletRequest request, Integer rolePk) {
		if (logger.isDebugEnabled()) {
			logger.debug("findFuncStringByRole(HttpServletRequest, String) - start"); //$NON-NLS-1$
		}

		SysUser su = WebUtils.getLoginUser(request);
		if (rolePk==null) {
			rolePk = su.getRolePk();
		}
		if (rolePk!=null) {
			//查询角色功能String
			Json res = service.findAllFuncStringByRole(rolePk);
			if (res.isSuccess()) {
				Object resObj = res.getObj();
				List<?> list = ReflectionUtil.convertObjectToBean(resObj, List.class);
				Map<String, String> menuMap = new HashMap<>();
				if (list != null) {
					boolean cacheRes = ArrayUtil.foreach(list, (m, i) -> {
						SysMenu sm = (SysMenu) m;
						if (sm != null) {
							menuMap.put(sm.getPk(), sm.getName());
							return true;
						}
						return false;
					});
					if (!cacheRes) {
						//缓存失败暂不做处理
					}
				}
			}
			return res;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("findFuncStringByRole(HttpServletRequest, String) - end"); //$NON-NLS-1$
		}
		return ErrorConfig.getSystemParamErrorJson();
	}
	/**
	 * 通过主键查询
	 * 
	 * @param pk
	 * @return
	 */ 
	public Json getByPk(HttpServletRequest request,String pk) {
		if(pk == null) {return ErrorConfig.getSystemParamErrorJson();};
		return checkResult(service.getByPk(pk));
	}
	/**
	 * 通过主键删除
	 * 
	 * @param pk
	 * @return
	 */ 
	public Json deleteByPk(HttpServletRequest request,String pk) {
		if(pk == null) {return ErrorConfig.getSystemParamErrorJson();};
		return checkResult(service.deleteByPk(pk));
	}
	/**
	 * 通过主键批量删除
	 * 
	 * @param pks
	 * @return
	 */ 
	public Json deleteByBatch(HttpServletRequest request,String... pks) {
		if(pks == null) {return ErrorConfig.getSystemParamErrorJson();};
		return checkResult(service.deleteByBatch(pks));
	}
	/**
	 * 通过主键启用
	 * 
	 * @param pk
	 * @return
	 */ 
	public Json enable(HttpServletRequest request,String pk) {
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
	public Json disable(HttpServletRequest request,String pk) {
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
	public Json enables(HttpServletRequest request,String... pks) {
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
	public Json disables(HttpServletRequest request,String... pks) {
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
	public Json insert(HttpServletRequest request,SysMenu data) {
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
	public Json insertBatch(HttpServletRequest request,List<SysMenu> list) {
		if(list == null) {return ErrorConfig.getSystemParamErrorJson();};
		SysUser user = WebUtils.getLoginUser(request);
		String checkResult = null;
		for(SysMenu data : list) {
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
	public Json update(HttpServletRequest request,SysMenu data) {
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
	public Json findByPage(HttpServletRequest request,SysMenu queryInfo, int pageNum, int pageSize,String orderBy) {
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
	public Json findAll(HttpServletRequest request,SysMenu queryInfo, String orderBy) {
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
	private String checkData(SysMenu data,SysUser user) {
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
	private List<Model> getConditions(SysMenu queryInfo,HttpServletRequest request) {
		if(queryInfo == null){
			return new ArrayList<Model>();
		}
		List<Model> conditions = MyBatisUtil.parseByObject(queryInfo, true);
		return conditions;
	}
//	public Json findAllFuncStringByRole(HttpServletRequest request) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}