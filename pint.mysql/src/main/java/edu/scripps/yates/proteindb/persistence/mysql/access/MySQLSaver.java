package edu.scripps.yates.proteindb.persistence.mysql.access;

import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.AmountType;
import edu.scripps.yates.proteindb.persistence.mysql.AnnotationType;
import edu.scripps.yates.proteindb.persistence.mysql.CombinationType;
import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.ConfidenceScoreType;
import edu.scripps.yates.proteindb.persistence.mysql.Gene;
import edu.scripps.yates.proteindb.persistence.mysql.Label;
import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Organism;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideAmount;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideScore;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession;
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
import edu.scripps.yates.proteindb.persistence.mysql.Threshold;
import edu.scripps.yates.proteindb.persistence.mysql.Tissue;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.ConditionAdapter;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.ProjectAdapter;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import gnu.trove.set.hash.THashSet;

/**
 * This class provides the methods for the appropiate saving of the data in the
 * database, but it doesn-t handle the session, and the potential exceptions
 * that could be generated (rollbacks needed)
 *
 * @author Salva
 *
 */
public class MySQLSaver {
	private final static Logger log = Logger.getLogger(MySQLSaver.class);
	private int geneLength = 0;

	private boolean saveProtein(edu.scripps.yates.proteindb.persistence.mysql.Protein protein) {
		final Set<ProteinAccession> proteinAccessions = protein.getProteinAccessions();
		if (protein.getId() != null)
			return true;
		// not save protein without PSMs
		if (protein.getPsms().isEmpty()) {
			log.info("Not saving protein " + PersistenceUtils.getPrimaryAccession(protein).getAccession() + " in MSRun "
					+ protein.getMsRun().getRunId() + " for not having PSMs");
			return false;
		}
		saveMSRun(protein.getMsRun());

		// organism
		final Organism organism = protein.getOrganism();
		if (organism != null) {
			saveOrganismForProtein(organism, protein);

		}

		ContextualSessionHandler.save(protein);
		if (protein.getId() == null) {
			log.info("Protein with no ID after saving it");
		}
		// log.debug("Saving protein " + next.getAccession());
		// protein accesssions

		if (proteinAccessions.isEmpty())
			log.error("The protein has to have accessions");
		boolean thereisPrimaryAcc = false;
		for (ProteinAccession proteinAccession : proteinAccessions) {
			if (proteinAccession.isIsPrimary())
				thereisPrimaryAcc = true;
			saveProteinAccession(proteinAccession, protein);
		}
		if (!thereisPrimaryAcc)
			log.error("The protein has to have one primary acc");
		// protein annotations
		final Set<ProteinAnnotation> proteinAnnotations = protein.getProteinAnnotations();
		for (ProteinAnnotation proteinAnnotation : proteinAnnotations) {
			saveProteinAnnotation(proteinAnnotation);
		}
		// genes
		final Set<Gene> genes = protein.getGenes();
		for (final Gene gene : genes) {
			saveGene(gene);
		}

		// applied threshold
		final Set<ProteinThreshold> appliedThresholds = protein.getProteinThresholds();
		for (ProteinThreshold appliedThreshold : appliedThresholds) {
			saveAppliedThreshold(appliedThreshold);
		}
		// scores
		final Set<ProteinScore> proteinScores = protein.getProteinScores();
		if (proteinScores != null) {
			for (ProteinScore proteinScore : proteinScores) {
				saveProteinScore(proteinScore);
			}
		}

		// // protein ratios
		// final Set<ProteinRatioValue> proteinRatios = protein
		// .getProteinRatioValues();
		// for (ProteinRatioValue proteinRatio : proteinRatios) {
		// saveProteinRatio(proteinRatio);
		// }
		// peptide
		// if (proteinPeptideRelation ==
		// ProteinPeptideRelationship.PROTEINS_HAS_PEPTIDES) {
		final Set<Peptide> peptides = protein.getPeptides();
		for (Peptide peptide : peptides) {
			savePeptide(peptide);
		}

		final Set<Psm> psms = protein.getPsms();
		for (Psm psm : psms) {
			if (psm.getId() != null)
				continue;

			for (Peptide peptide : peptides) {
				if (psm.getSequence().equals(peptide.getSequence())) {
					Set<Psm> psms2 = peptide.getPsms();
					boolean found = false;
					for (Psm psm2 : psms2) {
						if (psm2 == psm) {
							found = true;
						}
					}
					if (!found) {
						log.info(psm.getPsmId() + " not found in peptide " + peptide.getSequence());
					}
				}
			}
			savePSM(psm);
		}
		// }

		// amounts
		final Set<ProteinAmount> amounts = protein.getProteinAmounts();
		if (amounts != null) {
			for (ProteinAmount amount : amounts) {
				saveProteinAmount(amount);
			}
		}
		return true;
	}

