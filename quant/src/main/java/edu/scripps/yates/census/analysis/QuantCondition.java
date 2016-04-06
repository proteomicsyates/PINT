package edu.scripps.yates.census.analysis;

import java.util.Set;

import edu.scripps.yates.utilities.model.factories.ProjectEx;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.Sample;

public class QuantCondition implements Condition {
	private final String name;
	private final Project project;

	public QuantCondition(String name) {
		this(name, "myproject");
	}

	public QuantCondition(String name, String projectTag) {
		this.name = name;
		project = new ProjectEx(projectTag, null);
		((ProjectEx) project).setTag(projectTag);
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QuantCondition[" + name + "]";
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof QuantCondition) {
			return ((QuantCondition) obj).getName().equals(getName());
		}
		return super.equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return -1;
	}

	@Override
	public Sample getSample() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUnit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Project getProject() {
		return project;
	}

	@Override
	public Set<Protein> getProteins() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<PSM> getPSMs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Peptide> getPeptides() {
		// TODO Auto-generated method stub
		return null;
	}
}
