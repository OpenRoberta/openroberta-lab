grammar TextlyJava;

// Rules for complete program textual representation

program                 : declaration* mainBlock userFunc* EOF ;
declaration             : nameDecl '=' expr ';'                                                                                        # VariableDeclaration;
mainBlock               : VOID 'main' '(' ')' '{' statementList '}'                                                                    # MainFunc;
userFunc                : (PRIMITIVETYPE|VOID) NAME '(' (nameDecl (',' nameDecl)*)* ')' '{' statementList (op=RETURN expr';')? '}'     # FuncUser;
nameDecl                : PRIMITIVETYPE NAME                                                                                           # ParamsMethod;

// Statements block rules

statementList           : (stmt|commentStatement)*
                        ;

stmt                    : FNAMESTMT '(' ( expr (',' expr)* )? ')' ';'                                                                                                                      # StmtFunc
                        | NAME '(' ( expr* (',' expr)* )? ')' ';'                                                                                                                          # StmtUsedDefCall
                        | NAME op = SET  expr ';'                                                                                                                                          # BinaryVarAssign
                        | IF '(' expr ')' '{' statementList '}' ( ELSEIF '(' expr ')' '{' statementList '}' )* (op = ELSE '{' statementList '}')? (';')?                                   # ConditionStatementBlock
                        | REPEATFOR '('nameDecl '=' expr ';' NAME '<' expr ';' (expr op = STEP |NAME '=' NAME op = (ADD | SUB) expr |NAME '=' expr)? ')' '{' statementList '}' (';')?      # RepeatFor
                        | WHILE'(' expr ')' '{' statementList '}' (';')?                                                                                                                   # RepeatStatement
                        | REPEATFOREACH '(' nameDecl ':' expr ')' '{' statementList '}' (';')?                                                                                             # RepeatForEach
                        | WAIT '(' expr ')' '{' statementList '}' (op = ORWAITFOR '(' expr ')' '{' statementList '}' )*  (';')?                                                            # WaitStatement
                        | op = ( BREAK | CONTINUE ) ';'                                                                                                                                    # FlowControl
                        | WAITMS '(' expr ')' ';'                                                                                                                                          # WaitTimeStatement
                        | IF '(' expr ')' RETURN (NAME | expr)  ';'                                                                                                                        # UserFuncIfStmt
                        | robotStmt ';'                                                                                                                                                    # RobotStatement
                        ;
commentStatement        : SL_COMMENT                                                                                                                                                       # CommentLine;
robotStmt               : robotMicrobitv2Stmt
                        | robotWeDoStmt
                        | robotEv3Stmt
                        ;

// Expressions rules

expression   : expr EOF
             ;

expr         : NULL                                                      # NullConst
             | CONST                                                     # MathConst
             | NAME                                                      # VarName
             | literal                                                   # LiteralExp
             | connExpr                                                  # ConnExp
             | funCall                                                   # FuncExp
             | '(' expr ')'                                              # Parenthese
             | op=(ADD | SUB) expr                                       # UnaryN
             | op=NOT expr                                               # UnaryB
             | <assoc=right> expr op=POW  expr                           # BinaryN
             | <assoc=right> expr op=MOD expr                            # BinaryN
             | expr op = (MUL | DIV ) expr                               # BinaryN
             | expr op = (ADD | SUB) expr                                # BinaryN
             | expr op = AND expr                                        # BinaryB
             | expr op = OR expr                                         # BinaryB
             | expr op = EQUAL expr                                      # BinaryB
             | expr op = NEQUAL expr                                     # BinaryB
             | expr op = GET expr                                        # BinaryB
             | expr op = LET expr                                        # BinaryB
             | expr op = GEQ expr                                        # BinaryB
             | expr op = LEQ expr                                        # BinaryB
             | expr '?' expr ':' expr                                    # IfElseOp
             | imageExpr                                                 # ImageExpression                                        
             | robotExpr                                                 # RobotExpression
             ;

