package edu.scripps.yates.annotations.uniprot.fasta;

import java.util.List;

import org.apache.log4j.Logger;

import edu.scripps.yates.utilities.annotations.uniprot.ProteinExistence;
import edu.scripps.yates.utilities.annotations.uniprot.UniprotEntryUtil;
import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.fasta.Fasta;
import edu.scripps.yates.utilities.fasta.FastaImpl;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.pattern.Adapter;
import edu.scripps.yates.utilities.util.Pair;

public class FastaAdapterFromUniprotEntry implements Adapter<Fasta> {
	private static Logger log = Logger.getLogger(FastaAdapterFromUniprotEntry.class);
	private final Entry entry;

	public FastaAdapterFromUniprotEntry(Entry entry) {
		this.entry = entry;
	}

	@Override
	public Fasta adapt() {
		boolean isProteoform = false;
		final String description = UniprotEntryUtil.getProteinDescription(entry);
		final String primaryAcc = UniprotEntryUtil.getPrimaryAccession(entry);
		final String isoformVersion = FastaParser.getIsoformVersion(primaryAcc);
		if (isoformVersion != null) {
			isProteoform = true;
		}
		final List<String> names = UniprotEntryUtil.getNames(entry);
		String name = null;
		if (names == null || names.isEmpty()) {
			log.warn("No names for uniprot Entry " + primaryAcc);
			name = description;
		} else {
			name = names.get(0);
		}

		String sp = "sp";
		if (!UniprotEntryUtil.isSwissProt(entry)) {
			sp = "tr";
		}
		String defline = ">" + sp + "|" + primaryAcc + "|" + name + " " + description;
		if (FastaParser.isReverse(primaryAcc)) {
			defline = ">" + primaryAcc + " " + description;
		}
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
		final Fasta fasta = new FastaImpl(defline, UniprotEntryUtil.getProteinSequence(entry), isProteoform);
		return fasta;
	}

}
