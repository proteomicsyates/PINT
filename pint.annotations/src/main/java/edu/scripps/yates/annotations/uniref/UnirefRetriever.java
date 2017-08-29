package edu.scripps.yates.annotations.uniref;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniref.xml.EntryType;
import edu.scripps.yates.annotations.uniref.xml.MemberType;
import edu.scripps.yates.annotations.uniref.xml.PropertyType;
import edu.scripps.yates.annotations.uniref.xml.UniRef;
import edu.scripps.yates.annotations.util.PropertiesUtil;
import edu.scripps.yates.utilities.fasta.FastaParser;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

/**
 * This class is used for retrieving the UniRef90 entries from a given UNIPROT
 * accession. If a counterpart NCBI Tax ID is provided, only the entries in the
 * Uniref90 cluster that belongs to that taxonomy will be returned.
 * 
 * @author Salva
 *
 */
public class UnirefRetriever {
	private final static Logger log = Logger.getLogger(UnirefRetriever.class);
	private static final PropertiesUtil unirefProps = PropertiesUtil.getInstance(PropertiesUtil.UNIREF_PROPERTIES_FILE);
	private static JAXBContext jaxbContext;
	private static final String uniprotKBAccession = "UniProtKB accession";
	private static final String ncbiTaxonomyProperty = "NCBI taxonomy";
	private static final int MAX_NUM_ENTRIES_PER_QUERY = 250;
	private static UnirefRetriever instance;
	private final File mappingFile;
	private final Map<String, Set<String>> map = new THashMap<String, Set<String>>();
	private final Integer counterPartNCBITaxID;

	private UnirefRetriever(File outputFile2, Integer counterPartNCBITaxID2) {
		mappingFile = outputFile2;
		counterPartNCBITaxID = counterPartNCBITaxID2;
		loadFile();
	}

	/**
	 *
	 * @param mappingFile
	 *            a mapping file in which the mappings are going to be writted.
	 *            If was already used, it will be readed to check if the mapping
	 *            is already in the file or not.
	 * @param counterPartNCBITaxID
	 *            NCBI tax ID number for the counterpart species.
	 * @return
	 */
	public static UnirefRetriever getInstance(File mappingFile, Integer counterPartNCBITaxID) {
		boolean create = false;
		if (instance != null) {
			if (instance.mappingFile != null && !instance.mappingFile.getAbsolutePath().equals(mappingFile)) {
				create = true;
			}
			if (instance.mappingFile == null && mappingFile != null) {
				create = true;
			}
			if (instance.counterPartNCBITaxID == null && counterPartNCBITaxID != null) {
				create = true;
			}

			if (instance.counterPartNCBITaxID != null && !instance.counterPartNCBITaxID.equals(counterPartNCBITaxID)) {
				create = true;
			}
		}
		if (instance == null) {
			create = true;
		}
		if (create) {
			instance = new UnirefRetriever(mappingFile, counterPartNCBITaxID);
		}
		return instance;

	}

