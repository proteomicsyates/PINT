package edu.scripps.yates.server.lock;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import gnu.trove.map.hash.THashMap;

public class LockerByTag {
	private static final Logger log = Logger.getLogger(LockerByTag.class);
	private final static Map<String, ReentrantLock> locksByTags = new THashMap<String, ReentrantLock>();
	private final static ReadWriteLock mapLock = new ReentrantReadWriteLock(true);

	private static ReentrantLock getLock(String projectTag) {
		try {
			mapLock.writeLock().lock();
			ReentrantLock projectLock = locksByTags.get(projectTag);
			if (projectLock == null) {
				projectLock = new ReentrantLock(true);
				locksByTags.put(projectTag, projectLock);
				return projectLock;

			} else {
				return projectLock;
			}
		} finally {
			mapLock.writeLock().unlock();
		}
	}

	/**
	 * If the project is already locked, it waits until other thread unlock the
	 * project. Then, it lock the project.
	 *
	 * @param projectTag
	 * @param method
	 */

	public static void lock(String projectTag, Method method) {
		final ReentrantLock lock = getLock(projectTag);
		if (method != null) {
			log.info("Trying to acquire locker for tag " + projectTag + " from Method: " + method.getName() + " with "
					+ lock.getQueueLength() + " threads in the queue");
		} else {
			log.info("Trying to acquire locker for tag " + projectTag + " with " + lock.getQueueLength()
					+ " threads in the queue");
		}

		lock.lock();
		if (method != null) {
			log.info("Lock acquired by thread " + Thread.currentThread().getId() + " from Method " + method.getName()
					+ " for tag '" + projectTag + "'");
		} else {
			log.info("Lock acquired by thread " + Thread.currentThread().getId() + " for tag '" + projectTag + "'");
		}
	}

	/**
	 * Unlocks the project
	 *
	 * @param projectTag
	 */
	public static void unlock(String projectTag, Method method) {
		if (method != null) {
			log.info("Unlocking for tag" + projectTag + " from Method: " + method.getName());
		} else {
			log.info("Unlocking for tag " + projectTag);
		}
		final ReentrantLock lock = getLock(projectTag);
		log.info(lock.getQueueLength() + " threads are waiting for this thread to unlock the lock");
		lock.unlock();
		if (method != null) {
			log.info(projectTag + " tag unlocked from Method: " + method.getName());
		} else {
			log.info(projectTag + " tag unlocked");
		}
	}

	/**
	 * Unlocks the projects in the collection of project tags
	 *
	 * @param projectTag
	 */
	public static void unlock(Collection<String> projectTags, Method method) {
		for (final String projectTag : projectTags) {
			unlock(projectTag, method);
		}
	}

	/**
	 * Locks all projects in the Collection of projectTags
	 *
	 * @param projectTags
	 * @param method
	 */
	public static void lock(Collection<String> projectTags, Method method) {
		for (final String projectTag : projectTags) {
			lock(projectTag, method);
		}
	}
}
