/**
 * @fileoverview Sensor blocks for all systems.
 * @requires Blockly.Blocks
 * @author Beate
 */

'use strict';

goog.provide('Blockly.Blocks.robSensorDefinitions');

goog.require('Blockly.Blocks');

// define sensors here as a property of sensors  ********************************************************************************

/*- minimal example:
 *
 * sensors.ultrasonic.ev3 = {
 *     title : 'ULTRASONIC',
 *     modes : [ {
 *         name : 'PRESENCE',
 *         type : 'Boolean',
 *     } ],
 * };
 *
 */

/*- all supported properties:
 *
 * title,
 * ports,
 * modes,
 *     name,
 *     type,
 *     value,
 *     unit,
 *     op,
 * standardPort
 */

var sensors = {};

sensors.accelerometer = {};
sensors.accelerometer.calliope = {
    title : 'ACCELEROMETER',
    modes : [ {
        name : 'VALUE',
        type : 'Number',
        unit : 'MILLIG',
        op : 'NUM_REV',
        value : 0
    } ],
    ports : [ [ 'x', 'X' ], [ 'y', 'Y' ], [ 'z', 'Z' ], [ 'STRENGTH', 'STRENGTH' ] ]
};
sensors.accelerometer.microbit = sensors.accelerometer.calliope;

sensors.accelerometer.mbot = {
    title : 'ACCELEROMETER',
    modes : [ {
        name : 'X',
        type : 'Number',
        unit : 'g'
    }, {
        name : 'Y',
        type : 'Number',
        unit : 'g'
    }, {
        name : 'Z',
        type : 'Number',
        unit : 'g'
    } ],
    ports : [ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]
};

sensors.accelerometer.nao = {
    title : 'ACCELEROMETER',
    modes : [ {
        name : 'VALUE',
        type : 'Number',
        unit : 'MILLIG',
        op : 'NUM_REV',
        value : 512
    } ],
    ports : [ [ 'x', 'X' ], [ 'y', 'Y' ], [ 'z', 'Z' ] ]
};

sensors.accelerometer.vorwerk = sensors.accelerometer.calliope;

sensors.battery = {};
sensors.battery.botnroll = {
    title : 'BATTERY',
    modes : [ {
        name : 'VALUE',
        type : 'Number',
        unit : 'VOLT'
    } ]
};

sensors.code = {};
sensors.code.bob3 = {
    title : 'CODE',
    modes : [ {
        name : 'VALUE',
        type : 'Number',
        value : '11'
    } ]
};

sensors.colour = {};
sensors.colour.botnroll = {
    title : 'COLOUR',
    ports : [ [ 'LEFT', '1' ], [ 'RIGHT', '2' ] ],
    modes : [ {
        name : 'COLOUR',
        type : 'Colour',
        value : '#b30006'
    }, {
        name : 'LIGHT',
        type : 'Number',
        unit : 'PERCENT'
    }, {
        name : 'RGB',
        type : 'Array_Number'
    } ]
};
sensors.colour.ev3 = {
    title : 'COLOUR',
    ports : [ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ],
    modes : [ {
        name : 'COLOUR',
        type : 'Colour',
        value : '#b30006'
    }, {
        name : 'LIGHT',
        type : 'Number',
        unit : 'PERCENT',
        value : 50
    }, {
        name : 'AMBIENTLIGHT',
        type : 'Number',
        unit : 'PERCENT',
        value : 50
    }, {
        name : 'RGB',
        type : 'Array_Number'
    } ],
    standardPort : '3'
};
sensors.colour.nxt = {
    title : 'COLOUR',
    ports : [ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ],
    modes : [ {
        name : 'COLOUR',
        type : 'Colour',
        value : '#b30006'
    }, {
        name : 'LIGHT',
        type : 'Number',
        unit : 'PERCENT',
        value : 50
    }, {
        name : 'AMBIENTLIGHT',
        type : 'Number',
        unit : 'PERCENT',
        value : 50
    }, ],
    standardPort : '3'
};

sensors.compass = {};
sensors.compass.botnroll = {
    title : 'COMPASS',
    modes : [ {
        name : 'ANGLE',
        type : 'Number',
        unit : 'DEGREE'
    } ]
};
sensors.compass.calliope = sensors.compass.botnroll;
sensors.compass.microbit = sensors.compass.botnroll;
sensors.compass.ev3 = {
    title : 'COMPASS',
    modes : [ {
        name : 'ANGLE',
        type : 'Number',
        unit : 'DEGREE'
    }, {
        name : 'COMPASS',
        type : 'Number',
        unit : 'DEGREE'
    } ],
    ports : [ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ],
    standardPort : '1'
};

sensors.detectface = {};
sensors.detectface.nao = {
    title : 'DETECTFACE',
    modes : [ {
        name : 'NAMEONE',
        type : 'String',
        value : 'Roberta'
    }, {
        name : 'NAMEALL',
        type : 'Array_String'
    } ]
};

sensors.drop = {};
sensors.drop.arduino = {
    title : 'DROP',
    modes : [ {
        name : 'VALUE',
        type : 'Number',
        unit : 'PERCENT'
    } ],
    ports : 'CONFIGURATION'
};

