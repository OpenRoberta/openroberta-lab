package de.fhg.iais.roberta.syntax.action;

import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;import de.fhg.iais.roberta.syntax.BlockTypeContainer.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;

public abstract class MoveAction<V> extends Action<V> {
    private final IActorPort port;

    public MoveAction(IActorPort port, BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(kind, properties, comment);
        this.port = port;
    }

    /**
     * @return port on which the motor is connected.
     */
    public IActorPort getPort() {
        return this.port;
    }

}
