package edu.scripps.yates.excel.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import edu.scripps.yates.excel.ExcelColumn;

public class ExcelColumnImpl implements ExcelColumn {

	private final String key;
	private final String headerName;
	private final Map<Integer, Object> values = new HashMap<Integer, Object>();
	private int lastNonEmptyRow = 0;
	private boolean isNumerical = true;

	private final Set<String> stringValues = new HashSet<String>();

	public ExcelColumnImpl(String key, String headerName) {
		this.headerName = headerName;
		this.key = key;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getHeader() {
		return headerName;
	}

	@Override
	public List<Object> getValues() {
		List<Object> ret = new ArrayList<Object>();
		for (int i = 1; i <= lastNonEmptyRow; i++) {
			// if (!values.containsKey(i))
			// break;
			ret.add(values.get(i));
		}
		return ret;
	}

	@Override
	public int getLastNonEmptyRow() {
		return lastNonEmptyRow;
	}

	@Override
	public List<Object> getRandomValues(int numValues) {
		Random random = new Random(System.currentTimeMillis());
		int numElements = 0;
		if (numValues > values.size())
			numValues = values.size();
		List<Object> ret = new ArrayList<Object>();
		Set<Integer> indexSet = new HashSet<Integer>();
		while (numElements < numValues) {
			final int index = random.nextInt(values.size()) + 1;
			// don't repeat the index
			if (!indexSet.contains(index) && values.containsKey(index)) {
				indexSet.add(index);
				numElements++;
				ret.add(values.get(index));
			}
		}
		return ret;
	}

	@Override
	public boolean isNumerical() {
		return isNumerical;
	}

	public void addData(int rowIndex, Object cellValue) {
		if (cellValue != null && isNumerical && !(cellValue instanceof Number))
			isNumerical = false;
		if (cellValue != null && lastNonEmptyRow < rowIndex)
			lastNonEmptyRow = rowIndex;
		values.put(rowIndex, cellValue);
		if (cellValue != null)
			stringValues.add(cellValue.toString());
		else
			stringValues.add("");
	}

	@Override
	public boolean isBinary() {
		return stringValues.size() == 2;
	}

}
