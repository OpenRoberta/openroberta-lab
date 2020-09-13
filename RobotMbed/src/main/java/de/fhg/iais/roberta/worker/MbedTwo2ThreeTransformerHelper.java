package de.fhg.iais.roberta.worker;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Triple;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.dbc.Assert;

public final class MbedTwo2ThreeTransformerHelper {

    private static final Map<Triple<String, String, String>, String> PROG_BLOCK_TO_CONF_BLOCKLY_NAME = new HashMap<>();
    private static final Map<Pair<String, String>, String> CALLIOPE_XML_NAME_TO_FRONTEND_NAME = new HashMap<>();
    private static final Map<Pair<String, String>, String> CALLIBOT = new HashMap<>();
    private static final int OFFSET_X = 175;
    private static final int OFFSET_Y = 60;
    private static final int MAX_VERTICAL_BLOCKS = 8;

    static {
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("ACCELEROMETER_SENSING", "DEFAULT", "X"), "robConf_accelerometer");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("ACCELEROMETER_SENSING", "DEFAULT", "Y"), "robConf_accelerometer");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("ACCELEROMETER_SENSING", "DEFAULT", "Z"), "robConf_accelerometer");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("ACCELEROMETER_SENSING", "DEFAULT", "STRENGTH"), "robConf_accelerometer");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("ACCELEROMETER_SENSING", "VALUE", "X"), "robConf_accelerometer");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("ACCELEROMETER_SENSING", "VALUE", "Y"), "robConf_accelerometer");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("ACCELEROMETER_SENSING", "VALUE", "Z"), "robConf_accelerometer");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("ACCELEROMETER_SENSING", "VALUE", "STRENGTH"), "robConf_accelerometer");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("COMPASS_SENSING", "ANGLE", "NO_PORT"), "robConf_compass");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("COMPASS_SENSING", "DEFAULT", ""), "robConf_compass");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("FOURDIGITDISPLAY_SHOW_ACTION", "", "5"), "robConf_fourdigitdisplay");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("FOURDIGITDISPLAY_CLEAR_ACTION", "", "5"), "robConf_fourdigitdisplay");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("GYRO_SENSING", "ANGLE", "X"), "robConf_gyro");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("GYRO_SENSING", "ANGLE", "Y"), "robConf_gyro");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("HUMIDITY_SENSING", "HUMIDITY", "5"), "robConf_humidity");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("HUMIDITY_SENSING", "TEMPERATURE", "5"), "robConf_humidity");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("INFRARED_SENSING", "LINE", "2"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("INFRARED_SENSING", "LINE", "1"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("KEYS_SENSING", "PRESSED", "A"), "robConf_key");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("KEYS_SENSING", "PRESSED", "B"), "robConf_key");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("LEDBAR_SET_ACTION", "", "5"), "robConf_ledbar");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("LED_ON_ACTION", "", "0"), "robConf_rgbled");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("LED_ON_ACTION", "", "1"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("LED_ON_ACTION", "", "2"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("LED_ON_ACTION", "", "3"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("LED_ON_ACTION", "", "4"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("LED_ON_ACTION", "", "5"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("LIGHT_ACTION", "ON", "1"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("LIGHT_ACTION", "ON", "2"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("LIGHT_ACTION", "ON", "3"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("LIGHT_ACTION", "OFF", "1"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("LIGHT_ACTION", "OFF", "2"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("LIGHT_ACTION", "OFF", "3"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("LIGHT_SENSING", "DEFAULT", ""), "robConf_light");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("LIGHT_SENSING", "LIGHT_VALUE", "NO_PORT"), "robConf_light");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("LIGHT_SENSING", "VALUE", "NO_PORT"), "robConf_light");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("LIGHT_STATUS_ACTION", "OFF", "0"), "robConf_rgbled");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("LIGHT_STATUS_ACTION", "OFF", "1"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("LIGHT_STATUS_ACTION", "OFF", "2"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("LIGHT_STATUS_ACTION", "OFF", "3"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("LIGHT_STATUS_ACTION", "OFF", "4"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("LIGHT_STATUS_ACTION", "OFF", "5"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("MOTOR_ON_ACTION", "", "A"), "robConf_motor");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("MOTOR_ON_ACTION", "", "B"), "robConf_motor");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("MOTOR_ON_ACTION", "", "AB"), "robConf_motor");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("MOTOR_ON_ACTION", "", "0"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("MOTOR_ON_ACTION", "", "2"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("MOTOR_ON_ACTION", "", "3"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("MOTOR_STOP_ACTION", "", "A"), "robConf_motor");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("MOTOR_STOP_ACTION", "", "B"), "robConf_motor");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("MOTOR_STOP_ACTION", "", "AB"), "robConf_motor");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("MOTOR_STOP_ACTION", "", "0"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("MOTOR_STOP_ACTION", "", "2"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("MOTOR_STOP_ACTION", "", "3"), "robConf_callibot");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "ANALOG", "0"), "robConf_analogout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "ANALOG", "1"), "robConf_analogout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "ANALOG", "2"), "robConf_analogout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "ANALOG", "3"), "robConf_analogout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "ANALOG", "4"), "robConf_analogout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "ANALOG", "5"), "robConf_analogout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "ANALOG", "10"), "robConf_analogout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "ANALOG", "C04"), "robConf_analogout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "ANALOG", "C05"), "robConf_analogout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "ANALOG", "C06"), "robConf_analogout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "ANALOG", "C16"), "robConf_analogout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "ANALOG", "C17"), "robConf_analogout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "0"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "1"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "2"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "3"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "4"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "5"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "6"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "7"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "8"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "9"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "10"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "11"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "12"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "13"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "14"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "15"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "16"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "19"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "20"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "C04"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "C05"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "C06"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "C07"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "C08"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "C09"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "C10"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "C11"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "C12"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "C16"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "C17"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "C18"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "DIGITAL", "C19"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "0"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "1"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "2"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "3"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "4"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "5"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "6"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "7"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "8"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "9"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "10"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "11"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "12"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "13"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "14"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "15"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "16"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "19"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "20"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "C04"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "C05"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "C06"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "C07"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "C08"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "C09"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "C10"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "C11"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "C12"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "C16"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "C17"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "C18"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSEHIGH", "C19"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "0"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "1"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "2"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "3"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "4"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "5"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "6"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "7"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "8"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "9"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "10"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "11"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "12"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "13"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "14"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "15"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "16"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "19"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "20"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "C04"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "C05"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "C06"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "C07"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "C08"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "C09"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "C10"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "C11"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "C12"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "C16"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "C17"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "C18"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_READ_VALUE", "PULSELOW", "C19"), "robConf_digitalout");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "0"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "1"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "2"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "3"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "4"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "5"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "6"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "7"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "8"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "9"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "10"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "11"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "12"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "13"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "14"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "15"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "16"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "19"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "20"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "C04"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "C05"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "C06"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "C07"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "C08"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "C09"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "C10"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "C11"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "C12"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "C16"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "C17"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "C18"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "UP", "C19"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "0"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "1"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "2"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "3"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "4"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "5"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "6"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "7"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "8"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "9"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "10"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "11"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "12"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "13"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "14"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "15"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "16"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "19"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "20"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "C04"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "C05"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "C06"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "C07"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "C08"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "C09"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "C10"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "C11"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "C12"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "C16"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "C17"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "C18"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "DOWN", "C19"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "0"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "1"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "2"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "3"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "4"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "5"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "6"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "7"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "8"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "9"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "10"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "11"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "12"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "13"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "14"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "15"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "16"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "19"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "20"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "C04"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "C05"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "C06"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "C07"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "C08"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "C09"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "C10"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "C11"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "C12"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "C16"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "C17"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "C18"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_SET_PULL", "NONE", "C19"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "ANALOG", "0"), "robConf_analogin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "ANALOG", "1"), "robConf_analogin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "ANALOG", "2"), "robConf_analogin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "ANALOG", "3"), "robConf_analogin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "ANALOG", "4"), "robConf_analogin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "ANALOG", "5"), "robConf_analogin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "ANALOG", "10"), "robConf_analogin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "ANALOG", "C04"), "robConf_analogin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "ANALOG", "C05"), "robConf_analogin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "ANALOG", "C06"), "robConf_analogin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "ANALOG", "C16"), "robConf_analogin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "ANALOG", "C17"), "robConf_analogin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "0"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "1"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "2"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "3"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "4"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "5"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "6"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "7"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "8"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "9"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "10"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "11"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "12"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "13"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "14"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "15"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "16"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "19"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "20"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "C04"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "C05"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "C06"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "C07"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "C08"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "C09"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "C10"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "C11"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "C12"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "C16"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "C17"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "C18"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PIN_WRITE_VALUE", "DIGITAL", "C19"), "robConf_digitalin");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("PLAY_NOTE_ACTION", "", "NO_PORT"), "robConf_buzzer");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("SERVO_SET_ACTION", "", "1"), "robConf_servo");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("SERVO_SET_ACTION", "", "2"), "robConf_servo");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("SERVO_SET_ACTION", "", "5"), "robConf_servo");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("SERVO_SET_ACTION", "", "C04"), "robConf_servo");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("SERVO_SET_ACTION", "", "C05"), "robConf_servo");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("SERVO_SET_ACTION", "", "C06"), "robConf_servo");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("SERVO_SET_ACTION", "", "C16"), "robConf_servo");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("SERVO_SET_ACTION", "", "C17"), "robConf_servo");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("SOUND_SENSING", "SOUND", "NO_PORT"), "robConf_sound");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("TEMPERATURE_SENSING", "DEFAULT", ""), "robConf_temperature");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("TEMPERATURE_SENSING", "TEMPERATURE", "NO_PORT"), "robConf_temperature");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("TEMPERATURE_SENSING", "VALUE", "NO_PORT"), "robConf_temperature");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("TONE_ACTION", "", "NO_PORT"), "robConf_buzzer");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("ULTRASONIC_SENSING", "DISTANCE", "1"), "robConf_ultrasonic");
        PROG_BLOCK_TO_CONF_BLOCKLY_NAME.put(Triple.of("ULTRASONIC_SENSING", "DISTANCE", "2"), "robConf_callibot");

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
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_ledbar", "5"), "A1");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_motor", "A"), "Port_A");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_motor", "B"), "Port_B");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_servo", "1"), "P1");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_servo", "2"), "P2");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_servo", "5"), "A1");
        CALLIOPE_XML_NAME_TO_FRONTEND_NAME.put(Pair.of("robConf_ultrasonic", "1"), "A1");

        CALLIBOT.put(Pair.of("INFRARED_SENSING", "2"), "I_CalliBot_links");
        CALLIBOT.put(Pair.of("INFRARED_SENSING", "1"), "I_CalliBot_rechts");
        CALLIBOT.put(Pair.of("LIGHT_ACTION", "1"), "L_CalliBot_links");
        CALLIBOT.put(Pair.of("LIGHT_ACTION", "2"), "L_CalliBot_rechts");
        CALLIBOT.put(Pair.of("LIGHT_ACTION", "3"), "L_CalliBot_beide");
        CALLIBOT.put(Pair.of("LED_ON_ACTION", "1"), "CalliBot_links_vorne");
        CALLIBOT.put(Pair.of("LED_ON_ACTION", "4"), "CalliBot_rechts_vorne");
        CALLIBOT.put(Pair.of("LED_ON_ACTION", "2"), "CalliBot_links_hinten");
        CALLIBOT.put(Pair.of("LED_ON_ACTION", "3"), "CalliBot_rechts_hinten");
        CALLIBOT.put(Pair.of("LED_ON_ACTION", "5"), "CalliBot_alle");
        CALLIBOT.put(Pair.of("LIGHT_STATUS_ACTION", "1"), "CalliBot_links_vorne");
        CALLIBOT.put(Pair.of("LIGHT_STATUS_ACTION", "4"), "CalliBot_rechts_vorne");
        CALLIBOT.put(Pair.of("LIGHT_STATUS_ACTION", "2"), "CalliBot_links_hinten");
        CALLIBOT.put(Pair.of("LIGHT_STATUS_ACTION", "3"), "CalliBot_rechts_hinten");
        CALLIBOT.put(Pair.of("LIGHT_STATUS_ACTION", "5"), "CalliBot_alle");
        CALLIBOT.put(Pair.of("MOTOR_ON_ACTION", "0"), "CalliBot_links");
        CALLIBOT.put(Pair.of("MOTOR_ON_ACTION", "2"), "CalliBot_rechts");
        CALLIBOT.put(Pair.of("MOTOR_STOP_ACTION", "0"), "CalliBot_links");
        CALLIBOT.put(Pair.of("MOTOR_STOP_ACTION", "2"), "CalliBot_rechts");
