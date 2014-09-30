package de.fhg.iais.roberta.ast.transformer;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.HardwareComponent;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.DriveDirection;
import de.fhg.iais.roberta.ast.syntax.action.HardwareComponentType;
import de.fhg.iais.roberta.ast.syntax.action.MotorSide;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.dbc.DbcException;
import de.fhg.iais.roberta.util.Pair;

/**
 * JAXB to AST transformer for the brick configuration. Client should provide tree of jaxb objects.
 * 
 * @param <V>
 */

public class JaxbBrickConfigTransformer<V> extends JaxbAstTransformer<V> {

    public BrickConfiguration blockSetToBrickConfiguration(BlockSet program) {
        List<Instance> instances = program.getInstance();
        List<Block> blocks = instances.get(0).getBlock();
        return blockToBrickConfiguration(blocks.get(0));
    }

    private void extractHardwareComponent(List<Value> values, List<Pair<SensorPort, HardwareComponent>> sensors, List<Pair<ActorPort, HardwareComponent>> actors) {
        for ( Value value : values ) {
            if ( value.getName().startsWith("S") ) {
                //Extract sensor
                sensors.add(Pair.of(SensorPort.get(value.getName()), new HardwareComponent(extractHardwareComponentType(value.getBlock()))));
            } else {
                //Extract actor
                actors.add(Pair.of(ActorPort.get(value.getName()), new HardwareComponent(
                    extractHardwareComponentType(value.getBlock()),
                    extractDirectionOfRotation(value.getBlock()),
                    extractMotorSide(value.getBlock()))));
            }
        }
    }

    private HardwareComponentType extractHardwareComponentType(Block component) {
        switch ( component.getType() ) {
            case "robBrick_touch":
                return HardwareComponentType.EV3TouchSensor;
            case "robBrick_colour":
                return HardwareComponentType.EV3ColorSensor;

            case "robBrick_ultrasonic":
                return HardwareComponentType.EV3UltrasonicSensor;

            case "robBrick_gyro":
                return HardwareComponentType.EV3GyroSensor;

            case "robBrick_infrared":
                return HardwareComponentType.EV3IRSensor;

            case "robBrick_motor_middle":
                return HardwareComponentType.attributesMatch("middle", extractMotorRegulation(component));

            case "robBrick_motor_big":
                return HardwareComponentType.attributesMatch("large", extractMotorRegulation(component));

            case "robBrick_actor":
                return HardwareComponentType.BasicMotor;

            default:
                throw new DbcException("Invalid Hardware Component!");
        }

    }

    private String extractMotorRegulation(Block block) {
        List<Field> fields = extractFields(block, (short) 3);
        return extractField(fields, "MOTOR_REGULATION", (short) 0).equals("TRUE") ? "regulated" : "unregulated";
    }

    private DriveDirection extractDirectionOfRotation(Block block) {
        List<Field> fields = extractFields(block, (short) 3);
        return DriveDirection.get(extractField(fields, "MOTOR_REVERSE", (short) 1));
    }

    private MotorSide extractMotorSide(Block block) {
        List<Field> fields = extractFields(block, (short) 3);
        return MotorSide.get(extractField(fields, "MOTOR_DRIVE", (short) 2));
    }

    @Override
    protected BrickConfiguration blockToBrickConfiguration(Block block) {
        List<Field> fields;
        List<Value> values;

        List<Pair<SensorPort, HardwareComponent>> sensors = new ArrayList<Pair<SensorPort, HardwareComponent>>();
        List<Pair<ActorPort, HardwareComponent>> actors = new ArrayList<Pair<ActorPort, HardwareComponent>>();

        double trackWidth;
        double wheelDiameter;

        switch ( block.getType() ) {
            case "robBrick_EV3-Brick":
                fields = extractFields(block, (short) 2);
                values = extractValues(block, (short) 8);
                wheelDiameter = Double.valueOf(extractField(fields, "WHEEL_DIAMETER", (short) 0)).doubleValue();
                trackWidth = Double.valueOf(extractField(fields, "TRACK_WIDTH", (short) 1)).doubleValue();
                extractHardwareComponent(values, sensors, actors);
                return new BrickConfiguration.Builder().setTrackWidth(trackWidth).setWheelDiameter(wheelDiameter).addActors(actors).addSensors(sensors).build();
            default:
                throw new DbcException("There was no correct configuration block found!");
        }
    }

    @Override
    protected Phrase<V> blockToAST(Block block) {
        return null;
    }

}
