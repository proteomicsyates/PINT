package edu.scripps.yates.annotations.uniprot;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * The idea of this class is to use it as a configurator once, and then, get the
 * instance without the need of stating the uniprotReleaseFolder and the
 * useIndex
 * 
 * @author Salva
 * 
 */
public class UniprotProteinRetrievalSettings {
	private final File uniprotReleasesFolder;
	private final boolean useIndex;
	private static UniprotProteinRetrievalSettings instance;
	private final static Logger log = Logger
			.getLogger(UniprotProteinLocalRetriever.class);

	private UniprotProteinRetrievalSettings(File uniprotReleasesFolder,
			boolean useIndex) {
		this.uniprotReleasesFolder = uniprotReleasesFolder;
		this.useIndex = useIndex;
	}

	public static UniprotProteinRetrievalSettings getInstance() {
		if (instance != null) {
			return instance;
		}
		throw new IllegalArgumentException(
				"You MUST call UniprotProteinRetrievalSettings.getInstance(uniprotReleasesFolder, useIndex) first before to call getInstance()");
	}

	public static UniprotProteinRetrievalSettings getInstance(
			File uniprotReleasesFolder, boolean useIndex) {
		log.info("Getting instance of UniprotRetrievalSettings with:");
		log.info("Uniprot folder: " + uniprotReleasesFolder.getAbsolutePath());
		log.info("Use index: " + useIndex);
		instance = new UniprotProteinRetrievalSettings(uniprotReleasesFolder,
				useIndex);
		return instance;
	}

	/**
	 * @return the uniprotReleasesFolder
	 */
	public File getUniprotReleasesFolder() {
		return uniprotReleasesFolder;
	}

	/**
	 * @return the useIndex
	 */
	public boolean isUseIndex() {
		return useIndex;
	}
}
