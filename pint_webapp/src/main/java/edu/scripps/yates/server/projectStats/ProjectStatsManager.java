package edu.scripps.yates.server.projectStats;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import edu.scripps.yates.annotations.uniprot.UniprotProteinLocalRetriever;
import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.annotations.util.UniprotEntryUtil;
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
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.util.Pair;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class ProjectStatsManager {
	private final static Logger log = Logger.getLogger(ProjectStatsManager.class);
	private static ProjectStatsManager instance;
	private final File file;
	private final Map<String, ProjectStatsParent> map = new THashMap<String, ProjectStatsParent>();
	private final Map<Thread, Method> methodsByThread = new THashMap<Thread, Method>();
	private static ProjectStats generalProjectsStats = new ProjectStatsImpl(null, ProjectStatsType.PINT);

	private ProjectStatsManager(File file, Method method) {
		this.file = file;
		putMethodFromThread(method);
		loadFile();
	}

	private void loadFile() {
		// wait if someone is trying to access the file
		FileLock fileLock = null;
		BufferedReader br = null;
		try {
			final FileInputStream fis = new FileInputStream(file);
			log.info("Trying to get the lock from Thread " + Thread.currentThread().getId() + " from method "
					+ methodsByThread.get(Thread.currentThread()).getName());

			fileLock = fis.getChannel().lock(0L, Long.MAX_VALUE, true);

			log.info("Lock acquired from Thread " + Thread.currentThread().getId() + " from method "
					+ methodsByThread.get(Thread.currentThread()).getName());
			if (!file.exists()) {
				return;
			}

			final DataInputStream in = new DataInputStream(fis);
			br = new BufferedReader(new InputStreamReader(in));
			String line;
			String projectTag = null;
			while ((line = br.readLine()) != null) {

				if ("".equals(line.trim())) {
					continue;
				}
				boolean isGeneralPintStats = false;
				final String[] split = line.split("\t");
				projectTag = split[0];
				if (!"".equals(projectTag)) {
					if (!map.containsKey(projectTag)) {
						final ProjectStatsParent statsParent = new ProjectStatsParent(projectTag);
						map.put(projectTag, statsParent);
					}
				} else {
					// if projectTag is "" then it is general PINT stats
					isGeneralPintStats = true;
				}

				// first comes the type, then the id, and then the stats

				final ProjectStatsType projectStatsType = ProjectStatsType.valueOf(split[1]);
				final String id = split[2];
				final Integer num = getNumber(split[4]);
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
				final ProjectStatNumberType projectStatsNumberType = ProjectStatNumberType.valueOf(split[3]);
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
			log.info(FilenameUtils.getName(file.getAbsolutePath()) + " loaded correctly");
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {

			try {
				if (fileLock != null) {
					fileLock.release();
					log.info("Lock released from Thread " + Thread.currentThread().getId() + " from method "
							+ methodsByThread.get(Thread.currentThread()).getName());
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
			if (br != null) {
				try {
					br.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	private Integer getNumber(String string) {
		if (string != null && !"".equals(string)) {
			try {
				return Integer.valueOf(string);
			} catch (final NumberFormatException e) {

			}
		}
		return null;
	}

	public static synchronized ProjectStatsManager getInstance(Method method) {
		if (instance == null) {
			final File file = FileManager.getProjectStatsFile();
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
		final ProjectStats stats = getProjectStatsFromMSRun(projectTag, msRun);
		if (stats.getNumSamples() == null) {
			final List list = PreparedCriteria.getCriteriaForSamplesInProjectInMSRun(projectTag, msRun.getId()).list();
			stats.setNumSamples(list.size());

			updateFile(stats.getNumSamplesString(projectTag));
		}
		return stats.getNumSamples();
	}

	private void updateFile(String line) {
		// wait until someone release the lock

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, true);

			log.info("Trying to get the lock for update the file from Thread " + Thread.currentThread().getId()
					+ " from method " + methodsByThread.get(Thread.currentThread()).getName());

			log.info("Lock acquired from Thread " + Thread.currentThread().getId() + " from method "
					+ methodsByThread.get(Thread.currentThread()).getName());

			final List<String> projectList = new ArrayList<String>();
			projectList.addAll(map.keySet());
			Collections.sort(projectList);

			final String str = line + "\n";
			fos.write(str.getBytes());

		} catch (final FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (final IOException e1) {
			e1.printStackTrace();
		} finally {

			if (fos != null) {
				try {
					fos.close();
				} catch (final IOException e) {
					e.printStackTrace();
					log.error("Error while closing stats file: " + e.getMessage());
				}
			}

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
			final Integer numMSRuns = PreparedCriteria.getCriteriaForMSRunsInProjectInSample(projectTag, sample.getId())
					.list().size();
			projectStatsFromSample.setNumMSRuns(numMSRuns);

			updateFile(projectStatsFromSample.getNumMSRunsString(projectTag));
		}
		return projectStatsFromSample.getNumMSRuns();
	}

	public int getNumGenes(String projectTag, SampleBean sample) {
		final ProjectStats projectStatsFromSample = getProjectStatsFromSample(projectTag, sample);
		if (projectStatsFromSample.getNumGenes() == null) {
			final List<String> genes = PreparedCriteria.getCriteriaForProteinPrimaryAccs(projectTag, null,
					sample.getId(), null);
			final int numGenes = getGenesFromUniprot(genes).size();
			if (numGenes > 0) {
				projectStatsFromSample.setNumGenes(numGenes);
				updateFile(projectStatsFromSample.getNumGenesString(projectTag));
			}
		}
		return projectStatsFromSample.getNumGenes();

	}

	public int getNumPSMs(String projectTag, SampleBean sample) {
		final ProjectStats projectStatsFromSample = getProjectStatsFromSample(projectTag, sample);
		if (projectStatsFromSample.getNumPSMs() == null) {
			final Long numPSMs = PreparedCriteria.getCriteriaForNumDifferentPSMs(projectTag, null, sample.getId(),
					null);
			if (numPSMs != null && numPSMs > 0l) {
				projectStatsFromSample.setNumPSMs(numPSMs.intValue());
				updateFile(projectStatsFromSample.getNumPSMsString(projectTag));
			}
		}
		return projectStatsFromSample.getNumPSMs();
	}

	public int getNumDifferentPeptides(String projectTag, SampleBean sample) {
		final ProjectStats projectStatsFromSample = getProjectStatsFromSample(projectTag, sample);
		if (projectStatsFromSample.getNumPeptides() == null) {
			final Long numPeptides = PreparedCriteria.getCriteriaForNumDifferentPeptides(projectTag, null,
					sample.getId(), null);
			if (numPeptides != null && numPeptides > 0l) {
				projectStatsFromSample.setNumPeptides(numPeptides.intValue());
				updateFile(projectStatsFromSample.getNumPeptidesString(projectTag));
			}
		}
		return projectStatsFromSample.getNumPeptides();
	}

	public int getNumDifferentProteins(String projectTag, SampleBean sample) {
		final ProjectStats projectStatsFromSample = getProjectStatsFromSample(projectTag, sample);
		if (projectStatsFromSample.getNumProteins() == null) {
			final Long numProteins = PreparedCriteria.getCriteriaForNumDifferentProteins(projectTag, null,
					sample.getId(), null);
			if (numProteins != null && numProteins > 0l) {
				projectStatsFromSample.setNumProteins(numProteins.intValue());
				updateFile(projectStatsFromSample.getNumProteinsString(projectTag));
			}
		}
		return projectStatsFromSample.getNumProteins();

	}

	public int getNumMSRuns(String projectTag, ExperimentalConditionBean condition) {
		final ProjectStats projectStatsFromCondition = getProjectStatsFromCondition(projectTag, condition);
		if (projectStatsFromCondition.getNumMSRuns() == null) {
			final Long numMSRuns = PreparedCriteria.getCriteriaForNumDifferentMSRuns(projectTag, null,
					condition.getId());
			if (numMSRuns != null && numMSRuns > 0l) {
				projectStatsFromCondition.setNumMSRuns(numMSRuns.intValue());
				updateFile(projectStatsFromCondition.getNumMSRunsString(projectTag));
			}
		}
		return projectStatsFromCondition.getNumMSRuns();
	}

	public int getNumGenes(String projectTag, ExperimentalConditionBean condition) {
		final ProjectStats projectStatsFromCondition = getProjectStatsFromCondition(projectTag, condition);
		if (projectStatsFromCondition.getNumGenes() == null) {
			final List<String> accs = PreparedCriteria.getCriteriaForProteinPrimaryAccs(projectTag, null, null,
					condition.getId());
			final int numGenes = getGenesFromUniprot(accs).size();
			if (numGenes > 0) {
				projectStatsFromCondition.setNumGenes(numGenes);
				updateFile(projectStatsFromCondition.getNumGenesString(projectTag));
			}
		}
		return projectStatsFromCondition.getNumGenes();

	}

	public int getNumPSMs(String projectTag, ExperimentalConditionBean condition) {
		final ProjectStats projectStatsFromCondition = getProjectStatsFromCondition(projectTag, condition);
		if (projectStatsFromCondition.getNumPSMs() == null) {
			final Long numPSMs = PreparedCriteria.getCriteriaForNumDifferentPSMs(projectTag, null, null,
					condition.getId());
			if (numPSMs != null && numPSMs > 0l) {
				projectStatsFromCondition.setNumPSMs(numPSMs.intValue());
				updateFile(projectStatsFromCondition.getNumPSMsString(projectTag));
			}
		}
		return projectStatsFromCondition.getNumPSMs();
	}

	public int getNumDifferentPeptides(String projectTag, ExperimentalConditionBean condition) {
		final ProjectStats projectStatsFromCondition = getProjectStatsFromCondition(projectTag, condition);
		if (projectStatsFromCondition.getNumPeptides() == null) {
			final Long numPeptides = PreparedCriteria.getCriteriaForNumDifferentPeptides(projectTag, null, null,
					condition.getId());
			if (numPeptides != null && numPeptides > 0l) {
				projectStatsFromCondition.setNumPeptides(numPeptides.intValue());
				updateFile(projectStatsFromCondition.getNumPeptidesString(projectTag));
			}
		}
		return projectStatsFromCondition.getNumPeptides();
	}

	public int getNumDifferentProteins(String projectTag, ExperimentalConditionBean condition) {
		final ProjectStats projectStatsFromCondition = getProjectStatsFromCondition(projectTag, condition);
		if (projectStatsFromCondition.getNumProteins() == null) {
			final Long numProteins = PreparedCriteria.getCriteriaForNumDifferentProteins(projectTag, null, null,
					condition.getId());
			if (numProteins != null && numProteins >= 0l) {
				projectStatsFromCondition.setNumProteins(numProteins.intValue());
				updateFile(projectStatsFromCondition.getNumProteinsString(projectTag));
			}
		}
		return projectStatsFromCondition.getNumProteins();
	}

	public int getNumGenes(String projectTag, MSRunBean msRun) {
		final ProjectStats projectStatsFromMSRun = getProjectStatsFromMSRun(projectTag, msRun);
		if (projectStatsFromMSRun.getNumGenes() == null) {
			final List<String> accs = PreparedCriteria.getCriteriaForProteinPrimaryAccs(projectTag, msRun.getRunID(),
					null, null);
			final int numGenes = getGenesFromUniprot(accs).size();
			if (numGenes > 0) {
				projectStatsFromMSRun.setNumGenes(numGenes);
				updateFile(projectStatsFromMSRun.getNumGenesString(projectTag));
			}
		}
		return projectStatsFromMSRun.getNumGenes();
	}

	private Set<String> getGenesFromUniprot(List<String> accs) {
		final Iterator<String> iterator = accs.iterator();
		while (iterator.hasNext()) {
			final String acc = iterator.next();
			if (FastaParser.getUniProtACC(acc) == null) {
				iterator.remove();
			}
		}
		if (!accs.isEmpty()) {
			final String uniprotVersion = null;
			final UniprotProteinLocalRetriever uplr = new UniprotProteinLocalRetriever(
					UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
					UniprotProteinRetrievalSettings.getInstance().isUseIndex(), true, true);
			uplr.setCacheEnabled(false);
			final Map<String, Entry> annotatedProteins = uplr.getAnnotatedProteins(uniprotVersion, accs);
			final Set<String> genes = new THashSet<String>();
			for (final String acc : accs) {
				if (annotatedProteins.containsKey(acc)) {
					final List<Pair<String, String>> geneNames = UniprotEntryUtil
							.getGeneName(annotatedProteins.get(acc), true, true);
					if (geneNames != null && !geneNames.isEmpty()) {
						final Pair<String, String> pair = geneNames.get(0);
						genes.add(pair.getFirstelement());
					}
				}
			}
			return genes;
		}
		return Collections.emptySet();
	}

	public int getNumPSMs(String projectTag, MSRunBean msRun) {
		final ProjectStats projectStatsFromMSRun = getProjectStatsFromMSRun(projectTag, msRun);
		if (projectStatsFromMSRun.getNumPSMs() == null) {
			final Long numPSMs = PreparedCriteria.getCriteriaForNumDifferentPSMs(projectTag, msRun.getRunID(), null,
					null);
			if (numPSMs != null && numPSMs > 0l) {
				projectStatsFromMSRun.setNumPSMs(numPSMs.intValue());
				updateFile(projectStatsFromMSRun.getNumPSMsString(projectTag));
			}
		}
		return projectStatsFromMSRun.getNumPSMs();
	}

	public int getNumDifferentPeptides(String projectTag, MSRunBean msRun) {
		final ProjectStats projectStatsFromMSRun = getProjectStatsFromMSRun(projectTag, msRun);
		if (projectStatsFromMSRun.getNumPeptides() == null) {
			final Long numPeptides = PreparedCriteria.getCriteriaForNumDifferentPeptides(projectTag, msRun.getRunID(),
					null, null);
			if (numPeptides != null && numPeptides > 0l) {
				projectStatsFromMSRun.setNumPeptides(numPeptides.intValue());
				updateFile(projectStatsFromMSRun.getNumPeptidesString(projectTag));
			}
		}
		return projectStatsFromMSRun.getNumPeptides();
	}

	public int getNumDifferentProteins(String projectTag, MSRunBean msRun) {
		final ProjectStats projectStatsFromMSRun = getProjectStatsFromMSRun(projectTag, msRun);
		if (projectStatsFromMSRun.getNumProteins() == null) {
			final Long numProteins = PreparedCriteria.getCriteriaForNumDifferentProteins(projectTag, msRun.getRunID(),
					null, null);
			if (numProteins != null && numProteins > 0l) {
				projectStatsFromMSRun.setNumProteins(numProteins.intValue());
				updateFile(projectStatsFromMSRun.getNumProteinsString(projectTag));
			}
		}
		return projectStatsFromMSRun.getNumProteins();

	}

	public int getNumGenes(String projectTag) {
		final ProjectStats projectStatsFromProject = getProjectStatsFromProject(projectTag);
		if (projectStatsFromProject.getNumGenes() == null) {
			final List<String> accs = PreparedCriteria.getCriteriaForProteinPrimaryAccs(projectTag, null, null, null);

			final int numGenes = getGenesFromUniprot(accs).size();
			if (numGenes > 0) {
				projectStatsFromProject.setNumGenes(numGenes);
				updateFile(projectStatsFromProject.getNumGenesString(projectTag));
			}

		}
		return projectStatsFromProject.getNumGenes();
	}

	public int getNumPSMs(String projectTag) {
		final ProjectStats projectStatsFromProject = getProjectStatsFromProject(projectTag);
		if (projectStatsFromProject.getNumPSMs() == null) {
			final Long numPSMs = PreparedCriteria.getCriteriaForNumDifferentPSMs(projectTag, null, null, null);
			if (numPSMs > 0) {
				projectStatsFromProject.setNumPSMs(numPSMs.intValue());
				updateFile(projectStatsFromProject.getNumPSMsString(projectTag));
			}
		}
		return projectStatsFromProject.getNumPSMs();
	}

	public int getNumDifferentPeptides(String projectTag) {
		final ProjectStats projectStatsFromProject = getProjectStatsFromProject(projectTag);
		if (projectStatsFromProject.getNumPeptides() == null) {
			final Long numPeptides = PreparedCriteria.getCriteriaForNumDifferentPeptides(projectTag, null, null, null);
			if (numPeptides != null && numPeptides > 0l) {
				projectStatsFromProject.setNumPeptides(numPeptides.intValue());
				updateFile(projectStatsFromProject.getNumPeptidesString(projectTag));
			}
		}
		return projectStatsFromProject.getNumPeptides();

	}

	public int getNumDifferentProteins(String projectTag) {
		final ProjectStats projectStatsFromProject = getProjectStatsFromProject(projectTag);
		if (projectStatsFromProject.getNumProteins() == null) {
			final Long numProteins = PreparedCriteria.getCriteriaForNumDifferentProteins(projectTag, null, null, null);
			if (numProteins != null && numProteins > 0l) {
				projectStatsFromProject.setNumProteins(numProteins.intValue());
				updateFile(projectStatsFromProject.getNumProteinsString(projectTag));
			}
		}
		return projectStatsFromProject.getNumProteins();
	}

	public int getNumDifferentProteins() {
		if (generalProjectsStats.getNumProteins() == null) {
			final Integer numProteins = PreparedQueries.getNumDifferentProteins();
			generalProjectsStats.setNumProteins(numProteins);

			updateFile(generalProjectsStats.getNumProteinsString(""));
		}
		return generalProjectsStats.getNumProteins();
	}

	public static void clearGeneralProjectStats() {
		generalProjectsStats.clear();
	}

	public int getNumGenes() {
		if (generalProjectsStats.getNumGenes() == null) {
			final List<String> accs = PreparedCriteria.getCriteriaForProteinPrimaryAccs(null, null, null, null);
			final Set<String> genes = getGenesFromUniprot(accs);

			final Integer numGenes = genes.size();
			if (numGenes > 0) {
				generalProjectsStats.setNumGenes(numGenes);
				updateFile(generalProjectsStats.getNumGenesString(""));
			}
		}
		return generalProjectsStats.getNumGenes();
	}

	public int getNumDifferentPeptides() {
		if (generalProjectsStats.getNumPeptides() == null) {
			final Integer numPeptides = PreparedQueries.getNumDifferentPeptides();
			generalProjectsStats.setNumPeptides(numPeptides);

			updateFile(generalProjectsStats.getNumPeptidesString(""));
		}
		return generalProjectsStats.getNumPeptides();

	}

	public int getNumConditions() {
		if (generalProjectsStats.getNumConditions() == null) {
			final Integer numConditions = PreparedQueries.getNumConditions();
			generalProjectsStats.setNumConditions(numConditions);

			updateFile(generalProjectsStats.getNumConditionsString(""));
		}
		return generalProjectsStats.getNumConditions();

	}

	public int getNumPSMs() {
		if (generalProjectsStats.getNumPSMs() == null) {
			final Integer numPSMs = PreparedQueries.getNumPSMs();
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
			final Integer numMSRuns = PreparedQueries.getMSRunsByProject(projectTag).size();
			projectStatsFromProject.setNumMSRuns(numMSRuns);

			updateFile(projectStatsFromProject.getNumMSRunsString(""));
		}
		return projectStatsFromProject.getNumMSRuns();

	}

	public int getNumConditions(String projectTag) {
		final ProjectStats projectStatsFromProject = getProjectStatsFromProject(projectTag);
		if (projectStatsFromProject.getNumConditions() == null) {
			final Integer numConditions = PreparedCriteria.getCriteriaForConditionIDsInProject(projectTag).size();
			projectStatsFromProject.setNumConditions(numConditions);

			updateFile(projectStatsFromProject.getNumConditionsString(""));
		}
		return projectStatsFromProject.getNumConditions();
	}

}
