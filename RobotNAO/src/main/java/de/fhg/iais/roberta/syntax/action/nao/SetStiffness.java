package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.mode.action.nao.BodyPart;
import de.fhg.iais.roberta.mode.general.WorkingState;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "SET_STIFFNESS", category = "ACTOR", blocklyNames = {"naoActions_stiffness"})
public final class SetStiffness extends Action {

    public final BodyPart bodyPart;
    public final WorkingState onOff;

    public SetStiffness(BodyPart bodyPart, WorkingState onOff, BlocklyProperties properties) {
        super(properties);
        Assert.notNull(bodyPart, "Missing body part in SetStiffness block!");
        Assert.notNull(onOff, "Missing onOff in SetStiffness block!");
        this.bodyPart = bodyPart;
        this.onOff = onOff;
        setReadOnly();
    }

    public static  Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);

        String bodyPart = Jaxb2Ast.extractField(fields, BlocklyConstants.PART);
        String onOff = Jaxb2Ast.extractField(fields, BlocklyConstants.MODE);

        return new SetStiffness(BodyPart.get(bodyPart), WorkingState.get(onOff), Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public String toString() {
        return "SetStiffness [" + this.bodyPart + ", " + this.onOff + "]";
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.PART, this.bodyPart.toString());
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.MODE, this.onOff.toString());

        return jaxbDestination;
    }
}