package de.fhg.iais.roberta.ast.transformer;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.EV3Actor;
import de.fhg.iais.roberta.ast.syntax.EV3BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.EV3Sensor;
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

    public BrickConfiguration transform(BlockSet program) {
        List<Instance> instances = program.getInstance();
        List<Block> blocks = instances.get(0).getBlock();
        return blockToBrickConfiguration(blocks.get(0));
    }

    private void extractHardwareComponent(List<Value> values, List<Pair<SensorPort, EV3Sensor>> sensors, List<Pair<ActorPort, EV3Actor>> actors) {
        for ( Value value : values ) {
            if ( value.getName().startsWith("S") ) {
                //Extract sensor
                sensors.add(Pair.of(SensorPort.get(value.getName()), new EV3Sensor(extractHardwareComponentType(value.getBlock()))));
            } else {
                //Extract actor
                actors.add(Pair.of(ActorPort.get(value.getName()), new EV3Actor(
                    extractHardwareComponentType(value.getBlock()),
                    extractDirectionOfRotation(value.getBlock()),
                    extractMotorSide(value.getBlock()))));
            }
        }
    }

    private HardwareComponentType extractHardwareComponentType(Block component) {
        List<String> attributes = new ArrayList<String>();
        attributes.add(component.getType());
        if ( component.getType().equals("robBrick_motor_middle") || component.getType().equals("robBrick_motor_big") ) {
            attributes.add(extractMotorRegulation(component));
        }
        return HardwareComponentType.attributesMatch(attributes.toArray(new String[attributes.size()]));
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

    private BrickConfiguration blockToBrickConfiguration(Block block) {
        List<Field> fields;
        List<Value> values;

        List<Pair<SensorPort, EV3Sensor>> sensors = new ArrayList<Pair<SensorPort, EV3Sensor>>();
        List<Pair<ActorPort, EV3Actor>> actors = new ArrayList<Pair<ActorPort, EV3Actor>>();

        double trackWidth;
        double wheelDiameter;

        switch ( block.getType() ) {
            case "robBrick_EV3-Brick":
                fields = extractFields(block, (short) 2);
                values = extractValues(block, (short) 8);
                wheelDiameter = Double.valueOf(extractField(fields, "WHEEL_DIAMETER", (short) 0)).doubleValue();
                trackWidth = Double.valueOf(extractField(fields, "TRACK_WIDTH", (short) 1)).doubleValue();
                extractHardwareComponent(values, sensors, actors);
                return new EV3BrickConfiguration.Builder()
                    .setTrackWidth(trackWidth)
                    .setWheelDiameter(wheelDiameter)
                    .addActors(actors)
                    .addSensors(sensors)
                    .build();
            default:
                throw new DbcException("There was no correct configuration block found!");
        }
    }

    @Override
    protected Phrase<V> blockToAST(Block block) {
        return null;
    }
}
