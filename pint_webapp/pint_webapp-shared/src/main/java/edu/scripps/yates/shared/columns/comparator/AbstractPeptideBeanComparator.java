package edu.scripps.yates.shared.columns.comparator;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PeptideBean;

public abstract class AbstractPeptideBeanComparator extends AbstractBeanComparator<PeptideBean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4531906936062354999L;

	public AbstractPeptideBeanComparator() {
		super();
	}

	public AbstractPeptideBeanComparator(ColumnName columnName) {
		super(columnName);
	}

	public AbstractPeptideBeanComparator(ColumnName columnName, String scoreName) {
		super(columnName, scoreName);
	}

	public AbstractPeptideBeanComparator(ColumnName columnName, String conditionName, AmountType amountType,
			String projectTag) {
		super(columnName, conditionName, amountType, projectTag);
	}

	public AbstractPeptideBeanComparator(ColumnName columnName, String condition1Name, String condition2Name, String projectTag,
			String ratioName) {
		super(columnName, condition1Name, condition2Name, projectTag, ratioName);
	}

	public AbstractPeptideBeanComparator(ColumnName columnName, String condition1Name, String condition2Name, String projectTag,
			String ratioName, String ratioScoreName) {
		super(columnName, condition1Name, condition2Name, projectTag, ratioName, ratioScoreName);
	}
}
