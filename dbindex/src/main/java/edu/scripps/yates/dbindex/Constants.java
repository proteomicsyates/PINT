package edu.scripps.yates.dbindex;

/**
 * 
 * @author rpark
 */

public class Constants {

	public static double PROTON = 1.007276466;
	public static double H = 1.007825;
	public static final double H2O = 15.9949145 + 2 * H;
	public static final double H2O_PROTON = H2O + PROTON;
	public static final double H2O_PROTON_SCALED_DOWN = H2O_PROTON * 1000;
	public static final int MIN_PEP_LENGTH = 6;
	// public static final int MIN_FRAG_NUM=10;
	// public static final int MIN_PRECURSOR=600;
	// public static final int MAX_PRECURSOR=6000;

	public static final int PREFIX_RESIDUES = 4;
	public static final int PREFIX_RESIDUES2 = 8;

	public static final double MADD_DIFF_C12C13 = 1.003354826;
	public static final int MADD_DIFF_C12C13_PPM = 1003;

	// ////////SCORING //////////////
	public static final int SCORE_BIN_SIZE = 200;
	public static final double MAX_PRECURSOR_MASS = 8000.0;
	// public static final int MAX_CHARGE_STATE = 6;
	public static final int MAX_CHARGE_STATE_PRECURSOR = 2;
	// public static final int MAX_CHARGE_STATE = 16;

	// ///////OUTPUT ///////////////////
	public static final int PEPTIDE_NUM_DISPLAY = 5;
	public static final int NUM_REFERENCE = 50;
	// public static final int CON_SIZE = 7;

	/*
	 * public static final double log10 = 0.434294481; //public static final
	 * double MASSH2 = MASSH * 2; // MASSH2O + MASSPROTON public static final
	 * double MASSH3O = MASSH2O + MASSPROTON; public static final double
	 * DBINWIDTH = 1.f/1.0005079; public static final double MASSHDB =
	 * MASSH*DBINWIDTH; public static final double MASSPROTONDB =
	 * MASSPROTON*DBINWIDTH; public static final double MASSH3ODB =
	 * MASSH3O*DBINWIDTH; public static final String AVGISOTOPE = "avg"; public
	 * static final String MONOISOTOPE = "mono"; // Thermo source is about
	 * 1.002806, which does not look right public static final double
	 * MASSDIFFC12C13 = 1.003354826; public static final double MASSDIFFN14N15 =
	 * 0.997034968; public static final int NUMCHARS = 256;
	 */

	public static final int MAX_INDEX_RESIDUE_LEN = 3; // how many max. residue
														// ions on each side of
														// sequence to store in
														// index

	public static final int ONE_MILLION = 1000000;
	public static final double PRECISION = 0.000001;

	public static int NUM_BUCKETS = 0; // will override in constr
	public static int BUCKET_MASS_RANGE = 0; // will override in constr
	public static final int MAX_MASS_RANGES = 24; // will use optimzed
	// statements for first 24,
	// and by hand for above 24

	// commit the buffered row and clear the buffer after this many seq
	public static final int COMMIT_SEQUENCES = 1000;
	public static final int BYTE_PER_SEQUENCE = 8 + 3 * 4; // double mass (8),
															// int
}
