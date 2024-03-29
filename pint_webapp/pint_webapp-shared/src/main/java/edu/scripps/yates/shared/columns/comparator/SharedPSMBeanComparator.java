package edu.scripps.yates.shared.columns.comparator;

import java.util.List;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.PTMBean;
import edu.scripps.yates.shared.model.PeptideRelation;
import edu.scripps.yates.shared.model.ScoreBean;
import edu.scripps.yates.shared.util.SharedDataUtil;
import edu.scripps.yates.shared.util.UniprotFeatures;

public class SharedPSMBeanComparator extends AbstractPSMBeanComparator {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8933014418383050810L;

	public SharedPSMBeanComparator() {
		super();
	}

	public SharedPSMBeanComparator(ColumnName columnName) {
		super(columnName);
	}

	public SharedPSMBeanComparator(ColumnName columnName, String conditionName, AmountType amountType,
			String projectTag) {
		super(columnName, conditionName, amountType, projectTag);
	}

	public SharedPSMBeanComparator(ColumnName columnName, String condition1Name, String condition2Name,
			String projectTag, String ratioName) {
		super(columnName, condition1Name, condition2Name, projectTag, ratioName);
	}

	public SharedPSMBeanComparator(ColumnName columnName, String condition1Name, String condition2Name,
			String projectTag, String ratioName, String ratioScoreName) {
		super(columnName, condition1Name, condition2Name, projectTag, ratioName, ratioScoreName);
	}

	public SharedPSMBeanComparator(ColumnName columnName, String scoreName) {
		super(columnName, scoreName);
	}

	@Override
	public int compare(PSMBean o1, PSMBean o2) {
		if (columnName == ColumnName.PSM_SCORE && scoreName != null) {
			final ScoreBean score1 = o1.getScoreByName(scoreName);
			final ScoreBean score2 = o2.getScoreByName(scoreName);
			String value1 = null;
			String value2 = null;
			if (score1 != null)
				value1 = score1.getValue();
			if (score2 != null)
				value2 = score2.getValue();

			return compareNumberStrings(value1, value2, true);

		} else if (columnName == ColumnName.PSM_RATIO || columnName == ColumnName.PSM_RATIO_GRAPH) {
			return compareRatios(o1, o2, conditionName, condition2Name, projectTag, ratioName, false);

		} else if (columnName == ColumnName.PSM_RATIO_SCORE) {
			return compareRatioScores(o1, o2, conditionName, condition2Name, projectTag, ratioName, scoreName, false);
		} else {
			try {
				switch (columnName) {
				case ACC:
					return compareStrings(o1.getProteinAccessionString(), o2.getProteinAccessionString(), true);
				case PSM_ID:
					return compareStrings(o1.getPsmID(), o2.getPsmID(), true);
				case PSM_RUN_ID:
					return compareStrings(o1.getMsRun().getRunID(), o2.getMsRun().getRunID(), true);
				case DESCRIPTION:
					return compareStrings(o1.getProteinDescriptionString(), o2.getProteinDescriptionString(), true);
				case PEPTIDE_SEQUENCE:
					return compareStrings(o1.getSequence(), o2.getSequence(), true);
				case PEPTIDE_PI:
					return compareNumbers(o1.getPi(), o2.getPi());
				case PTM_SCORE:
					// try to convert to numbers:
					final String ptmScoreString1 = o1.getPTMScoreString();
					final String ptmScoreString2 = o2.getPTMScoreString();
					return compareNumberStrings(ptmScoreString1, ptmScoreString2, true);
				case PTMS:
					return compareStrings(o1.getPTMString(), o2.getPTMString(), true);
				case NUM_PTMS:
					List<PTMBean> ptms1 = o1.getPtms();
					int numPtms1 = ptms1 != null ? ptms1.size() : 0;
					List<PTMBean> ptms2 = o2.getPtms();
					int numPtms2 = ptms2 != null ? ptms2.size() : 0;
					return Integer.compare(numPtms1, numPtms2);
				case NUM_PTM_SITES:
					int ptmSites1 = 0;
					if (o1.getPtms() != null)
						for (PTMBean ptm : o1.getPtms()) {
							ptmSites1 += ptm.getPtmSites().size();
						}
					int ptmSites2 = 0;
					if (o2.getPtms() != null)
						for (PTMBean ptm : o2.getPtms()) {
							ptmSites2 += ptm.getPtmSites().size();
						}
					return Integer.compare(ptmSites1, ptmSites2);
				case CHARGE:
					return compareNumberStrings(o1.getChargeState(), o2.getChargeState(), true);
				case PEPTIDE_LENGTH:
					return Integer.compare(o1.getSequence().length(), o2.getSequence().length());

				case POSITION_IN_PROTEIN:
					int position1 = SharedDataUtil.getMinStartingPosition(o1);
					int position2 = SharedDataUtil.getMinStartingPosition(o2);
					return Integer.compare(position1, position2);

				case TAXONOMY:
					return compareStrings(o1.getOrganismsString(), o2.getOrganismsString(), true);

				case CONDITION:
					return compareStrings(o1.getConditionsString(), o2.getConditionsString(), true);
				case PEPTIDE_EVIDENCE:
					final PeptideRelation relation = o1.getRelation();
					final PeptideRelation relation2 = o2.getRelation();
					String relationName1 = "";
					String relationName2 = "";
					if (relation != null) {
						relationName1 = relation.name();
					}
					if (relation2 != null) {
						relationName2 = relation.name();
					}
					return relationName1.compareTo(relationName2);
				case PEPTIDE_ACTIVE_SITE:
				case PEPTIDE_DOMAIN_FAMILIES:
				case PEPTIDE_NATURAL_VARIATIONS:
				case PEPTIDE_SECONDARY_STRUCTURE:
				case PEPTIDE_EXPERIMENTAL_INFO:
				case PEPTIDE_MOLECULAR_PROCESSING:
				case PEPTIDE_PTM:
					final String[] uniprotFeatures = UniprotFeatures.getUniprotFeaturesByColumnName(columnName);
					if (uniprotFeatures != null && uniprotFeatures.length > 0) {
						return SharedDataUtil.getUniprotFeatureString(o1, uniprotFeatures)
								.compareTo(SharedDataUtil.getUniprotFeatureString(o2, uniprotFeatures));
					} else {
						return 0;
					}
				default:
					break;
				}
			} catch (NullPointerException e) {

			}
		}
		return 0;
	}

}