imageExpr    : 'image' '(' NAME ')'                                      # PredefinedImage
             | 'image' '.' 'define' '(' USER_DEFINED_IMAGE ')'           # UserDefinedImage
             | 'image' '.' 'shift' '(' NAME ',' expr ',' expr ')'        # ImageShift
             | 'image' '.' 'invert' (expr)                               # ImageInvert
             ;

literal      : COLOR                                                     # Col
             | INT                                                       # IntConst
             | FLOAT                                                     # FloatConst
             | BOOL                                                      # BoolConstB
             | '"'  (.*?|'.'|'?')  '"'                                   # ConstStr
             | '[' (expr ',')* expr? ']'                                 # ListExpr
             ;

connExpr     : 'connect' op0 = (STR|NAME) ',' op1 = (STR|NAME)           # Conn
             ;

funCall      : FNAME '(' ( (PRIMITIVETYPE)? | (expr (',' expr)* )?) ')'  # Func
             | NAME '(' ( expr (',' expr)* )? ')'                        # UserDefCall
             ;


robotExpr    : robotMicrobitv2Expr
             | robotWeDoExpr
             | robotEv3Expr
             ;

// Specific Expressions and Statement for MicrobitV2, the AST element will be generated by RobotsMbedTextlyJavaVisitor

robotMicrobitv2Expr     : 'microbitv2' '.' microbitv2SensorExpr          # RobotMicrobitv2Expression;
microbitv2SensorExpr    : ACCELEROMETER_SENSOR '(' NAME ')'
                        | LOGO_TOUCH_SENSOR '.' 'isPressed' '(' ')'
                        | COMPASS_SENSOR '.' 'getAngle' '(' ')'
                        | GESTURE_SENSOR '.' 'currentGesture' '(' NAME ')'
                        | KEYS_SENSOR '.' 'isPressed' '(' NAME ')'
                        | LIGHT_SENSOR '.' 'getLevel' '(' ')'
                        | PIN_GET_VALUE_SENSOR  '(' NAME ',' op = ('analog'|'digital'|'pulseHigh'|'pulseLow') ')'
                        | PIN_TOUCH_SENSOR '.' 'isPressed' '(' INT ')'
                        | SOUND_SENSOR '.' 'microphone' '.' 'soundLevel' '(' ')'
                        | TEMPERATURE_SENSOR '(' ')'
                        | TIMER_SENSOR '(' ')'
                        | GET_LED_BRIGTHNESS '('expr ',' expr ')'
                        | RECEIVEMESSAGE '(' PRIMITIVETYPE ')'
                        ;
robotMicrobitv2Stmt     : 'microbitv2' '.' microbitv2SensorStmt           # RobotMicrobitv2SensorStatement
                        | 'microbitv2' '.' microbitv2ActuatorStmt         # RobotMicrobitv2ActuatorStatement
                        ;
microbitv2SensorStmt    : PIN_SET_TOUCH_MODE '(' INT ',' op = ('capacitive'|'resistive') ')'
                        | TIMER_RESET '(' ')'
                        ;
microbitv2ActuatorStmt  : SHOWTEXT '(' expr ')'
                        | SHOWIMAGE '(' expr ')'
                        | SHOWANIMATION '(' expr ')'
                        | CLEARDISPLAY '(' ')'
                        | SETLED '('expr ',' expr ',' expr ')'
                        | SHOWONSERIALMOTOR '(' expr ')'
                        | PITCH '(' expr ',' expr ')'
                        | PLAYNOTE '(' NAME ',' NAME ')'
                        | PLAY '(' NAME ')'
                        | PLAYSOUND '(' NAME ')'
                        | SETVOLUME '(' expr ')'
                        | SPEAKER '(' NAME ')'
                        | WRITEVALUE '(' op = ('digital'|'analog') ',' NAME ',' expr ')'
                        | SWITCHLED '(' NAME ')'
                        | SHOWCHARACTER '(' expr ')'
                        | RADIOSEND '(' PRIMITIVETYPE ',' expr ','expr ')'
                        | RADIOSET '(' expr ')'
                        ;

