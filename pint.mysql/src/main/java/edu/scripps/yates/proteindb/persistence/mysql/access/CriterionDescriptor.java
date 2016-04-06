package edu.scripps.yates.proteindb.persistence.mysql.access;

import org.hibernate.criterion.Criterion;

public class CriterionDescriptor {
	private final Criterion criterion;
	private final String rootClass;
	private final String associationPath;

	public CriterionDescriptor(Criterion criterion, String associationPath) {
		super();
		this.criterion = criterion;
		this.associationPath = associationPath;
		rootClass = getRootClassFromAssociationPath(associationPath);

	}

	public static String getAssociatedClassFromAssociationPath(String associationPath) {
		String rootClass = null;
		if (associationPath.contains(".")) {
			rootClass = associationPath.substring(associationPath.lastIndexOf(".") + 1);
		} else {
			rootClass = associationPath;
		}
		return rootClass;
	}

	public static String getRootClassFromAssociationPath(String associationPath) {
		String rootClass = null;
		if (associationPath.contains(".")) {
			rootClass = associationPath.substring(0, associationPath.indexOf("."));
		} else {
			rootClass = associationPath;
		}
		return rootClass;
	}

	/**
	 * @return the criterion
	 */
	public Criterion getCriterion() {
		return criterion;
	}

	/**
	 * @return the rootClass
	 */
	public String getRootClass() {
		return rootClass;
	}

	/**
	 * @return the associationPath
	 */
	public String getAssociationPath() {
		return associationPath;
	}

}
