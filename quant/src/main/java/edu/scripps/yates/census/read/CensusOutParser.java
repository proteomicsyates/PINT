package edu.scripps.yates.census.read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.analysis.util.KeyUtils;
import edu.scripps.yates.census.read.model.CensusRatio;
import edu.scripps.yates.census.read.model.QuantAmount;
import edu.scripps.yates.census.read.model.QuantifiedPSMFromCensusOut;
import edu.scripps.yates.census.read.model.QuantifiedPeptide;
import edu.scripps.yates.census.read.model.QuantifiedProteinFromCensusOut;
import edu.scripps.yates.census.read.model.QuantifiedProteinFromDBIndexEntry;
import edu.scripps.yates.census.read.model.RatioScore;
import edu.scripps.yates.census.read.model.StaticMaps;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedProteinInterface;
import edu.scripps.yates.census.read.model.interfaces.Ratio;
import edu.scripps.yates.census.read.util.QuantificationLabel;
import edu.scripps.yates.dbindex.IndexedProtein;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;
import edu.scripps.yates.utilities.model.enums.AmountType;
import edu.scripps.yates.utilities.remote.RemoteSSHFileReference;

public class CensusOutParser extends AbstractQuantParser {
	private static final Logger log = Logger.getLogger(CensusOutParser.class);

	private static final String H = "H";
	private static final String P = "P";
	private static final String S = "S";
	private static final String SINGLETON_S = "&S";
	private static final String PLINE = "PLINE";
	private static final String SLINE = "SLINE";
	private static final String SINGLETON_SLINE = "&SLINE";
	private static final String LOCUS = "LOCUS";
	private static final String UNIQUE = "UNIQUE";
	private static final String SEQUENCE = "SEQUENCE";
	private static final String FILENAME = "FILE_NAME";

	private static final String DESCRIPTION = "DESCRIPTION";

	// PLine columns
	private static final String AVERAGE_RATIO = "AVERAGE_RATIO";
	private static final String STANDARD_DEVIATION = "STANDARD_DEVIATION";
	private static final String COMPOSITE_RATIO = "COMPOSITE_RATIO";
	private static final String COMPOSITE_RATIO_STANDARD_DEVIATION = "COMPOSITE_RATIO_STANDARD_DEVIATION";
	// SLine columns
	private static final String RATIO = "RATIO";
	private static final String PVALUE = "PVALUE";
	private static final String PROBABILITY_SCORE = "PROBABILITY_SCORE";
	private static final String PROFILE_SCORE = "PROFILE_SCORE";
	private static final String AREA_RATIO = "AREA_RATIO";
	private static final String SAM_INT = "SAM_INT";
	private static final String REF_INT = "REF_INT";
	private static final String PEAK_INT = "PEAK_INT";
	// &SLine columns
	private static final String SINGLETON_SCORE = "SINGLETON_SCORE";

	private static final String CS = "CS";

	private static final String SCAN = "SCAN";

	public CensusOutParser() {
		super();
	}

	public CensusOutParser(List<RemoteSSHFileReference> remoteSSHServers,
			List<Map<QuantCondition, QuantificationLabel>> labelsByConditions, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator) {
		super(remoteSSHServers, labelsByConditions, labelNumerator, labelDenominator);
	}

	public CensusOutParser(Map<QuantCondition, QuantificationLabel> labelsByConditions,
			Collection<RemoteSSHFileReference> remoteSSHServers, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator) {
		super(labelsByConditions, remoteSSHServers, labelNumerator, labelDenominator);
	}

	public CensusOutParser(RemoteSSHFileReference remoteSSHServer,
			Map<QuantCondition, QuantificationLabel> labelsByConditions, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator) throws FileNotFoundException {
		super(remoteSSHServer, labelsByConditions, labelNumerator, labelDenominator);
	}

