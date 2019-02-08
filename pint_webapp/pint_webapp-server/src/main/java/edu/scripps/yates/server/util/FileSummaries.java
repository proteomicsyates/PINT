package edu.scripps.yates.server.util;

import java.util.Map;

import edu.scripps.yates.shared.model.FileSummary;
import gnu.trove.map.hash.THashMap;

public class FileSummaries {
	private final Map<String, FileSummary> fileSummariesByFileID = new THashMap<String, FileSummary>();

	public FileSummary getFileSummaryByFileID(String fileID) {
		return fileSummariesByFileID.get(fileID);
	}

	public void addFileSummary(FileSummary fileSummary) {
		fileSummariesByFileID.put(fileSummary.getFileTypeBean().getId(), fileSummary);
	}
}
