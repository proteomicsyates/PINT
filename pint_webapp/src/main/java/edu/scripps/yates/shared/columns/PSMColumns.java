package edu.scripps.yates.shared.columns;

import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.PTMBean;
import edu.scripps.yates.shared.model.ScoreBean;
import edu.scripps.yates.shared.util.DataGridRenderValue;
import edu.scripps.yates.shared.util.NumberFormat;
import edu.scripps.yates.shared.util.SharedDataUtils;
import edu.scripps.yates.shared.util.UniprotFeatures;

public class PSMColumns implements ColumnProvider<PSMBean> {
	private static PSMColumns instance;
	private ArrayList<ColumnWithVisibility> columns;
	private static final String EMPTY_VALUE = "-";

	private PSMColumns() {

	}

	public static PSMColumns getInstance() {
		if (instance == null) {
			instance = new PSMColumns();
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
			col = new ColumnWithVisibility(ColumnName.PSM_ID, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PSM_RUN_ID, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.ACC, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_EVIDENCE, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_SEQUENCE, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_LENGTH, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.CHARGE, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.POSITION_IN_PROTEIN, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.TAXONOMY, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PSM_SCORE, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.NUM_PTMS, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.NUM_PTM_SITES, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PTMS, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PTM_SCORE, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_PI, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PSM_AMOUNT, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PSM_RATIO, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PSM_RATIO_GRAPH, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PSM_RATIO_SCORE, false);
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
	public String getValue(ColumnName columnName, PSMBean p, String conditionName, String condition2Name,
			String projectTag, AmountType amountType, String scoreName, String ratioName, boolean skipRatioInfinities) {
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
				return parseEmptyString(NumberFormat.getFormat("#.##").format(p.getPi()));
			return parseEmptyString("");
		case PTM_SCORE:
			return parseEmptyString(p.getPTMScoreString());
		case PTMS:
			return parseEmptyString(p.getPTMString());
		case NUM_PTMS:
			return parseEmptyString(String.valueOf(p.getPtms().size()));
		case NUM_PTM_SITES:
			int count = 0;
			for (PTMBean ptm : p.getPtms()) {
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
						return NumberFormat.getFormat("#.###").format(valueOf);
					else
						return scoreByName.getValue();
				} catch (NumberFormatException e) {
					return scoreByName.getValue();
				}

			}
			return parseEmptyString("");
		case PSM_AMOUNT:
			return parseEmptyString(DataGridRenderValue
					.getAmountDataGridRenderValue(p, conditionName, amountType, projectTag).getValue());

		case CHARGE:
			return parseEmptyString(p.getChargeState());
		case PSM_RATIO:
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
