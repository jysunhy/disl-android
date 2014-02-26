package ch.usi.dag.dislreserver.reqdispatch;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ch.usi.dag.dislreserver.exception.DiSLREServerException;
import ch.usi.dag.dislreserver.shadow.ShadowAddressSpace;

public interface RequestHandler {

    void handle (
        ShadowAddressSpace shadowAddressSpace, DataInputStream is, DataOutputStream os,
        boolean debug)
    throws DiSLREServerException;

	// invoked at exit
	void exit();
}
