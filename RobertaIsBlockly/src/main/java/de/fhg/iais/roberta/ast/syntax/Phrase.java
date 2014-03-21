package de.fhg.iais.roberta.ast.syntax;

public class Phrase {
    private boolean readOnly = false;

    public final boolean mayChange() {
        return !this.readOnly;
    }

    public final boolean isReadOnly() {
        return this.readOnly;
    }

    public final void setReadOnly() {
        this.readOnly = true;
        freeze();
    }

    protected void freeze() {
        // do nothing, allow subclasses to override
    }
}
