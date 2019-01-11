package edu.scripps.yates.shared.columns.comparator;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ProteinGroupBean;

public abstract class AbstractProteinGroupBeanComparator extends AbstractBeanComparator<ProteinGroupBean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5930789168085661414L;

	public AbstractProteinGroupBeanComparator() {
		super();
	}

	public AbstractProteinGroupBeanComparator(ColumnName columnName) {
		super(columnName);
	}

	public AbstractProteinGroupBeanComparator(ColumnName columnName, String scoreName) {
		super(columnName, scoreName);
	}

	public AbstractProteinGroupBeanComparator(ColumnName columnName, String conditionName, AmountType amountType,
			String projectTag) {
		super(columnName, conditionName, amountType, projectTag);
	}

	public AbstractProteinGroupBeanComparator(ColumnName columnName, String condition1Name, String condition2Name,
			String projectTag, String ratioName) {
		super(columnName, condition1Name, condition2Name, projectTag, ratioName);
	}

	public AbstractProteinGroupBeanComparator(ColumnName columnName, String condition1Name, String condition2Name,
			String projectTag, String ratioName, String ratioScoreName) {
		super(columnName, condition1Name, condition2Name, projectTag, ratioName, ratioScoreName);
	}
}
