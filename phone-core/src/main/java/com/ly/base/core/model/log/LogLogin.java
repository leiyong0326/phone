package com.ly.base.core.model.log;

import java.io.Serializable;
import java.util.Date;
/**
 * 登录历史
 * @author LeiYong
 *
 */
public class LogLogin implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2174143927398553059L;

	private Integer pk;

    private String createBy;
    private String createName;
    private String loginName;
    private String phone;

    private Date createTime;

    private String ip;

    private String country;

    private String province;

    private String city;

    private String area;

    private String adsl;

    private String from;

    private String browser;

    private String os;

    
    public LogLogin() {
		super();
	}
	

	public LogLogin(String createBy, String createName, String loginName, String phone, Date createTime, String ip,
			String country, String province, String city, String area, String adsl, String from, String browser,
			String os) {
		super();
		this.createBy = createBy;
		this.createName = createName;
		this.loginName = loginName;
		this.phone = phone;
		this.createTime = createTime;
		this.ip = ip;
		this.country = country;
		this.province = province;
		this.city = city;
		this.area = area;
		this.adsl = adsl;
		this.from = from;
		this.browser = browser;
		this.os = os;
	}

	public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getAdsl() {
        return adsl;
    }

    public void setAdsl(String adsl) {
        this.adsl = adsl == null ? null : adsl.trim();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from == null ? null : from.trim();
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser == null ? null : browser.trim();
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os == null ? null : os.trim();
    }

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}


	public String getLoginName() {
		return loginName;
	}


	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}
}