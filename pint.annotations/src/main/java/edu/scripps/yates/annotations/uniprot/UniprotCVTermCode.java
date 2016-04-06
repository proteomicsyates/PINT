package edu.scripps.yates.annotations.uniprot;

public enum UniprotCVTermCode {

	ID("Identifier (FT description)"), AC("Accession (PTM-xxxx)"), FT("Feature key"), TG("Target"), PA(
			"Position of the modification on the amino acid"), PP("Position of the modification in the polypeptide"), CF(
					"Correction formula"), MM("Monoisotopic mass difference"), MA("Average mass difference"), LC(
							"Cellular location"), TR("Taxonomic range"), KW("Keyword"), DR("Cross-reference to PTM databases");

	private final String content;
	public final static String ENTRY_END = "//";
	public final static String CODE_VALUE_SPLIT_VALUE = "   ";

	private UniprotCVTermCode(String content) {
		this.content = content;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

}