	private void savePSM(Psm psm) {
		if (psm.getId() != null)
			return;
		final MsRun msRun = psm.getMsRun();
		saveMSRun(msRun);

		savePeptide(psm.getPeptide());

		ContextualSessionHandler.save(psm);
		if (psm.getId() == null) {
			log.info("PSM with no ID after saving it");
		}
		final Set<Ptm> ptms = psm.getPtms();
		if (ptms != null) {
			for (Ptm ptm : ptms) {
				savePTM(ptm);
			}
		}
		final Set<PsmScore> scores = psm.getPsmScores();
		if (scores != null) {
			for (PsmScore psmScore : scores) {
				savePSMScore(psmScore);
			}
		}
		final Set<PsmAmount> amounts = psm.getPsmAmounts();
		if (amounts != null) {
			for (PsmAmount psmAmount : amounts) {
				savePsmAmount(psmAmount);
			}
		}
		final Set<Protein> proteins = psm.getProteins();
		if (proteins != null) {

			for (Protein protein : proteins) {
				saveProtein(protein);
			}
		}
	}

	private void savePeptide(Peptide peptide) {
		if (peptide == null) {
			log.info("Peptide is null");
		}
		if (peptide.getId() != null)
			return;
		saveMSRun(peptide.getMsRun());
		if (peptide.getSequence() == null)
			System.out.println("Sequence is null");

		ContextualSessionHandler.save(peptide);
		if (peptide.getId() == null) {
			log.info("Peptide with no ID after saving it");
		}
		// scores
		final Set<PeptideScore> scores = peptide.getPeptideScores();
		if (scores != null) {
			for (PeptideScore psmScore : scores) {
				savePeptideScore(psmScore);
			}
		}
		// amounts
		final Set<PeptideAmount> amounts = peptide.getPeptideAmounts();
		if (amounts != null) {
			for (PeptideAmount peptideAmount : amounts) {
				savePeptideAmount(peptideAmount);
			}
		}
		// // ratios
		// final Set<PeptideRatioValue> peptideRatioValues =
		// peptide.getPeptideRatioValues();
		// if (peptideRatioValues != null) {
		// for (PeptideRatioValue peptideRatioValue : peptideRatioValues) {
		// savePeptideRatio(peptideRatioValue);
		// }
		// }
		// psms
		final Set<Psm> psms = peptide.getPsms();
		if (psms != null) {
			for (Psm psm : psms) {
				savePSM(psm);
			}
		}
		// proteins
		final Set<Protein> proteins = peptide.getProteins();
		if (proteins != null) {
			for (Protein protein : proteins) {
				saveProtein(protein);
			}
		}

	}

	private void savePSMScore(PsmScore psmScore) {

		final ConfidenceScoreType confidenceScoreType = psmScore.getConfidenceScoreType();
		if (confidenceScoreType != null) {
			saveConfidenceScoreTypeForPsmScore(confidenceScoreType, psmScore);
		}
		if (psmScore.getPsm() == null) {
			log.info(psmScore);
		} else if (psmScore.getPsm().getId() == null) {
			log.info(psmScore);
		}
		ContextualSessionHandler.save(psmScore);
	}

	private void savePeptideScore(PeptideScore score) {

		final ConfidenceScoreType confidenceScoreType = score.getConfidenceScoreType();
		if (confidenceScoreType != null) {
			saveConfidenceScoreTypeForPeptideScore(confidenceScoreType, score);
		}
		ContextualSessionHandler.save(score);
	}

	private void saveProteinScore(ProteinScore score) {

		final ConfidenceScoreType confidenceScoreType = score.getConfidenceScoreType();
		if (confidenceScoreType != null) {
			saveConfidenceScoreTypeForProteinScore(confidenceScoreType, score);
		}
		ContextualSessionHandler.save(score);
	}

	private void savePTM(Ptm ptm) {
		ContextualSessionHandler.save(ptm);
		final Set<PtmSite> ptmSites = ptm.getPtmSites();
		if (ptmSites != null) {
			for (PtmSite ptmSite : ptmSites) {
				final ConfidenceScoreType confidenceScoreType = ptmSite.getConfidenceScoreType();
				if (confidenceScoreType != null) {
					saveConfidenceScoreTypeForPtmSite(confidenceScoreType, ptmSite);
				}
				ContextualSessionHandler.save(ptmSite);
			}
		}

	}

	private void saveGene(Gene gene) {
		if (geneLength < gene.getGeneId().length())
			geneLength = gene.getGeneId().length();
		ContextualSessionHandler.save(gene);

	}

	private void saveProteinRatio(ProteinRatioValue proteinRatioValue) {
		if (proteinRatioValue.getProtein() == null) {
			log.info("asdf");
		}
		boolean proteinSaved = saveProtein(proteinRatioValue.getProtein());
		if (!proteinSaved) {
			return;
		}
		ContextualSessionHandler.save(proteinRatioValue);
		final ConfidenceScoreType scoreType = proteinRatioValue.getConfidenceScoreType();
		if (scoreType != null) {
			saveConfidenceScoreTypeForProteinRatioValue(scoreType, proteinRatioValue);
		}

		// combination type
		CombinationType combinationType = proteinRatioValue.getCombinationType();
		if (combinationType != null) {
			saveCombinationTypeForProteinRatioValue(combinationType, proteinRatioValue);
		}

	}

	private void savePeptideRatio(PeptideRatioValue peptideRatioValue) {

		savePeptide(peptideRatioValue.getPeptide());

		ContextualSessionHandler.save(peptideRatioValue);

		final ConfidenceScoreType scoreType = peptideRatioValue.getConfidenceScoreType();
		if (scoreType != null) {
			saveConfidenceScoreTypeForPeptideRatioValue(scoreType, peptideRatioValue);

		}

		// combination type
		CombinationType combinationType = peptideRatioValue.getCombinationType();
		if (combinationType != null) {
			saveCombinationTypeForPeptideRatioValue(combinationType, peptideRatioValue);
		}

	}

