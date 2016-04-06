package edu.scripps.yates.shared.columns.comparator;

import java.util.List;
import java.util.Map;

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
			return compareNumberStrings(amountString1, amountString2);

		} else if (columnName == ColumnName.PSM_SCORE && scoreName != null) {
			final ScoreBean score1 = o1.getScoreByName(scoreName);
			final ScoreBean score2 = o2.getScoreByName(scoreName);
			String value1 = null;
			String value2 = null;
			if (score1 != null)
				value1 = score1.getValue();
			if (score2 != null)
				value2 = score2.getValue();

			return compareNumberStrings(value1, value2);

		} else if (columnName == ColumnName.PSM_RATIO) {
			return SharedDataUtils.compareRatios(o1, o2, conditionName, condition2Name, projectTag, ratioName, false);

		} else {
			try {
				switch (columnName) {
				case ACC:
					return o1.getProteinAccessionString().compareTo(o2.getProteinAccessionString());
				case PSM_ID:
					return o1.getPsmID().compareTo(o2.getPsmID());
				case PSM_RUN_ID:
					return o1.getMsRun().getRunID().compareTo(o2.getMsRun().getRunID());
				case DESCRIPTION:
					return o1.getProteinDescriptionString().compareTo(o2.getProteinDescriptionString());
				case PEPTIDE_SEQUENCE:
					return o1.getSequence().compareTo(o2.getSequence());
				case PEPTIDE_PI:
					return o1.getPi().compareTo(o2.getPi());
				case PTM_SCORE:
					// try to convert to numbers:
					final String ptmScoreString1 = o1.getPTMScoreString();
					final String ptmScoreString2 = o2.getPTMScoreString();
					return compareNumberStrings(ptmScoreString1, ptmScoreString2);
				case PTMS:
					return o1.getPTMString().compareTo(o2.getPTMString());
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
					if (o1.getChargeState() != null && o2.getChargeState() != null) {
						return Integer.compare(Integer.valueOf(o1.getChargeState()),
								Integer.valueOf(o2.getChargeState()));
					}
					return 0;
				case PEPTIDE_LENGTH:
					return Integer.compare(o1.getSequence().length(), o2.getSequence().length());

				case POSITION_IN_PROTEIN:
					int position1 = getMinPosition(o1);
					int position2 = getMinPosition(o2);
					return Integer.compare(position1, position2);

				case TAXONOMY:
					return o1.getOrganismsString().compareTo(o2.getOrganismsString());

				case CONDITION:
					return o1.getConditionsString().compareTo(o2.getConditionsString());
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

	private int getMinPosition(PSMBean o1) {
		int min = Integer.MAX_VALUE;
		final Map<String, List<Integer>> startingPositions = o1.getStartingPositions();
		for (List<Integer> positions : startingPositions.values()) {
			for (Integer position : positions) {
				if (min > position)
					min = position;
			}
		}

		return min;
	}

}