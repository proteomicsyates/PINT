package edu.scripps.yates.client.ui.wizard.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.ImportWizardServiceAsync;
import edu.scripps.yates.client.gui.PopUpPanelYesNo;
import edu.scripps.yates.client.gui.components.MyDialogBox;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.ui.wizard.Wizard.ButtonType;
import edu.scripps.yates.client.ui.wizard.event.NavigationEvent;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;

public class WizardPageFinal extends AbstractWizardPage {

	private static final String text1 = "Ok, we have everything we need for now.";
	private static final String text2 = "Now, if you click on 'FINISH' it will submit the import dataset process to PINT.";
	private static final String text3 = "If there is some incosistency in the data, the errors will be shown";
	private static final String text4 = "This process may take some minutes depending on the type and size of the input files.\n"
			+ "At the end of it, if ever a private URL will be displayed. Keep that URL since it will be the only way to access the dataset unless the PINT administrator (with master password) make the dataset public.";
	private FlexTable panel;
	private int rowForNewWidget;
	private final ImportWizardServiceAsync service = ImportWizardServiceAsync.Util.getInstance();
	private MyDialogBox loadingDialog;

	public WizardPageFinal() {
		super("Submit");

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
		final InlineHTML labelExplanation3 = new InlineHTML(text4);
		labelExplanation3.setStyleName(WizardStyles.WizardExplanationLabel);
		panel.setWidget(3, 0, labelExplanation3);
		rowForNewWidget = 4;
		ret.add(panel);
		return ret;
	}

