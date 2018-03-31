package edu.scripps.yates.annotations.uniprot;

import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.annotations.uniprot.xml.CommentType;
import edu.scripps.yates.annotations.uniprot.xml.DbReferenceType;
import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.annotations.uniprot.xml.EvidencedStringType;
import edu.scripps.yates.annotations.uniprot.xml.FeatureType;
import edu.scripps.yates.annotations.uniprot.xml.GeneNameType;
import edu.scripps.yates.annotations.uniprot.xml.GeneType;
import edu.scripps.yates.annotations.uniprot.xml.PropertyType;
import edu.scripps.yates.annotations.uniprot.xml.ProteinType.SubmittedName;

public class UniprotEntryUtil {
	public static String getENSGID(Entry entry) {
		if (entry != null) {
			if (entry.getDbReference() != null) {
				for (DbReferenceType dbReference : entry.getDbReference()) {
					if ("Ensembl".equals(dbReference.getType())) {
						if (dbReference.getProperty() != null) {
							for (PropertyType property : dbReference.getProperty()) {
								if ("gene ID".equals(property.getType())) {
									return property.getValue();
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	public static String getProteinSequence(Entry entry) {
		if (entry != null) {
			if (entry.getSequence() != null) {
				if (entry.getSequence().getValue() != null) {
					String seq = entry.getSequence().getValue().replace("\n", "");
					return seq;
				}
			}
		}
		return null;
	}

	public static Double getMolecularWeightInDalton(Entry entry) {
		if (entry != null) {
			if (entry.getSequence() != null) {
				if (entry.getSequence().getMass() > 0) {
					return Double.valueOf(entry.getSequence().getMass());
				}
			}
		}
		return null;
	}

	public static List<String> getGeneName(Entry entry, boolean justPrimary, boolean secondaryIfPrimaryIsNull) {
		List<String> ret = new ArrayList<String>();
		if (entry != null) {
			List<GeneType> gene = entry.getGene();
			if (gene != null) {
				for (GeneType geneType : gene) {
					for (GeneNameType geneNameType : geneType.getName()) {
						boolean isPrimary = false;
						if ("primary".contentEquals(geneNameType.getType())) {
							isPrimary = true;
						}
						if (!justPrimary || (justPrimary && isPrimary)) {
							ret.add(geneNameType.getValue());
							if (justPrimary) {
								return ret;
							}
						}
					}
				}
				if (justPrimary && secondaryIfPrimaryIsNull && ret.isEmpty()) {
					List<String> geneNames2 = getGeneName(entry, false, false);
					if (geneNames2.isEmpty()) {
					} else {
						ret.add(geneNames2.get(0));
					}
				}
			}
		}
		return ret;
	}

	/**
	 * <dbReference type="Proteomes" id="UP000005640"> <property type=
	 * "component" value="Chromosome 1"/> </dbReference>
	 * 
	 * @param entry
	 * @return
	 */
	public static String getChromosomeName(Entry entry) {
		if (entry != null) {
			if (entry.getDbReference() != null) {
				for (DbReferenceType dbReference : entry.getDbReference()) {
					if ("Proteomes".equals(dbReference.getType())) {
						if (dbReference.getProperty() != null) {
							for (PropertyType property : dbReference.getProperty()) {
								if (property.getValue().contains("Chromosome")) {
									return property.getValue();
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	public static String getUniprotEvidence(Entry entry) {
		if (entry != null) {
			if (entry.getProteinExistence() != null) {
				return entry.getProteinExistence().getType();
			}
		}
		return null;
	}

	public static String getProteinDescription(Entry entry) {
		if (entry != null) {
			if (entry.getProtein() != null) {
				if (entry.getProtein().getRecommendedName() != null) {
					if (entry.getProtein().getRecommendedName().getFullName() != null) {
						return entry.getProtein().getRecommendedName().getFullName().getValue();
					}
				} else {
					if (entry.getProtein().getSubmittedName() != null) {
						for (SubmittedName submittedName : entry.getProtein().getSubmittedName()) {
							EvidencedStringType fullName = submittedName.getFullName();
							if (fullName != null) {
								String fullNameValue = fullName.getValue();
								if (fullNameValue != null && !"".equals(fullNameValue)) {
									return fullNameValue;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	public static List<CommentType> getComments(Entry entry,
			uk.ac.ebi.kraken.interfaces.uniprot.comments.CommentType type) {
		List<CommentType> ret = new ArrayList<CommentType>();
		String typeString = type.toXmlDisplayName();
		if (entry != null) {
			if (entry.getComment() != null) {
				for (CommentType comment : entry.getComment()) {
					if (comment.getType().equals(typeString)) {
						ret.add(comment);
					}
				}
			}
		}
		return ret;
	}

	public static List<FeatureType> getFeatures(Entry entry,
			uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType type) {
		List<FeatureType> ret = new ArrayList<FeatureType>();
		String typeString = type.getValue();
		if (entry != null) {
			if (entry.getFeature() != null) {
				for (FeatureType feature : entry.getFeature()) {
					if (feature.getType().equals(typeString)) {
						ret.add(feature);
					}
				}
			}
		}
		return ret;
	}
}
