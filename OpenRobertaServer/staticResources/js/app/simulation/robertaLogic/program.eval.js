/**
 * Interpreter of program that is running in the simulation. This interpreter
 * reads every statement of the program and gives command to the simulation what
 * the robot should do.
 */
define([ 'robertaLogic.actors', 'robertaLogic.memory', 'robertaLogic.program', 'util', 'robertaLogic.constants' ], function(Actors, Memory, Program, UTIL,
        CONSTANTS) {
    var privateMem = new WeakMap();

    var internal = function(object) {
        if (!privateMem.has(object)) {
            privateMem.set(object, {});
        }
        return privateMem.get(object);
    }

    var ProgramEval = function() {
        internal(this).program = new Program();
        internal(this).memory = new Memory();
        internal(this).actors = new Actors();
        internal(this).simulationData = {};
        internal(this).outputCommands = {};
    };

    ProgramEval.prototype.getProgram = function() {
        return internal(this).program;
    };

    /**
     * Initialize the program that is executed in the simulation.
     * 
     * @param program
     *            {Object} - list of statements representing the program
     */
    ProgramEval.prototype.initProgram = function(program) {
        internal(this).memory.clear();
        internal(this).program.setNextStatement(true);
        internal(this).program.setWait(false);
        internal(this).program.set(program);
        internal(this).actors.resetMotorsSpeed();
    };

    /**
     * Function that executes one step of the program.
     * 
     * @param simulationData
     *            {Object} - sensor data from the simulation
     */
    ProgramEval.prototype.step = function(simulationData) {
        internal(this).outputCommands = {};
        setSensorActorValues(internal(this), simulationData);
        if (internal(this).program.isNextStatement()) {
            var stmt = internal(this).program.getRemove();
            switch (stmt.stmt) {
            case CONSTANTS.ASSIGN_STMT:
                evalAssignmentStmt(internal(this), stmt);
                break;

            case CONSTANTS.VAR_DECLARATION:
                evalVarDeclaration(internal(this), stmt);
                break;

            case CONSTANTS.IF_STMT:
                evalIf(internal(this), stmt);
                this.step(simulationData);
                break;

            case CONSTANTS.REPEAT_STMT:
                evalRepeat(internal(this), stmt);
                break;

            case CONSTANTS.DRIVE_ACTION:
                evalDriveAction(internal(this), stmt);
                break;

            case CONSTANTS.TURN_ACTION:
                evalTurnAction(internal(this), stmt);
                break;

            case CONSTANTS.MOTOR_ON_ACTION:
                evalMotorOnAction(internal(this), stmt);
                break;

            case CONSTANTS.SHOW_PICTURE_ACTION:
                evalShowPictureAction(internal(this), stmt);
                break;

            case CONSTANTS.SHOW_TEXT_ACTION:
                evalShowTextAction(internal(this), stmt);
                break;

            case CONSTANTS.CLEAR_DISPLAY_ACTION:
                evalClearDisplayAction(internal(this), stmt);
                break;

            case CONSTANTS.CREATE_DEBUG_ACTION:
                internal(this).outputCommands.debug = true;
                break;

            case CONSTANTS.WAIT_STMT:
                evalWaitStmt(internal(this), stmt);
                break;

            case CONSTANTS.WAIT_TIME_STMT:
                evalWaitTime(internal(this), simulationData, stmt);
                break;

            case CONSTANTS.TURN_LIGHT:
                evalLedOnAction(internal(this), simulationData, stmt);
                break;

            case CONSTANTS.STOP_DRIVE:
                internal(this).actors.setSpeed(0);
                break;

            case CONSTANTS.MOTOR_STOP:
                evalMotorStopAction(internal(this), stmt);
                break;

            case CONSTANTS.MOTOR_SET_POWER:
                evalMotorSetPowerAction(internal(this), stmt);
                break;

            case CONSTANTS.STATUS_LIGHT_ACTION:
                evalLedStatusAction(internal(this), stmt);
                break;

            case CONSTANTS.ENCODER_SENSOR_RESET:
                evalResetEncoderSensor(internal(this), stmt);
                break;

            case CONSTANTS.GYRO_SENSOR_RESET:
                evalResetGyroSensor(internal(this), stmt);
                break;

            case CONSTANTS.TIMER_SENSOR_RESET:
                evalResetTimerSensor(internal(this), stmt);
                break;

            case CONSTANTS.TONE_ACTION:
                evalToneAction(internal(this), simulationData, stmt);
                break;

            case CONSTANTS.PLAY_FILE_ACTION:
                evalPlayFileAction(internal(this), simulationData, stmt);
                break;

            case CONSTANTS.SET_VOLUME_ACTION:
                evalVolumeAction(internal(this), stmt);
                break;

            case CONSTANTS.CREATE_LISTS_GET_INDEX_STMT:
                evalListsGetIndexStmt(internal(this), stmt);
                break;
            case CONSTANTS.CREATE_LISTS_SET_INDEX:
                evalListsSetIndex(internal(this), stmt);
                break;
            case CONSTANTS.METHOD_CALL_VOID:
                evalMethodCallVoid(internal(this), stmt);
                break;

            default:
                throw "Invalid Statement " + stmt.stmt + "!";
            }
        }
        newSpeeds = internal(this).actors.checkCoveredDistanceAndCorrectSpeed(internal(this).program, internal(this).simulationData.correctDrive);
        internal(this).program.handleWaitTimer();
        outputSpeeds(internal(this), newSpeeds);
        // internal(this).outputCommands.terminated = internal(this).program.isTerminated();
        return internal(this).outputCommands;

    };

    var evalVarDeclaration = function(obj, stmt) {
        var value = evalExpr(obj, stmt.value);
        obj.memory.decl(stmt.name, value);
    };

    var evalAssignmentStmt = function(obj, stmt) {
        var value = evalExpr(obj, stmt.expr);
        obj.memory.assign(stmt.name, value);
    };

    var setSensorActorValues = function(obj, simulationData) {
        obj.simulationData = simulationData;
        obj.actors.getLeftMotor().setCurrentRotations(simulationData.encoder.left);
        obj.actors.getRightMotor().setCurrentRotations(simulationData.encoder.right);
        obj.program.getTimer().setCurrentTime(simulationData.time);
        obj.program.setNextFrameTimeDuration(simulationData.frameTime);
    };

    var outputSpeeds = function(obj, speeds) {
        obj.outputCommands.motors = {};
        obj.outputCommands.motors.powerLeft = obj.actors.getLeftMotor().getPower();
        obj.outputCommands.motors.powerRight = obj.actors.getRightMotor().getPower();
        if (speeds.left) {
            obj.outputCommands.motors.powerLeft = speeds.left;
        }
        if (speeds.right) {
            obj.outputCommands.motors.powerRight = speeds.right;
        }
    };

    var evalResetEncoderSensor = function(obj, stmt) {
        obj.outputCommands.encoder = {};
        if (stmt[CONSTANTS.MOTOR_SIDE] == CONSTANTS.MOTOR_LEFT) {
            obj.outputCommands.encoder.leftReset = true;
        } else {
            obj.outputCommands.encoder.rightReset = true;
        }
    };

    var evalResetGyroSensor = function(obj, stmt) {
        obj.outputCommands.gyroReset = true;
    };

    var evalResetTimerSensor = function(obj, stmt) {
        obj.outputCommands.timer = {};
        obj.outputCommands.timer[stmt.timer] = 'reset';
    };

    var evalWaitTime = function(obj, simulationData, stmt) {
        obj.program.setIsRunningTimer(true);
        obj.program.resetTimer(simulationData.time);
        obj.program.setTimer(evalExpr(obj, stmt.time));
    };

    var evalLedOnAction = function(obj, simulationData, stmt) {
        obj.outputCommands.led = {}
        obj.outputCommands.led.color = stmt.color;
        obj.outputCommands.led.mode = stmt.mode;
    };

    var evalLedStatusAction = function(obj, stmt) {
        obj.outputCommands.led = {}
        if (stmt.mode == CONSTANTS.RESET) {
            obj.outputCommands.led.color = '';
        }
        obj.outputCommands.led.mode = CONSTANTS.OFF;
    };

    var evalShowPictureAction = function(obj, stmt) {
        obj.outputCommands.display = {};
        obj.outputCommands.display.picture = stmt.picture;
        obj.outputCommands.display.x = evalExpr(obj, stmt.x);
        obj.outputCommands.display.y = evalExpr(obj, stmt.y);
    };

    var evalShowTextAction = function(obj, stmt) {
        obj.outputCommands.display = {};
        var val = evalExpr(obj, stmt.text);
        obj.outputCommands.display.text = String(roundIfSensorData(val, stmt.text.expr));
        obj.outputCommands.display.x = evalExpr(obj, stmt.x);
        obj.outputCommands.display.y = evalExpr(obj, stmt.y);
    };

    var roundIfSensorData = function(val, exprType) {
        if ((exprType == CONSTANTS.GET_SAMPLE || exprType == CONSTANTS.ENCODER_SENSOR_SAMPLE) && isNumber(val)) {
            val = UTIL.round(val, 2);
        }
        return val;
    };

    var evalClearDisplayAction = function(obj, stmt) {
        obj.outputCommands.display = {};
        obj.outputCommands.display.clear = true;

    };

    var evalToneAction = function(obj, simulationData, stmt) {
        obj.program.setIsRunningTimer(true);
        obj.program.resetTimer(simulationData.time);
        obj.program.setTimer(evalExpr(obj, stmt.duration));
        obj.outputCommands.tone = {};
        obj.outputCommands.tone.frequency = evalExpr(obj, stmt.frequency);
        obj.outputCommands.tone.duration = evalExpr(obj, stmt.duration);
    };

    var evalPlayFileAction = function(obj, simulationData, stmt) {
        obj.program.setIsRunningTimer(true);
        obj.program.resetTimer(simulationData.time);
        var duration = 0; // ms
        switch (stmt.file) {
        case 0:
            duration = 1000;
            break;
        case 1:
            duration = 350;
            break;
        case 2:
            duration = 700;
            break;
        case 3:
            duration = 700;
            break;
        case 4:
            duration = 500;
            break;
        }
        obj.program.setTimer(duration);
        obj.outputCommands.tone = {};
        obj.outputCommands.tone.file = stmt.file;
    };

    var evalVolumeAction = function(obj, stmt) {
        obj.outputCommands.volume = evalExpr(obj, stmt.volume);
    };

    var evalMethodCallVoid = function(obj, stmt) {
        var methodName = stmt.name;
        var method = obj.program.getMethod(methodName);
        for (var i = 0; i < stmt.parameters.length; i++) {
            var parameter = stmt.parameters[i];
            var value = stmt.values[i]
            if (obj.memory.get(parameter.name) == undefined) {
                obj.memory.decl(parameter.name, evalExpr(obj, value))
            } else {
                obj.memory.assign(parameter.name, evalExpr(obj, value));
            }
        }
        obj.program.prepend(method.stmtList);
    };

    var evalMethodCallReturn = function(obj, name, paramters, values) {
//        var method = obj.program.getMethod(name);
//        for (var i = 0; i < parameters.length; i++) {
//            var parameter = parameters[i];
//            var value = values[i]
//            if (obj.memory.get(parameter.name) == undefined) {
//                obj.memory.decl(parameter.name, evalExpr(obj, value))
//            } else {
//                obj.memory.assign(parameter.name, evalExpr(obj, value));
//            }
//        }
    };

    var evalTurnAction = function(obj, stmt) {
        obj.actors.initTachoMotors(obj.simulationData.encoder.left, obj.simulationData.encoder.right);
        obj.actors.setAngleSpeed(evalExpr(obj, stmt.speed), stmt[CONSTANTS.TURN_DIRECTION]);
        setAngleToTurn(obj, stmt);
    };

    var evalDriveAction = function(obj, stmt) {
        obj.actors.initTachoMotors(obj.simulationData.encoder.left, obj.simulationData.encoder.right);
        obj.actors.setSpeed(evalExpr(obj, stmt.speed), stmt[CONSTANTS.DRIVE_DIRECTION]);
        setDistanceToDrive(obj, stmt);
    };

    var evalMotorOnAction = function(obj, stmt) {
        if (stmt[CONSTANTS.MOTOR_SIDE] == CONSTANTS.MOTOR_LEFT) {
            obj.actors.initLeftTachoMotor(obj.simulationData.encoder.left);
            obj.actors.setLeftMotorSpeed(evalExpr(obj, stmt.speed));
        } else {
            obj.actors.initRightTachoMotor(obj.simulationData.encoder.right);
            obj.actors.setRightMotorSpeed(evalExpr(obj, stmt.speed));
        }
        setDurationToCover(obj, stmt);
    };

    var evalMotorSetPowerAction = function(obj, stmt) {
        if (stmt[CONSTANTS.MOTOR_SIDE] == CONSTANTS.MOTOR_LEFT) {
            obj.actors.setLeftMotorSpeed(evalExpr(obj, stmt.speed));
        } else {
            obj.actors.setRightMotorSpeed(evalExpr(obj, stmt.speed));
        }
    };

    var evalMotorStopAction = function(obj, stmt) {
        if (stmt[CONSTANTS.MOTOR_SIDE] == CONSTANTS.MOTOR_LEFT) {
            obj.actors.setLeftMotorSpeed(0);
        } else {
            obj.actors.setRightMotorSpeed(0);
        }
    };

    var evalMotorGetPowerAction = function(obj, motorSide) {
        if (motorSide == CONSTANTS.MOTOR_LEFT) {
            return obj.actors.getLeftMotor().getPower();
        } else {
            return obj.actors.getRightMotor().getPower();
        }
    };

    var setAngleToTurn = function(obj, stmt) {
        if (stmt.angle) {
            obj.actors.calculateAngleToCover(obj.program, evalExpr(obj, stmt.angle));
        }
    };

    var setDistanceToDrive = function(obj, stmt) {
        if (stmt.distance) {
            obj.actors.setDistanceToCover(obj.program, evalExpr(obj, stmt.distance));
        }
    };

    var setDurationToCover = function(obj, stmt) {
        if (stmt[CONSTANTS.MOTOR_DURATION]) {
            obj.actors.setMotorDuration(obj.program, (stmt[CONSTANTS.MOTOR_DURATION]).motorMoveMode, evalExpr(obj,
                    (stmt[CONSTANTS.MOTOR_DURATION]).motorDurationValue), stmt[CONSTANTS.MOTOR_SIDE]);
        }
    };

    var evalRepeat = function(obj, stmt) {
        switch (stmt.mode) {
        case CONSTANTS.TIMES:
            for (var i = 0; i < evalExpr(obj, stmt.expr); i++) {
                obj.program.prepend(stmt.stmtList);
            }
            break;
        case CONSTANTS.FOR_EACH:
            var i = stmt.eachCounter++;
            if (i == 0) {
                evalVarDeclaration(obj, stmt.expr.left);
            }
            var list = evalExpr(obj, stmt.expr.right);
            if (i < list.length) {
                obj.memory.assign(stmt.expr.left.name, list[i]);
                obj.program.prepend([ stmt ])
                obj.program.prepend(stmt.stmtList);
            }
            break;
        case CONSTANTS.FOR:
            if (obj.memory.get(stmt.expr[0].name) == undefined) {
                obj.memory.decl(stmt.expr[0].name, evalExpr(obj, stmt.expr[1]))
            } else {
                var step = evalExpr(obj, stmt.expr[3]);
                var oldValue = obj.memory.get(stmt.expr[0].name);
                obj.memory.assign(stmt.expr[0].name, oldValue + step);
            }
            var left = obj.memory.get(stmt.expr[0].name);
            var right = evalExpr(obj, stmt.expr[2]);
            if (left <= right) {
                obj.program.prepend([ stmt ]);
                obj.program.prepend(stmt.stmtList);
            }
            break;
        default:
            var value = evalExpr(obj, stmt.expr);
            if (value) {
                obj.program.prepend([ stmt ]);
                obj.program.prepend(stmt.stmtList);
            }
        }
    };

    var evalIf = function(obj, stmt) {
        var programPrefix;
        var value;
        for (var i = 0; i < stmt.exprList.length; i++) {
            value = evalExpr(obj, stmt.exprList[i]);
            if (value) {
                programPrefix = stmt.thenList[i];
                if (obj.program.isWait()) {
                    obj.program.getRemove();
                    obj.program.setWait(false);
                }
                break;
            }
        }
        if ((programPrefix == undefined || programPrefix == []) && !obj.program.isWait()) {
            programPrefix = stmt.elseStmts;
        }
        obj.program.prepend(programPrefix);
        return value;
    };

    var evalWaitStmt = function(obj, stmt) {
        obj.program.setWait(true);
        obj.program.prepend([ stmt ]);
        for (var i = 0; i < stmt.statements.length; i++) {
            var value = evalIf(obj, stmt.statements[i]);
            if (value) {
                break;
            }
        }
    };

    var evalExpr = function(obj, expr) {
        switch (expr.expr) {
        case CONSTANTS.NUM_CONST:
        case CONSTANTS.BOOL_CONST:
        case CONSTANTS.COLOR_CONST:
        case CONSTANTS.STRING_CONST:
            return expr.value;
        case CONSTANTS.NULL_CONST:
            return null;
        case CONSTANTS.ARRAY_NUMBER:
        case CONSTANTS.ARRAY_STRING:
        case CONSTANTS.ARRAY_COLOUR:
        case CONSTANTS.ARRAY_BOOLEAN:
            return evalArray(obj, expr.value);
        case CONSTANTS.CREATE_LIST_WITH_ITEM:
            return evalCreateArrayWithItem(obj, expr.size, expr.value);
        case CONSTANTS.CREATE_LIST_LENGTH:
            return evalListLength(obj, expr.list);
        case CONSTANTS.CREATE_LIST_IS_EMPTY:
            return evalListIsEmpty(obj, expr.list);
        case CONSTANTS.CREATE_LIST_FIND_ITEM:
            return evalListFindItem(obj, expr.position, expr.list, expr.item);
        case CONSTANTS.CREATE_LISTS_GET_INDEX:
            return evalListsGetIndex(obj, expr.list, expr.op, expr.position, expr.item);
        case CONSTANTS.TEXT_JOIN:
            return evalTextJoin(obj, expr.value);
        case CONSTANTS.CREATE_LISTS_GET_SUBLIST:
            return evalListsGetSubList(obj, expr);
        case CONSTANTS.VAR:
            return obj.memory.get(expr.name);
        case CONSTANTS.BINARY:
            return evalBinary(obj, expr.op, expr.left, expr.right);
        case CONSTANTS.UNARY:
            return evalUnary(obj, expr.op, expr.value);
        case CONSTANTS.TERNARY_EXPR:
            return evalTernaryExpr(obj, expr.exprList, expr.thenList, expr.elseStmts);
        case CONSTANTS.SINGLE_FUNCTION:
            return evalSingleFunction(obj, expr.op, expr.value);
        case CONSTANTS.RANDOM_INT:
            return evalRandInt(obj, expr.min, expr.max);
        case CONSTANTS.RANDOM_DOUBLE:
            return evalRandDouble();
        case CONSTANTS.MATH_CONSTRAIN_FUNCTION:
            return evalMathPropFunct(obj, expr.value, expr.min, expr.max);
        case CONSTANTS.MATH_ON_LIST:
            return evalMathOnList(obj, expr.op, expr.list);
        case CONSTANTS.MATH_PROP_FUNCT:
            return evalMathPropFunct(obj, expr.op, expr.arg1, expr.arg2);
        case CONSTANTS.MATH_CONST:
            return evalMathConst(obj, expr.value);
        case CONSTANTS.GET_SAMPLE:
            return evalSensor(obj, expr.sensorType, expr.sensorMode);
        case CONSTANTS.ENCODER_SENSOR_SAMPLE:
            return evalEncoderSensor(obj, expr.motorSide, expr.sensorMode);
        case CONSTANTS.MOTOR_GET_POWER:
            return evalMotorGetPowerAction(obj, expr.motorSide);
        case CONSTANTS.GET_VOLUME:
            return evalGetVolume(obj);
        case CONSTANTS.METHOD_CALL_RETURN:
            return evalMethodCallReturn(obj, expr.name, expr.parameters, expr.values);
        default:
            throw "Invalid Expression Type!";
        }
    };

    var evalSensor = function(obj, sensorType, sensorMode) {
        if (sensorMode) {
            return obj.simulationData[sensorType][sensorMode];
        }
        return obj.simulationData[sensorType];
    };

    var evalEncoderSensor = function(obj, motorSide, sensorMode) {
        var value = obj.simulationData.encoder.right / 360.0;
        if (motorSide == CONSTANTS.MOTOR_LEFT) {
            value = obj.simulationData.encoder.left / 360.0;
        }
        switch (sensorMode) {
        case CONSTANTS.ROTATION:
            return value;
        case CONSTANTS.DEGREE:
            return value * 360.;
        case CONSTANTS.DISTANCE:
            return value * (CONSTANTS.WHEEL_DIAMETER * 3.14);
        default:
            throw "Invalid Encoder Mode!";
        }
    };

    var evalGetVolume = function(obj) {
        return obj.simulationData[CONSTANTS.VOLUME];
    };

    var evalBinary = function(obj, op, left, right) {
        var valLeft = evalExpr(obj, left);
        var valRight = evalExpr(obj, right);
        var val;
        switch (op) {
        case CONSTANTS.ADD:
            val = valLeft + valRight;
            break;
        case CONSTANTS.MINUS:
            val = valLeft - valRight;
            break;
        case CONSTANTS.MULTIPLY:
            val = valLeft * valRight;
            break;
        case CONSTANTS.DIVIDE:
            val = valLeft / valRight;
            break;
        case CONSTANTS.POWER:
            val = Math.pow(valLeft, valRight);
            break;
        case CONSTANTS.TEXT_APPEND:
            valLeft = isNumber(valLeft) ? UTIL.round(valLeft, 2) : valLeft
            valRight = isNumber(valRight) ? UTIL.round(valRight, 2) : valRight
            val = String(valLeft) + String(valRight);
            break;
        case CONSTANTS.LT:
            val = valLeft < valRight;
            break;
        case CONSTANTS.GT:
            val = valLeft > valRight;
            break;
        case CONSTANTS.EQ:
            val = valLeft == valRight;
            break;
        case CONSTANTS.NEQ:
            val = valLeft != valRight;
            break;
        case CONSTANTS.GTE:
            val = valLeft >= valRight;
            break;
        case CONSTANTS.LTE:
            val = valLeft <= valRight;
            break;
        case CONSTANTS.OR:
            val = valLeft || valRight;
            break;
        case CONSTANTS.AND:
            val = valLeft && valRight;
            break;
        case CONSTANTS.MOD:
            val = valLeft % valRight;
            break;
        default:
            throw "Invalid Binary Operator";
        }
        return val;
    };

    var evalUnary = function(obj, op, value) {
        var val = evalExpr(obj, value);
        switch (op) {
        case CONSTANTS.NEG:
            return -val;
        case CONSTANTS.NOT:
            return !val;
        default:
            throw "Invalid Unary Operator";
        }
    };

    var evalSingleFunction = function(obj, functName, value) {
        var val = evalExpr(obj, value);
        switch (functName) {
        case 'ROOT':
            return Math.sqrt(val);
        case 'ABS':
            return Math.abs(val);
        case 'LN':
            return Math.log(val);
        case 'LOG10':
            return Math.log10(val);
        case 'EXP':
            return Math.exp(val);
        case 'POW10':
            return Math.pow(10, val);
        case 'SIN':
            return Math.sin(val);
        case 'COS':
            return Math.cos(val);
        case 'TAN':
            return Math.tan(val);
        case 'ASIN':
            return Math.asin(val);
        case 'ATAN':
            return Math.atan(val);
        case 'ACOS':
            return Math.acos(val);
        case 'ROUND':
            return Math.round(val);
        case 'ROUNDUP':
            return Math.ceil(val);
        case 'ROUNDDOWN':
            return Math.floor(val);
        default:
            throw "Invalid Function Name";
        }
    };

    var evalMathConst = function(obj, mathConst) {
        switch (mathConst) {
        case 'PI':
            return Math.PI;
        case 'E':
            return Math.E;
        case 'GOLDEN_RATIO':
            return (1.0 + Math.sqrt(5.0)) / 2.0;
        case 'SQRT2':
            return Math.SQRT2;
        case 'SQRT1_2':
            return Math.SQRT1_2;
        case 'INFINITY':
            return Infinity;
        default:
            throw "Invalid Math Constant Name";
        }
    };

    var evalMathOnList = function(obj, op, list) {
        var listVal = evalExpr(obj, list);
        switch (op) {
        case SUM:
            return listVal.reduce(function(x, y) {
                return x + y;
            });
        case CONSTANTS.MIN:
            return Math.min.apply(null, listVal);
        case CONSTANTS.MAX:
            return Math.max.apply(null, listVal);
        case CONSTANTS.AVERAGE:
            return mathMean(listVal);
        case CONSTANTS.MEDIAN:
            return mathMedian(listVal);
        case CONSTANTS.STD_DEV:
            return mathStandardDeviation(listVal);
        case CONSTANTS.RANDOM:
            return mathRandomList(listVal);
        default:
            throw "Invalid Matematical Operation On List";
        }
    };

    var evalMathPropFunct = function(obj, val, min, max) {
        var val_ = evalExpr(obj, val);
        var min_ = evalExpr(obj, min);
        var max_ = evalExpr(obj, max);
        return Math.min(Math.max(val_, min_), max_);
    };

    var evalMathConstrainFunct = function(obj, val, min, max) {
        var val1 = evalExpr(obj, arg1);
        if (arg2) {
            var val2 = evalExpr(obj, arg2);
        }
        switch (functName) {
        case 'EVEN':
            return val1 % 2 == 0;
        case 'ODD':
            return val1 % 2 != 0;
        case 'PRIME':
            return isPrime(val1);
        case 'WHOLE':
            return Number(val1) === val1 && val1 % 1 === 0;
        case 'POSITIVE':
            return val1 >= 0;
        case 'NEGATIVE':
            return val1 < 0;
        case 'DIVISIBLE_BY':
            return val1 % val2 == 0;
        default:
            throw "Invalid Math Property Function Name";
        }
    };

    function evalRandInt(obj, min, max) {
        min_ = evalExpr(obj, min);
        max_ = evalExpr(obj, max)
        return math_random_int(min_, max_);
    }

    var evalRandDouble = function() {
        return Math.random();
    };

    var evalArray = function(obj, values) {
        var result = [];
        for (var i = 0; i < values.length; i++) {
            result.push(evalExpr(obj, values[i]));
        }
        return result;
    };

    var evalCreateArrayWithItem = function(obj, length, value) {
        var size = evalExpr(obj, length);
        var val = evalExpr(obj, value);
        return Array(size).fill(val);
    };

    var evalListLength = function(obj, value) {
        var val = evalExpr(obj, value);
        return val.length;
    };

    var evalListIsEmpty = function(obj, value) {
        var val = evalExpr(obj, value);
        return val.length == 0;
    };

    var evalListFindItem = function(obj, position, value, item) {
        var list = evalExpr(obj, value);
        var ite = evalExpr(obj, item);
        if (position == FIRST) {
            return list.indexOf(ite);
        }
        return list.lastIndexOf(ite);
    };

    var evalListsGetIndex = function(obj, list, op, position, item) {
        var list = evalExpr(obj, list);
        var it;
        if (item) {
            it = evalExpr(obj, item);
        }
        var remove = op == CONSTANTS.GET_REMOVE;
        switch (position) {
        case CONSTANTS.FROM_START:
            if (remove) {
                return list.splice(it, 1)[0];
            }
            return list[it];
        case CONSTANTS.FROM_END:
            if (remove) {
                return listsRemoveFromEnd(list, it);
            }
            return list.slice(-(it + 1))[0];
        case CONSTANTS.FIRST:
            if (remove) {
                return list.shift();
            }
            return list[0];
        case CONSTANTS.LAST:
            if (remove) {
                return list.pop();
            }
            return list.slice(-1)[0];
        case CONSTANTS.RANDOM:
            return listsGetRandomItem(list, remove);
        default:
            throw "Position on list is not supported!";
        }
    };

    var evalListsGetIndexStmt = function(obj, stmt) {
        var list = evalExpr(obj, stmt.list);
        var it;
        if (stmt.item) {
            it = evalExpr(obj, stmt.item);
        }

        switch (stmt.position) {
        case CONSTANTS.FROM_START:
            list.splice(it, 1);
            break;
        case CONSTANTS.FROM_END:
            listsRemoveFromEnd(list, it);
            break;
        case CONSTANTS.FIRST:
            list.shift();
            break;
        case CONSTANTS.LAST:
            list.pop();
            break;
        case CONSTANTS.RANDOM:
            listsGetRandomItem(list, true);
            break;
        default:
            throw "Position on list is not supported!";
        }
    };

    var evalListsSetIndex = function(obj, stmt) {
        var list = evalExpr(obj, stmt.list);
        var it;
        if (stmt.item) {
            it = evalExpr(obj, stmt.item);
        }
        var newValue = stmt.value;
        var insert = op == CONSTANTS.INSERT;
        switch (stmt.position) {
        case CONSTANTS.FROM_START:
            if (insert) {
                list.splice(it, 0, newValue)
                break;
            }
            list[it] = newValue;
            break;
        case CONSTANTS.FROM_END:
            if (insert) {
                list.splice(list.length - it - 1, 0, newValue)
                break;
            }
            list[list.length - it - 1] = newValue;
            break;
        case CONSTANTS.FIRST:
            if (insert) {
                list.unshift(newValue)
                break;
            }
            list[0] = newValue;
            break;
        case CONSTANTS.LAST:
            if (insert) {
                list.push(newValue)
                break;
            }
            list[list.length - 1] = newValue;
            break;
        case CONSTANTS.RANDOM:
            var tmp_x = Math.floor(Math.random() * list.length);
            if (insert) {
                list.splice(tmp_x, 0, newValue)
                break;
            }
            list[tmp_x] = newValue;
            break;
        default:
            throw "Position on list is not supported!";
        }
    };

    var evalListsGetSubList = function(obj, expr) {
        var list = evalExpr(obj, expr.list);
        var at1 = 1;
        if (expr.at1) {
            at1 = evalExpr(obj, expr.at1);
        }
        var at2 = 1;
        if (expr.at2) {
            at2 = evalExpr(obj, expr.at2);
        }
        if (expr.where1 == CONSTANTS.FIRST && expr.where2 == CONSTANTS.LAST) {
            return list.concat();
        }
        return listsGetSubList(list, expr.where1, at1, expr.where2, at2);
    };

    var evalTernaryExpr = function(obj, cond, then, _else) {
        var condVal = evalExpr(obj, cond);
        if (condVal) {
            return evalExpr(obj, then);
        }
        return evalExpr(obj, _else);
    };

    var isPrime = function(n) {
        if (isNaN(n) || !isFinite(n) || n % 1 || n < 2) {
            return false;
        }
        if (n == leastFactor(n)) {
            return true;
        }
        return false;
    };

    var listsRemoveFromEnd = function(list, x) {
        x = list.length - x;
        return list.splice(x, 1)[0];
    };

    var listsGetRandomItem = function(list, remove) {
        var x = Math.floor(Math.random() * list.length);
        if (remove) {
            return list.splice(x, 1)[0];
        } else {
            return list[x];
        }
    };

    var listsGetSubList = function(list, where1, at1, where2, at2) {
        function getAt(where, at) {
            if (where == CONSTANTS.FROM_START) {
                at = at
            } else if (where == FROM_END) {
                at = list.length - 1 - at;
            } else if (where == FIRST) {
                at = 0;
            } else if (where == LAST) {
                at = list.length - 1;
            } else {
                throw 'Unhandled option (lists_getSublist).';
            }
            return at;
        }
        at1 = getAt(where1, at1);
        at2 = getAt(where2, at2) + 1;
        return list.slice(at1, at2);
    };

    var evalTextJoin = function(obj, values) {
        var result = "";
        for (var i = 0; i < values.length; i++) {
            var val = evalExpr(obj, values[i]);
            val = roundIfSensorData(val, values[i].expr)
            result += String(val);
        }
        return result;
    };

    var leastFactor = function(n) {
        if (isNaN(n) || !isFinite(n)) {
            return NaN;
        }
        if (n == 0) {
            return 0;
        }
        if (n % 1 || n * n < 2) {
            return 1;
        }
        if (n % 2 == 0) {
            return 2;
        }
        if (n % 3 == 0) {
            return 3;
        }
        if (n % 5 == 0) {
            return 5;
        }
        var m = Math.sqrt(n);
        for (var i = 7; i <= m; i += 30) {
            if (n % i == 0) {
                return i;
            }
            if (n % (i + 4) == 0) {
                return i + 4;
            }
            if (n % (i + 6) == 0) {
                return i + 6;
            }
            if (n % (i + 10) == 0) {
                return i + 10;
            }
            if (n % (i + 12) == 0) {
                return i + 12;
            }
            if (n % (i + 16) == 0) {
                return i + 16;
            }
            if (n % (i + 22) == 0) {
                return i + 22;
            }
            if (n % (i + 24) == 0) {
                return i + 24;
            }
        }
        return n;
    };

    var math_random_int = function(a, b) {
        if (a > b) {
            // Swap a and b to ensure a is smaller.
            var c = a;
            a = b;
            b = c;
        }
        return Math.floor(Math.random() * (b - a + 1) + a);
    };

    var mathMean = function(myList) {
        return myList.reduce(function(x, y) {
            return x + y;
        }) / myList.length;
    };

    var mathMedian = function(myList) {
        var localList = myList.filter(function(x) {
            return typeof x == 'number';
        });
        if (!localList.length) {
            return null;
        }
        localList.sort(function(a, b) {
            return b - a;
        });
        if (localList.length % 2 == 0) {
            return (localList[localList.length / 2 - 1] + localList[localList.length / 2]) / 2;
        } else {
            return localList[(localList.length - 1) / 2];
        }
    };

    var mathStandardDeviation = function(numbers) {
        var n = numbers.length;
        if (!n) {
            return null;
        }
        var mean = numbers.reduce(function(x, y) {
            return x + y;
        }) / n;
        var variance = 0;
        for (var j = 0; j < n; j++) {
            variance += Math.pow(numbers[j] - mean, 2);
        }
        variance = variance / n;
        return Math.sqrt(variance);
    };

    var mathRandomList = function(list) {
        var x = Math.floor(Math.random() * list.length);
        return list[x];
    };

    var isNumber = function(n) {
        return !isNaN(parseFloat(n)) && isFinite(n);
    };

    return ProgramEval;
});
