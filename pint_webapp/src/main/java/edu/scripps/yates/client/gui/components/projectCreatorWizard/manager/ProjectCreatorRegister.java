package edu.scripps.yates.client.gui.components.projectCreatorWizard.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class will register all the objects created in the interface, providing
 * unique identifiers, and keeping relationships between them in order to
 * propagate the updates of their properties.
 *
 * @author Salva
 *
 */
public class ProjectCreatorRegister {
	private static int availableID = 0;
	private static final Map<Integer, RepresentsDataObject> projectObjectMap = new HashMap<Integer, RepresentsDataObject>();
	private static final Map<Class, List<ReferencesDataObject>> listenersByClass = new HashMap<Class, List<ReferencesDataObject>>();
	private static final Map<Integer, List<ReferencesDataObject>> listenersByObjectID = new HashMap<Integer, List<ReferencesDataObject>>();

	/**
	 * Get the next available unique ID
	 *
	 * @return
	 */
	public static int getNextAvailableID() {
		return ++availableID;
	}

	public static void registerProjectObjectRepresenter(int id, RepresentsDataObject projectObject) {
		projectObjectMap.put(id, projectObject);
	}

	/**
	 * calls to updateGUIReferringToDataObjects in all the registered listeners
	 * for changes on object with the provided id
	 *
	 * @param id
	 */
	public static void fireModificationEvent(Class class1) {
		// get all the listeners for that class
		final List<ReferencesDataObject> list = listenersByClass.get(class1);
		if (list != null) {
			for (ReferencesDataObject guiUpdater : list) {
				guiUpdater.updateGUIReferringToDataObjects();
			}
		}
	}

	/**
	 * calls to updateGUIReferringToDataObjects in all the registered listeners
	 * for changes of the object with the provided ID
	 *
	 * @param id
	 */
	public static void fireModificationEvent(int id) {
		// get all the listeners for that object id
		final List<ReferencesDataObject> list = listenersByObjectID.get(id);
		if (list != null) {
			for (ReferencesDataObject guiUpdater : list) {
				guiUpdater.updateGUIReferringToDataObjects();
			}
		}

	}

	public static RepresentsDataObject getProjectObjectRepresenter(int id) {
		if (projectObjectMap.containsKey(id))
			return projectObjectMap.get(id);
		return null;
	}

	/**
	 * Gets the {@link RepresentsDataObject} having an object with the same
	 * class as provided
	 *
	 * @param class1
	 * @return
	 */
	public static List<RepresentsDataObject> getProjectObjectsRepresentingClass(Class class1) {
		List<RepresentsDataObject> ret = new ArrayList<RepresentsDataObject>();
		// look into the map and check the project object if they are the same
		// class
		final Collection<RepresentsDataObject> projectObjects = projectObjectMap.values();
		for (RepresentsDataObject projectObject : projectObjects) {
			if (class1.equals(projectObject.getObject().getClass())) {
				ret.add(projectObject);
			}
		}
		return ret;
	}

	/**
	 * Gets the {@link RepresentsDataObject} having an object with the same
	 * class as provided
	 *
	 * @param class1
	 * @return
	 */
	public static Map<Integer, RepresentsDataObject> getProjectObjectsMapRepresentingClass(Class class1) {
		Map<Integer, RepresentsDataObject> ret = new HashMap<Integer, RepresentsDataObject>();
		// look into the map and check the project object if they are the same
		// class
		final Collection<RepresentsDataObject> projectObjects = projectObjectMap.values();
		for (RepresentsDataObject projectObject : projectObjects) {
			if (class1.equals(projectObject.getObject().getClass())) {
				ret.put(projectObject.getInternalID(), projectObject);
			}
		}
		return ret;
	}

	public static void registerAsListenerByObjectClass(Class class1, ReferencesDataObject updater) {
		if (listenersByClass.containsKey(class1)) {
			listenersByClass.get(class1).add(updater);
		} else {
			List<ReferencesDataObject> list = new ArrayList<ReferencesDataObject>();
			list.add(updater);
			listenersByClass.put(class1, list);
		}
	}

