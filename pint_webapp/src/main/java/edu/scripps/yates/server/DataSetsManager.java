package edu.scripps.yates.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class DataSetsManager {
	private static final Map<String, DataSet> dataSetMap = new HashMap<String, DataSet>();
	private final static Logger log = Logger.getLogger(DataSetsManager.class);

	public static DataSet getDataSet(String sessionID, String name) {

		if (!dataSetMap.containsKey(sessionID)) {
			log.info("Creating new dataset '" + name + "' for sessionID: " + sessionID);
			DataSet dataSet = new DataSet(sessionID, name);
			dataSetMap.put(sessionID, dataSet);
			log.info(printStatistics());
			log.info("");
		}
		return dataSetMap.get(sessionID);
	}

	public static void clearDataSet(String sessionID) {
		log.info("Trying to remove dataset from sessionID: " + sessionID);
		if (sessionID != null) {
			if (dataSetMap.containsKey(sessionID)) {

				final DataSet dataSet = dataSetMap.get(sessionID);
				log.info("Removing dataset '" + dataSet.getName() + "' for sessionID: " + sessionID);
				dataSet.clearDataSet();
				dataSetMap.remove(sessionID);

			} else {
				log.info("There was no dataset with sessionID: " + sessionID);
			}
		}
		final String printStatistics = printStatistics();
		log.info(printStatistics);
		log.info("");
	}

	private static String printStatistics() {
		StringBuilder sb = new StringBuilder();
		sb.append("DataSetsManager having " + dataSetMap.size() + " datasets.\n");
		for (String sessionID : dataSetMap.keySet()) {
			sb.append(sessionID + "\t" + dataSetMap.get(sessionID));
		}
		return sb.toString();
	}
}
