package de.fhg.iais.roberta.syntax.sensor.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

/**
 * This class represents the <b>naoSensors_getFaceInformation</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate
 * code for detecting a NaoMark.<br/>
 * <br/>
 */
@NepoBasic(name = "NAO_FACE_INFORMATION", category = "SENSOR", blocklyNames = {"naoSensors_getFaceInformation"})
public final class DetectedFaceInformation<V> extends Sensor<V> {

    public final Expr<V> faceName;

    public DetectedFaceInformation(Expr<V> faceName, BlocklyProperties properties) {
        super(properties);
        this.faceName = faceName;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "DetectedFaceInformation [" + this.faceName + "]";
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        Phrase<V> faceName = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, BlocklyType.STRING));
        return new DetectedFaceInformation<>(Jaxb2Ast.convertPhraseToExpr(faceName), Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.MODE, "");
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.VALUE, this.faceName);
        return jaxbDestination;
    }
}
