package edu.scripps.yates.census.read.model.interfaces;

import java.util.Set;

import edu.scripps.yates.utilities.grouping.GroupablePSM;
import edu.scripps.yates.utilities.proteomicsmodel.HasAmounts;

/**
 * A quantified psm that is quantified in a particular MS Run (one fileName)
 *
 * @author Salva
 *
 */
public interface QuantifiedPSMInterface
		extends HasKey, PeptideSequenceInterface, GroupablePSM, HasRatios, HasAmounts, HasFileName {

	public String getRawFileName();

	public void setQuantifiedPeptide(QuantifiedPeptideInterface quantifiedPeptide);

	public QuantifiedPeptideInterface getQuantifiedPeptide();

	public Integer getCharge();

	public void addQuantifiedProtein(QuantifiedProteinInterface quantifiedProtein);

	public Set<QuantifiedProteinInterface> getQuantifiedProteins();

	public String getScan();

	public Float getDeltaCN();

	public Float getXcorr();

	public Float getDeltaMass();

	/**
	 * Gets the peak twith more intensity
	 *
	 * @return
	 */
	public Double getMaxPeak();

	public boolean isDiscarded();

	public void setDiscarded(boolean discarded);

	public boolean isSingleton();

}
