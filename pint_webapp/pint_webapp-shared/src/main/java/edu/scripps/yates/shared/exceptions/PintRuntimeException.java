package edu.scripps.yates.shared.exceptions;

import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;

public class PintRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7892527852044744594L;
	private PINT_ERROR_TYPE pintErrorType;

	public PintRuntimeException() {
		super();
	}

	public PintRuntimeException(PINT_ERROR_TYPE pintErrorType) {
		super(pintErrorType.name());
		this.pintErrorType = pintErrorType;
	}

	public PintRuntimeException(String message, PINT_ERROR_TYPE pintErrorType) {
		super(message);
		this.pintErrorType = pintErrorType;
	}

	public PintRuntimeException(Exception ex, PINT_ERROR_TYPE pintErrorType) {
		super(ex);
		this.pintErrorType = pintErrorType;
	}

	public PINT_ERROR_TYPE getPintErrorType() {
		return pintErrorType;
	}

	/**
	 * @param pintErrorType
	 *            the pintErrorType to set
	 */
	public void setPintErrorType(PINT_ERROR_TYPE pintErrorType) {
		this.pintErrorType = pintErrorType;
	}
}
