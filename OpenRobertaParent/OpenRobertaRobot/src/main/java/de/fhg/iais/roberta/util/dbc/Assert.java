package de.fhg.iais.roberta.util.dbc;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Assertion-Checker<br>
 * - for programming DBC (design by contract), used for pre- and post-conditions.<br>
 * - everywhere to validate assumptions. <br>
 * If an assertion is violated, a <b>DbcException</b> is thrown. <br>
 * The optional error message must state, that the assertion is <i>violated</i>, it must <i>not</i> express, what the assertion expects: <br>
 * <b>OK</b>: <code>Assert.isTrue(p == 4,"p is not equal to 4")</code><br>
 * <b>bad</b>: <code>Assert.isTrue(p == 4,"p equals 4")</code>. <br>
 * <b>often OK</b>: <code>Assert.isTrue(p == 4,"p should be equal to 4")</code> <br>
 * <br>
 * Extensions to the class are welcome.
 */
public final class Assert {
    /**
     * <b>DBC:</b> asserts, that a condition is <code>true</code>. If the assertion is violated, a {@link DbcException} is thrown.
     *
     * @param b the condition to be checked
     */
    public static void isTrue(boolean b) {
        if ( !b ) {
            throw new DbcException("Assertion is violated");
        }
    }

    /**
     * <b>DBC:</b> asserts, that a condition is <code>true</code>. If the assertion is violated, a {@link DbcException} is thrown. The error message must state,
     * that the assertion is <i>violated</i>
     *
     * @param b the condition to be checked
     * @param msg the error message; states, that the assertion is <i>violated</i>
     */
    public static void isTrue(boolean b, String msg) {
        if ( !b ) {
            throw new DbcException(msg);
        }
    }

    /**
     * <b>DBC:</b> asserts, that a condition is <code>true</code>. If the assertion is violated, a {@link DbcException} is thrown. The error message must state,
     * that the assertion is <i>violated</i><br>
     * <br>
     * The error-message is build <i>lazily</i> to optimize performance. This is not 100% true for primitive param types, because these have to be boxed ... .
     * For formatting details, see {@link String#format(String, Object...)}
     *
     * @param b the condition to be checked
     * @param format format for the error message; states, that the assertion is <i>violated</i>
     * @param params arguments for the format
     */
    public static void isTrue(boolean b, String format, Object... params) {
        if ( !b ) {
            String msg = Assert.makeMessage(format, params);
            throw new DbcException(msg);
        }
    }

    /**
     * <b>DBC:</b> asserts, that a condition is <code>true</code>. If the assertion is violated, a stacktrace is printed, then a {@link DbcException} is thrown.
     * <br>
     * <br>
     * Use in rare cases. If you have written a class using the DBC paradigm and you suspect, that caller of your class catch DBC exceptions and do not react on
     * DBC exceptions as required, a printed stack trace may help unveiling such a problem.<br>
     * <br>
     * The error-message is build <i>lazily</i> to optimize performance. This is not 100% true for primitive param types, because these have to be boxed ... .
     * For formatting details, see {@link String#format(String, Object...)}
     *
     * @param b the condition to be checked
     * @param format format for the error message; states, that the assertion is <i>violated</i>
     * @param params arguments for the format
     */

    public static void isTrueOnErrorPrintStacktrace(boolean b, String format, Object... params) {
        if ( !b ) {
            try {
                throw new DbcException("Assertion is violated");
            } catch ( DbcException e ) {
                String msg = Assert.makeMessage(format, params);
                throw new DbcException(msg);
            }
        }
    }

    /**
     * <b>DBC:</b> asserts, that a condition is <code>false</code>. If the assertion is violated, a {@link DbcException} is thrown.
     *
     * @param b the condition to be checked
     */
    public static void isFalse(boolean b) {
        if ( b ) {
            throw new DbcException("Assertion is violated");
        }
    }

