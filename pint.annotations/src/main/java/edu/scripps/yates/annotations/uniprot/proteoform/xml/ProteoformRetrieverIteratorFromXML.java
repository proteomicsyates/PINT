package edu.scripps.yates.annotations.uniprot.proteoform.xml;

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

public class ProteoformRetrieverIteratorFromXML implements Iterator<Proteoform> {
	private final boolean retrieveIsoforms;
	private final boolean retrievePTMs;
	private final String uniprotVersion;
	private final UniprotProteinLocalRetriever uplr;
	private final List<String> accs;
	private int currentAccIndex = 0;
	private final static int CHUNCK_SIZE = 100; // the number of accs that are
												// retrieved everytime the list
												// of proteoform is empty
	private final List<Proteoform> proteoformList = new ArrayList<Proteoform>();

	public ProteoformRetrieverIteratorFromXML(Collection<String> uniprotACCs, boolean retrieveIsoforms,
			boolean retrievePTMs, String uniprotVersion, UniprotProteinLocalRetriever uplr) {
		this.retrieveIsoforms = retrieveIsoforms;
		this.uniprotVersion = uniprotVersion;
		this.uplr = uplr;
		this.retrievePTMs = retrievePTMs;
		// I do this to make sure there is no repeated elements in the list
		if (uniprotACCs instanceof Set) {
			accs = ((Set<String>) uniprotACCs).stream().collect(Collectors.toList());
		} else {
			accs = uniprotACCs.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList());
		}
	}

	@Override
	public boolean hasNext() {
		if (currentAccIndex >= accs.size()) {
			return false;
		}
		if (proteoformList.isEmpty()) {
			loadList();
		}
		if (proteoformList.isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public Proteoform next() {
		if (proteoformList.isEmpty()) {
			loadList();
		}
		if (!proteoformList.isEmpty()) {
			final Proteoform ret = proteoformList.get(0);
			proteoformList.remove(0);
			return ret;
		}
		return null;
	}

	private void loadList() {
		final int nextIndex = Math.min(currentAccIndex + CHUNCK_SIZE, accs.size() - 1);
		final List<String> accList = accs.subList(currentAccIndex, nextIndex);
		if (accList.contains("P04434")) {
			System.out.println("asdf");
		}
		currentAccIndex = nextIndex;
		final Map<String, List<Proteoform>> proteoformsFromList = UniprotProteoformRetrieverFromXML
				.getProteoformsFromList(accList, retrieveIsoforms, retrievePTMs, uniprotVersion, uplr);
		System.out.println(proteoformsFromList.containsKey("P04434"));
		final Set<Proteoform> set = new HashSet<Proteoform>();
		for (final List<Proteoform> proteoforms : proteoformsFromList.values()) {
			set.addAll(proteoforms);
		}
		proteoformList.addAll(set);
	}

}
