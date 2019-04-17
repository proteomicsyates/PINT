package edu.scripps.yates.shared.tasks;

import java.io.Serializable;
import java.util.Date;

public class TaskKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3690686316943346371L;
	private String taskKey;
	private long timestamp;

	public TaskKey() {

	}

	public TaskKey(String taskKey) {
		this.taskKey = taskKey;
		this.timestamp = System.currentTimeMillis();
	}

	public String getTaskKey() {
		return taskKey;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public static TaskKey createKey(String taskKey) {
		return new TaskKey(taskKey);
	}

	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return taskKey + "at: " + new Date(timestamp);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TaskKey) {
			final TaskKey key2 = (TaskKey) obj;
			if (this.getTaskKey().equals(key2.getTaskKey())) {
				if (Long.compare(this.timestamp, key2.timestamp) == 0) {
					return true;
				}
			}
		}
		return super.equals(obj);
	}

}
