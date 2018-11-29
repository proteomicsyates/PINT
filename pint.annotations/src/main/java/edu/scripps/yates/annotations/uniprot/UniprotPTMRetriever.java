package edu.scripps.yates.annotations.uniprot;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.annotations.uniprot.xml.EvidenceType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.FeatureType;
import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public class UniprotPTMRetriever {
	private static UniprotPTMRetriever instance;
	private final boolean useIndex;
	private final File uniprotReleasesFolder;
	private final UniprotProteinLocalRetriever uplr;
	private final static String MODIFIED_RESIDUE = "modified residue";

	private UniprotPTMRetriever(File uniprotReleasesFolder2, boolean useIndex, boolean ignoreReferences,
			boolean ignoreDBReferences) {
		uniprotReleasesFolder = uniprotReleasesFolder2;
		this.useIndex = useIndex;
		uplr = new UniprotProteinLocalRetriever(uniprotReleasesFolder, useIndex, ignoreReferences, ignoreDBReferences);
	}

	public static UniprotPTMRetriever getInstance(File uniprotReleasesFolder, boolean useIndex,
			boolean ignoreReferences, boolean ignoreDBReferences) {
		boolean createNewInstance = false;
		if (instance == null) {
			createNewInstance = true;
		} else if (Boolean.compare(instance.useIndex, useIndex) != 0
				|| !uniprotReleasesFolder.getAbsolutePath().equals(instance.uniprotReleasesFolder.getAbsoluteFile())) {
			createNewInstance = true;
		}
		if (createNewInstance) {
			instance = new UniprotPTMRetriever(uniprotReleasesFolder, useIndex, ignoreReferences, ignoreDBReferences);
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
	public Map<String, TIntObjectHashMap<PTMInformation>> getPTMsFromUniprotAccs(String uniprotVersion,
			Collection<String> uniprotAccs) {
		final Map<String, Entry> annotatedProteins = uplr.getAnnotatedProteins(uniprotVersion, uniprotAccs);
		final Map<String, TIntObjectHashMap<PTMInformation>> ret = new THashMap<String, TIntObjectHashMap<PTMInformation>>();

		for (final String uniprotAcc : uniprotAccs) {
			final TIntObjectHashMap<PTMInformation> map = new TIntObjectHashMap<PTMInformation>();
			if (annotatedProteins.containsKey(uniprotAcc)) {
				final Entry entry = annotatedProteins.get(uniprotAcc);
				final List<FeatureType> features = entry.getFeature();
				if (features != null) {
					for (final FeatureType featureType : features) {
						if (featureType.getType().equals(MODIFIED_RESIDUE)) {
							final int position = featureType.getLocation().getPosition().getPosition().intValue();
							String proteinName = null;
							if (entry.getProtein() != null && entry.getProtein().getRecommendedName() != null
									&& entry.getProtein().getRecommendedName().getFullName() != null
									&& entry.getProtein().getRecommendedName().getFullName().getValue() != null) {
								proteinName = entry.getProtein().getRecommendedName().getFullName().getValue();
							}
							final PTMInformation ptm = new PTMInformation(uniprotAcc, featureType.getDescription(),
									proteinName, position, entry);
							final List<Integer> evidences = featureType.getEvidence();
							// add evidences
							if (evidences != null) {
								for (final Integer evidenceKey : evidences) {
									if (evidenceKey != null) {
										final EvidenceType evidenceType = getEvidence(entry, evidenceKey);
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
				for (final EvidenceType evidenceType : evidences) {
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
	public Map<String, TIntObjectHashMap<PTMInformation>> getPTMsFromUniprotAccs(Collection<String> uniprotAccs) {
		return getPTMsFromUniprotAccs(null, uniprotAccs);
	}

}
