package edu.scripps.yates.proteindb.queries.semantic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.ConditionToPeptideTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.ConditionToProteinTableMapper;
import edu.scripps.yates.proteindb.queries.LogicalOperator;
import edu.scripps.yates.proteindb.queries.dataproviders.DataProviderFromDB;
import edu.scripps.yates.proteindb.queries.dataproviders.protein.ProteinProviderFromProjects;
import edu.scripps.yates.proteindb.queries.dataproviders.protein.ProteinProviderFromProteinAccs;
import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromAmountCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromComplexAnnotationCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromGeneNameCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromPTMCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromProteinAccessionsCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromSEQCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromSimpleAnnotationCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromTaxonomyCommand;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import edu.scripps.yates.utilities.cores.SystemCoreManager;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;
import edu.scripps.yates.utilities.model.enums.AmountType;
import edu.scripps.yates.utilities.pi.ParIterator;
import edu.scripps.yates.utilities.pi.ParIterator.Schedule;
import edu.scripps.yates.utilities.pi.ParIteratorFactory;
import edu.scripps.yates.utilities.pi.reductions.Reducible;
import edu.scripps.yates.utilities.pi.reductions.Reduction;
import gnu.trove.set.hash.THashSet;

public class QueryInterface {
	private DataProviderFromDB proteinProvider;
	private QueryResult queryResult;
	private final QueryBinaryTree queryBinaryTree;
	private static final Logger log = Logger.getLogger(QueryInterface.class);
	private static final int MAX_NUMBER_PARALLEL_PROCESSES = 16;
	private boolean needLinkEvaluation = true;
	private final Set<String> projectTags;
	private final boolean testMode;
	private final boolean psmCentric;

	public QueryInterface(Set<String> projectTags, String queryString, boolean testMode, boolean psmCentric)
			throws MalformedQueryException {
		this(projectTags, new ProteinProviderFromProjects(projectTags), queryString, testMode, psmCentric);
	}

