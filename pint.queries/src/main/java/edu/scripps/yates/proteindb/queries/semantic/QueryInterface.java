package edu.scripps.yates.proteindb.queries.semantic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.sort.SystemCoreManager;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.annotations.uniprot.UniprotProteinRetriever;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAnnotation;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.ProteinAnnotationAdapter;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.QueryResult;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;
import edu.scripps.yates.proteindb.queries.dataproviders.protein.ProteinProviderFromProjects;
import edu.scripps.yates.proteindb.queries.dataproviders.protein.ProteinProviderFromProteinAccs;
import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromAmountCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromComplexAnnotationCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromPTMCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromProteinAccessionsCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromSEQCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromSimpleAnnotationCommand;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;
import edu.scripps.yates.utilities.model.enums.AmountType;
import pi.ParIterator;
import pi.ParIterator.Schedule;
import pi.ParIteratorFactory;
import pi.reductions.Reducible;
import pi.reductions.Reduction;

public class QueryInterface {
	private ProteinProviderFromDB proteinProvider;
	private QueryResult queryResult;
	private final QueryBinaryTree queryBinaryTree;
	private static final Logger log = Logger.getLogger(QueryInterface.class);
	private static final int MAX_NUMBER_PARALLEL_PROCESSES = 16;
	private boolean needLinkEvaluation = true;
	private final Set<String> projectTags;

	public QueryInterface(Set<String> projectTags, String queryString) throws MalformedQueryException {
		this(projectTags, new ProteinProviderFromProjects(projectTags), queryString);
	}

	public QueryInterface(Set<String> projectTags, ProteinProviderFromDB proteinProvider, String queryString)
			throws MalformedQueryException {
		this.projectTags = projectTags;
		this.proteinProvider = proteinProvider;
		this.proteinProvider.setProjectTags(projectTags);
		queryBinaryTree = new Infix2QueryBinaryTree().convertExpresion(queryString);
		final List<AbstractQuery> abstractQueries = queryBinaryTree.getAbstractQueries();
		// TODO
		// if there is only one abstract query, use the proteinprovider of the
		// abstract query
		if (abstractQueries.size() == 1) {
			log.info(
					"There is  only one command in the query. Trying to figure it out if we can narrow the initial dataset loading");
			final ProteinProviderFromDB proteinProvider2 = abstractQueries.get(0).getProteinProvider();
			if (proteinProvider2 != null) {
				if (abstractQueries.get(0).isNegative()) {
					log.info("The query is negative, so we cannot use the protein provider of that query");
					needLinkEvaluation = true;
					this.proteinProvider = proteinProvider;
				} else {
					// result from query is going to come directly from the
					// protein provider specific of the query

					needLinkEvaluation = abstractQueries.get(0).requiresFurtherEvaluation();
					String negation = needLinkEvaluation ? "" : " does not ";
					log.info("Changing protein provider to a narrow one of class: "
							+ proteinProvider2.getClass().getName() + " which " + negation
							+ "requires evaluation of links");
					this.proteinProvider = proteinProvider2;
				}
			} else {
				if (abstractQueries.get(0) instanceof QueryFromSimpleAnnotationCommand
						|| abstractQueries.get(0) instanceof QueryFromComplexAnnotationCommand) {
					proteinProvider = getDominantProteinProvider(queryBinaryTree);
				} else {
					log.info("Using protein provider to load all proteins in the project");
				}
				this.proteinProvider = proteinProvider;
			}
		} else {
			log.info(
					"There is more than one command in the query. Trying to figure it out if we can narrow the initial dataset loading");
			ProteinProviderFromDB dominantProteinProvider = getDominantProteinProvider(queryBinaryTree);
			if (dominantProteinProvider != null) {
				log.info("Changing protein provider to a narrow one");
				this.proteinProvider = dominantProteinProvider;
			} else {
				log.info("Using protein provider to load all proteins in the project");
				this.proteinProvider = proteinProvider;
			}
		}
		this.proteinProvider.setProjectTags(projectTags);

		// if in the binary tree, there is a annotation query, lets annotate
		// first all the proteins at once.

		for (AbstractQuery abstractQuery : abstractQueries) {
			if (abstractQuery instanceof QueryFromComplexAnnotationCommand) {
				ProteinAnnotator.getInstance(((QueryFromComplexAnnotationCommand) abstractQuery).getUniprotVersion())
						.annotateProteins(this.proteinProvider.getProteinMap());

				annotateProteins(this.proteinProvider.getProteinMap(),
						((QueryFromComplexAnnotationCommand) abstractQuery).getUniprotVersion());
				break;
			}
			if (abstractQuery instanceof QueryFromSimpleAnnotationCommand) {
				annotateProteins(this.proteinProvider.getProteinMap(),
						((QueryFromSimpleAnnotationCommand) abstractQuery).getUniprotVersion());
				break;
			}
			if (abstractQuery instanceof QueryFromSEQCommand) {
				// annotate the proteins in this case because the protein
				// sequence is going to be needed in the query
				if (abstractQuery.getAggregationLevel() == AggregationLevel.PROTEIN) {
					annotateProteins(this.proteinProvider.getProteinMap(), null);
					break;
				}
			}
		}

		// if all of them are annotation related queries, it is not needed a new
		// evaluation
		boolean allAreAnnotationQueries = true;
		for (AbstractQuery abstractQuery : abstractQueries) {
			if (abstractQuery instanceof QueryFromComplexAnnotationCommand
					|| abstractQuery instanceof QueryFromSimpleAnnotationCommand) {

			} else {
				allAreAnnotationQueries = false;
			}
		}
		if (allAreAnnotationQueries) {
			needLinkEvaluation = false;
		}
	}

