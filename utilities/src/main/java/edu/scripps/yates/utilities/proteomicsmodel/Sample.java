package edu.scripps.yates.utilities.proteomicsmodel;

public interface Sample {

	public String getDescription();

	public Boolean isWildType();

	public String getName();

	public Label getLabel();

	public Tissue getTissue();

	public Organism getOrganism();

}
