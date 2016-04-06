package edu.scripps.yates.dbindex;

/**
 * Represents a mass range to be passed to sequences query
 * 
 * @author Adam
 */
public class MassRange {

	private final double precMass;
	private final double tolerance;

	/**
	 * Create new mass range
	 * 
	 * @param precMass
	 *            precursor mass
	 * @param tolerance
	 *            mass dependant pre-calculated tolerance for that mass to use
	 *            example: resulting mass range will be <precMass-tolerance,
	 *            precMass+tolerance>
	 */
	public MassRange(double precMass, double tolerance) {
		this.precMass = precMass;
		this.tolerance = tolerance;
	}

	public double getPrecMass() {
		return precMass;
	}

	public double getTolerance() {
		return tolerance;
	}

	@Override
	public String toString() {
		return "MassRange{" + "precMass=" + precMass + ", tolerance="
				+ tolerance + '}';
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 29 * hash
				+ Long.valueOf(Double.doubleToLongBits(precMass)).intValue();
		hash = 29 * hash
				+ Long.valueOf(Double.doubleToLongBits(tolerance)).intValue();
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final MassRange other = (MassRange) obj;
		if (Double.doubleToLongBits(precMass) != Double
				.doubleToLongBits(other.precMass)) {
			return false;
		}
		if (Double.doubleToLongBits(tolerance) != Double
				.doubleToLongBits(other.tolerance)) {
			return false;
		}
		return true;
	}

}
