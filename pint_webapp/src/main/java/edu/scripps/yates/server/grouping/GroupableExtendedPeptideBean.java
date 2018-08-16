package edu.scripps.yates.server.grouping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.utilities.grouping.GroupablePeptide;
import edu.scripps.yates.utilities.grouping.GroupableProtein;
import edu.scripps.yates.utilities.grouping.PeptideRelation;
import gnu.trove.map.hash.THashMap;

public class GroupableExtendedPeptideBean implements GroupablePeptide {
	public final static Map<String, GroupableExtendedPeptideBean> map = new THashMap<String, GroupableExtendedPeptideBean>();
	private final PeptideBean peptide;
	private PeptideRelation relation;
	private ArrayList<GroupableProtein> groupableProteins;

	public GroupableExtendedPeptideBean(PeptideBean peptide) {
		this.peptide = peptide;
		map.put(peptide.getId(), this);
	}

	@Override
	public List<GroupableProtein> getGroupableProteins() {
		if (groupableProteins == null) {
			groupableProteins = new ArrayList<GroupableProtein>();
			final Set<ProteinBean> proteins = peptide.getProteins();
			for (final ProteinBean protein : proteins) {
				if (GroupableExtendedProteinBean.map.containsKey(protein.getId())) {
					groupableProteins.add(GroupableExtendedProteinBean.map.get(protein.getId()));
				} else {
					groupableProteins.add(new GroupableExtendedProteinBean(protein, false));
				}
			}
		}
		return groupableProteins;
	}

	@Override
	public String getIdentifier() {
		return String.valueOf(peptide.getPeptideBeanUniqueIdentifier());
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
		return peptide.getSequence();
	}

	/**
	 * @return the peptide
	 */
	public PeptideBean getPeptide() {
		return peptide;
	}

}
