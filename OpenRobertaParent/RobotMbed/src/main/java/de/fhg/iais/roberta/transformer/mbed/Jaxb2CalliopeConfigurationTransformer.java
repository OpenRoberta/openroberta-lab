package de.fhg.iais.roberta.transformer.mbed;

import java.util.List;
import java.util.Map;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.mbed.CalliopeConfiguration;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * JAXB to brick configuration. Client should provide a tree of jaxb objects. Generates a BrickConfiguration object.
 */
public class Jaxb2CalliopeConfigurationTransformer {
    IRobotFactory factory;

    public Jaxb2CalliopeConfigurationTransformer(IRobotFactory factory) {
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
        block.setType("robBrick_ardu-Brick");
        instance.getBlock().add(block);
        //        List<Field> fields = block.getField();
        //        fields.add(mkField("WHEEL_DIAMETER", Util1.formatDouble1digit(conf.getWheelDiameterCM())));
        //        fields.add(mkField("TRACK_WIDTH", Util1.formatDouble1digit(conf.getTrackWidthCM())));
        List<Value> values = block.getValue();
        {
            Map<ISensorPort, Sensor> sensors = conf.getSensors();
            for ( ISensorPort port : sensors.keySet() ) {
                Sensor sensor = sensors.get(port);
                Value hardwareComponent = new Value();
                hardwareComponent.setName(port.toString());
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
            case "mbedBrick_Calliope-Brick":
            case "mbedBrick_microbit-Brick":
                return new CalliopeConfiguration.Builder().build();
            default:
                throw new DbcException("There was no correct configuration block found!");
        }
    }
}
