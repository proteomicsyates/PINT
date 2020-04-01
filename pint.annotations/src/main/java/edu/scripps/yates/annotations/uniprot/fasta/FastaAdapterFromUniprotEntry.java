package edu.scripps.yates.annotations.uniprot.fasta;

import edu.scripps.yates.utilities.annotations.uniprot.UniprotEntryUtil;
import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.fasta.Fasta;
import edu.scripps.yates.utilities.fasta.FastaImpl;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.pattern.Adapter;

public class FastaAdapterFromUniprotEntry implements Adapter<Fasta> {
	private final Entry entry;

	public FastaAdapterFromUniprotEntry(Entry entry) {
		this.entry = entry;
	}

	@Override
	public Fasta adapt() {
		boolean isProteoform = false;
		final String primaryAcc = UniprotEntryUtil.getPrimaryAccession(entry);
		final String isoformVersion = FastaParser.getIsoformVersion(primaryAcc);
		if (isoformVersion != null) {
			isProteoform = true;
		}

		final String defline = UniprotEntryUtil.getFullFastaHeader(entry);
		final Fasta fasta = new FastaImpl(defline, UniprotEntryUtil.getProteinSequence(entry), isProteoform);
		return fasta;
	}

}
