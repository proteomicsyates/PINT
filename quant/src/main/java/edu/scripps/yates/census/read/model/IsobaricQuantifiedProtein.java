package edu.scripps.yates.census.read.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.quant.xml.ProteinType;
import edu.scripps.yates.census.quant.xml.ProteinType.Peptide;
import edu.scripps.yates.census.read.model.interfaces.QuantRatio;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPeptideInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedProteinInterface;
import edu.scripps.yates.census.read.util.QuantUtil;
import edu.scripps.yates.census.read.util.QuantificationLabel;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.grouping.GroupablePSM;
import edu.scripps.yates.utilities.grouping.ProteinEvidence;
import edu.scripps.yates.utilities.grouping.ProteinGroup;
import edu.scripps.yates.utilities.maths.Maths;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.util.Pair;

public class IsobaricQuantifiedProtein extends AbstractContainsQuantifiedPSMs implements QuantifiedProteinInterface {
	private static final Logger log = Logger.getLogger(IsobaricQuantifiedProtein.class);

	private final ProteinType protein;
	private final Set<QuantifiedPSMInterface> quantifiedPSMs = new HashSet<QuantifiedPSMInterface>();
	private boolean distinguishModifiedPeptides;
	private ProteinEvidence evidence;
	private String primaryAccession;
	private ProteinGroup proteinGroup;
	private String accessionType;
	private final Set<QuantifiedPeptideInterface> quantifiedPeptides = new HashSet<QuantifiedPeptideInterface>();
	private String description;
	private String taxonomy;
	private final Set<QuantRatio> ratios = new HashSet<QuantRatio>();
	private final Map<String, Double> countRatiosByConditionKey = new HashMap<String, Double>();
	private Map<QuantCondition, Set<Ion>> ionsByConditions;
	private final Set<Amount> amounts = new HashSet<Amount>();

	private final Set<String> fileNames = new HashSet<String>();

	private boolean discarded;

	public IsobaricQuantifiedProtein(ProteinType protein) throws IOException {
		this.protein = protein;
		final Pair<String, String> accPair = FastaParser.getACC(protein.getLocus());
		primaryAccession = accPair.getFirstelement();
		accessionType = accPair.getSecondElement();
	}

	public IsobaricQuantifiedProtein(String proteinACC) throws IOException {
		protein = null;
		final Pair<String, String> accPair = FastaParser.getACC(proteinACC);
		primaryAccession = accPair.getFirstelement();
		accessionType = accPair.getSecondElement();
	}

	@Override
	public String getKey() {
		return primaryAccession;
	}
	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.census.quant.xml.RelexChro.Protein#getPeptide()
	 */

