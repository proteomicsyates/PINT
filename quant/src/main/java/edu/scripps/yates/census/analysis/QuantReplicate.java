package edu.scripps.yates.census.analysis;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.census.read.model.IonSerie.IonSerieType;
import edu.scripps.yates.census.read.model.interfaces.IsobaricQuantParser;
import edu.scripps.yates.census.read.model.interfaces.QuantParser;
import edu.scripps.yates.census.read.util.QuantificationLabel;

public class QuantReplicate {
	private final Map<QuantCondition, QuantificationLabel> labelsByConditions = new HashMap<QuantCondition, QuantificationLabel>();
	private final Map<QuantificationLabel, QuantCondition> conditionsByLabels = new HashMap<QuantificationLabel, QuantCondition>();
	private final String name;
	private final QuantParser parser;

	public QuantReplicate(String name, QuantParser quantParser,
			Map<QuantCondition, QuantificationLabel> labelsByConditions, String decoyRegexp)
					throws FileNotFoundException {
		this.name = name;
		parser = quantParser;
		parser.setDecoyPattern(decoyRegexp);
		this.labelsByConditions.putAll(labelsByConditions);

		for (QuantCondition cond : labelsByConditions.keySet()) {
			conditionsByLabels.put(labelsByConditions.get(cond), cond);
		}
	}

	public void addIonExclusion(IonSerieType serieType, int ionNumber) {
		if (parser instanceof IsobaricQuantParser) {
			((IsobaricQuantParser) parser).addIonExclusion(serieType, ionNumber);
		} else {
			throw new IllegalArgumentException("addIonExclusion is only valid for IsobaricQuantParser");
		}
	}

	/**
	 * @return the labelsByConditions
	 */
	public Map<QuantCondition, QuantificationLabel> getLabelsByConditions() {
		return labelsByConditions;
	}

	/**
	 * @return the conditionsByLabels
	 */
	public Map<QuantificationLabel, QuantCondition> getConditionsByLabels() {
		return conditionsByLabels;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the parser
	 */
	public QuantParser getParser() {
		return parser;
	}

	public void setChargeStateSensible(boolean chargeSensible) {
		if (parser != null) {
			parser.setChargeStateSensible(chargeSensible);
		}

	}

}
