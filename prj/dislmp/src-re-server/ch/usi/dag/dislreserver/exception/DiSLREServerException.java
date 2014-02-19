package ch.usi.dag.dislreserver.exception;

public class DiSLREServerException extends Exception {

	private static final long serialVersionUID = 5272000884539359236L;

	public DiSLREServerException() {
		super();
	}

	public DiSLREServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public DiSLREServerException(String message) {
		super(message);
	}

	public DiSLREServerException(Throwable cause) {
		super(cause);
	}
}
