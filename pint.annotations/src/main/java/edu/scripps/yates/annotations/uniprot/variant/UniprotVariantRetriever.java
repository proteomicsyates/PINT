package edu.scripps.yates.annotations.uniprot.variant;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.annotations.uniprot.variant.model.Variant;

public interface UniprotVariantRetriever {
	public List<Variant> getVariantsFromOneEntry(String uniprotACC);

	public Map<String, List<Variant>> getVariants(Set<String> uniprotACCs);

	public Map<String, List<Variant>> getVariants(String... uniprotACCs);
}
