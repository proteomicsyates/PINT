package edu.scripps.yates.annotations.util;

import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.cache.AbstractCache;

public class UniprotEntryCache extends AbstractCache<Entry, String> {

	@Override
	protected Map<String, Entry> createMap() {
		return new HashMap<String, Entry>();
	}

}
