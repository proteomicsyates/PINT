package edu.scripps.yates.client.ui.wizard.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.statusreporter.StatusReporter;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean;

public class SamplesPanel extends FlexTable implements StatusReporter {
	private final Wizard<PintContext> wizard;
	private Label createNewSampleLabel;
	private TextBox createNewSampleTextbox;
	private VerticalPanel createdSamplesPanel;
	private Label createdOrNotSamples;
	private static final String NO_SAMPLES_CREATED_YET = "No samples created yet";

	public SamplesPanel(Wizard<PintContext> wizard) {
		this.wizard = wizard;
		setStyleName(WizardStyles.WizardQuestionPanel);
		init();

	}

	@Override
	public void showMessage(String message) {
		Window.confirm(message);
	}

	@Override
	public void showErrorMessage(Throwable throwable) {
		Window.alert(throwable.getMessage());
	}

	@Override
	public String getStatusReporterKey() {
		return SamplesPanel.class.getName();
	}

	private void editSampleName(boolean edit) {
		if (edit) {
			setWidget(0, 0, createNewSampleTextbox);
			createNewSampleTextbox.setFocus(true);
		} else {
			setWidget(0, 0, createNewSampleLabel);
			try {
				getWidget(0, 1);
			} catch (final IndexOutOfBoundsException e) {
				final Label widget = new Label(" ");
				widget.getElement().getStyle().setWidth(100, Unit.PX);
				setWidget(0, 1, widget);
			}
		}
	}

	private void init() {

		createNewSampleLabel = new Label("Click here to create a new sample");
		createNewSampleLabel.setStyleName(WizardStyles.WizardNewSampleLabel);
		createNewSampleTextbox = new TextBox();
		createNewSampleTextbox.setStyleName(WizardStyles.WizardNewSampleLabel);
		editSampleName(false);

		createNewSampleLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				editSampleName(true);
			}
		});
		// if ESCAPE key, just come back to label
		// if ENTER key, come back to label is no text or create sample if text
		createNewSampleTextbox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
					editSampleName(false);
				} else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER
						|| event.getNativeKeyCode() == KeyCodes.KEY_MAC_ENTER) {
					if ("".equals(createNewSampleTextbox.getValue())) {
						editSampleName(false);
					} else {
						createSample(createNewSampleTextbox.getValue());
						editSampleName(false);
					}
				}
			}
		});
		// if loses focus and it is empty, come back to label
		// if it is not empty, create a new sample and return to label
		createNewSampleTextbox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				GWT.log(" Blur");
				if ("".equals(createNewSampleTextbox.getValue())) {
					editSampleName(false);
				} else {
					createSample(createNewSampleTextbox.getValue());
					editSampleName(false);
				}
			}
		});
		createdSamplesPanel = new VerticalPanel();
		createdSamplesPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		createdOrNotSamples = new Label(NO_SAMPLES_CREATED_YET);
		createdOrNotSamples.setStyleName(WizardStyles.WizardExplanationLabel);
		createdSamplesPanel.add(createdOrNotSamples);
		setWidget(1, 0, createdSamplesPanel);
		getFlexCellFormatter().setColSpan(1, 0, 2);
	}

	protected boolean createSample(String sampleName) {
		final SampleTypeBean sampleObj = new SampleTypeBean();
		sampleObj.setId(sampleName);
		// add it to the cfg object
		try {
			PintImportCfgUtil.addSample(wizard.getContext().getPintImportConfiguration(), sampleObj);
		} catch (final PintException e) {
			e.printStackTrace();
			StatusReportersRegister.getInstance().notifyStatusReporters(e);
			return false;
		}
		final SampleWidget sampleWidget = new SampleWidget(sampleObj);
		sampleWidget.addOnRemoveTask(new DoSomethingTask2<SampleTypeBean>() {

			@Override
			public Void doSomething(SampleTypeBean item) {

				wizard.getContext().getPintImportConfiguration().getProject().getExperimentalDesign().getSampleSet()
						.getSample().remove(item);
				if (wizard.getContext().getPintImportConfiguration().getProject().getExperimentalDesign().getSampleSet()
						.getSample().isEmpty()) {
					createdOrNotSamples.setText(NO_SAMPLES_CREATED_YET);
				}
				createdSamplesPanel.getWidgetIndex(sampleWidget);
				return null;
			}
		});
		createdSamplesPanel.add(sampleWidget);
		createdOrNotSamples.setText("Created samples:");
		return true;
	}

	protected void removeFromCreatedSamples(SampleWidget sampleWidget) {
		// TODO Auto-generated method stub

	}
}
