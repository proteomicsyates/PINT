package edu.scripps.yates.utilities.util;

public class Pair<T, V> {
	private final T firstElement;
	private final V secondElement;

	public Pair(T firstElement, V secondElement) {
		this.firstElement = firstElement;
		this.secondElement = secondElement;
	}

	public T getFirstelement() {
		return this.firstElement;
	}

	public V getSecondElement() {
		return this.secondElement;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Pair) {
			Pair newPair = (Pair) obj;
			if (newPair.firstElement.equals(firstElement)
					&& newPair.secondElement.equals(secondElement)) {
				return true;
			}
			return false;
		}
		return super.equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + firstElement + "," + secondElement + "]";
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return 0;
	}

}
