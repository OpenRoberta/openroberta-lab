const
EXPR = "expr";
const
VALUE = "value";
const
OP = "op";
const
LEFT = "left";
const
RIGHT = "right";
const
BINARY = "Binary";
const
VAR = "Var";
const
TYPE = "type";
const
NAME = "name";
const
STMT = "stmt";
const
VAR_DECLARATION = "VarDeclaration";
const
ASSIGN_STMT = "AssignStmt";
const
REPEAT_STMT = "RepeatStmt";
const
IF_STMT = "IfStatement";
const
WAIT_STMT = "WaitStmt";

const 
WAIT_TIME_STMT = "WaitTimeSTMT" ;

const 
TIME = "time" ;

const
STMT_LIST = "stmtList";
const
DRIVE_ACTION = "DriveAction";
const
TURN_LIGHT = "turnLight";
const
TURN_ACTION = "TurnAction";
const
STOP_DRIVE = "stopDrive";
const
DRIVE_DIRECTION = "driveDirection";
const
TURN_DIRECTION = "turnDirection";
const
SPEED = "speed";
const
DISTANCE = "distance";
const
GET_SAMPLE = "GetSample";
const
SENSOR_TYPE = "sensorType";
const
SENSOR_MODE = "sensorMode";
const
THEN_LIST = "thenList";
const
ELSE_STMTS = "elseStmts";
const
EXPR_LIST = "exprList";
const
STATEMENTS = "statements";
const
TOUCH = "TOUCH";
const
ULTRASONIC = "ULTRASONIC";
const
NUM_CONST = "NumConst";
const
BOOL_CONST = "BoolConst";
const
NUMERIC = "Numeric";
const
ADD = "ADD";
const
MINUS = "MINUS";
const
POWER = "POWER";
const
DIVIDE = "DIVIDE";
const
MULTIPLY = "MULTIPLY";

const
LT = "LT";
const
GT = "GT";
const
EQ = "EQ";
const
NEQ = "NEQ";
const
GTE = "GTE";
const
LTE = "LTE";

const
OR = "OR";
const
AND = "AND";

const
FOREWARD = "FOREWARD";
const
BACKWARD = "BACKWARD";
const
WHILE = "WHILE";
const
FOREVER = "FOREVER";
const
ANGLE = "angle";
const
COLOR = "color";
const
COLOUR = "COLOUR";
const
MODE = "mode";
const
GREEN = "GREEN";
const
RED = "RED";
const
ORANGE = "ORANGE";

const
ON = "ON";
const
OFF = "OFF";
const
FLASH = "FLASH";
const
DOUBLE_FLASH = "DOUBLE_FLASH";

const
RESET_LIGHT = "resetLight";

const
TIMES = "TIMES";

const
COLOR_CONST = "COLOR_CONST";

const
COLOR_ENUM = {
    NONE : 0,
    BLACK : 1,
    BLUE : 2,
    GREEN : 3,
    YELLOW : 4,
    RED : 5,
    WHITE : 6,
    BROWN : 7
};

const
TRACKWIDTH = 40;
const
TURN_RATIO = (TRACKWIDTH / 3.) / 2.8;
const
WHEEL_DIAMETER = 5.6;
const
MAXDIAG = 2500;
const
MAXPOWER = 0.35 * 3; // real Robot drives approx. 35 cm / 1 sec -> 105 pix/sec | 3pix = 1cm
const
ENC = 360.0 / (3.0 * Math.PI * WHEEL_DIAMETER);
const
MAX_WIDTH = 2000;
const
MAX_HEIGHT = 1000;
const
WAVE_LENGTH = 60;