	private boolean containsSPCAmountQuery(QueryBinaryTree queryBinaryTree) {
		final Set<QueryFromAmountCommand> amountQueries = (Set<QueryFromAmountCommand>) queryBinaryTree
				.getAbstractQueries(QueryFromAmountCommand.class);
		for (QueryFromAmountCommand queryFromAmountCommand : amountQueries) {
			if (queryFromAmountCommand.getAggregationLevel() == AggregationLevel.PROTEIN) {
				if (queryFromAmountCommand.getAmountType() == AmountType.SPC) {
					return true;
				}
			}
		}
		return false;
	}

	ProteinProviderFromDB getDominantProteinProvider(QueryBinaryTree queryBinaryTree) {
		// protein ACCESSION QUERY
		if (queryBinaryTree.isPredominant(QueryFromProteinAccessionsCommand.class)) {
			log.info("There is at least one predominant query that is over protein accessions");
			// get all the queries from protein accessions
			Set<? extends AbstractQuery> accQueries = queryBinaryTree
					.getPredominantAbstractQueries(QueryFromProteinAccessionsCommand.class);
			if (accQueries.size() == 1) {
				log.info("Getting the protein provider of the accession query");
				return accQueries.iterator().next().getProteinProvider();
			} else {
				log.info(
						"There are more than one query about ACCs. Joining accessions of all of them and building the protein provider");
				Set<String> accs = new HashSet<String>();
				for (AbstractQuery abstractQuery : accQueries) {
					accs.addAll(((QueryFromProteinAccessionsCommand) abstractQuery).getAccs());
				}
				return new ProteinProviderFromProteinAccs(accs);
			}
		}
		// PTM QUERY
		if (queryBinaryTree.isPredominant(QueryFromPTMCommand.class)) {
			log.info("There is at least one predominant query that is over PTM ");
			// get all the queries from PTM
			Set<? extends AbstractQuery> ptmQueries = queryBinaryTree
					.getPredominantAbstractQueries(QueryFromPTMCommand.class);
			if (ptmQueries.size() == 1) {
				log.info("Getting the protein provider of the PTM query");
				return ptmQueries.iterator().next().getProteinProvider();
			} else {
				log.info("There are more than one predominant query about PTMs. It is not possible to join them");
				return null;
			}
		}
		// ANNOTATIONS
		if (queryBinaryTree.isAllQueries(QueryFromComplexAnnotationCommand.class)
				|| queryBinaryTree.isAllQueries(QueryFromSimpleAnnotationCommand.class)
				|| queryBinaryTree.isPredominant(QueryFromSimpleAnnotationCommand.class)
				|| queryBinaryTree.isPredominant(QueryFromComplexAnnotationCommand.class)) {
			log.info("There is at least one predominant query that is over Uniprot annotations ");

			Set<AbstractQuery> abstractQueries = new HashSet<AbstractQuery>();
			final Set<QueryFromComplexAnnotationCommand> predominantAbstractQueries = (Set<QueryFromComplexAnnotationCommand>) queryBinaryTree
					.getPredominantAbstractQueries(QueryFromComplexAnnotationCommand.class, false);
			abstractQueries.addAll(predominantAbstractQueries);
			final Set<QueryFromSimpleAnnotationCommand> predominantAbstractQueries2 = (Set<QueryFromSimpleAnnotationCommand>) queryBinaryTree
					.getPredominantAbstractQueries(QueryFromSimpleAnnotationCommand.class, false);
			abstractQueries.addAll(predominantAbstractQueries2);

			return getDominantProteinProviderFromAnnotationQueries(abstractQueries);
		}
		return null;
	}

