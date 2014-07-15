package ch.usi.dag.disl.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import ch.usi.dag.util.Duration;
import ch.usi.dag.util.Lists;


public class ClientServerEvaluationRunner extends Runner {

    private Job __client;

    private Job __server;

    private Job __shadow;

    private boolean clientOutNull;

    private boolean clientErrNull;

    private boolean shadowOutNull;

    private boolean shadowErrNull;

    private boolean serverOutNull;

    private boolean serverErrNull;


    public ClientServerEvaluationRunner (final Class <?> testClass) {
        super (testClass);

        clientOutNull = true;
        clientErrNull = true;
        shadowOutNull = true;
        shadowErrNull = true;
        serverOutNull = true;
        serverErrNull = true;
    }

    private Job __startServer (
        final File testInstJar, final File statusFile
    ) throws IOException {
        final List <String> command = Lists.newLinkedList (
            _JAVA_COMMAND_,
            "-classpath", Runner.classPath (_DISL_SERVER_JAR_, testInstJar)
        );

        if (statusFile != null) {
            command.add (String.format (
                "-Dserver.status.file=%s", statusFile
            ));
        }

        command.addAll (propertiesStartingWith ("dislserver."));
        command.addAll (propertiesStartingWith ("disl."));
        command.add (_DISL_SERVER_CLASS_.getName ());

        //

        final Job result = new Job (command);
        result.start ();
        return result;
    }


    private Job __startShadow (
        final File testInstJar, final File statusFile
    ) throws IOException {
        final List <String> command = Lists.newLinkedList (
            _JAVA_COMMAND_, "-Xms1G", "-Xmx2G",
            "-classpath", Runner.classPath (_SHVM_SERVER_JAR_, testInstJar)
        );

        if (statusFile != null) {
            command.add (String.format (
                "-Dserver.status.file=%s", statusFile
            ));
        }

        command.addAll (propertiesStartingWith ("dislreserver."));
        command.add (_SHVM_SERVER_CLASS_.getName ());

        //

        final Job result = new Job (command);
        result.start ();
        return result;
    }


    private Job __startClient (
        final File testInstJar, final File testAppJar
    ) throws IOException {
        final List <String> command = Lists.newLinkedList (
            _JAVA_COMMAND_,
            String.format ("-agentpath:%s", _DISL_AGENT_LIB_),
            String.format ("-agentpath:%s", _SHVM_AGENT_LIB_),
            String.format ("-Xbootclasspath/a:%s", Runner.classPath (
                _DISL_BYPASS_JAR_, _SHVM_DISPATCH_JAR_, testInstJar
            ))
        );

        command.addAll (propertiesStartingWith ("disl."));
        command.addAll (Arrays.asList (
            "-jar", testAppJar.toString ()
        ));

        //

        final Job result = new Job (command);
        result.start ();
        return result;
    }


    @Override
    protected void _start (
        final File testInstJar, final File testAppJar
    ) throws IOException {
        final File serverFile = File.createTempFile ("disl-", ".status");
        serverFile.deleteOnExit ();

        __server = __startServer (testInstJar, serverFile);

        watchFile (serverFile, _INIT_TIME_LIMIT_);

        if (! __server.isRunning ()) {
            throw new IOException ("server failed: "+ __server.getError ());
        }

        //

        final File shadowFile = File.createTempFile ("shvm-", ".status");
        shadowFile.deleteOnExit ();

        __shadow = __startShadow (testInstJar, shadowFile);

        watchFile (shadowFile, _INIT_TIME_LIMIT_);

        if (! __shadow.isRunning ()) {
            throw new IOException ("shadow failed: "+ __shadow.getError ());
        }

        //

        __client = __startClient (testInstJar, testAppJar);
    }


    @Override
    protected boolean _waitFor (final Duration duration) {
        boolean finished = true;
        finished = finished & __client.waitFor (duration);
        finished = finished & __server.waitFor (duration);
        finished = finished & __shadow.waitFor (duration);
        return finished;
    }


