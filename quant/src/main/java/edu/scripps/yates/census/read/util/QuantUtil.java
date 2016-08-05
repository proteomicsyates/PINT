package edu.scripps.yates.census.read.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.read.CensusOutParser;
import edu.scripps.yates.census.read.model.CensusRatio;
import edu.scripps.yates.census.read.model.IsobaricQuantifiedPSM;
import edu.scripps.yates.census.read.model.IsobaricQuantifiedPeptide;
import edu.scripps.yates.census.read.model.QuantifiedPSMFromCensusOut;
import edu.scripps.yates.census.read.model.QuantifiedPeptide;
import edu.scripps.yates.census.read.model.RatioScore;
import edu.scripps.yates.census.read.model.interfaces.QuantRatio;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPeptideInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedProteinInterface;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.maths.Maths;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;
import edu.scripps.yates.utilities.model.enums.AmountType;
import edu.scripps.yates.utilities.model.enums.CombinationType;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;

public class QuantUtil {
	public static void addToPeptideMap(QuantifiedPSMInterface quantifiedPSM, Map<String, QuantifiedPeptide> map,
			boolean distinguishModifiedPeptides) {
		final String sequenceKey = getSequenceKey(quantifiedPSM, distinguishModifiedPeptides);
		QuantifiedPeptide quantifiedPeptide = null;
		if (map.containsKey(sequenceKey)) {
			quantifiedPeptide = map.get(sequenceKey);
			quantifiedPeptide.addQuantifiedPSM(quantifiedPSM);
		} else {
			quantifiedPeptide = new QuantifiedPeptide(quantifiedPSM, distinguishModifiedPeptides);
			map.put(sequenceKey, quantifiedPeptide);
		}

		quantifiedPSM.setQuantifiedPeptide(quantifiedPeptide);
	}

	public static void addToIsobaricPeptideMap(IsobaricQuantifiedPSM quantifiedPSM,
			Map<String, IsobaricQuantifiedPeptide> map, boolean distinguishModifiedPeptides) {
		final String sequenceKey = getSequenceKey(quantifiedPSM, distinguishModifiedPeptides);
		IsobaricQuantifiedPeptide quantifiedPeptide = null;
		if (map.containsKey(sequenceKey)) {
			quantifiedPeptide = map.get(sequenceKey);
			quantifiedPeptide.addPSM(quantifiedPSM);
		} else {
			quantifiedPeptide = new IsobaricQuantifiedPeptide(quantifiedPSM, distinguishModifiedPeptides);
			map.put(sequenceKey, quantifiedPeptide);
		}

		quantifiedPSM.setQuantifiedPeptide(quantifiedPeptide);
	}

	/**
	 * Create a Map of {@link IsobaricQuantifiedPeptide} from a collection of
	 * {@link QuantifiedPSMInterface}s. The resulting peptides will be added
	 * also automatically to all the {@link QuantifiedProteinInterface}s of the
	 * {@link QuantifiedPSMInterface}s, removing fist the
	 * {@link IsobaricQuantifiedPeptide}s linked to any
	 * {@link QuantifiedPSMInterface} and {@link QuantifiedProteinInterface}
	 *
	 * @param quantifiedPSMs
	 * @param distringuishModifiedPeptides
	 * @return
	 */
	public static Map<String, IsobaricQuantifiedPeptide> getIsobaricQuantifiedPeptides(
			Collection<IsobaricQuantifiedPSM> quantifiedPSMs, boolean distringuishModifiedPeptides) {
		Map<String, IsobaricQuantifiedPeptide> peptideMap = new HashMap<String, IsobaricQuantifiedPeptide>();

		// remove any peptide in the psms and proteins before create them
		for (IsobaricQuantifiedPSM quantifiedPSM : quantifiedPSMs) {
			quantifiedPSM.setQuantifiedPeptide(null);
			for (QuantifiedProteinInterface protein : quantifiedPSM.getQuantifiedProteins()) {
				protein.getQuantifiedPeptides().clear();
			}
		}

		for (IsobaricQuantifiedPSM quantifiedPSM : quantifiedPSMs) {
			QuantUtil.addToIsobaricPeptideMap(quantifiedPSM, peptideMap, distringuishModifiedPeptides);
			final String sequenceKey = getSequenceKey(quantifiedPSM, distringuishModifiedPeptides);
			final IsobaricQuantifiedPeptide createdPeptide = peptideMap.get(sequenceKey);

			// add it to the proteins of the psm
			final Set<QuantifiedProteinInterface> quantifiedProteins = quantifiedPSM.getQuantifiedProteins();
			for (QuantifiedProteinInterface protein : quantifiedProteins) {
				protein.addPeptide(createdPeptide);
			}
		}

		return peptideMap;
	}

	public static String getSequenceKey(QuantifiedPSMInterface quantPSM, boolean distinguishModifiedSequence) {
		if (distinguishModifiedSequence) {
			return quantPSM.getSequence();
		} else {
			return FastaParser.cleanSequence(quantPSM.getSequence());
		}
	}

	/**
	 * Create a Map of {@link IsobaricQuantifiedPeptide} getting the peptides
	 * from the {@link QuantifiedPSMInterface}
	 *
	 * @param quantifiedPSMs
	 * @param distringuishModifiedPeptides
	 * @return
	 */
	public static Map<String, IsobaricQuantifiedPeptide> getIsobaricQuantifiedPeptides(
			Collection<IsobaricQuantifiedPSM> quantifiedPSMs) {
		Map<String, IsobaricQuantifiedPeptide> peptideMap = new HashMap<String, IsobaricQuantifiedPeptide>();

		for (IsobaricQuantifiedPSM quantifiedPSM : quantifiedPSMs) {
			final IsobaricQuantifiedPeptide quantifiedPeptide = quantifiedPSM.getQuantifiedPeptide();
			if (!peptideMap.containsKey(quantifiedPeptide.getKey())) {
				peptideMap.put(quantifiedPeptide.getKey(), quantifiedPeptide);
			}
		}

		return peptideMap;
	}

