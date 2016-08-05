package edu.scripps.yates.census.analysis.wrappers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.scripps.yates.census.analysis.FileMappingResults;

public class SanXotAnalysisResult {
	private final static Logger log = Logger.getLogger(SanXotAnalysisResult.class);
	private KalibrateResultWrapper kalibrationResult;
	private final List<IntegrationResultWrapper> integrationResults = new ArrayList<IntegrationResultWrapper>();
	private final HashMap<String, IntegrationResultWrapper> experimentIntegrationResults = new HashMap<String, IntegrationResultWrapper>();
	private final HashMap<String, Map<String, IntegrationResultWrapper>> replicateByExperimentIntegrationResults = new HashMap<String, Map<String, IntegrationResultWrapper>>();

	private final FileMappingResults fileMappingResult;

	public SanXotAnalysisResult(FileMappingResults fileMappingResult) {
		this.fileMappingResult = fileMappingResult;
	}

	/**
	 * @return the kalibrationResult
	 */
	public KalibrateResultWrapper getKalibrationResult() {
		return kalibrationResult;
	}

	/**
	 * @param kalibrationResult
	 *            the kalibrationResult to set
	 */
	public void setKalibrationResult(KalibrateResultWrapper kalibrationResult) {
		this.kalibrationResult = kalibrationResult;
	}

	/**
	 * @return the integrationResults
	 */
	public List<IntegrationResultWrapper> getIntegrationResults() {
		return integrationResults;
	}

	public void addIntegrationResult(IntegrationResultWrapper integrationResult) {
		integrationResults.add(integrationResult);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		try {
			String ret = "SanXotAnalysisResult:\n" + "\tkalibration:\n" + "\t\t K="
					+ kalibrationResult.getCalibrationKConstant() + "\n" + "\t\t V="
					+ kalibrationResult.getCalibrationVariance() + "\n\n";
			ret += "\tIntegration:\n";
			ret += "\t" + integrationResults.size() + " levels analyzed\n";
			for (IntegrationResultWrapper integrationResult : integrationResults) {
				if (integrationResult.getOutlierRemovalResult() != null) {
					ret += integrationResult.getOutlierRemovalResult().toString();
				}
				ret += integrationResult.toString();
			}

			return ret;
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}

	}

	public IntegrationResultWrapper getLastIntegrationResults() {
		final int size = integrationResults.size();
		return integrationResults.get(size - 1);

	}

	/**
	 * @return the fileMappingResult
	 */
	public FileMappingResults getFileMappingResult() {
		return fileMappingResult;
	}

	public void addExperimentIntegrationResult(IntegrationResultWrapper integrationResult, String experimentName) {
		log.info("Adding experiment level integration results for experiment " + experimentName);
		experimentIntegrationResults.put(experimentName, integrationResult);
		addIntegrationResult(integrationResult);
	}

	public void addReplicateExperimentIntegrationResult(IntegrationResultWrapper integrationResult,
			String experimentName, String replicateName) {
		log.info("Adding replicate level integration results for experiment " + experimentName + " and replicate "
				+ replicateName);

		Map<String, IntegrationResultWrapper> replicateIntegrationResults = null;
		if (replicateByExperimentIntegrationResults.containsKey(experimentName)) {
			replicateIntegrationResults = replicateByExperimentIntegrationResults.get(experimentName);
		} else {
			replicateIntegrationResults = new HashMap<String, IntegrationResultWrapper>();
			replicateByExperimentIntegrationResults.put(experimentName, replicateIntegrationResults);
		}
		replicateIntegrationResults.put(replicateName, integrationResult);
		addIntegrationResult(integrationResult);
	}

	public IntegrationResultWrapper getIntegrationResultForExperiment(String experimentName) {
		return experimentIntegrationResults.get(experimentName);
	}

	public HashMap<String, IntegrationResultWrapper> getExperimentIntegrationResults() {
		return experimentIntegrationResults;
	}

	/**
	 * Map by experiment. Returning a map by replicate.
	 *
	 * @return
	 */
	public Map<String, Map<String, IntegrationResultWrapper>> getReplicateIntegrationResultsByExperiment() {
		return replicateByExperimentIntegrationResults;
	}

	public IntegrationResultWrapper getReplicateIntegrationResultsByExperiment(String experimentName,
			String replicateName) {
		if (replicateByExperimentIntegrationResults.containsKey(experimentName)) {
			if (replicateByExperimentIntegrationResults.get(experimentName).containsKey(replicateName)) {
				return replicateByExperimentIntegrationResults.get(experimentName).get(replicateName);
			}
		}
		return null;
	}
}
