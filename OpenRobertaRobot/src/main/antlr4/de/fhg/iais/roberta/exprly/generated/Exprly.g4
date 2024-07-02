grammar Exprly;

expression: expr EOF
         ;

expr     : NULL                                                      # NullConst
         | CONST                                                     # MathConst
         | VAR                                                       # VarName
         | literal                                                   # LiteralExp
         | connExpr                                                  # ConnExp
         | funCall                                                   # FuncExp
         | '(' expr ')'                                              # Parenthese
         | op=(ADD | SUB) expr                                       # UnaryN
         | op=NOT expr                                               # UnaryB
         | <assoc=right> expr op=POW  expr                           # BinaryN
         | <assoc=right> expr op=MOD expr                            # BinaryN
         | expr op=(MUL | DIV ) expr                                 # BinaryN
         | expr op=(ADD | SUB) expr                                  # BinaryN
         | expr op=AND expr                                          # BinaryB
         | expr op=OR expr                                           # BinaryB
         | expr op=EQUAL expr                                        # BinaryB
         | expr op=NEQUAL expr                                       # BinaryB
         | expr op=GET expr                                          # BinaryB
         | expr op=LET expr                                          # BinaryB
         | expr op=GEQ expr                                          # BinaryB
         | expr op=LEQ expr                                          # BinaryB
         | expr '?' expr ':' expr									 # IfElseOp
         | PRIMITIVETYPE VAR                                         # ParamsMethod
         | robotExpr                                                 # RobotExpression
         ;

robotExpr:      robotSensorExpr
         //|      robotActuatorExpr
         //|      robotSpecificExpr
         ;

robotSensorExpr: ROBOT '.' SENSOR_EXPR '(' (expr (',' expr)*)? ')' # RobotSensorExpression;
robotActuatorExpr: ROBOT '.' ACTUATOR_EXPR '(' (expr (',' expr)*)? ')' # RobotActuatorExpression;
robotSpecificExpr: ROBOT '.' SPECIFIC_EXPR '(' (expr (',' expr)*)? ')' # RobotSpecificExpression;



statementList: (stmt';')*
         ;

stmt:    FNAMESTMT '(' ( expr (',' expr)* )? ')'                                                                                         # StmtFunc
        | VAR op=SET  expr                                                                                                               # BinaryVarAssign
        | IF '(' expr ')' '{' statementList '}' (ELSEIF '(' expr ')' '{' statementList '}')* (op=ELSE '{' statementList '}')?            # ConditionStatementBlock
        | REPEATFOR '('PRIMITIVETYPE VAR '=' expr ';' VAR '<'expr ';' (expr op=STEP |VAR '=' VAR op=(ADD | SUB) expr |VAR '=' expr)? ')' '{' statementList '}'                                    # RepeatFor
        | WHILE'(' expr ')' '{' statementList '}'                                                                                        # RepeatStatement
        | REPEATFOREACH '(' PRIMITIVETYPE  VAR ':' expr ')' '{' statementList '}'                                                        # RepeatForEach
        | WAIT '(' expr ')' '{' statementList '}' (op=ORWAITFOR '(' expr ')' '{' statementList '}')*                                     # WaitStatement
        | op= (BREAK | CONTINUE )                                                                                                        # FlowControl
        | WAITMS'(' expr ')'                                                                                                             # WaitTimeStatement
        | PRIMITIVETYPE FUNCTIONNAME '(' (expr(',' expr)*)* ')' '{' statementList (op=RETURN (VAR|expr))? '}'                            # FuncUser
        | IF '(' expr ')' RETURN (VAR | expr)                                                                                            # UserFuncIfStmt
        ;

robotStmt:  robotSensorStmt
       //|      robotActuatorStmt
       //|      robotSpecificStmt
         ;

robotSensorStmt: ROBOT '.' SENSOR_STMT '(' (expr (',' expr)*)? ')' # RobotSensorStatement
                 ;


program     : declaration* mainBlock EOF ;

declaration : PRIMITIVETYPE VAR ('=' expr)? ';' # VariableDeclaration ;

mainBlock   : 'void' 'main' '(' ')' '{' statementList '}' # MainFunc ;


