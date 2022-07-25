package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "NN_STEP_STMT", category = "STMT", blocklyNames = {"robactions_nnstep"})
public final class NNStepStmt extends Stmt {
    public NNStepStmt(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }
}
