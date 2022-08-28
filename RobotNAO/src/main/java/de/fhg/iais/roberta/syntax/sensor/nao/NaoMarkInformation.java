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

@NepoBasic(name = "NAO_MARK_INFORMATION", category = "SENSOR", blocklyNames = {"naoSensors_getMarkInformation"})
public final class NaoMarkInformation extends Sensor {

    public final Expr naoMarkId;

    public NaoMarkInformation(Expr naoMarkId, BlocklyProperties properties) {
        super(properties);
        this.naoMarkId = naoMarkId;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "NaoMarkInformation [" + this.naoMarkId.toString() + "]";
    }

    public static  Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        Phrase naoMarkId = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, BlocklyType.NUMBER));
        return new NaoMarkInformation(Jaxb2Ast.convertPhraseToExpr(naoMarkId), Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.MODE, "");
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.VALUE, this.naoMarkId);
        return jaxbDestination;
    }
}
