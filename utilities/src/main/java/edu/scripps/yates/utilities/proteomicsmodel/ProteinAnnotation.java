package edu.scripps.yates.utilities.proteomicsmodel;

public interface ProteinAnnotation {

	public AnnotationType getAnnotationType();

	public String getSource();

	public String getName();

	public String getValue();

}
