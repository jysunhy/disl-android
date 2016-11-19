package ch.usi.dag.disldroidreserver.reqdispatch;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;

import ch.usi.dag.disldroidreserver.exception.DiSLREServerException;

public interface RequestHandler {

    void handle (int pid, DataInputStream is, DataOutputStream os, boolean debug)
    throws DiSLREServerException;

    void handle (int pid, ByteBuffer is, boolean debug)
    throws Exception;
	// invoked at exit
	void exit();
}
