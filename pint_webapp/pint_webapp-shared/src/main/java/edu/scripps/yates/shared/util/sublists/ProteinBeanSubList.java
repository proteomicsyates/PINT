package edu.scripps.yates.shared.util.sublists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.light.ProteinBeanLight;

public class ProteinBeanSubList extends DataSubList<ProteinBeanLight> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3642483558752708395L;

	public ProteinBeanSubList() {

	}

	private ProteinBeanSubList(List<ProteinBeanLight> proteins, int totalNumber) {
		super(proteins, totalNumber);
	}

	public static ProteinBeanSubList getLightProteinBeanSubList(List<ProteinBean> proteins, int totalNumberOfItems) {

		final List<ProteinBeanLight> lightProteinBeans = new ArrayList<ProteinBeanLight>();
		for (final ProteinBean proteinBean : proteins) {
			final ProteinBeanLight clonedProtein = proteinBean.cloneToLightProteinBean();
			clonedProtein.getPeptides().clear();
			lightProteinBeans.add(clonedProtein);

		}
		return new ProteinBeanSubList(lightProteinBeans, totalNumberOfItems);
	}

	public static ProteinBeanSubList getLightProteinBeanSubListFromLightProteins(List<ProteinBeanLight> lightProteins,
			int totalNumberOfItems) {

		return new ProteinBeanSubList(lightProteins, totalNumberOfItems);
	}
}
