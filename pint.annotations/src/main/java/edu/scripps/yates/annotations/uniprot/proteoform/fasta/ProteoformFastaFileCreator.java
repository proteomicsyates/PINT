package edu.scripps.yates.annotations.uniprot.proteoform.fasta;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;

import com.compomics.dbtoolkit.io.implementations.FASTADBLoader;
import com.compomics.util.protein.Protein;

import edu.scripps.yates.annotations.uniprot.UniprotProteinLocalRetriever;
import edu.scripps.yates.annotations.uniprot.proteoform.xml.UniprotProteoformRetrieverFromXML;
import edu.scripps.yates.utilities.fasta.Fasta;
import edu.scripps.yates.utilities.fasta.FastaParser;
import gnu.trove.set.hash.THashSet;

public class ProteoformFastaFileCreator {

	private static File uniprotReleasesFolder = new File("Z:\\share\\Salva\\data\\uniprotKB");
	private static String uniprotVersion = null;;

	public static void main(String[] args) {
		FileWriter fw = null;
		try {
			final File fastaFile = new File(args[0]);
			final UniprotProteinLocalRetriever uplr = new UniprotProteinLocalRetriever(uniprotReleasesFolder, true);
			// read first the fasta and get annotations at once
			final FASTADBLoader loader = new FASTADBLoader();
			if (!loader.canReadFile(fastaFile)) {
				throw new IllegalAccessError("Cannot read fasta file " + fastaFile.getAbsolutePath());
			}
			int decoy = 0;
			int total = 0;
			loader.load(fastaFile.getAbsolutePath());
			final Set<String> accs = new THashSet<String>();
			Protein nextProtein = null;
			while ((nextProtein = loader.nextProtein()) != null) {
				total++;
				final String accession = nextProtein.getHeader().getAccession();
				if (FastaParser.isReverse(accession)) {
					decoy++;
					continue;
				}
				final String acc = FastaParser.getUniProtACC(accession);
				if (acc != null) {
					accs.add(acc);
				} else {
					System.err.println(
							"Accession " + accession + " not recognized as Uniprot. Skipping proteoforms for it.");
				}
			}
			System.out.println(accs.size() + "/" + total + " proteins recognized in Fasta file");
			if (decoy > 0) {
				System.out.println(decoy + " decoy proteis ignored");
			}
			uplr.getAnnotatedProteins(uniprotVersion, accs);
			final ProteoFormFastaReader reader = new ProteoFormFastaReader(fastaFile.getAbsolutePath(),
					new UniprotProteoformRetrieverFromXML(uplr, uniprotVersion));
			fw = new FileWriter(new File(fastaFile.getParentFile().getAbsoluteFile() + File.separator
					+ FilenameUtils.getBaseName(fastaFile.getAbsolutePath()) + "_proteoforms.fasta"));
			final Iterator<Fasta> fastaIterator = reader.getFastas();
			while (fastaIterator.hasNext()) {
				final Fasta fasta = fastaIterator.next();
				if (FastaParser.isReverse(fasta.getAccession())) {
					// do not write reverses
					continue;
				}
				fw.write(fasta.getOriginalDefline() + "\n");
				fw.write(fasta.getSequence() + "\n");
			}
			System.out.println("Done");
			System.exit(0);
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(-1);
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
