package com.ly.base.core.excel.export.ext;

import java.util.List;

import com.ly.base.common.util.DateUtil;
import com.ly.base.core.excel.export.ExcelExportSuper;
import com.ly.base.core.model.sys.SysUser;
/**
 * 导出
 * @author LeiYong
 *
 */
public class SysUserExcelExport extends ExcelExportSuper<SysUser> {
	
	public SysUserExcelExport(List<SysUser> data, SysUser conditions) {
		super(data, conditions);
	}

	@Override
	protected Object formatValue(SysUser t, String key) {
		switch (key) {
		case "sex":
			// 0-女 1-男
			return "0".equals(t.getSex()) ? WOMAN :MAN;
		case "enable":
			// 0-否 1-是
			return "0".equals(t.getEnable()) ? NO :YES;
		case "createTime":
			return t.getCreateTime() == null ? UNDEFINED : DateUtil.format(t.getCreateTime());
		case "updateTime":
			return t.getUpdateTime() == null ? UNDEFINED : DateUtil.format(t.getUpdateTime());
		}
		return null;
	}

	@Override
	protected String formatCondition(SysUser t) {
		StringBuffer sb = new StringBuffer();
		if (t.getName()!=null) {
			sb.append("姓名:");
			sb.append(t.getName());
			sb.append(BANK);
		}
		if (t.getPhone()!=null) {
			sb.append("手机号:");
			sb.append(t.getPhone());
			sb.append(BANK);
		}
		if (t.getSex()!=null) {
			sb.append("性别:");
			if ("0".equals(t.getSex())) sb.append(WOMAN);
			else if ("1".equals(t.getSex())) sb.append(MAN);
			else sb.append(UNDEFINED);
			sb.append(BANK);
		}
		if (t.getEnable()!=null) {
			sb.append("是否启用:");
			sb.append(t.getEnable());
			sb.append(BANK);
		}
		return sb.toString();
	}

	@Override
	protected String getReportName() {
		return "系统用户列表";
	}

}
