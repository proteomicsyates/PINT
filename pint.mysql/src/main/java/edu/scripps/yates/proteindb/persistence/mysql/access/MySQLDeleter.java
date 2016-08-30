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
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;

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
	private int geneLength = 0;

	private void deleteProtein(edu.scripps.yates.proteindb.persistence.mysql.Protein protein) {
		if (PersistenceUtils.getPrimaryAccession(protein).getAccession().equals("P52907")) {
			log.info("asdf");
		}
		final Set<ProteinAccession> proteinAccessions = protein.getProteinAccessions();
		for (ProteinAccession proteinAccession : proteinAccessions) {
			if (proteinAccession.getAccession().equals("IPI00000769")) {
				log.info("asdf");
			}
		}
		if (protein.getId() != null)
			return;
		// not delete protein without PSMs
		if (protein.getPsms().isEmpty()) {
			log.info("Not saving protein " + PersistenceUtils.getPrimaryAccession(protein).getAccession() + " in MSRun "
					+ protein.getMsRun().getRunId() + " for not having PSMs");
			return;
		}
		deleteMSRun(protein.getMsRun());
		ContextualSessionHandler.delete(protein.getOrganism());

		ContextualSessionHandler.delete(protein);
		// log.debug("Saving protein " + next.getAccession());
		// protein accesssions

		if (proteinAccessions.isEmpty())
			log.error("The protein has to have accessions");
		boolean thereisPrimaryAcc = false;
		for (ProteinAccession proteinAccession : proteinAccessions) {
			if (proteinAccession.isIsPrimary())
				thereisPrimaryAcc = true;
			deleteProteinAccession(proteinAccession);
		}
		if (!thereisPrimaryAcc)
			log.error("The protein has to have one primary acc");
		// protein annotations
		final Set<ProteinAnnotation> proteinAnnotations = protein.getProteinAnnotations();
		for (ProteinAnnotation proteinAnnotation : proteinAnnotations) {
			deleteProteinAnnotation(proteinAnnotation);
		}
		// genes
		final Set<Gene> genes = protein.getGenes();
		for (final Gene gene : genes) {
			deleteGene(gene);
		}

		// applied threshold
		final Set<ProteinThreshold> appliedThresholds = protein.getProteinThresholds();
		for (ProteinThreshold appliedThreshold : appliedThresholds) {
			deleteAppliedThreshold(appliedThreshold);
		}
		// scores
		final Set<ProteinScore> proteinScores = protein.getProteinScores();
		if (proteinScores != null) {
			for (ProteinScore proteinScore : proteinScores) {
				deleteProteinScore(proteinScore);
			}
		}

		// organism
		final Organism organism = protein.getOrganism();
		if (organism != null) {
			ContextualSessionHandler.delete(organism);

		}
		// // protein ratios
		// final Set<ProteinRatioValue> proteinRatios = protein
		// .getProteinRatioValues();
		// for (ProteinRatioValue proteinRatio : proteinRatios) {
		// deleteProteinRatio(proteinRatio);
		// }
		// peptide
		// if (proteinPeptideRelation ==
		// ProteinPeptideRelationship.PROTEINS_HAS_PEPTIDES) {
		final Set<Peptide> peptides = protein.getPeptides();
		int i = 0;
		int size = peptides.size();
		for (Peptide peptide : peptides) {
			i++;
			if (peptide.getId() != null)
				continue;
			// if (i % 5 == 0 || i == size)
			// log.debug("Saving peptide " + i + "/" + size);
			deletePeptide(peptide);
		}
		// }

		// amounts
		final Set<ProteinAmount> amounts = protein.getProteinAmounts();
		if (amounts != null) {
			for (ProteinAmount amount : amounts) {
				deleteProteinAmount(amount);
			}
		}
	}

	private void deletePSM(Psm psm) {

		final MsRun msRun = psm.getMsRun();
		deleteMSRun(msRun);

		final Set<Ptm> ptms = psm.getPtms();
		if (ptms != null) {
			for (Ptm ptm : ptms) {
				deletePTM(ptm);
			}
		}
		final Set<PsmScore> scores = psm.getPsmScores();
		if (scores != null) {
			for (PsmScore psmScore : scores) {
				deletePSMScore(psmScore);
			}
		}
		final Set<PsmAmount> amounts = psm.getPsmAmounts();
		if (amounts != null) {
			for (PsmAmount psmAmount : amounts) {
				deletePsmAmount(psmAmount);
			}
		}
		// ratios
		final Set<PsmRatioValue> psmRatioValues = psm.getPsmRatioValues();
		if (psmRatioValues != null) {
			for (PsmRatioValue psmRatioValue : psmRatioValues) {
				deletePsmRatio(psmRatioValue);
			}
		}
		ContextualSessionHandler.delete(psm);
	}

	private void deletePeptide(Peptide peptide) {

		deleteMSRun(peptide.getMsRun());

		// scores
		final Set<PeptideScore> scores = peptide.getPeptideScores();
		if (scores != null) {
			for (PeptideScore psmScore : scores) {
				deletePeptideScore(psmScore);
			}
		}
		// amounts
		final Set<PeptideAmount> amounts = peptide.getPeptideAmounts();
		if (amounts != null) {
			for (PeptideAmount peptideAmount : amounts) {
				deletePeptideAmount(peptideAmount);
			}
		}
		// ratios
		final Set<PeptideRatioValue> peptideRatioValues = peptide.getPeptideRatioValues();
		if (peptideRatioValues != null) {
			for (PeptideRatioValue peptideRatioValue : peptideRatioValues) {
				deletePeptideRatio(peptideRatioValue);
			}
		}

		ContextualSessionHandler.delete(peptide);
	}

	private void deletePSMScore(PsmScore psmScore) {

		final ConfidenceScoreType confidenceScoreType = psmScore.getConfidenceScoreType();
		if (confidenceScoreType != null) {
			deleteConfidenceScoreType(confidenceScoreType);
		}
		ContextualSessionHandler.delete(psmScore);
	}

	private void deletePeptideScore(PeptideScore score) {

		final ConfidenceScoreType confidenceScoreType = score.getConfidenceScoreType();
		if (confidenceScoreType != null) {
			deleteConfidenceScoreType(confidenceScoreType);
		}
		ContextualSessionHandler.delete(score);
	}

	private void deleteProteinScore(ProteinScore score) {

		final ConfidenceScoreType confidenceScoreType = score.getConfidenceScoreType();
		if (confidenceScoreType != null) {
			deleteConfidenceScoreType(confidenceScoreType);
		}
		ContextualSessionHandler.delete(score);
	}

	private void deletePTM(Ptm ptm) {
		final Set<PtmSite> ptmSites = ptm.getPtmSites();

		for (PtmSite ptmSite : ptmSites) {
			final ConfidenceScoreType confidenceScoreType = ptmSite.getConfidenceScoreType();
			if (confidenceScoreType != null) {
				deleteConfidenceScoreType(confidenceScoreType);
			}
			ContextualSessionHandler.delete(ptmSite);
		}

		ContextualSessionHandler.delete(ptm);

	}

	private void deleteGene(Gene gene) {
		if (geneLength < gene.getGeneId().length())
			geneLength = gene.getGeneId().length();
		ContextualSessionHandler.delete(gene);

	}

	private void deleteProteinRatio(ProteinRatioValue proteinRatioValue) {

		deleteProtein(proteinRatioValue.getProtein());

		final ConfidenceScoreType scoreType = proteinRatioValue.getConfidenceScoreType();
		if (scoreType != null) {
			deleteConfidenceScoreType(scoreType);

		}

		// combination type
		CombinationType combinationType = proteinRatioValue.getCombinationType();
		if (combinationType != null) {
			deleteCombinationType(combinationType);
		}

		ContextualSessionHandler.delete(proteinRatioValue);
	}

	private void deletePeptideRatio(PeptideRatioValue peptideRatioValue) {

		deletePeptide(peptideRatioValue.getPeptide());

		final ConfidenceScoreType scoreType = peptideRatioValue.getConfidenceScoreType();
		if (scoreType != null) {
			deleteConfidenceScoreType(scoreType);

		}

		// combination type
		CombinationType combinationType = peptideRatioValue.getCombinationType();
		if (combinationType != null) {
			deleteCombinationType(combinationType);
		}

		ContextualSessionHandler.delete(peptideRatioValue);
	}

	private void deletePsmRatio(PsmRatioValue psmRatioValue) {

		final ConfidenceScoreType scoreType = psmRatioValue.getConfidenceScoreType();
		if (scoreType != null) {
			deleteConfidenceScoreType(scoreType);

		}

		// combination type
		CombinationType combinationType = psmRatioValue.getCombinationType();
		if (combinationType != null) {
			deleteCombinationType(combinationType);
		}

		ContextualSessionHandler.delete(psmRatioValue);
	}

	private void deleteRatioDescriptor(RatioDescriptor ratioDescriptor) {
		if (ratioDescriptor.getId() != null)
			return;
		final Condition expCondition1 = ratioDescriptor.getConditionByExperimentalCondition1Id();
		final Condition expCondition2 = ratioDescriptor.getConditionByExperimentalCondition2Id();

		deleteExperimentalCondition(expCondition1);

		deleteExperimentalCondition(expCondition2);

		ContextualSessionHandler.delete(ratioDescriptor);

		final Set<ProteinRatioValue> proteinRatioValues = ratioDescriptor.getProteinRatioValues();
		if (proteinRatioValues != null) {
			for (ProteinRatioValue proteinRatioValue : proteinRatioValues) {
				deleteProteinRatio(proteinRatioValue);
			}
		}

		final Set<PeptideRatioValue> peptideRatioValues = ratioDescriptor.getPeptideRatioValues();
		if (peptideRatioValues != null) {
			for (PeptideRatioValue peptideRatioValue : peptideRatioValues) {
				deletePeptideRatio(peptideRatioValue);
			}
		}

		final Set<PsmRatioValue> psmRatioValues = ratioDescriptor.getPsmRatioValues();
		if (psmRatioValues != null) {
			for (PsmRatioValue psmRatioValue : psmRatioValues) {
				deletePsmRatio(psmRatioValue);
			}
		}
	}

	private void deleteAppliedThreshold(ProteinThreshold appliedThreshold) {
		final Threshold threshold = appliedThreshold.getThreshold();

		ContextualSessionHandler.delete(threshold);

		ContextualSessionHandler.delete(appliedThreshold);
	}

	private void deleteProteinAmount(ProteinAmount proteinAmount) {

		ContextualSessionHandler.delete(proteinAmount);
		// amount type
		final AmountType amountType = proteinAmount.getAmountType();
		if (amountType != null)
			deleteAmountType(amountType);

		// combination type
		CombinationType combinationType = proteinAmount.getCombinationType();
		if (combinationType != null) {
			deleteCombinationType(combinationType);
		}

	}

	private void deletePeptideAmount(PeptideAmount peptideAmount) {

		ContextualSessionHandler.delete(peptideAmount);
		// amount type
		final AmountType amountType = peptideAmount.getAmountType();
		if (amountType != null)
			deleteAmountType(amountType);

		// combination type
		CombinationType combinationType = peptideAmount.getCombinationType();
		if (combinationType != null) {
			deleteCombinationType(combinationType);

		}
	}

	private void deletePsmAmount(PsmAmount psmAmount) {

		ContextualSessionHandler.delete(psmAmount);
		// amount type
		final AmountType amountType = psmAmount.getAmountType();
		if (amountType != null)
			deleteAmountType(amountType);

		// combination type
		CombinationType combinationType = psmAmount.getCombinationType();
		if (combinationType != null) {
			deleteCombinationType(combinationType);
		}
	}

	private void deleteProteinAccession(ProteinAccession proteinAccession) {

		if (proteinAccession == null || proteinAccession.getAccession() == null)
			log.warn("CUIDADO");
		final ProteinAccession proteinAccessionInDB = ContextualSessionHandler.load(proteinAccession.getAccession(),
				ProteinAccession.class);
		if (proteinAccessionInDB != null) {
			if (proteinAccession.hashCode() == proteinAccessionInDB.hashCode())
				return;
			proteinAccessionInDB.setAccessionType(proteinAccession.getAccessionType());
			proteinAccessionInDB.setDescription(proteinAccession.getDescription());
			if (proteinAccession.isIsPrimary())
				proteinAccessionInDB.setIsPrimary(proteinAccession.isIsPrimary());
			proteinAccessionInDB.setAlternativeNames(proteinAccession.getAlternativeNames());
			proteinAccessionInDB.getProteins().addAll(proteinAccession.getProteins());
			ContextualSessionHandler.delete(proteinAccessionInDB);
			proteinAccession = proteinAccessionInDB;
		} else {
			// log.debug("saving " + proteinAccession.getAccession());
			ContextualSessionHandler.delete(proteinAccession);

		}
	}

	private void deleteAmountType(AmountType amountType) {

		ContextualSessionHandler.delete(amountType);
	}

	private void deleteCombinationType(CombinationType combinationType) {

		ContextualSessionHandler.delete(combinationType);
	}

	private void deleteConfidenceScoreType(ConfidenceScoreType confidenceScoreType) {
		ContextualSessionHandler.delete(confidenceScoreType);

	}

	private void deleteProteinAnnotation(ProteinAnnotation proteinAnnotation) {

		final AnnotationType annotationType = proteinAnnotation.getAnnotationType();
		deleteAnnotationType(annotationType);

		if (proteinAnnotation.getProtein() == null || proteinAnnotation.getProtein().getId() == null)
			log.debug("ASDF");
		// log.debug("Saving proteinAnnotation");
		ContextualSessionHandler.delete(proteinAnnotation);
		// log.debug("proteinAnnotation deleted with id="
		// + proteinAnnotation.getId());

	}

	private void deleteAnnotationType(AnnotationType annotationType) {
		final AnnotationType annotationTypeDB = ContextualSessionHandler.load(annotationType.getName(),
				AnnotationType.class);
		if (annotationTypeDB != null) {
			annotationTypeDB.getProteinAnnotations().addAll(annotationType.getProteinAnnotations());
			ContextualSessionHandler.delete(annotationTypeDB);
			annotationType = annotationTypeDB;

		} else {
			ContextualSessionHandler.delete(annotationType);

		}

	}

	private void deleteMSRun(MsRun msRun) {
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
		// ManagedSessionHandler.delete(oldMsRun);
		// msRun = oldMsRun;
		// } else {
		// ManagedSessionHandler.delete(msRun);
		// }
		ContextualSessionHandler.delete(msRun);
	}

	public void deleteExperimentalCondition(Condition condition) {

		log.info("Deleting condition: " + condition.getName() + " of project " + condition.getProject().getName());
		final Sample sample = condition.getSample();
		deleteSample(sample);

		final Set<Psm> psms = condition.getPsms();
		for (Psm psm : psms) {
			deletePSM(psm);
		}
		final Set<Peptide> peptides = condition.getPeptides();
		for (Peptide peptide : peptides) {
			deletePeptide(peptide);
		}
		final Set<Protein> proteins = condition.getProteins();
		for (Protein protein : proteins) {
			deleteProtein(protein);
		}

		// delete all the protein ratios
		Set<RatioDescriptor> ratioDescriptorsForExperimentalCondition1Id = condition
				.getRatioDescriptorsForExperimentalCondition1Id();
		for (RatioDescriptor proteinRatioDescriptor : ratioDescriptorsForExperimentalCondition1Id) {
			deleteRatioDescriptor(proteinRatioDescriptor);
		}

		Set<RatioDescriptor> ratioDescriptorsForExperimentalCondition2Id = condition
				.getRatioDescriptorsForExperimentalCondition2Id();
		for (RatioDescriptor proteinRatioDescriptor : ratioDescriptorsForExperimentalCondition2Id) {
			deleteRatioDescriptor(proteinRatioDescriptor);
		}

		ContextualSessionHandler.delete(condition);
	}

	public boolean deleteProject(String projectTag) {

		// look into the database if a project with the same name is already
		// created
		Project hibProject = MySQLProteinDBInterface.getDBProjectByTag(projectTag);
		if (hibProject != null) {

			final Set<Condition> conditions = hibProject.getConditions();
			for (Condition condition : conditions) {
				deleteExperimentalCondition(condition);
			}
			ContextualSessionHandler.delete(hibProject);

			return true;
		} else {
			throw new IllegalArgumentException(projectTag + " doesn't exist");
		}

	}

	private void deleteSample(Sample sample) {

		final Tissue tissue = sample.getTissue();
		if (tissue != null) {
			ContextualSessionHandler.delete(tissue);
		}

		final Label label = sample.getLabel();
		if (label != null)
			ContextualSessionHandler.delete(label);

		final Set<Organism> organisms = sample.getOrganisms();
		for (Organism organism : organisms) {
			ContextualSessionHandler.delete(organism);
		}
		ContextualSessionHandler.delete(sample);
	}

}
