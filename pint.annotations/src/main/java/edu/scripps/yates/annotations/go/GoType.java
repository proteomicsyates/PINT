package edu.scripps.yates.annotations.go;

public enum GoType {
	FUNCTION("Function"), COMPONENT("Component"), PROCESS("Process");
	private final String name;

	private GoType(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public static GoType getByName(String name) {
		for (GoType goType : values()) {
			if (goType.getName().equals(name)) {
				return goType;
			}
		}
		return null;
	}
}
