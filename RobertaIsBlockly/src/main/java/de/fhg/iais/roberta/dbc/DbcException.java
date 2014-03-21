package de.fhg.iais.roberta.dbc;

public class DbcException extends RuntimeException {
    private static final long serialVersionUID = 2164640960687162908L;

    /**
     * Constructs a new runtime exception with <code>null</code> as its detail message. The cause is not
     * initialized, and may subsequently be initialized by a call to {@link #initCause}.<br>
     * <i>Remark</i> For documentation purposes adapted from java.lang.RuntimeException
     */
    public DbcException() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message. The cause is not initialized, and
     * may subsequently be initialized by a call to {@link #initCause}.<br>
     * <i>Remark</i> For documentation purposes adapted from java.lang.RuntimeException
     * 
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public DbcException(String message) {
        super(message);
    }

    /**
     * Constructs a new runtime exception with the specified detail message and cause.
     * <p>
     * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically incorporated in this runtime exception's detail message.<br>
     * <i>Remark</i> For documentation purposes adapted from java.lang.RuntimeException
     * 
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value is permitted, and indicates that
     *        the cause is nonexistent or unknown.)
     */
    public DbcException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new runtime exception with the specified cause and a detail message of <tt>(cause==null ? null : cause.toString())</tt><br>
     * <i>Remark</i> For documentation purposes adapted from java.lang.RuntimeException
     * 
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value is permitted, and indicates that
     *        the cause is nonexistent or unknown.)
     */
    public DbcException(Throwable cause) {
        super(cause);
    }
}
