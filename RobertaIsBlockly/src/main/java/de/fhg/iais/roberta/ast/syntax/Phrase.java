package de.fhg.iais.roberta.ast.syntax;

/**
 * the top class of all class used to represent the AST (abstract syntax tree) of a program. After construction an AST should be immmutable. The logic to acieve
 * that is in this class. An object of a subclass of {@link Phrase} is initially writable, after the construction of the object has finished,
 * {@link #setReadOnly()} is called.
 * This cannot be undone later. It is expected that all subclasses of {@link #Phrase} do the following:<br>
 * - if in construction phase, they should use {@link #mayChange()} to assert that.<br>
 * - if the construction has finished and {@link #setReadOnly()} has been called, they should use {@link #isReadOnly()} to assert their immutability.
 */
public class Phrase {
    private boolean readOnly = false;

    /**
     * @return true, if the object is writable/mutable. This is true, if {@link #setReadOnly()} has not yet been called for this object
     */
    public final boolean mayChange() {
        return !this.readOnly;
    }

    /**
     * @return true, if the object is read-only/immutable. This is true, if {@link #setReadOnly()} has been called for this object
     */
    public final boolean isReadOnly() {
        return this.readOnly;
    }

    /**
     * make this {@link Phrase}-object read-only/immutable. Should be called if the construction phase has finished
     */
    public final void setReadOnly() {
        this.readOnly = true;
    }
}
