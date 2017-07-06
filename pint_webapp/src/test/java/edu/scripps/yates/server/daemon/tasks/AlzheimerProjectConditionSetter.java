package edu.scripps.yates.server.daemon.tasks;

import java.util.Set;

import javax.servlet.ServletContext;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.PsmAmount;
import edu.scripps.yates.proteindb.persistence.mysql.PsmRatioValue;

public class AlzheimerProjectConditionSetter extends PintServerDaemonTask {

	public AlzheimerProjectConditionSetter(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public void run() {
		try {

			log.info("Starting " + getTaskName());
			long numRuns = 0;
			long numPSMs = 0;
			long numPSMsUpdated = 0;
			long numPSMsNotUpdated = 0;

			long numPeptidesUpdated = 0;

			long numProteinsUpdated = 0;

			final Project project = ContextualSessionHandler.getCurrentSession().load(Project.class, 36);
			final Set<MsRun> msRuns = project.getMsRuns();
			for (MsRun msRun : msRuns) {
				ContextualSessionHandler.getCurrentSession().beginTransaction();
				numRuns++;
				final Set<Psm> psms = msRun.getPsms();
				for (Psm psm : psms) {
					numPSMs++;
					if (psm.getConditions().isEmpty()) {
						final Set<PsmRatioValue> psmRatioValues = psm.getPsmRatioValues();
						if (!psmRatioValues.isEmpty()) {
							for (PsmRatioValue psmRatioValue : psmRatioValues) {

								final Condition condition1 = psmRatioValue.getRatioDescriptor()
										.getConditionByExperimentalCondition1Id();
								final Condition condition2 = psmRatioValue.getRatioDescriptor()
										.getConditionByExperimentalCondition2Id();
								if (condition1 != null) {
									psm.getConditions().add(condition1);
								}
								if (condition2 != null) {
									psm.getConditions().add(condition2);
								}
								if (condition1 != null || condition2 != null) {
									ContextualSessionHandler.getCurrentSession().update(psm);
									numPSMsUpdated++;
								} else {
									numPSMsNotUpdated++;
								}

							}
						} else {
							final Set<PsmAmount> psmAmounts = psm.getPsmAmounts();
							if (!psmAmounts.isEmpty()) {
								for (PsmAmount psmAmount : psmAmounts) {
									if (psmAmount.getCondition() != null) {
										numPSMsUpdated++;
										psm.getConditions().add(psmAmount.getCondition());
										ContextualSessionHandler.getCurrentSession().update(psm);
									} else {
										numPSMsNotUpdated++;
									}
								}
							} else {
								numPSMsNotUpdated++;
							}
						}
					}
					log.info("MSRuns=" + numRuns + "\tPSMs=" + numPSMs + "\tUpdatedPSMs=" + numPSMsUpdated
							+ "\tNonUpdatedPSMs=" + numPSMsNotUpdated);
					Set<Condition> conditionsFromPSM = psm.getConditions();

					// peptides
					final Peptide peptide = psm.getPeptide();
					boolean updated = false;
					for (Condition condition : conditionsFromPSM) {
						boolean added = peptide.getConditions().add(condition);
						if (added) {
							numPeptidesUpdated++;
							updated = true;
						}
					}
					if (updated) {
						ContextualSessionHandler.getCurrentSession().update(peptide);
						log.info("MSRuns=" + numRuns + "\tUpdatedPeptides=" + numPeptidesUpdated);
					}

					// proteins
					final Set<Protein> protein = psm.getProteins();

					for (Protein protein2 : protein) {
						boolean updated2 = false;
						for (Condition condition : conditionsFromPSM) {
							boolean added = protein2.getConditions().add(condition);
							if (added) {
								numProteinsUpdated++;
								updated2 = true;
							}
						}
						if (updated2) {
							ContextualSessionHandler.getCurrentSession().update(protein);
							log.info("MSRuns=" + numRuns + "\tUpdatedProteins=" + numProteinsUpdated);
						}
					}

				}
				log.info("Finishing transaction");
				ContextualSessionHandler.finishGoodTransaction();
				log.info("transaction finished");
			}

		} catch (Exception e) {
			e.printStackTrace();
			ContextualSessionHandler.getCurrentSession().getTransaction().rollback();
		} finally {
			ContextualSessionHandler.getCurrentSession().close();
		}
	}

	@Override
	public boolean justRunOnce() {
		return true;
	}

}
