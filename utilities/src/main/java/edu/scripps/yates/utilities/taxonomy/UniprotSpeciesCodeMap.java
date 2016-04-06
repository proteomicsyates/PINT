package edu.scripps.yates.utilities.taxonomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class UniprotSpeciesCodeMap {
	private static final Logger log = Logger
			.getLogger(UniprotSpeciesCodeMap.class);
	private static final String speciesFileURLString = "http://www.uniprot.org/docs/speclist";
	private static final String localSpeciesFileName = "uniprot_speclist.txt";

	private static final String STARTING = "_____";
	private static final String COMMON_NAME_START = "C=";
	private static final String SCIENTIFIC_NAME_START = "N=";
	private static final String SYNONIM_START = "S=";

	private static UniprotSpeciesCodeMap instance;
	private final Map<String, UniprotOrganism> mapByCode = new HashMap<String, UniprotOrganism>();
	private final Map<String, UniprotOrganism> mapByScientificName = new HashMap<String, UniprotOrganism>();
	private final Map<String, UniprotOrganism> mapByCommonName = new HashMap<String, UniprotOrganism>();
	private final Map<Long, UniprotOrganism> mapByTaxonCode = new HashMap<Long, UniprotOrganism>();
	private final List<UniprotOrganism> list = new ArrayList<UniprotOrganism>();

	private static final String HIV = "Human immunodeficiency virus";
	private static final String ISOLATE = ".*\\(isolate\\s(\\S+).*\\)";

	private UniprotSpeciesCodeMap() {
		load();
	}

	public static UniprotSpeciesCodeMap getInstance() {
		if (instance == null)
			instance = new UniprotSpeciesCodeMap();
		return instance;
	}

	private void load() {
		// try to get first the remote up-to-date file
		boolean ok = localLoad();
		if (!ok) {
			log.info("Local loading of uniprot species from "
					+ speciesFileURLString
					+ " has failed. Trying using remote file.");
			ok = remoteLoad();
		}
		if (!ok)
			log.warn("There was not possible to load the uniprot species mapping neither local or remote");
	}

	private boolean localLoad() {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		InputStream is = classLoader.getResourceAsStream(localSpeciesFileName);
		if (is != null)
			try {
				parse(is);
				log.info(mapByCode.size() + " species mapped");
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		return false;
	}

	private boolean remoteLoad() {
		log.info("Trying to read species from Uniprot URL:"
				+ speciesFileURLString);
		URL url;
		try {
			url = new URL(speciesFileURLString);
			InputStream is = url.openStream();
			parse(is);
			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void parse(InputStream is) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		// get first line of the remote file as release notes
		String line = null;
		boolean started = false;
		String commonName = null;
		String scientificName = null;
		String code = null;
		String synonim = null;
		long taxonCode = 0;
		UniprotKingdom kingdom = null;
		while ((line = br.readLine()) != null) {
			line = line.trim();

			final StringTokenizer tokenizer = new StringTokenizer(line);
			if (tokenizer.hasMoreElements()) {
				final String firstToken = tokenizer.nextToken();
				if (firstToken.equals(STARTING)) {
					started = true;
					continue;
				}
				if (started) {
					if ("".equals(line))
						break;
					if (firstToken.startsWith(COMMON_NAME_START)) {
						commonName = line.substring(COMMON_NAME_START.length());
					} else if (firstToken.startsWith(SYNONIM_START)) {
						synonim = line.substring(SYNONIM_START.length());
					} else {

						// create the object if data is present
						if (code != null) {
							UniprotOrganism organism = new UniprotOrganism(
									code, kingdom, taxonCode, scientificName);
							if (commonName != null)
								organism.setCommonName(commonName);
							if (synonim != null)
								organism.setSynonim(synonim);
							// store in the map
							mapByCode.put(code, organism);
							mapByCommonName.put(commonName, organism);
							mapByScientificName.put(scientificName, organism);
							mapByTaxonCode.put(taxonCode, organism);
							list.add(organism);
						}
						code = firstToken;
					}
					int numToken = 1;
					while (tokenizer.hasMoreTokens()) {
						numToken++;
						String token = tokenizer.nextToken();
						if (numToken == 2) {
							if (token.length() == 1) {
								kingdom = UniprotKingdom.getByCodeChar(token
										.charAt(0));
							}
						} else if (numToken == 3) {
							if (token.endsWith(":")) {
								token = token.substring(0, token.length() - 1);
							}
							try {
								taxonCode = Long.valueOf(token);
							} catch (NumberFormatException e) {

							}
						} else if (numToken == 4) {
							if (token.startsWith(SCIENTIFIC_NAME_START)) {
								scientificName = token
										.substring(SCIENTIFIC_NAME_START
												.length());
							}
						} else if (numToken > 4) {
							scientificName += " " + token;
						}
					}

				}
			}

		}
		if (br != null)
			br.close();
		log.info(mapByCode.size() + " species readed");
	}

	/**
	 * Try to find the organism by different fields such as code, common name,
	 * scientific name and taxon id
	 * 
	 * @param string
	 * @return
	 */
	public UniprotOrganism get(String string) {
		try {
			if (mapByCode.containsKey(string)) {
				return mapByCode.get(string);
			} else if (mapByCommonName.containsKey(string)) {
				return mapByCommonName.get(string);
			} else if (mapByScientificName.containsKey(string)) {
				return mapByScientificName.get(string);
			} else if (mapByTaxonCode.containsKey(Long.valueOf(string))) {

				return mapByTaxonCode.get(Long.valueOf(string));

			}
		} catch (NumberFormatException e) {
		}

		if (string.startsWith(HIV)) {
			return lookAsHIV(string);
		}
		return null;
	}

	private UniprotOrganism lookAsHIV(String string) {
		// get the isolate
		Pattern isolatePattern = Pattern.compile(ISOLATE);
		final Matcher matcher = isolatePattern.matcher(string);
		if (matcher.find()) {
			final String isolate = matcher.group(1).trim();
			for (String scientificName : mapByScientificName.keySet()) {
				if (scientificName.contains(HIV)
						&& scientificName.contains("isolate " + isolate))
					return mapByScientificName.get(scientificName);
			}
		}
		return null;
	}

	public List<UniprotOrganism> getOrganisms() {
		return list;
	}
}
