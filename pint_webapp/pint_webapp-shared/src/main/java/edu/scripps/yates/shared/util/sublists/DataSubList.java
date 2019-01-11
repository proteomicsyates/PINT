package edu.scripps.yates.shared.util.sublists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class DataSubList<T> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3642483558752708395L;

	private List<T> dataList = new ArrayList<T>();
	private int totalNumber;

	protected DataSubList() {

	}

	protected DataSubList(List<T> data, int totalNumber) {
		this.dataList.addAll(data);
		this.totalNumber = totalNumber;
	}

	/**
	 * @return the proteins
	 */
	public List<T> getDataList() {
		return dataList;
	}

	/**
	 * @return the totalNumber
	 */
	public int getTotalNumber() {
		return totalNumber;
	}

	/**
	 * @param proteins
	 *            the proteins to set
	 */
	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	/**
	 * @param totalNumber
	 *            the totalNumber to set
	 */
	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}

	public long size() {
		return dataList.size();
	}

	public boolean isEmpty() {
		return this.dataList.isEmpty();
	}
}
