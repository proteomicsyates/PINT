package edu.scripps.yates.proteindb.persistence.mysql.access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;

public class CriterionSet {
	private final List<CriterionDescriptor> criterionDescriptors = new ArrayList<CriterionDescriptor>();

	public void addCriterion(String associationPath, Criterion criterion) {
		criterionDescriptors.add(new CriterionDescriptor(criterion, associationPath));
	}

	/**
	 * @return the criterionDescriptors
	 */
	public List<CriterionDescriptor> getCriterionDescriptors() {
		return criterionDescriptors;
	}

	public Map<String, List<CriterionDescriptor>> getCriterionByAssociationPath() {
		Map<String, List<CriterionDescriptor>> ret = new HashMap<String, List<CriterionDescriptor>>();
		for (CriterionDescriptor criterionDescriptor : criterionDescriptors) {
			if (ret.containsKey(criterionDescriptor.getAssociationPath())) {
				ret.get(criterionDescriptor.getAssociationPath()).add(criterionDescriptor);
			} else {
				List<CriterionDescriptor> list = new ArrayList<CriterionDescriptor>();
				list.add(criterionDescriptor);
				ret.put(criterionDescriptor.getAssociationPath(), list);
			}
		}
		return ret;
	}

	public void addCriterionSet(CriterionSet criterionSet2) {
		criterionDescriptors.addAll(criterionSet2.getCriterionDescriptors());
	}
}
