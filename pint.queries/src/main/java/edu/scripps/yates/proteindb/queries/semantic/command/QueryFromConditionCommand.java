package edu.scripps.yates.proteindb.queries.semantic.command;

import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.queries.Query;
import edu.scripps.yates.proteindb.queries.dataproviders.DataProviderFromDB;
import edu.scripps.yates.proteindb.queries.dataproviders.protein.ProteinProviderFromProjectCondition;
import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.AbstractQuery;
import edu.scripps.yates.proteindb.queries.semantic.ConditionReferenceFromCommandValue;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPSM;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPeptideSet;
import edu.scripps.yates.proteindb.queries.semantic.util.CommandReference;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;
import gnu.trove.set.hash.THashSet;

/**
 * Implements a {@link Query} from CONDITION command:<br>
 * COND[condition_name, experiment_name]
 *
 * @author Salva
 *
 */
public class QueryFromConditionCommand extends AbstractQuery {

	private final ConditionReferenceFromCommandValue conditionReference;
	private static final Logger log = Logger.getLogger(QueryFromConditionCommand.class);

	public QueryFromConditionCommand(CommandReference commandReference) throws MalformedQueryException {
		super(commandReference);
		conditionReference = new ConditionReferenceFromCommandValue(commandReference.getCommandValue());

	}

	/**
	 * @return the conditionReference
	 */
	public ConditionReferenceFromCommandValue getConditionReference() {
		return conditionReference;
	}

	@Override
	public boolean evaluate(LinkBetweenQueriableProteinSetAndPSM link) {

		if (conditionReference.passCondition(link.getQueriableProtein())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean evaluate(LinkBetweenQueriableProteinSetAndPeptideSet link) {

		if (conditionReference.passCondition(link.getQueriableProtein())) {
			return true;
		}
		return false;
	}

	@Override
	public AggregationLevel getAggregationLevel() {

		// it cannot be at PSM level, since that would not allow to ask for
		// proteins present in several conditions with an AND clause
		return AggregationLevel.PROTEIN;

	}

	@Override
	public DataProviderFromDB initProtenProvider() {
		return new ProteinProviderFromProjectCondition(conditionReference);
	}

	@Override
	public boolean requiresFurtherEvaluation() {
		if (conditionReference.getConditionProjects().size() > 1)
			return true;
		return false;
	}

	public boolean evaluate(Set<LinkBetweenQueriableProteinSetAndPSM> linkSetForSameProtein) {
		final Set<Condition> conditions = new THashSet<Condition>();
		for (final LinkBetweenQueriableProteinSetAndPSM proteinPSMLink : linkSetForSameProtein) {
			conditions.addAll(proteinPSMLink.getQueriableProtein().getConditions());
		}
		return conditionReference.passAllConditions(conditions);
	}

}
