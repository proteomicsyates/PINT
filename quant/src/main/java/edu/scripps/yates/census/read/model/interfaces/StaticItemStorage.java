package edu.scripps.yates.census.read.model.interfaces;

import java.util.HashMap;
import java.util.Map;

public class StaticItemStorage<T extends HasKey> {
	private final Map<String, T> map = new HashMap<String, T>();

	public boolean contains(T hasKeyObj) {
		return map.containsKey(hasKeyObj.getKey());
	}

	public boolean containsKey(String key) {
		return map.containsKey(key);
	}

	public boolean addItem(T hasKeyObj) {
		if (!map.containsKey(hasKeyObj.getKey())) {
			map.put(hasKeyObj.getKey(), hasKeyObj);
			return true;
		}
		return false;
	}

	public T getItem(String key) {
		return map.get(key);
	}

	public int size() {
		return map.size();
	}

	public void clear() {
		map.clear();
	}
}
