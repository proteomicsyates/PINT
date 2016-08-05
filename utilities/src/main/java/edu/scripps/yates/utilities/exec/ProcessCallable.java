package edu.scripps.yates.utilities.exec;

import java.util.concurrent.Callable;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.exec.ShutdownHookProcessDestroyer;

public class ProcessCallable implements Callable<Long> {

	private final long watchdogTimeout;
	private final ProcessExecutorHandler handler;
	private final CommandLine commandline;

	public ProcessCallable(long watchdogTimeout, ProcessExecutorHandler handler, CommandLine commandline) {
		this.watchdogTimeout = watchdogTimeout;
		this.handler = handler;
		this.commandline = commandline;
	}

	@Override
	public Long call() throws Exception {
		Executor executor = new DefaultExecutor();
		executor.setWorkingDirectory(null);
		executor.setProcessDestroyer(new ShutdownHookProcessDestroyer());
		ExecuteWatchdog watchDog = new ExecuteWatchdog(watchdogTimeout);
		executor.setWatchdog(watchDog);
		executor.setStreamHandler(
				new PumpStreamHandler(new MyLogOutputStream(handler, true), new MyLogOutputStream(handler, false)));
		Long exitValue;
		try {
			exitValue = new Long(executor.execute(commandline));

		} catch (ExecuteException e) {
			exitValue = new Long(e.getExitValue());
		}
		if (watchDog.killedProcess()) {
			exitValue = ProcessExecutor.TIMEOUT_ERROR_CODE;
		}

		return exitValue;
	}

}
