package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class describes the blockly-related properties (most of them are graphical properties) of a block from the AST.
 * <p>
 * Information contained in this class is used to recreate a blockly block when the AST is used to regenerate the original source.<br>
 * Every object that can be part of the AST (subclasses of Phrase<V>) should contain an instance an instance of this class, otherwise their graphical
 * representation cannot be regenerated.<br>
 * <br>
 */
public class BlocklyBlockProperties {
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

    private BlocklyBlockProperties(
        String blockType,
        String blocklyId,
        boolean disabled,
        boolean collapsed,
        Boolean inline,
        Boolean deletable,
        Boolean movable,
        Boolean inTask,
        Boolean shadow,
        Boolean errorAttribute) {
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
    }

    /**
     * factory method: create an object, that saves the blockly-related properties of a block for later regeneration.<br>
     * The names of the parameter should be self-explaining.
     *
     * @param blockType type of the block (<i>name of the block defined in blockly</i>); must be non-empty string
     * @param blocklyId id of the block when is transformed to XML; must be non-empty string
     * @param disabled true if the block is disabled
     * @param collapsed true if the block is collapsed
     * @param inline true if the block is inline (in one line)
     * @param deletable true if the block can be deleted
     * @param movable true if the block can be moved
     * @return property object with given properties
     */
    public static BlocklyBlockProperties make(
        String blockType,
        String blocklyId,
        boolean disabled,
        boolean collapsed,
        Boolean inline,
        Boolean deletable,
        Boolean movable,
        Boolean inTask,
        Boolean shadow,
        Boolean errorAttribute) {
        return new BlocklyBlockProperties(blockType, blocklyId, disabled, collapsed, inline, deletable, movable, inTask, shadow, errorAttribute);
    }

    /**
     * factory method: create an object, that has a kind of blockly-related default properties.<br>
     * <b>Main use: either testing or textual representation of programs (because in this case no graphical regeneration is required.</b>
     *
     * @param blockType
     * @param blocklyId
     * @return
     */
    public static BlocklyBlockProperties make(String blockType, String blocklyId) {
        return BlocklyBlockProperties.make(blockType, blocklyId, false, false, false, false, false, true, false, false);
    }

    /**
     * @return type of the block; type of the blocks is the name of the block in Blockly (ex. <b>robActions_motorDiff_stop</b>)
     */
    public String getBlockType() {
        return this.blockType;
    }

    /**
     * @return id of the block; it is generated when Blockly blocks are exported to XML
     */
    public String getBlocklyId() {
        return this.blocklyId;
    }

    /**
     * @return true if the block is disabled
     */
    public boolean isDisabled() {
        return this.disabled;
    }

    /**
     * @return true if the block is collapsed
     */
    public boolean isCollapsed() {
        return this.collapsed;
    }

    /**
     * @return true if the block is inline (in one line)
     */
    public Boolean isInline() {
        return this.inline;
    }

    /**
     * @return true if the block can be deleted
     */
    public Boolean isDeletable() {
        return this.deletable;
    }

    /**
     * @return the movable
     */
    public Boolean isMovable() {
        return this.movable;
    }

    /**
     * @return true if the block is executed in task o/w false
     */
    public Boolean isInTask() {
        return this.inTask;
    }

    public Boolean isShadow() {
        return this.shadow;
    }

    public Boolean isErrorAttribute() {
        return this.errorAttribute;
    }

    @Override
    public String toString() {
        return "BlocklyBlockProperties [blockType="
            + this.blockType
            + ", blocklyId="
            + this.blocklyId
            + ", disabled="
            + this.disabled
            + ", collapsed="
            + this.collapsed
            + ", inline="
            + this.inline
            + ", deletable="
            + this.deletable
            + ", movable="
            + this.movable
            + ", inTask="
            + this.inTask
            + ", shadow="
            + this.shadow
            + ", errorAttribute="
            + this.errorAttribute
            + "]";
    }

}
