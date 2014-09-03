package de.fhg.iais.roberta.ast.syntax.tasks;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;

public class ActivityTask<V> extends Task<V> {
    private final int x;
    private final int y;
    private final Expr<V> activityName;

    private ActivityTask(int x, int y, Expr<V> activityName, boolean disabled, String comment) {
        super(Phrase.Kind.ACTIVITY_TASK, disabled, comment);
        this.x = x;
        this.y = y;
        this.activityName = activityName;
        setReadOnly();
    }

    public static <V> ActivityTask<V> make(int x, int y, Expr<V> activityName, boolean disabled, String comment) {
        return new ActivityTask<V>(x, y, activityName, disabled, comment);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Expr<V> getActivityName() {
        return this.activityName;
    }

    @Override
    public int getPrecedence() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Assoc getAssoc() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        // TODO Auto-generated method stub

        return null;
    }

    @Override
    public String toString() {
        return "ActivityTask [x=" + this.x + ", y=" + this.y + ", activityName=" + this.activityName + "]";
    }

}
