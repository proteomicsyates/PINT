package edu.scripps.yates.annotations.uniprot;

import java.util.Collection;
import java.util.Map;

import edu.scripps.yates.annotations.uniprot.xml.Entry;

public interface UniprotRetriever {

	public Map<String, Entry> getAnnotatedProteins(String uniprotVersion,
			Collection<String> accessions);

	public Map<String, Entry> getAnnotatedProtein(String uniprotVersion,
			String accession);

	/**
	 * Gets the set of protein accessions that have been not be able to get
	 * retrieved
	 * 
	 * @return
	 */
	public Collection<String> getMissingAccessions();
}
