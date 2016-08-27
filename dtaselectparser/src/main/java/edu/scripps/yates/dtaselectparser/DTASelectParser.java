/**
 * diego Sep 17, 2013
 */
package edu.scripps.yates.dtaselectparser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;

import edu.scripps.yates.dbindex.DBIndexInterface;
import edu.scripps.yates.dbindex.IndexedProtein;
import edu.scripps.yates.dtaselectparser.util.DTASelectPSM;
import edu.scripps.yates.dtaselectparser.util.DTASelectProtein;
import edu.scripps.yates.dtaselectparser.util.DTASelectProteinGroup;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.remote.RemoteSSHFileReference;

/**
 * @author diego
 *
 */
public class DTASelectParser {
	private static final Logger log = Logger.getLogger(DTASelectParser.class);
	private final HashMap<String, DTASelectProtein> proteinsTable = new HashMap<String, DTASelectProtein>();
	private final HashMap<String, DTASelectPSM> psmTableByPSMID = new HashMap<String, DTASelectPSM>();
	private final HashMap<String, DTASelectPSM> psmTableByScanNumber = new HashMap<String, DTASelectPSM>();

	private final HashMap<String, List<DTASelectPSM>> psmTableBySequence = new HashMap<String, List<DTASelectPSM>>();
	private final Set<DTASelectProteinGroup> dtaSelectProteinGroups = new HashSet<DTASelectProteinGroup>();
	private final List<String> keys = new ArrayList<String>();
	public static final String PROLUCID = "ProLuCID";
	public static final String SEQUEST = "Sequest";
	private boolean dtaselect;
	private String runPath;
	private DBIndexInterface dbIndex;
	private boolean processed = false;
	private final HashMap<String, InputStream> fs;
	private Pattern decoyPattern;
	private final List<String> commandLineParameterStrings = new ArrayList<String>();
	private final Set<String> searchEngines = new HashSet<String>();
	private DTASelectCommandLineParameters commandLineParameter;
	private String searchEngineVersion;
	private String fastaPath;
	private final Set<String> spectraFileNames = new HashSet<String>();
	private String dtaSelectVersion;
	private final Set<String> spectraFileFullPaths = new HashSet<String>();

	public DTASelectParser(URL u) throws IOException {
		this(u.getFile(), u.openStream());
	}

	public DTASelectParser(RemoteSSHFileReference s) throws FileNotFoundException {
		this(s.getRemoteFile().getAbsolutePath(), new FileInputStream(s.getRemoteFile()));
	}

	public DTASelectParser(Collection<RemoteSSHFileReference> s) throws IOException {
		fs = new HashMap<String, InputStream>();
		for (RemoteSSHFileReference server : s) {
			final File remoteFile = server.getRemoteFile();
			fs.put(server.getRemoteFile().getAbsolutePath(), new FileInputStream(remoteFile));
		}

	}

	public DTASelectParser(List<File> s) throws FileNotFoundException {
		fs = new HashMap<String, InputStream>();
		for (File remoteFile : s) {
			fs.put(remoteFile.getAbsolutePath(), new FileInputStream(remoteFile));
		}
	}

	public DTASelectParser(File file) throws FileNotFoundException {
		fs = new HashMap<String, InputStream>();
		fs.put(file.getAbsolutePath(), new FileInputStream(file));
	}

	public DTASelectParser(String runId, InputStream f) {
		fs = new HashMap<String, InputStream>();
		fs.put(runId, f);
	}

