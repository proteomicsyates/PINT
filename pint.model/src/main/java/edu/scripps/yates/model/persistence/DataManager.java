package edu.scripps.yates.model.persistence;

import java.util.List;

import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.Organism;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.Sample;
import edu.scripps.yates.utilities.proteomicsmodel.Tissue;

/**
 * This interface provides the methods for retrieving data from a persistence
 * layer (excel, db...), by retrieving data using some key values. If no data is
 * found, creates it.
 * 
 * @author Salva
 * 
 */
public interface DataManager {

	public void saveProject(Project project);

	public Project getProjectByName(String name);

	List<Condition> getExperimentalConditionsByName(String name);

	MSRun getMSRunByPath(String path);

	MSRun getMSRunByRunID(String msID);

	List<Sample> getSamplesByName(String name);

	Organism getOrganismByID(String organismID);

	Tissue getTissueByID(String tissueID);

	List<Protein> getProteinsByAcc(Accession accession);

}
