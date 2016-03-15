const
ARG1 = "arg1";
const
ARG2 = "arg2";
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
UNARY = "Unary";
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
SINGLE_FUNCTION = "SingleFunction";
const
MATH_CONSTRAIN_FUNCTION = "MathConstrainFunct";
const
RANDOM_INT = "randInt";
const
RANDOM_DOUBLE = "randDouble";
const
ASSIGN_STMT = "AssignStmt";
const
REPEAT_STMT = "RepeatStmt";
const
IF_STMT = "IfStatement";
const
WAIT_STMT = "WaitStmt";
const
MATH_CONST = "MathConst";
const
SHOW_TEXT_ACTION = "ShowTextAction";
const
SHOW_PICTURE_ACTION = "ShowPictureAction";
const
CLEAR_DISPLAY_ACTION = "ClearDisplay";
const
CREATE_DEBUG_ACTION = "CreateDebugAction";
const
TEXT = "text";
const
PICTURE = "picture";
const
X = "x";
const
Y = "y";
const
MATH_PROP_FUNCT = "MathPropFunct";

const
WAIT_TIME_STMT = "WaitTimeSTMT";

const
TIME = "time";

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
RESET = "reset";
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
STRING_CONST = "StringConst";
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
NEG = "NEG";
const
MOD = "MOD";
const
MIN = "min";
const
MAX = "max";
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
STATUS_LIGHT_ACTION = "statusLightAction";

const
TIMES = "TIMES";

const
COLOR_CONST = "COLOR_CONST";

const
PILOT = "pilot";
const
MOTOR_LEFT = "C";
const
MOTOR_RIGHT = "B";
const
MOTOR_SIDE = "motorSide";
const
MOTOR_ON_ACTION = "motorOnAction";
const
MOTOR_GET_POWER = "motorGetPowerAction";
const
MOTOR_SET_POWER = "motorSetPowerAction";
const
ENCODER_SENSOR_RESET = "encoderSensorReset";
const
ENCODER_SENSOR_SAMPLE = "encoderSensorSample";
const
MOTOR_STOP = "motorStop";
const
MOTOR_MOVE_MODE = "motorMoveMode";
const
MOTOR_DURATION = "motorDuration";
const
MOTOR_DURATION_VALUE = "motorDurationValue";
const
ROTATIONS = "ROTATIONS";
const
ROTATION = "ROTATION";
const
DEGREE = "DEGREE";

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
MAXPOWER = 0.351858377 * 3; // real Robot drives approx. 35 cm / 1 sec -> 105 pix/sec | 3pix = 1cm
const
ENC = 360.0 / (3.0 * Math.PI * WHEEL_DIAMETER);
const
MAX_WIDTH = 2000;
const
MAX_HEIGHT = 1000;
const
WAVE_LENGTH = 60;
