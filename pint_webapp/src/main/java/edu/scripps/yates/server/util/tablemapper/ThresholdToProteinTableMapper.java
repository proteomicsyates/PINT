package edu.scripps.yates.server.util.tablemapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Threshold;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.wrappers.ProteinThresholdWrapper;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.THashSet;

public class ThresholdToProteinTableMapper {
	private final TIntObjectHashMap<TIntArrayList> threshold_to_Protein = new TIntObjectHashMap<TIntArrayList>();
	private final TIntObjectHashMap<List<ProteinThresholdWrapper>> protein_to_threshold = new TIntObjectHashMap<List<ProteinThresholdWrapper>>();
	private final TIntObjectHashMap<Threshold> thresholdMap = new TIntObjectHashMap<Threshold>();
	private static ThresholdToProteinTableMapper instance;

	private ThresholdToProteinTableMapper() {

	}

	public static ThresholdToProteinTableMapper getInstance() {
		if (instance == null) {
			instance = new ThresholdToProteinTableMapper();
			final List<Threshold> list = ContextualSessionHandler.getCurrentSession().createCriteria(Threshold.class)
					.list();
			for (final Threshold threshold : list) {
				instance.addThreshold(threshold);
			}

		}
		return instance;
	}

	public Threshold getThreshold(ProteinThresholdWrapper wrapper) {
		return thresholdMap.get(wrapper.getThresholdID());
	}

	public void addThreshold(Threshold threshold) {

		final int thresholdID = threshold.getId();
		thresholdMap.put(thresholdID, threshold);
		if (!threshold_to_Protein.containsKey(thresholdID)) {
			final List<ProteinThresholdWrapper> proteinThresholds = PreparedCriteria
					.getProteinThresholdsFromProteinThreshold(threshold);
			final Set<Integer> proteinIDs = new THashSet<Integer>();
			for (final ProteinThresholdWrapper proteinThresholdWrapper : proteinThresholds) {

				final int proteinID = proteinThresholdWrapper.getProteinID();
				proteinIDs.add(proteinID);
				if (!protein_to_threshold.containsKey(proteinID)) {
					final List<ProteinThresholdWrapper> list = new ArrayList<ProteinThresholdWrapper>();
					protein_to_threshold.put(proteinID, list);
				}

				protein_to_threshold.get(proteinID).add(proteinThresholdWrapper);
			}
			threshold_to_Protein.put(thresholdID, transformToTIntArray(proteinIDs));

		}
	}

	public List<ProteinThresholdWrapper> mapProtein(Protein protein) {

		return protein_to_threshold.get(protein.getId());
	}

	public TIntArrayList transformToTIntArray(Collection<Integer> collection) {
		final TIntArrayList ret = new TIntArrayList();
		for (final Integer num : collection) {
			ret.add(num);
		}
		return ret;
	}
}
