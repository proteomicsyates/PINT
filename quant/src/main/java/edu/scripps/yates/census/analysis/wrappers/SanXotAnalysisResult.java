package edu.scripps.yates.census.analysis.wrappers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.census.analysis.FileMappingResults;

public class SanXotAnalysisResult {
	private KalibrateResultWrapper kalibrationResult;
	private final List<IntegrationResultWrapper> integrationResults = new ArrayList<IntegrationResultWrapper>();

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
			String ret = "SanXotAnalysisResult:\n" + "\tkalibration:\n"
					+ "\t\t K=" + kalibrationResult.getCalibrationKConstant()
					+ "\n" + "\t\t V="
					+ kalibrationResult.getCalibrationVariance() + "\n\n";
			ret += "\tIntegration:\n";
			ret += "\t" + integrationResults.size() + " levels analyzed\n";
			for (IntegrationResultWrapper integrationResult : integrationResults) {
				if (integrationResult.getOutlierRemovalResult() != null) {
					ret += integrationResult.getOutlierRemovalResult()
							.toString();
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
}
