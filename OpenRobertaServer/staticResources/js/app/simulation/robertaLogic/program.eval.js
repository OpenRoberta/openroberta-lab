/**
 * Interpreter of program that is running in the simulation. This interpreter
 * reads every statement of the program and gives command to the simulation what
 * the robot should do.
 */
define([ 'robertaLogic.actors', 'robertaLogic.memory', 'robertaLogic.program' ], function(Actors, Memory, Program) {
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
            case ASSIGN_STMT:
                var value = evalExpr(internal(this), stmt.expr);
                internal(this).memory.assign(stmt.name, value);
                break;

            case VAR_DECLARATION:
                var value = evalExpr(internal(this), stmt.value);
                internal(this).memory.decl(stmt.name, value);
                break;

            case MATH_CHANGE:
                evalMathChange(internal(this), stmt);
                break;

            case IF_STMT:
                evalIf(internal(this), stmt);
                this.step(simulationData);
                break;

            case REPEAT_STMT:
                evalRepeat(internal(this), stmt);
                break;

            case DRIVE_ACTION:
                evalDriveAction(internal(this), stmt);
                break;

            case TURN_ACTION:
                evalTurnAction(internal(this), stmt);
                break;

            case MOTOR_ON_ACTION:
                evalMotorOnAction(internal(this), stmt);
                break;

            case SHOW_PICTURE_ACTION:
                evalShowPictureAction(internal(this), stmt);
                break;

            case SHOW_TEXT_ACTION:
                evalShowTextAction(internal(this), stmt);
                break;

            case CLEAR_DISPLAY_ACTION:
                evalClearDisplayAction(internal(this), stmt);
                break;

            case CREATE_DEBUG_ACTION:
                internal(this).outputCommands.debug = true;
                break;

            case WAIT_STMT:
                evalWaitStmt(internal(this), stmt);
                break;

            case WAIT_TIME_STMT:
                evalWaitTime(internal(this), simulationData, stmt);
                break;

            case TURN_LIGHT:
                evalLedOnAction(internal(this), simulationData, stmt);
                break;

            case STOP_DRIVE:
                internal(this).actors.setSpeed(0);
                break;

            case MOTOR_STOP:
                evalMotorStopAction(internal(this), stmt);
                break;

            case MOTOR_SET_POWER:
                evalMotorSetPowerAction(internal(this), stmt);
                break;

            case STATUS_LIGHT_ACTION:
                evalLedStatusAction(internal(this), stmt);
                break;

            case ENCODER_SENSOR_RESET:
                evalResetEncoderSensor(internal(this), stmt);
                break;

            case GYRO_SENSOR_RESET:
                evalResetGyroSensor(internal(this), stmt);
                break;

            case TIMER_SENSOR_RESET:
                evalResetTimerSensor(internal(this), stmt);
                break;

            case CREATE_LISTS_GET_INDEX_STMT:
                evalListsGetIndexStmt(internal(this), stmt);
                break;
            case CREATE_LISTS_SET_INDEX:
                evalListsSetIndex(internal(this), stmt);
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
        if (stmt[MOTOR_SIDE] == MOTOR_LEFT) {
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
        if (stmt.mode == RESET) {
            obj.outputCommands.led.color = '';
        }
        obj.outputCommands.led.mode = OFF;
    };

    var evalShowPictureAction = function(obj, stmt) {
        obj.outputCommands.display = {};
        obj.outputCommands.display.picture = stmt.picture;
        obj.outputCommands.display.x = evalExpr(obj, stmt.x);
        obj.outputCommands.display.y = evalExpr(obj, stmt.y);
    };

    var evalShowTextAction = function(obj, stmt) {
        obj.outputCommands.display = {};
        obj.outputCommands.display.text = String(evalExpr(obj, stmt.text));
        obj.outputCommands.display.x = evalExpr(obj, stmt.x);
        obj.outputCommands.display.y = evalExpr(obj, stmt.y);
    };

    var evalClearDisplayAction = function(obj, stmt) {
        obj.outputCommands.display = {};
        obj.outputCommands.display.clear = true;

    };

    var evalTurnAction = function(obj, stmt) {
        obj.actors.initTachoMotors(obj.simulationData.encoder.left, obj.simulationData.encoder.right);
        obj.actors.setAngleSpeed(evalExpr(obj, stmt.speed), stmt[TURN_DIRECTION]);
        setAngleToTurn(obj, stmt);
    };

    var evalDriveAction = function(obj, stmt) {
        obj.actors.initTachoMotors(obj.simulationData.encoder.left, obj.simulationData.encoder.right);
        obj.actors.setSpeed(evalExpr(obj, stmt.speed), stmt[DRIVE_DIRECTION]);
        setDistanceToDrive(obj, stmt);
    };

    var evalMotorOnAction = function(obj, stmt) {
        if (stmt[MOTOR_SIDE] == MOTOR_LEFT) {
            obj.actors.initLeftTachoMotor(obj.simulationData.encoder.left);
            obj.actors.setLeftMotorSpeed(evalExpr(obj, stmt.speed));
        } else {
            obj.actors.initRightTachoMotor(obj.simulationData.encoder.right);
            obj.actors.setRightMotorSpeed(evalExpr(obj, stmt.speed));
        }
        setDurationToCover(obj, stmt);
    };

    var evalMotorSetPowerAction = function(obj, stmt) {
        if (stmt[MOTOR_SIDE] == MOTOR_LEFT) {
            obj.actors.setLeftMotorSpeed(evalExpr(obj, stmt.speed));
        } else {
            obj.actors.setRightMotorSpeed(evalExpr(obj, stmt.speed));
        }
    };

    var evalMotorStopAction = function(obj, stmt) {
        if (stmt[MOTOR_SIDE] == MOTOR_LEFT) {
            obj.actors.setLeftMotorSpeed(0);
        } else {
            obj.actors.setRightMotorSpeed(0);
        }
    };

    var evalMotorGetPowerAction = function(obj, motorSide) {
        if (motorSide == MOTOR_LEFT) {
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
        if (stmt[MOTOR_DURATION]) {
            obj.actors.setMotorDuration(obj.program, (stmt[MOTOR_DURATION]).motorMoveMode, evalExpr(obj, (stmt[MOTOR_DURATION]).motorDurationValue),
                    stmt[MOTOR_SIDE]);
        }
    };

    var evalMathChange = function(obj, stmt) {
        var left = evalExpr(obj, stmt.left);
        var right = evalExpr(obj, stmt.right);

    };

    var evalRepeat = function(obj, stmt) {
        switch (stmt.mode) {
        case TIMES:
            for (var i = 0; i < evalExpr(obj, stmt.expr); i++) {
                obj.program.prepend(stmt.stmtList);
            }
            break;
        case FOR:
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
        case NUM_CONST:
        case BOOL_CONST:
        case COLOR_CONST:
        case STRING_CONST:
            return expr.value;
        case NULL_CONST:
            return null;
        case NUMERIC_ARRAY:
        case STRING_ARRAY:
        case COLOR_ARRAY:
        case BOOL_ARRAY:
            return evalArray(obj, expr.value);
        case CREATE_LIST_WITH_ITEM:
            return evalCreateArrayWithItem(obj, expr.size, expr.value);
        case CREATE_LIST_LENGTH:
            return evalListLength(obj, expr.list);
        case CREATE_LIST_IS_EMPTY:
            return evalListIsEmpty(obj, expr.list);
        case CREATE_LIST_FIND_ITEM:
            return evalListFindItem(obj, expr.position, expr.list, expr.item);
        case CREATE_LISTS_GET_INDEX:
            return evalListsGetIndex(obj, expr.list, expr.op, expr.position, expr.item);
        case TEXT_JOIN:
            return evalTextJoin(obj, expr.value);
        case CREATE_LISTS_GET_SUBLIST:
            return evalListsGetSubList(obj, expr);
        case VAR:
            return obj.memory.get(expr.name);
        case BINARY:
            return evalBinary(obj, expr.op, expr.left, expr.right);
        case UNARY:
            return evalUnary(obj, expr.op, expr.value);
        case TERNARY_EXPR:
            return evalTernaryExpr(obj, expr.exprList, expr.thenList, expr.elseStmts);
        case SINGLE_FUNCTION:
            return evalSingleFunction(obj, expr.op, expr.value);
        case RANDOM_INT:
            return evalRandInt(obj, expr.min, expr.max);
        case RANDOM_DOUBLE:
            return evalRandDouble();
        case MATH_CONSTRAIN_FUNCTION:
            return evalMathPropFunct(obj, expr.value, expr.min, expr.max);
        case MATH_ON_LIST:
            return evalMathOnList(obj, expr.op, expr.list);
        case MATH_PROP_FUNCT:
            return evalMathPropFunct(obj, expr.op, expr.arg1, expr.arg2);
        case MATH_CONST:
            return evalMathConst(obj, expr.value);
        case GET_SAMPLE:
            return evalSensor(obj, expr.sensorType, expr.sensorMode);
        case ENCODER_SENSOR_SAMPLE:
            return evalEncoderSensor(obj, expr.motorSide, expr.sensorMode);
        case MOTOR_GET_POWER:
            return evalMotorGetPowerAction(obj, expr.motorSide);
            break;
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
        var motor = obj.actors.getRightMotor();
        if (motorSide == MOTOR_LEFT) {
            motor = obj.actors.getLeftMotor()
        }
        switch (sensorMode) {
        case ROTATION:
            return motor.getCurrentRotations();
        case DEGREE:
            return motor.getCurrentRotations() * 360.;
        case DISTANCE:
            return motor.getCurrentRotations() * (WHEEL_DIAMETER * 3.14);
        default:
            throw "Invalid Encoder Mode!";
        }
    };

    var evalBinary = function(obj, op, left, right) {
        var valLeft = evalExpr(obj, left);
        var valRight = evalExpr(obj, right);
        var val;
        switch (op) {
        case ADD:
            val = valLeft + valRight;
            break;
        case MINUS:
            val = valLeft - valRight;
            break;
        case MULTIPLY:
            val = valLeft * valRight;
            break;
        case DIVIDE:
            val = valLeft / valRight;
            break;
        case POWER:
            val = Math.pow(valLeft, valRight);
            break;
        case LT:
            val = valLeft < valRight;
            break;
        case GT:
            val = valLeft > valRight;
            break;
        case EQ:
            val = valLeft == valRight;
            break;
        case NEQ:
            val = valLeft != valRight;
            break;
        case GTE:
            val = valLeft >= valRight;
            break;
        case LTE:
            val = valLeft <= valRight;
            break;
        case OR:
            val = valLeft || valRight;
            break;
        case AND:
            val = valLeft && valRight;
            break;
        case MOD:
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
        case NEG:
            return -val;
        case NOT:
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
        case MIN:
            return Math.min.apply(null, listVal);
        case MAX:
            return Math.max.apply(null, listVal);
        case AVERAGE:
            return mathMean(listVal);
        case MEDIAN:
            return mathMedian(listVal);
        case STD_DEV:
            return mathStandardDeviation(listVal);
        case RANDOM:
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
        var remove = op == GET_REMOVE;
        switch (position) {
        case FROM_START:
            if (remove) {
                return list.splice(it, 1)[0];
            }
            return list[it];
        case FROM_END:
            if (remove) {
                return listsRemoveFromEnd(list, it);
            }
            return list.slice(-(it + 1))[0];
        case FIRST:
            if (remove) {
                return list.shift();
            }
            return list[0];
        case LAST:
            if (remove) {
                return list.pop();
            }
            return list.slice(-1)[0];
        case RANDOM:
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
        case FROM_START:
            list.splice(it, 1);
            break;
        case FROM_END:
            listsRemoveFromEnd(list, it);
            break;
        case FIRST:
            list.shift();
            break;
        case LAST:
            list.pop();
            break;
        case RANDOM:
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
        var insert = op == INSERT;
        switch (stmt.position) {
        case FROM_START:
            if (insert) {
                list.splice(it, 0, newValue)
                break;
            }
            list[it] = newValue;
            break;
        case FROM_END:
            if (insert) {
                list.splice(list.length - it - 1, 0, newValue)
                break;
            }
            list[list.length - it - 1] = newValue;
            break;
        case FIRST:
            if (insert) {
                list.unshift(newValue)
                break;
            }
            list[0] = newValue;
            break;
        case LAST:
            if (insert) {
                list.push(newValue)
                break;
            }
            list[list.length - 1] = newValue;
            break;
        case RANDOM:
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
        if (expr.where1 == FIRST && expr.where2 == LAST) {
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
            if (where == FROM_START) {
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
            result += String(evalExpr(obj, values[i]));
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

    return ProgramEval;
});
