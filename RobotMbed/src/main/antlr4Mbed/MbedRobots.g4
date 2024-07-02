grammar MbedRobots;

import Exprly;

robotSensorExpr: 'microbitV2' '.' MICROBITV2_SENSORSEXPR '(' (expr (',' expr)*)? ')' # MicrobitV2SensorExpression;
//robotActuatorExpr: 'microbitV2' '.' MICROBITV2_ACTUATOREXPR '(' (expr (',' expr)*)? ')' # MicrobitV2ActuatorExpression;
//robotSpecificExpr: 'microbitV2' '.' MICROBITV2_SPECIFICEXPR '(' (expr (',' expr)*)? ')' # MicrobitV2SpecificExpression;

robotSensorStmt: ROBOT '.' MICROBITV2_SENSORSTMT '(' (expr (',' expr)*)? ')' # MicrobitV2SensorStatement;



MICROBITV2_SENSORSEXPR: ACCELEROMETER_SENSOR
                      | LOGO_TOUCH_SENSOR
                      | COMPASS_SENSOR
                      | PIN_SET_TOUCH_MODE
                      | GESTURE_SENSOR
                      | KEYS_SENSOR
                      | LIGHT_SENSOR
                      | PIN_GET_VALUE_SENSOR
                      | SOUND_SENSOR
                      | TEMPERATURE_SENSOR
                      | TIMER_SENSOR
                      ;
//MICROBITV2_ACTUATOREXPR:
                      //;
//MICROBITV2_SPECIFICEXPR:
                       //;
MICROBITV2_SENSORSTMT : PIN_TOUCH_SENSOR
                      | TIMER_RESET
                      ;
// Define sensor lexer rules
ACCELEROMETER_SENSOR: 'accelerometerSensor';
LOGO_TOUCH_SENSOR: 'logoTouchSensor';
COMPASS_SENSOR: 'compassSensor';
PIN_SET_TOUCH_MODE: 'pinSetTouchMode';
GESTURE_SENSOR: 'gestureSensor';
KEYS_SENSOR: 'keysSensor';
LIGHT_SENSOR: 'lightSensor';
PIN_GET_VALUE_SENSOR: 'pinGetValueSensor';
SOUND_SENSOR: 'soundSensor';
TEMPERATURE_SENSOR: 'temperatureSensor';
TIMER_SENSOR: 'timerSensor';

PIN_TOUCH_SENSOR: 'pinTouchSensor';
TIMER_RESET: 'timerReset';