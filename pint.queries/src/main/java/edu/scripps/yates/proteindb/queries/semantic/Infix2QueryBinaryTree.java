package edu.scripps.yates.proteindb.queries.semantic;

import java.util.EmptyStackException;
import java.util.Stack;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.util.Infix2BinaryTree;
import edu.scripps.yates.proteindb.queries.semantic.util.MyCommandTokenizer;

public class Infix2QueryBinaryTree extends Infix2BinaryTree<QueryBinaryTree, QueryBinaryTreeElement> {
	private final SemanticTranslator semanticTranslator;
	private final static Logger log = Logger.getLogger(Infix2QueryBinaryTree.class);

	public Infix2QueryBinaryTree() {
		super(true);
		semanticTranslator = new SemanticTranslator();
	}

	@Override
	public QueryBinaryTreeElement translate(String expression) throws MalformedQueryException {
		try {
			return new QueryBinaryTreeElement(semanticTranslator.translate(expression));
		} catch (MalformedQueryException e) {

			log.warn(e.getMessage());
			if (isThrowSemanticErrors()) {
				e.printStackTrace();
				throw new MalformedQueryException(e.getMessage());
			}
			return null;
		}
	}

	@Override
	public QueryBinaryTree createNewBinaryTree(QueryBinaryTreeElement treeElement) {
		return new QueryBinaryTree(treeElement.getAbstractQuery());
	}

	@Override
	public QueryBinaryTree convertExpresion(String infixExpression) throws MalformedQueryException {

		String postFixExpression = infix2postfix(infixExpression);
		Stack<QueryBinaryTree> stack = new Stack<QueryBinaryTree>();
		QueryBinaryTree newNode;
		QueryBinaryTree op1;
		QueryBinaryTree op2;
		String p;

		stack.clear();
		MyCommandTokenizer tokenizer = new MyCommandTokenizer(postFixExpression);
		while (tokenizer.hasMoreElements()) {
			final String word = tokenizer.nextToken();
			if (!MyCommandTokenizer.isOperator(word)) {
				// create new BNode
				newNode = createNewBinaryTree(translate(word));
				// insert in stack
				stack.push(newNode);
			} else {
				op1 = null;
				op2 = null;
				try {
					op1 = stack.pop();
				} catch (EmptyStackException e) {

				}
				try {
					op2 = stack.pop();
				} catch (EmptyStackException e) {

				}
				newNode = new QueryBinaryTree(word);
				newNode.setLeftBNode(op2);
				newNode.setRightBNode(op1);
				stack.push(newNode);
			}
		}
		QueryBinaryTree ret = stack.pop();
		log.info(
				"Expression \n'" + infixExpression + "'\nconverted to a binary tree as:'\n" + ret.printInOrder() + "'");
		return ret;
	}
}
