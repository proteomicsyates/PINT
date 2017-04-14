package edu.scripps.yates.annotations.uniprot;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.index.UniprotXmlIndex;
import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.annotations.uniprot.xml.ObjectFactory;
import edu.scripps.yates.annotations.uniprot.xml.Uniprot;
import edu.scripps.yates.utilities.fasta.FastaParser;

public class UniprotProteinLocalRetriever {
	private static final Logger log = Logger.getLogger(UniprotProteinLocalRetriever.class);
	public static final String UNIPROT_RELEASES_DATES_FILE_NAME = "uniprot_releases_dates.txt";

	private static String defaultUniprotVersion;
	private final File uniprotReleasesFolder;
	private Collection<String> missingAccessions = new HashSet<String>();
	private final boolean useIndex;
	private final static Map<File, UniprotXmlIndex> loadedIndexes = new HashMap<File, UniprotXmlIndex>();
	private static JAXBContext jaxbContext;
	private boolean cacheEnabled = true;
	private final Map<String, Entry> cache = new HashMap<String, Entry>();
	// private static final String PINT_DEVELOPER_ENV_VAR = "PINT_DEVELOPER";

	/**
	 *
	 * @param uniprotReleasesFolder
	 *            if null, this will call to the
	 *            {@link UniprotProteinRemoteRetriever}
	 * @param useIndex
	 */
	public UniprotProteinLocalRetriever(File uniprotReleasesFolder, boolean useIndex) {
		this.uniprotReleasesFolder = uniprotReleasesFolder;
		this.useIndex = useIndex;
		if (jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance(Uniprot.class);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * returns the accessions from accessionsToQuery not present in the
	 * proteinsRetrieved
	 *
	 * @param accessionsToQuery
	 * @param proteinsRetrieved
	 * @return
	 */
	public static Set<String> getMissingAccessions(Collection<String> accessionsToQuery,
			Map<String, ?> proteinsRetrieved) {

		Set<String> missingAccessions = new HashSet<String>();
		if (proteinsRetrieved.isEmpty()) {
			for (String accession : accessionsToQuery) {
				// changed in Dec2016
				// String nonIsoFormAcc = getNoIsoformAccession(accession);
				// missingAccessions.add(nonIsoFormAcc);
				missingAccessions.add(accession);
			}
		} else {

			for (String accession : accessionsToQuery) {

				// String nonIsoFormAcc = getNoIsoformAccession(accession);
				// if (!proteinsRetrieved.containsKey(nonIsoFormAcc)) {
				// missingAccessions.add(nonIsoFormAcc);
				// }
				if (!proteinsRetrieved.containsKey(accession)) {
					// check first if it is an accession like P12345-1
					String acc = accession;
					if ("1".equals(FastaParser.getIsoformVersion(accession))) {
						acc = FastaParser.getNoIsoformAccession(accession);
						if (!proteinsRetrieved.containsKey(acc)) {
							missingAccessions.add(accession);
						}
					} else {

						missingAccessions.add(accession);
					}
				}
			}
		}
		return missingAccessions;
	}

	private List<Entry> parseXmlFile(File xmlFile) {
		log.debug("Parsing file: " + xmlFile.getName());
		// Map<String, Protein> ret = new HashMap<String, Protein>();
		try {

			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			Uniprot uniprot = (Uniprot) unmarshaller.unmarshal(xmlFile);
			// ret = convertUniprotEntries2Proteins(uniprot);
			return uniprot.getEntry();
		} catch (JAXBException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		}

		// return ret;
		return Collections.EMPTY_LIST;
	}

	private File getUniprotAnnotationsFolder(String uniprotVersion) {
		if (uniprotReleasesFolder != null) {
			final String uniprotReleasesPath = uniprotReleasesFolder.getAbsolutePath();
			final String totalFolderPath = uniprotReleasesPath + File.separator + uniprotVersion;// +
																									// File.separator
																									// +
																									// idProject;
			return new File(totalFolderPath);
		} else {
			return null;
		}
	}

	private File getUniprotAnnotationsFile(String uniprotVersion) {
		if (uniprotReleasesFolder != null) {
			return new File(
					getUniprotAnnotationsFolder(uniprotVersion).getAbsolutePath() + File.separator + "uniprot.xml");
		} else {
			return null;
		}
	}

	/**
	 * Gets the file in which the uniprot releases dates will be stored
	 *
	 * @param projectFilesPath2
	 * @return
	 */
	public File getUniprotReleasesDatesFile() {
		return new File(uniprotReleasesFolder.getAbsolutePath() + File.separator + UNIPROT_RELEASES_DATES_FILE_NAME);

	}

	public Set<String> getUniprotVersionsForProjects(Map<String, Date> uploadedDateByProjectTags) {
		Set<String> ret = new HashSet<String>();
		if (uniprotReleasesFolder != null) {
			File uniprotReleasesFile = getUniprotReleasesDatesFile();
			if (uniprotReleasesFile.exists()) {
				Map<Object, Object> mapLines;
				try {
					mapLines = Files.lines(Paths.get(uniprotReleasesFile.toURI())).collect(Collectors
							.toMap(line -> line.toString().split("\t")[1], line -> line.toString().split("\t")[0]));

					for (String projectTag : uploadedDateByProjectTags.keySet()) {
						Date uploadDate = uploadedDateByProjectTags.get(projectTag);

						for (Object key : mapLines.keySet()) {
							DateFormat df = new SimpleDateFormat("yyyy MM dd");
							Date uniprotReleaseDate;
							try {
								uniprotReleaseDate = df.parse(key.toString());
								if (uploadDate.before(uniprotReleaseDate) || uploadDate.equals(uniprotReleaseDate)) {
									ret.add(mapLines.get(key).toString());
								}
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	private String getAvailableFileName(File projectFolder) {
		final String baseFileName = "uniprot";
		final String extension = ".xml";
		File file = new File(projectFolder + File.separator + baseFileName + extension);
		if (!file.exists()) {
			return baseFileName + extension;
		} else {
			int num = 1;
			file = new File(projectFolder + File.separator + baseFileName + "_" + num + extension);
			while (file.exists()) {
				num++;
				file = new File(projectFolder + File.separator + baseFileName + "_" + num + extension);
			}
			return baseFileName + "_" + num + extension;
		}
	}

	// protected static Map<Accession, Set<ProteinAnnotation>>
	// getProteinAnnotationsFromProteins(
	// Map<Accession, Protein> queryProteins) {
	// Map<Accession, Set<ProteinAnnotation>> ret = new HashMap<Accession,
	// Set<ProteinAnnotation>>();
	//
	// if (queryProteins != null) {
	// for (Accession proteinACC : queryProteins.keySet()) {
	// final Protein protein = queryProteins.get(proteinACC);
	// ret.put(proteinACC, protein.getAnnotations());
	// }
	// }
	//
	// return ret;
	// }

	protected File saveUniprotToLocalFilesystem(Uniprot uniprot, String uniprotVersion, boolean useIndex)
			throws JAXBException {
		if (uniprotReleasesFolder == null)
			return null;
		if (useIndex) {
			// using index
			final File projectFolder = getUniprotAnnotationsFolder(uniprotVersion);
			final boolean created = projectFolder.mkdirs();
			if (created)
				log.info(projectFolder.getAbsoluteFile() + " created in the local file system");
			File file = getUniprotAnnotationsFile(uniprotVersion);
			try {
				UniprotXmlIndex uniprotIndex = null;
				if (loadedIndexes.containsKey(file)) {
					uniprotIndex = loadedIndexes.get(file);
				} else {
					uniprotIndex = new UniprotXmlIndex(file);
					loadedIndexes.put(file, uniprotIndex);
				}
				final List<Entry> entries = uniprot.getEntry();
				for (Entry entry : entries) {
					uniprotIndex.addItem(entry);
				}
				if (entries.size() > 1) {
					log.info(entries.size() + " entries added to index of uniprot version " + uniprotVersion);
				} else {
					log.info(entries.get(0).getAccession().get(0) + " entry added to index of uniprot version "
							+ uniprotVersion);
				}
				return uniprotIndex.getIndexedFile();
			} catch (IOException e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}
		}

		// if not using index, or if some error happened

		// just add if the total entries have been loaded
		// if (!entries.isEmpty()) {
		// for (Entry entry : uniprot.getEntry()) {
		// entries.add(entry);
		// }
		// }

		final File projectFolder = getUniprotAnnotationsFolder(uniprotVersion);
		final boolean created = projectFolder.mkdirs();
		if (created)
			log.info(projectFolder.getAbsoluteFile() + " created in the local file system");
		String fileName = getAvailableFileName(projectFolder);
		File filetoCreate = new File(projectFolder + File.separator + fileName);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
		marshaller.marshal(uniprot, filetoCreate);
		log.info("File created succesfully: " + filetoCreate.getAbsolutePath());
		return filetoCreate;

	}

	public Collection<String> getMissingAccessions() {
		return missingAccessions;
	}

	public Map<String, Entry> getAnnotatedProtein(String uniprotVersion, String accession) {
		Set<String> accessions = new HashSet<String>();
		accessions.add(accession);
		return getAnnotatedProteins(uniprotVersion, accessions);
	}

	public synchronized Map<String, Entry> getAnnotatedProteins(String uniprotVersion, Collection<String> accessions) {
		return getAnnotatedProteins(uniprotVersion, accessions, true);
	}

	public synchronized Map<String, Entry> getAnnotatedProteins(String uniprotVersion, Collection<String> accessions,
			boolean retrieveFastaIsoforms) {
		Set<String> accsToSearch = new HashSet<String>();
		Set<String> mainIsoforms = new HashSet<String>();
		for (String acc : accessions) {
			// only search for the uniprot ones
			if (!FastaParser.isContaminant(acc) && !FastaParser.isReverse(acc)
					&& FastaParser.getUniProtACC(acc) != null) {
				if ("1".equals(FastaParser.getIsoformVersion(acc))) {
					String noIsoAcc = FastaParser.getNoIsoformAccession(acc);
					if (noIsoAcc != null) {
						log.debug("Registered main isoform accession: " + acc);
						accsToSearch.add(noIsoAcc);
						mainIsoforms.add(acc);
					}
				} else {
					accsToSearch.add(acc);
				}
			}
		}

		List<Entry> entries = new ArrayList<Entry>();
		if (cacheEnabled && !cache.isEmpty()) {
			int foundInCache = 0;
			// look into cache if enabled
			Iterator<String> iterator = accsToSearch.iterator();
			while (iterator.hasNext()) {
				String acc = iterator.next();
				Entry cachedEntry = cache.get(acc);
				if (cachedEntry != null) {
					entries.add(cachedEntry);
					iterator.remove();
					foundInCache++;
				}
			}

			log.debug(foundInCache + " entries found in cache");

		}
		if (uniprotVersion == null || "".equals(uniprotVersion)) {

			defaultUniprotVersion = UniprotProteinRemoteRetriever.getCurrentUniprotRemoteVersion();
			// if was not possible to get the uniprot version through Internet,
			// take the latest local folder
			if ("".equals(defaultUniprotVersion)) {
				defaultUniprotVersion = getLatestUniprotVersionFolderName();
			}
			uniprotVersion = defaultUniprotVersion;
		}
		if (uniprotVersion == null) {
			uniprotVersion = new Date().toString();
		}
		UniprotXmlIndex uniprotIndex = null;
		Set<String> missingProteinsAccs = new HashSet<String>();
		File projectFolder = getUniprotAnnotationsFolder(uniprotVersion);
		if (projectFolder != null && projectFolder.exists() && projectFolder.isDirectory()) {

			if (useIndex) {
				File uniprotXmlFile = getUniprotAnnotationsFile(uniprotVersion);
				try {

					if (loadedIndexes.containsKey(uniprotXmlFile)) {
						uniprotIndex = loadedIndexes.get(uniprotXmlFile);
					} else {
						uniprotIndex = new UniprotXmlIndex(uniprotXmlFile);
						loadedIndexes.put(uniprotXmlFile, uniprotIndex);
					}
					int numEntriesRetrievedFromIndex = 0;

					for (String acc : accsToSearch) {

						final Entry item = uniprotIndex.getItem(acc);
						if (item != null) {
							if (cacheEnabled) {
								addEntryToMap(cache, item);
							}
							entries.add(item);
							numEntriesRetrievedFromIndex++;
						}
					}
					if (accsToSearch.size() > 0) {
						log.debug(numEntriesRetrievedFromIndex + " entries retrieved from index");
					}

				} catch (IOException e) {
					e.printStackTrace();
					log.error(e.getMessage());
				} catch (JAXBException e) {
					e.printStackTrace();
					log.error(e.getMessage());
				}

			} else {
				final String[] xmlfilesNames = projectFolder.list(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						if (name.toLowerCase().endsWith(".xml"))
							return true;
						return false;
					}
				});
				missingProteinsAccs.addAll(accsToSearch);// noIsoformAccs);
				if (entries.isEmpty()) {
					if (xmlfilesNames.length > 1) {
						try {
							log.info("Reading " + xmlfilesNames.length + " XML files in "
									+ projectFolder.getAbsolutePath());
							final List<Entry> mergedEntries = mergeXMLFiles(xmlfilesNames, projectFolder,
									uniprotVersion);
							if (mergedEntries != null) {
								entries.addAll(mergedEntries);
							}
						} catch (JAXBException e) {
							e.printStackTrace();
							log.warn("Error trying to unify the uniprot xml files");
						}
					} else {
						log.info("Reading unified XML file in " + projectFolder.getAbsolutePath());
						entries.addAll(parseXmlFile(
								new File(projectFolder.getAbsolutePath() + File.separator + xmlfilesNames[0])));
					}
				}
			}
			HashMap<String, Entry> queryProteinsMap = new HashMap<String, Entry>();
			addEntriesToMap(queryProteinsMap, entries);// noIsoformAccs,
														// queryProteinsMap,
														// entries);

			// look for some protein missing in the local system
			missingProteinsAccs = getMissingAccessions(accsToSearch, queryProteinsMap);// noIsoformAccs,
																						// queryProteinsMap);

			if (!missingProteinsAccs.isEmpty()) {
				try {
					UniprotProteinRemoteRetriever uprr = new UniprotProteinRemoteRetriever(uniprotReleasesFolder,
							useIndex);
					uprr.setLookForIsoforms(retrieveFastaIsoforms);
					log.debug("Trying to retrieve  " + missingProteinsAccs.size()
							+ " proteins that were not present in the local system");
					Map<String, Entry> annotatedProteins = uprr.getAnnotatedProteins(uniprotVersion,
							missingProteinsAccs, uniprotIndex);
					if (cacheEnabled) {
						cache.putAll(annotatedProteins);
					}
					queryProteinsMap.putAll(annotatedProteins);
					missingAccessions = uprr.getMissingAccessions();
				} catch (IllegalArgumentException e) {
					log.warn(e.getMessage());
					log.warn("It is not possible to get remote information using the argument uniprot version: "
							+ uniprotVersion);
					missingAccessions = missingProteinsAccs;
				}
			}

			// final Map<String, Entry> converToOriginalAccessions =
			// converToOriginalAccessions(queryProteinsMap,
			// isoformMap);
			// return converToOriginalAccessions;

			// map main isoforms to the corresponding no isoform entries,
			// that is an entry like P12345-1 to P12345
			for (String mainIsoform : mainIsoforms) {
				final Entry entry = queryProteinsMap.get(FastaParser.getNoIsoformAccession(mainIsoform));
				if (entry != null) {
					log.debug("Mapping back " + mainIsoform + " to entry " + entry.getAccession().get(0));
					queryProteinsMap.put(mainIsoform, entry);
					if (cacheEnabled) {
						cache.put(mainIsoform, entry);
					}
				}
			}

			if (queryProteinsMap.size() > 1) {
				log.debug("Returning " + queryProteinsMap.size() + " / " + accessions.size() + " proteins");
			}
			return queryProteinsMap;
		} else {
			try {
				log.info("Local information (local index folder) not found");
				log.info("Trying to get it remotely from Uniprot repository");
				UniprotProteinRemoteRetriever uprr = new UniprotProteinRemoteRetriever(uniprotReleasesFolder, useIndex);
				final Map<String, Entry> queryProteinsMap = uprr.getAnnotatedProteins(uniprotVersion, accsToSearch,
						uniprotIndex);
				// map main isoforms to the corresponding no isoform entries,
				// that is an entry like P12345-1 to P12345
				for (String mainIsoform : mainIsoforms) {
					final Entry entry = queryProteinsMap.get(FastaParser.getNoIsoformAccession(mainIsoform));
					if (entry != null) {
						if (cacheEnabled) {
							cache.put(mainIsoform, entry);
						}
						log.info("Mapping back " + mainIsoform + " to entry " + entry.getAccession().get(0));
						queryProteinsMap.put(mainIsoform, entry);
					}
				}
				return queryProteinsMap;
			} catch (IllegalArgumentException e) {
				log.info(e.getMessage());
				log.info("It is not possible to get information using the argument uniprot version: " + uniprotVersion);
				return new HashMap<String, Entry>();
			}
		}
	}

	private Map<String, Entry> converToOriginalAccessions(HashMap<String, Entry> queryProteins,
			Map<String, String> isoformMap) {
		Map<String, Entry> ret = new HashMap<String, Entry>();
		for (String nonIsoformAcc : queryProteins.keySet()) {
			if (isoformMap.containsKey(nonIsoformAcc)) {
				ret.put(isoformMap.get(nonIsoformAcc), queryProteins.get(nonIsoformAcc));
			} else {
				log.warn("CUIDADO");
			}
		}
		return ret;
	}

	public String getLatestUniprotVersionFolderName() {
		if (uniprotReleasesFolder != null) {
			String regexp = "(\\d+)_(\\d+)";
			Pattern pattern = Pattern.compile(regexp);
			final String uniprotReleasesPath = uniprotReleasesFolder.getAbsolutePath();
			File folder = new File(uniprotReleasesPath);
			if (folder.exists()) {
				final File[] listFiles = folder.listFiles();
				int year = 0;
				int month = 0;
				Date releaseDate = null;
				for (File file : listFiles) {
					if (file.isDirectory()) {
						final String folderName = FilenameUtils.getName(file.getAbsolutePath());

						final Matcher matcher = pattern.matcher(folderName);
						if (matcher.find()) {
							year = Integer.valueOf(matcher.group(1));
							month = Integer.valueOf(matcher.group(2));
							Date releaseDateTmp = new Date(year, month, 1);
							if (releaseDate == null || releaseDate.compareTo(releaseDateTmp) == -1) {
								releaseDate = releaseDateTmp;
							}
						}
					}
				}
				if (releaseDate != null) {
					DecimalFormat twoDigits = new DecimalFormat("00");
					String monthString = twoDigits.format(month);
					DecimalFormat fourDigits = new DecimalFormat("0000");
					String yearString = fourDigits.format(year);
					final String ret = yearString + "_" + monthString;
					return ret;
				}
			}
		}
		return "";
	}

	/**
	 * Merge all the uniprot files into a single one. It will facilitate its
	 * posterior reading.
	 *
	 * @param xmlfilesNames
	 * @param projectFolder
	 * @param uniprotVersion
	 * @return
	 * @throws JAXBException
	 */
	private List<Entry> mergeXMLFiles(String[] xmlfilesNames, File projectFolder, String uniprotVersion)
			throws JAXBException {
		log.info("Merging " + xmlfilesNames.length + " xml files into one single file");
		Uniprot uniprot = new ObjectFactory().createUniprot();
		for (String xmlFileName : xmlfilesNames) {
			final List<Entry> entries = parseXmlFile(
					new File(projectFolder.getAbsolutePath() + File.separator + xmlFileName));
			if (entries != null) {
				log.info("Adding " + entries.size() + " entries to the list of entries");
				uniprot.getEntry().addAll(entries);
				log.info("Now having " + uniprot.getEntry().size() + " entries");
			}
		}

		// remove all the rest of the files
		for (String xmlFileName : xmlfilesNames) {
			final File file2 = new File(projectFolder.getAbsolutePath() + File.separator + xmlFileName);
			log.info("Deleting " + file2.getAbsolutePath() + "...");
			final boolean deleted = file2.delete();
			log.info(file2.getAbsolutePath() + " deleted = " + deleted);
		}

		File file = saveUniprotToLocalFilesystem(uniprot, uniprotVersion, useIndex);

		return uniprot.getEntry();
	}

	protected static void addEntriesToMap(Map<String, Entry> map, List<Entry> entries) {
		for (Entry entry : entries) {
			addEntryToMap(map, entry);
		}

	}

	protected static void addEntryToMap(Map<String, Entry> map, Entry entry) {

		final List<String> accessions = entry.getAccession();
		for (String accession : accessions) {
			// String nonIsoFormAcc = getNoIsoformAccession(accession);
			// if (accessionsToQuery.contains(nonIsoFormAcc))
			map.put(accession, entry);
		}

	}

	public boolean isCacheEnabled() {
		return cacheEnabled;
	}

	public void setCacheEnabled(boolean cacheEnabled) {
		this.cacheEnabled = cacheEnabled;
	}

}
