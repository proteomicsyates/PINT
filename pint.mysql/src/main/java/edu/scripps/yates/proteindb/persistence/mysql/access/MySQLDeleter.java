package edu.scripps.yates.proteindb.persistence.mysql.access;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.AmountType;
import edu.scripps.yates.proteindb.persistence.mysql.AnnotationType;
import edu.scripps.yates.proteindb.persistence.mysql.CombinationType;
import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.ConfidenceScoreType;
import edu.scripps.yates.proteindb.persistence.mysql.Label;
import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Organism;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideAmount;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideScore;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAmount;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAnnotation;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinScore;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinThreshold;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.PsmAmount;
import edu.scripps.yates.proteindb.persistence.mysql.PsmRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.PsmScore;
import edu.scripps.yates.proteindb.persistence.mysql.Ptm;
import edu.scripps.yates.proteindb.persistence.mysql.PtmSite;
import edu.scripps.yates.proteindb.persistence.mysql.RatioDescriptor;
import edu.scripps.yates.proteindb.persistence.mysql.Sample;
import edu.scripps.yates.proteindb.persistence.mysql.Tissue;
import edu.scripps.yates.utilities.progresscounter.ProgressCounter;
import edu.scripps.yates.utilities.progresscounter.ProgressPrintingType;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

/**
 * This class provides the methods for the appropiate deletion of the data in
 * the database, but it doesn-t handle the session, and the potential exceptions
 * that could be generated (rollbacks needed)
 *
 * @author Salva
 *
 */
public class MySQLDeleter {
	private final static Logger log = Logger.getLogger(MySQLDeleter.class);
	private final Set<Psm> deletedPsms = new THashSet<Psm>();
	private final Set<Peptide> deletedPeptides = new THashSet<Peptide>();
	private final Set<Protein> deletedProteins = new THashSet<Protein>();

	private void deleteProtein(edu.scripps.yates.proteindb.persistence.mysql.Protein protein) {
		if (deletedProteins.contains(protein)) {
			return;
		}
		deletedProteins.add(protein);
		if (protein.getId() == null) {
			return;
		}
		// final Set<ProteinAccession> proteinAccessions =
		// protein.getProteinAccessions();

		// protein accesssions

		// for (ProteinAccession proteinAccession : proteinAccessions) {
		// deleteProteinAccession(proteinAccession);
		// }

		// protein annotations
		final Set<ProteinAnnotation> proteinAnnotations = protein.getProteinAnnotations();
		for (final ProteinAnnotation proteinAnnotation : proteinAnnotations) {
			deleteProteinAnnotation(proteinAnnotation);
		}
		// genes
		// final Set<Gene> genes = protein.getGenes();
		// for (final Gene gene : genes) {
		// deleteGene(gene);
		// }

		// applied threshold
		final Set<ProteinThreshold> appliedThresholds = protein.getProteinThresholds();
		for (final ProteinThreshold appliedThreshold : appliedThresholds) {
			deleteAppliedThreshold(appliedThreshold);
		}
		// scores
		final Set<ProteinScore> proteinScores = protein.getProteinScores();
		if (proteinScores != null) {
			for (final ProteinScore proteinScore : proteinScores) {
				deleteProteinScore(proteinScore);
			}
		}

		// // protein ratios
		final Set<ProteinRatioValue> proteinRatios = protein.getProteinRatioValues();
		for (final ProteinRatioValue proteinRatio : proteinRatios) {
			deleteProteinRatio(proteinRatio);
		}

		// amounts
		final Set<ProteinAmount> amounts = protein.getProteinAmounts();
		for (final ProteinAmount amount : amounts) {
			deleteProteinAmount(amount);
		}

		// conditions
//		final Set<Condition> conditions = protein.getConditions();
//		for (final Condition condition : conditions) {
//			condition.getProteins().remove(protein);
//		}
		protein.getConditions().clear();

		protein.getMsRuns().clear();

		// peptides are the owners of protein-peptide relationships
		final Iterator<Peptide> peptideIterator = protein.getPeptides().iterator();
		while (peptideIterator.hasNext()) {
			final Peptide peptide = peptideIterator.next();
			peptide.getProteins().remove(protein);
			peptideIterator.remove();
			deletePeptide(peptide);
		}

		// psms are the owners of the protein-psm relationship
		final Iterator<Psm> psmsIterator = protein.getPsms().iterator();
		while (psmsIterator.hasNext()) {
			final Psm psm = psmsIterator.next();
			psm.getProteins().remove(protein);
			psmsIterator.remove();
			deletePSM(psm);
		}

		// delete protein who is not the owner of the relationship
		ContextualSessionHandler.delete(protein);
	}

