package edu.scripps.yates.proteindb.persistence.mysql;

// Generated Feb 24, 2015 2:42:07 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * ProteinAccession generated by hbm2java
 */
public class ProteinAccession implements java.io.Serializable {

	private String accession;
	private String accessionType;
	private String description;
	private boolean isPrimary;
	private String alternativeNames;
	private Set proteins = new HashSet(0);

	public ProteinAccession() {
	}

	public ProteinAccession(String accession, String accessionType,
			boolean isPrimary) {
		this.accession = accession;
		this.accessionType = accessionType;
		this.isPrimary = isPrimary;
	}

	public ProteinAccession(String accession, String accessionType,
			String description, boolean isPrimary, String alternativeNames,
			Set proteins) {
		this.accession = accession;
		this.accessionType = accessionType;
		this.description = description;
		this.isPrimary = isPrimary;
		this.alternativeNames = alternativeNames;
		this.proteins = proteins;
	}

	public String getAccession() {
		return this.accession;
	}

	public void setAccession(String accession) {
		this.accession = accession;
	}

	public String getAccessionType() {
		return this.accessionType;
	}

	public void setAccessionType(String accessionType) {
		this.accessionType = accessionType;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isIsPrimary() {
		return this.isPrimary;
	}

	public void setIsPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public String getAlternativeNames() {
		return this.alternativeNames;
	}

	public void setAlternativeNames(String alternativeNames) {
		this.alternativeNames = alternativeNames;
	}

	public Set getProteins() {
		return this.proteins;
	}

	public void setProteins(Set proteins) {
		this.proteins = proteins;
	}

}
