package de.fhg.iais.roberta.worker;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.dbc.Assert;

public final class MbedTwo2ThreeTransformerHelper {

    private static final Map<Pair<String, String>, String> PROG_BLOCK_TO_CONF_BLOCKLY_NAME = new HashMap<>();
    private static final Map<Pair<String, String>, String> CALLIOPE_XML_NAME_TO_FRONTEND_NAME = new HashMap<>();
    private static final int OFFSET_X = 175;
    private static final int OFFSET_Y = 60;
    private static final int MAX_VERTICAL_BLOCKS = 8;

    static {
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("ACCELEROMETER_SENSING", "DEFAULT"), "robConf_accelerometer");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("ACCELEROMETER_SENSING", "VALUE"), "robConf_accelerometer");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("COMPASS_SENSING", "ANGLE"), "robConf_compass");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("COMPASS_SENSING", "DEFAULT"), "robConf_compass");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("FOURDIGITDISPLAY_SHOW_ACTION", ""), "robConf_fourdigitdisplay");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("FOURDIGITDISPLAY_CLEAR_ACTION", ""), "robConf_fourdigitdisplay");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("GYRO_SENSING", "ANGLE"), "robConf_gyro");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("HUMIDITY_SENSING", "HUMIDITY"), "robConf_humidity");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("HUMIDITY_SENSING", "TEMPERATURE"), "robConf_humidity");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("INFRARED_SENSING", "LINE"), "robConf_infrared");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("KEYS_SENSING", "PRESSED"), "robConf_key");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("LEDBAR_SET_ACTION", ""), "robConf_ledbar");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("LED_ON_ACTION", ""), "robConf_rgbled");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("LIGHT_ACTION", "ON"), "robConf_led");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("LIGHT_ACTION", "OFF"), "robConf_led");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("LIGHT_SENSING", "DEFAULT"), "robConf_light");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("LIGHT_SENSING", "LIGHT_VALUE"), "robConf_light");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("LIGHT_SENSING", "VALUE"), "robConf_light");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("LIGHT_STATUS_ACTION", "OFF"), "robConf_rgbled");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("MOTOR_ON_ACTION", ""), "robConf_motor");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("MOTOR_STOP_ACTION", ""), "robConf_motor");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("PIN_READ_VALUE", "ANALOG"), "robConf_analogout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("PIN_READ_VALUE", "DIGITAL"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("PIN_READ_VALUE", "PULSEHIGH"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("PIN_READ_VALUE", "PULSELOW"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("PIN_SET_PULL", "UP"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("PIN_SET_PULL", "DOWN"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("PIN_SET_PULL", "NONE"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("PIN_WRITE_VALUE", "ANALOG"), "robConf_analogin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("PIN_WRITE_VALUE", "DIGITAL"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("PLAY_NOTE_ACTION", ""), "robConf_buzzer");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("SERVO_SET_ACTION", ""), "robConf_servo");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("SOUND_SENSING", "SOUND"), "robConf_sound");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("TEMPERATURE_SENSING", "DEFAULT"), "robConf_temperature");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("TEMPERATURE_SENSING", "TEMPERATURE"), "robConf_temperature");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("TEMPERATURE_SENSING", "VALUE"), "robConf_temperature");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("TONE_ACTION", ""), "robConf_buzzer");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Pair.of("ULTRASONIC_SENSING", "DISTANCE"), "robConf_ultrasonic");

        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_analogin", "1"), "P1");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_analogin", "2"), "P2");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_analogin", "5"), "A1");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_analogout", "1"), "P1");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_analogout", "2"), "P2");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_analogout", "5"), "A1");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_digitalin", "0"), "P0");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_digitalin", "1"), "P1");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_digitalin", "2"), "P2");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_digitalin", "3"), "P3");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_digitalin", "4"), "A0");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_digitalin", "5"), "A1");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_digitalout", "0"), "P0");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_digitalout", "1"), "P1");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_digitalout", "2"), "P2");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_digitalout", "3"), "P3");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_digitalout", "4"), "A0");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_digitalout", "5"), "A1");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_fourdigitdisplay", "5"), "A1");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_humidity", "5"), "A1");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_infrared", "2"), "CalliBot_links");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_infrared", "1"), "CalliBot_rechts");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_led", "1"), "CalliBot_links");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_led", "2"), "CalliBot_rechts");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_led", "3"), "CalliBot_beide");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_ledbar", "5"), "A1");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_motor", "0"), "CalliBot_links");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_motor", "2"), "CalliBot_rechts");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_motor", "A"), "Port_A");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_motor", "B"), "Port_B");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_rgbled", "1"), "CalliBot_links_vorne");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_rgbled", "4"), "CalliBot_rechts_vorne");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_rgbled", "2"), "CalliBot_links_hinten");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_rgbled", "3"), "CalliBot_rechts_hinten");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_rgbled", "5"), "CalliBot_alle");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_servo", "1"), "P1");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_servo", "2"), "P2");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_servo", "5"), "A1");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_ultrasonic", "1"), "A1");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_ultrasonic", "2"), "CalliBot");
    }

    private final BlocklyDropdownFactory dropdownFactory;
    private final String robotName;

    private final int maxX;
    private final int minY;
    private final int nDefaultComps;

    private final Map<String, Integer> portNames = new HashMap<>();
    private final Map<Pair<String, String>, ConfigurationComponent> createdComps = new HashMap<>();

    MbedTwo2ThreeTransformerHelper(BlocklyDropdownFactory dropdownFactory, ConfigurationAst defaultConf) {
        this.dropdownFactory = dropdownFactory;
        this.robotName = defaultConf.getRobotType();

        int mX = Integer.MIN_VALUE;
        int mY = Integer.MAX_VALUE;
        for ( ConfigurationComponent confComp : defaultConf.getConfigurationComponentsValues() ) {
            mX = Math.max(mX, confComp.getX());
            mY = Math.min(mY, confComp.getY());
            // Register the default components for any of the ports
            String pin1 = confComp.getOptProperty("PIN1");
            if ( pin1 == null ) {
                this.createdComps.put(Pair.of(confComp.getProperty().getBlockType(), "default"), confComp);
            } else {
                this.createdComps.put(Pair.of(confComp.getProperty().getBlockType(), pin1), confComp);
            }
        }
        this.maxX = mX;
        this.minY = mY;
        this.nDefaultComps = defaultConf.getConfigurationComponentsValues().size();
    }

    /**
     * Generates a {@link ConfigurationComponent} based on the supplied program block information.
     * Generated components are cached in createdComponents and may be reused if multiple program blocks access the same configuration block.
     * The type of the configuration block is based on the type of the program block as well as the mode.
     *
     * @param progBlockType the type of the program block
     * @param mode the mode of the program block
     * @param port the port of the program block
     * @return a generated configuration component, may reuse an existing one
     */
    public ConfigurationComponent getComponent(String progBlockType, String mode, String port) {
        // Reuse an existing component if able
        String confBlocklyName = PROG_BLOCK_TO_CONF_BLOCKLY_NAME.get(Pair.of(progBlockType, mode));
        Assert.notNull(confBlocklyName, progBlockType + " with mode " + mode + " does not have an an associated configuration blockly name!");
        // First check whether the component exists in the default configuration
        ConfigurationComponent confComp = this.createdComps.get(Pair.of(confBlocklyName, "default"));
        // Otherwise check if it has already been generated
        confComp = (confComp == null) ? this.createdComps.get(Pair.of(confBlocklyName, port)) : confComp;
        if ( confComp == null ) { // Otherwise generate a new one
            String confType = this.dropdownFactory.getConfigurationComponentTypeByBlocklyName(confBlocklyName);
            String name = getName(port, confBlocklyName);
            BlocklyBlockProperties properties = BlocklyBlockProperties.make(confBlocklyName, name, false, false, false, true, true, true, false, false);

            // Calculate block position
            int xPos = this.maxX + (OFFSET_X * (((this.createdComps.size() - this.nDefaultComps) / MAX_VERTICAL_BLOCKS) + 1));
            int yPos = this.minY + (OFFSET_Y * (((this.createdComps.size() - this.nDefaultComps) + 1) % MAX_VERTICAL_BLOCKS));

            confComp = new ConfigurationComponent(confType, true, name, name, Collections.singletonMap("PIN1", port), properties, null, xPos, yPos);
            this.createdComps.put(Pair.of(confBlocklyName, port), confComp);
        }
        return confComp;
    }

    /**
     * Creates an appropriate name based on the port. By default returns the port name itself as the name.
     * Ports are tracked individually in portNames, multiple usages of a port return a name with a suffix.
     * Some robot specific name changes are applied for calliope and microbit respectively.
     *
     * @param port the port to generate a name for
     * @param confBlocklyName the associated configuration block name
     * @return a unique name generated from the port
     */
    private String getName(String port, String confBlocklyName) {
        String modifiedPort;
        // Old microbit programs can only have pins as additionally necessary configuration blocks
        if ( this.robotName.equals("microbit") ) {
            modifiedPort = 'P' + port;
        } else if ( this.robotName.equals("calliope") && CALLIOPE_XML_NAME_TO_FRONTEND_NAME.containsKey(Pair.of(confBlocklyName, port)) ) {
            modifiedPort = CALLIOPE_XML_NAME_TO_FRONTEND_NAME.get(Pair.of(confBlocklyName, port));
        } else {
            modifiedPort = port;
        }

        if ( this.portNames.containsKey(modifiedPort) ) {
            this.portNames.put(modifiedPort, this.portNames.get(modifiedPort) + 1);
        } else {
            this.portNames.put(modifiedPort, 1);
        }

        return (this.portNames.get(modifiedPort) == 1) ? modifiedPort : (modifiedPort + '_' + this.portNames.get(modifiedPort));
    }
}