	private void savePsmRatio(PsmRatioValue psmRatioValue) {

		savePSM(psmRatioValue.getPsm());
		ContextualSessionHandler.save(psmRatioValue);
		final ConfidenceScoreType scoreType = psmRatioValue.getConfidenceScoreType();
		if (scoreType != null) {
			saveConfidenceScoreTypeForPsmRatioValue(scoreType, psmRatioValue);

		}

		// combination type
		CombinationType combinationType = psmRatioValue.getCombinationType();
		if (combinationType != null) {
			saveCombinationTypeForPsmRatioValue(combinationType, psmRatioValue);
		}

	}

	private void saveRatioDescriptor(RatioDescriptor ratioDescriptor) {
		if (ratioDescriptor.getId() != null)
			return;
		final Condition expCondition1 = ratioDescriptor.getConditionByExperimentalCondition1Id();
		final Condition expCondition2 = ratioDescriptor.getConditionByExperimentalCondition2Id();

		saveExperimentalCondition(expCondition1);

		saveExperimentalCondition(expCondition2);

		ContextualSessionHandler.save(ratioDescriptor);

		final Set<ProteinRatioValue> proteinRatioValues = ratioDescriptor.getProteinRatioValues();
		if (proteinRatioValues != null) {
			for (ProteinRatioValue proteinRatioValue : proteinRatioValues) {
				saveProteinRatio(proteinRatioValue);
			}
		}

		final Set<PeptideRatioValue> peptideRatioValues = ratioDescriptor.getPeptideRatioValues();
		if (peptideRatioValues != null) {
			for (PeptideRatioValue peptideRatioValue : peptideRatioValues) {
				savePeptideRatio(peptideRatioValue);
			}
		}

		final Set<PsmRatioValue> psmRatioValues = ratioDescriptor.getPsmRatioValues();
		if (psmRatioValues != null) {
			for (PsmRatioValue psmRatioValue : psmRatioValues) {
				savePsmRatio(psmRatioValue);
			}
		}
	}

	private void saveAppliedThreshold(ProteinThreshold appliedThreshold) {
		final Threshold threshold = appliedThreshold.getThreshold();

		ContextualSessionHandler.save(threshold);

		ContextualSessionHandler.save(appliedThreshold);
	}

	private void saveProteinAmount(ProteinAmount proteinAmount) {

		// // depending protein amounts
		// final Set<ProteinAmount> proteinAmountsForProteinAmountChild =
		// proteinAmount
		// .getProteinAmountsForProteinAmountChild();
		// for (ProteinAmount proteinAmountChild :
		// proteinAmountsForProteinAmountChild) {
		// saveProteinAmount(proteinAmountChild);
		// }

		// amount type
		final AmountType amountType = proteinAmount.getAmountType();
		if (amountType != null) {
			saveAmountTypeForProtein(amountType, proteinAmount);
		}

		// combination type
		CombinationType combinationType = proteinAmount.getCombinationType();
		if (combinationType != null) {
			saveCombinationTypeForProteinAmount(combinationType, proteinAmount);

		}
		// condition
		final Condition condition = proteinAmount.getCondition();
		saveExperimentalCondition(condition);

		// Protein
		// saveProtein(proteinAmount.getProtein());

		ContextualSessionHandler.save(proteinAmount);

	}

	private void savePeptideAmount(PeptideAmount peptideAmount) {

		// msruns
		// final Set<MsRun> msRuns = peptideAmount.getMsRuns();
		// for (MsRun msRun : msRuns) {
		// saveMSRun(msRun);
		// }

		// // depending protein amounts
		// final Set<ProteinAmount> proteinAmountsForProteinAmountChild =
		// proteinAmount
		// .getProteinAmountsForProteinAmountChild();
		// for (ProteinAmount proteinAmountChild :
		// proteinAmountsForProteinAmountChild) {
		// saveProteinAmount(proteinAmountChild);
		// }

		// amount type
		final AmountType amountType = peptideAmount.getAmountType();
		if (amountType != null) {
			saveAmountTypeForPeptide(amountType, peptideAmount);
		}

		// combination type
		CombinationType combinationType = peptideAmount.getCombinationType();
		if (combinationType != null) {
			saveCombinationTypeForPeptideAmount(combinationType, peptideAmount);

		}
		// condition
		final Condition condition = peptideAmount.getCondition();
		saveExperimentalCondition(condition);

		// // PSM
		// savePSM(peptideAmount.getPsm());

		ContextualSessionHandler.save(peptideAmount);

	}

