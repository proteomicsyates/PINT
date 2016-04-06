package edu.scripps.yates.annotations.uniprot;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.annotations.uniprot.xml.CommentType;
import edu.scripps.yates.annotations.uniprot.xml.CommentType.Absorption;
import edu.scripps.yates.annotations.uniprot.xml.CommentType.Disease;
import edu.scripps.yates.annotations.uniprot.xml.CommentType.TemperatureDependence;
import edu.scripps.yates.annotations.uniprot.xml.DbReferenceType;
import edu.scripps.yates.annotations.uniprot.xml.EvidencedStringType;
import edu.scripps.yates.annotations.uniprot.xml.InteractantType;
import edu.scripps.yates.annotations.uniprot.xml.IsoformType;
import edu.scripps.yates.annotations.uniprot.xml.IsoformType.Name;
import edu.scripps.yates.annotations.uniprot.xml.SubcellularLocationType;
import edu.scripps.yates.utilities.model.factories.ProteinAnnotationEx;
import edu.scripps.yates.utilities.proteomicsmodel.AnnotationType;
import edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation;

public class ProteinAnnotationsFromCommentAdapter
		implements org.proteored.miapeapi.interfaces.Adapter<Set<ProteinAnnotation>> {

	private final CommentType comment;

	public ProteinAnnotationsFromCommentAdapter(CommentType comment) {
		this.comment = comment;
	}

	String getStringFromAbsortionAnnotation(Absorption absorption) {
		if (absorption != null) {
			StringBuilder sb = new StringBuilder();
			if (absorption.getMax() != null && absorption.getMax().getValue() != null) {
				sb.append("Max: " + absorption.getMax().getValue());
			}
			if (absorption.getText() != null) {
				for (EvidencedStringType evidenceType : absorption.getText()) {
					if (evidenceType.getValue() != null) {
						if (!"".equals(sb.toString())) {
							sb.append("\n");
						}
						sb.append(evidenceType.getValue());
					}
				}
			}
			return sb.toString();
		}
		return null;
	}

	@Override
	public Set<ProteinAnnotation> adapt() {
		Set<ProteinAnnotation> ret = new HashSet<ProteinAnnotation>();
		// absorption
		final Absorption absorption = comment.getAbsorption();
		if (absorption != null && absorption.getText() != null) {
			ret.add(new ProteinAnnotationEx(AnnotationType.biophysicochemical_properties, "Absorption",
					getStringFromAbsortionAnnotation(absorption)));
		}
		// disease
		final Disease disease = comment.getDisease();
		if (disease != null) {
			final ProteinAnnotationEx proteinAnnotationEx = new ProteinAnnotationEx(AnnotationType.disease,
					disease.getName(), disease.getDescription());
			final DbReferenceType dbReference = disease.getDbReference();
			if (dbReference != null && dbReference.getType() != null) {
				proteinAnnotationEx.setSource(dbReference.getType());
			}
			ret.add(proteinAnnotationEx);
		}

		// subcellular location
		final List<SubcellularLocationType> subcellularLocation = comment.getSubcellularLocation();
		if (subcellularLocation != null)
			for (SubcellularLocationType subcellularLocationType : subcellularLocation) {

				final List<EvidencedStringType> locations = subcellularLocationType.getLocation();
				if (locations != null) {
					for (EvidencedStringType location : locations) {
						if (location.getValue() != null) {
							final ProteinAnnotationEx proteinAnnotationEx = new ProteinAnnotationEx(
									AnnotationType.subcellular_location, location.getValue());
							ret.add(proteinAnnotationEx);
						}
					}
				}
			}

		// isoform
		final List<IsoformType> isoforms = comment.getIsoform();
		if (isoforms != null)
			for (IsoformType isoform : isoforms) {
				final List<Name> names = isoform.getName();
				if (names != null)
					for (Name name : names) {
						if (name.getValue() != null) {
							final ProteinAnnotationEx proteinAnnotationEx = new ProteinAnnotationEx(
									AnnotationType.alternative_products, name.getValue());
							ret.add(proteinAnnotationEx);
						}
					}
			}

		// temperature dependence
		final TemperatureDependence temperatureDependence = comment.getTemperatureDependence();
		if (temperatureDependence != null && temperatureDependence.getText() != null) {
			final ProteinAnnotationEx proteinAnnotationEx = new ProteinAnnotationEx(
					AnnotationType.biophysicochemical_properties, "Temperature dependence",
					getStringFromTemperatureDependence(temperatureDependence));
			ret.add(proteinAnnotationEx);
		}

		// interactant
		final List<InteractantType> interactants = comment.getInteractant();
		for (InteractantType interactant : interactants) {
			if (interactant.getId() == null && interactant.getIntactId() != null) {
				final ProteinAnnotationEx proteinAnnotationEx = new ProteinAnnotationEx(AnnotationType.interation,
						"intactId:" + interactant.getIntactId());
				ret.add(proteinAnnotationEx);
			} else if (interactant.getId() != null && interactant.getLabel() != null) {
				final ProteinAnnotationEx proteinAnnotationEx = new ProteinAnnotationEx(AnnotationType.interation,
						"intactId:" + interactant.getIntactId(),
						"id:" + interactant.getId() + " label:" + interactant.getLabel());
				ret.add(proteinAnnotationEx);
			} else {
				final ProteinAnnotationEx proteinAnnotationEx = new ProteinAnnotationEx(AnnotationType.interation,
						"intactId:" + interactant.getIntactId(), "id:" + interactant.getId());
				ret.add(proteinAnnotationEx);
			}
		}
		// general comment
		if (comment.getText() != null) {
			final AnnotationType annotationType = AnnotationType.translateStringToAnnotationType(comment.getType());
			String value = comment.getText() == null ? null : getStringFromText(comment.getText());
			ProteinAnnotationEx proteinAnnotationEx = new ProteinAnnotationEx(annotationType, comment.getType(), value);
			ret.add(proteinAnnotationEx);
		}
		return ret;
	}

	private String getStringFromText(List<EvidencedStringType> text) {
		if (text != null) {
			StringBuilder sb = new StringBuilder();
			for (EvidencedStringType evidencedStringType : text) {
				if (evidencedStringType.getValue() != null) {
					if ("".equals(sb.toString())) {
						sb.append("\n");
					}
					sb.append(evidencedStringType.getValue());
				}
			}
			return sb.toString();
		}
		return null;
	}

	private String getStringFromTemperatureDependence(TemperatureDependence temperatureDependence) {
		if (temperatureDependence != null) {
			StringBuilder sb = new StringBuilder();
			if (temperatureDependence.getText() != null) {
				for (EvidencedStringType evidenceString : temperatureDependence.getText()) {
					if (evidenceString.getValue() != null) {
						if (!"".equals(sb.toString())) {
							sb.append("\n");
						}
						sb.append(evidenceString.getValue());
					}
				}
			}
			return sb.toString();
		}
		return null;
	}

}
