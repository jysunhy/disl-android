package ch.usi.dag.disl.test.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Class wraps standard java Process class and adds some more features. Prior to
 * the execution itself it saves binaries to the specified location and then
 * executes specified command.
 * 
 * It is possible to retrieve the exit value of the process or the state of the
 * process. Normal output stream and error stream can be accessed represented as
 * a String it is not intended for heavy use but just for debugging purposes.
 * 
 * @author Frantisek Haas
 * 
 */
public class Job {

    private Process              process        = null;
    /** Process build to create the child process. */
    private final ProcessBuilder processBuilder = new ProcessBuilder();
    /** Whether to close all streams to the child process right after start. */
    private boolean              closeStreams   = false;
    /** Process output. */
    private String               output         = new String();
    /** Process error. */
    private String               error          = new String();

    public Job(String[] command, File directory) {
        processBuilder.command(command);
        processBuilder.directory(directory);
        processBuilder.environment().clear();
    }

    public Job(String[] command) {
        processBuilder.command(command);
        processBuilder.environment().clear();
    }

    /**
     * Executes the command in the directory.
     * 
     */
    public void execute()
            throws IOException {
        process = processBuilder.start();
        /**
         * <p>
         * It seams that reading streams from created process is a bit lot
         * tricky and hangs the reader. Therefore it's better to redirect stdout
         * and stderr to files in the forked application.
         */
        if (closeStreams) {
            process.getOutputStream().close();
            process.getInputStream().close();
            process.getErrorStream().close();
        }
    }

    /**
     * Kills the process.
     * 
     */
    public void destroy() {
        if (process != null) {
            process.destroy();
        }
    }

    /**
     * Returns stream to process' input stream.
     * 
     * @return
     * @throws IOException
     */
    public OutputStream getInput()
            throws IOException {
        if (process == null) {
            throw new IOException("Process not started.");
        }

        return process.getOutputStream();
    }

    /**
     * Reads entire process output stream into string.
     * 
     * May block if process is still running.
     * 
     * @return
     * @throws IOException
     */
    public String getOutput()
            throws IOException {
        if (process == null || closeStreams) {
            return output;
        }

        final int BUFFER_SIZE = 4096;
        final int EOF = -1;

        InputStream outputStream = process.getInputStream();

        byte[] buffer = new byte[BUFFER_SIZE];
        int length = outputStream.read(buffer);

        while (length != EOF) {
            output += new String(buffer, 0, length);
            length = outputStream.read(buffer);
        }

        return output;
    }

    /**
     * Reads entire process error stream into string.
     * 
     * May block if process is still running.
     * 
     * @return
     * @throws IOException
     */
    public String getError()
            throws IOException {
        if (process == null || closeStreams) {
            return error;
        }

        final int BUFFER_SIZE = 4096;
        final int EOF = -1;

        InputStream errorStream = process.getErrorStream();

        byte[] buffer = new byte[BUFFER_SIZE];
        int length = errorStream.read(buffer);

        while (length != EOF) {
            error += new String(buffer, 0, length);
            length = errorStream.read(buffer);
        }

        return error;
    }

    /**
     * Returns job exit status. If job has not yet finished returns -1 by
     * default.
     * 
     * @return
     */
    public int getReturnCode() {
        if (process == null) {
            return 0;
        }

        try {
            return process.exitValue();
        } catch (IllegalThreadStateException e) {
            // thrown if process is still running
            return -1;
        }
    }

    /**
     * Waits maximum of specified time. Returns true if job is not running.
     * Returns false otherwise.
     * 
     * @param milliseconds
     * @return
     */
    public boolean waitFor(long milliseconds) {
        if (!isRunning()) {
            return true;
        }

        final long millisecondsWaitInterval = 100;
        while (true) {
            if (milliseconds < millisecondsWaitInterval) {
                try {
                    Thread.sleep(milliseconds);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
                return isRunning();

            } else {
                milliseconds -= millisecondsWaitInterval;
                try {
                    Thread.sleep(millisecondsWaitInterval);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }

                if (!isRunning()) {
                    return true;
                }
            }
        }
    }
    
    /**
     * Indicates job status.
     * 
     * @return
     *         True - If job is already started
     *         False - Otherwise.
     */
    public boolean isStarted() {
    	return (process != null);
    }

    /**
     * Indicates job status.
     * 
     * @return
     *         True - If job is still running.
     *         False - If job is not running yet or not anymore.
     */
    public boolean isRunning() {
        if (process == null) {
            return false;
        }

        try {
            process.exitValue();
            return false;
        } catch (IllegalThreadStateException e) {
            // thrown if process is still running
            return true;
        }
    }
    
    /**
     * Indicates job status.
     * 
     * @return
     *         True - If job is finished or never started.
     *         False - Otherwise.
     */
    public boolean isFinished() {
    	return !isRunning();
    }

    /**
     * Indicates job status using process exit value.
     * 
     * @return
     *         True - If job has successfully finished returning zero.
     *         False - If job has not finished yet or not successfully.
     */
    public boolean isSuccessfull() {
        if (process == null) {
            return false;
        }

        try {
            return (process.exitValue() == 0);
        } catch (IllegalThreadStateException e) {
            // thrown if process is still running
            return false;
        }
    }
}

