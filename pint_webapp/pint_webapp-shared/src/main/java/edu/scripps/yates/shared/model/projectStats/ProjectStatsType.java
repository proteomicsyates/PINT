package edu.scripps.yates.shared.model.projectStats;

public enum ProjectStatsType {
	PINT("PINT_STATS"), MSRUN("MSRUN_STATS"), PROJECT("PROJECT_STATS"), SAMPLE("SAMPLE_STATS"), CONDITION(
			"CONDITION_STATS");
	private final String description;

	ProjectStatsType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