	@Override
	public void beforeFirstShow() {

		getWizard().setButtonEnabled(ButtonType.BUTTON_FINISH, true);
		getWizard().setButtonEnabled(ButtonType.BUTTON_NEXT, false);
		getWizard().setButtonOverride(true);
		getWizard().getButton(ButtonType.BUTTON_FINISH).addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final PopUpPanelYesNo questionPanel = new PopUpPanelYesNo(false, true, true,
						"Dataset submission to PINT", "Are you sure?",
						"By clicking Yes you will submit the dataset to PINT", "Yes", "No");

				questionPanel.addButton1ClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						questionPanel.hide();
						submitDatasetToPINT();
					}

				});
				questionPanel.addButton2ClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						questionPanel.hide();
						questionPanel.hide();
					}
				});
				questionPanel.show();
			}
		});
		super.beforeFirstShow();
	}

	private void submitDatasetToPINT() {
		showLoadingDialog(
				"Please wait while input files are processed and dataset is imported.\nThis may take some minutes...");
		service.submitProject(getImportID(), getPintImportConfg(), new AsyncCallback<String>() {

			@Override
			public void onSuccess(String encryptedDatasetCode) {

				showDatasetSubmissionSuccessfull(encryptedDatasetCode);
				hiddeLoadingDialog();
			}

			@Override
			public void onFailure(Throwable caught) {
				StatusReportersRegister.getInstance().notifyStatusReporters("Some error occurred!");
				addErrorTable(caught);
				hiddeLoadingDialog();
			}
		});
	}

	private void hiddeLoadingDialog() {
		loadingDialog.hide();
	}

	private void showLoadingDialog(String text) {
		if (loadingDialog == null) {
			loadingDialog = new MyDialogBox(text, false, true, true);
		}
		loadingDialog.setText(text);

		loadingDialog.center();
	}

	protected void showDatasetSubmissionSuccessfull(String encryptedDatasetCode) {
		final String privateURL = Window.Location.getProtocol() + "//" + Window.Location.getHost() + "/?project="
				+ encryptedDatasetCode;
		StatusReportersRegister.getInstance()
				.notifyStatusReporters("Dataset '" + getContext().getPintImportConfiguration().getProject().getTag()
						+ "' has been successfully imported to PINT.\n"
						+ "By default, the project will be private, only accesible by this encoded URL:\n" + privateURL
						+ "\n");
		final FlexTable table = new FlexTable();
		table.setCellPadding(10);
		int row = 0;
		table.setStyleName(WizardStyles.WizardQuestionPanelLessRoundCornersGreen);
		final Label label1 = new Label("Dataset imported to PINT!");
		label1.setStyleName(WizardStyles.WizardWelcomeLabel2);
		label1.getElement().getStyle().setPaddingTop(10, Unit.PX);
		table.setWidget(row, 0, label1);
		table.getFlexCellFormatter().setColSpan(row, 0, 2);
		//
		row++;
		final Label label2 = new Label("Dataset identifier tag:");
		label2.setStyleName(WizardStyles.WizardExplanationLabel);
		table.setWidget(row, 0, label2);
		table.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		final Label label3 = new Label(getContext().getPintImportConfiguration().getProject().getTag());
		label3.setStyleName(WizardStyles.WizardExplanationLabel);
		table.setWidget(row, 1, label3);
		table.getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
		//
		row++;
		final Label label4 = new Label("Dataset private URL:");
		label4.setStyleName(WizardStyles.WizardExplanationLabel);
		table.setWidget(row, 0, label4);
		table.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		final Anchor link = new Anchor(privateURL);
		link.setStyleName(WizardStyles.WizardExplanationLabel);
		link.setTarget("_blank");
		link.setTitle("Click here to go to the explore the data in the dataset");
		link.setStyleName("linkPINT");
		link.setHref(privateURL);
		table.setWidget(row, 1, link);
		table.getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
		//
		row++;
		final Label label5 = new Label(
				"Keep this URL in your records since it will be the only way to access your dataset unless the PINT administrator make the dataset public.");
		label5.setStyleName(WizardStyles.WizardExplanationLabel);
		table.setWidget(row, 0, label5);
		table.getFlexCellFormatter().setColSpan(row, 0, 2);
		panel.setWidget(rowForNewWidget, 0, table);
		panel.getFlexCellFormatter().setHorizontalAlignment(rowForNewWidget, 0, HasHorizontalAlignment.ALIGN_CENTER);
		table.getElement().getStyle().setMarginTop(20, Unit.PX);

	}

	protected void addErrorTable(Throwable caught) {
		final FlexTable table = new FlexTable();
		int row = 0;
		table.setStyleName(WizardStyles.WizardQuestionPanelLessRoundCornersRed);
		final Label label1 = new Label("Some error ocurred while trying to submit the dataset '"
				+ getContext().getPintImportConfiguration().getProject().getTag() + "' to PINT:");
		label1.setStyleName(WizardStyles.WizardWelcomeLabel2);
		label1.getElement().getStyle().setPadding(20, Unit.PX);
		table.setWidget(row, 0, label1);
//		table.getFlexCellFormatter().setColSpan(row, 0, 2);
		//
		row++;
		final Label label2 = new Label(caught.getMessage());
		label2.setStyleName(WizardStyles.WizardExplanationLabel);
		label2.getElement().getStyle().setPadding(20, Unit.PX);
		table.setWidget(row, 0, label2);

		//
		if (caught.getCause() != null) {
			row++;
			final Label label3 = new Label("Caused by:");
			label3.setStyleName(WizardStyles.WizardExplanationLabel);
			table.setWidget(row, 0, label3);

			final Label label4 = new Label(caught.getCause().getMessage());
			label4.setStyleName(WizardStyles.WizardExplanationLabel);
			table.setWidget(row, 1, label4);
		}
		GWT.log(caught.getMessage());
		caught.printStackTrace();

		panel.setWidget(rowForNewWidget, 0, table);
		panel.getFlexCellFormatter().setHorizontalAlignment(rowForNewWidget, 0, HasHorizontalAlignment.ALIGN_CENTER);
		table.getElement().getStyle().setMarginTop(20, Unit.PX);

	}

	@Override
	public void beforeNext(NavigationEvent event) {
		getWizard().setButtonEnabled(ButtonType.BUTTON_FINISH, false);
		getWizard().setButtonOverride(true);
		super.beforeNext(event);
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
