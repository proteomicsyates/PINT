package edu.scripps.yates.utilities.grouping;

import java.util.ArrayList;
import java.util.List;

public class InferenceProtein {

	private List<InferencePeptide> inferencePeptides = new ArrayList<InferencePeptide>();
	private List<GroupableProtein> proteinsMerged = new ArrayList<GroupableProtein>();
	private ProteinEvidence evidence;
	private ProteinGroupInference group;
	private final String accession;

	public InferenceProtein(GroupableProtein prot) {
		this(prot, ProteinEvidence.NONCONCLUSIVE);
	}

	public InferenceProtein(GroupableProtein prot, ProteinEvidence e) {

		evidence = e;
		group = null;
		proteinsMerged.add(prot);
		accession = prot.getAccession();
	}

	public void addProtein(GroupableProtein p) {
		proteinsMerged.add(p);
	}

	public List<GroupableProtein> getProteinsMerged() {
		return proteinsMerged;
	}

	public List<InferencePeptide> getInferencePeptides() {
		return inferencePeptides;
	}

	public void setInferencePeptides(List<InferencePeptide> inferencePeptides) {
		this.inferencePeptides = inferencePeptides;
	}

	public ProteinEvidence getEvidence() {
		return evidence;
	}

	public void setEvidence(ProteinEvidence evidence) {
		for (GroupableProtein p : getProteinsMerged())
			p.setEvidence(evidence);

		this.evidence = evidence;
	}

	public ProteinGroupInference getGroup() {
		return group;
	}

	public void setGroup(ProteinGroupInference group) {
		this.group = group;
	}

	public void setProteinsMerged(List<GroupableProtein> proteinsMerged) {
		this.proteinsMerged = proteinsMerged;
	}

	public String getAccession() {
		return accession;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof InferenceProtein))
			return super.equals(obj);
		else {
			InferenceProtein protein = (InferenceProtein) obj;
			return protein.getAccession().equals(getAccession());
		}
	}

}