	public static void registerAsListenerByObjectID(int id, ReferencesDataObject updater) {
		if (listenersByObjectID.containsKey(id)) {
			listenersByObjectID.get(id).add(updater);
		} else {
			List<ReferencesDataObject> list = new ArrayList<ReferencesDataObject>();
			list.add(updater);
			listenersByObjectID.put(id, list);
		}
	}

	/**
	 * Remove the projectObject of the register. It also removes the listeners
	 * of the modifications of that object
	 *
	 * @param id
	 */
	public static void deleteProjectObjectRepresenter(int id) {
		System.out.println("Deleting project object representer " + id);
		logMaps();
		if (projectObjectMap.containsKey(id)) {
			projectObjectMap.remove(id);
		}
		if (listenersByObjectID.containsKey(id)) {
			listenersByObjectID.remove(id);
		}
		logMaps();
	}

	/**
	 * Remove the {@link ReferencesDataObject}
	 *
	 * @param referencesDataObject
	 */
	public static void deleteListener(ReferencesDataObject referencesDataObject) {
		if (referencesDataObject == null)
			return;

		System.out.println("Deleting listener " + referencesDataObject);
		logMaps();
		if (!listenersByClass.isEmpty()) {
			for (List<ReferencesDataObject> list : listenersByClass.values()) {
				if (list != null) {
					final Iterator<ReferencesDataObject> iterator = list.iterator();
					while (iterator.hasNext()) {
						final ReferencesDataObject next = iterator.next();
						if (next != null && next.equals(referencesDataObject)) {
							iterator.remove();
						}
					}
				}
			}
		}
		System.out.println("listener deleted by class");

		if (!listenersByObjectID.isEmpty()) {
			for (List<ReferencesDataObject> list : listenersByObjectID.values()) {
				if (list != null) {
					final Iterator<ReferencesDataObject> iterator = list.iterator();
					while (iterator.hasNext()) {
						final ReferencesDataObject next = iterator.next();
						if (next != null && next.equals(referencesDataObject)) {
							iterator.remove();
						}
					}
				}
			}
		}
		System.out.println("listener deleted by object ID");
		logMaps();
	}

	private static void logMaps() {
		System.out.println("-----------------");
		int listeners = 0;
		for (List<ReferencesDataObject> list : listenersByClass.values()) {
			if (list != null)
				listeners += list.size();
		}
		System.out.println(listeners + " listenersByClass");

		int listeners2 = 0;
		for (List<ReferencesDataObject> list : listenersByObjectID.values()) {
			if (list != null)
				listeners2 += list.size();
		}
		System.out.println(listeners2 + " listenersByObjectID");
		System.out.println(projectObjectMap.size() + " projectObjectMap");
		System.out.println("-----------------");
	}

	/**
	 * Gets the {@link RepresentsDataObject} that is being listened by a given
	 * {@link ReferencesDataObject}
	 *
	 * @param experimentalConditionEditorPanel
	 * @return
	 */
	public static RepresentsDataObject getProjectObjectRepresenter(ReferencesDataObject listener) {

		final Collection<Integer> internalObjectIDs = listenersByObjectID.keySet();
		for (Integer internalObjectID : internalObjectIDs) {
			final List<ReferencesDataObject> listeners = listenersByObjectID.get(internalObjectID);
			for (ReferencesDataObject listener2 : listeners) {
				if (listener2.equals(listener)) {
					return projectObjectMap.get(internalObjectID);
				}
			}
		}
		return null;
	}

	public static void clearRegister() {
		listenersByClass.clear();
		listenersByObjectID.clear();
		projectObjectMap.clear();
	}

	public static boolean containsAnyObjectRepresenterWithId(String objectId) {
		final Collection<RepresentsDataObject> registeredProjectObjects = projectObjectMap.values();
		for (RepresentsDataObject representsDataObject : registeredProjectObjects) {
			if (representsDataObject.getID().equals(objectId))
				return true;
		}
		return false;
	}

	// public static void deleteAsListenerByObjectClass(Class class1,
	// ReferencesDataObject updater) {
	// if (listenersByClass.containsKey(class1)) {
	// final List<ReferencesDataObject> list = listenersByClass
	// .get(class1);
	// final Iterator<ReferencesDataObject> iterator = list.iterator();
	// while (iterator.hasNext()) {
	// if (iterator.next().equals(updater))
	// iterator.remove();
	// }
	// }
	// }

}
