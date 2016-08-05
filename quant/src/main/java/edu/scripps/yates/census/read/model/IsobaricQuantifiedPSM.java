package edu.scripps.yates.census.read.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.analysis.util.KeyUtils;
import edu.scripps.yates.census.quant.xml.ProteinType.Peptide;
import edu.scripps.yates.census.quant.xml.ProteinType.Peptide.Frag;
import edu.scripps.yates.census.read.AbstractQuantParser;
import edu.scripps.yates.census.read.model.IonSerie.IonSerieType;
import edu.scripps.yates.census.read.model.interfaces.HasIsoRatios;
import edu.scripps.yates.census.read.model.interfaces.QuantRatio;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPeptideInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedProteinInterface;
import edu.scripps.yates.census.read.util.IonExclusion;
import edu.scripps.yates.census.read.util.QuantUtil;
import edu.scripps.yates.census.read.util.QuantificationLabel;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.grouping.GroupableProtein;
import edu.scripps.yates.utilities.grouping.PeptideRelation;
import edu.scripps.yates.utilities.maths.Maths;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;
import edu.scripps.yates.utilities.model.factories.AmountEx;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;

public class IsobaricQuantifiedPSM implements QuantifiedPSMInterface, HasIsoRatios {
	private static final Logger log = Logger.getLogger(IsobaricQuantifiedPSM.class);
	private final Peptide peptide;
	private final Set<QuantifiedProteinInterface> quantifiedProteins = new HashSet<QuantifiedProteinInterface>();
	private final Set<edu.scripps.yates.census.read.util.QuantificationLabel> labels = new HashSet<QuantificationLabel>();
	private final Set<String> taxonomies = new HashSet<String>();
	private IonSerie serieYHeavy;
	private IonSerie serieYLight;
	private IonSerie serieBHeavy;
	private IonSerie serieBLight;
	private List<IsoRatio> ratiosSerieY;
	private List<IsoRatio> ratiosSerieB;
	private PeptideRelation relation;
	private final Set<QuantRatio> ratios = new HashSet<QuantRatio>();
	private final Collection<IonExclusion> ionExclusions;
	private final boolean chargeStateSensible;
	private final String scan;
	private final String sequence;
	private static int scanNum = 0;
	private final Map<QuantCondition, QuantificationLabel> labelsByConditions;
	private final Map<QuantificationLabel, QuantCondition> conditionsByLabels;
	private QuantifiedPeptideInterface quantifiedPeptide;
	private final HashMap<String, Double> countRatiosByConditionKey = new HashMap<String, Double>();
	private final Set<Amount> amounts = new HashSet<Amount>();
	private final Set<String> fileNames = new HashSet<String>();
	private boolean discarded;

	/**
	 *
	 * @param peptide
	 * @param labelNumerator
	 *            for writting data file. Otherwise, it can be null
	 * @param labelDenominator
	 *            for writting data file. Otherwise it can be null
	 * @param spectrumToIonsMap
	 * @param peptideToSpectraMap
	 * @param ionKeys
	 * @param ionExclusions
	 * @param dataFileWriter
	 * @param chargeStateSensible
	 * @param string
	 * @throws IOException
	 */
	public IsobaricQuantifiedPSM(Peptide peptide, Map<QuantCondition, QuantificationLabel> labelsByConditions,
			HashMap<String, Set<String>> spectrumToIonsMap, HashMap<String, Set<String>> peptideToSpectraMap,
			Collection<IonExclusion> ionExclusions, boolean chargeStateSensible) throws IOException {
		this.chargeStateSensible = chargeStateSensible;
		this.peptide = peptide;
		this.ionExclusions = ionExclusions;
		scan = peptide.getScan();
		sequence = peptide.getSeq();
		this.labelsByConditions = labelsByConditions;

		conditionsByLabels = new HashMap<QuantificationLabel, QuantCondition>();
		for (QuantCondition condition : labelsByConditions.keySet()) {
			final QuantificationLabel quantificationLabel = labelsByConditions.get(condition);
			conditionsByLabels.put(quantificationLabel, condition);
		}

		process();
	}

	@Override
	public String getKey() {
		return KeyUtils.getSpectrumKey(peptide, chargeStateSensible);
	}

	/**
	 * @return the fileName
	 */
	@Override
	public String getRawFileName() {

		if (peptide != null) {
			return peptide.getFile();
		}
		return null;
	}

