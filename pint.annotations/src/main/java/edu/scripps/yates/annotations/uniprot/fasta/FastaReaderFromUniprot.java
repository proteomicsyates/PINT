package edu.scripps.yates.annotations.uniprot.fasta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.UniprotProteinLocalRetriever;
import edu.scripps.yates.annotations.uniprot.proteoform.fasta.ProteoFormFastaReader;
import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.fasta.Fasta;
import edu.scripps.yates.utilities.fasta.FastaReader;

/**
 * Implementation of a FastaReader that get the sequences from Uniprot once they
 * are read from the fasta file. So, it reads the fasta file to get the Uniprot
 * ACC and it goes to the Internet to get the sequence from there. If not found,
 * it will use the one in the fasta file.
 * 
 * @author salvador
 *
 */
public class FastaReaderFromUniprot extends FastaReader {
	private final static Logger log = Logger.getLogger(ProteoFormFastaReader.class);
	private final UniprotProteinLocalRetriever uplr;
	private final Set<String> canonicalUniprotEntries;
	private final String uniprotVersion;

	public FastaReaderFromUniprot(String fastaFileName, String uniprotVersion, UniprotProteinLocalRetriever uplr) {
		this(fastaFileName, null, uniprotVersion, uplr);
	}

	/**
	 * 
	 * 
	 * @param canonicalUniprotEntries set of uniprot accessions to consider from the
	 *                                fasta file
	 * @param uplr
	 */
	public FastaReaderFromUniprot(String fastaFileName, Set<String> canonicalUniprotEntries, String uniprotVersion,
			UniprotProteinLocalRetriever uplr) {
		super(fastaFileName);
		this.uplr = uplr;
		this.uniprotVersion = uniprotVersion;
		this.canonicalUniprotEntries = canonicalUniprotEntries;
	}

	@Override
	public Iterator<Fasta> getFastas() throws IOException {
		final Iterator<Fasta> fastaIterator = getFastaIterator();

		return fastaIterator;

	}

	private Iterator<Fasta> getFastaIterator() throws IOException {

		Set<String> uniprotACCs = null;

		// take just the ones in the canonicalUniprotEntries if not null
		if (canonicalUniprotEntries != null) {
			uniprotACCs = new HashSet<String>();
			uniprotACCs.addAll(canonicalUniprotEntries);
		} else {
			uniprotACCs = getUniprotACCsFromFasta();
		}

		// look for proteoforms of the proteins
		final Iterator<Entry> uniprotEntriesIterator = getUniprotEntryIterator(uniprotACCs);

		return new FastaIteratorFromUniprotEntries(uniprotEntriesIterator);

	}

	private Iterator<Entry> getUniprotEntryIterator(Set<String> uniprotACCs) {
		final Map<String, Entry> annotatedProteins = uplr.getAnnotatedProteins(uniprotVersion, uniprotACCs);
		final List<Entry> ret = new ArrayList<Entry>();
		for (final String acc : uniprotACCs) {
			if (annotatedProteins.containsKey(acc)) {
				ret.add(annotatedProteins.get(acc));
			}
		}
		return ret.iterator();
	}

	@Override
	public int getNumberFastas() throws IOException {
		throw new IOException("Using an iterator doesn't allow to know how many entries we have");
	}
}
