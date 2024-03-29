package edu.scripps.yates.server.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.excel.proteindb.importcfg.util.ImportCfgUtil;
import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.PrincipalInvestigatorBean;
import edu.scripps.yates.shared.model.ProjectBean;
import gnu.trove.map.hash.TIntObjectHashMap;

public class ProjectBeanAdapter implements Adapter<ProjectBean> {
	private final Project project;
	private final boolean mapTables;
	private final boolean includePeptides;
	private static ThreadLocal<TIntObjectHashMap<ProjectBean>> map = new ThreadLocal<TIntObjectHashMap<ProjectBean>>();

	public ProjectBeanAdapter(Project project, boolean mapTables, boolean includePeptides) {
		this.project = project;
		this.mapTables = mapTables;
		this.includePeptides = includePeptides;
		initializeMap();
	}

	private void initializeMap() {
		if (map.get() == null) {
			map.set(new TIntObjectHashMap<ProjectBean>());
		}
	}

	@Override
	public ProjectBean adapt() {
		if (map.get().containsKey(project.getId()))
			return map.get().get(project.getId());
		final ProjectBean ret = new ProjectBean();
		map.get().put(project.getId(), ret);
		ret.setDescription(project.getDescription());
		ret.setName(project.getName());
		ret.setPubmedLink(project.getPubmedLink());
		ret.setReleaseDate(project.getReleaseDate());
		ret.setUploadedDate(project.getUploadedDate());
		ret.setIsHidden(project.isHidden());
		ret.setTag(project.getTag());
		ret.setDbId(project.getId());
		ret.setPublicAvailable(!project.isPrivate_());
		ret.setBig(project.isBig());
		final Set<Condition> conditions = project.getConditions();
		if (conditions != null) {
			for (final Condition condition : conditions) {
				ret.getConditions().add(new ConditionBeanAdapter(condition, false, includePeptides).adapt());
			}

		}
		final Set<MsRun> msRuns = project.getMsRuns();
		if (msRuns != null) {
			for (final MsRun msRun : msRuns) {
				ret.getMsRuns().add(new MSRunBeanAdapter(msRun, mapTables).adapt());
			}

		}
		if (project.getPi() != null && !"".equals(project.getPi())) {
			if (project.getPi().contains(ImportCfgUtil.PI_SEPARATOR)) {
				final String[] split = project.getPi().split(ImportCfgUtil.PI_SEPARATOR);
				final PrincipalInvestigatorBean principalInvestigator = new PrincipalInvestigatorBean();
				principalInvestigator.setName(split[0]);
				principalInvestigator.setEmail(split[1]);
				principalInvestigator.setInstitution(split[2]);
				principalInvestigator.setCountry(split[3]);
				ret.setPrincipalInvestigator(principalInvestigator);
			} else {
				final PrincipalInvestigatorBean principalInvestigator = new PrincipalInvestigatorBean();
				principalInvestigator.setName(project.getPi());
				ret.setPrincipalInvestigator(principalInvestigator);
			}
		}
		if (project.getInstruments() != null && !"".equals(project.getInstruments())) {
			final List<String> instruments = new ArrayList<String>();
			if (project.getInstruments().contains(ImportCfgUtil.PI_SEPARATOR)) {
				final String[] split = project.getInstruments().split(ImportCfgUtil.PI_SEPARATOR);
				for (final String string : split) {
					instruments.add(string);
				}
			} else {
				instruments.add(project.getInstruments());
			}
			ret.setInstruments(instruments);
		}
		return ret;
	}

	public static void clearStaticMap() {
		if (map.get() != null) {
			map.get().clear();
		}
	}
}
