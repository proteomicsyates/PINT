package edu.scripps.yates.proteindb.persistence.mysql.access;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.PropertyValueException;

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
	private final HashSet<Psm> deletedPsms = new HashSet<Psm>();
	private final HashSet<Peptide> deletedPeptides = new HashSet<Peptide>();
	private final HashSet<Protein> deletedProteins = new HashSet<Protein>();

	private void deleteProtein(edu.scripps.yates.proteindb.persistence.mysql.Protein protein) {
		if (deletedProteins.contains(protein)) {
			return;
		}
		deletedProteins.add(protein);
		if (protein.getId() == null) {
			return;
		}
		final Set<ProteinAccession> proteinAccessions = protein.getProteinAccessions();

		// deleteMSRun(protein.getMsRun());
		// deleteOrganism(protein.getOrganism());

		// log.debug("Saving protein " + next.getAccession());
		// protein accesssions

		for (ProteinAccession proteinAccession : proteinAccessions) {
			deleteProteinAccession(proteinAccession);
		}

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

		// // protein ratios
		final Set<ProteinRatioValue> proteinRatios = protein.getProteinRatioValues();
		for (ProteinRatioValue proteinRatio : proteinRatios) {
			deleteProteinRatio(proteinRatio);
		}

		// amounts
		final Set<ProteinAmount> amounts = protein.getProteinAmounts();
		for (ProteinAmount amount : amounts) {
			deleteProteinAmount(amount);
		}

		// peptides
		final Iterator<Peptide> peptideIterator = protein.getPeptides().iterator();
		while (peptideIterator.hasNext()) {
			Peptide peptide = peptideIterator.next();
			peptide.getProteins().remove(peptide);
			peptideIterator.remove();
			// deletePeptide(peptide);
		}
		// psms
		final Iterator<Psm> psmsIterator = protein.getPsms().iterator();
		while (psmsIterator.hasNext()) {
			Psm psm = psmsIterator.next();
			psm.getProteins().remove(protein);
			psmsIterator.remove();
			// deletePSM(psm);
		}

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

		// proteins
		final Iterator<Protein> proteinIterator = psm.getProteins().iterator();
		while (proteinIterator.hasNext()) {
			Protein protein = proteinIterator.next();
			protein.getPsms().remove(psm);
			proteinIterator.remove();
			// deleteProtein(protein);
		}
		try {
			ContextualSessionHandler.delete(psm);
		} catch (PropertyValueException e) {
			e.printStackTrace();
			log.info(psm.getPeptide());
		}
		// peptide
		if (psm.getPeptide() != null) {
			psm.getPeptide().getPsms().remove(psm);
			// deletePeptide(psm.getPeptide());
		}
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
		// psms
		final Iterator<Psm> psmsIterator = peptide.getPsms().iterator();
		while (psmsIterator.hasNext()) {
			Psm psm = psmsIterator.next();
			psm.getPeptide().getPsms().remove(peptide);
			psmsIterator.remove();
			// deletePSM(psm);
		}
		// proteins
		final Iterator<Protein> proteinIterator = peptide.getProteins().iterator();
		while (proteinIterator.hasNext()) {
			Protein protein = proteinIterator.next();
			protein.getPeptides().remove(peptide);
			proteinIterator.remove();
			// deleteProtein(protein);
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

		for (PtmSite ptmSite : ptmSites) {
			// final ConfidenceScoreType confidenceScoreType =
			// ptmSite.getConfidenceScoreType();
			// if (confidenceScoreType != null) {
			// confidenceScoreType.getPtmSites().remove(ptmSite);
			// deleteConfidenceScoreType(confidenceScoreType);
			// }
			ContextualSessionHandler.delete(ptmSite);
		}

		ContextualSessionHandler.delete(ptm);

	}

	private void deleteGene(Gene gene) {

		ContextualSessionHandler.delete(gene);

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

	private void deleteThreshold(Threshold threshold) {

		ContextualSessionHandler.delete(threshold);

	}

	private void deleteProteinAmount(ProteinAmount proteinAmount) {

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

	private void deleteMSRun(MsRun msRun) {

		ContextualSessionHandler.delete(msRun);

	}

	public void deleteExperimentalCondition(Condition condition) {

		log.info("Deleting condition: " + condition.getName() + " of project " + condition.getProject().getName());

		final Set<Psm> psms = condition.getPsms();
		int percentage = 0;
		int num = 0;
		for (Psm psm : psms) {
			num++;
			int newPercentage = Double.valueOf(num * 100.0 / psms.size()).intValue();
			if (newPercentage != percentage) {
				percentage = newPercentage;
				log.info(num + "/" + psms.size() + "(" + newPercentage + "%) PSMs deleted ");
			}
			deletePSM(psm);
		}
		num = 0;
		final Set<Peptide> peptides = condition.getPeptides();
		percentage = 0;
		for (Peptide peptide : peptides) {
			num++;
			int newPercentage = Double.valueOf(num * 100.0 / peptides.size()).intValue();
			if (newPercentage != percentage) {
				percentage = newPercentage;
				log.info(num + "/" + peptides.size() + "(" + newPercentage + "%) peptides deleted ");
			}
			deletePeptide(peptide);
		}
		num = 0;
		final Set<Protein> proteins = condition.getProteins();
		percentage = 0;
		for (Protein protein : proteins) {
			num++;
			int newPercentage = Double.valueOf(num * 100.0 / proteins.size()).intValue();
			if (newPercentage != percentage) {
				percentage = newPercentage;
				log.info(num + "/" + proteins.size() + "(" + newPercentage + "%) proteins deleted ");
			}
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
		final Sample sample = condition.getSample();
		deleteSample(sample);
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
			final Set<MsRun> msRuns = hibProject.getMsRuns();
			for (MsRun msRun : msRuns) {
				deleteMSRun(msRun);
			}

			ContextualSessionHandler.delete(hibProject);

			return true;
		} else {
			throw new IllegalArgumentException(projectTag + " doesn't exist");
		}

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
		for (Organism organism : organisms) {
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
