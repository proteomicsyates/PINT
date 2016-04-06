package edu.scripps.yates.proteindb.persistence.plugin;

import java.util.Collection;
import java.util.List;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;

public interface ProteinFilter {
	public List<Protein> filter(Collection<Protein> proteins);
}
