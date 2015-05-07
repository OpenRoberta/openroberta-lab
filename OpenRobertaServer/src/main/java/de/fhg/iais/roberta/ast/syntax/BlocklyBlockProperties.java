package de.fhg.iais.roberta.ast.syntax;

import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class is describing properties of a current state of Blockly block in the AST.
 * <p>
 * Information contained in this class is used after to recreate Blockly block from AST.<br>
 * Every class from AST representing Blockly block must contain instance of this class.<br>
 * <br>
 * To create a object from this class {@link #make(String, String, boolean, boolean, Boolean, Boolean, Boolean)}
 *
 * @author kcvejoski
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

    private BlocklyBlockProperties(
        String blockType,
        String blocklyId,
        boolean disabled,
        boolean collapsed,
        Boolean inline,
        Boolean deletable,
        Boolean movable,
        Boolean inTask) {
        super();
        Assert.isTrue(!blocklyId.equals("") && !blockType.equals(""));
        this.blockType = blockType;
        this.blocklyId = blocklyId;
        this.disabled = disabled;
        this.collapsed = collapsed;
        this.inline = inline;
        this.deletable = deletable;
        this.movable = movable;
        this.inTask = inTask;
    }

    /**
     * Creates object of type {@link BlocklyBlockProperties}.
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
        Boolean inTask) {
        return new BlocklyBlockProperties(blockType, blocklyId, disabled, collapsed, inline, deletable, movable, inTask);
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
            + "]";
    }

}
