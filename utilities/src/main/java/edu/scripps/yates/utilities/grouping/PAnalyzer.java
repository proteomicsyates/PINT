package edu.scripps.yates.utilities.grouping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * 
 * @author gorka
 */
public class PAnalyzer {
	private static final Logger log = Logger
			.getLogger("log4j.logger.org.proteored");

	private final HashMap<String, InferenceProtein> mProts;
	private final HashMap<String, InferencePeptide> mPepts;
	private final List<ProteinGroupInference> mGroups;
	private PanalyzerStats mStats;
	private final boolean separateNonConclusiveProteins;

	public PAnalyzer(boolean separateNonConclusiveProteins) {
		mProts = new HashMap<String, InferenceProtein>();
		mPepts = new HashMap<String, InferencePeptide>();
		mGroups = new ArrayList<ProteinGroupInference>();
		this.separateNonConclusiveProteins = separateNonConclusiveProteins;
	}

	public List<ProteinGroup> run(Collection<GroupableProtein> proteins) {
		long t1 = System.currentTimeMillis();
		log.info("Grouping " + proteins.size() + " proteins");
		createInferenceMaps(proteins);
		log.debug("Running panalyzer for " + mProts.size() + " proteins and "
				+ mPepts.size() + " peptides");

		// long t2 = System.currentTimeMillis();
		log.debug("Classifying peptides");
		classifyPeptides();
		// long t3 = System.currentTimeMillis();
		log.debug("Classifying proteins");
		classifyProteins();
		// long t4 = System.currentTimeMillis();
		log.debug("creating groups");
		createGroups();
		// long t5 = System.currentTimeMillis();
		log.debug("marking indistinguibles");
		markIndistinguishable();
		// long t6 = System.currentTimeMillis();

		if (!separateNonConclusiveProteins) {
			log.info("Returning " + mGroups.size() + " protein groups from "
					+ proteins.size() + " proteins, in "
					+ (System.currentTimeMillis() - t1) + " milliseconds");
			log.debug("Collapsing Non conclusive groups");
			collapseNonConclusiveGroups();

		}

		log.debug("Extracting groups...");
		ArrayList<ProteinGroup> resultingGroups = extractProteinGroups();
		// long t7 = System.currentTimeMillis();

		// SorterUtil.sortProteinGroupsByAccession(resultingGroups);
		// this.fullGroupDump(System.out);
		// log.debug("Freeing memory");
		// Runtime.getRuntime().gc();
		// long t8 = System.currentTimeMillis();
		// log.info("Panalyzer times: " + (t2 - t1) + ", " + (t3 - t2) + ", "
		// + (t4 - t3) + ", " + (t5 - t4) + ", " + (t6 - t5) + ", "
		// + (t7 - t6) + ", " + (t8 - t7));

		log.info("Returning " + resultingGroups.size()
				+ " protein groups from " + proteins.size() + " proteins, in "
				+ (System.currentTimeMillis() - t1) + " milliseconds");

		return resultingGroups;
	}

	private void collapseNonConclusiveGroups() {
		final Iterator<ProteinGroupInference> iterator = mGroups.iterator();
		while (iterator.hasNext()) {
			ProteinGroupInference proteinGroup = iterator.next();
			InferenceProtein protein = proteinGroup.get(0);

			if (proteinGroup.getEvidence() == ProteinEvidence.NONCONCLUSIVE) {
				protein = proteinGroup.get(0);
				// non conclusive groups only have one protein at index = 0
				final int proteinHashCode = protein.hashCode();
				final List<InferencePeptide> peptides = protein
						.getInferencePeptides();
				for (InferencePeptide peptide : peptides) {
					if (peptide.getRelation() == PeptideRelation.NONDISCRIMINATING) {
						final List<InferenceProtein> proteinsSharingThisPeptide = peptide
								.getInferenceProteins();
						for (InferenceProtein proteinSharingThisPeptide : proteinsSharingThisPeptide) {
							if (proteinSharingThisPeptide.hashCode() != proteinHashCode) {
								if (!proteinSharingThisPeptide.getGroup()
										.contains(protein))
									proteinSharingThisPeptide.getGroup().add(
											protein);
								// protein.setEvidence(proteinSharingThisPeptide
								// .getGroup().getEvidence());

							}
						}
					}
				}
				iterator.remove();
			}
		}

	}

