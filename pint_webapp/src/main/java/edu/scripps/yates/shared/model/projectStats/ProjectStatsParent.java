package edu.scripps.yates.shared.model.projectStats;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.scripps.yates.shared.model.ExperimentalConditionBean;
import edu.scripps.yates.shared.model.MSRunBean;
import edu.scripps.yates.shared.model.SampleBean;
import edu.scripps.yates.shared.model.interfaces.HasId;

public class ProjectStatsParent {
	public static final String BEGIN_PROJECT = "BEGIN_PROJECT";

	private final String projectTag;
	private ProjectStats statsFromProject;
	private final Map<String, ProjectStats> statsFromMSRuns = new HashMap<String, ProjectStats>();
	private final Map<String, ProjectStats> statsFromConditions = new HashMap<String, ProjectStats>();
	private final Map<String, ProjectStats> statsFromSamples = new HashMap<String, ProjectStats>();

	public ProjectStats getProjectStatsFromProject() {
		if (statsFromProject == null) {
			statsFromProject = new ProjectStatsFromProjectBean();
		}
		return statsFromProject;
	}

	public List<String> getMSRunIDs() {
		List<String> list = new ArrayList<String>();
		list.addAll(statsFromMSRuns.keySet());
		Collections.sort(list);
		return list;
	}

	public List<String> getConditionIDs() {
		List<String> list = new ArrayList<String>();
		list.addAll(statsFromConditions.keySet());
		Collections.sort(list);
		return list;
	}

	public List<String> getSampleIDs() {
		List<String> list = new ArrayList<String>();
		list.addAll(statsFromSamples.keySet());
		Collections.sort(list);
		return list;
	}

	public ProjectStatsParent(String projectTag) {
		this.projectTag = projectTag;
	}

	public ProjectStats getProjectStatsByMSRun(MSRunBean msRun) {
		if (statsFromMSRuns.containsKey(msRun.getRunID())) {
			return statsFromMSRuns.get(msRun.getRunID());
		}
		final ProjectStatsFromMSRun projectStatsFromMSRun = new ProjectStatsFromMSRun(msRun);
		statsFromMSRuns.put(projectTag, projectStatsFromMSRun);
		return projectStatsFromMSRun;
	}

	public ProjectStats getProjectStatsBySample(SampleBean sample) {
		if (statsFromSamples.containsKey(sample.getId())) {
			return statsFromSamples.get(sample.getId());
		}
		final ProjectStats projectStatsFromSample = new ProjectStatsFromSample(sample);
		statsFromSamples.put(projectTag, projectStatsFromSample);
		return projectStatsFromSample;
	}

	public ProjectStats getProjectStatsByCondition(ExperimentalConditionBean condition) {
		if (statsFromConditions.containsKey(condition.getId())) {
			return statsFromConditions.get(condition.getId());
		}
		final ProjectStatsFromExperimentalCondition projectStatsFromCondition = new ProjectStatsFromExperimentalCondition(
				condition);
		statsFromConditions.put(projectTag, projectStatsFromCondition);
		return projectStatsFromCondition;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(BEGIN_PROJECT).append("\t").append(projectTag).append("\n");
		sb.append(statsFromProject).append("\n");

		final Collection<ProjectStats> statsFromConditionsCollection = statsFromConditions.values();
		for (ProjectStats projectStats : statsFromConditionsCollection) {
			sb.append(projectStats).append("\n");
		}
		final Collection<ProjectStats> statsFromMSRunsCollection = statsFromMSRuns.values();
		for (ProjectStats projectStats : statsFromMSRunsCollection) {
			sb.append(projectStats).append("\n");
		}
		final Collection<ProjectStats> statsFromSamplesCollection = statsFromSamples.values();
		for (ProjectStats projectStats : statsFromSamplesCollection) {
			sb.append(projectStats).append("\n");
		}

		return sb.toString();
	}

	public void addProjectStat(ProjectStats<HasId> stats) {
		switch (stats.getType()) {
		case CONDITION:
			statsFromConditions.put(stats.getT().getId(), stats);
			break;
		case MSRUN:
			statsFromMSRuns.put(stats.getT().getId(), stats);
			break;
		case PROJECT:
			statsFromProject = stats;
			break;
		case SAMPLE:
			statsFromSamples.put(stats.getT().getId(), stats);
			break;
		default:
			break;
		}

	}
}
