package de.fhg.iais.roberta.util.dbc;

public class VisitorException extends RuntimeException {
    /**
    *
    */
    private static final long serialVersionUID = 6929916646966954644L;

    public VisitorException() {
        super();
    }

    public VisitorException(String message) {
        super(message);
    }

    public VisitorException(String message, Throwable cause) {
        super(message, cause);
    }
}