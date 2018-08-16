package edu.scripps.yates.proteindb.queries.semantic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.queries.LogicalOperator;
import edu.scripps.yates.proteindb.queries.semantic.util.BinaryTree;
import gnu.trove.set.hash.THashSet;

public class QueryBinaryTree extends BinaryTree<QueryBinaryTreeElement> {
	private final static Logger log = Logger.getLogger(QueryBinaryTree.class);

	public QueryBinaryTree(AbstractQuery abstractQuery) {
		super(new QueryBinaryTreeElement(abstractQuery));
	}

	public QueryBinaryTree(String word) {
		super(new QueryBinaryTreeElement(word));
	}

	public boolean evaluate(LinkBetweenQueriableProteinSetAndPSM link) {
		if (element.getLogicalOperator() != null) {
			final boolean leftResult = getLeftBNode().evaluate(link);
			if (leftResult == false && LogicalOperator.AND == element.getLogicalOperator())
				return false;

			final boolean rightResult = getRightBNode().evaluate(link);
			if (rightResult == false && LogicalOperator.AND == element.getLogicalOperator())
				return false;

			switch (element.getLogicalOperator()) {
			case AND:
				final boolean result = leftResult & rightResult;
				return result;
			case OR:
				return leftResult || rightResult;
			case XOR:
				if (leftResult && !rightResult) {
					return true;
				} else if (!leftResult && rightResult) {
					return true;
				} else {
					return false;
				}
			default:
				break;
			}

		} else if (element.getAbstractQuery() != null) {
			final AbstractQuery query = element.getAbstractQuery();

			// execute
			final boolean queryResult = query.evaluate(link);

			// TODO CHECK THIS (11/11/15)
			// if (queryResult == false && query instanceof
			// QueryFromConditionCommand) {
			// QueryFromConditionCommand queryCondition =
			// (QueryFromConditionCommand) query;
			// queryResult =
			// queryCondition.evaluate(link.getLinkSetForSameProtein());
			// }
			boolean finalResult = queryResult;
			// look if it is negative
			if (query.isNegative()) {
				finalResult = !queryResult;
			} else {
				finalResult = queryResult;
			}

			return finalResult;
		}
		throw new IllegalArgumentException("Binary tree cannot be evaluated");

	}

	public boolean evaluate(LinkBetweenQueriableProteinSetAndPeptideSet link) {
		if (element.getLogicalOperator() != null) {
			final boolean leftResult = getLeftBNode().evaluate(link);
			if (leftResult == false && LogicalOperator.AND == element.getLogicalOperator())
				return false;

			final boolean rightResult = getRightBNode().evaluate(link);
			if (rightResult == false && LogicalOperator.AND == element.getLogicalOperator())
				return false;

			switch (element.getLogicalOperator()) {
			case AND:
				final boolean result = leftResult & rightResult;
				return result;
			case OR:
				return leftResult || rightResult;
			case XOR:
				if (leftResult && !rightResult) {
					return true;
				} else if (!leftResult && rightResult) {
					return true;
				} else {
					return false;
				}
			default:
				break;
			}

		} else if (element.getAbstractQuery() != null) {
			final AbstractQuery query = element.getAbstractQuery();

			// execute
			final boolean queryResult = query.evaluate(link);

			// TODO CHECK THIS (11/11/15)
			// if (queryResult == false && query instanceof
			// QueryFromConditionCommand) {
			// QueryFromConditionCommand queryCondition =
			// (QueryFromConditionCommand) query;
			// queryResult =
			// queryCondition.evaluate(link.getLinkSetForSameProtein());
			// }
			boolean finalResult = queryResult;
			// look if it is negative
			if (query.isNegative()) {
				finalResult = !queryResult;
			} else {
				finalResult = queryResult;
			}

			return finalResult;
		}
		throw new IllegalArgumentException("Binary tree cannot be evaluated");

	}

	private QueryBinaryTree getLeftBNode() {
		return (QueryBinaryTree) leftBNode;
	}

	private QueryBinaryTree getRightBNode() {
		return (QueryBinaryTree) rightBNode;
	}

	private AbstractQuery getLeftAbstractQuery() {
		return getLeftBNode().getElement().getAbstractQuery();
	}

	private AbstractQuery getRigthAbstractQuery() {
		return getRightBNode().getElement().getAbstractQuery();
	}

	public List<AbstractQuery> getAbstractQueries() {
		final List<AbstractQuery> ret = new ArrayList<AbstractQuery>();
		if (element.getLogicalOperator() != null) {
			ret.addAll(getLeftBNode().getAbstractQueries());
			ret.addAll(getRightBNode().getAbstractQueries());
		} else {
			ret.add(element.getAbstractQuery());
		}
		return ret;
	}

