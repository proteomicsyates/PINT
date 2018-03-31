package edu.scripps.yates.annotations.uniprot.proteoform;

import java.util.List;

import edu.scripps.yates.annotations.uniprot.xml.FeatureType;
import edu.scripps.yates.annotations.uniprot.xml.LocationType;
import uk.ac.ebi.kraken.interfaces.common.Value;
import uk.ac.ebi.kraken.interfaces.uniprot.features.ConflictFeature;
import uk.ac.ebi.kraken.interfaces.uniprot.features.ConflictReport;
import uk.ac.ebi.kraken.interfaces.uniprot.features.Feature;
import uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureLocation;
import uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureSequence;
import uk.ac.ebi.kraken.interfaces.uniprot.features.HasAlternativeSequence;
import uk.ac.ebi.kraken.interfaces.uniprot.features.HasFeatureDescription;
import uk.ac.ebi.kraken.interfaces.uniprot.features.HasFeatureStatus;
import uk.ac.ebi.kraken.interfaces.uniprot.features.MutagenFeature;
import uk.ac.ebi.kraken.interfaces.uniprot.features.VariantFeature;

public class ProteoformUtil {
	public static String translateSequence(FeatureType feature, String wholeOriginalSeq) {
		List<String> alternativeSequences = feature.getVariation();
		String newSeq = "";
		if (!alternativeSequences.isEmpty()) {
			newSeq = alternativeSequences.get(0);
		}
		int start = 0;
		int end = 0;
		if (feature.getLocation().getBegin() != null && feature.getLocation().getEnd() != null) {
			start = feature.getLocation().getBegin().getPosition().intValue();
			end = feature.getLocation().getEnd().getPosition().intValue();
		} else {
			if (feature.getLocation().getPosition() != null) {
				start = end = feature.getLocation().getPosition().getPosition().intValue();
			}
		}
		String ret = "";
		if (start > 0) {
			ret = wholeOriginalSeq.substring(0, start - 1);
		}
		ret += newSeq;

		ret += wholeOriginalSeq.substring(end, wholeOriginalSeq.length());
		return ret;
	}

	public static String translateSequence(HasAlternativeSequence hasAlternativeSequence, String wholeOriginalSeq) {
		List<FeatureSequence> alternativeSequences = hasAlternativeSequence.getAlternativeSequences();
		String newSeq = "";
		if (!alternativeSequences.isEmpty()) {
			newSeq = alternativeSequences.get(0).getValue();
		}
		int start = hasAlternativeSequence.getFeatureLocation().getStart();
		int end = hasAlternativeSequence.getFeatureLocation().getEnd();
		String ret = "";
		if (start > 0) {
			ret = wholeOriginalSeq.substring(0, start - 1);
		}
		ret += newSeq;
		ret += wholeOriginalSeq.substring(end, wholeOriginalSeq.length());
		return ret;
	}

	public static String getShortDescription(Feature feature) {
		StringBuilder sb = new StringBuilder();
		if (feature instanceof HasAlternativeSequence) {
			HasAlternativeSequence alternativeSequenceHolder = (HasAlternativeSequence) feature;
			String from = alternativeSequenceHolder.getOriginalSequence().getValue();
			String to = null;
			if (!alternativeSequenceHolder.getAlternativeSequences().isEmpty()) {
				to = alternativeSequenceHolder.getAlternativeSequences().get(0).getValue();
			}
			if (!"".equals(from)) {
				sb.append(from).append("->");
			} else {
				sb.append("_deletion");
			}
			if (to != null) {
				sb.append(to);
			}
			sb.append("_at_" + getLocationString(feature));
		}
		return sb.toString();
	}

