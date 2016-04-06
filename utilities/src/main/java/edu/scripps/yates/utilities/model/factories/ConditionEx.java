package edu.scripps.yates.utilities.model.factories;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.Sample;

/**
 * Describes an experimental condition such as a timeline or other feature that
 * is associated to a {@link SampleEx} in a {@link ExperimentEx}
 *
 * @author Salva
 *
 */
public class ConditionEx implements Condition, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -2683658404859522323L;
	private final String name;
	private Sample sample;
	private Project project;
	// optional
	private String description;
	private Double value;
	private String unit;
	private final Logger log = Logger.getLogger(ConditionEx.class);
	private final Set<Protein> proteins = new HashSet<Protein>();
	private final Set<PSM> psms = new HashSet<PSM>();
	private final Set<Peptide> peptides = new HashSet<Peptide>();;

	public ConditionEx(Condition condition, Sample sample2, Project project) {
		this(condition.getName(), sample2, project);
	}

	public ConditionEx(String conditionName, Sample sample2, Project project) {
		if (conditionName == null) {
			throw new IllegalArgumentException(
					"Condition name cannot be null in the ExperimentalCondition constructor");
		}
		name = conditionName;
		if (sample2 == null) {
			throw new IllegalArgumentException("Sample cannot be null in the ExperimentalCondition constructor");
		}
		sample = sample2;
		this.project = project;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the value
	 */
	@Override
	public Double getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Double value) {
		this.value = value;
	}

	/**
	 * @return the unit
	 */
	@Override
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit
	 *            the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExperimentalConditionEx [name=" + name + ", sample=" + sample + ", description=" + description
				+ ", value=" + value + ", unit=" + unit + "]";
	}

	/**
	 * @return the sample
	 */
	@Override
	public Sample getSample() {
		return sample;
	}

	/**
	 * Equals if they have the same name
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Condition) {

			final Condition expCondition = (Condition) obj;
			if (getName().equals(expCondition.getName()))
				return true;

		}
		return super.equals(obj);
	}

	@Override
	public Project getProject() {
		return project;
	}

	@Override
	public Set<Protein> getProteins() {
		return proteins;
	}

	@Override
	public Set<PSM> getPSMs() {
		return psms;
	}

	public void addProtein(Protein protein) {
		if (!proteins.contains(protein)) {
			proteins.add(protein);
		}

	}

	public void addPSM(PSM psm) {

		if (!psms.contains(psm))
			psms.add(psm);

	}

	/**
	 * @param sample
	 *            the sample to set
	 */
	public void setSample(Sample sample) {
		this.sample = sample;
	}

	/**
	 * @param project
	 *            the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public Set<Peptide> getPeptides() {
		return peptides;
	}

	public void addPeptide(Peptide peptide) {
		if (!peptides.contains(peptide))
			peptides.add(peptide);

	}
}