	public static Set<QuantRatio> getNonInfinityRatios(Set<QuantRatio> ratios) {
		Set<QuantRatio> set = new HashSet<QuantRatio>();
		for (QuantRatio ratio : ratios) {
			final double log2Ratio = ratio.getLog2Ratio(ratio.getQuantCondition1(), ratio.getQuantCondition2());
			if (Double.compare(log2Ratio, Double.MAX_VALUE) == 0 || Double.compare(log2Ratio, -Double.MAX_VALUE) == 0) {
				continue;
			}
			if (!Double.isInfinite(log2Ratio) && !Double.isNaN(log2Ratio)) {
				set.add(ratio);
			}
		}
		return set;
	}

	/**
	 * Creates a {@link QuantRatio} being the average of the provided ratios
	 *
	 * @param nonInfinityRatios
	 * @return
	 */
	public static QuantRatio getAverageRatio(Set<QuantRatio> nonInfinityRatios, AggregationLevel aggregationLevel) {
		QuantCondition cond1 = null;
		QuantCondition cond2 = null;
		List<Double> ratioValues = new ArrayList<Double>();
		for (QuantRatio quantRatio : nonInfinityRatios) {
			if (cond1 == null) {
				cond1 = quantRatio.getQuantCondition1();
			} else {
				if (!cond1.equals(quantRatio.getQuantCondition1())) {
					continue;
				}
			}
			if (cond2 == null) {
				cond2 = quantRatio.getQuantCondition2();
			} else {
				if (!cond2.equals(quantRatio.getQuantCondition2())) {
					continue;
				}
			}
			final Double log2Ratio = quantRatio.getLog2Ratio(cond1, cond2);
			if (log2Ratio != null && !Double.isInfinite(log2Ratio) && !Double.isNaN(log2Ratio)) {
				ratioValues.add(log2Ratio);
			}
		}
		if (ratioValues.isEmpty()) {
			return null;
		}
		final Double[] ratioValuesArray = ratioValues.toArray(new Double[0]);
		final double mean = Maths.mean(ratioValuesArray);
		final double stdev = Maths.stddev(ratioValuesArray);
		CensusRatio ret = new CensusRatio(mean, true, cond1, cond2, aggregationLevel, "Average of ratios");
		ret.setCombinationType(CombinationType.AVERAGE);
		RatioScore ratioScore = new RatioScore(String.valueOf(stdev), "STDEV", "Standard deviation of log2 ratios",
				"Standard deviation of multiple log2 ratios");
		ret.setRatioScore(ratioScore);
		return ret;
	}

	/**
	 * Gets a {@link Set} of {@link QuantRatio} as consensus ratios of the
	 * {@link Collection}of {@link QuantifiedPeptideInterface}, calling to
	 * getConsensusRatio() in each {@link QuantifiedPeptideInterface}
	 *
	 * @param peptides
	 * @param cond1
	 * @param cond2
	 * @param replicateName
	 *            if not null, get only the consensus {@link QuantRatio} for
	 *            that replicate
	 * @return
	 */
	public static Set<QuantRatio> getConsensusRatios(Collection<QuantifiedPeptideInterface> peptides,
			QuantCondition cond1, QuantCondition cond2, String replicateName) {
		Set<QuantRatio> ret = new HashSet<QuantRatio>();
		for (QuantifiedPeptideInterface peptide : peptides) {
			if (replicateName != null) {
				final QuantRatio consensusRatio = peptide.getConsensusRatio(cond1, cond2, replicateName);
				if (consensusRatio != null) {
					ret.add(consensusRatio);
				}
			} else {
				final QuantRatio consensusRatio = peptide.getConsensusRatio(cond1, cond2);
				if (consensusRatio != null) {
					ret.add(consensusRatio);
				}
			}
		}
		return ret;
	}

	public static Double getMaxAmountValueByAmountType(Set<Amount> amounts, AmountType amountType) {
		double max = -Double.MAX_VALUE;
		if (amounts != null) {
			for (Amount quantAmount : amounts) {
				if (quantAmount.getAmountType() == amountType) {
					if (quantAmount.getValue() > max) {
						max = quantAmount.getValue();
					}
				}
			}
		}
		if (max != -Double.MAX_VALUE) {
			return max;
		}
		return null;
	}

	/**
	 * For a {@link QuantifiedPSMFromCensusOut}, depending on if it is singleton
	 * or not, we get a different ratio.<br>
	 * For singletons, we get the AREA_RATIO and for non singletons, we get the
	 * RATIO
	 *
	 * @param quantifiedPSM
	 * @return
	 */
	public static QuantRatio getValidRatio(QuantifiedPSMFromCensusOut quantifiedPSM) {

		// RATIO for non singletons and AREA_RATIO for singletons
		if (quantifiedPSM.isSingleton()) {
			return getRatioByName(quantifiedPSM, CensusOutParser.AREA_RATIO);
		} else {
			return getRatioByName(quantifiedPSM, CensusOutParser.RATIO);
		}
	}

	private static QuantRatio getRatioByName(QuantifiedPSMFromCensusOut quantifiedPSM, String ratioDescription) {
		if (quantifiedPSM != null && quantifiedPSM.getRatios() != null) {
			for (QuantRatio ratio : quantifiedPSM.getRatios()) {
				if (ratio.getDescription().equals(ratioDescription)) {
					return ratio;
				}
			}
		}
		return null;
	}
}
