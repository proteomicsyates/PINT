package edu.scripps.yates.shared.util;

import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.shared.columns.ColumnName;

public class UniprotFeatures {
	private static Map<ColumnName, String[]> uniprotFeatures = new HashMap<ColumnName, String[]>();
	private static boolean mapLoaded = false;

	private static void loadMap() {

		String[] tmp = { "active site", "metal ion-binding site", "binding site", "site" };
		uniprotFeatures.put(ColumnName.PROTEIN_ACTIVE_SITE, tmp);
		uniprotFeatures.put(ColumnName.PEPTIDE_ACTIVE_SITE, tmp);
		String[] tmp2 = { "topological domain", "transmembrane region", "intramembrane region", "repeat",
				"calcium-binding region", "zinc finger region", "DNA-binding region",
				"nucleotide phosphate-binding region", "region of interest", "coiled-coil region",
				"short sequence motif", "compositionally biased region", "domain" };
		uniprotFeatures.put(ColumnName.PROTEIN_DOMAIN_FAMILIES, tmp2);
		uniprotFeatures.put(ColumnName.PEPTIDE_DOMAIN_FAMILIES, tmp2);
		String[] tmp3 = { "sequence variant", "splice variant" };
		uniprotFeatures.put(ColumnName.PROTEIN_NATURAL_VARIATIONS, tmp3);
		uniprotFeatures.put(ColumnName.PEPTIDE_NATURAL_VARIATIONS, tmp3);
		String[] tmp4 = { "helix", "turn", "strand" };
		uniprotFeatures.put(ColumnName.PROTEIN_SECONDARY_STRUCTURE, tmp4);
		uniprotFeatures.put(ColumnName.PEPTIDE_SECONDARY_STRUCTURE, tmp4);
		String[] tmp5 = { "mutagenesis site", "unsure residue", "sequence conflict", "non-consecutive residues",
				"non-terminal residue" };
		uniprotFeatures.put(ColumnName.PROTEIN_EXPERIMENTAL_INFO, tmp5);
		uniprotFeatures.put(ColumnName.PEPTIDE_EXPERIMENTAL_INFO, tmp5);
		String[] tmp6 = { "signal peptide", "initiator methionine", "chain", "peptide", "propectide",
				"transit peptide" };
		uniprotFeatures.put(ColumnName.PROTEIN_MOLECULAR_PROCESSING, tmp6);
		uniprotFeatures.put(ColumnName.PEPTIDE_MOLECULAR_PROCESSING, tmp6);
		String[] tmp7 = { "glycosylation site", "modified residue", "disulfide bond", "non-standard amino acid",
				"lipid moiety-binding region", "cross-link" };
		uniprotFeatures.put(ColumnName.PROTEIN_PTM, tmp7);
		uniprotFeatures.put(ColumnName.PEPTIDE_PTM, tmp7);
		mapLoaded = true;
	}

	public static String[] getUniprotFeaturesByColumnName(ColumnName columnName) {
		if (!mapLoaded) {
			loadMap();
		}
		if (uniprotFeatures.containsKey(columnName)) {
			return uniprotFeatures.get(columnName);
		}
		return new String[0];
	}

}
