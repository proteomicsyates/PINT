package edu.scripps.yates.server.projectStats;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.hibernate.HibernateException;

import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.server.ProjectLocker;
import edu.scripps.yates.server.util.FileManager;
import edu.scripps.yates.shared.model.ExperimentalConditionBean;
import edu.scripps.yates.shared.model.MSRunBean;
import edu.scripps.yates.shared.model.SampleBean;
import edu.scripps.yates.shared.model.projectStats.HasIdImpl;
import edu.scripps.yates.shared.model.projectStats.ProjectStats;
import edu.scripps.yates.shared.model.projectStats.ProjectStatsImpl;
import edu.scripps.yates.shared.model.projectStats.ProjectStatsParent;
import edu.scripps.yates.shared.model.projectStats.ProjectStatsType;

public class ProjectStatsManager {
	private final static Logger log = Logger.getLogger(ProjectStatsManager.class);
	private static final String PROJECT_STATS_LOCK = "PROJECT_STATS_LOCKS";
	private static ProjectStatsManager instance;
	private final File file;
	private boolean loaded = false;
	private final Map<String, ProjectStatsParent> map = new HashMap<String, ProjectStatsParent>();
	private ProjectStats generalProjectsStats;

	private ProjectStatsManager(File file) {
		this.file = file;
		loadFile();
	}

	private void loadFile() {
		// wait if someone is trying to access the file
		final Method method = new Object() {
		}.getClass().getEnclosingMethod();
		ProjectLocker.lock(PROJECT_STATS_LOCK, method);
		try {
			if (!file.exists()) {
				return;
			}
			List<String> readAllLines = Files.readAllLines(Paths.get(file.getAbsolutePath()), Charset.defaultCharset());
			String projectTag = null;
			for (String line : readAllLines) {
				line = line.trim();
				if ("".equals(line)) {
					continue;
				}
				if (line.startsWith(ProjectStatsParent.BEGIN_PROJECT)) {
					projectTag = line.split("\t")[1];
					ProjectStatsParent statsParent = new ProjectStatsParent(projectTag);
					map.put(projectTag, statsParent);
				} else {

					// first comes the type, then the id, and then the stats
					final String[] split = line.split("\t");
					final ProjectStatsType projectStatsType = ProjectStatsType.valueOf(split[0]);
					String id = split[1];
					Integer numConditions = getNumber(split[2]);
					Integer numMSRuns = getNumber(split[3]);
					Integer numSamples = getNumber(split[4]);
					Integer numProteins = getNumber(split[5]);
					Integer numGenes = getNumber(split[6]);
					Integer numPeptides = getNumber(split[7]);
					Integer numPSMs = getNumber(split[8]);

					ProjectStats stats = new ProjectStatsImpl(new HasIdImpl(id), projectStatsType);
					stats.setNumConditions(numConditions);
					stats.setNumGenes(numGenes);
					stats.setNumMSRuns(numMSRuns);
					stats.setNumPeptides(numPeptides);
					stats.setNumProteins(numProteins);
					stats.setNumPSMs(numPSMs);
					stats.setNumSamples(numSamples);
					if (projectTag != null) {
						map.get(projectTag).addProjectStat(stats);
					} else {
						generalProjectsStats = stats;
					}
				}
			}
			loaded = true;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Error while reading stats file: " + e.getMessage());
		} finally {
			ProjectLocker.unlock(PROJECT_STATS_LOCK, method);
		}
	}

	private Integer getNumber(String string) {
		if (string != null && !"".equals(string)) {
			try {
				return Integer.valueOf(string);
			} catch (NumberFormatException e) {

			}
		}
		return null;
	}

	public static ProjectStatsManager getInstance() {
		if (instance == null) {
			File file = FileManager.getProjectStatsFile();
			instance = new ProjectStatsManager(file);
		}
		instance.loadFile();
		return instance;
	}

