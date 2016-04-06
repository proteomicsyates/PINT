package edu.scripps.yates.census.analysis.clustering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.census.read.model.interfaces.QuantifiedPeptideInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedProteinInterface;
import edu.scripps.yates.utilities.alignment.nwalign.NWAlign;
import edu.scripps.yates.utilities.alignment.nwalign.NWResult;

public class ProteinCluster {

	// Two sets for proteins and peptides
	private Set<QuantifiedProteinInterface> proteinSet = new HashSet<QuantifiedProteinInterface>();
	private final Set<QuantifiedPeptideInterface> peptideSet = new HashSet<QuantifiedPeptideInterface>();
	private String forcedProteinClusterKey;

	// Adds proteins to the protein sets
	public void addProtein(QuantifiedProteinInterface protein) {
		proteinSet.add(protein);
		peptideSet.addAll(protein.getQuantifiedPeptides());
	}

	// Adds peptides to the peptide sets
	public void addPeptide(QuantifiedPeptideInterface peptide) {
		peptideSet.add(peptide);
		proteinSet.addAll(peptide.getQuantifiedProteins());
	}

	public NWResult[][] align() {
		// get peptides from clusters in form of set

		if (peptideSet.size() == 1) {
			return null;
		}

		NWResult[][] results = new NWResult[peptideSet.size()][peptideSet.size()];

		List<QuantifiedPeptideInterface> pepList = new ArrayList<QuantifiedPeptideInterface>();
		pepList.addAll(peptideSet);

		// two for loops: compare two different peptides
		for (int i = 0; i < pepList.size(); i++) {
			QuantifiedPeptideInterface pep1 = pepList.get(i);

			for (int j = i + 1; j < pepList.size(); j++) {
				if (i == j) {
					// same peptide, move on
					continue;
				}

				// NWAlign (give back score)
				QuantifiedPeptideInterface pep2 = pepList.get(j);
				NWResult result = NWAlign.needlemanWunsch(pep1.getSequence(), pep2.getSequence(), -11, -1);
				results[i][j] = result;
			}

		}
		return results;
	}

	public Set<QuantifiedProteinInterface> getProteinSet() {
		return proteinSet;
	}

	public void setProteinSet(Set<QuantifiedProteinInterface> proteinSet) {
		this.proteinSet = proteinSet;
	}

	public Set<QuantifiedPeptideInterface> getPeptideSet() {
		return peptideSet;
	}

	public String getProteinClusterKey() {
		if (forcedProteinClusterKey != null) {
			return forcedProteinClusterKey;
		}
		StringBuilder sb = new StringBuilder();

		List<QuantifiedProteinInterface> proteinList = new ArrayList<QuantifiedProteinInterface>();
		proteinList.addAll(proteinSet);
		Collections.sort(proteinList, new Comparator<QuantifiedProteinInterface>() {

			@Override
			public int compare(QuantifiedProteinInterface o1, QuantifiedProteinInterface o2) {
				return o1.getAccession().compareTo(o2.getAccession());
			}
		});
		Set<String> proteinAccs = new HashSet<String>();
		for (QuantifiedProteinInterface quantifiedProtein : proteinList) {
			if (!proteinAccs.contains(quantifiedProtein.getAccession())) {
				sb.append(quantifiedProtein.getAccession() + ":");
				proteinAccs.add(quantifiedProtein.getAccession());
			}
		}
		return sb.toString() + " [" + hashCode() + "]";

	}

	/**
	 * Force a value for the protein cluster set
	 *
	 * @param proteinClusterKey
	 *            the proteinClusterKey to set
	 */
	public void setProteinClusterKey(String proteinClusterKey) {
		forcedProteinClusterKey = proteinClusterKey;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getProteinClusterKey() + "\t[" + getPeptideString() + "]";
	}

	private String getPeptideString() {
		List<QuantifiedPeptideInterface> list = new ArrayList<QuantifiedPeptideInterface>();
		list.addAll(peptideSet);
		Collections.sort(list, new Comparator<QuantifiedPeptideInterface>() {

			@Override
			public int compare(QuantifiedPeptideInterface o1, QuantifiedPeptideInterface o2) {
				return o1.getSequence().compareTo(o2.getSequence());
			}
		});
		StringBuilder sb = new StringBuilder();
		for (QuantifiedPeptideInterface quantifiedPeptide : list) {
			if (!"".equals(sb.toString())) {
				sb.append(",");
			}
			sb.append(quantifiedPeptide.getSequence());

		}
		return sb.toString();
	}

}
