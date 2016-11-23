package com.ly.base.core.model.sys;

import java.io.Serializable;

/**
 * 系统菜单表
 * @author LeiYong
 *
 */
public class SysMenu implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3172810618302759201L;

	private String pk;

    private String upPk;

    private String name;

    private String text;

    private String url;

    private String icon;

    private Integer seq;

    private String menutype;

    private String enable;

    private String dsc;

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk == null ? null : pk.trim();
    }

    public String getUpPk() {
        return upPk;
    }

    public void setUpPk(String upPk) {
        this.upPk = upPk == null ? null : upPk.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text == null ? null : text.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getMenutype() {
        return menutype;
    }

    public void setMenutype(String menutype) {
        this.menutype = menutype == null ? null : menutype.trim();
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable == null ? null : enable.trim();
    }

    public String getDsc() {
        return dsc;
    }

    public void setDsc(String dsc) {
        this.dsc = dsc == null ? null : dsc.trim();
    }
}