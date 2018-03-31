package edu.scripps.yates.annotations.uniprot.proteoform.fasta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.compomics.dbtoolkit.io.implementations.FASTADBLoader;
import com.compomics.dbtoolkit.io.interfaces.DBLoader;
import com.compomics.util.protein.Protein;
import com.google.common.collect.Iterators;

import edu.scripps.yates.annotations.uniprot.proteoform.Proteoform;
import edu.scripps.yates.annotations.uniprot.proteoform.UniprotProteoformRetriever;
import edu.scripps.yates.utilities.fasta.Fasta;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.fasta.FastaReader;

public class ProteoFormFastaReader extends FastaReader {
	private final UniprotProteoformRetriever proteoFormRetriever;
	private Set<String> uniprotACCs;

	public ProteoFormFastaReader(String fastaFileName, UniprotProteoformRetriever proteoFormRetriever) {
		super(fastaFileName);
		this.proteoFormRetriever = proteoFormRetriever;
	}

	@Override
	public Iterator<Fasta> getFastas() throws IOException {
		return Iterators.concat(super.getFastas(), getProteoFormFastaIterator());
	}

	private Set<String> getUniprotACCs() throws IOException {
		if (uniprotACCs == null || uniprotACCs.isEmpty()) {
			uniprotACCs = new HashSet<String>();
			DBLoader loader = new FASTADBLoader();
			if (loader.canReadFile(new File(fastaFileName))) {

				Protein protein = null;
				loader.load(fastaFileName);
				while ((protein = loader.nextProtein()) != null) {
					String accession = protein.getHeader().getAccession();
					String uniprotAccession = FastaParser.getUniProtACC(accession);
					if (uniprotAccession != null) {
						uniprotACCs.add(uniprotAccession);
					}
				}
			}
		}
		return uniprotACCs;
	}

	private Iterator<Fasta> getProteoFormFastaIterator() throws IOException {

		Set<String> uniprotACCs = getUniprotACCs();

		// look for proteoforms of the proteins
		List<Proteoform> proteoformList = getProteoformList(uniprotACCs);

		return new FastaIteratorFromProteoforms(proteoformList);

	}

	private List<Proteoform> getProteoformList(Set<String> uniprotACCs) {
		Map<String, List<Proteoform>> proteoformMap = proteoFormRetriever.getProteoforms(uniprotACCs);
		List<Proteoform> proteoformList = new ArrayList<Proteoform>();
		for (List<Proteoform> proteoformList2 : proteoformMap.values()) {
			for (Proteoform proteoform : proteoformList2) {
				if (proteoform.isOriginal()) {
					continue;
				}
				proteoformList.add(proteoform);
			}
		}
		return proteoformList;
	}

	@Override
	public int getNumberFastas() throws IOException {
		List<Proteoform> proteoformList = getProteoformList(getUniprotACCs());
		int proteoformNumber = proteoformList.size();
		int mainProteinNumber = super.getNumberFastas();
		return mainProteinNumber + proteoformNumber;
	}
}