literal  : COLOR                                                     # Col
         | INT                                                       # IntConst
         | FLOAT                                                     # FloatConst
         | BOOL                                                      # BoolConstB
         | '"'  (.*?|'.'|'?')  '"'                                   # ConstStr
         | '[' (expr ',')* expr? ']'                                 # ListExpr
         ;

connExpr : 'connect' op0=(STR|VAR) ',' op1=(STR|VAR)                 # Conn
         ;

funCall  : FNAME '(' ( expr (',' expr)* )? ')'                      # Func
         | FUNCTIONNAME '(' ( expr (',' expr)* )? ')'               #UserDefCall
		 ;

// LEXER RULES
ROBOT: 'microbitV2' | 'wedo';


IF              :   'if';
ELSEIF          :   'else' [ \t]+ 'if';
ELSE            :   'else';
STEP            :   '++';

WHILE           :   'while';
REPEATFOR       :   'for';
REPEATFOREACH   :   'for' [ \t]+ 'each';
BREAK           :   'break';
CONTINUE        :   'continue';
WAIT            :   'waitUntil';
ORWAITFOR       :   'orWaitFor';
WAITMS          :   'wait ms';
RETURN          :   'return';

PRIMITIVETYPE   :   'Number'
                |    'Boolean'
                |    'String'
                |    'Color'
                |    'Connection'
                |    'Void'
                ;


NEWLINE : '\r'? '\n'  -> skip;

WS      : (' '|'\t')+ -> skip;

FNAME   : 'sin'
        | 'cos'
        | 'tan'
        | 'asin'
        | 'acos'
        | 'atan'
        | 'exp'
        | 'square'
        | 'sqrt'
        | 'abs'
        | 'log10'
        | 'log'
        | 'randomInt'
        | 'randomFloat'
        | 'randomItem'
        | 'floor'
        | 'ceil'
        | 'round'
        | 'isEven'
        | 'isOdd'
        | 'isPrime'
        | 'isWhole'
        | 'isEmpty'
        | 'isPositive'
        | 'isNegative'
        | 'isDivisibleBy'
        | 'sum'
        | 'max'
        | 'min'
        | 'average'
        | 'median'
        | 'stddev'
        | 'size'
        | 'indexOfFirst'
        | 'indexOfLast'
        | 'get'
        | 'getFromEnd'
        | 'getFirst'
        | 'getLast'
        | 'getAndRemove'
        | 'getAndRemoveFromEnd'
        | 'getAndRemoveFirst'
        | 'getAndRemoveLast'
        | 'createListWith'
        | 'subList'
        | 'subListFromIndexToLast'
        | 'subListFromIndexToEnd'
        | 'subListFromFirstToIndex'
        | 'subListFromFirstToLast'
        | 'subListFromFirstToEnd'
        | 'subListFromEndToIndex'
        | 'subListFromEndToEnd'
        | 'subListFromEndToLast'
        | 'print'
        | 'createTextWith'
        | 'constrain'
        | 'getRGB'
        ;

FNAMESTMT: 'showText'
        | 'set'
        | 'setFromEnd'
        | 'setFirst'
        | 'setLast'
        | 'insert'
        | 'insertFromEnd'
        | 'insertFirst'
        | 'insertLast'
        | 'remove'
        | 'removeFromEnd'
        | 'removeFirst'
        | 'removeLast'
        ;

CONST   : 'phi'
        | 'pi'
        | 'e'
        | 'sqrt2'
        | 'sqrt_1_2'
        | 'inf'
        ;

NULL    : 'null';

INT     : ('0'..'9')+;

FLOAT   : INT+ '.' INT*
        | '.' INT+
        ;

COLOR   : '#black'
        | '#blue'
        | '#green'
        | '#yellow'
        | '#red'
        | '#white'
        | '#brown'
        | '#none'
        | '#rgb(' HEX HEX HEX HEX HEX HEX ')'
        ;

BOOL    : 'true' | 'false';
HEX     : ('A'..'F'|'0'..'9');

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
MICROBITV2_SENSORSTMT : PIN_TOUCH_SENSOR
                      | TIMER_RESET
                      ;

