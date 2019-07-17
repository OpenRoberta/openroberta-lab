package de.fhg.iais.roberta.util.dbc;

import java.util.Map;

import de.fhg.iais.roberta.util.Key;

public class DbcKeyException extends DbcException {
    private static final long serialVersionUID = 6861291059819738332L;

    private final Key key;
    private final Map<String, Object> parameter;

    /**
     * Creates a runtime exception without detail message and cause.
     *
     * @param key the key, expressing the error that caused this exception. Not null.
     * @param parameter (optional) parameters for the message associated with the key
     */
    public DbcKeyException(Key key, Map<String, Object> parameter) {
        super();
        Assert.notNull(key);
        this.key = key;
        this.parameter = parameter;
    }

    /**
     * Creates a runtime exception with a detail message, but without cause.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     * @param key the key, expressing the error that caused this exception. Not null.
     * @param parameter (optional) parameters for the message associated with the key
     */
    public DbcKeyException(String message, Key key, Map<String, Object> parameter) {
        super(message);
        this.key = key;
        this.parameter = parameter;
    }

    /**
     * Creates a runtime exception with detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value is permitted, and indicates that
     *        the cause is nonexistent or unknown.)
     * @param key the key, expressing the error that caused this exception. Not null.
     * @param parameter (optional) parameters for the message associated with the key
     */
    public DbcKeyException(String message, Throwable cause, Key key, Map<String, Object> parameter) {
        super(message, cause);
        this.key = key;
        this.parameter = parameter;
    }

    /**
     * Creates a runtime exception with cause and a detail message of <tt>(cause==null ? null : cause.toString())</tt>
     *
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value is permitted, and indicates that
     *        the cause is nonexistent or unknown.)
     * @param key the key, expressing the error that caused this exception. Not null.
     * @param parameter (optional) parameters for the message associated with the key
     */
    public DbcKeyException(Throwable cause, Key key, Map<String, Object> parameter) {
        super(cause);
        this.key = key;
        this.parameter = parameter;
    }

    public Key getKey() {
        return key;
    }

    public Map<String, Object> getParameter() {
        return parameter;
    }
}
