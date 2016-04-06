package edu.scripps.yates.utilities.sequence;

import org.apache.log4j.Logger;

/**
 * Calculation of different biophysical properties on peptide sequences.
 *
 * @author Salva
 *
 */
public class PeptideSequenceProperties {
	private static Logger log = Logger.getLogger(PeptideSequenceProperties.class);

	/**
	 * Method taken from DTAFile class from DTASelect project code
	 *
	 * @param sequence
	 * @return
	 */
	public static float calculatepI(String sequence) {
		if (sequence == null || "".equals(sequence)) {
			throw new IllegalArgumentException("Sequence is null or empty. pI cannot be calculated");
		}
		int length = sequence.length();
		int looper;
		int countLys = 0;
		int countArg = 0;
		int countHis = 0;
		int countAsp = 0;
		int countGlu = 0;
		int countCys = 0;
		int countTyr = 0;
		float currentPH = 7.0f;
		float currentJump = 3.5f;
		float currentCharge;
		float lastCharge = 0;
		char currentResidue;
		if (length > 0) {
			for (looper = 0; looper < length; looper++) {
				currentResidue = sequence.charAt(looper);
				switch (currentResidue) {
				case 'A':
					break;
				case 'C':
					countCys++;
					break;
				case 'D':
					countAsp++;
					break;
				case 'E':
					countGlu++;
					break;
				case 'F':
					break;
				case 'G':
					break;
				case 'H':
					countHis++;
					break;
				case 'I':
					break;
				case 'K':
					countLys++;
					break;
				case 'L':
					break;
				case 'M':
					break;
				case 'N':
					break;
				case 'P':
					break;
				case 'Q':
					break;
				case 'R':
					countArg++;
					break;
				case 'S':
					break;
				case 'T':
					break;
				case 'V':
					break;
				case 'W':
					break;
				case 'Y':
					countTyr++;
					break;
				}
			}
			/*
			 * Use a bracketing strategy to identify the isoelectric point.
			 * Calculate charge at pH of 7, and then move up 3.5 or down 3.5 as
			 * necessary. Make each successive move up or down only half as
			 * large. Keep going until two successive charges reported match to
			 * one place past the decimal point.
			 */
			currentCharge = chargeAtPH(currentPH, countLys, countArg, countHis, countAsp, countGlu, countCys, countTyr);
			while (roundTo(currentCharge, 1) != roundTo(lastCharge, 1)) {
				// System.out.println("pH:\t" + new Float(currentPH).toString()
				// + "\tcharge\t" + new Float(currentCharge).toString());
				if (currentCharge > 0)
					currentPH += currentJump;
				else
					currentPH -= currentJump;
				currentJump /= 2;
				lastCharge = currentCharge;
				currentCharge = chargeAtPH(currentPH, countLys, countArg, countHis, countAsp, countGlu, countCys,
						countTyr);
				if ((currentPH > 14) || (currentPH < 0)) {
					final String message = "pI can't be figured for peptide " + sequence;
					log.error(message);
					throw new IllegalArgumentException(message);
				}
			}
		}
		return currentPH;
	}

	// Because I don't like the Math.roundTo function, I wrote this one.
	private static float roundTo(float Value, int Places) {
		// Converts a value to a rounded value
		if (Places == 0) {
			return new Integer(Math.round(Value)).intValue();
		} else {
			double Multiplier = Math.pow(10, Places);
			return new Double(Math.rint(Value * Multiplier) / Multiplier).floatValue();
		}
	}

	/*
	 * Determine the charge on a theoretical protein at a given pH given its
	 * relevant composition
	 */
	public static float chargeAtPH(float pH, int countLys, int countArg, int countHis, int countAsp, int countGlu,
			int countCys, int countTyr) {
		// Start out accumulator with charge of termini
		float accum = percentPositive(pH, 8.0f) - percentNegative(pH, 3.1f);
		accum += countLys * percentPositive(pH, 10.0f);
		accum += countArg * percentPositive(pH, 12.0f);
		accum += countHis * percentPositive(pH, 6.5f);
		accum -= countAsp * percentNegative(pH, 4.4f);
		accum -= countGlu * percentNegative(pH, 4.4f);
		accum -= countCys * percentNegative(pH, 8.5f);
		accum -= countTyr * percentNegative(pH, 10.0f);
		return accum;
	}

	/*
	 * What percentage of ions of given pK at given pH would be postively
	 * charged?
	 */
	private static float percentPositive(float pH, float pK) {
		double concentrationRatio = Math.pow(10f, pK - pH);
		return new Double(concentrationRatio / (concentrationRatio + 1)).floatValue();
	}

	/*
	 * What percentage of ions at given pK at given pH would be negatively
	 * charged?
	 */
	public static float percentNegative(float pH, float pK) {
		double concentrationRatio = Math.pow(10, pH - pK);
		return new Double(concentrationRatio / (concentrationRatio + 1)).floatValue();
	}
}
