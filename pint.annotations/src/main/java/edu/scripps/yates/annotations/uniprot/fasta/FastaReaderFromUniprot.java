package edu.scripps.yates.annotations.uniprot.fasta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.compomics.dbtoolkit.io.implementations.FASTADBLoader;
import com.compomics.dbtoolkit.io.interfaces.DBLoader;
import com.compomics.util.protein.Protein;

import edu.scripps.yates.annotations.uniprot.UniprotEntryAdapterFromFASTA;
import edu.scripps.yates.annotations.uniprot.UniprotFastaRetrieverFromUniprotIsoformFastaFile;
import edu.scripps.yates.annotations.uniprot.UniprotProteinLocalRetriever;
import edu.scripps.yates.annotations.uniprot.proteoform.fasta.ProteoFormFastaReader;
import edu.scripps.yates.utilities.annotations.uniprot.UniprotEntryUtil;
import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.fasta.Fasta;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.fasta.FastaReader;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

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
	private final Map<String, String> proteinSequencesFromFASTAByAcc = new THashMap<String, String>();
	private final Map<String, String> fastaHeadersFromFastaByAcc = new THashMap<String, String>();
	private final boolean includeIsoforms;
	private Integer numFastas;

	/**
	 * 
	 * @param fastaFileName
	 * @param uniprotVersion
	 * @param uplr
	 * @param includeIsoforms if true, isoforms from main forms will be retrieved
	 *                        too from uniprot even though they may not be in the
	 *                        fasta file
	 */
	public FastaReaderFromUniprot(String fastaFileName, String uniprotVersion, UniprotProteinLocalRetriever uplr,
			boolean includeIsoforms) {
		this(fastaFileName, null, uniprotVersion, uplr, includeIsoforms);
	}

	/**
	 * 
	 * 
	 * @param canonicalUniprotEntries
	 * @param uplr
	 */

	/**
	 * 
	 * @param fastaFileName
	 * @param canonicalUniprotEntries set of uniprot accessions to consider from the
	 *                                fasta file
	 * @param uniprotVersion
	 * @param uplr
	 * @param includeIsoforms         if true, isoforms from main forms will be
	 *                                retrieved too from uniprot even though they
	 *                                may not be in the fasta file
	 */
	public FastaReaderFromUniprot(String fastaFileName, Set<String> canonicalUniprotEntries, String uniprotVersion,
			UniprotProteinLocalRetriever uplr, boolean includeIsoforms) {
		super(fastaFileName);
		this.uplr = uplr;
		this.uniprotVersion = uniprotVersion;
		this.canonicalUniprotEntries = canonicalUniprotEntries;
		this.includeIsoforms = includeIsoforms;
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
			uniprotACCs = getACCsFromFasta();
		}

		// look for proteoforms of the proteins
		final Iterator<Entry> uniprotEntriesIterator = getUniprotEntryIterator(uniprotACCs);

		return new FastaIteratorFromUniprotEntries(uniprotEntriesIterator);

	}

	private Iterator<Entry> getUniprotEntryIterator(Set<String> uniprotACCs) {
		final Map<String, Entry> annotatedProteins = uplr.getAnnotatedProteins(uniprotVersion, uniprotACCs);

		UniprotFastaRetrieverFromUniprotIsoformFastaFile isoformfastaRetriever = null;
		Map<String, Entry> isoformEntries = null;
		if (includeIsoforms) {
			isoformfastaRetriever = new UniprotFastaRetrieverFromUniprotIsoformFastaFile(
					uplr.getUniprotReleasesFolder(), uplr.getLatestUniprotVersionFolderName());
			isoformEntries = isoformfastaRetriever.run();
		}
		final List<Entry> ret = new ArrayList<Entry>();
		for (final String acc : uniprotACCs) {
			if (annotatedProteins.containsKey(acc)) {
				final Entry entry = annotatedProteins.get(acc);
				if (entry != null) {
					ret.add(entry);
				} else {
					throw new IllegalArgumentException("Entry for " + acc + " shoudn't be null");
				}
				// remove sequence and fasta header from local maps to save memory
				proteinSequencesFromFASTAByAcc.remove(acc);
				fastaHeadersFromFastaByAcc.remove(acc);
				if (includeIsoforms && isoformfastaRetriever != null && isoformEntries != null) {
					// now, check whether the entry is a main form and if so, get the isoforms
					// entries
					final String isoformVersion = FastaParser.getIsoformVersion(acc);
					if (isoformVersion == null || "1".equals(isoformVersion)) {
						final List<String> isoformsAccs = UniprotEntryUtil.getIsoforms(entry);
						for (final String isoformAcc : isoformsAccs) {
							if (isoformEntries.containsKey(isoformAcc)) {
								final Entry isoformEntry = isoformEntries.get(isoformAcc);
								ret.add(isoformEntry);
							}
						}

					}
				}
			} else {
				// if not in the uniprot, it may be because of it is an obsolete sequence in the
				// database, or because it is a decoy
				// so, we get the sequence from the actual FASTA file.
				if (proteinSequencesFromFASTAByAcc.containsKey(acc)) {
					final String seq = proteinSequencesFromFASTAByAcc.get(acc);
					final String fastaHeader = fastaHeadersFromFastaByAcc.get(acc);
					ret.add(new UniprotEntryAdapterFromFASTA(acc, fastaHeader, seq).adapt());
				} else {
					throw new IllegalArgumentException(
							"Protein sequence for protein " + acc + " is not found in Uniprot nor in FASTA file??");
				}
			}
		}
		numFastas = ret.size();
		return ret.iterator();
	}

	@Override
	public int getNumberFastas() throws IOException {
		if (numFastas != null) {
			return numFastas;
		}
		throw new IOException("Using an iterator doesn't allow to know how many entries we have");
	}

	/**
	 * We override this method in order to keep protein sequences from FASTA. Then
	 * if some of the proteins are not found in Uniprot, we will take those from the
	 * FASTA
	 */
	@Override
	public Set<String> getACCsFromFasta() throws IOException {
		if (proteinACCs == null || proteinACCs.isEmpty()) {
			proteinACCs = new THashSet<String>();
			final DBLoader loader = new FASTADBLoader();
			if (fastaFileName != null && loader.canReadFile(new File(fastaFileName))) {

				Protein protein = null;
				loader.load(fastaFileName);
				while ((protein = loader.nextProtein()) != null) {
					final String accession = FastaParser.getACC(protein.getHeader().getRawHeader()).getAccession();
					proteinACCs.add(accession);
					this.proteinSequencesFromFASTAByAcc.put(accession, protein.getSequence().getSequence());
					this.fastaHeadersFromFastaByAcc.put(accession, protein.getHeader().getRawHeader());
				}
			}
		}
		return proteinACCs;
	}
}
