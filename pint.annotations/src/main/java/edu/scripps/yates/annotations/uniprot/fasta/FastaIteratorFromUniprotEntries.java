package edu.scripps.yates.annotations.uniprot.fasta;

import java.util.Iterator;

import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.fasta.Fasta;

public class FastaIteratorFromUniprotEntries implements Iterator<Fasta> {
	private final Iterator<Entry> uniprotEntryIterator;

	public FastaIteratorFromUniprotEntries(Iterator<Entry> uniprotEntryIterator) {

		this.uniprotEntryIterator = uniprotEntryIterator;
	}

	@Override
	public boolean hasNext() {
		return uniprotEntryIterator.hasNext();
	}

	@Override
	public Fasta next() {
		final Entry entry = uniprotEntryIterator.next();
		return new FastaAdapterFromUniprotEntry(entry).adapt();
	}

}
