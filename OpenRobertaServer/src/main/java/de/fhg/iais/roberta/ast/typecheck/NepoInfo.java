package de.fhg.iais.roberta.ast.typecheck;

/**
 * describes either an error or a problem w.r.t. a phrase of the AST. Collections of objects of this class are stored
 * in the mutable part of the phrase (within a phrase the tree structure is immutable, but the attachment of problems is (more) dynamic.
 *
 * @author rbudde
 */
public class NepoInfo {
    private final Severity severity;
    private final String message;

    private NepoInfo(Severity severity, String message) {
        this.severity = severity;
        this.message = message;
    }

    public static NepoInfo of(Severity severity, String message) {
        return new NepoInfo(severity, message);
    }

    public static NepoInfo error(String message) {
        return new NepoInfo(Severity.ERROR, message);
    }

    public static NepoInfo warning(String message) {
        return new NepoInfo(Severity.WARNING, message);
    }

    public Severity getSeverity() {
        return this.severity;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return "NepoProblem [" + this.severity + ": " + this.message + "]";
    }

    public static enum Severity {
        WARNING, ERROR;
    }
}
