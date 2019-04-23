package edu.scripps.yates.shared.tasks;

import java.util.Collection;

import edu.scripps.yates.shared.util.SharedDataUtil;

public class TaskKeyGenerator {
	public static TaskKey getKeyForGetDownloadLinkFromProteinGroups(String projectName,
			boolean separateNonConclusiveProteins) {
		return TaskKey.createKey(
				"GetDownloadLinkFromProteinGroups: " + projectName + Boolean.valueOf(separateNonConclusiveProteins));
	}

	public static TaskKey getKeyForGetDownloadLinkFromProteins(String projectTag) {
		return TaskKey.createKey("GetDownloadLinkFromProteins: " + projectTag);
	}

	public static TaskKey getKeyForGetProteinsFromProjectTask(String projectTag, String uniprotVersion) {
		return TaskKey.createKey("GetProteinsFromProjectTask: " + projectTag + " " + uniprotVersion);
	}

	public static TaskKey getKeyForGetProteinsFromQuery(Collection<String> projectTags, String queryText) {
		return TaskKey.createKey("GetProteinsFromQuery: " + SharedDataUtil.getProjectTagCollectionKey(projectTags) + " "
				+ queryText.toLowerCase());
	}

	public static TaskKey getKeyForGetProteinsFromQuery(Collection<String> projectTags) {
		return TaskKey.createKey("GroupingProteins: " + SharedDataUtil.getProjectTagCollectionKey(projectTags));
	}

	public static TaskKey getKeyForGetPSMsFromProjectTask(String projectTag) {
		return TaskKey.createKey("GetPSMsFromProjectTask: " + projectTag);
	}

	public static TaskKey getKeyForCancellingTask() {
		return TaskKey.createKey("cancelling task: ");
	}

	public static TaskKey getKeyForGetRandomProteinsAccessionsFromCensusChroFileTask(String filePath, long fileLenth,
			String discardDecoyExpression) {
		return TaskKey.createKey("GetRandomProteinsAccessionsFromCensusChroFileTask: " + filePath + " " + fileLenth
				+ " " + discardDecoyExpression);
	}

	public static TaskKey getKeyForGetRandomProteinsAccessionsFromCensusOutFileTask(String filePath, long fileLenth,
			String discardDecoyExpression) {
		return TaskKey.createKey("GetRandomProteinsAccessionsFromCensusOutFileTask: " + filePath + " " + fileLenth + " "
				+ discardDecoyExpression);
	}

	public static TaskKey getKeyForGetRandomProteinsAccessionsFromDTASelectFileTask(String filePath, long fileLenth,
			String discardDecoyExpression) {
		return TaskKey.createKey("GetRandomProteinsAccessionsFromDTASelectFileTask: " + filePath + " " + fileLenth + " "
				+ discardDecoyExpression);
	}

	public static TaskKey getKeyForShowPeptidesSharedByProteinsTask(String proteinAccessions) {
		return TaskKey.createKey("showPeptidesSharedbyProteinsTask: " + proteinAccessions);
	}

	public static TaskKey getKeyForGetDownloadLinkFromProteinGroupsFromQuery(String projectName,
			boolean separateNonConclusiveProteins) {

		return TaskKey.createKey("GetDownloadLinkFromProteinGroupsFromQuery: " + projectName + " "
				+ Boolean.valueOf(separateNonConclusiveProteins));
	}

	public static TaskKey getKeyForGetDownloadLinkFromProteinsFromQuery(String projectName) {

		return TaskKey.createKey("GetDownloadLinkFromProteinsFromQuery: " + projectName);
	}

	public static TaskKey getKeyForGetDownloadLinkForInputFilesOfProjectTask(String projectName) {
		return TaskKey.createKey("GetDownloadLinkForInputFilesOfProjectTask: " + projectName);
	}
}
