package edu.scripps.yates.census.read.model.interfaces;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.read.util.QuantificationLabel;
import edu.scripps.yates.dbindex.DBIndexInterface;
import edu.scripps.yates.utilities.remote.RemoteSSHFileReference;

public interface QuantParser {

	void addFile(File xmlFile, Map<QuantCondition, QuantificationLabel> labelsByConditions,
			QuantificationLabel labelNumerator, QuantificationLabel labelDenominator) throws FileNotFoundException;

	void addFile(RemoteSSHFileReference remoteFileReference,
			Map<QuantCondition, QuantificationLabel> labelsByConditions, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator);

	void setDistinguishModifiedPeptides(boolean distinguishModifiedPeptides);

	boolean isDistinguishModifiedPeptides();

	Map<String, QuantifiedPeptideInterface> getPeptideMap();

	Set<String> getTaxonomies();

	void setDbIndex(DBIndexInterface dbIndex);

	Map<String, QuantifiedPSMInterface> getPSMMap();

	Map<String, QuantifiedProteinInterface> getProteinMap();

	HashMap<String, Set<String>> getPeptideToSpectraMap();

	HashMap<String, Set<String>> getProteinToPeptidesMap();

	List<RemoteSSHFileReference> getRemoteFileRetrievers();

	void setDecoyPattern(String patternString) throws PatternSyntaxException;

	void setChargeStateSensible(boolean chargeSensible);

	Set<String> getUniprotAccSet();

	void setIgnoreNotFoundPeptidesInDB(boolean ignoreNotFoundPeptidesInDB);

	boolean isIgnoreNotFoundPeptidesInDB();

}
