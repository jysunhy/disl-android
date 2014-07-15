package ch.usi.dag.disldroidserver;

public class NetMessage {

	private final byte[] control;
    private final byte[] classCode;

    public NetMessage(final byte[] control, final byte[] classCode) {
        this.control = control;
        this.classCode = classCode;
    }

	public byte[] getControl() {
		return control;
	}

	public byte[] getClassCode() {
		return classCode;
	}
}
