package de.fhg.iais.roberta.util.ast;

import de.fhg.iais.roberta.blockly.generated.Comment;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class describes the blockly-related properties (most of them are graphical properties) of a block from the AST.
 * <p>
 * Information contained in this class is used to recreate a blockly block when the AST is used to regenerate the original source.<br>
 * Every object that can be part of the AST (subclasses of Phrase) should contain an instance an instance of this class, otherwise their graphical
 * representation cannot be regenerated.<br>
 * <br>
 */
public class BlocklyProperties {
    private final String blockType;
    private final String blocklyId;
    private final boolean disabled;
    private final Boolean inTask;
    private final boolean collapsed;
    private final Boolean inline;
    private final Boolean deletable;
    private final Boolean movable;
    private final Boolean shadow;
    private final Boolean errorAttribute;
    private final Comment comment;

    /**
     * create an object, that saves the blockly-related properties of a block for later regeneration.<br>
     * The names of the parameter should be self-explaining.
     */
    public BlocklyProperties(
        String blockType,
        String blocklyId,
        boolean disabled,
        boolean collapsed,
        Boolean inline,
        Boolean deletable,
        Boolean movable,
        Boolean inTask,
        Boolean shadow,
        Boolean errorAttribute,
        Comment comment)
    {
        Assert.isTrue(!blocklyId.equals("") && !blockType.equals(""));
        this.blockType = blockType;
        this.blocklyId = blocklyId;
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

    /**
     * factory method: create an object, that has a kind of blockly-related default properties.<br>
     * <b>Main use: testing or textual representation of programs (because in this case no graphical regeneration is required.</b>
     */
    public static BlocklyProperties make(String blockType, String blocklyId) {
        return new BlocklyProperties(blockType, blocklyId, false, false, false, false, false, true, false, false, null);
    }

    public String getBlockType() {
        return this.blockType;
    }

    public String getBlocklyId() {
        return this.blocklyId;
    }

    public Comment getComment() {
        return this.comment;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public boolean isCollapsed() {
        return this.collapsed;
    }

    public Boolean isInline() {
        return this.inline;
    }

    public Boolean isDeletable() {
        return this.deletable;
    }

    public Boolean isMovable() {
        return this.movable;
    }

    public Boolean isInTask() {
        return this.inTask;
    }

    public Boolean isShadow() {
        return this.shadow;
    }

    public Boolean isErrorAttribute() {
        return this.errorAttribute;
    }
}