	private void process(Map<String, InputStream> fs) throws IOException {

		DTASelectProteinGroup currentProteinGroup = new DTASelectProteinGroup();
		HashMap<String, Integer> psmHeaderPositions = new HashMap<String, Integer>();
		HashMap<String, Integer> proteinHeaderPositions = new HashMap<String, Integer>();
		int numDecoy = 0;
		for (String runId : fs.keySet()) {
			log.info("Reading " + runId + "...");
			InputStream f = fs.get(runId);
			BufferedInputStream bis = new BufferedInputStream(f);
			BufferedReader dis = new BufferedReader(new InputStreamReader(bis));

			String line;
			int numLine = 0;
			int searchEngineLine = -1;
			boolean intro = false;
			boolean conclusion = false;
			boolean isPsm = false;
			boolean subsetProteinsRemoved = false;
			while ((line = dis.readLine()) != null) {
				numLine++;
				if (numLine == 1) {
					dtaSelectVersion = line.split(" ")[1];
				}
				if (numLine == 2) {
					runPath = line.trim();
				}
				if (numLine == 3) {
					fastaPath = line.trim();
				}
				if (line.toLowerCase().startsWith(SEQUEST.toLowerCase())) {
					searchEngineLine = numLine;
					searchEngines.add(SEQUEST);
					setSearchEngineVersion(line.split(" ")[1]);
				}
				if (line.toLowerCase().startsWith(PROLUCID.toLowerCase())) {
					searchEngineLine = numLine;
					searchEngines.add(PROLUCID);
					setSearchEngineVersion(line.split(" ")[1]);
				}
				if (numLine == searchEngineLine + 1) {
					commandLineParameterStrings.add(line);
					commandLineParameter = new DTASelectCommandLineParameters(line);
				}
				if (line.startsWith("DTASelect")) {
					// DTASelectProtein p = new DTASelectProtein("DTA", 0, 0, 0,
					// line);
					// ptable.put("DTA", p);
					intro = true;
					continue;
				}
				if (line.startsWith("Locus")) {
					// parse psm header positions
					String[] splitted = line.split("\t");
					for (int position = 0; position < splitted.length; position++) {
						String header = splitted[position];
						proteinHeaderPositions.put(header, position);
					}

					// DTASelectProtein p = new DTASelectProtein("Title", 0, 0,
					// 0,
					// line);
					// ptable.put("Title", p);
					intro = false;
					continue;
				}
				if (line.startsWith("Unique")) {
					// parse psm header positions
					String[] splitted = line.split("\t");
					for (int position = 0; position < splitted.length; position++) {
						String header = splitted[position];
						psmHeaderPositions.put(header, position);
					}

					// DTASelectProtein p = new DTASelectProtein("Unique", 0, 0,
					// 0, line);
					// ptable.put("Unique", p);
					continue;
				}
				if (intro) {
					if (line.contains("Remove subset proteins")) {
						final String[] splitted = line.split("\t");
						if ("TRUE".equals(splitted[0])) {
							subsetProteinsRemoved = true;
						}
					}
					// DTASelectProtein p = ptable.get("DTA");
					// p.addExtra(line);
					continue;
				}
				if (conclusion) {
					// DTASelectProtein p = ptable.get("Conclusion");
					// p.addExtra(line);
					continue;
				}

				String[] elements = line.split("\t");
				// if (elements[1].equals("DTASelectProteins")) {
				if (elements[1].equals("Proteins")) {
					conclusion = true;
					// DTASelectProtein p = new DTASelectProtein("Conclusion",
					// 0,
					// 0, 0, line);
					// ptable.put("Conclusion", p);
					continue;
				}

				// this is the case of a protein
				if (isNumeric(elements[1])) {

					// if comes from a psm line, clear the current group of
					// proteins
					if (isPsm && dbIndex == null) {
						dtaSelectProteinGroups.add(currentProteinGroup);

						// restart the protein group
						currentProteinGroup = new DTASelectProteinGroup();

					}
					// if (dbIndex == null) {
					DTASelectProtein p = new DTASelectProtein(line, proteinHeaderPositions);
					if (proteinsTable.containsKey(p.getLocus())) {
						DTASelectProtein p2 = proteinsTable.get(p.getLocus());
						p = mergeProteins(p, p2);
					}
					if (!searchEngines.isEmpty())
						p.setSearchEngine(searchEngines.iterator().next());
					boolean skip = false;
					if (decoyPattern != null) {
						final Matcher matcher = decoyPattern.matcher(p.getLocus());
						if (matcher.find()) {
							numDecoy++;
							skip = true;
						}
					}
					if (!skip) {
						proteinsTable.put(p.getLocus(), p);
						keys.add(p.getLocus());
						currentProteinGroup.add(p);
					}
					// }
					isPsm = false;
				} else {
					// this is the case of a psm
					isPsm = true;
					String psmID = getPSMIdentifier(line, psmHeaderPositions);
					DTASelectPSM psm;
					if (psmTableByPSMID.containsKey(psmID)) {
						psm = psmTableByPSMID.get(psmID);
					} else {
						psm = new DTASelectPSM(line, psmHeaderPositions, runPath);
						if (!searchEngines.isEmpty())
							psm.setSearchEngine(searchEngines.iterator().next());
						if (!spectraFileNames.contains(psm.getSpectraFileName())) {
							spectraFileNames.add(psm.getSpectraFileName());
							log.debug(psm.getSpectraFileName() + " added to a set of " + spectraFileNames.size()
									+ " spectra file names in total");
						}
						String spectraFileFullPath = new File(runId).getParent() + File.separator
								+ psm.getSpectraFileName() + ".ms2";
						if (!spectraFileFullPaths.contains(spectraFileFullPath)) {
							spectraFileFullPaths.add(spectraFileFullPath);
							log.debug(spectraFileFullPath + " added to a set of " + spectraFileFullPaths.size()
									+ " spectra paths in total");
						}
						psmTableByPSMID.put(psm.getPsmIdentifier(), psm);
						addTopsmTable(psm);
						// if there is an indexed Fasta, look into it to get the
						// proteins
						if (dbIndex != null) {
							Set<IndexedProtein> indexedProteins = dbIndex.getProteins(psm.getSequence().getSequence());
							if (indexedProteins != null) {
								log.debug(indexedProteins.size() + " proteins contains "
										+ psm.getSequence().getSequence() + " on fasta file");
								for (IndexedProtein indexedProtein : indexedProteins) {
									final String indexedAccession = indexedProtein.getAccession();
									// we should take into account that in the
									// indexed database you may have decoy hits
									// that you want to avoid
									if (decoyPattern != null) {
										final Matcher matcher = decoyPattern.matcher(indexedAccession);
										if (matcher.find()) {
											numDecoy++;
											continue;
										}
									}
									keys.add(indexedAccession);
									DTASelectProtein protein = null;
									if (proteinsTable.containsKey(indexedAccession)) {
										protein = proteinsTable.get(indexedAccession);
									} else {
										protein = new DTASelectProtein(indexedProtein);
										proteinsTable.put(indexedAccession, protein);
									}
									// add the psm to the protein and vice-versa
									psm.addProtein(protein);
									protein.addPSM(psm);
								}
							}
						}
					}
					final String scanNumber = psm.getScan();
					// add to the table by scan number
					if (!psmTableByScanNumber.containsKey(scanNumber)) {
						psmTableByScanNumber.put(scanNumber, psm);
					}

					if (dbIndex == null) {
						// add the PSM to all the proteins in the current group
						// and all the proteins to the psm
						for (DTASelectProtein prot : currentProteinGroup) {
							prot.addPSM(psm);
							psm.addProtein(prot);
						}
					}
				}

			}
			try {
				f.close();
			} catch (IOException e) {

			}
		}

		processed = true;
		log.info(proteinsTable.size() + " proteins readed in " + this.fs.size() + " DTASelect file(s).");
		log.info(psmTableByPSMID.size() + " psms readed in " + this.fs.size() + " DTASelect file(s).");
		if (dbIndex == null) {
			dtaSelectProteinGroups.add(currentProteinGroup);
			log.info(dtaSelectProteinGroups.size() + " proteins groups readed in " + this.fs.size()
					+ " DTASelect file(s).");
		}
		if (decoyPattern != null)
			log.info(numDecoy + " proteins discarded as decoy.");
	}

