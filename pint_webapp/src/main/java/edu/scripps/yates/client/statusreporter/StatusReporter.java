package edu.scripps.yates.client.statusreporter;

public interface StatusReporter {

	public void showMessage(String message);

	public void showErrorMessage(Throwable throwable);

	public String getStatusReporterKey();

}
