package edu.scripps.yates.utilities.grouping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

public class ProteinGroup extends ArrayList<GroupableProtein> {
	/**
	 *
	 */
	private static final long serialVersionUID = 1516424786690373161L;
	private static final Logger log = Logger.getLogger("log4j.logger.org.proteored");
	private ProteinEvidence evidence;
	private List<String> accessions;
	private String key;

	public ProteinGroup(ProteinEvidence e) {
		super();
		evidence = e;
	}

	public ProteinGroup(ProteinGroupInference iProteinGroup) {
		if (iProteinGroup == null)
			throw new IllegalArgumentException("group is null");

		for (InferenceProtein inferenceProtein : iProteinGroup) {
			List<GroupableProtein> proteinsMerged = inferenceProtein.getProteinsMerged();
			for (GroupableProtein protein : proteinsMerged) {
				protein.setProteinGroup(this);
				protein.setEvidence(inferenceProtein.getEvidence());
			}
			this.addAll(proteinsMerged);
		}
		evidence = iProteinGroup.getEvidence();

	}

	public String getKey() {
		if (key != null)
			return key;
		String ret = "";

		List<String> accessions2 = getAccessions();

		for (String accession : accessions2) {
			if (!"".equals(ret)) {
				ret = ret + ",";
			}
			ret = ret + accession;
		}

		key = ret + "[" + evidence.toString() + "]";
		return key;
	}

	// public ProteinGroup() {
	// this(ProteinEvidence.AMBIGUOUSGROUP);
	// }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String shareOrContain = "sharing";
		if (size() == 1)
			shareOrContain = "containing";
		Set<String> seqs = new HashSet<String>();
		final List<GroupablePSM> psMs = getPSMs();
		for (GroupablePSM groupablePSM : psMs) {
			seqs.add(groupablePSM.getSequence());
		}
		sb.append("Group: " + getEvidence().name() + " - " + shareOrContain + " " + psMs.size() + " PSMs ("
				+ seqs.size() + " different sequences)\n");
		int memberGroup = 1;
		for (GroupableProtein protein : this) {
			StringBuilder sb2 = new StringBuilder();
			for (GroupablePSM groupablePSM : protein.getGroupablePSMs()) {
				sb2.append(groupablePSM.getSequence() + " ");
			}
			sb.append("PRT\t\t" + memberGroup++ + " " + protein.getAccession() + "\t" + sb2.toString() + "\n");
		}
		return sb.toString();
	}

	@Override
	public boolean equals(Object object) {
		// return shareOneProtein(object);
		return shareAllProteins(object);
		// return theyformJustOneGroup(object);
	}

	private boolean theyformJustOneGroup(Object object) {
		if (object instanceof ProteinGroup) {
			if (shareOneProtein(object)) {

				List<GroupableProtein> proteins = new ArrayList<GroupableProtein>();
				proteins.addAll(this);
				proteins.addAll((ProteinGroup) object);
				PAnalyzer pa = new PAnalyzer(false);
				List<ProteinGroup> groups = pa.run(proteins);
				if (groups.size() == 1)
					return true;
			}
			return false;
		}
		return super.equals(object);
	}

	public boolean shareAllProteins(Object object) {
		if (object instanceof ProteinGroup) {
			ProteinGroup pg2 = (ProteinGroup) object;

			if (getKey().equals(pg2.getKey())) {
				// if (this.evidence == pg2.evidence)
				return true;
			}
			return false;
		}
		// else if (object instanceof ProteinGroupOccurrence) {
		// ProteinGroupOccurrence pgo2 = (ProteinGroupOccurrence) object;
		// if (equals(pgo2.getFirstOccurrence()))
		// return true;
		// return false;
		// }
		return super.equals(object);
	}

	/**
	 * This method will determine how comparisons are made between
	 * proteinGroups! In this case, two groups are equals if share at least one
	 * protein.
	 */
	public boolean shareOneProtein(Object object) {
		if (object instanceof ProteinGroup) {
			ProteinGroup pg2 = (ProteinGroup) object;

			// At least share one protein
			for (String acc : getAccessions()) {
				if (pg2.getAccessions().contains(acc))
					return true;
			}
			return false;
		}
		// else if (object instanceof ProteinGroupOccurrence) {
		// ProteinGroupOccurrence pgo2 = (ProteinGroupOccurrence) object;
		// if (equals(pgo2.getFirstOccurrence()))
		// return true;
		// return false;
		// }
		return super.equals(object);
	}

	/*
	 * public int updateMinimum() { } List<ProteinGroup> getRecursive(
	 * ProteinGroup group, Iterator<Protein> it ) { List<ProteinGroup> res = new
	 * ArrayList<ProteinGroup>(); if( group == null ) group = new
	 * ProteinGroup(); res.add(group); if( !it.hasNext() ) return res;
	 * ProteinGroup group2 = (ProteinGroup)group.clone(); group2.add(it.next());
	 * res.addAll(getRecursive(group, it)); res.addAll(getRecursive(group2,
	 * it)); return res; }
	 */

	public List<String> getAccessions() {

		if (accessions != null)
			return accessions;
		accessions = new ArrayList<String>();
		for (GroupableProtein protein : this) {
			if (!accessions.contains(protein.getAccession()))
				accessions.add(protein.getAccession());
		}
		Collections.sort(accessions);
		return accessions;

	}

	@Override
	public void add(int index, GroupableProtein element) {
		accessions = null;
		super.add(index, element);
		key = null;
	}

	public ProteinEvidence getEvidence() {
		return evidence;
	}

	public void setEvidence(ProteinEvidence evidence) {
		this.evidence = evidence;

	}

	/**
	 * Gets all peptides from the proteins of the group
	 *
	 * @return
	 */
	public List<GroupablePSM> getPSMs() {
		// if (this.peptides == null || this.peptides.isEmpty()) {
		List<GroupablePSM> ret = new ArrayList<GroupablePSM>();
		Set<String> peptideIds = new HashSet<String>();
		for (GroupableProtein protein : this) {
			final List<GroupablePSM> psms = protein.getGroupablePSMs();
			if (psms != null)
				for (GroupablePSM psm : psms) {
					if (!peptideIds.contains(psm.getPSMIdentifier())) {
						peptideIds.add(psm.getPSMIdentifier());
						ret.add(psm);
					}
				}

		}
		// }

		return ret;
	}

	@Override
	public boolean add(GroupableProtein e) {
		key = null;
		return super.add(e);
	}

}
