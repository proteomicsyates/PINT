package edu.scripps.yates.census.analysis;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.scripps.yates.census.analysis.QuantAnalysis.ANALYSIS_LEVEL_OUTCOME;
import edu.scripps.yates.utilities.maths.Maths;
import edu.scripps.yates.utilities.util.Pair;

public class FileMappingResults {
	private static final String SANXOT_RUN_FOLDER_NAME = "sanxot";
	private final File workingFolder;
	private final ANALYSIS_LEVEL_OUTCOME analysisOutcome;
	private final Map<Integer, String> fileNamesByLevels = new HashMap<Integer, String>();
	// for all outcomes
	public static final String DATA_FILE = "dataFile.tsv";
	public static final String ION_TO_SPECTRUM_1 = "1-ion-spectrum.tsv";
	public static final String SPECTRUM_TO_PEPTIDE_REPLICATE_EXPERIMENT_2 = "2-spectrum-peptide.tsv";
	// for peptide outcome
	public static final String PEPTIDE_REPLICATE_EXPERIMENT_TO_PEPTIDE_EXPERIMENT_3 = "3-peptide_rep_exp-peptide_exp.tsv";
	public static final String PEPTIDE_EXPERIMENT_TO_PEPTIDE_4 = "4-peptide_exp-peptide.tsv";
	public static final String PEPTIDE_TO_ALL_5 = "5-peptide-all.tsv";
	// for protein outcome
	public static final String PEPTIDE_REPLICATE_EXPERIMENT_TO_PROTEIN_REPLICATE_EXPERIMENT_3 = "3-peptide-protein.tsv";
	public static final String PROTEIN_REPLICATE_EXPERIMENT_TO_PROTEIN_EXPERIMENT_4 = "4-protein_rep_exp-protein_exp.tsv";
	public static final String PROTEIN_EXPERIMENT_TO_PROTEIN_5 = "5-protein_exp-protein.tsv";
	public static final String PROTEIN_TO_ALL_6 = "6-protein-all.tsv";
	// for protein group outcome
	public static final String PEPTIDE_REPLICATE_EXPERIMENT_TO_PROTEINGROUP_REPLICATE_EXPERIMENT_3 = "3-peptide-proteinGroup.tsv";
	public static final String PROTEINGROUP_REPLICATE_EXPERIMENT_TO_PROTEINGROUP_EXPERIMENT_4 = "4-proteinGroup_rep_exp-proteinGroup_exp.tsv";
	public static final String PROTEINGROUP_EXPERIMENT_TO_PROTEINGROUP_5 = "5-proteinGroup_exp-proteinGroup.tsv";
	public static final String PROTEINGROUP_TO_ALL_6 = "6-proteinGroup-all.tsv";
	// for protein cluster outcome
	public static final String PEPTIDE_TO_PROTEIN_CLUSTER_5 = "5-peptide-proteinCluster.tsv";
	public static final String PROTEIN_CLUSTER_TO_ALL_6 = "6-proteinCluster-all.tsv";

	// maximum level
	private int MAX_LEVEL = 6;
	private final Map<String, List<String>> replicateAndExperimentNames;

