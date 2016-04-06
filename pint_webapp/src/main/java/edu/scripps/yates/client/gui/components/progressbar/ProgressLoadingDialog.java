package edu.scripps.yates.client.gui.components.progressbar;

import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.widgetideas.client.ProgressBar;

import edu.scripps.yates.client.ProteinRetrievalServiceAsync;
import edu.scripps.yates.client.ProteinRetrievalService;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.shared.util.ProgressStatus;

public abstract class ProgressLoadingDialog extends DialogBox {
	protected static final Logger log = Logger.getLogger("PINT");
	private static final int DELAY = 1000;
	private final InlineHTML inlineHTML;
	private final VerticalPanel panel;
	protected final ProteinRetrievalServiceAsync service = GWT.create(ProteinRetrievalService.class);
	protected final ProgressBar bar = new ProgressBar(100);
	protected boolean started = false;
	protected com.google.gwt.core.client.Scheduler.RepeatingCommand command;
	private Image imageLoader;

	public ProgressLoadingDialog(boolean showDynamicBar) {
		this("Progress on task", showDynamicBar);
	}

	public ProgressLoadingDialog() {
		this("Progress on task", true);
	}

	public ProgressLoadingDialog(String text) {
		this(text, true);
	}

	public ProgressLoadingDialog(String text, boolean showDynamicBar) {
		super(true, true);

		// Enable animation.
		setAnimationEnabled(true);

		// Enable glass background.
		setGlassEnabled(true);

		panel = new VerticalPanel();
		panel.setSize("100%", "100%");
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		inlineHTML = new InlineHTML(text);
		inlineHTML.setWidth("100%");
		panel.add(inlineHTML);

		final MyClientBundle myClientBundle = MyClientBundle.INSTANCE;
		if (showDynamicBar) {
			imageLoader = new Image(myClientBundle.roundedLoader());
			panel.add(imageLoader);
		}

		panel.add(bar);

		bar.setTextVisible(true);
		bar.setProgress(0);
		setWidget(panel);
	}

	protected void start() {
		Scheduler.get().scheduleFixedDelay(getRepeatingCommand(), DELAY);
	}

	protected abstract RepeatingCommand getRepeatingCommand();

	@Override
	public void setText(String text) {
		inlineHTML.setHTML(new SafeHtmlBuilder().appendEscapedLines(text).toSafeHtml());
	}

	/**
	 * Hide the dialog after a delay stated in milliseconds
	 *
	 * @param delayMg
	 */
	protected void hideAfter(int delayMg) {
		Timer timer = new Timer() {
			@Override
			public void run() {
				hide();
			}
		};
		timer.schedule(delayMg);
	}

	protected void setStatusOnBar(ProgressStatus progressStatus) {
		if (progressStatus != null) {
			if (Long.compare(Double.valueOf(bar.getMaxProgress()).longValue(), progressStatus.getMaxSteps()) != 0) {
				bar.setProgress(progressStatus.getCurrentStep());
				bar.setMaxProgress(Double.valueOf(progressStatus.getMaxSteps()));
				bar.setVisible(true);

			}
			if (progressStatus.getTaskDescription() != null && !"".equals(progressStatus.getTaskDescription())) {
				inlineHTML.setText(progressStatus.getTaskDescription());
			}
			bar.setMaxProgress(Double.valueOf(progressStatus.getMaxSteps()));
			bar.setProgress(Double.valueOf(progressStatus.getCurrentStep()));
			started = true;
		}
	}

	public void setStatusAsFinished() {
		bar.setProgress(bar.getMaxProgress());
	}

	public void finishAndHide() {
		finishAndHide(0);
	}

	public void finishAndHide(int delay) {
		started = true;
		setStatusAsFinished();
		hideAfter(delay);
	}

}
