package edu.scripps.yates.utilities.grouping;

import java.util.ArrayList;
import java.util.List;

public class InferencePeptide {
	private final List<InferenceProtein> inferenceProteins = new ArrayList<InferenceProtein>();
	private List<GroupablePSM> mergedPeptides = new ArrayList<GroupablePSM>();
	private PeptideRelation relation;
	private final String id;
	private final String sequence;

	public InferencePeptide(GroupablePSM pept) {
		this(pept, PeptideRelation.NONDISCRIMINATING);
	}

	public InferencePeptide(GroupablePSM pept, PeptideRelation r) {

		relation = r;
		mergedPeptides.add(pept);
		id = pept.getPSMIdentifier();
		sequence = pept.getSequence();
	}

	public void addPeptide(GroupablePSM p) {
		mergedPeptides.add(p);
	}

	public List<GroupablePSM> getPeptidesMerged() {
		return mergedPeptides;
	}

	@Override
	public String toString() {
		switch (relation) {
		case DISCRIMINATING:
			return getId() + "*";
		case NONDISCRIMINATING:
			return getId() + "**";
		}
		return new Integer(getId()).toString();
	}

	private String getId() {
		return id;
	}

	public List<InferenceProtein> getInferenceProteins() {
		return inferenceProteins;
	}

	public List<GroupablePSM> getMergedPeptides() {
		return mergedPeptides;
	}

	public void setMergedPeptides(List<GroupablePSM> mergedPeptides) {
		this.mergedPeptides = mergedPeptides;
	}

	public PeptideRelation getRelation() {
		return relation;
	}

	public void setRelation(PeptideRelation relation) {
		for (GroupablePSM p : mergedPeptides) {
			p.setRelation(relation);
		}
		this.relation = relation;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof InferencePeptide))
			return super.equals(obj);
		else {
			InferencePeptide peptide = (InferencePeptide) obj;
			return peptide.getSequence().equals(getSequence());
		}
	}

	private String getSequence() {
		return sequence;
	}
}
