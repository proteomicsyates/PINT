package edu.scripps.yates.server.daemon.tasks;

import java.util.List;

import javax.servlet.ServletContext;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;

public class PrimaryAccSetter extends PintServerDaemonTask {

	public PrimaryAccSetter(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public void run() {
		setPrimaryAccs();

	}

	@Override
	public boolean justRunOnce() {
		// TODO Auto-generated method stub
		return true;
	}

	private void setPrimaryAccs() {
		try {
			ContextualSessionHandler.getSession().beginTransaction();

			List<Protein> proteins = ContextualSessionHandler.createCriteria(Protein.class).list();
			int i = 0;
			for (Protein protein : proteins) {
				if (i % 100 == 0) {
					System.out.println(i + "/" + proteins.size());
				}
				i++;
				if (protein.getAcc() == null || "".equals(protein.getAcc())) {
					final ProteinAccession primaryAccession = PersistenceUtils.getPrimaryAccession(protein);
					protein.setAcc(primaryAccession.getAccession());
					ContextualSessionHandler.update(protein);
				}
			}
			ContextualSessionHandler.finishGoodTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			ContextualSessionHandler.rollbackTransaction();
		}
	}

}
