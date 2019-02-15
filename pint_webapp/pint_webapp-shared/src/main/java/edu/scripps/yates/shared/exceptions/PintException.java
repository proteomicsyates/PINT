package edu.scripps.yates.shared.exceptions;

import java.io.Serializable;

public class PintException extends Exception implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -905169250144352342L;
	private PINT_ERROR_TYPE pintErrorType;

	public static enum PINT_ERROR_TYPE {
		MAX_NUM_PSMS_EXCEDED, //
		IMPORT_PROCESS_ID_NOT_FOUND, //
		FILE_NOT_FOUND_IN_SERVER, //
		REMOTE_FILE_NOT_REACHABLE, //
		PROJECT_ALREADY_IN_SERVER, //
		REMOTE_SERVER_NOT_REACHABLE, //
		FILE_NOT_SUPPORTED_FOR_THIS_CALL, //
		FILE_READING_ERROR, INTERNAL_ERROR, //
		IMPORT_XML_SCHEMA_ERROR, //
		MOVING_FILE_ERROR, //
		LOGIN_FAILED, //
		QUERY_ERROR, //
		MISSING_INFORMATION, //
		DATA_EXPORTER_ERROR, //
		PROJECT_NOT_FOUND, //
		DB_ACCESS_ERROR, //
		TEXT_FORMAT_ERROR, //
		REACTOME_INPUT_FILE_GENERATION_ERROR, //
		THREAD_INTERRUPTED, //
		VALUE_NOT_SUPPORTED, //
		MAXIMUM_NUMBER_OF_PROJECTS_LOADED_AT_A_TIME, //
		FORMATTER_MISSING_IN_SHARED_MODULE, //
		ITEM_ID_REPEATED, //
		WIZARD_PAGE_INCOMPLETE, //
		PARSING_ERROR

	};

	public PintException() {
		super();
	}

	public PintException(String message, PINT_ERROR_TYPE pintErrorType) {
		super(message);
		this.pintErrorType = pintErrorType;
	}

	public PintException(Exception ex, PINT_ERROR_TYPE pintErrorType) {
		super(ex);
		this.pintErrorType = pintErrorType;
	}

	/**
	 * @return the pintErrorType
	 */
	public PINT_ERROR_TYPE getPintErrorType() {
		return pintErrorType;
	}

	/**
	 * @param pintErrorType the pintErrorType to set
	 */
	public void setPintErrorType(PINT_ERROR_TYPE pintErrorType) {
		this.pintErrorType = pintErrorType;
	}

}
