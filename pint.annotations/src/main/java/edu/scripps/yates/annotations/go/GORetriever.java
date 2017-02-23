package edu.scripps.yates.annotations.go;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import edu.scripps.yates.annotations.go.index.FlatFileCache;
import edu.scripps.yates.utilities.fasta.FastaParser;
import psidev.psi.tools.ontology_manager.OntologyManager;
import psidev.psi.tools.ontology_manager.impl.local.OntologyLoaderException;
import psidev.psi.tools.ontology_manager.interfaces.OntologyAccess;
import psidev.psi.tools.ontology_manager.interfaces.OntologyTermI;

public class GORetriever {
	private static final Logger log = Logger.getLogger(GORetriever.class);
	public static Map<String, Integer> indexByColumnName = new HashMap<String, Integer>();
	private static FlatFileCache cache;
	private static int CHUNK_SIZE = 100;
	private static final DecimalFormat df = new DecimalFormat("#.#");
	private OntologyAccess goOntology;

	public GORetriever(File indexFolder) {
		if (!indexFolder.exists()) {
			indexFolder.mkdirs();
		}
		cache = new FlatFileCache(indexFolder + File.separator + "gene_ontologies.txt");

	}

	public boolean containsGOTerm(String proteinID, String goID) throws IOException {
		final Set<GoEntry> goEntries = retrieveGOEntries(proteinID);
		if (goEntries != null) {
			for (GoEntry goEntry : goEntries) {
				if (goEntry.getGoID() != null && goEntry.getGoID().equals(goID)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean containsTermNamePart(String proteinID, String termNamePart) throws IOException {
		final Set<GoEntry> goEntries = retrieveGOEntries(proteinID);
		if (goEntries != null) {
			for (GoEntry goEntry : goEntries) {
				if (goEntry.getGoName() != null
						&& goEntry.getGoName().toLowerCase().contains(termNamePart.toLowerCase())) {
					return true;
				}
			}
		}
		return false;
	}

	public Set<GoEntry> retrieveGOEntries(String acc) throws IOException {
		List<String> list = new ArrayList<String>();
		list.add(acc);
		final Map<String, Set<GoEntry>> retrieveGOEntries = retrieveGOEntries(list);
		if (retrieveGOEntries != null) {
			if (retrieveGOEntries.containsKey(acc)) {
				return retrieveGOEntries.get(acc);
			}
		}
		return Collections.emptySet();

	}

	public Map<String, Set<GoEntry>> retrieveGOEntries(Collection<String> accs) throws IOException {
		Map<String, Set<GoEntry>> ret = new HashMap<String, Set<GoEntry>>();
		int num = 0;

		int percentage = 0;

		final Map<String, Set<GoEntry>> entriesByID = cache.getEntriesByID(accs);
		ret.putAll(entriesByID);
		Set<String> missingEntries = new HashSet<String>();
		if (accs.size() != entriesByID.size()) {
			for (String acc : accs) {
				if (!entriesByID.containsKey(acc) && !FastaParser.isContaminant(acc) && !FastaParser.isReverse(acc)) {
					missingEntries.add(acc);
				}
			}
			if (missingEntries.size() > 0) {
				log.info(missingEntries.size() + " proteins not found in the index");
			}
		}

		Set<String> toSend = new HashSet<String>();
		for (String acc : missingEntries) {
			if (FastaParser.isContaminant(acc) || FastaParser.isReverse(acc)) {
				continue;
			}
			toSend.add(acc);
			if (toSend.size() == CHUNK_SIZE) {
				final Set<GoEntry> entries = retrieve(toSend);
				addToMapByProteinID(entries, ret);

				toSend.clear();
			}
			num++;
			final int percentTMP = Double.valueOf(num * 100.0 / missingEntries.size()).intValue();
			if (percentage != Double.valueOf(percentTMP).intValue()) {
				log.info(df.format(percentTMP) + "% GO entries retrieved");
				percentage = percentTMP;
			}

		}
		if (!toSend.isEmpty()) {
			final Set<GoEntry> entries = retrieve(toSend);
			addToMapByProteinID(entries, ret);
		}
		if (!missingEntries.isEmpty()) {
			log.info("Still there are " + (missingEntries.size()) + " proteins missing");
		}
		return ret;
	}

	private void addToMapByProteinID(Set<GoEntry> entries, Map<String, Set<GoEntry>> ret) {
		for (GoEntry goEntry : entries) {
			if (ret.containsKey(goEntry.getId())) {
				ret.get(goEntry.getId()).addAll(entries);
			} else {
				ret.put(goEntry.getId(), entries);
			}
		}

	}

	private Set<GoEntry> retrieve(Collection<String> accs) throws IOException {
		try {

			Set<GoEntry> ret = new HashSet<GoEntry>();

			BufferedReader rd = null;
			try {
				StringBuilder sb = new StringBuilder();
				for (String acc : accs) {
					if (!"".equals(sb.toString())) {
						sb.append(",");
					}
					sb.append(acc);
				}
				// URL for annotations from QuickGO for one protein
				URL u = new URL("http://www.ebi.ac.uk/QuickGO/GAnnotation?protein=" + sb.toString() + "&format=tsv");
				log.info("Sending query: " + u.toString());
				// Connect
				HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
				// Get data
				rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				// Read data
				List<String> columns = Arrays.asList(rd.readLine().split("\t"));
				int index = 0;
				for (String string : columns) {
					indexByColumnName.put(string, index);
					index++;
				}
				// Read the annotations line by line
				String line;
				Set<String> entriesWithInfo = new HashSet<String>();
				while ((line = rd.readLine()) != null) {
					GoEntry entry = new GoEntry(line);
					ret.add(entry);
					entriesWithInfo.add(entry.getId());
				}
				for (String acc : accs) {
					if (!entriesWithInfo.contains(acc)) {
						// protein with no GO terms
						ret.add(new GoEntry(acc));
					}
				}

				log.debug(ret.size() + " GO terms retrieved for " + accs.size() + " proteins");
			} finally {
				// close input when finished
				if (rd != null) {
					rd.close();
				}
			}
			cache.addToIndex(ret);
			return ret;
		} catch (IOException e) {
			try {
				log.info("Retrying after  getting IOException: " + e.getMessage());

				return retrieve(accs);
			} catch (IOException e2) {
				return Collections.emptySet();
			}
		}
	}

	private OntologyAccess getGOOntology() {
		if (goOntology == null) {
			try {
				InputStream oboFile = new ClassPathResource("go.xml").getInputStream();

				OntologyManager om = new OntologyManager(oboFile);
				goOntology = om.getOntologyAccess("GO");
			} catch (OntologyLoaderException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return goOntology;
	}

	public OntologyTermI getGOTermByID(String goID) {

		OntologyAccess goOntology = getGOOntology();
		final OntologyTermI termForAccession = goOntology.getTermForAccession(goID);
		return termForAccession;
	}
}
