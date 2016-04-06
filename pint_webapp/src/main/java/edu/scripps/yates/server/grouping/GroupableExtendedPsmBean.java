package edu.scripps.yates.server.grouping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.utilities.grouping.GroupablePSM;
import edu.scripps.yates.utilities.grouping.GroupableProtein;
import edu.scripps.yates.utilities.grouping.PeptideRelation;

public class GroupableExtendedPsmBean implements GroupablePSM {
	public final static Map<String, GroupableExtendedPsmBean> map = new HashMap<String, GroupableExtendedPsmBean>();
	private final PSMBean psm;
	private PeptideRelation relation;
	private ArrayList<GroupableProtein> groupableProteins;

	public GroupableExtendedPsmBean(PSMBean psm) {
		this.psm = psm;
		map.put(psm.getId(), this);
	}

	@Override
	public List<GroupableProtein> getGroupableProteins() {
		if (groupableProteins == null) {
			groupableProteins = new ArrayList<GroupableProtein>();
			final Set<ProteinBean> proteins = psm.getProteins();
			for (ProteinBean protein : proteins) {
				if (GroupableExtendedProtein.map.containsKey(protein.getId())) {
					groupableProteins.add(GroupableExtendedProtein.map.get(protein.getId()));
				} else {
					groupableProteins.add(new GroupableExtendedProteinBean(protein));
				}
			}
		}
		return groupableProteins;
	}

	@Override
	public String getPSMIdentifier() {
		return psm.getPsmID();
	}

	@Override
	public PeptideRelation getRelation() {
		return relation;
	}

	@Override
	public void setRelation(PeptideRelation relation) {
		this.relation = relation;

	}

	@Override
	public String getSequence() {
		return psm.getSequence();
	}

	/**
	 * @return the psm
	 */
	public PSMBean getPsm() {
		return psm;
	}

}