// Specific Expressions and Statements for WeDo,  the AST element will be generated by WedoTextlyJavaVisitor

robotWeDoExpr           : 'wedo' '.' weDoSensorExpr                       # RobotWeDoExpression;
weDoSensorExpr          : GYRO_SENSOR '.' 'isTilted' '(' NAME ',' NAME ')'
                        | INFRARED_SENSOR '(' NAME ')'
                        | KEYS_SENSOR '.' 'isPressed' '(' NAME ')'
                        | TIMER_SENSOR '(' ')'
                        ;

robotWeDoStmt           : 'wedo' '.' wedoSensorStmt                      # RobotWeDoSensorStatement
                        | 'wedo' '.' wedoActuatorStmt                    # RobotWedoActuatorStatement
                        ;
wedoSensorStmt          : TIMER_RESET '(' ')' ;

wedoActuatorStmt        : SHOWTEXT '(' expr ')'
                        | MOTORMOVE '('NAME ',' expr (',' expr)? ')'
                        | MOTORSTOP '(' NAME ')'
                        | CLEARDISPLAY '(' ')'
                        | PITCH '(' NAME ',' expr ',' expr ')'
                        | TURNRGBON '(' NAME ',' expr ')'
                        | TURNRGBOFF '(' NAME ')'
                        ;
// Specific Expressions and Statements for Ev3,  the AST element will be generated by Ev3TextlyJavaVisitor
robotEv3Expr            : 'ev3' '.' ev3SensorExpr                          # RobotEv3Expression;
ev3SensorExpr           : GETSPEEDMOTOR '(' NAME ')'
                        | GETVOLUME '(' ')'
                        | TOUCH_SENSOR '.' 'isPressed' '(' INT ')'
                        | ULTRASONIC_SENSOR '.' 'getDistance' '(' INT ')'
                        | ULTRASONIC_SENSOR '.' 'getPresence' '(' INT ')'
                        | COLOR_SENSOR '(' NAME ',' INT  ')'
                        | INFRARED_SENSOR '.' 'getDistance' '(' INT ')'
                        | INFRARED_SENSOR '.' 'getPresence' '(' INT ')'
                        | ENCODER_SENSOR '.' 'getDegree' '(' NAME ')'
                        | ENCODER_SENSOR '.' 'getRotation' '(' NAME ')'
                        | ENCODER_SENSOR '.' 'getDistance' '(' NAME ')'
                        | KEYS_SENSOR '.' 'isPressed' '(' NAME ')'
                        | GYRO_SENSOR '.' 'getAngle' '(' INT ')'
                        | GYRO_SENSOR '.' 'getRate' '(' INT ')'
                        | TIMER_SENSOR '(' INT ')'
                        | SOUND_SENSOR '.' 'getSoundLevel' '(' INT ')'
                        | HT_COLOR_SENSOR '(' NAME ',' INT  ')'
                        | HT_INFRARED_SENSOR '.' 'getModulated' '(' INT ')'
                        | HT_INFRARED_SENSOR '.' 'getUnmodulated' '(' INT ')'
                        | HT_COMPASS_SENSOR '.' 'getAngle' '(' INT ')'
                        | HT_COMPASS_SENSOR '.' 'getCompass' '(' INT ')'
                        | CONNECT_TO_ROBOT '(' expr ')'
                        | RECEIVEMESSAGE '(' expr ')'
                        | WAIT_FOR_CONNECTION '(' ')'
                        | GETOUTPUTNEURON '(' NAME ')'
                        | GETWEIGHT '(' NAME ',' NAME  ')'
                        | GETBIAS '(' NAME ')'
                        ;

robotEv3Stmt            : 'ev3' '.' ev3SensorStmt                         # RobotEv3SensorStatement
                        | 'ev3' '.' ev3ActuatorStmt                       # RobotEv3ActuatorStatement
                        | 'ev3' '.' ev3xNN                                # RobotEv3NeuralNetworks
                        ;