	private void deleteOrganism(Organism organism) {
		if (organism.getProteins().isEmpty() && organism.getSamples().isEmpty()) {
			ContextualSessionHandler.delete(organism);
		}
	}

	private void deletePSM(Psm psm) {
		if (deletedPsms.contains(psm)) {
			return;
		}
		deletedPsms.add(psm);
		if (psm.getId() == null) {
			return;
		}
		// final MsRun msRun = psm.getMsRun();
		// msRun.getPsms().remove(psm);
		// deleteMSRun(msRun);

		final Set<Ptm> ptms = psm.getPtms();
		if (ptms != null) {
			for (final Ptm ptm : ptms) {
				deletePTM(ptm);
			}
		}
		final Set<PsmScore> scores = psm.getPsmScores();
		if (scores != null) {
			for (final PsmScore psmScore : scores) {
				deletePSMScore(psmScore);
			}
		}
		final Set<PsmAmount> amounts = psm.getPsmAmounts();
		if (amounts != null) {
			for (final PsmAmount psmAmount : amounts) {
				deletePsmAmount(psmAmount);
			}
		}
		// ratios
		final Set<PsmRatioValue> psmRatioValues = psm.getPsmRatioValues();
		if (psmRatioValues != null) {
			for (final PsmRatioValue psmRatioValue : psmRatioValues) {
				deletePsmRatio(psmRatioValue);
			}
		}

		// conditions
//		final Set<Condition> conditions = psm.getConditions();
//		for (final Condition condition : conditions) {
//			condition.getPsms().remove(psm);
//		}
		psm.getConditions().clear();

		psm.setMsRun(null);
		final Iterator<Protein> iterator = psm.getProteins().iterator();
		while (iterator.hasNext()) {
			final Protein protein = iterator.next();
			protein.getPsms().remove(psm);
			iterator.remove();
		}
		ContextualSessionHandler.delete(psm);
	}

	private void deletePeptide(Peptide peptide) {
		if (deletedPeptides.contains(peptide)) {
			return;
		}
		deletedPeptides.add(peptide);
		if (peptide.getId() == null) {
			return;
		}
		// deleteMSRun(peptide.getMsRun());

		final Set<Ptm> ptms = peptide.getPtms();
		if (ptms != null) {
			for (final Ptm ptm : ptms) {
				deletePTM(ptm);
			}
		}
		// scores
		final Set<PeptideScore> scores = peptide.getPeptideScores();
		if (scores != null) {
			for (final PeptideScore psmScore : scores) {
				deletePeptideScore(psmScore);
			}
		}
		// amounts
		final Set<PeptideAmount> amounts = peptide.getPeptideAmounts();
		if (amounts != null) {
			for (final PeptideAmount peptideAmount : amounts) {
				deletePeptideAmount(peptideAmount);
			}
		}
		// ratios
		final Set<PeptideRatioValue> peptideRatioValues = peptide.getPeptideRatioValues();
		if (peptideRatioValues != null) {
			for (final PeptideRatioValue peptideRatioValue : peptideRatioValues) {
				deletePeptideRatio(peptideRatioValue);
			}
		}

		// conditions
//		final Set<Condition> conditions = peptide.getConditions();
//		for (final Condition condition : conditions) {
//			condition.getPeptides().remove(peptide);
//		}
		peptide.getConditions().clear();
		peptide.getMsRuns().clear();

		// psms are the owners
		final Iterator<Psm> psmsIterator = peptide.getPsms().iterator();
		while (psmsIterator.hasNext()) {
			final Psm psm = psmsIterator.next();
			deletePSM(psm);
			psmsIterator.remove();
		}
		final Iterator<Protein> proteinIterator = peptide.getProteins().iterator();
		while (proteinIterator.hasNext()) {
			final Protein protein = proteinIterator.next();
			protein.getPeptides().remove(peptide);
			proteinIterator.remove();
		}
		ContextualSessionHandler.delete(peptide);

	}

