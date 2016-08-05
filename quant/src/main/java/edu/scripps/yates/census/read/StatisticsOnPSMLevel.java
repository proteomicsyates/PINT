package edu.scripps.yates.census.read;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.scripps.yates.census.analysis.util.KeyUtils;
import edu.scripps.yates.census.read.model.IsobaricQuantifiedPSM;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantRatio;
import edu.scripps.yates.census.read.util.QuantificationLabel;
import edu.scripps.yates.utilities.maths.Maths;

public class StatisticsOnPSMLevel {
	private static final Logger log = Logger.getLogger(StatisticsOnPSMLevel.class);
	private final static String SEPARATOR = "\t";
	private static final String YES = "1";
	private static final String NO = "0";
	private final CensusChroParser census;
	private final DecimalFormat df = new DecimalFormat("#.#");
	private StringBuilder psmTableString;
	private final boolean printPeptideTotal;
	private final QuantificationLabel labelNumerator;
	private final QuantificationLabel labelDenominator;

	public StatisticsOnPSMLevel(CensusChroParser census, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator, boolean printPeptideTotal) {
		this.census = census;
		this.printPeptideTotal = printPeptideTotal;
		this.labelDenominator = labelDenominator;
		this.labelNumerator = labelNumerator;
		process();
	}

