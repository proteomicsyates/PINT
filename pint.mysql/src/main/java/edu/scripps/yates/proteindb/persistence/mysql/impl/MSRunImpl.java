package edu.scripps.yates.proteindb.persistence.mysql.impl;

import java.util.Date;

import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import gnu.trove.map.hash.TIntObjectHashMap;

public class MSRunImpl implements MSRun {
	private final MsRun hibMSRun;
	protected static TIntObjectHashMap<MSRun> msRuns = new TIntObjectHashMap<MSRun>();

	public MSRunImpl(MsRun hibMSRun) {
		this.hibMSRun = hibMSRun;
		msRuns.put(hibMSRun.getId(), this);
	}

	@Override
	public String getRunId() {
		return hibMSRun.getRunId();
	}

	@Override
	public String getPath() {
		return hibMSRun.getPath();
	}

	@Override
	public Date getDate() {
		return hibMSRun.getDate();
	}

	@Override
	public int getDBId() {
		if (hibMSRun.getId() != null)
			return hibMSRun.getId();
		return -1;
	}

	@Override
	public Project getProject() {
		if (ProjectImpl.projectsMap.containsKey(hibMSRun.getProject().getId())) {
			return ProjectImpl.projectsMap.get(hibMSRun.getProject().getId());
		} else {
			Project project = new ProjectImpl(hibMSRun.getProject());
			return project;
		}
	}

}
