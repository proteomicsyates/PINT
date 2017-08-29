package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.annotations.uniprot.UniprotProteinRetriever;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import gnu.trove.map.hash.THashMap;

public class ProteinAccessionAdapter implements Adapter<ProteinAccession>, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -3917756538499650517L;
	private final Accession accession;
	private final boolean isPrimaryAccession;
	public static final String SEPARATOR = "***";
	private final static Map<String, edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession> map = new THashMap<String, edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession>();

	public ProteinAccessionAdapter(Accession accession, boolean isPrimaryAccession) {
		if (accession == null)
			throw new IllegalArgumentException("Accession cannot be null");
		this.accession = accession;
		this.isPrimaryAccession = isPrimaryAccession;
	}

	@Override
	public ProteinAccession adapt() {
		String accession2 = accession.getAccession();
		final int MAX_LENGHT_ACC_IN_DB = 15;
		if (accession2.length() > MAX_LENGHT_ACC_IN_DB)
			accession2 = accession2.substring(0, MAX_LENGHT_ACC_IN_DB - 1);
		if (map.containsKey(accession2)) {
			final ProteinAccession proteinAccession = map.get(accession2);
			if (isPrimaryAccession)
				proteinAccession.setIsPrimary(isPrimaryAccession);
			return proteinAccession;
		}
		ProteinAccession ret = new ProteinAccession(accession2, accession.getAccessionType().name(),
				isPrimaryAccession);
		map.put(accession2, ret);

		// protein
		// ret.getProteins().add(protein);

		Protein annotatedProtein = null;
		String description = accession.getDescription();
		if (description == null || "".equals(description)) {
			if (accession.getAccessionType().equals(AccessionType.UNIPROT)) {
				// take the description from Uniprot annotations if available
				UniprotProteinRetriever uplr = new UniprotProteinRetriever(null,
						UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
						UniprotProteinRetrievalSettings.getInstance().isUseIndex());
				Map<String, Protein> annotatedProteins = uplr.getAnnotatedProtein(accession2);
				if (annotatedProteins.containsKey(accession2)) {
					annotatedProtein = annotatedProteins.get(accession2);
					if (annotatedProtein != null) {
						description = annotatedProtein.getPrimaryAccession().getDescription();
					}
				}
			}
		}
		ret.setDescription(description);

		// alternative names
		List<String> alternativeNames = accession.getAlternativeNames();
		if (alternativeNames == null)
			alternativeNames = new ArrayList<String>();
		if (annotatedProtein != null && annotatedProtein.getPrimaryAccession().getAlternativeNames() != null)
			alternativeNames.addAll(annotatedProtein.getPrimaryAccession().getAlternativeNames());
		if (alternativeNames != null && !alternativeNames.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (String altName : alternativeNames) {
				if (!"".equals(sb.toString()))
					sb.append(SEPARATOR);
				sb.append(altName);
			}
			ret.setAlternativeNames(sb.toString());
		}
		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
