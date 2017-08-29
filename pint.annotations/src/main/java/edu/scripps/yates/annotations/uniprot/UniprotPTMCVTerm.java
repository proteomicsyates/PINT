package edu.scripps.yates.annotations.uniprot;

import java.util.Map;
import java.util.Set;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class UniprotPTMCVTerm {
	private final Map<UniprotCVTermCode, Set<String>> values = new THashMap<UniprotCVTermCode, Set<String>>();

	/**
	 * @return the values
	 */
	public Map<UniprotCVTermCode, Set<String>> getValues() {
		return values;
	}

	public Set<String> getValueSet(UniprotCVTermCode code) {
		return values.get(code);
	}

	public void addValue(UniprotCVTermCode code, String value) {
		if (values.containsKey(code)) {
			values.get(code).add(value);
		} else {
			Set<String> set = new THashSet<String>();
			set.add(value);
			values.put(code, set);
		}
	}

	public String getSingleValue(UniprotCVTermCode code) {
		if (values.containsKey(code)) {
			if (values.get(code).size() == 1) {
				return values.get(code).iterator().next();
			} else {
				throw new IllegalArgumentException("There is more than one value with code " + code.name());
			}
		}
		return null;
	}
}