	private DTASelectProtein mergeProteins(DTASelectProtein destination, DTASelectProtein origin) {
		if (destination == null)
			return null;

		destination.mergeWithProtein(origin);

		return destination;
	}

	private void addTopsmTable(DTASelectPSM psm) {
		final String psmKey = psm.getSequence().getSequence();
		if (psmTableBySequence.containsKey(psmKey)) {
			psmTableBySequence.get(psmKey).add(psm);
		} else {
			List<DTASelectPSM> list = new ArrayList<DTASelectPSM>();
			list.add(psm);
			psmTableBySequence.put(psmKey, list);
		}

	}

	public static String getPSMIdentifier(String line, HashMap<String, Integer> positions) {

		String[] elements = line.split("\t");

		String psmIdentifier = elements[positions.get(DTASelectPSM.PSM_ID)];
		String scan = FastaParser.getScanFromPSMIdentifier(psmIdentifier);
		String fileName = FastaParser.getFileNameFromPSMIdentifier(psmIdentifier);
		return fileName + "-" + scan;
	}

	public HashMap<String, DTASelectProtein> getDTASelectProteins() throws IOException {
		if (!processed)
			process(fs);

		return proteinsTable;
	}

	public Set<DTASelectProteinGroup> getProteinGroups() throws IOException {
		if (dbIndex != null)
			throw new IllegalArgumentException(
					"Reading proteins with a FASTA database, will not result in Protein groups");
		if (!processed)
			process(fs);
		return dtaSelectProteinGroups;
	}

