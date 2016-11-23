package com.ly.base.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.ly.base.common.em.ext.security.AdminSecurityEnum;
import com.ly.base.common.util.StringUtil;

public class AdminSecurityUtil {
	public static final String EDIT_FUNC = "edit";
	public static final String DEL_FUNC = "del";
	public static final String SHOW_FUNC = "show";
	public static final String ALL_FUNC = "*";

	/**
	 * 通过value获取对应的AdminSecurityEnum
	 * 
	 * @param value
	 * @return
	 */
	public static AdminSecurityEnum getSecurityEnumByValue(String value) {
		for (AdminSecurityEnum em : AdminSecurityEnum.values()) {
			if (em.getValue().equals(value)) {
				return em;
			}
		}
		return null;
	}

	/**
	 * 强转为AdminSecurityEnum
	 * 
	 * @param name
	 *            枚举的名称
	 * @return
	 */
	public static AdminSecurityEnum convertAdminSecurityEnum(String name) {
		try {
			AdminSecurityEnum em = AdminSecurityEnum.valueOf(name);
			return em;
		} catch (Exception e) {
			return null;
		}
	}

	public static String getFuncPk(String upPk, String func) {
		return StringUtil.appendStringNotNull(null, upPk, func);
	}

	/**
	 * 验证访问权限
	 * 
	 * @param em
	 * @param func
	 * @return
	 */
	public static boolean isPermitted(AdminSecurityEnum em, String func) {
		if (em == null) {
			return true;
		}
		String upPk = em.getDiscription();
		if (ALL_FUNC.equals(upPk)) {
			return true;
		}
		String funcPk = getFuncPk(upPk, func);
		Subject sj = SecurityUtils.getSubject();
		if (sj != null && sj.isAuthenticated()) {
			return sj.isPermitted(funcPk);
		}
		// 未登录
		return false;
	}
}
