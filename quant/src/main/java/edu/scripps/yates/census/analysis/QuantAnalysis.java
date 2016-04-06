package edu.scripps.yates.census.analysis;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

import edu.scripps.yates.census.analysis.clustering.ProteinCluster;
import edu.scripps.yates.census.analysis.clustering.ProteinClusterUtils;
import edu.scripps.yates.census.analysis.util.KeyUtils;
import edu.scripps.yates.census.analysis.wrappers.IntegrationResultWrapper;
import edu.scripps.yates.census.analysis.wrappers.OutStatsLine;
import edu.scripps.yates.census.analysis.wrappers.SanXotAnalysisResult;
import edu.scripps.yates.census.read.CensusChroParser;
import edu.scripps.yates.census.read.CensusOutParser;
import edu.scripps.yates.census.read.model.IonSerie.IonSerieType;
import edu.scripps.yates.census.read.model.IsoRatio;
import edu.scripps.yates.census.read.model.IsobaricQuantifiedPSM;
import edu.scripps.yates.census.read.model.IsobaricQuantifiedPeptide;
import edu.scripps.yates.census.read.model.interfaces.IsobaricQuantParser;
import edu.scripps.yates.census.read.model.interfaces.QuantParser;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPeptideInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedProteinInterface;
import edu.scripps.yates.census.read.util.IonExclusion;
import edu.scripps.yates.census.read.util.QuantificationLabel;
import edu.scripps.yates.dbindex.DBIndexInterface;
import edu.scripps.yates.dbindex.io.DBIndexSearchParams;
import edu.scripps.yates.utilities.alignment.nwalign.NWResult;
import edu.scripps.yates.utilities.grouping.GroupablePSM;
import edu.scripps.yates.utilities.grouping.GroupableProtein;
import edu.scripps.yates.utilities.grouping.PAnalyzer;
import edu.scripps.yates.utilities.grouping.ProteinGroup;

public class QuantAnalysis implements PropertyChangeListener {
	private static final Logger log = Logger.getLogger(QuantAnalysis.class);
	private boolean overrideFilesIfExists = true;
	private final List<QuantExperiment> quantExperiments = new ArrayList<QuantExperiment>();
	private final File workingFolder;
	private final String NL = System.getProperty("line.separator");
	private final QuantCondition condition1;
	private final QuantCondition condition2;
	private FileMappingResults fileMappingResults;
	private final QuantParameters quantParameters = new QuantParameters();
	private final Map<String, List<String>> replicateAndExperimentNames = new HashMap<String, List<String>>();
	private DBIndexInterface dbIndex;

	private static final String QUANT_FOLDER = "quant";
	private SanXotAnalysisResult result;
	private final ANALYSIS_LEVEL_OUTCOME analysisOutCome;

	private Long timeout = null;

	private boolean chargeStateSensible;

	private int minAlignmentScore;

	private double minPercentajeOfsmilirarity;

	private int minConsecutiveLength;
	private Set<Set<String>> proteinAccClusters;
	private final QuantificationType quantType;

	public static enum ANALYSIS_LEVEL_OUTCOME {
		PEPTIDE, PROTEIN, PROTEINGROUP, PROTEIN_CLUSTER, FORCED_CLUSTERS
	};

	/**
	 * Quantification analysis at protein group level
	 *
	 * @param workingPath
	 * @param condition1
	 * @param condition2
	 */
	public QuantAnalysis(QuantificationType quantType, String workingPath, QuantCondition condition1,
			QuantCondition condition2) {
		this.condition1 = condition1;
		this.condition2 = condition2;
		analysisOutCome = ANALYSIS_LEVEL_OUTCOME.PROTEINGROUP;
		workingFolder = createWorkingFolder(workingPath, analysisOutCome);
		this.quantType = quantType;
	}

	/**
	 * Quantification analysis at protein group level
	 *
	 * @param workingFolder
	 * @param condition1
	 * @param condition2
	 * @param analysisOutCome
	 */
	public QuantAnalysis(QuantificationType quantType, File workingFolder, QuantCondition condition1,
			QuantCondition condition2) {
		this.condition1 = condition1;
		this.condition2 = condition2;
		analysisOutCome = ANALYSIS_LEVEL_OUTCOME.PROTEINGROUP;
		this.workingFolder = createWorkingFolder(workingFolder, analysisOutCome);
		this.quantType = quantType;
	}

	/**
	 * Quantification analysis specifying the outcome level of the analysis
	 *
	 * @param workingPath
	 * @param condition1
	 * @param condition2
	 */
	public QuantAnalysis(QuantificationType quantType, String workingPath, QuantCondition condition1,
			QuantCondition condition2, ANALYSIS_LEVEL_OUTCOME analysisOutCome) {
		this.condition1 = condition1;
		this.condition2 = condition2;
		this.analysisOutCome = analysisOutCome;

		workingFolder = createWorkingFolder(workingPath, analysisOutCome);
		this.quantType = quantType;
	}

	/**
	 * Quantification analysis specifying the outcome level of the analysis
	 *
	 * @param workingFolder
	 * @param condition1
	 * @param condition2
	 * @param analysisOutCome
	 */
	public QuantAnalysis(QuantificationType quantType, File workingFolder, QuantCondition condition1,
			QuantCondition condition2, ANALYSIS_LEVEL_OUTCOME analysisOutCome) {
		this.condition1 = condition1;
		this.condition2 = condition2;
		this.analysisOutCome = analysisOutCome;
		this.workingFolder = createWorkingFolder(workingFolder, analysisOutCome);
		this.quantType = quantType;
	}

	private File createWorkingFolder(String workingPath, ANALYSIS_LEVEL_OUTCOME outcome) {
		File folder = new File(workingPath + File.separator + QUANT_FOLDER + File.separator + outcome.name());
		if (!folder.exists())
			folder.mkdirs();
		return folder;
	}

	private File createWorkingFolder(File workingFolder, ANALYSIS_LEVEL_OUTCOME outcome) {
		File folder = new File(
				workingFolder.getAbsolutePath() + File.separator + QUANT_FOLDER + File.separator + outcome.name());
		if (!folder.exists())
			folder.mkdirs();
		return folder;
	}

	public void addQuantExperiment(QuantExperiment exp) {

		quantExperiments.add(exp);
	}

	/**
	 * @return the quantExperiments
	 */
	public List<QuantExperiment> getQuantExperiments() {
		return quantExperiments;
	}

	/**
	 * @return the workingPath
	 */
	public File getWorkingPath() {
		return workingFolder;
	}

