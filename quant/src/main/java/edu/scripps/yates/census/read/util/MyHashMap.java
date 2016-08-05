package edu.scripps.yates.census.read.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import edu.scripps.yates.census.read.CensusOutParser;

public class MyHashMap<K, V> extends HashMap<K, V> {

	public MyHashMap() {

	}

	/*
	 * (non-Javadoc)
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@Override
	public V get(Object key) {

		final V v = super.get(key);
		if (v == null) {
			// try with a synonym of the key
			Set<Object> synoyms = getSynonyms(key);
			for (Object object : synoyms) {
				final V v2 = super.get(object);
				if (v2 != null) {
					return v2;
				}
			}
		}
		return v;
	}

	private Set<Object> getSynonyms(Object key) {
		Set<Object> ret = new HashSet<Object>();
		if (key != null) {
			if (key instanceof String) {
				String stringKey = (String) key;
				if (stringKey.equals(CensusOutParser.CS)) {
					ret.add(CensusOutParser.CState);
				}
				if (stringKey.equals(CensusOutParser.SCAN)) {
					ret.add(CensusOutParser.SCAN_NUM);
				}
				if (stringKey.equals(CensusOutParser.FILENAME)) {
					ret.add(CensusOutParser.FILE_name);
				}
			}
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.HashMap#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object key) {

		final boolean containsKey = super.containsKey(key);
		if (!containsKey) {
			// try with a synonym of the key
			Set<Object> synoyms = getSynonyms(key);
			for (Object object : synoyms) {
				if (super.containsKey(object)) {
					return true;
				}
			}
		}

		return containsKey;
	}

}
