package edu.scripps.yates.proteindb.queries.semantic.util;

public abstract class BinaryTree<T> {

	protected BinaryTree<T> leftBNode;
	protected BinaryTree<T> rightBNode; // the nodes
	protected final T element;

	public BinaryTree(T element) {// constructor
		this.element = element;
	}

	// /**
	// * @return the leftBNode
	// */
	// public BinaryTree<T> getLeftBNode() {
	// return leftBNode;
	// }
	//
	// /**
	// * @return the rightBNode
	// */
	// public BinaryTree<T> getRightBNode() {
	// return rightBNode;
	// }

	/**
	 * @return the element
	 */
	public T getElement() {
		return element;
	}

	/**
	 * @param leftBNode
	 *            the leftBNode to set
	 */
	public void setLeftBNode(BinaryTree<T> leftBNode) {
		this.leftBNode = leftBNode;
	}

	/**
	 * @param rightBNode
	 *            the rightBNode to set
	 */
	public void setRightBNode(BinaryTree<T> rightBNode) {
		this.rightBNode = rightBNode;
	}

	public String printInOrder() {
		StringBuilder sb = new StringBuilder();
		if (this.leftBNode != null) {
			if (!"".equals(sb.toString()))
				sb.append(" ");
			sb.append(this.leftBNode.printInOrder());
		}
		if (!"".equals(sb.toString()))
			sb.append(" ");
		sb.append(this.element.toString().trim());
		if (this.rightBNode != null) {
			if (!"".equals(sb.toString()))
				sb.append(" ");
			sb.append(this.rightBNode.printInOrder());
		}
		return sb.toString();
	}

}