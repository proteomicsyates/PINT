package edu.scripps.yates.server.daemon.tasks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import edu.scripps.yates.excel.ExcelReader;

public class AlzheimerProjectFDRSetter extends PintServerDaemonTask {
	private final static File finalFDRFile = new File(
			"/home/salvador/PInt/data/Alzheimer dataset/Summary_WithIDs.xlsx");
	private final static Logger log = Logger.getLogger(AlzheimerProjectFDRSetter.class);

	public AlzheimerProjectFDRSetter(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public void run() {
		final Map<Integer, Map<String, Map<String, Double>>> finalFDRsAndPValuesFromTable = getFinalFDRsAndPValuesFromTable();
		for (Map<String, Map<String, Double>> fdrsByProteinsByConditions : finalFDRsAndPValuesFromTable.values()) {
			for (String conditionName : fdrsByProteinsByConditions.keySet()) {
				String conditionNameInDB = translateConditionName(conditionName);
			}
		}
	}

	private String translateConditionName(String conditionName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean justRunOnce() {
		return true;
	}

	public Map<Integer, Map<String, Map<String, Double>>> getFinalFDRsAndPValuesFromTable() {
		ExcelReader excel;
		try {
			excel = new ExcelReader(finalFDRFile, 0, 1);

			Map<Integer, Map<String, Map<String, Double>>> superMap = new HashMap<Integer, Map<String, Map<String, Double>>>();
			for (int sheetNumber = 0; sheetNumber <= 1; sheetNumber++) {
				Map<String, Integer> locusColumnByConditionName = new HashMap<String, Integer>();
				Map<String, Integer> FDRColumnByConditionName = new HashMap<String, Integer>();
				populateColumnsByConditions(excel, sheetNumber, locusColumnByConditionName, FDRColumnByConditionName);

				Map<String, Map<String, Double>> map = new HashMap<String, Map<String, Double>>();
				superMap.put(sheetNumber, map);
				for (String conditionName : locusColumnByConditionName.keySet()) {
					Map<String, Double> proteinFDRMap = getProteinFDRMap(excel, sheetNumber, conditionName,
							locusColumnByConditionName.get(conditionName), FDRColumnByConditionName.get(conditionName));
					map.put(conditionName, proteinFDRMap);
				}
			}

			printMap(excel, superMap);
			return superMap;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void printMap(ExcelReader excel, Map<Integer, Map<String, Map<String, Double>>> superMap)
			throws IOException {
		for (int i = 0; i <= 1; i++) {
			log.info("Experiment: " + excel.getWorkbook().getSheetName(i));
			final Map<String, Map<String, Double>> map = superMap.get(i);
			List<String> conditionNames = new ArrayList<String>();
			conditionNames.addAll(map.keySet());
			Collections.sort(conditionNames);
			for (String conditionName : conditionNames) {
				final Map<String, Double> map2 = map.get(conditionName);
				for (String locus : map2.keySet()) {
					log.info(conditionName + "\t" + locus + "\t" + map2.get(locus));
				}
			}
		}

	}

	private Map<String, Double> getProteinFDRMap(ExcelReader excel, int sheetNumber, String conditionName,
			int locusColumn, int FDRColumn) {
		Map<String, Double> ret = new HashMap<String, Double>();
		int row = 2;
		while (true) {
			String fdrValue = excel.getNumberValue(sheetNumber, row, FDRColumn);
			if (fdrValue != null) {
				String locus = excel.getStringValue(sheetNumber, row, locusColumn);
				ret.put(locus, Double.valueOf(fdrValue));
				row++;
			} else {
				break;
			}

		}
		return ret;
	}

	private void populateColumnsByConditions(ExcelReader excel, int sheetNumber,
			Map<String, Integer> locusColumnByConditionName, Map<String, Integer> FDRColumnByConditionName)
			throws IOException {
		final List<String> columNames = excel.getColumnNames().get(sheetNumber);
		List<String> conditionNames = new ArrayList<String>();
		int index = 0;
		log.info("");
		Map<String, Integer> columnByCondition = new HashMap<String, Integer>();
		for (String columnName : columNames) {
			if (columnName != null) {
				columnByCondition.put(columnName, index);
				conditionNames.add(columnName);
			}
			index++;
		}
		// second row

		String cellValue;
		int conditionCounter = 0;
		for (int col = 0; col < 1000; col++) {
			cellValue = excel.getStringValue(sheetNumber, 1, col);
			if (cellValue != null) {
				if ("locus".equals(cellValue)) {
					locusColumnByConditionName.put(conditionNames.get(conditionCounter), col);
					conditionCounter++;
				}
				if (cellValue.contains("BH")) {
					FDRColumnByConditionName.put(conditionNames.get(conditionCounter - 1), col);
				}
			}
		}
		log.info("Locus columns:");
		for (String conditionName : conditionNames) {
			log.info(conditionName + "\t" + locusColumnByConditionName.get(conditionName));
		}

		log.info("FDR columns:");
		for (String conditionName : conditionNames) {
			log.info(conditionName + "\t" + FDRColumnByConditionName.get(conditionName));
		}

	}
}