	private void deletePSMScore(PsmScore psmScore) {

		// final ConfidenceScoreType confidenceScoreType =
		// psmScore.getConfidenceScoreType();
		// if (confidenceScoreType != null) {
		// confidenceScoreType.getPsmScores().remove(psmScore);
		//
		// deleteConfidenceScoreType(confidenceScoreType);
		//
		// }
		ContextualSessionHandler.delete(psmScore);
	}

	private void deletePeptideScore(PeptideScore score) {

		// final ConfidenceScoreType confidenceScoreType =
		// score.getConfidenceScoreType();
		// if (confidenceScoreType != null) {
		// deleteConfidenceScoreType(confidenceScoreType);
		// }
		ContextualSessionHandler.delete(score);
	}

	private void deleteProteinScore(ProteinScore score) {

		// final ConfidenceScoreType confidenceScoreType =
		// score.getConfidenceScoreType();
		// if (confidenceScoreType != null) {
		// deleteConfidenceScoreType(confidenceScoreType);
		// }
		ContextualSessionHandler.delete(score);
	}

	private void deletePTM(Ptm ptm) {
		final Set<PtmSite> ptmSites = ptm.getPtmSites();
		if (false) {
			for (final PtmSite ptmSite : ptmSites) {
				// final ConfidenceScoreType confidenceScoreType =
				// ptmSite.getConfidenceScoreType();
				// if (confidenceScoreType != null) {
				// confidenceScoreType.getPtmSites().remove(ptmSite);
				// deleteConfidenceScoreType(confidenceScoreType);
				// }
				ContextualSessionHandler.delete(ptmSite);
			}
		}
		ContextualSessionHandler.delete(ptm);

	}

	private void deleteProteinRatio(ProteinRatioValue proteinRatioValue) {
		// deleteRatioDescriptor(proteinRatioValue.getRatioDescriptor());
		// final ConfidenceScoreType scoreType =
		// proteinRatioValue.getConfidenceScoreType();
		// if (scoreType != null) {
		// deleteConfidenceScoreType(scoreType);
		//
		// }

		// combination type
		// CombinationType combinationType =
		// proteinRatioValue.getCombinationType();
		// if (combinationType != null) {
		// deleteCombinationType(combinationType);
		// }

		ContextualSessionHandler.delete(proteinRatioValue);
//		proteinRatioValue.getRatioDescriptor().getProteinRatioValues().remove(proteinRatioValue);

	}

	private void deletePeptideRatio(PeptideRatioValue peptideRatioValue) {

		// deletePeptide(peptideRatioValue.getPeptide());

		// final ConfidenceScoreType scoreType =
		// peptideRatioValue.getConfidenceScoreType();
		// if (scoreType != null) {
		// deleteConfidenceScoreType(scoreType);
		//
		// }

		// combination type
		// CombinationType combinationType =
		// peptideRatioValue.getCombinationType();
		// if (combinationType != null) {
		// deleteCombinationType(combinationType);
		// }

		ContextualSessionHandler.delete(peptideRatioValue);
	}

	private void deletePsmRatio(PsmRatioValue psmRatioValue) {

		// final ConfidenceScoreType scoreType =
		// psmRatioValue.getConfidenceScoreType();
		// if (scoreType != null) {
		// deleteConfidenceScoreType(scoreType);
		//
		// }

		// combination type
		// CombinationType combinationType = psmRatioValue.getCombinationType();
		// if (combinationType != null) {
		// deleteCombinationType(combinationType);
		// }

		ContextualSessionHandler.delete(psmRatioValue);
	}

