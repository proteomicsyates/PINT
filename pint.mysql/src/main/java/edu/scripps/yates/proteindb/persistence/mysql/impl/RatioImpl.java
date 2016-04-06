package edu.scripps.yates.proteindb.persistence.mysql.impl;

import edu.scripps.yates.proteindb.persistence.mysql.ConfidenceScoreType;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.PsmRatioValue;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;
import edu.scripps.yates.utilities.model.enums.CombinationType;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import edu.scripps.yates.utilities.proteomicsmodel.Score;

public class RatioImpl implements Ratio {
	private final ProteinRatioValue hibProteinRatioValue;
	private Condition condition1;
	private Condition condition2;
	private final PeptideRatioValue hibPeptideRatioValue;
	private final PsmRatioValue hibPsmRatioValue;
	private final AggregationLevel aggregationLevel;

	public RatioImpl(ProteinRatioValue proteinRatioValue) {
		hibProteinRatioValue = proteinRatioValue;
		hibPeptideRatioValue = null;
		hibPsmRatioValue = null;
		aggregationLevel = AggregationLevel.PROTEIN;
	}

	public RatioImpl(PeptideRatioValue peptideRatioValue) {
		hibPeptideRatioValue = peptideRatioValue;
		hibProteinRatioValue = null;
		hibPsmRatioValue = null;
		aggregationLevel = AggregationLevel.PEPTIDE;
	}

	public RatioImpl(PsmRatioValue psmRatioValue) {
		hibPsmRatioValue = psmRatioValue;
		hibPeptideRatioValue = null;
		hibProteinRatioValue = null;
		aggregationLevel = AggregationLevel.PSM;
	}

	@Override
	public double getValue() {
		return hibProteinRatioValue.getValue();
	}

	@Override
	public Condition getCondition1() {
		if (condition1 == null) {
			edu.scripps.yates.proteindb.persistence.mysql.Condition hibExperimentalCondition = null;
			if (hibProteinRatioValue != null)
				hibExperimentalCondition = hibProteinRatioValue.getRatioDescriptor()
						.getConditionByExperimentalCondition1Id();
			if (hibPeptideRatioValue != null)
				hibExperimentalCondition = hibPeptideRatioValue.getRatioDescriptor()
						.getConditionByExperimentalCondition1Id();
			if (hibPsmRatioValue != null)
				hibExperimentalCondition = hibPsmRatioValue.getRatioDescriptor()
						.getConditionByExperimentalCondition1Id();

			if (AmountImpl.experimentalConditions.containsKey(hibExperimentalCondition.getId())) {
				condition1 = AmountImpl.experimentalConditions.get(hibExperimentalCondition.getId());
			} else {
				final ConditionImpl experimentalConditionImpl = new ConditionImpl(hibExperimentalCondition);
				condition1 = experimentalConditionImpl;
				AmountImpl.experimentalConditions.put(hibExperimentalCondition.getId(), experimentalConditionImpl);
			}

		}
		return condition1;
	}

	@Override
	public Condition getCondition2() {
		if (condition2 == null) {
			edu.scripps.yates.proteindb.persistence.mysql.Condition hibExperimentalCondition = null;
			if (hibProteinRatioValue != null)
				hibExperimentalCondition = hibProteinRatioValue.getRatioDescriptor()
						.getConditionByExperimentalCondition2Id();
			if (AmountImpl.experimentalConditions.containsKey(hibExperimentalCondition.getId())) {
				condition2 = AmountImpl.experimentalConditions.get(hibExperimentalCondition.getId());
			} else {
				final ConditionImpl experimentalConditionImpl = new ConditionImpl(hibExperimentalCondition);
				condition2 = experimentalConditionImpl;
				AmountImpl.experimentalConditions.put(hibExperimentalCondition.getId(), experimentalConditionImpl);
			}

		}
		return condition2;
	}

	@Override
	public String getDescription() {
		if (hibProteinRatioValue != null)
			return hibProteinRatioValue.getRatioDescriptor().getDescription();
		if (hibPeptideRatioValue != null)
			return hibPeptideRatioValue.getRatioDescriptor().getDescription();
		if (hibPsmRatioValue != null)
			return hibPsmRatioValue.getRatioDescriptor().getDescription();
		return null;
	}

	@Override
	public Score getAssociatedConfidenceScore() {
		Double confidenceScoreValue = null;
		ConfidenceScoreType confidenceScoreType = null;
		String confidenceScoreName = null;

		if (hibProteinRatioValue != null) {
			confidenceScoreValue = hibProteinRatioValue.getConfidenceScoreValue();
			confidenceScoreType = hibProteinRatioValue.getConfidenceScoreType();
			confidenceScoreName = hibProteinRatioValue.getConfidenceScoreName();
		}
		if (hibPeptideRatioValue != null) {
			confidenceScoreValue = hibPeptideRatioValue.getConfidenceScoreValue();
			confidenceScoreType = hibPeptideRatioValue.getConfidenceScoreType();
			confidenceScoreName = hibPeptideRatioValue.getConfidenceScoreName();
		}
		if (hibPsmRatioValue != null) {
			confidenceScoreValue = hibPsmRatioValue.getConfidenceScoreValue();
			confidenceScoreType = hibPsmRatioValue.getConfidenceScoreType();
			confidenceScoreName = hibPsmRatioValue.getConfidenceScoreName();
		}

		if (confidenceScoreValue != null && confidenceScoreType != null) {
			return new ScoreImpl(confidenceScoreName, confidenceScoreValue.toString(), confidenceScoreType);
		}
		return null;
	}

	@Override
	public CombinationType getCombinationType() {
		return CombinationType.valueOf(hibProteinRatioValue.getCombinationType().getName());
	}

	@Override
	public AggregationLevel getAggregationLevel() {
		return aggregationLevel;
	}

}