	public static String getDescription(Feature feature) {
		StringBuilder sb = new StringBuilder();
		if (feature instanceof HasAlternativeSequence) {
			HasAlternativeSequence alternativeSequenceHolder = (HasAlternativeSequence) feature;
			String from = alternativeSequenceHolder.getOriginalSequence().getValue();
			String to = null;
			if (!alternativeSequenceHolder.getAlternativeSequences().isEmpty()) {
				to = alternativeSequenceHolder.getAlternativeSequences().get(0).getValue();
			}
			if (!"".equals(from)) {
				sb.append(from).append(" -> ");
			} else {
				sb.append("Deletion");
			}
			if (to != null) {
				sb.append(to);
			}
			sb.append(" at " + getLocationString(feature));

		}
		if (feature instanceof HasFeatureDescription) {
			String descriptionValue = ((HasFeatureDescription) feature).getFeatureDescription().getValue();
			if (!"".equals(descriptionValue) && !"".equals(sb.toString())) {
				sb.append(", ");
			}

			sb.append(descriptionValue);
		}

		// reports
		String report = "";
		if (feature instanceof VariantFeature) {
			report = getReport(((VariantFeature) feature).getVariantReport());
		} else if (feature instanceof MutagenFeature) {
			report = getReport(((MutagenFeature) feature).getMutagenReport());
		} else if (feature instanceof ConflictFeature) {

			List<ConflictReport> conflictReports = ((ConflictFeature) feature).getConflictReports();
			for (ConflictReport conflictReport : conflictReports) {
				if (!"".equals(report)) {
					report += ", ";
				}
				report += getReport(conflictReport);
			}

		}
		if (!"".equals(report)) {
			sb.append(", ");
		}
		sb.append(report);

		if (!"".equals(sb.toString())) {
			return sb.toString();
		}

		if (feature instanceof HasFeatureStatus) {
			HasFeatureStatus featureStatusHolder = feature;
			return featureStatusHolder.getFeatureStatus().getName() + "\nstart:"
					+ feature.getFeatureLocation().getStart() + "\tend:" + feature.getFeatureLocation().getEnd()
					+ "\tstart modif:" + feature.getFeatureLocation().getStartModifier() + "\tend modif:"
					+ feature.getFeatureLocation().getEndModifier();
		}

		return null;
	}

	public static String getShortDescription(FeatureType feature) {
		StringBuilder sb = new StringBuilder();
		if (feature.getVariation() != null && !feature.getVariation().isEmpty()) {

			String from = feature.getOriginal();
			String to = null;
			if (!feature.getVariation().isEmpty()) {
				to = feature.getVariation().get(0);
			}
			if (!"".equals(from)) {
				sb.append(from).append("->");
			} else {
				sb.append("_deletion");
			}
			if (to != null) {
				sb.append(to);
			}
			sb.append("_at_" + getLocationString(feature));

		}
		return sb.toString();
	}

	public static String getDescription(FeatureType feature) {
		StringBuilder sb = new StringBuilder();

		if (feature.getDescription() != null && !"".equals(feature.getDescription())) {
			String descriptionValue = feature.getDescription();
			if (!"".equals(descriptionValue) && !"".equals(sb.toString())) {
				sb.append(", ");
			}
			sb.append(descriptionValue);
		}

		if (!"".equals(sb.toString())) {
			return sb.toString();
		}

		return null;
	}

	private static String getReport(Value value) {
		return value.getValue();
	}

	public static String getLocationString(Feature feature) {
		FeatureLocation featureLocation = feature.getFeatureLocation();
		if (featureLocation == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		if (featureLocation.getStart() == featureLocation.getEnd()) {
			sb.append(featureLocation.getStart());
		} else {
			sb.append("[").append(featureLocation.getStart()).append("-").append(featureLocation.getEnd()).append("]");
		}
		return sb.toString();
	}

	public static String getLocationString(FeatureType feature) {
		LocationType featureLocation = feature.getLocation();
		if (featureLocation == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		if (featureLocation.getBegin() != null && featureLocation.getEnd() != null) {
			if (featureLocation.getBegin().getPosition().intValue() == featureLocation.getEnd().getPosition()
					.intValue()) {
				sb.append(featureLocation.getBegin().getPosition().intValue());
			} else {
				sb.append("[").append(featureLocation.getBegin().getPosition().intValue()).append("-")
						.append(featureLocation.getEnd().getPosition().intValue()).append("]");
			}
		} else {
			sb.append(featureLocation.getPosition().getPosition().intValue());
		}
		return sb.toString();
	}
}