	private void savePsmAmount(PsmAmount psmAmount) {

		// msruns
		// final Set<MsRun> msRuns = peptideAmount.getMsRuns();
		// for (MsRun msRun : msRuns) {
		// saveMSRun(msRun);
		// }

		// // depending protein amounts
		// final Set<ProteinAmount> proteinAmountsForProteinAmountChild =
		// proteinAmount
		// .getProteinAmountsForProteinAmountChild();
		// for (ProteinAmount proteinAmountChild :
		// proteinAmountsForProteinAmountChild) {
		// saveProteinAmount(proteinAmountChild);
		// }

		// amount type
		final AmountType amountType = psmAmount.getAmountType();
		if (amountType != null) {
			saveAmountTypeForPSM(amountType, psmAmount);
		}

		// combination type
		CombinationType combinationType = psmAmount.getCombinationType();
		if (combinationType != null) {
			saveCombinationTypeForPsmAmount(combinationType, psmAmount);

		}
		// condition
		final Condition condition = psmAmount.getCondition();
		saveExperimentalCondition(condition);

		// // PSM
		// savePSM(psmAmount.getPsm());
		if (psmAmount.getPsm() == null) {
			log.info(psmAmount);
		} else if (psmAmount.getPsm().getId() == null) {
			log.info(psmAmount);
		}
		ContextualSessionHandler.save(psmAmount);

	}

	public void saveProteinAccession(ProteinAccession proteinAccession, Protein protein) {

		if (proteinAccession == null || proteinAccession.getAccession() == null)
			log.warn("CUIDADO");
		final ProteinAccession proteinAccessionInDB = ContextualSessionHandler.load(proteinAccession.getAccession(),
				ProteinAccession.class);
		if (proteinAccessionInDB != null) {
			if (proteinAccession.hashCode() == proteinAccessionInDB.hashCode()) {
				return;
			}
			proteinAccessionInDB.setAccessionType(proteinAccession.getAccessionType());
			proteinAccessionInDB.setDescription(proteinAccession.getDescription());
			if (proteinAccession.isIsPrimary()) {
				proteinAccessionInDB.setIsPrimary(proteinAccession.isIsPrimary());
			}
			proteinAccessionInDB.setAlternativeNames(proteinAccession.getAlternativeNames());
			// proteinAccessionInDB.getProteins().add(protein);
			protein.getProteinAccessions().remove(proteinAccession);
			protein.getProteinAccessions().add(proteinAccessionInDB);
			ContextualSessionHandler.saveOrUpdate(protein);
			ContextualSessionHandler.saveOrUpdate(proteinAccessionInDB);
			proteinAccession = proteinAccessionInDB;
		} else {
			// log.debug("saving " + proteinAccession.getAccession());
			ContextualSessionHandler.save(proteinAccession);

		}
	}

	private void saveAmountTypeForProtein(AmountType amountType, ProteinAmount proteinAmount) {
		final AmountType amountTypeInDB = ContextualSessionHandler.load(amountType.getName(), AmountType.class);
		if (amountTypeInDB != null) {
			// amountTypeInDB.getProteinAmounts().add(proteinAmount);
			proteinAmount.setAmountType(amountTypeInDB);
			// ContextualSessionHandler.saveOrUpdate(proteinAmount);
			ContextualSessionHandler.saveOrUpdate(amountTypeInDB);
			amountType = amountTypeInDB;
		} else {
			ContextualSessionHandler.save(amountType);

		}

	}

	private void saveAmountTypeForPeptide(AmountType amountType, PeptideAmount peptideAmount) {
		final AmountType amountTypeInDB = ContextualSessionHandler.load(amountType.getName(), AmountType.class);
		if (amountTypeInDB != null) {
			// amountTypeInDB.getPeptideAmounts().add(peptideAmount);
			peptideAmount.setAmountType(amountTypeInDB);
			// ContextualSessionHandler.saveOrUpdate(peptideAmount);
			ContextualSessionHandler.saveOrUpdate(amountTypeInDB);
			amountType = amountTypeInDB;
		} else {
			ContextualSessionHandler.save(amountType);
		}

	}

	private void saveAmountTypeForPSM(AmountType amountType, PsmAmount psmAmount) {
		final AmountType amountTypeInDB = ContextualSessionHandler.load(amountType.getName(), AmountType.class);
		if (amountTypeInDB != null) {
			// amountTypeInDB.getPsmAmounts().add(psmAmount);
			psmAmount.setAmountType(amountTypeInDB);
			// ContextualSessionHandler.saveOrUpdate(psmAmount);
			ContextualSessionHandler.saveOrUpdate(amountTypeInDB);
			amountType = amountTypeInDB;
		} else {
			ContextualSessionHandler.save(amountType);

		}

	}

	private void saveCombinationTypeForProteinRatioValue(CombinationType combinationType,
			ProteinRatioValue proteinRatioValue) {
		final CombinationType combinationTypeInDB = ContextualSessionHandler.load(combinationType.getName(),
				CombinationType.class);
		if (combinationTypeInDB != null) {
			combinationTypeInDB.setDescription(combinationType.getDescription());
			// combinationTypeInDB.getProteinRatioValues().add(proteinRatioValue);
			proteinRatioValue.setCombinationType(combinationTypeInDB);
			ContextualSessionHandler.saveOrUpdate(proteinRatioValue);
			ContextualSessionHandler.saveOrUpdate(combinationTypeInDB);
			combinationType = combinationTypeInDB;
		} else {
			ContextualSessionHandler.save(combinationType);

		}

	}

