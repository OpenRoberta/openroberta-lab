package de.fhg.iais.roberta.util;

import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * The Option or Maybe class. If the option is <b>not</b> set, a message describing <b>why</b> that happened is stored
 *
 * @author rbudde
 * @param <X>
 */
public class Option<X> {
    private final X val;
    private final String message;

    private Option(final X val, String message) {
        this.val = val;
        this.message = message;
    }

    /**
     * make an option with a value (option is "set")
     *
     * @param val
     * @return the option
     */
    public static <T> Option<T> of(T val) {
        Assert.notNull(val);
        return new Option<T>(val, null);
    }

    /**
     * make an empty option (option is not "set")
     *
     * @param val
     * @return the option
     */
    public static <T> Option<T> empty(String message) {
        Assert.notNull(message);
        return new Option<T>(null, message);
    }

    /**
     * get the value. The option must be set.
     *
     * @return the value
     */
    public X getVal() {
        Assert.isTrue(this.message == null);
        return this.val;
    }

    /**
     * get the message.The option must be empty.
     *
     * @return the message
     */
    public String getMessage() {
        Assert.isTrue(this.message != null);
        return this.message;
    }

    /**
     * check whether the option is set or is empty
     *
     * @return true, if the option is set
     */
    public boolean isSet() {
        return this.message == null;
    }

    @Override
    public String toString() {
        return "Option [" + (isSet() ? "set" : "not set") + (!isSet() ? " with message=" + this.message : " with val=" + this.val) + "]";
    }

}