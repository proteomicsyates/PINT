package edu.scripps.yates.census.read;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;

import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.read.model.StaticMaps;
import edu.scripps.yates.census.read.model.interfaces.QuantParser;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPeptideInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedProteinInterface;
import edu.scripps.yates.census.read.util.QuantificationLabel;
import edu.scripps.yates.dbindex.DBIndexInterface;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.ipi.IPI2UniprotACCMap;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.model.factories.AccessionEx;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.remote.RemoteSSHFileReference;
import edu.scripps.yates.utilities.util.Pair;

public abstract class AbstractQuantParser implements QuantParser {
	private static final Logger log = Logger.getLogger(AbstractQuantParser.class);
	protected DBIndexInterface dbIndex;

	protected final List<RemoteSSHFileReference> remoteFileRetrievers = new ArrayList<RemoteSSHFileReference>();

	protected Pattern decoyPattern;
	public static Set<String> peptidesMissingInDB = new HashSet<String>();
	protected boolean ignoreNotFoundPeptidesInDB;

	// MAPS
	// key=experimentkey, values=proteinKeys
	protected final HashMap<String, Set<String>> experimentToProteinsMap = new HashMap<String, Set<String>>();
	// key=proteinkey, values=peptidekeys
	protected final HashMap<String, Set<String>> proteinToPeptidesMap = new HashMap<String, Set<String>>();
	// key=peptideKey, values=spectrumKeys
	protected final HashMap<String, Set<String>> peptideToSpectraMap = new HashMap<String, Set<String>>();

	// the key is the protein key
	protected final Map<String, QuantifiedProteinInterface> localProteinMap = new HashMap<String, QuantifiedProteinInterface>();
	// the key is the spectrum key
	protected final Map<String, QuantifiedPSMInterface> localPsmMap = new HashMap<String, QuantifiedPSMInterface>();
	// the key is the peptide key (the peptide sequence, distinguising between
	// modified or not, depending on 'distinguishModifiedPeptides' variable
	protected final Map<String, QuantifiedPeptideInterface> localPeptideMap = new HashMap<String, QuantifiedPeptideInterface>();
	// distinguish or nt between peptides modified or not for the map of
	// peptides
	protected boolean distinguishModifiedPeptides = true;
	protected final Set<String> taxonomies = new HashSet<String>();
	protected boolean processed = false;
	protected boolean chargeStateSensible = false;

	protected final Map<RemoteSSHFileReference, Map<QuantCondition, QuantificationLabel>> labelsByConditionsByFile = new HashMap<RemoteSSHFileReference, Map<QuantCondition, QuantificationLabel>>();

	protected final Map<RemoteSSHFileReference, QuantificationLabel> numeratorLabelByFile = new HashMap<RemoteSSHFileReference, QuantificationLabel>();
	protected final Map<RemoteSSHFileReference, QuantificationLabel> denominatorLabelByFile = new HashMap<RemoteSSHFileReference, QuantificationLabel>();

	public AbstractQuantParser() {

	}

	public AbstractQuantParser(List<RemoteSSHFileReference> remoteSSHServers,
			List<Map<QuantCondition, QuantificationLabel>> labelsByConditions, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator) {
		int index = 0;
		for (RemoteSSHFileReference remoteSSHServer : remoteSSHServers) {
			addFile(remoteSSHServer, labelsByConditions.get(index), labelNumerator, labelDenominator);
			index++;
		}
	}

	public AbstractQuantParser(Map<QuantCondition, QuantificationLabel> labelsByConditions,
			Collection<RemoteSSHFileReference> remoteSSHServers, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator) {

		for (RemoteSSHFileReference remoteSSHServer : remoteSSHServers) {
			addFile(remoteSSHServer, labelsByConditions, labelNumerator, labelDenominator);
		}
	}

	public AbstractQuantParser(RemoteSSHFileReference remoteSSHServer,
			Map<QuantCondition, QuantificationLabel> labelsByConditions, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator) throws FileNotFoundException {
		addFile(remoteSSHServer, labelsByConditions, labelNumerator, labelDenominator);
	}

	public AbstractQuantParser(File xmlFile, Map<QuantCondition, QuantificationLabel> labelsByConditions,
			QuantificationLabel labelNumerator, QuantificationLabel labelDenominator) throws FileNotFoundException {
		addFile(xmlFile, labelsByConditions, labelNumerator, labelDenominator);
	}

	public AbstractQuantParser(File[] xmlFiles, Map<QuantCondition, QuantificationLabel> labelsByConditions,
			QuantificationLabel labelNumerator, QuantificationLabel labelDenominator) throws FileNotFoundException {
		this(Arrays.asList(xmlFiles), labelsByConditions, labelNumerator, labelDenominator);
	}

