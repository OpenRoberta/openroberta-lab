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

public class Lsm9ds1AccSensor<V> extends BuiltinSensor<V> {

    private final Var<V> x, y, z;

    public Var<V> getX() {
        return x;
    }

    public Var<V> getY() {
        return y;
    }

    public Var<V> getZ() {
        return z;
    }

    private Lsm9ds1AccSensor(BlocklyBlockProperties properties, BlocklyComment comment, Var<V> x, Var<V> y, Var<V> z) {
        super(null, BlockTypeContainer.getByName("LSM9DS1_ACCELERATION"), properties, comment);
        this.x = x;
        this.y = y;
        this.z = z;
        setReadOnly();
    }

    public static <V> Lsm9ds1AccSensor<V> make(BlocklyBlockProperties properties, BlocklyComment comment, Var<V> x, Var<V> y, Var<V> z) {
        return new Lsm9ds1AccSensor<>(properties, comment, x, y, z);
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
        Var<V> x = helper.getVar(values, BlocklyConstants.VARIABLE_X);
        Var<V> y = helper.getVar(values, BlocklyConstants.VARIABLE_Y);
        Var<V> z = helper.getVar(values, BlocklyConstants.VARIABLE_Z);
        return Lsm9ds1AccSensor.make(Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block), x, y, z);
    }

    @Override
    public Block astToBlock() {
        Block block = new Block();
        Ast2Jaxb.setBasicProperties(this, block);
        Ast2Jaxb.addValue(block, BlocklyConstants.VARIABLE_X, this.x);
        Ast2Jaxb.addValue(block, BlocklyConstants.VARIABLE_Y, this.y);
        Ast2Jaxb.addValue(block, BlocklyConstants.VARIABLE_Z, this.z);
        return block;
    }
}