package edu.scripps.yates.proteindb.persistence.mysql.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.Project;

public class ProjectImpl implements Project {
	private final edu.scripps.yates.proteindb.persistence.mysql.Project hibProject;
	protected final static HashMap<Integer, Project> projectsMap = new HashMap<Integer, Project>();

	public ProjectImpl(edu.scripps.yates.proteindb.persistence.mysql.Project project) {
		hibProject = project;
		projectsMap.put(project.getId(), this);
	}

	@Override
	public String getDescription() {
		return hibProject.getDescription();
	}

	@Override
	public String getName() {
		return hibProject.getName();
	}

	@Override
	public Date getReleaseDate() {
		return hibProject.getReleaseDate();
	}

	@Override
	public URL getPubmedLink() {
		try {
			if (hibProject.getPubmedLink() != null)
				return new URL(hibProject.getPubmedLink());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Set<Condition> getConditions() {
		Set<Condition> ret = new HashSet<Condition>();
		final Set<edu.scripps.yates.proteindb.persistence.mysql.Condition> conditions = hibProject.getConditions();
		for (edu.scripps.yates.proteindb.persistence.mysql.Condition condition : conditions) {
			if (ConditionImpl.conditionsMap.containsKey(condition.getId()))
				ret.add(ConditionImpl.conditionsMap.get(condition.getId()));
			else {
				final ConditionImpl conditionImpl = new ConditionImpl(condition);
				ConditionImpl.conditionsMap.put(condition.getId(), conditionImpl);
				ret.add(conditionImpl);
			}
		}
		return ret;
	}

	@Override
	public boolean isPrivate() {
		return hibProject.isPrivate_();
	}

	@Override
	public String getTag() {
		return hibProject.getTag();
	}

	@Override
	public Date getUploadedDate() {
		return hibProject.getUploadedDate();
	}

	@Override
	public Set<MSRun> getMSRuns() {
		Set<MSRun> ret = new HashSet<MSRun>();
		final Set<MsRun> msRuns = hibProject.getMsRuns();
		if (msRuns != null) {
			for (MsRun hibMsRun : msRuns) {
				MSRun msRun = null;
				if (MSRunImpl.msRuns.containsKey(hibMsRun.getId())) {
					msRun = MSRunImpl.msRuns.get(hibMsRun.getId());
				} else {
					msRun = new MSRunImpl(hibMsRun);
				}
				ret.add(msRun);
			}
		}
		return ret;
	}

	@Override
	public boolean isBig() {
		return hibProject.isBig();
	}
}
