package edu.scripps.yates.shared.columns.comparator;

import java.util.List;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.PTMBean;
import edu.scripps.yates.shared.model.ScoreBean;
import edu.scripps.yates.shared.util.DataGridRenderValue;
import edu.scripps.yates.shared.util.SharedDataUtils;

public class PSMComparator extends BeanComparator<PSMBean> {

	/**
	 *
	 */
	private static final long serialVersionUID = 702229333822421895L;

	public PSMComparator() {
		super();
	}

	public PSMComparator(ColumnName columnName) {
		super(columnName);
	}

	public PSMComparator(ColumnName columnName, String conditionName, AmountType amountType, String projectTag) {
		super(columnName, conditionName, amountType, projectTag);
	}

	public PSMComparator(ColumnName columnName, String condition1Name, String condition2Name, String projectTag,
			String ratioName) {
		super(columnName, condition1Name, condition2Name, projectTag, ratioName);
	}

	public PSMComparator(ColumnName columnName, String scoreName) {
		super(columnName, scoreName);
	}

	@Override
	public int compare(PSMBean o1, PSMBean o2) {
		if (columnName == ColumnName.PSM_AMOUNT && conditionName != null) {
			// if the column name doesn't fit with any other column , it is
			// because
			// it is a experimental condition name
			String amountString1 = DataGridRenderValue
					.getAmountDataGridRenderValue(o1, conditionName, amountType, projectTag).getValue();
			String amountString2 = DataGridRenderValue
					.getAmountDataGridRenderValue(o2, conditionName, amountType, projectTag).getValue();
			return compareNumberStrings(amountString1, amountString2, ascendant, true);

		} else if (columnName == ColumnName.PSM_SCORE && scoreName != null) {
			final ScoreBean score1 = o1.getScoreByName(scoreName);
			final ScoreBean score2 = o2.getScoreByName(scoreName);
			String value1 = null;
			String value2 = null;
			if (score1 != null)
				value1 = score1.getValue();
			if (score2 != null)
				value2 = score2.getValue();

			return compareNumberStrings(value1, value2, ascendant, true);

		} else if (columnName == ColumnName.PSM_RATIO || columnName == ColumnName.PSM_RATIO_GRAPH) {
			return compareRatios(o1, o2, conditionName, condition2Name, projectTag, ratioName, false, ascendant);

		} else if (columnName == ColumnName.PSM_RATIO_SCORE) {
			return compareRatioScores(o1, o2, conditionName, condition2Name, projectTag, ratioName, false, ascendant);
		} else {
			try {
				switch (columnName) {
				case ACC:
					return compareStrings(o1.getProteinAccessionString(), o2.getProteinAccessionString(), ascendant,
							true);
				case PSM_ID:
					return compareStrings(o1.getPsmID(), o2.getPsmID(), ascendant, true);
				case PSM_RUN_ID:
					return compareStrings(o1.getMsRun().getRunID(), o2.getMsRun().getRunID(), ascendant, true);
				case DESCRIPTION:
					return compareStrings(o1.getProteinDescriptionString(), o2.getProteinDescriptionString(), ascendant,
							true);
				case PEPTIDE_SEQUENCE:
					return compareStrings(o1.getSequence(), o2.getSequence(), ascendant, true);
				case PEPTIDE_PI:
					return compareNumbers(o1.getPi(), o2.getPi(), ascendant);
				case PTM_SCORE:
					// try to convert to numbers:
					final String ptmScoreString1 = o1.getPTMScoreString();
					final String ptmScoreString2 = o2.getPTMScoreString();
					return compareNumberStrings(ptmScoreString1, ptmScoreString2, ascendant, true);
				case PTMS:
					return compareStrings(o1.getPTMString(), o2.getPTMString(), ascendant, true);
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
					return compareNumberStrings(o1.getChargeState(), o2.getChargeState(), ascendant, true);
				case PEPTIDE_LENGTH:
					return Integer.compare(o1.getSequence().length(), o2.getSequence().length());

				case POSITION_IN_PROTEIN:
					int position1 = SharedDataUtils.getMinStartingPosition(o1);
					int position2 = SharedDataUtils.getMinStartingPosition(o2);
					return Integer.compare(position1, position2);

				case TAXONOMY:
					return compareStrings(o1.getOrganismsString(), o2.getOrganismsString(), ascendant, true);

				case CONDITION:
					return compareStrings(o1.getConditionsString(), o2.getConditionsString(), ascendant, true);
				case PEPTIDE_EVIDENCE:
					return o1.getRelation().name().compareTo(o2.getRelation().name());
				default:
					break;
				}
			} catch (NullPointerException e) {

			}
		}
		return 0;
	}

}