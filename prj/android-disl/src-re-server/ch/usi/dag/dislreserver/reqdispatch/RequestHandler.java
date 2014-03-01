package ch.usi.dag.dislreserver.reqdispatch;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ch.usi.dag.dislreserver.exception.DiSLREServerException;

public interface RequestHandler {

    void handle (int pid, DataInputStream is, DataOutputStream os, boolean debug)
    throws DiSLREServerException;

	// invoked at exit
	void exit();
}
