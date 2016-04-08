package edu.scripps.yates.server.util;

import java.util.Collection;
import java.util.List;

import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.util.Pair;
import edu.scripps.yates.utilities.strings.StringUtils;

public class ServerDataUtils {
	// for calculating the protein coverage
	private static final String SPECIAL_CHARACTER = "$";

	public static void calculateProteinCoverage(ProteinBean proteinBean, String proteinSeq) {
		final Pair<Double, char[]> pair = calculateProteinCoverage(proteinBean.getPsms(), proteinBean.getPeptides(),
				proteinSeq, proteinBean.getPrimaryAccession().getAccession());
		proteinBean.setCoverage(pair.getFirstElement());
		proteinBean.setCoverageArrayString(pair.getSecondElement());
	}

	/**
	 * Calculate a protein coverage for a given protein sequence, and a given
	 * collection of peptides and psms.
	 *
	 * @param psms
	 * @param peptides
	 * @param proteinSeq
	 * @param accession
	 * @return a pair, having the protein coverage (num covered AA / protein
	 *         length) in the first element and the proteinStringCoverage in the
	 *         second element
	 */
	public static Pair<Double, char[]> calculateProteinCoverage(Collection<PSMBean> psms,
			Collection<PeptideBean> peptides, String proteinSeq, String accession) {
		StringBuilder proteinSeqTMP = new StringBuilder();
		proteinSeqTMP.append(proteinSeq);
		// RemoteServicesTasks.getPSMsFromProtein( sessionID,
		// proteinBean, false);
		if (psms != null) {
			for (PSMBean psmBean : psms) {
				final String pepSeq = psmBean.getSequence();
				if (pepSeq != null && !"".equals(pepSeq)) {
					String specialString = getSpecialString(pepSeq.length());
					List<Integer> positions = StringUtils.allIndexOf(proteinSeq, pepSeq);
					if (!positions.isEmpty()) {
						for (Integer position : positions) {
							psmBean.addPositionByProtein(accession, position);
							// replace the peptide in the protein with
							// an special string
							proteinSeqTMP.replace(position - 1, position + pepSeq.length() - 1, specialString);
						}
					}
				}
			}
		}
		if (peptides != null) {
			for (PeptideBean peptideBean : peptides) {
				final String pepSeq = peptideBean.getSequence();
				if (pepSeq != null && !"".equals(pepSeq)) {
					String specialString = getSpecialString(pepSeq.length());
					List<Integer> positions = StringUtils.allIndexOf(proteinSeq, pepSeq);
					if (!positions.isEmpty()) {
						for (Integer position : positions) {
							peptideBean.addPositionByProtein(accession, position);
							// replace the peptide in the protein with
							// an special string
							proteinSeqTMP.replace(position - 1, position + pepSeq.length() - 1, specialString);
						}
					}
				}
			}
		}
		// calculate the protein coverage
		final int numberOfCoveredAA = StringUtils.allIndexOf(proteinSeqTMP.toString(), SPECIAL_CHARACTER).size();
		double coverage = Double.valueOf(numberOfCoveredAA) / Double.valueOf(proteinSeq.length());

		char[] coveredSequenceArray = new char[proteinSeqTMP.length()];
		for (int index = 0; index < proteinSeqTMP.length(); index++) {
			if (proteinSeqTMP.charAt(index) == SPECIAL_CHARACTER.charAt(0)) {
				coveredSequenceArray[index] = '1';
			} else {
				coveredSequenceArray[index] = '0';
			}
		}

		Pair<Double, char[]> ret = new Pair<Double, char[]>(coverage, coveredSequenceArray);
		return ret;
	}

	/**
	 * Gets an special string with a certain length
	 *
	 * @param length
	 * @return
	 */
	private static String getSpecialString(int length) {
		StringBuilder sb = new StringBuilder();
		while (sb.toString().length() != length) {
			sb.append(SPECIAL_CHARACTER);
		}
		return sb.toString();
	}
}
