package edu.scripps.yates.utilities.grouping;

import java.util.List;

public interface GroupablePSM {

	String getSequence();

	String getPSMIdentifier();

	void setRelation(PeptideRelation relation);

	PeptideRelation getRelation();

	List<GroupableProtein> getGroupableProteins();

}
