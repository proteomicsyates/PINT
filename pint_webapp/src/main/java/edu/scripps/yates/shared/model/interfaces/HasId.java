package edu.scripps.yates.shared.model.interfaces;

public abstract class HasId implements Comparable<HasId> {
	public abstract String getId();

	@Override
	public int compareTo(HasId o) {
		if (o != null) {
			return getId().compareTo(o.getId());
		}
		return 0;
	}

}
