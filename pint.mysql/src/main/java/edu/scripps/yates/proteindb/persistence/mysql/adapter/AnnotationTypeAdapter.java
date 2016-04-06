package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.proteindb.persistence.mysql.AnnotationType;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAnnotation;

public class AnnotationTypeAdapter implements
		Adapter<edu.scripps.yates.proteindb.persistence.mysql.AnnotationType>,
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 832232304738325346L;
	private final edu.scripps.yates.utilities.proteomicsmodel.AnnotationType annotationType;
	private final edu.scripps.yates.proteindb.persistence.mysql.ProteinAnnotation hibProteinAnnotation;
	private final static Map<String, edu.scripps.yates.proteindb.persistence.mysql.AnnotationType> map = new HashMap<String, edu.scripps.yates.proteindb.persistence.mysql.AnnotationType>();

	public AnnotationTypeAdapter(
			edu.scripps.yates.utilities.proteomicsmodel.AnnotationType annotationType,
			ProteinAnnotation hibProteinAnnotation) {
		this.annotationType = annotationType;
		this.hibProteinAnnotation = hibProteinAnnotation;
	}

	@Override
	public edu.scripps.yates.proteindb.persistence.mysql.AnnotationType adapt() {
		AnnotationType ret = new AnnotationType();
		if (map.containsKey(annotationType.getKey()))
			return map.get(annotationType.getKey());
		map.put(annotationType.getKey(), ret);
		ret.setName(annotationType.getKey());
		ret.getProteinAnnotations().add(hibProteinAnnotation);

		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
