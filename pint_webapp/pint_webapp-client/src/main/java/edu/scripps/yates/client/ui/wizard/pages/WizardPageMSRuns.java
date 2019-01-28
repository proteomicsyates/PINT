package edu.scripps.yates.client.ui.wizard.pages;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.ui.wizard.pages.panels.MSRunsPanel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.exceptions.PintException;

public class WizardPageMSRuns extends AbstractWizardPage {

	private static final String text1 = "Here you have to define the MS runs of your experiment.";
	private static final String text2 = "A MS run can serve to differentiate samples analyzed under the same experimental conditions as technical or biological replicates. "
			+ "However, it can also be associated with multiple samples, like in a quantitative MS run that compares 2 or more samples.";
	private static final String text3 = "Here you just create the MS runs...we will figure out later how to associate them with the data in the input files, the experimental conditions and samples.";
	private FlexTable panel;

	public WizardPageMSRuns() {
		super("MS runs");
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
		return ret;
	}

	@Override
	public void beforeFirstShow() {
		final MSRunsPanel msrunPanel = new MSRunsPanel(getWizard());
		super.registerItemPanel(msrunPanel);
		panel.setWidget(3, 0, msrunPanel);
		super.beforeFirstShow();
	}

	@Override
	public PageID getPageID() {
		return PageIDController.getPageIDByPageClass(this.getClass());
	}

	@Override
	protected void registerPageTitle(String title) {
		PageTitleController.addPageTitle(this.getPageID(), title);
	}

	@Override
	public boolean isReady() throws PintException {
		// TODO Auto-generated method stub
		return super.isReady();
	}

//	@Override
//	public void beforeNext(NavigationEvent event) {
//		// jump over the transitional to the next page...trying to add it before if
//		// doesn't exist
//		final PageID summaryPageID = PageIDController.getPageIDByPageClass(WizardPageSummary1.class);
//		final PageID transitionalPageID = PageIDController.getPageIDByPageClass(WizardPageTransitional.class);
//		try {
//			final WizardPageSummary1 summaryPage = new WizardPageSummary1();
//			wizard.addPage(summaryPage);
//			// create link from MSRuns to Summary
//			final PageID msRunsPageID = getPageID();
//			wizard.getLinkManager().destroyLinksBetween(msRunsPageID, transitionalPageID, true);
//			wizard.getLinkManager().createTwoWayLink(msRunsPageID, summaryPageID);
//		} catch (final DuplicatePageException e) {
//			// we already added this page
//		}
//		event.cancel();
//		wizard.showPage(summaryPageID, false, true, true);
//	}

}
