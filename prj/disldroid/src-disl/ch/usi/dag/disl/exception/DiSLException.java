package ch.usi.dag.disl.exception;

public abstract class DiSLException extends Exception {

    private static final long serialVersionUID = 6916051574250648195L;

    public DiSLException() {
        super();
    }

    public DiSLException(String message, Throwable cause) {
        super(message, cause);
    }

    public DiSLException(String message) {
        super(message);
    }

    public DiSLException(Throwable cause) {
        super(cause);
    }

    public DiSLException (final String format, final Object ... args) {
        super (String.format (format, args));
    }

}
