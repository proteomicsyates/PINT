package edu.scripps.yates.proteindb.queries.dataproviders.psm;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.map.hash.THashMap;

public class PsmProviderFromTaxonomy implements ProteinProviderFromDB {
	private final String organismName;
	private final String ncbiTaxID;
	private Set<String> projectTags;
	private Map<String, Set<Psm>> psms;

	public PsmProviderFromTaxonomy(String organismName, String ncbiTaxID) {
		this.organismName = organismName;
		this.ncbiTaxID = ncbiTaxID;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap(boolean testMode) {
		if (psms == null) {
			psms = new THashMap<String, Set<Psm>>();
			int numPSMs = 0;
			if (projectTags == null || projectTags.isEmpty()) {
				final List<Psm> psmsWithTaxonomy = PreparedQueries.getPsmsWithTaxonomy(null, organismName, ncbiTaxID);
				if (testMode && numPSMs + psms.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
					PersistenceUtils.addToPSMMapByPsmId(psms,
							psmsWithTaxonomy.subList(0, Math.min(0, QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
				} else {
					PersistenceUtils.addToPSMMapByPsmId(psms, psmsWithTaxonomy);
				}
				numPSMs += psmsWithTaxonomy.size();
			} else {
				for (String projectTag : projectTags) {
					final List<Psm> psmsWithTaxonomy = PreparedQueries.getPsmsWithTaxonomy(projectTag, organismName,
							ncbiTaxID);
					if (testMode && numPSMs + psms.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
						PersistenceUtils.addToPSMMapByPsmId(psms,
								psmsWithTaxonomy.subList(0, Math.min(0, QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
					} else {
						PersistenceUtils.addToPSMMapByPsmId(psms, psmsWithTaxonomy);
					}
					numPSMs += psmsWithTaxonomy.size();
				}
			}
		}
		return psms;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap(boolean testMode) {
		return PersistenceUtils.getProteinsFromPsms(getPsmMap(testMode), true);
	}

	@Override
	public void setProjectTags(Set<String> projectTags) {
		this.projectTags = projectTags;
		psms = null;

	}

}
