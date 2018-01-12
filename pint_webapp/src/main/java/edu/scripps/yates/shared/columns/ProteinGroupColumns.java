package edu.scripps.yates.shared.columns;

import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.util.DataGridRenderValue;

public class ProteinGroupColumns implements ColumnProvider<ProteinGroupBean> {
	private static ProteinGroupColumns instance;
	private ArrayList<ColumnWithVisibility> columns;

	private ProteinGroupColumns() {

	}

	public static ProteinGroupColumns getInstance() {
		if (instance == null) {
			instance = new ProteinGroupColumns();
		}
		return instance;
	}

	@Override
	public ColumnWithVisibility getColumn(ColumnName columnName) {
		final List<ColumnWithVisibility> columns2 = getColumns();
		for (ColumnWithVisibility columnWithVisibility : columns2) {
			if (columnWithVisibility.getColumn() == columnName)
				return columnWithVisibility;
		}
		return null;
	}

	@Override
	public List<ColumnWithVisibility> getColumns() {
		if (columns == null) {
			columns = new ArrayList<ColumnWithVisibility>();

			ColumnWithVisibility col = new ColumnWithVisibility(ColumnName.PEPTIDES_TABLE_BUTTON, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.ACC, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.SECONDARY_ACCS, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.DESCRIPTION, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.ALTERNATIVE_NAMES, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.UNIPROT_PROTEIN_EXISTENCE, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PROTEIN_GROUP_TYPE, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.NUM_PROTEIN_GROUP_MEMBERS, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.GENE, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.TAXONOMY, false);
			columns.add(col);
			// addColumn(ColumnName.COVERAGE, false);
			col = new ColumnWithVisibility(ColumnName.PROTEIN_SEQUENCE_COVERAGE_IMG, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.SEQUENCE_COUNT, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.SPECTRUM_COUNT, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.SPC_PER_CONDITION, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PROTEIN_AMOUNT, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PROTEIN_RATIO, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.CONDITION, false);
			columns.add(col);
		}
		return columns;
	}

	@Override
	public String getValue(ColumnName columnName, ProteinGroupBean p, String conditionName, String condition2Name,
			String projectTag, AmountType amountType, String scoreName, String ratioName, boolean skipRatioInfinities) {
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
			return parseEmptyString(p.getAmountString(conditionName, projectTag));
		case PROTEIN_RATIO:
			return parseEmptyString(p.getRatioStringByConditions(conditionName, condition2Name, projectTag, ratioName,
					skipRatioInfinities, true));
		case PROTEIN_RATIO_SCORE:
			return parseEmptyString(p.getRatioScoreStringByConditions(conditionName, condition2Name, projectTag,
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

	private String parseZeroAndEmptyString(String string) {
		if (string == null || "0".equals(string) || "".equals(string))
			return "-";
		return string;
	}

	private String parseEmptyString(String string) {
		if (string == null || "".equals(string))
			return "-";
		return string;
	}
}
