package edu.scripps.yates.annotations.uniprot.proteoform.model;

import java.util.List;

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
	public static String translateSequence(HasAlternativeSequence hasAlternativeSequence, String wholeOriginalSeq) {
		List<FeatureSequence> alternativeSequences = hasAlternativeSequence.getAlternativeSequences();
		String newSeq = "";
		if (!alternativeSequences.isEmpty()) {
			newSeq = alternativeSequences.get(0).getValue();
		}
		int start = hasAlternativeSequence.getFeatureLocation().getStart();
		int end = hasAlternativeSequence.getFeatureLocation().getEnd();
		String ret = wholeOriginalSeq.substring(0, start - 1);
		ret += newSeq;
		ret += wholeOriginalSeq.substring(end, wholeOriginalSeq.length());
		return ret;
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
}
