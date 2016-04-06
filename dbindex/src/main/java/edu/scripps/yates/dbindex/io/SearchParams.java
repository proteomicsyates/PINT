/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package edu.scripps.yates.dbindex.io;

/**
 *
 * @author rpark
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.scripps.yates.dbindex.DBIndexer;
import edu.scripps.yates.dbindex.DBIndexer.IndexType;
import edu.scripps.yates.dbindex.Util;
import edu.scripps.yates.dbindex.model.Enzyme;

public class SearchParams implements DBIndexSearchParams {

	private String program;
	private String parametersFile;
	private String parameters;
	private double peptideMassTolerance;

	private double fragmentIonTolerance;
	private int fragmentIonToleranceInt;
	private double fragmentIonToleranceBinScale;

	private int maxMissedCleavages = 5;
	private int massTypeParent;
	private int massTypeFragment;
	private int numPeptideOutputLnes;
	private int removePrecursorPeak;

	private boolean useMonoParent;
	private boolean useMonoFragment;
	private boolean diffSearch;
	private int enzymeOffset;
	private String databaseName;
	private String indexDatabaseName;

	private int maxNumDiffMod = 3;
	private boolean variableTolerance = false;
	private double variablePeptideMassTolerance;
	private boolean usePPM = false;
	private double relativePeptideMassTolerance;
	private int isotopes = 0;
	private double n15Enrichment = 0.0f;
	private double matchPeakTolerance;
	private double hplusparent;
	private double hparent;
	private double oHparent;
	private double[] pdAAMassParent;
	private double[] pdAAMassFragment;
	private int numIonSeriesUsed;
	private String enzymeBreakAA;
	private String enzymeNoBreakAA;
	private double binWidth;
	private int[] ionSeries = new int[9];
	private int[] ionToUse;

	double diffMass1, diffMass2, diffMass3;
	String diffChar1, diffChar2, diffChar3;
	private boolean useEnzyme;
	private char[] enzymeArr;
	private static SearchParams sparams = null;

	private int neutralLossAions;
	private int neutralLossBions;
	private int neutralLossYions;
	private String enzymeName;
	private String enzymeResidues;
	private String enzymeCut;
	private String enzymeNocutResidues;
	private static double minPrecursorMass = 500.0f;
	private static double maxPrecursorMass = 6000.0f;
	private int minFragPeakNum = 8;

	private boolean useIndex = true;
	private DBIndexer.IndexType indexType = DBIndexer.IndexType.INDEX_NORMAL; // default
	private boolean inMemoryIndex = false;
	private int indexFactor = 6;

	private Enzyme enzyme;
	private static StringBuffer staticParams = new StringBuffer();
	private boolean highResolution = false;
	private int maxChargeState = 6;
	double[] weightArr = new double[12];

	private SearchParams() {
	}

	private List<ModResidue> modList = new ArrayList<ModResidue>();
	// private Set<double> modShiftSet = new HashSet<double>();
	private List<List<Double>> modGroupList = new ArrayList<List<Double>>();
	private boolean precursorHighResolution = true;

	private int scoreWin = 10;
	private boolean neturalLossIsotope = false;

	private boolean isH2OPlusProtonAdded;
	private int massGroupFactor;
	private char[] allowedInternalMissedCleavages;
	private boolean usingSeqDB;
	private boolean usingProtDB;
	private String mongoDBURI;
	private String massDBName;
	private String massDBCollection;
	private String seqDBName;
	private String seqDBCollection;
	private String protDBName;
	private String protDBCollection;
	private boolean usingMongoDB;
	private boolean semiCleavage;

	public static SearchParams getInstance() {
		if (sparams == null)
			sparams = new SearchParams();

		return sparams;
	}

	@Override
	public IndexType getIndexType() {
		return indexType;
	}

	public void setIndexType(IndexType indexType) {
		this.indexType = indexType;
	}

	@Override
	public boolean isUseIndex() {
		return useIndex;
	}

	public void setUseIndex(boolean useIndex) {
		this.useIndex = useIndex;
	}

	@Override
	public boolean isInMemoryIndex() {
		return inMemoryIndex;
	}

	public void setInMemoryIndex(boolean inMemoryIndex) {
		this.inMemoryIndex = inMemoryIndex;
	}

	@Override
	public int getIndexFactor() {
		return indexFactor;
	}

	public void setIndexFactor(int indexFactor) {
		this.indexFactor = indexFactor;
	}

	public void setMassTypeFragment(int massTypeFragment) {
		this.massTypeFragment = massTypeFragment;
	}

	public void setMassTypeParent(int massTypeParent) {
		this.massTypeParent = massTypeParent;
	}

	public void setMaxMissedCleavages(int maxMissedCleavages) {
		this.maxMissedCleavages = maxMissedCleavages;
	}

	public void setFragmentIonTolerance(double fragmentIonTolerance) {

		this.fragmentIonTolerance = fragmentIonTolerance;

		// System.out.println("============================" +
		// fragmentIonTolerance);

		fragmentIonToleranceBinScale = 1000.0f / fragmentIonTolerance;
		// System.out.println("============================" +
		// fragmentIonTolerance + " " + this.fragmentIonToleranceBinScale);

		/*
		 * if(fragmentIonTolerance<100f) this.neturalLossIsotope=true; else
		 * this.neturalLossIsotope=false;
		 */

		// if(fragmentIonTolerance>300.0f || fragmentIonTolerance<=0.0f)
		// this.highResolution=false;

	}

	public void setPeptideMassTolerance(double peptideMassTolerance) {

		if (peptideMassTolerance > 900)
			precursorHighResolution = false;

		this.peptideMassTolerance = peptideMassTolerance;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public void setParametersFile(String parametersFile) {
		this.parametersFile = parametersFile;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getProgram() {
		return program;
	}

	public String getParametersFile() {
		return parametersFile;
	}

	public String getParameters() {
		return parameters;
	}

	public double getPeptideMassTolerance() {
		return peptideMassTolerance;
	}

	public double getFragmentIonTolerance() {
		return fragmentIonTolerance;
	}

	@Override
	public int getMaxMissedCleavages() {
		return maxMissedCleavages;
	}

	public int getMassTypeParent() {
		return massTypeParent;
	}

	public int getMassTypeFragment() {
		return massTypeFragment;
	}

	public int getNumPeptideOutputLnes() {
		return numPeptideOutputLnes;
	}

	public void setNumPeptideOutputLnes(int numPeptideOutputLnes) {
		this.numPeptideOutputLnes = numPeptideOutputLnes;
	}

	public int getRemovePrecursorPeak() {
		return removePrecursorPeak;
	}

	public void setRemovePrecursorPeak(int removePrecursorPeak) {
		this.removePrecursorPeak = removePrecursorPeak;
	}

	public double getBinWidth() {
		return binWidth;
	}

	public void setBinWidth(double binWidth) {
		this.binWidth = binWidth;
	}

	@Override
	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public boolean isDiffSearch() {
		return diffSearch;
	}

	public void setDiffSearch(boolean diffSearch) {
		this.diffSearch = diffSearch;
	}

	public String getEnzymeBreakAA() {
		return enzymeBreakAA;
	}

	public void setEnzymeBreakAA(String enzymeBreakAA, int numMaxMissedClavages, boolean semiCleave) {
		enzymeArr = enzymeBreakAA.toCharArray();
		enzyme = new Enzyme(enzymeArr, numMaxMissedClavages, semiCleave);
		for (char ch : enzymeArr)
			enzyme.addCleavePosition(ch);

		this.enzymeBreakAA = enzymeBreakAA;
	}

	public String getEnzymeNoBreakAA() {
		return enzymeNoBreakAA;
	}

	public void setEnzymeNoBreakAA(String enzymeNoBreakAA) {
		this.enzymeNoBreakAA = enzymeNoBreakAA;
	}

	@Override
	public int getEnzymeOffset() {
		return enzymeOffset;
	}

	public void setEnzymeOffset(int enzymeOffset) {
		this.enzymeOffset = enzymeOffset;
	}

	public double getHparent() {
		return hparent;
	}

	public void setHparent(double hparent) {
		this.hparent = hparent;
	}

	public double getHplusparent() {
		return hplusparent;
	}

	public void setHplusparent(double hplusparent) {
		this.hplusparent = hplusparent;
	}

	public int[] getIonSeries() {
		return ionSeries;
	}

	public void setIonSeries(int[] ionSeries) {

		int count = 0;
		List<Integer> l = new ArrayList<Integer>();

		for (int i = 0; i < ionSeries.length; i++) {

			if (ionSeries[i] > 0) {
				// System.out.println("++" + i);
				l.add(i);

				/*
				 * switch (i) { /*a* case 0: l.add(0); break; /*b* case 1:
				 * l.add(1); break; /*c* case 2: l.add(2); break; /*x* case 6:
				 * l.add(6); break; /*y* case 7: l.add(7); break; /*z* case 8:
				 * l.add(8); break; }
				 */
			}

			count++;
		}

		int[] arr = new int[l.size()];
		count = 0;
		for (Iterator<Integer> itr = l.iterator(); itr.hasNext();) {
			arr[count++] = itr.next();
		}

		ionToUse = arr;
		this.ionSeries = ionSeries;
	}

	public int getIsotopes() {
		return isotopes;
	}

	public void setIsotopes(int isotopes) {
		this.isotopes = isotopes;
	}

	public double getMatchPeakTolerance() {
		return matchPeakTolerance;
	}

	public void setMatchPeakTolerance(double matchPeakTolerance) {
		this.matchPeakTolerance = matchPeakTolerance;
	}

	public int getMaxNumDiffMod() {
		return maxNumDiffMod;
	}

	public void setMaxNumDiffMod(int maxNumDiffMod) {
		this.maxNumDiffMod = maxNumDiffMod;
	}

	public double getN15Enrichment() {
		return n15Enrichment;
	}

	public void setN15Enrichment(double n15Enrichment) {
		this.n15Enrichment = n15Enrichment;
	}

	public int getNumIonSeriesUsed() {
		return numIonSeriesUsed;
	}

	public void setNumIonSeriesUsed(int numIonSeriesUsed) {
		this.numIonSeriesUsed = numIonSeriesUsed;
	}

	public double getoHparent() {
		return oHparent;
	}

	public void setoHparent(double oHparent) {
		this.oHparent = oHparent;
	}

	public double[] getPdAAMassFragment() {
		return pdAAMassFragment;
	}

	public void setPdAAMassFragment(double[] pdAAMassFragment) {
		this.pdAAMassFragment = pdAAMassFragment;
	}

	public double[] getPdAAMassParent() {
		return pdAAMassParent;
	}

	public void setPdAAMassParent(double[] pdAAMassParent) {
		this.pdAAMassParent = pdAAMassParent;
	}

	public double getRelativePeptideMassTolerance() {
		return relativePeptideMassTolerance;
	}

	public void setRelativePeptideMassTolerance(double relativePeptideMassTolerance) {
		this.relativePeptideMassTolerance = relativePeptideMassTolerance;
	}

	public boolean isUseMonoFragment() {
		return useMonoFragment;
	}

	public void setUseMonoFragment(boolean useMonoFragment) {
		this.useMonoFragment = useMonoFragment;
	}

	@Override
	public boolean isUseMonoParent() {
		return useMonoParent;
	}

	public void setUseMonoParent(boolean useMonoParent) {
		this.useMonoParent = useMonoParent;
	}

	public double getVariablePeptideMassTolerance() {
		return variablePeptideMassTolerance;
	}

	public void setVariablePeptideMassTolerance(double variablePeptideMassTolerance) {
		this.variablePeptideMassTolerance = variablePeptideMassTolerance;
	}

	public boolean isVariableTolerance() {
		return variableTolerance;
	}

	public void setVariableTolerance(boolean variableTolerance) {
		this.variableTolerance = variableTolerance;
	}

	public boolean isUsePPM() {
		return usePPM;
	}

	public void setUsePPM(boolean usePPM) {
		this.usePPM = usePPM;
	}

	public String getDiffChar1() {
		return diffChar1;
	}

	public void setDiffChar1(String diffChar1) {
		this.diffChar1 = diffChar1;
	}

	public String getDiffChar2() {
		return diffChar2;
	}

	public void setDiffChar2(String diffChar2) {
		this.diffChar2 = diffChar2;
	}

	public String getDiffChar3() {
		return diffChar3;
	}

	public void setDiffChar3(String diffChar3) {
		this.diffChar3 = diffChar3;
	}

	public double getDiffMass1() {
		return diffMass1;
	}

	public void setDiffMass1(double diffMass1) {
		this.diffMass1 = diffMass1;
	}

	public double getDiffMass2() {
		return diffMass2;
	}

	public void setDiffMass2(double diffMass2) {
		this.diffMass2 = diffMass2;
	}

	public double getDiffMass3() {
		return diffMass3;
	}

	public void setDiffMass3(double diffMass3) {
		this.diffMass3 = diffMass3;
	}

	public boolean isUseEnzyme() {
		return useEnzyme;
	}

	public void setUseEnzyme(boolean useEnzyme) {
		this.useEnzyme = useEnzyme;
	}

	@Override
	public char[] getEnzymeArr() {
		return enzymeArr;
	}

	public void setEnzymeArr(char[] enzymeArr) {
		this.enzymeArr = enzymeArr;
	}

	public int getNeutralLossAions() {
		return neutralLossAions;
	}

	public void setNeutralLossAions(int neutralLossAions) {
		this.neutralLossAions = neutralLossAions;
	}

	public int getNeutralLossBions() {
		return neutralLossBions;
	}

	public void setNeutralLossBions(int neutralLossBions) {
		this.neutralLossBions = neutralLossBions;
	}

	public int getNeutralLossYions() {
		return neutralLossYions;
	}

	public void setNeutralLossYions(int neutralLossYions) {
		this.neutralLossYions = neutralLossYions;
	}

	public int[] getIonToUse() {
		return ionToUse;
	}

	public void setIonToUse(int[] ionToUse) {
		this.ionToUse = ionToUse;
	}

	public static SearchParams getSparams() {
		return sparams;
	}

	public static void setSparams(SearchParams sparams) {
		SearchParams.sparams = sparams;
	}

	public void addModResidue(ModResidue r) {
		modList.add(r);
	}

	public List<ModResidue> getModList() {
		return modList;
	}

	public void setModList(List<ModResidue> modList) {
		this.modList = modList;
	}

	public List<List<Double>> getModGroupList() {
		return modGroupList;
	}

	public void setModGroupList(List<List<Double>> modGroupList) {
		this.modGroupList = modGroupList;
	}

	public void addModGroupList(List<Double> modGroup) {
		modGroupList.add(modGroup);
	}

	public String getIndexDatabaseName() {
		return indexDatabaseName;
	}

	public void setIndexDatabaseName(String indexDatabaseName) {
		this.indexDatabaseName = indexDatabaseName;
	}

	public String getEnzymeName() {
		return enzymeName;
	}

	public void setEnzymeName(String enzymeName) {
		this.enzymeName = enzymeName;
	}

	public String getEnzymeCut() {
		return enzymeCut;
	}

	public void setEnzymeCut(String enzymeCut) {
		this.enzymeCut = enzymeCut;
	}

	@Override
	public String getEnzymeResidues() {
		return enzymeResidues;
	}

	public void setEnzymeResidues(String enzymeResidues) {
		this.enzymeResidues = enzymeResidues;
	}

	@Override
	public String getEnzymeNocutResidues() {
		return enzymeNocutResidues;
	}

	public void setEnzymeNocutResidues(String enzymeNocutResidues) {
		this.enzymeNocutResidues = enzymeNocutResidues;
	}

	@Override
	public String getFullIndexFileName() {

		String uniqueIndexName = databaseName + "_";

		// generate a unique string based on current params that affect the
		// index
		final StringBuilder uniqueParams = new StringBuilder();
		// uniqueParams.append(sparam.getEnzyme().toString());
		// uniqueParams.append(sparam.getEnzymeNumber());
		uniqueParams.append(getEnzymeOffset());
		uniqueParams.append(" ").append(getEnzymeResidues());
		uniqueParams.append(" ").append(getEnzymeNocutResidues());

		// uniqueParams.append(" ").append(getIndexFactor() ) ;

		uniqueParams.append(", Cleav: ");
		uniqueParams.append(getMaxMissedCleavages());

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

		final String uniqueParamsStr = uniqueParams.toString();

		// logger.log(Level.INFO, "Unique params: " + uniqueParamsStr);

		String uniqueParamsStrHash = Util.getMd5(uniqueParamsStr);
		System.out.println("param===========" + uniqueParamsStr + "\t" + uniqueParamsStrHash);

		return uniqueIndexName + uniqueParamsStrHash;
	}

	public static void addStaticParam(char ch, double f) {
		// public static void addMass(int i, double mass) {
		SearchParams.staticParams.append(ch).append(f).append(" ");
	}

	public static void addStaticParam(String str, double f) {
		// public static void addMass(int i, double mass) {
		SearchParams.staticParams.append(str).append(f).append(" ");
	}

	public static StringBuffer getStaticParams() {

		return staticParams;
	}

	public static void setStaticParams(StringBuffer staticParams) {
		SearchParams.staticParams = staticParams;
	}

	@Override
	public double getMinPrecursorMass() {
		return minPrecursorMass;
	}

	public void setMinPrecursorMass(double minPrecursorMass) {
		this.minPrecursorMass = minPrecursorMass;
	}

	@Override
	public double getMaxPrecursorMass() {
		return maxPrecursorMass;
	}

	public void setMaxPrecursorMass(double maxPrecursorMass) {
		this.maxPrecursorMass = maxPrecursorMass;
	}

	public int getMinFragPeakNum() {
		return minFragPeakNum;
	}

	public void setMinFragPeakNum(int minFragPeakNum) {
		this.minFragPeakNum = minFragPeakNum;
	}

	public boolean isHighResolution() {
		return highResolution;
	}

	public void setHighResolution(boolean highResolution) {
		this.highResolution = highResolution;
	}

	public int getFragmentIonToleranceInt() {
		return fragmentIonToleranceInt;
	}

	public void setFragmentIonToleranceInt(int fragmentIonToleranceInt) {

		this.fragmentIonToleranceInt = fragmentIonToleranceInt;
	}

	public int getMaxChargeState() {
		return maxChargeState;
	}

	public void setMaxChargeState(int maxChargeState) {
		this.maxChargeState = maxChargeState;
	}

	public double getFragmentIonToleranceBinScale() {
		return fragmentIonToleranceBinScale;
	}

	public void setFragmentIonToleranceBinScale(double fragmentIonToleranceBinScale) {
		this.fragmentIonToleranceBinScale = fragmentIonToleranceBinScale;
	}

	public boolean isPrecursorHighResolution() {
		return precursorHighResolution;
	}

	public void setPrecursorHighResolution(boolean precursorHighResolution) {
		this.precursorHighResolution = precursorHighResolution;
	}

	public int getScoreWin() {
		return scoreWin;
	}

	public void setScoreWin(int scoreWin) {
		this.scoreWin = scoreWin;
	}

	public boolean isNeturalLossIsotope() {
		return neturalLossIsotope;
	}

	public void setNeturalLossIsotope(boolean neturalLossIsotope) {
		this.neturalLossIsotope = neturalLossIsotope;
	}

	public double[] getWeightArr() {
		return weightArr;
	}

	public void setWeightArr(double[] weightArr) {
		this.weightArr = weightArr;
	}

	@Override
	public Enzyme getEnzyme() {
		return enzyme;
	}

	public void setEnzyme(Enzyme enzyme) {
		this.enzyme = enzyme;
	}

	public double getWeight(int i) {
		return weightArr[i];
	}

	@Override
	public boolean isH2OPlusProtonAdded() {
		return isH2OPlusProtonAdded;
	}

	@Override
	public int getMassGroupFactor() {
		return massGroupFactor;
	}

	/**
	 * @param isH2OPlusProtonAdded
	 *            the isH2OPlusProtonAdded to set
	 */
	public void setH2OPlusProtonAdded(boolean isH2OPlusProtonAdded) {
		this.isH2OPlusProtonAdded = isH2OPlusProtonAdded;
	}

	/**
	 * @param massGroupFactor
	 *            the massGroupFactor to set
	 */
	public void setMassGroupFactor(int massGroupFactor) {
		this.massGroupFactor = massGroupFactor;
	}

	@Override
	public char[] getMandatoryInternalAAs() {
		return allowedInternalMissedCleavages;
	}

	/**
	 * @param allowedInternalMissedCleavages
	 *            the allowedInternalMissedCleavages to set
	 */
	public void setAllowedInternalMissedCleavages(char[] allowedInternalMissedCleavages) {
		this.allowedInternalMissedCleavages = allowedInternalMissedCleavages;
	}

	@Override
	public boolean isUsingProtDB() {
		return usingProtDB;
	}

	@Override
	public boolean isUsingSeqDB() {
		return usingSeqDB;
	}

	// SeqDB MongoDB setters
	public void setUsingSeqDB(boolean useSeqDB) {
		usingSeqDB = useSeqDB;
	} // ProtDB MongoDB setters

	public void setUsingProtDB(boolean useProtDB) {
		usingProtDB = useProtDB;
	}

	@Override
	public String getMongoDBURI() {
		return mongoDBURI;
	}

	public void setMongoDBURI(String trim) {
		mongoDBURI = trim;
	}

	@Override
	public String getMassDBName() {
		return massDBName;
	}

	@Override
	public String getMassDBCollection() {
		return massDBCollection;
	}

	public void setMassDBName(String massDBName) {
		this.massDBName = massDBName;
	}

	public void setMassDBCollection(String massDBCollection) {
		this.massDBCollection = massDBCollection;
	}

	public void setSeqDBName(String seqDBName) {
		this.seqDBName = seqDBName;
	}

	public void setSeqDBCollection(String seqDBCollectionName) {
		seqDBCollection = seqDBCollectionName;
	}

	@Override
	public String getSeqDBName() {
		return seqDBName;
	}

	@Override
	public String getSeqDBCollection() {
		return seqDBCollection;
	}

	@Override
	public String getProtDBName() {
		return protDBName;
	}

	@Override
	public String getProtDBCollection() {
		return protDBCollection;
	}

	public void setProtDBName(String protDBName) {
		this.protDBName = protDBName;
	}

	public void setProtDBCollection(String protDBCollectionName) {
		protDBCollection = protDBCollectionName;
	}

	public void setUsingMongoDB(boolean usingMongoDB) {
		this.usingMongoDB = usingMongoDB;
	}

	/**
	 * @return the usingMongoDB
	 */
	@Override
	public boolean isUsingMongoDB() {
		return usingMongoDB;
	}

	public void setSemiCleavage(boolean semiCleavage) {
		this.semiCleavage = semiCleavage;
	}

	@Override
	public boolean isSemiCleavage() {
		return semiCleavage;
	}
}
