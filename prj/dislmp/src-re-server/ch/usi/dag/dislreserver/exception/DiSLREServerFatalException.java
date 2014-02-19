package ch.usi.dag.dislreserver.exception;

public class DiSLREServerFatalException extends RuntimeException {

	private static final long serialVersionUID = -8431771285237240263L;

	public DiSLREServerFatalException() {
	}

	public DiSLREServerFatalException(String message) {
		super(message);
	}

	public DiSLREServerFatalException(Throwable cause) {
		super(cause);
	}

	public DiSLREServerFatalException(String message, Throwable cause) {
		super(message, cause);
	}
}
