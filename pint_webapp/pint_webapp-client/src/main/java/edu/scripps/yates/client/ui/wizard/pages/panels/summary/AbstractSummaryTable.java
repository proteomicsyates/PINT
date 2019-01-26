package edu.scripps.yates.client.ui.wizard.pages.panels.summary;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlexTable;

import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;

public abstract class AbstractSummaryTable extends FlexTable {
	public AbstractSummaryTable(int number) {
		this.setStyleName(WizardStyles.WizardQuestionPanelLessRoundCorners);
		getElement().getStyle().setPaddingTop(10, Unit.PX);
		if (number > 1) {
			getElement().getStyle().setMarginTop(10, Unit.PX);
		}
	}
}
