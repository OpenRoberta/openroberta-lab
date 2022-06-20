package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "STMT", blocklyNames = {"robActions_outputneuron_wo_var"}, containerType = "NN_OUTPUT_NEURON_WO_VAR_STMT")
public final class NNOutputNeuronWoVarStmt<V> extends Stmt<V> {
    @NepoField(name = BlocklyConstants.NAME)
    public final String name;

    public NNOutputNeuronWoVarStmt(BlocklyBlockProperties properties, BlocklyComment comment, String name) {
        super(properties, comment);
        this.name = name;
        setReadOnly();
    }

    public static <V> NNOutputNeuronWoVarStmt<V> make(BlocklyBlockProperties properties, BlocklyComment comment, String name) {
        return new NNOutputNeuronWoVarStmt<V>(properties, comment, name);
    }

    public String getName() {
        return this.name;
    }
}
