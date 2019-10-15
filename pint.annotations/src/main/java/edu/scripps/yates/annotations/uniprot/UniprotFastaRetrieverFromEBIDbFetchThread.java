package edu.scripps.yates.annotations.uniprot;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;

import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.dates.DatesUtil;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.http.ThreadSafeHttpClient;
import edu.scripps.yates.utilities.pi.ParIterator;
import edu.scripps.yates.utilities.pi.reductions.Reducible;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class UniprotFastaRetrieverFromEBIDbFetchThread extends Thread {
	private static final int LOG_SIZE_CHUNK = 200;
	private static Logger log = Logger.getLogger(UniprotFastaRetrieverFromEBIDbFetchThread.class);
	private final Reducible<Map<String, Entry>> reducibleMap;
	private final ParIterator<String> iterator;
	private final CloseableHttpClient httpClient;
	private int totalSubmitted = 0;
	private int totalFound = 0;
	private final int identifier;

	public UniprotFastaRetrieverFromEBIDbFetchThread(int id, ParIterator<String> iterator,
			Reducible<Map<String, Entry>> reducibleMap, CloseableHttpClient httpClient) {
		identifier = id;
		this.iterator = iterator;
		this.reducibleMap = reducibleMap;
		if (httpClient != null) {
			this.httpClient = httpClient;
		} else {
			this.httpClient = ThreadSafeHttpClient.createNewHttpClient();
		}
	}

	@Override
	public void run() {
		final Map<String, Entry> ret = new THashMap<String, Entry>();
		reducibleMap.set(ret);
		int count = 0;
		final int chunkSize = UniprotEntryRetrieverThread.MAX_NUM_TO_RETRIEVE; // defined by EBI
		final List<String> chunkToRetrieve = new ArrayList<String>();
		while (iterator.hasNext()) {
			final String accession = iterator.next();
			try {
				if (UniprotProteinRemoteRetriever.entriesWithNoFASTA.contains(accession)) {
					continue;
				}
				chunkToRetrieve.add(accession);
				if (chunkToRetrieve.size() == chunkSize) {
					makeRequestToGetFastaFromEBIDBFetch(chunkToRetrieve, ret);
					chunkToRetrieve.clear();
				}
			} finally {
				count++;
				if (count > 1 && count % LOG_SIZE_CHUNK == 0) {
					log.debug(ret.size() + " protein sequences retrieved in thread " + Thread.currentThread().getId());
				}
			}
		}
		if (!chunkToRetrieve.isEmpty()) {
			makeRequestToGetFastaFromEBIDBFetch(chunkToRetrieve, ret);
		}
		log.info(ret.size() + " sequences from thread " + identifier);
		if (ret.size() > 1) {
			log.debug(ret.size() + " protein sequences retrieved in thread " + Thread.currentThread().getId());
		}

	}

	private void makeRequestToGetFastaFromEBIDBFetch(List<String> accs, Map<String, Entry> ret) {
		try {
			final Set<String> foundSet = new THashSet<String>();
			final StringBuilder accListString = new StringBuilder();
			for (int i = 0; i < accs.size(); i++) {
				if (!"".equals(accListString.toString())) {
					accListString.append(',');
				}
				accListString.append(accs.get(i));
			}

			final URI uri = new URIBuilder().setScheme("http").setHost(UniprotEntryRetrieverThread.UNIPROT_EBI_SERVER)
					.setPath("/Tools/dbfetch/dbfetch").setParameter("db", "uniprotkb").setParameter("format", "fasta")
					.setParameter("style", "raw").setParameter("id", accListString.toString()).build();
			log.debug(uri);
			if (accs.size() > 1) {
				log.debug("From thread (" + identifier + ") submitting " + accs.size() + " (" + totalFound + "/"
						+ totalSubmitted + " found for now)");
			}
			final long t1 = System.currentTimeMillis();
			final String is = sendRequestWithThreadSafeHttpClient(uri);
			if (is != null) {
				final long t2 = System.currentTimeMillis();
				log.debug("Got a OK reply in " + DatesUtil.getDescriptiveTimeFromMillisecs(t2 - t1));
				// final String guessContentTypeFromStream =
				// URLConnection.guessContentTypeFromStream(is);
				// log.info(guessContentTypeFromStream + " received");

				final Map<String, Entry> fastas = parseFASTAResponse(is);
				// check the accs that are not in the respose and add them to the
				// entriesWithNoFASTA
				for (final String acc : accs) {
					if (!fastas.containsKey(acc)) {
						log.debug("Adding " + acc + " to the list of proteins with no FASTA sequence available.");
						UniprotProteinRemoteRetriever.entriesWithNoFASTA.add(acc);
					}
				}
				final long t3 = System.currentTimeMillis();
				log.debug("Response parsed in " + DatesUtil.getDescriptiveTimeFromMillisecs(t3 - t2));
				totalSubmitted += accs.size();
				ret.putAll(fastas);
			}
			totalFound += foundSet.size();
			log.debug(totalFound + " found out of " + totalSubmitted + " total submitted in thread " + identifier);
		} catch (final URISyntaxException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		} catch (final UnsupportedOperationException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		} catch (final IOException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		} catch (final Exception e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		}

	}

	private static Map<String, Entry> parseFASTAResponse(String myString) {
		final Map<String, Entry> ret = new THashMap<String, Entry>();
		final String[] split = myString.split("\n");
		StringBuilder sequence = new StringBuilder();
		String fastaHeader = null;
		String accession = null;
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
					final Entry entry = new UniprotEntryAdapterFromFASTA(accession, fastaHeader, sequence.toString())
							.adapt();
					ret.put(accession, entry);
				}
				fastaHeader = split[i];

				final Accession newAccession = FastaParser.getACC(fastaHeader);
				final String isoformVersion = FastaParser.getIsoformVersion(newAccession.getAccession());
				// transform this:
				// >SP:Q00005-2 Q00005 Isoform 2 of Serine/t
				// to:
				// >SP|Q00005-2| Q00005 Isoform 2 of Serine/t
				if (isoformVersion != null && Integer.valueOf(isoformVersion) > 1) {
					if (fastaHeader.toLowerCase().startsWith(">sp:") || fastaHeader.toLowerCase().startsWith(">tr:")) {
						fastaHeader = fastaHeader.substring(0, 3) + "|" + newAccession + "|"
								+ fastaHeader.substring(fastaHeader.indexOf(" "));
					}
				}
				sequence = new StringBuilder();
				if (!newAccession.getAccession().equals(accession)) {
					takeSequence = true;
				}
				accession = newAccession.getAccession();
			} else {
				sequence.append(split[i]);
			}
		}

		if ((numHeaders == 1 || takeSequence) && !"".equals(sequence.toString())) {
			final Entry entry = new UniprotEntryAdapterFromFASTA(accession, fastaHeader, sequence.toString()).adapt();
			ret.put(accession, entry);
		}
		log.debug("Parsing " + myString + " yielded " + ret.size() + " sequences");
		return ret;
	}

	private String sendRequestWithThreadSafeHttpClient(URI uri) throws UnsupportedOperationException, IOException {
		final String response = ThreadSafeHttpClient.getStringResponse(httpClient, new HttpGet(uri));
		return response;
	}

}
