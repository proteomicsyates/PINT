package edu.scripps.yates.annotations.uniprot;

import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.annotations.uniprot.xml.DbReferenceType;
import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.annotations.uniprot.xml.GeneNameType;
import edu.scripps.yates.annotations.uniprot.xml.GeneType;
import edu.scripps.yates.annotations.uniprot.xml.PropertyType;

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
	 * <dbReference type="Proteomes" id="UP000005640">
	 * <property type="component" value="Chromosome 1"/> </dbReference>
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
				}
			}
		}
		return null;
	}
}
