package edu.scripps.yates.server.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.ProteinImplFromUniprotEntry;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountBean;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.PTMBean;
import edu.scripps.yates.shared.model.PTMSiteBean;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.ScoreBean;
import edu.scripps.yates.shared.model.UniprotFeatureBean;
import edu.scripps.yates.shared.model.interfaces.ContainsRatios;
import edu.scripps.yates.shared.util.DataGridRenderValue;
import edu.scripps.yates.shared.util.Pair;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.SharedDataUtil;
import edu.scripps.yates.shared.util.UniprotFeatures;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.strings.StringUtils;
import gnu.trove.list.array.TIntArrayList;

public class ServerDataUtil {

	// for calculating the protein coverage
	private final static String SPECIAL_CHARACTER = "$";
	private final static Logger log = Logger.getLogger(ServerDataUtil.class);

	public static void calculateProteinCoverage(ProteinBean proteinBean, String proteinSeq) {
		final Pair<Double, char[]> pair = calculateProteinCoverage(proteinBean.getPsms(), proteinBean.getPeptides(),
				proteinBean.getDifferentSequences(), proteinSeq, proteinBean.getPrimaryAccession().getAccession());
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
	 * @return a pair, having the protein coverage (num covered AA / protein length)
	 *         in the first element and the proteinStringCoverage in the second
	 *         element
	 */
	public static Pair<Double, char[]> calculateProteinCoverage(Collection<PSMBean> psms,
			Collection<PeptideBean> peptides, Collection<String> fullSequences, String proteinSeq, String accession) {
		final StringBuilder proteinSeqTMP = new StringBuilder();
		proteinSeqTMP.append(proteinSeq);
		// RemoteServicesTasks.getPSMsFromProtein( sessionID,
		// proteinBean, false);
		if (psms != null) {
			for (final PSMBean psmBean : psms) {
				final String pepSeq = psmBean.getSequence();
				if (pepSeq != null && !"".equals(pepSeq)) {
					final String specialString = getSpecialString(pepSeq.length());
					final TIntArrayList positions = StringUtils.allPositionsOf(proteinSeq, pepSeq);
					if (!positions.isEmpty()) {
						for (final int position : positions.toArray()) {
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
		if (peptides != null && !peptides.isEmpty()) {
			for (final PeptideBean peptideBean : peptides) {
				final String pepSeq = peptideBean.getSequence();
				if (pepSeq != null && !"".equals(pepSeq)) {
					final String specialString = getSpecialString(pepSeq.length());
					final TIntArrayList positions = StringUtils.allPositionsOf(proteinSeq, pepSeq);
					if (!positions.isEmpty()) {
						for (final int position : positions.toArray()) {
							peptideBean.addPositionByProtein(accession,
									new Pair<Integer, Integer>(position, position + pepSeq.length()));
							// replace the peptide in the protein with
							// an special string
							proteinSeqTMP.replace(position - 1, position + pepSeq.length() - 1, specialString);
						}
					}
				}
			}
		} else if (fullSequences != null) {
			for (final String fulllSequence : fullSequences) {
				final String pepSeq = FastaParser.cleanSequence(fulllSequence);
				if (pepSeq != null && !"".equals(pepSeq)) {
					final String specialString = getSpecialString(pepSeq.length());
					final TIntArrayList positions = StringUtils.allPositionsOf(proteinSeq, pepSeq);
					if (!positions.isEmpty()) {
						for (final int position : positions.toArray()) {
//							peptideBean.addPositionByProtein(accession,
//									new Pair<Integer, Integer>(position, position + pepSeq.length()));
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
		final double coverage = Double.valueOf(numberOfCoveredAA) / Double.valueOf(proteinSeq.length());

		final char[] coveredSequenceArray = new char[proteinSeqTMP.length()];
		for (int index = 0; index < proteinSeqTMP.length(); index++) {
			if (proteinSeqTMP.charAt(index) == SPECIAL_CHARACTER.charAt(0)) {
				coveredSequenceArray[index] = '1';
			} else {
				coveredSequenceArray[index] = '0';
			}
		}

		final Pair<Double, char[]> ret = new Pair<Double, char[]>(coverage, coveredSequenceArray);
		return ret;
	}

	/**
	 * Gets an special string with a certain length
	 *
	 * @param length
	 * @return
	 */
	private static String getSpecialString(int length) {
		final StringBuilder sb = new StringBuilder();
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
		final UniprotFeatureBean ret = new UniprotFeatureBean();

		ret.setFeatureType(featureType);
		if (name != null) {
			ret.setDescription(name);
		}
		if (value != null && value.contains(annotationSeparator)) {
			final String[] split = value.split(annotationSeparator);
			for (final String string : split) {
				if (string.contains(":")) {
					final String[] split2 = string.split(":");
					final String type = split2[0].trim();
					String tmp = "";
					if (split2.length > 1) {
						tmp = split2[1].trim();
					}
					if (type.equals(ProteinImplFromUniprotEntry.STATUS)) {
						ret.setStatus(tmp);
					} else if (type.equals(ProteinImplFromUniprotEntry.REF)) {
						ret.setRef(tmp);
					} else if (type.equals(ProteinImplFromUniprotEntry.BEGIN)) {
						try {
							ret.setPositionStart(Integer.valueOf(tmp));
						} catch (final NumberFormatException e) {

						}
					} else if (type.equals(ProteinImplFromUniprotEntry.END)) {
						try {
							ret.setPositionEnd(Integer.valueOf(tmp));
						} catch (final NumberFormatException e) {

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
						} catch (final NumberFormatException e) {

						}
					}
				}
			}

		}
		return ret;
	}

	public static String getPTMScoreString(String ptmScoreName, List<PTMBean> ptms) {
		final Set<String> ret = new HashSet<String>();
		final StringBuilder sb = new StringBuilder();
		if (ptms != null) {
			for (final PTMBean ptm : ptms) {
				final String scoreString = getScoreString(ptm, ptmScoreName);
				if (!ret.contains(scoreString)) {
					if (!"".equals(sb.toString()))
						sb.append(SharedConstants.SEPARATOR);
					sb.append(scoreString);
					ret.add(scoreString);
				}

			}
		}
		return sb.toString();

	}

	public static String getScoreString(PTMBean ptm, String ptmScoreName) {
		final StringBuilder sb = new StringBuilder();
		for (final PTMSiteBean ptmSite : ptm.getPtmSites()) {
			final ScoreBean score = ptmSite.getScore();
			if (score != null && ptmScoreName.equals(score.getScoreName())) {
				if (!"".equals(sb.toString()))
					sb.append(", ");
				sb.append(ptmSite.getAa() + "(" + getParsedScoreValue(score) + ")");
			}
		}
		return sb.toString();
	}

	/**
	 * If the value is a double, it is parsed with 3 decimals. Otherwise, the value
	 * is returned as string
	 *
	 * @return
	 */
	public static String getParsedScoreValue(ScoreBean score) {
		try {
			final double doubleValue = Double.valueOf(score.getValue());
			final ServerNumberFormat format = new ServerNumberFormat("#.###");
			return format.format(doubleValue);
		} catch (final NumberFormatException e) {
			return score.getValue();
		}
	}

	public static String getPeptideColumnValue(ColumnName columnName, PeptideBean p, String conditionName,
			String condition2Name, String projectTag, AmountType amountType, String scoreName, String ratioName,
			boolean skipRatioInfinities) {
		if (p == null) {
			return parseEmptyString(null);
		}
		switch (columnName) {

		case PEPTIDE_SEQUENCE:
			return parseEmptyString(p.getSequence());
		case ACC:
			return parseEmptyString(p.getProteinAccessionString());
		case PEPTIDE_AMOUNT:

			return parseEmptyString(DataGridRenderValue.getAmountDataGridRenderValue(p, conditionName, amountType,
					projectTag, new ServerNumberFormat("#.##")).getValue());
		case SPC_PER_CONDITION:
			return parseEmptyString(
					DataGridRenderValue.getSPCPerConditionDataGridRenderValue(p, conditionName, projectTag).getValue());
		case PEPTIDE_RATIO_SCORE:
			return parseEmptyString(getRatioScoreStringByConditions(p, conditionName, condition2Name, projectTag,
					ratioName, scoreName, skipRatioInfinities, true));
		case PEPTIDE_RATIO:
			return parseEmptyString(getRatioStringByConditions(p, conditionName, condition2Name, projectTag, ratioName,
					skipRatioInfinities, true));
		case PEPTIDE_LENGTH:
			if (p.getSequence() != null)
				return parseEmptyString(String.valueOf(p.getSequence().length()));
			return SharedDataUtil.EMPTY_VALUE;
		case POSITION_IN_PROTEIN:
			return parseEmptyString(p.getStartingPositionsString());
		case TAXONOMY:
			return parseEmptyString(p.getOrganismsString());
		case CONDITION:
			return parseEmptyString(p.getConditionsString());
		case PEPTIDE_EVIDENCE:
			if (p.getRelation() != null) {
				return parseEmptyString(p.getRelation().name());
			} else {
				return parseEmptyString("");
			}
		case SPECTRUM_COUNT:
			return parseEmptyString(String.valueOf(p.getNumPSMs()));
		case PEPTIDE_ACTIVE_SITE:
			return parseEmptyString(SharedDataUtil.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PEPTIDE_DOMAIN_FAMILIES:
			return parseEmptyString(SharedDataUtil.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PEPTIDE_EXPERIMENTAL_INFO:
			return parseEmptyString(SharedDataUtil.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PEPTIDE_MOLECULAR_PROCESSING:
			return parseEmptyString(SharedDataUtil.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PEPTIDE_NATURAL_VARIATIONS:
			return parseEmptyString(SharedDataUtil.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PEPTIDE_SECONDARY_STRUCTURE:
			return parseEmptyString(SharedDataUtil.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PEPTIDE_PTM:
			return parseEmptyString(SharedDataUtil.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PTM_SCORE:
			return parseEmptyString(getPTMScoreString(p.getPtms()));
		case PTMS:
			return parseEmptyString(SharedDataUtil.getPTMString(p.getPtms()));
		case NUM_PTMS:
			return parseEmptyString(String.valueOf(p.getPtms().size()));
		case NUM_PTM_SITES:
			int count = 0;
			for (final PTMBean ptm : p.getPtms()) {
				count += ptm.getPtmSites().size();
			}
			return parseEmptyString(String.valueOf(count));
		case LINK_TO_PRIDE_CLUSTER:
			return "+";
		default:

			return parseEmptyString("");
		}
	}

	private static String parseEmptyString(String string) {
		return SharedDataUtil.parseEmptyString(string);
	}

	public static String getPTMScoreString(List<PTMBean> ptms) {
		final Set<String> ret = new HashSet<String>();
		final StringBuilder sb = new StringBuilder();
		if (ptms != null) {
			for (final PTMBean ptm : ptms) {
				final String scoreString = getScoreString(ptm);
				if (!ret.contains(scoreString)) {
					if (!"".equals(sb.toString()))
						sb.append(SharedConstants.SEPARATOR);
					sb.append(scoreString);
					ret.add(scoreString);
				}

			}
		}

		return sb.toString();
	}

	public static String getScoreString(PTMBean ptm) {
		final StringBuilder sb = new StringBuilder();
		for (final PTMSiteBean ptmSite : ptm.getPtmSites()) {
			final ScoreBean score = ptmSite.getScore();
			if (score != null) {
				if (!"".equals(sb.toString()))
					sb.append(", ");
				sb.append(ptmSite.getAa() + "(" + getParsedScoreValue(score) + ")");
			}
		}
		return sb.toString();
	}

	/**
	 * Gets a string with the scores associated with the ratios
	 *
	 * @param condition1Name
	 * @param condition2Name
	 * @param projectTag
	 * @param ratioName
	 * @param ratioScore
	 * @param skipInfinities
	 * @return
	 */
	public static String getRatioScoreStringByConditions(ContainsRatios containsRatios, String condition1Name,
			String condition2Name, String projectTag, String ratioName, String ratioScoreName, boolean skipInfinities,
			boolean formatNumber) {
		final StringBuilder sb = new StringBuilder();

		final List<ScoreBean> ratioScores = SharedDataUtil.getRatioScoresByConditions(containsRatios, condition1Name,
				condition2Name, projectTag, ratioName, ratioScoreName);
		for (final ScoreBean ratioScore : ratioScores) {
			try {
				final Double value = Double.valueOf(ratioScore.getValue());
				if (value.toString().endsWith(".0")) {
					if (!"".equals(sb.toString()))
						sb.append(SharedConstants.SEPARATOR);
					sb.append(String.valueOf(value.intValue()));
				} else {
					try {
						String format = null;
						if (formatNumber) {
							format = ServerNumberFormat.getScientificFormat(2, 3).format(value);
						} else {
							format = String.valueOf(value);
						}
						if (!"".equals(sb.toString()))
							sb.append(SharedConstants.SEPARATOR);
						sb.append(format);
					} catch (final NumberFormatException e2) {

					}
				}
			} catch (final NumberFormatException e) {
				// add the string as it is
			}

		}

		return sb.toString();
	}

	/**
	 * Gets a string with the ratio values
	 *
	 * @param condition1Name
	 * @param condition2Name
	 * @param projectTag
	 * @param ratioName
	 * @param skipInfinities
	 * @return
	 */
	public static String getRatioStringByConditions(ContainsRatios containsRatios, String condition1Name,
			String condition2Name, String projectTag, String ratioName, boolean skipInfinities, boolean formatNumber) {
		final StringBuilder sb = new StringBuilder();

		final List<RatioBean> ratiosByConditions = SharedDataUtil.getRatiosByConditions(containsRatios, condition1Name,
				condition2Name, projectTag, ratioName, skipInfinities);
		final List<Double> ratioValues = SharedDataUtil.getRatioValues(condition1Name, condition2Name,
				ratiosByConditions);
		for (final Double value : ratioValues) {

			try {
				String format = null;
				if (formatNumber) {
					format = formatNumber(value, 2, 3, true);
				} else {
					format = String.valueOf(value);
				}
				if (!"".equals(sb.toString()))
					sb.append(SharedConstants.SEPARATOR);
				sb.append(format);
			} catch (final NumberFormatException e2) {

			}

		}

		return sb.toString();
	}

	/**
	 * Formats a number as a decimal number with a certain number of decimals.
	 * 
	 * @param number
	 * @param maxDecimals
	 * @param scientificNotationIfSmaller if true and lets say that maxDecimal=3 and
	 *                                    the number is smaller than 0.001, then the
	 *                                    number will be formated with scientific
	 *                                    notation
	 * @return
	 */
	public static String formatNumber(double number, int minDecimals, int maxDecimals,
			boolean scientificNotationIfSmaller) {
		if (maxDecimals <= 0) {
			return ServerNumberFormat.getFormat("#").format(number);
		}
		final StringBuilder pattern = new StringBuilder("#.");
		final StringBuilder numberThresholdStringBuilder = new StringBuilder("0.");
		for (int i = 0; i < maxDecimals; i++) {
			pattern.append("#");
			numberThresholdStringBuilder.append("0");
		}
		final String numberThresholdString = numberThresholdStringBuilder.toString();
		final double numberThreshold = Double
				.valueOf(numberThresholdString.substring(0, numberThresholdString.length() - 1) + "1");
		if (scientificNotationIfSmaller && Math.abs(number) < numberThreshold) {
			return ServerNumberFormat.getScientificFormat(minDecimals, maxDecimals).format(number);

		} else {
			return ServerNumberFormat.getFormat(pattern.toString()).format(number);
		}
	}

	public static String getProteinColumnValue(ColumnName columnName, ProteinBean p, String conditionName,
			String condition2Name, String projectTag, AmountType amountType, String scoreName, String ratioName,
			boolean skipRatioInfinities) {
		switch (columnName) {
		case ACC:
			return parseEmptyString(p.getPrimaryAccession().getAccession());
		case DESCRIPTION:
			return parseEmptyString(p.getPrimaryAccession().getDescription());
		case SPECTRUM_COUNT:
			return parseEmptyString(String.valueOf(p.getNumPSMs()));
		case SPC_PER_CONDITION:
			return parseEmptyString(
					DataGridRenderValue.getSPCPerConditionDataGridRenderValue(p, conditionName, projectTag).getValue());

		case COVERAGE:
			// TODO calculate coverage depending on the PSMs, depending on which
			// runs are coming, from which conditions
			final Double coverage = p.getCoverage();
			if (coverage != null && coverage != 0.0) {
				return parseEmptyString(ServerNumberFormat.getFormat("#.#").format(coverage * 100) + "%");
			} else {
				return "-";
			}
		case PROTEIN_PI:
			// return parseZeroAndEmptyString(p.getPi()));
			return parseEmptyString(ServerNumberFormat.getFormat("#.##").format(p.getPi()));
		case GENE:
			return parseEmptyString(p.getGenesString(false));
		case MOL_W:
			final double mw = p.getMw();
			final String format = ServerNumberFormat.getFormat("#.###").format(mw);
			final String parseZeroAndEmptyString = parseEmptyString(format);
			return parseZeroAndEmptyString;
		case PROTEIN_SCORE:
			final ScoreBean scoreByName = p.getScoreByName(scoreName);
			if (scoreByName != null) {
				try {
					final Double valueOf = Double.valueOf(scoreByName.getValue());
					if (valueOf > 0.01) {
						return ServerNumberFormat.getFormat("#.###").format(valueOf);
					} else {
						return scoreByName.getValue();
					}
				} catch (final NumberFormatException e) {
					return scoreByName.getValue();
				}

			}
			return parseEmptyString("");
		case SEQUENCE_COUNT:
			return parseEmptyString(String.valueOf(p.getNumPeptides()));
		case PROTEIN_AMOUNT:
			return parseEmptyString(
					getAmountString(p.getAmountsByExperimentalCondition().get(conditionName), projectTag));
		case PROTEIN_RATIO:
			return parseEmptyString(getRatioStringByConditions(p, conditionName, condition2Name, projectTag, ratioName,
					skipRatioInfinities, true));

		case PROTEIN_RATIO_SCORE:
			return parseEmptyString(getRatioScoreStringByConditions(p, conditionName, condition2Name, projectTag,
					ratioName, scoreName, skipRatioInfinities, true));
		case PROTEIN_LENGTH:
			return parseEmptyString(String.valueOf(p.getLength()));
		case ALTERNATIVE_NAMES:
			return parseEmptyString(p.getAlternativeNamesString());
		case SECONDARY_ACCS:
			return parseEmptyString(p.getSecondaryAccessionsString());
		case TAXONOMY:
			if (p.getOrganism() != null) {
				return p.getOrganism().getId();
			}
			break;
		case PROTEIN_FUNCTION:
			return parseEmptyString(p.getFunctionString());
		case CONDITION:
			return parseEmptyString(p.getConditionsString());
		case OMIM:
			return parseEmptyString(p.getOmimIDString());
		case PROTEIN_GROUP_TYPE:
			if (p.getEvidence() != null) {
				return parseEmptyString(p.getEvidence().name());
			}
			break;
		case PEPTIDES_TABLE_BUTTON:
			return "+";
		case UNIPROT_PROTEIN_EXISTENCE:
			if (p.getUniprotProteinExistence() != null) {
				return parseEmptyString(p.getUniprotProteinExistence().getName());
			}
			break;

		case PROTEIN_ACTIVE_SITE:
			return parseEmptyString(SharedDataUtil.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PROTEIN_DOMAIN_FAMILIES:
			return parseEmptyString(SharedDataUtil.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PROTEIN_EXPERIMENTAL_INFO:
			return parseEmptyString(SharedDataUtil.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PROTEIN_MOLECULAR_PROCESSING:
			return parseEmptyString(SharedDataUtil.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PROTEIN_NATURAL_VARIATIONS:
			return parseEmptyString(SharedDataUtil.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PROTEIN_SECONDARY_STRUCTURE:
			return parseEmptyString(SharedDataUtil.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PROTEIN_PTM:
			return parseEmptyString(SharedDataUtil.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case LINK_TO_PRIDE_CLUSTER:
			return "+";
		case LINK_TO_INTACT:
			return "+";
		case LINK_TO_COMPLEX_PORTAL:
			return "+";
		default:
			return parseEmptyString("");
		}
		return parseEmptyString("");
	}

	public static String getAmountString(List<AmountBean> proteinAmountSet, String projectTag) {
		final StringBuilder sb = new StringBuilder();
		List<AmountBean> proteinAmounts = SharedDataUtil.sortAmountsByRunID(proteinAmountSet);
		if (proteinAmounts != null) {
			// if some amounts are resulting from the combination
			// (sum/average...)
			// over other amounts, report only them
			final List<AmountBean> composedAmounts = AmountBean.getComposedAmounts(proteinAmounts);
			if (!composedAmounts.isEmpty()) {
				proteinAmounts = SharedDataUtil.sortAmountsByRunID(composedAmounts);
			}
			for (final AmountBean proteinAmountBean : proteinAmounts) {
				if (proteinAmountBean.getExperimentalCondition().getProject().getTag().equals(projectTag)) {

					// if the resulting string is a number, try to format it:
					final Double doubleValue = proteinAmountBean.getValue();
					if (doubleValue.toString().endsWith(".0")) {
						if (!"".equals(sb.toString()))
							sb.append(SharedConstants.SEPARATOR);
						sb.append(String.valueOf(doubleValue.intValue()));
					} else {

						final String format = ServerNumberFormat.getFormat("#.##").format(doubleValue);
						if (!"".equals(sb.toString()))
							sb.append(SharedConstants.SEPARATOR);
						sb.append(format);

					}
				}
			}
		}

		return sb.toString();
	}

	public static String getPSMColumnValue(ColumnName columnName, PSMBean p, String conditionName,
			String condition2Name, String projectTag, AmountType amountType, String scoreName, String ratioName,
			boolean skipRatioInfinities) {
		if (p == null) {
			return parseEmptyString(null);
		}
		switch (columnName) {
		case PSM_ID:
			return parseEmptyString(p.getPsmID());
		case PSM_RUN_ID:
			if (p.getMsRun() != null)
				return parseEmptyString(p.getMsRun().getRunID());
			return parseEmptyString(null);
		case PEPTIDE_SEQUENCE:
			return parseEmptyString(p.getSequence());
		case PEPTIDE_PI:
			if (p.getPi() != null)
				return parseEmptyString(ServerNumberFormat.getFormat("#.##").format(p.getPi()));
			return parseEmptyString("");
		case PTM_SCORE:
			return parseEmptyString(getPTMScoreString(p.getPtms()));
		case PTMS:
			return parseEmptyString(SharedDataUtil.getPTMString(p.getPtms()));
		case NUM_PTMS:
			return parseEmptyString(String.valueOf(p.getPtms().size()));
		case NUM_PTM_SITES:
			int count = 0;
			for (final PTMBean ptm : p.getPtms()) {
				count += ptm.getPtmSites().size();
			}
			return parseEmptyString(String.valueOf(count));
		case ACC:
			return parseEmptyString(p.getProteinAccessionString());
		case DESCRIPTION:
			return parseEmptyString(p.getProteinDescriptionString());
		case PSM_SCORE:
			final ScoreBean scoreByName = p.getScoreByName(scoreName);
			if (scoreByName != null) {
				try {
					final Double valueOf = Double.valueOf(scoreByName.getValue());
					if (valueOf > 0.01)
						return ServerNumberFormat.getFormat("#.###").format(valueOf);
					else
						return scoreByName.getValue();
				} catch (final NumberFormatException e) {
					return scoreByName.getValue();
				}

			}
			return parseEmptyString("");
		case PSM_AMOUNT:
			return parseEmptyString(DataGridRenderValue.getAmountDataGridRenderValue(p, conditionName, amountType,
					projectTag, new ServerNumberFormat("#.##")).getValue());

		case CHARGE:
			return parseEmptyString(p.getChargeState());
		case PSM_RATIO:
			return parseEmptyString(getRatioStringByConditions(p, conditionName, condition2Name, projectTag, ratioName,
					skipRatioInfinities, true));
		case PEPTIDE_LENGTH:
			if (p.getSequence() != null)
				return parseEmptyString(String.valueOf(p.getSequence().length()));
			return SharedDataUtil.EMPTY_VALUE;
		case POSITION_IN_PROTEIN:
			return parseEmptyString(p.getStartingPositionsString());
		case TAXONOMY:
			return parseEmptyString(p.getOrganismsString());
		case CONDITION:
			return parseEmptyString(p.getConditionsString());
		case PEPTIDE_EVIDENCE:
			if (p.getRelation() != null) {
				return parseEmptyString(p.getRelation().name());
			} else {
				return parseEmptyString("");
			}
		case PEPTIDE_ACTIVE_SITE:
			return parseEmptyString(SharedDataUtil.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PEPTIDE_DOMAIN_FAMILIES:
			return parseEmptyString(SharedDataUtil.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PEPTIDE_EXPERIMENTAL_INFO:
			return parseEmptyString(SharedDataUtil.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PEPTIDE_MOLECULAR_PROCESSING:
			return parseEmptyString(SharedDataUtil.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PEPTIDE_NATURAL_VARIATIONS:
			return parseEmptyString(SharedDataUtil.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PEPTIDE_SECONDARY_STRUCTURE:
			return parseEmptyString(SharedDataUtil.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PEPTIDE_PTM:
			return parseEmptyString(SharedDataUtil.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case LINK_TO_PRIDE_CLUSTER:
			return "+";
		case PSM_RATIO_SCORE:
			return parseEmptyString(getRatioScoreStringByConditions(p, conditionName, condition2Name, projectTag,
					ratioName, scoreName, skipRatioInfinities, true));
		default:

			return parseEmptyString("");
		}
	}

	public static String getProteinGroupColumnValue(ColumnName columnName, ProteinGroupBean p, String conditionName,
			String condition2Name, String projectTag, AmountType amountType, String scoreName, String ratioName,
			boolean skipRatioInfinities) {
		switch (columnName) {
		case ACC:
			return parseEmptyString(p.getPrimaryAccessionsString());
		case DESCRIPTION:
			return parseEmptyString(p.getDescriptionsString());
		case SPECTRUM_COUNT:
			return parseEmptyString(String.valueOf(p.getNumPSMs()));
		case SPC_PER_CONDITION:
			return parseEmptyString(
					DataGridRenderValue.getSPCPerConditionDataGridRenderValue(p, conditionName, projectTag).getValue());

		case COVERAGE:
			// TODO calculate coverage depending on the PSMs, depending on which
			// runs are coming, from which conditions
			return "";
		case PROTEIN_GROUP_TYPE:
			return p.getGroupMemberEvidences();

		case GENE:
			return parseEmptyString(p.getGenesString(true));

		case SEQUENCE_COUNT:
			return parseEmptyString(String.valueOf(p.getNumPeptides()));
		case PROTEIN_AMOUNT:
			return parseEmptyString(
					getAmountString(p.getAmountsByExperimentalCondition().get(conditionName), projectTag));
		case PROTEIN_RATIO:
			return parseEmptyString(getRatioStringByConditions(p, conditionName, condition2Name, projectTag, ratioName,
					skipRatioInfinities, true));
		case PROTEIN_RATIO_SCORE:
			return parseEmptyString(getRatioScoreStringByConditions(p, conditionName, condition2Name, projectTag,
					ratioName, scoreName, skipRatioInfinities, true));
		case NUM_PROTEIN_GROUP_MEMBERS:
			return parseEmptyString(String.valueOf(p.getDifferentPrimaryAccessions().size()));

		case TAXONOMY:
			return p.getOrganismsString();
		case ALTERNATIVE_NAMES:
			return parseEmptyString(p.getAlternativeNamesString());
		case SECONDARY_ACCS:
			return parseEmptyString(p.getSecondaryAccessionsString());
		case CONDITION:
			return parseEmptyString(p.getConditionsString());
		case PEPTIDES_TABLE_BUTTON:
			return "+";
		default:
			return parseEmptyString("");
		}

	}
}
