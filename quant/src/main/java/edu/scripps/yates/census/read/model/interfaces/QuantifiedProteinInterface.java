package edu.scripps.yates.census.read.model.interfaces;

import java.util.Set;

import edu.scripps.yates.utilities.grouping.GroupableProtein;
import edu.scripps.yates.utilities.proteomicsmodel.HasAmounts;

/**
 * A quantified protein that can be quantified in different msruns and
 * experiments.
 *
 * @author Salva
 *
 */
public interface QuantifiedProteinInterface extends GroupableProtein, HasRatios, HasAmounts, HasKey, HasFileName {

	public String getDescription();

	public Set<String> getTaxonomies();

	public Set<QuantifiedPeptideInterface> getQuantifiedPeptides();

	public Set<QuantifiedPSMInterface> getQuantifiedPSMs();

	public void addPeptide(QuantifiedPeptideInterface peptide);

	public String getAccessionType();

	public void addPSM(QuantifiedPSMInterface quantifiedPSM);

	public void setTaxonomy(String taxonomy);

	public Integer getLength();

	public void setAccession(String primaryAccession);

	public void setDescription(String description);

	public Set<String> getRawFileNames();

	public void setDiscarded(boolean b);

	public boolean isDiscarded();

}
