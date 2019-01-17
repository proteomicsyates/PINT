package edu.scripps.yates.client.ui.wizard.pages;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.ui.wizard.pages.panels.ReferencedOrganismsPanel;
import edu.scripps.yates.client.ui.wizard.pages.panels.ReferencedTissuesPanel;
import edu.scripps.yates.client.ui.wizard.pages.panels.SamplesPanel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;

public class WizardPageSamples extends AbstractWizardPage {

	private static final String text1 = "Now, you need to define the samples that you are using in your dataset.";
	private static final String text2 = "A sample will have some attributes associated to it such as:<ul>"
			+ "<li>organism</li>" + "<li>sample origin (tissue/cell line)</li>"
			+ "<li>label (in case of quantification)</li></ul>";
	private static final String text3 = "For example, in a single 4Plex iTRAQ experiment in which the 2 first channels (114 and 115) are from experimental condition A"
			+ " and the 2 second channels (116 and 117) are from experimental condition B, you will have two options:"
			+ "<ol><li>to define 4 samples one per channel and associate each of them to a different label attribute</li>"
			+ "<li>to define 2 samples, one per condition and either do not associate them with labels or "
			+ "associated them with 2 labels (can be two labels as '114-115' and '116-117')</li></ol> ";
	private FlexTable panel;

	public WizardPageSamples() {
		super("Samples definition");

	}

	@Override
	protected Widget createPage() {
		final SimplePanel ret = new SimplePanel();
		panel = new FlexTable();
		panel.setStyleName(WizardStyles.wizardRegularPage);
		final Label welcomeLabel1 = new Label(text1);
		welcomeLabel1.setStyleName(WizardStyles.WizardRegularText);
		panel.setWidget(0, 0, welcomeLabel1);
		final InlineHTML labelExplanation1 = new InlineHTML(text2);
		labelExplanation1.setStyleName(WizardStyles.WizardExplanationLabel);
		panel.setWidget(1, 0, labelExplanation1);
		final InlineHTML labelExplanation2 = new InlineHTML(text3);
		labelExplanation2.setStyleName(WizardStyles.WizardExplanationLabel);
		panel.setWidget(2, 0, labelExplanation2);
		ret.add(panel);

		// because we have another panel of referenced samples at the right at (3,1)
		panel.getFlexCellFormatter().setColSpan(0, 0, 2);
		panel.getFlexCellFormatter().setColSpan(1, 0, 2);
		panel.getFlexCellFormatter().setColSpan(2, 0, 2);
		return ret;
	}

	@Override
	public void beforeShow() {
		// first column
		final SamplesPanel samplePanel = new SamplesPanel(getWizard());
		panel.setWidget(3, 0, samplePanel);
		panel.getFlexCellFormatter().setVerticalAlignment(3, 0, HasVerticalAlignment.ALIGN_TOP);
		samplePanel.getElement().getStyle().setMarginTop(20, Unit.PX);
		panel.getFlexCellFormatter().setRowSpan(3, 0, 2);
		super.registerItemPanel(samplePanel);

		// second column
		final ReferencedOrganismsPanel referencedOrganismPanel = new ReferencedOrganismsPanel(getWizard());
		panel.setWidget(3, 1, referencedOrganismPanel);
		panel.getFlexCellFormatter().setVerticalAlignment(3, 1, HasVerticalAlignment.ALIGN_TOP);
		referencedOrganismPanel.getElement().getStyle().setMarginTop(20, Unit.PX);
		referencedOrganismPanel.getElement().getStyle().setHeight(1, Unit.PX);

		final ReferencedTissuesPanel referencedTissuesPanel = new ReferencedTissuesPanel(getWizard());
		panel.setWidget(4, 0, referencedTissuesPanel);
		panel.getFlexCellFormatter().setVerticalAlignment(4, 0, HasVerticalAlignment.ALIGN_TOP);
		referencedTissuesPanel.getElement().getStyle().setMarginTop(20, Unit.PX);
		referencedTissuesPanel.getElement().getStyle().setHeight(1, Unit.PX);
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
