package edu.scripps.yates.annotations.uniprot;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.scripps.yates.annotations.util.PropertiesUtil;
import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.annotations.uniprot.xml.ObjectFactory;
import edu.scripps.yates.utilities.annotations.uniprot.xml.Uniprot;
import edu.scripps.yates.utilities.dates.DatesUtil;
import edu.scripps.yates.utilities.http.ThreadSafeHttpClient;
import edu.scripps.yates.utilities.jaxb.JaxbThreadSafe;
import edu.scripps.yates.utilities.pi.ParIterator;
import edu.scripps.yates.utilities.pi.reductions.Reducible;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class UniprotEntryRetrieverThread extends Thread {
	public static final String UNIPROT_EBI_SERVER = PropertiesUtil.getInstance(PropertiesUtil.UNIPROT_PROPERTIES_FILE)
			.getPropertyValue(PropertiesUtil.UNIPROT_EBI_SERVER_PROP);
	private static final String UNIPROT_EBI_PROTEINS_REST_PATH = PropertiesUtil
			.getInstance(PropertiesUtil.UNIPROT_PROPERTIES_FILE)
			.getPropertyValue(PropertiesUtil.UNIPROT_EBI_PROTEINS_REST_PATH);
	public static final int MAX_NUM_TO_RETRIEVE = 100; // defined by EBI
	private static final int PAGE_SIZE = 100; // defined by EBI
	private static Logger log = Logger.getLogger(UniprotEntryRetrieverThread.class);
	private final Reducible<Map<String, Entry>> reducibleMap;
	private final ParIterator<String> iterator;
	private static JAXBContext jaxbContext;
	// private Unmarshaller unmarshaller;
	private final int identifier;
	private JaxbThreadSafe jaxbThreadSave;
	private final CloseableHttpClient httpClient;
	private int totalSubmitted = 0;
	private int totalFound = 0;

	public UniprotEntryRetrieverThread() {
		this(-1, null, null, null);
	}

	public UniprotEntryRetrieverThread(int id, ParIterator<String> iterator, Reducible<Map<String, Entry>> reducibleMap,
			CloseableHttpClient httpClient) {
		identifier = id;
		this.iterator = iterator;
		this.reducibleMap = reducibleMap;
		if (httpClient != null) {
			this.httpClient = httpClient;
		} else {
			this.httpClient = ThreadSafeHttpClient.createNewHttpClient();
		}
		try {
			if (jaxbContext == null) {
				jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			}
			jaxbThreadSave = new JaxbThreadSafe(jaxbContext);
			// unmarshaller = jaxbContext.createUnmarshaller();
			//
		} catch (final JAXBException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Error trying to create unmarshaller for Uniprot entries");
		}

	}

	@Override
	public void run() {
		final Map<String, Entry> ret = new THashMap<String, Entry>();
		reducibleMap.set(ret);
		final Set<String> accessionsSent = new THashSet<String>();

		final List<String> accessionsToSend = new ArrayList<String>();
		while (iterator.hasNext()) {

			final String accession = iterator.next();

			accessionsSent.add(accession);
			accessionsToSend.add(accession);

			if (accessionsToSend.size() == MAX_NUM_TO_RETRIEVE) {
				makeRequest(accessionsToSend, ret);
				// restart list to send
				accessionsToSend.clear();
			}
		}
		if (!accessionsToSend.isEmpty()) {
			makeRequest(accessionsToSend, ret);
		}
		log.debug("From thread (" + identifier + ") finished. " + ret.size() + " protein entries retrieved");
	}

	private void makeRequest(List<String> accs, Map<String, Entry> ret) {
		try {
			final Set<String> foundSet = new THashSet<String>();
			final StringBuilder accListString = new StringBuilder();
			for (int i = 0; i < accs.size(); i++) {
				if (!"".equals(accListString.toString())) {
					accListString.append(',');
				}
				accListString.append(accs.get(i));
			}

			final URI uri = new URIBuilder().setScheme("http").setHost(UNIPROT_EBI_SERVER)
					.setPath("/Tools/dbfetch/dbfetch").setParameter("db", "uniprotkb")
					.setParameter("format", "uniprotxml").setParameter("id", accListString.toString()).build();

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

				final List<Entry> entries = parseResponse(is);
				final long t3 = System.currentTimeMillis();
				log.debug("Response parsed in " + DatesUtil.getDescriptiveTimeFromMillisecs(t3 - t2));
				totalSubmitted += accs.size();
				for (final Entry entry : entries) {
					UniprotProteinLocalRetriever.addEntryToMap(ret, entry);
					// final List<String> accessions = entry.getAccession();
					// for (final String acc : accessions) {
					//
					// // only use the keys of the list of accessions
					// // provided
					// if (accs.contains(acc)) {
					// foundSet.add(acc);
					//
					// }
					// }
				}
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

	private synchronized InputStream sendRequest(URL url) throws IOException, InterruptedException {
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
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setConnectTimeout(10000);// wait for 10s
			conn.connect();
			status = conn.getResponseCode();
		}
		if (status == HttpURLConnection.HTTP_OK) {
			return conn.getInputStream();
		} else {

			log.error("Failed, got " + conn.getResponseMessage() + " for " + url);

			conn.disconnect();
		}
		return null;

	}

	private String sendRequestWithThreadSafeHttpClient(URI uri) throws UnsupportedOperationException, IOException {
		final String response = ThreadSafeHttpClient.getStringResponse(httpClient, new HttpGet(uri));
		return response;
	}

	protected synchronized List<Entry> parseResponse(String is) throws JAXBException {

		log.debug("Processing response from remote input stream...");
		log.debug("Response length=" + is.length());
		if (is.startsWith("ERROR")) {
			return Collections.emptyList();
		}
		// OutputStream outputStream = null;
		try {

			final long t2 = System.currentTimeMillis();
			// final File targetFile = File.createTempFile("uniprot", ".tmp");

			// java.nio.file.Files.copy(is, targetFile.toPath(),
			// StandardCopyOption.REPLACE_EXISTING);

			// final XMLStreamReader xmlStreamReader =
			// XMLInputFactory.newInstance().createXMLStreamReader(is);
			final Uniprot uniprot = (Uniprot) jaxbThreadSave.unmarshal(IOUtils.toInputStream(is));

			log.debug("response unmarshalled succesfully in "
					+ DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t2));
			log.debug(uniprot.getEntry().size() + " entries");
			Thread.sleep(1l);

			return uniprot.getEntry();
			// } catch (final JAXBException e) {
			// e.printStackTrace();
			// log.warn("Error sending " + url);
			// log.warn(e.getMessage() + "\t" +
			// e.getLinkedException().getMessage());
		} catch (final InterruptedException e) {
			throw new RuntimeException("task cancelled");
		} catch (final JAXBException e) {
			log.error("Error unmarshalling: '" + is + "'");
			throw e;
		} finally {
			// if (is != null) {
			// IOUtils.closeQuietly(is);
			// }
		}

	}

	private static List<Entry> getRDFEntries(List<String> accessionsSent) {
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

		return ret.getEntry();
	}

	private static Entry parseRDFResponse(InputStream is, String accession) {

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
}
