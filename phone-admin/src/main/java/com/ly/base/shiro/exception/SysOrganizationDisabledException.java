package com.ly.base.shiro.exception;

import org.apache.shiro.authc.AccountException;

public class SysOrganizationDisabledException extends AccountException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5460885470024927106L;

    /**
     * Creates a new AccountException.
     */
    public SysOrganizationDisabledException() {
        super();
    }

    /**
     * Constructs a new SysOrganizationDisabledException.
     *
     * @param message the reason for the exception
     */
    public SysOrganizationDisabledException(String message) {
        super(message);
    }

    /**
     * Constructs a new SysOrganizationDisabledException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public SysOrganizationDisabledException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new SysOrganizationDisabledException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public SysOrganizationDisabledException(String message, Throwable cause) {
        super(message, cause);
    }

}
