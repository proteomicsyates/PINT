package edu.scripps.yates.utilities.masses;

public class MassesUtil {
	// proton mass
	public static final double H = 1.007825;

	/**
	 * m/z = (MW + zH+)/z<br>
	 * m/z = ((MW+H+) - H+ + zH+ )/z
	 *
	 * @param mass_plus_zh
	 *            as MW+H+
	 * @param z
	 * @return
	 */
	public static double getMassToCharge(double mass_plus_h, int z) {
		// substract a H+
		double m = mass_plus_h - H;
		// sum the mass of the H times the charge
		final double m_plus_zh = m + z * H;
		// divide by charge
		final double mz = m_plus_zh / z;
		return mz;

	}

	/**
	 * (M+H+) = z * (mz - H+) + H+
	 *
	 * @param mz
	 * @param z
	 * @return
	 */
	public static double getMplusH(double mz, int z) {
		// multiply by charge
		double m_plus_zh = mz * z;
		// substract the mass of the H times the charge
		double m = m_plus_zh - H * z;
		// add a H+
		double mass_plus_h = m + H;
		return mass_plus_h;
	}
}
