package edu.scripps.yates.server.adapters;

import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.ProjectBean;
import gnu.trove.map.hash.TIntObjectHashMap;

public class ProjectBeanAdapter implements Adapter<ProjectBean> {
	private final Project project;
	private final boolean mapTables;
	private static ThreadLocal<TIntObjectHashMap<ProjectBean>> map = new ThreadLocal<TIntObjectHashMap<ProjectBean>>();

	public ProjectBeanAdapter(Project project, boolean mapTables) {
		this.project = project;
		this.mapTables = mapTables;
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
				ret.getConditions().add(new ConditionBeanAdapter(condition, false).adapt());
			}

		}
		final Set<MsRun> msRuns = project.getMsRuns();
		if (msRuns != null) {
			for (final MsRun msRun : msRuns) {
				ret.getMsRuns().add(new MSRunBeanAdapter(msRun, mapTables).adapt());
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
