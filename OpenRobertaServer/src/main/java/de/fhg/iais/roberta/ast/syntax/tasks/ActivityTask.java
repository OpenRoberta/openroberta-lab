package de.fhg.iais.roberta.ast.syntax.tasks;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;

public class ActivityTask<V> extends Task<V> {
    private final Expr<V> activityName;

    private ActivityTask(Expr<V> activityName, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.ACTIVITY_TASK, properties, comment);
        this.activityName = activityName;
        setReadOnly();
    }

    public static <V> ActivityTask<V> make(Expr<V> activityName, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ActivityTask<V>(activityName, properties, comment);
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
        return "ActivityTask [activityName=" + this.activityName + "]";
    }

}
