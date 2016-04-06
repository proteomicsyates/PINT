package edu.scripps.yates.server.grouping;

import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.utilities.grouping.GroupablePSM;
import edu.scripps.yates.utilities.grouping.GroupableProtein;
import edu.scripps.yates.utilities.grouping.ProteinEvidence;
import edu.scripps.yates.utilities.grouping.ProteinGroup;

public class GroupableExtendedProteinBean implements GroupableProtein {

	private final ProteinBean protein;
	private ArrayList<GroupablePSM> groupablePSMs;
	private String primaryAccession;
	private ProteinGroup proteinGroup;
	private ProteinEvidence evidence;

	public GroupableExtendedProteinBean(ProteinBean protein) {
		this.protein = protein;
	}

	@Override
	public int getDBId() {
		// using the hascode to differentiate
		return protein.hashCode();
	}

	@Override
	public List<GroupablePSM> getGroupablePSMs() {
		if (groupablePSMs == null) {
			groupablePSMs = new ArrayList<GroupablePSM>();
			List<PSMBean> psms = protein.getPsms();
			for (PSMBean psm : psms) {
				if (GroupableExtendedPsm.map.containsKey(psm.getId())) {
					groupablePSMs
							.add(GroupableExtendedPsm.map.get(psm.getId()));
				} else {
					groupablePSMs.add(new GroupableExtendedPsmBean(psm));
				}
			}
		}
		return groupablePSMs;
	}

	@Override
	public String getAccession() {
		if (primaryAccession == null) {
			primaryAccession = protein.getPrimaryAccession().getAccession();
		}
		return primaryAccession;
	}

	@Override
	public ProteinGroup getProteinGroup() {
		return proteinGroup;
	}

	@Override
	public void setEvidence(ProteinEvidence evidence) {
		this.evidence = evidence;
	}

	@Override
	public ProteinEvidence getEvidence() {
		return evidence;
	}

	@Override
	public void setProteinGroup(ProteinGroup proteinGroup) {
		this.proteinGroup = proteinGroup;

	}

	public ProteinBean getProtein() {
		return protein;
	}
}
