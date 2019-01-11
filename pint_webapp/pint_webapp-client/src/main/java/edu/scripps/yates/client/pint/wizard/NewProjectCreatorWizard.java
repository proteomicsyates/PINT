package edu.scripps.yates.client.pint.wizard;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.scripps.yates.ImportWizardServiceAsync;
import edu.scripps.yates.client.pint.wizard.pages.WelcomePage;
import edu.scripps.yates.client.pint.wizard.pages.WizardPageInputFiles;
import edu.scripps.yates.client.pint.wizard.pages.WizardPageIsQuantitative;
import edu.scripps.yates.client.pint.wizard.pages.WizardPageProject;
import edu.scripps.yates.client.pint.wizard.pages.WizardPageSamples;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.ui.wizard.Wizard;

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
		addPage(new WizardPageSamples());
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
