package edu.scripps.yates.dbindex.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Takes care of forking a process and reading output / error streams to either a
 * string buffer or directly to a file writer
 * 
 * @author Adam
 */
public final class ProcessUtil {

    private static final Logger logger = Logger.getLogger(ProcessUtil.class.getName());
    private Process proc = null;
    private String command = null;
    private ProcessUtil.StreamToStringRedirect errorStringRedirect = null;
    private ProcessUtil.StreamToStringRedirect outputStringRedirect = null;
    private ProcessUtil.StreamToWriterRedirect outputWriterRedirect = null;

    /**
     * Execute a process. Redirect asynchronously stdout to a string and stderr
     * to nowhere. Use only for small outputs, otherwise use the execute()
     * variant with Writer.
     *
     * @param aCommand command to be executed
     * @param waitFor wait for termination or let it execute in background
     * @param params parameters of the command
     * @return string buffer with captured stdout
     */
    public synchronized String execute(final String aCommand, final boolean waitFor, final String... params) throws IOException, InterruptedException {
        String output = "";

        // build command array
        String[] arrayCommand = new String[params.length + 1];
        arrayCommand[0] = aCommand;

        StringBuilder arrayCommandToLog = new StringBuilder();
        arrayCommandToLog.append(aCommand).append(" ");

        for (int i = 1; i < arrayCommand.length; i++) {
            arrayCommand[i] = params[i - 1];
            arrayCommandToLog.append(arrayCommand[i]).append(" ");
        }

        final Runtime rt = Runtime.getRuntime();
        logger.log(Level.INFO, "Executing " + arrayCommandToLog.toString());

        proc = rt.exec(arrayCommand);

        //stderr redirect
        errorStringRedirect = new ProcessUtil.StreamToStringRedirect(proc.getErrorStream(), "ERROR");
        //stdout redirect
        outputStringRedirect = new ProcessUtil.StreamToStringRedirect(proc.getInputStream(), "OUTPUT");

        //start redurectors
        errorStringRedirect.start();
        outputStringRedirect.start();

        //wait for process to complete and capture error core
        if (waitFor) {
            final int exitVal = proc.waitFor();
            logger.log(Level.INFO, aCommand + " exit value: " + exitVal);
            //gc process with its streams
            proc = null;

            errorStringRedirect.stopRun();
            errorStringRedirect = null;

            outputStringRedirect.stopRun();
            output = outputStringRedirect.getOutput();

            outputStringRedirect = null;

        }
        return output;
    }

    /**
     * Execute a process. Redirect asynchronously stdout to a passed in writer
     * and stderr to nowhere.
     *
     * @param stdoutWriter file writer to write stdout to
     * @param waitFor whether to wait for the process to exit or let it run asynchronously
     * @param aCommand command to be executed
     * @param params parameters of the command
     * @return string buffer with captured stdout
     */
    public synchronized void execute(final Writer stdoutWriter, boolean waitFor, final String aCommand, final String... params) throws IOException, InterruptedException {

        // build command array
        String[] arrayCommand = new String[params.length + 1];
        arrayCommand[0] = aCommand;

        StringBuilder arrayCommandToLog = new StringBuilder();
        arrayCommandToLog.append(aCommand).append(" ");

        for (int i = 1; i < arrayCommand.length; i++) {
            arrayCommand[i] = params[i - 1];
            arrayCommandToLog.append(arrayCommand[i]).append(" ");
        }

        final Runtime rt = Runtime.getRuntime();
        logger.log(Level.INFO, "Executing " + arrayCommandToLog.toString());

        proc = rt.exec(arrayCommand);

        //stderr redirect
        errorStringRedirect = new ProcessUtil.StreamToStringRedirect(proc.getErrorStream(), "ERROR");
        //stdout redirect
        outputWriterRedirect = new ProcessUtil.StreamToWriterRedirect(proc.getInputStream(), stdoutWriter);

        //start redurectors
        errorStringRedirect.start();
        outputWriterRedirect.start();

        //wait for process to complete and capture error core
        if (waitFor) {
            final int exitVal = proc.waitFor();
            logger.log(Level.INFO, aCommand + " exit value: " + exitVal);

            //gc process with its streams
            proc = null;
        }
    }

