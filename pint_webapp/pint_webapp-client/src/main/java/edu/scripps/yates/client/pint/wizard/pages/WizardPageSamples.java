package edu.scripps.yates.client.pint.wizard.pages;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.ui.wizard.WizardPageHelper;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.client.ui.wizard.view.widget.SamplesPanel;

public class WizardPageSamples extends AbstractWizardPage {

	private static final String text1 = "Now, you need to define the samples that you are using in your dataset.";
	private static final String text2 = "A sample will have some attributes associated to it such as:<ul>"
			+ "<li>organism</li>" + "<li>sample origin (tissue/cell line)</li>"
			+ "<li>label (in case of quantification)</li></ul>";
	private static final String text3 = "For example, in a single 4Plex iTRAQ experiment in which the 2 first channels (114 and 115) are from experimental condition A"
			+ " and the 2 second channels (116 and 117) are from experimental condition B, you will have two options:"
			+ "<ol><li>to define 4 samples one per channel and associate each of them to a different label attribute</li>"
			+ "<li>to define 2 samples, one per condition and either do not associate them with labels or "
			+ "associated them with 2 labels (can be two labels as '114-115' and '116-117'</li></ol> ";
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
		return ret;
	}

	@Override
	public void onPageAdd(WizardPageHelper<PintContext> helper) {
		GWT.log("onPageAdd" + getClass().getName());
		super.onPageAdd(helper);
		final SamplesPanel samplePanel = new SamplesPanel(getWizard());
		panel.setWidget(3, 0, samplePanel);
	}

}
