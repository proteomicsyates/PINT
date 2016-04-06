package edu.scripps.yates.census.read.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.read.model.interfaces.HasIsoRatios;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.census.read.util.QuantificationLabel;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.grouping.ProteinGroup;
import edu.scripps.yates.utilities.maths.Maths;

public class IsobaricQuantifiedProteinGroup extends QuantifiedProteinGroup implements HasIsoRatios {
	private static final String SEPARATOR = " ## ";
	private final Set<IsobaricQuantifiedProtein> proteins = new HashSet<IsobaricQuantifiedProtein>();
	private Set<IsoRatio> ratios;
	private final Map<String, Double> countRatiosByConditionKey = new HashMap<String, Double>();
	private Map<QuantCondition, Set<Ion>> ionsByConditions;

	public IsobaricQuantifiedProteinGroup(ProteinGroup proteinGroup) {
		super(proteinGroup);
	}

	@Override
	public int size() {
		return getProteins().size();
	}

	public Set<IsobaricQuantifiedPSM> getIsobaricQuantifiedPSMs() {
		Set<IsobaricQuantifiedPSM> ret = new HashSet<IsobaricQuantifiedPSM>();
		for (IsobaricQuantifiedProtein quantifiedProtein : getProteins()) {
			final Set<QuantifiedPSMInterface> quantifiedPSMs = quantifiedProtein.getQuantifiedPSMs();
			for (QuantifiedPSMInterface quantifiedPSMInterface : quantifiedPSMs) {
				if (quantifiedPSMInterface instanceof IsobaricQuantifiedPSM) {
					ret.add((IsobaricQuantifiedPSM) quantifiedPSMInterface);
				}
			}
		}
		return ret;
	}

	@Override
	public String getAccessionString() {
		StringBuilder sb = new StringBuilder();
		for (IsobaricQuantifiedProtein quantifiedProtein : getProteins()) {
			if (!"".equals(sb.toString()))
				sb.append(SEPARATOR);
			sb.append(quantifiedProtein.getAccession());
		}
		return sb.toString();
	}

	@Override
	public String getDescriptionString() {
		StringBuilder sb = new StringBuilder();
		for (IsobaricQuantifiedProtein quantifiedProtein : getProteins()) {
			if (!"".equals(sb.toString()))
				sb.append(SEPARATOR);
			sb.append(quantifiedProtein.getDescription());
		}
		return sb.toString();
	}

	@Override
	public List<String> getTaxonomies() {
		List<String> ret = new ArrayList<String>();
		for (IsobaricQuantifiedProtein quantifiedProtein : getProteins()) {
			ret.add(quantifiedProtein.getTaxonomy());
		}
		return ret;
	}

	/**
	 * NOTE THAT THIS RETURNED LIST IS NOT VALID FOR ADDING NEW PROTEINS TO THE
	 * GROUP
	 *
	 * @return the proteins
	 */
	@Override
	public List<IsobaricQuantifiedProtein> getProteins() {
		List<IsobaricQuantifiedProtein> ret = new ArrayList<IsobaricQuantifiedProtein>();
		ret.addAll(proteins);
		Collections.sort(ret, new Comparator<IsobaricQuantifiedProtein>() {

			@Override
			public int compare(IsobaricQuantifiedProtein o1, IsobaricQuantifiedProtein o2) {
				final String accession1 = o1.getAccession();
				final String accession2 = o2.getAccession();
				return accession1.compareTo(accession2);
			}
		});
		return ret;
	}

	@Override
	public String getGeneNameString() {
		StringBuilder sb = new StringBuilder();
		for (IsobaricQuantifiedProtein quantifiedProtein : getProteins()) {
			if (!"".equals(sb.toString()))
				sb.append(SEPARATOR);
			String geneFromFastaHeader = FastaParser.getGeneFromFastaHeader(quantifiedProtein.getAccession());
			if (geneFromFastaHeader == null) {
				geneFromFastaHeader = FastaParser.getGeneFromFastaHeader(quantifiedProtein.getDescription());
			}
			sb.append(geneFromFastaHeader);
		}
		return sb.toString();
	}

	@Override
	public Set<String> getFileNames() {
		Set<String> ret = new HashSet<String>();
		for (IsobaricQuantifiedProtein quantprotein : proteins) {
			ret.addAll(quantprotein.getFileNames());
		}
		return ret;
	}

	@Override
	public Set<IsoRatio> getIsoRatios() {
		if (ratios == null || ratios.isEmpty()) {
			ratios = new HashSet<IsoRatio>();
			for (IsobaricQuantifiedPSM psm : getIsobaricQuantifiedPSMs()) {
				ratios.addAll(psm.getIsoRatios());
			}
		}
		return ratios;
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
	public double getMaxPeak() {
		double max = Double.MIN_VALUE;
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
