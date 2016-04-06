package edu.scripps.yates.census.analysis;

public class IntegrationException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8114555183071884569L;

	public static enum CODE {
		TIMEOUT
	};

	private final CODE code;

	public IntegrationException(CODE code, String message) {
		super(message);
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public CODE getCode() {
		return code;
	}
}
