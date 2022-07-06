package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(category = "STMT", blocklyNames = {"robActions_change_weight"}, name = "NN_CHANGE_WEIGHT_STMT")
public final class NNChangeWeightStmt extends Stmt {
    @NepoField(name = "FROM")
    public final String from;

    @NepoField(name = "TO")
    public final String to;

    @NepoField(name = "CHANGE")
    public final String change;

    @NepoValue(name = "VALUE", type = BlocklyType.NUMBER)
    public final Expr value;

    public NNChangeWeightStmt(BlocklyProperties properties, String from, String to, String change, Expr value) {
        super(properties);
        Assert.isTrue(value.isReadOnly() && value != null);
        this.from = from;
        this.to = to;
        this.change = change;
        this.value = value;
        setReadOnly();
    }

}
