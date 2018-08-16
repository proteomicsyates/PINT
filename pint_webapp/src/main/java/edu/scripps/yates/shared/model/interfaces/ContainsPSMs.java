package edu.scripps.yates.shared.model.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.model.ExperimentalConditionBean;
import edu.scripps.yates.shared.model.PSMBean;

public interface ContainsPSMs {
	public List<PSMBean> getPsms();

	public Set<Integer> getPSMDBIds();

	public Map<ExperimentalConditionBean, Set<Integer>> getPSMDBIdsByCondition();

	public Map<ExperimentalConditionBean, Integer> getNumPSMsByCondition();

	public int getNumPSMsByCondition(String projectTag, String conditionName);

}