	public AbstractQuantParser(File[] xmlFiles, Map<QuantCondition, QuantificationLabel>[] labelsByConditions,
			QuantificationLabel[] labelNumerator, QuantificationLabel[] labelDenominator) throws FileNotFoundException {
		for (int i = 0; i < xmlFiles.length; i++) {
			addFile(xmlFiles[i], labelsByConditions[i], labelNumerator[i], labelDenominator[i]);
		}
	}

	public AbstractQuantParser(Collection<File> xmlFiles, Map<QuantCondition, QuantificationLabel> labelsByConditions,
			QuantificationLabel labelNumerator, QuantificationLabel labelDenominator) throws FileNotFoundException {
		for (File xmlFile : xmlFiles) {
			addFile(xmlFile, labelsByConditions, labelNumerator, labelDenominator);
		}
	}

	public AbstractQuantParser(RemoteSSHFileReference remoteServer, QuantificationLabel label1, QuantCondition cond1,
			QuantificationLabel label2, QuantCondition cond2) {
		Map<QuantCondition, QuantificationLabel> map = new HashMap<QuantCondition, QuantificationLabel>();
		map.put(cond1, label1);
		map.put(cond2, label2);
		addFile(remoteServer, map, label1, label2);
	}

	public AbstractQuantParser(File inputFile, QuantificationLabel label1, QuantCondition cond1,
			QuantificationLabel label2, QuantCondition cond2) throws FileNotFoundException {
		Map<QuantCondition, QuantificationLabel> map = new HashMap<QuantCondition, QuantificationLabel>();
		map.put(cond1, label1);
		map.put(cond2, label2);
		addFile(inputFile, map, label1, label2);
	}

	@Override
	public void addFile(File xmlFile, Map<QuantCondition, QuantificationLabel> labelsByConditions,
			QuantificationLabel labelNumerator, QuantificationLabel labelDenominator) throws FileNotFoundException {
		if (!xmlFile.exists()) {
			throw new FileNotFoundException(xmlFile.getAbsolutePath() + " is not found in the file system");
		}
		final RemoteSSHFileReference remoteFileReference = new RemoteSSHFileReference(xmlFile);
		addFile(remoteFileReference, labelsByConditions, labelNumerator, labelDenominator);
	}

	@Override
	public void addFile(RemoteSSHFileReference remoteFileReference,
			Map<QuantCondition, QuantificationLabel> labelsByConditions, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator) {
		labelsByConditionsByFile.put(remoteFileReference, labelsByConditions);
		numeratorLabelByFile.put(remoteFileReference, labelNumerator);
		denominatorLabelByFile.put(remoteFileReference, labelDenominator);
		remoteFileRetrievers.add(remoteFileReference);
		clearStaticInfo();
		checkParameters();
	}

	/**
	 * It clears the static maps by protein keys and psm keys in
	 * {@link QuantifiedProteinInterface} and {@link QuantifiedPSMInterface}
	 * classes.<br>
	 * This method should be called at the beggining of an analysis in order to
	 * create just one {@link QuantifiedProteinInterface} for all the replicates
	 * and experiments for a given accession.
	 */
	public static void clearStaticInfo() {
		StaticMaps.proteinMap.clear();
		StaticMaps.psmMap.clear();
		StaticMaps.peptideMap.clear();
	}

	protected void checkParameters() {
		if (remoteFileRetrievers == null)
			throw new IllegalArgumentException("Input stream is null");

	}

	/**
	 * @return the chargeStateSensible
	 */
	public boolean isChargeStateSensible() {
		return chargeStateSensible;
	}

	/**
	 * @param chargeStateSensible
	 *            the chargeStateSensible to set: It means to distinguish or not
	 *            the distinct charges when getting data from the peptides with
	 *            the same sequence
	 */
	@Override
	public void setChargeStateSensible(boolean chargeStateSensible) {
		this.chargeStateSensible = chargeStateSensible;
	}

	@Override
	public void setDecoyPattern(String patternString) throws PatternSyntaxException {
		if (patternString != null) {
			decoyPattern = Pattern.compile(patternString);
		}
	}

	/**
	 * @return the remoteFileRetrievers
	 */
	@Override
	public List<RemoteSSHFileReference> getRemoteFileRetrievers() {
		return remoteFileRetrievers;
	}

	/**
	 * @return the proteinMap
	 */
	@Override
	public HashMap<String, Set<String>> getProteinToPeptidesMap() {
		if (!processed)
			process();
		return proteinToPeptidesMap;
	}

	/**
	 * @return the peptideMap
	 */
	@Override
	public HashMap<String, Set<String>> getPeptideToSpectraMap() {
		if (!processed)
			process();
		return peptideToSpectraMap;
	}

