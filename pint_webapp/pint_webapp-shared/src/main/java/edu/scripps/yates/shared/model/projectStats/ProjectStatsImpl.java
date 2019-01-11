package edu.scripps.yates.shared.model.projectStats;

public class ProjectStatsImpl<T> extends ProjectStats<T> {

	/**
	 *
	 */
	private static final long serialVersionUID = 5034052715142196111L;
	private final ProjectStatsType type;

	public ProjectStatsImpl(T t, ProjectStatsType type) {
		super(t);
		this.type = type;
	}

	@Override
	protected ProjectStatsType getType() {
		return type;
	}

}