    /**
     * <b>DBC:</b> asserts, that a condition is <code>false</code>. If the assertion is violated, a {@link DbcException} is thrown. The error message must
     * state, that the assertion is <i>violated</i>
     *
     * @param b the condition to be checked
     * @param msg the error message; states, that the assertion is <i>violated</i>
     */
    public static void isFalse(boolean b, String msg) {
        if ( b ) {
            throw new DbcException(msg);
        }
    }

    /**
     * <b>DBC:</b> asserts, that a condition is <code>false</code>. If the assertion is violated, a {@link DbcException} is thrown. The error message must
     * state, that the assertion is <i>violated</i><br>
     * <br>
     * The error-message is build <i>lazily</i> to optimize performance. This is not 100% true for primitive param types, because these have to be boxed ... .
     * For formatting details, see {@link String#format(String, Object...)}
     *
     * @param b the condition to be checked
     * @param format format for the error message; states, that the assertion is <i>violated</i>
     * @param params arguments for the format
     */
    public static void isFalse(boolean b, String format, Object... params) {
        if ( b ) {
            String msg = Assert.makeMessage(format, params);
            throw new DbcException(msg);
        }
    }

    /**
     * <b>DBC:</b> asserts, that a condition is <code>false</code>. If the assertion is violated, a stacktrace is printed, then a {@link DbcException} is
     * thrown. <br>
     * <br>
     * Use in rare cases. If you have written a class using the DBC paradigm and you suspect, that caller of your class catch DBC exceptions and do not react on
     * DBC exceptions as required, a printed stack trace may help unveiling such a problem.<br>
     * <br>
     * The error-message is build <i>lazily</i> to optimize performance. This is not 100% true for primitive param types, because these have to be boxed ... .
     * For formatting details, see {@link String#format(String, Object...)}
     *
     * @param b the condition to be checked
     * @param format format for the error message; states, that the assertion is <i>violated</i>
     * @param params arguments for the format
     */

    public static void isFalseOnErrorPrintStacktrace(boolean b, String format, Object... params) {
        if ( b ) {
            try {
                throw new DbcException("Assertion is violated");
            } catch ( DbcException e ) {
                String msg = Assert.makeMessage(format, params);
                throw new DbcException(msg);
            }
        }
    }

    /**
     * <b>DBC:</b> an assertion <i>is</i> violated, thus a {@link DbcException} is thrown.
     */
    public static void fail() {
        throw new DbcException("Assertion is violated");
    }

    /**
     * <b>DBC:</b> an assertion <i>is</i> violated, thus a {@link DbcException} is thrown.
     *
     * @param msg the error message; states, why the callers fails to fulfill the contract
     */
    public static void fail(String msg) {
        throw new DbcException(msg);
    }

    /**
     * <b>DBC:</b> asserts, that an object reference is <i>not</i> <code>null</code>. If the assertion is violated, a {@link DbcException} is thrown.<br>
     *
     * @param o expected to be not null
     */
    public static void notNull(Object o) {
        if ( o == null ) {
            throw new DbcException("Assertion notNull is violated");
        }
    }

    /**
     * <b>DBC:</b> asserts, that an object reference is <i>not</i> <code>null</code>. If the assertion is violated, a {@link DbcException} is thrown.
     *
     * @param o expected to be not null
     * @param msg the error message; states, that the assertion is <i>violated</i>
     */
    public static void notNull(Object o, String msg) {
        if ( o == null ) {
            throw new DbcException(msg);
        }
    }

    /**
     * <b>DBC:</b> asserts, that an object reference is <i>not</i> <code>null</code>. If the assertion is violated, a {@link DbcException} is thrown.<br>
     * <br>
     * The error-message is build <i>lazily</i> to optimize performance. This is not 100% true for primitive param types, because these have to be boxed ... .
     * For formatting details, see {@link String#format(String, Object...)}
     *
     * @param b the condition to be checked
     * @param format format for the error message; states, that the assertion is <i>violated</i>
     * @param params arguments for the format
     */
    public static void notNull(Object o, String format, Object... params) {
        if ( o == null ) {
            String msg = Assert.makeMessage(format, params);
            throw new DbcException(msg);
        }
    }

