package edu.scripps.yates.census.read.model.interfaces;

import java.util.Set;

import edu.scripps.yates.utilities.proteomicsmodel.HasAmounts;

/**
 * A quantified peptide that can be coming from different msruns (filenames),
 * and can contain psms from different msruns
 *
 * @author Salva
 *
 */
public interface QuantifiedPeptideInterface extends PeptideSequenceInterface, HasRatios, HasAmounts, HasFileName {

	public Set<QuantifiedProteinInterface> getQuantifiedProteins();

	public Set<QuantifiedPSMInterface> getQuantifiedPSMs();

	public boolean addQuantifiedPSM(QuantifiedPSMInterface psm);

	public Set<String> getRawFileNames();

	public void setDiscarded(boolean b);

	public boolean isDiscarded();

}
