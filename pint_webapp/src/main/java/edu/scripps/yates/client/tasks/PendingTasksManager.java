package edu.scripps.yates.client.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class PendingTasks {
	private static final HashMap<TaskType, List<String>> tasks = new HashMap<TaskType, List<String>>();

	public static List<String> getPendingTaskKeys(TaskType taskType) {
		// to ensure to return an empty set:
		if (!tasks.containsKey(taskType)) {
			List<String> set = new ArrayList<String>();
			tasks.put(taskType, set);
		}
		return tasks.get(taskType);
	}

	public static void addPendingTasks(TaskType taskType, Collection<String> keys) {
		getPendingTaskKeys(taskType).addAll(keys);
	}

	public static void addPendingTask(TaskType taskType, String key) {
		getPendingTaskKeys(taskType).add(key);
	}

	public static int removeTask(TaskType taskType, String key) {
		getPendingTaskKeys(taskType).remove(key);
		return getPendingTaskKeys(taskType).size();
	}

	public static int removeTasks(TaskType taskType, Collection<String> keys) {
		getPendingTaskKeys(taskType).removeAll(keys);
		return getPendingTaskKeys(taskType).size();
	}

	public static boolean isAnyPendingTask(TaskType taskType) {
		return !getPendingTaskKeys(taskType).isEmpty();
	}

	public static int getNumPendingTasks(TaskType taskType) {
		return getPendingTaskKeys(taskType).size();
	}

}
