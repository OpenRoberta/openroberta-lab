import * as C from "./constants";

export function build( program ) {
    var blocklyProgram;
    eval( program );
    return blocklyProgram;
}

export function createStmtList( stmts ) {
    return concat( ...stmts );
}

export function createConstant( dataType, value ) {
    var result = {};
    result[C.OPCODE] = C.EXPR;
    result[C.EXPR] = dataType;
    result[C.VALUE] = value;
    return [result];
}

export function createMathConstant( value ) {
    var result = {};
    result[C.OPCODE] = C.EXPR;
    result[C.EXPR] = C.MATH_CONST;
    result[C.VALUE] = value;
    return [result];
}

export function createMathChange( left, right ) {
    var binary = createBinaryExpr( C.ADD, left, right );
    return createAssignStmt( left.name, binary );
}

export function createTextAppend( left, right ) {
    var binary = createBinaryExpr( C.TEXT_APPEND, left, right );
    return createAssignStmt( left.name, binary );
}

export function createBinaryExpr( op, left, right ) {
    var result = {};
    result[C.OPCODE] = C.EXPR;
    result[C.EXPR] = C.BINARY;
    result[C.OP] = op;
    return concat( left, right, [result] );
}

export function createUnaryExpr( op, value ) {
    var result = {};
    result[C.OPCODE] = C.EXPR;
    result[C.EXPR] = C.UNARY;
    result[C.OP] = op;
    return push( value, result );
}

export function createSingleFunction( funcName, value ) {
    var result = {};
    result[C.OPCODE] = C.EXPR;
    result[C.EXPR] = C.SINGLE_FUNCTION;
    result[C.OP] = funcName;
    result[C.VALUE] = value;
    return [result];
}

export function createMathPropFunct( funcName, arg1, arg2 ) {
    var result = {};
    result[C.OPCODE] = C.EXPR;
    result[C.EXPR] = C.MATH_PROP_FUNCT;
    result[C.OP] = funcName;
    return concat( arg1, arg2, [result] );
}

export function createMathConstrainFunct( val, min, max ) {
    var result = {};
    result[C.OPCODE] = C.EXPR;
    result[C.EXPR] = C.MATH_CONSTRAIN_FUNCTION;
    return concat( val, min, max, [result] );
}

export function createRandInt( min, max ) {
    var result = {};
    result[C.OPCODE] = C.EXPR;
    result[C.EXPR] = C.RANDOM_INT;
    result[C.MIN] = min;
    result[C.MAX] = max;
    return concat( min, max, [result] );
}

export function createRandDouble() {
    var result = {};
    result[C.OPCODE] = C.EXPR;
    result[C.EXPR] = C.RANDOM_DOUBLE;
    return [result];
}

export function createVarReference( type, name ) {
    var result = {};
    result[C.OPCODE] = C.EXPR;
    result[C.EXPR] = C.VAR;
    result[C.TYPE] = type;
    result[C.NAME] = name;
    return [result];
}

export function createVarDeclaration( type, name, value ) {
    var result = {};
    result[C.OPCODE] = C.VAR_DECLARATION;
    result[C.TYPE] = type;
    result[C.NAME] = name;
    return push( value, result );
}

export function createAssignStmt( name, value ) {
    var result = {};
    result[C.OPCODE] = C.ASSIGN_STMT;
    result[C.NAME] = name;
    return push( value, result );
}

export function createAssignMethodParameter( name, value ) {
    var result = {};
    result[C.OPCODE] = C.ASSIGN_METHOD_PARAMETER_STMT;
    result[C.NAME] = name;
    return push( value, result );
}

export function createRepeatStmt( loopNumber, mode, variable, theDecl, theEnd, theStep, stmtList ) {
    if ( !Array.isArray( stmtList ) ) {
        throw "Expression List is not List!";
    }
    var result = {};
    result[C.OPCODE] = C.REPEAT_STMT;
    result[C.MODE] = mode;
    result[C.LOOP_NUMBER] = loopNumber;
    result[C.VAR] = variable;
    result[C.STMT_LIST] = stmtList;
    result[C.EACH_COUNTER] = 0;
    return concat( theDecl, theEnd, theStep, [result] );
}

export function createStmtFlowControl( loopNumber, mode ) {
    var result = {};
    result[C.OPCODE] = C.FLOW_CONTROL;
    result[C.MODE] = mode;
    result[C.LOOP_NUMBER] = loopNumber;
    return [result];
}