	private ProteinProviderFromDB getDominantProteinProviderFromAnnotationQueries(
			Set<? extends AbstractQuery> annotationQueries) {
		String uniprotKBVersion = null;

		// get all the proteins
		final Map<String, Set<Protein>> proteinMap = proteinProvider.getProteinMap();
		// annotate them
		ProteinAnnotator.getInstance(uniprotKBVersion).annotateProteins(proteinMap);
		// filter them and keep the valids
		Set<String> validProteinAccs = new HashSet<String>();
		log.info("Performing a pre evaluation of the proteins checking their annotations...");
		log.info("Checking " + proteinMap.size() + " proteins");
		for (String proteinAcc : proteinMap.keySet()) {
			Protein protein = proteinMap.get(proteinAcc).iterator().next();
			boolean valid = true;
			for (AbstractQuery abstractQuery : annotationQueries) {
				QueryFromSimpleAnnotationCommand query = (QueryFromSimpleAnnotationCommand) abstractQuery;
				final boolean evaluate = query.evaluate(protein.getProteinAnnotations());
				if (!abstractQuery.isNegative()) {
					if (!evaluate) {
						valid = false;
						break;
					}
				} else {
					if (evaluate) {
						valid = false;
						break;
					}
				}
			}

			if (valid) {
				validProteinAccs.add(proteinAcc);
			}
		}
		log.info(validProteinAccs.size() + " out of " + proteinMap.size() + " where valid");
		// construct the protein provider with the remaining valid proteins
		ProteinProviderFromDB proteinProviderFromAcc = new ProteinProviderFromProteinAccs(validProteinAccs);
		return proteinProviderFromAcc;
	}

	private void annotateProteins(Map<String, Set<Protein>> proteinList, String uniprotVersion) {
		UniprotProteinRetriever uplr = new UniprotProteinRetriever(uniprotVersion,
				UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
				UniprotProteinRetrievalSettings.getInstance().isUseIndex());
		log.info("Getting Uniprot annotations from " + proteinList.size() + " proteins");
		Collection<String> accessions = PersistenceUtils.getAccessionsByAccType(proteinList, AccessionType.UNIPROT);
		final Map<String, edu.scripps.yates.utilities.proteomicsmodel.Protein> annotatedProteins = uplr
				.getAnnotatedProteins(accessions);
		for (String accession : proteinList.keySet()) {
			final edu.scripps.yates.utilities.proteomicsmodel.Protein annotatedProtein = annotatedProteins
					.get(accession);
			if (annotatedProtein != null) {
				final Set<edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation> proteinAnnotations = annotatedProtein
						.getAnnotations();
				for (edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation proteinAnnotation : proteinAnnotations) {
					final Set<Protein> proteinSet = proteinList.get(accession);
					for (Protein protein : proteinSet) {
						final ProteinAnnotation proteinAnnotationNew = new ProteinAnnotationAdapter(proteinAnnotation,
								null).adapt();
						protein.getProteinAnnotations().add(proteinAnnotationNew);
					}

				}
			}
		}
		log.info("Annotations retrieved for " + proteinList.size() + " proteins");

	}

