package de.fhg.iais.roberta.ast.transformer;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.DriveDirection;
import de.fhg.iais.roberta.ast.syntax.action.MotorSide;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.brickconfiguration.BrickConfiguration;
import de.fhg.iais.roberta.brickconfiguration.ev3.EV3Actor;
import de.fhg.iais.roberta.brickconfiguration.ev3.EV3BrickConfiguration;
import de.fhg.iais.roberta.brickconfiguration.ev3.EV3Sensor;
import de.fhg.iais.roberta.dbc.DbcException;
import de.fhg.iais.roberta.hardwarecomponents.ev3.HardwareComponentEV3Actor;
import de.fhg.iais.roberta.hardwarecomponents.ev3.HardwareComponentEV3Sensor;
import de.fhg.iais.roberta.util.Pair;

/**
 * JAXB to AST transformer for the brick configuration. Client should provide tree of jaxb objects.
 *
 * @param <V>
 */
public class JaxbBrickConfigTransformer<V> extends JaxbAstTransformer<V> {

    /**
     * Returns brick configuration from given Blockly brick configuration.
     *
     * @param configuration
     * @return brick configuration
     */
    public BrickConfiguration transform(BlockSet configuration) {
        List<Instance> instances = configuration.getInstance();
        List<Block> blocks = instances.get(0).getBlock();
        return blockToBrickConfiguration(blocks.get(0));
    }

    private void extractHardwareComponent(List<Value> values, List<Pair<SensorPort, EV3Sensor>> sensors, List<Pair<ActorPort, EV3Actor>> actors) {
        for ( Value value : values ) {
            if ( value.getName().startsWith("S") ) {
                //Extract sensor
                sensors.add(Pair.of(SensorPort.get(value.getName()), new EV3Sensor(extractHardwareComponentTypeSensor(value.getBlock()))));
            } else {
                //Extract actor
                actors.add(Pair.of(ActorPort.get(value.getName()), new EV3Actor(
                    extractHardwareComponentTypeActor(value.getBlock()),
                    extractMotorRegulation(value.getBlock()),
                    extractDirectionOfRotation(value.getBlock()),
                    extractMotorSide(value.getBlock()))));
            }
        }
    }

    private HardwareComponentEV3Sensor extractHardwareComponentTypeSensor(Block component) {
        return HardwareComponentEV3Sensor.find(component.getType());
    }

    private HardwareComponentEV3Actor extractHardwareComponentTypeActor(Block component) {
        return HardwareComponentEV3Actor.find(component.getType());
    }

    private boolean extractMotorRegulation(Block block) {
        List<Field> fields = extractFields(block, (short) 3);
        return extractField(fields, "MOTOR_REGULATION").equals("TRUE") ? true : false;
    }

    private DriveDirection extractDirectionOfRotation(Block block) {
        List<Field> fields = extractFields(block, (short) 3);
        return DriveDirection.get(extractField(fields, "MOTOR_REVERSE"));
    }

    private MotorSide extractMotorSide(Block block) {
        List<Field> fields = extractFields(block, (short) 3);
        return MotorSide.get(extractField(fields, "MOTOR_DRIVE"));
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
                wheelDiameter = Double.valueOf(extractField(fields, "WHEEL_DIAMETER")).doubleValue();
                trackWidth = Double.valueOf(extractField(fields, "TRACK_WIDTH")).doubleValue();
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