ev3SensorStmt           : TIMER_RESET '(' INT ')'
                        | ENCODER_RESET '(' NAME ')'
                        | GYRO_RESET '(' INT ')'
                        | HT_CCOMPASSSTARTCALIBRATION '(' INT ')'
                        ;

ev3ActuatorStmt         : TURNONMOTOR '(' NAME ',' expr ')'
                        | ROTATEMOTOR '(' NAME ',' expr ',' NAME ',' expr ')'
                        | SETMOTORSPEED '(' NAME ',' expr ')'
                        | STOPMOTOR '(' NAME ',' NAME ')'
                        | DRIVEDISTANCE '(' NAME ',' expr (',' expr)? ')'
                        | STOPDRIVE '(' ')'
                        | ROTATEDIRECTIONANGLE '(' NAME ',' expr ',' expr ')'
                        | ROTATEDIRECTIONREGULATED '('NAME ',' expr ')'
                        | DRIVEINCURVE '('NAME ',' expr ',' expr (',' expr)? ')'
                        | SETLANGUAGE '(' NAME ')'
                        | DRAWTEXT '(' expr ',' expr ',' expr ')'
                        | DRAWPICTURE '(' NAME ')'
                        | CLEARDISPLAY '(' ')'
                        | PLAYTONE '(' expr ',' expr ')'
                        | PLAY '(' INT ')'
                        | SETVOLUME '(' expr ')'
                        | SAYTEXT '(' expr (',' expr ',' expr)? ')'
                        | LEDON '(' NAME ',' NAME ')'
                        | LEDOFF '(' ')'
                        | RESETLED '(' ')'
                        | SENDMESSAGE '(' expr ',' expr ')'
                        ;

ev3xNN                  : NNSTEP '(' ')'
                        | SETINPUTNEURON '(' NAME ',' expr ')'
                        | SETWEIGHT '(' NAME ',' NAME ',' expr ')'
                        | SETBIAS '(' NAME ',' expr ')'
                        ;
// LEXER RULES

IF              : 'if';
ELSEIF          : 'else' [ \t]+ 'if';
ELSE            : 'else';
STEP            : '++';

WHILE           : 'while';
REPEATFOR       : 'for';
REPEATFOREACH   : 'for' [ \t]+ 'each';
BREAK           : 'break';
CONTINUE        : 'continue';
WAIT            : 'waitUntil';
ORWAITFOR       : 'orWaitFor';
WAITMS          : 'wait ms';
RETURN          : 'return';

PRIMITIVETYPE   : 'Number'
                | 'Boolean'
                | 'String'
                | 'Colour'
                | 'Connection'
                | 'Image'
                | 'List<Number>'
                | 'List<Boolean>'
                | 'List<Colour>'
                | 'List<Connection>'
                | 'List<String>'
                | 'List<Image>'
                ;
VOID            : 'void';
NEWLINE         :   '\r'? '\n'  -> skip;

SL_COMMENT      : '//' .*? '\n';
WS              :   (' '|'\t')+ -> skip;


FNAME           : 'sin'
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
                | 'pow10'
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
                | 'createEmptyList'
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
                | 'castToString'
                | 'castToChar'
                | 'castToNumber'
                | 'castStringToNumber'
                ;

FNAMESTMT       : 'set'
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
                | 'changeBy'
                | 'appendText'
                ;

CONST           : 'phi'
                | 'pi'
                | 'e'
                | 'sqrt2'
                | 'sqrt_1_2'
                | 'inf'
                ;

NULL            : 'null';

INT             : ('0'..'9')+;

FLOAT           : INT+ '.' INT*
                | '.' INT+
                ;

