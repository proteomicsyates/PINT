package edu.scripps.yates.server.tasks;

import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import edu.scripps.yates.pi.ParIterator;
import edu.scripps.yates.pi.reductions.Reducible;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.queries.semantic.QueriableProteinSet;
import edu.scripps.yates.server.adapters.ProteinBeanAdapterFromProteinSet;
import edu.scripps.yates.shared.model.ProteinBean;
import gnu.trove.set.hash.THashSet;

public class ProteinBeanCreator extends Thread {
	private final ParIterator<Set<QueriableProteinSet>> iterator;
	private final Reducible<Set<ProteinBean>> reducibleMap;
	private static final Logger log = Logger.getLogger(ProteinBeanCreator.class);
	private final Set<String> hiddenPTMs;
	private static int threadCounter = 0;
	private final int numThread;

	public ProteinBeanCreator(ParIterator<Set<QueriableProteinSet>> iterator, Reducible<Set<ProteinBean>> reducibleMap,
			Set<String> hiddenPTMs) {
		this.iterator = iterator;
		this.reducibleMap = reducibleMap;
		this.hiddenPTMs = hiddenPTMs;
		numThread = ++threadCounter;
	}

	@Override
	public void run() {
		// needed for get a session in the thread
		Session session = ContextualSessionHandler.openSession();
		Set<ProteinBean> ret = new THashSet<ProteinBean>();
		reducibleMap.set(ret);
		int numProteins = 0;
		while (iterator.hasNext()) {
			numProteins++;
			try {
				final Set<QueriableProteinSet> proteinSet = iterator.next();
				final ProteinBean proteinBeanAdapted = new ProteinBeanAdapterFromProteinSet(proteinSet, hiddenPTMs)
						.adapt();
				ret.add(proteinBeanAdapted);
				if (numProteins % 10 == 0) {
					log.info(numProteins + " proteins in thread " + numThread);
				}
			} catch (Exception e) {
				e.printStackTrace();
				iterator.register(e);
			}
		}
		log.info(numProteins + " proteins processed in total in thread " + numThread);
	}
}
