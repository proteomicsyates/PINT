package edu.scripps.yates.shared.model.projectCreator.excel;

public class TissueTypeBean extends IdDescriptionTypeBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5375690553321568419L;

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TissueTypeBean) {
			TissueTypeBean tissue = (TissueTypeBean) obj;
			if (tissue.getId().equals(id))
				return true;
		}
		return super.equals(obj);
	}
}
