package edu.scripps.yates.shared.tasks;

import java.util.Collection;

import edu.scripps.yates.shared.util.SharedDataUtil;

public class SharedTaskKeyGenerator {
	public static String getKeyForGetDownloadLinkFromProteinGroups(String projectName,
			boolean separateNonConclusiveProteins) {
		return "GetDownloadLinkFromProteinGroups: " + projectName + Boolean.valueOf(separateNonConclusiveProteins);
	}

	public static String getKeyForGetDownloadLinkFromProteins(String projectTag) {
		return "GetDownloadLinkFromProteins: " + projectTag;
	}

	public static String getKeyForGetProteinsFromProjectTask(String projectTag, String uniprotVersion) {
		return "GetProteinsFromProjectTask: " + projectTag + uniprotVersion;
	}

	public static String getKeyForGetProteinsFromQuery(Collection<String> projectTags, String queryInOrder) {
		return "GetProteinsFromQuery: " + SharedDataUtil.getProjectTagCollectionKey(projectTags) + queryInOrder;
	}

	public static String getKeyForGetPSMsFromProjectTask(String projectTag) {
		return "GetPSMsFromProjectTask: " + projectTag;
	}

	public static String getKeyForGetRandomProteinsAccessionsFromCensusChroFileTask(String filePath, long fileLenth,
			String discardDecoyExpression) {
		return "GetRandomProteinsAccessionsFromCensusChroFileTask: " + filePath + fileLenth + discardDecoyExpression;
	}

	public static String getKeyForGetRandomProteinsAccessionsFromCensusOutFileTask(String filePath, long fileLenth,
			String discardDecoyExpression) {
		return "GetRandomProteinsAccessionsFromCensusOutFileTask: " + filePath + fileLenth + discardDecoyExpression;
	}

	public static String getKeyForGetRandomProteinsAccessionsFromDTASelectFileTask(String filePath, long fileLenth,
			String discardDecoyExpression) {
		return "GetRandomProteinsAccessionsFromDTASelectFileTask: " + filePath + fileLenth + discardDecoyExpression;
	}

	public static String getKeyForGetDownloadLinkFromProteinGroupsFromQuery(String projectName,
			boolean separateNonConclusiveProteins) {

		return "GetDownloadLinkFromProteinGroupsFromQuery: " + projectName
				+ Boolean.valueOf(separateNonConclusiveProteins);
	}

	public static String getKeyForGetDownloadLinkFromProteinsFromQuery(String projectName) {

		return "GetDownloadLinkFromProteinsFromQuery: " + projectName;
	}
}