COLOR           : '#black'
                | '#blue'
                | '#green'
                | '#yellow'
                | '#red'
                | '#white'
                | '#brown'
                | '#lightgreen'
                | '#cyan'
                | '#purple'
                | '#pink'
                | '#orange'
                | '#none'
                | '#gray'
                | '#rgb(' HEX HEX HEX HEX HEX HEX ')'
                ;

BOOL            : 'true' | 'false';

USER_DEFINED_IMAGE: USER_IMAGE_ROW
                    USER_IMAGE_ROW
                    USER_IMAGE_ROW
                    USER_IMAGE_ROW
                    USER_IMAGE_ROW
                  ;
USER_IMAGE_ROW    : '[' IMAGE_ELEMENT ',' IMAGE_ELEMENT ',' IMAGE_ELEMENT ',' IMAGE_ELEMENT ',' IMAGE_ELEMENT ']'
                  ;
IMAGE_ELEMENT     : '#' | '0'
                  ;
//Generic sensors
ACCELEROMETER_SENSOR        : 'accelerometerSensor';
COLOR_SENSOR                : 'colorSensor';
COMPASS_CALIBRATE           : 'compassCalibrate';
COMPASS_SENSOR              : 'compassSensor';
DETECT_MARK_SENSOR          : 'detectMarkSensor';
DROP_SENSOR                 : 'dropSensor';
EHT_COLOR_SENSOR            : 'ehtColorSensor';
ENCODER_RESET               : 'encoderReset';
ENCODER_SENSOR              : 'encoderSensor';
ENVIRONMENTAL_SENSOR        : 'environmentalSensor';
GESTURE_SENSOR              : 'gestureSensor';
GET_LINE_SENSOR             : 'getLineSensor';
GYRO_RESET                  : 'gyroReset';
GYRO_SENSOR                 : 'gyroSensor';
HT_COLOR_SENSOR             : 'hiTechColorSensor';
HT_COMPASS_SENSOR           : 'hiTechCompassSensor';
HT_INFRARED_SENSOR          : 'hiTechInfraredSensor';
HUMIDITY_SENSOR             : 'humiditySensor';
IR_SEEKER_SENSOR            : 'irSeekerSensor';
INFRARED_SENSOR             : 'infraredSensor';
KEYS_SENSOR                 : 'keysSensor';
LIGHT_SENSOR                : 'lightSensor';
MOISTURE_SENSOR             : 'moistureSensor';
MOTION_SENSOR               : 'motionSensor';
PARTICLE_SENSOR             : 'particleSensor';
PIN_GET_VALUE_SENSOR        : 'pinGetValueSensor';
PIN_TOUCH_SENSOR            : 'pinTouchSensor';
PULSE_SENSOR                : 'pulseSensor';
RFID_SENSOR                 : 'rfidSensor';
SOUND_SENSOR                : 'soundSensor';
TEMPERATURE_SENSOR          : 'temperatureSensor';
TIMER_RESET                 : 'timerReset';
TIMER_SENSOR                : 'timerSensor';
TOUCH_SENSOR                : 'touchSensor';
ULTRASONIC_SENSOR           : 'ultrasonicSensor';
VEML_LIGHT_SENSOR           : 'vemlLightSensor';
VOLTAGE_SENSOR              : 'voltageSensor';
CONNECT_TO_ROBOT            : 'connectToRobot';

