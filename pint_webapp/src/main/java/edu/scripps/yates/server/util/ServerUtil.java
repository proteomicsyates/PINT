package edu.scripps.yates.server.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;

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

}
