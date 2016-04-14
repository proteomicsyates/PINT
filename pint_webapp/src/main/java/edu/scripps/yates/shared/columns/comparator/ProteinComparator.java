package edu.scripps.yates.shared.columns.comparator;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.util.DataGridRenderValue;

public class ProteinComparator extends BeanComparator<ProteinBean> {

	/**
	 *
	 */
	private static final long serialVersionUID = -5795708428622637833L;

	public ProteinComparator() {
		super();
	};

	public ProteinComparator(ColumnName columnName) {
		super(columnName);
	}

	public ProteinComparator(ColumnName columnName, String experimentalConditionName, AmountType amountType,
			String projectTag) {
		super(columnName, experimentalConditionName, amountType, projectTag);
	}

	public ProteinComparator(ColumnName columnName, String experimentalCondition1Name,
			String experimentalCondition2Name, String projectTag, String ratioName) {
		super(columnName, experimentalCondition1Name, experimentalCondition2Name, projectTag, ratioName);
	}

	@Override
	public int compare(ProteinBean o1, ProteinBean o2) {
		if (columnName == ColumnName.PROTEIN_AMOUNT) {
			DataGridRenderValue data1 = DataGridRenderValue.getAmountDataGridRenderValue(o1, conditionName, amountType,
					projectTag);
			DataGridRenderValue data2 = DataGridRenderValue.getAmountDataGridRenderValue(o2, conditionName, amountType,
					projectTag);

			String amountString1 = data1.getValue();
			String amountString2 = data2.getValue();

			try {
				if ("".equals(amountString1))
					amountString1 = ascendant ? String.valueOf(Double.MAX_VALUE) : String.valueOf(-Double.MAX_VALUE);
				if ("".equals(amountString2))
					amountString2 = ascendant ? String.valueOf(-Double.MAX_VALUE) : String.valueOf(Double.MAX_VALUE);
				final Double amount1 = Double.valueOf(amountString1);
				final Double amount2 = Double.valueOf(amountString2);
				return compareNumbers(amount1, amount2, ascendant);
			} catch (NumberFormatException e) {
				return compareStrings(amountString1, amountString2, ascendant, true);
			}
		} else if (columnName == ColumnName.PROTEIN_RATIO || columnName == ColumnName.PROTEIN_RATIO_GRAPH) {
			return compareRatios(o1, o2, conditionName, condition2Name, projectTag, ratioName, false, ascendant);
		} else if (columnName == ColumnName.PROTEIN_RATIO_SCORE) {
			return compareRatioScores(o1, o2, conditionName, condition2Name, projectTag, ratioName, false, ascendant);
		}
		switch (columnName) {
		case ACC:
			return compareStrings(o1.getPrimaryAccession().getAccession(), o2.getPrimaryAccession().getAccession(),
					ascendant, false);
		case DESCRIPTION:
			String description1 = o1.getPrimaryAccession().getDescription();
			String description2 = o2.getPrimaryAccession().getDescription();
			return compareStrings(description1, description2, ascendant, true);

		case SPECTRUM_COUNT:
			return Integer.compare(o1.getNumPSMs(), o2.getNumPSMs());

		case COVERAGE:
			return compareNumbers(o1.getCoverage(), o2.getCoverage(), ascendant);
		case PROTEIN_SEQUENCE_COVERAGE_IMG:
			return compareNumbers(o1.getCoverage(), o2.getCoverage(), ascendant);
		case MOL_W:
			return Double.compare(o1.getMw(), o2.getMw());
		case PROTEIN_PI:
			return Double.compare(o1.getPi(), o2.getPi());
		case GENE:
			// take the primary only. If not available, take the rest
			String genesString = o1.getGenesString(true);
			if ("".equals(genesString))
				genesString = o1.getGenesString(false);
			String genesString2 = o2.getGenesString(true);
			if ("".equals(genesString2))
				genesString2 = o2.getGenesString(false);

			return compareStrings(genesString, genesString2, ascendant, true);
		case SEQUENCE_COUNT:
			return Integer.compare(o1.getNumPeptides(), o2.getNumPeptides());
		case PROTEIN_LENGTH:
			return Integer.compare(o1.getLength(), o2.getLength());
		case ALTERNATIVE_NAMES:
			return compareStrings(o1.getAlternativeNamesString(), o2.getAlternativeNamesString(), ascendant, true);
		case PROTEIN_FUNCTION:
			return compareStrings(o1.getFunctionString(), o2.getFunctionString(), ascendant, true);
		case SECONDARY_ACCS:
			return compareStrings(o1.getSecondaryAccessionsString(), o2.getSecondaryAccessionsString(), ascendant,
					true);
		case CONDITION:
			return compareStrings(o1.getConditionsString(), o2.getConditionsString(), ascendant, false);
		case OMIM:
			return compareStrings(o1.getOmimIDString(), o2.getOmimIDString(), ascendant, true);
		case PROTEIN_GROUP_TYPE:
			return o1.getEvidence().name().compareTo(o2.getEvidence().name());
		case UNIPROT_PROTEIN_EXISTENCE:
			String name1 = "";
			if (o1.getUniprotProteinExistence() != null) {
				name1 = o1.getUniprotProteinExistence().getName();
			}
			String name2 = "";
			if (o2.getUniprotProteinExistence() != null) {
				name2 = o2.getUniprotProteinExistence().getName();
			}
			return compareStrings(name1, name2, ascendant, true);
		default:
			break;
		}
		return 0;
	}

}
