package edu.scripps.yates.server.adapters;

import java.util.List;

import edu.scripps.yates.proteindb.persistence.mysql.PeptideRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.PsmRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.RatioDescriptor;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.RatioDescriptorIDToPSMRatioValueIDTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.RatioDescriptorIDToPeptideRatioValueIDTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.RatioDescriptorIDToProteinRatioValueIDTableMapper;
import edu.scripps.yates.shared.model.RatioDescriptorBean;
import edu.scripps.yates.shared.model.SharedAggregationLevel;
import gnu.trove.set.TIntSet;

public class RatioDescriptorAdapter implements Adapter<RatioDescriptorBean> {
	private final RatioDescriptor descriptor;
	private final SharedAggregationLevel aggregationLevel;
	private final ProteinRatioValue proteinRatioValue;
	private final PeptideRatioValue peptideRatioValue;
	private final PsmRatioValue psmRatioValue;

	public RatioDescriptorAdapter(RatioDescriptor descriptor, SharedAggregationLevel aggregationLevel,
			ProteinRatioValue proteinRatioValue) {
		this.descriptor = descriptor;
		this.aggregationLevel = aggregationLevel;
		this.proteinRatioValue = proteinRatioValue;
		this.peptideRatioValue = null;
		this.psmRatioValue = null;
	}

	public RatioDescriptorAdapter(RatioDescriptor descriptor, SharedAggregationLevel aggregationLevel,
			PeptideRatioValue peptideRatioValue) {
		this.descriptor = descriptor;
		this.aggregationLevel = aggregationLevel;
		this.proteinRatioValue = null;
		this.peptideRatioValue = peptideRatioValue;
		this.psmRatioValue = null;
	}

	public RatioDescriptorAdapter(RatioDescriptor descriptor, SharedAggregationLevel aggregationLevel,
			PsmRatioValue psmRatioValue) {
		this.descriptor = descriptor;
		this.aggregationLevel = aggregationLevel;
		this.proteinRatioValue = null;
		this.peptideRatioValue = null;
		this.psmRatioValue = psmRatioValue;
	}

	@Override
	public RatioDescriptorBean adapt() {
		final RatioDescriptorBean ret = new RatioDescriptorBean();
		ret.setRatioDescriptorID(descriptor.getId());
		ret.setCondition1Name(descriptor.getConditionByExperimentalCondition1Id().getName());
		ret.setCondition2Name(descriptor.getConditionByExperimentalCondition2Id().getName());
		ret.setProjectTag(descriptor.getConditionByExperimentalCondition1Id().getProject().getTag());
		ret.setRatioName(descriptor.getDescription());

		if (proteinRatioValue != null) {
			if (proteinRatioValue.getConfidenceScoreName() != null) {
				ret.setProteinScoreName(proteinRatioValue.getConfidenceScoreName());
			}
		} else if (peptideRatioValue != null) {
			if (peptideRatioValue.getConfidenceScoreName() != null) {
				ret.setPeptideScoreName(peptideRatioValue.getConfidenceScoreName());
			}
		} else if (psmRatioValue != null) {
			if (psmRatioValue.getConfidenceScoreName() != null) {
				ret.setPsmScoreName(psmRatioValue.getConfidenceScoreName());
			}
		} else {
			if (aggregationLevel == SharedAggregationLevel.PROTEIN) {
				final TIntSet proteinRatioValueIDs = RatioDescriptorIDToProteinRatioValueIDTableMapper.getInstance()
						.getProteinRatioValueIDsFromRatioDescriptorID(descriptor.getId());
				final List<ProteinRatioValue> proteinRatioValues = (List<ProteinRatioValue>) PreparedCriteria
						.getBatchLoadByIDs(ProteinRatioValue.class, proteinRatioValueIDs, true,
								proteinRatioValueIDs.size());
				if (proteinRatioValues != null && !proteinRatioValues.isEmpty()) {
					if (proteinRatioValues.get(0).getConfidenceScoreName() != null) {
						ret.setProteinScoreName(proteinRatioValues.get(0).getConfidenceScoreName());
					}
				}
			} else if (aggregationLevel == SharedAggregationLevel.PEPTIDE) {
				final TIntSet peptideRatioValueIDs = RatioDescriptorIDToPeptideRatioValueIDTableMapper.getInstance()
						.getPeptideRatioValueIDsFromRatioDescriptorID(descriptor.getId());
				final List<PeptideRatioValue> peptideRatioValues = (List<PeptideRatioValue>) PreparedCriteria
						.getBatchLoadByIDs(PeptideRatioValue.class, peptideRatioValueIDs, true,
								peptideRatioValueIDs.size());
				if (peptideRatioValues != null && !peptideRatioValues.isEmpty()) {
					if (peptideRatioValues.get(0).getConfidenceScoreName() != null) {
						ret.setPeptideScoreName(peptideRatioValues.get(0).getConfidenceScoreName());
					}
				}
			} else if (aggregationLevel == SharedAggregationLevel.PSM) {
				final TIntSet psmRatioValueIDs = RatioDescriptorIDToPSMRatioValueIDTableMapper.getInstance()
						.getPSMRatioValueIDsFromRatioDescriptorID(descriptor.getId());
				final List<PsmRatioValue> psmRatioValues = (List<PsmRatioValue>) PreparedCriteria
						.getBatchLoadByIDs(PsmRatioValue.class, psmRatioValueIDs, true, psmRatioValueIDs.size());
				if (psmRatioValues != null && !psmRatioValues.isEmpty()) {
					if (psmRatioValues.get(0).getConfidenceScoreName() != null) {
						ret.setPeptideScoreName(psmRatioValues.get(0).getConfidenceScoreName());
					}
				}
			}

		}
		ret.setAggregationLevel(aggregationLevel);
		return ret;
	}

}
