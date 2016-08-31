package edu.scripps.yates.client.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.interfaces.StatusReporter;

/**
 * Register of objects implementing the {@link StatusReporter} interfaz. This
 * will allow to call to showErrorMessage or showMessage to all the registered
 * objects from everywhere
 *
 * @author Salva
 *
 */
public class StatusReportersRegister {
	private final List<StatusReporter> statusReporters = new ArrayList<StatusReporter>();
	private static StatusReportersRegister instance;

	private StatusReportersRegister() {

	}

	public static StatusReportersRegister getInstance() {
		if (instance == null) {
			instance = new StatusReportersRegister();
		}
		return instance;
	}

	public void registerNewStatusReporter(StatusReporter statusReporter) {
		statusReporters.add(statusReporter);
	}

	public void notifyStatusReporters(String message) {
		for (StatusReporter statusReporter : statusReporters) {
			if (statusReporter instanceof Widget) {
				if (((Widget) statusReporter).isVisible()) {
					statusReporter.showMessage(message);
				}
			} else {
				statusReporter.showMessage(message);
			}
		}
	}

	public void notifyStatusReporters(Throwable throwable) {
		throwable.printStackTrace();
		for (StatusReporter statusReporter : statusReporters) {
			if (statusReporter instanceof Widget) {
				if (((Widget) statusReporter).isVisible()) {
					statusReporter.showErrorMessage(throwable);
				}
			} else {
				statusReporter.showErrorMessage(throwable);
			}
		}
	}
}
