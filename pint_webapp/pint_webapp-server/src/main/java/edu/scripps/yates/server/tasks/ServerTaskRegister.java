package edu.scripps.yates.server.tasks;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.StampedLock;

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

	private final Set<Task> runningTasks = new THashSet<Task>();
	/**
	 * To avoid concurrent access to the runningTasks
	 */
	private boolean accessRequested;

	private static ServerTaskRegister instance;
	private StampedLock lock = new StampedLock();
	private long currentStamp;

	private ServerTaskRegister() {

	}

	public static ServerTaskRegister getInstance() {
		if (instance == null) {
			instance = new ServerTaskRegister();
		}
		return instance;
	}

	/**
	 * register a {@link Task}. If the same task was already running, the thread
	 * will wait for it completion. Otherwise, the method will return normally.
	 *
	 * @param task
	 */
	public void registerTask(Task task) {
		if (task == null) {
			return;
		}
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

	public synchronized Task getRunningTask(String key) {
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
			log.info("Trying to unlock access to set of task with stamp " + currentStamp);
			accessRequested = false;
			lock.unlock(currentStamp);
			log.info("Access to set of task unlocked with stamp " + currentStamp);
		}
	}

	private synchronized boolean isOtherTaskLikeThisRunning(Task task2) {
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
			log.info("Trying to unlock access to set of task with stamp " + currentStamp);
			accessRequested = false;
			lock.unlock(currentStamp);
			log.info("Access to set of task unlocked with stamp " + currentStamp);
		}
	}

	public synchronized void endTask(Task task2) {
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
			log.info("Trying to unlock access to set of task with stamp " + currentStamp);
			accessRequested = false;
			lock.unlock(currentStamp);
			log.info("Access to set of task unlocked with stamp " + currentStamp);
		}
	}

	private synchronized void waitForAccess() {
		while ((currentStamp = lock.tryWriteLock()) == 0l) {
			try {
				log.info("Waiting for accessing to the set of tasks");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		accessRequested = true;
		log.info("Access to set of tasks requested with stamp " + currentStamp);
	}

	/**
	 * Returns true if the there is a task currently running having the same key
	 *
	 * @param key
	 * @param class1
	 * @return
	 */
	public boolean isRunningTask(String key, Class<? extends Task> class1) {
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
	public boolean isRunningTask(Task task) {
		return isRunningTask(task.getKey(), task.getClass());
	}
}
