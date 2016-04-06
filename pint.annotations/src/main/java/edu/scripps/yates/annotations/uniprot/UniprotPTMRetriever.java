package edu.scripps.yates.annotations.uniprot;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.annotations.uniprot.xml.EvidenceType;
import edu.scripps.yates.annotations.uniprot.xml.FeatureType;

public class UniprotPTMRetriever {
	private static UniprotPTMRetriever instance;
	private final boolean useIndex;
	private final File uniprotReleasesFolder;
	private final UniprotProteinLocalRetriever uplr;
	private final static String MODIFIED_RESIDUE = "modified residue";

	private UniprotPTMRetriever(File uniprotReleasesFolder2, boolean useIndex2) {
		uniprotReleasesFolder = uniprotReleasesFolder2;
		useIndex = useIndex2;
		uplr = new UniprotProteinLocalRetriever(uniprotReleasesFolder, useIndex);
	}

	public static UniprotPTMRetriever getInstance(File uniprotReleasesFolder, boolean useIndex) {
		boolean createNewInstance = false;
		if (instance == null) {
			createNewInstance = true;
		} else if (Boolean.compare(instance.useIndex, useIndex) != 0
				|| !uniprotReleasesFolder.getAbsolutePath().equals(instance.uniprotReleasesFolder.getAbsoluteFile())) {
			createNewInstance = true;
		}
		if (createNewInstance) {
			instance = new UniprotPTMRetriever(uniprotReleasesFolder, useIndex);
		}

		return instance;
	}

	/**
	 * Gets a Map in which the key is the provided Uniprot accessions and the
	 * values of the Map are also Maps with keys: the position of the
	 * modification and the values are the names of the modifications.<br>
	 *
	 *
	 * @param uniprotVersion
	 *            the version of the Uniprot you want to use in format: YYYY_MM.
	 *            If it is not the current one (taken from
	 *            ftp://ftp.uniprot.org/
	 *            pub/databases/uniprot/current_release/knowledgebase
	 *            /complete/reldate.txt), it only will work if the annotations
	 *            for these proteins were already retrieved before using the
	 *            index.
	 * @param uniprotAccs
	 * @return
	 */
	public Map<String, Map<Integer, PTMInformation>> getPTMsFromUniprotAccs(String uniprotVersion,
			Collection<String> uniprotAccs) {
		final Map<String, Entry> annotatedProteins = uplr.getAnnotatedProteins(uniprotVersion, uniprotAccs);
		Map<String, Map<Integer, PTMInformation>> ret = new HashMap<String, Map<Integer, PTMInformation>>();

		for (String uniprotAcc : uniprotAccs) {
			Map<Integer, PTMInformation> map = new HashMap<Integer, PTMInformation>();
			if (annotatedProteins.containsKey(uniprotAcc)) {
				final Entry entry = annotatedProteins.get(uniprotAcc);
				final List<FeatureType> features = entry.getFeature();
				if (features != null) {
					for (FeatureType featureType : features) {
						if (featureType.getType().equals(MODIFIED_RESIDUE)) {
							int position = featureType.getLocation().getPosition().getPosition().intValue();
							String proteinName = null;
							if (entry.getProtein() != null && entry.getProtein().getRecommendedName() != null
									&& entry.getProtein().getRecommendedName().getFullName() != null
									&& entry.getProtein().getRecommendedName().getFullName().getValue() != null) {
								proteinName = entry.getProtein().getRecommendedName().getFullName().getValue();
							}
							PTMInformation ptm = new PTMInformation(uniprotAcc, featureType.getDescription(),
									proteinName, position, entry);
							final List<Integer> evidences = featureType.getEvidence();
							// add evidences
							if (evidences != null) {
								for (Integer evidenceKey : evidences) {
									if (evidenceKey != null) {
										EvidenceType evidenceType = getEvidence(entry, evidenceKey);
										if (evidenceType != null) {
											ptm.addEvidence(evidenceType);
										}
									}
								}
							}
							map.put(position, ptm);
						}
					}
				}
			}
			ret.put(uniprotAcc, map);
		}

		return ret;
	}

	private EvidenceType getEvidence(Entry entry, Integer evidenceKey) {
		if (entry != null && evidenceKey != null) {
			final List<EvidenceType> evidences = entry.getEvidence();
			if (evidences != null) {
				for (EvidenceType evidenceType : evidences) {
					if (Integer.compare(evidenceType.getKey().intValue(), evidenceKey) == 0) {
						return evidenceType;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Gets a Map in which the key is the provided Uniprot accessions and the
	 * values of the Map are also Maps with keys: the position of the
	 * modification and the values are the names of the modifications.<br>
	 * It will get the current Uniprot version.
	 *
	 * @param uniprotAccs
	 * @return
	 */
	public Map<String, Map<Integer, PTMInformation>> getPTMsFromUniprotAccs(Collection<String> uniprotAccs) {
		return getPTMsFromUniprotAccs(null, uniprotAccs);
	}

}
