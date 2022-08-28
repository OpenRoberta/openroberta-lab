package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.mode.action.nao.Posture;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "APPLY_POSTURE", category = "ACTOR", blocklyNames = {"naoActions_applyPosture"})
public final class ApplyPosture extends Action {

    public final Posture posture;

    public ApplyPosture(Posture posture, BlocklyProperties properties) {
        super(properties);
        Assert.notNull(posture, "Missing posture in ApplyPosture block!");
        this.posture = posture;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "ApplyPosture [" + this.posture + "]";
    }

    public static  Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        String posture = Jaxb2Ast.extractField(fields, BlocklyConstants.DIRECTION);
        return new ApplyPosture(Posture.get(posture), Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.DIRECTION, this.posture.toString());

        return jaxbDestination;
    }
}
