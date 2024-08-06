grammar Exprly;

// Rules for complete program textual representation

program                 : declaration* mainBlock userFunc* EOF ;
declaration             : nameDecl '=' expr ';'                                        # VariableDeclaration ;
mainBlock               : 'void' 'main' '(' ')' '{' statementList '}'                               # MainFunc ;
userFunc                : PRIMITIVETYPE NAME '(' (expr(',' expr)*)* ')' '{' statementList (op=RETURN (NAME|expr))? '}' # FuncUser;
nameDecl                : PRIMITIVETYPE NAME                                                        # ParamsMethod;

// Statements block rules

statementList           : (stmt';')*
                        ;

stmt                    : FNAMESTMT '(' ( expr (',' expr)* )? ')'                                                                                                                       # StmtFunc
                        | NAME '(' ( expr (',' expr)* )? ')'                                                                                                                            # StmtUsedDefCall
                        | NAME op=SET  expr                                                                                                                                             # BinaryVarAssign
                        | IF '(' expr ')' '{' statementList '}' (ELSEIF '(' expr ')' '{' statementList '}')* (op=ELSE '{' statementList '}')?                                           # ConditionStatementBlock
                        | REPEATFOR '('nameDecl '=' expr ';' NAME '<'expr ';' (expr op=STEP |NAME '=' NAME op=(ADD | SUB) expr |NAME '=' expr)? ')' '{' statementList '}'     # RepeatFor
                        | WHILE'(' expr ')' '{' statementList '}'                                                                                                                       # RepeatStatement
                        | REPEATFOREACH '(' nameDecl ':' expr ')' '{' statementList '}'                                                                                      # RepeatForEach
                        | WAIT '(' expr ')' '{' statementList '}' (op=ORWAITFOR '(' expr ')' '{' statementList '}')*                                                                    # WaitStatement
                        | op= (BREAK | CONTINUE )                                                                                                                                       # FlowControl
                        | WAITMS'(' expr ')'                                                                                                                                            # WaitTimeStatement
                        | IF '(' expr ')' RETURN (NAME | expr)                                                                                                                          # UserFuncIfStmt
                        | robotStmt                                                                                                                                                     # RobotStatement
                        ;

robotStmt               :  robotMicrobitv2Stmt
                        |  robotWeDoStmt
                        ;

expression  :  expr EOF
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
             | expr '?' expr ':' expr                                    # IfElseOp
             | robotExpr                                                 # RobotExpression
             ;

literal      : COLOR                                                     # Col
             | INT                                                       # IntConst
             | FLOAT                                                     # FloatConst
             | BOOL                                                      # BoolConstB
             | '"'  (.*?|'.'|'?')  '"'                                   # ConstStr
             | '[' (expr ',')* expr? ']'                                 # ListExpr
             ;

connExpr     : 'connect' op0=(STR|NAME) ',' op1=(STR|NAME)               # Conn
             ;

funCall      : FNAME '(' ( expr (',' expr)* )? ')'                       # Func
             | NAME '(' ( expr (',' expr)* )? ')'                        # UserDefCall
             ;


robotExpr    :      robotMicrobitv2Expr
             |      robotWeDoExpr
             ;

// Specific Expressions and Statement for MicrobitV2, the AST element will be generated by RobotsMbedTextlyVisitor

robotMicrobitv2Expr     : 'microbitv2' '.' microbitv2SensorExpr          # RobotMicrobitv2Expression;
microbitv2SensorExpr    : ACCELEROMETER_SENSOR '(' NAME ')'
                        | LOGO_TOUCH_SENSOR '.' 'isPressed' '('')'
                        | COMPASS_SENSOR '.' 'getAngle' '('')'
                        | GESTURE_SENSOR '.' 'currentGesture' '(' op=('up' | 'down' | 'faceDown' | 'faceUp' | 'shake' | 'freefall') ')'
                        | KEYS_SENSOR '.' 'isPressed' '(' NAME ')'
                        | LIGHT_SENSOR '.' 'getLevel' '('')'
                        | PIN_GET_VALUE_SENSOR  '(' NAME ',' op=('analog'|'digital'|'pulseHigh'|'pulseLow')')'
                        | PIN_TOUCH_SENSOR '.' 'isPressed' '(' INT ')'
                        | SOUND_SENSOR '.' 'microphone''.' 'soundLevel' '('')'
                        | TEMPERATURE_SENSOR '('')'
                        | TIMER_SENSOR '('')'
                        ;
robotMicrobitv2Stmt     : 'microbitv2' '.' microbitv2SensorStmt                                     # RobotMicrobitv2Statement;
microbitv2SensorStmt    : PIN_SET_TOUCH_MODE '(' INT ',' op=('capacitive'|'resistive') ')'
                        |  TIMER_RESET '('')'
                        ;

// Specific Expressions and Statements for WeDo,  the AST element will be generated by WedoTextlyVisitor

robotWeDoExpr           : 'wedo' '.' weDoSensorExpr                     # RobotWeDoExpression;
weDoSensorExpr          : GYRO_SENSOR'.' 'isTilted' '('NAME ',' slot=(UP|DOWN|BACK|FRONT|NO|ANY) ')'
                        | INFRARED_SENSOR'('NAME')'
                        | KEYS_SENSOR'.' 'isPressed' '(' NAME ')'
                        | TIMER_SENSOR'('')'
                        ;

robotWeDoStmt           :  'wedo' '.' wedoSensorStmt                                                # RobotWeDoStatement;
wedoSensorStmt          :   TIMER_RESET '('')' ;


// LEXER RULES

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


NEWLINE         :   '\r'? '\n'  -> skip;

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

FNAMESTMT       : 'showText'
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
                | '#none'
                | '#rgb(' HEX HEX HEX HEX HEX HEX ')'
                ;

BOOL            : 'true' | 'false';

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
HT_COLOR_SENSOR             : 'htColorSensor';
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


NAME    : ('a'..'z'|'A'..'Z')('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;
HEX     : ('A'..'F'|'0'..'9');
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