	private void deleteRatioDescriptor(RatioDescriptor ratioDescriptor) {

		ContextualSessionHandler.delete(ratioDescriptor);

		// final Set<ProteinRatioValue> proteinRatioValues =
		// ratioDescriptor.getProteinRatioValues();
		// if (proteinRatioValues != null) {
		// for (ProteinRatioValue proteinRatioValue : proteinRatioValues) {
		// deleteProteinRatio(proteinRatioValue);
		// }
		// }
		//
		// final Set<PeptideRatioValue> peptideRatioValues =
		// ratioDescriptor.getPeptideRatioValues();
		// if (peptideRatioValues != null) {
		// for (PeptideRatioValue peptideRatioValue : peptideRatioValues) {
		// deletePeptideRatio(peptideRatioValue);
		// }
		// }
		//
		// final Set<PsmRatioValue> psmRatioValues =
		// ratioDescriptor.getPsmRatioValues();
		// if (psmRatioValues != null) {
		// for (PsmRatioValue psmRatioValue : psmRatioValues) {
		// deletePsmRatio(psmRatioValue);
		// }
		// }
	}

	private void deleteAppliedThreshold(ProteinThreshold appliedThreshold) {
		// deleteThreshold(appliedThreshold.getThreshold());

		ContextualSessionHandler.delete(appliedThreshold);
	}

	private void deleteProteinAmount(ProteinAmount proteinAmount) {
		proteinAmount.getCondition().getProteinAmounts().remove(proteinAmount);
		ContextualSessionHandler.delete(proteinAmount);
		// amount type
		// final AmountType amountType = proteinAmount.getAmountType();
		// if (amountType != null)
		// deleteAmountType(amountType);

		// combination type
		// CombinationType combinationType = proteinAmount.getCombinationType();
		// if (combinationType != null) {
		// deleteCombinationType(combinationType);
		// }

	}

	private void deletePeptideAmount(PeptideAmount peptideAmount) {
		peptideAmount.getCondition().getPeptideAmounts().remove(peptideAmount);

		ContextualSessionHandler.delete(peptideAmount);
		// amount type
		// final AmountType amountType = peptideAmount.getAmountType();
		// if (amountType != null)
		// deleteAmountType(amountType);

		// combination type
		// CombinationType combinationType = peptideAmount.getCombinationType();
		// if (combinationType != null) {
		// deleteCombinationType(combinationType);
		//
		// }
	}

	private void deletePsmAmount(PsmAmount psmAmount) {
		psmAmount.getCondition().getPsmAmounts().remove(psmAmount);
		ContextualSessionHandler.delete(psmAmount);
		// amount type
		// final AmountType amountType = psmAmount.getAmountType();
		// if (amountType != null)
		// deleteAmountType(amountType);

		// combination type
		// CombinationType combinationType = psmAmount.getCombinationType();
		// if (combinationType != null) {
		// deleteCombinationType(combinationType);
		// }
	}

	private void deleteAmountType(AmountType amountType) {

		ContextualSessionHandler.delete(amountType);
	}

	private void deleteCombinationType(CombinationType combinationType) {

		ContextualSessionHandler.delete(combinationType);
	}

	private void deleteConfidenceScoreType(ConfidenceScoreType confidenceScoreType) {
		if (confidenceScoreType.getPeptideRatioValues().isEmpty() && confidenceScoreType.getPeptideScores().isEmpty()
				&& confidenceScoreType.getProteinRatioValues().isEmpty()
				&& confidenceScoreType.getProteinScores().isEmpty() && confidenceScoreType.getPsmRatioValues().isEmpty()
				&& confidenceScoreType.getPsmScores().isEmpty() && confidenceScoreType.getPtmSites().isEmpty()) {
			ContextualSessionHandler.delete(confidenceScoreType);
		}
	}

	private void deleteProteinAnnotation(ProteinAnnotation proteinAnnotation) {

		// deleteAnnotationType(proteinAnnotation.getAnnotationType());

		ContextualSessionHandler.delete(proteinAnnotation);

	}

	private void deleteAnnotationType(AnnotationType annotationType) {

		ContextualSessionHandler.delete(annotationType);

	}