	public CensusOutParser(File xmlFile, Map<QuantCondition, QuantificationLabel> labelsByConditions,
			QuantificationLabel labelNumerator, QuantificationLabel labelDenominator) throws FileNotFoundException {
		super(xmlFile, labelsByConditions, labelNumerator, labelDenominator);
	}

	public CensusOutParser(File[] xmlFiles, Map<QuantCondition, QuantificationLabel> labelsByConditions,
			QuantificationLabel labelNumerator, QuantificationLabel labelDenominator) throws FileNotFoundException {
		super(xmlFiles, labelsByConditions, labelNumerator, labelDenominator);
	}

	public CensusOutParser(File[] xmlFiles, Map<QuantCondition, QuantificationLabel>[] labelsByConditions,
			QuantificationLabel[] labelNumerator, QuantificationLabel[] labelDenominator) throws FileNotFoundException {
		super(xmlFiles, labelsByConditions, labelNumerator, labelDenominator);
	}

	public CensusOutParser(Collection<File> xmlFiles, Map<QuantCondition, QuantificationLabel> labelsByConditions,
			QuantificationLabel labelNumerator, QuantificationLabel labelDenominator) throws FileNotFoundException {
		super(xmlFiles, labelsByConditions, labelNumerator, labelDenominator);
	}

	public CensusOutParser(RemoteSSHFileReference remoteServer, QuantificationLabel label1, QuantCondition cond1,
			QuantificationLabel label2, QuantCondition cond2) {
		super(remoteServer, label1, cond1, label2, cond2);
	}

	public CensusOutParser(File inputFile, QuantificationLabel label1, QuantCondition cond1, QuantificationLabel label2,
			QuantCondition cond2) throws FileNotFoundException {
		super(inputFile, label1, cond1, label2, cond2);
	}

