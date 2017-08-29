package edu.scripps.yates.persistence.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.Experiment;

import edu.scripps.yates.utilities.model.factories.MSRunEx;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.Organism;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.Sample;
import edu.scripps.yates.utilities.proteomicsmodel.Tissue;
import gnu.trove.map.hash.THashMap;

public class DataManagerMemory {
	private final Map<String, Project> projects = new THashMap<String, Project>();
	private final Map<String, List<Experiment>> experiments = new THashMap<String, List<Experiment>>();

	private final Map<String, List<Condition>> experimentConditions = new THashMap<String, List<Condition>>();
	/**
	 * A map of {@link MSRun} by path
	 */
	private final Map<String, MSRun> msRunsByPath = new THashMap<String, MSRun>();
	/**
	 * A map of {@link MSRun} by id
	 */
	private final Map<String, MSRun> msRunsByID = new THashMap<String, MSRun>();
	private final Map<String, List<Sample>> samples = new THashMap<String, List<Sample>>();
	private final Map<String, Organism> organisms = new THashMap<String, Organism>();
	private final Map<String, Tissue> tissues = new THashMap<String, Tissue>();
	private final Logger log = Logger.getLogger(DataManagerMemory.class);
	private final Map<Accession, List<Protein>> proteinMap = new THashMap<Accession, List<Protein>>();

	public Project getProjectByName(String projectName) {
		return projects.get(projectName);
	}

	public void saveProject(Project project) {
		final String name = project.getName();
		if (projects.containsKey(name)) {
			throw new IllegalArgumentException("Project with name '" + name + "' is already stored");
		} else {
			projects.put(name, project);
		}
	}

	public List<Condition> getExperimentalConditionsByName(String name) {
		return experimentConditions.get(name);
	}

	public void saveExperimentalCondition(Condition experimentalCondition) {
		final String name = experimentalCondition.getName();
		if (experimentConditions.containsKey(name)) {
			experimentConditions.get(name).add(experimentalCondition);
		} else {
			List<Condition> list = new ArrayList<Condition>();
			list.add(experimentalCondition);
			experimentConditions.put(name, list);
		}

	}

	public MSRun getMSRunByPath(String path) {
		return msRunsByPath.get(path);
	}

	public MSRun getMSRunByRunID(String runID) {
		return msRunsByID.get(runID);
	}

	public void saveMSRun(MSRun msRun) {
		log.debug("Saving new msRun in memory: " + msRun.getRunId() + ": " + msRun.getPath());
		msRunsByPath.put(msRun.getPath(), msRun);
		msRunsByID.put(msRun.getRunId(), msRun);
	}

	public List<Sample> getSamplesByName(String name) {
		return samples.get(name);
	}

	public void saveSample(Sample sample) {
		final String name = sample.getName();
		if (samples.containsKey(name)) {
			samples.get(name).add(sample);
		} else {
			List<Sample> list = new ArrayList<Sample>();
			list.add(sample);
			samples.put(name, list);
		}
	}

	public Organism getOrganismByID(String organismID) {
		return organisms.get(organismID);
	}

	public void saveOrganism(Organism organism) {
		organisms.put(organism.getOrganismID(), organism);

	}

	public Tissue getTissueByID(String tissueID) {
		return tissues.get(tissueID);
	}

	public void saveTissue(Tissue tissue) {
		tissues.put(tissue.getTissueID(), tissue);
	}

	public void saveProtein(Protein protein) {
		final List<Accession> accs = protein.getSecondaryAccessions();
		accs.add(protein.getPrimaryAccession());
		for (Accession accession : accs) {

			if (proteinMap.containsKey(accession)) {
				proteinMap.get(accession).add(protein);
			} else {
				List<Protein> list = new ArrayList<Protein>();
				list.add(protein);
				proteinMap.put(accession, list);
			}

		}
	}

	public List<Protein> getProteinsByAcc(Accession accession) {
		return proteinMap.get(accession);
	}

	public MSRun getMSRun(String runID, String path) {
		if (path != null) {
			MSRun msrun = getMSRunByPath(path);
			if (msrun == null && runID != null) {
				msrun = new MSRunEx(runID, path);
				saveMSRun(msrun);
			}
			return msrun;
		}
		// log.info("No run found for " + runID);
		return null;
	}

}
