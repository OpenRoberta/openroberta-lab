package de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.sensor.BuiltinSensor;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;

public class Hts221HumiditySensor<V> extends BuiltinSensor<V> {

    private final Var<V> humidity;

    public Var<V> getHumidity() {
        return humidity;
    }

    private Hts221HumiditySensor(BlocklyBlockProperties properties, BlocklyComment comment, Var<V> humidity) {
        super(null, BlockTypeContainer.getByName("HTS221_HUMIDITY"), properties, comment);
        this.humidity = humidity;
        setReadOnly();
    }

    public static <V> Hts221HumiditySensor<V> make(BlocklyBlockProperties properties, BlocklyComment comment, Var<V> humidity) {
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
        Var<V> humidity = helper.getVar(values, BlocklyConstants.VARIABLE_VALUE);
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