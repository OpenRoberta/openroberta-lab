package de.fhg.iais.roberta.syntax.action;

import de.fhg.iais.roberta.shared.action.ev3.ActorPort;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;

public abstract class MoveAction<V> extends Action<V> {
    private final ActorPort port;

    public MoveAction(ActorPort port, BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment) {
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
