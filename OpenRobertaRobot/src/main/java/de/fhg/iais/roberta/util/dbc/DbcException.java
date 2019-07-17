package de.fhg.iais.roberta.util.dbc;

public class DbcException extends RuntimeException {
    private static final long serialVersionUID = -705761295982248833L;

    /**
     * Creates a runtime exception without detail message and cause
     */
    public DbcException() {
        super();
    }

    /**
     * Creates a runtime exception with detail message, but without cause
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public DbcException(String message) {
        super(message);
    }

    /**
     * Creates a runtime exception with the detail message and cause
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value is permitted, and indicates that
     *        the cause is nonexistent or unknown.)
     */
    public DbcException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a runtime exception with cause and a detail message of <tt>(cause==null ? null : cause.toString())</tt><br>
     *
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value is permitted, and indicates that
     *        the cause is nonexistent or unknown.)
     */
    public DbcException(Throwable cause) {
        super(cause);
    }
}