	private void process() throws IOException {
		final String peptideKey = KeyUtils.getSequenceChargeKey(this, chargeStateSensible);

		final String spectrumKey = KeyUtils.getSpectrumKey(this, chargeStateSensible);

		final Frag frag = peptide.getFrag();
		final String yr = frag.getYr();
		final String ys = frag.getYs();

		// SERIE Y
		serieYHeavy = new IonSerie(QuantificationLabel.HEAVY, IonSerieType.Y, yr, ionExclusions);
		serieYLight = new IonSerie(QuantificationLabel.LIGHT, IonSerieType.Y, ys, ionExclusions);
		// check the ions and remove the ones that has the same intensities in
		// the two labels, which means that cannot be distinguished
		checkIons(serieYLight, serieYHeavy);

		ratiosSerieY = getRatiosFromSeries(serieYLight, serieYHeavy);
		ratios.addAll(ratiosSerieY);

		// SERIE B
		final String br = frag.getBr();
		final String bs = frag.getBs();
		serieBHeavy = new IonSerie(QuantificationLabel.HEAVY, IonSerieType.B, br, ionExclusions);
		serieBLight = new IonSerie(QuantificationLabel.LIGHT, IonSerieType.B, bs, ionExclusions);
		// check the ions and remove the ones that has the same intensities in
		// the two labels, which means that cannot be distinguised
		checkIons(serieBLight, serieBHeavy);
		ratiosSerieB = getRatiosFromSeries(serieBLight, serieBHeavy);

		ratios.addAll(ratiosSerieB);

		// create ion amounts
		createIonAmounts();
	}

	public void addSpectrumToIonsMaps(String spectrumKey, HashMap<String, Set<String>> spectrumToIonsMap,
			Set<String> ionKeys) {
		for (IsoRatio ratio : ratiosSerieY) {
			ratios.add(ratio);
			String ionKey = KeyUtils.getIonKey(ratio, peptide, chargeStateSensible);
			if (ionKeys.contains(ionKey)) {
				continue;
			}
			AbstractQuantParser.addToMap(spectrumKey, spectrumToIonsMap, ionKey);
		}
		for (IsoRatio ratio : ratiosSerieB) {
			ratios.add(ratio);
			String ionKey = KeyUtils.getIonKey(ratio, peptide, chargeStateSensible);
			if (ionKeys.contains(ionKey)) {
				continue;
			}
			AbstractQuantParser.addToMap(spectrumKey, spectrumToIonsMap, ionKey);
		}
	}

	private void createIonAmounts() {
		final Set<QuantCondition> conditions = getIonsByCondition().keySet();
		for (QuantCondition condition : conditions) {
			final Set<Ion> ions = getIonsByCondition().get(condition);
			if (ions != null) {
				for (Ion ion : ions) {
					AmountEx amount = new AmountEx(ion.getIntensity(),
							edu.scripps.yates.utilities.model.enums.AmountType.INTENSITY, condition);
					// singleton or not
					final boolean singleton = ion.isSingleton();
					amount.setSingleton(singleton);
					addAmount(amount);
				}
			}
		}

	}

	private void checkIons(IonSerie lightSerie, IonSerie heavySerie) {
		final HashMap<Integer, Ion> lightMap = lightSerie.getIonMap();
		final HashMap<Integer, Ion> heavyMap = heavySerie.getIonMap();
		int max = lightSerie.getMaxNumberIon();
		if (heavySerie.getMaxNumberIon() > max)
			max = heavySerie.getMaxNumberIon();

		for (int numIon = 1; numIon <= max; numIon++) {
			final Ion lightIon = lightMap.get(numIon);
			final Ion heavyIon = heavyMap.get(numIon);
			if (lightIon != null && heavyIon != null) {
				if (lightIon.getIntensity() == heavyIon.getIntensity()) {
					lightSerie.removeIon(numIon);
					heavySerie.removeIon(numIon);
				}
			}
		}
	}

	// private void addToIonsMap(List<Ion> ions, QUANTIFICATION_LABEL label) {
	// if (ions != null && !ions.isEmpty()) {
	// if (this.ions.containsKey(label)) {
	// this.ions.get(label).addAll(ions);
	// } else {
	// List<Ion> list = new ArrayList<Ion>();
	// list.addAll(ions);
	// this.ions.put(label, list);
	// }
	// // is labeled only if it is labeled with ions that doesn't belongs
	// // to any ratio
	// for (Ion ion : ions) {
	// if (ion.getRatio() == null) {
	// labels.add(label);
	// break;
	// }
	// }
	//
	// }
	// }