	public QueryResult getQueryResults() {
		if (queryResult == null) {

			List<LinkBetweenQueriableProteinSetAndPSM> links = QueriesUtil
					.createProteinPSMLinks(proteinProvider.getProteinMap());
			if (needLinkEvaluation) {

				int numDiscardedLinks = 0;
				int numValidLinks = 0;
				int numRound = 1;
				do {
					log.info("Evaluating " + links.size() + " links in round " + numRound);
					numDiscardedLinks = 0;
					numValidLinks = 0;
					int numLinks = 0;
					int totalLinks = links.size();
					final Iterator<LinkBetweenQueriableProteinSetAndPSM> linksIterator = links.iterator();
					while (linksIterator.hasNext()) {
						LinkBetweenQueriableProteinSetAndPSM link = linksIterator.next();
						numLinks++;
						if (numLinks % 100 == 0) {
							log.info(numRound + " round - " + numLinks + "/" + totalLinks + " links (" + numValidLinks
									+ " valid, " + numDiscardedLinks + " discarded)");
						}

						boolean valid = queryBinaryTree.evaluate(link);

						// evaluate the links between individual proteins and
						// psms
						// final Set<QueriableProtein2PSMLink> protein2psmLinks
						// = link.getProtein2PSMLinks();
						// for (QueriableProtein2PSMLink
						// queriableProtein2PSMLink : protein2psmLinks) {
						// boolean result =
						// queryBinaryTree.evaluate(queriableProtein2PSMLink);
						// link.setProtein2PsmResult(queriableProtein2PSMLink,
						// result);
						// }
						if (valid) {
							numValidLinks++;
						} else {
							numDiscardedLinks++;
							// delete link between QueriableProtein and
							// QueriablePSM
							link.detachFromProteinAndPSM();
							linksIterator.remove();
						}
					}
					log.info(links.size() + " Protein-PSM links remain after " + numRound + " round. " + numValidLinks
							+ " valid links. " + numDiscardedLinks + " were discarded.");
					numRound++;
					if (!containsSPCAmountQuery(queryBinaryTree)) {
						log.info("Query not containing SPC amount query, so it is not necessary to keep in the loop");
						break;
					}
					if (allQueriesAreTheSameAggregationLevel(queryBinaryTree)) {
						log.info(
								"All queries are in the same aggregation level, so it is not necessary to keep in the loop");
						break;
					}
				} while (numDiscardedLinks > 0);
			} else {
				// create the links between individual proteins and psms
				// for (LinkBetweenQueriableProteinSetAndPSM link : links) {
				// final Set<LinkBetweenProteinAndPSMImpl> protein2psmLinks =
				// link.getProtein2PSMLinks();
				// for (LinkBetweenProteinAndPSMImpl queriableProtein2PSMLink :
				// protein2psmLinks) {
				// // link.setProtein2PsmResult(queriableProtein2PSMLink,
				// // true);
				// }
				// }
			}
			queryResult = new QueryResult(links);
			// }else{
			// queryBinaryTree.getAbstractQueries().get(0).
			// }

		}
		return queryResult;
	}

	private boolean allQueriesAreTheSameAggregationLevel(QueryBinaryTree queryBinaryTree2) {
		AggregationLevel level = null;
		final List<AbstractQuery> abstractQueries = queryBinaryTree2.getAbstractQueries();
		for (AbstractQuery abstractQuery : abstractQueries) {
			final AggregationLevel aggregationLevel = abstractQuery.getAggregationLevel();
			if (level == null) {
				level = aggregationLevel;
			} else if (level != aggregationLevel) {
				return false;
			}
		}
		return true;
	}

