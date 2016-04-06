package edu.scripps.yates.client.interfaces;

public interface StatusReporter {

	public void showMessage(String message);

	public void showErrorMessage(Throwable throwable);
}
