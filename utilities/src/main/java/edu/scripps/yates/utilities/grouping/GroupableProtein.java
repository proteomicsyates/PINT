package edu.scripps.yates.utilities.grouping;

import java.util.List;

public interface GroupableProtein {

	public List<GroupablePSM> getGroupablePSMs();

	public ProteinGroup getProteinGroup();

	public int getDBId();

	public String getAccession();

	public void setEvidence(ProteinEvidence evidence);

	public ProteinEvidence getEvidence();

	public void setProteinGroup(ProteinGroup proteinGroup);

}
