package edu.scripps.yates.utilities.venndata;

import java.util.HashSet;
import java.util.Set;

public class MultipleValuesObject extends ContainsMultipleKeys {
	private final Set<String> values = new HashSet<String>();
	private final String name;

	public MultipleValuesObject(String name, String separator) {
		this.name = name.trim();
		if (name.contains(separator)) {
			final String[] split = name.split(separator);
			for (String string : split) {
				values.add(string.trim());
			}
		} else {
			values.add(this.name);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + name + "]";
	}

	@Override
	public Set<String> getKeys() {
		return values;
	}

	public String getName() {
		return name;
	}
}
