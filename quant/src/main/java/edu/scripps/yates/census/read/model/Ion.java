package edu.scripps.yates.census.read.model;

import edu.scripps.yates.census.read.util.QuantificationLabel;

public class Ion {
	private final double mass;
	private final double intensity;
	private final int ionNumber;
	private IsoRatio ratio;
	private final QuantificationLabel label;
	private boolean singleton = false;

	public Ion(int ionNumber, double mass, double intensity, QuantificationLabel label) {
		super();
		this.mass = mass;
		this.intensity = intensity;
		this.ionNumber = ionNumber;
		this.label = label;
	}

	/**
	 * @return the mass
	 */
	public double getMass() {
		return mass;
	}

	/**
	 * @return the intensity
	 */
	public double getIntensity() {
		return intensity;
	}

	/**
	 * @return the ionNumber
	 */
	public int getIonNumber() {
		return ionNumber;
	}

	public void setRatio(IsoRatio censusRatio) {
		ratio = censusRatio;

	}

	public IsoRatio getRatio() {
		return ratio;
	}

	public QuantificationLabel getLabel() {
		return label;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Ion " + label + " " + ionNumber + "[M=" + mass + ", I=" + intensity + ", ratio=" + ratio + "]";
	}

	/**
	 * @return the singleton
	 */
	public boolean isSingleton() {
		return singleton;
	}

	/**
	 * @param singleton
	 *            the singleton to set
	 */
	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

}
