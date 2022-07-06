package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.mode.action.nao.Frame;
import de.fhg.iais.roberta.mode.action.nao.PointLook;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "POINT_LOOK_AT", category = "ACTOR", blocklyNames = {"naoActions_pointLookAt"})
public final class PointLookAt extends Action {

    public final Frame frame;
    public final PointLook pointLook;
    public final Expr pointX;
    public final Expr pointY;
    public final Expr pointZ;
    public final Expr speed;

    public PointLookAt(
        Frame frame,
        PointLook pointLook,
        Expr pointX,
        Expr pointY,
        Expr pointZ,
        Expr speed,
        BlocklyProperties properties) {
        super(properties);
        //Assert.notNull(frame, "Missing frame in PointLookAt block!");
        //Assert.notNull(pointLook, "Missing point look in PointLookAt block!");
        this.frame = frame;
        this.pointLook = pointLook;
        this.pointX = pointX;
        this.pointY = pointY;
        this.pointZ = pointZ;
        this.speed = speed;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "PointLookAt [" + this.frame + ", " + this.pointLook + ", " + this.pointX + ", " + this.pointY + ", " + this.pointZ + ", " + this.speed + "]";
    }

    public static  Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 4);

        String pointLook = Jaxb2Ast.extractField(fields, BlocklyConstants.MODE);
        String frame = Jaxb2Ast.extractField(fields, BlocklyConstants.DIRECTION);
        Phrase pointX = helper.extractValue(values, new ExprParam(BlocklyConstants.X, BlocklyType.NUMBER_INT));
        Phrase pointY = helper.extractValue(values, new ExprParam(BlocklyConstants.Y, BlocklyType.NUMBER_INT));
        Phrase pointZ = helper.extractValue(values, new ExprParam(BlocklyConstants.Z, BlocklyType.NUMBER_INT));
        Phrase speed = helper.extractValue(values, new ExprParam(BlocklyConstants.SPEED, BlocklyType.NUMBER_INT));

        return new PointLookAt(Frame.get(frame), PointLook.get(pointLook), Jaxb2Ast.convertPhraseToExpr(pointX), Jaxb2Ast.convertPhraseToExpr(pointY), Jaxb2Ast.convertPhraseToExpr(pointZ), Jaxb2Ast.convertPhraseToExpr(speed), Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.MODE, this.pointLook.getValues()[0]);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.DIRECTION, this.frame.getValues()[0]);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.X, this.pointX);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.Y, this.pointY);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.Z, this.pointZ);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.SPEED, this.speed);

        return jaxbDestination;
    }
}
