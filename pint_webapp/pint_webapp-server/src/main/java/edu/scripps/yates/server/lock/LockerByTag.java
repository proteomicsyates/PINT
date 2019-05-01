package edu.scripps.yates.server.lock;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import edu.scripps.yates.shared.tasks.Task;
import gnu.trove.map.hash.THashMap;

public class LockerByTag {
	private static final Logger log = Logger.getLogger(LockerByTag.class);
	private final static Map<String, ReentrantLock> locksByTags = new THashMap<String, ReentrantLock>();
	private final static ReadWriteLock mapLock = new ReentrantReadWriteLock(true);
	private static final String UNIPROT_ANNOTATION = "uniprot annotation of project ";

	public static String getLockForUniprotAnnotation(String projectTag) {
		return UNIPROT_ANNOTATION + projectTag;
	}

	private static ReentrantLock getLock(String lockTag) {
		try {
			mapLock.writeLock().lock();
			ReentrantLock projectLock = locksByTags.get(lockTag);
			if (projectLock == null) {
				projectLock = new ReentrantLock(true);
				locksByTags.put(lockTag, projectLock);
				return projectLock;

			} else {
				return projectLock;
			}
		} finally {
			mapLock.writeLock().unlock();
		}
	}

	public static void lock(Task task, Method method) {
		lock(task.getKey().getTaskKey(), method);
	}

	/**
	 * If the project is already locked, it waits until other thread unlock the
	 * project. Then, it lock the project.
	 *
	 * @param lockTag
	 * @param method
	 */

	public static void lock(String lockTag, Method method) {
		final ReentrantLock lock = getLock(lockTag);
		if (method != null) {
			log.info("Trying to acquire locker for tag " + lockTag + " from Method: " + method.getName() + " with "
					+ lock.getQueueLength() + " threads in the queue");
		} else {
			log.info("Trying to acquire locker for tag " + lockTag + " with " + lock.getQueueLength()
					+ " threads in the queue");
		}
		boolean locked = false;
		if (lock.isLocked()) {
			log.info("Lock for tag '" + lockTag + "' is locked. I will wait for it from thread "
					+ Thread.currentThread().getId());
			locked = true;
		}
		lock.lock();
		if (locked) {
			log.info("Lock for tag '" + lockTag + "' has been released and taken by thread "
					+ Thread.currentThread().getId());
		}
		if (method != null) {
			log.info("Lock acquired by thread " + Thread.currentThread().getId() + " from Method " + method.getName()
					+ " for tag '" + lockTag + "'");
		} else {
			log.info("Lock acquired by thread " + Thread.currentThread().getId() + " for tag '" + lockTag + "'");
		}
	}

	public static void unlock(Task task, Method method) {
		unlock(task.getKey().getTaskKey(), method);
	}

	/**
	 * Unlocks the project
	 *
	 * @param lockTag
	 */
	public static void unlock(String lockTag, Method method) {
		if (method != null) {
			log.info("Unlocking for tag" + lockTag + " from Method: " + method.getName());
		} else {
			log.info("Unlocking for tag " + lockTag);
		}
		final ReentrantLock lock = getLock(lockTag);
		log.info(lock.getQueueLength() + " threads are waiting for this thread to unlock the lock");
		lock.unlock();
		if (method != null) {
			log.info(lockTag + " tag unlocked from Method: " + method.getName());
		} else {
			log.info(lockTag + " tag unlocked");
		}
	}

	/**
	 * Unlocks the projects in the collection of project tags
	 *
	 * @param projectTag
	 */
	public static void unlock(Collection<String> lockTags, Method method) {
		for (final String lockTag : lockTags) {
			unlock(lockTag, method);
		}
	}

	/**
	 * Locks all projects in the Collection of projectTags
	 *
	 * @param lockTags
	 * @param method
	 */
	public static void lock(Collection<String> lockTags, Method method) {
		for (final String lockTag : lockTags) {
			lock(lockTag, method);
		}
	}
}
