package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
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

	public List<Condition> getConditions(Protein protein) {
		final TIntArrayList mapIDs1 = mapIDs1(protein);

		final List<Condition> ret = new ArrayList<Condition>();
		if (mapIDs1 != null) {
			for (final int id : mapIDs1.toArray()) {
				final Condition condition = ContextualSessionHandler.load(id, Condition.class);
				if (condition != null) {
					ret.add(condition);
				}
			}
		} else {
			final Set<Condition> conditions = protein.getConditions();
			for (final Condition condition : conditions) {
				addObject1(condition);
			}
		}
		return ret;
	}

}
