package ch.usi.dag.disl.exception;

public class ReflectionException extends DiSLException {

	private static final long serialVersionUID = 1746507695125084587L;

	public ReflectionException() {
		super();
	}

	public ReflectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReflectionException(String message) {
		super(message);
	}

	public ReflectionException(Throwable cause) {
		super(cause);
	}
}
