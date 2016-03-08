/**
 * Interpreter of program that is running in the simulation. This interpreter
 * reads every statement of the program and gives command to the simulation what
 * the robot should do.
 */
define([ 'robertaLogic.actors', 'robertaLogic.sensors', 'robertaLogic.memory', 'robertaLogic.program', 'robertaLogic.led' ], function(Actors, Sensors, Memory,
        Program, Led) {

    var ProgramEval = function() {
        this.actors = new Actors();
        this.sensors = new Sensors();
        this.memory = new Memory;
        this.program = new Program();
        this.led = new Led();
    };

    /**
     * Initialize the program that is executed in the simulation.
     * 
     * @param program
     *            {Object} - list of statements representing the program
     */
    ProgramEval.prototype.initProgram = function(program) {
        this.memory.clear();
        this.program.setNextStatement(true);
        this.program.setWait(false);
        this.program.set(program);
        this.led.mode = OFF;
        this.actors.resetMotorsSpeed();
    };

    /**
     * Function that executes one step of the program.
     * 
     * @param simulationSensorData
     *            {Object} - sensor data from the simulation
     */
    ProgramEval.prototype.step = function(simulationSensorData) {
        setSensorActorValues(this, simulationSensorData);
        if (this.program.isNextStatement()) {
            var stmt = this.program.getRemove();
            switch (stmt.stmt) {
            case ASSIGN_STMT:
                var value = evalExpr(obj, stmt.expr);
                this.memory.assign(stmt.name, value);
                break;

            case VAR_DECLARATION:
                var value = evalExpr(obj, stmt.value);
                this.memory.decl(stmt.name, value);
                break;

            case IF_STMT:
                evalIf(this, stmt);
                this.step(simulationSensorData);
                break;

            case REPEAT_STMT:
                evalRepeat(this, stmt);
                this.step(simulationSensorData);
                break;

            case DRIVE_ACTION:
                evalDriveAction(this, simulationSensorData, stmt);
                break;

            case TURN_ACTION:
                evalTurnAction(this, simulationSensorData, stmt);
                break;

            case MOTOR_ON_ACTION:
                evalMotorOnAction(this, simulationSensorData, stmt);
                break;

            case SHOW_TEXT_ACTION:
                evalShowTextAction(this, stmt);
                break;

            case WAIT_STMT:
                evalWaitStmt(this, stmt);
                break;

            case WAIT_TIME_STMT:
                evalWaitTime(this, simulationSensorData, stmt);
                break;

            case TURN_LIGHT:
                evalLedOnAction(this, simulationSensorData, stmt);
                break;

            case STOP_DRIVE:
                this.actors.setSpeed(0);
                break;

            case MOTOR_STOP:
                evalMotorStopAction(stmt);
                break;

            case MOTOR_SET_POWER:
                evalMotorSetPowerAction(stmt);
                break;

            case RESET_LIGHT:
                this.led.color = GREEN;
                this.led.mode = OFF;
                break;

            case ENCODER_SENSOR_RESET:
                evalResetEncoderSensor(this, stmt);
                break;

            default:
                throw "Invalid Statement " + stmt.stmt + "!";
            }
        }
        this.actors.calculateCoveredDistance(this.program);
        this.program.handleWaitTimer();
    };

    var setSensorActorValues = function(obj, simulationSensorData) {
        obj.sensors.touchSensor = simulationSensorData.touch;
        obj.sensors.colorSensor = simulationSensorData.color;
        obj.sensors.lightSensor = simulationSensorData.light;
        obj.sensors.ultrasonicSensor = simulationSensorData.ultrasonic;
        obj.actors.getLeftMotor().setCurrentRotations(simulationSensorData.tacho[0]);
        obj.actors.getRightMotor().setCurrentRotations(simulationSensorData.tacho[1]);
        obj.program.getTimer().setCurrentTime(simulationSensorData.time);
    };

    function evalResetEncoderSensor(obj, stmt) {
        if (stmt[MOTOR_SIDE] == MOTOR_LEFT) {
            obj.actors.resetLeftTachoMotor(0);
        } else {
            obj.actors.resetRightTachoMotor(0);
        }
    }

    function evalWaitTime(obj, simulationSensorData, stmt) {
        obj.program.setIsRunningTimer(true);
        obj.program.resetTimer(simulationSensorData.time);
        obj.program.setTimer(evalExpr(obj, stmt.time));
    }

    function evalLedOnAction(obj, simulationSensorData, stmt) {
        obj.led.color = stmt.color;
        obj.led.mode = stmt.mode;
        obj.led.blinkAcc = 0.0;
        switch (obj.led.mode) {
        case "FLASH":
            obj.led.blink = 2;
            break;
        case "DOUBLE_FLASH":
            obj.led.blink = 4;
            break;
        }
    }

    function evalShowTextAction(obj, stmt) {
        val = evalExpr(obj, stmt.value)
        console.log(val);
    }

    function evalTurnAction(obj, simulationSensorData, stmt) {
        obj.actors.resetTachoMotors(simulationSensorData.tacho[0], simulationSensorData.tacho[1]);
        obj.actors.setAngleSpeed(evalExpr(obj, stmt.speed), stmt[TURN_DIRECTION]);
        setAngleToTurn(obj, stmt);
    }

    function evalDriveAction(obj, simulationSensorData, stmt) {
        obj.actors.resetTachoMotors(simulationSensorData.tacho[0], simulationSensorData.tacho[1]);
        obj.actors.setSpeed(evalExpr(obj, stmt.speed), stmt[DRIVE_DIRECTION]);
        setDistanceToDrive(obj, stmt);
    }

    function evalMotorOnAction(obj, simulationSensorData, stmt) {
        if (stmt[MOTOR_SIDE] == MOTOR_LEFT) {
            obj.actors.resetLeftTachoMotor(simulationSensorData.tacho[0]);
            obj.actors.setLeftMotorSpeed(evalExpr(obj, stmt.speed));
        } else {
            obj.actors.resetRightTachoMotor(simulationSensorData.tacho[1]);
            obj.actors.setRightMotorSpeed(evalExpr(obj, stmt.speed));
        }
        setDurationToCover(obj, stmt);
    }

    function evalMotorSetPowerAction(obj, stmt) {
        if (stmt[MOTOR_SIDE] == MOTOR_LEFT) {
            obj.actors.setLeftMotorSpeed(evalExpr(obj, stmt.speed));
        } else {
            obj.actors.setRightMotorSpeed(evalExpr(obj, stmt.speed));
        }
    }

    function evalMotorStopAction(obj, stmt) {
        if (stmt[MOTOR_SIDE] == MOTOR_LEFT) {
            obj.actors.setLeftMotorSpeed(0);
        } else {
            obj.actors.setRightMotorSpeed(0);
        }
    }

    function evalMotorGetPowerAction(obj, motorSide) {
        if (motorSide == MOTOR_LEFT) {
            return obj.actors.getLeftMotor().getPower();
        } else {
            return obj.actors.getRightMotor().getPower();
        }
    }

    function setAngleToTurn(obj, stmt) {
        if (stmt.angle != undefined) {
            obj.actors.calculateAngleToCover(obj.program, evalExpr(obj, stmt.angle));
        }
    }

    function setDistanceToDrive(obj, stmt) {
        if (stmt.distance != undefined) {
            obj.actors.setDistanceToCover(obj.program, evalExpr(obj, stmt.distance));
        }
    }

    function setDurationToCover(obj, stmt) {
        if (stmt[MOTOR_DURATION] != undefined) {
            obj.actors.setMotorDuration(obj.program, (stmt[MOTOR_DURATION]).motorMoveMode, evalExpr(obj, (stmt[MOTOR_DURATION]).motorDurationValue),
                    stmt[MOTOR_SIDE]);
        }
    }

    function evalRepeat(obj, stmt) {
        switch (stmt.mode) {
        case TIMES:
            for (var i = 0; i < evalExpr(obj, stmt.expr); i++) {
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
    }

    function evalIf(obj, stmt) {
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
    }

    function evalWaitStmt(obj, stmt) {
        obj.program.setWait(true);
        obj.program.prepend([ stmt ]);
        for (var i = 0; i < stmt.statements.length; i++) {
            var value = evalIf(obj, stmt.statements[i]);
            if (value) {
                break;
            }
        }

    }

    function evalExpr(obj, expr) {
        switch (expr.expr) {
        case NUM_CONST:
        case BOOL_CONST:
        case COLOR_CONST:
        case STRING_CONST:
            return expr.value;

        case VAR:
            return this.memory.get(expr.name);

        case BINARY:
            return evalBinary(obj, expr.op, expr.left, expr.right);

        case UNARY:
            return evalUnary(obj, expr.op, expr.value);

        case SINGLE_FUNCTION:
            return evalSingleFunction(obj, expr.op, expr.value);

        case RANDOM_INT:
            return evalRandInt(obj, expr.min, expr.max);

        case RANDOM_DOUBLE:
            return evalRandDouble();

        case MATH_CONSTRAIN_FUNCTION:
            return evalMathPropFunct(obj, expr.value, expr.min, expr.max);

        case MATH_PROP_FUNCT:
            return evalMathPropFunct(obj, expr.op, expr.arg1, expr.arg2);

        case MATH_CONST:
            return evalMathConst(obj, expr.value);

        case GET_SAMPLE:
            return evalSensor(obj, expr[SENSOR_TYPE], expr[SENSOR_MODE]);

        case ENCODER_SENSOR_SAMPLE:
            return evalEncoderSensor(obj, expr.motorSide, expr.sensorMode);

        case MOTOR_GET_POWER:
            return evalMotorGetPowerAction(obj, expr.motorSide);
            break;

        default:
            throw "Invalid Expression Type!";
        }
    }

    function evalSensor(obj, sensorType, sensorMode) {
        switch (sensorType) {
        case TOUCH:
            return obj.sensors.touchSensor;
        case ULTRASONIC:
            return obj.sensors.ultrasonicSensor;
        case RED:
            return obj.sensors.lightSensor;
        case COLOUR:
            return obj.sensors.colorSensor;
        case ANGLE:
            return obj.sensors.gyroSensor;
        case RATE:
            return obj.sensors.gyroSensor;

        default:
            throw "Invalid Sensor!";
        }
    }

    function evalEncoderSensor(obj, motorSide, sensorMode) {
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
    }

    function evalBinary(obj, op, left, right) {
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
    }

    function evalUnary(obj, op, value) {
        var val = evalExpr(obj, value);
        switch (op) {
        case NEG:
            return -val;
        default:
            throw "Invalid Unary Operator";
        }
    }

    function evalSingleFunction(obj, functName, value) {
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
    }

    function evalMathConst(obj, mathConst) {
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
    }

    function evalMathPropFunct(obj, val, min, max) {
        var val_ = evalExpr(obj, val);
        var min_ = evalExpr(obj, min);
        var max_ = evalExpr(obj, max);
        return Math.min(Math.max(val_, min_), max_);
    }

    function evalMathConstrainFunct(obj, val, min, max) {
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
    }

    function evalRandInt(obj, min, max) {
        min_ = evalExpr(obj, min);
        max_ = evalExpr(obj, max)
        return math_random_int(min_, max_);
    }

    function evalRandDouble() {
        return Math.random();
    }

    isPrime = function(n) {
        if (isNaN(n) || !isFinite(n) || n % 1 || n < 2) {
            return false;
        }
        if (n == leastFactor(n)) {
            return true;
        }
        return false;
    }

    leastFactor = function(n) {
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
    }

    function math_random_int(a, b) {
        if (a > b) {
            // Swap a and b to ensure a is smaller.
            var c = a;
            a = b;
            b = c;
        }
        return Math.floor(Math.random() * (b - a + 1) + a);
    }

    return ProgramEval;
});
