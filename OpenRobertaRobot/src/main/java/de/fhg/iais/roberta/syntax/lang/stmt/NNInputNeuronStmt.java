package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.transformer.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(containerType = "NN_INPUT_NEURON_STMT")
public class NNInputNeuronStmt<V> extends Stmt<V> {
    @NepoField(name = BlocklyConstants.NAME)
    public final String name;
    @NepoValue(name = BlocklyConstants.VALUE, type = BlocklyType.NUMBER)
    public final Expr<V> value;

    public NNInputNeuronStmt(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, String name, Expr<V> value) {
        super(kind, properties, comment);
        Assert.isTrue(value.isReadOnly() && value != null);
        this.name = name;
        this.value = value;
        setReadOnly();
    }

    public static <V> NNInputNeuronStmt<V> make(BlocklyBlockProperties properties, BlocklyComment comment, String name, Expr<V> value) {
        return new NNInputNeuronStmt<V>(BlockTypeContainer.getByName("NN_INPUT_NEURON_STMT"), properties, comment, name, value);
    }

    public String getName() {
        return this.name;
    }

    public Expr<V> getValue() {
            return this.value;
    }
}
