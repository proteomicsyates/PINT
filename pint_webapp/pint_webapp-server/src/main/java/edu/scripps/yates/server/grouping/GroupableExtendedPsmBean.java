package edu.scripps.yates.server.grouping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.utilities.grouping.GroupablePeptide;
import edu.scripps.yates.utilities.grouping.GroupableProtein;
import edu.scripps.yates.utilities.grouping.PeptideRelation;
import gnu.trove.map.hash.THashMap;

public class GroupableExtendedPsmBean implements GroupablePeptide {
	public final static Map<String, GroupableExtendedPsmBean> map = new THashMap<String, GroupableExtendedPsmBean>();
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
			for (final ProteinBean protein : proteins) {
				if (GroupableExtendedProteinBean.map.containsKey(protein.getId())) {
					groupableProteins.add(GroupableExtendedProteinBean.map.get(protein.getId()));
				} else {
					groupableProteins.add(new GroupableExtendedProteinBean(protein, true));
				}
			}
		}
		return groupableProteins;
	}

	@Override
	public String getIdentifier() {
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
