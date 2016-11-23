package com.ly.base.shiro;

import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

public class SysUserAuthenticationInfo extends SimpleAuthenticationInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6683549233672308378L;
//	private SysUser sysUser;
//	private SysOrganization sysOrganization;

	
//	public SysUserAuthenticationInfo(Object principal, Object credentials, String realmName,
//			SysOrganization sysOrganization) {
//		super(principal, credentials, realmName);
//		this.sysOrganization = sysOrganization;
//	}
	public SysUserAuthenticationInfo() {
		super();
	}

	public SysUserAuthenticationInfo(PrincipalCollection principals, Object credentials) {
		super(principals, credentials);
	}

	public SysUserAuthenticationInfo(Object principal, Object credentials, String realmName) {
		super(principal, credentials, realmName);
	}

	public SysUserAuthenticationInfo(PrincipalCollection principals, Object hashedCredentials,
			ByteSource credentialsSalt) {
		super(principals, hashedCredentials, credentialsSalt);
	}
	
	public SysUserAuthenticationInfo(Object principal, Object hashedCredentials, ByteSource credentialsSalt,
			String realmName) {
		super(principal, hashedCredentials, credentialsSalt, realmName);
	}

//	public SysUser getSysUser() {
//		return sysUser;
//	}
//	public SysUserAuthenticationInfo setSysUser(SysUser sysUser) {
//		this.sysUser = sysUser;
//		return this;
//	}
//	public UsrUser getUsrUser() {
//		return usrUser;
//	}
//	public SysUserAuthenticationInfo setUsrUser(UsrUser usrUser) {
//		this.usrUser = usrUser;
//		return this;
//	}
//	public SysOrganization getSysOrganization() {
//		return sysOrganization;
//	}
//	public SysUserAuthenticationInfo setSysOrganization(SysOrganization sysOrganization) {
//		this.sysOrganization = sysOrganization;
//		return this;
//	}
	
}
