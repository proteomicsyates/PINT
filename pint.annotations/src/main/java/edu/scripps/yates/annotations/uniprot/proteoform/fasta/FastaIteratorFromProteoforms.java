package edu.scripps.yates.annotations.uniprot.proteoform.fasta;

import java.util.Iterator;

import edu.scripps.yates.annotations.uniprot.proteoform.Proteoform;
import edu.scripps.yates.utilities.fasta.Fasta;

public class FastaIteratorFromProteoforms implements Iterator<Fasta> {
	private final Iterator<Proteoform> proteoformIterator;

	public FastaIteratorFromProteoforms(Iterator<Proteoform> proteoformList) {

		proteoformIterator = proteoformList;
	}

	@Override
	public boolean hasNext() {
		return proteoformIterator.hasNext();
	}

	@Override
	public Fasta next() {
		final Proteoform proteoform = proteoformIterator.next();
		return new FastaAdapterFromProteoform(proteoform).adapt();
	}

}
