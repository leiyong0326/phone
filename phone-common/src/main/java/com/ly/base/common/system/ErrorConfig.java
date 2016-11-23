package com.ly.base.common.system;

import com.ly.base.common.model.Json;

public class ErrorConfig {
	public static final String SYSTEM_ERROR = "系统异常,请重试";
	public static final String SYSTEM_ADD_ERROR = "系统异常,新增失败";
	public static final String SYSTEM_UPDATE_ERROR = "系统异常,修改失败";
	public static final String SYSTEM_DELETE_ERROR = "系统异常,删除失败";
	public static final String SYSTEM_ENABLE_ERROR = "系统异常,禁用失败";
	public static final String SYSTEM_DISABLE_ERROR = "系统异常,启用失败";
	public static final String SYSTEM_PARAM_ERROR = "参数异常";
	//SYS
//	public static final String SYS_ORGANIZATION_SERVER_END = "机构服务已过期,请续期";
//	public static final String SYS_ORGANIZATION_DISABLE = "机构已禁用";
//	public static final String SYS_DEPARTMENT_DISABLE = "部门已禁用";
	private static final String SYS_USER_DISABLE = "用户已禁用";
	private static final String SYS_USER_NOT_LOGIN = "请重新登录";
	private static final String SYS_GRANT_OPERATE = "不允许对当前角色授权";
	
	private final static Json SYSTEM_PARAM_ERROR_JSON = new Json(ErrorConfig.SYSTEM_PARAM_ERROR);
	private final static Json SYSTEM_ADD_ERROR_JSON = new Json(ErrorConfig.SYSTEM_ADD_ERROR);
	private final static Json SYSTEM_ERROR_JSON = new Json(ErrorConfig.SYSTEM_ERROR);
	private final static Json SYSTEM_UPDATE_ERROR_JSON = new Json(ErrorConfig.SYSTEM_UPDATE_ERROR);
	private final static Json SYSTEM_DELETE_ERROR_JSON = new Json(ErrorConfig.SYSTEM_DELETE_ERROR);
	private final static Json SYSTEM_ENABLE_ERROR_JSON = new Json(ErrorConfig.SYSTEM_ENABLE_ERROR);
	private final static Json SYSTEM_DISABLE_ERROR_JSON = new Json(ErrorConfig.SYSTEM_DISABLE_ERROR);
	private final static Json SYS_USER_DISABLE_JSON = new Json(ErrorConfig.SYS_USER_DISABLE);
	private final static Json SYS_USER_NOT_LOGIN_JSON = new Json(ErrorConfig.SYS_USER_NOT_LOGIN);
	private final static Json SYS_GRANT_OPERATE_JSON = new Json(ErrorConfig.SYS_GRANT_OPERATE);
	public static Json getSystemParamErrorJson() {
		return SYSTEM_PARAM_ERROR_JSON;
	}
	public static Json getSystemAddErrorJson() {
		return SYSTEM_ADD_ERROR_JSON;
	}
	public static Json getSystemErrorJson() {
		return SYSTEM_ERROR_JSON;
	}
	public static Json getSystemUpdateErrorJson() {
		return SYSTEM_UPDATE_ERROR_JSON;
	}
	public static Json getSystemDeleteErrorJson() {
		return SYSTEM_DELETE_ERROR_JSON;
	}
	public static Json getSystemEnableErrorJson() {
		return SYSTEM_ENABLE_ERROR_JSON;
	}
	public static Json getSystemDisableErrorJson() {
		return SYSTEM_DISABLE_ERROR_JSON;
	}
	public static Json getSysUserDisableJson() {
		return SYS_USER_DISABLE_JSON;
	}
	public static Json getSysUserNotLoginJson() {
		return SYS_USER_NOT_LOGIN_JSON;
	}
	public static Json getSysGrantOperateJson() {
		return SYS_GRANT_OPERATE_JSON;
	}
	
}
