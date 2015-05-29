package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class describes the comment and how it is shown to particular blockly block.
 * <p>
 * Information contained in this class is used after to recreate Blockly block from AST.<br>
 * <br>
 * To create a object from this class {@link #make(String, boolean, String, String)}
 *
 * @author kcvejoski
 */
public class BlocklyComment {
    private final String comment;
    private final boolean pinned;
    private final String height;
    private final String width;

    private BlocklyComment(String comment, boolean pinned, String height, String width) {
        super();
        Assert.isTrue(!height.equals("") && !width.equals(""));
        this.comment = comment;
        this.pinned = pinned;
        this.height = height;
        this.width = width;
    }

    /**
     * Create object of type {@link BlocklyComment}.
     *
     * @param comment is the content of the comment; must be non-empty string
     * @param pinned true if the comment is pinned (shown) in the Blockly workspace
     * @param height of the box where the comment is shown; must be non-empty string
     * @param width of the box where the comment is shown; must be non-empty string
     * @return object describing the comment and how it is shown
     */
    public static BlocklyComment make(String comment, boolean pinned, String height, String width) {
        return new BlocklyComment(comment, pinned, height, width);
    }

    /**
     * @return content of the comment
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * @return true if the comment is shown in the Blockly workspace
     */
    public boolean isPinned() {
        return this.pinned;
    }

    /**
     * @return height of the box where the comment is shown
     */
    public String getHeight() {
        return this.height;
    }

    /**
     * @return width of the box where the comment is shown
     */
    public String getWidth() {
        return this.width;
    }

}