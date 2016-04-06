package edu.scripps.yates.utilities.exec;

public interface ProcessExecutorHandler {
	public void onStandardOutput(String msg);

	public void onStandardError(String msg);
}
