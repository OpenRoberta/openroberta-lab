package de.fhg.iais.roberta.syntax.action;

import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

public abstract class MoveAction<V> extends Action<V> implements WithUserDefinedPort<V> {
    public final String port;

    public MoveAction(BlocklyBlockProperties properties, BlocklyComment comment, String port) {
        super(properties, comment);
        this.port = port;
    }

    @Override
    public String getUserDefinedPort() {
        return this.port;
    }
}