	private void saveCombinationTypeForPeptideRatioValue(CombinationType combinationType,
			PeptideRatioValue peptideRatioValue) {
		final CombinationType combinationTypeInDB = ContextualSessionHandler.load(combinationType.getName(),
				CombinationType.class);
		if (combinationTypeInDB != null) {
			combinationTypeInDB.setDescription(combinationType.getDescription());
			// combinationTypeInDB.getPeptideRatioValues().add(peptideRatioValue);
			peptideRatioValue.setCombinationType(combinationTypeInDB);
			ContextualSessionHandler.saveOrUpdate(peptideRatioValue);
			ContextualSessionHandler.saveOrUpdate(combinationTypeInDB);
			combinationType = combinationTypeInDB;
		} else {
			ContextualSessionHandler.save(combinationType);

		}

	}

	private void saveCombinationTypeForPsmRatioValue(CombinationType combinationType, PsmRatioValue psmRatioValue) {
		final CombinationType combinationTypeInDB = ContextualSessionHandler.load(combinationType.getName(),
				CombinationType.class);
		if (combinationTypeInDB != null) {
			combinationTypeInDB.setDescription(combinationType.getDescription());
			// combinationTypeInDB.getPsmRatioValues().add(psmRatioValue);
			psmRatioValue.setCombinationType(combinationTypeInDB);
			ContextualSessionHandler.saveOrUpdate(psmRatioValue);
			ContextualSessionHandler.saveOrUpdate(combinationTypeInDB);
			combinationType = combinationTypeInDB;
		} else {
			ContextualSessionHandler.save(combinationType);

		}

	}

	private void saveCombinationTypeForProteinAmount(CombinationType combinationType, ProteinAmount proteinAmount) {
		final CombinationType combinationTypeInDB = ContextualSessionHandler.load(combinationType.getName(),
				CombinationType.class);
		if (combinationTypeInDB != null) {
			combinationTypeInDB.setDescription(combinationType.getDescription());
			// combinationTypeInDB.getProteinAmounts().add(proteinAmount);
			proteinAmount.setCombinationType(combinationTypeInDB);
			// ContextualSessionHandler.saveOrUpdate(proteinAmount);
			ContextualSessionHandler.saveOrUpdate(combinationTypeInDB);
			combinationType = combinationTypeInDB;
		} else {
			ContextualSessionHandler.save(combinationType);

		}

	}

	private void saveCombinationTypeForPeptideAmount(CombinationType combinationType, PeptideAmount peptideAmount) {
		final CombinationType combinationTypeInDB = ContextualSessionHandler.load(combinationType.getName(),
				CombinationType.class);
		if (combinationTypeInDB != null) {
			combinationTypeInDB.setDescription(combinationType.getDescription());
			// combinationTypeInDB.getPeptideAmounts().add(peptideAmount);
			peptideAmount.setCombinationType(combinationTypeInDB);
			// ContextualSessionHandler.saveOrUpdate(peptideAmount);
			ContextualSessionHandler.saveOrUpdate(combinationTypeInDB);
			combinationType = combinationTypeInDB;
		} else {
			ContextualSessionHandler.save(combinationType);

		}

	}

	private void saveCombinationTypeForPsmAmount(CombinationType combinationType, PsmAmount psmAmount) {
		final CombinationType combinationTypeInDB = ContextualSessionHandler.load(combinationType.getName(),
				CombinationType.class);
		if (combinationTypeInDB != null) {
			combinationTypeInDB.setDescription(combinationType.getDescription());
			// combinationTypeInDB.getPsmAmounts().add(psmAmount);
			psmAmount.setCombinationType(combinationTypeInDB);
			// ContextualSessionHandler.saveOrUpdate(psmAmount);
			ContextualSessionHandler.saveOrUpdate(combinationTypeInDB);
			combinationType = combinationTypeInDB;
		} else {
			ContextualSessionHandler.save(combinationType);

		}

	}

	private void saveConfidenceScoreTypeForPsmScore(ConfidenceScoreType confidenceScoreType, PsmScore score) {
		final ConfidenceScoreType confidenceScoreTypeInDB = ContextualSessionHandler.load(confidenceScoreType.getName(),
				ConfidenceScoreType.class);
		if (confidenceScoreTypeInDB != null) {
			confidenceScoreTypeInDB.setName(confidenceScoreType.getName());
			confidenceScoreTypeInDB.setDescription(confidenceScoreType.getDescription());
			// confidenceScoreTypeInDB.getPsmScores().add(score);
			score.setConfidenceScoreType(confidenceScoreTypeInDB);
			// ContextualSessionHandler.saveOrUpdate(score);
			ContextualSessionHandler.saveOrUpdate(confidenceScoreTypeInDB);
			confidenceScoreType = confidenceScoreTypeInDB;
		} else {
			ContextualSessionHandler.save(confidenceScoreType);
		}

	}

	private void saveConfidenceScoreTypeForPeptideScore(ConfidenceScoreType confidenceScoreType, PeptideScore score) {
		final ConfidenceScoreType confidenceScoreTypeInDB = ContextualSessionHandler.load(confidenceScoreType.getName(),
				ConfidenceScoreType.class);
		if (confidenceScoreTypeInDB != null) {
			confidenceScoreTypeInDB.setName(confidenceScoreType.getName());
			confidenceScoreTypeInDB.setDescription(confidenceScoreType.getDescription());
			// confidenceScoreTypeInDB.getPeptideScores().add(score);
			score.setConfidenceScoreType(confidenceScoreTypeInDB);
			// ContextualSessionHandler.saveOrUpdate(score);
			ContextualSessionHandler.saveOrUpdate(confidenceScoreTypeInDB);
			confidenceScoreType = confidenceScoreTypeInDB;
		} else {
			ContextualSessionHandler.save(confidenceScoreType);
		}

	}

