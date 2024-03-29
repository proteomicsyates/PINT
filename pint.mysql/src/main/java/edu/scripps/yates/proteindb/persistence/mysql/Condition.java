package edu.scripps.yates.proteindb.persistence.mysql;

// Generated Feb 24, 2015 2:42:07 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * Condition generated by hbm2java
 */
public class Condition implements java.io.Serializable {

	private Integer id;
	private Project project;
	private Sample sample;
	private String name;
	private String description;
	private Double value;
	private String unit;
	private Set psmAmounts = new HashSet(0);
	private Set peptides = new HashSet(0);
	private Set proteinAmounts = new HashSet(0);
	private Set ratioDescriptorsForExperimentalCondition2Id = new HashSet(0);
	private Set proteins = new HashSet(0);
	private Set peptideAmounts = new HashSet(0);
	private Set ratioDescriptorsForExperimentalCondition1Id = new HashSet(0);
	private Set psms = new HashSet(0);

	public Condition() {
	}

	public Condition(Project project, Sample sample, String name) {
		this.project = project;
		this.sample = sample;
		this.name = name;
	}

	public Condition(Project project, Sample sample, String name,
			String description, Double value, String unit, Set psmAmounts,
			Set peptides, Set proteinAmounts,
			Set ratioDescriptorsForExperimentalCondition2Id, Set proteins,
			Set peptideAmounts,
			Set ratioDescriptorsForExperimentalCondition1Id, Set psms) {
		this.project = project;
		this.sample = sample;
		this.name = name;
		this.description = description;
		this.value = value;
		this.unit = unit;
		this.psmAmounts = psmAmounts;
		this.peptides = peptides;
		this.proteinAmounts = proteinAmounts;
		this.ratioDescriptorsForExperimentalCondition2Id = ratioDescriptorsForExperimentalCondition2Id;
		this.proteins = proteins;
		this.peptideAmounts = peptideAmounts;
		this.ratioDescriptorsForExperimentalCondition1Id = ratioDescriptorsForExperimentalCondition1Id;
		this.psms = psms;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Sample getSample() {
		return this.sample;
	}

	public void setSample(Sample sample) {
		this.sample = sample;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getValue() {
		return this.value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Set getPsmAmounts() {
		return this.psmAmounts;
	}

	public void setPsmAmounts(Set psmAmounts) {
		this.psmAmounts = psmAmounts;
	}

	public Set getPeptides() {
		return this.peptides;
	}

	public void setPeptides(Set peptides) {
		this.peptides = peptides;
	}

	public Set getProteinAmounts() {
		return this.proteinAmounts;
	}

	public void setProteinAmounts(Set proteinAmounts) {
		this.proteinAmounts = proteinAmounts;
	}

	public Set getRatioDescriptorsForExperimentalCondition2Id() {
		return this.ratioDescriptorsForExperimentalCondition2Id;
	}

	public void setRatioDescriptorsForExperimentalCondition2Id(
			Set ratioDescriptorsForExperimentalCondition2Id) {
		this.ratioDescriptorsForExperimentalCondition2Id = ratioDescriptorsForExperimentalCondition2Id;
	}

	public Set getProteins() {
		return this.proteins;
	}

	public void setProteins(Set proteins) {
		this.proteins = proteins;
	}

	public Set getPeptideAmounts() {
		return this.peptideAmounts;
	}

	public void setPeptideAmounts(Set peptideAmounts) {
		this.peptideAmounts = peptideAmounts;
	}

	public Set getRatioDescriptorsForExperimentalCondition1Id() {
		return this.ratioDescriptorsForExperimentalCondition1Id;
	}

	public void setRatioDescriptorsForExperimentalCondition1Id(
			Set ratioDescriptorsForExperimentalCondition1Id) {
		this.ratioDescriptorsForExperimentalCondition1Id = ratioDescriptorsForExperimentalCondition1Id;
	}

	public Set getPsms() {
		return this.psms;
	}

	public void setPsms(Set psms) {
		this.psms = psms;
	}

}