	private void deleteMSRun(MsRun msRun) throws InterruptedException {
		log.info("Deleting MSRun:  " + msRun.getRunId() + " of project " + msRun.getProject().getTag());
		ContextualSessionHandler.refresh(msRun);

		final Set<Protein> proteins = msRun.getProteins();
		ProgressCounter counter = new ProgressCounter(proteins.size(), ProgressPrintingType.PERCENTAGE_STEPS, 0);
		counter.setShowRemainingTime(true);
		counter.setSuffix("proteins deleted");
		final Iterator<Protein> iterator = proteins.iterator();
		while (iterator.hasNext()) {
			final Protein protein = iterator.next();
			counter.increment();
			final String printIfNecessary = counter.printIfNecessary();
			if (printIfNecessary != null && !"".equals(printIfNecessary)) {
				ContextualSessionHandler.flush();
				log.info(printIfNecessary);
			}
			deleteProtein(protein);
			iterator.remove();
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
		}
		final Set<Peptide> peptides = msRun.getPeptides();
		counter = new ProgressCounter(peptides.size(), ProgressPrintingType.PERCENTAGE_STEPS, 0);
		counter.setShowRemainingTime(true);
		counter.setSuffix("peptides deleted");
		final Iterator<Peptide> iterator2 = peptides.iterator();
		while (iterator2.hasNext()) {
			final Peptide peptide = iterator2.next();
			counter.increment();
			final String print = counter.printIfNecessary();
			if (print != null && !"".equals(print)) {
				ContextualSessionHandler.flush();
				log.info(print);
			}
			deletePeptide(peptide);
			iterator2.remove();
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
		}

		final Set<Psm> psms = msRun.getPsms();
		counter = new ProgressCounter(psms.size(), ProgressPrintingType.PERCENTAGE_STEPS, 0);
		counter.setShowRemainingTime(true);
		counter.setSuffix("PSMs deleted");
		final Iterator<Psm> iterator3 = psms.iterator();
		while (iterator3.hasNext()) {
			final Psm psm = iterator3.next();
			counter.increment();
			final String print = counter.printIfNecessary();
			if (print != null && !"".equals(print)) {
				ContextualSessionHandler.flush();
				log.info(print);
			}
			deletePSM(psm);
			iterator3.remove();
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
		}

		ContextualSessionHandler.delete(msRun);

	}

	public boolean deleteProject2(String projectTag) throws InterruptedException {

		// look into the database if a project with the same name is already
		// created
		final Project hibProject = MySQLProteinDBInterface.getDBProjectByTag(projectTag);
		if (hibProject != null) {
			log.info("deleting project " + hibProject.getTag());
			// get a map between MSRuns and Conditions
			final Map<MsRun, Set<Condition>> conditionsByMSRun = getConditionsByMSRun(hibProject);
			final Map<Condition, Set<MsRun>> msRunsByCondition = getMSRunsByCondition(conditionsByMSRun);
			int initialMSRunNumber = 0;
			final Set<MsRun> deletedMSRuns = new THashSet<MsRun>();
			final Set<Condition> deletedConditions = new THashSet<Condition>();
			while (true) {
				ContextualSessionHandler.beginGoodTransaction();
				ContextualSessionHandler.refresh(hibProject);
				final Set<MsRun> msRuns = hibProject.getMsRuns();
				if (initialMSRunNumber == 0) {
					initialMSRunNumber = msRuns.size();
				}
				log.info(msRuns.size() + " MSRuns to delete in project " + hibProject.getTag());
				final List<Condition> conditionList = new ArrayList<Condition>();
				conditionList.addAll(msRunsByCondition.keySet());
				Collections.sort(conditionList, new Comparator<Condition>() {

					@Override
					public int compare(Condition o1, Condition o2) {
						final Set<MsRun> msRuns1 = msRunsByCondition.get(o1);
						// String conditionString1 =
						// getConditionString(conditions1);
						final Set<MsRun> msRuns2 = msRunsByCondition.get(o2);
						// String conditionString2 =
						// getConditionString(conditions2);
						// return conditionString1.compareTo(conditionString2);
						return Integer.compare(msRuns1.size(), msRuns2.size());
					}

				});
				for (final Condition condition : conditionList) {
					final Set<MsRun> msRunList = msRunsByCondition.get(condition);
					ContextualSessionHandler.beginGoodTransaction();
					for (final MsRun msRun : msRunList) {
						if (deletedMSRuns.contains(msRun)) {
							continue;
						}
						deleteMSRun(msRun);
						deletedMSRuns.add(msRun);
						final Set<Condition> conditions = conditionsByMSRun.get(msRun);
						log.info(conditions.size() + " conditions associated with MSRun " + msRun.getRunId());
						log.info("conditions associated: " + getConditionString(conditionsByMSRun.get(msRun)));
						// check if all msruns of all conditions have been
						// deleted
						boolean allDeleted = true;
						if (conditions != null) {
							for (final Condition condition2 : conditions) {
								final Set<MsRun> msRunSet = msRunsByCondition.get(condition2);
								for (final MsRun msRun2 : msRunSet) {
									if (!deletedMSRuns.contains(msRun2)) {
										log.info(msRun2.getRunId() + " is not yet deleted... continuing the loop.");
										allDeleted = false;
										break;
									}
								}
							}
						}
						if (allDeleted) {
							log.info("Flushing session...");
							ContextualSessionHandler.flush();
							log.info("Clearing session...");
							ContextualSessionHandler.clear();
							log.info("Session clear.");
							ContextualSessionHandler.finishGoodTransaction();
							break;
						}
					}
				}
				if (initialMSRunNumber == deletedMSRuns.size()) {
					break;
				}
			}
			ContextualSessionHandler.beginGoodTransaction();
			ContextualSessionHandler.refresh(hibProject);
			final Set<Condition> conditions = hibProject.getConditions();

			for (final Condition condition : conditions) {

				deleteCondition(condition);

			}
			for (final Condition condition : conditions) {
				final Sample sample = condition.getSample();
				deleteSample(sample);
			}

			ContextualSessionHandler.delete(hibProject);

			return true;
		} else {
			throw new IllegalArgumentException(projectTag + " doesn't exist");
		}

	}

