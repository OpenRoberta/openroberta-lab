package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(category = "STMT", blocklyNames = {"robActions_set_weight"}, name = "NN_SET_WEIGHT_STMT")
public final class NNSetWeightStmt extends Stmt {
    @NepoField(name = "FROM")
    public final String from;

    @NepoField(name = "TO")
    public final String to;

    @NepoValue(name = "VALUE", type = BlocklyType.NUMBER)
    public final Expr value;

    public NNSetWeightStmt(BlocklyProperties properties, String from, String to, Expr value) {
        super(properties);
        Assert.isTrue(value.isReadOnly() && value != null);
        this.from = from;
        this.to = to;
        this.value = value;
        setReadOnly();
    }

}
