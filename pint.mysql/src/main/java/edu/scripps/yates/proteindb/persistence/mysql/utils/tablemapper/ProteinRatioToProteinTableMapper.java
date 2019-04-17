package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.RatioDescriptor;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.ConditionIDToRatioDescriptorIDTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.RatioDescriptorIDToProteinRatioValueIDTableMapper;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

public class ProteinRatioToProteinTableMapper {
	private final static Logger log = Logger.getLogger(ProteinRatioToProteinTableMapper.class);
	private final TIntObjectHashMap<List<ProteinRatioValue>> protein_to_proteinRatioValue = new TIntObjectHashMap<List<ProteinRatioValue>>();
	private static ProteinRatioToProteinTableMapper instance;
	private final TIntObjectHashMap<RatioDescriptor> ratioDescriptorMap = new TIntObjectHashMap<RatioDescriptor>();
	private final TIntHashSet conditionIDs = new TIntHashSet();

	private ProteinRatioToProteinTableMapper() {

	}

	public static ProteinRatioToProteinTableMapper getInstance() {
		if (instance == null) {
			instance = new ProteinRatioToProteinTableMapper();
		}
		return instance;
	}

	public void addCondition(Condition condition) {
		if (!conditionIDs.contains(condition.getId())) {
			log.info("Caching protein ratios of condition " + condition.getName());
			conditionIDs.add(condition.getId());
			// get the ratio descriptors that imply the condition
			final TIntSet ratioDescriptorIDs = ConditionIDToRatioDescriptorIDTableMapper.getInstance()
					.getRatioDescriptorIDsFromConditionID(condition.getId());

			final List<RatioDescriptor> ratioDescriptors = PreparedCriteria
					.getRatioDescriptorsFromRatioDescriptorIDs(ratioDescriptorIDs, true, 100);
			// get the proteinratiovaluewrappers that point to the
			// ratiodescriptors
			for (final RatioDescriptor ratioDescriptor : ratioDescriptors) {
				if (!ratioDescriptorMap.containsKey(ratioDescriptor.getId())) {
					ratioDescriptorMap.put(ratioDescriptor.getId(), ratioDescriptor);
					final TIntSet proteinRatioValueIDs = RatioDescriptorIDToProteinRatioValueIDTableMapper.getInstance()
							.getProteinRatioValueIDsFromRatioDescriptorID(ratioDescriptor.getId());
					final List<ProteinRatioValue> proteinRatioValues = (List<ProteinRatioValue>) PreparedCriteria
							.getBatchLoadByIDs(ProteinRatioValue.class, proteinRatioValueIDs, true, 100);
// 							.getProteinRatioValuesWrappersFromRatioDescriptor(ratioDescriptor);
					final Set<Integer> proteinIDs = new THashSet<Integer>();
					for (final ProteinRatioValue proteinRatioValue : proteinRatioValues) {
						// TODO chech if this makes a query:
						final int proteinID = proteinRatioValue.getProtein().getId();
						if (!protein_to_proteinRatioValue.containsKey(proteinID)) {
							final List<ProteinRatioValue> list = new ArrayList<ProteinRatioValue>();
							protein_to_proteinRatioValue.put(proteinID, list);
						}

						protein_to_proteinRatioValue.get(proteinID).add(proteinRatioValue);
					}
				}
			}
		}
	}

	public List<ProteinRatioValue> mapProtein(Protein protein) {

		return protein_to_proteinRatioValue.get(protein.getId());
	}

	public RatioDescriptor getRatioDescriptor(int ratioDescriptorID) {
		return ratioDescriptorMap.get(ratioDescriptorID);
	}
}
