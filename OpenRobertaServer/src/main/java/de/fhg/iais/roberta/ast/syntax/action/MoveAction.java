package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;

public abstract class MoveAction<V> extends Action<V> {
    private final ActorPort port;

    public MoveAction(ActorPort port, Kind kind, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(kind, properties, comment);
        this.port = port;
    }

    /**
     * @return port on which the motor is connected.
     */
    public ActorPort getPort() {
        return this.port;
    }

}
