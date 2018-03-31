package edu.scripps.yates.annotations.uniprot.proteoform.fasta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.scripps.yates.annotations.uniprot.proteoform.Proteoform;
import edu.scripps.yates.utilities.fasta.Fasta;

public class FastaIteratorFromProteoforms implements Iterator<Fasta> {
	private final List<Proteoform> proteoforms = new ArrayList<Proteoform>();
	private Iterator<Proteoform> proteoformIterator;

	public FastaIteratorFromProteoforms(List<Proteoform> proteoformList) {

		for (Proteoform proteoform : proteoformList) {
			if (proteoform.isOriginal()) {
				continue;
			}
			proteoforms.add(proteoform);
		}

		proteoformIterator = proteoforms.iterator();
	}

	@Override
	public boolean hasNext() {
		return proteoformIterator.hasNext();
	}

	@Override
	public Fasta next() {
		Proteoform proteoform = proteoformIterator.next();
		return new FastaAdapterFromProteoform(proteoform).adapt();
	}

}
