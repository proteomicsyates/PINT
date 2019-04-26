package edu.scripps.yates.shared.model.interfaces;

import java.util.List;
import java.util.Map;

import edu.scripps.yates.shared.model.ExperimentalConditionBean;
import edu.scripps.yates.shared.model.PSMBeanLight;

public interface ContainsLightPSMs {
	public List<PSMBeanLight> getPsms();

	public int getNumPSMs();

	public Map<ExperimentalConditionBean, Integer> getNumPSMsByCondition();

	public int getNumPSMsByCondition(String projectTag, String conditionName);

}
