package edu.scripps.yates.server.util.tablemapper;

import java.util.List;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import gnu.trove.list.array.TIntArrayList;

public class ConditionToPeptideTableMapper extends AbstractTableMapper<Condition, Peptide> {

	private static ConditionToPeptideTableMapper instance;

	private ConditionToPeptideTableMapper() {

	}

	public static ConditionToPeptideTableMapper getInstance() {
		if (instance == null) {
			instance = new ConditionToPeptideTableMapper();
		}
		return instance;
	}

	@Override
	public TIntArrayList queryForIDs(Condition condition) {
		final List<Integer> peptideIDs = PreparedCriteria.getPeptideIDsFromCondition(condition);
		return transformToTIntArray(peptideIDs);
	}

	@Override
	public int getIDFromT(Condition t) {
		return t.getId();
	}

	@Override
	public int getIDFromY(Peptide y) {
		return y.getId();
	}

}
