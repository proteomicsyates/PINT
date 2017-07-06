package edu.scripps.yates.server.tasks;

import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import gnu.trove.set.hash.THashSet;

/**
 * This class will track all server requests in order to not repeat the same
 * request if it have been already run or if it is curretly running
 *
 * @author Salva
 *
 */
public class ServerTaskRegister {
	private static Logger log = Logger.getLogger(ServerTaskRegister.class);

	private static final Set<Task> runningTasks = new THashSet<Task>();
	/**
	 * To avoid concurrent access to the runningTasks
	 */
	private static boolean accessRequested;

	/**
	 * register a {@link Task}. If the same task was already running, the thread
	 * will wait for it completion. Otherwise, the method will return normally.
	 *
	 * @param task
	 */
	public static void registerTask(Task task) {

		while (isOtherTaskLikeThisRunning(task)) {
			try {
				log.info("Waiting for a previous task: " + task);
				log.info(runningTasks.size() + " tasks in the queue");
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		runningTasks.add(task);
		log.info("Task registered: " + task);

	}

	public static synchronized Task getRunningTask(String key) {
		try {
			waitForAccess();
			final Iterator<Task> iterator = runningTasks.iterator();
			while (iterator.hasNext()) {
				Task task = iterator.next();
				if (task.getKey().equals(key))
					return task;
			}
			return null;
		} finally {
			log.info("Access to set of task released");
			accessRequested = false;
		}
	}

	private static synchronized boolean isOtherTaskLikeThisRunning(Task task2) {
		try {
			waitForAccess();
			final Iterator<Task> iterator = runningTasks.iterator();
			while (iterator.hasNext()) {
				Task task = iterator.next();
				if (task.equals(task2))
					return true;
				if (task.getClass().equals(task2.getClass())) {
					if (task.getKey().equals(task2.getKey())) {
						log.info("A task like " + task + " is already running.");
						return true;
					}
				}
			}
			log.info("There is no other task like " + task2);
			return false;
		} finally {
			log.info("Access to set of task released");
			accessRequested = false;
		}
	}

	public static synchronized void endTask(Task task2) {
		try {
			if (task2 == null)
				return;
			waitForAccess();
			final Iterator<Task> iterator = runningTasks.iterator();
			while (iterator.hasNext()) {
				Task task = iterator.next();
				if (task.getClass().equals(task2.getClass())) {
					if (task.getKey().equals(task2.getKey())) {
						iterator.remove();
						log.info("Task removed: " + task2);
						log.info("Pending tasks: " + runningTasks.size());
					}
				}
			}
			return;
		} finally {
			log.info("Access to set of task released");
			accessRequested = false;
		}
	}

	private static synchronized void waitForAccess() {
		while (accessRequested) {
			try {
				log.info("Waiting for accessing to the set of tasks");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		accessRequested = true;
		// log.info("Access to set of tasks requested");
	}

	/**
	 * Returns true if the there is a task currently running having the same key
	 *
	 * @param key
	 * @param class1
	 * @return
	 */
	public static boolean isRunningTask(String key, Class<? extends Task> class1) {
		final Task runningTask = getRunningTask(key);
		if (runningTask != null) {
			if (runningTask.getClass().equals(class1)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if a task with the same key and Class is currently running
	 *
	 * @param task
	 * @return
	 */
	public static boolean isRunningTask(Task task) {
		return isRunningTask(task.getKey(), task.getClass());
	}
}
