package edu.scripps.yates.proteindb.queries.semantic.util;

import java.util.EmptyStackException;
import java.util.Stack;

import org.apache.log4j.Logger;

public abstract class Infix2BinaryTree<T extends BinaryTree<Y>, Y> {
	private static final Logger log = Logger.getLogger(Infix2BinaryTree.class);
	private boolean throwSemanticErrors;

	public Infix2BinaryTree() {
		this(true);
	}

	/**
	 * 
	 * @param provider
	 * @param evaluateQueryExpression
	 *            if false, expressions will not be evaluated for a correct
	 *            syntax
	 */
	public Infix2BinaryTree(boolean throwSemanticErrors) {
		setThrowSemanticErrors(throwSemanticErrors);
	}

	public abstract T createNewBinaryTree(Y yObject);

	public abstract T convertExpresion(String infixExpression) throws Exception;

	protected static String infix2postfix(String infixExpression) {
		StringBuilder postFix = new StringBuilder();
		String n1;
		Stack<String> stack = new Stack<String>();

		stack.clear();
		infixExpression = infixExpression.replace("\n", " ");
		MyCommandTokenizer tokenizer = new MyCommandTokenizer(infixExpression);
		String word = tokenizer.nextToken();
		while (word != null) {
			if (!MyCommandTokenizer.isOperator(word)
					&& !MyCommandTokenizer.isParenthesis(word)) {
				postFix.append(word + " ");
				word = nextFromTokenizer(tokenizer); // next word
			}
			if ("(".equals(word)) {
				stack.push(word);
				word = nextFromTokenizer(tokenizer); // next word
			}
			if (")".equals(word)) {
				n1 = popFromStack(stack);
				while (!"".equals(n1) && !"(".equals(n1)) {
					postFix.append(n1 + " ");
					n1 = popFromStack(stack);
				}
				word = nextFromTokenizer(tokenizer); // next word
			}

			if (MyCommandTokenizer.isOperator(word)) {
				if (stack.isEmpty()) {
					stack.push(word);
				} else {
					n1 = popFromStack(stack);
					while (MyCommandTokenizer.priority(n1) >= MyCommandTokenizer
							.priority(word)) {
						postFix.append(n1 + " ");
						n1 = popFromStack(stack);
					}
					stack.push(n1);
					stack.push(word);
				}
				word = nextFromTokenizer(tokenizer); // next word
			}
		}
		while (!stack.isEmpty()) {
			n1 = popFromStack(stack);
			postFix.append(n1 + " ");
		}

		String ret = postFix.toString();
		log.info("Expression '" + infixExpression
				+ "' converted to postfix as:'" + ret + "'");
		return ret;
	}

	private static String nextFromTokenizer(MyCommandTokenizer tokenizer) {
		try {
			return tokenizer.nextToken();
		} catch (Exception e) {
			return null;
		}
	}

	private static String popFromStack(Stack<String> stack) {
		try {
			return stack.pop();
		} catch (EmptyStackException e) {
			return "";
		}
	}

	protected abstract Y translate(String word) throws Exception;

	/**
	 * @return the throwSemanticErrors
	 */
	public boolean isThrowSemanticErrors() {
		return throwSemanticErrors;
	}

	/**
	 * @param throwSemanticErrors
	 *            the throwSemanticErrors to set
	 */
	public void setThrowSemanticErrors(boolean throwSemanticErrors) {
		this.throwSemanticErrors = throwSemanticErrors;
	}
}
