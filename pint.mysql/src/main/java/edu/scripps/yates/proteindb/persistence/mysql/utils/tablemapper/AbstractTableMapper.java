package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper;

import java.util.Collection;

import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.THashSet;

public abstract class AbstractTableMapper<T, Y> {
	private final TIntObjectHashMap<TIntArrayList> t_to_y = new TIntObjectHashMap<TIntArrayList>();
	private final TIntObjectHashMap<TIntArrayList> y_to_t = new TIntObjectHashMap<TIntArrayList>();

	public void addObject1(T t) {
		final int tID = getIDFromT(t);
		if (!t_to_y.containsKey(tID)) {
			final TIntArrayList yIDs = doMapping(t);
			t_to_y.put(tID, yIDs);
			for (final int yID : yIDs.toArray()) {
				if (!y_to_t.containsKey(yID)) {
					final TIntArrayList tIDs = new TIntArrayList();
					y_to_t.put(yID, tIDs);
				}
				y_to_t.get(yID).add(tID);
			}
		}
	}

	public void addObjects1(Collection<T> ts) {

		final THashSet<T> notFoundTs = new THashSet<T>();

		for (final T t : ts) {
			final int tID = getIDFromT(t);
			if (!t_to_y.containsKey(tID)) {
				notFoundTs.add(t);
			}
		}

		final TIntObjectHashMap<TIntArrayList> yIDsByTID = doMappings(notFoundTs);
		t_to_y.putAll(yIDsByTID);

		for (final int tID : yIDsByTID.keys()) {
			final TIntArrayList yIDs = yIDsByTID.get(tID);
			for (final int yID : yIDs.toArray()) {
				if (!y_to_t.containsKey(yID)) {
					final TIntArrayList tIDs = new TIntArrayList();
					y_to_t.put(yID, tIDs);
				}
				y_to_t.get(yID).add(tID);
			}
		}

	}

	public TIntArrayList mapIDs1(Y y) {
		final int yID = getIDFromY(y);
		return y_to_t.get(yID);
	}

	public TIntArrayList mapIDs2(T t) {
		final int tID = getIDFromT(t);
		return t_to_y.get(tID);
	}

	public TIntArrayList getYIDs(T t) {
		final int tID = getIDFromT(t);
		return t_to_y.get(tID);
	}

	protected TIntObjectHashMap<TIntArrayList> doMappings(THashSet<T> ts) {
		final TIntObjectHashMap<TIntArrayList> ret = new TIntObjectHashMap<TIntArrayList>();
		for (final T t : ts) {
			final TIntArrayList yIDs = doMapping(t);
			ret.put(getIDFromT(t), yIDs);
		}
		return ret;
	}

	protected abstract TIntArrayList doMapping(T t);

	protected abstract int getIDFromT(T t);

	protected abstract int getIDFromY(Y y);

	public TIntArrayList transformToTIntArray(Collection<Integer> collection) {
		final TIntArrayList ret = new TIntArrayList();
		for (final Integer num : collection) {
			ret.add(num);
		}
		return ret;
	}
}
