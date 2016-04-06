package edu.scripps.yates.census.analysis.wrappers;

public class SanxotQuantResult {
	private final String key;
	private final double log2ratio;
	private final double weight;
	private double fdr;
	private double zValue;

	/**
	 * Create a {@link SanxotQuantResult} stating the main features:
	 *
	 * @param key
	 * @param log2ratio
	 * @param weight
	 */
	public SanxotQuantResult(String key, double log2ratio, double weight) {
		super();
		this.key = key;
		this.log2ratio = log2ratio;
		this.weight = weight;
	}

	/**
	 * Create a {@link SanxotQuantResult} by parsing the main features from a
	 * string in which TAB separated values are readed. <br>
	 * This line can come from files with sufix 'lowerV' or 'lowerW', which
	 * contains 3 columns where:
	 * <ul>
	 * <li>first value is the key of the quant (protein ACC, peptide seq,
	 * protein cluster key...)</li>
	 * <li>second value is the log2ratio</li>
	 * <li>third value is the quantification weight</li>
	 * </ul>
	 * <br>
	 * or from a file with sufix 'outStats' which contains 9 columns where:
	 * <ul>
	 * <li>the key of the quant (protein ACC, peptide seq, protein cluster
	 * key...) in the 4th column</li>
	 * <li>the log2ratio in the 5th column</li>
	 * <li>the weight (Vinf) in the 6th column</li>
	 * <li>the Z value in the 8th column</li>
	 * <li>the FDR value in the 9th column</li>
	 * </ul>
	 *
	 * @param parseableLine
	 */
	public SanxotQuantResult(String parseableLine) {
		super();
		try {
			final String[] split = parseableLine.split("\t");
			if (split.length == 3) {

				key = split[0];
				log2ratio = Double.valueOf(split[1]);
				weight = Double.valueOf(split[2]);
			} else if (split.length == 9) {
				key = split[3];
				log2ratio = Double.valueOf(split[4]);
				weight = Double.valueOf(split[5]);
				// zValue can be 'nan' in the file
				try {
					zValue = Double.valueOf(split[7]);
				} catch (NumberFormatException e) {
					zValue = Double.NaN;
				}
				// fdr can be 'nan' in the file
				try {
					fdr = Double.valueOf(split[8]);
				} catch (NumberFormatException e) {
					fdr = Double.NaN;
				}
			} else {
				throw new IllegalArgumentException(
						"Irrecognized line '" + parseableLine + "'. It can only have either 3 or 9 columns");
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the log2ratio
	 */
	public double getLog2ratio() {
		return log2ratio;
	}

	/**
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * @return the fdr
	 */
	public double getFdr() {
		return fdr;
	}

	/**
	 * @param fdr
	 *            the fdr to set
	 */
	public void setFdr(double fdr) {
		this.fdr = fdr;
	}

	/**
	 * @return the zValue
	 */
	public double getzValue() {
		return zValue;
	}

	/**
	 * @param zValue
	 *            the zValue to set
	 */
	public void setzValue(double zValue) {
		this.zValue = zValue;
	}

	public double getNonLog2ratio() {
		return Math.pow(2.0, log2ratio);
	}

}
