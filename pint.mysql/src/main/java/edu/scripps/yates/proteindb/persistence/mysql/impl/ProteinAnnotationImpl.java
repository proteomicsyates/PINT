package edu.scripps.yates.proteindb.persistence.mysql.impl;

import java.util.HashMap;

import edu.scripps.yates.utilities.proteomicsmodel.AnnotationType;
import edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation;

public class ProteinAnnotationImpl implements ProteinAnnotation {
	private final edu.scripps.yates.proteindb.persistence.mysql.ProteinAnnotation hibProteinAnnotation;
	private AnnotationType annotationType;
	protected final static HashMap<Integer, ProteinAnnotation> proteinAnnotationsMap = new HashMap<Integer, ProteinAnnotation>();

	public ProteinAnnotationImpl(
			edu.scripps.yates.proteindb.persistence.mysql.ProteinAnnotation proteinAnnotation) {
		hibProteinAnnotation = proteinAnnotation;
		proteinAnnotationsMap.put(proteinAnnotation.getId(), this);
	}

	@Override
	public AnnotationType getAnnotationType() {
		if (annotationType == null) {
			final edu.scripps.yates.proteindb.persistence.mysql.AnnotationType annotationType = hibProteinAnnotation
					.getAnnotationType();
			if (annotationType != null) {
				this.annotationType = AnnotationType
						.translateStringToAnnotationType(annotationType
								.getName());
			}
		}
		return annotationType;
	}

	@Override
	public String getSource() {
		return hibProteinAnnotation.getSource();
	}

	@Override
	public String getValue() {
		return hibProteinAnnotation.getValue();
	}

	@Override
	public String getName() {
		return hibProteinAnnotation.getName();
	}

}