	/**
	 * Gets the Protein by Protein key.
	 *
	 * @return the proteinMap
	 */
	@Override
	public final Map<String, QuantifiedProteinInterface> getProteinMap() {
		if (!processed) {
			process();
			expandProteinMap();
		}
		return localProteinMap;
	}

	@Override
	public final Map<String, QuantifiedPSMInterface> getPSMMap() {
		if (!processed)
			process();
		return localPsmMap;
	}

	/**
	 * @param dbIndex
	 *            the dbIndex to set
	 */
	@Override
	public void setDbIndex(DBIndexInterface dbIndex) {
		this.dbIndex = dbIndex;
	}

	@Override
	public Set<String> getTaxonomies() {
		if (!processed)
			process();
		return taxonomies;
	}

	/**
	 * @return the peptideMap
	 */
	@Override
	public Map<String, QuantifiedPeptideInterface> getPeptideMap() {
		if (!processed)
			process();
		return localPeptideMap;
	}

	/**
	 * @return the distinguishModifiedPeptides
	 */
	@Override
	public boolean isDistinguishModifiedPeptides() {
		return distinguishModifiedPeptides;
	}

	/**
	 * @param distinguishModifiedPeptides
	 *            the distinguishModifiedPeptides to set
	 */
	@Override
	public void setDistinguishModifiedPeptides(boolean distinguishModifiedPeptides) {
		this.distinguishModifiedPeptides = distinguishModifiedPeptides;
	}

	protected abstract void process();

	protected void addToMap(String key, HashMap<String, Set<String>> map, String value) {
		if (map.containsKey(key)) {
			map.get(key).add(value);
		} else {
			Set<String> set = new HashSet<String>();
			set.add(value);
			map.put(key, set);
		}

	}

	/**
	 * @return the ignoreNotFoundPeptidesInDB
	 */
	public boolean isIgnoreNotFoundPeptidesInDB() {
		return ignoreNotFoundPeptidesInDB;
	}

	/**
	 * @param ignoreNotFoundPeptidesInDB
	 *            the ignoreNotFoundPeptidesInDB to set
	 */
	public void setIgnoreNotFoundPeptidesInDB(boolean ignoreNotFoundPeptidesInDB) {
		this.ignoreNotFoundPeptidesInDB = ignoreNotFoundPeptidesInDB;
	}

	/**
	 * Gets a set of Uniprot Accessions from the protein set in the parser. If
	 * the accessions are not Uniprot formatted, they are not retrieved here.
	 * <br>
	 * This function is used for getting annotations in uniprot for the proteins
	 * that are actually from uniprot.
	 *
	 * @return
	 */
	public Set<String> getUniprotAccSet() {
		Set<String> ret = new HashSet<String>();
		final Set<String> keySet = getProteinMap().keySet();
		for (String acc : keySet) {
			if (FastaParser.getUniProtACC(acc) != null) {
				ret.add(acc);
			}
		}
		return ret;
	}

	/**
	 * To be called after process()
	 */
	private void expandProteinMap() {
		if (!localProteinMap.isEmpty()) {
			int originalNumberOfEntries = localProteinMap.size();
			Map<String, QuantifiedProteinInterface> newMap = new HashMap<String, QuantifiedProteinInterface>();
			for (String accession : localProteinMap.keySet()) {
				final QuantifiedProteinInterface quantProtein = localProteinMap.get(accession);
				final Pair<String, String> acc = FastaParser.getACC(accession);
				if (acc.getSecondElement().equals("IPI")) {
					if (acc.getFirstelement().equals("IPI00114389.4")) {
						log.info("asdf");
					}
					Accession primaryAccession = new AccessionEx(accession, AccessionType.IPI);
					Pair<Accession, Set<Accession>> pair = IPI2UniprotACCMap.getInstance()
							.getPrimaryAndSecondaryAccessionsFromIPI(primaryAccession);
					if (pair.getFirstelement() != null) {
						primaryAccession = pair.getFirstelement();
						if (!newMap.containsKey(primaryAccession)) {
							newMap.put(primaryAccession.getAccession(), quantProtein);
						}
					}
					final Set<Accession> secondaryAccs = pair.getSecondElement();
					if (secondaryAccs != null) {
						for (Accession secondaryAcc : secondaryAccs) {
							if (!newMap.containsKey(secondaryAcc.getAccession())) {
								newMap.put(secondaryAcc.getAccession(), quantProtein);
							}
						}

					}
				}
			}
			for (String acc : newMap.keySet()) {
				if (!localProteinMap.containsKey(acc)) {
					localProteinMap.put(acc, newMap.get(acc));
				}
			}
			if (originalNumberOfEntries != localProteinMap.size()) {
				log.info("Protein Map expanded from " + originalNumberOfEntries + " to " + localProteinMap.size());
			}
		}
	}
}
