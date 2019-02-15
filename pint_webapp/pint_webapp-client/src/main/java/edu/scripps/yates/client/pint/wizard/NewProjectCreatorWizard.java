package edu.scripps.yates.client.pint.wizard;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;

import edu.scripps.yates.ImportWizardServiceAsync;
import edu.scripps.yates.client.gui.PopUpPanelYesNo;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.pages.WelcomePage;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageConditions;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageInputFiles;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageLabels;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageMSRuns;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageOrganisms;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageProject;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageSamples;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageSummary1;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageTissues;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.client.util.ClientSafeHtmlUtils;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;
import edu.scripps.yates.shared.util.SharedConstants;

public class NewProjectCreatorWizard extends Wizard<PintContext> {
	private final ImportWizardServiceAsync service = ImportWizardServiceAsync.Util.getInstance();
	private Label saveProgressButton;

	public NewProjectCreatorWizard(String sessionID) {
		super("PINT project creator wizard", new PintContext(sessionID));
		setupWizard();
	}

	public NewProjectCreatorWizard(edu.scripps.yates.client.ui.wizard.Wizard.Display view, String sessionID) {
		super("PINT project creator wizard", new PintContext(sessionID), view);
		setupWizard();
	}

	public void showSaveProgressButton(boolean show) {
		this.saveProgressButton.setVisible(show);
	}

	private void setupWizard() {
		// add to the right of the title a button to save progress

		saveProgressButton = new Label("Save progress");
		showSaveProgressButton(false); // show when receiving import id
		saveProgressButton.setStyleName(WizardStyles.WizardButtonSmall);
		getTitlePanel().add(saveProgressButton);
		getTitlePanel().setCellVerticalAlignment(saveProgressButton, HasVerticalAlignment.ALIGN_MIDDLE);
		getTitlePanel().getElement().getStyle().setBackgroundColor("#e9f8f7");
		saveProgressButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// send conf to server
				final PintImportCfgBean pintImportConfiguration = getContext().getPintImportConfiguration();
				PintImportCfgUtil.removeNonUsedItems(pintImportConfiguration);
				service.validatePintImportCfg(pintImportConfiguration, new AsyncCallback<String>() {

					@Override
					public void onSuccess(String result) {
						// download conf
						// return an URL to download the file:
						final String fileName = SafeHtmlUtils.htmlEscape(result);

						final String href = ClientSafeHtmlUtils.getDownloadURL(fileName,
								SharedConstants.IMPORT_CFG_FILE_TYPE);
						com.google.gwt.user.client.Window.open(href, "_blank", "");
					}

					@Override
					public void onFailure(Throwable caught) {
						StatusReportersRegister.getInstance().notifyStatusReporters(caught);

					}
				});

			}
		});

		setUseLazyPageLoading(false);
		addPage(new WelcomePage());
		addPage(new WizardPageProject());
		addPage(new WizardPageInputFiles());
//		addPage(new WizardPageIsQuantitative());
		addPage(new WizardPageOrganisms());
		addPage(new WizardPageTissues());
		addPage(new WizardPageLabels());
		addPage(new WizardPageSamples());
		addPage(new WizardPageConditions());
		addPage(new WizardPageMSRuns());
		addPage(new WizardPageSummary1());

		setButtonVisible(ButtonType.BUTTON_CANCEL, true);
		// finish
		getButton(ButtonType.BUTTON_FINISH).setEnabled(false);
		// cancel
		getButton(ButtonType.BUTTON_CANCEL).addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final PopUpPanelYesNo questionPanel = new PopUpPanelYesNo(false, true, true, "Cancel import process",
						"Are you sure you want to cancel the import process?",
						"If you cancel now, you will lose the progress in this import dataset process.\n"
								+ "You can save it by clicking on the top-right corner button and downloading the import dataset configuration.",
						"Yes", "No");

				questionPanel.addButton1ClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						// process cancelled
						final NewProjectCreatorWizard wizard = new NewProjectCreatorWizard(getContext().getSessionID());
						RootLayoutPanel.get().add(wizard);
						questionPanel.hide();
					}
				});
				questionPanel.addButton2ClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						questionPanel.hide();
					}
				});
				questionPanel.show();
			}
		});
		getButton(ButtonType.BUTTON_CANCEL).setEnabled(false); // disable until showing saving button
	}

}
