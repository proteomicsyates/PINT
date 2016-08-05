package edu.scripps.yates.census.read.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.analysis.util.KeyUtils;
import edu.scripps.yates.census.read.model.interfaces.HasRatios;
import edu.scripps.yates.census.read.model.interfaces.PeptideSequenceInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantRatio;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPeptideInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedProteinInterface;
import edu.scripps.yates.census.read.util.QuantUtil;
import edu.scripps.yates.census.read.util.QuantificationLabel;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.grouping.GroupablePSM;
import edu.scripps.yates.utilities.grouping.GroupableProtein;
import edu.scripps.yates.utilities.grouping.PeptideRelation;
import edu.scripps.yates.utilities.maths.Maths;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;

public class QuantifiedPSMFromCensusOut
		implements GroupablePSM, PeptideSequenceInterface, HasRatios, QuantifiedPSMInterface {
	private static final Logger log = Logger.getLogger(QuantifiedPSMFromCensusOut.class);
	private final Set<QuantifiedProteinInterface> quantifiedProteins = new HashSet<QuantifiedProteinInterface>();
	private final HashSet<edu.scripps.yates.census.read.util.QuantificationLabel> labels = new HashSet<QuantificationLabel>();
	private final HashSet<String> taxonomies = new HashSet<String>();

	private PeptideRelation relation;
	private final Set<QuantRatio> ratios = new HashSet<QuantRatio>();
	private final String rawFileName;
	private final String scan;
	private final String sequence;
	private final Map<QuantificationLabel, QuantCondition> conditionsByLabels;
	private QuantifiedPeptideInterface quantifiedPeptide;
	private final String fullSequence;
	private final Integer charge;
	private Float calcMHplus;
	private Float mhPlus;
	private Float deltaCN;
	private Float xcorr;
	private Float deltaMass;
	private final Set<Amount> amounts = new HashSet<Amount>();
	private final Set<String> fileNames = new HashSet<String>();
	private boolean discarded;
	private boolean singleton;

	public QuantifiedPSMFromCensusOut(String sequence, Map<QuantCondition, QuantificationLabel> labelsByConditions,
			HashMap<String, Set<String>> peptideToSpectraMap, int scanNumber, int chargeState,
			boolean chargeStateSensible, String rawFileName, boolean singleton) throws IOException {
		fullSequence = sequence;
		this.sequence = FastaParser.cleanSequence(sequence);
		scan = String.valueOf(scanNumber);
		conditionsByLabels = new HashMap<QuantificationLabel, QuantCondition>();
		for (QuantCondition condition : labelsByConditions.keySet()) {
			final QuantificationLabel quantificationLabel = labelsByConditions.get(condition);
			conditionsByLabels.put(quantificationLabel, condition);
		}
		charge = chargeState;
		// remove the H of HEAVY
		if (rawFileName != null && rawFileName.startsWith("H")) {
			this.rawFileName = rawFileName.substring(1);
		} else {
			this.rawFileName = rawFileName;
		}
		this.singleton = singleton;
		final String peptideKey = KeyUtils.getSequenceChargeKey(this, chargeStateSensible);
		final String spectrumKey = KeyUtils.getSpectrumKey(this, chargeStateSensible);

		addToMap(peptideKey, peptideToSpectraMap, spectrumKey);
	}

	@Override
	public String getKey() {
		return getPSMIdentifier();
	}

	private void addToMap(String key, HashMap<String, Set<String>> map, String value) {
		if (map == null)
			return;
		if (map.containsKey(key)) {
			map.get(key).add(value);
		} else {
			Set<String> set = new HashSet<String>();
			set.add(value);
			map.put(key, set);
		}

	}

	/**
	 * @return the fileName
	 */
	@Override
	public String getRawFileName() {
		return rawFileName;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * edu.scripps.yates.census.quant.xml.RelexChro.Protein.Peptide#getScan()
	 */
	@Override
	public String getScan() {

		return scan;

	}

	@Override
	public void addQuantifiedProtein(QuantifiedProteinInterface quantifiedProtein) {
		if (!quantifiedProteins.contains(quantifiedProtein))
			quantifiedProteins.add(quantifiedProtein);

		// get the taxonomy
		final String taxonomy = quantifiedProtein.getTaxonomy();
		taxonomies.add(taxonomy);
	}

	/**
	 * @return the quantifiedProteins
	 */
	@Override
	public Set<QuantifiedProteinInterface> getQuantifiedProteins() {
		return quantifiedProteins;
	}

	/**
	 * @return the ratios
	 */
	@Override
	public Set<QuantRatio> getRatios() {

		return ratios;
	}

	/**
	 * Gets the labels that this {@link QuantifiedPSMFromCensusOut} has been
	 * labeled ONLY with some label.<br>
	 * So, may happen that contains any ratio and it is not labeled
	 *
	 * @return the labels
	 */
	public HashSet<QuantificationLabel> getLabels() {
		return labels;
	}

	/**
	 * @return the taxonomies
	 */
	@Override
	public HashSet<String> getTaxonomies() {
		return taxonomies;
	}

	@Override
	public String getSequence() {
		return sequence;
	}

	@Override
	public String getPSMIdentifier() {
		return KeyUtils.getSpectrumKey(this, false);
	}

	@Override
	public void setRelation(PeptideRelation relation) {
		this.relation = relation;

	}

	@Override
	public PeptideRelation getRelation() {
		return relation;
	}

	@Override
	public List<GroupableProtein> getGroupableProteins() {
		List<GroupableProtein> ret = new ArrayList<GroupableProtein>();
		ret.addAll(getQuantifiedProteins());
		return ret;
	}

	@Override
	public double getMeanRatios(QuantCondition quantConditionNumerator, QuantCondition quantConditionDenominator) {

		final Set<QuantRatio> ratioSet = getRatios();
		List<Double> ratios = new ArrayList<Double>();

		for (QuantRatio isoRatio : ratioSet) {
			ratios.add(isoRatio.getLog2Ratio(quantConditionNumerator, quantConditionDenominator));
		}
		return Maths.mean(ratios.toArray(new Double[0]));
	}

	@Override
	public double getSTDRatios(QuantCondition quantConditionNumerator, QuantCondition quantConditionDenominator) {

		final Set<QuantRatio> ratioSet = getRatios();
		List<Double> ratios = new ArrayList<Double>();
		for (QuantRatio isoRatio : ratioSet) {
			ratios.add(isoRatio.getLog2Ratio(quantConditionNumerator, quantConditionDenominator));
		}
		return Maths.stddev(ratios.toArray(new Double[0]));
	}

	@Override
	public void setQuantifiedPeptide(QuantifiedPeptideInterface quantifiedPeptide) {
		this.quantifiedPeptide = quantifiedPeptide;
		if (quantifiedPeptide != null) {
			quantifiedPeptide.addQuantifiedPSM(this);
		}
	}

	/**
	 * @return the quantifiedPeptide
	 */
	@Override
	public QuantifiedPeptideInterface getQuantifiedPeptide() {
		return quantifiedPeptide;
	}

	@Override
	public String getFullSequence() {
		return fullSequence;
	}

	@Override
	public Integer getCharge() {
		return charge;
	}

	@Override
	public Float getCalcMHplus() {
		return calcMHplus;
	}

	@Override
	public Float getMHplus() {
		return mhPlus;
	}

	@Override
	public void addRatio(QuantRatio ratio) {
		// dont add it if it is already one ratio with the same value and
		// description
		for (QuantRatio ratio2 : ratios) {
			if (ratio2.getDescription().equals(ratio.getDescription())) {
				if (Double.compare(ratio2.getValue(), ratio.getValue()) == 0) {
					return;
				}
			}
		}
		ratios.add(ratio);
	}

	@Override
	public Float getDeltaCN() {
		return deltaCN;
	}

	@Override
	public Float getXcorr() {
		return xcorr;
	}

	@Override
	public Float getDeltaMass() {
		return deltaMass;
	}

	/**
	 * @param deltaCN
	 *            the deltaCN to set
	 */
	public void setDeltaCN(Float deltaCN) {
		this.deltaCN = deltaCN;
	}

	/**
	 * @param xcorr
	 *            the xcorr to set
	 */
	public void setXcorr(Float xcorr) {
		this.xcorr = xcorr;
	}

	/**
	 * @param deltaMass
	 *            the deltaMass to set
	 */
	public void setDeltaMass(Float deltaMass) {
		this.deltaMass = deltaMass;
	}

	@Override
	public Set<Amount> getAmounts() {
		return amounts;
	}

	@Override
	public void addAmount(Amount amount) {
		amounts.add(amount);

	}

	@Override
	public Double getMaxPeak() {

		double max = -Double.MAX_VALUE;
		for (Amount amount : getAmounts()) {
			if (Double.compare(amount.getValue(), max) > 0) {
				max = amount.getValue();
			}
		}
		if (Double.compare(max, -Double.MAX_VALUE) != 0) {
			return max;
		}
		return null;
	}

	@Override
	public Set<QuantRatio> getNonInfinityRatios() {
		return QuantUtil.getNonInfinityRatios(getRatios());
	}

	@Override
	public void addFileName(String fileName) {
		fileNames.add(fileName);

	}

	@Override
	public Set<String> getFileNames() {
		return fileNames;
	}

	@Override
	public QuantRatio getConsensusRatio(QuantCondition quantConditionNumerator,
			QuantCondition quantConditionDenominator) {
		return QuantUtil.getAverageRatio(QuantUtil.getNonInfinityRatios(getRatios()), AggregationLevel.PSM);
	}

	@Override
	public QuantRatio getConsensusRatio(QuantCondition quantConditionNumerator,
			QuantCondition quantConditionDenominator, String replicateName) {
		if (getRawFileName().equals(replicateName)) {
			return QuantUtil.getAverageRatio(QuantUtil.getNonInfinityRatios(getRatios()), AggregationLevel.PSM);
		}
		return null;
	}

	@Override
	public boolean isDiscarded() {

		return discarded;
	}

	@Override
	public void setDiscarded(boolean discarded) {
		this.discarded = discarded;

	}

	/**
	 * @return the singleton
	 */
	@Override
	public boolean isSingleton() {
		return singleton;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;

	}
}
