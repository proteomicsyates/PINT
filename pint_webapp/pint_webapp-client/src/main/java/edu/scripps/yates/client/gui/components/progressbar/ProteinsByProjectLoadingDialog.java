package edu.scripps.yates.client.gui.components.progressbar;

import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.scripps.yates.shared.tasks.SharedTaskKeyGenerator;
import edu.scripps.yates.shared.util.ProgressStatus;

public class ProteinsByProjectLoadingDialog extends ProgressLoadingDialog {
	private final String sessionID;
	private final String projectTag;
	private final String uniprotVersion;

	public ProteinsByProjectLoadingDialog(final String sessionID, String projectTag, String uniprotVersion) {
		super("Loading proteins from project", true, false);
		this.sessionID = sessionID;
		this.projectTag = projectTag;
		this.uniprotVersion = uniprotVersion;
		start();
	}

	@Override
	protected RepeatingCommand getRepeatingCommand() {
		RepeatingCommand command = new RepeatingCommand() {
			boolean waitingResponse = false;
			final String key = SharedTaskKeyGenerator.getKeyForGetProteinsFromProjectTask(projectTag, uniprotVersion);

			@Override
			public boolean execute() {
				log.info("Executing scheduled task in ProteinsByProjectLoadingDialog");
				if (!waitingResponse) {
					waitingResponse = true;
					service.getProgressStatus(sessionID, key, new AsyncCallback<ProgressStatus>() {

						@Override
						public void onSuccess(ProgressStatus progressStatus) {
							center();
							log.info("Progress status received for task " + key + ": " + progressStatus);

							waitingResponse = false;
							if (progressStatus != null) {
								log.info("Setting progress to " + progressStatus.getCurrentStep() + " / "
										+ progressStatus.getMaxSteps());
								setStatusOnBar(progressStatus);
							} else {
								setStatusAsFinished();
							}
						}

						@Override
						public void onFailure(Throwable caught) {
							waitingResponse = false;
							log.warning("Error when requesting progress on task " + key + ": " + caught.getMessage());
						}
					});
				}
				if (bar.getPercent() >= 1.0 && started) {
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
