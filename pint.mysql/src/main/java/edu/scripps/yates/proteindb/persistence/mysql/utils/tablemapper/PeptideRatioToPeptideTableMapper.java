package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.RatioDescriptor;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.wrappers.RatioValueWrapper;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.TIntHashSet;

public class PeptideRatioToPeptideTableMapper {
	private final static Logger log = Logger.getLogger(PeptideRatioToPeptideTableMapper.class);
	private final TIntObjectHashMap<List<RatioValueWrapper>> peptide_to_peptideRatioValue = new TIntObjectHashMap<List<RatioValueWrapper>>();
	private static PeptideRatioToPeptideTableMapper instance;
	private final TIntObjectHashMap<RatioDescriptor> ratioDescriptorMap = new TIntObjectHashMap<RatioDescriptor>();
	private final TIntHashSet conditionIDs = new TIntHashSet();

	private PeptideRatioToPeptideTableMapper() {

	}

	public static PeptideRatioToPeptideTableMapper getInstance() {
		if (instance == null) {
			instance = new PeptideRatioToPeptideTableMapper();
		}
		return instance;
	}

	public void addCondition(Condition condition) {
		if (!conditionIDs.contains(condition.getId())) {
			log.info("Caching peptide ratios of condition " + condition.getName());
			conditionIDs.add(condition.getId());
			// get the ratio descriptors that imply the condition
			final List<RatioDescriptor> ratioDescriptors = PreparedCriteria.getRatioDescriptorsFromCondition(condition);
			// get the proteinratiovaluewrappers that point to the
			// ratiodescriptors
			for (final RatioDescriptor ratioDescriptor : ratioDescriptors) {
				if (!ratioDescriptorMap.containsKey(ratioDescriptor.getId())) {
					ratioDescriptorMap.put(ratioDescriptor.getId(), ratioDescriptor);
					final List<RatioValueWrapper> peptideRatioValues = PreparedCriteria
							.getPeptideRatioValuesWrappersFromRatioDescriptor(ratioDescriptor);
					for (final RatioValueWrapper peptideRatioValue : peptideRatioValues) {

						final int peptideID = peptideRatioValue.getProteinPeptideOrPSMID();
						if (!peptide_to_peptideRatioValue.containsKey(peptideID)) {
							final List<RatioValueWrapper> list = new ArrayList<RatioValueWrapper>();
							peptide_to_peptideRatioValue.put(peptideID, list);
						}

						peptide_to_peptideRatioValue.get(peptideID).add(peptideRatioValue);
					}
				}
			}
		}
	}

	public List<RatioValueWrapper> mapPeptide(Peptide peptide) {

		return peptide_to_peptideRatioValue.get(peptide.getId());
	}

	public RatioDescriptor getRatioDescriptor(int ratioDescriptorID) {
		return ratioDescriptorMap.get(ratioDescriptorID);
	}
}