	private void saveConfidenceScoreTypeForProteinScore(ConfidenceScoreType confidenceScoreType, ProteinScore score) {
		final ConfidenceScoreType confidenceScoreTypeInDB = ContextualSessionHandler.load(confidenceScoreType.getName(),
				ConfidenceScoreType.class);
		if (confidenceScoreTypeInDB != null) {
			confidenceScoreTypeInDB.setName(confidenceScoreType.getName());
			confidenceScoreTypeInDB.setDescription(confidenceScoreType.getDescription());
			// confidenceScoreTypeInDB.getProteinScores().add(score);
			score.setConfidenceScoreType(confidenceScoreTypeInDB);
			// ContextualSessionHandler.saveOrUpdate(score);
			ContextualSessionHandler.saveOrUpdate(confidenceScoreTypeInDB);
			confidenceScoreType = confidenceScoreTypeInDB;
		} else {
			ContextualSessionHandler.save(confidenceScoreType);
		}

	}

	private void saveConfidenceScoreTypeForPtmSite(ConfidenceScoreType confidenceScoreType, PtmSite ptmSite) {
		final ConfidenceScoreType confidenceScoreTypeInDB = ContextualSessionHandler.load(confidenceScoreType.getName(),
				ConfidenceScoreType.class);
		if (confidenceScoreTypeInDB != null) {
			confidenceScoreTypeInDB.setName(confidenceScoreType.getName());
			confidenceScoreTypeInDB.setDescription(confidenceScoreType.getDescription());
			// confidenceScoreTypeInDB.getPtmSites().add(ptmSite);
			ptmSite.setConfidenceScoreType(confidenceScoreTypeInDB);
			// ContextualSessionHandler.saveOrUpdate(ptmSite);
			ContextualSessionHandler.saveOrUpdate(confidenceScoreTypeInDB);
			confidenceScoreType = confidenceScoreTypeInDB;
		} else {
			ContextualSessionHandler.save(confidenceScoreType);
		}

	}

	private void saveConfidenceScoreTypeForProteinRatioValue(ConfidenceScoreType confidenceScoreType,
			ProteinRatioValue proteinRatioValue) {
		final ConfidenceScoreType confidenceScoreTypeInDB = ContextualSessionHandler.load(confidenceScoreType.getName(),
				ConfidenceScoreType.class);
		if (confidenceScoreTypeInDB != null) {
			confidenceScoreTypeInDB.setName(confidenceScoreType.getName());
			confidenceScoreTypeInDB.setDescription(confidenceScoreType.getDescription());
			// confidenceScoreTypeInDB.getProteinRatioValues().add(proteinRatioValue);
			proteinRatioValue.setConfidenceScoreType(confidenceScoreTypeInDB);
			ContextualSessionHandler.saveOrUpdate(proteinRatioValue);
			ContextualSessionHandler.saveOrUpdate(confidenceScoreTypeInDB);
			confidenceScoreType = confidenceScoreTypeInDB;
		} else {
			ContextualSessionHandler.save(confidenceScoreType);
		}

	}

	private void saveConfidenceScoreTypeForPeptideRatioValue(ConfidenceScoreType confidenceScoreType,
			PeptideRatioValue peptideRatioValue) {
		final ConfidenceScoreType confidenceScoreTypeInDB = ContextualSessionHandler.load(confidenceScoreType.getName(),
				ConfidenceScoreType.class);
		if (confidenceScoreTypeInDB != null) {
			confidenceScoreTypeInDB.setName(confidenceScoreType.getName());
			confidenceScoreTypeInDB.setDescription(confidenceScoreType.getDescription());
			// confidenceScoreTypeInDB.getPeptideRatioValues().add(peptideRatioValue);
			peptideRatioValue.setConfidenceScoreType(confidenceScoreTypeInDB);
			ContextualSessionHandler.saveOrUpdate(peptideRatioValue);
			ContextualSessionHandler.saveOrUpdate(confidenceScoreTypeInDB);
			confidenceScoreType = confidenceScoreTypeInDB;
		} else {
			ContextualSessionHandler.save(confidenceScoreType);
		}

	}

	private void saveConfidenceScoreTypeForPsmRatioValue(ConfidenceScoreType confidenceScoreType,
			PsmRatioValue psmRatioValue) {
		final ConfidenceScoreType confidenceScoreTypeInDB = ContextualSessionHandler.load(confidenceScoreType.getName(),
				ConfidenceScoreType.class);
		if (confidenceScoreTypeInDB != null) {
			confidenceScoreTypeInDB.setName(confidenceScoreType.getName());
			confidenceScoreTypeInDB.setDescription(confidenceScoreType.getDescription());
			// confidenceScoreTypeInDB.getPsmRatioValues().add(psmRatioValue);
			psmRatioValue.setConfidenceScoreType(confidenceScoreTypeInDB);
			ContextualSessionHandler.saveOrUpdate(psmRatioValue);
			ContextualSessionHandler.saveOrUpdate(confidenceScoreTypeInDB);
			confidenceScoreType = confidenceScoreTypeInDB;
		} else {
			ContextualSessionHandler.save(confidenceScoreType);
		}

	}

