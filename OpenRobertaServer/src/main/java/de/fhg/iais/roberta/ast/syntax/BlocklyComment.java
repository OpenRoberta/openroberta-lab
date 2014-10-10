package de.fhg.iais.roberta.ast.syntax;

import de.fhg.iais.roberta.dbc.Assert;

public class BlocklyComment {

    private final String comment;
    private final boolean pinned;
    private final String height;
    private final String width;

    private BlocklyComment(String comment, boolean pinned, String height, String width) {
        super();
        Assert.isTrue(comment != "" && height != "" && width != "");
        this.comment = comment;
        this.pinned = pinned;
        this.height = height;
        this.width = width;
    }

    public static BlocklyComment make(String comment, boolean pinned, String height, String width) {
        return new BlocklyComment(comment, pinned, height, width);
    }

    public String getComment() {
        return this.comment;
    }

    public boolean isPinned() {
        return this.pinned;
    }

    public String getHeight() {
        return this.height;
    }

    public String getWidth() {
        return this.width;
    }

}