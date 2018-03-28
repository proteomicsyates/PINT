package edu.scripps.yates.annotations.uniprot.proteoform.model;

import java.util.Map;
import java.util.Set;

import edu.scripps.yates.annotations.uniprot.UniprotCVTermCode;
import edu.scripps.yates.annotations.uniprot.UniprotPTMCVTerm;

public class UniprotPTM {
	private final int positionInProtein;
	private final UniprotPTMCVTerm ptmCVTerm;

	public UniprotPTM(int positionInProtein, UniprotPTMCVTerm ptmCVTerm) {
		this.ptmCVTerm = ptmCVTerm;
		this.positionInProtein = positionInProtein;
	}

	public int getPositionInProtein() {
		return positionInProtein;
	}

	public UniprotPTMCVTerm getPtmCVTerm() {
		return ptmCVTerm;
	}

	public String getSingleValue(UniprotCVTermCode code) {
		return ptmCVTerm.getSingleValue(code);
	}

	public Set<String> getValueSet(UniprotCVTermCode code) {
		return ptmCVTerm.getValueSet(code);
	}

	public Map<UniprotCVTermCode, Set<String>> getValues() {
		return ptmCVTerm.getValues();
	}

	@Override
	public String toString() {
		return positionInProtein + " " + ptmCVTerm.getSingleValue(UniprotCVTermCode.ID);
	}
}