    /**
     * Interrupt the running process and stop its stream redirect threads
     */
    public synchronized void stop() {
        logger.log(Level.INFO, "Stopping Execution of: " + command);

        if (errorStringRedirect != null) {
            errorStringRedirect.stopRun();
            errorStringRedirect = null;
        }

        if (outputStringRedirect != null) {
            outputStringRedirect.stopRun();
            outputStringRedirect = null;
        }

        if (outputWriterRedirect != null) {
            outputWriterRedirect.stopRun();
            outputWriterRedirect = null;
        }

        if (proc != null) {
            proc.destroy();
            proc = null;
        }
    }

    /**
     * Asynchronously read the output of a given input stream and write to a
     * string to be returned. Any exception during execution of the command is
     * managed in this thread.
     *
     */
    public static class StreamToStringRedirect extends Thread {

        private static final Logger logger = Logger.getLogger(StreamToStringRedirect.class.getName());
        private InputStream is;
        private StringBuffer output = new StringBuffer();
        private volatile boolean doRun = false;

        StreamToStringRedirect(final InputStream anIs, final String aType) {
            this.is = anIs;
            this.doRun = true;
        }

        /**
         * Asynchronous read of the input stream. <br /> Will report output as
         * its its displayed.
         *
         * @see java.lang.Thread#run()
         */
        @Override
        public final void run() {
            final String SEP = System.getProperty("line.separator");
            InputStreamReader isr = null;
            BufferedReader br = null;
            try {
                isr = new InputStreamReader(this.is);
                br = new BufferedReader(isr);
                String line = null;
                while (doRun && (line = br.readLine()) != null) {
                    this.output.append(line).append(SEP);
                }
            } catch (final IOException ex) {
                logger.log(Level.WARNING, "Error redirecting stream to string buffer", ex);
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException ex) {
                        logger.log(Level.SEVERE, "Error closing stream reader", ex);
                    }
                }
            }
        }

        /**
         * Stop running the stream redirect. The thread will exit out gracefully
         * after the current readLine() on stream unblocks
         */
        public void stopRun() {
            doRun = false;
        }

        /**
         * Get output filled asynchronously. <br /> Should be called after
         * execution
         *
         * @return final output
         */
        public final String getOutput() {
            return this.output.toString();
        }
    }

    /**
     * Asynchronously read the output of a given input stream and write to a
     * file writer passed in by the client. Client is responsible for closing
     * the writer.
     *
     * Any exception during execution of the command is managed in this thread.
     *
     */
    public static class StreamToWriterRedirect extends Thread {

        private static final Logger logger = Logger.getLogger(StreamToStringRedirect.class.getName());
        private InputStream is;
        private volatile boolean doRun = false;
        private Writer writer = null;

        StreamToWriterRedirect(final InputStream anIs, final Writer writer) {
            this.is = anIs;
            this.writer = writer;
            this.doRun = true;
        }

        /**
         * Asynchronous read of the input stream. <br /> Will report output as
         * its its displayed.
         *
         * @see java.lang.Thread#run()
         */
        @Override
        public final void run() {
            final String SEP = System.getProperty("line.separator");
            InputStreamReader isr = null;
            BufferedReader br = null;
            try {
                isr = new InputStreamReader(this.is);
                br = new BufferedReader(isr);
                String line = null;
                while (doRun && (line = br.readLine()) != null) {
                    writer.append(line).append(SEP);
                }
            } catch (final IOException ex) {
                logger.log(Level.SEVERE, "Error reading output and writing to file writer", ex);
            } finally {
                try {
                    if (doRun) {
                        writer.flush();
                    }
                    if (br != null) {
                        br.close();
                    }

                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Error flushing file writer", ex);
                }
            }
        }

        /**
         * Stop running the stream redirect. The thread will exit out gracefully
         * after the current readLine() on stream unblocks
         */
        public void stopRun() {
            doRun = false;
        }
    }
}