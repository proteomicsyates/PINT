package edu.scripps.yates.client.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.ListBox;

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
import edu.scripps.yates.shared.model.RatioDistribution;
import edu.scripps.yates.shared.model.ScoreBean;
import edu.scripps.yates.shared.model.interfaces.ContainsRatios;
import edu.scripps.yates.shared.util.DataGridRenderValue;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.SharedDataUtil;
import edu.scripps.yates.shared.util.UniprotFeatures;

public class ClientDataUtil {

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
				return parseEmptyString(ClientNumberFormat.getFormat("#.#").format(coverage * 100) + "%");
			} else {
				return "-";
			}
		case PROTEIN_PI:
			// return parseZeroAndEmptyString(p.getPi()));
			return parseEmptyString(ClientNumberFormat.getFormat("#.##").format(p.getPi()));
		case GENE:
			return parseEmptyString(p.getGenesString(false));
		case MOL_W:
			final double mw = p.getMw();
			final String format = ClientNumberFormat.getFormat("#.###").format(mw);
			final String parseZeroAndEmptyString = parseEmptyString(format);
			return parseZeroAndEmptyString;
		case PROTEIN_SCORE:
			final ScoreBean scoreByName = p.getScoreByName(scoreName);
			if (scoreByName != null) {
				try {
					final Double valueOf = Double.valueOf(scoreByName.getValue());
					if (valueOf > 0.01) {
						return ClientNumberFormat.getFormat("#.###").format(valueOf);
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

	public static String parseEmptyString(String string) {
		return SharedDataUtil.parseEmptyString(string);
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
					projectTag, new ClientNumberFormat("#.##")).getValue());
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
			return ClientNumberFormat.getFormat("#").format(number);
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
			return ClientNumberFormat.getScientificFormat(minDecimals, maxDecimals).format(number);

		} else {
			return ClientNumberFormat.getFormat(pattern.toString()).format(number);
		}
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
							format = ClientNumberFormat.getScientificFormat(2, 3).format(value);
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
				return parseEmptyString(ClientNumberFormat.getFormat("#.##").format(p.getPi()));
			return parseEmptyString("");
		case PTM_SCORE:
			return parseEmptyString(p.getPTMScoreString());
		case PTMS:
			return parseEmptyString(p.getPTMString());
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
						return ClientNumberFormat.getFormat("#.###").format(valueOf);
					else
						return scoreByName.getValue();
				} catch (final NumberFormatException e) {
					return scoreByName.getValue();
				}

			}
			return parseEmptyString("");
		case PSM_AMOUNT:
			return parseEmptyString(DataGridRenderValue.getAmountDataGridRenderValue(p, conditionName, amountType,
					projectTag, new ClientNumberFormat("#.##")).getValue());

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
						try {
							final String format = ClientNumberFormat.getFormat("#.##").format(doubleValue);
							if (!"".equals(sb.toString()))
								sb.append(SharedConstants.SEPARATOR);
							sb.append(format);
						} catch (final NumberFormatException e2) {

						}
					}
				}
			}
		}

		return sb.toString();
	}

	public static String getAmountString(ProteinGroupBean proteinGroup, String conditionName, String projectTag) {
		final StringBuilder sb = new StringBuilder();
		for (final ProteinBean protein : proteinGroup) {
			if (!"".equals(sb.toString()))
				sb.append(SharedConstants.SEPARATOR);
			sb.append(getAmountString(protein.getAmountsByExperimentalCondition().get(conditionName), projectTag));
		}
		return sb.toString();
	}

	public static String getRatioDistributionString(RatioDistribution ratioDistribution) {
		final ClientNumberFormat format = new ClientNumberFormat("#.##");
		final StringBuilder sb = new StringBuilder();
		sb.append("Ratio distribution in the entire dataset: " + ratioDistribution.getRatioKey() + " [")
				.append(format.format(ratioDistribution.getMinRatio())).append(",")
				.append(format.format(ratioDistribution.getMaxRatio())).append("]");
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
			final ClientNumberFormat format = new ClientNumberFormat("#.###");
			return format.format(doubleValue);
		} catch (final NumberFormatException e) {
			return score.getValue();
		}
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

	/**
	 * This function builds a new name for the element of conditions.<br>
	 * The name of the condition will be 1AB-ConditionName if there are more than
	 * one project in the project {@link ListBox} or just AB-ConditionName if there
	 * is only one project in the list box.<br>
	 * It will check which is the next symbol for the condition to be added.
	 *
	 * @param conditionName
	 * @param projectName
	 * @param conditionsListBox
	 * @param projectsListBox
	 * @return
	 */
	public static String getNewElementNameForCondition(String conditionName, String projectName,
			ListBox conditionsListBox, ListBox projectsListBox) {
		String conditionSymbol = "";
		String projectSymbol = "";
		final boolean moreThanOneProject = projectsListBox.getItemCount() > 1;
		if (moreThanOneProject) {
			// more than one project
			projectSymbol = parseProjectSymbolFromListBox(projectName, projectsListBox);
		}
		conditionSymbol = getNextAvailableConditionSymbol(conditionsListBox, projectSymbol);

		return projectSymbol + conditionSymbol + "-" + conditionName;
	}

	/**
	 * The project symbol will be searched in the listbox assuming that projects
	 * will be presented as "projectSymbol-projectName".
	 *
	 * @param element
	 * @return the projectSymbol
	 */
	public static String parseProjectSymbolFromListBox(String projectName, ListBox projectListBox) {
		if (projectListBox.getItemCount() <= 1)
			return "";
		for (int i = 0; i < projectListBox.getItemCount(); i++) {
			final String element = projectListBox.getItemText(i);
			if (element.contains("-")) {
				final String name = element.split("-")[1];
				if (name.equals(projectName)) {
					final String symbol = element.split("-")[0];
					return symbol;
				}
			} else {
				return element;
			}
		}
		throw new IllegalArgumentException("No project name found!");
	}

	/**
	 * If the projectSymbol is empty, then the listbox MUST have just one element,
	 * and it will be returned.<br>
	 * If the projectSymbol is not empty, then, the project name will be searched in
	 * the listbox assuming that projects will be presented as
	 * "projectSymbol-projectName".
	 *
	 * @param projectSymbol
	 * @param listBox
	 * @return the projectName
	 */
	public static String parseProjectNameFromListBox(String projectSymbol, ListBox listBox) {
		if (listBox.getItemCount() == 0)
			return "";
		if (projectSymbol == null || "".equals(projectSymbol))
			return listBox.getItemText(0);
		for (int i = 0; i < listBox.getItemCount(); i++) {
			final String element = listBox.getItemText(i);
			if (element.contains("-")) {
				final String symbol = element.split("-")[0];
				if (symbol.equals(projectSymbol))
					return element.split("-")[1];
			} else {
				return element;
			}
		}
		throw new IllegalArgumentException("No project name found!");
	}

	/**
	 * This function builds a new name for the element of project.<br>
	 * The name of the project will be 1-projectName.<br>
	 * It will check which is the next symbol for the project to be added.
	 *
	 * @param projectName
	 * @param projectsListBox
	 * @return
	 */
	public static String getNewElementNameForProject(String projectName, ListBox projectsListBox) {
		final String projectSymbol = getNextAvailableProjectSymbol(projectsListBox);
		if (projectSymbol != null && !"".equals(projectSymbol))
			return projectSymbol + "-" + projectName;
		return projectName;
	}

	/**
	 * Given a project and a {@link ListBox} containing condition symbols (or
	 * empty), it returns the next available condition symbol. The condition symbols
	 * are like: 1AB-ConditionName where the 1 means that the condition is from
	 * project 1 (if there are more projects, otherwise it would be no number) and
	 * the AB is the code for the condition, starting by 'A'.
	 *
	 * @param conditionsListBox
	 * @param projectSymbol
	 * @return
	 */
	private static String getNextAvailableConditionSymbol(ListBox conditionsListBox, String projectSymbol) {
		if (conditionsListBox.getItemCount() == 0) {
			return String.valueOf((char) 65);
		} else {
			final String lastProjectSymbol = parseProjectSymbolFromConditionSelection(
					conditionsListBox.getItemText(conditionsListBox.getItemCount() - 1));
			if (!lastProjectSymbol.equals(projectSymbol)) {
				return String.valueOf((char) 65);
			}
			final String lastConditionSymbol = parseConditionSymbolFromConditionSelection(
					conditionsListBox.getItemText(conditionsListBox.getItemCount() - 1));
			if (lastConditionSymbol.length() == 1) {
				final char charAt = lastConditionSymbol.charAt(0);
				if (charAt == 'Z') {
					return "AA";
				} else {
					final String valueOf = String.valueOf((char) (charAt + 1));
					return valueOf;
				}
			} else {
				// if has already two characteres
				final char charAt = lastConditionSymbol.charAt(1);
				final String valueOf = lastConditionSymbol.charAt(0) + String.valueOf((char) (charAt + 1));
				return valueOf;
			}
		}
	}

	private static String getNextAvailableProjectSymbol(ListBox projectListBox) {
		if (projectListBox.getItemCount() == 0) {
			return "1";
		} else {
			final String itemText = projectListBox.getItemText(projectListBox.getItemCount() - 1);
			final int lastProjectSymbol = Integer.valueOf(parseProjectSymbolFromProjectSelection(itemText));
			return String.valueOf(lastProjectSymbol + 1);
		}
	}

	/**
	 * If element is 1-projectName, then, <br>
	 * the 1 is the symbol. if there is not 1-. return empty string. It could have
	 * more than one number like 12-Project
	 *
	 * @param projectSelection
	 * @return the projectSymbol
	 */
	private static String parseProjectSymbolFromProjectSelection(String projectSelection) {

		if (projectSelection.contains("-")) {
			final String prefix = projectSelection.split("-")[0];
			if (SharedDataUtil.isNumber(prefix)) {
				return prefix;
			}
		}
		return "";

	}

	/**
	 * If element is 1C-ConditionName, then, <br>
	 * the 1 is the project, <br>
	 * and the C is the condition.<br>
	 * if element is 1AC-ConditionName, then, the 1 is the project and the AC is the
	 * condition.
	 *
	 * @param element
	 * @return the C
	 */
	public static String parseConditionSymbolFromConditionSelection(String element) {
		if (element.contains("-")) {
			String prefix = element.split("-")[0];
			final String firstCharacter = String.valueOf(prefix.charAt(0));
			if (SharedDataUtil.isNumber(firstCharacter)) {
				prefix = prefix.substring(1);
			}
			return prefix;
		}
		return "";
	}

	/**
	 * If element is 1C-ConditionName, then, <br>
	 * the 1 is the project, and C is the condition.<br>
	 * If element is 12AB-ConditionName, then,<br>
	 * the 12 is the project and AB is the condition.
	 *
	 * @param element
	 * @return the 1
	 */
	public static String parseProjectSymbolFromConditionSelection(String element) {
		if (element.contains("-")) {
			final String prefix = element.split("-")[0];
			int index = 0;
			while (SharedDataUtil.isNumber(String.valueOf(prefix.charAt(index)))) {
				index++;
			}
			if (index > 0) {
				return prefix.substring(0, index);
			}
		}
		return "";
	}

	public static Set<String> getItemValuesFromListBox(ListBox listBox) {
		final Set<String> selectedItems = new HashSet<String>();
		for (int i = 0; i < listBox.getItemCount(); i++) {
			selectedItems.add(listBox.getValue(i));
		}
		return selectedItems;
	}
}
