package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(category = "EXPR", blocklyNames = {"robSensors_get_bias"}, name = "NN_GET_BIAS")
public final class NNGetBias extends Expr {
    @NepoField(name = "NAME")
    public final String name;

    public NNGetBias(BlocklyProperties properties, String name) {
        super(properties);
        this.name = name;
        setReadOnly();
    }

}
