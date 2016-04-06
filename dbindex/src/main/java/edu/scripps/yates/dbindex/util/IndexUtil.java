package edu.scripps.yates.dbindex.util;

import org.apache.log4j.Logger;

import edu.scripps.yates.dbindex.Constants;
import edu.scripps.yates.dbindex.Util;
import edu.scripps.yates.dbindex.io.DBIndexSearchParams;
import edu.scripps.yates.dbindex.io.SearchParams;
import edu.scripps.yates.dbindex.model.AssignMass;

public class IndexUtil {
	private final static Logger log = Logger.getLogger(IndexUtil.class);

	/**
	 * Calculates the mass of a sequence, by summing all the masses of all the
	 * AAs and then summing the mass of a water molecule H2O and the proton mass
	 * (is the parameter is true), as well as the mass of the cTerm and nTerm if
	 * configured in {@link AssignMass}
	 *
	 * @param sequence
	 * @param isH2OPlusProtonAdded
	 * @return
	 */
	public static double calculateMass(String sequence, boolean isH2OPlusProtonAdded) {
		double mass = 0;
		if (isH2OPlusProtonAdded)
			mass += Constants.H2O_PROTON;
		mass += AssignMass.getcTerm();
		mass += AssignMass.getnTerm();
		for (int i = 0; i < sequence.length(); i++) {
			// System.out.println(AssignMass.getMass(sequence.charAt(i)));
			mass += AssignMass.getMass(sequence.charAt(i));
		}
		return mass;
	}

	/**
	 * This is the number of rows to lookup in the index from the key it is
	 * calculated from the mass to search.<br>
	 * So, if someone looks to a mass of 1340.386, it will be multiplied by the
	 * massGroupFactor, and then, then integer part will be taken as the key (if
	 * massGroupFactor is 1, then 1340 is the key). Then, the index will look
	 * into 1340-1, 1340 and 1340+1 entries in the SQLLite database, being the
	 * 1, in this case, the value of numRowsToLookup.<br>
	 * Remain this on 0, since from the last change by Salva 24Nov2014, setting
	 * massGroupFactor to 10000 this is not needed.
	 */
	private static int numRowsToLookup = 0;// 1;

	public static void setNumRowsToLookup(int numRowsToLookup) {
		IndexUtil.numRowsToLookup = numRowsToLookup;
	}

	public static int getNumRowsToLookup() {
		return numRowsToLookup;
	}

	/**
	 * Gets the tolerance in Da referred to an actualMass and the ppmTolerance
	 *
	 * @param actualMass
	 * @param ppmTolerance
	 * @return
	 */
	public static double getToleranceInDalton(double actualMass, double ppmTolerance) {
		return actualMass * (1 - 1 / (ppmTolerance / Constants.ONE_MILLION + 1));
	}

	public static String createFullIndexFileName(DBIndexSearchParams params) {
		String uniqueIndexName = params.getDatabaseName() + "_";

		// generate a unique string based on current params that affect the
		// index
		final StringBuilder uniqueParams = new StringBuilder();
		// uniqueParams.append(sparam.getEnzyme().toString());
		// uniqueParams.append(sparam.getEnzymeNumber());
		uniqueParams.append(params.getEnzymeOffset());
		uniqueParams.append(" ").append(params.getEnzymeResidues());
		uniqueParams.append(" ").append(params.getEnzymeNocutResidues());

		// uniqueParams.append(" ").append(getIndexFactor() ) ;

		uniqueParams.append(", Cleav: ");
		// uniqueParams.append(params.getMaxInternalCleavageSites());
		uniqueParams.append(params.getMaxMissedCleavages());

		uniqueParams.append(", Static: ").append(SearchParams.getStaticParams());

		/*
		 * uniqueParams.append(getMaxNumDiffMod());
		 * uniqueParams.append("\nMods:"); for (final ModResidue mod :
		 * getModList()) { uniqueParams.append(mod.toString()).append(" "); }
		 * uniqueParams.append("\nMods groups:"); for (final List<double>
		 * modGroupList : getModGroupList()) { for (final double f :
		 * modGroupList) { uniqueParams.append(f).append(" "); } }
		 */

		// System.out.println("===" + uniqueParams.toString());

		// logger.log(Level.INFO, "Unique params: " + uniqueParamsStr);

		// added by Salva 24Nov2014
		uniqueParams.append(", isH2OPlusProtonAdded: " + params.isH2OPlusProtonAdded());
		uniqueParams.append(", massGroupFactor: " + params.getMassGroupFactor());
		if (params.getMandatoryInternalAAs() != null)
			uniqueParams.append(", MandatoryInternalAAs: " + params.getMandatoryInternalAAs().toString());
		uniqueParams.append(", semiCleave" + params.isSemiCleavage());

		final String uniqueParamsStr = uniqueParams.toString();
		String uniqueParamsStrHash = Util.getMd5(uniqueParamsStr);
		log.info("Index Unique String: " + uniqueParamsStr);
		log.info("Index Unique hashkey: " + uniqueParamsStrHash);
		return uniqueIndexName + uniqueParamsStrHash;
	}
}
