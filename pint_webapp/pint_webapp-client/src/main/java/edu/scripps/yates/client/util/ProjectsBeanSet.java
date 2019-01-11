package edu.scripps.yates.client.util;

import java.util.HashSet;

import edu.scripps.yates.shared.model.ProjectBean;

public class ProjectsBeanSet extends HashSet<ProjectBean> {
	/**
	 *
	 */
	private static final long serialVersionUID = -8607110413169328450L;

	public boolean containsProject(String projectTag) {
		for (final ProjectBean projectBean : this) {
			if (projectBean.getTag().equals(projectTag)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsBigProject() {
		for (final ProjectBean projectBean : this) {
			if (projectBean.isBig()) {
				return true;
			}
		}
		return false;
	}

	public boolean containsPrivateProject() {
		for (final ProjectBean projectBean : this) {
			if (!projectBean.isPublicAvailable()) {
				return true;
			}
		}
		return false;
	}

	public ProjectBean getBigProject() {
		for (final ProjectBean projectBean : this) {
			if (projectBean.isBig()) {
				return projectBean;
			}
		}
		return null;
	}

	public ProjectBean getByTag(String projectTag) {
		for (final ProjectBean projectBean : this) {
			if (projectBean.getTag().equals(projectTag)) {
				return projectBean;
			}
		}
		return null;
	}
}
