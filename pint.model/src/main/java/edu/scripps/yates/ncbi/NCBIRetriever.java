package edu.scripps.yates.ncbi;

import edu.scripps.yates.utilities.properties.PropertiesUtil;
import edu.scripps.yates.utilities.proteomicsmodel.enums.AccessionType;

public class NCBIRetriever {
	private static final String NCBI_SERVER_URL = "ncbi.server.url";
	private static final String NCBI_SERVER_SUFIX = "ncbi.server.url.sufix";

	public static String getNCBIProteinNameFromAccession(
			edu.scripps.yates.utilities.proteomicsmodel.Accession accession) {
		if (accession.getAccessionType() == AccessionType.NCBI) {
			String text = getFastaHeaderFromNCBI(accession.getAccession());
			return text;
		}
		return null;
	}

	private static String getFastaHeaderFromNCBI(String accession) {
		final String ncbiServerURL = PropertiesUtil
				.getPropertyValue(NCBI_SERVER_URL);
		final String ncbiServerURLSuffix = PropertiesUtil
				.getPropertyValue(NCBI_SERVER_SUFIX);
		String urlString = ncbiServerURL + accession + ncbiServerURLSuffix;

		return null;
	}
}