	private void saveProteinAnnotation(ProteinAnnotation proteinAnnotation) {

		final AnnotationType annotationType = proteinAnnotation.getAnnotationType();
		saveAnnotationType(annotationType, proteinAnnotation);

		if (proteinAnnotation.getProtein() == null || proteinAnnotation.getProtein().getId() == null)
			log.debug("ASDF");
		// log.debug("Saving proteinAnnotation");
		ContextualSessionHandler.save(proteinAnnotation);
		// log.debug("proteinAnnotation saved with id="
		// + proteinAnnotation.getId());

	}

	private void saveAnnotationType(AnnotationType annotationType, ProteinAnnotation proteinAnnotation) {
		final AnnotationType annotationTypeDB = ContextualSessionHandler.load(annotationType.getName(),
				AnnotationType.class);
		if (annotationTypeDB != null) {
			// annotationTypeDB.getProteinAnnotations().add(proteinAnnotation);
			proteinAnnotation.setAnnotationType(annotationTypeDB);
			// ContextualSessionHandler.saveOrUpdate(proteinAnnotation);
			ContextualSessionHandler.saveOrUpdate(annotationTypeDB);
			annotationType = annotationTypeDB;

		} else {
			ContextualSessionHandler.save(annotationType);

		}

	}

	private void saveMSRun(MsRun msRun) {
		if (msRun == null)
			log.info("ms run is null!!!");
		if (msRun.getId() != null)
			return;
		// final MsRun oldMsRun = ManagedSessionHandler.load(msRun.getId(),
		// MsRun.class);
		// if (oldMsRun != null) {
		// oldMsRun.setPath(msRun.getPath());
		// oldMsRun.setDate(msRun.getDate());
		// oldMsRun.getPsms().addAll(msRun.getPsms());
		// ManagedSessionHandler.saveOrUpdate(oldMsRun);
		// msRun = oldMsRun;
		// } else {
		// ManagedSessionHandler.save(msRun);
		// }
		ContextualSessionHandler.save(msRun);
	}

	public void saveExperimentalCondition(Condition hibExperimentalCondition) {
		if (hibExperimentalCondition.getId() != null)
			return;
		log.info("Saving condition: " + hibExperimentalCondition.getName() + " of project "
				+ hibExperimentalCondition.getProject().getName());
		final Sample sample = hibExperimentalCondition.getSample();
		saveSample(sample);

		ContextualSessionHandler.save(hibExperimentalCondition);

		// proteinamounts
		final Set<Protein> proteins = hibExperimentalCondition.getProteins();
		log.info("Saving " + proteins.size() + " proteins");
		Set<Protein> discardedProteins = new THashSet<Protein>();
		if (proteins != null && !proteins.isEmpty()) {
			int size = proteins.size();
			int i = 1;
			for (Protein protein : proteins) {
				i++;
				if (i % 100 == 0) {
					log.info(i * 100 / size + "% of proteins saved in condition " + hibExperimentalCondition.getName());
				}
				if (protein.getId() != null) {
					continue;
				}
				// not save protein without PSMs
				if (protein.getPsms().isEmpty()) {
					// log.debug("Not saving protein " +
					// PersistenceUtils.getPrimaryAccession(protein).getAccession()
					// + " in MSRun " + protein.getMsRun().getRunId() + " for
					// not having PSMs");
					discardedProteins.add(protein);
				} else {
					saveProtein(protein);
				}
			}
			if (!discardedProteins.isEmpty()) {
				log.warn("Removing " + discardedProteins.size() + " proteins from condition "
						+ hibExperimentalCondition.getName() + " condition without PSMs");
				for (Protein protein : discardedProteins) {
					final boolean removed = hibExperimentalCondition.getProteins().remove(protein);
				}
				if (hibExperimentalCondition.getProteins().isEmpty()) {
					throw new IllegalArgumentException("The condition '" + hibExperimentalCondition.getName()
							+ "' has not proteins with at least one PSM");
				}
			}
		} else {
			log.info("Condition without proteins");
			final Set<Psm> psms = hibExperimentalCondition.getPsms();
			int size = psms.size();
			int i = 1;
			for (Psm psm : psms) {
				if (psm.getId() != null) {
					continue;
				}
				if (i % 1000 == 0 || i == size) {
					log.debug("Saving psm " + i + "/" + size);
				}
				savePSM(psm);
				i++;
			}
		}
		// save all PSMs
		final Set<Psm> psms = hibExperimentalCondition.getPsms();
		int size = psms.size();
		int i = 1;
		for (Psm psm : psms) {
			if (psm.getId() != null) {
				continue;
			}
			if (i % 1000 == 0 || i == size) {
				log.debug("Saving psm " + i + "/" + size);
			}
			savePSM(psm);
			i++;
		}

		// save all the protein ratios
		Set<RatioDescriptor> ratioDescriptorsForExperimentalCondition1Id = hibExperimentalCondition
				.getRatioDescriptorsForExperimentalCondition1Id();
		for (RatioDescriptor ratioDescriptor : ratioDescriptorsForExperimentalCondition1Id) {
			saveRatioDescriptor(ratioDescriptor);
		}

		Set<RatioDescriptor> ratioDescriptorsForExperimentalCondition2Id = hibExperimentalCondition
				.getRatioDescriptorsForExperimentalCondition2Id();
		for (RatioDescriptor ratioDescriptor : ratioDescriptorsForExperimentalCondition2Id) {
			saveRatioDescriptor(ratioDescriptor);
		}

	}

