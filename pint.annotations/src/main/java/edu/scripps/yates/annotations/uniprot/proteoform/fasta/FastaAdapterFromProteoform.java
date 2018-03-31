package edu.scripps.yates.annotations.uniprot.proteoform.fasta;

import edu.scripps.yates.annotations.uniprot.proteoform.Proteoform;
import edu.scripps.yates.utilities.fasta.Fasta;
import edu.scripps.yates.utilities.fasta.FastaImpl;
import edu.scripps.yates.utilities.pattern.Adapter;

public class FastaAdapterFromProteoform implements Adapter<Fasta> {

	private Proteoform proteoform;

	public FastaAdapterFromProteoform(Proteoform proteoform) {
		this.proteoform = proteoform;
	}

	@Override
	public Fasta adapt() {
		Fasta fasta = new FastaImpl(">" + proteoform.getId() + " " + proteoform.getDescription(), proteoform.getSeq());
		return fasta;
	}

}
