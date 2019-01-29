package edu.scripps.yates.client.ui.wizard.pages;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.ImportWizardServiceAsync;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.ui.wizard.Wizard.ButtonType;
import edu.scripps.yates.client.ui.wizard.form.ProjectForm;
import edu.scripps.yates.client.ui.wizard.pages.panels.WizardFormPanel;
import edu.scripps.yates.client.ui.wizard.WizardPageHelper;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;

public class WizardPageProject extends AbstractWizardPage {

	private static final String text1 = "First things first... how do you want to call this dataset?";
	private final ImportWizardServiceAsync service = ImportWizardServiceAsync.Util.getInstance();
	private FlexTable panel;
	private WizardFormPanel textFormPanel;

	public WizardPageProject() {
		super("Dataset name");

	}

	@Override
	protected Widget createPage() {
		final String text0 = "Wait until a new import process is registered...";
		final SimplePanel ret = new SimplePanel();
		panel = new FlexTable();
		ret.setWidget(panel);
		panel.setStyleName(WizardStyles.wizardRegularPage);
		final Label label0 = new Label(text0);
		label0.setStyleName(WizardStyles.WizardWelcomeLabel2);
		panel.setWidget(0, 0, label0);

		return ret;
	}

	protected Widget createInteractivePage() {
		panel.clear();
		final Label label0 = new Label("Import process registered with ID: " + getPintImportConfg().getImportID());
		label0.setStyleName(WizardStyles.WizardExplanationLabel);
		panel.setWidget(0, 0, label0);

		final Label label1 = new Label(text1);
		label1.setStyleName(WizardStyles.WizardWelcomeLabel2);
		panel.setWidget(1, 0, label1);

		final ProjectForm projectTextForm = new ProjectForm(getContext());
		textFormPanel = new WizardFormPanel(projectTextForm, getWizard());

		panel.setWidget(2, 0, textFormPanel);

		return panel;
	}

	@Override
	public void onPageAdd(WizardPageHelper<PintContext> helper) {
		GWT.log("onPageAdd " + getClass().getName());
		super.onPageAdd(helper);

		// set ID
		service.generateNewImportProcessID(new AsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer importID) {
				getPintImportConfg().setImportID(importID);
				createInteractivePage();
			}

			@Override
			public void onFailure(Throwable caught) {
				StatusReportersRegister.getInstance().notifyStatusReporters(caught);

			}
		});
	}

	@Override
	public void beforeFirstShow() {
		// disable next until user response
		super.getWizard().setButtonEnabled(ButtonType.BUTTON_NEXT, false);
		// we have to override this, otherwise it doesnt work
		getWizard().setButtonOverride(true);
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
