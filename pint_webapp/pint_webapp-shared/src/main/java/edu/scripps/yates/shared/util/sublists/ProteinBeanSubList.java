package edu.scripps.yates.shared.util.sublists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.shared.model.ProteinBean;

public class ProteinBeanSubList extends DataSubList<ProteinBean> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3642483558752708395L;

	public ProteinBeanSubList() {

	}

	public ProteinBeanSubList(List<ProteinBean> proteins, int totalNumber) {
		super(proteins, totalNumber);
	}

	public static ProteinBeanSubList getLightProteinBeanSubList(List<ProteinBean> proteins, int totalNumberOfItems) {

		final List<ProteinBean> clonedProteins = new ArrayList<ProteinBean>();
		for (final ProteinBean proteinBean : proteins) {
			final ProteinBean clonedProtein = proteinBean.cloneToLightProteinBean();
			clonedProtein.getDbIds().clear();
			clonedProtein.getPeptideDBIds().clear();
			clonedProtein.getPeptideDBIdsByCondition().clear();
			clonedProteins.add(clonedProtein);

		}
		return new ProteinBeanSubList(clonedProteins, totalNumberOfItems);
	}

	public static ProteinBeanSubList getLightProteinBeanSubListFromLightProteins(List<ProteinBean> lightProteins,
			int totalNumberOfItems) {

		return new ProteinBeanSubList(lightProteins, totalNumberOfItems);
	}
}
