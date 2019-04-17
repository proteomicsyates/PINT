package edu.scripps.yates.client.gui.components.progressbar;

import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.scripps.yates.ProteinRetrievalService;
import edu.scripps.yates.ProteinRetrievalServiceAsync;
import edu.scripps.yates.client.gui.components.ProgressBar;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.shared.util.ProgressStatus;

public abstract class ProgressLoadingDialog extends DialogBox {
	protected static final Logger log = Logger.getLogger("PINT");
	private static final int DELAY = 2000;
	private final InlineHTML titleText;
	private final InlineHTML currentTaskText;
	private final VerticalPanel panel;
	protected final ProteinRetrievalServiceAsync service = GWT.create(ProteinRetrievalService.class);
	protected final ProgressBar bar = new ProgressBar(100);
	protected boolean started = false;
	protected boolean finished = false;
	protected com.google.gwt.core.client.Scheduler.RepeatingCommand command;
	private Image imageLoader;
	private Button button;
	private HandlerRegistration buttonHandler;

	public ProgressLoadingDialog(boolean showDynamicBar) {
		this("Progress on task", showDynamicBar, false);
	}

	public ProgressLoadingDialog() {
		this("Progress on task", true, false);
	}

	public ProgressLoadingDialog(String text) {
		this(text, true, false);
	}

	public ProgressLoadingDialog(boolean showDynamicBar, boolean showButton) {
		this("Progress on task", showDynamicBar, showButton);
	}

	public ProgressLoadingDialog(String text, boolean showButton) {
		this(text, true, showButton);
	}

	public ProgressLoadingDialog(String text, boolean showDynamicBar, boolean showButton) {
		super(true, true);

		// Enable animation.
		setAnimationEnabled(true);

		// Enable glass background.
		setGlassEnabled(true);

		panel = new VerticalPanel();
		panel.setSize("100%", "100%");
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		final VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		titleText = new InlineHTML(text);
		titleText.setStyleName("ProgressBarTitle");
		verticalPanel.add(titleText);

		panel.add(verticalPanel);

		final MyClientBundle myClientBundle = MyClientBundle.INSTANCE;
		if (showDynamicBar) {
			imageLoader = new Image(myClientBundle.horizontalLoader());
			imageLoader.getElement().getStyle().setMargin(10, Unit.PX);
			final HorizontalPanel horizontal = new HorizontalPanel();
			horizontal.add(imageLoader);
			horizontal.setCellHorizontalAlignment(imageLoader, HasHorizontalAlignment.ALIGN_CENTER);
			panel.add(horizontal);
		}

		currentTaskText = new InlineHTML("Starting...");
		currentTaskText.setStyleName("ProgressBarTaskDescription");
		panel.add(currentTaskText);
		final VerticalPanel p = new VerticalPanel();
		p.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		p.getElement().getStyle().setWidth(100, Unit.PX);
		p.add(bar);
		panel.add(p);

		bar.setTextVisible(true);
		bar.setProgress(0);

		if (showButton) {
			button = new Button("Close");
			buttonHandler = button.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					ProgressLoadingDialog.this.hide();
				}
			});
			panel.add(button);
		}
		setWidget(panel);
	}

	public void setButtonText(String text) {
		if (button == null) {
			throw new IllegalArgumentException("Dialog has no button!");
		}
		button.setText(text);
	}

	public void addButtonAction(ClickHandler handler) {
		if (button == null) {
			throw new IllegalArgumentException("Dialog has no button!");
		}
		button.addClickHandler(handler);
	}

	public void setButtonAction(ClickHandler handler) {
		if (button == null) {
			throw new IllegalArgumentException("Dialog has no button!");
		}
		if (buttonHandler != null) {
			buttonHandler.removeHandler();
		}
		if (handler != null) {
			button.addClickHandler(handler);
		}
	}

	protected void start() {
		Scheduler.get().scheduleFixedDelay(getRepeatingCommandForAskingProgress(), DELAY);
	}

	protected abstract RepeatingCommand getRepeatingCommandForAskingProgress();

	@Override
	public void setText(String text) {
		titleText.setHTML(new SafeHtmlBuilder().appendEscapedLines(text).toSafeHtml());
	}

	/**
	 * Hide the dialog after a delay stated in milliseconds
	 *
	 * @param delayMillis
	 */
	protected void hideAfter(int delayMillis) {
		final Timer timer = new Timer() {
			@Override
			public void run() {
				hide();
			}
		};
		timer.schedule(delayMillis);
	}

	protected void setStatusOnBar(ProgressStatus progressStatus) {
		if (finished) {
			return;
		}
		if (progressStatus != null) {
			// if maxProgress is different than maxSteps, set the maxProgress
//			if (Long.compare(Double.valueOf(bar.getMaxProgress()).longValue(), progressStatus.getMaxSteps()) != 0) {
//				bar.setProgress(progressStatus.getCurrentStep());
//				bar.setMaxProgress(Double.valueOf(progressStatus.getMaxSteps()));
//				bar.setVisible(true);
//				return;
//			}
			if (progressStatus.getTaskDescription() != null && !"".equals(progressStatus.getTaskDescription())) {
				currentTaskText.setText(progressStatus.getTaskDescription());
			}
			bar.setMaxProgress(Double.valueOf(progressStatus.getMaxSteps()));
			bar.setProgress(Double.valueOf(progressStatus.getCurrentStep()));
			if (progressStatus.getCurrentStep() == progressStatus.getMaxSteps()) {
				finishAndHide(1000);
			}
		}
		started = true;
	}

	private void setStatusAsFinished() {
		bar.setProgress(bar.getMaxProgress());
		finished = true;
	}

	public void finishAndHide() {
		finishAndHide(0);
	}

	public void finishAndHide(int delayMillis) {
		started = false;
		setStatusAsFinished();
		hideAfter(delayMillis);
	}

}
