package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper;

import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
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
	public TIntArrayList doMapping(Condition condition) {
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

	public List<Condition> getConditions(Peptide peptide) {
		final TIntArrayList mapIDs1 = mapIDs1(peptide);
		final List<Condition> ret = new ArrayList<Condition>();
		for (final int id : mapIDs1.toArray()) {
			final Condition condition = ContextualSessionHandler.load(id, Condition.class);
			if (condition != null) {
				ret.add(condition);
			}
		}

		return ret;
	}

}
