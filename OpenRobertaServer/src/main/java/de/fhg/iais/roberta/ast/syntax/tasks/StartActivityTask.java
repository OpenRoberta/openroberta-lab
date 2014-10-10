package de.fhg.iais.roberta.ast.syntax.tasks;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;

public class StartActivityTask<V> extends Expr<V> {

    private final Expr<V> activityName;

    private StartActivityTask(Expr<V> activityName, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.START_ACTIVITY_TASK, properties, comment);
        this.activityName = activityName;
        setReadOnly();
    }

    public static <V> StartActivityTask<V> make(Expr<V> activityName, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new StartActivityTask<V>(activityName, properties, comment);
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
        return visitor.visitStartActivityTask(this);
    }

    @Override
    public String toString() {
        return "StartActivityTask [activityName=" + this.activityName + "]";
    }
}