	public Map<String, Set<String>> getUnirefClustersEntries(Collection<String> uniprotACCs) {

		log.info("Retrieving " + uniprotACCs.size() + " uniref clusters entries");
		if (counterPartNCBITaxID != null) {
			log.info("Looking for proteins with 90% of similarity in species: " + counterPartNCBITaxID);
		}
		if (jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance(UniRef.class);
			} catch (JAXBException e) {
				e.printStackTrace();

			}
		}
		final Set<String> missingAccessions = getMissingAccessions(uniprotACCs, map.keySet());
		log.info("Missing accessions not in file: " + missingAccessions.size());
		Map<String, Set<String>> ret = retrieve(missingAccessions, counterPartNCBITaxID);
		final Set<String> missingAccessions2 = getMissingAccessions(uniprotACCs, map.keySet());
		log.info("Missing accessions: " + missingAccessions2.size());
		// return ret;
		// if (!missingAccessions.isEmpty()) {
		// log.info("Trying to recover " + missingAccessions.size() + " missing
		// accessions");
		// Map<String, Set<DbReferenceType>> ret2 = retrieve(missingAccessions,
		// counterPartNCBITaxID);
		// log.info(ret.size() + " where recovered");
		// for (String acc : ret2.keySet()) {
		// final Set<DbReferenceType> c = ret2.get(acc);
		// if (ret.containsKey(acc)) {
		// log.warn("Not possible");
		// } else {
		// ret.put(acc, c);
		// }
		// }
		// }
		Map<String, Set<String>> ret2 = new THashMap<String, Set<String>>();
		for (String uniprotAcc : uniprotACCs) {
			final Set<String> set = map.get(uniprotAcc);
			if (set != null) {
				for (String otherUniprotAcc : set) {
					addToMap(ret2, uniprotAcc, otherUniprotAcc);
				}

			}
		}
		return ret2;

	}

	private Map<String, Set<String>> retrieve(Collection<String> uniprotACCs, Integer counterPartNCBITaxID) {
		final String unirefUrl = unirefProps.getPropertyValue(PropertiesUtil.UNIREF_URL_PROP);
		Map<String, Set<String>> ret = new THashMap<String, Set<String>>();
		final Iterator<String> uniprotIDIterator = uniprotACCs.iterator();
		int num = 0;
		while (num < uniprotACCs.size()) {
			Set<String> queryIDs = new THashSet<String>();
			for (int i = 0; i < MAX_NUM_ENTRIES_PER_QUERY; i++) {
				if (uniprotIDIterator.hasNext()) {
					String uniprotAcc = uniprotIDIterator.next();
					num++;
					if (!FastaParser.isUniProtACC(uniprotAcc)) {
						log.info(uniprotAcc + " is not recognized as an Uniprot Accession. Ignoring accession "
								+ uniprotAcc);
						continue;
					}
					queryIDs.add(uniprotAcc);

				} else {
					break;
				}
			}

			// create URL
			// String url = unirefUrl + "?query=member:" + uniprotAcc +
			// "*&fil=identity:0.9&sort=score&format=xml";
			String url = unirefUrl + "?query=";
			int i = 0;
			for (String queryACC : queryIDs) {
				if (i > 0) {
					url += "+or+";
				}
				url += "UniRef90_" + queryACC;
				i++;
			}
			url += "&format=xml";
			log.info(num + "/" + uniprotACCs.size() + " processed");
			Map<String, Set<String>> ret2 = new THashMap<String, Set<String>>();
			UniRef uniref = sendRequest(url);
			if (uniref != null && uniref.getEntry() != null) {
				for (EntryType entry : uniref.getEntry()) {
					final MemberType representativeMember = entry.getRepresentativeMember();
					final List<PropertyType> properties = representativeMember.getDbReference().getProperty();
					PropertyType uniprotAccType = getProperty(properties, uniprotKBAccession);
					if (uniprotAccType != null) {
						// found the members with the taxonomy provided
						final List<MemberType> members = entry.getMember();
						if (members != null) {
							for (MemberType memberType : members) {
								if (memberType.getDbReference() != null) {
									final PropertyType taxonomyProperty = getProperty(
											memberType.getDbReference().getProperty(), ncbiTaxonomyProperty);
									if (taxonomyProperty == null) {
										continue;
									}
									try {
										Integer taxId = Integer.valueOf(taxonomyProperty.getValue());
										if (counterPartNCBITaxID == null || taxId.equals(counterPartNCBITaxID)) {
											final PropertyType property = getProperty(
													memberType.getDbReference().getProperty(),
													UnirefRetriever.uniprotKBAccession);
											if (property != null) {
												final String otherProteinAcc = property.getValue();

												final String uniprotAcc = uniprotAccType.getValue();
												if (!map.containsKey(uniprotAcc)) {
													addToMap(map, uniprotAcc, otherProteinAcc);
													addToMap(ret, uniprotAcc, otherProteinAcc);
													addToMap(ret2, uniprotAcc, otherProteinAcc);
												}
											}
										}
									} catch (NumberFormatException e) {
										// do nothing
									}
								}
							}
						}

					}

				}
			}
			final Set<String> missingAccessions = getMissingAccessions(queryIDs, ret2.keySet());
			for (String missing : missingAccessions) {
				ret2.put(missing, new THashSet<String>());
			}
			appendToFile(ret2);
		}

		return ret;
	}

	private void addToMap(Map<String, Set<String>> map, String uniprotAcc, String otherUniprotAcc) {
		if (map.containsKey(uniprotAcc)) {
			map.get(uniprotAcc).add(otherUniprotAcc);
		} else {
			Set<String> set = new THashSet<String>();
			set.add(otherUniprotAcc);
			map.put(uniprotAcc, set);
		}
	}

	private static Set<String> getMissingAccessions(Collection<String> queryIDs, Collection<String> retrievedEntries) {
		Set<String> ret = new THashSet<String>();
		for (String queryID : queryIDs) {
			if (!retrievedEntries.contains(queryID)) {
				ret.add(queryID);
			}
		}
		return ret;
	}

	private static PropertyType getProperty(List<PropertyType> properties, String propertyTypeName) {
		if (properties != null) {
			for (PropertyType propertyType : properties) {
				if (propertyType.getType().equals(propertyTypeName)) {
					return propertyType;
				}
			}
		}
		return null;
	}

	private static UniRef sendRequest(String urlString) {

		try {
			// log.info("Submitting String= " + urlString + "...");
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
				log.info("Got a OK reply in " + (t2 - t1) / 1000 + "sg");
				InputStream is = conn.getInputStream();
				URLConnection.guessContentTypeFromStream(is);
				final UniRef response = parseResponse(is);
				return response;
			} else
				log.error("Failed, got " + conn.getResponseMessage() + " for " + urlString);
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

	private static UniRef parseResponse(InputStream is) {

		log.debug("Processing response");
		OutputStream outputStream = null;
		try {

			final File createTempFile = File.createTempFile("uniref", "xml");
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
			UniRef uniref = (UniRef) unmarshaller.unmarshal(createTempFile);

			log.debug("Response parsed succesfully");
			log.debug(uniref.getEntry().size() + " entries");
			return uniref;
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

	private synchronized void appendToFile(Map<String, Set<String>> map) {
		if (mappingFile == null) {
			return;
		}
		log.info("Appending " + map.size() + " more mappings");
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(mappingFile, true)));
			for (String proteinAcc : map.keySet()) {
				final Set<String> otherProteinsAcc = map.get(proteinAcc);
				out.write(proteinAcc);
				if (otherProteinsAcc.isEmpty()) {
					out.write("\t-");
				} else {
					for (String otherProteinAcc : otherProteinsAcc) {
						out.write("\t" + otherProteinAcc);
					}
				}
				out.write("\n");
			}
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		} finally {
			if (out != null) {
				out.close();
			}
			log.info("Appending done");

		}
	}

	private synchronized void loadFile() {
		if (mappingFile == null) {
			return;
		}
		BufferedReader br = null;
		try {
			InputStream fis = new FileInputStream(mappingFile);
			InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("\t")) {
					final String[] split = line.split("\t");
					String oneSpeciesProtAcc = split[0];
					for (int i = 1; i < split.length; i++) {
						if (map.containsKey(oneSpeciesProtAcc)) {
							map.get(oneSpeciesProtAcc).add(split[i]);
						} else {
							Set<String> set = new THashSet<String>();
							set.add(split[i]);
							map.put(oneSpeciesProtAcc, set);
						}
					}
				}
			}
		} catch (IOException e) {

		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			log.info("Loaded mapping for " + map.size() + " proteins");
		}

	}
}
