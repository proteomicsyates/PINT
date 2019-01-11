package edu.scripps.yates.client.gui.components.projectCreatorWizard.manager;

import edu.scripps.yates.shared.model.interfaces.HasId;

/**
 * This interfaze represents an object in the project hierarchy
 * 
 * @author Salva
 * 
 * @param <HasId>
 */
public interface RepresentsDataObject extends RepresentsObject<HasId> {

	/**
	 * Gets the unique ID
	 * 
	 * @return
	 */
	public int getInternalID();

	/**
	 * Gets the name
	 * 
	 * @return
	 */
	public String getID();

}