	public Integer saveProject(edu.scripps.yates.utilities.proteomicsmodel.Project project) {

		// check if there is already a project with that name
		String projectTag = project.getTag();

		// look into the database if a project with the same name is already
		// created
		Project hibProject = MySQLProteinDBInterface.getDBProjectByTag(projectTag);
		if (hibProject == null) {
			ProjectAdapter.clearStaticInformation();
			hibProject = new ProjectAdapter(project).adapt();
			ContextualSessionHandler.save(hibProject);
			// experiments
			final Set<Condition> hibExperimentConditions = hibProject.getConditions();
			for (Condition condition : hibExperimentConditions) {
				saveExperimentalCondition(condition);
			}
		} else {
			log.warn("Project '" + projectTag
					+ "' already present in the database. The experiments will be attached to that project with identifier: "
					+ hibProject.getId());
			final Set<edu.scripps.yates.utilities.proteomicsmodel.Condition> conditions = project.getConditions();
			for (edu.scripps.yates.utilities.proteomicsmodel.Condition condition : conditions) {
				// Condition hibCondition =
				// MySQLProteinDBInterface.getDBConditionByName(projectTag,
				// condition.getName());
				// if (hibCondition == null) {
				Condition hibCondition = new ConditionAdapter(condition, hibProject).adapt();
				saveExperimentalCondition(hibCondition);
				// } else {
				//
				// }
			}
		}
		log.info("Flushing session");
		ContextualSessionHandler.flush();
		log.info("Session flushed");
		log.info("Clearing session");
		ContextualSessionHandler.clear();
		log.info("Session cleared");
		return hibProject.getId();
	}

	private void saveSample(Sample sample) {
		if (sample.getId() != null)
			return;
		final Tissue tissue = sample.getTissue();
		if (tissue != null) {
			saveTissue(tissue, sample);
		}

		final Label label = sample.getLabel();
		if (label != null) {
			saveLabel(label, sample);
		}
		ContextualSessionHandler.save(sample);

		final Set<Organism> organisms = sample.getOrganisms();
		for (Organism organism : organisms) {
			saveOrganismForSample(organism, sample);
		}
	}

	private void saveLabel(Label label, Sample sample) {
		final Label oldLabel = ContextualSessionHandler.load(label.getId(), Label.class);
		if (oldLabel != null) {
			oldLabel.setName(label.getName());
			oldLabel.setMassDiff(label.getMassDiff());
			// oldLabel.getSamples().add(sample);
			sample.setLabel(oldLabel);
			// ContextualSessionHandler.saveOrUpdate(sample);
			ContextualSessionHandler.saveOrUpdate(oldLabel);
			label = oldLabel;
		} else {
			ContextualSessionHandler.save(label);
		}
	}

	private void saveTissue(Tissue tissue, Sample sample) {
		final Tissue oldTissue = ContextualSessionHandler.load(tissue.getTissueId(), Tissue.class);
		if (oldTissue != null) {
			oldTissue.setName(tissue.getName());
			// oldTissue.getSamples().add(sample);
			sample.setTissue(oldTissue);
			// ContextualSessionHandler.saveOrUpdate(sample);
			ContextualSessionHandler.saveOrUpdate(oldTissue);
			tissue = oldTissue;
		} else {
			ContextualSessionHandler.save(tissue);
		}

	}

	private void saveOrganismForSample(Organism organism, Sample sample) {
		if (organism == null) {
			log.info("CUIDADO");
			throw new IllegalArgumentException("The protein has no organism!");
		}
		final Organism oldOrganism = ContextualSessionHandler.load(organism.getTaxonomyId(), Organism.class);
		if (oldOrganism != null) {
			oldOrganism.setName(organism.getName());
			// oldOrganism.getSamples().add(sample);
			sample.getOrganisms().add(oldOrganism);
			sample.getOrganisms().remove(organism);
			ContextualSessionHandler.saveOrUpdate(sample);
			ContextualSessionHandler.saveOrUpdate(oldOrganism);
			organism = oldOrganism;
		} else {
			ContextualSessionHandler.save(organism);
		}
	}

	private void saveOrganismForProtein(Organism organism, Protein protein) {
		if (organism == null) {
			log.info("CUIDADO");
			throw new IllegalArgumentException("The protein has no organism!");
		}
		final Organism oldOrganism = ContextualSessionHandler.load(organism.getTaxonomyId(), Organism.class);
		if (oldOrganism != null) {
			oldOrganism.setName(organism.getName());
			// oldOrganism.getProteins().add(protein);
			protein.setOrganism(oldOrganism);
			ContextualSessionHandler.saveOrUpdate(protein);
			ContextualSessionHandler.saveOrUpdate(oldOrganism);
			organism = oldOrganism;
		} else {
			ContextualSessionHandler.save(organism);
		}
	}

}
