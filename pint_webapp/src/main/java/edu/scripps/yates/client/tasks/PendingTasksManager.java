package edu.scripps.yates.client.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PendingTasksManager {
	private static final Map<TaskType, List<String>> tasks = new HashMap<TaskType, List<String>>();
	private static final Set<PendingTaskHandler> controllers = new HashSet<PendingTaskHandler>();

	private static enum OPERATION {
		ADD, REMOVE
	};

	/**
	 * This will register the {@link PendingTaskHandler} in order to notify him
	 * each time a new task is added or removed
	 *
	 * @param controller
	 */
	public static void registerPendingTaskController(PendingTaskHandler controller) {
		controllers.add(controller);
	}

	/**
	 * Returns a list of pending tasks keys of one type.<br>
	 * Returns an empty List if there is no task of that type
	 * 
	 * @param taskType
	 * @return
	 */
	public static List<String> getPendingTaskKeys(TaskType taskType) {
		// to ensure to return an empty set:
		if (!tasks.containsKey(taskType)) {
			final List<String> set = new ArrayList<String>();
			tasks.put(taskType, set);
		}
		return tasks.get(taskType);
	}

	public static void addPendingTasks(TaskType taskType, Collection<String> keys) {

		final List<String> pendingTaskKeys = getPendingTaskKeys(taskType);
		for (final String key : pendingTaskKeys) {
			pendingTaskKeys.add(key);
			notifyPendingTaskControllers(OPERATION.ADD);
		}

	}

	public static void addPendingTask(TaskType taskType, String key) {
		final List<String> pendingTaskKeys = getPendingTaskKeys(taskType);
		pendingTaskKeys.add(key);
		notifyPendingTaskControllers(OPERATION.ADD);
	}

	private static void notifyPendingTaskControllers(OPERATION add) {
		for (final PendingTaskHandler pendingTaskController : controllers) {
			switch (add) {
			case ADD:
				pendingTaskController.onTaskAdded();
				break;
			case REMOVE:
				pendingTaskController.onTaskRemoved();
				break;
			default:
				break;
			}
		}

	}

	public static int removeTask(TaskType taskType, String key) {
		getPendingTaskKeys(taskType).remove(key);
		notifyPendingTaskControllers(OPERATION.REMOVE);
		return getNumPendingTasks(taskType);
	}

	public static int removeTasks(TaskType taskType, Collection<String> keys) {
		if (keys == null || keys.isEmpty()) {
			return getNumPendingTasks(taskType);
		}
		getPendingTaskKeys(taskType).removeAll(keys);
		notifyPendingTaskControllers(OPERATION.REMOVE);
		return getNumPendingTasks(taskType);
	}

	public static boolean isAnyPendingTask(TaskType taskType) {
		return !getPendingTaskKeys(taskType).isEmpty();
	}

	public static int getNumPendingTasks(TaskType taskType) {
		return getPendingTaskKeys(taskType).size();
	}

	public static void removeAllTasks(TaskType taskType) {
		removeTasks(taskType, getPendingTaskKeys(taskType));
	}

}
