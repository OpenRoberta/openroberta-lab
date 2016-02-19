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
                step(simulationSensorData);
                break;

            case REPEAT_STMT:
                evalRepeat(this, stmt);
                step(simulationSensorData);
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

            case WAIT_STMT:
                evalWaitStmt(this, stmt);
                break;

            case WAIT_TIME_STMT:
                evalWaitTime(this, simulationSensorData, stmt);
                break;

            case TURN_LIGHT:
                this.led.color = stmt.color;
                this.led.mode = stmt.mode;
                break;

            case STOP_DRIVE:
                this.actors.setSpeed(0);
                break;

            case MOTOR_STOP:
                evalMotorStopAction(stmt);
                break;

            case RESET_LIGHT:
                this.led.color = GREEN;
                this.led.mode = OFF;
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

    function evalWaitTime(obj, simulationSensorData, stmt) {
        obj.program.setIsRunningTimer(true);
        obj.program.resetTimer(simulationSensorData.time);
        obj.program.setTimer(evalExpr(obj, stmt.time));
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

    function evalMotorStopAction(obj, stmt) {
        if (stmt[MOTOR_SIDE] == MOTOR_LEFT) {
            obj.actors.setLeftMotorSpeed(0);
        } else {
            obj.actors.setRightMotorSpeed(0);
        }
    }

    function setAngleToTurn(obj, stmt) {
        if (stmt.angle != undefined) {
            obj.actors.clculateAngleToCover(obj.program, evalExpr(obj, stmt.angle));
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
            return expr.value;

        case VAR:
            return this.memory.get(expr.name);

        case BINARY:
            return evalBinary(obj, expr.op, expr.left, expr.right);

        case GET_SAMPLE:
            return evalSensor(obj, expr[SENSOR_TYPE], expr[SENSOR_MODE]);
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
        default:
            throw "Invalid Sensor!";
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
        default:
            throw "Invalid Binary Operator";
        }
        return val;
    }

    return ProgramEval;
});
