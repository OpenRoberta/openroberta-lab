package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "STMT", blocklyNames = {"robActions_inputneuron"}, name = "NN_INPUT_NEURON_STMT")
public final class NNInputNeuronStmt<V> extends Stmt<V> {
    @NepoField(name = BlocklyConstants.NAME)
    public final String name;
    @NepoValue(name = BlocklyConstants.VALUE, type = BlocklyType.NUMBER)
    public final Expr<V> value;

    public NNInputNeuronStmt(BlocklyBlockProperties properties, BlocklyComment comment, String name, Expr<V> value) {
        super(properties, comment);
        Assert.isTrue(value.isReadOnly() && value != null);
        this.name = name;
        this.value = value;
        setReadOnly();
    }

}
