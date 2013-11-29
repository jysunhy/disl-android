package ch.usi.dag.disl.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class ClientServerEvaluationRunner {

    private final int WAIT_FOR_INIT_MS = 3000;

    private final int WAIT_FOR_TEST_MS = 60000;

    private final Class <?> c;

    private Job client;

    private Job server;

    private Job evaluation;

    private boolean clientOutNull;

    private boolean clientErrNull;

    private boolean evaluationOutNull;

    private boolean evaluationErrNull;

    private boolean serverOutNull;

    private boolean serverErrNull;


    public ClientServerEvaluationRunner (final Class <?> c) {
        this.c = c;
        clientOutNull = true;
        clientErrNull = true;
        evaluationOutNull = true;
        evaluationErrNull = true;
        serverOutNull = true;
        serverErrNull = true;
    }


    private String getTestName () {
        final String [] packages = this.c.getPackage ().getName ().split ("\\.");
        return packages [packages.length - 2];
    }


    private void startServer (final String test, final String instrJar)
    throws IOException {
        final String serverJar = "build/disl-server.jar";
        final String serverClass = "ch.usi.dag.dislserver.DiSLServer";

        /**
         * -cp ${INSTR_LIB}:${DISL_LIB_P}/disl-server.jar \
         * ch.usi.dag.dislserver.DiSLServer \ "$@" &
         */
        final String [] serverCmd = new String [] {
            "java",
            "-cp", instrJar + ":" + serverJar,
            serverClass
        };
        server = new Job (serverCmd);
        server.execute ();
    }


    private void startEvaluation (final String test, final String instrJar)
    throws IOException {
        final String evaluationJar = "build/dislre-server.jar";
        final String evaluationClass = "ch.usi.dag.dislreserver.DiSLREServer";

        /**
         * ${JAVA_HOME:+$JAVA_HOME/jre/bin/}java \ -Xms1G -Xmx2G \ -cp
         * ${INSTR_LIB}:${DISL_LIB_P}/dislre-server.jar \
         * ch.usi.dag.dislreserver.DiSLREServer \ "$@" &
         */
        final String [] evaluationCmd = new String [] {
            "java",
            "-Xms1G", "-Xmx2G",
            "-cp", instrJar + ":" + evaluationJar,
            evaluationClass
        };
        evaluation = new Job (evaluationCmd);
        evaluation.execute ();
    }


    private void startClient (final String test, final String instrJar)
    throws IOException {
        final String cagentLib = "build/libdislagent.so";
        final String eagentLib = "build/libdislreagent.so";
        final String jagentJar = "build/disl-agent.jar";
        final String dispatchJar = "build/dislre-dispatch.jar";
        final String xboot = "-Xbootclasspath/a:"
            + jagentJar + ":" + instrJar + ":" + dispatchJar;

        /**
         * ${JAVA_HOME:+$JAVA_HOME/jre/bin/}java \ -agentpath:${C_AGENT}
         * -agentpath:${RE_AGENT} \ -javaagent:${DISL_LIB_P}/disl-agent.jar \
         * -Xbootclasspath
         * /a:${DISL_LIB_P}/disl-agent.jar:${INSTR_LIB}:${DISL_LIB_P
         * }/dislre-dispatch.jar \ "$@"
         */
        final String [] clientCmd = new String [] {
            "java",
            "-agentpath:" + cagentLib,
            "-agentpath:" + eagentLib,
            "-javaagent:" + jagentJar,
            xboot,
            "-jar", "build-test/disl-app-" + test + ".jar"
        };
        client = new Job (clientCmd);
        client.execute ();
    }


    public void start ()
    throws IOException {
        final String test = getTestName ();
        final String instrJar = "build-test/disl-instr-" + test + ".jar";

        startServer (test, instrJar);
        startEvaluation (test, instrJar);

        try {
            Thread.sleep (WAIT_FOR_INIT_MS);
        } catch (final InterruptedException e) {
            Thread.interrupted ();
        }

        startClient (test, instrJar);
    }


    private boolean waitFor (final long milliseconds) {
        boolean finished = true;
        finished = finished & client.waitFor (milliseconds);
        finished = finished & evaluation.waitFor (milliseconds);
        finished = finished & server.waitFor (milliseconds);
        return finished;
    }


    public boolean waitFor () {
        return waitFor (WAIT_FOR_TEST_MS);
    }


    public void assertIsStarted () {
        assertTrue ("client not started", client.isStarted ());
        assertTrue ("evaluation not started", evaluation.isStarted ());
        assertTrue ("server not started", server.isStarted ());
    }


    public void assertIsFinished () {
        assertTrue ("client not finished", client.isFinished ());
        assertTrue ("evaluation not finished", evaluation.isFinished ());
        assertTrue ("server not finished", server.isFinished ());
    }


    public void assertIsSuccessfull () {
        assertTrue ("client not successfull", client.isSuccessfull ());
        assertTrue ("evaluation not successfull", evaluation.isSuccessfull ());
        assertTrue ("server not successfull", server.isSuccessfull ());
    }


    private void writeFile (final String filename, final String str)
    throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter (filename)) {
            out.print (str);
        }
    }


    public void destroyIfRunningAndFlushOutputs ()
    throws FileNotFoundException, IOException {
        if (client != null) {
            if (client.isRunning ()) {
                client.destroy ();
            }
            if (!client.isRunning ()) {
                writeFile (
                    String.format ("tmp.%s.client.out.txt", getTestName ()),
                    client.getOutput ());
            }
            if (!client.isRunning ()) {
                writeFile (
                    String.format ("tmp.%s.client.err.txt", getTestName ()),
                    client.getError ());
            }
        }
        if (evaluation != null) {
            if (evaluation.isRunning ()) {
                evaluation.destroy ();
            }
            if (!evaluation.isRunning ()) {
                writeFile (
                    String.format ("tmp.%s.evaluation.out.txt", getTestName ()),
                    evaluation.getOutput ());
            }
            if (!evaluation.isRunning ()) {
                writeFile (
                    String.format ("tmp.%s.evaluation.err.txt", getTestName ()),
                    evaluation.getError ());
            }
        }
        if (server != null) {
            if (server.isRunning ()) {
                server.destroy ();
            }
            if (!server.isRunning ()) {
                writeFile (
                    String.format ("tmp.%s.server.out.txt", getTestName ()),
                    server.getOutput ());
            }
            if (!server.isRunning ()) {
                writeFile (
                    String.format ("tmp.%s.server.err.txt", getTestName ()),
                    server.getError ());
            }
        }
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


    public void assertEvaluationOut (final String filename)
    throws IOException {
        evaluationOutNull = false;
        assertEquals (
            "evaluation out does not match", getResource (filename),
            evaluation.getOutput ());
    }


    public void assertEvaluationOutNull ()
    throws IOException {
        evaluationOutNull = false;
        assertEquals ("evaluation out does not match", "", evaluation.getOutput ());
    }


    public void assertEvaluationErr (final String filename)
    throws IOException {
        evaluationErrNull = false;
        assertEquals (
            "evaluation err does not match", getResource (filename),
            evaluation.getError ());
    }


    public void assertEvaluationrErrNull ()
    throws IOException {
        evaluationErrNull = false;
        assertEquals ("evaluation err does not match", "", evaluation.getError ());
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

        if (evaluationOutNull) {
            assertClientOutNull ();
        }
        if (evaluationErrNull) {
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

        System.out.println ("evaluation-out:");
        System.out.println (evaluation.getOutput ());
        System.out.println ("evaluation-err:");
        System.out.println (evaluation.getError ());

        System.out.println ("server-out:");
        System.out.println (server.getOutput ());
        System.out.println ("server-err:");
        System.out.println (server.getError ());
    }


    public void destroy () {
        if (client != null) {
            client.destroy ();
        }
        if (evaluation != null) {
            evaluation.destroy ();
        }
        if (server != null) {
            server.destroy ();
        }
    }
}
