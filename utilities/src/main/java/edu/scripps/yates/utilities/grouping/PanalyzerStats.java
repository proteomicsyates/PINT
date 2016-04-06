package edu.scripps.yates.utilities.grouping;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

public class PanalyzerStats {
	private static final Logger log = Logger
			.getLogger("log4j.logger.org.proteored");
	// public int PeptideCount;
	public int proteinCount;
	public int proteinMaxCount;
	public int proteinMinCount;
	public int conclusiveCount;
	public int nonConclusiveCount;
	public int groupedCount;
	public int ambiguousGroupCount;
	public int indistinguishableGroupCount;
	public int filteredCount;
	public int differentNonConclusiveCount;

	public PanalyzerStats(Collection<ProteinGroupInference> groups) {
		List<ProteinGroupInference> nonConclusiveGroups = new ArrayList<ProteinGroupInference>();
		for (ProteinGroupInference group : groups) {
			switch (group.getEvidence()) {
			case AMBIGUOUSGROUP:
				ambiguousGroupCount++;
				groupedCount += group.size();
				proteinCount++;
				break;
			case CONCLUSIVE:
				conclusiveCount++;
				proteinCount++;
				break;
			case FILTERED:
				filteredCount++;
				break;
			case INDISTINGUISHABLE:
				indistinguishableGroupCount++;
				groupedCount += group.size();
				proteinCount++;
				break;
			case NONCONCLUSIVE:
				nonConclusiveCount++;
				if (!nonConclusiveGroups.contains(group))
					nonConclusiveGroups.add(group);
				break;
			}
			if (group.getEvidence() != ProteinEvidence.FILTERED)
				proteinMaxCount += group.size();
		}
		// this.PeptideCount = mPepts.size();
		differentNonConclusiveCount = nonConclusiveGroups.size();
		log.debug("Returning " + differentNonConclusiveCount
				+ " different non conclusive groups and " + nonConclusiveCount
				+ " non conclusive groups");
		proteinMinCount = -1;
	}

	public PanalyzerStats(List<ProteinGroup> groups) {
		List<ProteinGroup> nonConclusiveGroups = new ArrayList<ProteinGroup>();
		for (ProteinGroup group : groups) {
			switch (group.getEvidence()) {
			case AMBIGUOUSGROUP:
				ambiguousGroupCount++;
				groupedCount += group.size();
				proteinCount++;
				break;
			case CONCLUSIVE:
				conclusiveCount++;
				proteinCount++;
				break;
			case FILTERED:
				filteredCount++;
				break;
			case INDISTINGUISHABLE:
				indistinguishableGroupCount++;
				groupedCount += group.size();
				proteinCount++;
				break;
			case NONCONCLUSIVE:
				nonConclusiveCount++;
				if (!nonConclusiveGroups.contains(group))
					nonConclusiveGroups.add(group);
				break;
			}
			if (group.getEvidence() != ProteinEvidence.FILTERED)
				proteinMaxCount += group.size();
		}
		// this.PeptideCount = mPepts.size();
		differentNonConclusiveCount = nonConclusiveGroups.size();
		log.debug("Returning " + differentNonConclusiveCount
				+ " different non conclusive groups and " + nonConclusiveCount
				+ " non conclusive groups");
		proteinMinCount = -1;
	}

	public void dump(PrintStream stream) {
		// stream.println("Peptide count: " + PeptideCount);
		stream.println(toString());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		// stream.println("Peptide count: " + PeptideCount);
		sb.append("\tTotal number of proteins: " + proteinMaxCount + "\n");
		sb.append("\tEffective \"protein\" count: " + proteinCount + "\n\n");

		sb.append("\tConclusive proteins: " + conclusiveCount + "\n");
		sb.append("\tNon-conclusive proteins: " + nonConclusiveCount + "\n");
		sb.append("\tIndistinguishable groups: " + indistinguishableGroupCount
				+ "\n");
		sb.append("\tAmbiguous groups: " + ambiguousGroupCount + "\n\n");
		sb.append("\tProteins in some group: " + groupedCount + "\n");
		sb.append("\tProteins filtered: " + filteredCount + "\n");

		return sb.toString();
	}

}
