package edu.scripps.yates.shared.model;

import java.io.Serializable;

public class PrincipalInvestigatorBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1442132861943353928L;
	private String name;
	private String email;
	private String institution;
	private String country;

	public PrincipalInvestigatorBean() {

	}

	public void setName(String name) {
		this.name = name;

	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getName() {
		return name;
	}

}
