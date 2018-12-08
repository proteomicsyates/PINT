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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import edu.scripps.yates.dtaselectparser.DTASelectCommandLineParameters;
import edu.scripps.yates.dtaselectparser.DTASelectParser;
import edu.scripps.yates.dtaselectparser.util.DTASelectProtein;
import edu.scripps.yates.utilities.parsers.idparser.IdentifiedPSMInterface;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.StaticProteomicsModelStorage;
import edu.scripps.yates.utilities.remote.RemoteSSHFileReference;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class ProteinDTASelectParser {
	private static final Logger log = Logger.getLogger(ProteinDTASelectParser.class);
	private final DTASelectParser parser;
	boolean dtaselect;
	private Map<String, Set<Protein>> proteins;
	private Map<String, PSM> psms;

	public ProteinDTASelectParser(URL u) throws IOException {
		this(FilenameUtils.getBaseName(u.getFile()), u.openStream());
	}

	public ProteinDTASelectParser(String runId, RemoteSSHFileReference s) throws IOException {
		this(runId, s.getRemoteInputStream());
	}

	public ProteinDTASelectParser(RemoteSSHFileReference s) throws IOException {
		this(s.getRemoteFileName(), new FileInputStream(s.getRemoteFile()));
	}

	public ProteinDTASelectParser(Map<String, RemoteSSHFileReference> s) throws IOException {
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
	public Map<String, Set<Protein>> getProteins() {
		if (proteins == null) {
			proteins = new THashMap<String, Set<Protein>>();
			Collection<Protein> dtaSelectProteins;
			try {
				dtaSelectProteins = parser.getProteins();
				for (final DTASelectProtein dtaSelectProtein : dtaSelectProteins) {
					final List<IdentifiedPSMInterface> psMs2 = dtaSelectProtein.getPSMs();
					// take the MSRuns from psms
					final Set<String> msRunIDs = new THashSet<String>();

					for (final IdentifiedPSMInterface dtaPSM : psMs2) {
						msRunIDs.add(dtaPSM.getRawFileName());
					}

					for (final String msRunID : msRunIDs) {
						Protein protein = new ProteinImplFromDTASelect(dtaSelectProtein, msRunID, false);

						if (StaticProteomicsModelStorage.containsProtein(msRunID, null, protein.getAccession())) {
							protein = StaticProteomicsModelStorage.getSingleProtein(msRunID, null,
									protein.getAccession());
						} else {
							StaticProteomicsModelStorage.addProtein(protein, msRunID, null);
						}

						if (!proteins.containsKey(protein.getAccession())) {
							final Set<Protein> set = new THashSet<Protein>();
							set.add(protein);
							proteins.put(protein.getAccession(), set);
						} else {
							proteins.get(protein.getAccession()).add(protein);
						}
					}
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return proteins;
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
		return parser.getSoftwareVersion();
	}

	public String getSearchEngineVersion() throws IOException {
		return parser.getSearchEngineVersion();
	}

	public Set<String> getSearchEngines() throws IOException {
		return parser.getSearchEngines();
	}

	public DTASelectCommandLineParameters getCommandLineParameter() throws IOException {
		return (DTASelectCommandLineParameters) parser.getCommandLineParameter();
	}

	public List<String> getCommandLineParameterString() throws IOException {
		return parser.getCommandLineParameterStrings();
	}

	public String getRunPath() throws IOException {
		return parser.getRunPath();
	}

	public Map<String, PSM> getPSMsByPSMID() {
		if (psms == null) {
			psms = new THashMap<String, PSM>();
			for (final Set<Protein> proteinSet : getProteins().values()) {
				for (final Protein dtaSelectProtein : proteinSet) {
					for (final PSM psm : dtaSelectProtein.getPSMs()) {
						if (!psms.containsKey(psm.getIdentifier())) {
							psms.put(psm.getIdentifier(), psm);
						}
					}
				}
			}
		}
		return psms;
	}

}
