package com.ly.base.core.model.sys;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 登录用户表
 * @author LeiYong
 *
 */
public class SysUser implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -9213995972439959901L;

	private String pk;
    private String loginName;//登录名
    private String phone;//手机号
    private String upPk;//上级编号
    private String upName;//上级名称
    private String password;//密码
    private String name;//姓名
    private String cardId;//身份证号
    private String cardAddress;//身份证地址
	private String address;//联系地址
    private BigDecimal ratio;//可提现金额
    private BigDecimal ratioTotal;//总金额
    private Integer score;//总积分
    private Integer saleTotal;//总销量
    private Date birthday;//生日
    private String email;
    private String qq;
    private Integer rolePk;//角色pk
    private String roleName;//角色名称
    private Integer accountPk;//会员级别
    private Date createTime;//创建时间
    private Date updateTime;//更新时间
    private Date updatePassword;//密码更新时间
    private Date loginTime;//密码更新时间
    private String enable;//是否启用
    private String face;//头像
    private String sex;//性别
    private String referrer;//推荐人
    private String referrerName;//推荐人姓名
    private String referrerPhone;//推荐人手机号
    private String alipay;//支付宝号
    private String wechat;//微信帐号
    //extends
    private String ip;
    private String os;
    private String browser;
    private String salt;	//shiro 
    private Integer count;	 
    private String accountName;

    public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
    public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk == null ? null : pk.trim();
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getUpPk() {
        return upPk;
    }

    public void setUpPk(String upPk) {
        this.upPk = upPk == null ? null : upPk.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId == null ? null : cardId.trim();
    }

    public String getCardAddress() {
        return cardAddress;
    }

    public void setCardAddress(String cardAddress) {
        this.cardAddress = cardAddress == null ? null : cardAddress.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public Integer getRolePk() {
        return rolePk;
    }

    public void setRolePk(Integer rolePk) {
        this.rolePk = rolePk;
    }

    public Integer getAccountPk() {
        return accountPk;
    }

    public void setAccountPk(Integer accountPk) {
        this.accountPk = accountPk;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getUpdatePassword() {
        return updatePassword;
    }

    public void setUpdatePassword(Date updatePassword) {
        this.updatePassword = updatePassword;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable == null ? null : enable.trim();
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face == null ? null : face.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer == null ? null : referrer.trim();
    }

    public String getReferrerName() {
        return referrerName;
    }

    public void setReferrerName(String referrerName) {
        this.referrerName = referrerName == null ? null : referrerName.trim();
    }

    public String getReferrerPhone() {
        return referrerPhone;
    }

    public void setReferrerPhone(String referrerPhone) {
        this.referrerPhone = referrerPhone == null ? null : referrerPhone.trim();
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay == null ? null : alipay.trim();
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat == null ? null : wechat.trim();
    }

	public String getUpName() {
		return upName;
	}

	public void setUpName(String upName) {
		this.upName = upName;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public BigDecimal getRatioTotal() {
		return ratioTotal;
	}

	public void setRatioTotal(BigDecimal ratioTotal) {
		this.ratioTotal = ratioTotal;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getSaleTotal() {
		return saleTotal;
	}

	public void setSaleTotal(Integer saleTotal) {
		this.saleTotal = saleTotal;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
}