package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinDataProvider;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.map.hash.THashMap;

public class ProteinProviderFromProteinAccs extends ProteinDataProvider {
	private final List<String> accs = new ArrayList<String>();

	public ProteinProviderFromProteinAccs(Collection<String> accs) {
		if (accs == null)
			throw new IllegalArgumentException("accession list is null");
		for (final String acc : accs) {
			if (acc != null && !"".equals(acc))
				this.accs.add(acc);
		}

	}

	@Override
	public Map<String, Set<Protein>> getProteinMap(boolean testMode) {
		if (result == null) {
			result = new THashMap<String, Set<Protein>>();
			int numProteins = 0;
			if (projectTags != null) {
				for (final String projectTag : projectTags) {
					final List<Protein> proteinsWithAccessions = PreparedCriteria.getCriteriaByProteinACC(accs,
							projectTag);
					if (testMode && numProteins + proteinsWithAccessions.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
						PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithAccessions.subList(0, Math
								.min(proteinsWithAccessions.size(), QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins)));
						return result;
					} else {
						PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithAccessions);
					}
					numProteins += proteinsWithAccessions.size();
				}
			} else {
				final List<Protein> proteinsWithAccessions = PreparedCriteria.getCriteriaByProteinACC(accs, null);
				if (testMode && numProteins + proteinsWithAccessions.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
					PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithAccessions.subList(0,
							Math.min(proteinsWithAccessions.size(), QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins)));
					return result;
				} else {
					PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithAccessions);
				}
			}

		}
		return result;
	}

}
