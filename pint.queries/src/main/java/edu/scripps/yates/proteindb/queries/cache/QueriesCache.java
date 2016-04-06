package edu.scripps.yates.proteindb.queries.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.queries.Query;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;

public class QueriesCache {
	private static final Map<String, Map<String, Set<Psm>>> map = new HashMap<String, Map<String, Set<Psm>>>();
	private static QueriesCache instance;
	private static final boolean enabled = false;

	private QueriesCache() {

	}

	public String processKey(Query query, Set<String> projectTags) {
		// sort project tags
		List<String> sortedProjectTags = new ArrayList<String>();
		sortedProjectTags.addAll(projectTags);
		Collections.sort(sortedProjectTags);
		String ret = query.getCommandReference().toString();
		for (String projectTag : sortedProjectTags) {
			ret += projectTag;
		}
		return ret;
	}

	public static QueriesCache getInstance() {
		if (instance == null) {
			instance = new QueriesCache();
		}
		return instance;
	}

	public void addtoCache(Map<String, Set<Psm>> t, Query query,
			Set<String> projectTags) {
		String processedKey = processKey(query, projectTags);
		if (QueriesUtil.QUERY_CACHE_ENABLED) {
			if (map.containsKey(processedKey)) {
				map.get(processedKey).putAll(t);
			} else {
				Map<String, Set<Psm>> set = new HashMap<String, Set<Psm>>();
				set.putAll(t);
				map.put(processedKey, set);
			}
		}
	}

	public Map<String, Set<Psm>> getFromCache(Query query,
			Set<String> projectTags) {
		String processedKey = processKey(query, projectTags);
		return map.get(processedKey);
	}

	public void removeFromCache(Query query, Set<String> projectTags) {
		String processedKey = processKey(query, projectTags);
		map.remove(processedKey);

	}

	public boolean contains(Query query, Set<String> projectTags) {
		String processedKey = processKey(query, projectTags);
		return map.containsKey(processedKey);
	}

	/**
	 * @return the isenabled
	 */
	public static boolean isEnabled() {
		return enabled;
	}

}
