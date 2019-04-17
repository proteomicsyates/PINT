package edu.scripps.yates.proteindb.queries.dataproviders.psm;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.PsmDataProvider;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.map.hash.THashMap;

public class PsmProviderFromTaxonomy extends PsmDataProvider {
	private final String organismName;
	private final String ncbiTaxID;

	public PsmProviderFromTaxonomy(String organismName, String ncbiTaxID) {
		this.organismName = organismName;
		this.ncbiTaxID = ncbiTaxID;
	}

	@Override
	public Map<String, Collection<Psm>> getPsmMap(boolean testMode) {
		if (result == null) {
			result = new THashMap<String, Collection<Psm>>();
			int numPSMs = 0;
			if (projectTags == null || projectTags.isEmpty()) {
				final List<Psm> psmsWithTaxonomy = PreparedQueries.getPsmsWithTaxonomy(null, organismName, ncbiTaxID);
				if (testMode && numPSMs + result.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
					PersistenceUtils.addToPSMMapByPsmId(result,
							psmsWithTaxonomy.subList(0, Math.min(0, QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
				} else {
					PersistenceUtils.addToPSMMapByPsmId(result, psmsWithTaxonomy);
				}
				numPSMs += psmsWithTaxonomy.size();
			} else {
				for (final String projectTag : projectTags) {
					final List<Psm> psmsWithTaxonomy = PreparedQueries.getPsmsWithTaxonomy(projectTag, organismName,
							ncbiTaxID);
					if (testMode && numPSMs + result.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
						PersistenceUtils.addToPSMMapByPsmId(result,
								psmsWithTaxonomy.subList(0, Math.min(0, QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
					} else {
						PersistenceUtils.addToPSMMapByPsmId(result, psmsWithTaxonomy);
					}
					numPSMs += psmsWithTaxonomy.size();
				}
			}
		}
		return result;
	}

}
