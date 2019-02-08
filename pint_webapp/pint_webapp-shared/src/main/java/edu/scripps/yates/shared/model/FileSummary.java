package edu.scripps.yates.shared.model;

import java.io.Serializable;
import java.util.Map;

import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public class FileSummary implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6699480477559237262L;
	private int numProteins;
	private int numPeptides;
	private int numPSMs;
	private FileTypeBean fileTypeBean;
	private String fileSizeString;
	private int numSheets;
	private Map<String, Integer> sheetMap;

	public FileSummary() {

	}

	public FileSummary(int numProteins, int numPeptides, int numPSMs) {
		this.numProteins = numProteins;
		this.numPeptides = numPeptides;
		this.numPSMs = numPSMs;
	}

	public int getNumProteins() {
		return numProteins;
	}

	public void setNumProteins(int numProteins) {
		this.numProteins = numProteins;
	}

	public int getNumPeptides() {
		return numPeptides;
	}

	public void setNumPeptides(int numPeptides) {
		this.numPeptides = numPeptides;
	}

	public int getNumPSMs() {
		return numPSMs;
	}

	public void setNumPSMs(int numPSMs) {
		this.numPSMs = numPSMs;
	}

	public void setFileTypeBean(FileTypeBean fileTypeBean) {
		this.fileTypeBean = fileTypeBean;
	}

	public void setFileSizeString(String descriptiveSizeFromBytes) {
		this.fileSizeString = descriptiveSizeFromBytes;

	}

	public FileTypeBean getFileTypeBean() {
		return fileTypeBean;
	}

	public String getFileSizeString() {
		return fileSizeString;
	}

	public void setNumSheets(int size) {
		numSheets = size;
	}

	protected int getNumSheets() {
		return numSheets;
	}

	public void setSheetMap(Map<String, Integer> sheetMap) {
		this.sheetMap = sheetMap;

	}

	protected Map<String, Integer> getSheetMap() {
		return sheetMap;
	}
}
