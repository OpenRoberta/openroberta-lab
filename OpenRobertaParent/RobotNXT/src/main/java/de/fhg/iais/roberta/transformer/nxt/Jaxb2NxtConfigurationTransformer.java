package de.fhg.iais.roberta.transformer.nxt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.components.nxt.NxtConfiguration;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * JAXB to brick configuration. Client should provide a tree of jaxb objects. Generates a BrickConfiguration object.
 */
public class Jaxb2NxtConfigurationTransformer {
    IRobotFactory factory;

    public Jaxb2NxtConfigurationTransformer(IRobotFactory factory) {
        this.factory = factory;
    }

    public Configuration transform(BlockSet blockSet) {
        List<Instance> instances = blockSet.getInstance();
        List<Block> blocks = instances.get(0).getBlock();
        return blockToBrickConfiguration(blocks.get(0));
    }

    public BlockSet transformInverse(Configuration conf) {
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
        fields.add(mkField("WHEEL_DIAMETER", Util1.formatDouble1digit(conf.getWheelDiameterCM())));
        fields.add(mkField("TRACK_WIDTH", Util1.formatDouble1digit(conf.getTrackWidthCM())));
        List<Value> values = block.getValue();
        {
            Map<ISensorPort, Sensor> sensors = conf.getSensors();
            for ( ISensorPort port : sensors.keySet() ) {
                Sensor sensor = sensors.get(port);
                Value hardwareComponent = new Value();
                hardwareComponent.setName(port.getCodeName());
                Block sensorBlock = mkBlock(idCount++);
                hardwareComponent.setBlock(sensorBlock);
                sensorBlock.setType(sensor.getType().blocklyName());
                values.add(hardwareComponent);
            }
        }
        {
            Map<IActorPort, Actor> actors = conf.getActors();
            for ( IActorPort port : actors.keySet() ) {
                Actor actor = actors.get(port);
                Value hardwareComponent = new Value();
                hardwareComponent.setName(port.getOraName());
                Block actorBlock = mkBlock(idCount++);
                hardwareComponent.setBlock(actorBlock);
                actorBlock.setType(actor.getName().blocklyName());
                List<Field> actorFields = actorBlock.getField();
                actorFields.add(mkField("MOTOR_REGULATION", ("" + actor.isRegulated()).toUpperCase()));
                String rotation = actor.getRotationDirection() == DriveDirection.FOREWARD ? "OFF" : "ON";
                actorFields.add(mkField("MOTOR_REVERSE", rotation));
                if ( !actor.getName().blocklyName().equals("robBrick_motor_middle") ) {
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
        block.setDisabled(false);
        block.setIntask(true);
        return block;
    }

    private Field mkField(String name, String value) {
        Field field = new Field();
        field.setName(name);
        field.setValue(value);
        return field;
    }

    private Configuration blockToBrickConfiguration(Block block) {
        switch ( block.getType() ) {
            case "robBrick_EV3-Brick":
                List<Pair<ISensorPort, Sensor>> sensors = new ArrayList<>();
                List<Pair<IActorPort, Actor>> actors = new ArrayList<>();
                List<Field> fields = extractFields(block, (short) 2);
                double wheelDiameter = Double.valueOf(extractField(fields, "WHEEL_DIAMETER", (short) 0)).doubleValue();
                double trackWidth = Double.valueOf(extractField(fields, "TRACK_WIDTH", (short) 1)).doubleValue();

                List<Value> values = extractValues(block, (short) 8);
                extractHardwareComponent(values, sensors, actors);

                return new NxtConfiguration.Builder().setTrackWidth(trackWidth).setWheelDiameter(wheelDiameter).addActors(actors).addSensors(sensors).build();
            default:
                throw new DbcException("There was no correct configuration block found!");
        }
    }

    private void extractHardwareComponent(List<Value> values, List<Pair<ISensorPort, Sensor>> sensors, List<Pair<IActorPort, Actor>> actors) {
        for ( Value value : values ) {
            if ( value.getName().startsWith("S") ) {
                // Extract sensor
                sensors.add(Pair.of(this.factory.getSensorPort(value.getName()), new Sensor(SensorType.get(value.getBlock().getType()))));
            } else {
                List<Field> fields;
                // Extract actor
                switch ( value.getBlock().getType() ) {
                    case "robBrick_motor_middle":
                        fields = extractFields(value.getBlock(), (short) 2);
                        actors.add(
                            Pair.of(
                                this.factory.getActorPort(value.getName()),
                                new Actor(
                                    ActorType.get(value.getBlock().getType()),
                                    extractField(fields, "MOTOR_REGULATION", 0).equals("TRUE"),
                                    this.factory.getDriveDirection(extractField(fields, "MOTOR_REVERSE", 1)),
                                    MotorSide.NONE)));

                        break;
                    case "robBrick_motor_big":

                        fields = extractFields(value.getBlock(), (short) 3);
                        actors.add(
                            Pair.of(
                                this.factory.getActorPort(value.getName()),
                                new Actor(
                                    ActorType.get(value.getBlock().getType()),
                                    extractField(fields, "MOTOR_REGULATION", 0).equals("TRUE"),
                                    this.factory.getDriveDirection(extractField(fields, "MOTOR_REVERSE", 1)),
                                    this.factory.getMotorSide(extractField(fields, "MOTOR_DRIVE", 2)))));
                        break;
                    case "robBrick_actor":
                        actors.add(
                            Pair.of(
                                this.factory.getActorPort(value.getName()),
                                new Actor(ActorType.get(value.getBlock().getType()), false, DriveDirection.FOREWARD, MotorSide.NONE)));
                        break;
                    default:
                        throw new DbcException("Invalid motor type!");
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
