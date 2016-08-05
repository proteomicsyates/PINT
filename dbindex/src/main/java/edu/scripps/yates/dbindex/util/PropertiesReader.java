package edu.scripps.yates.dbindex.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
	private static final String PROPERTIES_FILE_NAME = "dbindex.properties";
	public static final String DBINDEX_PATH = "dbindex.path";
	public static final String DBINDEX_PATH_SERVER = "dbindex.path.server";
	public static final String DEFAULT_INDEX_FACTOR = "default_index_factor";
	public static final String DEFAULT_IN_MEMORY_INDEX = "default_in_memory_index";
	public static final String DEFAULT_INDEX_TYPE = "default_index_type";
	public static final String DEFAULT_MAX_INTERNAL_CLEAVAGES_SITES = "default_max_internal_cleavages";
	public static final String MAX_PRECURSOR_MASS = "default_max_precursor_mass";
	public static final String MIN_PRECURSOR_MASS = "default_min_precursor_mass";
	public static final String USE_INDEX = "default_use_index";
	public static final String ENZYME_NOCUT_RESIDUES = "default_enzyme_nocut_residues";
	public static final String ENZYME_RESIDUES = "default_enzyme_residues";
	public static final String ENZYME_OFFSET = "default_enzyme_offset";
	public static final String MASS_TYPE_PARENT = "default_mass_type_parent";
	public static final String MASS_GROUP_FACTOR = "mass_group_factor";
	public static final String ADD_H2O_PLUS_PROTON = "add_h2o_plus_proton";
	public static final String MANDATORY_INTERNAL_AAs = "mandatory_internal_AAs";
	public static final String SEMICLEAVAGE = "default_semicleavage";

	public static Properties getProperties() throws Exception {
		ClassLoader cl = PropertiesReader.class.getClassLoader();
		InputStream is;

		is = cl.getResourceAsStream(PROPERTIES_FILE_NAME);
		if (is == null)
			throw new Exception(PROPERTIES_FILE_NAME + " file not found");

		Properties prop = new Properties();
		try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
		return prop;
	}
}