	public void runSanXot() throws IOException {

		// clear static data from censusParsers
		log.info("Clearing static data from parser");
		CensusOutParser.clearStaticInfo();
		CensusChroParser.clearStaticInfo();

		// set chargeStateSesible to all experiments and replicates
		setChargeStateSensible(chargeStateSensible);

		if (fileMappingResults == null) {
			writeFiles();
		}
		SanXotInterfaze sanxot = new SanXotInterfaze(fileMappingResults, quantParameters);
		if (timeout != null) {
			sanxot.setTimeout(timeout);
		}
		// sanxot.addPropertyChangeListener(this);
		// sanxot.execute();
		try {
			sanxot.analyze();
			result = sanxot.getResult();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	public FileMappingResults writeFiles() throws IOException {
		// assign db index to replicates (if exists)
		assignDBIndexToRuns();
		// read experiment and replicate names and stores at
		// replicateAndExperimentNames Map
		readExperimentAndReplicateNames();
		// write data file, which is the one with the ratios of the lower level
		writeDataFile();

		switch (analysisOutCome) {
		case PROTEINGROUP:
			writeRelationshipsFilesForProteinGroupOutcome();
			break;
		case PROTEIN:
			writeRelationshipsFilesForProteinOutcome();
			break;
		case FORCED_CLUSTERS:
			writeRelationshipsFilesForForcedProteinClustersOutcome();
			break;
		case PEPTIDE:
			writeRelationshipsFilesForPeptideOutcome();
			break;
		case PROTEIN_CLUSTER:
			if (minAlignmentScore == 0) {
				throw new IllegalArgumentException(
						"Minimum alignment score is needed. call to setMinAligmentScore(int min)");
			}
			if (Double.compare(minPercentajeOfsmilirarity, 0.0) == 0) {
				throw new IllegalArgumentException(
						"Minimum percentage of similarity is needed. call to setMinPercentajeOfsmilirarity(double min)");
			}
			if (minConsecutiveLength == 0) {
				throw new IllegalArgumentException(
						"Minimum consecutive length is needed. call to setMinConsecutiveLength(int consecutiveLength)");
			}
			writeRelationshipsFilesForProteinClustersOutcome(minAlignmentScore, minPercentajeOfsmilirarity,
					minConsecutiveLength);
		default:
			break;
		}

		fileMappingResults = new FileMappingResults(quantType, workingFolder, analysisOutCome,
				replicateAndExperimentNames);
		return fileMappingResults;
	}

	/**
	 * Iterates over the list of experiments and save the names of the
	 * experiments and replicates for its further use.
	 */
	private void readExperimentAndReplicateNames() {
		for (QuantExperiment exp : quantExperiments) {
			final String experimentName = exp.getName();
			if (!replicateAndExperimentNames.containsKey(experimentName)) {
				replicateAndExperimentNames.put(experimentName, new ArrayList<String>());
			}
			for (QuantReplicate rep : exp.getReplicates()) {
				String replicateName = rep.getName();
				replicateAndExperimentNames.get(experimentName).add(replicateName);
			}
		}
	}

	private void assignDBIndexToRuns() {
		if (dbIndex != null) {
			log.info("Setting index to all replicates in the analysis");
			for (QuantExperiment quantExperiment : quantExperiments) {
				final List<QuantReplicate> replicates = quantExperiment.getReplicates();
				for (QuantReplicate quantReplicate : replicates) {
					final QuantParser parser = quantReplicate.getParser();

					if (parser instanceof IsobaricQuantParser) {
						// by default, remove B1 and Y1
						Set<IonExclusion> ionExclusions = new HashSet<IonExclusion>();
						ionExclusions.add(new IonExclusion(IonSerieType.B, 1));
						ionExclusions.add(new IonExclusion(IonSerieType.Y, 1));
						((IsobaricQuantParser) parser).addIonExclusions(ionExclusions);
					}
					parser.setDbIndex(dbIndex);
				}
			}
		}
	}

	private void writeDataFile() throws IOException {

		FileWriter dataFileWriter = new FileWriter(
				getWorkingPath().getAbsolutePath() + File.separator + FileMappingResults.DATA_FILE);
		dataFileWriter.write("id\tX\tVcal" + NL);

		try {
			for (QuantExperiment exp : quantExperiments) {
				String expName = "_" + exp.getName();
				if (quantExperiments.size() == 1)
					expName = "";
				for (QuantReplicate rep : exp.getReplicates()) {
					String repName = rep.getName();
					if (exp.getReplicates().size() == 1) {
						if (!"".equals(expName))
							repName = "_" + expName;
						else
							repName = "";
					} else {
						repName = "_" + repName + expName;
					}
					final QuantParser parser = rep.getParser();
					final Collection<QuantifiedPSMInterface> quantifiedPSMs = parser.getPSMMap().values();
					for (QuantifiedPSMInterface quantifiedPSM : quantifiedPSMs) {
						if (quantifiedPSM instanceof IsobaricQuantifiedPSM) {
							IsobaricQuantifiedPSM isoPSM = (IsobaricQuantifiedPSM) quantifiedPSM;
							final Set<IsoRatio> ratios = isoPSM.getNonInfinityIsoRatios();
							for (IsoRatio ratio : ratios) {
								double ratioValue = ratio.getLog2Ratio(condition1, condition2);

								String ionKey = KeyUtils.getIonKey(ratio, isoPSM, chargeStateSensible) + repName;
								Double fittingWeight = null;

								switch (quantType) {
								case ISOTOPOLOGUES:
									fittingWeight = (ratio.getMaxIntensity())
											/ Math.sqrt(ratio.getMass(QuantificationLabel.LIGHT));
									break;
								case iTRAQ:
									fittingWeight = isoPSM.getMaxPeak();
									break;
								case TMT:
									fittingWeight = isoPSM.getMaxPeak();
									break;
								default:
									break;
								}
								// TODO
								// double qualityMeasurement =
								// ratio.getMaxPeak() *
								// ratio.getMaxPeak()
								// / (ratio.getIon1().getMass() *
								// ratio.getIon1().getMass());

								dataFileWriter.write(ionKey + "\t" + ratioValue + "\t" + fittingWeight + "\n");
							}
						} else {
							throw new IllegalArgumentException(
									"Other psm not isobaricquantifiedPSM is not supported yet!");
						}
					}

				}
			}
		} finally {
			if (dataFileWriter != null) {
				dataFileWriter.close();
			}
		}
	}

	private void writeRelationshipsFilesForProteinGroupOutcome() throws IOException {
		writeIonToSpectrumMap();
		writeSpectrumToPeptideExperimentReplicateMap();
		writePeptideExperimentReplicateToProteinGroupExperimentReplicateMap();
		try {
			// writeProteinExperimentReplicateToProteinExperimentMap();
			writeProteinGroupExperimentReplicateToProteinGroupExperimentMap();
			try {
				// writeProteinExperimentToProteinMap();
				writeProteinGroupExperimentToProteinGroupMap();
			} catch (IllegalArgumentException e) {
				log.info(e.getMessage());
				// "There is not more than 1 experiment"
				// do nothing and perform the last step: proteinToAll
			}

		} catch (IllegalArgumentException e) {
			log.info(e.getMessage());
			// "There is not any experiment with some replicates"
			try {
				// writeProteinExperimentToProteinMap();
				writeProteinGroupExperimentReplicateToProteinGroupExperimentMap();
				writeProteinGroupExperimentToProteinGroupMap();
			} catch (IllegalArgumentException e2) {
				log.info(e2.getMessage());
				// "There is not more than 1 experiment"
				// do nothing and perform the last step: proteinToAll
			}
		}

		// writeProteinToAllMap();

		writeProteinGroupToAllMap();

	}

	private void writeRelationshipsFilesForForcedProteinClustersOutcome() throws IOException {
		writeIonToSpectrumMap();

		writeSpectrumToPeptideExperimentReplicateMap();
		try {
			writePeptideExperimentReplicateToPeptideExperimentMap();
			try {
				writePeptideExperimentToPeptideMap();
			} catch (IllegalArgumentException e) {
				log.info(e.getMessage());
				// "There is not more than 1 experiment"
				// do nothing and perform the last step: proteinToAll
			}

		} catch (IllegalArgumentException e) {
			log.info(e.getMessage());
			// "There is not any experiment with some replicates"
			try {
				writePeptideExperimentToPeptideMap();
			} catch (IllegalArgumentException e2) {
				log.info(e2.getMessage());
				// "There is not more than 1 experiment"
				// do nothing and perform the last step: proteinToAll
			}
		}
		final Set<ProteinCluster> proteinClusterMap = writePeptideToForcedProteinClusterMap(proteinAccClusters);
		writeProteinClusterToAllMap(minAlignmentScore, minPercentajeOfsmilirarity, minConsecutiveLength,
				proteinClusterMap);

	}

	private void writeRelationshipsFilesForProteinClustersOutcome(int minAlignmentScore,
			double minPercentajeOfsmilirarity, int minConsecutiveLength) throws IOException {

		writeIonToSpectrumMap();
		writeSpectrumToPeptideExperimentReplicateMap();
		try {
			writePeptideExperimentReplicateToPeptideExperimentMap();
			try {
				writePeptideExperimentToPeptideMap();
			} catch (IllegalArgumentException e) {
				log.info(e.getMessage());
				// "There is not more than 1 experiment"
				// do nothing and perform the last step: proteinToAll
			}

		} catch (IllegalArgumentException e) {
			log.info(e.getMessage());
			// "There is not any experiment with some replicates"
			try {
				writePeptideExperimentToPeptideMap();
			} catch (IllegalArgumentException e2) {
				log.info(e2.getMessage());
				// "There is not more than 1 experiment"
				// do nothing and perform the last step: proteinToAll
			}
		}
		final Set<ProteinCluster> proteinClusterMap = writePeptideToProteinClusterMap(minAlignmentScore,
				minPercentajeOfsmilirarity, minConsecutiveLength);
		writeProteinClusterToAllMap(minAlignmentScore, minPercentajeOfsmilirarity, minConsecutiveLength,
				proteinClusterMap);

	}

	private void writeRelationshipsFilesForProteinOutcome() throws IOException {
		if (quantType == QuantificationType.ISOTOPOLOGUES) {
			writeIonToSpectrumMap();
		}
		writeSpectrumToPeptideExperimentReplicateMap();
		writePeptideToProteinMap();
		try {
			writeProteinExperimentReplicateToProteinExperimentMap();
			try {
				writeProteinExperimentToProteinMap();
			} catch (IllegalArgumentException e) {
				log.info(e.getMessage());
				// "There is not more than 1 experiment"
				// do nothing and perform the last step: proteinToAll
			}

		} catch (IllegalArgumentException e) {
			log.info(e.getMessage());
			// "There is not any experiment with some replicates"
			try {
				writeProteinExperimentToProteinMap();
			} catch (IllegalArgumentException e2) {
				log.info(e2.getMessage());
				// "There is not more than 1 experiment"
				// do nothing and perform the last step: proteinToAll
			}
		}
		writeProteinToAllMap();
	}

	private void writeRelationshipsFilesForPeptideOutcome() throws IOException {
		writeIonToSpectrumMap();
		writeSpectrumToPeptideExperimentReplicateMap();

		try {
			writePeptideExperimentReplicateToPeptideExperimentMap();
			try {
				writePeptideExperimentToPeptideMap();
			} catch (IllegalArgumentException e) {
				log.info(e.getMessage());
				// "There is not more than 1 experiment"
				// do nothing and perform the last step: proteinToAll
			}

		} catch (IllegalArgumentException e) {
			log.info(e.getMessage());
			// "There is not any experiment with some replicates"
			try {
				writePeptideExperimentToPeptideMap();
			} catch (IllegalArgumentException e2) {
				log.info(e2.getMessage());
				// "There is not more than 1 experiment"
				// do nothing and perform the last step: proteinToAll
			}
		}
		writePeptideToAllMap();
	}

	/**
	 * ALL --> ACC1<br>
	 * ALL --> ACC2<br>
	 * ALL --> ACC3<br>
	 *
	 * @throws IOException
	 */
	private void writeProteinToAllMap() throws IOException {
		FileWriter writer = new FileWriter(
				getWorkingPath().getAbsolutePath() + File.separator + FileMappingResults.PROTEIN_TO_ALL_6);
		HashMap<String, Set<String>> map = new HashMap<String, Set<String>>();
		String all = "all";
		for (QuantExperiment exp : quantExperiments) {
			for (QuantReplicate rep : exp.getReplicates()) {
				final QuantParser parser = rep.getParser();
				final Map<String, QuantifiedProteinInterface> quantifiedProteinMap = parser.getProteinMap();
				for (String proteinKey : quantifiedProteinMap.keySet()) {
					if (map.containsKey(all)) {
						map.get(all).add(proteinKey);
					} else {
						Set<String> set = new HashSet<String>();
						set.add(proteinKey);
						map.put(all, set);
					}
				}
			}
		}
		String header = "all" + "\t" + "acc" + "\t" + "all --> protein";
		writeMapToFile(header, map, writer);
	}

	/**
	 * PEP1 --> CLUSTER1<br>
	 * PEP2 --> CLUSTER1<br>
	 * PEP3 --> CLUSTER2<br>
	 * PEP3 --> CLUSTER2<br>
	 * <br>
	 *
	 * @return
	 * @throws IOException
	 */
	private Set<ProteinCluster> writePeptideToForcedProteinClusterMap(Set<Set<String>> forcedProteinClusters)
			throws IOException {

		final File file = new File(
				getWorkingPath().getAbsolutePath() + File.separator + FileMappingResults.PEPTIDE_TO_PROTEIN_CLUSTER_5);

		// create the clusters according to the parameter
		Set<ProteinCluster> proteinClusters = new HashSet<ProteinCluster>();
		for (Set<String> proteinAccs : forcedProteinClusters) {
			List<String> proteinAccList = new ArrayList<String>();
			proteinAccList.addAll(proteinAccs);
			Collections.sort(proteinAccList);
			StringBuilder proteinClusterKey = new StringBuilder();
			for (String proteinAcc : proteinAccList) {
				proteinClusterKey.append(proteinAcc + ":");
			}
			ProteinCluster cluster = new ProteinCluster();
			cluster.setProteinClusterKey(proteinClusterKey.toString());
			proteinClusters.add(cluster);
			for (String proteinACC : proteinAccs) {
				for (QuantExperiment exp : quantExperiments) {
					for (QuantReplicate rep : exp.getReplicates()) {
						final QuantParser parser = rep.getParser();
						final Map<String, QuantifiedProteinInterface> proteinMap = parser.getProteinMap();
						if (proteinMap.containsKey(proteinACC)) {
							final QuantifiedProteinInterface quantifiedProtein = proteinMap.get(proteinACC);
							cluster.addProtein(quantifiedProtein);
						}
					}
				}
			}
		}

		if (!overrideFilesIfExists && file.exists()) {
			return proteinClusters;
		}
		HashMap<String, Set<String>> map = new HashMap<String, Set<String>>();

		for (ProteinCluster proteinCluster : proteinClusters) {
			String proteinClusterKey = proteinCluster.getProteinClusterKey();
			final Set<QuantifiedPeptideInterface> quantifiedPeptides = proteinCluster.getPeptideSet();
			for (QuantifiedPeptideInterface quantifiedPeptide : quantifiedPeptides) {
				final String peptideKey = quantifiedPeptide.getKey();
				if (map.containsKey(proteinClusterKey)) {
					map.get(proteinClusterKey).add(peptideKey);
				} else {
					Set<String> set = new HashSet<String>();
					set.add(peptideKey);
					map.put(proteinClusterKey, set);
				}
			}
		}
		FileWriter writer = new FileWriter(file);
		String header = "pep" + "\t" + "proteinCluster" + "\t" + "proteinCluster --> peptide";
		writeMapToFile(header, map, writer);
		return proteinClusters;
	}

	/**
	 * PEP1 --> CLUSTER1<br>
	 * PEP2 --> CLUSTER1<br>
	 * PEP3 --> CLUSTER2<br>
	 * PEP3 --> CLUSTER2<br>
	 * <br>
	 * Parameters for the clustering building:<br>
	 *
	 * @param minAlignmentScore
	 * @param minPercentajeOfsmilirarity
	 * @param minConsecutiveLength
	 * @return
	 * @throws IOException
	 */
	private Set<ProteinCluster> writePeptideToProteinClusterMap(int minAlignmentScore,
			double minPercentajeOfsmilirarity, int minConsecutiveLength) throws IOException {

		final File file = new File(
				getWorkingPath().getAbsolutePath() + File.separator + FileMappingResults.PEPTIDE_TO_PROTEIN_CLUSTER_5);

		List<QuantifiedPSMInterface> quantPSMs = new ArrayList<QuantifiedPSMInterface>();
		HashMap<String, Set<String>> map = new HashMap<String, Set<String>>();
		for (QuantExperiment exp : quantExperiments) {

			for (QuantReplicate rep : exp.getReplicates()) {

				final QuantParser parser = rep.getParser();
				// in this case, get all proteins and construct protein groups.
				// Then, asign in the map to peptides
				final Map<String, QuantifiedPSMInterface> psmMap = parser.getPSMMap();

				for (QuantifiedPSMInterface quantPSM : psmMap.values()) {
					quantPSMs.add(quantPSM);
				}

			}
		}

		// create the peptides from all PSMs.
		final Map<String, QuantifiedPeptideInterface> peptideMap = IsobaricQuantifiedPeptide
				.getQuantifiedPeptides(quantPSMs);

		final Set<ProteinCluster> proteinClusters = ProteinClusterUtils.getProteinClusters(peptideMap,
				minAlignmentScore, minPercentajeOfsmilirarity, minConsecutiveLength);
		if (!overrideFilesIfExists && file.exists()) {
			return proteinClusters;
		}
		FileWriter writer = new FileWriter(file);
		for (ProteinCluster proteinCluster : proteinClusters) {
			String proteinClusterKey = proteinCluster.getProteinClusterKey();

			final Set<QuantifiedPeptideInterface> quantifiedPeptides = proteinCluster.getPeptideSet();
			for (QuantifiedPeptideInterface quantifiedPeptide : quantifiedPeptides) {
				final String peptideKey = quantifiedPeptide.getKey();
				if (map.containsKey(proteinClusterKey)) {
					map.get(proteinClusterKey).add(peptideKey);
				} else {
					Set<String> set = new HashSet<String>();
					set.add(peptideKey);
					map.put(proteinClusterKey, set);
				}
			}
		}

		String header = "pep" + "\t" + "proteinCluster" + "\t" + "proteinCluster --> peptide";
		writeMapToFile(header, map, writer);
		return proteinClusters;
	}

	/**
	 * ALL --> PEP1<br>
	 * ALL --> PEP2<br>
	 * ALL --> PEP3<br>
	 *
	 * @throws IOException
	 */
	private void writePeptideToAllMap() throws IOException {
		final File file = new File(
				getWorkingPath().getAbsolutePath() + File.separator + FileMappingResults.PEPTIDE_TO_ALL_5);
		if (!overrideFilesIfExists && file.exists()) {
			return;
		}
		FileWriter writer = new FileWriter(file);
		HashMap<String, Set<String>> map = new HashMap<String, Set<String>>();
		String all = "all";
		for (QuantExperiment exp : quantExperiments) {
			for (QuantReplicate rep : exp.getReplicates()) {
				final QuantParser parser = rep.getParser();
				final HashMap<String, Set<String>> peptideToSpectraMap2 = parser.getPeptideToSpectraMap();
				for (String peptideKey : peptideToSpectraMap2.keySet()) {
					if (map.containsKey(all)) {
						map.get(all).add(peptideKey);
					} else {
						Set<String> set = new HashSet<String>();
						set.add(peptideKey);
						map.put(all, set);
					}
				}
			}
		}
		String header = "all" + "\t" + "sequence+charge" + "\t" + "all --> peptide";
		writeMapToFile(header, map, writer);
	}

	/**
	 * ALL --> ProteinCluster1<br>
	 * ALL --> ProteinCluster2<br>
	 * ALL --> ProteinCluster3<br>
	 *
	 * @param proteinClusterMap
	 *
	 * @return
	 *
	 * @throws IOException
	 */
	private void writeProteinClusterToAllMap(int minAlignmentScore, double minPercentajeOfsmilirarity,
			int minConsecutiveLength, Set<ProteinCluster> proteinClusters) throws IOException {
		final File file = new File(
				getWorkingPath().getAbsolutePath() + File.separator + FileMappingResults.PROTEIN_CLUSTER_TO_ALL_6);
		if (!overrideFilesIfExists && file.exists()) {
			return;
		}
		FileWriter writer = new FileWriter(file);
		String all = "all";
		List<GroupableProtein> groupableProteins = new ArrayList<GroupableProtein>();
		HashMap<String, Set<String>> map = new HashMap<String, Set<String>>();
		for (QuantExperiment exp : quantExperiments) {

			for (QuantReplicate rep : exp.getReplicates()) {

				final QuantParser parser = rep.getParser();
				// in this case, get all proteins and construct protein groups.
				// Then, asign in the map to peptides
				final Map<String, QuantifiedProteinInterface> proteinMap = parser.getProteinMap();

				for (QuantifiedProteinInterface quantProtein : proteinMap.values()) {
					groupableProteins.add(quantProtein);
				}

			}
		}

		Set<String> set = new HashSet<String>();
		for (ProteinCluster proteinCluster : proteinClusters) {
			String proteinClusterKey = proteinCluster.getProteinClusterKey();
			set.add(proteinClusterKey);
		}

		map.put(all, set);
		String header = "all" + "\t" + "proteinCluster" + "\t" + "all --> proteinCluster";
		writeMapToFile(header, map, writer);

	}

	private void writeProteinGroupToAllMap() throws IOException {
		final File file = new File(
				getWorkingPath().getAbsolutePath() + File.separator + FileMappingResults.PROTEINGROUP_TO_ALL_6);
		if (!overrideFilesIfExists && file.exists()) {
			return;
		}
		FileWriter writer = new FileWriter(file);
		HashMap<String, Set<String>> map = new HashMap<String, Set<String>>();
		String all = "all";
		for (QuantExperiment exp : quantExperiments) {
			for (QuantReplicate rep : exp.getReplicates()) {
				final QuantParser parser = rep.getParser();
				List<GroupableProtein> groupableProteins = new ArrayList<GroupableProtein>();
				groupableProteins.addAll(parser.getProteinMap().values());
				final List<ProteinGroup> proteinGroups = getProteinGroups(groupableProteins);
				for (ProteinGroup proteinGroup : proteinGroups) {
					String proteinKey = KeyUtils.getGroupKey(proteinGroup);
					if (map.containsKey(all)) {
						map.get(all).add(proteinKey);
					} else {
						Set<String> set = new HashSet<String>();
						set.add(proteinKey);
						map.put(all, set);
					}
				}
			}
		}
		String header = "all" + "\t" + "acc" + "\t" + "all --> protein";
		writeMapToFile(header, map, writer);
	}

	/**
	 * ACC1 --> ACC1_EXP1<br>
	 * ACC1 --> ACC1_EXP2<br>
	 * ACC2 --> ACC2_EXP1<br>
	 * ACC3 --> ACC3_EXP2<br>
	 *
	 * @throws IOException
	 */
	private void writeProteinExperimentToProteinMap() throws IOException {
		if (quantExperiments.size() == 1)
			throw new IllegalArgumentException("There is not more than 1 experiment");

		final File file = new File(getWorkingPath().getAbsolutePath() + File.separator
				+ FileMappingResults.PROTEIN_EXPERIMENT_TO_PROTEIN_5);
		if (!overrideFilesIfExists && file.exists()) {
			return;
		}
		FileWriter writer = new FileWriter(file);
		HashMap<String, Set<String>> map = new HashMap<String, Set<String>>();

		for (QuantExperiment exp : quantExperiments) {
			String expName = "_" + exp.getName();
			if (quantExperiments.size() == 1)
				expName = "";
			for (QuantReplicate rep : exp.getReplicates()) {
				final QuantParser parser = rep.getParser();
				final Map<String, QuantifiedProteinInterface> quantifiedProteinMap = parser.getProteinMap();
				for (String proteinKey : quantifiedProteinMap.keySet()) {
					if (map.containsKey(proteinKey)) {
						map.get(proteinKey).add(proteinKey + expName);
					} else {
						Set<String> set = new HashSet<String>();
						set.add(proteinKey + expName);
						map.put(proteinKey, set);
					}
				}
			}
		}
		String header = "acc" + "\t" + "acc+experiment" + "\t" + "protein --> protein-experiment";
		writeMapToFile(header, map, writer);

	}

	/**
	 * PEP1 --> PEP1_EXP1<br>
	 * PEP1 --> PEP1_EXP2<br>
	 * PEP2 --> PEP2_EXP1<br>
	 * PEP3 --> PEP3_EXP2<br>
	 *
	 * @throws IOException
	 */
	private void writePeptideExperimentToPeptideMap() throws IOException {
		if (quantExperiments.size() == 1)
			throw new IllegalArgumentException("There is not more than 1 experiment");

		final File file = new File(getWorkingPath().getAbsolutePath() + File.separator
				+ FileMappingResults.PEPTIDE_EXPERIMENT_TO_PEPTIDE_4);
		if (!overrideFilesIfExists && file.exists()) {
			return;
		}
		FileWriter writer = new FileWriter(file);
		HashMap<String, Set<String>> map = new HashMap<String, Set<String>>();

		for (QuantExperiment exp : quantExperiments) {
			String expName = "_" + exp.getName();
			if (quantExperiments.size() == 1)
				expName = "";
			for (QuantReplicate rep : exp.getReplicates()) {
				final QuantParser parser = rep.getParser();
				for (String peptideKey : parser.getPeptideToSpectraMap().keySet()) {
					if (map.containsKey(peptideKey)) {
						map.get(peptideKey).add(peptideKey + expName);
					} else {
						Set<String> set = new HashSet<String>();
						set.add(peptideKey + expName);
						map.put(peptideKey, set);
					}
				}
			}
		}
		String header = "sequence+charge" + "\t" + "sequence+charge+experiment" + "\t"
				+ "peptide --> peptide-experiment";
		writeMapToFile(header, map, writer);

	}

	private void writeProteinGroupExperimentToProteinGroupMap() throws IOException {
		if (quantExperiments.size() == 1)
			throw new IllegalArgumentException("There is not more than 1 experiment");

		final File file = new File(getWorkingPath().getAbsolutePath() + File.separator
				+ FileMappingResults.PROTEINGROUP_EXPERIMENT_TO_PROTEINGROUP_5);
		if (!overrideFilesIfExists && file.exists()) {
			return;
		}
		FileWriter writer = new FileWriter(file);
		HashMap<String, Set<String>> map = new HashMap<String, Set<String>>();

		for (QuantExperiment exp : quantExperiments) {
			String expName = "_" + exp.getName();
			if (quantExperiments.size() == 1)
				expName = "";
			for (QuantReplicate rep : exp.getReplicates()) {
				final QuantParser parser = rep.getParser();
				List<GroupableProtein> groupableProteins = new ArrayList<GroupableProtein>();
				groupableProteins.addAll(parser.getProteinMap().values());
				final List<ProteinGroup> proteinGroups = getProteinGroups(groupableProteins);
				for (ProteinGroup proteinGroup : proteinGroups) {
					String proteinGroupKey = KeyUtils.getGroupKey(proteinGroup);
					if (map.containsKey(proteinGroupKey)) {
						map.get(proteinGroupKey).add(proteinGroupKey + expName);
					} else {
						Set<String> set = new HashSet<String>();
						set.add(proteinGroupKey + expName);
						map.put(proteinGroupKey, set);
					}
				}
			}
		}
		String header = "acc" + "\t" + "acc+experiment" + "\t" + "proteinGroup --> proteinGroup-experiment";
		writeMapToFile(header, map, writer);

	}

	/**
	 * This step is only valid if some experiment has replicates. Otherwise, an
	 * exception will be thrown.<br>
	 * ACC_EXP1 --> ACC_REP1_EXP1<br>
	 * ACC_EXP1 --> ACC_REP2_EXP1<br>
	 * ACC_EXP2 --> ACC_REP1_EXP2<br>
	 * ACC_EXP2 --> ACC_REP2_EXP2<br>
	 *
	 * @throws IOException
	 */
	private void writeProteinExperimentReplicateToProteinExperimentMap() throws IOException {

		boolean someReplicates = false;
		for (QuantExperiment exp : quantExperiments) {
			if (exp.getReplicates().size() > 1)
				someReplicates = true;
		}
		if (!someReplicates)
			throw new IllegalArgumentException("There is not any experiment with some replicates");

		final File file = new File(getWorkingPath().getAbsolutePath() + File.separator
				+ FileMappingResults.PROTEIN_REPLICATE_EXPERIMENT_TO_PROTEIN_EXPERIMENT_4);
		if (!overrideFilesIfExists && file.exists()) {
			return;
		}
		FileWriter writer = new FileWriter(file);
		HashMap<String, Set<String>> map = new HashMap<String, Set<String>>();

		for (QuantExperiment exp : quantExperiments) {
			String expName = "_" + exp.getName();
			if (quantExperiments.size() == 1)
				expName = "";
			for (QuantReplicate rep : exp.getReplicates()) {
				String repName = rep.getName();
				if (exp.getReplicates().size() == 1) {
					if (!"".equals(expName))
						repName = "_" + expName;
					else
						repName = "";
				} else {
					repName = "_" + repName + expName;
				}
				final QuantParser parser = rep.getParser();
				final Map<String, QuantifiedProteinInterface> quantifiedProteinMap = parser.getProteinMap();
				for (String proteinKey : quantifiedProteinMap.keySet()) {
					if (map.containsKey(proteinKey + expName)) {
						map.get(proteinKey + expName).add(proteinKey + repName);
					} else {
						Set<String> set = new HashSet<String>();
						set.add(proteinKey + repName);
						map.put(proteinKey + expName, set);
					}
				}

			}
		}
		String header = "acc+experiment" + "\t" + "acc+replicate+experiment" + "\t"
				+ "protein-experiment --> protein-replicate-experiment";
		writeMapToFile(header, map, writer);

	}

	/**
	 * This step is only valid if some experiment has replicates. Otherwise, an
	 * exception will be thrown.<br>
	 * PEP_EXP1 --> PEP_REP1_EXP1<br>
	 * PEP_EXP1 --> PEP_REP2_EXP1<br>
	 * PEP_EXP2 --> PEP_REP1_EXP2<br>
	 * PEP_EXP2 --> PEP_REP2_EXP2<br>
	 *
	 * @throws IOException
	 */
	private void writePeptideExperimentReplicateToPeptideExperimentMap() throws IOException {

		boolean someReplicates = false;
		for (QuantExperiment exp : quantExperiments) {
			if (exp.getReplicates().size() > 1)
				someReplicates = true;
		}
		if (!someReplicates)
			throw new IllegalArgumentException("There is not any experiment with some replicates");

		final File file = new File(getWorkingPath().getAbsolutePath() + File.separator
				+ FileMappingResults.PEPTIDE_REPLICATE_EXPERIMENT_TO_PEPTIDE_EXPERIMENT_3);
		if (!overrideFilesIfExists && file.exists()) {
			return;
		}
		FileWriter writer = new FileWriter(file);
		HashMap<String, Set<String>> map = new HashMap<String, Set<String>>();

		for (QuantExperiment exp : quantExperiments) {
			String expName = "_" + exp.getName();
			if (quantExperiments.size() == 1)
				expName = "";
			for (QuantReplicate rep : exp.getReplicates()) {
				String repName = rep.getName();
				if (exp.getReplicates().size() == 1) {
					if (!"".equals(expName))
						repName = "_" + expName;
					else
						repName = "";
				} else {
					repName = "_" + repName + expName;
				}
				final QuantParser parser = rep.getParser();
				for (String peptideKey : parser.getPeptideToSpectraMap().keySet()) {
					if (map.containsKey(peptideKey + expName)) {
						map.get(peptideKey + expName).add(peptideKey + repName);
					} else {
						Set<String> set = new HashSet<String>();
						set.add(peptideKey + repName);
						map.put(peptideKey + expName, set);
					}
				}
			}
		}
		String header = "sequence+charge+experiment" + "\t" + "sequence+charge+replicate+experiment" + "\t"
				+ "peptide-replicate-experiment --> peptide-experiment";
		writeMapToFile(header, map, writer);

	}

	private void writeProteinGroupExperimentReplicateToProteinGroupExperimentMap() throws IOException {

		boolean someReplicates = false;
		for (QuantExperiment exp : quantExperiments) {
			if (exp.getReplicates().size() > 1)
				someReplicates = true;
		}
		if (!someReplicates)
			throw new IllegalArgumentException("There is not any experiment with some replicates");

		final File file = new File(getWorkingPath().getAbsolutePath() + File.separator
				+ FileMappingResults.PROTEINGROUP_REPLICATE_EXPERIMENT_TO_PROTEINGROUP_EXPERIMENT_4);
		if (!overrideFilesIfExists && file.exists()) {
			return;
		}
		FileWriter writer = new FileWriter(file);
		HashMap<String, Set<String>> map = new HashMap<String, Set<String>>();

		for (QuantExperiment exp : quantExperiments) {
			String expName = "_" + exp.getName();
			if (quantExperiments.size() == 1)
				expName = "";
			for (QuantReplicate rep : exp.getReplicates()) {
				String repName = rep.getName();
				if (exp.getReplicates().size() == 1) {
					if (!"".equals(expName))
						repName = "_" + expName;
					else
						repName = "";
				} else {
					repName = "_" + repName + expName;
				}
				final QuantParser parser = rep.getParser();
				List<GroupableProtein> groupableProteins = new ArrayList<GroupableProtein>();
				groupableProteins.addAll(parser.getProteinMap().values());
				final List<ProteinGroup> proteinGroups = getProteinGroups(groupableProteins);
				for (ProteinGroup proteinGroup : proteinGroups) {
					final String proteinGroupKey = KeyUtils.getGroupKey(proteinGroup);
					if (map.containsKey(proteinGroupKey + expName)) {
						map.get(proteinGroupKey + expName).add(proteinGroupKey + repName);
					} else {
						Set<String> set = new HashSet<String>();
						set.add(proteinGroupKey + repName);
						map.put(proteinGroupKey + expName, set);
					}
				}

			}
		}
		String header = "acc+experiment" + "\t" + "acc+replicate+experiment" + "\t"
				+ "proteinGroup-experiment --> proteinGroup-replicate-experiment";
		writeMapToFile(header, map, writer);

	}

	/**
	 * ACC1_REP1_EXP1 --> PEPTIDEA_3_REP1_EXP1<br>
	 * ACC1_REP1_EXP1 --> PEPTIDEB_2_REP1_EXP1<br>
	 * ACC1_REP2_EXP1 --> PEPTIDEA_3_REP2_EXP1<br>
	 * ACC1_REP1_EXP2 --> PEPTIDEA_3_REP1_EXP2<br>
	 * ACC1_REP1_EXP2 --> PEPTIDEB_2_REP1_EXP2<br>
	 * ACC1_REP2_EXP2 --> PEPTIDEA_3_REP2_EXP2<br>
	 *
	 * @throws IOException
	 */
	private void writePeptideExperimentReplicateToProteinGroupExperimentReplicateMap() throws IOException {
		final File file = new File(getWorkingPath().getAbsolutePath() + File.separator
				+ FileMappingResults.PEPTIDE_REPLICATE_EXPERIMENT_TO_PROTEINGROUP_REPLICATE_EXPERIMENT_3);
		if (!overrideFilesIfExists && file.exists()) {
			return;
		}
		FileWriter writer = new FileWriter(file);
		HashMap<String, Set<String>> map = new HashMap<String, Set<String>>();

		for (QuantExperiment exp : quantExperiments) {
			String expName = "_" + exp.getName();
			if (quantExperiments.size() == 1)
				expName = "";
			for (QuantReplicate rep : exp.getReplicates()) {
				String repName = rep.getName();
				if (exp.getReplicates().size() == 1) {
					if (!"".equals(expName))
						repName = "_" + expName;
					else
						repName = "";
				} else {
					repName = "_" + repName + expName;
				}
				final QuantParser parser = rep.getParser();
				// in this case, get all proteins and construct protein groups.
				// Then, asign in the map to peptides
				List<GroupableProtein> groupableProteins = new ArrayList<GroupableProtein>();
				groupableProteins.addAll(parser.getProteinMap().values());
				final List<ProteinGroup> proteinGroups = getProteinGroups(groupableProteins);
				final HashMap<String, Set<String>> proteinGroupToPeptideMap2 = getProteinGroupToPeptideMap(
						proteinGroups);

				mergeMaps(map, proteinGroupToPeptideMap2, repName, repName);
			}
		}
		String header = "acc+replicate+experiment" + "\t" + "sequence+charge+replicate+experiment" + "\t"
				+ "proteinGroup-replicate-experiment --> peptide-replicate-experiment";
		writeMapToFile(header, map, writer);

	}

	/**
	 * ACC1_REP1_EXP1 --> PEPTIDEA_3_REP1_EXP1<br>
	 * ACC1_REP1_EXP1 --> PEPTIDEB_2_REP1_EXP1<br>
	 * ACC1_REP2_EXP1 --> PEPTIDEA_3_REP2_EXP1<br>
	 * ACC1_REP1_EXP2 --> PEPTIDEA_3_REP1_EXP2<br>
	 * ACC1_REP1_EXP2 --> PEPTIDEB_2_REP1_EXP2<br>
	 * ACC1_REP2_EXP2 --> PEPTIDEA_3_REP2_EXP2<br>
	 *
	 * @throws IOException
	 */
	private void writePeptideToProteinMap() throws IOException {
		final File file = new File(getWorkingPath().getAbsolutePath() + File.separator
				+ FileMappingResults.PEPTIDE_REPLICATE_EXPERIMENT_TO_PROTEIN_REPLICATE_EXPERIMENT_3);
		if (!overrideFilesIfExists && file.exists()) {
			return;
		}
		FileWriter writer = new FileWriter(file);
		HashMap<String, Set<String>> map = new HashMap<String, Set<String>>();

		for (QuantExperiment exp : quantExperiments) {
			String expName = "_" + exp.getName();
			if (quantExperiments.size() == 1)
				expName = "";
			for (QuantReplicate rep : exp.getReplicates()) {
				String repName = rep.getName();
				if (exp.getReplicates().size() == 1) {
					if (!"".equals(expName))
						repName = "_" + expName;
					else
						repName = "";
				} else {
					repName = "_" + repName + expName;
				}
				final QuantParser parser = rep.getParser();
				// in this case, get all proteins and construct protein groups.
				// Then, asign in the map to peptides
				final Map<String, QuantifiedProteinInterface> proteinMap = parser.getProteinMap();
				final HashMap<String, Set<String>> map2 = new HashMap<String, Set<String>>();
				for (String proteinKey : proteinMap.keySet()) {
					final QuantifiedProteinInterface quantifiedProtein = proteinMap.get(proteinKey);
					// final List<Peptide> peptides = quantifiedProtein
					// .getPeptide();
					// if (peptides != null) {
					//
					// for (Peptide peptide : peptides) {
					// final String peptideKey = CensusChroUtil
					// .getPeptideKey(peptide);
					// if (map2.containsKey(proteinKey)) {
					// map2.get(proteinKey).add(peptideKey);
					// } else {
					// Set<String> set = new HashSet<String>();
					// set.add(peptideKey);
					// map2.put(proteinKey, set);
					// }
					// }
					// } else {
					final Set<QuantifiedPSMInterface> quantifiedPSMs = quantifiedProtein.getQuantifiedPSMs();
					for (QuantifiedPSMInterface quantifiedPSM : quantifiedPSMs) {
						final String peptideKey = KeyUtils.getSequenceChargeKey(quantifiedPSM, chargeStateSensible);
						if (map2.containsKey(proteinKey)) {
							map2.get(proteinKey).add(peptideKey);
						} else {
							Set<String> set = new HashSet<String>();
							set.add(peptideKey);
							map2.put(proteinKey, set);
						}
					}
					// }
				}
				mergeMaps(map, map2, repName, repName);
			}
		}
		String header = "acc+replicate+experiment" + "\t" + "sequence+charge+replicate+experiment" + "\t"
				+ "protein-replicate-experiment --> peptide-replicate-experiment";
		writeMapToFile(header, map, writer);

	}

	private HashMap<String, Set<String>> getProteinGroupToPeptideMap(List<ProteinGroup> proteinGroups) {
		HashMap<String, Set<String>> ret = new HashMap<String, Set<String>>();
		for (ProteinGroup proteinGroup : proteinGroups) {
			final String groupKey = KeyUtils.getGroupKey(proteinGroup);
			Set<String> set = new HashSet<String>();
			final List<GroupablePSM> psMs = proteinGroup.getPSMs();
			for (GroupablePSM groupablePSM : psMs) {
				if (groupablePSM instanceof QuantifiedPSMInterface) {
					QuantifiedPSMInterface psm = (QuantifiedPSMInterface) groupablePSM;
					final String peptideKey = KeyUtils.getSequenceChargeKey(psm, chargeStateSensible);
					set.add(peptideKey);
				}
			}
			ret.put(groupKey, set);
		}
		return ret;
	}

	private List<ProteinGroup> getProteinGroups(List<GroupableProtein> groupableProteins) {
		PAnalyzer pa = new PAnalyzer(false);
		List<ProteinGroup> proteinGroups = pa.run(groupableProteins);
		return proteinGroups;
	}

	private boolean passAlignmentFilter(NWResult alignResult, int minAlignmentScore, double minPercentajeOfsmilirarity,
			int minConsecutiveLength) {
		if (alignResult.getFinalAlignmentScore() >= minAlignmentScore) {
			if (alignResult.getSequenceIdentity() >= minPercentajeOfsmilirarity) {
				if (alignResult.getMaxConsecutiveIdenticalAlignment() >= minConsecutiveLength) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * PEPTIDEA_3_REP1_EXP1 --> SCAN1_RAWFILE1<br>
	 * PEPTIDEA_3_REP1_EXP1 --> SCAN2_RAWFILE1<br>
	 * PEPTIDEA_3_REP2_EXP1 --> SCAN1_RAWFILE2<br>
	 * PEPTIDEB_2_REP1_EXP1 --> SCAN3_RAWFILE1<br>
	 *
	 * @throws IOException
	 */
	private void writeSpectrumToPeptideExperimentReplicateMap() throws IOException {
		FileWriter writer = new FileWriter(getWorkingPath().getAbsolutePath() + File.separator
				+ FileMappingResults.SPECTRUM_TO_PEPTIDE_REPLICATE_EXPERIMENT_2);
		HashMap<String, Set<String>> map = new HashMap<String, Set<String>>();

		for (QuantExperiment exp : quantExperiments) {
			String expName = "_" + exp.getName();
			if (quantExperiments.size() == 1)
				expName = "";
			for (QuantReplicate rep : exp.getReplicates()) {
				String repName = rep.getName();
				if (exp.getReplicates().size() == 1) {
					if (!"".equals(expName))
						repName = "_" + expName;
					else
						repName = "";
				} else {
					repName = "_" + repName + expName;
				}
				final QuantParser parser = rep.getParser();
				final HashMap<String, Set<String>> tmpMap = parser.getPeptideToSpectraMap();
				final HashMap<String, Set<String>> peptideToSpectraMap2 = addRepNameToMap(tmpMap, repName);
				mergeMaps(map, peptideToSpectraMap2, repName, "");
			}
		}
		String header = "sequence+charge+replicate+experiment" + "\t" + "scan+raw_file" + "\t"
				+ "spectrum --> peptide-replicate-experiment";
		writeMapToFile(header, map, writer);

	}

	/**
	 * SCAN1_RAWFILE1 --> IONY1_SCAN1_RAWFILE1<br>
	 * SCAN1_RAWFILE1 --> IONB2_SCAN1_RAWFILE1<br>
	 * SCAN2_RAWFILE1 --> IONY1_SCAN2_RAWFILE1<br>
	 *
	 * @throws IOException
	 */
	private void writeIonToSpectrumMap() throws IOException {
		if (quantType != QuantificationType.ISOTOPOLOGUES) {
			return;
		}
		final File file = new File(
				getWorkingPath().getAbsolutePath() + File.separator + FileMappingResults.ION_TO_SPECTRUM_1);
		if (!overrideFilesIfExists && file.exists()) {
			return;
		}
		FileWriter writer = new FileWriter(file);
		HashMap<String, Set<String>> map = new HashMap<String, Set<String>>();

		for (QuantExperiment exp : quantExperiments) {
			String expName = "_" + exp.getName();
			if (quantExperiments.size() == 1)
				expName = "";
			for (QuantReplicate rep : exp.getReplicates()) {

				String repName = rep.getName();
				if (exp.getReplicates().size() == 1) {
					if (!"".equals(expName))
						repName = "_" + expName;
					else
						repName = "";
				} else {
					repName = "_" + repName + expName;
				}
				final IsobaricQuantParser parser = (IsobaricQuantParser) rep.getParser();
				final HashMap<String, Set<String>> tmpMap = parser.getSpectrumToIonsMap();

				// add repName to the elements of the map
				final HashMap<String, Set<String>> spectrumToIonsMap2 = addRepNameToMap(tmpMap, repName);

				mergeMaps(map, spectrumToIonsMap2, "", "");
			}
		}

		String header = "scan+raw_file" + "\t" + "ion_type+scan+raw_file" + "\t" + "ion --> spectrum";
		writeMapToFile(header, map, writer);
	}

	private HashMap<String, Set<String>> addRepNameToMap(HashMap<String, Set<String>> map, String repName) {
		final HashMap<String, Set<String>> ret = new HashMap<String, Set<String>>();
		for (String key : map.keySet()) {
			final Set<String> keys = map.get(key);
			Set<String> correctedKeys = new HashSet<String>();
			for (String key2 : keys) {
				String newKey = key2;
				if (!key2.endsWith(repName))
					newKey += repName;
				correctedKeys.add(newKey);
			}
			String newKey2 = key;
			if (!newKey2.endsWith(repName))
				newKey2 += repName;
			ret.put(newKey2, correctedKeys);
		}
		return ret;
	}

	private void writeMapToFile(String header, HashMap<String, Set<String>> map, FileWriter writer) throws IOException {
		try {
			writer.write(header + NL);
			final Set<String> keySet = map.keySet();
			List<String> list = new ArrayList<String>();
			list.addAll(keySet);
			Collections.sort(list);
			for (String key : list) {
				final Set<String> set = map.get(key);
				for (String value : set) {
					writer.write(key + "\t" + value + NL);
				}
			}
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	private void mergeMaps(HashMap<String, Set<String>> receiver, HashMap<String, Set<String>> donor,
			String suffixForKey, String suffixForValue) {
		for (String originalkey : donor.keySet()) {
			String key = originalkey;
			if (!key.endsWith(suffixForKey))
				key += suffixForKey;
			final Set<String> donorValues = donor.get(originalkey);
			if (receiver.containsKey(key)) {
				for (String donorValue : donorValues) {
					receiver.get(key).add(donorValue + suffixForValue);
				}
			} else {
				Set<String> set = new HashSet<String>();
				for (String donorValue : donorValues) {
					String key2 = donorValue;
					if (!key2.endsWith(suffixForKey))
						key2 += suffixForKey;
					set.add(key2);
				}
				receiver.put(key, set);
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent prop) {
		if (prop.getPropertyName().equals(SanXotInterfaze.CALIBRATING)) {
			log.info("Waiting for calibrating data...");
		} else if (prop.getPropertyName().equals(SanXotInterfaze.STARTING_COMMAND)) {
			final String commandString = (String) prop.getNewValue();
			log.info("Waiting for running command: " + commandString);
		} else if (prop.getPropertyName().equals(SanXotInterfaze.END_COMMAND)) {
			final Long command = (Long) prop.getNewValue();
			log.info("Command finished with ext code: " + command);
			if (command == -999)
				System.exit(-999);
		} else if (prop.getPropertyName().equals(SanXotInterfaze.END_ANALYSIS)) {
			result = (SanXotAnalysisResult) prop.getNewValue();
			log.info(result);
			final IntegrationResultWrapper lastIntegrationResults = result.getLastIntegrationResults();
			final List<OutStatsLine> resultData = lastIntegrationResults.getResultData();
			for (OutStatsLine outStatsLine : resultData) {
				System.out.println(outStatsLine.getIdsup() + "\t" + outStatsLine.getFDR());
			}
		}
	}

	public void setOutlierRemoval(boolean b) {
		quantParameters.setPerformRemoveOutliers(b);
	}

	public void setCalibration(boolean b) {
		quantParameters.setPerformCalibration(b);
	}

	public void setFDR(double fdr) {
		quantParameters.setFdr(fdr);
	}

	public void setFastaFile(File fastaFile) {
		log.info("Constructing index from fasta file: " + fastaFile + " using default parameters");
		setFastaFile(DBIndexInterface.getDefaultDBIndexParams(fastaFile));
	}

	public void setFastaFile(DBIndexSearchParams dbIndexSearchParams) {
		log.info("Constructing index using provided parameters");
		dbIndex = DBIndexInterface.getByParam(dbIndexSearchParams);
	}

	public void setTimeout(long timeoutInMilliseconds) {
		timeout = timeoutInMilliseconds;
	}

	/**
	 * @return the result
	 */
	public SanXotAnalysisResult getResult() {
		return result;
	}

	/**
	 * States if the method should differentiate peptides with different charge
	 * states or not. In case of not doing that, peptides with different charge
	 * states will be merged as they are the same peptide.
	 *
	 * @param b
	 */
	public void setChargeStateSensible(boolean chargeSensible) {
		chargeStateSensible = chargeSensible;
		for (QuantExperiment quantExperiment : quantExperiments) {
			quantExperiment.setChargeStateSensible(chargeSensible);
		}
	}

	/**
	 * @return the minAlignmentScore
	 */
	public int getMinAlignmentScore() {
		return minAlignmentScore;
	}

	/**
	 * @param minAlignmentScore
	 *            the minAlignmentScore to set
	 */
	public void setMinAlignmentScore(int minAlignmentScore) {
		this.minAlignmentScore = minAlignmentScore;
	}

	/**
	 * @return the minPercentajeOfsmilirarity
	 */
	public double getMinPercentajeOfsmilirarity() {
		return minPercentajeOfsmilirarity;
	}

	/**
	 * @param minPercentajeOfsmilirarity
	 *            the minPercentajeOfsmilirarity to set
	 */
	public void setMinPercentajeOfsmilirarity(double minPercentajeOfsmilirarity) {
		this.minPercentajeOfsmilirarity = minPercentajeOfsmilirarity;
	}

	/**
	 * @return the minConsecutiveLength
	 */
	public int getMinConsecutiveLength() {
		return minConsecutiveLength;
	}

	/**
	 * @param minConsecutiveLength
	 *            the minConsecutiveLength to set
	 */
	public void setMinConsecutiveLength(int minConsecutiveLength) {
		this.minConsecutiveLength = minConsecutiveLength;
	}

	/**
	 * @return the overrideFilesIfExists
	 */
	public boolean isOverrideFilesIfExists() {
		return overrideFilesIfExists;
	}

	/**
	 * Determine if override or not the relationaship and data files if they
	 * already exist.
	 *
	 * @param overrideFilesIfExists
	 *            the overrideFilesIfExists to set
	 */
	public void setOverrideFilesIfExists(boolean overrideFilesIfExists) {
		this.overrideFilesIfExists = overrideFilesIfExists;
	}

	/**
	 * force the analysis to retrieve the protein level values by grouping
	 * proteins according to the proteinAccClusters provided in the parameter
	 *
	 * @param proteinAccClusters
	 * @throws IllegalArgumentException
	 *             if the {@link ANALYSIS_LEVEL_OUTCOME} is not set to
	 *             ANALYSIS_LEVEL_OUTCOME.FORCED_CLUSTERS
	 */
	public void forceProteinClusters(Set<Set<String>> proteinAccClusters) throws IllegalArgumentException {
		if (analysisOutCome != ANALYSIS_LEVEL_OUTCOME.FORCED_CLUSTERS) {
			throw new IllegalArgumentException(
					"Analysis outcome has to be set to " + ANALYSIS_LEVEL_OUTCOME.FORCED_CLUSTERS);
		}

		this.proteinAccClusters = proteinAccClusters;
	}
}
