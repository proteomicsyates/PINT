package edu.scripps.yates.annotations.uniprot.proteoform;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.annotations.uniprot.proteoform.model.Proteoform;

public interface UniprotProteoformRetriever {
	public List<Proteoform> getVariantsFromOneEntry(String uniprotACC);

	public Map<String, List<Proteoform>> getVariants(Set<String> uniprotACCs);

	public Map<String, List<Proteoform>> getVariants(String... uniprotACCs);
}
