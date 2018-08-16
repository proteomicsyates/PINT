package edu.scripps.yates.proteindb.queries.semantic;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.utilities.pi.ParIterator;
import edu.scripps.yates.utilities.pi.reductions.Reducible;

public class ProteinPeptideLinkParallelProcesor extends Thread {
	private final static Logger log = Logger.getLogger(ProteinPeptideLinkParallelProcesor.class);
	private final ParIterator<LinkBetweenQueriableProteinSetAndPeptideSet> iterator;
	private final Reducible<List<LinkBetweenQueriableProteinSetAndPeptideSet>> reducibleLinkList;
	private final QueryBinaryTree queryBinaryTree;
	private int numDiscardedLinks = 0;
	private long runningTime;
	private int numValidLinks = 0;
	private final List<LinkBetweenQueriableProteinSetAndPeptideSet> discardedLinks = new ArrayList<LinkBetweenQueriableProteinSetAndPeptideSet>();

	public ProteinPeptideLinkParallelProcesor(ParIterator<LinkBetweenQueriableProteinSetAndPeptideSet> iterator,
			Reducible<List<LinkBetweenQueriableProteinSetAndPeptideSet>> reducibleLinkMap2,
			QueryBinaryTree queryBinaryTree) {
		this.iterator = iterator;
		reducibleLinkList = reducibleLinkMap2;
		this.queryBinaryTree = queryBinaryTree;
	}

	@Override
	public void run() {
		try {
			ContextualSessionHandler.openSession();
			ContextualSessionHandler.beginGoodTransaction();
			final long t1 = System.currentTimeMillis();
			log.info("Processing links from thread: " + Thread.currentThread().getId());
			final List<LinkBetweenQueriableProteinSetAndPeptideSet> linkList = new ArrayList<LinkBetweenQueriableProteinSetAndPeptideSet>();
			reducibleLinkList.set(linkList);
			while (iterator.hasNext()) {
				try {
					final LinkBetweenQueriableProteinSetAndPeptideSet link = iterator.next();
					final boolean valid = queryBinaryTree.evaluate(link);

					if (!valid) {
						numDiscardedLinks++;
						link.detachFromProteinAndPeptide();
						iterator.remove();
						discardedLinks.add(link);
					} else {
						numValidLinks++;
						linkList.add(link);
					}

				} catch (final Exception e) {
					e.printStackTrace();
					iterator.register(e);
				}
			}
			runningTime = System.currentTimeMillis() - t1;
			log.info("Processing links from thread: " + Thread.currentThread().getId() + " is finished in "
					+ runningTime + " msg");
			log.info(numValidLinks + " valid. " + numDiscardedLinks + " invalid");
		} catch (final Exception e) {
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

	public List<LinkBetweenQueriableProteinSetAndPeptideSet> getDiscardedLinks() {
		return discardedLinks;
	}

}
