package edu.scripps.yates.annotations.uniprot;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.annotations.uniprot.xml.SequenceType;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.utils.ModelUtils;

public class UniprotProteinRetriever {
	private final static Logger log = Logger.getLogger(UniprotProteinRetriever.class);
	private static final HashMap<String, Protein> cachedProteins = new HashMap<String, Protein>();
	private static final HashSet<String> notAvailableProteins = new HashSet<String>();
	public static boolean enableCache = true;
	private final UniprotProteinLocalRetriever uniprotLocalRetriever;
	private final String uniprotVersion;

	public UniprotProteinRetriever(String uniprotVersion, File uniprotReleasesFolder, boolean useIndex) {
		uniprotLocalRetriever = new UniprotProteinLocalRetriever(uniprotReleasesFolder, useIndex);
		this.uniprotVersion = uniprotVersion;
	}

	public Set<String> getUniprotVersionsForProjects(Map<String, Date> uploadDatesByProjectTags) {
		return uniprotLocalRetriever.getUniprotVersionsForProjects(uploadDatesByProjectTags);
	}

	private HashMap<String, Protein> getFromCache(Collection<String> accessions) {
		HashMap<String, Protein> ret = new HashMap<String, Protein>();
		for (String accession : accessions) {
			ret.putAll(getFromCache(accession));
		}
		return ret;
	}

	private HashMap<String, Protein> getFromCache(String accession) {
		HashMap<String, Protein> ret = new HashMap<String, Protein>();

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
		Map<String, String> ret = new HashMap<String, String>();
		Set<String> missingAccessions = new HashSet<String>();
		if (accessions != null && !accessions.isEmpty()) {
			// discard the proteins that were retrieved before and there was no
			// sequence
			for (String accession : accessions) {
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
					for (String acc : accessions) {
						if (annotatedProteins.containsKey(acc)) {
							final Entry entry = annotatedProteins.get(acc);
							final SequenceType uniprotSeq = entry.getSequence();
							if (uniprotSeq != null) {
								String actualSeq = uniprotSeq.getValue().trim();
								if (actualSeq != null && !"".equals(actualSeq)) {
									actualSeq = actualSeq.replaceAll("\n", "");
									actualSeq = actualSeq.replaceAll(" ", "");
									ret.put(acc, actualSeq);
									// if it is an isoform like P12345-1, map it
									// also to P12345
									if ("1".equals(FastaParser.getIsoformVersion(acc))) {
										final String noIsoformAccession = FastaParser.getNoIsoformAccession(acc);
										log.info("Protein sequence from principal isoform " + acc
												+ " mapped also to accession " + noIsoformAccession);
										ret.put(noIsoformAccession, actualSeq);
									}
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
				Map<String, Protein> nullProteins = new HashMap<String, Protein>();
				for (String acc : missingAccessions) {
					nullProteins.put(acc, null);
				}
				addToCache(nullProteins);
			}
			// this.missingAccessions.clear();
			missingAccessions = UniprotProteinLocalRetriever.getMissingAccessions(accessions, ret);
			if (!missingAccessions.isEmpty()) {
				// add to missing accession in order to avoid to search for them
				// again
				log.info("Adding " + missingAccessions.size()
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
		Set<String> set = new HashSet<String>();
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
		HashMap<String, Protein> ret = new HashMap<String, Protein>();
		// log.info(entries.size() + " entries found");
		for (Entry entry : entries) {
			Protein protein = new ProteinImplFromUniprotEntry(entry);
			ret.put(protein.getPrimaryAccession().getAccession(), protein);
			// index by all accessions
			final List<Accession> uniprotAccs = ModelUtils.getAccessions(protein, AccessionType.UNIPROT);
			if (uniprotAccs != null) {
				for (Accession acc : uniprotAccs) {
					ret.put(acc.getAccession(), protein);
				}
			}
		}
		log.debug(ret.size() + " Uniprot entries converted to Proteins in the model");
		return ret;
	}

	public Map<String, String> getAnnotatedProteinSequence(String accession) {
		Set<String> accessions = new HashSet<String>();
		accessions.add(accession);
		return getAnnotatedProteinSequence(accessions);

	}
}
