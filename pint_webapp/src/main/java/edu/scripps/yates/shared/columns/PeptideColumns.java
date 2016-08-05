package edu.scripps.yates.shared.columns;

import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.util.DataGridRenderValue;
import edu.scripps.yates.shared.util.SharedDataUtils;
import edu.scripps.yates.shared.util.UniprotFeatures;

public class PeptideColumns implements ColumnProvider<PeptideBean> {
	private static PeptideColumns instance;
	private ArrayList<ColumnWithVisibility> columns;
	private static final String EMPTY_VALUE = "-";

	private PeptideColumns() {

	}

	public static PeptideColumns getInstance() {
		if (instance == null) {
			instance = new PeptideColumns();
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
			ColumnWithVisibility col = new ColumnWithVisibility(ColumnName.LINK_TO_PRIDE_CLUSTER, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.ACC, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_EVIDENCE, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_SEQUENCE, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_LENGTH, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.POSITION_IN_PROTEIN, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.SPECTRUM_COUNT, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.TAXONOMY, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_AMOUNT, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_RATIO, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_RATIO_GRAPH, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_RATIO_SCORE, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.CONDITION, false);
			columns.add(col);

			col = new ColumnWithVisibility(ColumnName.PEPTIDE_DOMAIN_FAMILIES, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_SECONDARY_STRUCTURE, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_ACTIVE_SITE, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_EXPERIMENTAL_INFO, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_MOLECULAR_PROCESSING, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_PTM, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_NATURAL_VARIATIONS, false);
			columns.add(col);

		}
		return columns;
	}

	@Override
	public String getValue(ColumnName columnName, PeptideBean p, String conditionName, String condition2Name,
			String projectTag, AmountType amountType, String scoreName, String ratioName, boolean skipRatioInfinities) {
		if (p == null) {
			return parseEmptyString(null);
		}
		switch (columnName) {

		case PEPTIDE_SEQUENCE:
			return parseEmptyString(p.getSequence());
		case ACC:
			return parseEmptyString(p.getProteinAccessionString());
		case DESCRIPTION:
			return parseEmptyString(p.getProteinDescriptionString());
		case PEPTIDE_AMOUNT:
			return parseEmptyString(DataGridRenderValue
					.getAmountDataGridRenderValue(p, conditionName, amountType, projectTag).getValue());
		case PEPTIDE_RATIO:
			return parseEmptyString(p.getRatioStringByConditions(conditionName, condition2Name, projectTag, ratioName,
					skipRatioInfinities));
		case PEPTIDE_LENGTH:
			if (p.getSequence() != null)
				return parseEmptyString(String.valueOf(p.getSequence().length()));
			return EMPTY_VALUE;
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
			return parseEmptyString(SharedDataUtils.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PEPTIDE_DOMAIN_FAMILIES:
			return parseEmptyString(SharedDataUtils.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PEPTIDE_EXPERIMENTAL_INFO:
			return parseEmptyString(SharedDataUtils.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PEPTIDE_MOLECULAR_PROCESSING:
			return parseEmptyString(SharedDataUtils.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PEPTIDE_NATURAL_VARIATIONS:
			return parseEmptyString(SharedDataUtils.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PEPTIDE_SECONDARY_STRUCTURE:
			return parseEmptyString(SharedDataUtils.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case PEPTIDE_PTM:
			return parseEmptyString(SharedDataUtils.getUniprotFeatureString(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
		case LINK_TO_PRIDE_CLUSTER:
			return "+";
		default:

			return parseEmptyString("");
		}
	}

	private String parseEmptyString(String string) {
		// if (string == null || "0".equals(string) || "".equals(string))
		if (string == null || "".equals(string))
			return EMPTY_VALUE;
		return string;
	}

	private String parseZeroAndEmptyString(String string) {
		if (string == null || "0".equals(string) || "".equals(string))
			return EMPTY_VALUE;
		return string;
	}
}
