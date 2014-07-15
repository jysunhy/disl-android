package ch.usi.dag.disldroidserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetMessageReader {

	private final Socket socket;
	private final DataInputStream is;
	private final DataOutputStream os;

	public NetMessageReader(final Socket socket) throws IOException {

		this.socket = socket;

		is = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		os = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
	}

	public NetMessage readMessage() throws IOException {

		// protocol:
		// java int - control string length (ctl)
		// java int - class code length (ccl)
		// bytes[ctl] - control string (contains class name)
		// bytes[ccl] - class code

		final int controlLength = is.readInt();
		final int classCodeLength = is.readInt();

		// allocate buffer for class reading
		final byte[] control = new byte[controlLength];
		final byte[] classCode = new byte[classCodeLength];

		// read class
		is.readFully(control);
		is.readFully(classCode);

		return new NetMessage(control, classCode);
	}

	public void close() throws IOException {

		is.close();
		socket.close();
	}

	public void sendMessage(final NetMessage nm) throws IOException {

		// protocol:
		// java int - control string (ctl)
		// java int - class code length (ccl)
		// bytes[ctl] - control string
		// bytes[ccl] - class code

		os.writeInt(nm.getControl().length);
		os.writeInt(nm.getClassCode().length);

		os.write(nm.getControl());
		os.write(nm.getClassCode());
		os.flush();
	}
}
