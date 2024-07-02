grammar WeDoRobots;

import Exprly;

robotSensorExpr: 'wedo' '.' WEDO_SENSORSEXPR '(' (expr (',' expr)*)? ')' # WeDoSensorExpression;

robotSensorStmt: 'wedo' '.' WEDO_SENSORSTMT '(' (expr (',' expr)*)? ')' # WeDoSensorStatement;



WEDO_SENSORSEXPR: GYRO_SENSOR
                 | INFRARED_SENSOR
                 | KEYS_SENSOR
                 | TIMER_SENSOR
                 ;


WEDO_SENSORSTMT : TIMER_RESET
                ;

GYRO_SENSOR: 'gyroSensor';
INFRARED_SENSOR: 'infraredSensor';
KEYS_SENSOR: 'keysSensor';
TIMER_SENSOR: 'timerSensor';

TIMER_RESET: 'timerReset';