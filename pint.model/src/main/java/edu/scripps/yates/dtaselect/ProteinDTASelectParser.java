/**
 * diego Sep 17, 2013
 */
package edu.scripps.yates.dtaselect;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import edu.scripps.yates.dtaselectparser.DTASelectCommandLineParameters;
import edu.scripps.yates.dtaselectparser.DTASelectParser;
import edu.scripps.yates.dtaselectparser.util.DTASelectPSM;
import edu.scripps.yates.dtaselectparser.util.DTASelectProtein;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.model.factories.AccessionEx;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.ProteomicsModelStaticStorage;
import edu.scripps.yates.utilities.remote.RemoteSSHFileReference;
import edu.scripps.yates.utilities.util.Pair;

public class ProteinDTASelectParser {
	private static final Logger log = Logger.getLogger(ProteinDTASelectParser.class);
	private final DTASelectParser parser;
	boolean dtaselect;
	private HashMap<String, Set<Protein>> proteins;
	private Map<String, PSM> psms;

	public ProteinDTASelectParser(URL u) throws IOException {
		this(FilenameUtils.getBaseName(u.getFile()), u.openStream());
	}

	public ProteinDTASelectParser(RemoteSSHFileReference s) throws IOException {
		this(s.getRemoteFileName(), new FileInputStream(s.getRemoteFile()));
	}

	public ProteinDTASelectParser(Collection<RemoteSSHFileReference> s) throws IOException {
		parser = new DTASelectParser(s);
	}

	public ProteinDTASelectParser(List<File> s) throws IOException {
		parser = new DTASelectParser(s);
	}

	public ProteinDTASelectParser(String runId, InputStream f) throws IOException {
		parser = new DTASelectParser(runId, f);
	}

	/**
	 * Gets a map of proteins. A single accession can be mapped to several
	 * proteins, because each protein is a protein in a particular msRun, and
	 * sometimes DTASelect contains proteins from multiple msRuns
	 *
	 * @return
	 */
	public HashMap<String, Set<Protein>> getProteins() {
		if (proteins == null) {
			proteins = new HashMap<String, Set<Protein>>();
			Collection<DTASelectProtein> dtaSelectProteins;
			try {
				dtaSelectProteins = parser.getDTASelectProteins().values();
				for (DTASelectProtein dtaSelectProtein : dtaSelectProteins) {
					final List<DTASelectPSM> psMs2 = dtaSelectProtein.getPSMs();
					// take the MSRuns from psms
					Set<String> msRunIDs = new HashSet<String>();
					for (DTASelectPSM dtaPSM : psMs2) {
						msRunIDs.add(dtaPSM.getRawFileName());
					}
					for (String msRunID : msRunIDs) {
						Protein protein = new ProteinImplFromDTASelect(dtaSelectProtein, msRunID);
						if (ProteomicsModelStaticStorage.containsProtein(msRunID, null, protein.getAccession())) {
							protein = ProteomicsModelStaticStorage.getSingleProtein(msRunID, null,
									protein.getAccession());
						} else {
							ProteomicsModelStaticStorage.addProtein(protein, msRunID, null);
						}

						if (!proteins.containsKey(protein.getAccession())) {
							Set<Protein> set = new HashSet<Protein>();
							set.add(protein);
							proteins.put(protein.getAccession(), set);
						} else {
							proteins.get(protein.getAccession()).add(protein);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return proteins;
	}

	/**
	 * Parse DTASelectProtein locus for getting the primary accession, parsing
	 * it accordingly
	 *
	 * @param dtaSelectProtein
	 * @return
	 */
	public static Accession getProteinAccessionFromDTASelectProtein(DTASelectProtein dtaSelectProtein) {
		if (dtaSelectProtein == null)
			return null;
		return getProteinAccessionFromDTASelectProtein(dtaSelectProtein.getLocus(), dtaSelectProtein.getDescription());
	}

	/**
	 * Parse DTASelectProtein locus for getting the primary accession, parsing
	 * it accordingly
	 *
	 * @param dtaSelectProtein
	 * @return
	 */
	public static Accession getProteinAccessionFromDTASelectProtein(String dtaSelectProteinLocus, String description) {

		String locus = dtaSelectProteinLocus;
		if (locus == null || "".equals(locus))
			return null;
		AccessionEx primaryAccession = null;

		final Pair<String, String> acc = FastaParser.getACC(locus);
		AccessionType accType = AccessionType.fromValue(acc.getSecondElement());
		if (accType == null) {
			accType = AccessionType.IPI;
		}
		primaryAccession = new AccessionEx(acc.getFirstelement(), accType);

		primaryAccession.setDescription(description);
		return primaryAccession;
	}

	public void setDecoyPattern(String decoyPattern) {
		parser.setDecoyPattern(decoyPattern);

	}

	public Set<String> getSpectraFileFullPaths() throws IOException {
		return parser.getSpectraFileFullPaths();
	}

	public DTASelectParser getParser() {
		return parser;
	}

	public Set<String> getSpectraFileNames() throws IOException {
		return parser.getSpectraFileNames();
	}

	public String getFastaPath() throws IOException {
		return parser.getFastaPath();
	}

	public String getDecoyPattern() throws IOException {
		return parser.getDecoyPattern();
	}

	public String getDTASelectVersion() throws IOException {
		return parser.getDTASelectVersion();
	}

	public String getSearchEngineVersion() throws IOException {
		return parser.getSearchEngineVersion();
	}

	public Set<String> getSearchEngines() throws IOException {
		return parser.getSearchEngines();
	}

	public DTASelectCommandLineParameters getCommandLineParameter() throws IOException {
		return parser.getCommandLineParameter();
	}

	public List<String> getCommandLineParameterString() throws IOException {
		return parser.getCommandLineParameterStrings();
	}

	public String getRunPath() throws IOException {
		return parser.getRunPath();
	}

	public Map<String, PSM> getPSMsByPSMID() {
		if (psms == null) {
			psms = new HashMap<String, PSM>();
			for (Set<Protein> proteinSet : getProteins().values()) {
				for (Protein dtaSelectProtein : proteinSet) {
					for (PSM psm : dtaSelectProtein.getPSMs()) {
						if (!psms.containsKey(psm.getPSMIdentifier())) {
							psms.put(psm.getPSMIdentifier(), psm);
						}
					}
				}
			}
		}
		return psms;
	}

}
