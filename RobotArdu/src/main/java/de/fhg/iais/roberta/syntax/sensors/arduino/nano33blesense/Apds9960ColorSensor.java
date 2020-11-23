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

public class Apds9960ColorSensor<V> extends BuiltinSensor<V> {

    private final Var<V> r, g, b;

    public Var<V> getR() {
        return r;
    }

    public Var<V> getG() {
        return g;
    }

    public Var<V> getB() {
        return b;
    }

    private Apds9960ColorSensor(BlocklyBlockProperties properties, BlocklyComment comment, Var<V> r, Var<V> g, Var<V> b) {
        super(null, BlockTypeContainer.getByName("APDS9960_COLOR"), properties, comment);
        this.r = r;
        this.g = g;
        this.b = b;
        setReadOnly();
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IArduinoVisitor<V>) visitor).visitApds9960ColorSensor(this);
    }

    public static <V> Apds9960ColorSensor<V> make(BlocklyBlockProperties properties, BlocklyComment comment, Var<V> r, Var<V> g, Var<V> b) {
        return new Apds9960ColorSensor<>(properties, comment, r, g, b);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 3);
        Var<V> r = helper.getVar(values, BlocklyConstants.VARIABLE_R);
        Var<V> g = helper.getVar(values, BlocklyConstants.VARIABLE_G);
        Var<V> b = helper.getVar(values, BlocklyConstants.VARIABLE_B);
        return Apds9960ColorSensor.make(Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block), r, g, b);
    }

    @Override
    public Block astToBlock() {
        Block block = new Block();
        Ast2Jaxb.setBasicProperties(this, block);
        Ast2Jaxb.addValue(block, BlocklyConstants.VARIABLE_R, this.r);
        Ast2Jaxb.addValue(block, BlocklyConstants.VARIABLE_G, this.g);
        Ast2Jaxb.addValue(block, BlocklyConstants.VARIABLE_B, this.b);
        return block;
    }
}