	/*
	 * (non-Javadoc)
	 * @see
	 * edu.scripps.yates.census.quant.xml.RelexChro.Protein.Peptide#getChro()
	 */

	public String getChro() {

		return peptide.getChro();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * edu.scripps.yates.census.quant.xml.RelexChro.Protein.Peptide#getFrag()
	 */

	public Frag getFrag() {

		return peptide.getFrag();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * edu.scripps.yates.census.quant.xml.RelexChro.Protein.Peptide#getUnique()
	 */

	public String getUnique() {

		return peptide.getUnique();
	}

	@Override
	public String getScan() {

		return scan;

	}

	public String getSeq() {

		return sequence;
	}

	@Override
	public Float getXcorr() {

		return peptide.getXcorr();
	}

	@Override
	public Float getCalcMHplus() {

		return peptide.getCalcMHplus();
	}

	@Override
	public Float getMHplus() {

		return peptide.getMHplus();
	}

	public Float getTotalIntensity() {

		return peptide.getTotalIntensity();
	}

	public Integer getSpRank() {

		return peptide.getSpRank();
	}

	public Float getSpScore() {

		return peptide.getSpScore();
	}

	public Integer getRedundancy() {

		return peptide.getRedundancy();
	}

	@Override
	public Float getDeltaCN() {

		return peptide.getDeltaCN();
	}

	@Override
	public Float getDeltaMass() {

		return peptide.getDeltaMass();
	}

	@Override
	public Integer getCharge() {

		return peptide.getCharge();
	}

	public Integer getSpC() {

		return peptide.getSpC();
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
	 * Returns true if the peptide contains any {@link Ion} labeled with a
	 * certain {@link QuantificationLabel} and not composing any ratio.
	 *
	 * @param label
	 * @return
	 */
	@Override
	public boolean containsAnySingletonIon(QuantificationLabel label) {
		return containsAnySingletonIon(label, serieBHeavy) || containsAnySingletonIon(label, serieBLight)
				|| containsAnySingletonIon(label, serieYHeavy) || containsAnySingletonIon(label, serieYLight);
	}

	/**
	 * Returns true if the peptide contains any {@link Ion} labeled with a
	 * certain {@link QuantificationLabel}, not matter if they are composing any
	 * ratio or not.
	 *
	 * @param label
	 * @return
	 */
	@Override
	public boolean containsAnyIon(QuantificationLabel label) {
		return containsAnyIon(label, serieBHeavy) || containsAnyIon(label, serieBLight)
				|| containsAnyIon(label, serieYHeavy) || containsAnyIon(label, serieYLight);
	}

	/**
	 * Returns true if the peptide contains any {@link Ion} labeled with a
	 * certain {@link QuantificationLabel}, no matter if they are composing any
	 * ratio or not.
	 *
	 * @param label
	 * @return
	 */
	private boolean containsAnyIon(QuantificationLabel label, IonSerie serie) {
		if (serie != null) {
			return label.equals(serie.getNonNullLabel()) && !serie.getNonNullIons().isEmpty();
		}
		return false;
	}

	/**
	 * Returns true if the peptide contains any {@link Ion} labeled with a
	 * certain {@link QuantificationLabel} and not composing any ratio.
	 *
	 * @param label
	 * @return
	 */
	private boolean containsAnySingletonIon(QuantificationLabel label, IonSerie serie) {
		if (serie != null) {
			return label.equals(serie.getNonNullLabel()) && serie.isSingletonLabeled();
		}
		return false;
	}

	private List<IsoRatio> getRatiosFromSeries(IonSerie serier, IonSerie series) {

		List<IsoRatio> ret = new ArrayList<IsoRatio>();
		if (series != null) {
			int max = serier.getMaxNumberIon();
			if (series.getMaxNumberIon() > max)
				max = series.getMaxNumberIon();
			for (int ionNumber = 1; ionNumber <= max; ionNumber++) {
				final Ion ionr = serier.getIon(ionNumber);
				final Ion ions = series.getIon(ionNumber);

				// ignore the ions in ionExclusion
				boolean excludeThisIon = false;
				if (ionExclusions != null) {
					for (IonExclusion ionExlusion : ionExclusions) {
						if (ionExlusion.getIonNumber() == ionNumber) {
							if (ionExlusion.getIonSerieType() == serier.getIonSerieType()) {
								excludeThisIon = true;
							}
							if (ionExlusion.getIonSerieType() == series.getIonSerieType()) {
								excludeThisIon = true;
							}
						}
					}
				}
				if (excludeThisIon) {
					continue;
				}

				// if (ionr != null && ions != null) {
				// ignore if they are not null but they are the same. That is a
				// Not
				// Determined peak (ND)

				if (ionr != null && ions != null && ionr.getIntensity() == ions.getIntensity()) {
					continue;
				}
				// create the ratio if at least one of the is not null
				if (ionr == null && ions == null)
					continue;

				final QuantificationLabel label1 = series.getNonNullLabel();
				final QuantificationLabel label2 = serier.getNonNullLabel();
				final QuantCondition condition1 = conditionsByLabels.get(label1);
				final QuantCondition condition2 = conditionsByLabels.get(label2);
				ret.add(new IsoRatio(ions, label1, condition1, ionr, label2, condition2, ionNumber,
						serier.getIonSerieType(), AggregationLevel.PSM));

			}
		}
		return ret;
	}

	/**
	 * @return the ratios
	 */
	@Override
	public Set<IsoRatio> getIsoRatios() {
		Set<IsoRatio> isoRatios = new HashSet<IsoRatio>();
		for (QuantRatio ratio : getRatios()) {
			if (ratio instanceof IsoRatio) {
				isoRatios.add((IsoRatio) ratio);
			}
		}
		return isoRatios;
	}

	/**
	 * @return the ratios
	 */
	@Override
	public Set<IsoRatio> getNonInfinityIsoRatios() {
		Set<IsoRatio> ret = new HashSet<IsoRatio>();
		for (IsoRatio isoRatio : getIsoRatios()) {
			if (Double.compare(isoRatio.getLog2Ratio(isoRatio.getLabel1(), isoRatio.getLabel2()), Double.MAX_VALUE) == 0
					|| Double.compare(isoRatio.getLog2Ratio(isoRatio.getLabel1(), isoRatio.getLabel2()),
							-Double.MAX_VALUE) == 0) {
				continue;
			}
			ret.add(isoRatio);
		}
		return ret;
	}

	/**
	 * @return the singletonIons
	 */
	@Override
	public Map<QuantificationLabel, Set<Ion>> getSingletonIonsByLabel() {
		Map<QuantificationLabel, Set<Ion>> ret = new HashMap<QuantificationLabel, Set<Ion>>();
		final Map<QuantificationLabel, Set<Ion>> singletonIons = getSingletonIons(serieBHeavy);
		// if (!singletonIons.isEmpty())
		ret.putAll(singletonIons);
		final Map<QuantificationLabel, Set<Ion>> singletonIons2 = getSingletonIons(serieBLight);
		// if (!singletonIons2.isEmpty()) {
		addToMapByLabel(ret, singletonIons2);
		// }
		final Map<QuantificationLabel, Set<Ion>> singletonIons3 = getSingletonIons(serieYHeavy);
		// if (!singletonIons3.isEmpty()) {
		addToMapByLabel(ret, singletonIons3);
		// }
		final Map<QuantificationLabel, Set<Ion>> singletonIons4 = getSingletonIons(serieYLight);
		// if (!singletonIons4.isEmpty()) {
		addToMapByLabel(ret, singletonIons4);
		// }
		return ret;
	}

	/**
	 * @return the singletonIons
	 */
	@Override
	public Map<QuantificationLabel, Set<Ion>> getIonsByLabel() {
		Map<QuantificationLabel, Set<Ion>> ret = new HashMap<QuantificationLabel, Set<Ion>>();
		final Map<QuantificationLabel, Set<Ion>> singletonIons = getIons(serieBHeavy);
		if (!singletonIons.isEmpty())
			addToMapByLabel(ret, singletonIons);
		final Map<QuantificationLabel, Set<Ion>> singletonIons2 = getIons(serieBLight);
		if (!singletonIons2.isEmpty()) {
			addToMapByLabel(ret, singletonIons2);
		}
		final Map<QuantificationLabel, Set<Ion>> singletonIons3 = getIons(serieYHeavy);
		if (!singletonIons3.isEmpty()) {
			addToMapByLabel(ret, singletonIons3);
		}
		final Map<QuantificationLabel, Set<Ion>> singletonIons4 = getIons(serieYLight);
		if (!singletonIons4.isEmpty()) {
			addToMapByLabel(ret, singletonIons4);
		}

		return ret;
	}

	private void addToMapByLabel(Map<QuantificationLabel, Set<Ion>> receiver,
			Map<QuantificationLabel, Set<Ion>> donor) {
		for (QuantificationLabel label : donor.keySet()) {
			final Set<Ion> ions = donor.get(label);
			if (receiver.containsKey(label)) {
				receiver.get(label).addAll(ions);
			} else {
				Set<Ion> set = new HashSet<Ion>();
				set.addAll(ions);
				receiver.put(label, set);
			}
		}

	}

	private void addToMapByCondition(Map<QuantCondition, Set<Ion>> receiver, Map<QuantificationLabel, Set<Ion>> donor) {
		for (QuantificationLabel label : donor.keySet()) {
			final QuantCondition condition = conditionsByLabels.get(label);
			final Set<Ion> ions = donor.get(label);
			if (receiver.containsKey(condition)) {
				receiver.get(condition).addAll(ions);
			} else {
				Set<Ion> set = new HashSet<Ion>();
				set.addAll(ions);
				receiver.put(condition, set);
			}
		}

	}

	/**
	 * @return the singletonIons
	 */
	@Override
	public Map<QuantificationLabel, Set<Ion>> getSingletonIons(IonSerie serie) {
		Map<QuantificationLabel, Set<Ion>> ret = new HashMap<QuantificationLabel, Set<Ion>>();
		final Set<Ion> singletonIons = serie.getSingletonIons();
		// if (!singletonIons.isEmpty()) {
		ret.put(serie.getNonNullLabel(), singletonIons);
		// }
		return ret;
	}

	/**
	 * @return the singletonIons
	 */
	@Override
	public Map<QuantificationLabel, Set<Ion>> getIons(IonSerie serie) {
		Map<QuantificationLabel, Set<Ion>> ret = new HashMap<QuantificationLabel, Set<Ion>>();
		if (serie != null) {
			final Set<Ion> ions = serie.getNonNullIons();
			if (!ions.isEmpty()) {
				ret.put(serie.getNonNullLabel(), ions);
			}
		}
		return ret;
	}

	@Override
	public Set<Ion> getIonsByLabel(QuantificationLabel label) {
		Set<Ion> ret = new HashSet<Ion>();
		final Set<Ion> ions = getIonsByLabel().get(label);
		if (ions != null && !ions.isEmpty()) {
			ret.addAll(ions);
		}
		return ret;
	}

	@Override
	public Set<Ion> getSingletonIonsByLabel(QuantificationLabel label) {
		Set<Ion> list = new HashSet<Ion>();
		Set<Ion> singletonIons = getSingletonIonsByLabel().get(label);
		if (singletonIons != null && !singletonIons.isEmpty()) {
			list.addAll(singletonIons);
		}
		return list;
	}

	/**
	 * Gets the labels that this {@link IsobaricQuantifiedPSM} has been labeled
	 * ONLY with some label.<br>
	 * So, may happen that contains any ratio and it is not labeled
	 *
	 * @return the labels
	 */
	public Set<QuantificationLabel> getLabels() {
		return labels;
	}

	/**
	 * @return the taxonomies
	 */
	@Override
	public Set<String> getTaxonomies() {
		return taxonomies;
	}

	@Override
	public String getSequence() {
		return FastaParser.cleanSequence(peptide.getSeq());
	}

	@Override
	public String getPSMIdentifier() {
		if (peptide != null) {
			return KeyUtils.getSpectrumKey(peptide, chargeStateSensible);
		} else {
			return getRawFileName() + "-" + getScan();
		}
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
	public Double getMaxPeak() {
		double max = -Double.MAX_VALUE;
		final Map<QuantificationLabel, Set<Ion>> ionsBylabel = getIonsByLabel();
		for (Set<Ion> ions : ionsBylabel.values()) {
			for (Ion ion : ions) {
				if (ion.getIntensity() > max)
					max = ion.getIntensity();
			}
		}
		return max;
	}

	public double getMaxPeakInRatio() {
		double max = -Long.MAX_VALUE;
		final Set<IsoRatio> ratios = getIsoRatios();
		for (IsoRatio ratio : ratios) {
			if (ratio.getMaxIntensity() > max) {
				max = ratio.getMaxIntensity();
			}
		}
		return max;
	}

	@Override
	public double getMeanRatios(QuantCondition quantConditionNumerator, QuantCondition quantConditionDenominator) {

		final Set<IsoRatio> isobaricRatios = getNonInfinityIsoRatios();
		List<Double> ratios = new ArrayList<Double>();

		for (IsoRatio isoRatio : isobaricRatios) {
			ratios.add(isoRatio.getLog2Ratio(quantConditionNumerator, quantConditionDenominator));
		}
		return Maths.mean(ratios.toArray(new Double[0]));
	}

	@Override
	public double getSTDRatios(QuantCondition quantConditionNumerator, QuantCondition quantConditionDenominator) {

		final Set<IsoRatio> isobaricRatios = getNonInfinityIsoRatios();
		List<Double> ratios = new ArrayList<Double>();
		for (IsoRatio isoRatio : isobaricRatios) {
			ratios.add(isoRatio.getLog2Ratio(quantConditionNumerator, quantConditionDenominator));
		}
		return Maths.stddev(ratios.toArray(new Double[0]));
	}

	@Override
	public Map<QuantCondition, Set<Ion>> getSingletonIonsByCondition() {
		Map<QuantCondition, Set<Ion>> ret = new HashMap<QuantCondition, Set<Ion>>();
		final Map<QuantificationLabel, Set<Ion>> singletonIons = getSingletonIons(serieBHeavy);
		// if (!singletonIons.isEmpty())
		addToMapByCondition(ret, singletonIons);

		final Map<QuantificationLabel, Set<Ion>> singletonIons2 = getSingletonIons(serieBLight);
		// if (!singletonIons2.isEmpty()) {
		addToMapByCondition(ret, singletonIons2);
		// }
		final Map<QuantificationLabel, Set<Ion>> singletonIons3 = getSingletonIons(serieYHeavy);
		// if (!singletonIons3.isEmpty()) {
		addToMapByCondition(ret, singletonIons3);

		// }
		final Map<QuantificationLabel, Set<Ion>> singletonIons4 = getSingletonIons(serieYLight);
		// if (!singletonIons4.isEmpty()) {
		addToMapByCondition(ret, singletonIons4);

		// }
		return ret;
	}

	@Override
	public Map<QuantCondition, Set<Ion>> getIonsByCondition() {
		Map<QuantCondition, Set<Ion>> ret = new HashMap<QuantCondition, Set<Ion>>();
		final Map<QuantificationLabel, Set<Ion>> singletonIons = getIons(serieBHeavy);
		if (!singletonIons.isEmpty()) {
			addToMapByCondition(ret, singletonIons);
		}
		final Map<QuantificationLabel, Set<Ion>> singletonIons2 = getIons(serieBLight);
		if (!singletonIons2.isEmpty()) {
			addToMapByCondition(ret, singletonIons2);
		}
		final Map<QuantificationLabel, Set<Ion>> singletonIons3 = getIons(serieYHeavy);
		if (!singletonIons3.isEmpty()) {
			addToMapByCondition(ret, singletonIons3);
		}
		final Map<QuantificationLabel, Set<Ion>> singletonIons4 = getIons(serieYLight);
		if (!singletonIons4.isEmpty()) {
			addToMapByCondition(ret, singletonIons4);
		}
		return ret;
	}

	@Override
	public void setQuantifiedPeptide(QuantifiedPeptideInterface quantifiedPeptide) {
		if (quantifiedPeptide instanceof IsobaricQuantifiedPeptide) {
			this.quantifiedPeptide = quantifiedPeptide;
			if (quantifiedPeptide != null) {
				quantifiedPeptide.addQuantifiedPSM(this);
			}
		} else {
			throw new IllegalArgumentException("Only IsobaricQuantifiedPeptides are allowed");
		}
	}

	/**
	 * @return the quantifiedPeptide
	 */
	@Override
	public IsobaricQuantifiedPeptide getQuantifiedPeptide() {
		return (IsobaricQuantifiedPeptide) quantifiedPeptide;
	}

	@Override
	public String getFullSequence() {
		return peptide.getSeq();
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
	public Set<QuantRatio> getRatios() {
		return ratios;
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
		ratios.add(ratio);

	}

	@Override
	public Set<QuantRatio> getNonInfinityRatios() {
		return QuantUtil.getNonInfinityRatios(getRatios());
	}

	/**
	 * @return the peptide
	 */
	public Peptide getPeptide() {
		return peptide;
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

	@Override
	public boolean isSingleton() {
		return getNonInfinityIsoRatios().isEmpty();
	}
}
