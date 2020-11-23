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
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;

public class Apds9960DistanceSensor<V> extends BuiltinSensor<V> {

    private final Var<V> distance;

    public Var<V> getDistance() {
        return distance;
    }

    private Apds9960DistanceSensor(BlocklyBlockProperties properties, BlocklyComment comment, Var<V> distance) {
        super(null, BlockTypeContainer.getByName("APDS9960_DISTANCE"), properties, comment);
        this.distance = distance;
        setReadOnly();
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IArduinoVisitor<V>) visitor).visitApds9960DistanceSensor(this);
    }

    public static <V> Apds9960DistanceSensor<V> make(BlocklyBlockProperties properties, BlocklyComment comment, Var<V> distance) {
        return new Apds9960DistanceSensor<>(properties, comment, distance);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        Var<V> distance = helper.getVar(values, BlocklyConstants.VARIABLE_VALUE);
        return Apds9960DistanceSensor.make(Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block), distance);
    }

    @Override
    public Block astToBlock() {
        Block block = new Block();
        Ast2Jaxb.setBasicProperties(this, block);
        Ast2Jaxb.addValue(block, BlocklyConstants.VARIABLE_VALUE, this.distance);
        return block;
    }
}