	public Integer getNumSamples(String projectTag, MSRunBean msRun) throws HibernateException, IOException {
		ProjectStats stats = getProjectStatsFromMSRun(projectTag, msRun);
		if (stats.getNumSamples() == null) {
			final List list = PreparedCriteria.getCriteriaForSamplesInProjectInMSRun(projectTag, msRun.getId()).list();
			stats.setNumSamples(list.size());
			updateFile();
		}
		return stats.getNumSamples();
	}

	private void updateFile() {
		// wait until someone release the lock
		final Method method = new Object() {
		}.getClass().getEnclosingMethod();
		ProjectLocker.lock(PROJECT_STATS_LOCK, method);
		try {
			List<String> projectList = new ArrayList<String>();
			projectList.addAll(map.keySet());
			Collections.sort(projectList);
			FileWriter fw = null;

			try {
				fw = new FileWriter(file);
				if (generalProjectsStats == null) {
					generalProjectsStats = new ProjectStatsImpl<T>(null, ProjectStatsType.PINT);
				}
				fw.write(generalProjectsStats.toString() + "\n");
				for (String projectTag : projectList) {
					fw.write(map.get(projectTag).toString());
					fw.write("\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Some error happened while updating stats file: " + e.getMessage());
			} finally {
				if (fw != null) {
					try {
						fw.close();
					} catch (IOException e) {
						e.printStackTrace();
						log.error("Error while closing stats file: " + e.getMessage());
					}
				}
			}
		} finally {
			ProjectLocker.unlock(PROJECT_STATS_LOCK, method);
		}
	}

	private ProjectStats getProjectStatsFromProject(String projectTag) {
		if (!map.containsKey(projectTag)) {
			map.put(projectTag, new ProjectStatsParent(projectTag));
		}
		return map.get(projectTag).getProjectStatsFromProject();
	}

	private ProjectStats getProjectStatsFromMSRun(String projectTag, MSRunBean msRun) {
		if (!map.containsKey(projectTag)) {
			map.put(projectTag, new ProjectStatsParent(projectTag));
		}
		return map.get(projectTag).getProjectStatsByMSRun(msRun);
	}

	private ProjectStats getProjectStatsFromCondition(String projectTag, ExperimentalConditionBean condition) {
		if (!map.containsKey(projectTag)) {
			map.put(projectTag, new ProjectStatsParent(projectTag));
		}
		return map.get(projectTag).getProjectStatsByCondition(condition);
	}

	private ProjectStats getProjectStatsFromSample(String projectTag, SampleBean sample) {
		if (!map.containsKey(projectTag)) {
			map.put(projectTag, new ProjectStatsParent(projectTag));
		}
		return map.get(projectTag).getProjectStatsBySample(sample);
	}

	public int getNumConditions(String projectTag, MSRunBean msRun) {
		final ProjectStats projectStatsFromMSRun = getProjectStatsFromMSRun(projectTag, msRun);
		if (projectStatsFromMSRun.getNumConditions() == null) {
			final List list = PreparedCriteria.getCriteriaForConditionsInProjectInMSRun(projectTag, msRun.getId())
					.list();
			projectStatsFromMSRun.setNumConditions(list.size());
			updateFile();
		}

		return projectStatsFromMSRun.getNumConditions();
	}

	public int getNumMSRuns(String projectTag, SampleBean sample) {
		final ProjectStats projectStatsFromSample = getProjectStatsFromSample(projectTag, sample);
		if (projectStatsFromSample.getNumMSRuns() == null) {
			Integer numMSRuns = PreparedCriteria.getCriteriaForMSRunsInProjectInSample(projectTag, sample.getId())
					.list().size();
			projectStatsFromSample.setNumMSRuns(numMSRuns);
			updateFile();
		}
		return projectStatsFromSample.getNumMSRuns();
	}

	public int getNumGenes(String projectTag, SampleBean sample) {
		final ProjectStats projectStatsFromSample = getProjectStatsFromSample(projectTag, sample);
		if (projectStatsFromSample.getNumGenes() == null) {
			Integer numGenes = PreparedCriteria.getCriteriaForGenesInProjectInSample(projectTag, sample.getId()).list()
					.size();
			projectStatsFromSample.setNumGenes(numGenes);
			updateFile();
		}
		return projectStatsFromSample.getNumGenes();

	}

	public int getNumPSMs(String projectTag, SampleBean sample) {
		final ProjectStats projectStatsFromSample = getProjectStatsFromSample(projectTag, sample);
		if (projectStatsFromSample.getNumPSMs() == null) {
			Integer numPSMs = PreparedCriteria.getCriteriaForDifferentPSMsInProjectInSample(projectTag, sample.getId())
					.list().size();
			projectStatsFromSample.setNumPSMs(numPSMs);
			updateFile();
		}
		return projectStatsFromSample.getNumPSMs();
	}

	public int getNumDifferentPeptides(String projectTag, SampleBean sample) {
		final ProjectStats projectStatsFromSample = getProjectStatsFromSample(projectTag, sample);
		if (projectStatsFromSample.getNumPeptides() == null) {
			Integer numPeptides = PreparedCriteria
					.getCriteriaForDifferentPeptidesInProjectInSample(projectTag, sample.getId()).list().size();
			projectStatsFromSample.setNumPeptides(numPeptides);
			updateFile();
		}
		return projectStatsFromSample.getNumPeptides();
	}

	public int getNumDifferentProteins(String projectTag, SampleBean sample) {
		final ProjectStats projectStatsFromSample = getProjectStatsFromSample(projectTag, sample);
		if (projectStatsFromSample.getNumProteins() == null) {
			Integer numProteins = PreparedCriteria
					.getCriteriaForProteinPrimaryAccsInProjectInSample(projectTag, sample.getId()).list().size();
			projectStatsFromSample.setNumProteins(numProteins);
			updateFile();
		}
		return projectStatsFromSample.getNumProteins();

	}

	public int getNumMSRuns(String projectTag, ExperimentalConditionBean condition) {
		final ProjectStats projectStatsFromCondition = getProjectStatsFromCondition(projectTag, condition);
		if (projectStatsFromCondition.getNumMSRuns() == null) {
			Integer numMSRuns = PreparedCriteria.getCriteriaForMSRunsInProjectInCondition(projectTag, condition.getId())
					.list().size();
			projectStatsFromCondition.setNumMSRuns(numMSRuns);
			updateFile();
		}
		return projectStatsFromCondition.getNumMSRuns();
	}

	public int getNumGenes(String projectTag, ExperimentalConditionBean condition) {
		final ProjectStats projectStatsFromCondition = getProjectStatsFromCondition(projectTag, condition);
		if (projectStatsFromCondition.getNumGenes() == null) {
			Integer numGenes = PreparedCriteria.getCriteriaForGenesInProjectInCondition(projectTag, condition.getId())
					.list().size();
			projectStatsFromCondition.setNumGenes(numGenes);
			updateFile();
		}
		return projectStatsFromCondition.getNumGenes();

	}

	public int getNumPSMs(String projectTag, ExperimentalConditionBean condition) {
		final ProjectStats projectStatsFromCondition = getProjectStatsFromCondition(projectTag, condition);
		if (projectStatsFromCondition.getNumPSMs() == null) {
			Integer numPSMs = PreparedCriteria
					.getCriteriaForDifferentPSMsInProjectInCondition(projectTag, condition.getId()).list().size();
			projectStatsFromCondition.setNumPSMs(numPSMs);
			updateFile();
		}
		return projectStatsFromCondition.getNumPSMs();
	}

	public int getNumDifferentPeptides(String projectTag, ExperimentalConditionBean condition) {
		final ProjectStats projectStatsFromCondition = getProjectStatsFromCondition(projectTag, condition);
		if (projectStatsFromCondition.getNumPeptides() == null) {
			Integer numPeptides = PreparedCriteria
					.getCriteriaForDifferentPeptidesInProjectInCondition(projectTag, condition.getId()).list().size();
			projectStatsFromCondition.setNumPeptides(numPeptides);
			updateFile();
		}
		return projectStatsFromCondition.getNumPeptides();
	}

	public int getNumDifferentProteins(String projectTag, ExperimentalConditionBean condition) {
		final ProjectStats projectStatsFromCondition = getProjectStatsFromCondition(projectTag, condition);
		if (projectStatsFromCondition.getNumProteins() == null) {
			Integer numProteins = PreparedCriteria
					.getCriteriaForProteinPrimaryAccsInProjectInCondition(projectTag, condition.getId()).list().size();
			projectStatsFromCondition.setNumProteins(numProteins);
			updateFile();
		}
		return projectStatsFromCondition.getNumProteins();
	}

	public int getNumGenes(String projectTag, MSRunBean msRun) {
		final ProjectStats projectStatsFromMSRun = getProjectStatsFromMSRun(projectTag, msRun);
		if (projectStatsFromMSRun.getNumGenes() == null) {
			Integer numProteins = PreparedCriteria.getCriteriaForGenesInProjectInMSRun(projectTag, msRun.getRunID())
					.list().size();
			projectStatsFromMSRun.setNumGenes(numProteins);
			updateFile();
		}
		return projectStatsFromMSRun.getNumGenes();
	}

	public int getNumPSMs(String projectTag, MSRunBean msRun) {
		final ProjectStats projectStatsFromMSRun = getProjectStatsFromMSRun(projectTag, msRun);
		if (projectStatsFromMSRun.getNumPSMs() == null) {
			Integer numPSMs = PreparedCriteria.getCriteriaForDifferentPSMsInProjectInMSRun(projectTag, msRun.getRunID())
					.list().size();
			projectStatsFromMSRun.setNumPSMs(numPSMs);
			updateFile();
		}
		return projectStatsFromMSRun.getNumPSMs();
	}

	public int getNumDifferentPeptides(String projectTag, MSRunBean msRun) {
		final ProjectStats projectStatsFromMSRun = getProjectStatsFromMSRun(projectTag, msRun);
		if (projectStatsFromMSRun.getNumPeptides() == null) {
			Integer numPeptides = PreparedCriteria
					.getCriteriaForDifferentPeptidesInProjectInMSRun(projectTag, msRun.getRunID()).list().size();
			projectStatsFromMSRun.setNumPeptides(numPeptides);
			updateFile();
		}
		return projectStatsFromMSRun.getNumPeptides();
	}

	public int getNumDifferentProteins(String projectTag, MSRunBean msRun) {
		final ProjectStats projectStatsFromMSRun = getProjectStatsFromMSRun(projectTag, msRun);
		if (projectStatsFromMSRun.getNumProteins() == null) {
			Integer numProteins = PreparedCriteria
					.getCriteriaForProteinPrimaryAccsInProjectInMSRun(projectTag, msRun.getRunID()).list().size();
			projectStatsFromMSRun.setNumProteins(numProteins);
			updateFile();
		}
		return projectStatsFromMSRun.getNumProteins();

	}

	public int getNumGenes(String projectTag) {
		final ProjectStats projectStatsFromProject = getProjectStatsFromProject(projectTag);
		if (projectStatsFromProject.getNumGenes() == null) {
			final List list = PreparedCriteria.getCriteriaForGenesInProject(projectTag).list();
			Integer numGenes = list.size();
			projectStatsFromProject.setNumGenes(numGenes);
			updateFile();
		}
		return projectStatsFromProject.getNumGenes();
	}

	public int getNumPSMs(String projectTag) {
		final ProjectStats projectStatsFromProject = getProjectStatsFromProject(projectTag);
		if (projectStatsFromProject.getNumPSMs() == null) {
			Integer numPSMs = PreparedCriteria.getCriteriaForDifferentPSMsInProject(projectTag).list().size();
			projectStatsFromProject.setNumPSMs(numPSMs);
			updateFile();
		}
		return projectStatsFromProject.getNumPSMs();
	}

	public int getNumDifferentPeptides(String projectTag) {
		final ProjectStats projectStatsFromProject = getProjectStatsFromProject(projectTag);
		if (projectStatsFromProject.getNumPeptides() == null) {
			Integer numPeptides = PreparedCriteria.getCriteriaForDifferentPeptidesInProject(projectTag).list().size();
			projectStatsFromProject.setNumPeptides(numPeptides);
			updateFile();
		}
		return projectStatsFromProject.getNumPeptides();
	}

	public int getNumDifferentProteins(String projectTag) {
		final ProjectStats projectStatsFromProject = getProjectStatsFromProject(projectTag);
		if (projectStatsFromProject.getNumProteins() == null) {
			Integer numProteins = PreparedCriteria.getCriteriaForProteinPrimaryAccsInProject(projectTag).list().size();
			projectStatsFromProject.setNumProteins(numProteins);
			updateFile();
		}
		return projectStatsFromProject.getNumProteins();
	}

	public int getNumDifferentProteins() {
		if (generalProjectsStats.getNumProteins() == null) {
			Integer numProteins = PreparedQueries.getNumDifferentProteins();
			generalProjectsStats.setNumProteins(numProteins);
			updateFile();
		}
		return generalProjectsStats.getNumProteins();
	}

	public int getNumGenes() {
		if (generalProjectsStats.getNumGenes() == null) {
			Integer numGenes = PreparedCriteria.getCriteriaForGenes().list().size();
			generalProjectsStats.setNumGenes(numGenes);
			updateFile();
		}
		return generalProjectsStats.getNumGenes();
	}

	public int getNumDifferentPeptides() {
		if (generalProjectsStats.getNumPeptides() == null) {
			Integer numPeptides = PreparedQueries.getNumDifferentPeptides();
			generalProjectsStats.setNumPeptides(numPeptides);
			updateFile();
		}
		return generalProjectsStats.getNumPeptides();

	}

	public int getNumConditions() {
		if (generalProjectsStats.getNumConditions() == null) {
			Integer numConditions = PreparedQueries.getNumConditions();
			generalProjectsStats.setNumConditions(numConditions);
			updateFile();
		}
		return generalProjectsStats.getNumConditions();

	}

	public int getNumPSMs() {
		if (generalProjectsStats.getNumPSMs() == null) {
			Integer numPSMs = PreparedQueries.getNumPSMs();
			generalProjectsStats.setNumPSMs(numPSMs);
			updateFile();
		}

		return generalProjectsStats.getNumPSMs();
	}

	public int getNumProjects() {
		return PreparedQueries.getNumProjects();
	}

	public int getNumMSRuns(String projectTag) {
		final ProjectStats projectStatsFromProject = getProjectStatsFromProject(projectTag);
		if (projectStatsFromProject.getNumMSRuns() == null) {
			Integer numMSRuns = PreparedQueries.getMSRunsByProject(projectTag).size();
			projectStatsFromProject.setNumMSRuns(numMSRuns);
			updateFile();
		}
		return projectStatsFromProject.getNumMSRuns();

	}

	public int getNumConditions(String projectTag) {
		final ProjectStats projectStatsFromProject = getProjectStatsFromProject(projectTag);
		if (projectStatsFromProject.getNumConditions() == null) {
			Integer numConditions = PreparedCriteria.getCriteriaForConditionsInProject(projectTag).list().size();
			projectStatsFromProject.setNumConditions(numConditions);
			updateFile();
		}
		return projectStatsFromProject.getNumConditions();
	}

}
