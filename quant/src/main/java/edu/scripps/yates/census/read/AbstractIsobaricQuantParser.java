package edu.scripps.yates.census.read;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.read.model.IonSerie.IonSerieType;
import edu.scripps.yates.census.read.model.interfaces.IsobaricQuantParser;
import edu.scripps.yates.census.read.util.IonExclusion;
import edu.scripps.yates.census.read.util.QuantificationLabel;
import edu.scripps.yates.utilities.remote.RemoteSSHFileReference;

public abstract class AbstractIsobaricQuantParser extends AbstractQuantParser implements IsobaricQuantParser {
	// key=spectrumKeys, values=ionKeys
	protected final HashMap<String, Set<String>> spectrumToIonsMap = new HashMap<String, Set<String>>();
	protected final Set<String> ionKeys = new HashSet<String>();

	// ion exclusions
	protected final List<IonExclusion> ionExclusions = new ArrayList<IonExclusion>();

	public AbstractIsobaricQuantParser() {
		super();
	}

	public AbstractIsobaricQuantParser(List<RemoteSSHFileReference> remoteSSHServers,
			List<Map<QuantCondition, QuantificationLabel>> labelsByConditions, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator) {
		super(remoteSSHServers, labelsByConditions, labelNumerator, labelDenominator);
	}

	public AbstractIsobaricQuantParser(Map<QuantCondition, QuantificationLabel> labelsByConditions,
			Collection<RemoteSSHFileReference> remoteSSHServers, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator) {
		super(labelsByConditions, remoteSSHServers, labelNumerator, labelDenominator);
	}

	public AbstractIsobaricQuantParser(RemoteSSHFileReference remoteSSHServer,
			Map<QuantCondition, QuantificationLabel> labelsByConditions, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator) throws FileNotFoundException {
		super(remoteSSHServer, labelsByConditions, labelNumerator, labelDenominator);
	}

	public AbstractIsobaricQuantParser(File xmlFile, Map<QuantCondition, QuantificationLabel> labelsByConditions,
			QuantificationLabel labelNumerator, QuantificationLabel labelDenominator) throws FileNotFoundException {
		super(xmlFile, labelsByConditions, labelNumerator, labelDenominator);
	}

	public AbstractIsobaricQuantParser(File[] xmlFiles, Map<QuantCondition, QuantificationLabel> labelsByConditions,
			QuantificationLabel labelNumerator, QuantificationLabel labelDenominator) throws FileNotFoundException {
		super(xmlFiles, labelsByConditions, labelNumerator, labelDenominator);
	}

	public AbstractIsobaricQuantParser(File[] xmlFiles, Map<QuantCondition, QuantificationLabel>[] labelsByConditions,
			QuantificationLabel[] labelNumerator, QuantificationLabel[] labelDenominator) throws FileNotFoundException {
		super(xmlFiles, labelsByConditions, labelNumerator, labelDenominator);
	}

	public AbstractIsobaricQuantParser(Collection<File> xmlFiles,
			Map<QuantCondition, QuantificationLabel> labelsByConditions, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator) throws FileNotFoundException {
		super(xmlFiles, labelsByConditions, labelNumerator, labelDenominator);
	}

	public AbstractIsobaricQuantParser(RemoteSSHFileReference remoteServer, QuantificationLabel label1,
			QuantCondition cond1, QuantificationLabel label2, QuantCondition cond2) {
		super(remoteServer, label1, cond1, label2, cond2);
	}

	public AbstractIsobaricQuantParser(File inputFile, QuantificationLabel label1, QuantCondition cond1,
			QuantificationLabel label2, QuantCondition cond2) throws FileNotFoundException {
		super(inputFile, label1, cond1, label2, cond2);
	}

	/**
	 * Gets the set of spectrum to ions map
	 *
	 * @return the spectrumMap
	 */
	@Override
	public HashMap<String, Set<String>> getSpectrumToIonsMap() {
		if (!processed)
			process();
		return spectrumToIonsMap;
	}

	/**
	 * @param excludeY1Ions
	 *            the excludeY1Ions to set
	 */
	@Override
	public void addIonExclusion(IonSerieType serieType, int ionNumber) {
		ionExclusions.add(new IonExclusion(serieType, ionNumber));
	}

	/**
	 * @param excludeY1Ions
	 *            the excludeY1Ions to set
	 */
	public void addIonExclusion(IonExclusion ionExclusion) {
		ionExclusions.add(ionExclusion);
	}

	/**
	 * @param excludeY1Ions
	 *            the excludeY1Ions to set
	 */
	@Override
	public void addIonExclusions(Collection<IonExclusion> ionExclusions) {
		this.ionExclusions.addAll(ionExclusions);
	}

	/**
	 * @return the ionExclusions
	 */
	public List<IonExclusion> getIonExclusions() {
		return ionExclusions;
	}

	@Override
	protected abstract void process();

}
