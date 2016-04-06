package edu.scripps.yates.utilities.model.enums;

public enum CombinationType {
	AVERAGE("average", "the average between the values"), SUM("sum",
			"the sum between the values");
	private final String name;
	private final String description;

	private CombinationType(String name, String description) {
		this.name = name;
		this.description = description;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	public static CombinationType getCombinationType(String name) {
		final CombinationType[] values = CombinationType.values();
		for (CombinationType combinationType : values) {
			if (combinationType.name.equalsIgnoreCase(name)
					|| combinationType.description.equalsIgnoreCase(name))
				return combinationType;
		}
		return null;
	}
}