	public HashMap<String, DTASelectPSM> getDTASelectPSMsByPSMID() throws IOException {
		if (!processed)
			process(fs);
		return psmTableByPSMID;
	}

	public HashMap<String, DTASelectPSM> getDTASelectPSMsByScanNumber() throws IOException {
		if (!processed)
			process(fs);
		return psmTableByScanNumber;
	}

	public HashMap<String, List<DTASelectPSM>> getDTASelectPSMsBySequence() throws IOException {
		if (!processed)
			process(fs);
		return psmTableBySequence;
	}

	private boolean isNumeric(String string) {
		try {
			Double.valueOf(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * @param dbIndex
	 *            the dbIndex to set
	 */
	public void setDbIndex(DBIndexInterface dbIndex) {
		this.dbIndex = dbIndex;
	}

	public void setDecoyPattern(String patternString) throws PatternSyntaxException {
		if (patternString != null) {
			decoyPattern = Pattern.compile(patternString);
		}
	}

	/**
	 * @return the runPath
	 * @throws IOException
	 */
	public String getRunPath() throws IOException {
		if (!processed)
			process(fs);
		return runPath;
	}

	/**
	 * @return the commandLineParameterStrings
	 * @throws IOException
	 */
	public List<String> getCommandLineParameterStrings() throws IOException {
		if (!processed)
			process(fs);
		return commandLineParameterStrings;
	}

	/**
	 * @return the searchEngines
	 * @throws IOException
	 */
	public Set<String> getSearchEngines() throws IOException {
		if (!processed)
			process(fs);
		return searchEngines;
	}

	/**
	 * @return the commandLineParameter
	 * @throws IOException
	 */
	public DTASelectCommandLineParameters getCommandLineParameter() throws IOException {
		if (!processed)
			process(fs);
		return commandLineParameter;
	}

	/**
	 * @return the searchEngineVersion
	 * @throws IOException
	 */
	public String getSearchEngineVersion() throws IOException {
		if (!processed)
			process(fs);

		return searchEngineVersion;
	}

	/**
	 * @param searchEngineVersion
	 *            the searchEngineVersion to set
	 */
	public void setSearchEngineVersion(String searchEngineVersion) {
		this.searchEngineVersion = searchEngineVersion;
	}

	public String getFastaPath() throws IOException {
		if (!processed)
			process(fs);

		return fastaPath;
	}

	public Set<String> getSpectraFileNames() throws IOException {
		if (!processed)
			process(fs);

		return spectraFileNames;
	}

	public String getDecoyPattern() throws IOException {
		if (!processed)
			process(fs);

		if (decoyPattern != null) {
			return decoyPattern.toString();
		}
		return null;
	}

	public String getDTASelectVersion() throws IOException {
		if (!processed)
			process(fs);

		return dtaSelectVersion;
	}

	public Set<String> getSpectraFileFullPaths() throws IOException {
		if (!processed)
			process(fs);
		return spectraFileFullPaths;
	}
}
