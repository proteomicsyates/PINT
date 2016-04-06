package edu.scripps.yates.excel;

import java.util.List;

public interface ExcelColumn {
	public String getKey();

	public String getHeader();

	public List<Object> getValues();

	public int getLastNonEmptyRow();

	public List<Object> getRandomValues(int numValues);

	public boolean isNumerical();

	public boolean isBinary();
}
