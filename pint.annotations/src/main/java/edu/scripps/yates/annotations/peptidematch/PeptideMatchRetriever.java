package edu.scripps.yates.annotations.peptidematch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.peptidematch.xml.PeptideMatch;

public class PeptideMatchRetriever {
	private static final String PEPTIDEMATCH_SERVER = "http://research.bioinformatics.udel.edu/peptidematch/webservices/peptidematch_rest";
	private static final Logger log = Logger.getLogger(PeptideMatchRetriever.class);
	private static JAXBContext jaxbContext;

	static {
		if (jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance(PeptideMatch.class);
			} catch (JAXBException e) {
				e.printStackTrace();

			}
		}
	}

	private PeptideMatch run(ParameterNameValue[] params) throws Exception {
		StringBuilder locationBuilder = new StringBuilder(PEPTIDEMATCH_SERVER + "?");
		for (int i = 0; i < params.length; i++) {
			if (i > 0)
				locationBuilder.append('&');
			locationBuilder.append(params[i].name).append('=').append(params[i].value);
		}
		String location = locationBuilder.toString();
		URL url = new URL(location);
		log.info("Submitting...");
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
			log.info("Got a OK reply");
			InputStream reader = conn.getInputStream();
			final String contentType = URLConnection.guessContentTypeFromStream(reader);
			log.info("Content type: " + contentType);
			final File tmpFile = File.createTempFile("peptideMatch", "xml");
			tmpFile.deleteOnExit();
			FileWriter fw = new FileWriter(tmpFile);
			// StringBuilder builder = new StringBuilder();
			int a = 0;
			while ((a = reader.read()) != -1) {
				fw.write((char) a);
			}
			fw.close();
			final File tmpFile2 = File.createTempFile("peptideMatch2", "xml");
			tmpFile2.deleteOnExit();
			FileWriter fw2 = new FileWriter(tmpFile2);
			FileInputStream fis = new FileInputStream(tmpFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.trim().startsWith("<peptideMatch")) {
					line = "<peptideMatch>";
				}
				fw2.write(line + "\n");
			}
			fw2.close();
			br.close();
			// System.out.println(builder.toString());

			final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			final PeptideMatch peptideMatch = (PeptideMatch) unmarshaller.unmarshal(tmpFile2);
			return peptideMatch;
		} else {
			log.error("Failed, got " + conn.getResponseMessage() + " for " + location);
		}
		conn.disconnect();
		return null;
	}

	public static void main(String[] args) throws Exception {
		PeptideMatchRetriever pmr = new PeptideMatchRetriever();
		final PeptideMatch ret = pmr.run("MITSAAGIISLLDEDEPQLK,MILLEVNNR", "9606,10090");
		System.out.println(ret.getMatchPerPeptide().getNumberOfMatchedProteins() + " proteins");

	}

	public PeptideMatch run(String peptide, String organism) {
		try {
			ParameterNameValue[] params = new ParameterNameValue[] { new ParameterNameValue("format", "xml"),
					new ParameterNameValue("query", peptide), new ParameterNameValue("organism", organism) };
			return run(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static class ParameterNameValue {
		private final String name;
		private final String value;

		public ParameterNameValue(String name, String value) throws UnsupportedEncodingException {
			this.name = URLEncoder.encode(name, "UTF-8");
			this.value = URLEncoder.encode(value, "UTF-8");
		}
	}
}
