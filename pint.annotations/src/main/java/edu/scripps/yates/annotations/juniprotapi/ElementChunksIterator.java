package edu.scripps.yates.annotations.juniprotapi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ElementChunksIterator implements Iterator<List<String>> {
	private final List<List<String>> elementsListOfLists = new ArrayList<List<String>>();
	private Iterator<List<String>> iterator;

	public ElementChunksIterator(int chunkSize, Collection<String> elements) {
		List<String> list = new ArrayList<String>();
		for (String element : elements) {
			list.add(element);
			if (list.size() == chunkSize && !list.isEmpty()) {
				elementsListOfLists.add(list);
				list = new ArrayList<String>();
			}
		}
		if (!list.isEmpty()) {
			elementsListOfLists.add(list);
		}
	}

	private Iterator<List<String>> getIterator() {
		if (iterator == null) {
			iterator = elementsListOfLists.iterator();
		}
		return iterator;
	}

	@Override
	public boolean hasNext() {
		return getIterator().hasNext();
	}

	@Override
	public List<String> next() {
		return getIterator().next();
	}

}
