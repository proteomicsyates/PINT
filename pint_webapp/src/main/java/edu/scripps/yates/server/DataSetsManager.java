package edu.scripps.yates.server;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import gnu.trove.map.hash.THashMap;

public class DataSetsManager {
	private static final Map<String, DataSet> dataSetMap = new THashMap<String, DataSet>();
	private final static Logger log = Logger.getLogger(DataSetsManager.class);

	public static DataSet getDataSet(String sessionID, String name, boolean createIfNotExist, boolean psmCentric) {

		if (createIfNotExist && (!dataSetMap.containsKey(sessionID) || dataSetMap.get(sessionID) == null)) {
			log.info("Creating new dataset '" + name + "' for sessionID: " + sessionID + " from thread "
					+ Thread.currentThread().getId());
			final DataSet dataSet = new DataSet(sessionID, name, psmCentric);
			dataSetMap.put(sessionID, dataSet);
			log.info(printStatistics());
			log.info("");
		}
		final DataSet ret = dataSetMap.get(sessionID);
		if (ret == null) {
			return DataSet.emptyDataSet();
		}
		ret.setLatestAccess(new Date());
		if (name != null) {
			ret.setName(name);
		}
		return ret;
	}

	/**
	 * Clears the content of a dataset in the static map of datasets and sends a
	 * interrupt signal to the thread that is working on that dataset
	 * 
	 * @param sessionID
	 */
	public static void clearDataSet(String sessionID) {

		if (sessionID != null) {
			if (dataSetMap.containsKey(sessionID)) {

				final DataSet dataSet = dataSetMap.get(sessionID);
				// get, if any, any thread that is ongoing building the dataset
				// for that session.
				// if exists, it is because the user went away from one dataset
				// and loaded the other. So that, we want to cancel that thread.
				try {
					final Thread thread = dataSet.getActiveDatasetThread();
					if (thread != Thread.currentThread()) {
						log.info("Interrumping previous thread '" + thread.getId()
								+ "' loading a different dataset for this session ID: " + sessionID);
						thread.interrupt();
					}
				} catch (final SecurityException e) {
					e.printStackTrace();
				}
				log.info("Clearing dataset '" + dataSet.getName() + "' from sessionID: " + sessionID);
				dataSet.clearDataSet();

			} else {
				log.info("There is no dataset with sessionID: " + sessionID);
			}
		}
		final String printStatistics = printStatistics();
		log.info(printStatistics);
		log.info("");
	}

	public static void removeDataSet(String sessionID) {
		if (sessionID != null) {
			if (dataSetMap.containsKey(sessionID)) {
				final DataSet dataSet = dataSetMap.get(sessionID);
				log.info("Removing dataset '" + dataSet.getName() + "' for sessionID: " + sessionID);
				dataSetMap.remove(sessionID);
			}
		}
	}

	private static String printStatistics() {
		final StringBuilder sb = new StringBuilder();
		sb.append("DataSetsManager having " + dataSetMap.size() + " datasets.\n");
		for (final String sessionID : dataSetMap.keySet()) {
			final DataSet dataSet = dataSetMap.get(sessionID);
			sb.append(sessionID + "\t" + dataSet + "\n");
		}
		return sb.toString();
	}
}
