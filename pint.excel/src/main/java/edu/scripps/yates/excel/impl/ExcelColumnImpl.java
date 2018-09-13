package edu.scripps.yates.excel.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import edu.scripps.yates.excel.ExcelColumn;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

public class ExcelColumnImpl implements ExcelColumn {

	private final String key;
	private final String headerName;
	private final TIntObjectHashMap<Object> values = new TIntObjectHashMap<Object>();
	private int lastNonEmptyRow = 0;
	private boolean isNumerical = true;

	private final Set<String> stringValues = new THashSet<String>();
	private ArrayList<Object> list;

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
		if (list == null) {
			list = new ArrayList<Object>();
		}
		if (list.size() != lastNonEmptyRow) {
			list.clear();
			for (int i = 1; i <= lastNonEmptyRow; i++) {
				// if (!values.containsKey(i))
				// break;
				list.add(values.get(i));
			}
		}
		// final List<Object> ret = new ArrayList<Object>();
		// for (int i = 1; i <= lastNonEmptyRow; i++) {
		// // if (!values.containsKey(i))
		// // break;
		// ret.add(values.get(i));
		// }
		// return ret;
		return list;
	}

	@Override
	public int getLastNonEmptyRow() {
		return lastNonEmptyRow;
	}

	@Override
	public List<Object> getRandomValues(int numValues) {
		final Random random = new Random(System.currentTimeMillis());
		int numElements = 0;
		if (numValues > values.size())
			numValues = values.size();
		final List<Object> ret = new ArrayList<Object>();
		final TIntHashSet indexSet = new TIntHashSet();
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

	@Override
	public int getSize() {
		return values.size();
	}

}
