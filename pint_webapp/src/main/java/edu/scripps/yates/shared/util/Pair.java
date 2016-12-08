package edu.scripps.yates.shared.util;

import java.io.Serializable;

public class Pair<T, Y> implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -8060848097661870291L;
	private T firstElement;
	private Y secondElement;

	public Pair(T firstElement, Y secondElement) {
		this.firstElement = firstElement;
		this.secondElement = secondElement;
	}

	public T getFirstElement() {
		return this.firstElement;
	}

	public Y getSecondElement() {
		return secondElement;
	}

	public void setFirstElement(T object) {
		this.firstElement = object;
	}

	public void setSecondElement(Y object) {
		this.secondElement = object;
	}

}
