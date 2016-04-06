package edu.scripps.yates.shared.columns.comparator;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.util.DataGridRenderValue;
import edu.scripps.yates.shared.util.SharedDataUtils;

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
		} else if (columnName == ColumnName.PROTEIN_RATIO_SCORE) {
			return SharedDataUtils.compareRatioScores(o1, o2, conditionName, condition2Name, projectTag, ratioName,
					false);
		}
		switch (columnName) {
		case ACC:
			return o1.getPrimaryAccession().getAccession().compareTo(o2.getPrimaryAccession().getAccession());
		case DESCRIPTION:
			String description1 = o1.getPrimaryAccession().getDescription();
			String description2 = o2.getPrimaryAccession().getDescription();
			if (description1 == null)
				description1 = "";
			if (description2 == null)
				description2 = "";

			return description1.compareToIgnoreCase(description2);
		case SPECTRUM_COUNT:
			return Integer.compare(o1.getNumPSMs(), o2.getNumPSMs());

		case COVERAGE:
			return compareCoverages(o1, o2);
		case PROTEIN_SEQUENCE_COVERAGE_IMG:
			return compareCoverages(o1, o2);
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
		case PROTEIN_LENGTH:
			return Integer.compare(o1.getLength(), o2.getLength());
		case ALTERNATIVE_NAMES:
			return o1.getAlternativeNamesString().compareToIgnoreCase(o2.getAlternativeNamesString());
		case PROTEIN_FUNCTION:
			return o1.getFunctionString().compareToIgnoreCase(o2.getFunctionString());
		case SECONDARY_ACCS:
			return o1.getSecondaryAccessionsString().compareTo(o2.getSecondaryAccessionsString());
		case CONDITION:
			return o1.getConditionsString().compareTo(o2.getConditionsString());
		case OMIM:
			return o1.getOmimIDString().compareTo(o2.getOmimIDString());
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
			return name1.compareTo(name2);
		default:
			break;
		}
		return 0;
	}

	protected static int compareCoverages(ProteinBean o1, ProteinBean o2) {
		Double coverage1 = o1.getCoverage();
		Double coverage2 = o2.getCoverage();
		if (coverage1 == null)
			coverage1 = 0.0;
		if (coverage2 == null)
			coverage2 = 0.0;
		return Double.compare(coverage1, coverage2);
	}

}
