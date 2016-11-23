package com.ly.base.common.model;

public class AddressBean {
	private String country;
	private String area;
	private String region;
	private String city;
	private String county;
	private String isp;
	/**
	 * 获取国家
	 * @return
	 */
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * 获取区域
	 * @return
	 */
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	/**
	 * 获取地域(华东,华南)
	 * @return
	 */
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	/**
	 * 获取城市
	 * @return
	 */
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * 区县
	 * @return
	 */
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	/**
	 * 获取入网运营商
	 * @return
	 */
	public String getIsp() {
		return isp;
	}
	public void setIsp(String isp) {
		this.isp = isp;
	}

}
