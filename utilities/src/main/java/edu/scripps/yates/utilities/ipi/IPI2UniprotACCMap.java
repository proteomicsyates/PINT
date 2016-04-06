package edu.scripps.yates.utilities.ipi;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import edu.scripps.yates.utilities.ipi.UniprotEntry.UNIPROT_TYPE;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.model.factories.AccessionEx;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.utils.ModelUtils;
import edu.scripps.yates.utilities.util.Pair;

public class IPI2UniprotACCMap {
	private static final String regularMappingFileName = "uniprot2IPI.tsv";
	private static final String ipi_3_23_MappingFileName = "ipi3.23_To_Uniprot_mapping.txt";

	private static final HashMap<String, List<UniprotEntry>> ipi2Uniprot = new HashMap<String, List<UniprotEntry>>();
	private static final HashMap<UniprotEntry, List<String>> uniprotEntry2IPI = new HashMap<UniprotEntry, List<String>>();
	private static final HashMap<String, UniprotEntry> uniprotEntryMap = new HashMap<String, UniprotEntry>();
	private static Logger log = Logger.getLogger(IPI2UniprotACCMap.class);
	private static IPI2UniprotACCMap instance;

	private static enum MAP_TYPE {
		REGULAR, IPI_3_23
	};

	private IPI2UniprotACCMap() {

	}

	public static IPI2UniprotACCMap getInstance() {
		if (instance == null) {
			instance = new IPI2UniprotACCMap();
		}
		return instance;
	}

	public List<String> map2IPI(String uniprotACC) {

		if (uniprotEntry2IPI.isEmpty())
			loadMap();
		if (uniprotEntryMap.containsKey(uniprotACC)) {
			final UniprotEntry uniprotEntry = uniprotEntryMap.get(uniprotACC);
			if (uniprotEntry2IPI.containsKey(uniprotEntry))
				return uniprotEntry2IPI.get(uniprotEntry);
		} else {
			// remove the isoform "Q9P2R7-1" to "Q9P2R7"
			if (uniprotACC.matches("\\w+\\-\\w*")) {
				uniprotACC = uniprotACC.substring(0, uniprotACC.indexOf("-"));
				if (uniprotEntryMap.containsKey(uniprotACC)) {
					final UniprotEntry uniprotEntry = uniprotEntryMap.get(uniprotACC);
					if (uniprotEntry2IPI.containsKey(uniprotEntry))
						return uniprotEntry2IPI.get(uniprotEntry);
				}
			}
		}

		return Collections.emptyList();
	}

	public List<String> map2IPI(Protein uniprotProtein) {
		final List<Accession> accessions = ModelUtils.getAccessions(uniprotProtein, AccessionType.UNIPROT);
		List<String> list = new ArrayList<String>();
		if (accessions != null) {
			for (Accession acc : accessions) {
				final List<String> map2ipi = map2IPI(acc.getAccession());
				for (String ipi : map2ipi) {
					if (!list.contains(ipi))
						list.add(ipi);
				}

			}
		}
		return list;
	}

	public List<UniprotEntry> map2Uniprot(String ipiACC) {
		if (ipi2Uniprot.isEmpty())
			loadMap();

		if (ipiACC != null && ipi2Uniprot.containsKey(ipiACC)) {
			return ipi2Uniprot.get(ipiACC);
		}
		// if not found, remove the isoform "IPI123123.2" to "IPI123123"
		if (ipiACC != null && ipiACC.matches("\\w+\\.\\w*")) {
			ipiACC = ipiACC.substring(0, ipiACC.indexOf("."));
			if (ipi2Uniprot.containsKey(ipiACC)) {
				return ipi2Uniprot.get(ipiACC);
			}
		}

		return Collections.emptyList();
	}

