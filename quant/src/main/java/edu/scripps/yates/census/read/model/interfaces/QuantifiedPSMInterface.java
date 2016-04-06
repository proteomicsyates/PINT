package edu.scripps.yates.census.read.model.interfaces;

import java.util.Set;

import edu.scripps.yates.census.read.model.QuantifiedPeptide;
import edu.scripps.yates.utilities.grouping.GroupablePSM;
import edu.scripps.yates.utilities.proteomicsmodel.HasAmounts;

public interface QuantifiedPSMInterface extends HasKey, PeptideSequenceInterface, GroupablePSM, HasRatios, HasAmounts {

	public String getFileName();

	public void setQuantifiedPeptide(QuantifiedPeptide quantifiedPeptide);

	public QuantifiedPeptideInterface getQuantifiedPeptide();

	public Integer getCharge();

	public void addQuantifiedProtein(QuantifiedProteinInterface quantifiedProtein);

	public Set<QuantifiedProteinInterface> getQuantifiedProteins();

	public String getScan();

	public Float getDeltaCN();

	public Float getXcorr();

	public Float getDeltaMass();

}
