package edu.scripps.yates.annotations.uniprot.fasta;

import java.util.List;

import edu.scripps.yates.utilities.annotations.uniprot.ProteinExistence;
import edu.scripps.yates.utilities.annotations.uniprot.UniprotEntryUtil;
import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.fasta.Fasta;
import edu.scripps.yates.utilities.fasta.FastaImpl;
import edu.scripps.yates.utilities.pattern.Adapter;
import edu.scripps.yates.utilities.util.Pair;

public class FastaAdapterFromUniprotEntry implements Adapter<Fasta> {

	private final Entry entry;

	public FastaAdapterFromUniprotEntry(Entry entry) {
		this.entry = entry;
	}

	@Override
	public Fasta adapt() {
		final String description = UniprotEntryUtil.getProteinDescription(entry);
		final String primaryAcc = UniprotEntryUtil.getPrimaryAccession(entry);
		final String name = UniprotEntryUtil.getNames(entry).get(0);

		String sp = "sp";
		if (!UniprotEntryUtil.isSwissProt(entry)) {
			sp = "tr";
		}
		String defline = ">" + sp + "|" + primaryAcc + "|" + name + " " + description;
		final List<Pair<String, String>> geneNames = UniprotEntryUtil.getGeneName(entry, true, true);
		if (geneNames != null && !geneNames.isEmpty()) {
			defline += " GN=" + geneNames.get(0).getFirstelement();
		}
		final String taxonomy = UniprotEntryUtil.getTaxonomyName(entry);
		if (taxonomy != null) {
			defline += " OS=" + taxonomy;
		}
		final String taxID = UniprotEntryUtil.getTaxonomyNCBIID(entry);
		if (taxonomy != null) {
			defline += " OX=" + taxID;
		}
		final Integer sequenceVersion = UniprotEntryUtil.getSequenceVersion(entry);
		if (sequenceVersion != null) {
			defline += " SV=" + sequenceVersion;
		}
		final ProteinExistence pe = UniprotEntryUtil.getProteinExistence(entry);
		if (pe != null) {
			defline += " PE=" + pe.getNum();
		}
		final Fasta fasta = new FastaImpl(defline, UniprotEntryUtil.getProteinSequence(entry), false);
		return fasta;
	}

}
