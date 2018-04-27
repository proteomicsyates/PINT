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
import edu.scripps.yates.annotations.util.UniprotEntryCache;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.memory.MemoryUsageReport;
import edu.scripps.yates.utilities.progresscounter.ProgressCounter;
import edu.scripps.yates.utilities.progresscounter.ProgressPrintingType;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class UniprotProteinLocalRetriever {
	private static final Logger log = Logger.getLogger(UniprotProteinLocalRetriever.class);
	public static final String UNIPROT_RELEASES_DATES_FILE_NAME = "uniprot_releases_dates.txt";

	private static String defaultUniprotVersion;
	private final File uniprotReleasesFolder;
	private Collection<String> missingAccessions = new THashSet<>();
	private final boolean useIndex;
	private final static Map<File, UniprotXmlIndex> loadedIndexes = new THashMap<>();
	private static JAXBContext jaxbContext;
	private static boolean cacheEnabled = true;

	private static final UniprotEntryCache cache = new UniprotEntryCache();
	private boolean retrieveFastaIsoforms = true;
	private final List<String> entryKeys = new ArrayList<>();
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
			} catch (final JAXBException e) {
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

		final Set<String> missingAccessions = new THashSet<>();
		if (proteinsRetrieved.isEmpty()) {
			for (final String accession : accessionsToQuery) {
				// changed in Dec2016
				// String nonIsoFormAcc = getNoIsoformAccession(accession);
				// missingAccessions.add(nonIsoFormAcc);
				missingAccessions.add(accession);
			}
		} else {

			for (final String accession : accessionsToQuery) {

				// String nonIsoFormAcc = getNoIsoformAccession(accession);
				// if (!proteinsRetrieved.containsKey(nonIsoFormAcc)) {
				// missingAccessions.add(nonIsoFormAcc);
				// }
				if (!proteinsRetrieved.containsKey(accession) || (proteinsRetrieved.get(accession) == null)) {
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
		// Map<String, Protein> ret = new THashMap<String, Protein>();
		try {

			final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			final Uniprot uniprot = (Uniprot) unmarshaller.unmarshal(xmlFile);
			// ret = convertUniprotEntries2Proteins(uniprot);
			return uniprot.getEntry();
		} catch (final JAXBException e) {
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
		final Set<String> ret = new THashSet<>();
		if (uniprotReleasesFolder != null) {
			final File uniprotReleasesFile = getUniprotReleasesDatesFile();
			if (uniprotReleasesFile.exists()) {
				Map<Object, Object> mapLines;
				try {
					mapLines = Files.lines(Paths.get(uniprotReleasesFile.toURI())).collect(Collectors
							.toMap(line -> line.toString().split("\t")[1], line -> line.toString().split("\t")[0]));

					for (final String projectTag : uploadedDateByProjectTags.keySet()) {
						final Date uploadDate = uploadedDateByProjectTags.get(projectTag);

						for (final Object key : mapLines.keySet()) {
							final DateFormat df = new SimpleDateFormat("yyyy MM dd");
							Date uniprotReleaseDate;
							try {
								uniprotReleaseDate = df.parse(key.toString());
								if (uploadDate.before(uniprotReleaseDate) || uploadDate.equals(uniprotReleaseDate)) {
									ret.add(mapLines.get(key).toString());
								}
							} catch (final ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				} catch (final IOException e) {
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
	// Map<Accession, Set<ProteinAnnotation>> ret = new THashMap<Accession,
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
		if (uniprot == null || uniprot.getEntry() == null || uniprot.getEntry().isEmpty()) {
			return null;
		}
		if (useIndex) {
			// using index
			final File projectFolder = getUniprotAnnotationsFolder(uniprotVersion);
			if (!projectFolder.exists()) {
				final boolean created = projectFolder.mkdirs();
				if (created)
					log.info(projectFolder.getAbsoluteFile() + " created in the local file system");
			}
			final File file = getUniprotAnnotationsFile(uniprotVersion);
			try {
				UniprotXmlIndex uniprotIndex = null;
				if (loadedIndexes.containsKey(file)) {
					uniprotIndex = loadedIndexes.get(file);
				} else {
					uniprotIndex = new UniprotXmlIndex(file);
					loadedIndexes.put(file, uniprotIndex);
				}
				final List<Entry> entries = uniprot.getEntry();
				for (final Entry entry : entries) {
					uniprotIndex.addItem(entry, null);
				}
				if (entries.size() > 1) {
					log.info(entries.size() + " entries added to index of uniprot version " + uniprotVersion);
				} else {
					log.debug(entries.get(0).getAccession().get(0) + " entry added to index of uniprot version "
							+ uniprotVersion);
				}
				return uniprotIndex.getIndexedFile();
			} catch (final IOException e) {
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
		final String fileName = getAvailableFileName(projectFolder);
		final File filetoCreate = new File(projectFolder + File.separator + fileName);
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
		final Set<String> accessions = new THashSet<>();
		accessions.add(accession);
		return getAnnotatedProteins(uniprotVersion, accessions);
	}

	public synchronized Map<String, Entry> getAnnotatedProteins(String uniprotVersion, Collection<String> accessions) {
		return getAnnotatedProteins(uniprotVersion, accessions, retrieveFastaIsoforms);
	}

	public synchronized Map<String, Entry> getAnnotatedProteins(String uniprotVersion, Collection<String> accessions,
			boolean retrieveFastaIsoforms) {
		final Set<String> accsToSearch = new THashSet<>();
		final Set<String> mainIsoforms = new THashSet<>();
		for (final String acc : accessions) {
			// only search for the uniprot ones
			if (!FastaParser.isContaminant(acc) && !FastaParser.isReverse(acc)
					&& FastaParser.getUniProtACC(acc) != null) {
				if (!FastaParser.isProteoform(FastaParser.getUniProtACC(acc))) {
					if ("1".equals(FastaParser.getIsoformVersion(acc))) {
						final String noIsoAcc = FastaParser.getNoIsoformAccession(acc);
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
		}

		final List<Entry> entries = new ArrayList<>();
		if (cacheEnabled && !cache.isEmpty()) {
			int foundInCache = 0;
			// look into cache if enabled
			final int toFindInCache = accsToSearch.size();
			final Iterator<String> iterator = accsToSearch.iterator();
			while (iterator.hasNext()) {
				if (Thread.currentThread().isInterrupted()) {
					throw new RuntimeException("Thread interrupted");
				}
				final String acc = iterator.next();
				final Entry cachedEntry = cache.getFromCache(acc);
				if (cachedEntry != null) {
					entries.add(cachedEntry);
					iterator.remove();
					foundInCache++;
				}
			}

			log.debug(foundInCache + " entries found in cache");
			if (foundInCache == toFindInCache) {
				final Map<String, Entry> queryProteinsMap = new THashMap<>();
				addEntriesToMap(queryProteinsMap, entries);
				return queryProteinsMap;
			}
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
		Set<String> missingProteinsAccs = new THashSet<>();
		final File projectFolder = getUniprotAnnotationsFolder(uniprotVersion);
		if (projectFolder != null) {

			if (useIndex) {
				final File uniprotXmlFile = getUniprotAnnotationsFile(uniprotVersion);
				try {

					if (loadedIndexes.containsKey(uniprotXmlFile)) {
						uniprotIndex = loadedIndexes.get(uniprotXmlFile);
					} else {
						uniprotIndex = new UniprotXmlIndex(uniprotXmlFile);
						loadedIndexes.put(uniprotXmlFile, uniprotIndex);
					}
					int numEntriesRetrievedFromIndex = 0;
					if (!uniprotIndex.isEmpty() && !accsToSearch.isEmpty()) {
						log.debug("Looking " + accsToSearch.size() + " entries in the local index of annotations at "
								+ uniprotXmlFile.getAbsolutePath());
						final ProgressCounter counter = new ProgressCounter(accsToSearch.size(),
								ProgressPrintingType.PERCENTAGE_STEPS, 0);
						for (final String acc : accsToSearch) {
							counter.increment();
							final String printIfNecessary = counter.printIfNecessary();
							if (accsToSearch.size() > 1 && !"".equals(printIfNecessary)) {
								log.debug(printIfNecessary);
							}
							if (Thread.currentThread().isInterrupted()) {
								throw new RuntimeException("Thread interrupted");
							}
							final Entry item = uniprotIndex.getItem(acc);
							if (item != null) {
								if (cacheEnabled) {
									checkMemoryForCache(1);
									addEntryToCache(cache, item);
								}
								entries.add(item);
								numEntriesRetrievedFromIndex++;
							}
						}
						if (accsToSearch.size() > 0) {
							log.debug(numEntriesRetrievedFromIndex + " entries retrieved from index");
						}
					}

				} catch (final IOException e) {
					e.printStackTrace();
					log.error(e.getMessage());
				} catch (final JAXBException e) {
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
						} catch (final JAXBException e) {
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
			final Map<String, Entry> queryProteinsMap = new THashMap<>();
			addEntriesToMap(queryProteinsMap, entries);// noIsoformAccs,
														// queryProteinsMap,
														// entries);

			// look for some protein missing in the local system
			missingProteinsAccs = getMissingAccessions(accsToSearch, queryProteinsMap);// noIsoformAccs,
																						// queryProteinsMap);

			if (!missingProteinsAccs.isEmpty()) {
				try {
					final UniprotProteinRemoteRetriever uprr = new UniprotProteinRemoteRetriever(uniprotReleasesFolder,
							useIndex);
					uprr.setLookForIsoforms(retrieveFastaIsoforms);
					log.debug("Trying to retrieve  " + missingProteinsAccs.size()
							+ " proteins that were not present in the local system");
					final Map<String, Entry> annotatedProteins = uprr.getAnnotatedProteins(uniprotVersion,
							missingProteinsAccs, uniprotIndex, cache);
					if (cacheEnabled) {
						checkMemoryForCache(annotatedProteins.size());
						cache.addtoCache(annotatedProteins);
					}
					queryProteinsMap.putAll(annotatedProteins);
					missingAccessions = uprr.getMissingAccessions();
				} catch (final IllegalArgumentException e) {
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
			for (final String mainIsoform : mainIsoforms) {
				if (Thread.currentThread().isInterrupted()) {
					throw new RuntimeException("Thread interrupted");
				}
				final Entry entry = queryProteinsMap.get(FastaParser.getNoIsoformAccession(mainIsoform));
				if (entry != null) {
					log.debug("Mapping back " + mainIsoform + " to entry " + entry.getAccession().get(0));
					queryProteinsMap.put(mainIsoform, entry);
					if (cacheEnabled) {
						cache.addtoCache(entry, mainIsoform);
					}
				}
			}

			if (queryProteinsMap.size() > 1) {
				log.debug("Returning " + queryProteinsMap.size() + " / " + accessions.size() + " proteins");
			}
			return queryProteinsMap;
		} else {
			try {
				String folderPath = "";
				if (projectFolder != null) {
					folderPath = projectFolder.getAbsolutePath();
				}
				log.info("Local information (local index folder) not found " + folderPath);
				log.info("Trying to get it remotely from Uniprot repository");
				final UniprotProteinRemoteRetriever uprr = new UniprotProteinRemoteRetriever(uniprotReleasesFolder,
						useIndex);
				final Map<String, Entry> queryProteinsMap = uprr.getAnnotatedProteins(uniprotVersion, accsToSearch,
						uniprotIndex, cache);
				// map main isoforms to the corresponding no isoform entries,
				// that is an entry like P12345-1 to P12345
				for (final String mainIsoform : mainIsoforms) {
					if (Thread.currentThread().isInterrupted()) {
						throw new RuntimeException("Thread interrupted");
					}
					final Entry entry = queryProteinsMap.get(FastaParser.getNoIsoformAccession(mainIsoform));
					if (entry != null) {
						if (cacheEnabled) {
							cache.addtoCache(entry, mainIsoform);
						}
						log.info("Mapping back " + mainIsoform + " to entry " + entry.getAccession().get(0));
						queryProteinsMap.put(mainIsoform, entry);
					}
				}
				return queryProteinsMap;
			} catch (final IllegalArgumentException e) {
				log.info(e.getMessage());
				log.info("It is not possible to get information using the argument uniprot version: " + uniprotVersion);
				return new THashMap<>();
			}
		}
	}

	private void checkMemoryForCache(int numItems) {
		final double freeMemoryPercentage = MemoryUsageReport.getFreeMemoryPercentage();
		if (freeMemoryPercentage < MemoryUsageReport.RECOMMENDED_MINIMUM_MEMORY_PERCENTAGE) {
			// remove one item from cache
			log.warn("Memory free is under " + MemoryUsageReport.RECOMMENDED_MINIMUM_MEMORY_PERCENTAGE + "%: "
					+ freeMemoryPercentage + ". Removing old entry from the cache before entering another one.");
			for (int i = 0; i < numItems; i++) {
				removeFirstEntryFromCache();
			}
		}
	}

	private void removeFirstEntryFromCache() {
		final Entry entry = cache.getFromCache(getFirstEntryKeyFromCache());
		if (entry != null) {
			final List<String> accession = entry.getAccession();
			for (final String acc : accession) {
				cache.removeFromCache(acc);
				entryKeys.remove(acc);
			}
		} else {
			log.warn("?");
		}
	}

	private String getFirstEntryKeyFromCache() {
		if (!entryKeys.isEmpty()) {
			return entryKeys.get(0);
		}
		return null;
	}

	private void addEntryKeys(Entry entry) {
		final List<String> accession = entry.getAccession();
		for (final String acc : accession) {
			entryKeys.add(acc);
		}
	}

	private Map<String, Entry> converToOriginalAccessions(HashMap<String, Entry> queryProteins,
			Map<String, String> isoformMap) {
		final Map<String, Entry> ret = new THashMap<>();
		for (final String nonIsoformAcc : queryProteins.keySet()) {
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
			final String regexp = "(\\d+)_(\\d+)";
			final Pattern pattern = Pattern.compile(regexp);
			final String uniprotReleasesPath = uniprotReleasesFolder.getAbsolutePath();
			final File folder = new File(uniprotReleasesPath);
			if (folder.exists()) {
				final File[] listFiles = folder.listFiles();
				int year = 0;
				int month = 0;
				Date releaseDate = null;
				for (final File file : listFiles) {
					if (file.isDirectory()) {
						final String folderName = FilenameUtils.getName(file.getAbsolutePath());

						final Matcher matcher = pattern.matcher(folderName);
						if (matcher.find()) {
							year = Integer.valueOf(matcher.group(1));
							month = Integer.valueOf(matcher.group(2));
							final Date releaseDateTmp = new Date(year, month, 1);
							if (releaseDate == null || releaseDate.compareTo(releaseDateTmp) == -1) {
								releaseDate = releaseDateTmp;
							}
						}
					}
				}
				if (releaseDate != null) {
					final DecimalFormat twoDigits = new DecimalFormat("00");
					final String monthString = twoDigits.format(month);
					final DecimalFormat fourDigits = new DecimalFormat("0000");
					final String yearString = fourDigits.format(year);
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
		final Uniprot uniprot = new ObjectFactory().createUniprot();
		for (final String xmlFileName : xmlfilesNames) {
			final List<Entry> entries = parseXmlFile(
					new File(projectFolder.getAbsolutePath() + File.separator + xmlFileName));
			if (entries != null) {
				log.info("Adding " + entries.size() + " entries to the list of entries");
				uniprot.getEntry().addAll(entries);
				log.info("Now having " + uniprot.getEntry().size() + " entries");
			}
		}

		// remove all the rest of the files
		for (final String xmlFileName : xmlfilesNames) {
			final File file2 = new File(projectFolder.getAbsolutePath() + File.separator + xmlFileName);
			log.info("Deleting " + file2.getAbsolutePath() + "...");
			final boolean deleted = file2.delete();
			log.info(file2.getAbsolutePath() + " deleted = " + deleted);
		}

		final File file = saveUniprotToLocalFilesystem(uniprot, uniprotVersion, useIndex);

		return uniprot.getEntry();
	}

	protected static void addEntriesToMap(UniprotEntryCache map, List<Entry> entries) {
		for (final Entry entry : entries) {
			addEntryToCache(map, entry);
		}

	}

	protected static void addEntriesToMap(Map<String, Entry> map, List<Entry> entries) {
		for (final Entry entry : entries) {
			addEntryToMap(map, entry);
		}
	}

	protected static void addEntryToMap(Map<String, Entry> map, Entry entry) {

		final List<String> accessions = entry.getAccession();
		for (final String accession : accessions) {
			// String nonIsoFormAcc = getNoIsoformAccession(accession);
			// if (accessionsToQuery.contains(nonIsoFormAcc))
			map.put(accession, entry);
		}

	}

	protected static void addEntryToCache(UniprotEntryCache cache, Entry entry) {

		final List<String> accessions = entry.getAccession();
		for (final String accession : accessions) {
			// String nonIsoFormAcc = getNoIsoformAccession(accession);
			// if (accessionsToQuery.contains(nonIsoFormAcc))
			cache.addtoCache(entry, accession);
		}

	}

	public boolean isCacheEnabled() {
		return cacheEnabled;
	}

	public void setCacheEnabled(boolean cacheEnabled) {
		UniprotProteinLocalRetriever.cacheEnabled = cacheEnabled;
	}

	public void setRetrieveFastaIsoforms(boolean b) {
		retrieveFastaIsoforms = b;

	}

}
