package edu.scripps.yates.client.gui.reactome;

import org.reactome.web.analysis.client.model.AnalysisResult;

public interface AnalysisPerformedHandler {

	void onAnalysisPerformed(AnalysisResult result);

	void onAnalysisError(Throwable exception);

}
