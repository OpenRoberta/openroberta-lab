package de.fhg.iais.roberta.syntax.sensor.makeblock;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.syntax.sensor.makeblock.Accelerometer.Coordinate;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.MakeblockAstVisitor;


public final class Accelerometer<V> extends Sensor<V> {

    private final Coordinate coordinate;
    private final ISensorPort port;

    private Accelerometer(Coordinate coordinate, ISensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("ACCELEROMETER_GET_SAMPLE"), properties, comment);
        this.port = port;
        this.coordinate = coordinate;
        setReadOnly();
    }

    /**
     * Creates instance of {@link Gyroscope}. This instance is read only and can not be modified.
     *
     * @param port {@link ActorPort} on which the motor is connected,
     * @param param {@link MotionParam} that set up the parameters for the movement of the robot (number of rotations or degrees and speed),
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link Gyroscope}
     */
    static <V> Accelerometer<V> make(Coordinate coordinate, ISensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new Accelerometer<V>(coordinate, port, properties, comment);
    }

    public Coordinate getCoordinate() {
        return this.coordinate;
    }
    
    public ISensorPort getPort() {
        return this.port;
    }


    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((MakeblockAstVisitor<V>) visitor).visitAccelerometer(this);
    }
    

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
    	IRobotFactory factory = helper.getModeFactory();
        List<Field> fields = helper.extractFields(block, (short) 2);
        String coordinate = helper.extractField(fields, BlocklyConstants.COORDINATE);
        String port = helper.extractField(fields, BlocklyConstants.SENSORPORT);
        return Accelerometer
            .make(Coordinate.valueOf(coordinate), factory.getSensorPort(port), helper.extractBlockProperties(block), helper.extractComment(block));
    }


    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        String fieldValue = this.port.getPortNumber();
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.COORDINATE, this.coordinate.toString());

        return jaxbDestination;
    }
    

    @Override
    public String toString() {
        return "Acceletometer [port = " + this.port + ", coordinate  = " + this.coordinate + "]";
    }
 



    /**
     * Modes in which the sensor can operate.
     */
    public static enum Coordinate {
    	  X, Y, Z;
    }

}