	/**
	 * I get ERROR 37820[Thread-9] - org.hibernate.LazyInitializationException.
	 * <init >(LazyInitializationException.java:42) - illegal access to loading
	 * collection
	 *
	 * @return
	 */
	public QueryResult getQueryResultsParallel() {
		if (queryResult == null) {
			List<LinkBetweenQueriableProteinSetAndPSM> links = QueriesUtil
					.createProteinPSMLinks(proteinProvider.getProteinMap());

			int numDiscardedLinks = 0;
			int numRound = 1;
			do {
				int threadCount = SystemCoreManager.getAvailableNumSystemCores(MAX_NUMBER_PARALLEL_PROCESSES);
				log.info("Evaluating " + links.size() + " links in round " + numRound + " using " + threadCount
						+ " cores out of " + Runtime.getRuntime().availableProcessors());
				ParIterator<LinkBetweenQueriableProteinSetAndPSM> iterator = ParIteratorFactory.createParIterator(links,
						threadCount, Schedule.GUIDED);

				Reducible<List<LinkBetweenQueriableProteinSetAndPSM>> reducibleLinkMap = new Reducible<List<LinkBetweenQueriableProteinSetAndPSM>>();
				List<ProteinPSMLinkParallelProcesor> runners = new ArrayList<ProteinPSMLinkParallelProcesor>();
				for (int numCore = 0; numCore < threadCount; numCore++) {
					// take current DB session
					ProteinPSMLinkParallelProcesor runner = new ProteinPSMLinkParallelProcesor(iterator,
							reducibleLinkMap, queryBinaryTree);
					runners.add(runner);
					runner.start();
				}
				if (iterator.getAllExceptions().length > 0) {
					throw new IllegalArgumentException(iterator.getAllExceptions()[0].getException());
				}
				// Main thread waits for worker threads to complete
				for (int k = 0; k < threadCount; k++) {
					try {
						runners.get(k).join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				Reduction<List<LinkBetweenQueriableProteinSetAndPSM>> linkReduction = new Reduction<List<LinkBetweenQueriableProteinSetAndPSM>>() {
					@Override
					public List<LinkBetweenQueriableProteinSetAndPSM> reduce(
							List<LinkBetweenQueriableProteinSetAndPSM> first,
							List<LinkBetweenQueriableProteinSetAndPSM> second) {
						first.addAll(second);
						return first;
					}

				};
				links = reducibleLinkMap.reduce(linkReduction);
				numDiscardedLinks = 0;
				long time = 0;
				for (int k = 0; k < threadCount; k++) {
					numDiscardedLinks = +runners.get(k).getNumDiscardedLinks();
					time = +runners.get(k).getRunningTime();
				}
				log.info(links.size() + " Protein-PSM links remain after " + numRound + " round. " + numDiscardedLinks
						+ " were discarded in " + time / 1000 + "sg");
				numRound++;
			} while (numDiscardedLinks > 0);

			queryResult = new QueryResult(links);

		}
		return queryResult;
	}

	/**
	 * I get ERROR 37820[Thread-9] - org.hibernate.LazyInitializationException.
	 * <init >(LazyInitializationException.java:42) - illegal access to loading
	 * collection
	 *
	 * @return
	 */
	// public QueryResult getQueryResultsWithJava8() {
	// if (queryResult == null) {
	//
	// List<ProteinPSMLink> links =
	// QueriesUtil.createProteinPSMLinks(proteinProvider.getProteinMap());
	// if (needLinkEvaluation) {
	//
	// Predicate<ProteinPSMLink> predicate = new Predicate<ProteinPSMLink>() {
	// @Override
	// public boolean test(ProteinPSMLink link) {
	// final boolean valid = queryBinaryTree.evaluate(link);
	// if (!valid) {
	// link.detachFromProteinAndPSM();
	// }
	// return valid;
	// }
	// };
	// int numValid;
	// int numRound = 1;
	// do {
	// numValid = links.size();
	// final Stream<ProteinPSMLink> stream = links.stream();
	// final Stream<ProteinPSMLink> filtered = stream.filter(predicate);
	// links = filtered.collect(Collectors.toCollection(ArrayList::new));
	// System.out.println(links.size() + " valid links");
	//
	// log.info(links.size() + " Protein-PSM links remain after " + numRound + "
	// round. ");
	// numRound++;
	// } while (numValid != links.size());
	// }
	// queryResult = new QueryResult(links);
	// // }else{
	// // queryBinaryTree.getAbstractQueries().get(0).
	// // }
	//
	// }
	// return queryResult;
	// }

	public String printInOrder() {
		return queryBinaryTree.printInOrder();
	}
}
