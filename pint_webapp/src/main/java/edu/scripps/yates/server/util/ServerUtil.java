package edu.scripps.yates.server.util;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.server.configuration.PintConfigurationPropertiesIO;
import edu.scripps.yates.shared.configuration.PintConfigurationProperties;

public class ServerUtil {
	private static final Logger log = Logger.getLogger(ServerUtil.class);

	/**
	 * Gets the {@link Peptide} objects from the {@link Protein}s and organize
	 * them in a {@link Map} using the sequence as the key
	 *
	 * @param proteins
	 * @return
	 */
	public static Map<String, Set<Peptide>> getPeptideMapFromProteins(Map<String, Set<Protein>> proteins) {
		Map<String, Set<Peptide>> ret = new HashMap<String, Set<Peptide>>();

		for (Set<Protein> proteinSet : proteins.values()) {
			for (Protein protein : proteinSet) {
				final Set<Peptide> peptides = protein.getPeptides();
				for (Peptide peptide : peptides) {
					final String sequence = peptide.getSequence();
					if (ret.containsKey(sequence)) {
						ret.get(sequence).add(peptide);
					} else {
						Set<Peptide> set = new HashSet<Peptide>();
						set.add(peptide);
						ret.put(sequence, set);
					}
				}
			}
		}

		return ret;
	}

	/**
	 * Gets the {@link Peptide} objects from the {@link Protein}s and organize
	 * them in a {@link Map} using the sequence as the key
	 *
	 * @param proteins
	 * @return
	 */
	public static Map<String, Set<Peptide>> getPeptideMapFromProteins(Collection<Protein> proteins) {
		Map<String, Set<Peptide>> ret = new HashMap<String, Set<Peptide>>();

		for (Protein protein : proteins) {
			final Set<Peptide> peptides = protein.getPeptides();
			for (Peptide peptide : peptides) {
				final String sequence = peptide.getSequence();
				if (ret.containsKey(sequence)) {
					ret.get(sequence).add(peptide);
				} else {
					Set<Peptide> set = new HashSet<Peptide>();
					set.add(peptide);
					ret.put(sequence, set);
				}
			}
		}

		return ret;
	}

	public static boolean isTestServer() {
		Map<String, String> environmentalVariables = System.getenv();
		String testComputer = environmentalVariables.get(ServerConstants.PINT_DEVELOPER_ENV_VAR);
		if (testComputer != null && testComputer.equalsIgnoreCase("true")) {
			return true;
		}
		return false;
	}

	public static boolean isScrippsServer() {
		Map<String, String> environmentalVariables = System.getenv();
		String testComputer = environmentalVariables.get(ServerConstants.PINT_SCRIPPS_ENV_VAR);
		if (testComputer != null && testComputer.equalsIgnoreCase("true")) {
			return true;
		}
		return false;
	}

	public static File getPINTPropertiesFile(ServletContext context) {
		String pintPropertiesFile = ServerConstants.PINT_PROPERTIES_FILE_NAME;
		if (isTestServer()) {
			log.info("TEST SERVER DETECTED.");
			pintPropertiesFile = ServerConstants.PINT_TEST_PROPERTIES_FILE_NAME;
		}
		if (isScrippsServer()) {
			log.info("SCRIPPS SERVER DETECTED.");
			pintPropertiesFile = ServerConstants.PINT_SCRIPPS_PROPERTIES_FILE_NAME;
		}

		final File file = new File(context.getRealPath("/WEB-INF") + File.separator + pintPropertiesFile);
		log.info("Using properties file: " + file.getAbsolutePath());
		return file;
	}

	public static PintConfigurationProperties getPINTProperties(ServletContext context) {
		try {
			return PintConfigurationPropertiesIO.readProperties(getPINTPropertiesFile(context));
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException("Error getting PINT properties file");
	}

}
