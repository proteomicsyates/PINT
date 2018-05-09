package edu.scripps.yates.annotations.uniprot.proteoform.fasta;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Iterators;

import edu.scripps.yates.annotations.uniprot.proteoform.Proteoform;
import edu.scripps.yates.annotations.uniprot.proteoform.UniprotProteoformRetriever;
import edu.scripps.yates.utilities.fasta.Fasta;
import edu.scripps.yates.utilities.fasta.FastaReader;

/**
 * Reads a fastaFile and then provides an iterator to iterate over proteoforms
 * of the proteins in the fasta file. It is important to know that the order of
 * the proteins in the iterator will be kept.
 * 
 * @author salvador
 *
 */
public class ProteoFormFastaReader extends FastaReader {
	private final UniprotProteoformRetriever proteoFormRetriever;
	private final Set<String> canonicalUniprotEntries;

	public ProteoFormFastaReader(String fastaFileName, UniprotProteoformRetriever proteoFormRetriever) {
		super(fastaFileName);
		this.proteoFormRetriever = proteoFormRetriever;
		canonicalUniprotEntries = null;
	}

	/**
	 * 
	 * 
	 * @param canonicalUniprotEntries
	 *            set of uniprot accessions to consider from the fasta file
	 * @param proteoFormRetriever
	 */
	public ProteoFormFastaReader(Set<String> canonicalUniprotEntries, UniprotProteoformRetriever proteoFormRetriever) {
		super(null);
		this.proteoFormRetriever = proteoFormRetriever;
		this.canonicalUniprotEntries = canonicalUniprotEntries;
	}

	@Override
	public Iterator<Fasta> getFastas() throws IOException {
		return Iterators.concat(super.getFastas(), getProteoFormFastaIterator());
	}

	public Iterator<Fasta> getProteoFormFastaIterator() throws IOException {

		Set<String> uniprotACCs = null;

		// take just the ones in the canonicalUniprotEntries if not null
		if (canonicalUniprotEntries != null) {
			uniprotACCs = new HashSet<String>();
			uniprotACCs.addAll(canonicalUniprotEntries);
		} else {
			uniprotACCs = getUniprotACCsFromFasta();
		}
		// look for proteoforms of the proteins
		final Iterator<Proteoform> proteoformList = proteoFormRetriever.getProteoformIterator(uniprotACCs);

		return new FastaIteratorFromProteoforms(proteoformList);

	}

	@Override
	public int getNumberFastas() throws IOException {
		throw new IOException("Using an iterator doesn't allow to know how many proteoforms we have");
	}
}
