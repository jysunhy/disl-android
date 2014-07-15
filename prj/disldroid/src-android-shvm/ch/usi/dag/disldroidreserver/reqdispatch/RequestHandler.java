package ch.usi.dag.disldroidreserver.reqdispatch;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ch.usi.dag.disldroidreserver.exception.DiSLREServerException;

public interface RequestHandler {

    void handle (int pid, DataInputStream is, DataOutputStream os, boolean debug)
    throws DiSLREServerException;

	// invoked at exit
	void exit();
}
