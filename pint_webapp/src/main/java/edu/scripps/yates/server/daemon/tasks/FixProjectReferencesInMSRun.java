package edu.scripps.yates.server.daemon.tasks;

import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;

public class FixProjectReferencesInMSRun extends PintServerDaemonTask {
	private final static Logger log = Logger.getLogger(FixProjectReferencesInMSRun.class);

	public FixProjectReferencesInMSRun(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public void run() {
		try {
			ContextualSessionHandler.beginGoodTransaction();
			final List<MsRun> msRuns = ContextualSessionHandler.retrieveList(MsRun.class);
			for (MsRun msRun : msRuns) {

				final Set<Protein> proteins = msRun.getProteins();
				Protein protein = proteins.iterator().next();
				final Set<Condition> conditions = protein.getConditions();
				final Condition condition = conditions.iterator().next();
				final Project project = condition.getProject();
				log.info("Fixing msRun " + msRun.getRunId() + " in project " + project.getTag());
				msRun.setProject(project);
				project.getMsRuns().add(msRun);
				ContextualSessionHandler.saveOrUpdate(msRun);
				ContextualSessionHandler.saveOrUpdate(project);
			}
		} finally {
			ContextualSessionHandler.finishGoodTransaction();
			log.info("Fixing done");
		}
	}

	@Override
	public boolean justRunOnce() {
		return true;
	}
}
