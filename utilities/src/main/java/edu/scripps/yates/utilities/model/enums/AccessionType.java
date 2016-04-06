package edu.scripps.yates.utilities.model.enums;

public enum AccessionType {
	IPI, UNIPROT, NCBI, UNKNOWN;

	public String value() {
		return name();
	}

	public static AccessionType fromValue(String v) {
		try {
			for (AccessionType accType : values()) {
				if (accType.name().equalsIgnoreCase(v))
					return accType;
			}
		} catch (Exception e) {
		}
		return UNKNOWN;
	}
}
