package de.fhg.iais.roberta.ast.syntax.tasks;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;

public class MainTask<V> extends Task<V> {

    private final int x;
    private final int y;

    private MainTask(int x, int y, boolean disabled, String comment) {
        super(Phrase.Kind.MAIN_TASK, disabled, comment);
        this.x = x;
        this.y = y;
        setReadOnly();
    }

    /**
     * creates instance of {@link MainTask}. This instance is read only and cannot be modified.
     */
    public static <V> MainTask<V> make(int x, int y, boolean disabled, String comment) {
        return new MainTask<V>(x, y, disabled, comment);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public int getPrecedence() {
        return 0;
    }

    @Override
    public Assoc getAssoc() {
        return null;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitMainTask(this);
    }

    @Override
    public String toString() {
        return "MainTask [x=" + this.x + ", y=" + this.y + "]";
    }

}
