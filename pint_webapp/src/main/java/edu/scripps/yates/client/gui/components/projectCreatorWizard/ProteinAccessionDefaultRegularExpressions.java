package edu.scripps.yates.client.gui.components.projectCreatorWizard;

public enum ProteinAccessionDefaultRegularExpressions {
	UNIPROT("UniProt accession", ".*\\|(\\S+)\\|.*"), IPI("IPI accession",
			"(IPI\\S+)\\s+.+");

	private final String name;
	private final String regularExpressionString;

	private ProteinAccessionDefaultRegularExpressions(String name,
			String regexpString) {
		this.name = name;
		regularExpressionString = regexpString;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the regularExpressionString
	 */
	public String getRegularExpressionString() {
		return regularExpressionString;
	}
}