//        CALLIBOT.put(Pair.of("MOTOR_STOP_ACTION", "3"), "robConf_callibot");
        CALLIBOT.put(Pair.of("ULTRASONIC_SENSING", "2"), "CalliBot_vorne");
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
    public Pair<ConfigurationComponent, String> getComponentAndName(String progBlockType, String mode, String port) {
        // Reuse an existing component if able
        String confBlocklyName = PROG_BLOCK_TO_CONF_BLOCKLY_NAME.get(Triple.of(progBlockType, mode, port));
        Assert.notNull(confBlocklyName, progBlockType + " with mode " + mode + " and port " + port + " does not have an an associated configuration blockly name!");
        // First check whether the component exists in the default configuration
        ConfigurationComponent confComp = this.createdComps.get(Pair.of(confBlocklyName, "default"));
        // Otherwise check if it has already been generated
        confComp = (confComp == null) ? this.createdComps.get(Pair.of(confBlocklyName, port)) : confComp;

        if ( confComp == null ) { // Otherwise generate a new one
            confComp = createComponent(confBlocklyName, port);
            if (confComp.getComponentType().equals("CALLIBOT")) {
                // Register Callibot as a default component, as it should be found regardless of the port
                this.createdComps.put(Pair.of(confBlocklyName, "default"), confComp);
            } else {
                this.createdComps.put(Pair.of(confBlocklyName, port), confComp);
            }
        }
        String name;
        if (confComp.getComponentType().equals("CALLIBOT")) {
            name = CALLIBOT.get(Pair.of(progBlockType, port));
        } else {
            name = confComp.getUserDefinedPortName();
        }
        return Pair.of(confComp, name);
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
        // Old microbit programs can only have pins and the buzzer as additionally necessary configuration blocks
        if ( this.robotName.equals("microbit") ) {
            if ( confBlocklyName.equals("robConf_buzzer") ) {
                modifiedPort = "BZ";
            } else {
                modifiedPort = 'P' + port;
            }
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

    private ConfigurationComponent createComponent(String confBlocklyName, String port) {
        // Calculate block position
        int xPos = this.maxX + (OFFSET_X * (((this.createdComps.size() - this.nDefaultComps) / MAX_VERTICAL_BLOCKS) + 1));
        int yPos = this.minY + (OFFSET_Y * (((this.createdComps.size() - this.nDefaultComps)) % MAX_VERTICAL_BLOCKS));

        String confType = this.dropdownFactory.getConfigurationComponentTypeByBlocklyName(confBlocklyName);
        String name;
        Map<String, String> properties = new HashMap<>();
        if (confType.equals("CALLIBOT")) {
            name = "CalliBot";
            properties.put("MOTOR_L", "CalliBot_links");
            properties.put("MOTOR_R", "CalliBot_rechts");
            properties.put("RGBLED_LF", "CalliBot_links_vorne");
            properties.put("RGBLED_RF", "CalliBot_rechts_vorne");
            properties.put("RGBLED_LR", "CalliBot_links_hinten");
            properties.put("RGBLED_RR", "CalliBot_rechts_hinten");
            properties.put("RGBLED_A", "CalliBot_alle");
            properties.put("LED_L", "L_CalliBot_links");
            properties.put("LED_R", "L_CalliBot_rechts");
            properties.put("LED_B", "L_CalliBot_beide");
            properties.put("INFRARED_L", "I_CalliBot_links");
            properties.put("INFRARED_R", "I_CalliBot_rechts");
            properties.put("ULTRASONIC", "CalliBot_vorne");
        } else {
            name = getName(port, confBlocklyName);
            properties = Collections.singletonMap("PIN1", port);
        }
        BlocklyBlockProperties blocklyProperties = BlocklyBlockProperties.make(confBlocklyName, name, false, false, false, true, true, true, false, false);
        return new ConfigurationComponent(confType, true, name, name, properties, blocklyProperties, null, xPos, yPos);
    }
}
