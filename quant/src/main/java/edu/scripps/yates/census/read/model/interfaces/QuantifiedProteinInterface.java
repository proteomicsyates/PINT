package edu.scripps.yates.census.read.model.interfaces;

import java.util.Set;

import edu.scripps.yates.utilities.grouping.GroupableProtein;
import edu.scripps.yates.utilities.proteomicsmodel.HasAmounts;

public interface QuantifiedProteinInterface extends GroupableProtein, HasRatios, HasAmounts, HasKey {

	public String getDescription();

	public String getTaxonomy();

	public Set<QuantifiedPeptideInterface> getQuantifiedPeptides();

	public Set<QuantifiedPSMInterface> getQuantifiedPSMs();

	public void addPeptide(QuantifiedPeptideInterface peptide);

	public String getAccessionType();

	public void addPSM(QuantifiedPSMInterface quantifiedPSM);

	public void setTaxonomy(String taxonomy);

	public Set<String> getFileNames();

	public Integer getLength();

	public void setAccession(String primaryAccession);

	public void setDescription(String description);

}
