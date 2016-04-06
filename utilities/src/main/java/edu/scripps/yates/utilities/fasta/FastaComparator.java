package edu.scripps.yates.utilities.fasta;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.compomics.dbtoolkit.io.EnzymeLoader;
import com.compomics.dbtoolkit.io.implementations.FASTADBLoader;
import com.compomics.dbtoolkit.io.implementations.FASTAHeaderFilter;
import com.compomics.util.protein.Enzyme;
import com.compomics.util.protein.Protein;

import edu.scripps.yates.utilities.venndata.VennData;

public class FastaComparator {
	private final static Logger log = Logger.getLogger(FastaComparator.class);
	private final int minPeptideLength = 6;
	private final int maxPeptideLength = Integer.MAX_VALUE;
	private final String DECOY_PREFIX = "Reverse";

	private final HashMap<String, Set<String>> map;
	private final NumberFormat nf = NumberFormat.getPercentInstance();
	private final Enzyme enzyme;
	private final List<File> files;
	private final File imageFileoutput;
	private VennData vennData;

	// public static void main(String[] args) throws IOException {
	// File fastaFile = new File(
	// "C:\\Users\\Salva\\Desktop\\data\\isotopologues\\databases\\NCBI_RefSeq_Melanogaster__07-01-2014_reversed.fasta");
	//
	// String[] species = { "Drosophila melanogaster", "Drosophila virilis" };
	// FastaComparator fastaComparator = new FastaComparator(fastaFile,
	// species);
	// fastaComparator.analyzeIntersections();
	// }

	public static void main(String[] args) throws IOException {
		File fastaFile = new File(
				"C:\\Users\\Salva\\Desktop\\data\\isotopologues\\databases\\D_simulans_canonical_and_isoform.fasta");
		File fastaFile2 = new File(
				"C:\\Users\\Salva\\Desktop\\data\\isotopologues\\databases\\NCBI_RefSeq_Melanogaster__07-01-2014_reversed.fasta");
		File venndiagramFile = new File(
				"C:\\Users\\Salva\\Desktop\\data\\isotopologues\\databases\\venn.png");
		List<File> files = new ArrayList<File>();
		files.add(fastaFile);
		files.add(fastaFile2);
		String[] species = { "Drosophila virilis", "Drosophila melanogaster",
				"Drosophila simulans" };
		FastaComparator fastaComparator = new FastaComparator(files, species,
				venndiagramFile, "Lys-C");
		fastaComparator.analyzeIntersections();
		fastaComparator.analyzeIntersectionsUsingVennData();
		URL url = fastaComparator.getVennDiagramURL();
		System.out.println(url);
	}

	public FastaComparator(List<File> files, String[] species,
			File imageFileOutput, String enzymeName) throws IOException {
		this.files = files;
		imageFileoutput = imageFileOutput;

		enzyme = EnzymeLoader.loadEnzyme(enzymeName, null);
		enzyme.setMiscleavages(0);

		map = new HashMap<String, Set<String>>();

		for (String specie : species) {
			Set<String> set = new HashSet<String>();
			int numProteinsFromSpecie = 0;
			for (File file : files) {
				FASTADBLoader loader = new FASTADBLoader();
				loader.load(file.getAbsolutePath());

				FASTAHeaderFilter filefilter = new FASTAHeaderFilter(specie);
				Protein entry = loader.nextFilteredProtein(filefilter);
				while (entry != null) {
					if (!entry.getHeader().getFullHeaderWithAddenda()
							.contains(DECOY_PREFIX)) {
						numProteinsFromSpecie++;
						final Protein[] peptides = enzyme.cleave(entry,
								minPeptideLength, maxPeptideLength);
						for (Protein peptide : peptides) {
							final String sequence = peptide.getSequence()
									.getSequence();
							set.add(sequence);
						}
					}
					entry = loader.nextFilteredProtein(filefilter);
				}
			}
			System.out.println(set.size() + " Different peptides for specie: "
					+ specie + " in " + numProteinsFromSpecie + " proteins");
			map.put(specie, set);
		}
	}

