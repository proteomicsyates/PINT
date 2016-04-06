package edu.scripps.yates.genes;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import edu.scripps.yates.utilities.strings.StringUtils;

/**
 * This class loads a file comming from HUGO Gene Nomenclature Committee:
 * http://www.genenames.org/cgi-bin/statistics in which a TAB separated file
 * containing at least ten columns is loaded.
 * 
 * @author Salva
 * 
 */
public class GeneReader {
	private static final String defaultGeneFileName = "protein-coding_gene.txt";
	private static GeneReader instance;
	private final String geneFileName;
	private InputStream geneFileInputStream = null;
	private static Logger log = Logger.getLogger(GeneReader.class);
	private final Map<String, GeneInformation> mapBySymbol = new HashMap<String, GeneInformation>();
	private final Map<String, GeneInformation> mapByName = new HashMap<String, GeneInformation>();
	private final List<GeneInformation> list = new ArrayList<GeneInformation>();

	private GeneReader(String geneFileName) {
		this.geneFileName = geneFileName;
		try {
			geneFileInputStream = new ClassPathResource(geneFileName)
					.getInputStream();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e);
		}
	}

	private GeneReader(String geneFileName, InputStream is) {
		this.geneFileName = geneFileName;
		geneFileInputStream = is;
	}

	public static GeneReader getInstance(String geneFileName) {
		if (instance == null || !instance.geneFileName.equals(geneFileName)) {
			instance = new GeneReader(geneFileName);
		}
		return instance;
	}

	public static GeneReader getInstance(File geneFile) {
		if (instance == null
				|| !instance.geneFileName.equals(FilenameUtils.getName(geneFile
						.getAbsolutePath()))) {
			InputStream is;
			try {
				is = new FileInputStream(geneFile);
				instance = new GeneReader(FilenameUtils.getName(geneFile
						.getAbsolutePath()), is);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				log.error(e);
			}
		}
		return instance;
	}

	public static GeneReader getInstance() {
		return getInstance(defaultGeneFileName);
	}

	/**
	 * Loads the data into the maps and list. the keys of the maps will be
	 * stored as lower case, in order to then retrieve them as case insensitive
	 */
	private void loadData() {
		BufferedReader br = null;
		try {
			log.info("Loading genes from " + geneFileName);

			br = new BufferedReader(new InputStreamReader(new DataInputStream(
					geneFileInputStream)));
			String line;
			int numLines = 0;
			while ((line = br.readLine()) != null) {
				numLines++;
				// skip header
				if (numLines == 1)
					continue;
				final String[] split = line.split("\t");
				if (split.length > 0) {
					try {
						GeneInformation gene = new GeneInformation();

						gene.setHgncId(Integer.valueOf(split[0].substring(5)));
						if (split.length > 1)
							gene.setApprovedSymbol(split[1]);
						if (split.length > 2)
							gene.setApprovedName(split[2]);
						if (split.length > 3)
							gene.setStatus(split[3]);
						if (split.length > 4)
							gene.setPreviousSymbols(split[4]);
						if (split.length > 5)
							gene.setPreviousNames(split[5]);
						if (split.length > 6)
							gene.setSynonyms(split[6]);
						if (split.length > 7)
							gene.setChromosome(split[7]);
						if (split.length > 8)
							gene.setAccessionNumbers(split[8]);
						if (split.length > 9)
							gene.setRefSeqIDs(split[9]);
						// add to maps
						mapByName.put(gene.getApprovedName().toLowerCase()
								.trim(), gene);
						mapBySymbol.put(gene.getApprovedSymbol().toLowerCase()
								.trim(), gene);
						// add to list
						list.add(gene);
					} catch (Exception e) {
						log.warn(e);
						// if some gene is not well formed, try the next one
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		} finally {
			log.info(list.size() + " genes loaded from " + geneFileName);
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Gets all genes with a certain approved name. If containing parameter is
	 * true, it will also report the ones which approved name contains the
	 * provided geneName.
	 * 
	 * @param name
	 * @param containing
	 * @return
	 */
	public List<GeneInformation> getGenesByName(String geneName,
			boolean containing) {
		if (list.isEmpty())
			loadData();

		List<GeneInformation> ret = new ArrayList<GeneInformation>();
		if (!containing) {
			if (mapByName.containsKey(geneName.toLowerCase())) {
				ret.add(mapByName.get(geneName.toLowerCase()));
			}
		} else {
			// iterate over all list
			for (String approvedName : mapByName.keySet()) {
				if (StringUtils.compareStrings(approvedName,
						geneName.toLowerCase(), true, true, false)) {
					ret.add(mapByName.get(approvedName));
				}
			}
		}
		return ret;
	}

	/**
	 * Gets all genes with a certain approved symbol. If containing parameter is
	 * true, it will also report the ones which approved symbol contains the
	 * provided geneName.
	 * 
	 * @param name
	 * @param containing
	 * @return
	 */
	public List<GeneInformation> getGenesBySymbol(String geneSymbol,
			boolean containing) {
		if (list.isEmpty())
			loadData();
		List<GeneInformation> ret = new ArrayList<GeneInformation>();
		if (!containing) {
			if (mapBySymbol.containsKey(geneSymbol.toLowerCase())) {
				ret.add(mapBySymbol.get(geneSymbol.toLowerCase()));
			}
		} else {
			// iterate over all list
			for (String approvedSymbol : mapBySymbol.keySet()) {
				if (StringUtils.compareStrings(approvedSymbol,
						geneSymbol.toLowerCase(), true, true, false)) {
					ret.add(mapBySymbol.get(approvedSymbol));
				}
			}
		}
		return ret;
	}
}
