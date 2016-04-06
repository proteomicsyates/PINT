package edu.scripps.yates.census.analysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.swing.SwingWorker;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import edu.scripps.yates.census.analysis.wrappers.IntegrationResultWrapper;
import edu.scripps.yates.census.analysis.wrappers.KalibrateResultWrapper;
import edu.scripps.yates.census.analysis.wrappers.OutlierRemovalResultWrapper;
import edu.scripps.yates.census.analysis.wrappers.SanXotAnalysisResult;
import edu.scripps.yates.census.read.util.FileSplitter;
import edu.scripps.yates.utilities.exec.ProcessExecutor;
import edu.scripps.yates.utilities.exec.ProcessExecutorHandler;
import edu.scripps.yates.utilities.files.FileUtils;
import edu.scripps.yates.utilities.util.Pair;

public class SanXotInterfaze extends SwingWorker<Object, Void> {
	private static final Logger log = Logger.getLogger(SanXotInterfaze.class);
	private static final String defaultSanxotLocation = "C:\\Users\\Salva\\Desktop\\Dropbox\\Scripps\\Isotopolgue\\isotopologues_to_share\\SanXoT";
	private static final String SANXOT_EXE = "sanxot.exe";
	private static final String KLIBRATE_EXE = "klibrate.exe";
	private static final String SANXOT_SIEVE_EXE = "sanxotsieve.exe";

	// fire change event
	public static final String CALIBRATING = "calibrating";
	public static final String CALIBRATING_DONE = "calibrating_done";
	public static final String CALIBRATING_ERROR = "calibrating_error";
	public static final String INTEGRATING = "integrating";
	public static final String INTEGRATING_DONE = "integrating_done";
	private static final String OUTLIER_REMOVAL = "outlier removal";
	private static final String OUTLIER_REMOVAL_DONE = "outlier removal done";
	public static final String STARTING_COMMAND = "starting_command";
	public static final String END_COMMAND = "end_command";
	private static final long DEFAULT_TIMEOUT = 1000 * 60 * 1;// 1 min
	private final int DEFAULT_MAX_ITERATIONS = 300;

	public static final String END_ANALYSIS = "end analysis";

	private String sanxotLocation;
	private final FileMappingResults fileMappingResults;
	private final boolean performCalibration;
	private final boolean performRemoveOutliers;
	private final double fdr;
	private FileWriter logFileWriter;
	private final SanXotAnalysisResult result;
	private long timeout;
	private int maxIterations;

	public SanXotInterfaze(FileMappingResults fileMappingResults, QuantParameters quantParameters) {
		this(fileMappingResults, quantParameters, DEFAULT_TIMEOUT);
	}

	public SanXotInterfaze(FileMappingResults fileMappingResults, QuantParameters quantParameters, long timeout) {
		sanxotLocation = defaultSanxotLocation;
		this.fileMappingResults = fileMappingResults;
		performCalibration = quantParameters.isPerformCalibration();
		performRemoveOutliers = quantParameters.isPerformRemoveOutliers();
		// TODO customize the FDR depending on the level
		fdr = quantParameters.getOutlierRemovalFdr();
		result = new SanXotAnalysisResult(fileMappingResults);
		this.timeout = timeout;
		maxIterations = DEFAULT_MAX_ITERATIONS;
	}

