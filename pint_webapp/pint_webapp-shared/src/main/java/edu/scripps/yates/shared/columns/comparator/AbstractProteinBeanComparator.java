package edu.scripps.yates.shared.columns.comparator;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ProteinBean;

public abstract class AbstractProteinBeanComparator extends AbstractBeanComparator<ProteinBean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6597518799615955368L;

	public AbstractProteinBeanComparator() {
		super();
	}

	public AbstractProteinBeanComparator(ColumnName columnName) {
		super(columnName);
	}

	public AbstractProteinBeanComparator(ColumnName columnName, String scoreName) {
		super(columnName, scoreName);
	}

	public AbstractProteinBeanComparator(ColumnName columnName, String conditionName, AmountType amountType,
			String projectTag) {
		super(columnName, conditionName, amountType, projectTag);
	}

	public AbstractProteinBeanComparator(ColumnName columnName, String condition1Name, String condition2Name, String projectTag,
			String ratioName) {
		super(columnName, condition1Name, condition2Name, projectTag, ratioName);
	}

	public AbstractProteinBeanComparator(ColumnName columnName, String condition1Name, String condition2Name, String projectTag,
			String ratioName, String ratioScoreName) {
		super(columnName, condition1Name, condition2Name, projectTag, ratioName, ratioScoreName);
	}
}
