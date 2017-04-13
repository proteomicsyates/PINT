package edu.scripps.yates.annotations.uniprot;

import edu.scripps.yates.annotations.uniprot.xml.DbReferenceType;
import edu.scripps.yates.annotations.uniprot.xml.Entry;
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
}
