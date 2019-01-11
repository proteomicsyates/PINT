package edu.scripps.yates.server.util;

public class ServerConstants {
	public static final String PINT_DEVELOPER_ENV_VAR = "SERVER_TEST";
	public static final String PINT_SCRIPPS_ENV_VAR = "SCRIPPS_SERVER";

	public static final String PROJECT_XML_CFG = "import_schema_0.7.xsd";

	/**
	 * Constants for reading default view file
	 */
	public static final String DEFAULT_VIEW_COLUMNS = "COL";
	public static final String DEFAULT_VIEW_METADATA = "MT";
	public static final String DEFAULT_QUERY_STRING = "default query string";
	public static final String PROTEIN_GROUP_COLUMNS = "protein group columns";
	public static final String END_COLUMNS = "end columns";
	public static final String PROTEIN_COLUMNS = "protein columns";
	public static final String PSM_COLUMNS = "psm columns";
	public static final String PROTEINS_SORTED_BY = "proteins sorted by";
	public static final String PROTEIN_GROUPS_SORTED_BY = "protein groups sorted by";
	public static final String PSMS_SORTED_BY = "psms sorted by";
	public static final String DEFAULT_TAB = "default tab";
	public static final String PROTEIN_ORDER = "protein order";
	public static final String PROTEIN_GROUP_ORDER = "protein group order";
	public static final String PSM_ORDER = "psm order";
	public static final String PROTEIN_GROUP_SORTING_SCORE = "protein group sorting score";
	public static final String PROTEIN_SORTING_SCORE = "protein sorting score";
	public static final String PSM_SORTING_SCORE = "psm sorting score";
	public static final String PROJECT_DESCRIPTION = "project description";
	public static final String END_PROJECT_DESCRIPTION = "end project description";
	public static final String PROJECT_INSTRUCTIONS = "project instructions";
	public static final String END_PROJECT_INSTRUCTIONS = "end project instructions";
	public static final String PROJECT_VIEW_COMMENTS = "project view comments";
	public static final String END_PROJECT_VIEW_COMMENTS = "end project view comments";
	public static final String PROTEIN_PAGE_SIZE = "protein table page size";
	public static final String PROTEIN_GROUP_PAGE_SIZE = "protein group table page size";
	public static final String PSM_PAGE_SIZE = "psm table page size";
	public static final String HIDDEN_PTMS = "hidden ptms";
	public static final String PEPTIDES_SORTED_BY = "peptides sorted by";
	public static final String PEPTIDE_ORDER = "peptide order";
	public static final String PEPTIDE_SORTING_SCORE = "peptide sorting score";
	public static final String PEPTIDE_PAGE_SIZE = "peptide table page size";
	public static final String PEPTIDE_COLUMNS = "peptide columns";

	// this file should be located at the PINT_HOME_FOLDER
	public static final String PINT_PROPERTIES_FILE_NAME = "pint.properties";
	public static final String PINT_TEST_PROPERTIES_FILE_NAME = "pint_test.properties";
	public static final String PINT_LATEST_DELETED_PROJECTS_FILE = "pint_latest_deleted_projects.txt";

	public static boolean psmCentricByDefault = false;

}
