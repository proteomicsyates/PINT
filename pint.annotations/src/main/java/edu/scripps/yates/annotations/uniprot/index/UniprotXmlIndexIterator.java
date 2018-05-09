package edu.scripps.yates.annotations.uniprot.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.annotations.util.UniprotEntryUtil;
import edu.scripps.yates.utilities.util.Pair;

public class UniprotXmlIndexIterator implements Iterator<Entry> {
	private final static Logger log = Logger.getLogger(UniprotXmlIndexIterator.class);
	private final List<Pair<Long, Long>> listofpairs;
	private final List<Entry> currentListOfEntries = new ArrayList<Entry>();
	private final UniprotXmlIndexIO uniprotFileIndexIO;
	private final boolean ignoreReferences;
	private final boolean ignoreDBReferences;

	public UniprotXmlIndexIterator(List<Pair<Long, Long>> listofpairs, UniprotXmlIndexIO uniprotFileIndexIO,
			boolean ignoreReferences, boolean ignoreDBReferences) {
		this.listofpairs = listofpairs;
		this.uniprotFileIndexIO = uniprotFileIndexIO;
		this.ignoreReferences = ignoreReferences;
		this.ignoreDBReferences = ignoreDBReferences;
	}

	@Override
	public boolean hasNext() {
		if (currentListOfEntries.isEmpty() && listofpairs.isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public Entry next() {
		if (!currentListOfEntries.isEmpty()) {
			// take the first from the list of created entries
			final Entry entry = currentListOfEntries.get(0);
			currentListOfEntries.remove(0);
			return entry;
		} else {
			if (!listofpairs.isEmpty()) {
				// take the first from the list of pairs in positions
				final Pair<Long, Long> pair = listofpairs.get(0);
				listofpairs.remove(0);

				try {
					final String item = uniprotFileIndexIO.getItem(pair.getFirstelement(), pair.getSecondElement());

					final List<Entry> entries = uniprotFileIndexIO.unmarshallMultipleEntriesFromString(item);
					for (final Entry entry : entries) {
						// in order to save memory, remove non used elements
						// such as
						// references
						UniprotEntryUtil.removeNonUsedElements(entry, ignoreReferences, ignoreDBReferences);
						currentListOfEntries.add(entry);
					}
					return next();
				} catch (final IOException e) {
					e.printStackTrace();
					log.error("Error accessing index from iterator");
				}
			}
		}
		return null;
	}

}
