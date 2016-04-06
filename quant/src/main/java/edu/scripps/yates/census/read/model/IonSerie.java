package edu.scripps.yates.census.read.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.census.read.util.IonExclusion;
import edu.scripps.yates.census.read.util.QuantificationLabel;

public class IonSerie {
	private static final String NO_PEAK = "NP";
	private static final String MULTI_PEAK = "MP";
	private static final String MSC = "MSC";
	private int maxNumberIon = 0;
	private final HashMap<Integer, Ion> ionMap = new HashMap<Integer, Ion>();

	public enum IonSerieType {
		Y, B
	};

	private static Logger log = Logger.getLogger(IonSerie.class);
	private final IonSerieType ionSerieType;
	private final QuantificationLabel label;

	public IonSerie(QuantificationLabel label, IonSerieType ionSerieType, String rawString,
			Collection<IonExclusion> ionExclusions) {
		this.label = label;
		this.ionSerieType = ionSerieType;

		parseRawString(rawString, ionExclusions);
	}

	private void parseRawString(String rawString, Collection<IonExclusion> ionExclusions) {
		final String[] split = rawString.split("\\s");
		int ionNumber = 0;
		for (int i = 0; i < split.length - 1; i = i + 2) {
			ionNumber++;
			if (excludeIon(ionNumber, ionSerieType, ionExclusions)) {
				// log.debug("Excluding ion " + ionSerieType + " ion number "
				// + ionNumber);
				continue;
			}
			double mass = Double.valueOf(split[i]);

			if (split[i + 1].equals(NO_PEAK)) {
				// NP: No peak, NO LABELLED
			} else if (split[i + 1].equals(MULTI_PEAK)) {
				// MP: multi peak, identified but no quantified. NO LABELLED
			} else if (split[i + 1].equals(MSC)) {
				// MSC: discard it
			} else {
				Long intensity = parseIntensity(split[i + 1]);
				if (intensity != null) {
					Ion ion = new Ion(ionNumber, mass, intensity, label);
					ionMap.put(ionNumber, ion);
					maxNumberIon = ionNumber;
				} else {
					log.warn("this cannot happen!");
				}
			}
		}
	}

	private boolean excludeIon(int ionNumber, IonSerieType ionSerieType2, Collection<IonExclusion> ionExclusions) {
		if (ionExclusions != null) {
			for (IonExclusion ionExclusion : ionExclusions) {
				if (ionExclusion.getIonSerieType().equals(ionSerieType2) && ionExclusion.getIonNumber() == ionNumber) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Removes the ion indexed with the numIon and updates the isLabelled()
	 * according to that
	 *
	 * @param numIon
	 */
	public void removeIon(int numIon) {
		ionMap.remove(numIon);

	}

	private Long parseIntensity(String string) {
		try {
			final Double valueOf = Double.valueOf(string);
			if (valueOf != null)
				return valueOf.longValue();
		} catch (NumberFormatException e) {

		}

		return null;
	}

	/**
	 * @return the numIons
	 */
	public int getMaxNumberIon() {
		return maxNumberIon;
	}

	public Ion getIon(int ionNumber) {
		return ionMap.get(ionNumber);
	}

	public HashMap<Integer, Ion> getIonMap() {
		return ionMap;
	}

	/**
	 * @return the ionSerieType
	 */
	public IonSerieType getIonSerieType() {
		return ionSerieType;
	}

	/**
	 * get the label of the ion series just in case it has been labeled, which
	 * means that at least one valid peak has been found. Otherwise, will return
	 * null.
	 *
	 * @return the label
	 */
	public QuantificationLabel getLabel() {
		if (isLabeled())
			return label;
		return null;
	}

	/**
	 * get the label of the ion series just in case it has been labeled in at
	 * least one singleton ion, which means that at least one valid peak has
	 * been found and is not composing any ratio. Otherwise, it will return
	 * null.
	 *
	 * @return the label
	 */
	public QuantificationLabel getSingletonLabel() {
		if (isSingletonLabeled())
			return label;
		return null;
	}

	/**
	 * It is labeled just if contains at least one ion that it is not composing
	 * a ratio.
	 *
	 * @return the isLabelled
	 */
	public boolean isSingletonLabeled() {
		for (Ion ion : ionMap.values()) {
			if (ion.getRatio() == null)
				return true;
		}
		return false;
	}

	/**
	 * It is labeled if contains at least one ion
	 *
	 * @return the isLabelled
	 */
	public boolean isLabeled() {

		return !ionMap.isEmpty();
	}

	/**
	 * Gets the non null ions
	 *
	 * @return
	 */
	public Set<Ion> getNonNullIons() {

		Set<Ion> ret = new HashSet<Ion>();
		for (Ion ion : ionMap.values()) {
			ret.add(ion);
		}

		return ret;
	}

	/**
	 * Gets the singleton Ions, which are the one that are not null and they are
	 * not composing a ratio, of if they are composing a ratio, this is INFINITE
	 *
	 * @return
	 */
	public Set<Ion> getSingletonIons() {
		Set<Ion> list = new HashSet<Ion>();
		for (Ion ion : ionMap.values()) {
			if (ion.getRatio() == null)
				list.add(ion);
			else if (ion.isSingleton()) {
				list.add(ion);
			}
		}
		return list;
	}

	/**
	 * Gets the label of the serie, even if the serie has not ions, and
	 * therefore isLabeled() == false
	 *
	 * @return
	 */
	public QuantificationLabel getNonNullLabel() {
		return label;
	}
}
