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
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.server.util.FileManager;
import edu.scripps.yates.shared.model.ExperimentalConditionBean;
import edu.scripps.yates.shared.model.MSRunBean;
import edu.scripps.yates.shared.model.SampleBean;
import edu.scripps.yates.shared.model.projectStats.HasIdImpl;
import edu.scripps.yates.shared.model.projectStats.ProjectStatNumberType;
import edu.scripps.yates.shared.model.projectStats.ProjectStats;
import edu.scripps.yates.shared.model.projectStats.ProjectStatsImpl;
import edu.scripps.yates.shared.model.projectStats.ProjectStatsParent;
import edu.scripps.yates.shared.model.projectStats.ProjectStatsType;

public class ProjectStatsManager {
	private final static Logger log = Logger.getLogger(ProjectStatsManager.class);
	private static ProjectStatsManager instance;
	private final File file;
	private final Map<String, ProjectStatsParent> map = new HashMap<String, ProjectStatsParent>();
	private final HashMap<Thread, Method> methodsByThread = new HashMap<Thread, Method>();
	private final static ReentrantLock lock = new ReentrantLock();
	private static ProjectStats generalProjectsStats = new ProjectStatsImpl(null, ProjectStatsType.PINT);

	private ProjectStatsManager(File file, Method method) {
		this.file = file;
		putMethodFromThread(method);
		loadFile();
	}

