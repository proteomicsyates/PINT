package edu.scripps.yates.shared.thirdparty.pseaquant;

import java.util.List;

import edu.scripps.yates.shared.model.RatioDescriptorBean;

public interface PSEAQuantForm {

	RatioDescriptorBean getRatioDescriptor();

	PSEAQuantAnnotationDatabase getAnnotationDatabase();

	PSEAQuantCVTol getCVTol();

	PSEAQuantLiteratureBias getLiteratureBias();

	PSEAQuantQuantType getQuantType();

	PSEAQuantSupportedOrganism getOrganism();

	String getEmail();

	long getNumberOfSamplings();

	Double getCVTolFactor();

	List<PSEAQuantReplicate> getReplicates();
}
