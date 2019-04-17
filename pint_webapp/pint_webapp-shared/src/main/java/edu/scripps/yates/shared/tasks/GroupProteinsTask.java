package edu.scripps.yates.shared.tasks;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.scripps.yates.shared.util.SharedDataUtil;

public class GroupProteinsTask extends Task {

	/**
	 * 
	 */
	private static final long serialVersionUID = 251415155243576383L;
	private final Set<String> projectTags = new HashSet<String>();

	public GroupProteinsTask() {

	}

	public GroupProteinsTask(Collection<String> projectTags) {
		super(TaskKeyGenerator.getKeyForGetProteinsFromQuery(projectTags), TaskType.GROUP_PROTEINS);
		projectTags.addAll(projectTags);
	}

	@Override
	public String getTaskDescription() {
		if (projectTags.size() == 1) {
			return getType().getSingleTaskMessage(projectTags.iterator().next());
		} else {
			return getType().getMultipleTaskMessage(SharedDataUtil.getProjectTagCollectionKey(projectTags));
		}
	}

}
