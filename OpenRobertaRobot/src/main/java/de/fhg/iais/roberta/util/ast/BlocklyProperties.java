package de.fhg.iais.roberta.util.ast;

import org.antlr.v4.runtime.ParserRuleContext;

import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class describes representation-related properties of an object of the AST (always sub-classes of {@link de.fhg.iais.roberta.syntax.Phrase}.
 * <ul>
 *     <li>the sub-object {@link #blocklyRegion} stores graphical properties needed by blockly to recreate a blockly block when the AST is used to regenerate
 *   the graphics.
 *     <li> the sub-object {@link #textRegion} stores textual properties (like line and column numbers) that are used for the textual representation of an AST.
 * </ul>
 */
public class BlocklyProperties {
    public final String blockType;
    public final String blocklyId;
    public final BlocklyRegion blocklyRegion;
    public final TextRegion textRegion;

    /**
     * create an object, that saves the blockly-related properties of a block for later regeneration.<br>
     * The names of the parameter should be self-explaining.
     */
    public BlocklyProperties(
        String blockType,
        String blocklyId,
        BlocklyRegion blocklyRegion,
        TextRegion textRegion) {
        Assert.isTrue(!blocklyId.equals("") && !blockType.equals(""));
        this.blockType = blockType;
        this.blocklyId = blocklyId;
        this.blocklyRegion = blocklyRegion;
        this.textRegion = textRegion;
    }

    /**
     * factory method: create an object with blockly default properties.<br>
     * <b>for textual representation of programs (because in this case no real blockly properties are required.</b>
     */
    public static BlocklyProperties make(String blockType, String blocklyId, ParserRuleContext ctx) {
        return BlocklyProperties.make(blockType, blocklyId, false, ctx);
    }

    /**
     * factory method: create an objectwith blockly default properties.<br>
     * <b>for textual representation of programs (because in this case no real blockly properties are required.</b>
     */
    public static BlocklyProperties make(String blockType, String blocklyId, boolean inline, ParserRuleContext ctx) {
        TextRegion rg = ctx == null ? null : new TextRegion(ctx.start.getLine(), ctx.start.getStartIndex(), ctx.stop.getLine(), ctx.stop.getStopIndex());
        BlocklyRegion br = new BlocklyRegion(false, false, inline, true, true, true, false, false, null);
        return new BlocklyProperties(blockType, blocklyId, br, rg);
    }

    public String getTextPosition() {
        if ( this.textRegion == null ) {
            return "at unknown position";
        } else {
            return " at " + this.textRegion.lineStart + ":" + this.textRegion.colStart + " - " + this.textRegion.lineStop + ":" + this.textRegion.colStop;
        }
    }

    public TextRegion getTextRegion() {
        return textRegion;
    }
}
