package edu.scripps.yates.shared.util.sublists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.shared.model.PSMBean;

public class PsmBeanSubList extends DataSubList<PSMBean>implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3642483558752708395L;

	public PsmBeanSubList() {

	}

	private PsmBeanSubList(List<PSMBean> psms, int totalNumber) {
		super(psms, totalNumber);

	}

	public static PsmBeanSubList getLightPsmsBeanSubList(List<PSMBean> psms, int totalNumberOfItems) {

		List<PSMBean> clonedPSMBeans = new ArrayList<PSMBean>();
		for (PSMBean psmBean : psms) {
			PSMBean clonedPsmBean = psmBean.cloneToLightPsmBean();
			clonedPSMBeans.add(clonedPsmBean);

		}
		return new PsmBeanSubList(clonedPSMBeans, totalNumberOfItems);
	}

}
