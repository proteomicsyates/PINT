package edu.scripps.yates.census.read.model.interfaces;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import edu.scripps.yates.census.read.model.IonSerie.IonSerieType;
import edu.scripps.yates.census.read.util.IonExclusion;

public interface IsobaricQuantParser extends QuantParser {

	HashMap<String, Set<String>> getSpectrumToIonsMap();

	void addIonExclusion(IonSerieType serieType, int ionNumber);

	void addIonExclusions(Collection<IonExclusion> ionExclusions);

}
