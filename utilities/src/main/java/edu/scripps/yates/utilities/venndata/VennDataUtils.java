package edu.scripps.yates.utilities.venndata;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VennDataUtils<T extends ContainsMultipleKeys> {

	protected VennDataUtils() {

	}

	protected Set<T> getObjectsByKeys(Set<String> keys, Map<String, T>... hashes) {
		Set<T> ret = new HashSet<T>();

		for (String key : keys) {
			for (Map<String, T> hash : hashes) {
				if (hash.containsKey(key)) {
					ret.add(hash.get(key));
					continue;
				}
			}
		}
		return ret;
	}

	public Set<T> getObjects(Map<String, T> hash) {
		Set<T> ret = new HashSet<T>();

		for (T obj : hash.values()) {
			ret.add(obj);
		}

		return ret;
	}

}
