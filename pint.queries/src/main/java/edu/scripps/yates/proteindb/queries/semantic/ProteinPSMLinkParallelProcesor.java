package edu.scripps.yates.proteindb.queries.semantic;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import pi.ParIterator;
import pi.reductions.Reducible;

public class ProteinPSMLinkParallelProcesor extends Thread {
	private final static Logger log = Logger.getLogger(ProteinPSMLinkParallelProcesor.class);
	private final ParIterator<LinkBetweenQueriableProteinSetAndPSM> iterator;
	private final Reducible<List<LinkBetweenQueriableProteinSetAndPSM>> reducibleLinkList;
	private final QueryBinaryTree queryBinaryTree;
	private int numDiscardedLinks = 0;
	private long runningTime;
	private int numValidLinks = 0;
	private final List<LinkBetweenQueriableProteinSetAndPSM> discardedLinks = new ArrayList<LinkBetweenQueriableProteinSetAndPSM>();

	public ProteinPSMLinkParallelProcesor(ParIterator<LinkBetweenQueriableProteinSetAndPSM> iterator,
			Reducible<List<LinkBetweenQueriableProteinSetAndPSM>> reducibleLinkMap2, QueryBinaryTree queryBinaryTree) {
		this.iterator = iterator;
		reducibleLinkList = reducibleLinkMap2;
		this.queryBinaryTree = queryBinaryTree;
	}

	@Override
	public void run() {
		try {
			ContextualSessionHandler.openSession();
			ContextualSessionHandler.beginGoodTransaction();
			long t1 = System.currentTimeMillis();
			log.info("Processing links from thread: " + Thread.currentThread().getId());
			List<LinkBetweenQueriableProteinSetAndPSM> linkList = new ArrayList<LinkBetweenQueriableProteinSetAndPSM>();
			reducibleLinkList.set(linkList);
			while (iterator.hasNext()) {
				try {
					final LinkBetweenQueriableProteinSetAndPSM link = iterator.next();
					final boolean valid = queryBinaryTree.evaluate(link);

					if (!valid) {
						numDiscardedLinks++;
						link.detachFromProteinAndPSM();
						iterator.remove();
						discardedLinks.add(link);
					} else {
						numValidLinks++;
						linkList.add(link);
					}

				} catch (Exception e) {
					e.printStackTrace();
					iterator.register(e);
				}
			}
			runningTime = System.currentTimeMillis() - t1;
			log.info("Processing links from thread: " + Thread.currentThread().getId() + " is finished in "
					+ runningTime + " msg");
			log.info(numValidLinks + " valid. " + numDiscardedLinks + " invalid");
		} catch (Exception e) {
			e.printStackTrace();
			ContextualSessionHandler.rollbackTransaction();
		} finally {
			ContextualSessionHandler.finishGoodTransaction();
			ContextualSessionHandler.closeSession();
		}
		// ContextualSessionHandler.finishGoodTransaction();
	}

	/**
	 * @return the numDiscardedLinks
	 */
	public int getNumDiscardedLinks() {
		return numDiscardedLinks;
	}

	public long getRunningTime() {
		return runningTime;
	}

	public List<LinkBetweenQueriableProteinSetAndPSM> getDiscardedLinks() {
		return discardedLinks;
	}

}