	private ArrayList<ProteinGroup> extractProteinGroups() {
		ArrayList<ProteinGroup> ret = new ArrayList<ProteinGroup>();

		for (ProteinGroupInference iProteinGroup : mGroups) {
			ProteinGroup pg = new ProteinGroup(iProteinGroup);
			ret.add(pg);
		}

		return ret;
	}

	private void createInferenceMaps(Collection<GroupableProtein> proteins) {
		InferenceProtein iProt = null;
		InferencePeptide iPept = null;
		Set<Integer> proteinIds = new HashSet<Integer>();
		boolean someProteinwithoutPeptides = false;
		for (GroupableProtein prot : proteins) {
			if (!proteinIds.contains(prot.getDBId())) {
				proteinIds.add(prot.getDBId());
				iProt = mProts.get(prot.getAccession());
				if (iProt == null) {
					iProt = new InferenceProtein(prot);
					mProts.put(iProt.getAccession(), iProt);
				} else {
					// merge in a common inference protein
					iProt.addProtein(prot);
				}

				List<GroupablePSM> peptides = prot.getGroupablePSMs();
				if (peptides != null && !peptides.isEmpty()) {
					for (GroupablePSM pept : peptides) {
						iPept = mPepts.get(pept.getSequence());
						if (iPept == null) {
							iPept = new InferencePeptide(pept);
							mPepts.put(pept.getSequence(), iPept);
						} else {
							// merge in a common inference peptide
							iPept.addPeptide(pept);
						}
						if (!iPept.getInferenceProteins().contains(iProt))
							iPept.getInferenceProteins().add(iProt);
						if (!iProt.getInferencePeptides().contains(iPept))
							iProt.getInferencePeptides().add(iPept);
					}
				} else {
					// log.warn(prot.getDBId() + " no tiene peptidos!");
					someProteinwithoutPeptides = true;
				}
			} else {
				log.warn("This protein is already taken ");
			}
		}
		if (someProteinwithoutPeptides)
			log.warn("some protein without peptides!!!!!");

	}

	private void classifyPeptides() {
		// Locate unique peptides
		for (InferencePeptide pept : mPepts.values()) {
			if (pept.getInferenceProteins().size() == 1) {
				pept.setRelation(PeptideRelation.UNIQUE);
				pept.getInferenceProteins().get(0)
						.setEvidence(ProteinEvidence.CONCLUSIVE);
			} else
				pept.setRelation(PeptideRelation.DISCRIMINATING);
		}
		// Locate non-meaningful peptides (first round)
		for (InferenceProtein prot : mProts.values())
			if (prot.getEvidence() == ProteinEvidence.CONCLUSIVE)
				// if conclusive is because they have a unique peptide
				for (InferencePeptide pept : prot.getInferencePeptides())
					if (pept.getRelation() != PeptideRelation.UNIQUE)
						pept.setRelation(PeptideRelation.NONDISCRIMINATING);

		// Locate non-meaningful peptides (second round)
		boolean shared;
		for (InferencePeptide pept : mPepts.values()) {
			if (pept.getRelation() != PeptideRelation.DISCRIMINATING)
				continue;
			for (InferencePeptide pept2 : pept.getInferenceProteins().get(0)
					.getInferencePeptides()) {
				if (pept2.getRelation() == PeptideRelation.NONDISCRIMINATING)
					continue;
				if (pept2.getInferenceProteins().size() <= pept
						.getInferenceProteins().size())
					continue;
				shared = true;
				for (InferenceProtein p : pept.getInferenceProteins())
					if (!p.getInferencePeptides().contains(pept2)) {
						shared = false;
						break;
					}
				if (shared)
					pept2.setRelation(PeptideRelation.NONDISCRIMINATING);
			}
		}
	}