	public FileMappingResults(QuantificationType quantType, File workingFolder, ANALYSIS_LEVEL_OUTCOME analysisOutcome,
			Map<String, List<String>> replicateAndExperimentNames) {
		this.replicateAndExperimentNames = replicateAndExperimentNames;

		switch (analysisOutcome) {
		case PROTEINGROUP:
			if (quantType == QuantificationType.ISOTOPOLOGUES) {
				fileNamesByLevels.put(1, ION_TO_SPECTRUM_1);
			}
			fileNamesByLevels.put(2, SPECTRUM_TO_PEPTIDE_REPLICATE_EXPERIMENT_2);
			fileNamesByLevels.put(3, PEPTIDE_REPLICATE_EXPERIMENT_TO_PROTEINGROUP_REPLICATE_EXPERIMENT_3);
			fileNamesByLevels.put(4, PROTEINGROUP_REPLICATE_EXPERIMENT_TO_PROTEINGROUP_EXPERIMENT_4);
			fileNamesByLevels.put(5, PROTEINGROUP_EXPERIMENT_TO_PROTEINGROUP_5);
			fileNamesByLevels.put(6, PROTEINGROUP_TO_ALL_6);
			MAX_LEVEL = 6;
			break;
		case PROTEIN:
			if (quantType == QuantificationType.ISOTOPOLOGUES) {
				fileNamesByLevels.put(1, ION_TO_SPECTRUM_1);
			}
			fileNamesByLevels.put(2, SPECTRUM_TO_PEPTIDE_REPLICATE_EXPERIMENT_2);
			fileNamesByLevels.put(3, PEPTIDE_REPLICATE_EXPERIMENT_TO_PROTEIN_REPLICATE_EXPERIMENT_3);
			fileNamesByLevels.put(4, PROTEIN_REPLICATE_EXPERIMENT_TO_PROTEIN_EXPERIMENT_4);
			fileNamesByLevels.put(5, PROTEIN_EXPERIMENT_TO_PROTEIN_5);
			fileNamesByLevels.put(6, PROTEIN_TO_ALL_6);
			MAX_LEVEL = 6;
			break;
		case PEPTIDE:
			if (quantType == QuantificationType.ISOTOPOLOGUES) {
				fileNamesByLevels.put(1, ION_TO_SPECTRUM_1);
			}
			fileNamesByLevels.put(2, SPECTRUM_TO_PEPTIDE_REPLICATE_EXPERIMENT_2);
			fileNamesByLevels.put(3, PEPTIDE_REPLICATE_EXPERIMENT_TO_PEPTIDE_EXPERIMENT_3);
			fileNamesByLevels.put(4, PEPTIDE_EXPERIMENT_TO_PEPTIDE_4);
			fileNamesByLevels.put(5, PEPTIDE_TO_ALL_5);
			MAX_LEVEL = 5;
			break;
		case PROTEIN_CLUSTER:
			if (quantType == QuantificationType.ISOTOPOLOGUES) {
				fileNamesByLevels.put(1, ION_TO_SPECTRUM_1);
			}
			fileNamesByLevels.put(2, SPECTRUM_TO_PEPTIDE_REPLICATE_EXPERIMENT_2);
			fileNamesByLevels.put(3, PEPTIDE_REPLICATE_EXPERIMENT_TO_PEPTIDE_EXPERIMENT_3);
			fileNamesByLevels.put(4, PEPTIDE_EXPERIMENT_TO_PEPTIDE_4);
			fileNamesByLevels.put(5, PEPTIDE_TO_PROTEIN_CLUSTER_5);
			fileNamesByLevels.put(6, PROTEIN_CLUSTER_TO_ALL_6);
			MAX_LEVEL = 6;
			break;
		case FORCED_CLUSTERS:
			if (quantType == QuantificationType.ISOTOPOLOGUES) {
				fileNamesByLevels.put(1, ION_TO_SPECTRUM_1);
			}
			fileNamesByLevels.put(2, SPECTRUM_TO_PEPTIDE_REPLICATE_EXPERIMENT_2);
			fileNamesByLevels.put(3, PEPTIDE_REPLICATE_EXPERIMENT_TO_PEPTIDE_EXPERIMENT_3);
			fileNamesByLevels.put(4, PEPTIDE_EXPERIMENT_TO_PEPTIDE_4);
			fileNamesByLevels.put(5, PEPTIDE_TO_PROTEIN_CLUSTER_5);
			fileNamesByLevels.put(6, PROTEIN_CLUSTER_TO_ALL_6);
			MAX_LEVEL = 6;
			break;
		default:
			break;
		}
		this.workingFolder = workingFolder;
		this.analysisOutcome = analysisOutcome;
	}

