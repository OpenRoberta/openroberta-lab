package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "STMT", blocklyNames = {"robActions_change_weight"}, name = "NN_CHANGE_WEIGHT_STMT")
public final class NNChangeWeightStmt<V> extends Stmt<V> {
    @NepoField(name = BlocklyConstants.FROM)
    public final String from;
    @NepoField(name = BlocklyConstants.TO)
    public final String to;
    @NepoField(name = BlocklyConstants.CHANGE)
    public final String change;
    @NepoValue(name = BlocklyConstants.VALUE, type = BlocklyType.NUMBER)
    public final Expr<V> value;

    public NNChangeWeightStmt(BlocklyBlockProperties properties, BlocklyComment comment, String from, String to, String change, Expr<V> value) {
        super(properties, comment);
        Assert.isTrue(value.isReadOnly() && value != null);
        this.from = from;
        this.to = to;
        this.change = change;
        this.value = value;
        setReadOnly();
    }

    public static <V> NNChangeWeightStmt<V> make(BlocklyBlockProperties properties, BlocklyComment comment, String from, String to, String change, Expr<V> value) {
        return new NNChangeWeightStmt<V>(properties, comment, from, to, change, value);
    }

    public String getFrom() {
        return this.from;
    }

    public String getTo() {
        return this.to;
    }

    public String getChange() {
        return this.change;
    }

    public Expr<V> getValue() {
        return this.value;
    }
}
