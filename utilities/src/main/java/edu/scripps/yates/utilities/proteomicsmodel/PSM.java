package edu.scripps.yates.utilities.proteomicsmodel;

import java.util.List;

import edu.scripps.yates.utilities.grouping.GroupablePSM;

public interface PSM extends HasScores, HasRatios, HasAmounts, HasConditions, HasMSRun, GroupablePSM, HasProteins {

	public Double getExperimentalMH();

	public Double getCalcMH();

	public Double getMassErrorPPM();

	public Double getTotalIntensity();

	public Integer getSPR();

	public Double getIonProportion();

	public Double getPI();

	/**
	 * Gets the peptide sequence including PTMs Nterm and Cterm aminoacids, as
	 * it is reported by the search engine
	 *
	 * @return
	 */
	public String getFullSequence();

	public List<PTM> getPTMs();

	public Peptide getPeptide();

	public void setPeptide(Peptide peptide);

	public int getDBId();

	public String getAfterSeq();

	public String getBeforeSeq();

	public String getChargeState();

	public String getScanNumber();

	@Override
	public boolean equals(Object obj);
}
