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
		String defline = ">sp|" + proteoform.getId() + "| " + proteoform.getDescription();
		if (proteoform.getGene() != null) {
			defline += " GN=" + proteoform.getGene();
		}
		if (proteoform.getTaxonomy() != null) {
			defline += " OS=" + proteoform.getTaxonomy();
		}
		Fasta fasta = new FastaImpl(defline, proteoform.getSeq());
		return fasta;
	}

}
