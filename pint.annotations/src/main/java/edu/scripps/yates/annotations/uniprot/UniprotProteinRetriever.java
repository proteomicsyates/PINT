package edu.scripps.yates.annotations.uniprot;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.annotations.uniprot.xml.SequenceType;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.utils.ModelUtils;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class UniprotProteinRetriever {
	private final static Logger log = Logger.getLogger(UniprotProteinRetriever.class);
	private static final Map<String, Protein> cachedProteins = new THashMap<String, Protein>();
	private static final Set<String> notAvailableProteins = new THashSet<String>();
	public static boolean enableCache = true;
	private final UniprotProteinLocalRetriever uniprotLocalRetriever;
	private final String uniprotVersion;

	public UniprotProteinRetriever(String uniprotVersion, File uniprotReleasesFolder, boolean useIndex) {
		this(uniprotVersion, uniprotReleasesFolder, useIndex, false, false);
	}

	public UniprotProteinRetriever(String uniprotVersion, File uniprotReleasesFolder, boolean useIndex,
			boolean ignoreReferences, boolean ignoreDBReferences) {
		uniprotLocalRetriever = new UniprotProteinLocalRetriever(uniprotReleasesFolder, useIndex, ignoreReferences,
				ignoreDBReferences);
		this.uniprotVersion = uniprotVersion;
	}

	public void setCacheEnabled(boolean enableCache) {
		uniprotLocalRetriever.setCacheEnabled(enableCache);
	}

	public Set<String> getUniprotVersionsForProjects(Map<String, Date> uploadDatesByProjectTags) {
		return uniprotLocalRetriever.getUniprotVersionsForProjects(uploadDatesByProjectTags);
	}

	private Map<String, Protein> getFromCache(Collection<String> accessions) {
		final Map<String, Protein> ret = new THashMap<String, Protein>();
		for (final String accession : accessions) {
			ret.putAll(getFromCache(accession));
		}
		return ret;
	}

	private Map<String, Protein> getFromCache(String accession) {
		final Map<String, Protein> ret = new THashMap<String, Protein>();

		if (enableCache && cachedProteins.containsKey(accession)) {
			ret.put(accession, cachedProteins.get(accession));

		}
		if (notAvailableProteins.contains(accession)) {
			ret.put(accession, null);
		}

		return ret;
	}

	private void addToCache(Map<String, Protein> queryProteins) {
		if (enableCache) {
			cachedProteins.putAll(queryProteins);
		}
	}

	public Map<String, String> getAnnotatedProteinSequence(Collection<String> accessions) {
		final Map<String, String> ret = new THashMap<String, String>();
		final Set<String> missingAccessions = new THashSet<String>();
		if (accessions != null && !accessions.isEmpty()) {
			// discard the proteins that were retrieved before and there was no
			// sequence
			for (final String accession : accessions) {
				if (!notAvailableProteins.contains(accession)) {
					missingAccessions.add(accession);
				} else {
					log.warn("Protein " + accession
							+ " was already search for its sequence in Uniprot and it was not found");
				}
			}
			if (!missingAccessions.isEmpty()) {
				final Map<String, Entry> annotatedProteins = uniprotLocalRetriever.getAnnotatedProteins(uniprotVersion,
						missingAccessions);
				if (!annotatedProteins.isEmpty()) {
					for (final String acc : accessions) {
						if (annotatedProteins.containsKey(acc)) {
							final Entry entry = annotatedProteins.get(acc);
							final SequenceType uniprotSeq = entry.getSequence();
							if (uniprotSeq != null) {
								String actualSeq = uniprotSeq.getValue().trim();
								if (actualSeq != null && !"".equals(actualSeq)) {
									actualSeq = actualSeq.replaceAll("\n", "");
									actualSeq = actualSeq.replaceAll(" ", "");
									ret.put(acc, actualSeq);

								}
							} else {
								log.warn("There is entry in Uniprot, but no sequence for protein: " + acc);
								notAvailableProteins.add(acc);
							}
						} else {
							log.warn("There is no uniprot sequence for protein: " + acc);
							notAvailableProteins.add(acc);
						}
					}
				}
			}
		}
		return ret;
	}

	public Map<String, Protein> getAnnotatedProteins(Collection<String> accessions) {
		final Map<String, Protein> ret = getFromCache(accessions);
		if (accessions.size() > 1) {
			// do not print this if only one
			log.info("Looking for " + accessions.size() + " proteins in Uniprot");
		}
		Set<String> missingAccessions = UniprotProteinLocalRetriever.getMissingAccessions(accessions, ret);
		if (!missingAccessions.isEmpty()) {
			final Map<String, Entry> annotatedProteins = uniprotLocalRetriever.getAnnotatedProteins(uniprotVersion,
					missingAccessions);
			if (!annotatedProteins.isEmpty()) {
				final Map<String, Protein> proteins = convertUniprotEntries2Proteins(annotatedProteins.values());
				addToCache(proteins);
				ret.putAll(proteins);
			} else {
				// even not finding the proteins, store them in the cache in
				// order to not look for them again
				final Map<String, Protein> nullProteins = new THashMap<String, Protein>();
				for (final String acc : missingAccessions) {
					nullProteins.put(acc, null);
				}
				addToCache(nullProteins);
			}
			// this.missingAccessions.clear();
			missingAccessions = UniprotProteinLocalRetriever.getMissingAccessions(accessions, ret);
			if (!missingAccessions.isEmpty()) {
				// add to missing accession in order to avoid to search for them
				// again
				log.debug("Adding " + missingAccessions.size()
						+ " missing accessions to the notAvailableProteins variable");
				notAvailableProteins.addAll(missingAccessions);
			}
			return ret;
		} else {
			// this.missingAccessions.clear();
			return ret;
		}

	}

	public Map<String, Entry> getAnnotatedUniprotEntries(Collection<String> accessions) {

		return uniprotLocalRetriever.getAnnotatedProteins(uniprotVersion, accessions);
	}

	/**
	 * Returns a map by accession. Note that even if the entry by the accession
	 * is there, the value may be null if has not been found
	 *
	 * @param accession
	 * @return
	 */
	public Map<String, Protein> getAnnotatedProtein(String accession) {
		final Set<String> set = new THashSet<String>();
		set.add(accession);
		return getAnnotatedProteins(set);

	}

	// /**
	// * Gets the accessions which were not able to be retrieved
	// *
	// * @return
	// */
	// public Collection<String> getMissingAccessions() {
	// return missingAccessions;
	// }

	public static Map<String, Protein> convertUniprotEntries2Proteins(Collection<Entry> entries) {
		final Map<String, Protein> ret = new THashMap<String, Protein>();
		// log.info(entries.size() + " entries found");
		for (final Entry entry : entries) {
			if (Thread.currentThread().isInterrupted()) {
				throw new RuntimeException("Thread interrupted.");
			}
			final Protein protein = new ProteinImplFromUniprotEntry(entry);
			ret.put(protein.getPrimaryAccession().getAccession(), protein);
			// index by all accessions
			final List<Accession> uniprotAccs = ModelUtils.getAccessions(protein, AccessionType.UNIPROT);
			if (uniprotAccs != null) {
				for (final Accession acc : uniprotAccs) {
					ret.put(acc.getAccession(), protein);
				}
			}
		}
		log.debug(ret.size() + " Uniprot entries converted to Proteins in the model");
		return ret;
	}

	/**
	 * Returns a map in which the key is the accession and the value is the
	 * protein sequence
	 * 
	 * @param accession
	 * @return
	 */
	public Map<String, String> getAnnotatedProteinSequence(String accession) {
		final Set<String> accessions = new THashSet<String>();
		accessions.add(accession);
		return getAnnotatedProteinSequence(accessions);

	}
}
