package edu.scripps.yates.utilities.model.enums;

public enum AggregationLevel {
	PSM, PEPTIDE, PROTEIN, PROTEINGROUP;

	public static String getValuesString() {
		StringBuilder sb = new StringBuilder();
		for (AggregationLevel level : values()) {
			if (!"".equals(sb.toString()))
				sb.append(",");
			sb.append(level.name());
		}
		return sb.toString();
	}

	public static AggregationLevel getAggregationLevelByString(String aggregationLevelString) {
		final AggregationLevel[] values = AggregationLevel.values();
		for (AggregationLevel aggregationLevel : values) {
			if (aggregationLevel.name().toLowerCase().equals(aggregationLevelString.toLowerCase()))
				return aggregationLevel;
		}
		return null;
	}
}
