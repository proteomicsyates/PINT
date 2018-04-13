package edu.scripps.yates.annotations.uniprot.proteoform.fasta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.compomics.dbtoolkit.io.implementations.FASTADBLoader;
import com.compomics.dbtoolkit.io.interfaces.DBLoader;
import com.compomics.util.protein.Protein;
import com.google.common.collect.Iterators;

import edu.scripps.yates.annotations.uniprot.proteoform.Proteoform;
import edu.scripps.yates.annotations.uniprot.proteoform.UniprotProteoformRetriever;
import edu.scripps.yates.utilities.fasta.Fasta;
import edu.scripps.yates.utilities.fasta.FastaParser;
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
	private List<String> uniprotACCs;

	public ProteoFormFastaReader(String fastaFileName, UniprotProteoformRetriever proteoFormRetriever) {
		super(fastaFileName);
		this.proteoFormRetriever = proteoFormRetriever;
	}

	@Override
	public Iterator<Fasta> getFastas() throws IOException {
		return Iterators.concat(super.getFastas(), getProteoFormFastaIterator());
	}

	private List<String> getUniprotACCs() throws IOException {
		if (uniprotACCs == null || uniprotACCs.isEmpty()) {
			uniprotACCs = new ArrayList<String>();
			final DBLoader loader = new FASTADBLoader();
			if (loader.canReadFile(new File(fastaFileName))) {

				Protein protein = null;
				loader.load(fastaFileName);
				while ((protein = loader.nextProtein()) != null) {
					String accession = protein.getHeader().getAccession();
					if (accession == null) {
						accession = protein.getHeader().getAccessionOrRest();
					}
					final String uniprotAccession = FastaParser.getUniProtACC(accession);
					if (uniprotAccession != null) {
						uniprotACCs.add(uniprotAccession);
					}
				}
			}
		}
		return uniprotACCs;
	}

	private Iterator<Fasta> getProteoFormFastaIterator() throws IOException {

		final List<String> uniprotACCs = getUniprotACCs();

		// look for proteoforms of the proteins
		final List<Proteoform> proteoformList = getProteoformList(uniprotACCs);

		return new FastaIteratorFromProteoforms(proteoformList);

	}

	private List<Proteoform> getProteoformList(List<String> uniprotACCs) {
		final Map<String, List<Proteoform>> proteoformMap = proteoFormRetriever.getProteoforms(uniprotACCs);
		final List<Proteoform> proteoformList = new ArrayList<Proteoform>();
		for (final String acc : uniprotACCs) {
			if (proteoformMap.containsKey(acc)) {
				final List<Proteoform> proteoformList2 = proteoformMap.get(acc);

				for (final Proteoform proteoform : proteoformList2) {
					if (proteoform.isOriginal()) {
						continue;
					}
					proteoformList.add(proteoform);
				}
			}
		}
		return proteoformList;
	}

	@Override
	public int getNumberFastas() throws IOException {
		final List<Proteoform> proteoformList = getProteoformList(getUniprotACCs());
		final int proteoformNumber = proteoformList.size();
		final int mainProteinNumber = super.getNumberFastas();
		return mainProteinNumber + proteoformNumber;
	}
}
