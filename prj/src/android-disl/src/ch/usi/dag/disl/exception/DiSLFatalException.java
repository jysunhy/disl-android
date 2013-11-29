package ch.usi.dag.disl.exception;

/**
 * This exception means, there is something bad in DiSL implementation.
 * User should not experience this normally.
 *  
 */
public class DiSLFatalException extends RuntimeException {

	private static final long serialVersionUID = -1367296993008634784L;

	public DiSLFatalException() {
		super();
	}

	public DiSLFatalException(String message, Throwable cause) {
		super(message, cause);
	}

	public DiSLFatalException(String message) {
		super(message);
	}

	public DiSLFatalException(Throwable cause) {
		super(cause);
	}

}