	public List<Peptide> getPeptide() {
		if (protein != null)
			return protein.getPeptide();

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.census.quant.xml.RelexChro.Protein#getSeqCt()
	 */

	public Integer getSeqCt() {
		if (protein != null)
			return protein.getSeqCt();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.census.quant.xml.RelexChro.Protein#getSpecCt()
	 */

	public Integer getSpecCt() {
		if (protein != null)
			return protein.getSpecCt();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.census.quant.xml.RelexChro.Protein#getSeqCov()
	 */

	public String getSeqCov() {
		if (protein != null)
			return protein.getSeqCov();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.census.quant.xml.RelexChro.Protein#getLength()
	 */

	@Override
	public Integer getLength() {
		if (protein != null) {
			try {
				return Integer.valueOf(protein.getLength());
			} catch (NumberFormatException e) {

			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.census.quant.xml.RelexChro.Protein#getMolwt()
	 */

	public Float getMolwt() {
		if (protein != null) {
			try {
				return Float.valueOf(protein.getMolwt());
			} catch (NumberFormatException e) {

			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.census.quant.xml.RelexChro.Protein#getPi()
	 */

	public Float getPi() {
		if (protein != null) {
			try {
				return Float.valueOf(protein.getPi());
			} catch (NumberFormatException e) {

			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.census.quant.xml.RelexChro.Protein#getVal()
	 */

	public String getVal() {
		if (protein != null)
			return protein.getVal();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.census.quant.xml.RelexChro.Protein#getDesc()
	 */
	@Override
	public String getDescription() {
		if (description == null) {
			if (protein != null) {
				description = protein.getDesc();
			}
		}
		return description;
	}

	/**
	 * @return the quantifiedPSMs
	 */
	@Override
	public Set<QuantifiedPSMInterface> getQuantifiedPSMs() {
		return quantifiedPSMs;
	}

	/**
	 * @return the quantifiedPeptides
	 */
	@Override
	public Set<QuantifiedPeptideInterface> getQuantifiedPeptides() {
		return quantifiedPeptides;
	}

	@Override
	public void addPSM(QuantifiedPSMInterface quantifiedPSM) {
		quantifiedPSMs.add(quantifiedPSM);
		quantifiedPSM.addQuantifiedProtein(this);

	}

	@Override
	public int getDBId() {
		return hashCode();
	}

	@Override
	public String getAccession() {
		return primaryAccession;
	}

	@Override
	public String getAccessionType() {
		return accessionType;
	}

	@Override
	public void setEvidence(ProteinEvidence evidence) {
		this.evidence = evidence;

	}

	@Override
	public void setProteinGroup(ProteinGroup proteinGroup) {
		this.proteinGroup = proteinGroup;

	}

	@Override
	public List<GroupablePSM> getGroupablePSMs() {
		List<GroupablePSM> list = new ArrayList<GroupablePSM>();
		list.addAll(getQuantifiedPSMs());
		return list;
	}

	@Override
	public ProteinGroup getProteinGroup() {
		return proteinGroup;
	}

	@Override
	public Set<String> getTaxonomies() {
		if (taxonomy == null) {
			String fastaHeader = null;
			final String accession = getAccession();
			if (protein != null)
				fastaHeader = protein.getDesc();

			final String organismNameFromFastaHeader = FastaParser.getOrganismNameFromFastaHeader(fastaHeader,
					accession);
			taxonomy = organismNameFromFastaHeader;
		}
		Set<String> set = new HashSet<String>();
		set.add(taxonomy);
		return set;
	}

	@Override
	public ProteinEvidence getEvidence() {
		return evidence;
	}

	/**
	 * @return the distinguishModifiedPeptides
	 */
	public boolean isDistinguishModifiedPeptides() {
		return distinguishModifiedPeptides;
	}

	/**
	 * @param distinguishModifiedPeptides
	 *            the distinguishModifiedPeptides to set
	 */
	public void setDistinguishModifiedPeptides(boolean distinguishModifiedPeptides) {
		this.distinguishModifiedPeptides = distinguishModifiedPeptides;
	}

	/**
	 * @return the fileNames
	 */
	@Override
	public Set<String> getRawFileNames() {
		Set<String> ret = new HashSet<String>();
		final Set<QuantifiedPSMInterface> quantifiedPSMs2 = getQuantifiedPSMs();
		for (QuantifiedPSMInterface quantifiedPSMInterface : quantifiedPSMs2) {
			ret.add(quantifiedPSMInterface.getRawFileName());
		}
		return ret;
	}

	@Override
	public void addPeptide(QuantifiedPeptideInterface peptide) {
		if (quantifiedPeptides.contains(peptide))
			return;
		quantifiedPeptides.add(peptide);
	}

	/**
	 * @return the primaryAccession
	 */
	public String getPrimaryAccession() {
		return primaryAccession;
	}

	/**
	 * @param primaryAccession
	 *            the primaryAccession to set
	 */
	public void setPrimaryAccession(String primaryAccession) {
		this.primaryAccession = primaryAccession;
	}

	/**
	 * @param accessionType
	 *            the accessionType to set
	 */
	public void setAccessionType(String accessionType) {
		this.accessionType = accessionType;
	}

	@Override
	public void setAccession(String primaryAccession) {
		this.primaryAccession = primaryAccession;

	}

	@Override
	public void setDescription(String description) {
		this.description = description;

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getAccession() + ": ");
		List<QuantifiedPeptideInterface> list = new ArrayList<QuantifiedPeptideInterface>();
		list.addAll(getQuantifiedPeptides());
		Collections.sort(list, new Comparator<QuantifiedPeptideInterface>() {

			@Override
			public int compare(QuantifiedPeptideInterface o1, QuantifiedPeptideInterface o2) {
				return o1.getSequence().compareTo(o2.getSequence());
			}
		});
		StringBuilder sb2 = new StringBuilder();
		for (QuantifiedPeptideInterface quantifiedPeptide : list) {
			if (!"".equals(sb2.toString()))
				sb2.append(",");
			sb2.append(quantifiedPeptide.getSequence());
		}
		sb.append(sb2);
		return sb.toString();
	}

	@Override
	public void setTaxonomy(String taxonomy) {
		this.taxonomy = taxonomy;

	}

	public Set<IsobaricQuantifiedPSM> getIsobaricQuantifiedPSMs() {
		Set<IsobaricQuantifiedPSM> ret = new HashSet<IsobaricQuantifiedPSM>();
		final Set<QuantifiedPSMInterface> quantifiedPSMs2 = getQuantifiedPSMs();
		for (QuantifiedPSMInterface quantifiedPSMInterface : quantifiedPSMs2) {
			if (quantifiedPSMInterface instanceof IsobaricQuantifiedPSM) {
				ret.add((IsobaricQuantifiedPSM) quantifiedPSMInterface);
			}
		}
		return ret;
	}

	public Set<IsobaricQuantifiedPeptide> getIsobaricQuantifiedPeptides() {
		Set<IsobaricQuantifiedPeptide> ret = new HashSet<IsobaricQuantifiedPeptide>();
		final Set<QuantifiedPeptideInterface> quantifiedPeptides2 = getQuantifiedPeptides();
		for (QuantifiedPeptideInterface quantifiedPeptideInterface : quantifiedPeptides2) {
			if (quantifiedPeptideInterface instanceof IsobaricQuantifiedPeptide) {
				ret.add((IsobaricQuantifiedPeptide) quantifiedPeptideInterface);
			}
		}
		return ret;
	}

	@Override
	public Set<QuantRatio> getRatios() {
		if (ratios.isEmpty()) {
			for (IsobaricQuantifiedPSM psm : getIsobaricQuantifiedPSMs()) {
				ratios.addAll(psm.getIsoRatios());
			}
		}
		return ratios;
	}

	@Override
	public Set<QuantRatio> getRatios(String replicateName) {
		if (ratios.isEmpty()) {
			for (IsobaricQuantifiedPSM psm : getIsobaricQuantifiedPSMs()) {
				if (psm.getRawFileName().equals(replicateName)) {
					ratios.addAll(psm.getIsoRatios());
				}
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
	public boolean containsAnyIon(QuantificationLabel label) {
		for (IsobaricQuantifiedPSM quantifiedPeptide : getIsobaricQuantifiedPSMs()) {
			if (quantifiedPeptide.containsAnyIon(label))
				return true;
		}
		return false;
	}

	public Map<QuantificationLabel, Set<Ion>> getIons(IonSerie ionSerie) {
		Map<QuantificationLabel, Set<Ion>> ret = new HashMap<QuantificationLabel, Set<Ion>>();
		for (IsobaricQuantifiedPSM psm : getIsobaricQuantifiedPSMs()) {
			final Map<QuantificationLabel, Set<Ion>> ions = psm.getIons(ionSerie);
			mergeMaps(ret, ions);

		}
		return ret;
	}

	public Set<Ion> getSingletonIonsByLabel(QuantificationLabel label) {
		Set<Ion> ret = new HashSet<Ion>();
		for (IsobaricQuantifiedPSM psm : getIsobaricQuantifiedPSMs()) {
			ret.addAll(psm.getSingletonIonsByLabel(label));
		}
		return ret;
	}

	public Set<Ion> getIonsByLabel(QuantificationLabel label) {
		Set<Ion> ret = new HashSet<Ion>();
		for (IsobaricQuantifiedPSM psm : getIsobaricQuantifiedPSMs()) {
			ret.addAll(psm.getIonsByLabel(label));
		}
		return ret;
	}

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

	public Set<IsoRatio> getNonInfinityIsoRatios() {
		Set<IsoRatio> ret = new HashSet<IsoRatio>();
		for (IsobaricQuantifiedPSM psm : getIsobaricQuantifiedPSMs()) {
			ret.addAll(psm.getNonInfinityIsoRatios());
		}
		return ret;
	}

	public double getMaxPeak() {
		double max = Double.MIN_VALUE;
		for (IsobaricQuantifiedPSM psm : getIsobaricQuantifiedPSMs()) {
			if (max < psm.getMaxPeak()) {
				max = psm.getMaxPeak();
			}
		}
		return max;
	}

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

	@Override
	public Set<Amount> getAmounts() {
		return amounts;
	}

	@Override
	public void addAmount(Amount amount) {
		amounts.add(amount);
	}

	@Override
	public void addRatio(QuantRatio ratio) {
		getRatios();
		ratios.add(ratio);

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
	public QuantRatio getConsensusRatio(QuantCondition cond1, QuantCondition cond2) {
		return QuantUtil.getAverageRatio(QuantUtil.getNonInfinityRatios(getRatios()), AggregationLevel.PROTEIN);
	}

	@Override
	public QuantRatio getConsensusRatio(QuantCondition cond1, QuantCondition cond2, String replicateName) {
		return QuantUtil.getAverageRatio(QuantUtil.getNonInfinityRatios(getRatios(replicateName)),
				AggregationLevel.PROTEIN);
	}

	@Override
	public boolean isDiscarded() {

		return discarded;
	}

	@Override
	public void setDiscarded(boolean discarded) {
		this.discarded = discarded;
		final Set<QuantifiedPSMInterface> quantifiedPSMs = getQuantifiedPSMs();
		for (QuantifiedPSMInterface quantifiedPSMInterface : quantifiedPSMs) {
			quantifiedPSMInterface.setDiscarded(discarded);
		}
	}
}
