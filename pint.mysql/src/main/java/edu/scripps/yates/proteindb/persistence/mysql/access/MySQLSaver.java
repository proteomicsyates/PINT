package edu.scripps.yates.proteindb.persistence.mysql.access;

import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.census.read.model.StaticQuantMaps;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.AnnotationType;
import edu.scripps.yates.proteindb.persistence.mysql.Condition;
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
import edu.scripps.yates.proteindb.persistence.mysql.adapter.ConditionAdapter;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.ProjectAdapter;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.utilities.memory.MemoryUsageReport;
import edu.scripps.yates.utilities.progresscounter.ProgressCounter;
import edu.scripps.yates.utilities.progresscounter.ProgressPrintingType;
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.StaticProteomicsModelStorage;
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
	private final int geneLength = 0;

	private boolean saveProtein(edu.scripps.yates.proteindb.persistence.mysql.Protein protein) {
		// final Set<ProteinAccession> proteinAccessions =
		// protein.getProteinAccessions();
		if (protein.getId() != null)
			return true;
		// not save protein without PSMs
		final Set<MsRun> msRuns = protein.getMsRuns();
		if (protein.getPsms().isEmpty()) {
			log.info("Not saving protein " + PersistenceUtils.getPrimaryAccession(protein).getAccession() + " in "
					+ msRuns.size() + " MSRun(s) for not having PSMs");
			return false;
		}
		for (final MsRun msrun : msRuns) {
			saveMSRun(msrun);
		}

		// // organism
		// final Organism organism = protein.getOrganism();
		// if (organism != null) {
		// saveOrganismForProtein(organism, protein);
		//
		// }

		ContextualSessionHandler.save(protein);
		if (protein.getId() == null) {
			log.info("Protein with no ID after saving it");
		}
		// protein accesssions

		// if (proteinAccessions.isEmpty())
		// log.error("The protein has to have accessions");
		// boolean thereisPrimaryAcc = false;
		// for (final ProteinAccession proteinAccession : proteinAccessions) {
		// if (proteinAccession.isIsPrimary())
		// thereisPrimaryAcc = true;
		// saveProteinAccession(proteinAccession, protein);
		// }
		// if (!thereisPrimaryAcc)
		// log.error("The protein has to have one primary acc");
		// protein annotations
		final Set<ProteinAnnotation> proteinAnnotations = protein.getProteinAnnotations();
		for (final ProteinAnnotation proteinAnnotation : proteinAnnotations) {
			saveProteinAnnotation(proteinAnnotation);
		}
		// genes
		// final Set<Gene> genes = protein.getGenes();
		// for (final Gene gene : genes) {
		// saveGene(gene);
		// }

		// applied threshold
		final Set<ProteinThreshold> appliedThresholds = protein.getProteinThresholds();
		for (final ProteinThreshold appliedThreshold : appliedThresholds) {
			saveAppliedThreshold(appliedThreshold);
		}
		// scores
		final Set<ProteinScore> proteinScores = protein.getProteinScores();
		if (proteinScores != null) {
			for (final ProteinScore proteinScore : proteinScores) {
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
		for (final Peptide peptide : peptides) {
			savePeptide(peptide);
		}

		final Set<Psm> psms = protein.getPsms();
		for (final Psm psm : psms) {
			if (psm.getId() != null)
				continue;

			for (final Peptide peptide : peptides) {
				if (psm.getSequence().equals(peptide.getSequence())) {
					final Set<Psm> psms2 = peptide.getPsms();
					boolean found = false;
					for (final Psm psm2 : psms2) {
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
			for (final ProteinAmount amount : amounts) {
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
			for (final Ptm ptm : ptms) {
				savePTM(ptm);
			}
		}
		final Set<PsmScore> scores = psm.getPsmScores();
		if (scores != null) {
			for (final PsmScore psmScore : scores) {
				savePSMScore(psmScore);
			}
		}
		final Set<PsmAmount> amounts = psm.getPsmAmounts();
		if (amounts != null) {
			for (final PsmAmount psmAmount : amounts) {
				savePsmAmount(psmAmount);
			}
		}
		final Set<Protein> proteins = psm.getProteins();
		if (proteins != null) {

			for (final Protein protein : proteins) {
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
		final Set<MsRun> msRuns = peptide.getMsRuns();
		for (final MsRun msRun : msRuns) {
			saveMSRun(msRun);
		}
		if (peptide.getSequence() == null)
			System.out.println("Sequence is null");

		ContextualSessionHandler.save(peptide);
		if (peptide.getId() == null) {
			log.info("Peptide with no ID after saving it");
		}
		// ptms
		final Set<Ptm> ptms = peptide.getPtms();
		if (ptms != null) {
			for (final Ptm ptm : ptms) {
				savePTM(ptm);
			}
		}
		// scores
		final Set<PeptideScore> scores = peptide.getPeptideScores();
		if (scores != null) {
			for (final PeptideScore psmScore : scores) {
				savePeptideScore(psmScore);
			}
		}
		// amounts
		final Set<PeptideAmount> amounts = peptide.getPeptideAmounts();
		if (amounts != null) {
			for (final PeptideAmount peptideAmount : amounts) {
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
			for (final Psm psm : psms) {
				savePSM(psm);
			}
		}
		// proteins
		final Set<Protein> proteins = peptide.getProteins();
		if (proteins != null) {
			for (final Protein protein : proteins) {
				saveProtein(protein);
			}
		}

	}

	private void savePSMScore(PsmScore psmScore) {

		if (psmScore.getPsm() == null) {
			log.info(psmScore);
		} else if (psmScore.getPsm().getId() == null) {
			log.info(psmScore);
		}
		ContextualSessionHandler.save(psmScore);
	}

	private void savePeptideScore(PeptideScore score) {

		ContextualSessionHandler.save(score);
	}

	private void saveProteinScore(ProteinScore score) {

		ContextualSessionHandler.save(score);
	}

	private void savePTM(Ptm ptm) {
		ContextualSessionHandler.save(ptm);
		final Set<PtmSite> ptmSites = ptm.getPtmSites();
		if (ptmSites != null) {
			for (final PtmSite ptmSite : ptmSites) {
				ContextualSessionHandler.save(ptmSite);
			}
		}

	}

	private void saveProteinRatio(ProteinRatioValue proteinRatioValue) {
		if (proteinRatioValue.getProtein() == null) {
			log.info("asdf");
		}
		final boolean proteinSaved = saveProtein(proteinRatioValue.getProtein());
		if (!proteinSaved) {
			return;
		}
		ContextualSessionHandler.save(proteinRatioValue);

	}

	private void savePeptideRatio(PeptideRatioValue peptideRatioValue) {

		savePeptide(peptideRatioValue.getPeptide());

		ContextualSessionHandler.save(peptideRatioValue);

	}

	private void savePsmRatio(PsmRatioValue psmRatioValue) {

		savePSM(psmRatioValue.getPsm());
		ContextualSessionHandler.save(psmRatioValue);

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
			for (final ProteinRatioValue proteinRatioValue : proteinRatioValues) {
				saveProteinRatio(proteinRatioValue);
			}
		}

		final Set<PeptideRatioValue> peptideRatioValues = ratioDescriptor.getPeptideRatioValues();
		if (peptideRatioValues != null) {
			for (final PeptideRatioValue peptideRatioValue : peptideRatioValues) {
				savePeptideRatio(peptideRatioValue);
			}
		}

		final Set<PsmRatioValue> psmRatioValues = ratioDescriptor.getPsmRatioValues();
		if (psmRatioValues != null) {
			for (final PsmRatioValue psmRatioValue : psmRatioValues) {
				savePsmRatio(psmRatioValue);
			}
		}
	}

	private void saveAppliedThreshold(ProteinThreshold appliedThreshold) {

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
		final Set<Protein> discardedProteins = new THashSet<Protein>();
		final ProgressCounter counter = new ProgressCounter(proteins.size(), ProgressPrintingType.PERCENTAGE_STEPS, 0);
		if (proteins != null && !proteins.isEmpty()) {

			for (final Protein protein : proteins) {
				counter.increment();
				final String printIfNecessary = counter.printIfNecessary();
				if (printIfNecessary != null && !"".equals(printIfNecessary)) {
					log.info(printIfNecessary + " proteins saved in condition " + hibExperimentalCondition.getName());
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
				for (final Protein protein : discardedProteins) {
					hibExperimentalCondition.getProteins().remove(protein);
				}
				if (hibExperimentalCondition.getProteins().isEmpty()) {
					throw new IllegalArgumentException("The condition '" + hibExperimentalCondition.getName()
							+ "' has not proteins with at least one PSM");
				}
			}
		} else {
			log.info("Condition without proteins");
			final Set<Psm> psms = hibExperimentalCondition.getPsms();
			final ProgressCounter counter2 = new ProgressCounter(psms.size(), ProgressPrintingType.PERCENTAGE_STEPS, 0);
			for (final Psm psm : psms) {
				counter2.increment();
				if (psm.getId() != null) {
					continue;
				}
				final String printIfNecessary = counter2.printIfNecessary();
				if (printIfNecessary != null && !"".equals(printIfNecessary)) {
					log.info("Saving psm " + printIfNecessary);
				}
				savePSM(psm);
			}
		}
		// save all PSMs
		final Set<Psm> psms = hibExperimentalCondition.getPsms();
		final ProgressCounter counter2 = new ProgressCounter(psms.size(), ProgressPrintingType.PERCENTAGE_STEPS, 0);
		for (final Psm psm : psms) {
			counter2.increment();
			if (psm.getId() != null) {
				continue;
			}
			final String printIfNecessary = counter2.printIfNecessary();
			if (printIfNecessary != null && !"".equals(printIfNecessary)) {
				log.info("Saving psms " + printIfNecessary + " in condition " + hibExperimentalCondition.getName());
			}
			log.info("this shouldnt happen");
			if (hibExperimentalCondition.getPeptides().contains(psm.getPeptide())) {
				final Integer pepID = psm.getPeptide().getId();
				log.info(pepID);
			}
			final Set<Protein> proteinSet = psm.getProteins();
			for (final Protein protein : proteinSet) {
				if (hibExperimentalCondition.getProteins().contains(protein)) {
					final Integer proteinID = protein.getId();
					log.info(proteinID);
				}

			}
			savePSM(psm);

		}

		// save all the protein ratios
		final Set<RatioDescriptor> ratioDescriptorsForExperimentalCondition1Id = hibExperimentalCondition
				.getRatioDescriptorsForExperimentalCondition1Id();
		for (final RatioDescriptor ratioDescriptor : ratioDescriptorsForExperimentalCondition1Id) {
			saveRatioDescriptor(ratioDescriptor);
		}

		final Set<RatioDescriptor> ratioDescriptorsForExperimentalCondition2Id = hibExperimentalCondition
				.getRatioDescriptorsForExperimentalCondition2Id();
		for (final RatioDescriptor ratioDescriptor : ratioDescriptorsForExperimentalCondition2Id) {
			saveRatioDescriptor(ratioDescriptor);
		}

	}

	public Integer saveProject(edu.scripps.yates.utilities.proteomicsmodel.Project project) {

		// check if there is already a project with that name
		final String projectTag = project.getTag();

		// look into the database if a project with the same name is already
		// created
		Project hibProject = MySQLProteinDBInterface.getDBProjectByTag(projectTag);
		if (hibProject == null) {
			ProjectAdapter.clearStaticInformation();
			hibProject = new ProjectAdapter(project).adapt();
			log.info(MemoryUsageReport.getFreeMemoryDescriptiveString() + " before discarding project");
			project = null;
			StaticQuantMaps.clearInfo();
			StaticProteomicsModelStorage.clearData();
			System.gc();
			log.info(MemoryUsageReport.getFreeMemoryDescriptiveString() + " after discarding project");
			ContextualSessionHandler.save(hibProject);
			// experiments
			final Set<Condition> hibExperimentConditions = hibProject.getConditions();
			for (final Condition condition : hibExperimentConditions) {
				saveExperimentalCondition(condition);
			}
		} else {
			log.warn("Project '" + projectTag
					+ "' already present in the database. The experiments will be attached to that project with identifier: "
					+ hibProject.getId());
			final Set<edu.scripps.yates.utilities.proteomicsmodel.Condition> conditions = project.getConditions();
			for (final edu.scripps.yates.utilities.proteomicsmodel.Condition condition : conditions) {
				// Condition hibCondition =
				// MySQLProteinDBInterface.getDBConditionByName(projectTag,
				// condition.getName());
				// if (hibCondition == null) {
				final Condition hibCondition = new ConditionAdapter(condition, hibProject).adapt();
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
		for (final Organism organism : organisms) {
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

	// private void saveOrganismForProtein(Organism organism, Protein protein) {
	// if (organism == null) {
	// log.info("CUIDADO");
	// throw new IllegalArgumentException("The protein has no organism!");
	// }
	// final Organism oldOrganism =
	// ContextualSessionHandler.load(organism.getTaxonomyId(), Organism.class);
	// if (oldOrganism != null) {
	// oldOrganism.setName(organism.getName());
	// // oldOrganism.getProteins().add(protein);
	// protein.setOrganism(oldOrganism);
	// ContextualSessionHandler.saveOrUpdate(protein);
	// ContextualSessionHandler.saveOrUpdate(oldOrganism);
	// organism = oldOrganism;
	// } else {
	// ContextualSessionHandler.save(organism);
	// }
	// }

}
