package de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.BuiltinSensor;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;

public class Apds9960ColorSensor<V> extends BuiltinSensor<V> {

    private final Expr<V> r, g, b;

    public Expr<V> getR() {
        return r;
    }

    public Expr<V> getG() {
        return g;
    }

    public Expr<V> getB() {
        return b;
    }

    private Apds9960ColorSensor(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> r, Expr<V> g, Expr<V> b) {
        super(null, BlockTypeContainer.getByName("APDS9960_COLOR"), properties, comment);
        this.r = r;
        this.g = g;
        this.b = b;
        setReadOnly();
    }

    public static <V> Apds9960ColorSensor<V> make(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> r, Expr<V> g, Expr<V> b) {
        return new Apds9960ColorSensor<>(properties, comment, r, g, b);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 3);
        Expr<V> r = helper.getVar(values, BlocklyConstants.VARIABLE_R);
        Expr<V> g = helper.getVar(values, BlocklyConstants.VARIABLE_G);
        Expr<V> b = helper.getVar(values, BlocklyConstants.VARIABLE_B);
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