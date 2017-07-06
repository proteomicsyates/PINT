package edu.scripps.yates.annotations.uniprot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.annotations.util.PropertiesUtil;
import edu.scripps.yates.utilities.proteomicsmodel.AnnotationType;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class ProteinAnnotationIndex {
	private static final int MINIMUM_LENGTH = Integer.valueOf(PropertiesUtil
			.getInstance(PropertiesUtil.UNIPROT_PROPERTIES_FILE).getPropertyValue(PropertiesUtil.MINIMUM_LENGHT_PROP));
	private static ProteinAnnotationIndex instance;
	private final Map<AnnotationType, Set<Protein>> proteinsByAnnotationType = new THashMap<AnnotationType, Set<Protein>>();
	private final Map<String, Set<Protein>> proteinsByAnnotationName = new THashMap<String, Set<Protein>>();
	private final Map<String, Set<Protein>> proteinsByAnnotationValue = new THashMap<String, Set<Protein>>();

	private enum StringComparisonType {
		EXACT, CONTAINS
	};

	public static ProteinAnnotationIndex getInstance() {
		if (ProteinAnnotationIndex.instance == null)
			ProteinAnnotationIndex.instance = new ProteinAnnotationIndex();
		return ProteinAnnotationIndex.instance;
	}

	public void index(HashMap<String, Protein> proteins) {
		for (String proteinACC : proteins.keySet()) {
			Protein protein = proteins.get(proteinACC);
			final Set<ProteinAnnotation> proteinAnnotations = protein.getAnnotations();
			if (proteinAnnotations != null) {
				for (ProteinAnnotation proteinAnnotation : proteinAnnotations) {
					// index NAME
					final String name = proteinAnnotation.getName();
					if (name != null && "".equals(name)) {
						if (proteinsByAnnotationName.containsKey(name)) {
							proteinsByAnnotationName.get(name).add(protein);
						} else {
							Set<Protein> set = new THashSet<Protein>();
							set.add(protein);
							proteinsByAnnotationName.put(name, set);
						}
					}

					// index VALUE
					final String value = proteinAnnotation.getValue();
					if (value != null && "".equals(value)) {
						if (proteinsByAnnotationValue.containsKey(value)) {
							proteinsByAnnotationValue.get(value).add(protein);
						} else {
							Set<Protein> set = new THashSet<Protein>();
							set.add(protein);
							proteinsByAnnotationValue.put(value, set);
						}
					}

					// index AnnotationType
					final AnnotationType annotationType = proteinAnnotation.getAnnotationType();
					if (annotationType != null) {
						if (proteinsByAnnotationType.containsKey(annotationType)) {
							proteinsByAnnotationType.get(annotationType).add(protein);
						} else {
							Set<Protein> set = new THashSet<Protein>();
							set.add(protein);
							proteinsByAnnotationType.put(annotationType, set);
						}
					}
				}
			}
		}
	}

	public Set<Protein> getProteinsByAnnotationType(AnnotationType annotationType) {
		if (proteinsByAnnotationType.containsKey(annotationType))
			return proteinsByAnnotationType.get(annotationType);
		return new THashSet<Protein>();
	}

	public Set<Protein> getProteinsByAnnotationType(Collection<AnnotationType> annotationTypes) {
		Set<Protein> ret = new THashSet<Protein>();
		for (AnnotationType annotationType : annotationTypes) {
			ret.addAll(proteinsByAnnotationType.get(annotationType));
		}
		return ret;
	}

	public Set<Protein> getProteinsByAnnotationName(String name, StringComparisonType stringComparation) {
		Set<Protein> ret = new THashSet<Protein>();
		if (name != null && name.length() >= MINIMUM_LENGTH) {
			if (StringComparisonType.EXACT.equals(stringComparation)) {
				if (proteinsByAnnotationName.containsKey(name)) {
					return proteinsByAnnotationName.get(name);
				} else {

				}
			} else if (StringComparisonType.CONTAINS.equals(stringComparation)) {
				final Set<String> keySet = proteinsByAnnotationName.keySet();
				for (String annotationName : keySet) {
					if (annotationName.contains(name)) {
						ret.addAll(proteinsByAnnotationName.get(annotationName));
					}
				}
			}
		}
		return ret;
	}

	public Set<Protein> getProteinsByAnnotationValue(String value, StringComparisonType stringComparation) {
		Set<Protein> ret = new THashSet<Protein>();
		if (value != null && value.length() >= MINIMUM_LENGTH) {
			if (StringComparisonType.EXACT.equals(stringComparation)) {
				if (proteinsByAnnotationValue.containsKey(value)) {
					return proteinsByAnnotationValue.get(value);
				} else {

				}
			} else if (StringComparisonType.CONTAINS.equals(stringComparation)) {
				final Set<String> keySet = proteinsByAnnotationValue.keySet();
				for (String annotationValue : keySet) {
					if (annotationValue.contains(value)) {
						ret.addAll(proteinsByAnnotationValue.get(annotationValue));
					}
				}
			}
		}
		return ret;
	}
}
