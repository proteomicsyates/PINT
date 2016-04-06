package edu.scripps.yates.server.grouping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.utilities.grouping.GroupablePSM;
import edu.scripps.yates.utilities.grouping.GroupableProtein;
import edu.scripps.yates.utilities.grouping.ProteinEvidence;
import edu.scripps.yates.utilities.grouping.ProteinGroup;

public class GroupableExtendedProtein implements GroupableProtein {

	public static final Map<Integer, GroupableExtendedProtein> map = new HashMap<Integer, GroupableExtendedProtein>();
	private final Protein protein;
	private ArrayList<GroupablePSM> groupablePSMs;
	private String primaryAccession;
	private ProteinGroup proteinGroup;
	private ProteinEvidence evidence;

	public GroupableExtendedProtein(Protein protein) {
		this.protein = protein;
		map.put(protein.getId(), this);
	}

	@Override
	public int getDBId() {
		return protein.getId();
	}

	@Override
	public List<GroupablePSM> getGroupablePSMs() {
		if (groupablePSMs == null) {
			groupablePSMs = new ArrayList<GroupablePSM>();
			final Set<Psm> psms = protein.getPsms();
			for (Psm psm : psms) {
				if (GroupableExtendedPsm.map.containsKey(psm.getId())) {
					groupablePSMs
							.add(GroupableExtendedPsm.map.get(psm.getId()));
				} else {
					groupablePSMs.add(new GroupableExtendedPsm(psm));
				}
			}
		}
		return groupablePSMs;
	}

	@Override
	public String getAccession() {
		if (primaryAccession == null) {
			final ProteinAccession acc = PersistenceUtils
					.getPrimaryAccession(protein);
			primaryAccession = acc.getAccession();
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

	public Protein getProtein() {
		return protein;
	}
}
