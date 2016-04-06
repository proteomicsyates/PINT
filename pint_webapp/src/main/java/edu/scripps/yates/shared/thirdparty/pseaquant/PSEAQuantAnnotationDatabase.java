package edu.scripps.yates.shared.thirdparty.pseaquant;

public enum PSEAQuantAnnotationDatabase {

	GO("GO", "Gene Ontology"), MSIG_DB("MSigDB",
			"Molecular Signature Database (can only be used when h. sapiens protein symbols are provided)"), COPAKB(
					"COPaKB",
					"Cardiac Organellar Protein Atlas Knowledgebase (COPaKB) (can only be used when h. sapiens protein symbols are provived)");
	private final String annotationDBName;
	private final String text;

	private PSEAQuantAnnotationDatabase(String organismName, String text) {
		annotationDBName = organismName;
		this.text = text;
	}

	public String getText() {
		return text;
	}

	/**
	 * @return the annotationDBName
	 */
	public String getAnnotationDBName() {
		return annotationDBName;
	}

	public static String getParameterName() {
		return "annotation";
	}

	public static PSEAQuantAnnotationDatabase getByAnnotationDBName(String dbName) {
		for (PSEAQuantAnnotationDatabase db : PSEAQuantAnnotationDatabase.values()) {
			if (db.getAnnotationDBName().equals(dbName)) {
				return db;
			}
		}
		return null;
	}
}
