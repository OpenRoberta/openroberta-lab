package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.mode.action.nao.Joint;
import de.fhg.iais.roberta.mode.action.nao.RelativeAbsolute;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "MOVE_JOINT", category = "ACTOR", blocklyNames = {"naoActions_moveJoint"})
public final class MoveJoint extends Action {

    public final Joint joint;
    public final RelativeAbsolute relativeAbsolute;
    public final Expr degrees;

    public MoveJoint(Joint joint, RelativeAbsolute relativeAbsolute, Expr degrees, BlocklyProperties properties) {
        super(properties);
        Assert.notNull(joint, "Missing joint in MoveJoint block!");
        Assert.notNull(relativeAbsolute, "Missing relativeAbsolute in MoveJoint block!");
        this.joint = joint;
        this.relativeAbsolute = relativeAbsolute;
        this.degrees = degrees;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "MoveJoint [" + this.joint + ", " + this.relativeAbsolute + ", " + this.degrees + "]";
    }

    public static  Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);

        String joint = Jaxb2Ast.extractField(fields, BlocklyConstants.JOINT);
        String relativeAbsolute = Jaxb2Ast.extractField(fields, BlocklyConstants.MODE);

        Phrase walkDistance = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, BlocklyType.NUMBER_INT));

        return new MoveJoint(Joint.get(joint), RelativeAbsolute.get(relativeAbsolute), Jaxb2Ast.convertPhraseToExpr(walkDistance), Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.JOINT, this.joint.toString());
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.MODE, this.relativeAbsolute.toString());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.POWER, this.degrees);

        return jaxbDestination;
    }
}
