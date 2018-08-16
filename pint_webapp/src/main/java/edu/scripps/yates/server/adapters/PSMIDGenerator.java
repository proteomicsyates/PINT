package edu.scripps.yates.server.adapters;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import gnu.trove.list.array.TIntArrayList;

public class PSMIDGenerator {
	private int psmID = 0;
	private static PSMIDGenerator instance;
	private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

	private PSMIDGenerator() {

	}

	public static PSMIDGenerator getInstance() {
		if (instance == null) {
			instance = new PSMIDGenerator();
		}
		return instance;
	}

	public int getNewID() {
		lock.writeLock().lock();
		try {
			return ++psmID;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public TIntArrayList getNewIDs(int numIDs) {
		final TIntArrayList ret = new TIntArrayList();
		for (int i = 0; i < numIDs; i++) {
			ret.add(getNewID());
		}
		return ret;
	}
}
