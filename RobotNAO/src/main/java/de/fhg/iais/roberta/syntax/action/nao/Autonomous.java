package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.mode.general.WorkingState;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "AUTONOMOUS", category = "ACTOR", blocklyNames = {"naoActions_autonomous"})
public final class Autonomous<V> extends Action<V> {

    public final WorkingState onOff;

    public Autonomous(WorkingState onOff, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment);
        Assert.notNull(onOff, "Missing onOff in Autonomous block!");
        this.onOff = onOff;
        setReadOnly();
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);

        String onOff = Jaxb2Ast.extractField(fields, BlocklyConstants.MODE);

        return new Autonomous<>(WorkingState.get(onOff), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public String toString() {
        return "SetStiffness [" + ", " + this.onOff + "]";
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.MODE, this.onOff.toString());

        return jaxbDestination;
    }
}