package ch.usi.dag.disl.exception;

public class TransformerException extends DiSLException {

    private static final long serialVersionUID = 8899006117334791742L;

    public TransformerException() {
    }

    public TransformerException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransformerException(String message) {
        super(message);
    }

    public TransformerException(Throwable cause) {
        super(cause);
    }

}