	/**
	 *
	 * @param writeFiles
	 *            whether to write output files necessary to run SanXot program
	 */
	@Override
	protected void process() {
		processed = false;
		log.info("Processing file...");

		try {
			int numDecoy = 0;
			boolean someValidFile = false;
			for (RemoteSSHFileReference remoteFileRetriever : remoteFileRetrievers) {
				final Map<QuantCondition, QuantificationLabel> labelsByConditions = labelsByConditionsByFile
						.get(remoteFileRetriever);
				final Map<QuantificationLabel, QuantCondition> conditionsByLabels = new HashMap<QuantificationLabel, QuantCondition>();
				for (QuantCondition cond : labelsByConditions.keySet()) {
					conditionsByLabels.put(labelsByConditions.get(cond), cond);
				}
				QuantificationLabel labelNumerator = numeratorLabelByFile.get(remoteFileRetriever);
				QuantificationLabel labelDenominator = denominatorLabelByFile.get(remoteFileRetriever);

				String experimentKey = FilenameUtils.getBaseName(remoteFileRetriever.getOutputFile().getAbsolutePath());
				log.info(experimentKey);
				// get all the Quantified PSMs first
				Set<QuantifiedPSMInterface> psms = new HashSet<QuantifiedPSMInterface>();
				final File remoteFile = remoteFileRetriever.getRemoteFile();
				if (remoteFile == null || !remoteFile.exists())
					continue;
				log.info("Reading " + remoteFile.getAbsolutePath());
				someValidFile = true;
				BufferedReader br = null;
				try {
					br = new BufferedReader(new FileReader(remoteFile));

					String line;
					List<String> pLineHeaderList = new ArrayList<String>();
					List<String> sLineHeaderList = new ArrayList<String>();
					List<String> singletonSLineHeaderList = new ArrayList<String>();
					Set<QuantifiedProteinFromCensusOut> proteinGroup = new HashSet<QuantifiedProteinFromCensusOut>();
					boolean itWasPeptides = false;
					while ((line = br.readLine()) != null) {
						if (line.startsWith(H)) {
							if (line.contains("\t")) {
								final String[] split = line.split("\t");
								// if second element is PLINE
								if (split[1].equals(PLINE) && split[2].equals(LOCUS)) {
									for (int i = 1; i < split.length; i++) {
										pLineHeaderList.add(split[i]);
									}
								} else // if second element is SLINE
								if (split[1].equals(SLINE) && split[2].equals(UNIQUE)) {
									for (int i = 1; i < split.length; i++) {
										sLineHeaderList.add(split[i]);
									}
								} else // if second element is &SLINE
								if (split[1].equals(SINGLETON_SLINE) && split[2].equals(UNIQUE)) {
									for (int i = 1; i < split.length; i++) {
										if (split[i].equals(PVALUE)) {
											continue; // SINGLETONG SLINE DOESNT
														// HAVE PVALUES!
										}
										singletonSLineHeaderList.add(split[i]);
									}
								}
							}
						} else if (line.startsWith(P)) {
							try {
								if (itWasPeptides) {
									proteinGroup.clear();
								}
								itWasPeptides = false;
								QuantifiedProteinFromCensusOut quantifiedProtein = processProteinLine(line,
										pLineHeaderList, conditionsByLabels, labelNumerator, labelDenominator,
										numDecoy);

								proteinGroup.add(quantifiedProtein);
							} catch (DiscardProteinException e) {
								continue;
							} catch (IllegalArgumentException e) {
								log.error(e);
							}
						} else if (line.startsWith(S)) {
							itWasPeptides = true;
							// if there is not protein is because it was
							// discarded by the decoy pattern, so ignore any psm
							if (proteinGroup.isEmpty()) {
								continue;
							}
							try {
								processPSMLine(line, sLineHeaderList, proteinGroup, psms, conditionsByLabels,
										labelsByConditions, labelNumerator, labelDenominator, experimentKey,
										remoteFileRetriever, false);
							} catch (IllegalArgumentException e) {
								log.error(e);
							}
						} else if (line.startsWith(SINGLETON_S)) {
							itWasPeptides = true;
							// if there is not protein is because it was
							// discarded by the decoy pattern, so ignore any psm
							if (proteinGroup.isEmpty()) {
								continue;
							}
							try {
								processPSMLine(line, singletonSLineHeaderList, proteinGroup, psms, conditionsByLabels,
										labelsByConditions, labelNumerator, labelDenominator, experimentKey,
										remoteFileRetriever, true);
							} catch (IllegalArgumentException e) {
								log.error(e);
							}
						}

					}

					br.close();

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (br != null) {
						br.close();
					}
				}

				log.info("(" + experimentKey + ") " + psms.size() + " PSMs from this parser. "
						+ StaticMaps.psmMap.size() + " PSMs in the system");
				log.info("(" + experimentKey + ") " + localProteinMap.size() + " Proteins from this parser. "
						+ StaticMaps.proteinMap.size() + " Proteins in the system");
				log.info("(" + experimentKey + ") " + localPeptideMap.size() + " Peptides from this parser. "
						+ StaticMaps.peptideMap.size() + " Peptides in the system");
				if (decoyPattern != null) {
					log.info(numDecoy + " decoy Proteins were discarded  in " + experimentKey);
				}
			}
			if (!someValidFile)
				throw new IllegalArgumentException("some error occurred while reading the files");

			processed = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// if (processed) {
			// // to create the peptides at the end
			// peptideMap.clear();
			// peptideMap.putAll(
			// QuantifiedPeptide.getQuantifiedPeptides(getPSMMap().values(),
			// distinguishModifiedPeptides));
			// }
		}
	}