	/**
	 * Returns true if all the logical operations in the tree are equals to the
	 * one provided
	 *
	 * @return
	 */
	public boolean isAllLogicalOperations(LogicalOperator logicalOperator) {
		if (element.getLogicalOperator() != null) {
			return element.getLogicalOperator() == logicalOperator
					&& getLeftBNode().isAllLogicalOperations(logicalOperator)
					&& getRightBNode().isAllLogicalOperations(logicalOperator);
		} else {
			return true;
		}

	}

	/**
	 * Returns true if all the queries in the tree are equals to the one
	 * provided
	 *
	 * @return
	 */
	public boolean isAllQueries(Class<? extends AbstractQuery> class1) {
		if (element.getLogicalOperator() != null) {
			return getLeftBNode().isAllQueries(class1) && getRightBNode().isAllQueries(class1);
		} else {
			return class1.isInstance(element.getAbstractQuery());
		}
	}

	/**
	 * Returns true if some of the queries in the tree are equals to the one
	 * provided
	 *
	 * @return
	 */
	public boolean isSomeQueries(Class<? extends AbstractQuery> class1) {
		if (element.getLogicalOperator() != null) {
			return getLeftBNode().isAllQueries(class1) || getRightBNode().isAllQueries(class1);
		} else {
			return class1.isInstance(element.getAbstractQuery());
		}
	}

	/**
	 * Get a Set of the queries that are of the type passed in the argument
	 *
	 * @param class1
	 * @return
	 */
	public Set<? extends AbstractQuery> getAbstractQueries(Class<? extends AbstractQuery> class1) {
		final Set<AbstractQuery> ret = new THashSet<AbstractQuery>();
		for (final AbstractQuery abstractQuery : getAbstractQueries()) {
			if (class1.isInstance(abstractQuery)) {
				ret.add(abstractQuery);
			}
		}
		return ret;
	}

	/**
	 * Returns true if there is some query in the tree that is negative
	 *
	 * @return
	 */
	public boolean isSomeNegativeQuery() {
		if (element.getLogicalOperator() != null) {
			return getLeftBNode().isSomeNegativeQuery() || getRightBNode().isSomeNegativeQuery();
		} else {
			return element.getAbstractQuery().isNegative();
		}
	}

	/**
	 * Gets a set of queries of a certain class, which is predominant, which
	 * means that going up in the query tree, all the logical operators are AND
	 * By default, the queries have to be non negative
	 *
	 * @param class1
	 * @return
	 */
	public Set<? extends AbstractQuery> getPredominantAbstractQueries(Class<? extends AbstractQuery> class1,
			LogicalOperator logicalOperator) {
		return getPredominantAbstractQueries(class1, true, logicalOperator);
	}

	/**
	 * Gets a set of queries of a certain class, which is predominant, which
	 * means that going up in the query tree, all the logical operators are a
	 * certain one passed as argument
	 *
	 * @param require
	 *            or not that the query is not negative.
	 * @param class1
	 * @return
	 */
	public Set<? extends AbstractQuery> getPredominantAbstractQueries(Class<? extends AbstractQuery> class1,
			boolean requireNonNegative, LogicalOperator logicalOperator) {
		final Set<AbstractQuery> ret = new THashSet<AbstractQuery>();
		if (element.getLogicalOperator() != null) {
			if (element.getLogicalOperator() == logicalOperator) {
				ret.addAll(getLeftBNode().getPredominantAbstractQueries(class1, requireNonNegative, logicalOperator));
				ret.addAll(getRightBNode().getPredominantAbstractQueries(class1, requireNonNegative, logicalOperator));
			}
			return ret;
		} else {
			if (class1.isInstance(element.getAbstractQuery())) {
				if (!requireNonNegative || !element.getAbstractQuery().isNegative()) {
					ret.add(element.getAbstractQuery());
				}
			}
		}
		return ret;
	}

	/**
	 * Returns true if there is at least one query of a certain class that is
	 * predominant, which means that going up in the query tree, all the logical
	 * operators are AND
	 *
	 * @param class1
	 * @return
	 */
	public boolean isPredominant(Class<? extends AbstractQuery> class1) {
		if (element.getLogicalOperator() != null) {
			if (element.getLogicalOperator() == LogicalOperator.AND) {
				return getLeftBNode().isPredominant(class1) || getRightBNode().isPredominant(class1);
			}
			return false;
		} else {
			final AbstractQuery abstractQuery = element.getAbstractQuery();
			final boolean negative = abstractQuery.isNegative();
			return class1.isInstance(abstractQuery) && !negative;
		}
	}

}