sensors.drop_off = {};
sensors.drop_off.vorwerk = {
    title : 'DROP_OFF',
    modes : [ {
        name : 'DISTANCE',
        type : 'Number',
        unit : 'CM'
    } ],
    ports : [ [ 'LEFT', 'LEFT' ], [ 'RIGHT', 'RIGHT' ] ]
};

sensors.detectmark = {};
sensors.detectmark.nao = {
    title : 'DETECTMARK',
    modes : [ {
        name : 'IDONE',
        type : 'Number',
        value : '84',
        op : 'NUM_EQ'
    }, {
        name : 'IDALL',
        type : 'Array_Number'
    } ]
};

sensors.encoder = {};
sensors.encoder.botnroll = {
    title : 'ENCODER',
    modes : [ {
        name : 'DEGREE',
        type : 'Number',
        unit : 'DEGREE'
    }, {
        name : 'ROTATION',
        type : 'Number',
        unit : ''
    }, {
        name : 'DISTANCE',
        type : 'Number',
        unit : 'CM'
    } ],
    ports : [ [ 'A', 'A' ], [ 'B', 'B' ], [ 'C', 'C' ], [ 'D', 'D' ] ]
};

sensors.encoder.ev3 = {
    title : 'ENCODER',
    modes : [ {
        name : 'DEGREE',
        type : 'Number',
        unit : 'DEGREE',
        op : 'NUM_REV',
        value : 180
    }, {
        name : 'ROTATION',
        type : 'Number',
        unit : '',
        op : 'NUM_REV',
        value : 2
    }, {
        name : 'DISTANCE',
        type : 'Number',
        unit : 'CM'
    } ],
    ports : [ [ 'A', 'A' ], [ 'B', 'B' ], [ 'C', 'C' ], [ 'D', 'D' ] ],
    standardPort : 'B'
};

sensors.encoder.nxt = {
    title : 'ENCODER',
    modes : [ {
        name : 'DEGREE',
        type : 'Number',
        unit : 'DEGREE',
        op : 'NUM_REV',
        value : 180
    }, {
        name : 'ROTATION',
        type : 'Number',
        unit : '',
        op : 'NUM_REV',
        value : 2
    }, {
        name : 'DISTANCE',
        type : 'Number',
        unit : 'CM'
    } ],
    ports : [ [ 'A', 'A' ], [ 'B', 'B' ], [ 'C', 'C' ] ],
    standardPort : 'B'
};

sensors.encoder.arduino = {
    title : 'ENCODER',
    modes : [ {
        name : 'ROTATION',
        type : 'Number',
        unit : '',
        op : 'NUM_REV',
        value : 2
    } ],
    ports : 'CONFIGURATION'
};

sensors.electriccurrent = {};
sensors.electriccurrent.nao = {
    title : 'ELECTRICCURRENT',
    modes : [ {
        name : 'VALUE',
        type : 'Number',
        unit : 'AMPERE'
    } ],
    ports : [
            {
                port : [ 'PORT_HEAD', 'HEAD' ],
                slots : [ [ 'SLOT_YAW', 'YAW' ], [ 'SLOT_PITCH', 'PITCH' ] ]
            },
            {
                port : [ 'PORT_SHOULDER', 'SHOULDER' ],
                slots : [ [ 'SLOT_LEFT_PITCH', 'LEFT_PITCH' ], [ 'SLOT_LEFT_ROLL', 'LEFT_ROLL' ], [ 'SLOT_RIGHT_PITCH', 'RIGHT_PITCH' ],
                        [ 'SLOT_RIGHT_ROLL', 'RIGHT_ROLL' ] ]
            },
            {
                port : [ 'PORT_ELBOW', 'ELBOW' ],
                slots : [ [ 'SLOT_LEFT_YAW', 'LEFT_YAW' ], [ 'SLOT_LEFT_ROLL', 'LEFT_ROLL' ], [ 'SLOT_RIGHT_YAW', 'RIGHT_YAW' ],
                        [ 'SLOT_RIGHT_ROLL', 'RIGHT_ROLL' ] ]
            },
            {
                port : [ 'PORT_WRIST', 'WRIST' ],
                slots : [ [ 'SLOT_LEFT_YAW', 'LEFT_YAW' ], [ 'SLOT_RIGHT_YAW', 'RIGHT_YAW' ] ]
            },
            {
                port : [ 'PORT_HAND', 'HAND' ],
                slots : [ [ 'LEFT', 'LEFT' ], [ 'RIGHT', 'RIGHT' ] ]
            },
            {
                port : [ 'PORT_HIP', 'HIP' ],
                slots : [ [ 'SLOT_LEFT_YAW_PITCH', 'LEFT_YAW_PITCH' ], [ 'SLOT_LEFT_ROLL', 'LEFT_ROLL' ], [ 'SLOT_LEFT_PITCH', 'LEFT_PITCH' ],
                        [ 'SLOT_RIGHT_YAW_PITCH', 'RIGHT_YAW_PITCH' ], [ 'SLOT_RIGHT_ROLL', 'RIGHT_ROLL' ], [ 'SLOT_RIGHT_PITCH', 'RIGHT_PITCH' ] ]
            },
            {
                port : [ 'PORT_KNEE', 'KNEE' ],
                slots : [ [ 'SLOT_LEFT_PITCH', 'LEFT_PITCH' ], [ 'SLOT_RIGHT_PITCH', 'RIGHT_PITCH' ] ]
            },
            {
                port : [ 'PORT_ANKLE', 'ANKLE' ],
                slots : [ [ 'SLOT_LEFT_PITCH', 'LEFT_PITCH' ], [ 'SLOT_LEFT_ROLL', 'LEFT_ROLL' ], [ 'SLOT_RIGHT_PITCH', 'RIGHT_PITCH' ],
                        [ 'SLOT_RIGHT_ROLL', 'RIGHT_ROLL' ] ]
            } ]
};

