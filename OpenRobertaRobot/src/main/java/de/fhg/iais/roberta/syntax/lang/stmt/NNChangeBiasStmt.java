package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "STMT", blocklyNames = {"robActions_change_bias"}, name = "NN_CHANGE_BIAS_STMT")
public final class NNChangeBiasStmt<V> extends Stmt<V> {
    @NepoField(name = BlocklyConstants.NAME)
    public final String name;
    @NepoField(name = BlocklyConstants.CHANGE)
    public final String change;
    @NepoValue(name = BlocklyConstants.VALUE, type = BlocklyType.NUMBER)
    public final Expr<V> value;

    public NNChangeBiasStmt(BlocklyProperties properties, String name, String change, Expr<V> value) {
        super(properties);
        Assert.isTrue(value.isReadOnly() && value != null);
        this.name = name;
        this.change = change;
        this.value = value;
        setReadOnly();
    }

}
