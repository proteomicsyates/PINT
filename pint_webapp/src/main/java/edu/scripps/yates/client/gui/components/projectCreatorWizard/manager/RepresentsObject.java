package edu.scripps.yates.client.gui.components.projectCreatorWizard.manager;

/**
 * This interfaze represents an object in the project hierarchy
 * 
 * @author Salva
 * 
 * @param <HasId>
 */
public interface RepresentsObject<T> {

	/**
	 * Gets the object in the model that is represented in this
	 * {@link RepresentsObject}
	 * 
	 * @return
	 */
	public T getObject();

	/**
	 * Update the represented object. This method will be called when a change
	 * in the GUI is made, in order to maintain the referenced object
	 * synchonized with the GUI.
	 */
	public abstract void updateRepresentedObject();

	/**
	 * Replace the represented object by the one in the parameter and update the
	 * GUI showing its data.
	 * 
	 * @param condition
	 */
	public void updateGUIFromObjectData(T dataObject);

	/**
	 * Update the GUI showing the data from the object that represents
	 */
	public void updateGUIFromObjectData();

}
