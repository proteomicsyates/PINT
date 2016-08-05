package edu.scripps.yates.census.read.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.read.model.interfaces.HasIsoRatios;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPeptideInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedProteinInterface;
import edu.scripps.yates.census.read.util.QuantUtil;
import edu.scripps.yates.census.read.util.QuantificationLabel;
import edu.scripps.yates.utilities.maths.Maths;

public class IsobaricQuantifiedPeptide extends QuantifiedPeptide implements QuantifiedPeptideInterface, HasIsoRatios {
	private final Map<String, Double> countRatiosByConditionKey = new HashMap<String, Double>();
	private Map<QuantCondition, Set<Ion>> ionsByConditions;

	/**
	 * Creates a {@link IsobaricQuantifiedPeptide} object, adding the
	 * {@link QuantifiedPSMInterface} to its list of
	 * {@link QuantifiedPSMInterface}s
	 *
	 * @param quantPSM
	 * @param distinguishModifiedSequences
	 */
	public IsobaricQuantifiedPeptide(IsobaricQuantifiedPSM quantPSM, boolean distinguishModifiedSequences) {
		super(quantPSM, distinguishModifiedSequences);
	}

	/**
	 * It assures that has the same sequence, taking into account the
	 * distinguishModifiedSequences of the instance
	 */

	public boolean addPSM(IsobaricQuantifiedPSM quantPSM) {
		if (sequenceKey.equals(QuantUtil.getSequenceKey(quantPSM, distinguishModifiedSequences))) {
			return psms.add(quantPSM);
		}
		return false;
	}

	public Set<IsobaricQuantifiedPSM> getIsobaricQuantifiedPSMs() {
		Set<IsobaricQuantifiedPSM> isoPsms = new HashSet<IsobaricQuantifiedPSM>();
		for (QuantifiedPSMInterface quantifiedPSM : psms) {
			if (quantifiedPSM instanceof IsobaricQuantifiedPSM) {
				isoPsms.add((IsobaricQuantifiedPSM) quantifiedPSM);
			}
		}
		return isoPsms;
	}

	@Override
	public String getSequence() {
		return sequenceKey;
	}

	/**
	 *
	 * @return NOte that the returned set is created everytime this method is
	 *         called, because proteins are taken from the psms of the peptide
	 */
	@Override
	public Set<QuantifiedProteinInterface> getQuantifiedProteins() {
		Set<QuantifiedProteinInterface> set = new HashSet<QuantifiedProteinInterface>();
		for (QuantifiedPSMInterface psm : psms) {
			for (QuantifiedProteinInterface quantProtein : psm.getQuantifiedProteins()) {
				set.add(quantProtein);
			}
		}
		return set;
	}

	@Override
	public Float getCalcMHplus() {
		if (!psms.isEmpty())
			return psms.iterator().next().getCalcMHplus();
		return null;
	}

	/**
	 * @return the fileNames
	 */
	@Override
	public Set<String> getRawFileNames() {
		Set<String> ret = new HashSet<String>();
		for (QuantifiedPSMInterface quantPSM : psms) {
			ret.add(quantPSM.getRawFileName());
		}
		return ret;
	}

	@Override
	public String toString() {
		return getSequence();
	}

	@Override
	public String getFullSequence() {
		return getSequence();
	}

	@Override
	public Set<String> getTaxonomies() {
		Set<String> ret = new HashSet<String>();
		for (QuantifiedPSMInterface quantifiedPSM : psms) {
			ret.addAll(quantifiedPSM.getTaxonomies());
		}
		return ret;
	}

	@Override
	public Float getMHplus() {
		if (!psms.isEmpty()) {
			return psms.iterator().next().getMHplus();
		}
		return null;
	}

	@Override
	public Set<IsoRatio> getIsoRatios() {
		Set<IsoRatio> ret = new HashSet<IsoRatio>();

		final Set<IsobaricQuantifiedPSM> quantifiedPSMs = getIsobaricQuantifiedPSMs();
		for (IsobaricQuantifiedPSM quantifiedPSM : quantifiedPSMs) {
			ret.addAll(quantifiedPSM.getIsoRatios());
		}

		return ret;
	}

