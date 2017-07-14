package edu.scripps.yates.client.statusreporter;

interface StatusReporter {

	public void showMessage(String message);

	public void showErrorMessage(Throwable throwable);

}
