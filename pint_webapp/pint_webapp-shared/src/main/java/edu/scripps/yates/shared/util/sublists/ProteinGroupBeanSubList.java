package edu.scripps.yates.shared.util.sublists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.model.light.ProteinGroupBeanLight;

public class ProteinGroupBeanSubList extends DataSubList<ProteinGroupBeanLight> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2532708329084447068L;

	public ProteinGroupBeanSubList() {

	}

	private ProteinGroupBeanSubList(List<ProteinGroupBeanLight> proteinGroups, int totalNumber) {
		super(proteinGroups, totalNumber);
	}

	public static ProteinGroupBeanSubList getLightProteinGroupBeanSubList(List<ProteinGroupBean> proteinGroups,
			int totalNumberOfItems) {
		final List<ProteinGroupBeanLight> lightProteinGroups = new ArrayList<ProteinGroupBeanLight>();
		for (final ProteinGroupBean proteinGroupBean : proteinGroups) {
			final ProteinGroupBeanLight clonedProteinGroup = proteinGroupBean.cloneToLightProteinGroupBean();
			lightProteinGroups.add(clonedProteinGroup);

		}
		return new ProteinGroupBeanSubList(lightProteinGroups, totalNumberOfItems);
	}

}
