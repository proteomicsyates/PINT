package edu.scripps.yates.annotations.omim;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.omim.xml.Omim;
import edu.scripps.yates.annotations.omim.xml.Omim.EntryList;
import edu.scripps.yates.annotations.omim.xml.Omim.EntryList.Entry;
import edu.scripps.yates.annotations.omim.xml.Omim.EntryList.Entry.Titles;
import edu.scripps.yates.annotations.util.PropertiesUtil;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.TIntHashSet;

public class OmimRetriever {
	private final static Logger log = Logger.getLogger(OmimRetriever.class);
	private static final PropertiesUtil omimProps = PropertiesUtil.getInstance(PropertiesUtil.OMIM_PROPERTIES_FILE);
	private static JAXBContext jaxbContext;
	private final static int MAX_NUM_ENTRIES_PER_QUERY = 100;

	public static JAXBContext getJAXBContext() {
		if (jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance(Omim.class);
			} catch (JAXBException e) {
				e.printStackTrace();

			}
		}
		return jaxbContext;
	}

	public static TIntObjectHashMap<OmimEntry> retrieveOmimEntries(String omimAPIKey, Collection<Integer> omimIDs) {
		TIntHashSet set = new TIntHashSet(omimIDs);

		return retrieveOmimEntries(omimAPIKey, set);
	}

	public static TIntObjectHashMap<OmimEntry> retrieveOmimEntries(String omimAPIKey, int[] omimIDs) {
		List<Integer> list = new ArrayList<Integer>();
		for (Integer omimID : omimIDs) {
			list.add(omimID);
		}
		return retrieveOmimEntries(omimAPIKey, list);
	}

	public static TIntObjectHashMap<OmimEntry> retrieveOmimEntries(String omimAPIKey, TIntHashSet omimIDs) {

		log.info("Retrieving " + omimIDs.size() + " OMIM entries");

		String omimUrl = omimProps.getPropertyValue(PropertiesUtil.OMIM_URL_PROP);
		if (omimUrl == null || "".equals(omimUrl)) {
			omimUrl = "http://api.omim.org";
		}
		String omimEntryParam = omimProps.getPropertyValue(PropertiesUtil.OMIM_ENTRY_PROP);
		if (omimEntryParam == null || "".equals(omimEntryParam)) {
			omimEntryParam = "/api/entry";
		}
		TIntObjectHashMap<OmimEntry> ret = new TIntObjectHashMap<OmimEntry>();

		int numOmimIDs = 0;
		TIntIterator omimIDIterator = omimIDs.iterator();
		while (numOmimIDs < omimIDs.size()) {
			if (Thread.currentThread().isInterrupted()) {
				throw new RuntimeException("Thread interrupted.");
			}

			TIntHashSet queryIDs = new TIntHashSet();
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
			TIntIterator iterator = queryIDs.iterator();
			while (iterator.hasNext()) {
				int omimID = iterator.next();
				if (numId > 1) {
					url += ",";
				}
				url += omimID;
				numId++;
			}
			url += "&format=xml&apiKey=" + omimAPIKey;
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
		Map<String, List<OmimEntry>> ret = new THashMap<String, List<OmimEntry>>();
		Omim2UniprotIDMap omimToUniprotMap = new Omim2UniprotIDMap();

		final Map<String, TIntHashSet> uniprot2OmimMap = omimToUniprotMap.uniprot2Omim(uniprotAccs);
		TIntHashSet omimIDs = new TIntHashSet();
		for (TIntHashSet omimIDSet : uniprot2OmimMap.values()) {
			omimIDs.addAll(omimIDSet);
		}
		// get all omimEntries at the same time faster than one by one
		final TIntObjectHashMap<OmimEntry> omimDiseaseNamesByOmimID = retrieveOmimEntries(omimAPIKey, omimIDs._set);
		for (String uniprotAcc : uniprotAccs) {
			final TIntHashSet omimIDSet = uniprot2OmimMap.get(uniprotAcc);
			if (omimIDSet != null) {
				TIntIterator iterator = omimIDSet.iterator();
				while (iterator.hasNext()) {
					int omimID = iterator.next();
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
		HttpURLConnection conn = null;
		try {
			log.info("Submitting String= " + urlString + "...");
			URL url = new URL(urlString).toURI().toURL();
			log.info("Submitting URL= " + url + "...");
			long t1 = System.currentTimeMillis();
			conn = (HttpURLConnection) url.openConnection();
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
				if (response != null) {
					return response.getEntryList();
				}
				return null;
			} else {
				log.error("Failed, got " + conn.getResponseMessage() + " for " + urlString);
			}
			conn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return null;
	}

	private static Omim parseResponse(InputStream is) {

		log.debug("Processing response");

		try {

			Unmarshaller unmarshaller = getJAXBContext().createUnmarshaller();
			Omim omim = (Omim) unmarshaller.unmarshal(is);

			log.debug("Response parsed succesfully");
			if (omim.getEntryList() != null) {
				log.debug(omim.getEntryList().getEntry().size() + " entries");
			}
			return omim;
		} catch (JAXBException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return null;
	}

	/**
	 * Throws
	 * 
	 * @param omimKey
	 */
	public static void checkOMIMKey(String omimKey) throws OmimException {
		String url = "http://api.omim.org/api/entry?mimNumber=100100&apiKey=" + omimKey;
		EntryList sendRequest = sendRequest(url);
		if (sendRequest == null) {
			String message = "OMIM API Key '" + omimKey + "' is invalid";
			log.error(message);
			throw new OmimException(message);
		}
	}
}
