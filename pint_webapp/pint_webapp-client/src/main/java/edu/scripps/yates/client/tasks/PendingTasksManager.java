package edu.scripps.yates.client.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.tasks.Task;
import edu.scripps.yates.shared.tasks.TaskType;

public class PendingTasksManager {
	private static final Map<TaskType, List<Task>> tasks = new HashMap<TaskType, List<Task>>();
	private static final Set<PendingTaskHandler> controllers = new HashSet<PendingTaskHandler>();

	private static enum OPERATION {
		ADD, REMOVE
	};

	/**
	 * This will register the {@link PendingTaskHandler} in order to notify him each
	 * time a new task is added or removed
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
	public static List<Task> getPendingTasks(TaskType taskType) {
		// to ensure to return an empty set:
		if (!tasks.containsKey(taskType)) {
			final List<Task> set = new ArrayList<Task>();
			tasks.put(taskType, set);
		}
		return tasks.get(taskType);
	}

	public static Task addPendingTask(Task task) {
		final TaskType taskType = task.getType();
		final List<Task> pendingTasks = getPendingTasks(taskType);
		pendingTasks.add(task);
		notifyPendingTaskControllers(OPERATION.ADD);
		return task;
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

	public static int removeTask(Task task) {
		getPendingTasks(task.getType()).remove(task);
		notifyPendingTaskControllers(OPERATION.REMOVE);
		return getNumPendingTasks(task.getType());
	}

	public static int removeTasks(TaskType taskType, Collection<Task> tasks) {
		if (tasks == null || tasks.isEmpty()) {
			return getNumPendingTasks(taskType);
		}
		getPendingTasks(taskType).removeAll(tasks);
		notifyPendingTaskControllers(OPERATION.REMOVE);
		return getNumPendingTasks(taskType);
	}

	public static boolean isAnyPendingTask(TaskType taskType) {
		return !getPendingTasks(taskType).isEmpty();
	}

	public static int getNumPendingTasks(TaskType taskType) {
		return getPendingTasks(taskType).size();
	}

	public static void removeAllTasks(TaskType taskType) {
		removeTasks(taskType, getPendingTasks(taskType));
	}

}
