package edu.scripps.yates.census.read;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedProteinInterface;
import edu.scripps.yates.census.read.util.CensusChroUtil;
import edu.scripps.yates.census.read.util.QuantificationLabel;
import edu.scripps.yates.utilities.grouping.GroupableProtein;
import edu.scripps.yates.utilities.grouping.PAnalyzer;
import edu.scripps.yates.utilities.grouping.PanalyzerStats;
import edu.scripps.yates.utilities.grouping.ProteinGroup;
import edu.scripps.yates.utilities.taxonomy.UniprotOrganism;
import edu.scripps.yates.utilities.taxonomy.UniprotSpeciesCodeMap;

public class StatisticsOnProteinLevel {
	private static final Logger log = Logger.getLogger(StatisticsOnProteinLevel.class);
	private final CensusChroParser census;
	private final Map<String, QuantifiedProteinInterface> proteinsInBothSpecies = new HashMap<String, QuantifiedProteinInterface>();
	private final Map<String, Map<String, QuantifiedProteinInterface>> proteinsInOneSpecie = new HashMap<String, Map<String, QuantifiedProteinInterface>>();
	private final Set<String> allTaxonomies = new HashSet<String>();
	private final Map<String, QuantifiedProteinInterface> proteinsContainingRatios = new HashMap<String, QuantifiedProteinInterface>();
	private final Map<String, QuantifiedProteinInterface> proteinsNotContainingRatios = new HashMap<String, QuantifiedProteinInterface>();
	private final DecimalFormat df = new DecimalFormat("#.#");
	// private DBIndexInterface dbIndex;
	// private final File dbIndexParamFile;
	private List<ProteinGroup> proteinGroups;
	private PanalyzerStats groupingStats;

	public StatisticsOnProteinLevel(CensusChroParser census) {
		this.census = census;
		process();
	}

	private void process() {
		log.info("Getting statistics from data...");

		// Set<String> peptideKeys = new HashSet<String>();
		// for (QuantifiedProtein quantifiedProtein : census.getProteinMap()
		// .values()) {
		// final List<Peptide> peptides = quantifiedProtein.getPeptide();
		// for (Peptide peptide : peptides) {
		// final String spectrumKey = CensusUtil.getSpectrumKey(peptide);
		// if (peptideKeys.contains(spectrumKey)) {
		// log.info("PEPTIDES ARE REPEATED OVER THE FILE> "
		// + spectrumKey);
		// } else {
		// peptideKeys.add(spectrumKey);
		// }
		// }
		// }

		// final Set<String> quantifiedProteinAccs = census
		// .getProteinToPeptidesMap().keySet();
		log.info("Iterating over " + census.getPSMMap().size() + " quantified psms");
		int count = 0;
		for (QuantifiedPSMInterface quantifiedPSM : census.getPSMMap().values()) {
			if (count++ % 500 == 0) {
				log.info(df.format(Double.valueOf(count) * 100 / census.getPSMMap().size()) + " % of PSMs...");
			}
			Set<String> taxonomies = getTaxonomies(quantifiedPSM);
			allTaxonomies.addAll(taxonomies);
			if (!quantifiedPSM.getRatios().isEmpty()) {
				addProteinsFromPSM(quantifiedPSM, proteinsContainingRatios);
			} else {
				addProteinsFromPSM(quantifiedPSM, proteinsNotContainingRatios);
			}

			if (taxonomies.size() > 1) {
				// both
				addProteinsFromPSM(quantifiedPSM, proteinsInBothSpecies);
			} else if (taxonomies.size() == 1) {
				String taxonomy = taxonomies.iterator().next();
				// just in taxonomy
				if (proteinsInOneSpecie.containsKey(taxonomy)) {
					addProteinsFromPSM(quantifiedPSM, proteinsInOneSpecie.get(taxonomy));
				} else {
					Map<String, QuantifiedProteinInterface> map = new HashMap<String, QuantifiedProteinInterface>();
					addProteinsFromPSM(quantifiedPSM, map);
					proteinsInOneSpecie.put(taxonomy, map);
				}
			} else {
				log.warn("This cannot happen");
			}

		}
		int numProteinsWithNoRatiosBefore = proteinsNotContainingRatios.size();
		log.info("Consolidating " + numProteinsWithNoRatiosBefore + " proteins with no ratios...");
		count = 0;
		final Iterator<String> iterator = proteinsNotContainingRatios.keySet().iterator();
		int total = proteinsNotContainingRatios.size();
		Set<String> keysForDeletion = new HashSet<String>();
		while (iterator.hasNext()) {
			if (count++ % 100 == 0) {
				log.info(df.format(Double.valueOf(count) * 100 / total) + " % of proteins with no ratios...");
			}
			String proteinKey = iterator.next();
			if (proteinsContainingRatios.containsKey(proteinKey)) {
				keysForDeletion.add(proteinKey);
			}
		}
		for (String proteinKey : keysForDeletion) {
			proteinsNotContainingRatios.remove(proteinKey);
		}

		log.info("Removed " + (numProteinsWithNoRatiosBefore - proteinsNotContainingRatios.size())
				+ " proteins from the list of proteins without ratios");

		log.info(census.getProteinMap().size() + " representative proteins quantified");
		log.info("Proteins in both species: " + proteinsInBothSpecies.size());
		for (String taxonomy : proteinsInOneSpecie.keySet()) {
			log.info("Proteins just in " + taxonomy + ": " + proteinsInOneSpecie.get(taxonomy).size());
		}
		log.info(proteinsNotContainingRatios.size() + " proteins with no ratios");
		log.info(proteinsContainingRatios.size() + " proteins containing ratios");
		log.info("Statistics done.");

	}

