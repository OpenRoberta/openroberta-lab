package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoPhrase;

@NepoPhrase(containerType = "NN_OUTPUT_NEURON_WO_VAR_STMT")
public class NNOutputNeuronWoVarStmt<V> extends Stmt<V> {
    @NepoField(name = BlocklyConstants.NAME)
    public final String name;

    public NNOutputNeuronWoVarStmt(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, String name) {
        super(kind, properties, comment);
        this.name = name;
        setReadOnly();
    }

    public static <V> NNOutputNeuronWoVarStmt<V> make(BlocklyBlockProperties properties, BlocklyComment comment, String name) {
        return new NNOutputNeuronWoVarStmt<V>(BlockTypeContainer.getByName("NN_OUTPUT_NEURON_STMT"), properties, comment, name);
    }

    public String getName() {
        return this.name;
    }
}
