package edu.scripps.yates.utilities.venndata;

import java.util.Set;

/**
 * An object containing a list of string keys, that, is equal to another when
 * they have at least one key in common
 *
 * @author Salva
 *
 */
public abstract class ContainsMultipleKeys {
	public abstract Set<String> getKeys();

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return 123;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ContainsMultipleKeys) {
			ContainsMultipleKeys obj2 = (ContainsMultipleKeys) obj;
			for (String key : getKeys()) {
				if (obj2.getKeys().contains(key)) {
					return true;
				}
			}
			return false;
		}
		return super.equals(obj);
	}

}