export function createRgbColor( rgbColor ) {
    var result = {};
    result[C.OPCODE] = C.EXPR;
    result[C.EXPR] = C.RGB_COLOR_CONST;
    result[C.VALUE] = rgbColor;
    return [result];
}

export function createLedOnAction( ledColor ) {
    var result = {};
    result[C.OPCODE] = C.LED_ON_ACTION;
    result[C.RGB_COLOR_CONST] = ledColor;
    return [result];
}

export function createShowTextAction( text, x, y ) {
    var result = {};
    result[C.OPCODE] = C.SHOW_TEXT_ACTION;
    return concat( text, x, y, [result] );
}

export function createDriveAction( speed, direction, distance ) {
    var result = {};
    result[C.OPCODE] = C.DRIVE_ACTION;
    result[C.SPEED] = speed;
    result[C.DRIVE_DIRECTION] = direction;
    result[C.DISTANCE] = distance;
    return concat( speed, distance, [result] );
}

export function createMotorOnAction( speed, motorSide, motorDuration ) {
    var result = {};
    result[C.OPCODE] = C.MOTOR_ON_ACTION;
    result[C.MOTOR_SIDE] = motorSide;
    return concat( speed, motorDuration, [result] );
}

export function createSetMotorPowerAction( motorSide, speed ) {
    var result = {};
    result[C.OPCODE] = C.MOTOR_SET_POWER;
    result[C.MOTOR_SIDE] = motorSide;
    return push( speed, result );
}

export function createGetMotorPower( motorSide ) {
    var result = {};
    result[C.OPCODE] = C.EXPR;
    result[C.EXPR] = C.MOTOR_GET_POWER;
    result[C.MOTOR_SIDE] = motorSide;
    return [result];
}

export function createDuration( motorMoveMode, duration ) {
    var result = {};
    result[C.OPCODE] = C.MOTOR_ACTION;
    result[C.MOTOR_MOVE_MODE] = motorMoveMode;
    return push( duration, result );
}

export function createToneAction( frequency, duration ) {
    var result = {};
    result[C.OPCODE] = C.TONE_ACTION;
    result[C.FREQUENCY] = frequency;
    result[C.DURATION] = duration;
    return concat( frequency, duration, [result] )
}

export function createSetVolumeAction( mode, volume ) {
    var result = {};
    result[C.OPCODE] = C.SET_VOLUME_ACTION;
    return push( volume, result );
}

export function createGetVolume() {
    var result = {};
    result[C.OPCODE] = C.EXPR;
    result[C.EXPR] = C.GET_VOLUME;
    return [result];
}

export function createPlayFileAction( file ) {
    var result = {};
    result[C.OPCODE] = C.PLAY_FILE_ACTION;
    return push( file, result );
}

export function createTurnLight( color, mode ) {
    var result = {};
    result[C.OPCODE] = C.TURN_LIGHT;
    result[C.COLOUR] = color;
    result[C.MODE] = mode;
    return [result];
}

export function createLightSensorAction( color, mode ) {
    var result = {};
    result[C.OPCODE] = C.LIGHT_ACTION;
    result[C.COLOUR] = color;
    result[C.MODE] = mode;
    return [result];
}

export function createStatusLight( mode ) {
    var result = {};
    result[C.OPCODE] = C.STATUS_LIGHT_ACTION;
    result[C.MODE] = mode;
    return [result];
}

export function createStopMotorAction( motorSide ) {
    var result = {};
    result[C.OPCODE] = C.MOTOR_STOP;
    result[C.MOTOR_SIDE] = motorSide;
    return [result];
}

export function createGetSample( sensorType, sensorMode ) {
    var result = {};
    result[C.OPCODE] = C.EXPR;
    result[C.EXPR] = C.GET_SAMPLE;
    result[C.SENSOR_TYPE] = sensorType;
    result[C.SENSOR_MODE] = sensorMode;
    return [result];
}

export function createGetGyroSensorSample( sensorType, sensorMode ) {
    var result = {};
    result[C.OPCODE] = C.EXPR;
    result[C.EXPR] = C.GET_GYRO_SENSOR_SAMPLE;
    result[C.SENSOR_TYPE] = sensorType;
    result[C.SENSOR_MODE] = sensorMode;
    return [result];
}

export function createResetGyroSensor() {
    var result = {};
    result[C.OPCODE] = C.GYRO_SENSOR_RESET;
    return [result];
}