	private void loadMap() {
		BufferedReader br = null;
		try {
			log.info("Loading IPI-Uniprot map");
			String fileName = ipi_3_23_MappingFileName;

			InputStream is = new ClassPathResource(fileName).getInputStream();
			br = new BufferedReader(new InputStreamReader(new DataInputStream(is)));
			String line;
			int numLines = 0;
			while ((line = br.readLine()) != null) {
				numLines++;
				final String[] split = line.split("\t");
				String uniprotAcc = split[0];
				// // remove the isoform "Q9P2R7-1" to "Q9P2R7"
				// if (uniprotAcc.matches("\\w+\\-\\w*"))
				// uniprotAcc = uniprotAcc.substring(0,
				// uniprotAcc.indexOf("-"));
				String ipiAcc = split[1];
				String name = null;
				UniprotEntry.UNIPROT_TYPE uniprotType = UNIPROT_TYPE.NA;
				if (split.length > 2) {
					uniprotType = UniprotEntry.UNIPROT_TYPE.valueOf(split[2]);
				}
				if (split.length > 3) {
					name = split[3];
				}
				// if (ipiAcc.contains("."))
				// ipiAcc = ipiAcc.substring(0, ipiAcc.indexOf("."));
				// build the uniprotEntry
				UniprotEntry entry = new UniprotEntry(uniprotAcc, uniprotType, name);
				// entrymap
				uniprotEntryMap.put(uniprotAcc, entry);

				// uniprot 2 IPI
				if (uniprotEntry2IPI.containsKey(entry)) {
					final List<String> list = uniprotEntry2IPI.get(entry);
					if (!list.contains(ipiAcc))
						list.add(ipiAcc);
				} else {
					List<String> list = new ArrayList<String>();
					list.add(ipiAcc);
					uniprotEntry2IPI.put(entry, list);
				}

				// IPI 2 uniprot
				if (ipi2Uniprot.containsKey(ipiAcc)) {
					final List<UniprotEntry> list = ipi2Uniprot.get(ipiAcc);
					if (!list.contains(entry))
						list.add(entry);
				} else {
					List<UniprotEntry> list = new ArrayList<UniprotEntry>();
					list.add(entry);
					ipi2Uniprot.put(ipiAcc, list);
				}
			}
			br.close();
			log.info(ipi2Uniprot.size() + " " + uniprotEntry2IPI.size() + " mappings readed in " + numLines + " lines");
			if (fileName.equals(ipi_3_23_MappingFileName)) {
				// use the other mapping to complement the first one.
				// JUST ADD NEW MAPPINGS FROM IPI TO UNIPROT IF THE IPI WAS NOT
				// ALREADY MAPPED
				is = new ClassPathResource(regularMappingFileName).getInputStream();
				br = new BufferedReader(new InputStreamReader(new DataInputStream(is)));
				numLines = 0;
				while ((line = br.readLine()) != null) {
					numLines++;
					final String[] split = line.split("\t");
					String uniprotAcc = split[0];
					// remove the isoform "Q9P2R7-1" to "Q9P2R7"
					// if (uniprotAcc.matches("\\w+\\-\\w*"))
					// uniprotAcc = uniprotAcc.substring(0,
					// uniprotAcc.indexOf("-"));
					String ipiAcc = split[1];
					String name = null;
					UniprotEntry.UNIPROT_TYPE uniprotType = UNIPROT_TYPE.NA;
					if (split.length > 2) {
						uniprotType = UniprotEntry.UNIPROT_TYPE.valueOf(split[2]);
					}
					if (split.length > 3) {
						name = split[3];
					}
					// remove the isoform IPI00114389.4 to IPI00114389
					// if (ipiAcc.contains("."))
					// ipiAcc = ipiAcc.substring(0, ipiAcc.indexOf("."));

					// build the uniprotEntry
					UniprotEntry entry = new UniprotEntry(uniprotAcc, uniprotType, name);
					// entrymap
					uniprotEntryMap.put(uniprotAcc, entry);

					// uniprot 2 IPI
					if (uniprotEntry2IPI.containsKey(entry)) {
						final List<String> list = uniprotEntry2IPI.get(entry);
						if (!list.contains(ipiAcc)) {
							list.add(ipiAcc);
						}
					} else {
						List<String> list = new ArrayList<String>();
						list.add(ipiAcc);
						uniprotEntry2IPI.put(entry, list);
					}

					// IPI 2 uniprot
					if (!ipi2Uniprot.containsKey(ipiAcc)) {
						List<UniprotEntry> list = new ArrayList<UniprotEntry>();
						list.add(entry);
						ipi2Uniprot.put(ipiAcc, list);
					} else {
						if (!ipi2Uniprot.get(ipiAcc).contains(entry)) {
							ipi2Uniprot.get(ipiAcc).add(entry);
						} else {
							log.debug("Mapping already found. Ignoring " + ipiAcc);
						}
					}
				}

				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Pair<Accession, Set<Accession>> getPrimaryAndSecondaryAccessionsFromIPI(Accession primaryAccession) {
		Set<Accession> secondaryAccessions = new HashSet<Accession>();
		AccessionEx newPrimaryAcc = null;
		final List<UniprotEntry> map2Uniprot = IPI2UniprotACCMap.getInstance()
				.map2Uniprot(primaryAccession.getAccession());
		boolean swissprot = false;
		// look for the swissprot one
		for (UniprotEntry uniprotEntry : map2Uniprot) {
			if (uniprotEntry.getType().equals(UNIPROT_TYPE.SWISSPROT)) {
				swissprot = true;
				AccessionEx secondaryAccession = new AccessionEx(primaryAccession.getAccession(), AccessionType.IPI);
				secondaryAccession.setDescription(primaryAccession.getDescription());
				secondaryAccessions.add(secondaryAccession);
				newPrimaryAcc = new AccessionEx(uniprotEntry.getAcc(), AccessionType.UNIPROT);
				newPrimaryAcc.setDescription(uniprotEntry.getName());
			}
		}
		if (!swissprot) {
			// add all as primary, the last one will remain as the
			// primary
			for (UniprotEntry uniprotEntry : map2Uniprot) {
				AccessionEx secondaryAccession = new AccessionEx(primaryAccession.getAccession(),
						primaryAccession.getAccessionType());
				secondaryAccession.setDescription(primaryAccession.getDescription());
				secondaryAccessions.add(secondaryAccession);
				newPrimaryAcc = new AccessionEx(uniprotEntry.getAcc(), AccessionType.UNIPROT);
				newPrimaryAcc.setDescription(uniprotEntry.getName());
			}
		} else {
			// add all that are different to the primary to secondary
			for (UniprotEntry uniprotEntry : map2Uniprot) {
				if (!uniprotEntry.getAcc().equals(primaryAccession.getAccession())) {
					AccessionEx secondaryAccession = new AccessionEx(uniprotEntry.getAcc(), AccessionType.UNIPROT);
					secondaryAccession.setDescription(uniprotEntry.getName());
					secondaryAccessions.add(secondaryAccession);
				}
			}
		}
		Pair<Accession, Set<Accession>> ret = new Pair<Accession, Set<Accession>>(newPrimaryAcc, secondaryAccessions);
		return ret;
	}
}
