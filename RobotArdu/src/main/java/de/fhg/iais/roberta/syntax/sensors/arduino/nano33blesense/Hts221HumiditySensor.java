package de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.InternalSensor;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "HTS221_HUMIDITY", category = "SENSOR", blocklyNames = {"robsensors_hts221_humidity_getDataAvailableSample"})
public final class Hts221HumiditySensor extends InternalSensor {

    public final Expr humidity;

    public Hts221HumiditySensor(BlocklyProperties properties, Expr humidity) {
        super(properties, null);
        this.humidity = humidity;
        setReadOnly();
    }

    public static Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        Expr humidity = helper.getVar(values, BlocklyConstants.VARIABLE_VALUE);
        return new Hts221HumiditySensor(Jaxb2Ast.extractBlocklyProperties(block), humidity);
    }

    @Override
    public Block astToBlock() {
        Block block = new Block();
        Ast2Jaxb.setBasicProperties(this, block);
        Ast2Jaxb.addValue(block, BlocklyConstants.VARIABLE_VALUE, this.humidity);
        return block;
    }
}