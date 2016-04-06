package edu.scripps.yates.annotations.uniprot;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.annotations.uniprot.UniprotProteinLocalRetriever;
import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.annotations.uniprot.xml.SequenceType;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.utils.ModelUtils;

public class UniprotProteinRetriever {
	private static final HashMap<String, Protein> cachedProteins = new HashMap<String, Protein>();
	private final UniprotProteinLocalRetriever uniprotLocalRetriever;
	private final String uniprotVersion;
	private final Set<String> missingAccessions = new HashSet<String>();

	public UniprotProteinRetriever(String uniprotVersion,
			File uniprotReleasesFolder, boolean useIndex) {
		uniprotLocalRetriever = new UniprotProteinLocalRetriever(
				uniprotReleasesFolder, useIndex);
		this.uniprotVersion = uniprotVersion;
	}

	public Set<String> getUniprotVersionsForProjects(Set<String> projectTags) {
		return uniprotLocalRetriever.getUniprotVersionsForProjects(projectTags);
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
		if (!cachedProteins.isEmpty())
			if (cachedProteins.containsKey(accession))
				ret.put(accession, cachedProteins.get(accession));

		return ret;
	}

	private void addToCache(Map<String, Protein> queryProteins) {
		cachedProteins.putAll(queryProteins);

	}

	public Map<String, String> getAnnotatedProteinSequence(
			Collection<String> accessions) {
		Map<String, String> ret = new HashMap<String, String>();
		if (accessions != null && !accessions.isEmpty()) {
			final Map<String, Entry> annotatedProteins = uniprotLocalRetriever
					.getAnnotatedProteins(uniprotVersion, accessions);
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
						}
					}
				}
			}
		}
		return ret;
	}

	public Map<String, Protein> getAnnotatedProteins(
			Collection<String> accessions) {
		final Map<String, Protein> ret = getFromCache(accessions);
		Set<String> missingAccessions = UniprotProteinLocalRetriever
				.getMissingAccessions(accessions, ret);
		if (!missingAccessions.isEmpty()) {
			final Map<String, Entry> annotatedProteins = uniprotLocalRetriever
					.getAnnotatedProteins(uniprotVersion, missingAccessions);
			if (!annotatedProteins.isEmpty()) {
				final Map<String, Protein> proteins = convertUniprotEntries2Proteins(annotatedProteins
						.values());
				addToCache(proteins);
				ret.putAll(proteins);
			}
			this.missingAccessions.clear();
			this.missingAccessions.addAll(UniprotProteinLocalRetriever
					.getMissingAccessions(accessions, ret));

			return ret;
		} else {
			this.missingAccessions.clear();
			return ret;
		}

	}

	public Map<String, Protein> getAnnotatedProtein(String accession) {
		Set<String> set = new HashSet<String>();
		set.add(accession);
		return getAnnotatedProteins(set);

	}

	/**
	 * Gets the accessions which were not able to be retrieved
	 * 
	 * @return
	 */
	public Collection<String> getMissingAccessions() {
		return missingAccessions;
	}

	public static Map<String, Protein> convertUniprotEntries2Proteins(
			Collection<Entry> entries) {
		HashMap<String, Protein> ret = new HashMap<String, Protein>();
		// log.info(entries.size() + " entries found");
		for (Entry entry : entries) {
			Protein protein = new ProteinImplFromUniprotEntry(entry);
			ret.put(protein.getPrimaryAccession().getAccession(), protein);
			// index by all accessions
			final List<Accession> uniprotAccs = ModelUtils.getAccessions(
					protein, AccessionType.UNIPROT);
			if (uniprotAccs != null) {
				for (Accession acc : uniprotAccs) {
					ret.put(acc.getAccession(), protein);
				}
			}
		}
		// log.info(ret.size() + " proteins converted in the model");
		return ret;
	}
}
