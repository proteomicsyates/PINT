package edu.scripps.yates.annotations.uniprot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.annotations.uniprot.xml.Uniprot;
import edu.scripps.yates.annotations.util.PropertiesUtil;

/**
 * This class retrieves the protein information from uniprot
 *
 * @author Salva
 *
 */
public class UniprotProteinRemoteRetriever implements UniprotRetriever {
	private static final String UNIPROT_EBI_SERVER = PropertiesUtil.getInstance(PropertiesUtil.UNIPROT_PROPERTIES_FILE)
			.getPropertyValue(PropertiesUtil.UNIPROT_EBI_SERVER_PROP);
	private static final String UNIPROT_SERVER = PropertiesUtil.getInstance(PropertiesUtil.UNIPROT_PROPERTIES_FILE)
			.getPropertyValue(PropertiesUtil.UNIPROT_SERVER_PROP);
	private static final Logger log = Logger.getLogger(UniprotProteinRemoteRetriever.class);
	private static final int MAX_NUM_TO_RETRIEVE = 200; // defined by EBI fetch
	private Set<String> missingAccessions = new HashSet<String>();
	private final File uniprotReleaseFolder;
	private final boolean useIndex;
	private static JAXBContext jaxbContext;
	private static Date currentUniprotVersionRetrievedDate;
	private static String currentUniprotVersion;

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
			} catch (JAXBException e) {
				e.printStackTrace();

			}
		}
		// only accessible by local retriever
		this.uniprotReleaseFolder = uniprotReleaseFolder;
		this.useIndex = useIndex;
	}

	public Uniprot getProteins(Collection<String> accessions, String uniprotVersion) {
		Uniprot uniprot = new Uniprot();

		List<String> list = new ArrayList<String>();
		for (String acc : accessions) {
			if (acc.toLowerCase().contains("reverse")) {
				continue;
			}
			list.add(acc);
		}

		Set<String> accessionsSent = new HashSet<String>();
		try {
			int totalNumAccs = 0;
			while (totalNumAccs < list.size()) {
				StringBuilder locationBuilder = new StringBuilder(UNIPROT_EBI_SERVER + "&format=xml&id=");
				int numAccs = 0;
				while (totalNumAccs < list.size()) {
					if (numAccs > 0)
						locationBuilder.append(',');
					locationBuilder.append(list.get(totalNumAccs));
					accessionsSent.add(list.get(totalNumAccs));
					numAccs++;
					totalNumAccs++;
					if (numAccs == MAX_NUM_TO_RETRIEVE)
						break;
				}

				String location = locationBuilder.toString();
				URL url = new URL(location).toURI().toURL();
				log.info("Submitting " + numAccs + " (" + totalNumAccs + "/" + list.size() + ") at '" + location
						+ "'...");
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
					log.info("Got a OK reply in " + (t2 - t1) / 1000 + "sg");
					InputStream is = conn.getInputStream();
					URLConnection.guessContentTypeFromStream(is);
					final List<Entry> entries = parseResponse(is, uniprotVersion, accessionsSent);
					uniprot.getEntry().addAll(entries);
				} else
					log.error("Failed, got " + conn.getResponseMessage() + " for " + location);
				conn.disconnect();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		}
		return uniprot;
	}

	public Uniprot getProteins(String queryString) {

		try {
			Set<String> accessionsSent = new HashSet<String>();
			accessionsSent.add(queryString);
			StringBuilder locationBuilder = new StringBuilder(UNIPROT_SERVER + queryString + "&format=xml");

			String location = locationBuilder.toString();
			location = location.replace(" ", "%20");
			URL url = new URL(location).toURI().toURL();
			log.info("Submitting " + locationBuilder + "...");
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
				log.info("Got a OK reply in " + (t2 - t1) / 1000 + "sg");
				InputStream is = conn.getInputStream();
				URLConnection.guessContentTypeFromStream(is);
				final Uniprot response = parseResponse(is, accessionsSent);
				return response;
			} else
				log.error("Failed, got " + conn.getResponseMessage() + " for " + location);
			conn.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		}
		return null;
	}

	private List<Entry> parseResponse(InputStream is, String uniprotVersion, Set<String> accessionsSent) {

		final Uniprot uniprot = parseResponse(is, accessionsSent);
		if (uniprot != null) {
			// call to the UniprotLocalRetriever to save into the file
			// system
			try {
				if (uniprotReleaseFolder != null) {
					UniprotProteinLocalRetriever uplr = new UniprotProteinLocalRetriever(uniprotReleaseFolder,
							useIndex);
					uplr.saveUniprotToLocalFilesystem(uniprot, uniprotVersion, useIndex);
				}
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			log.debug("Response parsed succesfully");
			return uniprot.getEntry();
		} else {
			return new ArrayList<Entry>();
		}

	}

	private Uniprot parseResponse(InputStream is, Set<String> accessionsSent) {

		log.debug("Processing response");
		OutputStream outputStream = null;
		try {

			final File createTempFile = File.createTempFile("uniprot", "xml");
			createTempFile.deleteOnExit();
			// read this file into InputStream

			// write the inputStream to a FileOutputStream
			outputStream = new FileOutputStream(createTempFile);

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = is.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

			outputStream.close();
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			Uniprot uniprot = (Uniprot) unmarshaller.unmarshal(createTempFile);

			log.debug("Response parsed succesfully");
			log.debug(uniprot.getEntry().size() + " entries");
			return uniprot;
		} catch (JAXBException e) {
			// e.printStackTrace();
			log.warn(e.getMessage() + "\t" + e.getLinkedException().getMessage());
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
		return getRDFEntries(accessionsSent);
	}

	private Uniprot getRDFEntries(Set<String> accessionsSent) {
		Uniprot ret = new Uniprot();

		for (String accession : accessionsSent) {
			try {
				StringBuilder locationBuilder = new StringBuilder(
						"http://www.uniprot.org/uniprot/" + accession + ".rdf");
				String location = locationBuilder.toString();
				location = location.replace(" ", "%20");
				URL url = new URL(location).toURI().toURL();
				log.info("Submitting " + locationBuilder + "...");
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
					log.info("Got a OK reply in " + (t2 - t1) / 1000 + "sg");
					InputStream is = conn.getInputStream();
					URLConnection.guessContentTypeFromStream(is);
					final Entry entry = parseRDFResponse(is, accession);
					if (entry != null)
						ret.getEntry().add(entry);
				} else {
					log.error("Failed, got " + conn.getResponseMessage() + " for " + location);
				}
				conn.disconnect();

			} catch (MalformedURLException e) {
				e.printStackTrace();
				log.warn(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				log.warn(e.getMessage());
			} catch (InterruptedException e) {
				e.printStackTrace();
				log.warn(e.getMessage());
			} catch (URISyntaxException e) {
				e.printStackTrace();
				log.warn(e.getMessage());
			}
		}

		return ret;
	}

	private Entry parseRDFResponse(InputStream is, String accession) {

		boolean obsolete = false;
		boolean reviewed = false;
		String modified = null;
		int version = 0;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			NodeList nList = doc.getElementsByTagName("rdf:Description");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
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
			Entry ret = new UniprotEntryAdapterFromRDF(accession, obsolete, reviewed, modified, version).adapt();
			return ret;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
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

		Collection<String> set = new HashSet<String>();
		String[] array = new String[] { "P13368,P20806,Q9UM73,P97793,Q17192" };
		for (String string : array) {
			set.add(string);
		}
		UniprotProteinRemoteRetriever uprr = new UniprotProteinRemoteRetriever(
				new File("C:\\users\\salva\\desktop\\tmp\\UniprotKB"), true);
		final Uniprot uniprot = uprr.getProteins(set, UniprotProteinRemoteRetriever.getCurrentUniprotRemoteVersion());
		for (Entry entry2 : uniprot.getEntry()) {
			final List<String> names = entry2.getName();
			for (String string : names) {
				System.out.println(string);
			}
		}
	}

	@Override
	public synchronized Map<String, Entry> getAnnotatedProteins(String uniprotVersion, Collection<String> accessions)
			throws IllegalArgumentException {
		final String currentUniprotRemoteVersion = getCurrentUniprotRemoteVersion();
		if (currentUniprotRemoteVersion.equals(uniprotVersion)) {
			log.info("Current uniprot release matches with the provided: " + uniprotVersion);
			log.info("Attemping to retrieve " + accessions.size() + " accessions");
			long t1 = System.currentTimeMillis();
			final Uniprot proteins = getProteins(accessions, uniprotVersion);

			long t2 = System.currentTimeMillis();

			log.info(proteins.getEntry().size() + " accessions retrieved in " + (t2 - t1) / 1000 + "sg");

			// long t3 = System.currentTimeMillis();
			Map<String, Entry> map = new HashMap<String, Entry>();
			UniprotProteinLocalRetriever.addEntriesToMap(accessions, map, proteins.getEntry());
			checkIfSomeProteinIsMissing(accessions, map, uniprotVersion);

			return map;
		}
		throw new IllegalArgumentException("Current version of uniprot different from provided: " + uniprotVersion
				+ " vs " + currentUniprotRemoteVersion);

	}

	private synchronized void checkIfSomeProteinIsMissing(Collection<String> accessions, Map<String, Entry> retrieved,
			String uniprotVersion) {
		int numBeforeRecovery = retrieved.size();
		Set<String> missingProteins = getMissingAccs(retrieved, accessions);
		if (!missingProteins.isEmpty()) {
			log.info("Trying to recover " + missingProteins.size() + " proteins");

			final Uniprot proteins = getProteins(missingProteins, uniprotVersion);
			final List<Entry> entries = proteins.getEntry();
			UniprotProteinLocalRetriever.addEntriesToMap(accessions, retrieved, entries);
			final int numRecovered = retrieved.size() - numBeforeRecovery;
			log.info(numRecovered + " proteins were able to be recovered");
			log.info(missingProteins.size() - numRecovered + " proteins still remain missing");
			missingAccessions = getMissingAccs(retrieved, accessions);
			StringBuilder allMissing = new StringBuilder();
			for (String acc : missingAccessions) {
				allMissing.append(acc).append(",");
				if (allMissing.length() > 1000) {
					break;
				}
			}
			log.info("Still missing: " + missingAccessions.size() + " accessions: " + allMissing.toString());
		} else {
			log.info("No missing proteins. All were retrieved.");
		}
	}

	private Set<String> getMissingAccs(Map<String, ?> ret, Collection<String> accessions) {
		Set<String> missingProteins = new HashSet<String>();
		for (String accession : accessions) {
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
		final String releaseNotesURLString = PropertiesUtil.getInstance(PropertiesUtil.UNIPROT_PROPERTIES_FILE)
				.getPropertyValue(PropertiesUtil.UNIPROT_RELEASES_NOTES_PROP);
		log.debug("Getting uniprot current release from " + releaseNotesURLString);
		if (releaseNotesURLString != null) {
			URL url;
			try {
				url = new URL(releaseNotesURLString);

				InputStream is = url.openStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
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
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				log.warn(e.getMessage());
			}
		}
		return "";
	}

	@Override
	public Set<String> getMissingAccessions() {
		return missingAccessions;
	}

	@Override
	public Map<String, Entry> getAnnotatedProtein(String uniprotVersion, String accession) {
		Set<String> accessions = new HashSet<String>();
		accessions.add(accession);
		return getAnnotatedProteins(uniprotVersion, accessions);
	}

}
