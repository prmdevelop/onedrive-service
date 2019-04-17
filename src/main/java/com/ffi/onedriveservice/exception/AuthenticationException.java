package com.ffi.onedriveservice.exception;

/**
 * An AuthenticationException should be thrown when anything goes wrong during
 * login or logout. They are also appropriate for any problems related to
 * identity management.
 */
public class AuthenticationException extends ApplicationBusinessException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new authentication exception.
	 */
	protected AuthenticationException() {
		// hidden
	}

	/**
	 * Creates a new instance of {@code AuthenticationException}.
	 * 
	 * @param userMessage the message displayed to the user
	 * @param logMessage  the message logged
	 */
	public AuthenticationException(String userMessage, String logMessage) {
		super(userMessage, logMessage);
	}

	/**
	 * Instantiates a new authentication exception.
	 * 
	 * @param userMessage the message displayed to the user
	 * @param logMessage  the message logged
	 * @param cause       the root cause
	 */
	public AuthenticationException(String userMessage, String logMessage, Throwable cause) {
		super(userMessage, logMessage, cause);
	}
}

