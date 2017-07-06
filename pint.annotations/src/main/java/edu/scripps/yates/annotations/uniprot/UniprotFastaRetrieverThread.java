package edu.scripps.yates.annotations.uniprot;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.pi.ParIterator;
import edu.scripps.yates.pi.reductions.Reducible;
import edu.scripps.yates.utilities.dates.DatesUtil;
import gnu.trove.map.hash.THashMap;

public class UniprotFastaRetrieverThread extends Thread {
	private static Logger log = Logger.getLogger(UniprotFastaRetrieverThread.class);
	private final Reducible<Map<String, Entry>> reducibleMap;
	private final ParIterator<String> iterator;

	public UniprotFastaRetrieverThread(ParIterator<String> iterator, Reducible<Map<String, Entry>> reducibleMap) {
		this.iterator = iterator;
		this.reducibleMap = reducibleMap;
	}

	@Override
	public void run() {
		Map<String, Entry> ret = new THashMap<String, Entry>();
		reducibleMap.set(ret);
		int num = 0;

		while (iterator.hasNext()) {
			String accession = iterator.next();
			try {
				if (UniprotProteinRemoteRetriever.entriesWithNoFASTA.contains(accession)) {
					continue;
				}
				StringBuilder locationBuilder = new StringBuilder(
						"http://www.uniprot.org/uniprot/" + accession + ".fasta");
				String location = locationBuilder.toString();
				location = location.replace(" ", "%20");
				URL url = new URL(location).toURI().toURL();
				log.info("Submitting " + locationBuilder + " from thread " + Thread.currentThread().getId() + "...");
				long t1 = System.currentTimeMillis();
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				HttpURLConnection.setFollowRedirects(true);
				conn.setDoInput(true);
				conn.connect();

				int status = conn.getResponseCode();
				while (true) {
					int wait = 0;
					String header = conn.getHeaderField("Retry-After");
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
					long t2 = System.currentTimeMillis();
					log.info("Got a OK reply in " + DatesUtil.getDescriptiveTimeFromMillisecs(t2 - t1) + " (protein "
							+ num++ + ") in thread " + Thread.currentThread().getId());
					InputStream is = conn.getInputStream();
					URLConnection.guessContentTypeFromStream(is);
					final Entry fastaEntry = UniprotProteinRemoteRetriever.parseFASTAResponse(is, accession);
					if (fastaEntry != null) {
						ret.put(accession, fastaEntry);
					} else {
						log.info("Adding " + accession + " to the list of proteins with no FASTA sequence available.");
						UniprotProteinRemoteRetriever.entriesWithNoFASTA.add(accession);
					}
				} else {
					log.warn("Failed, got " + conn.getResponseMessage() + " for " + location);
				}
				conn.disconnect();

			} catch (MalformedURLException e) {
				e.printStackTrace();
				log.warn(e.getMessage());
				iterator.register(e);
			} catch (IOException e) {
				e.printStackTrace();
				log.warn(e.getMessage());
				iterator.register(e);
			} catch (InterruptedException e) {
				e.printStackTrace();
				log.warn(e.getMessage());
				iterator.register(e);
			} catch (URISyntaxException e) {
				e.printStackTrace();
				log.warn(e.getMessage());
				iterator.register(e);
			}
		}

	}
}