VAR     : ('a'..'z')('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;
FUNCTIONNAME: ('A'..'Z')('a'..'z'|'A'..'Z'|'0'..'9'|'_')* ;
STR     : ('a'..'z'|'A'..'Z'|'0'..'9')+;
SET     :'=';
AND     : '&&';
OR      : '||';
NOT     : '!';
EQUAL   : '==';
NEQUAL  : '!=';
GET     : '>';
LET     : '<';
GEQ     : '>=';
LEQ     : '<=';
MOD     : '%';
POW     : '^';
MUL     : '*';
DIV     : '/';
ADD     : '+';
SUB     : '-';


//Generic sensors
ACCELEROMETER_SENSOR: 'accelerometerSensor';
COLOR_SENSOR: 'colorSensor';
COMPASS_CALIBRATE: 'compassCalibrate';
COMPASS_SENSOR: 'compassSensor';
DETECT_MARK_SENSOR: 'detectMarkSensor';
DROP_SENSOR: 'dropSensor';
EHT_COLOR_SENSOR: 'ehtColorSensor';
ENCODER_RESET: 'encoderReset';
ENCODER_SENSOR: 'encoderSensor';
ENVIRONMENTAL_SENSOR: 'environmentalSensor';
GESTURE_SENSOR: 'gestureSensor';
GET_LINE_SENSOR: 'getLineSensor';
GYRO_RESET: 'gyroReset';
GYRO_SENSOR: 'gyroSensor';
HT_COLOR_SENSOR: 'htColorSensor';
HUMIDITY_SENSOR: 'humiditySensor';
IR_SEEKER_SENSOR: 'irSeekerSensor';
INFRARED_SENSOR: 'infraredSensor';
KEYS_SENSOR: 'keysSensor';
LIGHT_SENSOR: 'lightSensor';
MOISTURE_SENSOR: 'moistureSensor';
MOTION_SENSOR: 'motionSensor';
PARTICLE_SENSOR: 'particleSensor';
PIN_GET_VALUE_SENSOR: 'pinGetValueSensor';
PIN_TOUCH_SENSOR: 'pinTouchSensor';
PULSE_SENSOR: 'pulseSensor';
RFID_SENSOR: 'rfidSensor';
SOUND_SENSOR: 'soundSensor';
TEMPERATURE_SENSOR: 'temperatureSensor';
TIMER_RESET: 'timerReset';
TIMER_SENSOR: 'timerSensor';
TOUCH_SENSOR: 'touchSensor';
ULTRASONIC_SENSOR: 'ultrasonicSensor';
VEML_LIGHT_SENSOR: 'vemlLightSensor';
VOLTAGE_SENSOR: 'voltageSensor';


//Specific Sensors
CAMERA_SENSOR: 'cameraSensor';
CAMERA_THRESHOLD: 'cameraThreshold';
CODE_PAD_SENSOR: 'codePadSensor';
COLOUR_BLOB: 'colourBlob';
DETECT_FACE_INFORMATION: 'detectFaceInformation';
DETECT_FACE_SENSOR: 'detectFaceSensor';
ELECTRIC_CURRENT_SENSOR: 'electricCurrentSensor';
FSR_SENSOR: 'fsrSensor';
GPS_SENSOR: 'gpsSensor';
GYRO_RESET_AXIS: 'gyroResetAxis';
JOYSTICK: 'joystick';
LOGO_TOUCH_SENSOR: 'logoTouchSensor';
MARKER_INFORMATION: 'markerInformation';
NAO_MARK_INFORMATION: 'naoMarkInformation';
ODOMETRY_SENSOR: 'odometrySensor';
ODOMETRY_SENSOR_RESET: 'odometrySensorReset';
OPTICAL_SENSOR: 'opticalSensor';
PIN_SET_TOUCH_MODE: 'pinSetTouchMode';
QUAD_RGB_SENSOR: 'quadRGBSensor';
RADIO_RSSI_SENSOR: 'radioRssiSensor';
RECOGNIZE_WORD: 'recognizeWord';
RESET_SENSOR: 'resetSensor';
SOUND_RECORD: 'soundRecord';
TAP_SENSOR: 'tapSensor';