	private void loadFile() {
		// wait if someone is trying to access the file

		try {
			log.info("Trying to get the lock from Thread " + Thread.currentThread().getId() + " from method "
					+ methodsByThread.get(Thread.currentThread()).getName());
			lock.lockInterruptibly();
			log.info("Lock acquired from Thread " + Thread.currentThread().getId() + " from method "
					+ methodsByThread.get(Thread.currentThread()).getName());
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
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
				boolean isGeneralPintStats = false;
				final String[] split = line.split("\t");
				projectTag = split[0];
				if (!"".equals(projectTag)) {
					if (!map.containsKey(projectTag)) {
						ProjectStatsParent statsParent = new ProjectStatsParent(projectTag);
						map.put(projectTag, statsParent);
					}
				} else {
					// if projectTag is "" then it is general PINT stats
					isGeneralPintStats = true;
				}

				// first comes the type, then the id, and then the stats

				final ProjectStatsType projectStatsType = ProjectStatsType.valueOf(split[1]);
				String id = split[2];
				Integer num = getNumber(split[4]);
				ProjectStats projectStats = null;
				if (!isGeneralPintStats) {
					projectStats = map.get(projectTag).getProjectStats(id, projectStatsType);
					if (projectStats == null) {
						projectStats = new ProjectStatsImpl(new HasIdImpl(id), projectStatsType);
						map.get(projectTag).addProjectStat(projectStats);
					}
				} else {
					projectStats = generalProjectsStats;
				}
				ProjectStatNumberType projectStatsNumberType = ProjectStatNumberType.valueOf(split[3]);
				switch (projectStatsNumberType) {
				case NUM_CONDITIONS:
					projectStats.setNumConditions(num);
					break;
				case NUM_GENES:
					projectStats.setNumGenes(num);
					break;
				case NUM_MSRUNS:
					projectStats.setNumMSRuns(num);
					break;
				case NUM_PEPTIDES:
					projectStats.setNumPeptides(num);
					break;
				case NUM_PROTEINS:
					projectStats.setNumProteins(num);
					break;
				case NUM_PSMS:
					projectStats.setNumPSMs(num);
					break;
				case NUM_SAMPLE:
					projectStats.setNumSamples(num);
					break;
				default:
					break;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Error while reading stats file: " + e.getMessage());
		} finally {
			lock.unlock();
			log.info("Lock released from Thread " + Thread.currentThread().getId() + " from method "
					+ methodsByThread.get(Thread.currentThread()).getName());
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

	public static synchronized ProjectStatsManager getInstance(Method method) {
		if (instance == null) {
			File file = FileManager.getProjectStatsFile();
			instance = new ProjectStatsManager(file, method);
		} else {
			instance.putMethodFromThread(method);
		}
		return instance;
	}

	private void putMethodFromThread(Method method) {
		methodsByThread.put(Thread.currentThread(), method);

	}

	public Integer getNumSamples(String projectTag, MSRunBean msRun) throws HibernateException, IOException {
		ProjectStats stats = getProjectStatsFromMSRun(projectTag, msRun);
		if (stats.getNumSamples() == null) {
			final List list = PreparedCriteria.getCriteriaForSamplesInProjectInMSRun(projectTag, msRun.getId()).list();
			stats.setNumSamples(list.size());

			updateFile(stats.getNumSamplesString(projectTag));
		}
		return stats.getNumSamples();
	}

	private void updateFile(String line) {
		// wait until someone release the lock

		log.info("Trying to get the lock from Thread " + Thread.currentThread().getId() + " from method "
				+ methodsByThread.get(Thread.currentThread()));
		try {
			lock.lockInterruptibly();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		log.info("Lock acquired from Thread " + Thread.currentThread().getId() + " from method "
				+ methodsByThread.get(Thread.currentThread()));
		try {
			List<String> projectList = new ArrayList<String>();
			projectList.addAll(map.keySet());
			Collections.sort(projectList);
			FileWriter fw = null;

			try {
				fw = new FileWriter(file, true);

				fw.write(line + "\n");

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
			lock.unlock();
			log.info("Lock released from Thread " + Thread.currentThread().getId() + " from method "
					+ methodsByThread.get(Thread.currentThread()));
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

			updateFile(projectStatsFromMSRun.getNumConditionsString(projectTag));
		}

		return projectStatsFromMSRun.getNumConditions();
	}

	public int getNumMSRuns(String projectTag, SampleBean sample) {
		final ProjectStats projectStatsFromSample = getProjectStatsFromSample(projectTag, sample);
		if (projectStatsFromSample.getNumMSRuns() == null) {
			Integer numMSRuns = PreparedCriteria.getCriteriaForMSRunsInProjectInSample(projectTag, sample.getId())
					.list().size();
			projectStatsFromSample.setNumMSRuns(numMSRuns);

			updateFile(projectStatsFromSample.getNumMSRunsString(projectTag));
		}
		return projectStatsFromSample.getNumMSRuns();
	}

	public int getNumGenes(String projectTag, SampleBean sample) {
		final ProjectStats projectStatsFromSample = getProjectStatsFromSample(projectTag, sample);
		if (projectStatsFromSample.getNumGenes() == null) {
			Integer numGenes = PreparedCriteria.getCriteriaForGenesInProjectInSample(projectTag, sample.getId()).list()
					.size();
			projectStatsFromSample.setNumGenes(numGenes);

			updateFile(projectStatsFromSample.getNumGenesString(projectTag));
		}
		return projectStatsFromSample.getNumGenes();

	}

	public int getNumPSMs(String projectTag, SampleBean sample) {
		final ProjectStats projectStatsFromSample = getProjectStatsFromSample(projectTag, sample);
		if (projectStatsFromSample.getNumPSMs() == null) {
			Integer numPSMs = PreparedCriteria.getCriteriaForDifferentPSMsInProjectInSample(projectTag, sample.getId())
					.list().size();
			projectStatsFromSample.setNumPSMs(numPSMs);

			updateFile(projectStatsFromSample.getNumPSMsString(projectTag));
		}
		return projectStatsFromSample.getNumPSMs();
	}

	public int getNumDifferentPeptides(String projectTag, SampleBean sample) {
		final ProjectStats projectStatsFromSample = getProjectStatsFromSample(projectTag, sample);
		if (projectStatsFromSample.getNumPeptides() == null) {
			Integer numPeptides = PreparedCriteria
					.getCriteriaForDifferentPeptidesInProjectInSample(projectTag, sample.getId()).list().size();
			projectStatsFromSample.setNumPeptides(numPeptides);

			updateFile(projectStatsFromSample.getNumPeptidesString(projectTag));
		}
		return projectStatsFromSample.getNumPeptides();
	}

	public int getNumDifferentProteins(String projectTag, SampleBean sample) {
		final ProjectStats projectStatsFromSample = getProjectStatsFromSample(projectTag, sample);
		if (projectStatsFromSample.getNumProteins() == null) {
			Integer numProteins = PreparedCriteria
					.getCriteriaForProteinPrimaryAccsInProjectInSample(projectTag, sample.getId()).list().size();
			projectStatsFromSample.setNumProteins(numProteins);

			updateFile(projectStatsFromSample.getNumProteinsString(projectTag));
		}
		return projectStatsFromSample.getNumProteins();

	}

	public int getNumMSRuns(String projectTag, ExperimentalConditionBean condition) {
		final ProjectStats projectStatsFromCondition = getProjectStatsFromCondition(projectTag, condition);
		if (projectStatsFromCondition.getNumMSRuns() == null) {
			Integer numMSRuns = PreparedCriteria.getCriteriaForMSRunsInProjectInCondition(projectTag, condition.getId())
					.list().size();
			projectStatsFromCondition.setNumMSRuns(numMSRuns);

			updateFile(projectStatsFromCondition.getNumMSRunsString(projectTag));
		}
		return projectStatsFromCondition.getNumMSRuns();
	}

	public int getNumGenes(String projectTag, ExperimentalConditionBean condition) {
		final ProjectStats projectStatsFromCondition = getProjectStatsFromCondition(projectTag, condition);
		if (projectStatsFromCondition.getNumGenes() == null) {
			Integer numGenes = PreparedCriteria.getCriteriaForGenesInProjectInCondition(projectTag, condition.getId())
					.list().size();
			projectStatsFromCondition.setNumGenes(numGenes);

			updateFile(projectStatsFromCondition.getNumGenesString(projectTag));
		}
		return projectStatsFromCondition.getNumGenes();

	}

	public int getNumPSMs(String projectTag, ExperimentalConditionBean condition) {
		final ProjectStats projectStatsFromCondition = getProjectStatsFromCondition(projectTag, condition);
		if (projectStatsFromCondition.getNumPSMs() == null) {
			Integer numPSMs = PreparedCriteria
					.getCriteriaForDifferentPSMsInProjectInCondition(projectTag, condition.getId()).list().size();
			projectStatsFromCondition.setNumPSMs(numPSMs);

			updateFile(projectStatsFromCondition.getNumPSMsString(projectTag));
		}
		return projectStatsFromCondition.getNumPSMs();
	}

	public int getNumDifferentPeptides(String projectTag, ExperimentalConditionBean condition) {
		final ProjectStats projectStatsFromCondition = getProjectStatsFromCondition(projectTag, condition);
		if (projectStatsFromCondition.getNumPeptides() == null) {
			Integer numPeptides = PreparedCriteria
					.getCriteriaForDifferentPeptidesInProjectInCondition(projectTag, condition.getId()).list().size();
			projectStatsFromCondition.setNumPeptides(numPeptides);

			updateFile(projectStatsFromCondition.getNumPeptidesString(projectTag));
		}
		return projectStatsFromCondition.getNumPeptides();
	}

	public int getNumDifferentProteins(String projectTag, ExperimentalConditionBean condition) {
		final ProjectStats projectStatsFromCondition = getProjectStatsFromCondition(projectTag, condition);
		if (projectStatsFromCondition.getNumProteins() == null) {
			Integer numProteins = PreparedCriteria
					.getCriteriaForProteinPrimaryAccsInProjectInCondition(projectTag, condition.getId()).list().size();
			projectStatsFromCondition.setNumProteins(numProteins);

			updateFile(projectStatsFromCondition.getNumProteinsString(projectTag));
		}
		return projectStatsFromCondition.getNumProteins();
	}

	public int getNumGenes(String projectTag, MSRunBean msRun) {
		final ProjectStats projectStatsFromMSRun = getProjectStatsFromMSRun(projectTag, msRun);
		if (projectStatsFromMSRun.getNumGenes() == null) {
			Integer numProteins = PreparedCriteria.getCriteriaForGenesInProjectInMSRun(projectTag, msRun.getRunID())
					.list().size();
			projectStatsFromMSRun.setNumGenes(numProteins);

			updateFile(projectStatsFromMSRun.getNumGenesString(projectTag));
		}
		return projectStatsFromMSRun.getNumGenes();
	}

	public int getNumPSMs(String projectTag, MSRunBean msRun) {
		final ProjectStats projectStatsFromMSRun = getProjectStatsFromMSRun(projectTag, msRun);
		if (projectStatsFromMSRun.getNumPSMs() == null) {
			Integer numPSMs = PreparedCriteria.getCriteriaForDifferentPSMsInProjectInMSRun(projectTag, msRun.getRunID())
					.list().size();
			projectStatsFromMSRun.setNumPSMs(numPSMs);

			updateFile(projectStatsFromMSRun.getNumPSMsString(projectTag));
		}
		return projectStatsFromMSRun.getNumPSMs();
	}

	public int getNumDifferentPeptides(String projectTag, MSRunBean msRun) {
		final ProjectStats projectStatsFromMSRun = getProjectStatsFromMSRun(projectTag, msRun);
		if (projectStatsFromMSRun.getNumPeptides() == null) {
			Integer numPeptides = PreparedCriteria
					.getCriteriaForDifferentPeptidesInProjectInMSRun(projectTag, msRun.getRunID()).list().size();
			projectStatsFromMSRun.setNumPeptides(numPeptides);

			updateFile(projectStatsFromMSRun.getNumPeptidesString(projectTag));
		}
		return projectStatsFromMSRun.getNumPeptides();
	}

	public int getNumDifferentProteins(String projectTag, MSRunBean msRun) {
		final ProjectStats projectStatsFromMSRun = getProjectStatsFromMSRun(projectTag, msRun);
		if (projectStatsFromMSRun.getNumProteins() == null) {
			Integer numProteins = PreparedCriteria
					.getCriteriaForProteinPrimaryAccsInProjectInMSRun(projectTag, msRun.getRunID()).list().size();
			projectStatsFromMSRun.setNumProteins(numProteins);

			updateFile(projectStatsFromMSRun.getNumProteinsString(projectTag));
		}
		return projectStatsFromMSRun.getNumProteins();

	}

	public int getNumGenes(String projectTag) {
		final ProjectStats projectStatsFromProject = getProjectStatsFromProject(projectTag);
		if (projectStatsFromProject.getNumGenes() == null) {
			final List list = PreparedCriteria.getCriteriaForGenesInProject(projectTag).list();
			Integer numGenes = list.size();
			projectStatsFromProject.setNumGenes(numGenes);

			updateFile(projectStatsFromProject.getNumGenesString(projectTag));
		}
		return projectStatsFromProject.getNumGenes();
	}

	public int getNumPSMs(String projectTag) {
		final ProjectStats projectStatsFromProject = getProjectStatsFromProject(projectTag);
		if (projectStatsFromProject.getNumPSMs() == null) {
			Integer numPSMs = PreparedCriteria.getCriteriaForDifferentPSMsInProject(projectTag).list().size();
			projectStatsFromProject.setNumPSMs(numPSMs);

			updateFile(projectStatsFromProject.getNumPSMsString(projectTag));
		}
		return projectStatsFromProject.getNumPSMs();
	}

	public int getNumDifferentPeptides(String projectTag) {
		final ProjectStats projectStatsFromProject = getProjectStatsFromProject(projectTag);
		if (projectStatsFromProject.getNumPeptides() == null) {
			Integer numPeptides = PreparedCriteria.getCriteriaForDifferentPeptidesInProject(projectTag).list().size();
			projectStatsFromProject.setNumPeptides(numPeptides);

			updateFile(projectStatsFromProject.getNumPeptidesString(projectTag));
		}
		return projectStatsFromProject.getNumPeptides();
	}

	public int getNumDifferentProteins(String projectTag) {
		final ProjectStats projectStatsFromProject = getProjectStatsFromProject(projectTag);
		if (projectStatsFromProject.getNumProteins() == null) {
			Integer numProteins = PreparedCriteria.getCriteriaForProteinPrimaryAccsInProject(projectTag).list().size();
			projectStatsFromProject.setNumProteins(numProteins);

			updateFile(projectStatsFromProject.getNumProteinsString(projectTag));
		}
		return projectStatsFromProject.getNumProteins();
	}

	public int getNumDifferentProteins() {
		if (generalProjectsStats.getNumProteins() == null) {
			Integer numProteins = PreparedQueries.getNumDifferentProteins();
			generalProjectsStats.setNumProteins(numProteins);

			updateFile(generalProjectsStats.getNumProteinsString(""));
		}
		return generalProjectsStats.getNumProteins();
	}

	public int getNumGenes() {
		if (generalProjectsStats.getNumGenes() == null) {
			Integer numGenes = PreparedCriteria.getCriteriaForGenes().list().size();
			generalProjectsStats.setNumGenes(numGenes);

			updateFile(generalProjectsStats.getNumGenesString(""));
		}
		return generalProjectsStats.getNumGenes();
	}

	public int getNumDifferentPeptides() {
		if (generalProjectsStats.getNumPeptides() == null) {
			Integer numPeptides = PreparedQueries.getNumDifferentPeptides();
			generalProjectsStats.setNumPeptides(numPeptides);

			updateFile(generalProjectsStats.getNumPeptidesString(""));
		}
		return generalProjectsStats.getNumPeptides();

	}

	public int getNumConditions() {
		if (generalProjectsStats.getNumConditions() == null) {
			Integer numConditions = PreparedQueries.getNumConditions();
			generalProjectsStats.setNumConditions(numConditions);

			updateFile(generalProjectsStats.getNumConditionsString(""));
		}
		return generalProjectsStats.getNumConditions();

	}

	public int getNumPSMs() {
		if (generalProjectsStats.getNumPSMs() == null) {
			Integer numPSMs = PreparedQueries.getNumPSMs();
			generalProjectsStats.setNumPSMs(numPSMs);

			updateFile(generalProjectsStats.getNumPSMsString(""));
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

			updateFile(projectStatsFromProject.getNumMSRunsString(""));
		}
		return projectStatsFromProject.getNumMSRuns();

	}

	public int getNumConditions(String projectTag) {
		final ProjectStats projectStatsFromProject = getProjectStatsFromProject(projectTag);
		if (projectStatsFromProject.getNumConditions() == null) {
			Integer numConditions = PreparedCriteria.getCriteriaForConditionsInProject(projectTag).list().size();
			projectStatsFromProject.setNumConditions(numConditions);

			updateFile(projectStatsFromProject.getNumConditionsString(""));
		}
		return projectStatsFromProject.getNumConditions();
	}

}
