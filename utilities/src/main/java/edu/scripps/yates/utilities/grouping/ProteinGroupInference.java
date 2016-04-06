package edu.scripps.yates.utilities.grouping;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ProteinGroupInference extends ArrayList<InferenceProtein> {
	private ProteinEvidence evidence;
	private ProteinGroupInference minimum;

	public ProteinGroupInference(ProteinEvidence e) {
		super();
		evidence = e;
		minimum = null;
	}

	public ProteinGroupInference() {
		this(ProteinEvidence.AMBIGUOUSGROUP);
	}

	@Override
	public String toString() {
		return this.getKey();
	}

	public String getKey() {
		String ret = "";

		List<String> accessions2 = this.getAccessions();
		int i = 0;
		for (String accession : accessions2) {
			if (i > 0) {
				ret = ret + ", ";
			}
			ret = ret + accession;
			i++;
		}

		return ret + "[" + this.evidence.toString() + "]";
	}

	public List<String> getAccessions() {
		List<String> accessions = new ArrayList<String>();
		for (InferenceProtein protein : this) {
			if (!accessions.contains(protein.getAccession()))
				accessions.add(protein.getAccession());
		}
		return accessions;

	}

	public void dump(PrintStream stream) {
		stream.println(evidence.toString());
		for (InferenceProtein prot : this) {
			stream.print("\t" + prot.getAccession() + ": ");
			for (InferencePeptide pept : prot.getInferencePeptides())
				stream.print(pept.toString() + " ");
			stream.println();
		}
	}

	public ProteinEvidence getEvidence() {
		return evidence;
	}

	public void setEvidence(ProteinEvidence evidence) {
		this.evidence = evidence;
	}

	public ProteinGroupInference getMinimum() {
		return minimum;
	}

	public void setMinimum(ProteinGroupInference minimum) {
		this.minimum = minimum;
	}

	@Override
	public boolean add(InferenceProtein e) {
		return super.add(e);
	}
	/*
	 * public int updateMinimum() { }
	 * 
	 * List<ProteinGroup> getRecursive( ProteinGroup group,
	 * Iterator<ExtendedIdentifiedProtein> it ) { List<ProteinGroup> res = new
	 * ArrayList<ProteinGroup>(); if( group == null ) group = new
	 * ProteinGroup(); res.add(group); if( !it.hasNext() ) return res;
	 * ProteinGroup group2 = (ProteinGroup)group.clone(); group2.add(it.next());
	 * res.addAll(getRecursive(group, it)); res.addAll(getRecursive(group2,
	 * it)); return res; }
	 */

}
