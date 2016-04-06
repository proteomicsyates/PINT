package edu.scripps.yates.shared.model;

public enum SharedAggregationLevel {
	PSM, PEPTIDE, PROTEIN, PROTEINGROUP;

	public static String getValuesString() {
		StringBuilder sb = new StringBuilder();
		for (SharedAggregationLevel level : values()) {
			if (!"".equals(sb.toString()))
				sb.append(",");
			sb.append(level.name());
		}
		return sb.toString();
	}

	public static SharedAggregationLevel getAggregationLevelByString(String aggregationLevelString) {
		final SharedAggregationLevel[] values = SharedAggregationLevel.values();
		for (SharedAggregationLevel aggregationLevel : values) {
			if (aggregationLevel.name().toLowerCase().equals(aggregationLevelString.toLowerCase()))
				return aggregationLevel;
		}
		return null;
	}
}