	public boolean deleteProject(String projectTag) throws InterruptedException {

		// look into the database if a project with the same name is already
		// created
		final Project hibProject = MySQLProteinDBInterface.getDBProjectByTag(projectTag);
		if (hibProject != null) {
			log.info("deleting project " + hibProject.getTag() + " with id: " + hibProject.getId());
			// get a map between MSRuns and Conditions
			final Map<MsRun, Set<Condition>> conditionsByMSRun = getConditionsByMSRun(hibProject);
//			final Map<Condition, Set<MsRun>> msRunsByCondition = getMSRunsByCondition(conditionsByMSRun);
			int initialMSRunNumber = 0;

			ContextualSessionHandler.beginGoodTransaction();
			ContextualSessionHandler.refresh(hibProject);
			final Set<MsRun> msRuns = hibProject.getMsRuns();
			for (final MsRun msRun : msRuns) {
				ContextualSessionHandler.beginGoodTransaction();
				deleteMSRun(msRun);
				log.info("Flushing session...");
				ContextualSessionHandler.flush();
//				log.info("Clearing session...");
//				ContextualSessionHandler.clear();
				log.info("Session clear. Now finishing transaction");
				ContextualSessionHandler.finishGoodTransaction();
				log.info("Transaction finished.");
			}
			ContextualSessionHandler.beginGoodTransaction();
			if (initialMSRunNumber == 0) {
				initialMSRunNumber = msRuns.size();
			}

			final Set<Condition> conditions = hibProject.getConditions();

			for (final Condition condition : conditions) {

				deleteCondition(condition);

			}
			for (final Condition condition : conditions) {
				final Sample sample = condition.getSample();
				deleteSample(sample);
			}

			ContextualSessionHandler.delete(hibProject);

			return true;
		} else {
			throw new IllegalArgumentException(projectTag + " doesn't exist");
		}

	}

	private String getConditionString(Set<Condition> conditions) {
		final List<String> list = new ArrayList<String>();
		for (final Condition condition : conditions) {
			if (!list.contains(condition.getName())) {
				list.add(condition.getName());
			}
		}
		final StringBuilder sb = new StringBuilder();
		Collections.sort(list);
		for (final String conditionName : list) {
			sb.append(conditionName + ",");
		}
		return sb.toString();
	}

