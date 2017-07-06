package edu.scripps.yates.proteindb.queries.dataproviders.psm;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.map.hash.THashMap;

public class PsmProviderFromSEQ implements ProteinProviderFromDB {

	private final String mySqlRegularExpression;
	private Map<String, Set<Psm>> result;
	private Set<String> projectTags;

	public PsmProviderFromSEQ(String regularExpression) {
		mySqlRegularExpression = getMySQLRegularExpression(regularExpression);
	}

	/**
	 * Change:<br>
	 * X* by %<br>
	 * X+ by _%<br>
	 * X by _<br>
	 *
	 * @param regularExpression
	 * @return
	 */
	private String getMySQLRegularExpression(String regularExpression) {
		regularExpression = regularExpression.toUpperCase();
		regularExpression = regularExpression.replace("\\W*", "%");
		regularExpression = regularExpression.replace("\\W+", "_%");
		regularExpression = regularExpression.replace("\\W", "_");
		String ret = "";

		// replace anything between brakets by %
		boolean insideBraket = false;
		for (int i = 0; i < regularExpression.length(); i++) {
			if (regularExpression.charAt(i) == '[') {
				insideBraket = true;
				ret += "%";
			} else if (regularExpression.charAt(i) == ']') {
				insideBraket = false;
			} else if (regularExpression.charAt(i) == '|') {

			} else {
				if (!insideBraket) {
					ret += regularExpression.charAt(i);
				}
			}
		}
		if (!"%".equals(ret)) {
			return "%" + ret + "%";
		}
		return ret;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap(boolean testMode) {
		if (result == null) {
			result = new THashMap<String, Set<Psm>>();
			int numPSMs = 0;
			if (projectTags == null || projectTags.isEmpty()) {
				List<Psm> psms = PreparedCriteria.getCriteriaForPsmSequence(mySqlRegularExpression, null).list();
				if (testMode && numPSMs + psms.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
					PersistenceUtils.addToPSMMapByPsmId(result,
							psms.subList(0, Math.min(psms.size(), QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
				} else {
					PersistenceUtils.addToPSMMapByPsmId(result, psms);
				}
				numPSMs += psms.size();
			} else {
				for (String projectTag : projectTags) {
					List<Psm> psms = PreparedCriteria.getCriteriaForPsmSequence(mySqlRegularExpression, projectTag)
							.list();
					if (testMode && numPSMs + psms.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
						PersistenceUtils.addToPSMMapByPsmId(result,
								psms.subList(0, Math.min(psms.size(), QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
						return result;
					} else {
						PersistenceUtils.addToPSMMapByPsmId(result, psms);
					}
					numPSMs += psms.size();
				}
			}
		}
		return result;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap(boolean testMode) {
		return PersistenceUtils.getProteinsFromPsms(getPsmMap(testMode), true);
	}

	@Override
	public void setProjectTags(Set<String> projectNames) {
		projectTags = projectNames;
		result = null;
	}
}