	private Set<String> getTaxonomies(QuantifiedPSMInterface quantifiedPSM) {

		final Set<String> taxonomies = quantifiedPSM.getTaxonomies();
		Set<String> ret = new HashSet<String>();
		for (String taxonomy : taxonomies) {
			final UniprotOrganism uniprotOrganism = UniprotSpeciesCodeMap.getInstance().get(taxonomy);
			if (uniprotOrganism != null) {
				ret.add(uniprotOrganism.getCode());
			} else {
				ret.add(taxonomy);
			}
		}
		return ret;

	}

	/**
	 * This function will add all the accessions of the proteins that the
	 * quantified psm belongs by using two different approaches depending on the
	 * constructor used for this class
	 *
	 * @param psm
	 * @param map
	 */
	private void addProteinsFromPSM(QuantifiedPSMInterface psm, Map<String, QuantifiedProteinInterface> map) {
		final Set<QuantifiedProteinInterface> quantifiedProteins = psm.getQuantifiedProteins();

		for (QuantifiedProteinInterface quantifiedProtein : quantifiedProteins) {
			map.put(quantifiedProtein.getKey(), quantifiedProtein);
		}

	}

	/**
	 * @return the proteinsInBothSpecies
	 */
	public Map<String, QuantifiedProteinInterface> getProteinsInBothSpecies() {
		return proteinsInBothSpecies;
	}

	/**
	 * @return the proteinsInOneSpecie
	 */
	public Map<String, Map<String, QuantifiedProteinInterface>> getProteinsInOneSpecie() {
		return proteinsInOneSpecie;
	}

	/**
	 * Gets the taxonomies
	 *
	 * @return
	 */
	public Set<String> getTaxonomies() {
		return allTaxonomies;
	}

