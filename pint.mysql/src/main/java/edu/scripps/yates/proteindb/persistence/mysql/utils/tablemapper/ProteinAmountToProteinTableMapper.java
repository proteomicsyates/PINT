package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAmount;
import edu.scripps.yates.proteindb.persistence.mysql.RatioDescriptor;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.ConditionIDToProteinAmountIDTableMapper;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public class ProteinAmountToProteinTableMapper {
	private final static Logger log = Logger.getLogger(ProteinAmountToProteinTableMapper.class);
	private final TIntObjectHashMap<List<ProteinAmount>> protein_to_proteinAmountValue = new TIntObjectHashMap<List<ProteinAmount>>();
	private static ProteinAmountToProteinTableMapper instance;
	private final TIntHashSet conditionIDs = new TIntHashSet();
	private final TIntObjectHashMap<RatioDescriptor> ratioDescriptorMap = new TIntObjectHashMap<RatioDescriptor>();

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
			final TIntSet proteinAmountIDs = ConditionIDToProteinAmountIDTableMapper.getInstance()
					.getProteinAmountIDsFromConditionID(condition.getId());
			final List<ProteinAmount> proteinAmounts = (List<ProteinAmount>) PreparedCriteria
					.getBatchLoadByIDs(ProteinAmount.class, proteinAmountIDs, true, 250);
//			final List<AmountValueWrapper> proteinAmountValues = PreparedCriteria
//					.getProteinAmountValuesWrappersFromCondition(condition);
			for (final ProteinAmount proteinAmountValue : proteinAmounts) {

				final int proteinID = proteinAmountValue.getProtein().getId();
				if (!protein_to_proteinAmountValue.containsKey(proteinID)) {
					final List<ProteinAmount> list = new ArrayList<ProteinAmount>();
					protein_to_proteinAmountValue.put(proteinID, list);
				}

				protein_to_proteinAmountValue.get(proteinID).add(proteinAmountValue);
			}
		}
	}

	public List<ProteinAmount> mapProtein(Protein protein) {

		return protein_to_proteinAmountValue.get(protein.getId());
	}

}