sensors.fsr = {};
sensors.fsr.nao = {
    title : 'FSR',
    modes : [ {
        name : 'VALUE',
        type : 'Number',
        unit : 'NEWTON',
        op : 'NUM_REV',
        value : 10
    } ],
    ports : [ [ 'LEFT', 'LEFT' ], [ 'RIGHT', 'RIGHT' ] ]
};

sensors.gesture = {};
sensors.gesture.calliope = {
    //    title : 'GESTURE',
    //    modes : [ {
    //        name : 'PRESSED',
    //        type : 'Boolean',
    //        question : true,
    //    } ],
    //    ports : [ [ 'SENSOR_GESTURE_UP', 'UP' ], [ 'SENSOR_GESTURE_DOWN', 'DOWN' ], [ 'SENSOR_GESTURE_FACE_UP', 'FACE_UP' ],
    //            [ 'SENSOR_GESTURE_FACE_DOWN', 'FACE_DOWN' ], [ 'SENSOR_GESTURE_SHAKE', 'SHAKE' ], [ 'SENSOR_GESTURE_FREEFALL', 'FREEFALL' ] ],
    //    standardPort : 'UP',
    title : 'GESTURE',
    modes : [ {
        name : 'UP',
        type : 'Boolean'
    }, {
        name : 'DOWN',
        type : 'Boolean'
    }, {
        name : 'FACE_DOWN',
        type : 'Boolean'
    }, {
        name : 'FACE_UP',
        type : 'Boolean'
    }, {
        name : 'SHAKE',
        type : 'Boolean'
    }, {
        name : 'FREEFALL',
        type : 'Boolean'
    } ]
};
sensors.gesture.microbit = sensors.gesture.calliope;

sensors.gyro = {};
sensors.gyro.calliope = {
    title : 'GYRO',
    modes : [ {
        name : 'ANGLE',
        type : 'Number',
        unit : 'DEGREE',
        op : 'NUM_REV',
        value : 90
    } ],
    ports : [ [ 'x', 'X' ], [ 'y', 'Y' ] ]
};
sensors.gyro.nao = sensors.gyro.calliope;

sensors.gyro.mbot = {
    title : 'GYRO',
    modes : [ {
        name : 'X',
        type : 'Number',
        unit : 'DEGREE'
    }, {
        name : 'Y',
        type : 'Number',
        unit : 'DEGREE'
    } ],
    ports : [ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]
};

sensors.gyro.ev3 = {
    title : 'GYRO',
    modes : [ {
        name : 'ANGLE',
        type : 'Number',
        unit : 'DEGREE',
        op : 'NUM_REV',
        value : 90
    }, {
        name : 'RATE',
        type : 'Number',
        unit : 'OMEGA',
        op : 'NUM_REV',
        value : 90
    } ],
    ports : [ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ],
    standardPort : '2'
};

sensors.gyro.wedo = {
    title : 'GYRO',
    modes : [ {
        name : 'TILTED',
        type : 'Boolean',
        question : true,
    } ],
    slots : [ [ 'SLOT_TILTED_UP', 'UP' ], [ 'SLOT_TILTED_DOWN', 'DOWN' ], [ 'SLOT_TILTED_BACK', 'BACK' ], [ 'SLOT_TILTED_FRONT', 'FRONT' ],
            [ 'SLOT_TILTED_NO', 'NO' ], [ 'SLOT_TILTED_ANY', 'ANY' ] ],
    ports : 'CONFIGURATION'
};

sensors.humidity = {};
sensors.humidity.arduino = {
    title : 'HUMIDITY',
    modes : [ {
        name : 'HUMIDITY',
        type : 'Number',
        unit : 'PERCENT'
    }, {
        name : 'TEMPERATURE',
        type : 'Number',
        unit : 'DEGREE'
    } ],
    ports : 'CONFIGURATION'
}

sensors.infrared = {};
sensors.infrared.botnroll = {
    title : 'INFRARED',
    ports : [ [ 'LEFT', '1' ], [ 'RIGHT', '2' ], [ 'BOTH', 'BOTH' ] ],
    modes : [ {
        name : 'OBSTACLE',
        type : 'Boolean'
    }, {
        name : 'PRESENCE',
        type : 'Boolean'
    } ]
};

sensors.infrared.arduino = {
    title : 'INFRARED',
    modes : [ {
        name : 'VALUE',
        type : 'Number'
    }, {
        name : 'PRESENCE',
        type : 'Boolean'
    } ],
    ports : 'CONFIGURATION'
};