	private void processPSMLine(String line, List<String> sLineHeaderList,
			Set<QuantifiedProteinFromCensusOut> quantifiedProteins, Set<QuantifiedPSMInterface> psms,
			Map<QuantificationLabel, QuantCondition> conditionsByLabels,
			Map<QuantCondition, QuantificationLabel> labelsByConditions, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator, String experimentKey, RemoteSSHFileReference remoteFileRetriever,
			boolean singleton) throws IOException {
		// new psm
		Map<String, String> mapValues = getMapFromSLine(sLineHeaderList, line);
		String sequence = mapValues.get(SEQUENCE);

		// dont look into the QuantifiedPSM.map because each
		// line is always a new PSM
		String fileName = FilenameUtils.getName(remoteFileRetriever.getOutputFile().getAbsolutePath());
		if (mapValues.containsKey(FILENAME)) {
			fileName = mapValues.get(FILENAME);
		}
		// scan number
		int scanNumber = 0;
		if (mapValues.containsKey(SCAN)) {
			scanNumber = Double.valueOf(mapValues.get(SCAN)).intValue();
		}
		QuantifiedPSMFromCensusOut quantifiedPSM = new QuantifiedPSMFromCensusOut(sequence, labelsByConditions,
				peptideToSpectraMap, scanNumber, Double.valueOf(mapValues.get(CS)).intValue(), chargeStateSensible,
				fileName);

		psms.add(quantifiedPSM);
		// add to map
		if (!localPsmMap.containsKey(quantifiedPSM.getKey())) {
			localPsmMap.put(quantifiedPSM.getKey(), quantifiedPSM);
		}
		// PSM regular ratio
		// add PSM ratios from census out
		if (mapValues.containsKey(RATIO)) {
			try {

				final double ratioValue = Double.valueOf(mapValues.get(RATIO));
				CensusRatio ratio = new CensusRatio(ratioValue, false, conditionsByLabels, labelNumerator,
						labelDenominator, AggregationLevel.PSM, RATIO);
				// profile score
				String scoreValue = null;
				if (mapValues.containsKey(PROFILE_SCORE) &&
				// because profile score will be
				// assigned to area ratio in case of
				// exist
						!mapValues.containsKey(AREA_RATIO)) {
					scoreValue = mapValues.get(PROFILE_SCORE);
					if (!"NA".equals(scoreValue)) {
						RatioScore ratioScore = new RatioScore(scoreValue, PROFILE_SCORE,
								"PSM-level quantification confidence metric",
								"fitting score comparing peak and gaussian distribution");
						ratio.setRatioScore(ratioScore);
					}
					// score regression p-value N15
				} else if (mapValues.containsKey(PVALUE)) {
					scoreValue = mapValues.get(PVALUE);
					if (!"NA".equals(scoreValue)) {
						RatioScore ratioScore = new RatioScore(scoreValue, PVALUE, "PSM-level p-value",
								"probability score based on LR");
						ratio.setRatioScore(ratioScore);
					}
					// score regression p-value SILAC
				} else if (mapValues.containsKey(PROBABILITY_SCORE)) {
					scoreValue = mapValues.get(PROBABILITY_SCORE);
					if (!"NA".equals(scoreValue)) {
						RatioScore ratioScore = new RatioScore(scoreValue, PROBABILITY_SCORE, "PSM-level p-value",
								"probability score based on LR");
						ratio.setRatioScore(ratioScore);
					}
				}
				// add ratio to PSM
				quantifiedPSM.addRatio(ratio);
			} catch (NumberFormatException e) {
				// skip this
			}
		}

		// PSM area ratio
		// add PSM ratios from census out
		if (mapValues.containsKey(AREA_RATIO)) {
			try {
				final double ratioValue = Double.valueOf(mapValues.get(AREA_RATIO));
				CensusRatio ratio = new CensusRatio(ratioValue, false, conditionsByLabels, labelNumerator,
						labelDenominator, AggregationLevel.PSM, AREA_RATIO);
				// profile score
				if (mapValues.containsKey(PROFILE_SCORE)) {
					String scoreValue = mapValues.get(PROFILE_SCORE);
					if (!"NA".equals(scoreValue)) {
						RatioScore ratioScore = new RatioScore(scoreValue, PROFILE_SCORE,
								"PSM-level quantification confidence metric",
								"fitting score comparing peak and gaussian distribution");
						ratio.setRatioScore(ratioScore);
					}
				} else if (mapValues.containsKey(SINGLETON_SCORE)) {
					String scoreValue = mapValues.get(SINGLETON_SCORE);
					if (!"NA".equals(scoreValue)) {
						RatioScore ratioScore = new RatioScore(scoreValue, SINGLETON_SCORE,
								"PSM-level quantification confidence metric", "Singleton score");
						ratio.setRatioScore(ratioScore);
					}
				}
				// add ratio to PSM
				quantifiedPSM.addRatio(ratio);
			} catch (NumberFormatException e) {
				// skip this
			}
		}

		// PSM amounts
		QuantAmount light = null;
		QuantAmount heavy = null;
		// SAM_INT
		// light peptide peak area from reconstructed
		// chromatogram
		if (mapValues.containsKey(SAM_INT)) {
			try {
				final double value = Double.valueOf(mapValues.get(SAM_INT));
				light = new QuantAmount(value, AmountType.AREA, conditionsByLabels.get(QuantificationLabel.LIGHT));
				// add amount to PSM
				quantifiedPSM.addAmount(light);
			} catch (NumberFormatException e) {
				// skip this
			}
		}
		// REF_INT
		// heavy peptide peak area from reconstructed
		// chromatogram
		if (mapValues.containsKey(REF_INT)) {
			try {
				final double value = Double.valueOf(mapValues.get(REF_INT));
				heavy = new QuantAmount(value, AmountType.AREA, conditionsByLabels.get(QuantificationLabel.HEAVY));
				// add amount to PSM
				quantifiedPSM.addAmount(heavy);
			} catch (NumberFormatException e) {
				// skip this
			}
		}
		if (singleton && light != null && heavy != null) {
			if (light.getValue() == 0.0) {
				heavy.setSingleton(true);
			}
			if (heavy.getValue() == 0.0) {
				light.setSingleton(true);
			}
		}
		// REGRESSION_FACTOR
		// regression score (r)
		if (mapValues.containsKey(AmountType.REGRESSION_FACTOR.name())) {
			try {
				final double value = Double.valueOf(mapValues.get(AmountType.REGRESSION_FACTOR.name()));
				QuantAmount amount = new QuantAmount(value, AmountType.REGRESSION_FACTOR,
						conditionsByLabels.get(QuantificationLabel.LIGHT));
				// add amount to PSM
				quantifiedPSM.addAmount(amount);
			} catch (NumberFormatException e) {
				// skip this
			}
		}
		// create the peptide
		QuantifiedPeptide quantifiedPeptide = null;
		final String peptideKey = KeyUtils.getSequenceChargeKey(quantifiedPSM, chargeStateSensible);
		if (StaticMaps.peptideMap.containsKey(peptideKey)) {
			quantifiedPeptide = StaticMaps.peptideMap.getItem(peptideKey);
		} else {
			quantifiedPeptide = new QuantifiedPeptide(quantifiedPSM, distinguishModifiedPeptides);
		}

		quantifiedPSM.setQuantifiedPeptide(quantifiedPeptide);
		// add peptide to map
		if (!localPeptideMap.containsKey(peptideKey)) {
			localPeptideMap.put(peptideKey, quantifiedPeptide);
		}

		if (dbIndex != null) {
			String cleanSeq = quantifiedPSM.getSequence();
			final Set<IndexedProtein> indexedProteins = dbIndex.getProteins(cleanSeq);
			if (indexedProteins.isEmpty()) {
				throw new IllegalArgumentException("The peptide " + cleanSeq
						+ " is not found in Fasta DB.\nReview the default indexing parameters such as the number of allowed misscleavages.");
				// log.warn("The peptide " + cleanSeq +
				// " is not found in Fasta DB.");
				// continue;
			}
			// create a new Quantified Protein for each
			// indexedProtein
			for (IndexedProtein indexedProtein : indexedProteins) {
				String proteinKey = KeyUtils.getProteinKey(indexedProtein);
				QuantifiedProteinInterface newQuantifiedProtein = null;
				if (StaticMaps.proteinMap.containsKey(proteinKey)) {
					newQuantifiedProtein = StaticMaps.proteinMap.getItem(proteinKey);

				} else {
					newQuantifiedProtein = new QuantifiedProteinFromDBIndexEntry(indexedProtein);

				}

				// add protein to protein map
				taxonomies.add(newQuantifiedProtein.getTaxonomy());
				localProteinMap.put(proteinKey, newQuantifiedProtein);
				// add to protein-experiment map
				addToMap(experimentKey, experimentToProteinsMap, proteinKey);
				// add psm to the protein
				newQuantifiedProtein.addPSM(quantifiedPSM);
				// add peptide to the protein
				newQuantifiedProtein.addPeptide(quantifiedPeptide);
				// add protein to the psm
				quantifiedPSM.addQuantifiedProtein(newQuantifiedProtein);
				// add to the map (if it was already
				// there is not a problem, it will be
				// only once)
				addToMap(proteinKey, proteinToPeptidesMap,
						KeyUtils.getSequenceChargeKey(quantifiedPSM, chargeStateSensible));

			}
		}
		// use the already created quantified
		// protein

		for (QuantifiedProteinFromCensusOut quantifiedProtein : quantifiedProteins) {
			// add psm to the proteins
			quantifiedProtein.addPSM(quantifiedPSM);
			// add protein to the psm
			quantifiedPSM.addQuantifiedProtein(quantifiedProtein);
			// add to the map (if it was already there
			// is not a problem, it will be only once)
			String proteinKey = quantifiedProtein.getKey();
			addToMap(proteinKey, proteinToPeptidesMap,
					KeyUtils.getSequenceChargeKey(quantifiedPSM, chargeStateSensible));
			// add protein to protein map
			localProteinMap.put(proteinKey, quantifiedProtein);
			// add to protein-experiment map
			addToMap(experimentKey, experimentToProteinsMap, proteinKey);
		}

	}

