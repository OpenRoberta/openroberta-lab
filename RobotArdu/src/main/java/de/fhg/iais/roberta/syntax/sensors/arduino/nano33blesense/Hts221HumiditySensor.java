package de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.BuiltinSensor;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "HTS221_HUMIDITY", category = "SENSOR", blocklyNames = {"robsensors_hts221_humidity_getDataAvailableSample"})
public final class Hts221HumiditySensor<V> extends BuiltinSensor<V> {

    public final Expr<V> humidity;

    public Expr<V> getHumidity() {
        return humidity;
    }

    private Hts221HumiditySensor(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> humidity) {
        super(properties, comment, null);
        this.humidity = humidity;
        setReadOnly();
    }

    public static <V> Hts221HumiditySensor<V> make(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> humidity) {
        return new Hts221HumiditySensor<>(properties, comment, humidity);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        Expr<V> humidity = helper.getVar(values, BlocklyConstants.VARIABLE_VALUE);
        return Hts221HumiditySensor.make(Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block), humidity);
    }

    @Override
    public Block astToBlock() {
        Block block = new Block();
        Ast2Jaxb.setBasicProperties(this, block);
        Ast2Jaxb.addValue(block, BlocklyConstants.VARIABLE_VALUE, this.humidity);
        return block;
    }
}