export function createResetTimer( timer ) {
    var result = {};
    result[C.OPCODE] = C.TIMER_SENSOR_RESET;
    result[C.TIMER] = timer;
    return [result];
}

export function createGetSampleEncoderSensor( motorSide, sensorMode ) {
    var result = {};
    result[C.OPCODE] = C.EXPR;
    result[C.EXPR] = C.ENCODER_SENSOR_SAMPLE;
    result[C.MOTOR_SIDE] = motorSide;
    result[C.SENSOR_MODE] = sensorMode;
    return [result];
}

export function createResetEncoderSensor( motorSide ) {
    var result = {};
    result[C.OPCODE] = C.ENCODER_SENSOR_RESET;
    result[C.MOTOR_SIDE] = motorSide;
    return [result];
}

export function createIfStmt( exprList, thenList, elseStmts ) {
    if ( !Array.isArray( exprList ) ) {
        throw "Expression List is not List!"
    }
    if ( !Array.isArray( thenList ) ) {
        throw "Then List is not List!"
    }
    var result = {};
    result[C.OPCODE] = C.IF_STMT;
    result[C.EXPR_LIST] = {};
    for ( var i = 0; i < exprList.length; i++ ) {
        result[C.EXPR_LIST][i] = exprList[i];
    }
    result[C.THEN_LIST] = {};
    for ( var i = 0; i < thenList.length; i++ ) {
        result[C.THEN_LIST][i] = thenList[i];
    }
    result[C.ELSE_STMTS] = elseStmts;
    return [result];
}

export function createWaitStmt( stmtList ) {
    if ( !Array.isArray( stmtList ) ) {
        throw "Statement List is not a List!";
    }
    var result = {};
    result[C.OPCODE] = C.WAIT_STMT;
    result[C.STATEMENTS] = {};
    for ( var i = 0; i < stmtList.length; i++ ) {
        result[C.STATEMENTS][i] = stmtList[i];
    }
    return [result];
}

export function createWaitTimeStmt( timeValue ) {
    var result = {};
    result[C.OPCODE] = C.WAIT_TIME_STMT;
    return push( timeValue, result );
}

export function createTextJoin( values ) {
    var result = {};
    result[C.OPCODE] = C.EXPR;
    result[C.EXPR] = C.TEXT_JOIN;
    result[C.VALUE] = values;
    return [result];
}

export function createTernaryExpr( cond, then, _else ) {
    var result = {};
    result[C.OPCODE] = C.EXPR;
    result[C.EXPR] = C.TERNARY_EXPR;
    result[C.THEN_LIST] = then;
    result[C.ELSE_STMTS] = _else;
    return push( cond, result );
}

export function createMethodVoid( methodName, parameters, stmts ) {
    var result = {};
    result[C.FUNCTION_DECLARATION] = C.METHOD_VOID;
    result[C.NAME] = methodName;
    result[C.PARAMETERS] = parameters;
    result[C.STMT_LIST] = stmts;
    return [result];
}

export function createMethodReturn( methodName, stmts, returnType, return_ ) {
    var result = {};
    result[C.FUNCTION_DECLARATION] = C.METHOD_RETURN;
    result[C.NAME] = methodName;
    result[C.STMT_LIST] = stmts;
    result[C.RETURN_TYPE] = returnType;
    result[C.RETURN] = return_;
    return [result];
}

export function createIfReturn( condition, returnType, return_ ) {
    var result = {};
    result[C.OPCODE] = C.IF_RETURN;
    result[C.RETURN_TYPE] = returnType;
    result[C.RETURN] = return_;
    return push( condition, result );
}

export function createMethodCallVoid( methodName, parameters ) {
    var result = {};
    result[C.OPCODE] = C.METHOD_CALL_VOID;
    result[C.NAME] = methodName;
    result[C.PARAMETERS] = parameters;
    return [result];
}

export function createMethodCallReturn( methodName, parameters, values ) {
    var result = {};
    result[C.EXPR] = C.METHOD_CALL_RETURN;
    result[C.NAME] = methodName;
    result[C.PARAMETERS] = parameters;
    result[C.VALUES] = values;
    return [result];
}

export function createNoopStmt() {
    var result = {};
    result[C.OPCODE] = C.NOOP_STMT;
    return [result];
}

export function concat( ...codes: any[] ) {
    let first = codes.shift();
    return first.concat( ...codes );
}

export function push( code: any[], op: any ) {
    return concat( code, [op] );
}