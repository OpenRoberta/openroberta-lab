package de.fhg.iais.roberta.worker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.transformer.Jaxb2ConfigurationAst;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;

public final class MbedTwo2ThreeTransformerHelper {

    private static final Map<Pair<String, String>, String> PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING = new HashMap<>();

    static {
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("ACCELEROMETER_SENSING", "DEFAULT"), "ACCELEROMETER");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("ACCELEROMETER_SENSING", "VALUE"), "ACCELEROMETER");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("COMPASS_SENSING", "ANGLE"), "COMPASS");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("COMPASS_SENSING", "DEFAULT"), "COMPASS");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("FOURDIGITDISPLAY_SHOW_ACTION", ""), "FOURDIGITDISPLAY");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("FOURDIGITDISPLAY_CLEAR_ACTION", ""), "FOURDIGITDISPLAY");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("GYRO_SENSING", "ANGLE"), "GYRO");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("HUMIDITY_SENSING", "HUMIDITY"), "HUMIDITY");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("HUMIDITY_SENSING", "TEMPERATURE"), "HUMIDITY");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("INFRARED_SENSING", "LINE"), "INFRARED");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("KEYS_SENSING", "PRESSED"), "KEY");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("LEDBAR_SET_ACTION", ""), "LEDBAR");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("LED_ON_ACTION", ""), "RGBLED");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("LIGHT_ACTION", "ON"), "LED");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("LIGHT_ACTION", "OFF"), "LED");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("LIGHT_SENSING", "DEFAULT"), "LIGHT");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("LIGHT_SENSING", "LIGHT_VALUE"), "LIGHT");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("LIGHT_SENSING", "VALUE"), "LIGHT");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("LIGHT_STATUS_ACTION", "OFF"), "RGBLED");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("MOTOR_ON_ACTION", ""), "MOTOR");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("MOTOR_STOP_ACTION", ""), "MOTOR");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("PIN_READ_VALUE", "ANALOG"), "ANALOG_PIN");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("PIN_READ_VALUE", "DIGITAL"), "DIGITAL_PIN");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("PIN_READ_VALUE", "PULSEHIGH"), "DIGITAL_PIN");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("PIN_READ_VALUE", "PULSELOW"), "DIGITAL_PIN");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("PIN_SET_PULL", "UP"), "DIGITAL_INPUT");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("PIN_SET_PULL", "DOWN"), "DIGITAL_INPUT");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("PIN_SET_PULL", "NONE"), "DIGITAL_INPUT");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("PIN_WRITE_VALUE", "ANALOG"), "ANALOG_INPUT");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("PIN_WRITE_VALUE", "DIGITAL"), "DIGITAL_INPUT");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("PLAY_NOTE_ACTION", ""), "BUZZER");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("SERVO_SET_ACTION", ""), "SERVOMOTOR");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("SOUND_SENSING", "SOUND"), "SOUND");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("TEMPERATURE_SENSING", "DEFAULT"), "TEMPERATURE");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("TEMPERATURE_SENSING", "TEMPERATURE"), "TEMPERATURE");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("TEMPERATURE_SENSING", "VALUE"), "TEMPERATURE");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("TONE_ACTION", ""), "BUZZER");
        PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.put(Pair.of("ULTRASONIC_SENSING", "DISTANCE"), "ULTRASONIC");
    }

    private final ConfigurationAst transformerConfiguration;

    public MbedTwo2ThreeTransformerHelper(IRobotFactory factory) {
        try {
            BlockSet blockSet = JaxbHelper.xml2BlockSet(factory.getConfigurationTransformer());
            this.transformerConfiguration = Jaxb2ConfigurationAst.blocks2NewConfig(blockSet, factory.getBlocklyDropdownFactory());
        } catch ( JAXBException e ) {
            throw new DbcException("Could not load transformer configuration!", e);
        }
    }

    // every component needs a default value, location, etc -> loaded from transformer configuration
    public ConfigurationComponent getComponent(String progBlockType, String mode, String port) {
        String confType = PROG_BLOCK_TO_CONF_BLOCK_TYPE_MAPPING.get(Pair.of(progBlockType, mode));
        Assert.notNull(confType, progBlockType + " with mode " + mode + " does not have an an associated conf block!");
        List<ConfigurationComponent> comps = this.transformerConfiguration.getConfigurationComponentsValues().stream().filter(cc -> {
            if ( cc.getComponentType().equals(confType) ) {
                String pin1 = cc.getComponentProperties().get("PIN1"); // TODO rename PIN1?
                if ( pin1 != null ) {
                    return pin1.equals(port);
                } else {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());
        if ( comps.size() > 1 ) { // a block that is allowed multiple times in the configuration
            return comps
                .stream()
                .filter(confComp -> confComp.getUserDefinedPortName().equals(port))
                .findFirst()
                .orElseThrow(() -> new DbcException("No default block exists for " + confType + " on port " + port + '!'));
        } else { // a block that can only exist once in the configuration
            return comps.stream().findFirst().orElseThrow(() -> new DbcException("No default block exists for confType " + confType + '!'));
        }
    }
}
