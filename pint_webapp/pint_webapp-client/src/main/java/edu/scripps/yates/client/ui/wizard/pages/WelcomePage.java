package edu.scripps.yates.client.ui.wizard.pages;

import java.util.Map;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.WhiteSpace;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.ImportWizardServiceAsync;
import edu.scripps.yates.client.ui.wizard.pages.panels.SelectImportConfFilePanel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.client.util.ClientSafeHtmlUtils;
import edu.scripps.yates.shared.util.SharedConstants;

public class WelcomePage extends AbstractWizardPage {

	private static final String welcomeText = "Welcome to the Project Creator Wizard for PINT\n"
			+ "Here you will be able to upload your proteomics datasets to PINT.";
	private static final String welcomeText2 = "In the bottom of the page you have the navigation buttons in order to move forward and backwards through this process step by step.";
	private static final String welcomeText3 = "The purpose of this is that you can define the experimental design of your project (experimental conditions, MS runs, quantitative ratios, etc...), to be able to upload the files with the actual data and assign them to the proper information in the experimental design.";
	private static final String welcomeText4 = "Click 'Next' to start, or...";
	private final ImportWizardServiceAsync service = ImportWizardServiceAsync.Util.getInstance();

	public WelcomePage() {
		super("Introduction");
	}

	@Override
	protected Widget createPage() {
		final FlowPanel flowPanel = new FlowPanel();
		flowPanel.setStyleName(WizardStyles.wizardWelcome);
		final Label welcomeLabel1 = new Label(welcomeText);
		welcomeLabel1.setStyleName(WizardStyles.WizardWelcomeLabel1);
		flowPanel.add(welcomeLabel1);

		final Label welcomeLabel2 = new Label(welcomeText2);
		welcomeLabel2.setStyleName(WizardStyles.WizardWelcomeLabel2);
		flowPanel.add(welcomeLabel2);

		final Label welcomeLabel3 = new Label(welcomeText3);
		welcomeLabel3.setStyleName(WizardStyles.WizardWelcomeLabel2);
		flowPanel.add(welcomeLabel3);

		final Label welcomeLabel4 = new Label(welcomeText4);
		welcomeLabel4.setStyleName(WizardStyles.WizardWelcomeLabel2);
		welcomeLabel4.getElement().getStyle().setMarginTop(40, Unit.PX);
		flowPanel.add(welcomeLabel4);

		return flowPanel;
	}

	@Override
	public void beforeFirstShow() {
		final SelectImportConfFilePanel panel = new SelectImportConfFilePanel(getWizard());

		final FlowPanel flowPanel = (FlowPanel) asWidget();
		flowPanel.add(panel);
		panel.getElement().getStyle().setMarginTop(20, Unit.PX);

		getImportTemplatesDownloadPanel();
		super.beforeFirstShow();
	}

	private void getImportTemplatesDownloadPanel() {

		final FlowPanel panel = (FlowPanel) asWidget();
		final FlexTable table = new FlexTable();
		panel.add(table);
		table.getElement().getStyle().setMarginTop(30, Unit.PX);
		table.setStyleName(WizardStyles.WizardQuestionPanelLessRoundCorners);
		table.getElement().getStyle().setPaddingTop(20, Unit.PX);
		final Label label1 = new Label("Here you have some import dataset configurations:");
		label1.setStyleName(WizardStyles.WizardWelcomeLabel2);
		int row = 0;
		table.setWidget(row, 0, label1);
//
		row++;
		final Label label2 = new Label(
				"You can download and use them in this webpage to load template dataset import configurations");
		label2.setStyleName(WizardStyles.WizardExplanationLabel);
		table.setWidget(row, 0, label2);
		final int rowForButtons = row;
		service.getTemplateFiles(new AsyncCallback<Map<String, String>>() {

			@Override
			public void onFailure(Throwable caught) {
				// do nothing
			}

			@Override
			public void onSuccess(Map<String, String> files) {
				int row2 = rowForButtons;
				for (final String fileName : files.keySet()) {
					final String datasetDescription = files.get(fileName);
					row2++;
					final FlexTable table2 = new FlexTable();
					table2.setWidth("100%");
					table2.getElement().getStyle().setMarginTop(5, Unit.PX);
					table.setWidget(row2, 0, table2);
					table.getElement().getStyle().setMarginTop(10, Unit.PX);
					final Label button = createDownloadButton(fileName);
					button.getElement().getStyle().setWhiteSpace(WhiteSpace.NOWRAP);
					button.getElement().getStyle().setMarginRight(20, Unit.PX);
					table2.setWidget(0, 0, button);
					table2.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
					table2.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
					final InlineHTML datasetDescriptionLabel = new InlineHTML(
							new SafeHtmlBuilder().appendEscapedLines(datasetDescription).toSafeHtml());
					datasetDescriptionLabel.setStyleName(WizardStyles.WizardInfoMessage);
					table2.setWidget(0, 1, datasetDescriptionLabel);
					table2.getFlexCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
					table2.getFlexCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
					table2.getFlexCellFormatter().setWidth(0, 1, "100%");

				}

			}
		});

	}

	protected Label createDownloadButton(String file) {
		final Label button = new Label(file);
		button.setStyleName(WizardStyles.WizardButtonSmall);
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				final String href = ClientSafeHtmlUtils.getDownloadURL(file, SharedConstants.TEMPLATE);
				com.google.gwt.user.client.Window.open(href, "_blank", "");

			}
		});
		return button;
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
