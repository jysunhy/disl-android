package ch.usi.dag.disl.exception;

public class ParserException extends DiSLException {

    private static final long serialVersionUID = -2826083567381934062L;

    public ParserException() {
        super();
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserException(String message) {
        super(message);
    }

    public ParserException(Throwable cause) {
        super(cause);
    }
}