	private QuantifiedProteinFromCensusOut processProteinLine(String line, List<String> pLineHeaderList,
			Map<QuantificationLabel, QuantCondition> conditionsByLabels, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator, int numDecoy) throws DiscardProteinException {
		// new protein
		Map<String, String> mapValues = getMapFromPLine(pLineHeaderList, line);
		String proteinACC = mapValues.get(LOCUS);

		QuantifiedProteinFromCensusOut quantifiedProtein = null;
		if (StaticMaps.proteinMap.containsKey(proteinACC)) {
			quantifiedProtein = (QuantifiedProteinFromCensusOut) StaticMaps.proteinMap.getItem(proteinACC);
		} else {
			quantifiedProtein = new QuantifiedProteinFromCensusOut(proteinACC);
			String description = mapValues.get(DESCRIPTION);
			quantifiedProtein.setDescription(description);
		}

		// apply the pattern if available
		if (decoyPattern != null) {
			final Matcher matcher = decoyPattern.matcher(proteinACC);
			if (matcher.find()) {
				quantifiedProtein = null;
				numDecoy++;
				throw new DiscardProteinException("Protein " + proteinACC + " is DECOY.");
			}
		}
		// add protein ratio
		// first look if the composite ratio is calculated
		if (mapValues.containsKey(COMPOSITE_RATIO)) {
			try {
				final double ratioValue = Double.valueOf(mapValues.get(COMPOSITE_RATIO));
				String stdValue = null;
				if (mapValues.containsKey(COMPOSITE_RATIO_STANDARD_DEVIATION)) {
					stdValue = mapValues.get(COMPOSITE_RATIO_STANDARD_DEVIATION);
					if ("".equals(stdValue)) {
						stdValue = "0.0";
					}
				}
				Ratio ratio = new CensusRatio(ratioValue, stdValue, false, conditionsByLabels, labelNumerator,
						labelDenominator, AggregationLevel.PROTEIN, COMPOSITE_RATIO);
				quantifiedProtein.addRatio(ratio);
			} catch (NumberFormatException e) {
				// skip this
			}
			// if there is not composite ratio, use the
			// regular ratio
		} else if (mapValues.containsKey(AVERAGE_RATIO) || mapValues.containsKey(AREA_RATIO)) {
			try {
				if (mapValues.containsKey(AVERAGE_RATIO)) {
					final double ratioValue = Double.valueOf(mapValues.get(AVERAGE_RATIO));
					String stdValue = null;
					if (mapValues.containsKey(STANDARD_DEVIATION)) {
						stdValue = mapValues.get(STANDARD_DEVIATION);
						if ("".equals(stdValue)) {
							stdValue = "0.0";
						}
					}
					Ratio ratio = new CensusRatio(ratioValue, stdValue, false, conditionsByLabels, labelNumerator,
							labelDenominator, AggregationLevel.PROTEIN, "AVG_RATIO");
					quantifiedProtein.addRatio(ratio);
				}
			} catch (NumberFormatException e) {
				// skip this
			}
			try {
				if (mapValues.containsKey(AREA_RATIO)) {
					final double ratioValue = Double.valueOf(mapValues.get(AREA_RATIO));
					Ratio ratio = new CensusRatio(ratioValue, null, false, conditionsByLabels, labelNumerator,
							labelDenominator, AggregationLevel.PROTEIN, AREA_RATIO);
					quantifiedProtein.addRatio(ratio);
				}
			} catch (NumberFormatException e) {
				// skip this
			}
		}
		return quantifiedProtein;

	}

