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
	private List<GroupableProtein> groupableProteins;
	private final String sequence;

	public GroupableExtendedPeptideBean(PeptideBean peptide) {
		this.peptide = peptide;
		this.sequence = peptide.getId();
		map.put(peptide.getId(), this);
	}

	public GroupableExtendedPeptideBean(String cleanPeptideSequence) {
		this.peptide = null;
		this.sequence = cleanPeptideSequence;
		map.put(cleanPeptideSequence, this);
	}

	@Override
	public List<GroupableProtein> getGroupableProteins() {
		if (groupableProteins == null) {
			groupableProteins = new ArrayList<GroupableProtein>();
			if (peptide != null) {
				final Set<ProteinBean> proteins = peptide.getProteins();
				for (final ProteinBean protein : proteins) {
					if (GroupableExtendedProteinBean.map.containsKey(protein.getId())) {
						groupableProteins.add(GroupableExtendedProteinBean.map.get(protein.getId()));
					} else {
						groupableProteins.add(new GroupableExtendedProteinBean(protein, false));
					}
				}
			}
		}
		return groupableProteins;
	}

	@Override
	public String getIdentifier() {
		if (peptide != null) {
			return String.valueOf(peptide.getPeptideBeanUniqueIdentifier());
		} else {
			return sequence;
		}
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
		return sequence;
	}

	/**
	 * @return the peptide
	 */
	public PeptideBean getPeptide() {
		return peptide;
	}

	public boolean addGroupableProteins(GroupableExtendedProteinBean groupableExtendedProteinBean) {
		if (!getGroupableProteins().contains(groupableExtendedProteinBean)) {
			return getGroupableProteins().add(groupableExtendedProteinBean);
		}
		return false;
	}

}
