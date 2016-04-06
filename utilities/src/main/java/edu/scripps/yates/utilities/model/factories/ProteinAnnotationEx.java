package edu.scripps.yates.utilities.model.factories;

import java.io.Serializable;

import edu.scripps.yates.utilities.proteomicsmodel.AnnotationType;
import edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation;

public class ProteinAnnotationEx implements ProteinAnnotation, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 5494073457435018849L;
	private final String value;
	private final String name;
	/**
	 * description of the source of the annotation, i.e. GO , genemania...manual
	 * annotation...
	 */
	private String source;
	/**
	 * DISEASE_ CATALYTIC_ACTIVITY FUNCTION ... all the column names like
	 * "uniprot_annotation::General annotation _FUNCTION_"
	 */
	private final AnnotationType annotationType;

	/**
	 * Constructor of a {@link ProteinAnnotation} giving an
	 * {@link AnnotationType}, a name and a value
	 *
	 * @param annotationType
	 * @param name
	 * @param value
	 */
	public ProteinAnnotationEx(AnnotationType annotationType, String name, String value) {

		if (annotationType == null || name == null || "".equals(name))
			throw new IllegalArgumentException("annotation cannot be null");
		this.annotationType = annotationType;
		if (!"".equals(value))
			this.value = value;
		else
			this.value = null;
		this.name = name;
	}

	/**
	 * Constructor of a {@link ProteinAnnotation} giving an
	 * {@link AnnotationType} and a name.<br>
	 * In this case, the value will be null
	 *
	 * @param annotationType
	 * @param name
	 */
	public ProteinAnnotationEx(AnnotationType annotationType, String name) {
		this(annotationType, name, null);
	}

	/**
	 * @return the annotationType
	 */
	@Override
	public AnnotationType getAnnotationType() {
		return annotationType;
	}

	/**
	 * @return the source
	 */
	@Override
	public String getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the annotation
	 */
	@Override
	public String getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProteinAnnotation [name=" + name + ", value=" + value + ", source=" + source + ", annotationType="
				+ annotationType + "]";
	}

	@Override
	public String getName() {
		return name;
	}

}
