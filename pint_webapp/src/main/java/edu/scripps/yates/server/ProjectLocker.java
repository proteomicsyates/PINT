package edu.scripps.yates.server;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

public class ProjectLocker {
	private static final Map<String, ReentrantLock> projectLocked = new HashMap<String, ReentrantLock>();
	private static final Logger log = Logger.getLogger(ProjectLocker.class);

	private static ReentrantLock getLock(String projectTag) {
		if (!projectLocked.containsKey(projectTag)) {
			ReentrantLock lock = new ReentrantLock(true);
			projectLocked.put(projectTag, lock);
		}
		return projectLocked.get(projectTag);
	}

	/**
	 * If the project is already locked, it waits until other thread unlock the
	 * project. Then, it lock the project.
	 *
	 * @param projectTag
	 * @param method
	 */

	public static void lock(String projectTag, Method method) {
		log.info("Locking " + projectTag + " from Method: " + method.getName());
		final ReentrantLock lock = getLock(projectTag);
		log.info(projectTag + " locked from Method: " + method.getName());
		lock.lock();
		log.info("Lock acquired by thread " + Thread.currentThread().getId() + " from method " + method.getName());
	}

	/**
	 * Unlocks the project
	 *
	 * @param projectTag
	 */
	public static void unlock(String projectTag, Method method) {
		log.info("Unlocking " + projectTag + " from Method: " + method.getName());
		final ReentrantLock lock = getLock(projectTag);
		log.info(lock.getQueueLength() + " threads are waiting for this thread to unlock the lock");
		lock.unlock();
		log.info(projectTag + " unlocked from Method: " + method.getName());

	}

	/**
	 * Unlocks the projects in the collection of project tags
	 *
	 * @param projectTag
	 */
	public static void unlock(Collection<String> projectTags, Method method) {
		for (String projectTag : projectTags) {
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
		for (String projectTag : projectTags) {
			lock(projectTag, method);
		}
	}
}
