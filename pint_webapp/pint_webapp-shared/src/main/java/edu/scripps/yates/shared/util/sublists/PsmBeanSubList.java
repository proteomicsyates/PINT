package edu.scripps.yates.shared.util.sublists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.PSMBeanLight;

public class PsmBeanSubList extends DataSubList<PSMBeanLight> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3642483558752708395L;

	public PsmBeanSubList() {

	}

	private PsmBeanSubList(List<PSMBeanLight> psms, int totalNumber) {
		super(psms, totalNumber);

	}

	public static PsmBeanSubList getLightPsmsBeanSubList(List<PSMBean> psms, int totalNumberOfItems) {

		final List<PSMBeanLight> lightPSMBeans = new ArrayList<PSMBeanLight>();
		for (final PSMBean psmBean : psms) {
			final PSMBeanLight clonedPsmBean = psmBean.cloneToLightPsmBean();
			lightPSMBeans.add(clonedPsmBean);

		}
		return new PsmBeanSubList(lightPSMBeans, totalNumberOfItems);
	}

}