	private void classifyProteins() {
		boolean group;

		for (InferenceProtein prot : mProts.values()) {
			if (prot.getEvidence() == ProteinEvidence.CONCLUSIVE)
				continue;
			List<InferencePeptide> peptides = prot.getInferencePeptides();
			if (peptides == null || peptides.isEmpty()) {
				prot.setEvidence(ProteinEvidence.FILTERED);
				continue;
			}

			group = false;
			for (InferencePeptide pept : peptides)
				if (pept.getRelation() == PeptideRelation.DISCRIMINATING) {
					group = true;
					break;
				}
			prot.setEvidence(group ? ProteinEvidence.AMBIGUOUSGROUP
					: ProteinEvidence.NONCONCLUSIVE);
		}

	}

	private void createGroups() {
		for (InferenceProtein prot : mProts.values()) {
			if (prot.getGroup() == null) {
				ProteinGroupInference group = new ProteinGroupInference(
						prot.getEvidence());
				group.add(prot);
				prot.setGroup(group);
				mGroups.add(group);
			}
			if (prot.getEvidence() != ProteinEvidence.AMBIGUOUSGROUP)
				continue;
			for (InferencePeptide pept : prot.getInferencePeptides()) {
				if (pept.getRelation() != PeptideRelation.DISCRIMINATING)
					continue;
				for (InferenceProtein subp : pept.getInferenceProteins()) {
					if (subp.getEvidence() != ProteinEvidence.AMBIGUOUSGROUP)
						continue;
					if (subp.getGroup() != null) { // merge groups
						if (subp.getGroup() == prot.getGroup())
							continue;
						mGroups.remove(prot.getGroup());
						subp.getGroup().addAll(prot.getGroup());
						for (InferenceProtein pg : prot.getGroup())
							pg.setGroup(subp.getGroup());
						continue;
					}
					prot.getGroup().add(subp);
					subp.setGroup(prot.getGroup());
				}
			}
		}
	}

	// private void markIndistinguishable() {
	// boolean indistinguishable;
	// for (ProteinGroupInference group : mGroups) {
	// if (group.getEvidence() != ProteinEvidence.AMBIGUOUSGROUP)
	// continue;
	// indistinguishable = true;
	// for (InferencePeptide pept : group.get(0).getInferencePeptides()) {
	// if (pept.getRelation() != PeptideRelation.DISCRIMINATING)
	// continue;
	// for (InferenceProtein prot : group) {
	// if (!prot.getInferencePeptides().contains(pept)) {
	// indistinguishable = false;
	// break;
	// }
	// }
	// if (!indistinguishable)
	// break;
	// }
	// if (indistinguishable)
	// group.setEvidence(ProteinEvidence.INDISTINGUISHABLE);
	// }
	//
	// }
	private void markIndistinguishable() {
		Set<InferencePeptide> discriminating = new HashSet<InferencePeptide>();
		boolean indistinguishable;
		for (ProteinGroupInference group : mGroups) {
			if (group.getEvidence() != ProteinEvidence.AMBIGUOUSGROUP
					|| group.size() < 2)
				continue;
			indistinguishable = true;
			for (InferenceProtein prot : group)
				for (InferencePeptide pept : prot.getInferencePeptides())
					if (pept.getRelation() == PeptideRelation.DISCRIMINATING)
						discriminating.add(pept);
			for (InferenceProtein prot : group)
				if (!prot.getInferencePeptides().containsAll(discriminating)) {
					indistinguishable = false;
					break;
				}
			discriminating.clear();
			if (indistinguishable) {
				group.setEvidence(ProteinEvidence.INDISTINGUISHABLE);
				for (InferenceProtein prot : group)
					prot.setEvidence(ProteinEvidence.INDISTINGUISHABLE);
			}
		}
	}

	private PanalyzerStats getStats(List<ProteinGroupInference> groups) {
		return new PanalyzerStats(groups);
	}

	public PanalyzerStats getStats() {
		if (mStats == null)
			mStats = getStats(mGroups);
		return mStats;
	}

}
