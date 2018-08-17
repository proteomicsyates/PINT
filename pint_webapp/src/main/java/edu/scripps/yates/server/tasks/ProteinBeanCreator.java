package edu.scripps.yates.server.tasks;

import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.queries.semantic.QueriableProteinSet;
import edu.scripps.yates.server.adapters.ProteinBeanAdapterFromProteinSet;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.utilities.pi.ParIterator;
import edu.scripps.yates.utilities.pi.reductions.Reducible;
import gnu.trove.set.hash.THashSet;

public class ProteinBeanCreator extends Thread {
	private final ParIterator<QueriableProteinSet> iterator;
	private final Reducible<Set<ProteinBean>> reducibleMap;
	private static final Logger log = Logger.getLogger(ProteinBeanCreator.class);
	private final Set<String> hiddenPTMs;
	private static int threadCounter = 0;
	private final int numThread;
	private final boolean psmCentric;

	public ProteinBeanCreator(ParIterator<QueriableProteinSet> iterator, Reducible<Set<ProteinBean>> reducibleMap,
			Set<String> hiddenPTMs, boolean psmCentric) {
		this.iterator = iterator;
		this.reducibleMap = reducibleMap;
		this.hiddenPTMs = hiddenPTMs;
		this.psmCentric = psmCentric;
		numThread = ++threadCounter;
	}

	@Override
	public void run() {
		// needed for get a session in the thread
		final Session session = ContextualSessionHandler.openSession();
		final Set<ProteinBean> ret = new THashSet<ProteinBean>();
		reducibleMap.set(ret);
		int numProteins = 0;
		while (iterator.hasNext()) {
			numProteins++;
			try {
				final QueriableProteinSet proteinSet = iterator.next();
				final ProteinBean proteinBeanAdapted = new ProteinBeanAdapterFromProteinSet(proteinSet, hiddenPTMs,
						psmCentric).adapt();
				ret.add(proteinBeanAdapted);
				if (numProteins % 10 == 0) {
					log.info(numProteins + " proteins in thread " + numThread);
				}
			} catch (final Exception e) {
				e.printStackTrace();
				iterator.register(e);
			}
		}
		log.info(numProteins + " proteins processed in total in thread " + numThread);
	}
}
