package edu.scripps.yates.utilities.index;

import java.util.Map;

import edu.scripps.yates.utilities.util.Pair;

public interface FileIndex<T> {
	public T getItem(String key);

	public Map<String, Pair<Long, Long>> addItem(T item);
}
