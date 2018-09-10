package edu.scripps.yates.server.util.tablemapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.RatioDescriptor;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.wrappers.RatioValueWrapper;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

public class ProteinRatioToProteinTableMapper {
	private final static Logger log = Logger.getLogger(ProteinRatioToProteinTableMapper.class);
	private final TIntObjectHashMap<List<RatioValueWrapper>> protein_to_proteinRatioValue = new TIntObjectHashMap<List<RatioValueWrapper>>();
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
			final List<RatioDescriptor> ratioDescriptors = PreparedCriteria.getRatioDescriptorsFromCondition(condition);
			// get the proteinratiovaluewrappers that point to the
			// ratiodescriptors
			for (final RatioDescriptor ratioDescriptor : ratioDescriptors) {
				if (!ratioDescriptorMap.containsKey(ratioDescriptor.getId())) {
					ratioDescriptorMap.put(ratioDescriptor.getId(), ratioDescriptor);
					final List<RatioValueWrapper> proteinRatioValues = PreparedCriteria
							.getProteinRatioValuesWrappersFromRatioDescriptor(ratioDescriptor);
					final Set<Integer> proteinIDs = new THashSet<Integer>();
					for (final RatioValueWrapper proteinRatioValue : proteinRatioValues) {

						final int proteinID = proteinRatioValue.getProteinPeptideOrPSMID();
						if (!protein_to_proteinRatioValue.containsKey(proteinID)) {
							final List<RatioValueWrapper> list = new ArrayList<RatioValueWrapper>();
							protein_to_proteinRatioValue.put(proteinID, list);
						}

						protein_to_proteinRatioValue.get(proteinID).add(proteinRatioValue);
					}
				}
			}
		}
	}

	public List<RatioValueWrapper> mapProtein(Protein protein) {

		return protein_to_proteinRatioValue.get(protein.getId());
	}

	public RatioDescriptor getRatioDescriptor(int ratioDescriptorID) {
		return ratioDescriptorMap.get(ratioDescriptorID);
	}
}
