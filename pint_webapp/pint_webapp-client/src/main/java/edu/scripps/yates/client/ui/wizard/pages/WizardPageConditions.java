package edu.scripps.yates.client.ui.wizard.pages;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.ui.wizard.pages.panels.ConditionsPanel;
import edu.scripps.yates.client.ui.wizard.pages.panels.ReferencedSamplesPanel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;

public class WizardPageConditions extends AbstractWizardPage {

	private static final String text1 = "Now, you need to define the experimental conditions in your dataset.";
	private static final String text2 = "If the experiment is not quantitative you may want just to create one experimental condition, but at least one has to be created";
	private static final String text3 = "If the experiment is quantitative, you must create one experimental condition per different sample that you are comparing.\n"
			+ "Remember that is a one-to-one association, and therefore if you have several samples for the same experimental condition, "
			+ "you will probably need to just create one sample and then create multiple MSRuns per sample with the same condition.";
	private FlexTable panel;

	public WizardPageConditions() {
		super("Experimental conditions");

	}

	@Override
	protected Widget createPage() {
		final SimplePanel ret = new SimplePanel();
		panel = new FlexTable();
		panel.setStyleName(WizardStyles.wizardRegularPage);
		final Label welcomeLabel1 = new Label(text1);
		welcomeLabel1.setStyleName(WizardStyles.WizardWelcomeLabel2);
		panel.setWidget(0, 0, welcomeLabel1);
		final InlineHTML labelExplanation1 = new InlineHTML(text2);
		labelExplanation1.setStyleName(WizardStyles.WizardExplanationLabel);
		panel.setWidget(1, 0, labelExplanation1);
		final InlineHTML labelExplanation2 = new InlineHTML(text3);
		labelExplanation2.setStyleName(WizardStyles.WizardExplanationLabel);
		panel.setWidget(2, 0, labelExplanation2);
		ret.add(panel);
		// because we have another panel of referenced samples at the right at 3,1
		panel.getFlexCellFormatter().setColSpan(0, 0, 2);
		panel.getFlexCellFormatter().setColSpan(1, 0, 2);
		panel.getFlexCellFormatter().setColSpan(2, 0, 2);
		return ret;
	}

	@Override
	public void beforeFirstShow() {
		final ConditionsPanel experimentalConditionsPanel = new ConditionsPanel(getWizard());
		panel.setWidget(3, 0, experimentalConditionsPanel);
		panel.getFlexCellFormatter().setVerticalAlignment(3, 0, HasVerticalAlignment.ALIGN_TOP);
		experimentalConditionsPanel.getElement().getStyle().setMarginTop(20, Unit.PX);
		super.registerItemPanel(experimentalConditionsPanel);

		final ReferencedSamplesPanel referencedSamplesPanel = new ReferencedSamplesPanel(getWizard());
		panel.setWidget(3, 1, referencedSamplesPanel);
		panel.getFlexCellFormatter().setVerticalAlignment(3, 1, HasVerticalAlignment.ALIGN_TOP);
		referencedSamplesPanel.getElement().getStyle().setMarginTop(20, Unit.PX);

		super.beforeShow();
	}

	@Override
	public PageID getPageID() {
		return PageIDController.getPageIDByPageClass(this.getClass());
	}

	@Override
	protected void registerPageTitle(String title) {
		PageTitleController.addPageTitle(this.getPageID(), title);
	}
}
