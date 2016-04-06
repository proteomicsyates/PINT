package edu.scripps.yates.census.read.util;

import edu.scripps.yates.census.read.model.IonSerie.IonSerieType;

public class IonExclusion {
	private final IonSerieType ionSerieType;
	private final int ionNumber;

	public IonExclusion(IonSerieType ionSerieType, int ionNumber) {
		this.ionSerieType = ionSerieType;
		this.ionNumber = ionNumber;
	}

	/**
	 * @return the ionSerieType
	 */
	public IonSerieType getIonSerieType() {
		return ionSerieType;
	}

	/**
	 * @return the ionNumber
	 */
	public int getIonNumber() {
		return ionNumber;
	}

}
