package edu.scripps.yates.server.adapters;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.ConditionToPeptideTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.ConditionToProteinTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.PeptideAmountToPeptideTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.PeptideRatioToPeptideTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.ProteinAmountToProteinTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.ProteinRatioToProteinTableMapper;
import edu.scripps.yates.shared.model.ExperimentalConditionBean;
import edu.scripps.yates.shared.model.ProjectBean;
import gnu.trove.map.hash.TIntObjectHashMap;

public class ConditionBeanAdapter implements Adapter<ExperimentalConditionBean> {
	private final Condition experimentalCondition;
	private static ThreadLocal<TIntObjectHashMap<ExperimentalConditionBean>> map = new ThreadLocal<TIntObjectHashMap<ExperimentalConditionBean>>();
	private final boolean mapTables;
	private final boolean includePeptides;
	private final static Logger log = Logger.getLogger(ConditionBeanAdapter.class);

	public ConditionBeanAdapter(Condition experimentalCondition, boolean mapTables, boolean includePeptides) {
		this.experimentalCondition = experimentalCondition;
		this.mapTables = mapTables;
		this.includePeptides = includePeptides;
		initializeMap();
	}

	private static void initializeMap() {
		if (map.get() == null) {
			map.set(new TIntObjectHashMap<ExperimentalConditionBean>());
		}
	}

	public static ExperimentalConditionBean getBeanByConditionID(int conditionID) {
		initializeMap();
		return map.get().get(conditionID);
	}

	@Override
	public ExperimentalConditionBean adapt() {
		if (map.get().containsKey(experimentalCondition.getId())) {
			if (mapTables) {
				ConditionToProteinTableMapper.getInstance().addObject1(experimentalCondition);
				ProteinRatioToProteinTableMapper.getInstance().addCondition(experimentalCondition);
				ProteinAmountToProteinTableMapper.getInstance().addCondition(experimentalCondition);
				if (includePeptides) {
					ConditionToPeptideTableMapper.getInstance().addObject1(experimentalCondition);
					PeptideRatioToPeptideTableMapper.getInstance().addCondition(experimentalCondition);
					PeptideAmountToPeptideTableMapper.getInstance().addCondition(experimentalCondition);
				}
			}
			final ExperimentalConditionBean experimentalConditionBean = map.get().get(experimentalCondition.getId());
			return experimentalConditionBean;
		}
		final ExperimentalConditionBean ret = new ExperimentalConditionBean();
		map.get().put(experimentalCondition.getId(), ret);
		ret.setDescription(experimentalCondition.getDescription());
		ret.setName(experimentalCondition.getName());
		final ProjectBean project = new ProjectBeanAdapter(experimentalCondition.getProject(), mapTables,
				includePeptides).adapt();
		ret.setProject(project);
		ret.setSample(new SampleBeanAdapter(experimentalCondition.getSample(), project).adapt());
		ret.setUnit(experimentalCondition.getUnit());
		ret.setValue(experimentalCondition.getValue());

		// map this condition to all proteins so that the query
		// "getConditionsFromProtein" it is not done more times

		if (mapTables) {
			ConditionToProteinTableMapper.getInstance().addObject1(experimentalCondition);
			ProteinRatioToProteinTableMapper.getInstance().addCondition(experimentalCondition);
			ProteinAmountToProteinTableMapper.getInstance().addCondition(experimentalCondition);
			if (includePeptides) {
				ConditionToPeptideTableMapper.getInstance().addObject1(experimentalCondition);
				PeptideRatioToPeptideTableMapper.getInstance().addCondition(experimentalCondition);
				PeptideAmountToPeptideTableMapper.getInstance().addCondition(experimentalCondition);
			}
		}
		return ret;
	}

	public static void clearStaticMap() {
		if (map.get() != null) {
			map.get().clear();
		}
	}
}
