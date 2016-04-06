package edu.scripps.yates.utilities.proteomicsmodel;

import java.util.Set;

public interface HasProteins {
	public Set<Protein> getProteins();

	public void addProtein(Protein protein);
}
