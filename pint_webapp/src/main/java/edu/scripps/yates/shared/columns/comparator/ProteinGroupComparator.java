package edu.scripps.yates.shared.columns.comparator;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.util.DataGridRenderValue;
import edu.scripps.yates.shared.util.SharedDataUtils;

public class ProteinGroupComparator extends BeanComparator<ProteinGroupBean> {

	/**
	 *
	 */
	private static final long serialVersionUID = -8171348085149617072L;

	public ProteinGroupComparator() {
		super();
	}

	public ProteinGroupComparator(ColumnName columnName) {
		super(columnName);
	}

	public ProteinGroupComparator(ColumnName columnName, String experimentalConditionName, AmountType amountType,
			String projectTag) {
		super(columnName, experimentalConditionName, amountType, projectTag);
	}

	public ProteinGroupComparator(ColumnName columnName, String experimentalCondition1Name,
			String experimentalCondition2Name, String projectTag, String ratioName) {
		super(columnName, experimentalCondition1Name, experimentalCondition2Name, projectTag, ratioName);
	}

	@Override
	public int compare(ProteinGroupBean o1, ProteinGroupBean o2) {
		if (columnName == ColumnName.PROTEIN_AMOUNT) {
			DataGridRenderValue data1 = DataGridRenderValue.getAmountDataGridRenderValue(o1, conditionName, amountType,
					projectTag);
			DataGridRenderValue data2 = DataGridRenderValue.getAmountDataGridRenderValue(o2, conditionName, amountType,
					projectTag);

			String amountString1 = data1.getValue();
			String amountString2 = data2.getValue();

			// String amountString1 = o1.getAmountString(conditionName,
			// projectTag);
			// String amountString2 = o2.getAmountString(conditionName,
			// projectTag);
			try {
				if ("".equals(amountString1))
					amountString1 = "0";
				if ("".equals(amountString2))
					amountString2 = "0";
				final Double amount1 = Double.valueOf(amountString1);
				final Double amount2 = Double.valueOf(amountString2);
				return amount1.compareTo(amount2);
			} catch (NumberFormatException e) {
				return amountString1.compareTo(amountString2);
			}
		} else if (columnName == ColumnName.PROTEIN_RATIO) {
			return SharedDataUtils.compareRatios(o1, o2, conditionName, condition2Name, projectTag, ratioName, false);
		}
		switch (columnName) {
		case ACC:
			return o1.getPrimaryAccessionsString().compareTo(o2.getPrimaryAccessionsString());
		case DESCRIPTION:
			String description1 = o1.getDescriptionsString();
			String description2 = o2.getDescriptionsString();
			if (description1 == null)
				description1 = "";
			if (description2 == null)
				description2 = "";

			return description1.compareToIgnoreCase(description2);
		case SPECTRUM_COUNT:
			return Integer.compare(o1.getNumPSMs(), o2.getNumPSMs());

		case COVERAGE:
			return ProteinComparator.compareCoverages(o1.get(0), o2.get(0));
		case PROTEIN_SEQUENCE_COVERAGE_IMG:
			return ProteinComparator.compareCoverages(o1.get(0), o2.get(0));

		case GENE:
			final String genesString = o1.getGenesString(true);
			final String genesString2 = o2.getGenesString(true);
			if (genesString.equals(genesString2))
				return 0;
			// in order to have the empty ones at the bottom
			if ("".equals(genesString) || "-".equals(genesString))
				return 1;
			if ("".equals(genesString2) || "-".equals(genesString2))
				return -1;
			return genesString.compareToIgnoreCase(genesString2);
		case SEQUENCE_COUNT:
			return Integer.compare(o1.getNumPeptides(), o2.getNumPeptides());
		case PROTEIN_GROUP_TYPE:
			return o1.getGroupMemberEvidences().compareTo(o2.getGroupMemberEvidences());
		case UNIPROT_PROTEIN_EXISTENCE:
			return o1.getGroupMemberExistences().compareTo(o2.getGroupMemberExistences());
		case NUM_PROTEIN_GROUP_MEMBERS:
			return Integer.compare(o1.getDifferentPrimaryAccessions().size(),
					o2.getDifferentPrimaryAccessions().size());
		case SECONDARY_ACCS:
			return o1.getSecondaryAccessionsString().compareTo(o2.getSecondaryAccessionsString());
		case CONDITION:
			return o1.getConditionsString().compareTo(o2.getConditionsString());
		default:
			break;
		}
		return 0;
	}

}
