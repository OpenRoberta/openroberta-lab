package de.fhg.iais.roberta.transformer.ev3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.components.ev3.EV3Actor;
import de.fhg.iais.roberta.components.ev3.EV3Actors;
import de.fhg.iais.roberta.components.ev3.EV3Sensor;
import de.fhg.iais.roberta.components.ev3.EV3Sensors;
import de.fhg.iais.roberta.components.ev3.Ev3Configuration;
import de.fhg.iais.roberta.shared.action.ev3.ActorPort;
import de.fhg.iais.roberta.shared.action.ev3.DriveDirection;
import de.fhg.iais.roberta.shared.action.ev3.MotorSide;
import de.fhg.iais.roberta.shared.sensor.ev3.SensorPort;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * JAXB to brick configuration. Client should provide a tree of jaxb objects.
 * Generates a BrickConfiguration object.
 */
public class Jaxb2Ev3ConfigurationTransformer {
    public Ev3Configuration transform(BlockSet blockSet) {
        List<Instance> instances = blockSet.getInstance();
        List<Block> blocks = instances.get(0).getBlock();
        return blockToBrickConfiguration(blocks.get(0));
    }

    public BlockSet transformInverse(Ev3Configuration conf) {
        int idCount = 1;
        BlockSet blockSet = new BlockSet();
        Instance instance = new Instance();
        blockSet.getInstance().add(instance);
        instance.setX("20");
        instance.setY("20");
        Block block = mkBlock(idCount++);
        block.setType("robBrick_EV3-Brick");
        instance.getBlock().add(block);
        List<Field> fields = block.getField();
        fields.add(mkField("WHEEL_DIAMETER", Util.formatDouble1digit(conf.getWheelDiameterCM())));
        fields.add(mkField("TRACK_WIDTH", Util.formatDouble1digit(conf.getTrackWidthCM())));
        List<Value> values = block.getValue();
        {
            Map<SensorPort, EV3Sensor> sensors = conf.getSensors();
            for ( SensorPort port : sensors.keySet() ) {
                EV3Sensor sensor = sensors.get(port);
                Value hardwareComponent = new Value();
                hardwareComponent.setName(port.toString());
                Block sensorBlock = mkBlock(idCount++);
                hardwareComponent.setBlock(sensorBlock);
                sensorBlock.setType(sensor.getComponentType().getName());
                values.add(hardwareComponent);
            }
        }
        {
            Map<ActorPort, EV3Actor> actors = conf.getActors();
            for ( ActorPort port : actors.keySet() ) {
                EV3Actor actor = actors.get(port);
                Value hardwareComponent = new Value();
                hardwareComponent.setName(port.getXmlName());
                Block actorBlock = mkBlock(idCount++);
                hardwareComponent.setBlock(actorBlock);
                actorBlock.setType(actor.getComponentType().getName());
                List<Field> actorFields = actorBlock.getField();
                actorFields.add(mkField("MOTOR_REGULATION", ("" + actor.isRegulated()).toUpperCase()));
                actorFields.add(mkField("MOTOR_REVERSE", actor.getRotationDirection().toString()));
                if ( !actor.getComponentType().getName().equals("robBrick_motor_middle") ) {
                    actorFields.add(mkField("MOTOR_DRIVE", actor.getMotorSide().toString()));
                }
                values.add(hardwareComponent);
            }
        }
        return blockSet;
    }

    private Block mkBlock(int id) {
        Block block = new Block();
        block.setId("" + id);
        block.setInline(false);
        block.setDisabled(true);
        return block;
    }

    private Field mkField(String name, String value) {
        Field field = new Field();
        field.setName(name);
        field.setValue(value);
        return field;
    }

    private Ev3Configuration blockToBrickConfiguration(Block block) {
        switch ( block.getType() ) {
            case "robBrick_EV3-Brick":
                List<Pair<SensorPort, EV3Sensor>> sensors = new ArrayList<Pair<SensorPort, EV3Sensor>>();
                List<Pair<ActorPort, EV3Actor>> actors = new ArrayList<Pair<ActorPort, EV3Actor>>();
                List<Field> fields = extractFields(block, (short) 2);
                double wheelDiameter = Double.valueOf(extractField(fields, "WHEEL_DIAMETER", (short) 0)).doubleValue();
                double trackWidth = Double.valueOf(extractField(fields, "TRACK_WIDTH", (short) 1)).doubleValue();

                List<Value> values = extractValues(block, (short) 8);
                extractHardwareComponent(values, sensors, actors);

                return new Ev3Configuration.Builder().setTrackWidth(trackWidth).setWheelDiameter(wheelDiameter).addActors(actors).addSensors(sensors).build();
            default:
                throw new DbcException("There was no correct configuration block found!");
        }
    }

    private void extractHardwareComponent(List<Value> values, List<Pair<SensorPort, EV3Sensor>> sensors, List<Pair<ActorPort, EV3Actor>> actors) {
        for ( Value value : values ) {
            if ( value.getName().startsWith("S") ) {
                // Extract sensor
                sensors.add(Pair.of(SensorPort.get(value.getName()), new EV3Sensor(EV3Sensors.find(value.getBlock().getType()))));
            } else {
                List<Field> fields;
                // Extract actor
                switch ( value.getBlock().getType() ) {
                    case "robBrick_motor_middle":
                        fields = extractFields(value.getBlock(), (short) 2);
                        actors.add(Pair.of(
                            ActorPort.get(value.getName()),
                            new EV3Actor(EV3Actors.find(value.getBlock().getType()), extractField(fields, "MOTOR_REGULATION", 0).equals("TRUE"), DriveDirection
                                .get(extractField(fields, "MOTOR_REVERSE", 1)), MotorSide.NONE)));

                        break;
                    case "robBrick_motor_big":
                        fields = extractFields(value.getBlock(), (short) 3);
                        actors.add(Pair.of(
                            ActorPort.get(value.getName()),
                            new EV3Actor(EV3Actors.find(value.getBlock().getType()), extractField(fields, "MOTOR_REGULATION", 0).equals("TRUE"), DriveDirection
                                .get(extractField(fields, "MOTOR_REVERSE", 1)), MotorSide.get(extractField(fields, "MOTOR_DRIVE", 2)))));
                        break;
                    default:
                        throw new DbcException("Invalide motor type!");
                }
            }
        }
    }

    private List<Value> extractValues(Block block, int numOfValues) {
        List<Value> values;
        values = block.getValue();
        Assert.isTrue(values.size() <= numOfValues, "Values size is not less or equal to " + numOfValues + "!");
        return values;
    }

    private List<Field> extractFields(Block block, int numOfFields) {
        List<Field> fields;
        fields = block.getField();
        Assert.isTrue(fields.size() == numOfFields, "Number of fields is not equal to " + numOfFields + "!");
        return fields;
    }

    private String extractField(List<Field> fields, String name, int fieldLocation) {
        Field field = fields.get(fieldLocation);
        Assert.isTrue(field.getName().equals(name), "Field name is not equal to " + name + "!");
        return field.getValue();
    }
}
