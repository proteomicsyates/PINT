package edu.scripps.yates.shared.model;

import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.shared.exceptions.PintRuntimeException;

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
		throw new PintRuntimeException("The value '" + aggregationLevelString + "' is not supported as a valid "
				+ SharedAggregationLevel.class.getCanonicalName(), PINT_ERROR_TYPE.VALUE_NOT_SUPPORTED);
	}
}
