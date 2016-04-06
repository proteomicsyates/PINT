package edu.scripps.yates.utilities.exec;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.exec.CommandLine;

public class ProcessExecutor {
	public static final Long TIMEOUT_ERROR_CODE = -999L;

	public static Future<Long> runProcess(final CommandLine commandline,
			final ProcessExecutorHandler handler, final long watchdogTimeout)
			throws IOException {

		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<Long> result = executor.submit(new ProcessCallable(
				watchdogTimeout, handler, commandline));
		executor.shutdown();
		return result;

	}
}
