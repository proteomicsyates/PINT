package edu.scripps.yates.proteindb.queries.dataproviders.psm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;

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
	public Map<String, Set<Psm>> getPsmMap() {
		if (result == null) {
			result = new HashMap<String, Set<Psm>>();
			if (projectTags == null || projectTags.isEmpty()) {
				List<Psm> psms = PreparedCriteria.getCriteriaForPsmSequence(mySqlRegularExpression, null).list();
				PersistenceUtils.addToPSMMapByPsmId(result, psms);
			} else {
				for (String projectTag : projectTags) {
					List<Psm> psms = PreparedCriteria.getCriteriaForPsmSequence(mySqlRegularExpression, projectTag)
							.list();
					PersistenceUtils.addToPSMMapByPsmId(result, psms);
				}
			}
		}
		return result;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap() {
		return PersistenceUtils.getProteinsFromPsms(getPsmMap(), true);
	}

	@Override
	public void setProjectTags(Set<String> projectNames) {
		projectTags = projectNames;
		result = null;
	}
}