    @Override
    protected void _assertIsStarted () {
        assertTrue ("client not started", __client.isStarted ());
        assertTrue ("server not started", __server.isStarted ());
        assertTrue ("shadow not started", __shadow.isStarted ());
    }


    @Override
    protected void _assertIsFinished () {
        assertTrue ("client not finished", __client.isFinished ());
        assertTrue ("server not finished", __server.isFinished ());
        assertTrue ("shadow not finished", __shadow.isFinished ());
    }


    @Override
    protected void _assertIsSuccessful () {
        assertTrue ("client failed", __client.isSuccessful ());
        assertTrue ("server failed", __server.isSuccessful ());
        assertTrue ("shadow failed", __shadow.isSuccessful ());
    }


    @Override
    protected void _destroyIfRunningAndDumpOutputs () throws IOException {
        _destroyIfRunningAndDumpOutputs (__client, "client");
        _destroyIfRunningAndDumpOutputs (__server, "server");
        _destroyIfRunningAndDumpOutputs (__shadow, "shadow");
    }


    public void assertClientOut (final String fileName) throws IOException {
        clientOutNull = false;
        assertEquals (
            "client out does not match",
            _loadResource (fileName), __client.getOutput ()
        );
    }


    public void assertClientOutNull () throws IOException {
        clientOutNull = false;
        assertEquals ("client out does not match", "", __client.getOutput ());
    }


    public void assertClientErr (final String fileName) throws IOException {
        clientErrNull = false;
        assertEquals (
            "client err does not match",
            _loadResource (fileName), __client.getError ()
        );
    }


    public void assertClientErrNull () throws IOException {
        clientErrNull = false;
        assertEquals ("client err does not match", "", __client.getError ());
    }


    public void assertShadowOut (final String fileName) throws IOException {
        shadowOutNull = false;
        assertEquals (
            "shadow out does not match",
            _loadResource (fileName), __shadow.getOutput ()
        );
    }


    public void assertShadowOutNull () throws IOException {
        shadowOutNull = false;
        assertEquals ("shadow out does not match", "", __shadow.getOutput ());
    }


    public void assertShadowErr (final String fileName) throws IOException {
        shadowErrNull = false;
        assertEquals (
            "shadow err does not match",
            _loadResource (fileName), __shadow.getError ()
        );
    }


    public void assertShadowErrNull () throws IOException {
        shadowErrNull = false;
        assertEquals ("shadow err does not match", "", __shadow.getError ());
    }


    public void assertServerOut (final String fileName) throws IOException {
        serverOutNull = false;
        assertEquals (
            "server out does not match",
            _loadResource (fileName), __server.getOutput ()
        );
    }


    public void assertServerOutNull ()
    throws IOException {
        serverOutNull = false;
        assertEquals ("server out does not match", "", __server.getOutput ());
    }


    public void assertServerErr (final String fileName) throws IOException {
        serverErrNull = false;
        assertEquals (
            "server err does not match",
            _loadResource (fileName), __server.getError ()
        );
    }


    public void assertServerErrNull () throws IOException {
        serverErrNull = false;
        assertEquals ("server err does not match", "", __server.getError ());
    }


    @Override
    protected void _assertRestOutErrNull () throws IOException {
        if (clientOutNull) {
            assertClientOutNull ();
        }

        if (clientErrNull) {
            assertClientErrNull ();
        }

        if (shadowOutNull) {
            assertShadowOutNull ();
        }
        if (shadowErrNull) {
            assertShadowErrNull ();
        }

        if (serverOutNull) {
            assertServerOutNull ();
        }
        if (serverErrNull) {
            assertServerErrNull ();
        }
    }


    @Override
    protected void _destroy () {
        if (__client != null) {
            __client.destroy ();
        }
        if (__shadow != null) {
            __shadow.destroy ();
        }
        if (__server != null) {
            __server.destroy ();
        }
    }
}
