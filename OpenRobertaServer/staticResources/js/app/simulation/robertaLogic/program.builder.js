//define(function(require, exports) {

function createConstant(dataType, value) {
    var result = {};
    result[EXPR] = dataType;
    result[VALUE] = value;
    return result;
}

function createMathConstant(value) {
    var result = {};
    result[EXPR] = MATH_CONST;
    result[VALUE] = value;
    return result;
}

function createBinaryExpr(op, left, right) {
    var result = {};
    result[EXPR] = BINARY;
    result[OP] = op;
    result[LEFT] = left;
    result[RIGHT] = right;
    return result;
}

function createUnaryExpr(op, value) {
    var result = {};
    result[EXPR] = UNARY;
    result[OP] = op;
    result[VALUE] = value;
    return result;
}

function createSingleFunction(funcName, value) {
    var result = {};
    result[EXPR] = SINGLE_FUNCTION;
    result[OP] = funcName;
    result[VALUE] = value;
    return result;
}

function createMathPropFunct(funcName, arg1, arg2) {
    var result = {};
    result[EXPR] = MATH_PROP_FUNCT;
    result[OP] = funcName;
    result[ARG1] = arg1;
    result[ARG2] = arg2;
    return result;
}

function createMathConstrainFunct(val, min, max) {
    var result = {};
    result[EXPR] = MATH_CONSTRAIN_FUNCTION;
    result[VALUE] = val;
    result[MIN] = min;
    result[MAX] = max;
    return result;
}

function createRandInt(min, max) {
    var result = {};
    result[EXPR] = RANDOM_INT;
    result[MIN] = min;
    result[MAX] = max;
    return result;
}

function createRandDouble() {
    var result = {};
    result[EXPR] = RANDOM_DOUBLE;
    return result;
}

function createVarReference(type, name) {
    var result = {};
    result[EXPR] = VAR;
    result[TYPE] = type;
    result[NAME] = name;
    return result;
}

function createVarDeclaration(type, name, value) {
    var result = {};
    result[STMT] = VAR_DECLARATION;
    result[TYPE] = type;
    result[NAME] = name;
    result[VALUE] = value;
    return result;
}

function createAssignStmt(name, value) {
    var result = {};
    result[STMT] = ASSIGN_STMT;
    result[NAME] = name;
    result[EXPR] = value;
    return result;
}

function createRepeatStmt(mode, expr, stmtList) {
    if (!Array.isArray(stmtList)) {
        throw "Expression List is not List!";
    }
    var result = {};
    result[MODE] = mode;
    result[STMT] = REPEAT_STMT;
    result[EXPR] = expr;
    result[STMT_LIST] = stmtList;
    return result;
}

function createShowPictureAction(picture, x, y) {
    var result = {};
    result[STMT] = SHOW_PICTURE_ACTION;
    result[PICTURE] = picture;
    result[X] = x;
    result[Y] = y;
    return result;
}

function createShowTextAction(text, x, y) {
    var result = {};
    result[STMT] = SHOW_TEXT_ACTION;
    result[TEXT] = text;
    result[X] = x;
    result[Y] = y;
    return result;
}

function createClearDisplayAction() {
    var result = {};
    result[STMT] = CLEAR_DISPLAY_ACTION;
    return result;
}

function createDebugAction() {
    var result = {};
    result[STMT] = CREATE_DEBUG_ACTION;

    return result;
}
function createDriveAction(speed, direction, distance) {
    var result = {};
    result[STMT] = DRIVE_ACTION;
    result[SPEED] = speed;
    result[DRIVE_DIRECTION] = direction;
    result[DISTANCE] = distance;

    return result;
}

function createMotorOnAction(speed, motorSide, motorDuration) {
    var result = {};
    result[STMT] = MOTOR_ON_ACTION;
    result[SPEED] = speed;
    result[MOTOR_SIDE] = motorSide;
    result[MOTOR_DURATION] = motorDuration;

    return result;
}

function createSetMotorPowerAction(motorSide, speed) {
    var result = {};
    result[STMT] = MOTOR_SET_POWER;
    result[SPEED] = speed;
    result[MOTOR_SIDE] = motorSide;

    return result;
}

function createGetMotorPower(motorSide) {
    var result = {};
    result[EXPR] = MOTOR_GET_POWER;
    result[MOTOR_SIDE] = motorSide;

    return result;
}

function createDuration(motorMoveMode, duration) {
    var result = {};
    result[MOTOR_MOVE_MODE] = motorMoveMode;
    result[MOTOR_DURATION_VALUE] = duration;
    return result;
}

function createTurnAction(speed, direction, angle) {
    var result = {};
    result[STMT] = TURN_ACTION;
    result[SPEED] = speed;
    result[TURN_DIRECTION] = direction;
    result[ANGLE] = angle;

    return result;
}

function createTurnLight(color, mode) {
    var result = {};
    result[STMT] = TURN_LIGHT;
    result[COLOR] = color;
    result[MODE] = mode;
    return result;
}

function createStatusLight(mode) {
    var result = {};
    result[STMT] = STATUS_LIGHT_ACTION;
    result[MODE] = mode;
    return result;
}

function createStopDrive() {
    var result = {};
    result[STMT] = STOP_DRIVE;
    return result;
}

function createStopMotorAction(motorSide) {
    var result = {};
    result[STMT] = MOTOR_STOP;
    result[MOTOR_SIDE] = motorSide;
    return result;
}

function createGetSample(sensorType, senorMode) {
    var result = {};
    result[EXPR] = GET_SAMPLE;
    result[SENSOR_TYPE] = sensorType;
    result[SENSOR_MODE] = senorMode;
    return result;
}
function createGetSampleEncoderSensor(motorSide, senorMode) {
    var result = {};
    result[EXPR] = ENCODER_SENSOR_SAMPLE;
    result[MOTOR_SIDE] = motorSide;
    result[SENSOR_MODE] = senorMode;

    return result;
}

function createResetEncoderSensor(motorSide) {
    var result = {};
    result[STMT] = ENCODER_SENSOR_RESET;
    result[MOTOR_SIDE] = motorSide;

    return result;
}

function createIfStmt(exprList, thenList, elseStmts) {
    if (!Array.isArray(exprList)) {
        throw "Expression List is not List!"
    }
    if (!Array.isArray(thenList)) {
        throw "Then List is not List!"
    }
    result = {};
    result[STMT] = IF_STMT;
    result[EXPR_LIST] = exprList;
    result[THEN_LIST] = thenList;
    result[ELSE_STMTS] = elseStmts;
    return result;
}

function createWaitStmt(stmtList) {
    if (!Array.isArray(stmtList)) {
        throw "Statement List is not a List!";
    }
    var result = {};
    result[STMT] = WAIT_STMT;
    result[STATEMENTS] = stmtList;
    return result;
}

function createWaitTimeStmt(timeValue) {
    var result = {};
    result[STMT] = WAIT_TIME_STMT;
    result[TIME] = timeValue;
    return result;
}

//    var evalStmts = [];
//    exports.build = function(program) {
//        var stmts = program.split(";");
//        stmts.forEach(function(stmt) {
//            evalStmts.push(eval(stmt));
//        });
//    };
//});
