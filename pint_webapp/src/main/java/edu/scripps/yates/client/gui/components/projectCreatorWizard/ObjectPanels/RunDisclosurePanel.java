package edu.scripps.yates.client.gui.components.projectCreatorWizard.ObjectPanels;

import java.util.Date;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;

import edu.scripps.yates.shared.model.interfaces.HasId;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;

public class RunDisclosurePanel extends ClosableWithTitlePanel {
	private MsRunTypeBean run = new MsRunTypeBean();
	private final TextBox path;
	private final DateBox dateBox;
	private final Format format = new DateBox.DefaultFormat(
			DateTimeFormat.getFormat("MM/dd/yyyy"));
	private static int numRuns = 1;

	public RunDisclosurePanel(String sessionID, int importJobID) {
		super(sessionID, importJobID, "Run " + numRuns++, true);
		// path
		Label pathLabel = new Label("Path:");
		addWidget(pathLabel);
		path = new TextBox();
		path.setVisibleLength(40);
		path.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				fireModificationEvent();
			}
		});
		addWidget(path);
		// date
		Label dateLabel = new Label("Date:");
		addWidget(dateLabel);
		dateBox = new DateBox();
		dateBox.setFormat(format);
		dateBox.setValue(new Date());
		dateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				fireModificationEvent();
			}
		});
		addWidget(dateBox);

		updateRepresentedObject();

		fireModificationEvent();
	}

	@Override
	public MsRunTypeBean getObject() {
		return run;
	}

	@Override
	public void updateRepresentedObject() {
		run.setId(getID());
		run.setDate(dateBox.getValue());
		run.setPath(path.getText());
	}

	@Override
	public void updateGUIFromObjectData(HasId object) {
		if (object instanceof MsRunTypeBean) {
			run = (MsRunTypeBean) object;
			updateGUIFromObjectData();
		}

	}

	@Override
	public void updateGUIFromObjectData() {

		// set name
		setId(run.getId());
		dateBox.setValue(run.getDate());
		path.setText(run.getPath());

	}

	/**
	 * Decrease the counter and then call to editableDisclosurePanel.close()
	 */
	@Override
	public void close() {
		super.close();
	}

	@Override
	protected boolean isValid() {
		updateRepresentedObject();
		final MsRunTypeBean run = getObject();
		if (run.getId() == null || "".equals(run.getId()))
			return false;
		return true;

	}

}
