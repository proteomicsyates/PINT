package edu.scripps.yates.census.read.model.interfaces;

import java.util.Set;

import edu.scripps.yates.utilities.proteomicsmodel.HasAmounts;

public interface QuantifiedPeptideInterface extends PeptideSequenceInterface, HasRatios, HasAmounts {

	public Set<QuantifiedProteinInterface> getQuantifiedProteins();

	public Set<QuantifiedPSMInterface> getQuantifiedPSMs();

	public Set<String> getRawFileNames();

}