sensors.infrared.bob3 = {
    title : 'INFRARED',
    modes : [ {
        name : 'AMBIENTLIGHT',
        type : 'Number',
        unit : 'PERCENT'
    }, {
        name : 'REFLEXION',
        type : 'Number',
        unit : 'PERCENT'
    } ]
};

sensors.infrared.mbot = {
    title : 'INFRARED',
    modes : [ {
        name : 'LINE',
        type : 'Boolean'
    } ],
    ports : [ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ],
    slots : [ [ 'SLOT_LEFT', '1' ], [ 'SLOT_RIGHT', '2' ] ],
    standardPort : '2'
};

sensors.infrared.ev3 = {
    title : 'INFRARED',
    ports : [ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ],
    modes : [ {
        name : 'DISTANCE',
        type : 'Number',
        unit : 'CM'
    }, {
        name : 'PRESENCE',
        type : 'Array_Number'
    } ]
};

sensors.infrared.wedo = {
    title : 'INFRARED',
    ports : 'CONFIGURATION',
    modes : [ {
        name : 'DISTANCE',
        type : 'Number',
        // unit : 'CM'
        value : 10
    } ]
};

sensors.irseeker = {};
sensors.irseeker.ev3 = {
    title : 'IRSEEKER',
    ports : [ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ],
    modes : [ {
        name : 'MODULATED',
        type : 'Number',
        unit : 'DEGREE'
    }, {
        name : 'UNMODULATED',
        type : 'Number',
        unit : 'DEGREE'
    } ]
}

sensors.key = {};
sensors.key.botnroll = {
    title : 'KEY',
    modes : [ {
        name : 'PRESSED',
        type : 'Boolean',
        question : true
    } ],
    ports : [ [ '1', 'LEFT' ], [ '2', 'ENTER' ], [ '3', 'RIGHT' ], [ 'SENSOR_KEY_ANY', 'ANY' ] ]
};

sensors.key.arduino = {
    title : 'KEY',
    modes : [ {
        name : 'PRESSED',
        type : 'Boolean',
        question : true
    } ],
    ports : 'CONFIGURATION'
};

sensors.key.mbot = {
    title : 'KEY',
    modes : [ {
        name : 'PRESSED',
        type : 'Boolean',
        question : true
    } ]
};

sensors.key.calliope = {
    title : 'KEY',
    modes : [ {
        name : 'PRESSED',
        type : 'Boolean',
        question : true
    } ],
    ports : [ [ 'A', 'button_a' ], [ 'B', 'button_b' ] ]
};

sensors.key.ev3 = {
    title : 'KEY',
    modes : [ {
        name : 'PRESSED',
        type : 'Boolean',
        question : true
    } ],
    ports : [ [ 'SENSOR_KEY_ENTER', 'ENTER' ], [ 'SENSOR_KEY_UP', 'UP' ], [ 'SENSOR_KEY_DOWN', 'DOWN' ], [ 'SENSOR_KEY_LEFT', 'LEFT' ],
            [ 'SENSOR_KEY_RIGHT', 'RIGHT' ], [ 'SENSOR_KEY_ESCAPE', 'ESCAPE' ], [ 'SENSOR_KEY_ANY', 'ANY' ] ]
};
sensors.key.nxt = {
    title : 'KEY',
    modes : [ {
        name : 'PRESSED',
        type : 'Boolean',
        question : true
    } ],
    ports : [ [ 'SENSOR_KEY_ENTER', 'ENTER' ], [ 'SENSOR_KEY_LEFT', 'LEFT' ], [ 'SENSOR_KEY_RIGHT', 'RIGHT' ] ]
};
sensors.key.microbit = sensors.key.calliope;
sensors.key.wedo = {
    title : 'KEY',
    modes : [ {
        name : 'PRESSED',
        type : 'Boolean',
        question : true
    } ],
    ports : 'CONFIGURATION'
};

sensors.light = {};
sensors.light.botnroll = {
    title : 'LIGHT',
    modes : [ {
        name : 'LIGHT',
        type : 'Number',
        unit : 'PERCENT'
    } ],
    ports : [ [ '0', '0' ], [ '1', '1' ], [ '2', '2' ], [ '3', '3' ], [ '4', '4' ], [ '5', '5' ], [ '6', '6' ], [ '7', '7' ] ]
};

sensors.light.arduino = {
    title : 'LIGHT',
    modes : [ {
        name : 'LIGHT',
        type : 'Number',
        unit : 'PERCENT'
    } ],
    ports : 'CONFIGURATION'
};

sensors.light.calliope = {
    title : 'LIGHT',
    modes : [ {
        name : 'VALUE',
        type : 'Number',
        unit : 'PERCENT'
    } ]
};

sensors.light.nxt = {
    title : 'LIGHT',
    modes : [ {
        name : 'LIGHT',
        type : 'Number',
        unit : 'PERCENT',
        value : 50
    }, {
        name : 'AMBIENTLIGHT',
        type : 'Number',
        unit : 'PERCENT',
        value : 50
    } ],
    ports : [ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ],
    standardPort : '3'
};