	private void process() {
		psmTableString = new StringBuilder();
		log.info("Getting statistics from data...");
		// columnns
		psmTableString.append("Pep seq" + SEPARATOR + "PSM id" + SEPARATOR);
		List<String> taxonomyList = new ArrayList<String>();
		taxonomyList.addAll(census.getTaxonomies());
		Collections.sort(taxonomyList);
		for (String taxonomy : taxonomyList) {
			psmTableString.append("Id on " + taxonomy + SEPARATOR);
		}
		for (QuantificationLabel label : QuantificationLabel.values()) {
			psmTableString.append("# ions labeled as " + label + SEPARATOR);
		}
		psmTableString.append("Avg ratio" + SEPARATOR);
		psmTableString.append("# ratios" + SEPARATOR);
		psmTableString.append("\n");

		List<edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface> sortedPSMs = getSortedPsms(
				census.getPSMMap());
		log.info("Iterating over " + sortedPSMs.size() + " quantified psms");
		int count = 0;
		String currentPeptideSequence = null;
		int peptideNumRatios = 0;
		int psmNumber = 0;
		Map<QuantificationLabel, Integer> peptideIonsLabeled = new HashMap<QuantificationLabel, Integer>();
		Map<String, Boolean> peptideIdentifiedBySpecie = new HashMap<String, Boolean>();
		List<Double> peptideRatios = new ArrayList<Double>();
		for (QuantifiedPSMInterface quantifiedPSM : sortedPSMs) {
			final String seq = quantifiedPSM.getFullSequence();
			if (currentPeptideSequence != null && !seq.equals(currentPeptideSequence) && printPeptideTotal) {
				// print PEPTIDE data
				psmTableString.append(currentPeptideSequence + SEPARATOR);
				psmTableString.append(psmNumber + SEPARATOR);
				for (String taxonomy : taxonomyList) {
					if (peptideIdentifiedBySpecie.containsKey(taxonomy) && peptideIdentifiedBySpecie.get(taxonomy)) {
						psmTableString.append(YES + SEPARATOR);
					} else {
						psmTableString.append(NO + SEPARATOR);
					}
				}
				for (QuantificationLabel label : QuantificationLabel.values()) {
					if (peptideIonsLabeled.containsKey(label)) {
						psmTableString.append(peptideIonsLabeled.get(label) + SEPARATOR);
					} else {
						psmTableString.append("0" + SEPARATOR);
					}
				}
				if (!peptideRatios.isEmpty()) {
					double sumOfAvgRatios = 0.0;
					for (Double avratio : peptideRatios) {
						sumOfAvgRatios += avratio;
					}
					double avgOfAvgRatios = sumOfAvgRatios / peptideRatios.size();
					psmTableString.append(avgOfAvgRatios + SEPARATOR);
				} else {
					psmTableString.append("-" + SEPARATOR);
				}
				psmTableString.append(peptideNumRatios + SEPARATOR);
				psmTableString.append("\n\n");

				// reset all peptide data
				peptideIdentifiedBySpecie.clear();
				peptideIonsLabeled.clear();
				peptideRatios.clear();
				peptideNumRatios = 0;
				psmNumber = 0;
			}
			currentPeptideSequence = seq;
			String psmID = KeyUtils.getSpectrumKey(quantifiedPSM, census.isChargeStateSensible());
			if (count++ % 500 == 0) {
				log.info(df.format(Double.valueOf(count) * 100 / census.getPSMMap().size()) + " % of PSMs...");
			}
			if ("3504-DmDv_10e_each_071214_02".equals(psmID))
				System.out.println("asdf");

			// PEP SEQ
			psmTableString.append(seq + SEPARATOR);
			// PSM ID
			psmTableString.append(psmID + SEPARATOR);
			// taxonomies
			for (String taxonomy : taxonomyList) {
				if (quantifiedPSM.getTaxonomies().contains(taxonomy)) {
					psmTableString.append(YES + SEPARATOR);
					peptideIdentifiedBySpecie.put(taxonomy, true);
				} else {
					psmTableString.append(NO + SEPARATOR);
				}
			}
			// labeled
			for (QuantificationLabel label : QuantificationLabel.values()) {
				if (quantifiedPSM instanceof IsobaricQuantifiedPSM) {
					IsobaricQuantifiedPSM isoPsm = (IsobaricQuantifiedPSM) quantifiedPSM;
					final int numLabeledIons = isoPsm.getSingletonIonsByLabel(label).size();
					psmTableString.append(numLabeledIons + SEPARATOR);
					if (peptideIonsLabeled.containsKey(label)) {
						peptideIonsLabeled.put(label, peptideIonsLabeled.get(label) + numLabeledIons);
					} else {
						peptideIonsLabeled.put(label, numLabeledIons);
					}
				}
			}

			Double avgRatio = null;
			if (!quantifiedPSM.getRatios().isEmpty()) {
				double sum = 0.0;
				int valid = 0;
				for (QuantRatio ratio : quantifiedPSM.getRatios()) {
					if (!Maths.isMaxOrMinValue(ratio.getLog2Ratio(labelNumerator, labelDenominator))
							&& !Double.isNaN(ratio.getLog2Ratio(labelNumerator, labelDenominator))) {
						sum += ratio.getLog2Ratio(labelNumerator, labelDenominator);
						valid++;
					}
				}
				avgRatio = sum / valid;
				peptideRatios.add(avgRatio);
			}
			if (avgRatio == null) {
				psmTableString.append("-" + SEPARATOR);
			} else {
				psmTableString.append(avgRatio + SEPARATOR);
			}
			psmTableString.append(quantifiedPSM.getRatios().size() + SEPARATOR);
			peptideNumRatios += quantifiedPSM.getRatios().size();
			psmNumber++;
			psmTableString.append("\n");

		}

		log.info("Statistics done.");

	}

	private List<QuantifiedPSMInterface> getSortedPsms(Map<String, QuantifiedPSMInterface> psmMap) {
		final List<QuantifiedPSMInterface> ret = new ArrayList<QuantifiedPSMInterface>();
		ret.addAll(psmMap.values());
		Collections.sort(ret, new Comparator<QuantifiedPSMInterface>() {

			@Override
			public int compare(QuantifiedPSMInterface o1, QuantifiedPSMInterface o2) {
				String seq1 = o1.getFullSequence();
				String seq2 = o2.getFullSequence();
				return seq1.compareTo(seq2);
			}

		});
		return ret;
	}

	/**
	 * Prints a table with the number of protein identified in each tag (light
	 * or heavy) for each species.
	 *
	 * @return
	 */
	public String printPSMTable() {
		return psmTableString.toString();

	}

}