	private Map<MsRun, Set<Condition>> getConditionsByMSRun(Project hibProject) {
		log.info("Getting conditions mapped to MSRuns...");
		final Map<MsRun, Set<Condition>> conditionsByMSRun = new THashMap<MsRun, Set<Condition>>();
		final Set<MsRun> msRuns = hibProject.getMsRuns();
		for (final MsRun msRun : msRuns) {
			// Set<Condition> conditions = new THashSet<Condition>();
			// final Set<Psm> psms = msRun.getPsms();
			// for (Psm psm : psms) {
			// conditions.addAll(psm.getConditions());
			// break;
			// }
			// psms.clear();
			// System.gc();
			// final Set<Protein> proteins = msRun.getProteins();
			// for (Protein protein : proteins) {
			// conditions.addAll(protein.getConditions());
			// break;
			// }
			// proteins.clear();
			// System.gc();
			// final Set<Peptide> peptides = msRun.getPeptides();
			// for (Peptide peptide : peptides) {
			// conditions.addAll(peptide.getConditions());
			// break;
			// }
			// peptides.clear();
			// System.gc();
			final List<Condition> conditions = PreparedCriteria.getConditionsByMSRunCriteria(msRun);
			final Set<Condition> set = new THashSet<Condition>();
			set.addAll(conditions);
			log.info("MSRun " + msRun.getRunId() + " mapped to " + conditions.size() + " conditions");
			conditionsByMSRun.put(msRun, set);

			if (true) {
				break;
			}
		}
		log.info(conditionsByMSRun.size() + " conditions mapped to MSRuns.");

		return conditionsByMSRun;
	}

	private void deleteCondition(Condition condition) {
		// ratio descriptors
		final Set<RatioDescriptor> ratioDescriptorsForExperimentalCondition1Id = condition
				.getRatioDescriptorsForExperimentalCondition1Id();
		for (final RatioDescriptor ratioDescriptor : ratioDescriptorsForExperimentalCondition1Id) {
			deleteRatioDescriptor(ratioDescriptor);
		}
		final Set<RatioDescriptor> ratioDescriptorsForExperimentalCondition2Id = condition
				.getRatioDescriptorsForExperimentalCondition2Id();
		for (final RatioDescriptor ratioDescriptor : ratioDescriptorsForExperimentalCondition2Id) {
			deleteRatioDescriptor(ratioDescriptor);
		}
		ContextualSessionHandler.delete(condition);
	}

	private Map<Condition, Set<MsRun>> getMSRunsByCondition(Map<MsRun, Set<Condition>> conditionsByMSRun) {
		final Map<Condition, Set<MsRun>> msRunsByCondition = new THashMap<Condition, Set<MsRun>>();
		log.info("Getting MSRuns mapped to conditions...");
		final Set<MsRun> msruns = conditionsByMSRun.keySet();
		for (final MsRun msrun : msruns) {
			final Set<Condition> conditions = conditionsByMSRun.get(msrun);
			for (final Condition condition2 : conditions) {
				if (msRunsByCondition.containsKey(condition2)) {
					msRunsByCondition.get(condition2).add(msrun);
				} else {
					final Set<MsRun> msRunSet = new THashSet<MsRun>();
					msRunSet.add(msrun);
					msRunsByCondition.put(condition2, msRunSet);
				}
			}
		}
		log.info(msRunsByCondition.size() + " MSRuns mapped to conditions.");

		return msRunsByCondition;
	}

	private void deleteSample(Sample sample) {

		final Tissue tissue = sample.getTissue();
		tissue.getSamples().remove(sample);
		if (tissue != null && tissue.getSamples().isEmpty()) {
			ContextualSessionHandler.delete(tissue);
		}

		final Label label = sample.getLabel();
		if (label != null && label.getSamples() != null) {
			label.getSamples().remove(sample);

			if (label.getSamples().isEmpty()) {
				ContextualSessionHandler.delete(label);
			}
		}
		final Set<Organism> organisms = sample.getOrganisms();
		for (final Organism organism : organisms) {
			if (organism.getSamples() != null) {
				organism.getSamples().remove(sample);

				if (organism.getSamples().isEmpty()) {
					deleteOrganism(organism);
				}
			}
		}
		ContextualSessionHandler.delete(sample);
	}

}
