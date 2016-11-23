package com.ly.base.shiro.exception;

import org.apache.shiro.authc.AccountException;

public class SysDepartmentDisabledException extends AccountException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5460885470024927106L;

    /**
     * Creates a new AccountException.
     */
    public SysDepartmentDisabledException() {
        super();
    }

    /**
     * Constructs a new SysDepartmentDisabledException.
     *
     * @param message the reason for the exception
     */
    public SysDepartmentDisabledException(String message) {
        super(message);
    }

    /**
     * Constructs a new SysDepartmentDisabledException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public SysDepartmentDisabledException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new SysDepartmentDisabledException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public SysDepartmentDisabledException(String message, Throwable cause) {
        super(message, cause);
    }

}
