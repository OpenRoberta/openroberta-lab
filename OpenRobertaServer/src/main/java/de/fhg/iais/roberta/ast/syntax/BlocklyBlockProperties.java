package de.fhg.iais.roberta.ast.syntax;

import de.fhg.iais.roberta.dbc.Assert;

public class BlocklyBlockProperties implements INepoId {
    private final String blockType;
    private final String blocklyId;
    private final boolean disabled;
    private final boolean collapsed;
    private final Boolean inline;

    private BlocklyBlockProperties(String blockType, String blocklyId, boolean disabled, boolean collapsed, Boolean inline) {
        super();
        Assert.isTrue(blocklyId != "" && blockType != "");
        this.blockType = blockType;
        this.blocklyId = blocklyId;
        this.disabled = disabled;
        this.collapsed = collapsed;
        this.inline = inline;
    }

    public static BlocklyBlockProperties of(String blockType, String blocklyId, boolean disabled, boolean collapsed, Boolean inline) {
        return new BlocklyBlockProperties(blockType, blocklyId, disabled, collapsed, inline);
    }

    public String getBlockType() {
        return this.blockType;
    }

    public String getBlocklyId() {
        return this.blocklyId;
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

    @Override
    public String toString() {
        return "NepoBlocklyId [blocklyId=" + this.blocklyId + ", disabled=" + this.disabled + ", collapsed=" + this.collapsed + ", inline=" + this.inline + "]";
    }
}
