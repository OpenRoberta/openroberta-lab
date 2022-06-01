package de.fhg.iais.roberta.syntax.sensors.arduino.mbot;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;

public final class FlameSensor<V> extends Sensor<V> {

    private final String port;

    private FlameSensor(String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("FLAMESENSOR_GET_SAMPLE"), properties, comment);
        this.port = port;
        setReadOnly();
    }

    /**
     * Creates instance of {@link FlameSensor}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link FlameSensor}
     */
    static <V> FlameSensor<V> make(String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new FlameSensor<>(port, properties, comment);
    }

    public String getPort() {
        return this.port;
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        final BlocklyDropdownFactory factory = helper.getDropdownFactory();
        final List<Field> fields = Jaxb2Ast.extractFields(block, (short) 3);
        final String port = Jaxb2Ast.extractField(fields, BlocklyConstants.SENSORPORT);
        return FlameSensor.make(Jaxb2Ast.sanitizePort(port), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        final Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        final String fieldValue = this.port;
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);

        return jaxbDestination;
    }

    @Override
    public String toString() {
        return "flameSensor [port = " + this.port + "]";
    }

}
