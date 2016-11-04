package edu.scripps.yates.annotations.uniprot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRemoteRetriever;
import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.annotations.uniprot.xml.GeneNameType;
import edu.scripps.yates.annotations.uniprot.xml.GeneType;
import edu.scripps.yates.annotations.uniprot.xml.Uniprot;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;

public class UniprotACCQuery {
	private static Logger log = Logger.getLogger(UniprotACCQuery.class);
	private static Map<String, Uniprot> entriesByQuery = new HashMap<String, Uniprot>();
	private final static Map<String, Set<String>> map = new HashMap<String, Set<String>>();

	/**
	 * Parse the protein description for taxid and gene symbols and use that
	 * information for query in Uniprot
	 *
	 * @param proteinDescription
	 * @return
	 */
	public static List<Protein> map2Uniprot(Accession accession, boolean forcePrimaryGene, File uniprotReleasesFolder,
			boolean useIndex) {

		// in case of not finding any mapping use the protein description
		String taxid = getTaxIdFromProteinDescription(accession.getDescription());
		String geneSymbol = getGeneSymbolFromProteinDescription(accession.getDescription());
		final List<Protein> map2Uniprot = map2Uniprot(taxid, geneSymbol, forcePrimaryGene, uniprotReleasesFolder,
				useIndex);
		// keep it in a file
		for (Protein protein : map2Uniprot) {
			addToMappingFile(protein.getPrimaryAccession().getAccession(), accession.getAccession());
		}
		return map2Uniprot;
	}

	private static void addToMappingFile(String uniprotAcc, String otherAcc) {
		if (map.isEmpty()) {
			loadMap();
		}
		if (map.containsKey(otherAcc))
			return;
		File file = getMappingFile();
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
			out.println(uniprotAcc + "\t" + otherAcc);
			if (map.containsKey(otherAcc)) {
				map.get(otherAcc).add(uniprotAcc);
			} else {
				Set<String> set = new HashSet<String>();
				set.add(uniprotAcc);
				map.put(otherAcc, set);
			}
			out.close();
		} catch (IOException e) {

		} finally {
			if (out != null)
				out.close();
		}

	}

	private static File getMappingFile() {
		return new File(System.getProperty("user.dir") + File.separator + "MAPPING2UNIPROT.tsv");
	}

	private static void loadMap() {
		File file = getMappingFile();
		FileInputStream fstream = null;
		BufferedReader br = null;
		try {
			fstream = new FileInputStream(file);

			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				final String[] split = strLine.split("\t");
				String uniprotAcc = split[0];
				String otherAcc = split[1];
				if (map.containsKey(otherAcc)) {
					map.get(otherAcc).add(uniprotAcc);
				} else {
					Set<String> set = new HashSet<String>();
					set.add(uniprotAcc);
					map.put(otherAcc, set);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * use taxid and gene symbols for query in Uniprot
	 *
	 * @param taxid
	 * @param geneSymbol
	 * @return
	 */
	private static List<Protein> map2Uniprot(String taxid, String geneSymbol, boolean forcePrimaryGene,
			File uniprotReleasesFolder, boolean useIndex) {
		String uniprotqueryURL = getUniprotQueryURL(taxid, geneSymbol);
		if (uniprotqueryURL != null) {
			Uniprot proteins = getUniprotAccsFromQuery(uniprotqueryURL, uniprotReleasesFolder, useIndex);
			List<Protein> list = new ArrayList<Protein>();
			if (proteins != null) {
				final List<Entry> entries = proteins.getEntry();
				for (Entry entry : entries) {
					GeneNameType gene = getGene(entry, geneSymbol);
					if (gene == null) {
						continue;
					}
					if (forcePrimaryGene && !gene.getType().equals("primary")) {
						// skip this one
						continue;
					}

					Protein protein = new ProteinImplFromUniprotEntry(entry);
					list.add(protein);
				}
			}
			// if the list is empty, report all the entries without enforcing to
			// be primary
			if (list.isEmpty() && forcePrimaryGene)
				return map2Uniprot(taxid, geneSymbol, false, uniprotReleasesFolder, useIndex);
			return list;
		}
		return new ArrayList<Protein>();
	}

	private static GeneNameType getGene(Entry entry, String geneSymbol) {
		if (entry != null) {
			final List<GeneType> gene = entry.getGene();
			if (gene != null) {
				for (GeneType geneType : gene) {
					final List<GeneNameType> names = geneType.getName();
					if (names != null) {
						for (GeneNameType geneNameType : names) {
							if (geneNameType.getValue().equals(geneSymbol))
								return geneNameType;
						}
					}
				}
			}
		}
		return null;
	}

	private static Uniprot getUniprotAccsFromQuery(String uniprotqueryURL, File uniprotReleasesFolder,
			boolean useIndex) {
		Uniprot proteins = null;
		if (entriesByQuery.containsKey(uniprotqueryURL)) {
			proteins = entriesByQuery.get(uniprotqueryURL);
		} else {
			UniprotProteinRemoteRetriever uprr = new UniprotProteinRemoteRetriever(uniprotReleasesFolder, useIndex);
			proteins = uprr.getProteins(uniprotqueryURL);
			entriesByQuery.put(uniprotqueryURL, proteins);
		}
		return proteins;
	}

	private static String getUniprotQueryURL(String taxid, String geneSymbol) {
		if (geneSymbol == null)
			return null;
		StringBuilder sb = new StringBuilder();

		sb.append("gene:" + geneSymbol);

		if (taxid != null) {
			if (!"".equals(sb.toString()))
				sb.append(" AND ");
			sb.append("taxonomy:" + taxid);
		}
		sb.append("&sort=score");
		return sb.toString();

	}

	private static String getTaxIdFromProteinDescription(String proteinDescription) {
		if (proteinDescription != null) {
			final String taxIDRegexp = ".*Tax_Id=(\\w+)\\s+.*";
			Pattern pattern = Pattern.compile(taxIDRegexp);
			final Matcher matcher = pattern.matcher(proteinDescription);
			if (matcher.find()) {
				if (matcher.groupCount() == 1) {
					return matcher.group(1);
				}
			}
		}
		return null;
	}

	private static String getGeneSymbolFromProteinDescription(String proteinDescription) {
		if (proteinDescription != null) {
			final String taxIDRegexp = ".*Gene_Symbol=(\\w+)\\s+.*";
			Pattern pattern = Pattern.compile(taxIDRegexp);
			final Matcher matcher = pattern.matcher(proteinDescription);
			if (matcher.find()) {
				if (matcher.groupCount() == 1) {
					return matcher.group(1);
				}
			}
		}
		return null;
	}

}