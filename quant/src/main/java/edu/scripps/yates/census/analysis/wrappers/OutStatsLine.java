package edu.scripps.yates.census.analysis.wrappers;

public class OutStatsLine {
	private final String idsup;
	private final Double Xsup;
	private final Double Vsup;
	private final String idinf;
	private final Double Xinf;
	private final Double Vinf;
	private final int n;
	private final Double Z;
	private final Double FDR;

	public OutStatsLine(String line) {
		final String[] split = line.split("\t");
		if (split.length != 9)
			throw new IllegalArgumentException(
					"OutStats line not recognized: '" + line + "'");
		idsup = split[0];
		if (split[1].equalsIgnoreCase("nan"))
			Xsup = Double.NaN;
		else
			Xsup = Double.valueOf(split[1]);

		if (split[2].equalsIgnoreCase("nan"))
			Vsup = Double.NaN;
		else
			Vsup = Double.valueOf(split[2]);

		idinf = split[3];

		if (split[4].equalsIgnoreCase("nan"))
			Xinf = Double.NaN;
		else
			Xinf = Double.valueOf(split[4]);

		if (split[5].equalsIgnoreCase("nan"))
			Vinf = Double.NaN;
		else
			Vinf = Double.valueOf(split[5]);
		n = Integer.valueOf(split[6]);

		if (split[7].equalsIgnoreCase("nan"))
			Z = Double.NaN;
		else
			Z = Double.valueOf(split[7]);

		if (split[8].equalsIgnoreCase("nan"))
			FDR = Double.NaN;
		else
			FDR = Double.valueOf(split[8]);
	}

	/**
	 * @return the idsup
	 */
	public String getIdsup() {
		return idsup;
	}

	/**
	 * @return the xsup
	 */
	public Double getXsup() {
		return Xsup;
	}

	/**
	 * @return the vsup
	 */
	public Double getVsup() {
		return Vsup;
	}

	/**
	 * @return the idinf
	 */
	public String getIdinf() {
		return idinf;
	}

	/**
	 * @return the xinf
	 */
	public Double getXinf() {
		return Xinf;
	}

	/**
	 * @return the vinf
	 */
	public Double getVinf() {
		return Vinf;
	}

	/**
	 * @return the n
	 */
	public int getN() {
		return n;
	}

	/**
	 * @return the z
	 */
	public Double getZ() {
		return Z;
	}

	/**
	 * @return the fDR
	 */
	public Double getFDR() {
		return FDR;
	}

}
