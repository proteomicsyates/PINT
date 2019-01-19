package edu.scripps.yates.client.ui.wizard.pages.panels;

import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;

public interface IDGenerator {
	/**
	 * Generates a new unique ID checking all the other IDS in a
	 * {@link PintImportCfgBean}
	 * 
	 * @param id
	 * @return
	 */
	public String getNewID(String id);

}
