package edu.scripps.yates.server.daemon.tasks;

import java.util.Set;

import javax.servlet.ServletContext;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.PsmRatioValue;

public class AlzheimerProjectConditionSetter extends PintServerDaemonTask {

	public AlzheimerProjectConditionSetter(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public void run() {
		try {
			ContextualSessionHandler.getSession().beginTransaction();
			log.info("Starting " + getTaskName());
			int numRuns = 0;
			int numPSMs = 0;
			int numPSMsUpdated = 0;
			int numPSMsNotUpdated = 0;
			final Project project = ContextualSessionHandler.getSession().load(Project.class, 36);
			final Set<MsRun> msRuns = project.getMsRuns();
			for (MsRun msRun : msRuns) {
				numRuns++;
				final Set<Psm> psms = msRun.getPsms();
				for (Psm psm : psms) {
					numPSMs++;
					if (psm.getConditions().isEmpty()) {
						final Set<PsmRatioValue> psmRatioValues = psm.getPsmRatioValues();
						if (!psmRatioValues.isEmpty()) {
							for (PsmRatioValue psmRatioValue : psmRatioValues) {
								numPSMsUpdated++;
								final Condition condition1 = psmRatioValue.getRatioDescriptor()
										.getConditionByExperimentalCondition1Id();
								final Condition condition2 = psmRatioValue.getRatioDescriptor()
										.getConditionByExperimentalCondition2Id();
								psm.getConditions().add(condition1);
								psm.getConditions().add(condition2);
							}
						} else {
							numPSMsNotUpdated++;
						}
					}
					log.info("MSRuns=" + numRuns + "\tPSMs=" + numPSMs + "\tUpdatedPSMs=" + numPSMsUpdated
							+ "\tNonUpdatedPSMs=" + numPSMsNotUpdated);
				}
			}
			log.info("Finishing transaction");
			ContextualSessionHandler.finishGoodTransaction();
			log.info("transaction finished");
		} catch (Exception e) {
			e.printStackTrace();
			ContextualSessionHandler.getSession().getTransaction().rollback();
		} finally {
			ContextualSessionHandler.getSession().close();
		}
	}

	@Override
	public boolean justRunOnce() {
		return true;
	}

}
