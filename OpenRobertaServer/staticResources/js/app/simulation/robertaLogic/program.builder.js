define([ 'exports', 'robertaLogic.constants' ], function(exports, CONST) {

    function build(program) {
        eval(program);
        return blocklyProgram;
    }

    exports.build = build;

    function createConstant(dataType, value) {
        var result = {};
        result[CONST.EXPR] = dataType;
        result[CONST.VALUE] = value;
        return result;
    }

    function createMathConstant(value) {
        var result = {};
        result[CONST.EXPR] = CONST.MATH_CONST;
        result[CONST.VALUE] = value;
        return result;
    }

    function createMathChange(left, right) {
        var binary = createBinaryExpr(CONST.ADD, left, right);
        var assignment = createAssignStmt(left.name, binary)
        return assignment;
    }

    function createTextAppend(left, right) {
        var binary = createBinaryExpr(CONST.TEXT_APPEND, left, right);
        var assignment = createAssignStmt(left.name, binary)
        return assignment;
    }

    function createBinaryExpr(op, left, right) {
        var result = {};
        result[CONST.EXPR] = CONST.BINARY;
        result[CONST.OP] = op;
        result[CONST.LEFT] = left;
        result[CONST.RIGHT] = right;
        return result;
    }

    function createUnaryExpr(op, value) {
        var result = {};
        result[CONST.EXPR] = CONST.UNARY;
        result[CONST.OP] = op;
        result[CONST.VALUE] = value;
        return result;
    }

    function createSingleFunction(funcName, value) {
        var result = {};
        result[CONST.EXPR] = CONST.SINGLE_FUNCTION;
        result[CONST.OP] = funcName;
        result[CONST.VALUE] = value;
        return result;
    }

    function createMathPropFunct(funcName, arg1, arg2) {
        var result = {};
        result[CONST.EXPR] = CONST.MATH_PROP_FUNCT;
        result[CONST.OP] = funcName;
        result[CONST.ARG1] = arg1;
        result[CONST.ARG2] = arg2;
        return result;
    }

    function createMathConstrainFunct(val, min, max) {
        var result = {};
        result[CONST.EXPR] = CONST.MATH_CONSTRAIN_FUNCTION;
        result[CONST.VALUE] = val;
        result[CONST.MIN] = min;
        result[CONST.MAX] = max;
        return result;
    }

    function createRandInt(min, max) {
        var result = {};
        result[CONST.EXPR] = CONST.RANDOM_INT;
        result[CONST.MIN] = min;
        result[CONST.MAX] = max;
        return result;
    }

    function createRandDouble() {
        var result = {};
        result[EXPR] = RANDOM_DOUBLE;
        return result;
    }

    function createVarReference(type, name) {
        var result = {};
        result[CONST.EXPR] = CONST.VAR;
        result[CONST.TYPE] = type;
        result[CONST.NAME] = name;
        return result;
    }

    function createVarDeclaration(type, name, value) {
        var result = {};
        result[CONST.STMT] = CONST.VAR_DECLARATION;
        result[CONST.TYPE] = type;
        result[CONST.NAME] = name;
        result[CONST.VALUE] = value;
        return result;
    }

    function createAssignStmt(name, value) {
        var result = {};
        result[CONST.STMT] = CONST.ASSIGN_STMT;
        result[CONST.NAME] = name;
        result[CONST.EXPR] = value;
        return result;
    }

    function createRepeatStmt(mode, expr, stmtList) {
        if (!Array.isArray(stmtList)) {
            throw "Expression List is not List!";
        }
        var result = {};
        result[CONST.MODE] = mode;
        result[CONST.STMT] = CONST.REPEAT_STMT;
        result[CONST.EXPR] = expr;
        result[CONST.STMT_LIST] = stmtList;
        result.eachCounter = 0;
        return result;
    }

    function createShowPictureAction(picture, x, y) {
        var result = {};
        result[CONST.STMT] = CONST.SHOW_PICTURE_ACTION;
        result[CONST.PICTURE] = picture;
        result[CONST.X] = x;
        result[CONST.Y] = y;
        return result;
    }

    function createShowTextAction(text, x, y) {
        var result = {};
        result[CONST.STMT] = CONST.SHOW_TEXT_ACTION;
        result[CONST.TEXT] = text;
        result[CONST.X] = x;
        result[CONST.Y] = y;
        return result;
    }

    function createClearDisplayAction() {
        var result = {};
        result[CONST.STMT] = CONST.CLEAR_DISPLAY_ACTION;
        return result;
    }

    function createDebugAction() {
        var result = {};
        result[CONST.STMT] = CONST.CREATE_DEBUG_ACTION;

        return result;
    }

    function createDriveAction(speed, direction, distance) {
        var result = {};
        result[CONST.STMT] = CONST.DRIVE_ACTION;
        result[CONST.SPEED] = speed;
        result[CONST.DRIVE_DIRECTION] = direction;
        result[CONST.DISTANCE] = distance;

        return result;
    }

    function createMotorOnAction(speed, motorSide, motorDuration) {
        var result = {};
        result[CONST.STMT] = CONST.MOTOR_ON_ACTION;
        result[CONST.SPEED] = speed;
        result[CONST.MOTOR_SIDE] = motorSide;
        result[CONST.MOTOR_DURATION] = motorDuration;

        return result;
    }

    function createSetMotorPowerAction(motorSide, speed) {
        var result = {};
        result[CONST.STMT] = CONST.MOTOR_SET_POWER;
        result[CONST.SPEED] = speed;
        result[CONST.MOTOR_SIDE] = motorSide;

        return result;
    }

    function createGetMotorPower(motorSide) {
        var result = {};
        result[CONST.EXPR] = CONST.MOTOR_GET_POWER;
        result[CONST.MOTOR_SIDE] = motorSide;

        return result;
    }

    function createDuration(motorMoveMode, duration) {
        var result = {};
        result[CONST.MOTOR_MOVE_MODE] = motorMoveMode;
        result[CONST.MOTOR_DURATION_VALUE] = duration;
        return result;
    }

    function createToneAction(frequency, duration) {
        var result = {};
        result[CONST.STMT] = CONST.TONE_ACTION;
        result[CONST.FREQUENCY] = frequency;
        result[CONST.DURATION] = duration;
        return result;
    }

    function createSetVolumeAction(mode, volume) {
        var result = {};
        result[CONST.STMT] = CONST.SET_VOLUME_ACTION;
        result[CONST.VOLUME] = volume;
        return result;
    }

    function createGetVolume() {
        var result = {};
        result[CONST.EXPR] = CONST.GET_VOLUME;
        return result;
    }

    function createPlayFileAction(file) {
        var result = {};
        result[CONST.STMT] = CONST.PLAY_FILE_ACTION;
        result[CONST.FILE] = file;
        return result;
    }

    function createTurnAction(speed, direction, angle) {
        var result = {};
        result[CONST.STMT] = CONST.TURN_ACTION;
        result[CONST.SPEED] = speed;
        result[CONST.TURN_DIRECTION] = direction;
        result[CONST.ANGLE] = angle;

        return result;
    }

    function createTurnLight(color, mode) {
        var result = {};
        result[CONST.STMT] = CONST.TURN_LIGHT;
        result[CONST.COLOR] = color;
        result[CONST.MODE] = mode;
        return result;
    }

    function createStatusLight(mode) {
        var result = {};
        result[CONST.STMT] = CONST.STATUS_LIGHT_ACTION;
        result[CONST.MODE] = mode;
        return result;
    }

    function createStopDrive() {
        var result = {};
        result[CONST.STMT] = CONST.STOP_DRIVE;
        return result;
    }

    function createStopMotorAction(motorSide) {
        var result = {};
        result[CONST.STMT] = CONST.MOTOR_STOP;
        result[CONST.MOTOR_SIDE] = motorSide;
        return result;
    }

    function createGetSample(sensorType, sensorMode) {
        var result = {};
        result[CONST.EXPR] = CONST.GET_SAMPLE;
        result[CONST.SENSOR_TYPE] = sensorType;
        result[CONST.SENSOR_MODE] = sensorMode;
        return result;
    }

    function createResetGyroSensor() {
        var result = {};
        result[CONST.STMT] = CONST.GYRO_SENSOR_RESET;
        return result;
    }

    function createResetTimer(timer) {
        var result = {};
        result[CONST.STMT] = CONST.TIMER_SENSOR_RESET;
        result[CONST.TIMER] = timer;
        return result;
    }

    function createGetSampleEncoderSensor(motorSide, senorMode) {
        var result = {};
        result[CONST.EXPR] = CONST.ENCODER_SENSOR_SAMPLE;
        result[CONST.MOTOR_SIDE] = motorSide;
        result[CONST.SENSOR_MODE] = senorMode;

        return result;
    }

    function createResetEncoderSensor(motorSide) {
        var result = {};
        result[CONST.STMT] = CONST.ENCODER_SENSOR_RESET;
        result[CONST.MOTOR_SIDE] = motorSide;

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
        result[CONST.STMT] = CONST.IF_STMT;
        result[CONST.EXPR_LIST] = exprList;
        result[CONST.THEN_LIST] = thenList;
        result[CONST.ELSE_STMTS] = elseStmts;
        return result;
    }

    function createWaitStmt(stmtList) {
        if (!Array.isArray(stmtList)) {
            throw "Statement List is not a List!";
        }
        var result = {};
        result[CONST.STMT] = CONST.WAIT_STMT;
        result[CONST.STATEMENTS] = stmtList;
        return result;
    }

    function createWaitTimeStmt(timeValue) {
        var result = {};
        result[CONST.STMT] = CONST.WAIT_TIME_STMT;
        result[CONST.TIME] = timeValue;
        return result;
    }

    function createCreateListWith(type, values) {
        var result = {}
        result[CONST.EXPR] = type;
        result[CONST.VALUE] = values;
        return result;
    }

    function createCreateListWithItem(value, size) {
        var result = {}
        result[CONST.EXPR] = CONST.CREATE_LIST_WITH_ITEM;
        result[CONST.SIZE] = size;
        result[CONST.VALUE] = value;
        return result;
    }

    function createListLength(list) {
        var result = {};
        result[CONST.EXPR] = CONST.CREATE_LIST_LENGTH;
        result[CONST.LIST] = list;
        return result;
    }

    function createListIsEmpty(list) {
        var result = {};
        result[CONST.EXPR] = CONST.CREATE_LIST_IS_EMPTY;
        result[CONST.LIST] = list;
        return result;
    }

    function createListFindItem(position, list, item) {
        var result = {};
        result[CONST.EXPR] = CONST.CREATE_LIST_FIND_ITEM;
        result[CONST.POSITION] = position;
        result[CONST.LIST] = list;
        result[CONST.ITEM] = item;
        return result;
    }

    function createListsSetIndex(list, op, newVal, position, item) {
        var result = {};
        result[CONST.STMT] = CONST.CREATE_LISTS_SET_INDEX;
        result[CONST.OP] = op;
        result[CONST.POSITION] = position;
        result[CONST.VALUE] = newVal;
        result[CONST.LIST] = list;
        result[CONST.ITEM] = item;
        return result;
    }

    function createListsGetIndex(list, op, position, item) {
        var result = {};
        result[CONST.EXPR] = CONST.CREATE_LISTS_GET_INDEX;
        result[CONST.OP] = op;
        result[CONST.POSITION] = position;
        result[CONST.LIST] = list;
        result[CONST.ITEM] = item;
        return result;
    }

    function createListsGetIndexStmt(list, op, position, item) {
        var result = {};
        result[CONST.STMT] = CONST.CREATE_LISTS_GET_INDEX_STMT;
        result[CONST.OP] = op;
        result[CONST.POSITION] = position;
        result[CONST.LIST] = list;
        result[CONST.ITEM] = item;
        return result;
    }

    function createGetSubList(args) {
        var result = {};
        result = args;
        result[CONST.EXPR] = CONST.CREATE_LISTS_GET_SUBLIST;
        return result;
    }

    function createMathOnList(op, list) {
        var result = {};
        result[CONST.EXPR] = CONST.MATH_ON_LIST;
        result[CONST.OP] = op;
        result[CONST.LIST] = list;
        return result;
    }

    function createTextJoin(values) {
        var result = {};
        result[CONST.EXPR] = CONST.TEXT_JOIN;
        result[CONST.VALUE] = values;
        return result;
    }

    function createTernaryExpr(cond, then, _else) {
        var result = {};
        result[CONST.EXPR] = CONST.TERNARY_EXPR;
        result[CONST.EXPR_LIST] = cond;
        result[CONST.THEN_LIST] = then;
        result[CONST.ELSE_STMTS] = _else;
        return result;
    }

    function createMethodVoid(methodName, parameters, stmts) {
        var result = {};
        result[CONST.FUNCTION_DECLARATION] = CONST.METHOD_VOID;
        result[CONST.NAME] = methodName;
        result[CONST.PARAMETERS] = parameters;
        result[CONST.STMT_LIST] = stmts;
        return result;
    }

    function createMethodReturn(methodName, stmts, return_) {
        var result = {};
        result[CONST.FUNCTION_DECLARATION] = CONST.METHOD_RETURN;
        result[CONST.NAME] = methodName;
        result[CONST.STMT_LIST] = stmts;
        result[CONST.RETURN] = return_;
        return result;
    }

    function createMethodCallVoid(methodName, parameters, values) {
        var result = {};
        result[CONST.STMT] = CONST.METHOD_CALL_VOID;
        result[CONST.NAME] = methodName;
        result[CONST.PARAMETERS] = parameters;
        result[CONST.VALUES] = values;
        return result;
    }

    function createMethodCallReturn(methodName, parameters, values) {
        var result = {};
        result[CONST.EXPR] = CONST.METHOD_CALL_RETURN;
        result[CONST.NAME] = methodName;
        result[CONST.PARAMETERS] = parameters;
        result[CONST.VALUES] = values;
        return result;
    }

});
