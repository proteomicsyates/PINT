package edu.scripps.yates.annotations.uniprot;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.scripps.yates.annotations.uniprot.index.UniprotXmlIndex;
import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.annotations.uniprot.xml.SequenceType;
import edu.scripps.yates.annotations.uniprot.xml.Uniprot;
import edu.scripps.yates.annotations.util.PropertiesUtil;
import edu.scripps.yates.annotations.util.UniprotEntryCache;
import edu.scripps.yates.utilities.cores.SystemCoreManager;
import edu.scripps.yates.utilities.dates.DatesUtil;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.files.FileUtils;
import edu.scripps.yates.utilities.pi.ParIterator;
import edu.scripps.yates.utilities.pi.ParIterator.Schedule;
import edu.scripps.yates.utilities.pi.ParIteratorFactory;
import edu.scripps.yates.utilities.pi.reductions.Reducible;
import edu.scripps.yates.utilities.pi.reductions.Reduction;
import edu.scripps.yates.utilities.progresscounter.ProgressCounter;
import edu.scripps.yates.utilities.progresscounter.ProgressPrintingType;
import edu.scripps.yates.utilities.util.Pair;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

/**
 * This class retrieves the protein information from uniprot
 *
 * @author Salva
 *
 */
public class UniprotProteinRemoteRetriever {
	private static final String UNIPROT_EBI_SERVER = PropertiesUtil.getInstance(PropertiesUtil.UNIPROT_PROPERTIES_FILE)
			.getPropertyValue(PropertiesUtil.UNIPROT_EBI_SERVER_PROP);
	private static final String UNIPROT_SERVER = PropertiesUtil.getInstance(PropertiesUtil.UNIPROT_PROPERTIES_FILE)
			.getPropertyValue(PropertiesUtil.UNIPROT_SERVER_PROP);
	private static final Logger log = Logger.getLogger(UniprotProteinRemoteRetriever.class);
	// private static final int MAX_NUM_TO_RETRIEVE = 200; // defined by EBI
	// fetch
	private static final int MAX_NUM_TO_RETRIEVE = 100; // defined by EBI
														// protein service
	private Set<String> missingAccessions = new THashSet<>();
	private final File uniprotReleaseFolder;
	private final boolean useIndex;
	private boolean lookForIsoforms = false;
	private final static Set<String> doNotFound = new THashSet<>();
	private static Unmarshaller unmarshaller;
	private static Marshaller marshaller;
	private static JAXBContext jaxbContext;
	private static Date currentUniprotVersionRetrievedDate;
	private static String currentUniprotVersion;
	private static boolean notTryUntilNextDay;
	protected static final Set<String> entriesWithNoFASTA = new THashSet<>();
	protected static final Set<String> entriesWithNoInfo = new THashSet<>();
	private final ExecutorService executor = Executors.newSingleThreadExecutor();
	// it cannot be multithread to write in a file
	// private final ExecutorService executor = Executors.newCachedThreadPool();

	// service

