package com.ffi.onedriveservice.exception;

public class ApplicationBusinessException extends Exception{

	
protected static final long serialVersionUID = 1L;
	
	/**
	 *
	 */
	protected String logMessage = null;
	
	/**
	 * Instantiates a new ApplicationBusinessException.
	 */
	protected ApplicationBusinessException() {
		// hidden
	}

	
	/**
	 * Instantiates a new ApplicationBusinessException with a user message. (Needed
	 * by anything which subclasses this.)
	 *
	 * @param userMessage Message displayed to user.
	 */
	public ApplicationBusinessException(String userMessage) {
		// hidden
		super(userMessage);
	}
	
	/**
	 * Creates a new instance of ApplicationBusinessException. This exception is
	 * automatically logged, so that simply by using this API, applications will
	 * generate an extensive log. 
	 *
	 * @param userMessage the message displayed to the user
	 * @param logMessage  the message logged
	 */
	public ApplicationBusinessException(String userMessage, String logMessage) {
		super(userMessage);
		this.logMessage = logMessage;
	}
	
	/**
	 * Creates a new instance of ApplicationBusinessException that includes a root
	 * cause Throwable.
	 * 
	 * @param userMessage the message displayed to the user
	 * @param cause       the cause
	 */
	public ApplicationBusinessException(String userMessage, Throwable cause) {
		super(userMessage, cause);
	}
	
	/**
	 * Creates a new instance of ApplicationBusinessException that includes a root
	 * cause Throwable.
	 * 
	 * @param userMessage the message displayed to the user
	 * @param logMessage  the message logged
	 * @param cause       the cause
	 */
	public ApplicationBusinessException(String userMessage, String logMessage, Throwable cause) {
		super(userMessage, cause);
		this.logMessage = logMessage;
	}
	
	/**
	 * Returns message meant for display to users
	 * Note that if you are unsure of what set this message, it would probably be a
	 * good idea to encode this message before displaying it to the end user.
	 * 
	 * @return a String containing a message that is safe to display to users
	 */
	public String getUserMessage() {
		return getMessage();
	}

	/**
	 * Returns a message that is safe to display in logs, but may contain sensitive
	 * information and therefore probably should not be displayed to users.
	 * @return a String containing a message that is safe to display in logs, but
	 *         probably not to users as it may contain sensitive information.
	 */
	public String getLogMessage() {
		return logMessage;
	}
}
