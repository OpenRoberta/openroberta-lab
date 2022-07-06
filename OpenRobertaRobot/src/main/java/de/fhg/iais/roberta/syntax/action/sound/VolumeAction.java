package de.fhg.iais.roberta.syntax.action.sound;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.NullConst;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "VOLUME_ACTION", category = "ACTOR", blocklyNames = {"robActions_play_getVolume", "robActions_play_setVolume"})
public final class VolumeAction<V> extends Action<V> {
    public final Mode mode;
    public final Expr<V> volume;
    public final String port;
    public final List<Hide> hide;

    public VolumeAction(Mode mode, Expr<V> volume, String port, List<Hide> hide, BlocklyProperties properties) {
        super(properties);
        Assert.isTrue(volume != null && volume.isReadOnly() && mode != null);
        this.volume = volume;
        this.mode = mode;
        this.port = port;
        this.hide = hide;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "VolumeAction [" + this.mode + ", " + this.volume + "]";
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        Phrase<V> expr;
        Mode mode;
        if ( block.getType().equals(BlocklyConstants.ROB_ACTIONS_PLAY_SET_VOLUME) ) {
            List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
            expr = helper.extractValue(values, new ExprParam(BlocklyConstants.VOLUME, BlocklyType.NUMBER_INT));
            mode = VolumeAction.Mode.SET;
        } else {
            expr = new NullConst<V>(Jaxb2Ast.extractBlocklyProperties(block));
            mode = VolumeAction.Mode.GET;
        }

        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        if ( fields.stream().anyMatch(field -> field.getName().equals(BlocklyConstants.ACTORPORT)) ) {
            String port = Jaxb2Ast.extractField(fields, BlocklyConstants.ACTORPORT);
            return new VolumeAction<>(mode, Jaxb2Ast.convertPhraseToExpr(expr), port, block.getHide(), Jaxb2Ast.extractBlocklyProperties(block));
        }

        return new VolumeAction<>(mode, Jaxb2Ast.convertPhraseToExpr(expr), BlocklyConstants.EMPTY_PORT, null, Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        if ( this.mode == VolumeAction.Mode.SET ) {
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.VOLUME, this.volume);
        }
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.ACTORPORT, port);
        if ( this.hide != null ) {
            jaxbDestination.getHide().addAll(hide);
        }

        return jaxbDestination;
    }

    /**
     * Type of action we want to do (set or get the volume).
     */
    public static enum Mode {
        SET, GET;
    }
}
