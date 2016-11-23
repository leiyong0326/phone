package com.ly.base.core.model.sys;

import java.io.Serializable;

/**
 * 系统角色表
 * @author LeiYong
 *
 */
public class SysRole implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3694535472434563353L;

	private Integer pk;

    private String name;

    private String dsc;

    private String enable;

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDsc() {
        return dsc;
    }

    public void setDsc(String dsc) {
        this.dsc = dsc == null ? null : dsc.trim();
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable == null ? null : enable.trim();
    }
}