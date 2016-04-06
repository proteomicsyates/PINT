package edu.scripps.yates.census.read.model.interfaces;

import java.util.Set;

public interface PeptideSequenceInterface extends HasKey {
	public String getSequence();

	public String getFullSequence();

	public Float getCalcMHplus();

	public Set<String> getTaxonomies();

	public Float getMHplus();

}
