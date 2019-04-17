package edu.scripps.yates.server.grouping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.grouping.GroupablePeptide;
import edu.scripps.yates.utilities.grouping.GroupableProtein;
import edu.scripps.yates.utilities.grouping.ProteinEvidence;
import edu.scripps.yates.utilities.grouping.ProteinGroup;
import gnu.trove.map.hash.THashMap;

public class GroupableExtendedProteinBean implements GroupableProtein {
	public final static Map<String, GroupableExtendedProteinBean> map = new THashMap<String, GroupableExtendedProteinBean>();

	private final ProteinBean protein;
	private List<GroupablePeptide> groupablePeptides;
	private String primaryAccession;
	private ProteinGroup proteinGroup;
	private ProteinEvidence evidence;
	private final boolean psmCentric;

	public GroupableExtendedProteinBean(ProteinBean protein, boolean psmCentric) {
		this.protein = protein;
		this.psmCentric = psmCentric;
	}

	@Override
	public int getUniqueID() {
		// using the hascode to differentiate
		return protein.hashCode();
	}

	@Override
	public List<GroupablePeptide> getGroupablePeptides() {
		if (groupablePeptides == null) {
			groupablePeptides = new ArrayList<GroupablePeptide>();
			if (psmCentric) {
				final List<PSMBean> psms = protein.getPsms();
				for (final PSMBean psm : psms) {
					if (GroupableExtendedPsmBean.map.containsKey(psm.getId())) {
						groupablePeptides.add(GroupableExtendedPsmBean.map.get(psm.getId()));
					} else {
						groupablePeptides.add(new GroupableExtendedPsmBean(psm));
					}
				}
			} else {
				final List<PeptideBean> peptides = protein.getPeptides();
				if (!peptides.isEmpty()) {
					for (final PeptideBean peptide : peptides) {
						if (GroupableExtendedPeptideBean.map.containsKey(peptide.getId())) {
							groupablePeptides.add(GroupableExtendedPeptideBean.map.get(peptide.getId()));
						} else {
							groupablePeptides.add(new GroupableExtendedPeptideBean(peptide));
						}
					}
				} else {
					// this happens if the loading of the peptides is disabled
					// but we have the peptideIds
					final Set<String> fullPeptideSequences = protein.getDifferentSequences();
					for (final String fullPeptideSequence : fullPeptideSequences) {
						final String cleanPeptideSequence = FastaParser.cleanSequence(fullPeptideSequence);
						GroupableExtendedPeptideBean peptide = null;
						if (GroupableExtendedPeptideBean.map.containsKey(cleanPeptideSequence)) {
							peptide = GroupableExtendedPeptideBean.map.get(cleanPeptideSequence);
						} else {
							peptide = new GroupableExtendedPeptideBean(cleanPeptideSequence);
						}
						groupablePeptides.add(peptide);
						peptide.addGroupableProteins(this);
					}
				}
			}
		}
		return groupablePeptides;
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
