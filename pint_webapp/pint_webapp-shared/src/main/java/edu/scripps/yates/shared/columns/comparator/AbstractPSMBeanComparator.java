package edu.scripps.yates.shared.columns.comparator;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PSMBean;

public abstract class AbstractPSMBeanComparator extends AbstractBeanComparator<PSMBean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2108753071239421434L;

	public AbstractPSMBeanComparator() {
		super();
	}

	public AbstractPSMBeanComparator(ColumnName columnName) {
		super(columnName);
	}

	public AbstractPSMBeanComparator(ColumnName columnName, String scoreName) {
		super(columnName, scoreName);
	}

	public AbstractPSMBeanComparator(ColumnName columnName, String conditionName, AmountType amountType, String projectTag) {
		super(columnName, conditionName, amountType, projectTag);
	}

	public AbstractPSMBeanComparator(ColumnName columnName, String condition1Name, String condition2Name, String projectTag,
			String ratioName) {
		super(columnName, condition1Name, condition2Name, projectTag, ratioName);
	}

	public AbstractPSMBeanComparator(ColumnName columnName, String condition1Name, String condition2Name, String projectTag,
			String ratioName, String ratioScoreName) {
		super(columnName, condition1Name, condition2Name, projectTag, ratioName, ratioScoreName);
	}
}
