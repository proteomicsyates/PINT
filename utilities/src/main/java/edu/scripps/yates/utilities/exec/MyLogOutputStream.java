package edu.scripps.yates.utilities.exec;

import org.apache.commons.exec.LogOutputStream;

public class MyLogOutputStream extends LogOutputStream {

	private final ProcessExecutorHandler handler;
	private final boolean forewordToStandardOutput;

	public MyLogOutputStream(ProcessExecutorHandler handler,
			boolean forewordToStandardOutput) {
		this.handler = handler;
		this.forewordToStandardOutput = forewordToStandardOutput;
	}

	@Override
	protected void processLine(String line, int level) {
		if (forewordToStandardOutput) {
			handler.onStandardOutput(line);
		} else {
			handler.onStandardError(line);
		}
	}

}