	/**
	 * This model is not correcting the protein loading error by default. When
	 * integrating protein_rep to protein (or protein_rep_exp to protein_exp),
	 * an additional step is needed.<br>
	 * This funtion will return true if the level provided by the parameter
	 * corresponds to the level in which for the current
	 * {@link ANALYSIS_LEVEL_OUTCOME} the additional step is needed.
	 *
	 * @return
	 */
	public boolean isDataMergingNeeded(int level) {
		final String fileName = fileNamesByLevels.get(level);
		switch (analysisOutcome) {
		case PROTEINGROUP:
			if (fileName.equals(PROTEINGROUP_REPLICATE_EXPERIMENT_TO_PROTEINGROUP_EXPERIMENT_4))
				return true;
			break;
		case PROTEIN:
			if (fileName.equals(PROTEIN_REPLICATE_EXPERIMENT_TO_PROTEIN_EXPERIMENT_4))
				return true;
			if (fileName.equals(PROTEIN_EXPERIMENT_TO_PROTEIN_5))
				return true;
			break;
		case PEPTIDE:
			if (fileName.equals(PEPTIDE_REPLICATE_EXPERIMENT_TO_PEPTIDE_EXPERIMENT_3))
				return true;
			if (fileName.equals(PEPTIDE_EXPERIMENT_TO_PEPTIDE_4))
				return true;
			break;
		case PROTEIN_CLUSTER:
			if (fileName.equals(PEPTIDE_REPLICATE_EXPERIMENT_TO_PEPTIDE_EXPERIMENT_3))
				return true;
			if (fileName.equals(PEPTIDE_EXPERIMENT_TO_PEPTIDE_4))
				return true;
			break;
		case FORCED_CLUSTERS:
			if (fileName.equals(PEPTIDE_REPLICATE_EXPERIMENT_TO_PEPTIDE_EXPERIMENT_3))
				return true;
			if (fileName.equals(PEPTIDE_EXPERIMENT_TO_PEPTIDE_4))
				return true;
			break;
		default:
			break;
		}
		return false;
	}

	public String getFileNameByLevel(int level) {
		return fileNamesByLevels.get(level);
	}

	/**
	 * Gets a Pair with the key=level and the corresponding file.<br>
	 * This will ensure that even if we don't have the level 1, that is,
	 * ion_spectrum, this function will return the next available level in the
	 * analysis.
	 *
	 * @return
	 */
	public Pair<Integer, File> getFirstLevel() {
		for (int i = 1; i < MAX_LEVEL; i++) {
			File file = getFileLevel(i);
			if (file != null && file.exists()) {
				Pair<Integer, File> ret = new Pair<Integer, File>(i, file);
				return ret;
			}
		}
		return null;
	}

	public Pair<Integer, File> getFilePairLevel(int level) {
		final File file = new File(workingFolder.getAbsolutePath() + File.separator + fileNamesByLevels.get(level));
		if (file.exists()) {
			Pair<Integer, File> ret = new Pair<Integer, File>(level, file);
			return ret;
		}
		return null;
	}

	public File getFileLevel(int level) {
		final File file = new File(workingFolder.getAbsolutePath() + File.separator + fileNamesByLevels.get(level));
		if (file.exists())
			return file;
		return null;
	}

	public File getDataFile() {
		final File file = new File(workingFolder.getAbsolutePath() + File.separator + DATA_FILE);
		if (file.exists())
			return file;
		return null;
	}

	/**
	 * @return the workingFolder
	 */
	public File getWorkingFolder() {
		return workingFolder;
	}

	public File getSanXotWorkingFolder() {
		final File folder = new File(workingFolder.getAbsoluteFile() + File.separator + SANXOT_RUN_FOLDER_NAME);
		if (!folder.exists()) {
			folder.mkdirs();
		} else {
			// remove all files
			final File[] listFiles = folder.listFiles();
			for (File file : listFiles) {
				file.delete();
			}
		}
		return folder;
	}

	/**
	 * Gets the next available level just after the level is provided in the
	 * parameter
	 *
	 * @param firstelement
	 * @return
	 * @throws NextLevelException
	 */
	public Pair<Integer, File> getNextAvailableLevel(Integer firstelement) throws NextLevelException {
		for (int i = firstelement + 1; i <= MAX_LEVEL; i++) {
			File file = getFileLevel(i);
			if (file != null && file.exists()) {
				Pair<Integer, File> ret = new Pair<Integer, File>(i, file);
				return ret;
			}
		}
		throw new NextLevelException(
				"There is not next level for " + firstelement + " since the MAX level is " + MAX_LEVEL);

	}

	public int getMaxLevel() {
		return Maths.max(fileNamesByLevels.keySet().toArray(new Integer[0]));
	}

	/**
	 * Returns a Map in which the keys are the names of the experiments and the
	 * values are the list of the names of the replicates for each experiment
	 *
	 * @return
	 */
	public Map<String, List<String>> getExperimentAndReplicateNames() {
		return replicateAndExperimentNames;
	}

}
