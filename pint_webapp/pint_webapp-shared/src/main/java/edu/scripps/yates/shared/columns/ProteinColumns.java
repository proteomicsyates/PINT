package edu.scripps.yates.shared.columns;

import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.shared.model.ProteinBean;

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
			col = new ColumnWithVisibility(ColumnName.LINK_TO_PRIDE_CLUSTER, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.LINK_TO_INTACT, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.LINK_TO_COMPLEX_PORTAL, true);
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
			if (
			// Pint.psmCentric
			false) {
				col = new ColumnWithVisibility(ColumnName.SPC_PER_CONDITION, false);
				columns.add(col);
			}

			col = new ColumnWithVisibility(ColumnName.PROTEIN_SCORE, false);
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
			col = new ColumnWithVisibility(ColumnName.REACTOME_ID_LINK, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PROTEIN_DOMAIN_FAMILIES, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PROTEIN_SECONDARY_STRUCTURE, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PROTEIN_ACTIVE_SITE, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PROTEIN_EXPERIMENTAL_INFO, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PROTEIN_MOLECULAR_PROCESSING, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PROTEIN_PTM, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PROTEIN_NATURAL_VARIATIONS, false);
			columns.add(col);
		}
		return columns;
	}

	@Override
	public ColumnWithVisibility getColumn(ColumnName columnName) {
		final List<ColumnWithVisibility> columns2 = getColumns();
		for (final ColumnWithVisibility columnWithVisibility : columns2) {
			if (columnWithVisibility.getColumn() == columnName)
				return columnWithVisibility;
		}
		return null;
	}

	private String parseZeroAndEmptyString(String string) {
		if (string == null || "0".equals(string) || "".equals(string))
			return "-";
		return string;
	}

}
