package edu.scripps.yates.client.pint.wizard;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.scripps.yates.ImportWizardServiceAsync;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.pages.WelcomePage;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageConditions;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageInputFiles;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageIsQuantitative;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageOrganisms;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageProject;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageSamples;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageTissues;

public class NewProjectCreatorWizard extends Wizard<PintContext> {
	private final ImportWizardServiceAsync service = ImportWizardServiceAsync.Util.getInstance();

	public NewProjectCreatorWizard(String sessionID) {
		super("PINT project creator wizard", new PintContext(sessionID));
		setupWizard();
	}

	public NewProjectCreatorWizard(edu.scripps.yates.client.ui.wizard.Wizard.Display view, String sessionID) {
		super("PINT project creator wizard", new PintContext(sessionID), view);
		setupWizard();
	}

	private void setupWizard() {
		addPage(new WelcomePage());
		addPage(new WizardPageProject());
		addPage(new WizardPageInputFiles());
		addPage(new WizardPageIsQuantitative());
		addPage(new WizardPageOrganisms());
		addPage(new WizardPageTissues());
		addPage(new WizardPageSamples());
		addPage(new WizardPageConditions());
		setUseLazyPageLoading(true);
		setButtonVisible(ButtonType.BUTTON_CANCEL, true);
		// finish
		getButton(ButtonType.BUTTON_FINISH).addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				service.validatePintImportCfg(getContext().getPintImportConfiguration(), new AsyncCallback<String>() {

					@Override
					public void onSuccess(String result) {
						StatusReportersRegister.getInstance().notifyStatusReporters("All is Ok. File at: " + result);

					}

					@Override
					public void onFailure(Throwable caught) {
						StatusReportersRegister.getInstance().notifyStatusReporters(caught);

					}
				});

			}
		});
	}

}
