package ch.usi.dag.disl.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class ClientServerRunner {

    private final int WAIT_FOR_SERVER_INIT_MS = 3000;

    private final int WAIT_FOR_TEST_MS = 60000;

    private final Class <?> c;

    private Job client;

    private Job server;

    private boolean clientOutNull;

    private boolean clientErrNull;

    private boolean serverOutNull;

    private boolean serverErrNull;


    public ClientServerRunner (final Class <?> c) {
        this.c = c;
        clientOutNull = true;
        clientErrNull = true;
        serverOutNull = true;
        serverErrNull = true;
    }


    private String getTestName () {
        final String [] packages = this.c.getPackage ().getName ().split ("\\.");
        return packages [packages.length - 2];
    }


    public void start ()
    throws IOException {
        final String test = getTestName ();

        final String [] serverCmd = new String [] {
            "java",
            "-cp", "build-test/disl-instr-" + test + ".jar:build/disl-server.jar",
            "ch.usi.dag.dislserver.DiSLServer"
        };
        server = new Job (serverCmd);
        server.execute ();

        server.waitFor (WAIT_FOR_SERVER_INIT_MS);

        final String [] clientCmd = new String [] {
            "java",
            "-agentpath:build/libdislagent.so",
            "-javaagent:build/disl-agent.jar",
            "-Xbootclasspath/a:build/disl-agent.jar:build-test/disl-instr-"
                + test + ".jar",
            "-jar", "build-test/disl-app-" + test + ".jar"
        };
        client = new Job (clientCmd);
        client.execute ();
    }


    private boolean waitFor (final long milliseconds) {
        boolean finished = true;
        finished = finished & client.waitFor (milliseconds);
        finished = finished & server.waitFor (milliseconds);
        return finished;
    }


    public boolean waitFor () {
        return waitFor (WAIT_FOR_TEST_MS);
    }


    public void assertIsStarted () {
        assertTrue ("client not started", client.isStarted ());
        assertTrue ("server not started", server.isStarted ());
    }


    public void assertIsFinished () {
        assertTrue ("client not finished", client.isFinished ());
        assertTrue ("server not finished", server.isFinished ());
    }


    public void assertIsSuccessfull () {
        assertTrue ("client not successfull", client.isSuccessfull ());
        assertTrue ("server not successfull", server.isSuccessfull ());
    }


    private void writeFile (final String filename, final String str)
    throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter (filename)) {
            out.print (str);
        }
    }


    private void destroyIfRunningAndFlushOutputs (
        final boolean cout, final boolean cerr, final boolean sout, final boolean serr, final boolean destroy)
    throws FileNotFoundException, IOException {
        if (client != null) {
            if (client.isRunning () && destroy) {
                client.destroy ();
            }
            if (!client.isRunning () && cout) {
                writeFile (
                    String.format ("tmp.%s.client.out.txt", getTestName ()),
                    client.getOutput ());
            }
            if (!client.isRunning () && cerr) {
                writeFile (
                    String.format ("tmp.%s.client.err.txt", getTestName ()),
                    client.getError ());
            }
        }
        if (server != null) {
            if (server.isRunning () && destroy) {
                server.destroy ();
            }
            if (!server.isRunning () && cout) {
                writeFile (
                    String.format ("tmp.%s.server.out.txt", getTestName ()),
                    server.getOutput ());
            }
            if (!server.isRunning () && cerr) {
                writeFile (
                    String.format ("tmp.%s.server.err.txt", getTestName ()),
                    server.getError ());
            }
        }
    }


    public void destroyIfRunningAndFlushOutputs ()
    throws FileNotFoundException, IOException {
        destroyIfRunningAndFlushOutputs (true, true, true, true, true);
    }


    private String getResource (final String filename)
    throws IOException {
        try (BufferedReader reader = new BufferedReader (
            new InputStreamReader (this.c.getResourceAsStream (filename), "UTF-8"));) {

            final StringBuffer buffer = new StringBuffer ();
            for (int c = reader.read (); c != -1; c = reader.read ()) {
                buffer.append ((char) c);
            }

            return buffer.toString ();
        }
    }


    public void assertClientOut (final String filename)
    throws IOException {
        clientOutNull = false;
        assertEquals (
            "client out does not match", getResource (filename), client.getOutput ());
    }


    public void assertClientOutNull ()
    throws IOException {
        clientOutNull = false;
        assertEquals ("client out does not match", "", client.getOutput ());
    }


    public void assertClientErr (final String filename)
    throws IOException {
        clientErrNull = false;
        assertEquals (
            "client err does not match", getResource (filename), client.getError ());
    }


    public void assertClientErrNull ()
    throws IOException {
        clientErrNull = false;
        assertEquals ("client err does not match", "", client.getError ());
    }


    public void assertServerOut (final String filename)
    throws IOException {
        serverOutNull = false;
        assertEquals (
            "server out does not match", getResource (filename), server.getOutput ());
    }


    public void assertServerOutNull ()
    throws IOException {
        serverOutNull = false;
        assertEquals ("server out does not match", "", server.getOutput ());
    }


    public void assertServerErr (final String filename)
    throws IOException {
        serverErrNull = false;
        assertEquals (
            "server err does not match", getResource (filename), server.getError ());
    }


    public void assertServerErrNull ()
    throws IOException {
        serverErrNull = false;
        assertEquals ("server err does not match", "", server.getError ());
    }


    public void assertRestOutErrNull ()
    throws IOException {
        if (clientOutNull) {
            assertClientOutNull ();
        }
        if (clientErrNull) {
            assertClientErrNull ();
        }
        if (serverOutNull) {
            assertServerOutNull ();
        }
        if (serverErrNull) {
            assertServerErrNull ();
        }
    }


    public void verbose ()
    throws IOException {
        System.out.println ("client-out:");
        System.out.println (client.getOutput ());
        System.out.println ("client-err:");
        System.out.println (client.getError ());
        System.out.println ("server-out:");
        System.out.println (server.getOutput ());
        System.out.println ("server-err:");
        System.out.println (server.getError ());
    }


    public void destroy () {
        if (client != null) {
            client.destroy ();
        }
        if (server != null) {
            server.destroy ();
        }
    }
}
