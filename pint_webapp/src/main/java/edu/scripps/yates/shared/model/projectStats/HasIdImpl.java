package edu.scripps.yates.shared.model.projectStats;

import edu.scripps.yates.shared.model.interfaces.HasId;

public class HasIdImpl implements HasId {
	private final String id;

	public HasIdImpl(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

}
