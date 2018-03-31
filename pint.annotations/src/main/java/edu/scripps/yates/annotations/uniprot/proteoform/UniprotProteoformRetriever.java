package edu.scripps.yates.annotations.uniprot.proteoform;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UniprotProteoformRetriever {
	public List<Proteoform> getProteoformsFromOneEntry(String uniprotACC);

	public Map<String, List<Proteoform>> getProteoforms(Set<String> uniprotACCs);

	public Map<String, List<Proteoform>> getProteoforms(String... uniprotACCs);
}
