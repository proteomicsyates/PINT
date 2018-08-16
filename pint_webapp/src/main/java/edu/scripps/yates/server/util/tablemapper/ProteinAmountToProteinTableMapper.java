package edu.scripps.yates.server.util.tablemapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.wrappers.AmountValueWrapper;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.TIntHashSet;

public class ProteinAmountToProteinTableMapper {
	private final static Logger log = Logger.getLogger(ProteinAmountToProteinTableMapper.class);
	private final TIntObjectHashMap<List<AmountValueWrapper>> protein_to_proteinAmountValue = new TIntObjectHashMap<List<AmountValueWrapper>>();
	private static ProteinAmountToProteinTableMapper instance;
	private final TIntHashSet conditionIDs = new TIntHashSet();

	private ProteinAmountToProteinTableMapper() {

	}

	public static ProteinAmountToProteinTableMapper getInstance() {
		if (instance == null) {
			instance = new ProteinAmountToProteinTableMapper();
		}
		return instance;
	}

	public void addCondition(Condition condition) {
		if (!conditionIDs.contains(condition.getId())) {
			log.info("Caching protein amounts of condition " + condition.getName());
			conditionIDs.add(condition.getId());
			// get the proteinvaluewrappers that point to the
			// condition

			final List<AmountValueWrapper> proteinAmountValues = PreparedCriteria
					.getProteinAmountValuesWrappersFromCondition(condition);
			for (final AmountValueWrapper proteinAmountValue : proteinAmountValues) {

				final int proteinID = proteinAmountValue.getItemID();
				if (!protein_to_proteinAmountValue.containsKey(proteinID)) {
					final List<AmountValueWrapper> list = new ArrayList<AmountValueWrapper>();
					protein_to_proteinAmountValue.put(proteinID, list);
				}

				protein_to_proteinAmountValue.get(proteinID).add(proteinAmountValue);
			}
		}
	}

	public List<AmountValueWrapper> mapProtein(Protein protein) {

		return protein_to_proteinAmountValue.get(protein.getId());
	}

}
