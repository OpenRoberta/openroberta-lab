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

@NepoBasic(name = "LSM9DS1_MAGNETICFIELD", category = "SENSOR", blocklyNames = {"robsensors_lsm9ds1_magneticfield_getDataAvailableSample"})
public final class Lsm9ds1MagneticFieldSensor extends InternalSensor {

    public final Expr x, y, z;

    public Lsm9ds1MagneticFieldSensor(BlocklyProperties properties, Expr x, Expr y, Expr z) {
        super(properties, null);
        this.x = x;
        this.y = y;
        this.z = z;
        setReadOnly();
    }

    public static Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 3);
        Expr x = helper.getVar(values, BlocklyConstants.VARIABLE_X);
        Expr y = helper.getVar(values, BlocklyConstants.VARIABLE_Y);
        Expr z = helper.getVar(values, BlocklyConstants.VARIABLE_Z);
        return new Lsm9ds1MagneticFieldSensor(Jaxb2Ast.extractBlocklyProperties(block), x, y, z);
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