	public void setSanxotLocation(String path) {
		sanxotLocation = path;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.SwingWorker#done()
	 */
	@Override
	protected void done() {
		log.info("Sanxot done");
		if (isCancelled())
			log.info("Sanxot cancelled");
		if (logFileWriter != null) {
			try {
				logFileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		firePropertyChange(END_ANALYSIS, null, result);

		super.done();
	}

	@Override
	protected Object doInBackground() throws Exception {
		analyze();
		return null;
	}

	public void analyze() throws IOException, InterruptedException, ExecutionException {
		logFileWriter = new FileWriter(new File("C:\\Users\\Salva\\Desktop\\tmp\\sanxot_cmds.txt"));

		log.info("Starting SanXot interfaze");
		log.info("Timeout set at " + timeout / 1000 + " sg.");
		int lowLevel = 0;
		int upperLevel = 0;
		File dataFile = fileMappingResults.getDataFile();
		File infoFile = null;
		try {
			final Map<String, List<String>> experimentAndReplicateNames = fileMappingResults
					.getExperimentAndReplicateNames();
			Pair<Integer, File> lowLevelPair = fileMappingResults.getFirstLevel();
			lowLevel = lowLevelPair.getFirstelement();
			Pair<Integer, File> upperLevelPair = fileMappingResults.getNextAvailableLevel(lowLevel);
			upperLevel = upperLevelPair.getFirstelement();
			File relatFile = lowLevelPair.getSecondElement();
			Map<String, IntegrationResultWrapper> experimentIntegrationResults = new HashMap<String, IntegrationResultWrapper>();

			for (String experimentName : experimentAndReplicateNames.keySet()) {
				log.info("Experiment: " + experimentName);
				// get the names of the replicates and experiments in order to
				// split the relation files
				List<String> dataSetNames = getDataSetNames(experimentAndReplicateNames, experimentName);

				// split relatFile in many files as replicates
				Map<String, File> relatFiles = FileSplitter
						.splitFiles(fileMappingResults.getFirstLevel().getSecondElement(), dataSetNames);

				// split dataFile in many files as replicates
				Map<String, File> dataFiles = FileSplitter.splitFiles(fileMappingResults.getDataFile(), dataSetNames);

				Map<String, File> calibratedDataFiles = new HashMap<String, File>();
				for (String datasetName : relatFiles.keySet()) {
					relatFile = relatFiles.get(datasetName);
					dataFile = dataFiles.get(datasetName);
					// get lowLevels again
					lowLevel = fileMappingResults.getFirstLevel().getFirstelement();
					upperLevel = fileMappingResults.getNextAvailableLevel(lowLevel).getFirstelement();
					// CALIBRATION
					if (performCalibration) {
						final KalibrateResultWrapper calibrationResult = calibrate(lowLevel, upperLevel, relatFile,
								dataFile, "_" + datasetName);
						result.setKalibrationResult(calibrationResult);
						if (calibrationResult.getCalibratedDataFile() != null) {
							dataFile = calibrationResult.getCalibratedDataFile();
						}
						calibratedDataFiles.put(datasetName, dataFile);
					} else {
						calibratedDataFiles.put(datasetName, dataFile);
					}
				}

				Map<String, File> lastDataFiles = new HashMap<String, File>();
				// loop
				boolean dataMergingNeeded = false;
				for (String datasetName : relatFiles.keySet()) {
					log.info("Replicate: " + datasetName);
					relatFile = relatFiles.get(datasetName);
					dataFile = calibratedDataFiles.get(datasetName);
					// get lowLevels again
					lowLevel = fileMappingResults.getFirstLevel().getFirstelement();
					upperLevel = fileMappingResults.getNextAvailableLevel(lowLevel).getFirstelement();
					dataMergingNeeded = false;
					while (!dataMergingNeeded && upperLevel <= fileMappingResults.getMaxLevel()) {
						dataMergingNeeded = fileMappingResults.isDataMergingNeeded(lowLevel);

						relatFile = fileMappingResults.getFileLevel(lowLevel);
						// split relatFile in many files as datasets
						relatFiles = FileSplitter.splitFiles(relatFile, dataSetNames);
						relatFile = relatFiles.get(datasetName);
						lowLevelPair = fileMappingResults.getFilePairLevel(lowLevel);
						upperLevelPair = fileMappingResults.getNextAvailableLevel(lowLevelPair.getFirstelement());
						upperLevel = upperLevelPair.getFirstelement();
						log.info(lowLevel + " to " + upperLevel + " on " + datasetName);
						IntegrationResultWrapper integrationResult = null;
						if (dataMergingNeeded) {
							log.info("Skipping this step. Apparently it is not necessary");
							// integrationResult = integrate(lowLevel,
							// upperLevel, null, dataFile, null, datasetName,
							// null);
							//
							// result.addIntegrationResult(integrationResult);
						} else {
							// first integration, estimating the variance by
							// fitting
							// algorithm

							integrationResult = integrate(lowLevel, upperLevel, relatFile, dataFile, null, datasetName,
									null);

							result.addIntegrationResult(integrationResult);

							// remove outliers
							if (performRemoveOutliers) {
								// not perform in the last interation
								if (fileMappingResults.getNextAvailableLevel(upperLevel) == null) {
								} else {
									infoFile = integrationResult.getInfoFile();
									final OutlierRemovalResultWrapper removeOutliers = removeOutliers(lowLevelPair,
											upperLevelPair, dataFile, infoFile, fdr);
									relatFile = removeOutliers.getRelatFile();
									// second integration, using the variance
									// calculated in previous one (use of
									// infoFile
									// for forcing the
									// variance to be that one)

									integrationResult = integrate(lowLevel, upperLevel, relatFile, dataFile, infoFile,
											datasetName, null);

									integrationResult.setOutlierRemovalResult(removeOutliers);
									result.addIntegrationResult(integrationResult);
								}
							}
							// get datafile from higherlevel data file
							dataFile = integrationResult.getHigherLevelDataFile();
							lastDataFiles.put(datasetName, dataFile);

							// next level
							lowLevel = upperLevel;
						}
					}
				}
				// data merging
				if (dataMergingNeeded) {
					lowLevelPair = fileMappingResults.getFilePairLevel(lowLevel);
					upperLevelPair = fileMappingResults.getFilePairLevel(upperLevel);
					relatFile = lowLevelPair.getSecondElement();
					// merge the latest data files into only one
					// Concatenate higherlevel result files in a single one
					File mergedDataFile = new File(fileMappingResults.getWorkingFolder().getAbsolutePath()
							+ File.separator + experimentName + "_" + lowLevel + "_" + upperLevel + "_merged.tsv");
					FileUtils.mergeFiles(lastDataFiles.values(), mergedDataFile, true);

					// // up one level
					// lowLevelPair = upperLevelPair;
					// lowLevel = lowLevelPair.getFirstelement();
					// upperLevelPair = fileMappingResults
					// .getNextAvailableLevel(upperLevel);
					// upperLevel = upperLevelPair.getFirstelement();
					// relatFile = lowLevelPair.getSecondElement();
					IntegrationResultWrapper integrationResult = integrate(lowLevel, upperLevel, relatFile,
							mergedDataFile, null, experimentName, null);

					result.addIntegrationResult(integrationResult);
					// keep the results in a Map by experiment name
					experimentIntegrationResults.put(experimentName, integrationResult);
				}
			}

			// now, merge results at experiment level if there is more than one
			// experiment
			// lowLevelPair = upperLevelPair;
			// lowLevel = lowLevelPair.getFirstelement();
			// upperLevelPair =
			// fileMappingResults.getNextAvailableLevel(upperLevel);
			// upperLevel = upperLevelPair.getFirstelement();

			// first integration without relation file (option -C)
			// for (String experimentName :
			// experimentIntegrationResults.keySet()) {
			// dataFile =
			// experimentIntegrationResults.get(experimentName).getHigherLevelDataFile();
			// final IntegrationResultWrapper integrationResult =
			// integrate(lowLevel, upperLevel, null, dataFile,
			// null, experimentName, null);
			// experimentIntegrationResults.put(experimentName,
			// integrationResult);
			// }
			if (experimentAndReplicateNames.size() > 1) {
				// merge all higher data files
				Set<File> higherLevelDataResults = new HashSet<File>();
				for (IntegrationResultWrapper integrationResult : experimentIntegrationResults.values()) {
					higherLevelDataResults.add(integrationResult.getHigherLevelDataFile());
				}
				File mergedFile = new File(fileMappingResults.getWorkingFolder().getAbsolutePath() + File.separator
						+ "experiment_data_merged.tsv");
				FileUtils.mergeFiles(higherLevelDataResults, mergedFile, true);
				// integrate that in the next level
				lowLevelPair = upperLevelPair;
				lowLevel = lowLevelPair.getFirstelement();
				upperLevelPair = fileMappingResults.getNextAvailableLevel(upperLevel);
				if (upperLevelPair != null) {
					upperLevel = upperLevelPair.getFirstelement();
					final IntegrationResultWrapper integrationResult = integrate(lowLevel, upperLevel,
							lowLevelPair.getSecondElement(), mergedFile, null, "_TOTAL", null);
					result.addIntegrationResult(integrationResult);
				} else {
					upperLevel = lowLevel;
				}
				// make the last integration
				final IntegrationResultWrapper lastIntegrationResults = result.getLastIntegrationResults();
				final IntegrationResultWrapper integrationResult = integrate(lowLevel, upperLevel, null,
						lastIntegrationResults.getHigherLevelDataFile(), null, "", null);
				result.addIntegrationResult(integrationResult);
				firePropertyChange(END_ANALYSIS, null, integrationResult);
			} else {
				// make the last integration
				// integrate that in the next level
				lowLevelPair = upperLevelPair;
				lowLevel = lowLevelPair.getFirstelement();
				final IntegrationResultWrapper lastIntegrationResults = result.getLastIntegrationResults();
				final IntegrationResultWrapper integrationResult = integrate(lowLevel, upperLevel, null,
						lastIntegrationResults.getHigherLevelDataFile(), null, "", null);
				result.addIntegrationResult(integrationResult);
			}
		} catch (NextLevelException e) {
			// make the last integration
			final IntegrationResultWrapper lastIntegrationResults = result.getLastIntegrationResults();
			final IntegrationResultWrapper integrationResult = integrate(lowLevel, upperLevel, null,
					lastIntegrationResults.getHigherLevelDataFile(), null, "", null);
			result.addIntegrationResult(integrationResult);
		}
	}

	private List<String> getDataSetNames(Map<String, List<String>> experimentAndReplicateNames, String experimentName) {
		boolean onlyOneExperiment = experimentAndReplicateNames.size() == 1;
		final List<String> replicateNames = experimentAndReplicateNames.get(experimentName);
		boolean onlyOneReplicate = replicateNames.size() == 1;

		List<String> datasetNames = new ArrayList<String>();
		for (String replicateName : replicateNames) {
			String datasetName = onlyOneExperiment ? "" : experimentName;
			if (!onlyOneReplicate) {
				if (!"".endsWith(datasetName))
					datasetName = "_" + datasetName;
				datasetName = replicateName + datasetName;
			}

			datasetNames.add(datasetName);
		}
		return datasetNames;
	}

	private OutlierRemovalResultWrapper removeOutliers(Pair<Integer, File> lowLevel, Pair<Integer, File> upperLevel,
			File dataFile, File infoFile, Double fdr) throws IOException, InterruptedException, ExecutionException {
		final String msg = "Removing outliers data from level " + lowLevel.getFirstelement() + " to "
				+ upperLevel.getFirstelement() + "...";
		log.info(msg);
		firePropertyChange(OUTLIER_REMOVAL, null, msg);
		String prefix = OutlierRemovalResultWrapper.DEFAULT_OUTLIER_REMOVAL_PREFIX + lowLevel.getFirstelement() + "-"
				+ upperLevel.getFirstelement();
		CommandLine removeOutlierCommandLine = getRemoveOutliersCommandLine(lowLevel, prefix, dataFile, infoFile, fdr);

		Long exitCode = runCommand(removeOutlierCommandLine);
		if (exitCode.longValue() != 0)
			throw new IllegalArgumentException("Some error happen while outlier removal process");
		OutlierRemovalResultWrapper outliersRemovalResults = new OutlierRemovalResultWrapper(
				fileMappingResults.getWorkingFolder(), prefix);

		log.info("Outlier removal performed. New relation file at:"
				+ outliersRemovalResults.getRelatFile().getAbsolutePath());

		firePropertyChange(OUTLIER_REMOVAL_DONE, null, outliersRemovalResults);
		return outliersRemovalResults;

	}

	private IntegrationResultWrapper integrate(int lowLevel, int upperLevel, File relatFile, File dataFile,
			File infoFile, String prefix, Double forzedVariance)
					throws IOException, InterruptedException, ExecutionException {
		final String msg = "Integrating data from level " + lowLevel + " to " + upperLevel + "...";
		log.info(msg);
		if (relatFile == null) {
			log.info("Not using relationship file. Using -C option to correct protein loading error");
		} else {
			log.info("Using relationship file " + FilenameUtils.getName(relatFile.getAbsolutePath()));
		}
		log.info("Using data file  " + FilenameUtils.getName(dataFile.getAbsolutePath()));
		firePropertyChange(INTEGRATING, null, msg);
		String prefixString = lowLevel + "-" + upperLevel + "_" + prefix;
		CommandLine integratingCommandLine = getIntegrationCommandLine(relatFile, dataFile, infoFile, prefixString,
				forzedVariance);

		final Long exitCode = runCommand(integratingCommandLine);
		if (exitCode.longValue() != 0) {
			if (exitCode.longValue() == ProcessExecutor.TIMEOUT_ERROR_CODE) {
				final String message = "The process cound't finish before the timeout of " + timeout + " ms";
				log.warn(message);
				log.info("Trying to fix the problem by forzing variance to 0 (Using -f v0)");
				integratingCommandLine = getIntegrationCommandLine(relatFile, dataFile, infoFile, prefixString, 0.0);
				Long newExitCode = runCommand(integratingCommandLine);
				if (newExitCode.longValue() != 0) {
					if (newExitCode.longValue() == ProcessExecutor.TIMEOUT_ERROR_CODE) {
						log.warn(message);
						throw new IllegalArgumentException(message);
					}
					throw new IllegalArgumentException("Some error happen while integration process");
				}
			} else {
				throw new IllegalArgumentException("Some error happen while integration process");
			}
		}
		IntegrationResultWrapper integrationResults = new IntegrationResultWrapper(
				fileMappingResults.getWorkingFolder(), prefixString, lowLevel, upperLevel, fileMappingResults);

		log.info("Integration performed. Integration file at:"
				+ integrationResults.getHigherLevelDataFile().getAbsolutePath());
		log.info("Integration Variance=" + integrationResults.getIntegrationVariance());
		firePropertyChange(INTEGRATING_DONE, null, integrationResults);
		return integrationResults;
	}

	private KalibrateResultWrapper calibrate(int lowLevel, int upperLevel, File relatFile, File dataFile, String key)
			throws IOException, InterruptedException, ExecutionException {
		final String msg = "Calibrating data " + lowLevel + " - " + upperLevel + "...";

		log.info(msg);
		firePropertyChange(CALIBRATING, null, msg);
		String prefix = KalibrateResultWrapper.DEFAULT_CALIBRATED_PREFIX + lowLevel + "-" + upperLevel + key;
		CommandLine calibratingCommandLine = getCalibratingCommandLine(relatFile, dataFile, prefix);

		Long exitCode = runCommand(calibratingCommandLine);
		if (exitCode.longValue() != 0) {
			throw new IllegalArgumentException(
					"Some error happen while calibration process. You may consider to increase the timeout time by saxotInterface.setTimeout(long timeout) method");
		}
		KalibrateResultWrapper calibrationResults = new KalibrateResultWrapper(fileMappingResults.getWorkingFolder(),
				prefix);
		if (calibrationResults.getCalibratedDataFile() != null) {
			log.info("Calibration performed. Calibrated file at:"
					+ calibrationResults.getCalibratedDataFile().getAbsolutePath());
			log.info("Calibration K=" + calibrationResults.getCalibrationKConstant());
			log.info("Calibration Variance=" + calibrationResults.getCalibrationVariance());
			firePropertyChange(CALIBRATING_DONE, null, calibrationResults);
		} else {
			log.warn("Calibration of data file " + dataFile.getAbsolutePath() + " failed");
			firePropertyChange(CALIBRATING_ERROR, null, null);
		}
		return calibrationResults;
	}

	private CommandLine getCalibratingCommandLine(File relatFile, File dataFile, String prefix) {
		// klibrate -p"%directory%" -d%datafile% -r%level_1_2_file%
		// -a%level_1_2_prefix% -g
		CommandLine commandline = new CommandLine(new File(sanxotLocation) + File.separator + KLIBRATE_EXE);
		// String dataFileName = FilenameUtils.getName(fileMappingResults
		// .getDataFile().getAbsolutePath());
		String dataFileName = FilenameUtils.getName(dataFile.getAbsolutePath());
		String level1File = FilenameUtils.getName(relatFile.getAbsolutePath());

		commandline.addArgument("-d" + dataFileName);
		commandline.addArgument("-r" + level1File);
		commandline.addArgument("-a" + prefix);
		commandline.addArgument("-p" + fileMappingResults.getWorkingFolder().getAbsolutePath());
		commandline.addArgument("-m" + maxIterations);
		commandline.addArgument("-g");
		return commandline;
	}

	private CommandLine getIntegrationCommandLine(File relatFile, File dataFile, File infoFile, String prefix,
			Double forcedVariance) {
		// sanxot -d%level_1_2_prefix%_calibrated.xls -p"%directory%"
		// -r%level_1_2_file% -a%level_1_2_prefix%%outliers_sufix% -g
		CommandLine commandline = new CommandLine(new File(sanxotLocation) + File.separator + SANXOT_EXE);
		String dataFileName = FilenameUtils.getName(dataFile.getAbsolutePath());

		String infoFileName = null;
		if (infoFile != null)
			infoFileName = FilenameUtils.getName(infoFile.getAbsolutePath());

		commandline.addArgument("-d" + dataFileName);
		if (relatFile != null) {
			String level1FileName = FilenameUtils.getName(relatFile.getAbsolutePath());
			commandline.addArgument("-r" + level1FileName);
		} else {
			commandline.addArgument("-C");
		}
		if (infoFile != null) {
			commandline.addArgument("-V" + infoFileName);
			commandline.addArgument("-f");
		}
		if (forcedVariance != null) {
			commandline.addArgument("-v" + String.valueOf(forcedVariance));
			commandline.addArgument("-f");
		}
		commandline.addArgument("-a" + prefix);
		commandline.addArgument("-p" + fileMappingResults.getWorkingFolder().getAbsolutePath());
		commandline.addArgument("-g");
		return commandline;
	}

	private CommandLine getRemoveOutliersCommandLine(Pair<Integer, File> lowLevel, String prefix, File dataFile,
			File infoFile, Double fdr) {
		// sanxotsieve -d%level_1_2_prefix%_calibrated.xls -p"%directory%"
		// -r%level_1_2_file% -V%level_1_2_prefix%%outliers_sufix%_infoFile.txt
		// -a%level_1_2_prefix%-sanxotsieve-results -f%fdr_outliers_removal%
		CommandLine commandline = new CommandLine(new File(sanxotLocation) + File.separator + SANXOT_SIEVE_EXE);
		String dataFileName = FilenameUtils.getName(dataFile.getAbsolutePath());

		String level1FileName = FilenameUtils.getName(lowLevel.getSecondElement().getAbsolutePath());
		String infoFileName = FilenameUtils.getName(infoFile.getAbsolutePath());

		commandline.addArgument("-d" + dataFileName);
		commandline.addArgument("-p" + fileMappingResults.getWorkingFolder().getAbsolutePath());
		commandline.addArgument("-r" + level1FileName);
		commandline.addArgument("-V" + infoFileName);
		commandline.addArgument("-a" + prefix);
		commandline.addArgument("-f" + fdr.toString());
		return commandline;
	}

	private Long runCommand(CommandLine commandLine) throws IOException, InterruptedException, ExecutionException {
		final String commandString = commandLine.toString();
		log.info("Running: " + commandString);
		logFileWriter.write(commandString + "\n");
		firePropertyChange(STARTING_COMMAND, null, commandString);

		ProcessExecutorHandler handler = new ProcessExecutorHandler() {

			@Override
			public void onStandardOutput(String msg) {
				log.debug("OUTPUT:" + msg);

			}

			@Override
			public void onStandardError(String msg) {
				log.warn("ERROR:" + msg);

			}
		};
		final Future<Long> runProcess = ProcessExecutor.runProcess(commandLine, handler, timeout);
		while (!runProcess.isDone() && !runProcess.isCancelled()) {
			Thread.sleep(1000);
		}
		final Long processExitCode = runProcess.get();
		log.info("Process exitValue: " + processExitCode);
		firePropertyChange(END_COMMAND, null, processExitCode);
		return processExitCode;
	}

	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	/**
	 * @return the result
	 */
	public SanXotAnalysisResult getResult() {
		return result;
	}

}
