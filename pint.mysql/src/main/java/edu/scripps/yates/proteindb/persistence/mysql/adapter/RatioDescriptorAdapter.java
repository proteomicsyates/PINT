package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.proteindb.persistence.mysql.PeptideRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.PsmRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.RatioDescriptor;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;

public class RatioDescriptorAdapter implements
		Adapter<edu.scripps.yates.proteindb.persistence.mysql.RatioDescriptor>,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5973374111298264539L;
	private final String description;
	private final Condition condition1;
	private final Condition condition2;
	private final ProteinRatioValue hibProteinRatioValue;
	private final Project hibProject;
	private final PeptideRatioValue hibPeptideRatioValue;
	private final PsmRatioValue hibPsmRatioValue;
	private final static Map<String, RatioDescriptor> map = new HashMap<String, RatioDescriptor>();

	public RatioDescriptorAdapter(String description, Condition condition1,
			Condition condition2, ProteinRatioValue hibProteinRatioValue,
			Project hibProject) {
		this.description = description;
		this.condition1 = condition1;
		this.condition2 = condition2;
		hibPeptideRatioValue = null;
		hibPsmRatioValue = null;
		this.hibProteinRatioValue = hibProteinRatioValue;
		this.hibProject = hibProject;
	}

	public RatioDescriptorAdapter(String description, Condition condition1,
			Condition condition2, PeptideRatioValue hibPeptideRatioValue,
			Project hibProject) {
		this.description = description;
		this.condition1 = condition1;
		this.condition2 = condition2;
		this.hibPeptideRatioValue = hibPeptideRatioValue;
		hibPsmRatioValue = null;
		hibProteinRatioValue = null;
		this.hibProject = hibProject;
	}

	public RatioDescriptorAdapter(String description, Condition condition1,
			Condition condition2, PsmRatioValue hibPsmRatioValue,
			Project hibProject) {
		this.description = description;
		this.condition1 = condition1;
		this.condition2 = condition2;
		this.hibPsmRatioValue = hibPsmRatioValue;
		hibPeptideRatioValue = null;
		hibProteinRatioValue = null;
		this.hibProject = hibProject;
	}

	@Override
	public RatioDescriptor adapt() {
		RatioDescriptor ret = null;
		String key = getKey(description, condition1, condition2);
		if (map.containsKey(key)) {
			final RatioDescriptor ratioDescriptor = map.get(key);
			if (hibProteinRatioValue != null)
				ratioDescriptor.getProteinRatioValues().add(
						hibProteinRatioValue);
			if (hibPeptideRatioValue != null)
				ratioDescriptor.getPeptideRatioValues().add(
						hibPeptideRatioValue);
			if (hibPsmRatioValue != null)
				ratioDescriptor.getPsmRatioValues().add(hibPsmRatioValue);
			return ratioDescriptor;
		}
		ret = new RatioDescriptor();
		map.put(key, ret);

		// condition 1
		edu.scripps.yates.proteindb.persistence.mysql.Condition hibCondition1 = ConditionAdapter.map
				.get(condition1.hashCode());
		if (hibCondition1 == null) {
			hibCondition1 = new ConditionAdapter(condition1, hibProject)
					.adapt();
			ConditionAdapter.map.put(condition1.hashCode(), hibCondition1);
		}
		// condition 2
		edu.scripps.yates.proteindb.persistence.mysql.Condition hibCondition2 = ConditionAdapter.map
				.get(condition2.hashCode());
		if (hibCondition2 == null) {
			hibCondition2 = new ConditionAdapter(condition2, hibProject)
					.adapt();
			ConditionAdapter.map.put(condition2.hashCode(), hibCondition2);
		}
		ret.setConditionByExperimentalCondition1Id(hibCondition1);
		ret.setConditionByExperimentalCondition2Id(hibCondition2);
		ret.setDescription(description);

		if (hibProteinRatioValue != null)
			ret.getProteinRatioValues().add(hibProteinRatioValue);
		if (hibPeptideRatioValue != null)
			ret.getPeptideRatioValues().add(hibPeptideRatioValue);
		if (hibPsmRatioValue != null)
			ret.getPsmRatioValues().add(hibPsmRatioValue);
		hibCondition1.getRatioDescriptorsForExperimentalCondition1Id().add(ret);
		hibCondition2.getRatioDescriptorsForExperimentalCondition2Id().add(ret);
		return ret;
	}

	private String getKey(String description, Condition condition1,
			Condition condition2) {
		return description + condition1.getName() + condition2.getName();
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
