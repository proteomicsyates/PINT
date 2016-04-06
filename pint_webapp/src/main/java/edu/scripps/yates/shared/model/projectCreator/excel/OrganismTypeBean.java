package edu.scripps.yates.shared.model.projectCreator.excel;

public class OrganismTypeBean extends IdDescriptionTypeBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1106758327525465394L;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof OrganismTypeBean) {
			OrganismTypeBean organism = (OrganismTypeBean) obj;
			if (organism.getId().equals(id))
				return true;
		}
		return super.equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return -1;
	}
}
