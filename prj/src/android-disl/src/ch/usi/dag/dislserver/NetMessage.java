package ch.usi.dag.dislserver;

public class NetMessage {
    
	private byte[] control;
    private byte[] classCode;

    public NetMessage(byte[] control, byte[] classCode) {
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