	private Map<String, String> getMapFromPLine(List<String> pLineHeaderList, String line) {
		Map<String, String> map = new HashMap<String, String>();
		String[] split = line.split("\t");
		if (split.length != pLineHeaderList.size()) {
			// try to see if there is one that is empty
			String[] splitTMP = new String[split.length - 1];
			if (split[5].equals("")) {

				for (int i = 0; i < split.length - 1; i++) {
					if (i < 5) {
						splitTMP[i] = split[i];
					} else {
						splitTMP[i] = split[i + 1];
					}
				}
				split = splitTMP;
			}
		}
		if (split.length == pLineHeaderList.size()) {
			int i = 1;
			for (String header : pLineHeaderList) {
				if (header.equals(PLINE)) {
					continue;
				}
				map.put(header, split[i]);
				i++;
			}
		} else {
			throw new IllegalArgumentException(
					line + " has different number of columns than the header which have " + pLineHeaderList.size());
		}
		return map;
	}

	private Map<String, String> getMapFromSLine(List<String> sLineHeaderList, String line) {
		Map<String, String> map = new HashMap<String, String>();
		String[] split = line.split("\t");
		split = removeElements(split, "N/A");

		// trying to recover some lines in which the peptide information is
		// shifted to the right one column from the sequence
		if ("".equals(split[1]) && "".equals(split[2]) && split.length == sLineHeaderList.size() + 1) {
			// copy into a new array
			String[] newSplit = new String[sLineHeaderList.size()];
			for (int index = 0; index < newSplit.length; index++) {
				if (index <= 1) {
					newSplit[index] = split[index];
				} else {
					newSplit[index] = split[index + 1];
				}
			}
			split = newSplit;
		}
		if (split.length == sLineHeaderList.size()) {
			int i = 0;
			for (String header : sLineHeaderList) {
				map.put(header, split[i]);
				i++;
			}
		} else {
			throw new IllegalArgumentException(line + " has different number of columns than the header which have "
					+ sLineHeaderList.size() + ". LINE HAS " + split.length);
		}
		return map;
	}

	private String[] removeElements(String[] split, String elementToRemove) {
		List<String> list = new ArrayList<String>();
		for (String splitElement : split) {
			if (splitElement.equals(elementToRemove)) {
				continue;
			}
			list.add(splitElement);
		}
		return list.toArray(new String[0]);
	}

}