	/**
	 * Returns true if the protein contains any {@link Ion} labeled with a
	 * certain {@link QuantificationLabel} not paired with any other label
	 *
	 * @param label
	 * @return
	 */
	@Override
	public boolean containsAnySingletonIon(QuantificationLabel label) {
		for (IsobaricQuantifiedPSM quantifiedPSM : getIsobaricQuantifiedPSMs()) {
			if (quantifiedPSM.containsAnySingletonIon(label))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if the protein contains any {@link Ion} labeled with a
	 * certain {@link QuantificationLabel} not matter if they are paired with
	 * any other label or not (getting ratios or not)
	 *
	 * @param label
	 * @return
	 */
	@Override
	public boolean containsAnyIon(QuantificationLabel label) {
		for (IsobaricQuantifiedPSM quantifiedPeptide : getIsobaricQuantifiedPSMs()) {
			if (quantifiedPeptide.containsAnyIon(label))
				return true;
		}
		return false;
	}

	@Override
	public Map<QuantificationLabel, Set<Ion>> getIons(IonSerie ionSerie) {
		Map<QuantificationLabel, Set<Ion>> ret = new HashMap<QuantificationLabel, Set<Ion>>();
		for (IsobaricQuantifiedPSM psm : getIsobaricQuantifiedPSMs()) {
			final Map<QuantificationLabel, Set<Ion>> ions = psm.getIons(ionSerie);
			mergeMaps(ret, ions);

		}
		return ret;
	}

	@Override
	public Set<Ion> getSingletonIonsByLabel(QuantificationLabel label) {
		Set<Ion> ret = new HashSet<Ion>();
		for (IsobaricQuantifiedPSM psm : getIsobaricQuantifiedPSMs()) {
			ret.addAll(psm.getSingletonIonsByLabel(label));
		}
		return ret;
	}

	@Override
	public Set<Ion> getIonsByLabel(QuantificationLabel label) {
		Set<Ion> ret = new HashSet<Ion>();
		for (IsobaricQuantifiedPSM psm : getIsobaricQuantifiedPSMs()) {
			ret.addAll(psm.getIonsByLabel(label));
		}
		return ret;
	}

	@Override
	public Map<QuantificationLabel, Set<Ion>> getSingletonIons(IonSerie ionSerie) {
		Map<QuantificationLabel, Set<Ion>> ret = new HashMap<QuantificationLabel, Set<Ion>>();
		for (IsobaricQuantifiedPSM psm : getIsobaricQuantifiedPSMs()) {
			final Map<QuantificationLabel, Set<Ion>> singletonIons = psm.getSingletonIons(ionSerie);
			mergeMaps(ret, singletonIons);

		}
		return ret;
	}

	private void mergeMaps(Map<QuantificationLabel, Set<Ion>> receiverMap,
			Map<QuantificationLabel, Set<Ion>> donorMap) {
		for (QuantificationLabel label : donorMap.keySet()) {
			if (receiverMap.containsKey(label)) {
				receiverMap.get(label).addAll(donorMap.get(label));
			} else {
				Set<Ion> set = new HashSet<Ion>();
				set.addAll(donorMap.get(label));
				receiverMap.put(label, set);
			}
		}

	}

	@Override
	public Map<QuantCondition, Set<Ion>> getSingletonIonsByCondition() {
		Map<QuantCondition, Set<Ion>> ret = new HashMap<QuantCondition, Set<Ion>>();
		for (IsobaricQuantifiedPSM psm : getIsobaricQuantifiedPSMs()) {
			final Map<QuantCondition, Set<Ion>> singletonIons = psm.getSingletonIonsByCondition();
			for (QuantCondition condition : singletonIons.keySet()) {
				if (ret.containsKey(condition)) {
					ret.get(condition).addAll(singletonIons.get(condition));
				} else {
					Set<Ion> set = new HashSet<Ion>();
					set.addAll(singletonIons.get(condition));
					ret.put(condition, set);
				}
			}
		}
		return ret;
	}

	@Override
	public Set<IsoRatio> getNonInfinityIsoRatios() {
		Set<IsoRatio> ret = new HashSet<IsoRatio>();
		for (IsobaricQuantifiedPSM psm : getIsobaricQuantifiedPSMs()) {
			ret.addAll(psm.getNonInfinityIsoRatios());
		}
		return ret;
	}

	@Override
	public Double getMaxPeak() {
		double max = -Double.MAX_VALUE;
		for (IsobaricQuantifiedPSM psm : getIsobaricQuantifiedPSMs()) {
			if (max < psm.getMaxPeak()) {
				max = psm.getMaxPeak();
			}
		}
		return max;
	}

	@Override
	public Map<QuantificationLabel, Set<Ion>> getSingletonIonsByLabel() {
		Map<QuantificationLabel, Set<Ion>> ret = new HashMap<QuantificationLabel, Set<Ion>>();
		for (IsobaricQuantifiedPSM psm : getIsobaricQuantifiedPSMs()) {
			final Map<QuantificationLabel, Set<Ion>> singletonIons = psm.getSingletonIonsByLabel();
			for (QuantificationLabel label : singletonIons.keySet()) {
				if (ret.containsKey(label)) {
					ret.get(label).addAll(singletonIons.get(label));
				} else {
					Set<Ion> set = new HashSet<Ion>();
					set.addAll(singletonIons.get(label));
					ret.put(label, set);
				}
			}
		}
		return ret;
	}

	@Override
	public Map<QuantificationLabel, Set<Ion>> getIonsByLabel() {
		Map<QuantificationLabel, Set<Ion>> ret = new HashMap<QuantificationLabel, Set<Ion>>();
		for (IsobaricQuantifiedPSM psm : getIsobaricQuantifiedPSMs()) {
			final Map<QuantificationLabel, Set<Ion>> ions = psm.getIonsByLabel();
			for (QuantificationLabel label : ions.keySet()) {
				if (ret.containsKey(label)) {
					ret.get(label).addAll(ions.get(label));
				} else {
					Set<Ion> set = new HashSet<Ion>();
					set.addAll(ions.get(label));
					ret.put(label, set);
				}
			}
		}
		return ret;
	}

	@Override
	public double getMeanRatios(QuantCondition quantConditionNumerator, QuantCondition quantConditionDenominator) {
		List<Double> ratioValues = new ArrayList<Double>();

		for (IsoRatio ratio : getNonInfinityIsoRatios()) {
			ratioValues.add(ratio.getLog2Ratio(quantConditionNumerator, quantConditionDenominator));
		}

		return Maths.mean(ratioValues.toArray(new Double[0]));
	}

	@Override
	public double getSTDRatios(QuantCondition quantConditionNumerator, QuantCondition quantConditionDenominator) {
		List<Double> ratioValues = new ArrayList<Double>();

		for (IsoRatio ratio : getNonInfinityIsoRatios()) {
			ratioValues.add(ratio.getLog2Ratio(quantConditionNumerator, quantConditionDenominator));
		}

		return Maths.stddev(ratioValues.toArray(new Double[0]));
	}

	/**
	 *
	 * @param cond1
	 * @param cond2
	 * @return
	 */
	@Override
	public double getCountRatio(QuantCondition cond1, QuantCondition cond2) {
		String conditionKey = cond1.getName() + cond2.getName();
		if (countRatiosByConditionKey.containsKey(conditionKey)) {
			return countRatiosByConditionKey.get(conditionKey);
		} else {
			Set<Ion> ions1 = getIonsByCondition().get(cond1);
			int numIons1 = 0;
			if (ions1 != null) {
				numIons1 = ions1.size();
			}
			Set<Ion> ions2 = getIonsByCondition().get(cond2);
			int numIons2 = 0;
			if (ions2 != null) {
				numIons2 = ions2.size();
			}
			if (numIons1 == 0 && numIons2 != 0) {
				return Double.NEGATIVE_INFINITY;
			}
			if (numIons1 != 0 && numIons2 == 0) {
				return Double.POSITIVE_INFINITY;
			}
			if (numIons1 == 0 && numIons2 == 0) {
				return Double.NaN;
			}
			final double ratio = Math.log(1.0 * numIons1 / numIons2) / Math.log(2);
			countRatiosByConditionKey.put(conditionKey, ratio);
			return ratio;
		}
	}

	@Override
	public Map<QuantCondition, Set<Ion>> getIonsByCondition() {
		if (ionsByConditions == null) {
			ionsByConditions = new HashMap<QuantCondition, Set<Ion>>();
			for (IsobaricQuantifiedPSM quantPSM : getIsobaricQuantifiedPSMs()) {
				final Map<QuantCondition, Set<Ion>> ions = quantPSM.getIonsByCondition();
				for (QuantCondition condition : ions.keySet()) {
					final Set<Ion> c = ions.get(condition);
					if (ionsByConditions.containsKey(condition)) {
						ionsByConditions.get(condition).addAll(c);
					} else {
						ionsByConditions.put(condition, c);
					}
				}
			}
		}
		return ionsByConditions;
	}

}
