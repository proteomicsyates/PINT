package edu.scripps.yates.client.gui.components.progressbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.shared.tasks.Task;
import edu.scripps.yates.shared.util.ProgressStatus;

public class AdvancedProgressDialog extends ProgressLoadingDialog {
	private final String sessionID;
	private final Task task;

	public AdvancedProgressDialog(final String sessionID, Task task) {
		super(task.getTaskDescription(), true, true);
		this.task = task;
		this.sessionID = sessionID;
		start();
	}

	@Override
	protected RepeatingCommand getRepeatingCommandForAskingProgress() {
		final RepeatingCommand command = new RepeatingCommand() {
			boolean waitingResponse = false;

			@Override
			public boolean execute() {
				GWT.log("Executing scheduled task in ProteinsByProjectLoadingDialog with key: " + task);
				if (!waitingResponse) {
					waitingResponse = true;
					service.getProgressStatus(sessionID, task, new AsyncCallback<ProgressStatus>() {

						@Override
						public void onSuccess(ProgressStatus progressStatus) {
							center();
							GWT.log("Progress status received for task " + task + ": " + progressStatus);

							waitingResponse = false;
							if (progressStatus != null) {
								GWT.log("Setting progress to " + progressStatus.getCurrentStep() + " / "
										+ progressStatus.getMaxSteps());
								setStatusOnBar(progressStatus);
							}
						}

						@Override
						public void onFailure(Throwable caught) {
							waitingResponse = false;
							final String message = "Error when requesting progress on task " + task + ": "
									+ caught.getMessage();
							GWT.log(message);
							StatusReportersRegister.getInstance().notifyStatusReporters(message);
							finishAndHide(1000);
						}
					});
				}
				if (finished || (bar.getPercent() >= 1.0 && started)) {
					started = false;
					// close the dialog after 2 sg
					hideAfter(2000);
					// stop asking for progress to the server: return false;
					return false;
				} else {
					return true;
				}
			}
		};
		return command;
	}
}