	/**
	 *
	 * @param uniprotReleaseFolder
	 *            if null, the retrieved information will not be saved locally
	 * @param useIndex
	 */
	protected UniprotProteinRemoteRetriever(File uniprotReleaseFolder, boolean useIndex) {
		if (jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance(Uniprot.class);
				unmarshaller = jaxbContext.createUnmarshaller();
				marshaller = jaxbContext.createMarshaller();
			} catch (final JAXBException e) {
				e.printStackTrace();

			}
		}
		// only accessible by local retriever
		this.uniprotReleaseFolder = uniprotReleaseFolder;
		this.useIndex = useIndex;
	}

	public Uniprot getProteins(Collection<String> accessions, String uniprotVersion, UniprotXmlIndex uniprotIndex,
			UniprotEntryCache cache) {
		if (accessions == null || accessions.isEmpty()) {
			return new Uniprot();
		}
		final Map<String, Entry> entryMap = new THashMap<>();
		final List<String> noIsoformList = new ArrayList<>();
		final Set<String> isoformList = new THashSet<>();
		final ProgressCounter counter = new ProgressCounter(accessions.size(), ProgressPrintingType.PERCENTAGE_STEPS,
				0);
		log.info("Looking for " + accessions.size() + " proteins in the index of annotations");
		for (final String acc : accessions) {
			if (entriesWithNoInfo.contains(acc)) {
				// do not try again
				continue;
			}
			counter.increment();
			final String printIfNecessary = counter.printIfNecessary();
			if (!"".equals(printIfNecessary)) {
				log.info(printIfNecessary);
			}
			if (Thread.currentThread().isInterrupted()) {
				throw new RuntimeException("Thread interrupted");
			}
			// skip reverse proteins
			if (acc.toLowerCase().contains("reverse")) {
				continue;
			}
			// convert isoform accessions to non isoforms but keep the isoforms
			final String isoformVersion = FastaParser.getIsoformVersion(acc);
			final String noIsoformAccession = FastaParser.getNoIsoformAccession(acc);
			if (isoformVersion != null && !"1".equals(isoformVersion)) {
				// if (uniprotIndex != null) {
				// // first, check if the isoform was already retrieved
				// // previously
				// if (!uniprotIndex.isEmpty()) {
				// final Entry isoformEntry = uniprotIndex.getItem(acc);
				// if (isoformEntry != null) {
				// entryMap.put(isoformVersion, isoformEntry);
				// continue;
				// }
				// }
				// }
				isoformList.add(acc);
				if (!noIsoformList.contains(noIsoformAccession)) {
					noIsoformList.add(noIsoformAccession);
				}
				if (cache != null && cache.contains(noIsoformAccession)) {
					final Entry nonIsoformEntry = cache.getFromCache(noIsoformAccession);
					if (nonIsoformEntry != null) {
						entryMap.put(noIsoformAccession, nonIsoformEntry);
						// do not search it again
						noIsoformList.remove(noIsoformAccession);
					}
				}
				if (uniprotIndex != null) {
					// if we already have the main form in the index
					final Entry nonIsoformEntry = uniprotIndex.getItem(noIsoformAccession);
					if (nonIsoformEntry != null) {
						entryMap.put(noIsoformAccession, nonIsoformEntry);
						// do not search it again
						noIsoformList.remove(noIsoformAccession);
					}
				}
			} else {
				if (!noIsoformList.contains(noIsoformAccession)) {
					noIsoformList.add(noIsoformAccession);
				}
			}
		}

		final Set<String> accessionsSent = new THashSet<>();
		try {

			int totalNumAccs = 0;
			while (totalNumAccs < noIsoformList.size()) {
				// StringBuilder locationBuilder = new
				// StringBuilder(UNIPROT_EBI_SERVER + "&format=xml&id=");
				final StringBuilder locationBuilder = new StringBuilder(UNIPROT_EBI_SERVER + "&accession=");
				int numAccs = 0;
				while (totalNumAccs < noIsoformList.size()) {
					if (numAccs > 0)
						locationBuilder.append(',');
					locationBuilder.append(noIsoformList.get(totalNumAccs));
					accessionsSent.add(noIsoformList.get(totalNumAccs));
					numAccs++;
					totalNumAccs++;
					if (numAccs == MAX_NUM_TO_RETRIEVE)
						break;
				}

				final String location = locationBuilder.toString();
				final URL url = new URL(location).toURI().toURL();
				log.info("Submitting " + numAccs + " (" + totalNumAccs + "/" + noIsoformList.size() + ") at '"
						+ location + "'...");
				final long t1 = System.currentTimeMillis();
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				HttpURLConnection.setFollowRedirects(true);
				conn.setDoInput(true);
				conn.setConnectTimeout(10000);// wait for 10s
				conn.connect();

				int status = conn.getResponseCode();
				while (true) {
					int wait = 0;
					final String header = conn.getHeaderField("Retry-After");
					if (header != null)
						wait = Integer.valueOf(header);
					if (wait == 0)
						break;
					log.debug("Waiting (" + wait + ")...");
					conn.disconnect();
					Thread.sleep(wait * 1000);
					conn = (HttpURLConnection) new URL(location).openConnection();
					conn.setDoInput(true);
					conn.setConnectTimeout(10000);// wait for 10s
					conn.connect();
					status = conn.getResponseCode();
				}
				if (status == HttpURLConnection.HTTP_OK) {
					final long t2 = System.currentTimeMillis();
					log.debug("Got a OK reply in " + DatesUtil.getDescriptiveTimeFromMillisecs(t2 - t1));
					final InputStream is = conn.getInputStream();
					URLConnection.guessContentTypeFromStream(is);
					final List<Entry> entries = parseResponse(is, uniprotVersion, accessionsSent);
					final long t3 = System.currentTimeMillis();
					log.debug("Response parsed in " + DatesUtil.getDescriptiveTimeFromMillisecs(t3 - t2));
					for (final Entry entry : entries) {
						final List<String> accession = entry.getAccession();
						for (final String acc : accession) {
							if (accessionsSent.contains(acc)) {
								entryMap.put(acc, entry);
							}
						}
					}
				} else {
					log.error("Failed, got " + conn.getResponseMessage() + " for " + location);
				}
				conn.disconnect();
			}
		} catch (final MalformedURLException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		} catch (final IOException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		} catch (final InterruptedException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		} catch (final URISyntaxException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		}

		// now, if there are isoforms, look for the protein sequences of them
		if (lookForIsoforms) {
			final Map<String, Entry> fastaEntries = getFASTASequencesInParallel(isoformList);
			// Map<String, Entry> fastaEntries = getFASTASequences(isoformList);
			if (!fastaEntries.isEmpty()) {
				for (final String isoformAcc : isoformList) {
					final String noIsoformAcc = FastaParser.getNoIsoformAccession(isoformAcc);
					if (entryMap.containsKey(noIsoformAcc)) {
						final Entry noIsoformEntry = entryMap.get(noIsoformAcc);

						if (fastaEntries.containsKey(isoformAcc)) {
							final Entry isoformEntry = fastaEntries.get(isoformAcc);
							final SequenceType sequenceType = isoformEntry.getSequence();
							if (sequenceType != null) {
								final String proteinSequence = sequenceType.getValue();
								if (proteinSequence != null) {
									final Entry clonedEntry = cloneEntry(noIsoformEntry);
									// override the protein sequence
									if (clonedEntry.getSequence() == null) {
										clonedEntry.setSequence(new SequenceType());
									}
									clonedEntry.getSequence().setValue(proteinSequence);
									// override main accession
									clonedEntry.getAccession().clear();
									clonedEntry.getAccession().add(isoformAcc);
									// override names
									clonedEntry.getProtein()
											.setRecommendedName(isoformEntry.getProtein().getRecommendedName());
									clonedEntry.getProtein().getAlternativeName().clear();
									clonedEntry.getProtein().getAlternativeName()
											.addAll(isoformEntry.getProtein().getAlternativeName());
									// store it with the isoform acc
									entryMap.put(isoformAcc, clonedEntry);

									// call to the UniprotLocalRetriever to save
									// into
									// the file
									// system
									try {
										if (uniprotReleaseFolder != null) {
											final UniprotProteinLocalRetriever uplr = new UniprotProteinLocalRetriever(
													uniprotReleaseFolder, useIndex);
											final Uniprot uniprot = new Uniprot();
											uniprot.getEntry().add(clonedEntry);
											uplr.saveUniprotToLocalFilesystem(uniprot, uniprotVersion, useIndex);
										}
									} catch (final JAXBException e) {
										e.printStackTrace();
									}
									log.debug("Response parsed succesfully");
								}
							}
						}
					}
				}
			}
		}
		final Uniprot uniprot = new Uniprot();
		uniprot.getEntry().addAll(entryMap.values());

		for (final String acc : accessions) {
			if (!entryMap.containsKey(acc)) {
				entriesWithNoInfo.add(acc);
			}
		}
		return uniprot;
	}

	/**
	 * Clone an {@link Entry} by marshalling to a file and unmarshalling it
	 * again
	 *
	 * @param entry
	 * @return
	 */
	private Entry cloneEntry(Entry entry) {
		try {

			final Uniprot uniprot = new Uniprot();
			uniprot.getEntry().add(entry);

			final File tempFile = File.createTempFile(entry.getAccession().get(0), ".xml");
			tempFile.deleteOnExit();
			marshaller.marshal(uniprot, tempFile);
			final Uniprot newUniprotObj = (Uniprot) unmarshaller.unmarshal(tempFile);
			return newUniprotObj.getEntry().get(0);
		} catch (JAXBException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Uniprot getProteins(String queryString) {

		try {
			final Set<String> accessionsSent = new THashSet<>();
			accessionsSent.add(queryString);
			final StringBuilder locationBuilder = new StringBuilder(UNIPROT_SERVER + queryString + "&format=xml");

			String location = locationBuilder.toString();
			location = location.replace(" ", "%20");
			final URL url = new URL(location).toURI().toURL();
			log.info("Submitting " + locationBuilder + "...");
			final long t1 = System.currentTimeMillis();
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			HttpURLConnection.setFollowRedirects(true);
			conn.setDoInput(true);
			conn.connect();

			int status = conn.getResponseCode();
			while (true) {
				int wait = 0;
				final String header = conn.getHeaderField("Retry-After");
				if (header != null)
					wait = Integer.valueOf(header);
				if (wait == 0)
					break;
				log.info("Waiting (" + wait + ")...");
				conn.disconnect();
				Thread.sleep(wait * 1000);
				conn = (HttpURLConnection) new URL(location).openConnection();
				conn.setDoInput(true);
				conn.connect();
				status = conn.getResponseCode();
			}
			if (status == HttpURLConnection.HTTP_OK) {
				final long t2 = System.currentTimeMillis();
				log.debug("Got a OK reply in " + (t2 - t1) / 1000 + "sg");
				final InputStream is = conn.getInputStream();
				URLConnection.guessContentTypeFromStream(is);
				final Uniprot response = parseResponse(is, accessionsSent);
				return response;
			} else
				log.error("Failed, got " + conn.getResponseMessage() + " for " + location);
			conn.disconnect();

		} catch (final MalformedURLException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		} catch (final IOException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		} catch (final InterruptedException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		} catch (final URISyntaxException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		}
		return null;
	}

	private static int executors = 0;
	ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

	private List<Entry> parseResponse(InputStream is, String uniprotVersion, Set<String> accessionsSent) {

		final Uniprot uniprot = parseResponse(is, accessionsSent);
		if (uniprot != null) {
			// call to the UniprotLocalRetriever to save into the file
			// system

			if (uniprotReleaseFolder != null) {
				log.debug("Launching parallel process to write entries in index");
				final WriteLock writeLock = lock.writeLock();
				writeLock.lock();
				try {
					log.debug(++executors + " executors already");
				} finally {
					writeLock.unlock();
				}
				executor.submit(() -> {

					try {
						final long t1 = System.currentTimeMillis();
						log.info("Saving " + uniprot.getEntry().size() + " entries to local index...");
						final UniprotProteinLocalRetriever uplr = new UniprotProteinLocalRetriever(uniprotReleaseFolder,
								useIndex);
						uplr.saveUniprotToLocalFilesystem(uniprot, uniprotVersion, useIndex);
						final ReadLock readLock = lock.readLock();
						readLock.lock();
						try {
							log.info("Executor " + executors + ": Entries saved to local index in "
									+ DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1));
						} finally {
							readLock.unlock();
						}
					} catch (final JAXBException e) {
						e.printStackTrace();
						log.warn(e);
					}
					final WriteLock writeLock2 = lock.writeLock();
					writeLock2.lock();
					try {
						executors--;
					} finally {
						writeLock2.unlock();
					}
				});

			}

			log.debug("Response parsed succesfully");
			return uniprot.getEntry();
		} else

		{
			return new ArrayList<>();
		}

	}

	private Uniprot parseResponse(InputStream is, Set<String> accessionsSent) {

		log.debug("Processing response from remote input stream...");

		// OutputStream outputStream = null;
		try {
			final long t1 = System.currentTimeMillis();
			final File file = FileUtils.getFileFromInputStream(is);
			file.deleteOnExit();
			log.debug("Input stream saved as file " + FileUtils.getDescriptiveSizeFromBytes(file.length()) + " in "
					+ DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1));
			final long t2 = System.currentTimeMillis();
			final Uniprot uniprot = (Uniprot) unmarshaller.unmarshal(file);

			log.debug("File unmarshalled succesfully in "
					+ DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t2));
			log.debug(uniprot.getEntry().size() + " entries");
			Thread.sleep(1l);
			return uniprot;
		} catch (final JAXBException e) {
			// e.printStackTrace();
			log.warn(e.getMessage() + "\t" + e.getLinkedException().getMessage());
		} catch (final IOException e2) {
			e2.printStackTrace();

		} catch (final InterruptedException e) {
			throw new RuntimeException("task cancelled");
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
			// if (outputStream != null) {
			// try {
			// // outputStream.flush();
			// outputStream.close();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			//
			// }
		}
		return getRDFEntries(accessionsSent);
	}

	private Uniprot getRDFEntries(Set<String> accessionsSent) {
		final Uniprot ret = new Uniprot();
		int num = 0;
		for (final String accession : accessionsSent) {
			try {
				final StringBuilder locationBuilder = new StringBuilder(
						"http://www.uniprot.org/uniprot/" + accession + ".rdf");
				String location = locationBuilder.toString();
				location = location.replace(" ", "%20");
				final URL url = new URL(location).toURI().toURL();
				log.info("Submitting " + locationBuilder + "...");
				final long t1 = System.currentTimeMillis();
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				HttpURLConnection.setFollowRedirects(true);
				conn.setDoInput(true);
				conn.connect();

				int status = conn.getResponseCode();
				while (true) {
					int wait = 0;
					final String header = conn.getHeaderField("Retry-After");
					if (header != null)
						wait = Integer.valueOf(header);
					if (wait == 0)
						break;
					log.info("Waiting (" + wait + ")...");
					conn.disconnect();
					Thread.sleep(wait * 1000);
					conn = (HttpURLConnection) new URL(location).openConnection();
					conn.setDoInput(true);
					conn.connect();
					status = conn.getResponseCode();
				}
				if (status == HttpURLConnection.HTTP_OK) {
					final long t2 = System.currentTimeMillis();
					log.debug("Got a OK reply in " + DatesUtil.getDescriptiveTimeFromMillisecs(t2 - t1) + " (protein "
							+ num++ + "/" + accessionsSent.size() + ")");
					final InputStream is = conn.getInputStream();
					URLConnection.guessContentTypeFromStream(is);
					final Entry entry = parseRDFResponse(is, accession);
					if (entry != null)
						ret.getEntry().add(entry);
				} else {
					log.error("Failed, got " + conn.getResponseMessage() + " for " + location);
				}
				conn.disconnect();

			} catch (final MalformedURLException e) {
				e.printStackTrace();
				log.warn(e.getMessage());
			} catch (final IOException e) {
				e.printStackTrace();
				log.warn(e.getMessage());
			} catch (final InterruptedException e) {
				e.printStackTrace();
				log.warn(e.getMessage());
			} catch (final URISyntaxException e) {
				e.printStackTrace();
				log.warn(e.getMessage());
			}
		}

		return ret;
	}

	public static Map<String, Entry> getFASTASequencesInParallel(Set<String> accessions) {
		final Set<String> validToLook = new THashSet<>();
		for (final String acc : accessions) {
			if (!entriesWithNoFASTA.contains(acc)) {
				validToLook.add(acc);
			}
		}
		if (validToLook.isEmpty()) {
			return Collections.emptyMap();
		}
		final int threadCount = Math.min(SystemCoreManager.getAvailableNumSystemCores(8), accessions.size());
		log.info("getting fasta sequences of " + accessions.size() + " proteins in parallel using " + threadCount
				+ " threads...");
		final ParIterator<String> iterator = ParIteratorFactory.createParIterator(accessions, threadCount,
				Schedule.GUIDED);
		final Reducible<Map<String, Entry>> reducibleEntryMap = new Reducible<>();
		final List<UniprotFastaRetrieverThread> runners = new ArrayList<>();
		for (int numCore = 0; numCore < threadCount; numCore++) {
			// take current DB session
			final UniprotFastaRetrieverThread runner = new UniprotFastaRetrieverThread(iterator, reducibleEntryMap);
			runners.add(runner);
			runner.start();
		}

		// Main thread waits for worker threads to complete
		for (int k = 0; k < threadCount; k++) {
			try {
				runners.get(k).join();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}

		final Reduction<Map<String, Entry>> entryReduction = new Reduction<Map<String, Entry>>() {
			@Override
			public Map<String, Entry> reduce(Map<String, Entry> first, Map<String, Entry> second) {
				final Map<String, Entry> ret = new THashMap<>();
				for (final String acc : first.keySet()) {
					if (!ret.containsKey(acc)) {
						ret.put(acc, first.get(acc));
					}
				}
				for (final String acc : second.keySet()) {
					if (!ret.containsKey(acc)) {
						ret.put(acc, second.get(acc));
					}
				}
				return ret;
			}

		};
		final Map<String, Entry> ret = reducibleEntryMap.reduce(entryReduction);
		log.info("Retrieved " + ret.size() + " FASTA entries in paralell");
		return ret;
	}

	private Map<String, Entry> getFASTASequences(Set<String> accessions) {
		final Map<String, Entry> ret = new THashMap<>();
		int num = 0;
		log.debug("Trying to get the fasta sequences of " + accessions.size() + " proteins (probably isoforms)");
		for (final String accession : accessions) {
			try {
				if (entriesWithNoFASTA.contains(accession)) {
					continue;
				}
				final StringBuilder locationBuilder = new StringBuilder(
						"http://www.uniprot.org/uniprot/" + accession + ".fasta");
				String location = locationBuilder.toString();
				location = location.replace(" ", "%20");
				final URL url = new URL(location).toURI().toURL();
				log.info("Submitting " + locationBuilder + "...");
				final long t1 = System.currentTimeMillis();
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				HttpURLConnection.setFollowRedirects(true);
				conn.setDoInput(true);
				conn.connect();

				int status = conn.getResponseCode();
				while (true) {
					int wait = 0;
					final String header = conn.getHeaderField("Retry-After");
					if (header != null)
						wait = Integer.valueOf(header);
					if (wait == 0)
						break;
					log.info("Waiting (" + wait + ")...");
					conn.disconnect();
					Thread.sleep(wait * 1000);
					conn = (HttpURLConnection) new URL(location).openConnection();
					conn.setDoInput(true);
					conn.connect();
					status = conn.getResponseCode();
				}
				if (status == HttpURLConnection.HTTP_OK) {
					final long t2 = System.currentTimeMillis();
					log.info("Got a OK reply in " + DatesUtil.getDescriptiveTimeFromMillisecs(t2 - t1) + " (protein "
							+ num++ + "/" + accessions.size() + ")");
					final InputStream is = conn.getInputStream();
					URLConnection.guessContentTypeFromStream(is);
					final Entry fastaEntry = parseFASTAResponse(is, accession);
					if (fastaEntry != null) {
						ret.put(accession, fastaEntry);
					} else {
						log.info("Adding " + accession + " to the list of proteins with no FASTA sequence available.");
						entriesWithNoFASTA.add(accession);
					}
				} else {
					log.warn("Failed, got " + conn.getResponseMessage() + " for " + location);
				}
				conn.disconnect();

			} catch (final MalformedURLException e) {
				e.printStackTrace();
				log.warn(e.getMessage());
			} catch (final IOException e) {
				e.printStackTrace();
				log.warn(e.getMessage());
			} catch (final InterruptedException e) {
				e.printStackTrace();
				log.warn(e.getMessage());
			} catch (final URISyntaxException e) {
				e.printStackTrace();
				log.warn(e.getMessage());
			}
		}

		return ret;
	}

	protected static Entry parseRDFResponse(InputStream is, String accession) {

		boolean obsolete = false;
		boolean reviewed = false;
		String modified = null;
		int version = 0;
		final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			final Document doc = dBuilder.parse(is);
			final NodeList nList = doc.getElementsByTagName("rdf:Description");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				final Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					final Element eElement = (Element) nNode;
					final NodeList obsoleteNode = eElement.getElementsByTagName("obsolete");
					if (obsoleteNode != null && obsoleteNode.getLength() > 0) {
						if (obsoleteNode.item(0).getFirstChild().getNodeValue().equals("true")) {
							obsolete = true;
						}
					}
					final NodeList reviewedNode = eElement.getElementsByTagName("reviewed");
					if (reviewedNode != null && reviewedNode.getLength() > 0) {
						if (reviewedNode.item(0).getFirstChild().getNodeValue().equals("true")) {
							reviewed = true;
						}
					}
					final NodeList modifiedNode = eElement.getElementsByTagName("modified");
					if (modifiedNode != null && modifiedNode.getLength() > 0) {
						modified = modifiedNode.item(0).getFirstChild().getNodeValue();
					}
					final NodeList versionNode = eElement.getElementsByTagName("version");
					if (versionNode != null && versionNode.getLength() > 0) {
						version = Integer.valueOf(versionNode.item(0).getFirstChild().getNodeValue());
					}
				}
			}
			final Entry ret = new UniprotEntryAdapterFromRDF(accession, obsolete, reviewed, modified, version).adapt();
			return ret;
		} catch (final ParserConfigurationException e) {
			e.printStackTrace();
		} catch (final SAXException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected static Entry parseFASTAResponse(InputStream is, String accession) {

		String myString;
		try {
			myString = IOUtils.toString(is, "UTF-8");

			final String[] split = myString.split("\n");
			StringBuilder sequence = new StringBuilder();
			String fastaHeader = null;

			// it may return more than one fasta header, so get the one for the
			// accession
			boolean takeSequence = false;
			for (int i = 0; i < split.length; i++) {
				if (split[i].startsWith(">")) {

					// check if there was a sequence before and it was the
					// correct one (takeSequence=true)
					if (takeSequence && !"".equals(sequence.toString())) {
						return new UniprotEntryAdapterFromFASTA(accession, fastaHeader, sequence.toString()).adapt();
					}
					fastaHeader = split[i];
					final Pair<String, String> accession2 = FastaParser.getACC(fastaHeader);
					sequence = new StringBuilder();
					if (accession2.getFirstelement().equals(accession)) {
						takeSequence = true;
					}
				} else {
					sequence.append(split[i]);
				}
			}

			if (takeSequence && !"".equals(sequence.toString())) {
				return new UniprotEntryAdapterFromFASTA(accession, fastaHeader, sequence.toString()).adapt();
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		// run("mapping", new ParameterNameValue[] {
		// new ParameterNameValue("from", "ACC"),
		// new ParameterNameValue("to", "P_REFSEQ_AC"),
		// new ParameterNameValue("format", "tab"),
		// new ParameterNameValue("query",
		// "P13368 P20806 Q9UM73 P97793 Q17192"), });

		final Collection<String> set = new THashSet<>();

		final String[] array = new String[] { "Q8CIE2-2", "P06802-2", "P01895-2", "Q8BUL6-2" };
		for (final String string : array) {
			set.add(string);
		}
		final UniprotProteinRemoteRetriever uprr = new UniprotProteinRemoteRetriever(
				new File("C:\\users\\salva\\desktop\\tmp\\UniprotKB"), true);
		uprr.setLookForIsoforms(true);
		final Uniprot uniprot = uprr.getProteins(set, UniprotProteinRemoteRetriever.getCurrentUniprotRemoteVersion(),
				null, null);
		for (final Entry entry2 : uniprot.getEntry()) {
			final List<String> names = entry2.getName();
			for (final String string : names) {
				System.out.println(string);
			}
		}
	}

	public synchronized Map<String, Entry> getAnnotatedProteins(String uniprotVersion, Collection<String> accessions,
			UniprotXmlIndex uniprotLocalIndex, UniprotEntryCache cache) throws IllegalArgumentException {
		final String currentUniprotRemoteVersion = getCurrentUniprotRemoteVersion();
		if (currentUniprotRemoteVersion.equals(uniprotVersion)) {
			log.debug("Current uniprot release matches with the provided: " + uniprotVersion);
			log.debug("Attemping to retrieve " + accessions.size() + " accessions");
			final long t1 = System.currentTimeMillis();
			final Set<String> toSearch = new THashSet<>();
			for (final String acc : accessions) {
				if (!doNotFound.contains(acc)) {
					toSearch.add(acc);
				}
			}
			final Uniprot proteins = getProteins(toSearch, uniprotVersion, uniprotLocalIndex, cache);

			final long t2 = System.currentTimeMillis();

			log.debug(proteins.getEntry().size() + " accessions retrieved in " + (t2 - t1) / 1000 + "sg");

			// long t3 = System.currentTimeMillis();
			final Map<String, Entry> map = new THashMap<>();
			UniprotProteinLocalRetriever.addEntriesToMap(map, proteins.getEntry());
			checkIfSomeProteinIsMissing(toSearch, map, uniprotVersion, uniprotLocalIndex, cache);

			return map;
		}
		throw new IllegalArgumentException("Current version of uniprot different from provided: " + uniprotVersion
				+ " vs " + currentUniprotRemoteVersion);

	}

	private synchronized void checkIfSomeProteinIsMissing(Collection<String> accessions, Map<String, Entry> retrieved,
			String uniprotVersion, UniprotXmlIndex uniprotLocalIndex, UniprotEntryCache cache) {
		final int numBeforeRecovery = retrieved.size();
		final Set<String> missingProteins = getMissingAccs(retrieved, accessions);
		if (!missingProteins.isEmpty()) {
			log.debug("Trying to recover " + missingProteins.size() + " proteins");

			final Uniprot proteins = getProteins(missingProteins, uniprotVersion, uniprotLocalIndex, cache);
			final List<Entry> entries = proteins.getEntry();
			UniprotProteinLocalRetriever.addEntriesToMap(retrieved, entries);
			final int numRecovered = retrieved.size() - numBeforeRecovery;
			log.debug(numRecovered + " proteins were able to be recovered");
			log.debug(missingProteins.size() - numRecovered + " proteins still remain missing");
			missingAccessions = getMissingAccs(retrieved, accessions);
			final StringBuilder allMissing = new StringBuilder();
			for (final String acc : missingAccessions) {
				allMissing.append(acc).append(",");
				if (allMissing.length() > 1000) {
					break;
				}
			}
			log.debug("Still missing: " + missingAccessions.size() + " accessions: " + allMissing.toString());
			doNotFound.addAll(missingAccessions);
		} else {
			log.debug("No missing proteins. All were retrieved.");
		}
	}

	private Set<String> getMissingAccs(Map<String, ?> ret, Collection<String> accessions) {
		final Set<String> missingProteins = new THashSet<>();
		for (final String accession : accessions) {
			if (!ret.containsKey(accession))
				missingProteins.add(accession);
		}
		return missingProteins;
	}

	public static String getCurrentUniprotRemoteVersion() {
		// check if today the current uniprot version was already retrieved
		if (UniprotProteinRemoteRetriever.currentUniprotVersion != null
				&& DateUtils.isSameDay(UniprotProteinRemoteRetriever.currentUniprotVersionRetrievedDate, new Date())) {
			return UniprotProteinRemoteRetriever.currentUniprotVersion;
		}
		if (notTryUntilNextDay
				&& DateUtils.isSameDay(UniprotProteinRemoteRetriever.currentUniprotVersionRetrievedDate, new Date())) {
			log.debug("After a timeout today...I will try again tomorrow");
			return "";
		}
		final String releaseNotesURLString = PropertiesUtil.getInstance(PropertiesUtil.UNIPROT_PROPERTIES_FILE)
				.getPropertyValue(PropertiesUtil.UNIPROT_RELEASES_NOTES_PROP);
		log.info("Getting uniprot current release from " + releaseNotesURLString);
		if (releaseNotesURLString != null) {
			URL url;
			try {
				notTryUntilNextDay = false;
				url = new URL(releaseNotesURLString);

				final InputStream is = url.openStream();
				final BufferedReader br = new BufferedReader(new InputStreamReader(is));
				// get first line of the remote file as release notes
				String ret = br.readLine().trim();
				final String string = "Release ";
				if (ret.contains(string)) {
					ret = ret.substring(ret.indexOf(string) + string.length());
					// return until the first space
					if (ret.contains(" "))
						ret = ret.substring(0, ret.indexOf(" "));
				}
				log.debug("Current uniprot version is: " + ret);
				br.close();
				UniprotProteinRemoteRetriever.currentUniprotVersionRetrievedDate = new Date();
				UniprotProteinRemoteRetriever.currentUniprotVersion = ret;
				return ret;
			} catch (final MalformedURLException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				log.warn(e.getMessage());
				if (e instanceof ConnectException && e.getMessage().contains("Connection timed out: connect")) {
					UniprotProteinRemoteRetriever.currentUniprotVersionRetrievedDate = new Date();
					notTryUntilNextDay = true;
				} else {
					notTryUntilNextDay = false;
				}
			}
		}
		return "";
	}

	public Set<String> getMissingAccessions() {
		return missingAccessions;
	}

	public Map<String, Entry> getAnnotatedProtein(String uniprotVersion, String accession, UniprotEntryCache cache) {
		return getAnnotatedProtein(uniprotVersion, accession, null, cache);
	}

	public Map<String, Entry> getAnnotatedProtein(String uniprotVersion, String accession,
			UniprotXmlIndex uniprotLocalIndex, UniprotEntryCache cache) {
		final Set<String> accessions = new THashSet<>();
		accessions.add(accession);
		return getAnnotatedProteins(uniprotVersion, accessions, uniprotLocalIndex, cache);
	}

	public void setLookForIsoforms(boolean lookForIsoforms) {
		this.lookForIsoforms = lookForIsoforms;
	}

}
