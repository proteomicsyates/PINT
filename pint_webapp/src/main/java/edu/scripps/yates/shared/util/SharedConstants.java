package edu.scripps.yates.shared.util;

public class SharedConstants {
	public static final String UNIPROT_ACC_LINK = "http://www.uniprot.org/uniprot/";
	public static final String NCBI_PROTEIN_LINK = "http://www.ncbi.nlm.nih.gov/protein/";
	public static final String GENENAMES_LINK = "http://www.genenames.org/cgi-bin/gene_symbol_report?hgnc_id=";

	public final static String EXCEL_ID_SEPARATOR = "##";
	public static final String JOB_ID_PARAM = "jobID";
	public static final String FILE_ID_PARAM = "fileID";
	public static final String FILE_FORMAT = "format";
	public static final String TAXONOMY_LINK_BY_ID = "http://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?id=";
	public static final String OMIM_ENTRY_LINK = "http://omim.org/entry/";
	public static final String REACTOME_ENTRY_LINK = "http://www.reactome.org/content/detail/";

	public static final String FILE_TYPE = "fileType";
	public static final String ID_DATA_FILE_TYPE = "idDataFile";
	public static final String REACTOME_ANALYSIS_RESULT_FILE_TYPE = "reactomeAnalysisResultFile";
	public static final String IMPORT_CFG_FILE_TYPE = "importCfgFile";
	public static final String FILE_TO_DOWNLOAD = "filetodownload";
	public static final char INFINITY_CHAR_CODE = '\u221e';

	public static final String NEW_LINE_JAVA = "\n";
	public static final String NEW_LINE_HTML = "<br>";
	public static final String SEPARATOR = NEW_LINE_JAVA;

	public static final String PINT_DEVELOPER_ENV_VAR = "PINT_DEVELOPER";
	public static final int PSM_DEFAULT_PAGE_SIZE = 200;
	public static final int PEPTIDE_DEFAULT_PAGE_SIZE = 200;
	public static final int PROTEIN_DEFAULT_PAGE_SIZE = 50;
	public static final int PROTEINGROUP_DEFAULT_PAGE_SIZE = 50;
	public static final int REACTOME_PATHWAYS_DEFAULT_PAGE_SIZE = 15;

	public static final String PSEA_QUANT_DATA_FILE_TYPE = "psea_quant_input";
	// footers of the data view tables. to enable set to true
	public static final boolean FOOTERS_ENABLED = false;
	// enable or disable the daemon tasks in PintserverDaemon.java
	public static final boolean DAEMON_TASKS_ENABLED = false;

	// for development purposes. Set to Integer.MAX_VALUE in production mode.
	public static final int MAX_NUM_PROTEINS = Integer.MAX_VALUE;

	// change this values to activate or deactivate the caches at server and/or
	// client sides
	public static boolean SERVER_CACHE_ENABLED = true;
	public static boolean CLIENT_CACHE_ENABLED = true;

	// aligment of peptides
	public static final int DEFAULT_ALIGNMENT_SCORE = 20;
	public static final double DEFAULT_SIMILARITY_SCORE = 70; // in %
	public static final int MAX_NUMBER_PARALLEL_PROCESSES = 8;

	// enable send emails
	public static final boolean EMAIL_ENABLED = false;

	// tables selection model
	public static final boolean TABLE_WITH_MULTIPLE_SELECTION = false;
}
