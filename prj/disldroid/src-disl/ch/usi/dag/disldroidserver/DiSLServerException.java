package ch.usi.dag.disldroidserver;

public class DiSLServerException extends Exception {

	private static final long serialVersionUID = 5272000884539359236L;

	public DiSLServerException() {
		super();
	}

	public DiSLServerException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public DiSLServerException(final String message) {
		super(message);
	}

	public DiSLServerException(final Throwable cause) {
		super(cause);
	}
}
