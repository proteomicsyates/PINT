package edu.scripps.yates.server.util.tablemapper;

import java.util.List;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import gnu.trove.list.array.TIntArrayList;

public class ConditionToProteinTableMapper extends AbstractTableMapper<Condition, Protein> {

	private static ConditionToProteinTableMapper instance;

	private ConditionToProteinTableMapper() {

	}

	public static ConditionToProteinTableMapper getInstance() {
		if (instance == null) {
			instance = new ConditionToProteinTableMapper();
		}
		return instance;
	}

	@Override
	public TIntArrayList queryForIDs(Condition condition) {
		final List<Integer> proteinIDs = PreparedCriteria.getProteinIDsFromCondition(condition);
		return transformToTIntArray(proteinIDs);
	}

	@Override
	public int getIDFromT(Condition t) {
		return t.getId();
	}

	@Override
	public int getIDFromY(Protein y) {
		return y.getId();
	}

}
