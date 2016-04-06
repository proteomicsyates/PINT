package edu.scripps.yates.server.grouping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.utilities.grouping.GroupablePSM;
import edu.scripps.yates.utilities.grouping.GroupableProtein;
import edu.scripps.yates.utilities.grouping.PeptideRelation;

public class GroupableExtendedPsm implements GroupablePSM {
	public final static Map<Integer, GroupableExtendedPsm> map = new HashMap<Integer, GroupableExtendedPsm>();
	private final Psm psm;
	private PeptideRelation relation;
	private ArrayList<GroupableProtein> groupableProteins;

	public GroupableExtendedPsm(Psm psm) {
		this.psm = psm;
		map.put(psm.getId(), this);
	}

	@Override
	public List<GroupableProtein> getGroupableProteins() {
		if (groupableProteins == null) {
			groupableProteins = new ArrayList<GroupableProtein>();
			final Set<Protein> proteins = psm.getProteins();
			for (Protein protein : proteins) {
				if (GroupableExtendedProtein.map.containsKey(protein.getId())) {
					groupableProteins.add(GroupableExtendedProtein.map
							.get(protein.getId()));
				} else {
					groupableProteins
							.add(new GroupableExtendedProtein(protein));
				}
			}
		}
		return groupableProteins;
	}

	@Override
	public String getPSMIdentifier() {
		return psm.getPsmId();
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

}
