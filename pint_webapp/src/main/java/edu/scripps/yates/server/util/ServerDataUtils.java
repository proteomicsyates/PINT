package edu.scripps.yates.server.util;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.ProteinImplFromUniprotEntry;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.UniprotFeatureBean;
import edu.scripps.yates.shared.util.Pair;
import edu.scripps.yates.utilities.strings.StringUtils;

public class ServerDataUtils {
	// for calculating the protein coverage
	private static final String SPECIAL_CHARACTER = "$";
	private final static Logger log = Logger.getLogger(ServerDataUtils.class);

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
					List<Integer> positions = StringUtils.allPositionsOf(proteinSeq, pepSeq);
					if (!positions.isEmpty()) {
						for (Integer position : positions) {
							psmBean.addPositionByProtein(accession,
									new Pair<Integer, Integer>(position, position + pepSeq.length()));
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
					List<Integer> positions = StringUtils.allPositionsOf(proteinSeq, pepSeq);
					if (!positions.isEmpty()) {
						for (Integer position : positions) {
							peptideBean.addPositionByProtein(accession,
									new Pair<Integer, Integer>(position, position + pepSeq.length()));
							// replace the peptide in the protein with
							// an special string
							proteinSeqTMP.replace(position - 1, position + pepSeq.length() - 1, specialString);
						}
					}
				}
			}
		}
		// calculate the protein coverage
		final int numberOfCoveredAA = StringUtils.allPositionsOf(proteinSeqTMP.toString(), SPECIAL_CHARACTER).size();
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

	/**
	 * Parse a string for getting the {@link UniprotFeatureBean}. The string has
	 * been encoded in {@link ProteinImplFromUniprotEntry}.
	 *
	 * @param value
	 * @param value
	 * @param string2
	 * @return
	 */
	public static UniprotFeatureBean getFromValue(String featureType, String name, String value) {
		final String annotationSeparator = ProteinImplFromUniprotEntry.ANNOTATION_SEPARATOR;
		UniprotFeatureBean ret = new UniprotFeatureBean();
		if (featureType.contains("splice")) {
			log.info(featureType);
		}
		ret.setFeatureType(featureType);
		if (name != null) {
			ret.setDescription(name);
		}
		if (value != null && value.contains(annotationSeparator)) {
			final String[] split = value.split(annotationSeparator);
			for (String string : split) {
				if (string.contains(":")) {
					final String[] split2 = string.split(":");
					String type = split2[0].trim();
					String tmp = split2[1].trim();
					if (type.equals(ProteinImplFromUniprotEntry.STATUS)) {
						ret.setStatus(tmp);
					} else if (type.equals(ProteinImplFromUniprotEntry.REF)) {
						ret.setRef(tmp);
					} else if (type.equals(ProteinImplFromUniprotEntry.BEGIN)) {
						try {
							ret.setPositionStart(Integer.valueOf(tmp));
						} catch (NumberFormatException e) {

						}
					} else if (type.equals(ProteinImplFromUniprotEntry.END)) {
						try {
							ret.setPositionEnd(Integer.valueOf(tmp));
						} catch (NumberFormatException e) {

						}
					} else if (type.equals(ProteinImplFromUniprotEntry.ORIGINAL)) {
						ret.setOriginal(tmp);
					} else if (type.equals("variation")) {
						ret.setVariation(tmp);
					} else if (type.equals(ProteinImplFromUniprotEntry.ID)) {
						ret.setDescription(tmp);
					} else if (type.equals(ProteinImplFromUniprotEntry.POSITION)) {
						try {
							ret.setPositionStart(Integer.valueOf(tmp));
							ret.setPositionEnd(Integer.valueOf(tmp));
						} catch (NumberFormatException e) {

						}
					}
				}
			}

		}
		return ret;
	}

}
