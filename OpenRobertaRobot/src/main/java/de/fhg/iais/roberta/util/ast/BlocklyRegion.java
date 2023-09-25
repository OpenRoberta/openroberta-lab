package de.fhg.iais.roberta.util.ast;

import de.fhg.iais.roberta.blockly.generated.Comment;

public class BlocklyRegion {
    public final boolean disabled;
    public final boolean collapsed;
    public final Boolean inline;
    public final Boolean deletable;
    public final Boolean movable;
    public final Boolean inTask;
    public final Boolean shadow;
    public final Boolean errorAttribute;

    public final Comment comment;

    public BlocklyRegion(
        boolean disabled,
        boolean collapsed,
        Boolean inline,
        Boolean deletable,
        Boolean movable,
        Boolean inTask,
        Boolean shadow,
        Boolean errorAttribute,
        Comment comment) {
        this.disabled = disabled;
        this.collapsed = collapsed;
        this.inline = inline;
        this.deletable = deletable;
        this.movable = movable;
        this.inTask = inTask;
        this.shadow = shadow;
        this.errorAttribute = errorAttribute;
        this.comment = comment;
    }
}