sensors.light.mbot = {
    title : 'LIGHT',
    modes : [ {
        name : 'LIGHT',
        type : 'Number',
        unit : 'PERCENT',
        value : 50
    } ],
    ports : [ [ 'Port internal', '6' ], [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]
};

sensors.moisture = {};
sensors.moisture.arduino = {
    title : 'MOISTURE',
    modes : [ {
        name : 'VALUE',
        type : 'Number',
        unit : 'PERCENT'
    } ],
    ports : 'CONFIGURATION'
};

sensors.motion = {};
sensors.motion.arduino = {
    title : 'MOTION',
    modes : [ {
        name : 'PRESENCE',
        type : 'Boolean'
    } ],
    ports : 'CONFIGURATION'
};

sensors.motion.mbot = {
    title : 'MOTION',
    modes : [ {
        name : 'PRESENCE',
        type : 'Boolean'
    } ],
    ports : [ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ],
    standardPort : '3'
}

sensors.out = {};
sensors.out.arduino = {
    title : 'OUT',
    modes : [ {
        name : 'ANALOG',
        type : 'Number',
        value : '100',
        op : 'NUM_REV'
    }, {
        name : 'DIGITAL',
        type : 'Number',
        value : '1',
        op : 'NUM_EQ'
    } ],
    ports : 'CONFIGURATION'
};

sensors.potentiometer = {};
sensors.potentiometer.arduino = {
    title : 'POTENTIOMETER',
    modes : [ {
        name : 'VALUE',
        type : 'Number',
        unit : 'VOLT'
    } ],
    ports : 'CONFIGURATION'
};

sensors.potentiometer.mbot = {
    title : 'POTENTIOMETER',
    modes : [ {
        name : 'VALUE',
        type : 'Number',
        unit : 'VOLT'
    } ],
    ports : [ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ],
    standardPort : '4'
};

sensors.pin = {};
sensors.pin.calliope = {
    title : 'PIN',
    modes : [
            {
                name : 'ANALOG',
                type : 'Number',
                ports : [ [ 'P1', '1' ], [ 'P2', '2' ], [ 'A1', '5' ], [ 'C04', 'C04' ], [ 'C05', 'C05' ], [ 'C06', 'C06' ], [ 'C16', 'C16' ], [ 'C17', 'C17' ] ]
            },
            {
                name : 'DIGITAL',
                type : 'Number',
                ports : [ [ 'P0', '0' ], [ 'P1', '1' ], [ 'P2', '2' ], [ 'P3', '3' ], [ 'A0', '4' ], [ 'A1', '5' ], [ 'C04', 'C04' ], [ 'C05', 'C05' ],
                        [ 'C06', 'C06' ], [ 'C07', 'C07' ], [ 'C08', 'C08' ], [ 'C09', 'C09' ], [ 'C10', 'C10' ], [ 'C11', 'C11' ], [ 'C12', 'C12' ],
                        [ 'C16', 'C16' ], [ 'C17', 'C17' ], [ 'C18', 'C18' ], [ 'C19', 'C19' ] ]
            },
            {
                name : 'PULSEHIGH',
                type : 'Number',
                ports : [ [ 'P0', '0' ], [ 'P1', '1' ], [ 'P2', '2' ], [ 'P3', '3' ], [ 'A0', '4' ], [ 'A1', '5' ], [ 'C04', 'C04' ], [ 'C05', 'C05' ],
                        [ 'C06', 'C06' ], [ 'C07', 'C07' ], [ 'C08', 'C08' ], [ 'C09', 'C09' ], [ 'C10', 'C10' ], [ 'C11', 'C11' ], [ 'C12', 'C12' ],
                        [ 'C16', 'C16' ], [ 'C17', 'C17' ], [ 'C18', 'C18' ], [ 'C19', 'C19' ] ]
            },
            {
                name : 'PULSELOW',
                type : 'Number',
                ports : [ [ 'P0', '0' ], [ 'P1', '1' ], [ 'P2', '2' ], [ 'P3', '3' ], [ 'A0', '4' ], [ 'A1', '5' ], [ 'C04', 'C04' ], [ 'C05', 'C05' ],
                        [ 'C06', 'C06' ], [ 'C07', 'C07' ], [ 'C08', 'C08' ], [ 'C09', 'C09' ], [ 'C10', 'C10' ], [ 'C11', 'C11' ], [ 'C12', 'C12' ],
                        [ 'C16', 'C16' ], [ 'C17', 'C17' ], [ 'C18', 'C18' ], [ 'C19', 'C19' ] ]
            } ]
};
sensors.pin.microbit = {
    title : 'PIN',
    ports : [ [ '0', '0' ], [ '1', '1' ], [ '2', '2' ] ],
    modes : [ {
        name : 'ANALOG',
        type : 'Number'
    }, {
        name : 'DIGITAL',
        type : 'Number'
    }, {
        name : 'PULSEHIGH',
        type : 'Number'
    }, {
        name : 'PULSELOW',
        type : 'Number'
    } ]
};

sensors.pintouch = {};
sensors.pintouch.bob3 = {
    title : 'PINTOUCH',
    ports : [ [ 'LEFT', '2' ], [ 'RIGHT', '1' ] ],
    slots : [ [ 'SENSOR_TOP', '1' ], [ 'CENTER', '2' ], [ 'SENSOR_BOTTOM', '3' ], [ 'SENSOR_ANY', '0' ] ],
    modes : [ {
        name : 'PRESSED',
        type : 'Boolean',
        question : true
    } ]
};

sensors.pintouch.calliope = {
    title : 'PINTOUCH',
    ports : [ [ ' 0', '0' ], [ ' 1', '1' ], [ ' 2', '2' ], [ ' 3', '3' ] ],
    modes : [ {
        name : 'PRESSED',
        type : 'Boolean',
        question : true
    } ],
    standardPort : '1'
};

sensors.pintouch.microbit = {
    title : 'PINTOUCH',
    ports : [ [ ' 0', '0' ], [ ' 1', '1' ], [ ' 2', '2' ] ],
    modes : [ {
        name : 'PRESSED',
        type : 'Boolean',
        question : true
    } ],
    standardPort : '1'
};

sensors.pulse = {};
sensors.pulse.arduino = {
    title : 'PULSE',
    modes : [ {
        name : 'VALUE',
        type : 'Number'
    } ],
    ports : 'CONFIGURATION'
};

sensors.rssi = {};
sensors.rssi.calliope = {
    title : 'RSSI',
    modes : [ {
        name : 'VALUE',
        type : 'Number'
    } ]
};

sensors.rfid = {};
sensors.rfid.arduino = {
    title : 'RFID',
    modes : [ {
        name : 'IDONE',
        type : 'String'
    }, {
        name : 'PRESENCE',
        type : 'Boolean'
    } ],
    ports : 'CONFIGURATION'
};

sensors.sound = {};
sensors.sound.calliope = {
    title : 'SOUND',
    modes : [ {
        name : 'SOUND',
        type : 'Number',
        unit : 'PERCENT',
        op : 'NUM_REV',
        value : 50
    } ]
}
sensors.sound.ev3 = {
    title : 'SOUND',
    modes : [ {
        name : 'SOUND',
        type : 'Number',
        unit : 'PERCENT',
        op : 'NUM_REV',
        value : 50
    } ],
    ports : [ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ],
    standardPort : '2'
};

sensors.sound.nxt = sensors.sound.ev3;
sensors.sound.mbot = sensors.sound.ev3;

sensors.temperature = {};
sensors.temperature.bob3 = {
    title : 'TEMPERATURE',
    modes : [ {
        name : 'VALUE',
        type : 'Number',
        unit : 'DEGREE',
        value : 20
    } ]
};

sensors.temperature.mbot = {
    title : 'TEMPERATURE',
    modes : [ {
        name : 'VALUE',
        type : 'Number',
        unit : 'DEGREE',
        value : 20
    } ],
    ports : [ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ],
    standardPort : '3'
};

sensors.temperature.arduino = {
    title : 'TEMPERATURE',
    modes : [ {
        name : 'VALUE',
        type : 'Number',
        unit : 'DEGREE',
        value : 20
    } ],
    ports : 'CONFIGURATION'
};

sensors.temperature.calliope = sensors.temperature.bob3
sensors.temperature.microbit = sensors.temperature.bob3

sensors.timer = {};
sensors.timer.botnroll = {
    title : 'TIMER',
    modes : [ {
        name : 'VALUE',
        type : 'Number',
        unit : 'MS',
        op : 'NUM_REV',
        value : 500
    } ],
    ports : [ [ ' 1', '1' ] ]
};
sensors.timer.bob3 = sensors.timer.botnroll;
sensors.timer.calliope = sensors.timer.botnroll;
sensors.timer.nxt = sensors.timer.botnroll;
sensors.timer.microbit = sensors.timer.botnroll;
sensors.timer.arduino = sensors.timer.botnroll;
sensors.timer.mbot = sensors.timer.arduino;
sensors.timer.wedo = sensors.timer.botnroll;
sensors.timer.ev3 = {
    title : 'TIMER',
    modes : [ {
        name : 'VALUE',
        type : 'Number',
        unit : 'MS',
        op : 'NUM_REV',
        value : 500
    } ],
    ports : [ [ ' 1', '1' ], [ ' 2', '2' ], [ ' 3', '3' ], [ ' 4', '4' ], [ ' 5', '5' ] ]
};

sensors.touch = {};
sensors.touch.ev3 = {
    title : 'TOUCH',
    ports : [ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ],
    modes : [ {
        name : 'PRESSED',
        type : 'Boolean',
        question : true
    } ],
    standardPort : '1'
};
sensors.touch.nxt = sensors.touch.ev3;
sensors.touch.mbot = sensors.touch.ev3;
sensors.touch.nao = {
    title : 'TOUCH',
    modes : [ {
        name : 'PRESSED',
        type : 'Boolean',
        question : true
    } ],
    ports : [ {
        port : [ 'PORT_HEAD', 'HEAD' ],
        slots : [ [ 'SLOT_FRONT', 'FRONT' ], [ 'SLOT_MIDDLE', 'MIDDLE' ], [ 'SLOT_REAR', 'REAR' ] ]
    }, {
        port : [ 'PORT_HAND', 'HAND' ],
        slots : [ [ 'LEFT', 'LEFT' ], [ 'RIGHT', 'RIGHT' ] ]
    }, {
        port : [ 'PORT_BUMPER', 'BUMPER' ],
        slots : [ [ 'LEFT', 'LEFT' ], [ 'RIGHT', 'RIGHT' ] ]
    } ]
};

sensors.touch.vorwerk = {
    title : 'TOUCH',
    modes : [ {
        name : 'PRESSED',
        type : 'Boolean',
        question : true
    } ],
    ports : [ [ 'LEFT', 'LEFT' ], [ 'RIGHT', 'RIGHT' ] ],
    slots : [ [ 'SLOT_FRONT', 'FRONT' ], [ 'SLOT_SIDE', 'SIDE' ] ]
};

sensors.ultrasonic = {};
sensors.ultrasonic.botnroll = {
    title : 'ULTRASONIC',
    ports : [ [ 'LEFT', '0' ], [ 'CENTER', '1' ], [ 'RIGHT', '2' ], [ 'SENSOR_SONAR', '3' ] ],
    modes : [ {
        name : 'DISTANCE',
        type : 'Number',
        unit : 'CM'
    } ]
};
sensors.ultrasonic.arduino = {
    title : 'ULTRASONIC',
    modes : [ {
        name : 'DISTANCE',
        type : 'Number',
        unit : 'CM'
    } ],
    ports : 'CONFIGURATION'
};
sensors.ultrasonic.ev3 = {
    title : 'ULTRASONIC',
    ports : [ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ],
    modes : [ {
        name : 'DISTANCE',
        type : 'Number',
        unit : 'CM'
    }, {
        name : 'PRESENCE',
        type : 'Boolean'
    } ],
    standardPort : '4'
};

sensors.ultrasonic.nxt = {
    title : 'ULTRASONIC',
    ports : [ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ],
    modes : [ {
        name : 'DISTANCE',
        type : 'Number',
        unit : 'CM'
    } ],
    standardPort : '4'
};
sensors.ultrasonic.mbot = {
    title : 'ULTRASONIC',
    ports : [ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ],
    modes : [ {
        name : 'DISTANCE',
        type : 'Number',
        unit : 'CM'
    } ],
    standardPort : '3'
};

sensors.ultrasonic.nao = {
    title : 'ULTRASONIC',
    modes : [ {
        name : 'DISTANCE',
        type : 'Number',
        unit : 'CM'
    } ]
};

sensors.ultrasonic.vorwerk = {
    title : 'ULTRASONIC',
    modes : [ {
        name : 'DISTANCE',
        type : 'Number',
        unit : 'CM'
    } ],
    ports : [ [ 'LEFT', 'LEFT_ULTRASONIC' ], [ 'CENTER', 'CENTER_ULTRASONIC' ], [ 'RIGHT', 'RIGHT_ULTRASONIC' ] ],
    slots : [ [ 'LEFT', 'LEFT' ], [ 'CENTER', 'CENTER' ], [ 'RIGHT', 'RIGHT' ] ]
};

sensors.wall = {};
sensors.wall.vorwerk = {
    title : 'WALL',
    modes : [ {
        name : 'DISTANCE',
        type : 'Number',
        unit : 'CM'
    } ]
};

sensors.flame = {};
sensors.flame.mbot = {
    title : 'FLAME',
    ports : [ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ],
    modes : [ {
        name : 'VALUE',
        type : 'Number',
    } ],
    standardPort : '4'
};

sensors.joystick = {};
sensors.joystick.mbot = {
    title : 'JOYSTICK',
    ports : [ [ 'Port 1', '1' ], [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ],
    modes : [ {
        name : 'X',
        type : 'Number'
    }, {
        name : 'Y',
        type : 'Number'
    } ],
    standardPort : '3'
};

var sensorsAll = [];
sensorsAll.botnroll = [ sensors.infrared.botnroll, sensors.light.botnroll, sensors.compass.botnroll, sensors.ultrasonic.botnroll, sensors.colour.botnroll,
        sensors.key.botnroll ];
sensorsAll.mbot = [ sensors.key.mbot, sensors.ultrasonic.mbot, sensors.infrared.mbot, sensors.light.mbot, sensors.timer.mbot ];
sensorsAll.ev3 = [ sensors.touch.ev3, sensors.ultrasonic.ev3, sensors.colour.ev3, sensors.infrared.ev3, sensors.encoder.ev3, sensors.key.ev3, sensors.gyro.ev3,
        sensors.timer.ev3, sensors.compass.ev3, sensors.irseeker.ev3 ];
sensorsAll.nxt = [ sensors.touch.nxt, sensors.sound.nxt, sensors.light.nxt, sensors.ultrasonic.nxt, sensors.encoder.nxt, sensors.key.nxt, sensors.colour.nxt,
        sensors.timer.nxt ];
sensorsAll.bob3 = [ sensors.pintouch.bob3, sensors.infrared.bob3, sensors.temperature.bob3, sensors.timer.bob3 ];
sensorsAll.calliope = [ sensors.key.calliope, sensors.pintouch.calliope, sensors.gesture.calliope, sensors.compass.calliope, sensors.sound.calliope,
        sensors.timer.calliope, sensors.temperature.calliope, sensors.light.calliope, sensors.pin.calliope, sensors.gyro.calliope,
        sensors.accelerometer.calliope ];
sensorsAll.microbit = [ sensors.key.microbit, sensors.pintouch.microbit, sensors.gesture.microbit, sensors.compass.microbit, sensors.timer.microbit,
        sensors.temperature.microbit, sensors.pin.microbit, sensors.accelerometer.microbit ];
sensorsAll.arduino = [ sensors.out.arduino, sensors.key.arduino, sensors.timer.arduino, sensors.temperature.arduino, sensors.ultrasonic.arduino,
        sensors.light.arduino, sensors.moisture.arduino, sensors.potentiometer.arduino, sensors.infrared.arduino, sensors.humidity.arduino,
        sensors.encoder.arduino, sensors.motion.arduino, sensors.pulse.arduino, sensors.drop.arduino, sensors.rfid.arduino ];
sensorsAll.nao = [ sensors.touch.nao, sensors.accelerometer.nao, sensors.gyro.nao, sensors.ultrasonic.nao, sensors.fsr.nao, sensors.electriccurrent.nao,
        sensors.detectface.nao, sensors.detectmark.nao ];
sensorsAll.vorwerk = [ sensors.touch.vorwerk, sensors.accelerometer.vorwerk, sensors.ultrasonic.vorwerk, sensors.wall.vorwerk, sensors.drop_off.vorwerk ];
sensorsAll.wedo = [ sensors.key.wedo, sensors.gyro.wedo, sensors.infrared.wedo, sensors.timer.wedo ];

function initSensors() {
    for ( var sensor in sensors) {
        if (sensors.hasOwnProperty(sensor)) {
            Blockly.Blocks['robSensors_' + sensor + '_getSample'] = {
                sensor : sensor,
                init : function() {
                    Blockly.Blocks['robSensors_generic'].init.call(this, sensors[this.sensor][this.workspace.device]);
                }
            };
        }
    }
};

initSensors();

Blockly.Blocks['robSensors_getSample'] = {
    init : function() {
        Blockly.Blocks['robSensors_generic_all'].init.call(this, sensorsAll[this.workspace.device]);
    }
};

// map other names to the available ones here ***********************************************************************************

Blockly.Blocks['robSensors_battery_voltage'] = Blockly.Blocks['robSensors_battery_getSample'];
Blockly.Blocks['robSensors_key_isPressed'] = Blockly.Blocks['robSensors_key_getSample'];
Blockly.Blocks['robSensors_touch_isPressed'] = Blockly.Blocks['robSensors_touch_getSample'];

Blockly.Blocks['robSensors_getSample_ardu'] = Blockly.Blocks['robSensors_getSample'];

Blockly.Blocks['bob3Sensors_ambientlight'] = Blockly.Blocks['robSensors_infrared_getSample'];
Blockly.Blocks['bob3Sensors_temperature_getSample'] = Blockly.Blocks['robSensors_temperature_getSample'];
Blockly.Blocks['bob3Sensors_getCode'] = Blockly.Blocks['robSensors_code_getSample'];
Blockly.Blocks['bob3Sensors_touch_getSample'] = Blockly.Blocks['robSensors_pintouch_getSample'];
Blockly.Blocks['bob3Sensors_getSample_bob3'] = Blockly.Blocks['robSensors_getSample'];

Blockly.Blocks['mbedSensors_getSample'] = Blockly.Blocks['robSensors_getSample'];
// Blockly.Blocks['mbedControls_wait_for'] = Blockly.Blocks['robControls_wait_for'];
Blockly.Blocks['mbedSensors_key_isPressed'] = Blockly.Blocks['robSensors_key_getSample'];
Blockly.Blocks['mbedSensors_pin_isTouched'] = Blockly.Blocks['robSensors_pintouch_getSample'];
Blockly.Blocks['mbedSensors_gesture_isActive'] = Blockly.Blocks['robSensors_gesture_getSample'];
Blockly.Blocks['mbedSensors_compass_getSample'] = Blockly.Blocks['robSensors_compass_getSample'];
Blockly.Blocks['mbedSensors_microphone_getSample'] = Blockly.Blocks['robSensors_sound_getSample'];
Blockly.Blocks['mbedSensors_timer_getSample'] = Blockly.Blocks['robSensors_timer_getSample'];
Blockly.Blocks['mbedSensors_temperature_getSample'] = Blockly.Blocks['robSensors_temperature_getSample'];
Blockly.Blocks['mbedSensors_getRssi'] = Blockly.Blocks['robSensors_rssi_getSample'];
Blockly.Blocks['mbedSensors_ambientLight_getSample'] = Blockly.Blocks['robSensors_light_getSample'];
Blockly.Blocks['mbedSensors_pin_getSample'] = Blockly.Blocks['robSensors_pin_getSample'];
Blockly.Blocks['mbedSensors_rotation_getSample'] = Blockly.Blocks['robSensors_gyro_getSample'];
Blockly.Blocks['mbedSensors_acceleration_getSample'] = Blockly.Blocks['robSensors_accelerometer_getSample'];
Blockly.Blocks['naoSensors_accelerometer'] = Blockly.Blocks['robSensors_accelerometer_getSample'];
