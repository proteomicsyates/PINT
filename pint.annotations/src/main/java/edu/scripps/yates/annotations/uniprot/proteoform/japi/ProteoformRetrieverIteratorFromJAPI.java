package edu.scripps.yates.annotations.uniprot.proteoform.japi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import edu.scripps.yates.annotations.uniprot.UniprotProteinLocalRetriever;
import edu.scripps.yates.annotations.uniprot.proteoform.Proteoform;

public class ProteoformRetrieverIteratorFromJAPI implements Iterator<Proteoform> {
	private final Set<String> uniprotACCs = new HashSet<String>();
	private final boolean retrieveIsoforms;
	private final boolean retrievePTMs;
	private final UniprotProteinLocalRetriever uplr;
	private final List<String> accs;
	private final int currentAccIndex = 0;
	private final static int CHUNCK_SIZE = 100; // the number of accs that are
												// retrieved everytime the list
												// of proteoform is empty
	private final List<Proteoform> proteoformList = new ArrayList<Proteoform>();

	public ProteoformRetrieverIteratorFromJAPI(Collection<String> uniprotACCs, boolean retrieveIsoforms,
			boolean retrievePTMs, UniprotProteinLocalRetriever uplr) {
		this.uniprotACCs.addAll(uniprotACCs);
		this.retrieveIsoforms = retrieveIsoforms;
		this.uplr = uplr;
		this.retrievePTMs = retrievePTMs;
		// I do this to make sure there is no repeated elements in the list
		accs = uniprotACCs.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList());
	}

	@Override
	public boolean hasNext() {
		return currentAccIndex < accs.size() - 1 || proteoformList.size() > 0;
	}

	@Override
	public Proteoform next() {
		if (proteoformList.isEmpty()) {
			final List<String> accList = accs.subList(currentAccIndex,
					Math.min(currentAccIndex + CHUNCK_SIZE, accs.size() - 1));
			final Map<String, List<Proteoform>> proteoformsFromList = UniprotProteoformJAPIRetriever.getProteoforms(
					accList, UniprotProteoformJAPIRetriever.defaultChunkSize, retrieveIsoforms, retrievePTMs, uplr);
			final Set<Proteoform> set = new HashSet<Proteoform>();
			for (final List<Proteoform> proteoforms : proteoformsFromList.values()) {
				set.addAll(proteoforms);
			}
			proteoformList.addAll(set);
		}
		if (!proteoformList.isEmpty()) {
			final Proteoform ret = proteoformList.get(0);
			proteoformList.remove(0);
			return ret;
		}
		return null;
	}

}
