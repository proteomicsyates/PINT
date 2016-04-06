package edu.scripps.yates.annotations.omim;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.omim.xml.Omim;
import edu.scripps.yates.annotations.omim.xml.Omim.EntryList;
import edu.scripps.yates.annotations.omim.xml.Omim.EntryList.Entry;
import edu.scripps.yates.annotations.omim.xml.Omim.EntryList.Entry.Titles;
import edu.scripps.yates.annotations.util.PropertiesUtil;

public class OmimRetriever {
	private final static Logger log = Logger.getLogger(OmimRetriever.class);
	private static final PropertiesUtil omimProps = PropertiesUtil.getInstance(PropertiesUtil.OMIM_PROPERTIES_FILE);
	private static JAXBContext jaxbContext;
	private final static int MAX_NUM_ENTRIES_PER_QUERY = 100;

	public static Map<Integer, OmimEntry> retrieveOmimEntries(String omimAPIKey, Collection<Integer> omimIDs) {
		log.info("Retrieving " + omimIDs.size() + " OMIM entries");
		if (jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance(Omim.class);
			} catch (JAXBException e) {
				e.printStackTrace();

			}
		}
		String omimUrl = omimProps.getPropertyValue(PropertiesUtil.OMIM_URL_PROP);
		if (omimUrl == null || "".equals(omimUrl)) {
			omimUrl = "http://api.omim.org";
		}
		String omimEntryParam = omimProps.getPropertyValue(PropertiesUtil.OMIM_ENTRY_PROP);
		if (omimEntryParam == null || "".equals(omimEntryParam)) {
			omimEntryParam = "/api/entry";
		}
		Map<Integer, OmimEntry> ret = new HashMap<Integer, OmimEntry>();

		int numOmimIDs = 0;
		final Iterator<Integer> omimIDIterator = omimIDs.iterator();
		while (numOmimIDs < omimIDs.size()) {
			Set<Integer> queryIDs = new HashSet<Integer>();
			for (int i = 0; i < MAX_NUM_ENTRIES_PER_QUERY; i++) {
				if (omimIDIterator.hasNext()) {
					queryIDs.add(omimIDIterator.next());
					numOmimIDs++;
				} else {
					break;
				}
			}
			String url = omimUrl + omimEntryParam + "?mimNumber=";
			int numId = 1;
			for (Integer omimID : queryIDs) {
				if (numId > 1) {
					url += ",";
				}
				url += omimID;
				numId++;
			}
			url += "&format=xml&&apiKey=" + omimAPIKey;
			EntryList entryList = sendRequest(url);
			if (entryList != null && entryList.getEntry() != null) {
				for (Entry entry : entryList.getEntry()) {
					OmimEntry omimEntry = new OmimEntry(entry.getMimNumber());
					final Titles titles = entry.getTitles();
					if (titles != null) {
						final String preferredTitle = titles.getPreferredTitle();
						if (preferredTitle != null) {
							omimEntry.setPreferredTitle(preferredTitle);
						}
						final String alternativeTitles = titles.getAlternativeTitles();
						if (alternativeTitles != null) {
							if (alternativeTitles.contains(";")) {
								final String[] split = alternativeTitles.split(";");
								for (String string : split) {
									string = string.trim();
									if (!"".equals(string)) {
										omimEntry.getAlternativeTitles().add(string);
									}
								}
							} else {
								omimEntry.getAlternativeTitles().add(alternativeTitles.trim());
							}
						}
					}

					ret.put(entry.getMimNumber(), omimEntry);
				}
			}
		}
		return ret;

	}

	public static Map<String, List<OmimEntry>> getAssociatedOmimEntries(String omimAPIKey,
			Collection<String> uniprotAccs) {
		Map<String, List<OmimEntry>> ret = new HashMap<String, List<OmimEntry>>();
		Omim2UniprotIDMap omimToUniprotMap = new Omim2UniprotIDMap();

		final Map<String, Set<Integer>> uniprot2OmimMap = omimToUniprotMap.uniprot2Omim(uniprotAccs);
		Set<Integer> omimIDs = new HashSet<Integer>();
		for (Set<Integer> omimIDSet : uniprot2OmimMap.values()) {
			omimIDs.addAll(omimIDSet);
		}
		// get all omimEntries at the same time faster than one by one
		final Map<Integer, OmimEntry> omimDiseaseNamesByOmimID = retrieveOmimEntries(omimAPIKey, omimIDs);
		for (String uniprotAcc : uniprotAccs) {
			final Set<Integer> omimIDSet = uniprot2OmimMap.get(uniprotAcc);
			if (omimIDSet != null) {
				for (Integer omimID : omimIDSet) {
					final OmimEntry omimEntry = omimDiseaseNamesByOmimID.get(omimID);
					if (omimEntry != null) {
						if (ret.containsKey(uniprotAcc)) {
							ret.get(uniprotAcc).add(omimEntry);
						} else {
							List<OmimEntry> list = new ArrayList<OmimEntry>();
							list.add(omimEntry);
							ret.put(uniprotAcc, list);
						}
					}
				}
			}
		}
		return ret;
	}

	private static EntryList sendRequest(String urlString) {

		try {
			log.info("Submitting String= " + urlString + "...");
			URL url = new URL(urlString).toURI().toURL();
			log.info("Submitting URL= " + url + "...");
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
				conn = (HttpURLConnection) new URL(urlString).openConnection();
				conn.setDoInput(true);
				conn.connect();
				status = conn.getResponseCode();
			}
			if (status == HttpURLConnection.HTTP_OK) {
				long t2 = System.currentTimeMillis();
				log.info("Got a OK reply in " + (t2 - t1) * 1.0 / 1000 + "sg");
				InputStream is = conn.getInputStream();
				URLConnection.guessContentTypeFromStream(is);
				final Omim response = parseResponse(is);
				return response.getEntryList();
			} else
				log.error("Failed, got " + conn.getResponseMessage() + " for " + urlString);
			conn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		}
		return null;
	}

	private static Omim parseResponse(InputStream is) {

		log.debug("Processing response");
		OutputStream outputStream = null;
		try {

			final File createTempFile = File.createTempFile("omim", "xml");
			createTempFile.deleteOnExit();
			// read this file into InputStream

			// write the inputStream to a FileOutputStream
			outputStream = new FileOutputStream(createTempFile);

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = is.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

			System.out.println("Done!");
			outputStream.close();
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			Omim omim = (Omim) unmarshaller.unmarshal(createTempFile);

			log.debug("Response parsed succesfully");
			log.debug(omim.getEntryList().getEntry().size() + " entries");
			return omim;
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					// outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		return null;
	}
}
