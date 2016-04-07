package edu.scripps.yates.shared.columns;

import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.util.NumberFormat;

public class ProteinColumns implements ColumnProvider<ProteinBean> {
	private static ProteinColumns instance;
	private ArrayList<ColumnWithVisibility> columns;

	private ProteinColumns() {

	}

	public static ProteinColumns getInstance() {
		if (instance == null) {
			instance = new ProteinColumns();
		}
		return instance;
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
			col = new ColumnWithVisibility(ColumnName.GENE, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.TAXONOMY, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.MOL_W, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PROTEIN_LENGTH, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PROTEIN_PI, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.COVERAGE, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PROTEIN_SEQUENCE_COVERAGE_IMG, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PROTEIN_GROUP_TYPE, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.SEQUENCE_COUNT, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.SPECTRUM_COUNT, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PROTEIN_AMOUNT, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PROTEIN_RATIO, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PROTEIN_RATIO_GRAPH, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PROTEIN_RATIO_SCORE, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.CONDITION, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.OMIM, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PROTEIN_FUNCTION, true);
			columns.add(col);
		}
		return columns;
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
	public String getValue(ColumnName columnName, ProteinBean p, String conditionName, String condition2Name,
			String projectTag, AmountType amountType, String scoreName, String ratioName, boolean skipRatioInfinities) {
		switch (columnName) {
		case ACC:
			return parseEmptyString(p.getPrimaryAccession().getAccession());
		case DESCRIPTION:
			return parseEmptyString(p.getPrimaryAccession().getDescription());
		case SPECTRUM_COUNT:
			return parseEmptyString(String.valueOf(p.getNumPSMs()));
		case COVERAGE:
			// TODO calculate coverage depending on the PSMs, depending on which
			// runs are coming, from which conditions
			final Double coverage = p.getCoverage();
			if (coverage != null && coverage != 0.0) {
				return parseEmptyString(NumberFormat.getFormat("#.#").format(coverage * 100) + "%");
			} else {
				return "-";
			}
		case PROTEIN_PI:
			// return parseZeroAndEmptyString(p.getPi()));
			return parseEmptyString(NumberFormat.getFormat("#.##").format(p.getPi()));
		case GENE:
			return parseEmptyString(p.getGenesString(false));
		case MOL_W:
			final double mw = p.getMw();
			final String format = NumberFormat.getFormat("#.###").format(mw);
			final String parseZeroAndEmptyString = parseEmptyString(format);
			return parseZeroAndEmptyString;

		case SEQUENCE_COUNT:
			return parseEmptyString(String.valueOf(p.getNumPeptides()));
		case PROTEIN_AMOUNT:
			return parseEmptyString(p.getAmountString(conditionName, projectTag));
		case PROTEIN_RATIO:
			return parseEmptyString(p.getRatioStringByConditions(conditionName, condition2Name, projectTag, ratioName,
					skipRatioInfinities));

		case PROTEIN_RATIO_SCORE:
			return parseEmptyString(p.getRatioScoreStringByConditions(conditionName, condition2Name, projectTag,
					ratioName, skipRatioInfinities));
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
		default:
			return parseEmptyString("");
		}
		return parseEmptyString("");
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