	public QueryInterface(Set<String> projectTags, DataProviderFromDB proteinProvider, String queryString,
			boolean testMode, boolean psmCentric) throws MalformedQueryException {
		this.testMode = testMode;
		this.psmCentric = psmCentric;
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
			final DataProviderFromDB proteinProvider2 = abstractQueries.get(0).getProteinProvider();
			if (proteinProvider2 != null) {
				if (abstractQueries.get(0).isNegative()) {
					log.info("The query is negative, so we cannot use the protein provider of that query");
					needLinkEvaluation = true;
					this.proteinProvider = proteinProvider;
				} else {
					// result from query is going to come directly from the
					// protein provider specific of the query

					needLinkEvaluation = abstractQueries.get(0).requiresFurtherEvaluation();
					final String negation = needLinkEvaluation ? "" : " does not ";
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
			final DataProviderFromDB dominantProteinProvider = getDominantProteinProvider(queryBinaryTree);
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

		for (final AbstractQuery abstractQuery : abstractQueries) {
			if (abstractQuery instanceof QueryFromComplexAnnotationCommand) {
				final String uniprotVersion = ((QueryFromComplexAnnotationCommand) abstractQuery).getUniprotVersion();
				ProteinAnnotator.getInstance(uniprotVersion)
						.annotateProteins(this.proteinProvider.getProteinMap(testMode));

				break;
			}
			if (abstractQuery instanceof QueryFromSimpleAnnotationCommand) {
				final String uniprotVersion = ((QueryFromSimpleAnnotationCommand) abstractQuery).getUniprotVersion();
				ProteinAnnotator.getInstance(uniprotVersion)
						.annotateProteins(this.proteinProvider.getProteinMap(testMode));

				break;
			}
			if (abstractQuery instanceof QueryFromSEQCommand) {
				// annotate the proteins in this case because the protein
				// sequence is going to be needed in the query
				if (abstractQuery.getAggregationLevel() == AggregationLevel.PROTEIN) {
					ProteinAnnotator.getInstance(null).annotateProteins(this.proteinProvider.getProteinMap(testMode));
					break;
				}
			}
			if (abstractQuery instanceof QueryFromTaxonomyCommand) {
				// annotate the proteins in this case because the protein
				// taxonomy is going to be needed in the query
				if (abstractQuery.getAggregationLevel() == AggregationLevel.PROTEIN) {
					ProteinAnnotator.getInstance(null).annotateProteins(this.proteinProvider.getProteinMap(testMode));
					break;
				}
			}
			if (abstractQuery instanceof QueryFromGeneNameCommand) {
				// annotate the proteins in this case because the protein
				// taxonomy is going to be needed in the query
				if (abstractQuery.getAggregationLevel() == AggregationLevel.PROTEIN) {
					ProteinAnnotator.getInstance(null).annotateProteins(this.proteinProvider.getProteinMap(testMode));
					break;
				}
			}
		}

		// if all of them are annotation related queries, it is not needed a new
		// evaluation
		boolean allAreAnnotationQueries = true;
		for (final AbstractQuery abstractQuery : abstractQueries) {
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
		for (final QueryFromAmountCommand queryFromAmountCommand : amountQueries) {
			if (queryFromAmountCommand.getAggregationLevel() == AggregationLevel.PROTEIN) {
				if (queryFromAmountCommand.getAmountType() == AmountType.SPC) {
					return true;
				}
			}
		}
		return false;
	}

	DataProviderFromDB getDominantProteinProvider(QueryBinaryTree queryBinaryTree) {
		// protein ACCESSION QUERY
		if (queryBinaryTree.isPredominant(QueryFromProteinAccessionsCommand.class)) {
			log.info("There is at least one predominant query that is over protein accessions");
			// get all the queries from protein accessions
			final Set<? extends AbstractQuery> accQueries = queryBinaryTree
					.getPredominantAbstractQueries(QueryFromProteinAccessionsCommand.class, LogicalOperator.AND);
			if (accQueries.size() == 1) {
				log.info("Getting the protein provider of the accession query");
				return accQueries.iterator().next().getProteinProvider();
			} else {
				log.info(
						"There are more than one query about ACCs. Joining accessions of all of them and building the protein provider");
				final Set<String> accs = new THashSet<String>();
				for (final AbstractQuery abstractQuery : accQueries) {
					accs.addAll(((QueryFromProteinAccessionsCommand) abstractQuery).getAccs());
				}
				return new ProteinProviderFromProteinAccs(accs);
			}
		}
		// PTM QUERY
		if (queryBinaryTree.isPredominant(QueryFromPTMCommand.class)) {
			log.info("There is at least one predominant query that is over PTM ");
			// get all the queries from PTM
			final Set<? extends AbstractQuery> ptmQueries = queryBinaryTree
					.getPredominantAbstractQueries(QueryFromPTMCommand.class, LogicalOperator.AND);
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

			final Set<AbstractQuery> abstractQueries = new THashSet<AbstractQuery>();
			final Set<QueryFromComplexAnnotationCommand> predominantAbstractQueries = (Set<QueryFromComplexAnnotationCommand>) queryBinaryTree
					.getPredominantAbstractQueries(QueryFromComplexAnnotationCommand.class, false, LogicalOperator.AND);
			abstractQueries.addAll(predominantAbstractQueries);
			final Set<QueryFromSimpleAnnotationCommand> predominantAbstractQueries2 = (Set<QueryFromSimpleAnnotationCommand>) queryBinaryTree
					.getPredominantAbstractQueries(QueryFromSimpleAnnotationCommand.class, false, LogicalOperator.AND);
			abstractQueries.addAll(predominantAbstractQueries2);
			LogicalOperator logicalOperator = LogicalOperator.AND;
			if (abstractQueries.isEmpty()) {
				// try to see if we have all the annotation queries with an or
				final Set<QueryFromComplexAnnotationCommand> predominantAbstractQueries3 = (Set<QueryFromComplexAnnotationCommand>) queryBinaryTree
						.getPredominantAbstractQueries(QueryFromComplexAnnotationCommand.class, false,
								LogicalOperator.OR);
				abstractQueries.addAll(predominantAbstractQueries3);
				final Set<QueryFromSimpleAnnotationCommand> predominantAbstractQueries4 = (Set<QueryFromSimpleAnnotationCommand>) queryBinaryTree
						.getPredominantAbstractQueries(QueryFromSimpleAnnotationCommand.class, false,
								LogicalOperator.OR);
				abstractQueries.addAll(predominantAbstractQueries4);
				if (!abstractQueries.isEmpty()) {
					logicalOperator = LogicalOperator.OR;
				}
			}

			return getDominantProteinProviderFromAnnotationQueries(abstractQueries, logicalOperator);
		}
		return null;
	}

	public boolean isProteinLevelQuery() {
		return allQueriesAreTheSameAggregationLevel(queryBinaryTree, AggregationLevel.PROTEIN);
	}

	private DataProviderFromDB getDominantProteinProviderFromAnnotationQueries(
			Set<? extends AbstractQuery> annotationQueries, LogicalOperator logicalOperator) {
		final String uniprotKBVersion = null;

		// get all the proteins
		final Map<String, Set<Protein>> proteinMap = proteinProvider.getProteinMap(testMode);
		// annotate them. After, they will be available by
		// getProteinAnnotationByProteinAcc(acc)
		ProteinAnnotator.getInstance(uniprotKBVersion).annotateProteins(proteinMap);
		// filter them and keep the valids
		final Set<String> validProteinAccs = new THashSet<String>();
		log.info("Performing a pre evaluation of the proteins checking their annotations...");
		log.info("Checking " + proteinMap.size() + " proteins");
		for (final String proteinAcc : proteinMap.keySet()) {
			final Set<edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation> annotations = ProteinAnnotator
					.getInstance(uniprotKBVersion).getProteinAnnotationByProteinAcc(proteinAcc);
			boolean valid = true;
			for (final AbstractQuery abstractQuery : annotationQueries) {
				boolean evaluationResult = true;
				if (abstractQuery instanceof QueryFromSimpleAnnotationCommand) {
					final QueryFromSimpleAnnotationCommand query = (QueryFromSimpleAnnotationCommand) abstractQuery;
					evaluationResult = query.evaluate(annotations);
				} else if (abstractQuery instanceof QueryFromComplexAnnotationCommand) {
					final QueryFromComplexAnnotationCommand query = (QueryFromComplexAnnotationCommand) abstractQuery;
					evaluationResult = query.evaluate(annotations);
				}
				if (!abstractQuery.isNegative()) {
					if (!evaluationResult) {
						valid = false;
						if (logicalOperator == LogicalOperator.AND) {
							break;
						}
					} else {
						// it is valid
						if (logicalOperator == LogicalOperator.OR) {
							valid = true;
							break;
						}
					}
				} else {
					// is negative
					if (evaluationResult) {
						valid = false;
						if (logicalOperator == LogicalOperator.AND) {
							break;
						}
					} else {
						// it is valid
						if (logicalOperator == LogicalOperator.OR) {
							valid = true;
							break;
						}
					}
				}
			}

			if (valid) {
				validProteinAccs.add(proteinAcc);
			}
		}
		log.info(validProteinAccs.size() + " out of " + proteinMap.size() + " where valid");
		// construct the protein provider with the remaining valid proteins
		final DataProviderFromDB proteinProviderFromAcc = new ProteinProviderFromProteinAccs(validProteinAccs);
		return proteinProviderFromAcc;
	}

	public QueryResult getQueryResults() {
		if (queryResult == null) {
			List<LinkBetweenQueriableProteinSetAndPSM> linksBetweenProteinsAndPSMs = null;
			List<LinkBetweenQueriableProteinSetAndPSM> invalidLinksBetweenProteinsAndPSMs = null;
			List<LinkBetweenQueriableProteinSetAndPeptideSet> linksBetweenProteinsAndPeptides = null;
			List<LinkBetweenQueriableProteinSetAndPeptideSet> invalidLinksBetweenProteinsAndPeptides = null;
			// initialize project items mappings
			// important to optimize the method getConditions() from
			// QueriableProteinSet and QueriablePeptideSet
			// ProjectItemsMapping.getInstance().loadMappings(projectTags);
			final List<Condition> conditions = getConditions(projectTags);
			for (final Condition condition : conditions) {
				ConditionToProteinTableMapper.getInstance().addObject1(condition);
				ConditionToPeptideTableMapper.getInstance().addObject1(condition);
			}
			//
			if (psmCentric) {
				linksBetweenProteinsAndPSMs = QueriesUtil
						.createProteinPSMLinks(proteinProvider.getProteinMap(testMode));
				invalidLinksBetweenProteinsAndPSMs = new ArrayList<LinkBetweenQueriableProteinSetAndPSM>();
				if (needLinkEvaluation) {

					int numDiscardedLinks = 0;
					int numValidLinks = 0;
					int numRound = 1;
					do {
						log.info("Evaluating " + linksBetweenProteinsAndPSMs.size()
								+ " links between proteins and PSMs in round " + numRound);
						numDiscardedLinks = 0;
						numValidLinks = 0;
						int numLinks = 0;
						final int totalLinks = linksBetweenProteinsAndPSMs.size();
						final Iterator<LinkBetweenQueriableProteinSetAndPSM> linksIterator = linksBetweenProteinsAndPSMs
								.iterator();
						while (linksIterator.hasNext()) {
							final LinkBetweenQueriableProteinSetAndPSM link = linksIterator.next();
							numLinks++;
							if (numLinks % 100 == 0) {
								log.info(numRound + " round - " + numLinks + "/" + totalLinks + " links ("
										+ numValidLinks + " valid, " + numDiscardedLinks + " discarded)");
							}

							final boolean valid = queryBinaryTree.evaluate(link);

							// evaluate the links between individual proteins
							// and
							// psms
							// final Set<QueriableProtein2PSMLink>
							// protein2psmLinks
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
								invalidLinksBetweenProteinsAndPSMs.add(link);
								numDiscardedLinks++;
								// delete link between QueriableProtein and
								// QueriablePSM
								link.detachFromProteinAndPSM();
								linksIterator.remove();
							}
						}
						log.info(linksBetweenProteinsAndPSMs.size() + " Protein-PSM links remain after " + numRound
								+ " round. " + numValidLinks + " valid links. " + numDiscardedLinks
								+ " were discarded.");
						numRound++;
						if (!containsSPCAmountQuery(queryBinaryTree)) {
							log.info(
									"Query not containing SPC amount query, so it is not necessary to keep in the loop");
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
					// final Set<LinkBetweenProteinAndPSMImpl> protein2psmLinks
					// =
					// link.getProtein2PSMLinks();
					// for (LinkBetweenProteinAndPSMImpl
					// queriableProtein2PSMLink :
					// protein2psmLinks) {
					// // link.setProtein2PsmResult(queriableProtein2PSMLink,
					// // true);
					// }
					// }
				}
			} else {
				final Map<String, Set<Protein>> proteinMap = proteinProvider.getProteinMap(testMode);
				linksBetweenProteinsAndPeptides = QueriesUtil.createProteinPeptideLinks(proteinMap);
				invalidLinksBetweenProteinsAndPeptides = new ArrayList<LinkBetweenQueriableProteinSetAndPeptideSet>();
				if (needLinkEvaluation) {

					int numDiscardedLinks = 0;
					int numValidLinks = 0;
					int numRound = 1;
					do {
						log.info("Evaluating " + linksBetweenProteinsAndPeptides.size()
								+ " links between proteins and peptides in round " + numRound);
						numDiscardedLinks = 0;
						numValidLinks = 0;
						int numLinks = 0;
						final int totalLinks = linksBetweenProteinsAndPeptides.size();
						final Iterator<LinkBetweenQueriableProteinSetAndPeptideSet> linksIterator = linksBetweenProteinsAndPeptides
								.iterator();
						while (linksIterator.hasNext()) {
							final LinkBetweenQueriableProteinSetAndPeptideSet link = linksIterator.next();
							numLinks++;
							if (numLinks % 100 == 0) {
								log.info(numRound + " round - " + numLinks + "/" + totalLinks + " links ("
										+ numValidLinks + " valid, " + numDiscardedLinks + " discarded)");
							}

							final boolean valid = queryBinaryTree.evaluate(link);

							// evaluate the links between individual proteins
							// and
							// psms
							// final Set<QueriableProtein2PSMLink>
							// protein2psmLinks
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
								invalidLinksBetweenProteinsAndPeptides.add(link);
								numDiscardedLinks++;
								// delete link between QueriableProtein and
								// QueriablePSM
								link.detachFromProteinAndPeptide();
								linksIterator.remove();
							}
						}
						log.info(linksBetweenProteinsAndPeptides.size() + " Protein-Peptide links remain after "
								+ numRound + " round. " + numValidLinks + " valid links. " + numDiscardedLinks
								+ " were discarded.");
						numRound++;
						if (!containsSPCAmountQuery(queryBinaryTree)) {
							log.info(
									"Query not containing SPC amount query, so it is not necessary to keep in the loop");
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
					// final Set<LinkBetweenProteinAndPSMImpl> protein2psmLinks
					// =
					// link.getProtein2PSMLinks();
					// for (LinkBetweenProteinAndPSMImpl
					// queriableProtein2PSMLink :
					// protein2psmLinks) {
					// // link.setProtein2PsmResult(queriableProtein2PSMLink,
					// // true);
					// }
					// }
				}
			}
			queryResult = new QueryResult(linksBetweenProteinsAndPSMs, linksBetweenProteinsAndPeptides,
					invalidLinksBetweenProteinsAndPSMs, invalidLinksBetweenProteinsAndPeptides);
			// }else{
			// queryBinaryTree.getAbstractQueries().get(0).
			// }

		}
		return queryResult;
	}

	private List<Condition> getConditions(Set<String> projectTags) {
		final List<Condition> conditions = new ArrayList<Condition>();
		for (final String projectTag : projectTags) {
			conditions.addAll(PreparedCriteria.getConditionsByProjectCriteria(projectTag));

		}
		return conditions;
	}

	private boolean allQueriesAreTheSameAggregationLevel(QueryBinaryTree queryBinaryTree2) {
		AggregationLevel level = null;
		final List<AbstractQuery> abstractQueries = queryBinaryTree2.getAbstractQueries();
		for (final AbstractQuery abstractQuery : abstractQueries) {
			final AggregationLevel aggregationLevel = abstractQuery.getAggregationLevel();
			if (level == null) {
				level = aggregationLevel;
			} else if (level != aggregationLevel) {
				return false;
			}
		}
		return true;
	}

	private boolean allQueriesAreTheSameAggregationLevel(QueryBinaryTree queryBinaryTree2, AggregationLevel level) {

		final List<AbstractQuery> abstractQueries = queryBinaryTree2.getAbstractQueries();
		for (final AbstractQuery abstractQuery : abstractQueries) {
			final AggregationLevel aggregationLevel = abstractQuery.getAggregationLevel();
			if (level != aggregationLevel) {
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
			List<LinkBetweenQueriableProteinSetAndPSM> linksBetweenProteinsAndPSMs = null;
			List<LinkBetweenQueriableProteinSetAndPSM> invalidLinksBetweenProteinsAndPSMs = null;
			List<LinkBetweenQueriableProteinSetAndPeptideSet> linksBetweenProteinsAndPeptides = null;
			List<LinkBetweenQueriableProteinSetAndPeptideSet> invalidLinksBetweenProteinsAndPeptides = null;

			if (psmCentric) {
				linksBetweenProteinsAndPSMs = QueriesUtil
						.createProteinPSMLinks(proteinProvider.getProteinMap(testMode));

				int numDiscardedLinks = 0;
				int numRound = 1;

				invalidLinksBetweenProteinsAndPSMs = new ArrayList<LinkBetweenQueriableProteinSetAndPSM>();

				do {
					final int threadCount = SystemCoreManager.getAvailableNumSystemCores(MAX_NUMBER_PARALLEL_PROCESSES);
					log.info("Evaluating " + linksBetweenProteinsAndPSMs.size()
							+ " links between proteins and PSMs in round " + numRound + " using " + threadCount
							+ " cores out of " + Runtime.getRuntime().availableProcessors());
					final ParIterator<LinkBetweenQueriableProteinSetAndPSM> iterator = ParIteratorFactory
							.createParIterator(linksBetweenProteinsAndPSMs, threadCount, Schedule.GUIDED);

					final Reducible<List<LinkBetweenQueriableProteinSetAndPSM>> reducibleLinkMap = new Reducible<List<LinkBetweenQueriableProteinSetAndPSM>>();
					final List<ProteinPSMLinkParallelProcesor> runners = new ArrayList<ProteinPSMLinkParallelProcesor>();
					for (int numCore = 0; numCore < threadCount; numCore++) {
						// take current DB session
						final ProteinPSMLinkParallelProcesor runner = new ProteinPSMLinkParallelProcesor(iterator,
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
						} catch (final InterruptedException e) {
							e.printStackTrace();
						}
					}
					final Reduction<List<LinkBetweenQueriableProteinSetAndPSM>> linkReduction = new Reduction<List<LinkBetweenQueriableProteinSetAndPSM>>() {
						@Override
						public List<LinkBetweenQueriableProteinSetAndPSM> reduce(
								List<LinkBetweenQueriableProteinSetAndPSM> first,
								List<LinkBetweenQueriableProteinSetAndPSM> second) {
							first.addAll(second);
							return first;
						}

					};
					linksBetweenProteinsAndPSMs = reducibleLinkMap.reduce(linkReduction);
					numDiscardedLinks = 0;
					long time = 0;
					for (int k = 0; k < threadCount; k++) {
						numDiscardedLinks = +runners.get(k).getNumDiscardedLinks();
						invalidLinksBetweenProteinsAndPSMs.addAll(runners.get(k).getDiscardedLinks());
						time = +runners.get(k).getRunningTime();
					}
					log.info(linksBetweenProteinsAndPSMs.size() + " Protein-PSM links remain after " + numRound
							+ " round. " + numDiscardedLinks + " were discarded in " + time / 1000 + "sg");
					numRound++;
				} while (numDiscardedLinks > 0);

			} else {
				linksBetweenProteinsAndPeptides = QueriesUtil
						.createProteinPeptideLinks(proteinProvider.getProteinMap(testMode));

				int numDiscardedLinks = 0;
				int numRound = 1;

				invalidLinksBetweenProteinsAndPeptides = new ArrayList<LinkBetweenQueriableProteinSetAndPeptideSet>();

				do {
					final int threadCount = SystemCoreManager.getAvailableNumSystemCores(MAX_NUMBER_PARALLEL_PROCESSES);
					log.info("Evaluating " + linksBetweenProteinsAndPeptides.size()
							+ " links between proteins and peptides in round " + numRound + " using " + threadCount
							+ " cores out of " + Runtime.getRuntime().availableProcessors());
					final ParIterator<LinkBetweenQueriableProteinSetAndPeptideSet> iterator = ParIteratorFactory
							.createParIterator(linksBetweenProteinsAndPeptides, threadCount, Schedule.GUIDED);

					final Reducible<List<LinkBetweenQueriableProteinSetAndPeptideSet>> reducibleLinkMap = new Reducible<List<LinkBetweenQueriableProteinSetAndPeptideSet>>();
					final List<ProteinPeptideLinkParallelProcesor> runners = new ArrayList<ProteinPeptideLinkParallelProcesor>();
					for (int numCore = 0; numCore < threadCount; numCore++) {
						// take current DB session
						final ProteinPeptideLinkParallelProcesor runner = new ProteinPeptideLinkParallelProcesor(
								iterator, reducibleLinkMap, queryBinaryTree);
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
						} catch (final InterruptedException e) {
							e.printStackTrace();
						}
					}
					final Reduction<List<LinkBetweenQueriableProteinSetAndPeptideSet>> linkReduction = new Reduction<List<LinkBetweenQueriableProteinSetAndPeptideSet>>() {
						@Override
						public List<LinkBetweenQueriableProteinSetAndPeptideSet> reduce(
								List<LinkBetweenQueriableProteinSetAndPeptideSet> first,
								List<LinkBetweenQueriableProteinSetAndPeptideSet> second) {
							first.addAll(second);
							return first;
						}

					};
					linksBetweenProteinsAndPeptides = reducibleLinkMap.reduce(linkReduction);
					numDiscardedLinks = 0;
					long time = 0;
					for (int k = 0; k < threadCount; k++) {
						numDiscardedLinks = +runners.get(k).getNumDiscardedLinks();
						invalidLinksBetweenProteinsAndPeptides.addAll(runners.get(k).getDiscardedLinks());
						time = +runners.get(k).getRunningTime();
					}
					log.info(linksBetweenProteinsAndPeptides.size() + " Protein-Peptide links remain after " + numRound
							+ " round. " + numDiscardedLinks + " were discarded in " + time / 1000 + "sg");
					numRound++;
				} while (numDiscardedLinks > 0);
			}
			queryResult = new QueryResult(linksBetweenProteinsAndPSMs, linksBetweenProteinsAndPeptides,
					invalidLinksBetweenProteinsAndPSMs, invalidLinksBetweenProteinsAndPeptides);

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
