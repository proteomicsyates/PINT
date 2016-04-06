package edu.scripps.yates.shared.util.sublists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.shared.model.ProteinGroupBean;

public class ProteinGroupBeanSubList extends DataSubList<ProteinGroupBean>implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2532708329084447068L;

	public ProteinGroupBeanSubList() {

	}

	private ProteinGroupBeanSubList(List<ProteinGroupBean> proteinGroups, int totalNumber) {
		super(proteinGroups, totalNumber);
	}

	public static ProteinGroupBeanSubList getLightProteinGroupBeanSubList(List<ProteinGroupBean> proteinGroups,
			int totalNumberOfItems) {
		List<ProteinGroupBean> clonedProteinGroups = new ArrayList<ProteinGroupBean>();
		for (ProteinGroupBean proteinGroupBean : proteinGroups) {
			ProteinGroupBean clonedProteinGroup = proteinGroupBean.cloneToLightProteinGroupBean();
			clonedProteinGroups.add(clonedProteinGroup);

		}
		return new ProteinGroupBeanSubList(clonedProteinGroups, totalNumberOfItems);
	}

}
