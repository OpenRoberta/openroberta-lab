function createConstant(dataType, value) {
    var result = {};
    result[EXPR] = dataType;
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
        throw "Expression List is not List!"
    }
    var result = {};
    result[MODE] = mode;
    result[STMT] = REPEAT_STMT;
    result[EXPR] = expr;
    result[STMT_LIST] = stmtList;
    return result;
}

function createDriveAction(speed, direction, distance) {
    var result = {};
    result[STMT] = DRIVE_ACTION;
    result[SPEED] = speed;
    result[DRIVE_DIRECTION] = direction;
    if (distance == undefined) {
        result[DISTANCE] = undefined;
    } else {
        result[DISTANCE] = distance;
    }
    return result;
}

function createMotorOnAction(speed, motorSide, motorDuration) {
    var result = {};
    result[STMT] = MOTOR_ON_ACTION;
    result[SPEED] = speed;
    result[MOTOR_SIDE] = motorSide;
    if (motorDuration == undefined) {
        result[MOTOR_DURATION] = undefined;
    } else {
        result[MOTOR_DURATION] = motorDuration;
    }
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
    if (angle == undefined) {
        result[ANGLE] = undefined;
    } else {
        result[ANGLE] = angle;
    }
    return result;
}

function createTurnLight(color, mode) {
    var result = {};
    result[STMT] = TURN_LIGHT;
    result[COLOR] = color;
    result[MODE] = mode;
    return result;
}

function createResetLight() {
    var result = {};
    result[STMT] = RESET_LIGHT;
    return result;
}

function createStopDrive() {
    var result = {};
    result[STMT] = STOP_DRIVE;
    return result;
}

function createGetSample(sensorType, senorMode) {
    var result = {};
    result[EXPR] = GET_SAMPLE;
    result[SENSOR_TYPE] = sensorType;
    result[SENSOR_MODE] = senorMode;
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