//Specific Sensors
CAMERA_SENSOR               : 'cameraSensor';
CAMERA_THRESHOLD            : 'cameraThreshold';
CODE_PAD_SENSOR             : 'codePadSensor';
COLOUR_BLOB                 : 'colourBlob';
DETECT_FACE_INFORMATION     : 'detectFaceInformation';
DETECT_FACE_SENSOR          : 'detectFaceSensor';
ELECTRIC_CURRENT_SENSOR     : 'electricCurrentSensor';
FSR_SENSOR                  : 'fsrSensor';
GPS_SENSOR                  : 'gpsSensor';
GYRO_RESET_AXIS             : 'gyroResetAxis';
JOYSTICK                    : 'joystick';
LOGO_TOUCH_SENSOR           : 'logoTouchSensor';
MARKER_INFORMATION          : 'markerInformation';
NAO_MARK_INFORMATION        : 'naoMarkInformation';
ODOMETRY_SENSOR             : 'odometrySensor';
ODOMETRY_SENSOR_RESET       : 'odometrySensorReset';
OPTICAL_SENSOR              : 'opticalSensor';
PIN_SET_TOUCH_MODE          : 'pinSetTouchMode';
QUAD_RGB_SENSOR             : 'quadRGBSensor';
RADIO_RSSI_SENSOR           : 'radioRssiSensor';
RECOGNIZE_WORD              : 'recognizeWord';
RESET_SENSOR                : 'resetSensor';
SOUND_RECORD                : 'soundRecord';
TAP_SENSOR                  : 'tapSensor';
HT_CCOMPASSSTARTCALIBRATION : 'hiTecCompassStartCalibration';

//ACTUATORS
GET_LED_BRIGTHNESS          : 'getLedBrigthness';
SHOWTEXT                    : 'showText';
SHOWIMAGE                   : 'showImage';
SHOWANIMATION               : 'showAnimation';
CLEARDISPLAY                : 'clearDisplay';
SETLED                      : 'setLed';
SHOWONSERIALMOTOR           : 'showOnSerial';
PITCH                       : 'pitch';
PLAYNOTE                    : 'playNote';
PLAY                        : 'playFile';
PLAYSOUND                   : 'playSound';
SETVOLUME                   : 'setVolume';
SPEAKER                     : 'speaker';
WRITEVALUE                  : 'writeValuePin';
SWITCHLED                   : 'switchLed';
SHOWCHARACTER               : 'showCharacter';
TURNRGBON                   : 'turnRgbOn';
TURNRGBOFF                  : 'turnRgbOff';
MOTORMOVE                   : 'motor.move';
MOTORSTOP                   : 'motor.stop';
RADIOSEND                   : 'radioSend';
RADIOSET                    : 'radioSet';
RECEIVEMESSAGE              : 'receiveMessage';
TURNONMOTOR                 : 'turnOnRegulatedMotor';
ROTATEMOTOR                 : 'rotateRegulatedMotor';
SETMOTORSPEED               : 'setRegulatedMotorSpeed';
STOPMOTOR                   : 'stopRegulatedMotor';
DRIVEDISTANCE               : 'driveDistance';
REGULATEDDRIVE              : 'regulatedDrive';
STOPDRIVE                   : 'stopRegulatedDrive';
ROTATEDIRECTIONANGLE        : 'rotateDirectionAngle';
ROTATEDIRECTIONREGULATED    : 'rotateDirectionRegulated';
DRIVEINCURVE                : 'driveInCurve';
SETLANGUAGE                 : 'setLanguage';
DRAWTEXT                    : 'drawText';
DRAWPICTURE                 : 'drawPicture';
PLAYTONE                    : 'playTone';
SAYTEXT                     : 'sayText';
LEDON                       : 'ledOn';
LEDOFF                      : 'ledOff';
RESETLED                    : 'resetLed';
GETSPEEDMOTOR               : 'getSpeedMotor';
GETVOLUME                   : 'getVolume';
SENDMESSAGE                 : 'sendMessage';
WAIT_FOR_CONNECTION         : 'waitForConnection';

//NEURAL NETWORKS
NNSTEP                      : 'nnStep';
SETINPUTNEURON              : 'setInputNeuron';
SETWEIGHT                   : 'setWeight';
SETBIAS                     : 'setBias';
GETOUTPUTNEURON             : 'getOutputNeuron';
GETWEIGHT                   : 'getWeight';
GETBIAS                     : 'getBias';

NAME    : ('a'..'z'|'A'..'Z')('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;
HEX     : ('a'..'z'|'A'..'F'|'0'..'9');
STR     : ('a'..'z'|'A'..'Z'|'0'..'9')+;
SET     : '=';
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