	public void analyzeIntersections() {
		if (map.size() == 2) {
			final Set<String> keySet = map.keySet();
			List<String> species = new ArrayList<String>();
			species.addAll(keySet);
			Collections.sort(species);
			Set<String> col1 = map.get(species.get(0));
			Set<String> col2 = map.get(species.get(1));
			Set<String> justIn1 = new HashSet<String>();
			Set<String> justIn2 = new HashSet<String>();
			Set<String> intersection = new HashSet<String>();
			Set<String> union = new HashSet<String>();

			for (String seq1 : col1) {
				if (col2.contains(seq1)) {
					intersection.add(seq1);
				} else {
					justIn1.add(seq1);
				}
				union.add(seq1);
			}
			for (String seq2 : col2) {
				if (col1.contains(seq2)) {
					intersection.add(seq2);
				} else {
					justIn2.add(seq2);
				}
				union.add(seq2);
			}
			System.out.println("-------------------------------");
			System.out.println("Analysis of " + getSpeciesNames(species));
			System.out.println("Using files: " + getFileNames(files));
			System.out.println("-------------------------------");
			System.out.println("Decoys skipped with prefix: " + DECOY_PREFIX);
			System.out.println("-------------------------------");

			System.out.println("Enzyme used: " + enzyme);
			System.out.println("-------------------------------");
			System.out.println("Peptides exclusive from " + species.get(0)
					+ ": " + justIn1.size() + "("
					+ nf.format(Double.valueOf(justIn1.size()) / union.size())
					+ ")");
			System.out.println("Peptides exclusive from " + species.get(1)
					+ ": " + justIn2.size() + "("
					+ nf.format(Double.valueOf(justIn2.size()) / union.size())
					+ ")");
			System.out.println("Peptides present in both species: "
					+ intersection.size()
					+ "("
					+ nf.format(Double.valueOf(intersection.size())
							/ union.size()) + ")");
			System.out.println("Union: " + union.size());
			System.out.println("-------------------------------");

		}

	}

	public void analyzeIntersectionsUsingVennData() throws IOException {
		if (map.size() <= 3) {

			final Set<String> keySet = map.keySet();
			List<String> species = new ArrayList<String>();
			species.addAll(keySet);
			Collections.sort(species);
			Set<String> col1 = null;
			Set<String> col2 = null;
			Set<String> col3 = null;
			String name1 = null;
			String name2 = null;
			String name3 = null;

			col1 = map.get(species.get(0));
			name1 = species.get(0);
			if (species.size() > 1) {
				col2 = map.get(species.get(1));
				name2 = species.get(1);
			}
			if (species.size() > 2) {
				col3 = map.get(species.get(2));
				name3 = species.get(2);
			}

			vennData = new VennData(getSpeciesNames(species), name1, col1,
					name2, col2, name3, col3);
			System.out.println("-------------------------------");
			System.out.println("Analysis of " + getSpeciesNames(species));
			System.out.println("Using files: " + getFileNames(files));
			System.out.println("-------------------------------");
			System.out.println("Decoys skipped with prefix: " + DECOY_PREFIX);
			System.out.println("-------------------------------");

			System.out.println("Enzyme used: " + enzyme);
			System.out.println("-------------------------------");
			System.out.println(vennData
					.getIntersectionsText(getSpeciesNames(species)));
			System.out.println("-------------------------------");

		}

	}

	public void generateVennDiagram() throws IOException {
		vennData.saveVennDiagramToFile(imageFileoutput);
	}

	public URL getVennDiagramURL() throws IOException {
		return vennData.getImageURL();
	}

	private String getFileNames(List<File> files2) {
		StringBuilder sb = new StringBuilder();
		for (File file : files2) {
			if (!"".equals(sb.toString()))
				sb.append(", ");
			sb.append(file.getName());
		}
		return sb.toString();
	}

	private String getSpeciesNames(List<String> species) {
		StringBuilder sb = new StringBuilder();
		for (String specie : species) {
			if (!"".equals(sb.toString()))
				sb.append(" vs ");
			sb.append(specie);
		}
		return sb.toString();
	}
}
