package edu.scripps.yates.annotations.uniprot.proteoform;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public interface UniprotProteoformRetriever {
	public List<Proteoform> getProteoformsFromOneEntry(String uniprotACC);

	public Map<String, List<Proteoform>> getProteoforms(Collection<String> uniprotACCs);

	public Map<String, List<Proteoform>> getProteoforms(String... uniprotACCs);

	public Iterator<Proteoform> getProteoformIterator(Collection<String> uniprotACCs);

	public Iterator<Proteoform> getProteoformIterator(String... uniprotACCs);
}