    /**
     * <b>DBC:</b> asserts, that an object reference is <code>null</code>. If the assertion is violated, a {@link DbcException} is thrown.<br>
     *
     * @param o expected to be null
     */
    public static void isNull(Object o) {
        if ( o != null ) {
            throw new DbcException("Assertion isNull is violated");
        }
    }

    /**
     * <b>DBC:</b> asserts, that an object reference is <code>null</code>. If the assertion is violated, a {@link DbcException} is thrown.
     *
     * @param o expected to be null
     * @param msg the error message; states, that the assertion is <i>violated</i>
     */
    public static void isNull(Object o, String msg) {
        if ( o != null ) {
            throw new DbcException(msg);
        }
    }

    /**
     * <b>DBC:</b> asserts, that an object reference is <code>null</code>. If the assertion is violated, a {@link DbcException} is thrown.<br>
     * <br>
     * The error-message is build <i>lazily</i> to optimize performance. This is not 100% true for primitive param types, because these have to be boxed ... .
     * For formatting details, see {@link String#format(String, Object...)}
     *
     * @param o expected to be null
     * @param format format for the error message; states, that the assertion is <i>violated</i>
     * @param params arguments for the format
     */
    public static void isNull(Object o, String format, Object... params) {
        if ( o != null ) {
            String msg = Assert.makeMessage(format, params);
            throw new DbcException(msg);
        }
    }

    /**
     * <b>DBC:</b> asserts, that a reference is a non-empty string. If the assertion is violated, a {@link DbcException} is thrown.
     *
     * @param s expected to be a non-empty string
     */
    public static void nonEmptyString(String s) {
        if ( s == null || s.isEmpty() ) {
            throw new DbcException("Assertion nonEmptyString violated");
        }
    }

    /**
     * <b>DBC:</b> asserts, that a reference is a non-empty string. If the assertion is violated, a {@link DbcException} is thrown.
     *
     * @param s expected to be a non-empty string
     * @param msg the error message; states, that the assertion is <i>violated</i>
     */
    public static void nonEmptyString(String s, String msg) {
        if ( s == null || s.isEmpty() ) {
            throw new DbcException(msg);
        }
    }

    /**
     * <b>DBC:</b> asserts, that a reference is a non-empty string. If the assertion is violated, a {@link DbcException} is thrown.<br>
     * <br>
     * The error-message is build <i>lazily</i> to optimize performance. This is not 100% true for primitive param types, because these have to be boxed ... .
     * For formatting details, see {@link String#format(String, Object...)}
     *
     * @param b the condition to be checked
     * @param format format for the error message; states, that the assertion is <i>violated</i>
     * @param params arguments for the format
     */
    public static void nonEmptyString(String s, String format, Object... params) {
        if ( s == null || s.isEmpty() ) {
            String msg = Assert.makeMessage(format, params);
            throw new DbcException(msg);
        }
    }

    @SuppressFBWarnings(value = "REC_CATCH_EXCEPTION", justification = "here any exception should generate a default message")
    private static String makeMessage(String format, Object... params) {
        String msg = "Assertion evaluiert zu FALSE";
        try {
            msg = String.format(format, params);
        } catch ( Exception e1 ) {
            try {
                StringBuffer sb = new StringBuffer();
                sb.append(format);
                sb.append(" : ");
                if ( params == null ) {
                    sb.append((Object) null);
                } else {
                    boolean first = true;
                    for ( Object param : params ) {
                        if ( first ) {
                            first = false;
                        } else {
                            sb.append(" , ");
                        }
                        sb.append(param);
                    }
                    msg = sb.toString();
                }
            } catch ( Exception e2 ) {
                // use the init-string
            }
        }
        return msg;
    }
}