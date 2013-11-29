package ch.usi.dag.dislserver;

public class DiSLServerException extends Exception {

	private static final long serialVersionUID = 5272000884539359236L;

	public DiSLServerException() {
		super();
	}

	public DiSLServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public DiSLServerException(String message) {
		super(message);
	}

	public DiSLServerException(Throwable cause) {
		super(cause);
	}
}
