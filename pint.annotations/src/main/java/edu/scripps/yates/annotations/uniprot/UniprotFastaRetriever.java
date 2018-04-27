package edu.scripps.yates.annotations.uniprot;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.dates.DatesUtil;

public class UniprotFastaRetriever {
	private final static Logger log = Logger.getLogger(UniprotFastaRetriever.class);

	/**
	 * Retrieves the fasta entry using the URL
	 * http://www.uniprot.org/uniprot/P12345.fasta
	 * 
	 * @param uniprotACC
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static Entry getFastaEntry(String uniprotACC) throws URISyntaxException, IOException, InterruptedException {

		final StringBuilder locationBuilder = new StringBuilder(
				"http://www.uniprot.org/uniprot/" + uniprotACC + ".fasta");
		String location = locationBuilder.toString();
		location = location.replace(" ", "%20");
		final URL url = new URL(location).toURI().toURL();
		log.info("Submitting " + locationBuilder + " from thread " + Thread.currentThread().getId() + "...");
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
			log.debug("Got a OK reply in " + DatesUtil.getDescriptiveTimeFromMillisecs(t2 - t1));
			final InputStream is = conn.getInputStream();
			URLConnection.guessContentTypeFromStream(is);
			final Entry fastaEntry = UniprotProteinRemoteRetriever.parseFASTAResponse(is, uniprotACC);
			if (fastaEntry != null) {
				return fastaEntry;
			} else {
				log.debug("Adding " + uniprotACC + " to the list of proteins with no FASTA sequence available.");
				UniprotProteinRemoteRetriever.entriesWithNoFASTA.add(uniprotACC);
			}
		}
		return null;

	}
}