	/**
	 * Prints a table with the number of protein identified in each tag (light
	 * or heavy) for each species.
	 *
	 * @return
	 */
	public String printProteinQuantificationTableBySpecies() {
		String separator = "\t\t\t";
		StringBuilder sb = new StringBuilder();
		StringBuilder allspecies = new StringBuilder();
		StringBuilder allLabels = new StringBuilder();

		final Set<String> species = getTaxonomies();
		List<String> sortedSpecies = new ArrayList<String>();
		sortedSpecies.addAll(species);
		Collections.sort(sortedSpecies);
		sb.append(separator);

		for (String specie : sortedSpecies) {
			sb.append(specie + separator);
			if (!"".equals(allspecies.toString()))
				allspecies.append("+");
			allspecies.append(specie);
		}
		sb.append(allspecies.toString() + "\n");
		final QuantificationLabel[] labels = QuantificationLabel.values();
		for (int i = 0; i < labels.length; i++) {
			final QuantificationLabel label = labels[i];
			if (!"".equals(allLabels.toString()))
				allLabels.append("+");
			allLabels.append(label);
			sb.append(label + separator);
			for (String specie : sortedSpecies) {
				final Set<String> proteinKeys = proteinsInOneSpecie.get(specie).keySet();
				int proteinsInSpecieWithLabel = 0;
				if (proteinKeys != null) {
					for (String proteinKey : proteinKeys) {
						final QuantifiedProteinInterface quantifiedProtein = census.getProteinMap().get(proteinKey);
						if (quantifiedProtein != null && CensusChroUtil.containsAnyIon(quantifiedProtein, label)
								&& !CensusChroUtil.containsAnyIonWithAnyOtherLabelThan(quantifiedProtein, label)) {
							proteinsInSpecieWithLabel++;
						}
					}
				}
				sb.append(proteinsInSpecieWithLabel + separator);
			}
			// proteins in both species
			int proteinsInBothSpecieWithLabel = 0;
			for (String proteinKey : proteinsInBothSpecies.keySet()) {
				QuantifiedProteinInterface quantifiedProtein = census.getProteinMap().get(proteinKey);
				if (quantifiedProtein != null && CensusChroUtil.containsAnyIon(quantifiedProtein, label)
						&& !CensusChroUtil.containsAnyIonWithAnyOtherLabelThan(quantifiedProtein, label)) {
					proteinsInBothSpecieWithLabel++;
				}
			}
			sb.append(proteinsInBothSpecieWithLabel + "\n");
		}
		sb.append(allLabels.toString() + separator);
		// all labels and one species
		for (String specie : sortedSpecies) {
			final Set<String> proteinKeys = proteinsInOneSpecie.get(specie).keySet();
			int numProteinsWithAllLabels = 0;
			if (proteinKeys != null) {
				for (String proteinKey : proteinKeys) {
					QuantifiedProteinInterface quantifiedProtein = census.getProteinMap().get(proteinKey);
					if (quantifiedProtein != null) {
						boolean found = true;
						for (QuantificationLabel label : QuantificationLabel.values()) {
							if (!CensusChroUtil.containsAnyIon(quantifiedProtein, label)) {
								found = false;
								break;
							}
						}
						if (found) {
							numProteinsWithAllLabels++;
						}
					}
				}
			}
			sb.append(numProteinsWithAllLabels + separator);
		}
		// both labels and both species
		int numProteinsWithAllLabels = 0;
		for (String proteinKey : proteinsInBothSpecies.keySet()) {
			QuantifiedProteinInterface quantifiedProtein = census.getProteinMap().get(proteinKey);
			if (quantifiedProtein != null) {
				boolean found = true;
				for (QuantificationLabel label : QuantificationLabel.values()) {
					if (!CensusChroUtil.containsAnyIon(quantifiedProtein, label)) {
						found = false;
						break;
					}
				}
				if (found) {
					numProteinsWithAllLabels++;
				}
			}
		}
		sb.append(numProteinsWithAllLabels + "\n");
		return sb.toString();
	}

	public List<ProteinGroup> getProteinGroups() {
		if (proteinGroups == null) {
			PAnalyzer pa = new PAnalyzer(false);
			List<GroupableProtein> set = new ArrayList<GroupableProtein>();
			set.addAll(census.getProteinMap().values());
			proteinGroups = pa.run(set);
			groupingStats = pa.getStats();
		}
		return proteinGroups;
	}

	/**
	 * @return the groupingStats
	 */
	public PanalyzerStats getGroupingStats() {
		if (groupingStats == null) {
			getProteinGroups();
		}
		return groupingStats;
	}

	/**
	 * @return the allTaxonomies
	 */
	public Set<String> getAllTaxonomies() {
		return allTaxonomies;
	}

	/**
	 * @return the proteinsContainingRatios
	 */
	public Map<String, QuantifiedProteinInterface> getProteinsContainingRatios() {
		return proteinsContainingRatios;
	}

	/**
	 * @return the proteinsNotContainingRatios
	 */
	public Map<String, QuantifiedProteinInterface> getProteinsNotContainingRatios() {
		return proteinsNotContainingRatios;
	}

	public String printFullReport() {
		StringBuilder sb = new StringBuilder();
		sb.append(printProteinQuantificationTableBySpecies() + "\n");
		sb.append(getProteinsContainingRatios().size() + " proteins containing at least one ratio in one peptide\n");
		sb.append(getProteinsNotContainingRatios().size() + " proteins not containing ratios in their peptides\n");
		sb.append(getGroupingStats().toString());
		return sb.toString();
	}
}
