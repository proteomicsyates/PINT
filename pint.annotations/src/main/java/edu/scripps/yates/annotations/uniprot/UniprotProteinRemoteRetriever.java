package edu.scripps.yates.annotations.uniprot;

import java.io.BufferedReader;
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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.annotations.uniprot.xml.Uniprot;
import edu.scripps.yates.annotations.util.PropertiesUtil;
import edu.scripps.yates.annotations.util.UniprotEntryCache;
import edu.scripps.yates.utilities.cores.SystemCoreManager;
import edu.scripps.yates.utilities.dates.DatesUtil;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.http.ThreadSafeHttpClient;
import edu.scripps.yates.utilities.pi.ParIterator;
import edu.scripps.yates.utilities.pi.ParIterator.Schedule;
import edu.scripps.yates.utilities.pi.ParIteratorFactory;
import edu.scripps.yates.utilities.pi.exceptions.ParIteratorException;
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

	private static final String UNIPROT_SERVER = PropertiesUtil.getInstance(PropertiesUtil.UNIPROT_PROPERTIES_FILE)
			.getPropertyValue(PropertiesUtil.UNIPROT_SERVER_PROP);
	private static final Logger log = Logger.getLogger(UniprotProteinRemoteRetriever.class);
	// private static final int MAX_NUM_TO_RETRIEVE = 200; // defined by EBI
	// fetch

	private Set<String> missingAccessions = new THashSet<>();
	private boolean lookForIsoforms = false;
	// private Unmarshaller unmarshaller;
	private JAXBContext jaxbContext;
	private final static Set<String> doNotFound = new THashSet<>();

	private static Date currentUniprotVersionRetrievedDate;
	private static String currentUniprotVersion;
	private static boolean notTryUntilNextDay;
	protected static final Set<String> entriesWithNoFASTA = Collections.synchronizedSet(new THashSet<>());
	protected static final Set<String> entriesWithNoInfo = Collections.synchronizedSet(new THashSet<>());

	// service

	public Uniprot getProteins(Collection<String> accessions, String uniprotVersion, UniprotEntryCache cache) {
		if (accessions == null || accessions.isEmpty()) {
			return new Uniprot();
		}
		final Map<String, Entry> entryMap = new THashMap<>();
		final List<String> noIsoformList = new ArrayList<>();
		final Set<String> isoformList = new THashSet<>();
		final ProgressCounter counter = new ProgressCounter(accessions.size(), ProgressPrintingType.PERCENTAGE_STEPS,
				0);
		if (accessions.size() > 1) {
			log.info("Looking for " + accessions.size() + " proteins in the index of annotations");
		}
		for (final String acc : accessions) {
			if (entriesWithNoInfo.contains(acc)) {
				// do not try again
				continue;
			}
			counter.increment();
			final String printIfNecessary = counter.printIfNecessary();
			if (accessions.size() > 1 && !"".equals(printIfNecessary)) {
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
			if (isoformVersion != null) {
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

			} else {
				if (!noIsoformList.contains(noIsoformAccession)) {
					noIsoformList.add(noIsoformAccession);
				}
			}
		}
		final Map<String, Entry> uniprotEntriesInParallel = getUniprotEntriesInParallel(noIsoformList);
		entryMap.putAll(uniprotEntriesInParallel);

		// now, if there are isoforms, look for the protein sequences of them
		if (lookForIsoforms) {
			final Map<String, Entry> fastaEntries = getFASTASequencesInParallel(isoformList);

			// store it with the isoform acc
			entryMap.putAll(fastaEntries);

			log.debug("Response parsed succesfully");

		}
		final Uniprot uniprot = new Uniprot();
		uniprot.getEntry().addAll(entryMap.values());

		// iterate over the accessions and store the ones not retrieved
		for (final String acc : accessions) {
			if (!entryMap.containsKey(acc)) {
				entriesWithNoInfo.add(acc);
			}
		}
		return uniprot;
	}

	public static Map<String, Entry> getUniprotEntriesInParallel(List<String> accessions) {
		if (accessions == null || accessions.isEmpty()) {
			return Collections.emptyMap();
		}
		final int threadCount = Math.min(SystemCoreManager.getAvailableNumSystemCores(), accessions.size());
		// threadCount = 2;
		log.info("getting Uniprot entries of " + accessions.size() + " proteins in parallel using " + threadCount
				+ " threads...");
		final ParIterator<String> iterator = ParIteratorFactory.createParIterator(accessions, threadCount,
				Schedule.GUIDED);
		final Reducible<Map<String, Entry>> reducibleEntryMap = new Reducible<>();
		final List<UniprotEntryRetrieverThread> runners = new ArrayList<>();
		final CloseableHttpClient httpClient = ThreadSafeHttpClient.createHttpClient();
		for (int numCore = 0; numCore < threadCount; numCore++) {
			// take current DB session
			final UniprotEntryRetrieverThread runner = new UniprotEntryRetrieverThread(numCore + 1, iterator,
					reducibleEntryMap, httpClient);
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
		// close httpClient
		try {
			httpClient.close();
		} catch (final IOException e1) {
			e1.printStackTrace();
			log.warn("Error closing HTTP client");
		}
		// Handle exceptions
		final ParIteratorException<String>[] piExceptions = iterator.getAllExceptions();
		final StringBuilder errorMessage = new StringBuilder();
		for (int k = 0; k < piExceptions.length; k++) {
			final ParIteratorException<String> pie = piExceptions[k];
			// object for iteration in which exception was encountered
			final String iteration = pie.getIteration();
			// thread executing that iteration
			final Thread thread = pie.getRegisteringThread();
			// actual exception thrown
			final Exception e = pie.getException();
			// print exact location of exception
			e.printStackTrace();
			errorMessage.append("Error when trying to retrieve " + iteration + " from uniprot in thread "
					+ thread.getId() + ": " + e.getMessage() + "\n");
		}
		if (!"".equals(errorMessage.toString())) {
			throw new IllegalArgumentException(errorMessage.toString());
		}
		// reduction
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
		if (!ret.isEmpty()) {
			log.info("Retrieved " + ret.size() + " uniprot entries in paralell");
		}
		return ret;

	}

	public List<Entry> getProteins(String queryString) {

		try {
			// if (unmarshaller == null) {
			//
			// jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			// unmarshaller = jaxbContext.createUnmarshaller();
			//
			// }
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
				final List<Entry> response = new UniprotEntryRetrieverThread().parseResponse(IOUtils.toString(is));
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
		} catch (final JAXBException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		}
		return null;
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
		log.debug("getting fasta sequences of " + accessions.size() + " proteins in parallel using " + threadCount
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
		if (!ret.isEmpty() && ret.size() > 10) {
			log.info("Retrieved " + ret.size() + " FASTA entries in paralell");
		}
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
			int numHeaders = 0;
			for (int i = 0; i < split.length; i++) {
				if (split[i].startsWith(">")) {
					numHeaders++;
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

			if ((numHeaders == 1 || takeSequence) && !"".equals(sequence.toString())) {
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
		final UniprotProteinRemoteRetriever uprr = new UniprotProteinRemoteRetriever();
		uprr.setLookForIsoforms(true);
		final Uniprot uniprot = uprr.getProteins(set, UniprotProteinRemoteRetriever.getCurrentUniprotRemoteVersion(),
				null);
		for (final Entry entry2 : uniprot.getEntry()) {
			final List<String> names = entry2.getName();
			for (final String string : names) {
				System.out.println(string);
			}
		}
	}

	public synchronized Map<String, Entry> getAnnotatedProteins(String uniprotVersion, Collection<String> accessions,
			UniprotEntryCache cache) throws IllegalArgumentException {
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
			final Uniprot proteins = getProteins(toSearch, uniprotVersion, cache);

			final long t2 = System.currentTimeMillis();

			log.debug(proteins.getEntry().size() + " accessions retrieved in " + (t2 - t1) / 1000 + "sg");

			// long t3 = System.currentTimeMillis();
			final Map<String, Entry> map = new THashMap<>();
			UniprotProteinLocalRetriever.addEntriesToMap(map, proteins.getEntry());
			checkIfSomeProteinIsMissing(toSearch, map, uniprotVersion, cache);

			return map;
		}
		throw new IllegalArgumentException("Current version of uniprot different from provided: " + uniprotVersion
				+ " vs " + currentUniprotRemoteVersion);

	}

	private synchronized void checkIfSomeProteinIsMissing(Collection<String> accessions, Map<String, Entry> retrieved,
			String uniprotVersion, UniprotEntryCache cache) {
		final int numBeforeRecovery = retrieved.size();
		final Set<String> missingProteins = getMissingAccs(retrieved, accessions);
		if (!missingProteins.isEmpty()) {
			log.debug("Trying to recover " + missingProteins.size() + " proteins");

			final Uniprot proteins = getProteins(missingProteins, uniprotVersion, cache);
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

		if (releaseNotesURLString != null) {
			log.info("Getting uniprot current release from " + releaseNotesURLString);
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
				log.info("Current uniprot version is: " + ret);
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
		final Set<String> accessions = new THashSet<>();
		accessions.add(accession);
		return getAnnotatedProteins(uniprotVersion, accessions, cache);
	}

	public void setLookForIsoforms(boolean lookForIsoforms) {
		this.lookForIsoforms = lookForIsoforms;
	}

}
