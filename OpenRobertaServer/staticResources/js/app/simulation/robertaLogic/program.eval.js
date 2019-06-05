/**
 * Interpreter of program that is running in the simulation. This interpreter
 * reads every statement of the program and gives command to the simulation what
 * the robot should do.
 */
define(['robertaLogic.actors', 'robertaLogic.memory', 'robertaLogic.program', 'robertaLogic.gyro', 'util', 'robertaLogic.constants',
    'simulation.program.builder'
], function (Actors, Memory, Program, Gyro, UTIL, CONSTANTS, PROGRAM_BUILDER) {
    var privateMem = new WeakMap();
    var internal = function (object) {
        if (!privateMem.has(object)) {
            privateMem.set(object, {});
        }
        return privateMem.get(object);
    };

    var ProgramEval = function () {
        internal(this).program = new Program();
        internal(this).memory = new Memory();
        internal(this).actors = new Actors();
        internal(this).gyro = new Gyro();
        internal(this).simulationData = {};
        internal(this).outputCommands = {};
        internal(this).currentStatement = {};
        internal(this).modifiedStmt = false;
        internal(this).repeatStmtExpr = {};
        internal(this).funcioncCalls = new WeakMap();
    };

    ProgramEval.prototype.getProgram = function () {
        return internal(this).program;
    };

    /**
     * Initialize the program that is executed in the simulation.
     * 
     * @param program
     *            {Object} - list of statements representing the program
     */
    ProgramEval.prototype.initProgram = function (program) {
        internal(this).memory.clear();
        internal(this).program.setNextStatement(true);
        internal(this).program.setWait(false);
        internal(this).program.set(program);
        internal(this).actors.resetMotorsSpeed();
        internal(this).simulationData = {};
        internal(this).outputCommands = {};
        internal(this).currentStatement = {};
        internal(this).modifiedStmt = false;
        internal(this).repeatStmtExpr = {};
        internal(this).funcioncCalls = new WeakMap();
    };

    /**
     * Function that executes one step of the program.
     * 
     * @param simulationData
     *            {Object} - sensor data from the simulation
     */
    ProgramEval.prototype.step = function (simulationData) {
        internal(this).outputCommands = {};
        setSensorActorValues(internal(this), simulationData);
        console.log('eval stmt')
        if (internal(this).program.isNextStatement()) {
            var stmt = internal(this).program.getRemove();
            internal(this).currentStatement = stmt;
            switch (stmt.stmt) {
                case CONSTANTS.ASSIGN_STMT:
                    evalAssignmentStmt(internal(this), stmt);
                    break;

                case CONSTANTS.ASSIGN_METHOD_PARAMETER_STMT:
                    evalAssignMethodParameters(internal(this), stmt);
                    break;

                case CONSTANTS.VAR_DECLARATION:
                    evalVarDeclaration(internal(this), "");
                    break;

                case CONSTANTS.IF_STMT:
                    evalIf(internal(this), stmt);
                    // this.step(simulationData);
                    break;

                case CONSTANTS.REPEAT_STMT:
                    evalRepeat(internal(this), stmt);
                    break;

                case CONSTANTS.DRIVE_ACTION:
                    evalDriveAction(internal(this), stmt);
                    break;

                case CONSTANTS.CURVE_ACTION:
                    evalCurveAction(internal(this), stmt);
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

                case CONSTANTS.DISPLAY_IMAGE_ACTION:
                    evalDisplayImageAction(internal(this), simulationData, stmt);
                    break;

                case CONSTANTS.SHOW_TEXT_ACTION:
                    evalShowTextAction(internal(this), stmt);
                    break;

                case CONSTANTS.DISPLAY_TEXT_ACTION:
                    evalDisplayTextAction(internal(this), simulationData, stmt);
                    break;

                case CONSTANTS.DISPLAY_SET_BRIGHTNESS_ACTION:
                    evalDisplaySetBrightnessAction(internal(this));
                    break;

                case CONSTANTS.DISPLAY_SET_PIXEL_ACTION:
                    evalDisplaySetPixelAction(internal(this));
                    break;

                case CONSTANTS.CLEAR_DISPLAY_ACTION:
                    evalClearDisplayAction(internal(this));
                    break;

                case CONSTANTS.CREATE_DEBUG_ACTION:
                    internal(this).outputCommands.debug = true;
                    break;

                case CONSTANTS.WAIT_STMT:
                    evalWaitStmt(internal(this), stmt);
                    break;

                case CONSTANTS.WAIT_TIME_STMT:
                    evalWaitTime(internal(this), simulationData);
                    break;

                case CONSTANTS.TURN_LIGHT:
                    evalTurnLightAction(internal(this), stmt);
                    break;

                case CONSTANTS.LED_ON_ACTION:
                    evalLedOnAction(internal(this));
                    break;

                case CONSTANTS.LIGHT_ACTION:
                    evalLightSensorAction(internal(this), stmt);
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
                    evalResetGyroSensor(internal(this));
                    break;

                case CONSTANTS.TIMER_SENSOR_RESET:
                    evalResetTimerSensor(internal(this), stmt);
                    break;

                case CONSTANTS.TONE_ACTION:
                    evalToneAction(internal(this), simulationData);
                    break;

                case CONSTANTS.PLAY_FILE_ACTION:
                    evalPlayFileAction(internal(this), simulationData, stmt);
                    break;

                case CONSTANTS.SET_LANGUAGE_ACTION:
                    evalSetLanguageAction(internal(this));
                    break;

                case CONSTANTS.SAY_TEXT_ACTION:
                    evalSayTextAction(internal(this));
                    break;

                case CONSTANTS.SET_VOLUME_ACTION:
                    evalVolumeAction(internal(this));
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

                case CONSTANTS.PIN_WRITE_VALUE_SENSOR:
                    evalPinWriteValueSensor(internal(this), stmt);
                    break;

                case CONSTANTS.FLOW_CONTROL:
                    evalFlowControlStatement(internal(this), stmt);
                    break;
                case CONSTANTS.IF_RETURN:
                    evalIfReturn(internal(this), stmt);
                    break;
                case CONSTANTS.NOOP_STMT:
                    this.step(simulationData);
                    break;
                case CONSTANTS.CONSOLE_DEBUG:
                    evalConsoleDebugAction(internal(this));
                    break;
                case CONSTANTS.ASSERT_STMT:
                    evalAssertStmt(internal(this), stmt);
                    break;
                default:
                    throw "Invalid Statement " + stmt.stmt + "!";
            }

            if (internal(this).modifiedStmt) {
                internal(this).program.addCustomMethodForEvaluation([internal(this).currentStatement]);
                internal(this).program.merge();
                internal(this).modifiedStmt = false;
            }

        }
        var newSpeeds = internal(this).actors.checkCoveredDistanceAndCorrectSpeed(internal(this).program, internal(this).simulationData.correctDrive);
        internal(this).program.handleWaitTimer();
        outputSpeeds(internal(this), newSpeeds);
        internal(this).outputCommands.terminated = internal(this).program.isTerminated();
        return internal(this).outputCommands;

    };

    var evalFlowControlStatement = function (obj, stmt) {
        var t = obj.program.getRemove();
        while (!(t.stmt == CONSTANTS.REPEAT_STMT && t.loopNumber == stmt.loopNumber)) {
            t = obj.program.getRemove();
        }
        if (stmt.mode == CONSTANTS.CONTINUE) {
            obj.program.prepend([t]);
        }
    };

    var evalVarDeclaration = function (obj, propName) {
        var stmt;
        var value;
        if (propName === "") {
            stmt = obj.currentStatement;
            value = evalExpr(obj, "value");
        } else {
            stmt = UTIL.getPropertyFromObject(obj.currentStatement, propName);
            value = evalExpr(obj, propName + ".value");
        }
        if (!isObject(value) && !obj.modifiedStmt) {
            obj.memory.decl(stmt.name, value);
        }
    };

    var evalAssignmentStmt = function (obj, stmt) {
        var value = evalExpr(obj, "expr");
        if (!isObject(value) && !obj.modifiedStmt) {
            obj.memory.assign(stmt.name, value);
        }
    };

    var evalAssignMethodParameters = function (obj, stmt) {
        var parameterName = stmt.name;
        var value = evalExpr(obj, "expr");
        var isParameterDefined = obj.memory.get(parameterName) !== undefined;
        if (isParameterDefined) {
            obj.memory.assign(parameterName, value);
        } else {
            obj.memory.decl(parameterName, value);
        }
    };

    var setSensorActorValues = function (obj, simulationData) {
        obj.simulationData = simulationData;
        if (simulationData.encoder) {
            obj.actors.getLeftMotor().setCurrentRotations(simulationData.encoder.left);
            obj.actors.getRightMotor().setCurrentRotations(simulationData.encoder.right);
        }
        if (simulationData.gyro) {
            obj.gyro.update(simulationData.gyro.angle);
            obj.gyro.setRate(simulationData.gyro.rate);
        }
        obj.program.getTimer().setCurrentTime(simulationData.time);
        // We multiply the next frame by two because of the unstable frame rate
        obj.program.setNextFrameTimeDuration(simulationData.frameTime * 5.0);
    };

    var outputSpeeds = function (obj, speeds) {
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

    var evalResetEncoderSensor = function (obj, stmt) {
        obj.outputCommands.encoder = {};
        if (stmt[CONSTANTS.MOTOR_SIDE] == CONSTANTS.MOTOR_LEFT) {
            obj.outputCommands.encoder.leftReset = true;
        } else {
            obj.outputCommands.encoder.rightReset = true;
        }
    };

    var evalResetGyroSensor = function (obj) {
        obj.gyro.reset();
    };

    var evalResetTimerSensor = function (obj, stmt) {
        obj.outputCommands.timer = {};
        obj.outputCommands.timer[stmt.timer] = 'reset';
    };

    var evalWaitTime = function (obj, simulationData) {
        var value = evalExpr(obj, "time");
        if (!isObject(value) && !obj.modifiedStmt) {
            setSimulationTimer(obj, simulationData, value);
        }
    };

    var evalLightSensorAction = function (obj, stmt) {
        obj.outputCommands.led = {};
        obj.outputCommands.led.color = stmt.colorValue;
        obj.outputCommands.led.mode = stmt.mode;
    };

    var evalTurnLightAction = function (obj, stmt) {
        obj.outputCommands.led = {};
        obj.outputCommands.led.color = stmt.colorValue;
        obj.outputCommands.led.mode = stmt.mode;
    };

    var evalLedOnAction = function (obj) {
        var value = evalExpr(obj, "rgbColor");
        if (!isObject(value) && !obj.modifiedStmt) {
            obj.outputCommands.led = {};
            obj.outputCommands.led.color = value;
        }
    };

    var evalLedStatusAction = function (obj, stmt) {
        obj.outputCommands.led = {};
        if (stmt.mode == CONSTANTS.RESET) {
            obj.outputCommands.led.color = '';
        }
        obj.outputCommands.led.mode = CONSTANTS.OFF;
    };

    var evalShowPictureAction = function (obj, stmt) {
        var x = evalExpr(obj, "x");
        var y = evalExpr(obj, "y");
        if (!isObject(x) && !isObject(y) && !obj.modifiedStmt) {
            obj.outputCommands.display = {};
            obj.outputCommands.display.picture = stmt.picture;
            obj.outputCommands.display.x = x;
            obj.outputCommands.display.y = y;
        }
    };

    var evalDisplayImageAction = function (obj, simulationData, stmt) {
        obj.outputCommands.display = {};
        obj.outputCommands.display.mode = stmt.mode;
        var image = evalExpr(obj, "image");
        if (!isObject(image) && !obj.modifiedStmt) {
            obj.outputCommands.display.picture = image;
            if (stmt.mode == CONSTANTS.ANIMATION) {
                var duration = obj.outputCommands.display.picture.length * 200;
                setSimulationTimer(obj, simulationData, duration);
            }
        }
    };

    var evalShowTextAction = function (obj, stmt) {
        var text = evalExpr(obj, "text");
        var x = evalExpr(obj, "x");
        var y = evalExpr(obj, "y");

        if (!isObject(x) && !isObject(y) && !isObject(text) && !obj.modifiedStmt) {
            obj.outputCommands.display = {};
            obj.outputCommands.display.text = String(roundIfSensorData(text, stmt.text.expr));
            obj.outputCommands.display.x = x;
            obj.outputCommands.display.y = y;
        }
    };

    var evalDisplayTextAction = function (obj, simulationData, stmt) {
        var text = evalExpr(obj, "text");
        if (!isObject(text) && !obj.modifiedStmt) {
            obj.outputCommands.display = {};
            if (stmt.mode == CONSTANTS.TEXT) {
                obj.outputCommands.display.text = String(roundIfSensorData(text, stmt.text.expr));
            } else {
                obj.outputCommands.display.character = String(roundIfSensorData(text, stmt.text.expr));
            }
            obj.program.setIsRunningTimer(true);
            obj.program.resetTimer(simulationData.time);
            // TODO get the time needed to display this specific string from the
            // simulation or a finish flag.
            var duration = 0;
            if (stmt.mode == CONSTANTS.TEXT) {
                duration = (obj.outputCommands.display.text.length + 1) * 7 * 150;
            } else {
                if (obj.outputCommands.display.character.length > 1) {
                    duration = (obj.outputCommands.display.character.length) * 400;
                } else {
                    duration = 0;
                }
            }
            obj.program.setTimer(duration);
        }
    };

    var evalImageShiftAction = function (obj, direction) {
        var image = evalExpr(obj, "image");
        var n = evalExpr(obj, "n");
        return shiftImage(image, direction, n);
    };

    var evalImageInvertAction = function (obj) {
        var image = evalExpr(obj, "image");
        return invertImage(image);
    };

    var evalDisplaySetBrightnessAction = function (obj) {
        var value = evalExpr(obj, "value");
        if (!isObject(value) && !obj.modifiedStmt) {
            obj.outputCommands.display = {};
            obj.outputCommands.display[CONSTANTS.BRIGHTNESS] = value;
        }
    };

    var evalDisplaySetPixelAction = function (obj) {
        var x = evalExpr(obj, "x");
        var y = evalExpr(obj, "y");
        var value = evalExpr(obj, "value");
        if (!isObject(x) && !isObject(y) && !isObject(value) && !obj.modifiedStmt) {
            obj.outputCommands.display = {};
            obj.outputCommands.display[CONSTANTS.PIXEL] = {};
            obj.outputCommands.display[CONSTANTS.PIXEL][CONSTANTS.X] = x;
            obj.outputCommands.display[CONSTANTS.PIXEL][CONSTANTS.Y] = y;
            obj.outputCommands.display[CONSTANTS.PIXEL][CONSTANTS.BRIGHTNESS] = value;
        }
    };

    var evalDisplayGetBrightnessAction = function (obj) {
        return obj.simulationData[CONSTANTS.BRIGHTNESS];
    };

    var evalDisplayGetPixelAction = function (obj) {
        var X = evalExpr(obj, "x");
        var Y = evalExpr(obj, "y");
        if (!isObject(X) && !isObject(Y) && !obj.modifiedStmt) {
            return obj.simulationData[CONSTANTS.PIXEL][Y][X] * 9.0 / 255.0;
        }
    };

    var roundIfSensorData = function (val, exprType) {
        if ((exprType == CONSTANTS.GET_SAMPLE || exprType == CONSTANTS.ENCODER_SENSOR_SAMPLE) && isNumber(val)) {
            val = UTIL.round(val, 2);
        }
        return val;
    };

    var evalClearDisplayAction = function (obj) {
        obj.outputCommands.display = {};
        obj.outputCommands.display.clear = true;
    };

    var setSimulationTimer = function (obj, simulationData, duration) {
        obj.program.setIsRunningTimer(true);
        obj.program.resetTimer(simulationData.time);
        obj.program.setTimer(duration);
    };

    var isObject = function (obj) {
        return typeof obj === 'object' && !(obj instanceof Array);
    };

    var evalToneAction = function (obj, simulationData) {
        var timerDuration = evalExpr(obj, "duration");
        var frequency = evalExpr(obj, "frequency");
        if (!isObject(timerDuration) && !isObject(frequency) && !obj.modifiedStmt) {
            setSimulationTimer(obj, simulationData, timerDuration);
            obj.outputCommands.tone = {};
            obj.outputCommands.tone.frequency = frequency;
            obj.outputCommands.tone.duration = timerDuration;
        }
    };

    var evalPlayFileAction = function (obj, simulationData, stmt) {
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
        setSimulationTimer(obj, simulationData, duration);
        obj.outputCommands.tone = {};
        obj.outputCommands.tone.file = stmt.file;
    };

    var evalSetLanguageAction = function (obj) {
        var value = evalExpr(obj, "language");
        if (!isObject(value) && !obj.modifiedStmt) {
            obj.outputCommands.language = value;
        }
    };

    var evalSayTextAction = function (obj) {
        var text = evalExpr(obj, "text");
        if (!isObject(text) && !obj.modifiedStmt) {
            obj.outputCommands.sayText = {};
            obj.outputCommands.sayText.text = text;

            if (obj.currentStatement.speed !== undefined && obj.currentStatement.pitch !== undefined) {
                var speed = evalExpr(obj, "speed");
                var pitch = evalExpr(obj, "pitch");
                if (!isObject(speed) && !isObject(pitch)) {
                    obj.outputCommands.sayText.speed = speed;
                    obj.outputCommands.sayText.pitch = pitch;
                }
            }
        }
    };

    var evalVolumeAction = function (obj) {
        var value = evalExpr(obj, "volume");
        if (!isObject(value) && !obj.modifiedStmt) {
            obj.outputCommands.volume = value;
        }
    };

    var evalMethodCallVoid = function (obj, stmt) {
        var methodName = stmt.name;
        var method = obj.program.getMethod(methodName);
        method.stmtList.forEach(function (stmt) {
            stmt.callFromFunction = methodName;
        });
        obj.program.prepend(method.stmtList);
        obj.program.prepend(stmt.parameters);
    };

    var evalPinWriteValueSensor = function (obj, stmt) {
        var value = evalExpr(obj, "value");
        if (!isObject(value) && !obj.modifiedStmt) {
            obj.outputCommands[stmt.pin] = {};
            obj.outputCommands[stmt.pin][stmt.type] = {};
            obj.outputCommands[stmt.pin][stmt.type] = value;
        }
    };

    var evalMethodCallReturn = function (obj, name, parameters) {
        var method = obj.program.getMethod(name);
        obj.memory.increaseMethodCalls(name);
        var numberOfCalls = obj.memory.getNumberOfMethodCalls(name);
        var functionCallName = "funct_" + name + "_call_" + numberOfCalls;
        var returnVariable = createReturnPlaceHolderVariable(method, functionCallName);
        obj.program.addCustomMethodForEvaluation(parameters, functionCallName);
        obj.program.addCustomMethodForEvaluation(method.stmtList, functionCallName);

        obj.program.addCustomMethodForEvaluation([returnVariable], functionCallName);
        return createReferenceToReturnVarible(method.returnType, functionCallName);
    };

    var evalIfReturn = function (obj, stmt) {
        var condition = evalExpr(obj, "expr");
        var functionCallName = stmt.callFromFunction;
        if (condition) {
            while (obj.program.get().callFromFunction == functionCallName && obj.program.get().callFromFunction !== undefined) {
                obj.program.getRemove();
            }
            var returnVariable = createReturnPlaceHolderVariable(stmt, functionCallName);
            obj.program.prepend([returnVariable]);
        }

    };


    var createReturnPlaceHolderVariable = function (method, funcionCallName) {
        var returnVariable = PROGRAM_BUILDER.build("blocklyProgram=createVarDeclaration(CONST." + method.returnType + ",\"" + funcionCallName + "\")");
        returnVariable.value = method.return;
        return returnVariable;
    };

    var createReferenceToReturnVarible = function (returnType, funcionCallName) {
        return PROGRAM_BUILDER.build("blocklyProgram=createVarReference(CONST." + returnType + ",\"" + funcionCallName + "\")");
    };

    var evalTurnAction = function (obj, stmt) {
        var speed = evalExpr(obj, "speed");
        if (!isObject(speed) && !obj.modifiedStmt) {
            obj.actors.initTachoMotors(obj.simulationData.encoder.left, obj.simulationData.encoder.right);
            obj.actors.setAngleSpeed(speed, stmt[CONSTANTS.TURN_DIRECTION]);
            setAngleToTurn(obj, stmt);
        }
    };

    var evalDriveAction = function (obj, stmt) {
        var speed = evalExpr(obj, "speed");
        if (!isObject(speed) && !obj.modifiedStmt) {
            obj.actors.initTachoMotors(obj.simulationData.encoder.left, obj.simulationData.encoder.right);
            obj.actors.setSpeed(speed, stmt[CONSTANTS.DRIVE_DIRECTION]);
            setDistanceToDrive(obj, stmt);
        }
    };

    var evalCurveAction = function (obj, stmt) {
        var speedL = evalExpr(obj, "speedL");
        var speedR = evalExpr(obj, "speedR");
        if (!isObject(speedL) && !isObject(speedR) && !obj.modifiedStmt) {
            obj.actors.initTachoMotors(obj.simulationData.encoder.left, obj.simulationData.encoder.right);
            obj.actors.setLeftMotorSpeed(speedL, stmt[CONSTANTS.DRIVE_DIRECTION]);
            obj.actors.setRightMotorSpeed(speedR, stmt[CONSTANTS.DRIVE_DIRECTION]);
            setDistanceToDrive(obj, stmt);
        }
    };

    var evalMotorOnAction = function (obj, stmt) {
        var speed = evalExpr(obj, "speed");
        if (!isObject(speed) && !obj.modifiedStmt) {
            if (stmt[CONSTANTS.MOTOR_SIDE] == CONSTANTS.MOTOR_LEFT) {
                obj.actors.initLeftTachoMotor(obj.simulationData.encoder.left);
                obj.actors.setLeftMotorSpeed(speed, CONSTANTS.FOREWARD);
            } else if (stmt[CONSTANTS.MOTOR_SIDE] == CONSTANTS.MOTOR_RIGHT) {
                obj.actors.initRightTachoMotor(obj.simulationData.encoder.right);
                obj.actors.setRightMotorSpeed(speed, CONSTANTS.FOREWARD);
            } else if (stmt[CONSTANTS.MOTOR_SIDE] == CONSTANTS.MOTOR_XA) {
                obj.actors.setLeftMotorSpeed(speed, CONSTANTS.FOREWARD);
            } else if (stmt[CONSTANTS.MOTOR_SIDE] == CONSTANTS.MOTOR_XB) {
                obj.actors.setRightMotorSpeed(speed, CONSTANTS.FOREWARD);
            } else if (stmt[CONSTANTS.MOTOR_SIDE] == CONSTANTS.MOTOR_XAB) {
                obj.actors.setRightMotorSpeed(speed, CONSTANTS.FOREWARD);
                obj.actors.setLeftMotorSpeed(speed, CONSTANTS.FOREWARD);
            }
            setDurationToCover(obj, stmt);
        }
    };

    var evalMotorSetPowerAction = function (obj, stmt) {
        var speed = evalExpr(obj, "speed");
        if (!isObject(speed) && !obj.modifiedStmt) {
            if (stmt[CONSTANTS.MOTOR_SIDE] == CONSTANTS.MOTOR_LEFT) {
                obj.actors.setLeftMotorSpeed(speed, CONSTANTS.FOREWARD);
            } else {
                obj.actors.setRightMotorSpeed(speed, CONSTANTS.FOREWARD);
            }
        }
    };

    var evalMotorStopAction = function (obj, stmt) {
        if (stmt[CONSTANTS.MOTOR_SIDE] == CONSTANTS.MOTOR_LEFT) {
            obj.actors.setLeftMotorSpeed(0, CONSTANTS.FOREWARD);
        } else if (stmt[CONSTANTS.MOTOR_SIDE] == CONSTANTS.MOTOR_RIGHT) {
            obj.actors.setRightMotorSpeed(0, CONSTANTS.FOREWARD);
        } else if (stmt[CONSTANTS.MOTOR_SIDE] == CONSTANTS.MOTOR_XA) {
            obj.actors.setLeftMotorSpeed(0, CONSTANTS.FOREWARD);
        } else if (stmt[CONSTANTS.MOTOR_SIDE] == CONSTANTS.MOTOR_XB) {
            obj.actors.setRightMotorSpeed(0, CONSTANTS.FOREWARD);
        } else if (stmt[CONSTANTS.MOTOR_SIDE] == CONSTANTS.MOTOR_XAB) {
            obj.actors.setLeftMotorSpeed(0, CONSTANTS.FOREWARD);
            obj.actors.setRightMotorSpeed(0, CONSTANTS.FOREWARD);
        }
    };

    var evalMotorGetPowerAction = function (obj, motorSide) {
        if (obj.currentStatement.motorSide == CONSTANTS.MOTOR_LEFT) {
            return obj.actors.getLeftMotor().getPower();
        } else {
            return obj.actors.getRightMotor().getPower();
        }
    };

    var setAngleToTurn = function (obj, stmt) {
        if (stmt.angle) {
            var angle = evalExpr(obj, "angle");
            if (!isObject(angle) && !obj.modifiedStmt) {
                obj.actors.calculateAngleToCover(obj.program, angle);
            }
        }
    };

    var setDistanceToDrive = function (obj, stmt) {
        if (stmt.distance) {
            var distance = evalExpr(obj, "distance");
            if (!isObject(distance) && !obj.modifiedStmt) {
                obj.actors.setDistanceToCover(obj.program, distance);
            }
        }
    };

    var setDurationToCover = function (obj, stmt) {
        if (stmt[CONSTANTS.MOTOR_DURATION]) {
            var motorDuration = evalExpr(obj, CONSTANTS.MOTOR_DURATION + ".motorDurationValue");
            if (!isObject(motorDuration) && !obj.modifiedStmt) {
                obj.actors.setMotorDuration(obj.program, (stmt[CONSTANTS.MOTOR_DURATION]).motorMoveMode, motorDuration, stmt[CONSTANTS.MOTOR_SIDE]);
            }
        }
    };

    var evalRepeat = function (obj, stmt) {
        if (UTIL.isEmpty(obj.repeatStmtExpr)) {
            obj.repeatStmtExpr = UTIL.clone(stmt);
        }
        switch (stmt.mode) {
            case CONSTANTS.FOR_EACH:
                var i = obj.repeatStmtExpr.eachCounter++;
                if (i === 0) {
                    evalVarDeclaration(obj, "expr.left");
                }
                var list = evalExpr(obj, "expr.right");
                if (i == list.length) {
                    obj.memory.remove(stmt.expr.left.name);
                    obj.repeatStmtExpr = {};
                    break;
                }
                if (!isObject(list) && !obj.modifiedStmt) {
                    if (i < list.length) {
                        obj.memory.assign(stmt.expr.left.name, list[i]);
                        obj.program.prepend([obj.repeatStmtExpr]);
                        obj.program.prepend(stmt.stmtList);
                    }
                }
                break;
            case CONSTANTS.TIMES:
            case CONSTANTS.FOR:
                var from = evalExpr(obj, "expr", 1);
                var to = evalExpr(obj, "expr", 2);
                var step = evalExpr(obj, "expr", 3);

                if (!isObject(from) && !isObject(to) && !isObject(to) && !obj.modifiedStmt) {
                    if (obj.memory.get(stmt.expr[0].name) === undefined) {
                        obj.memory.decl(stmt.expr[0].name, from);
                    } else {
                        var oldValue = obj.memory.get(stmt.expr[0].name);
                        obj.memory.assign(stmt.expr[0].name, oldValue + step);
                    }
                    var left = obj.memory.get(stmt.expr[0].name);
                    if (left < to) {
                        obj.program.prepend([obj.repeatStmtExpr]);
                        obj.program.prepend(stmt.stmtList);
                        obj.repeatStmtExpr = {};
                    } else {
                        obj.memory.remove(stmt.expr[0].name);
                        obj.repeatStmtExpr = {};
                    }
                }
                break;
            default:
                var value = evalExpr(obj, "expr");
                if (!isObject(value) && !obj.modifiedStmt) {
                    if (value) {
                        obj.program.prepend([obj.repeatStmtExpr]);
                        obj.program.prepend(stmt.stmtList);
                    }
                    obj.repeatStmtExpr = {};
                }
        }
    };

    var evalIf = function (obj, stmt) {
        var programPrefix;
        var value;

        for (var k in stmt.exprList) {
            value = evalExpr(obj, "exprList." + k);
            if (!isObject(value) && value && !obj.modifiedStmt) {
                programPrefix = stmt.thenList[k];
                if (obj.program.isWait()) {
                    // obj.program.getRemove();
                    obj.program.setWait(false);
                }
                break;
            }
        }
        if ((programPrefix === undefined || programPrefix == []) && !obj.program.isWait() && !obj.modifiedStmt) {
            programPrefix = stmt.elseStmts;
        }
        obj.program.prepend(programPrefix);
        return value;
    };

    var evalWaitStmt = function (obj, stmt) {
        var stmtNotEvaluated = true;
        obj.program.setWait(true);
        if (UTIL.isEmpty(obj.repeatStmtExpr)) {
            obj.repeatStmtExpr = UTIL.clone(stmt);
        }
        for (var k in stmt.statements) {
            obj.currentStatement = stmt.statements[k];
            var value = evalIf(obj, stmt.statements[k]);
            obj.currentStatement = stmt;

            if (!isObject(value) && !obj.modifiedStmt) {
                if (value) {
                    stmtNotEvaluated = false;
                    break;
                }
            }
        }
        if (stmtNotEvaluated) {
            obj.program.prepend([obj.repeatStmtExpr]);
        }
        obj.repeatStmtExpr = {};
    };

    var evalExpr = function (obj, propName, arrayIndex) {
        var expr = UTIL.getPropertyFromObject(obj.currentStatement, propName, arrayIndex);
        var stmt = obj.currentStatement;
        obj.currentStatement = expr;
        var val = null;
        switch (expr.expr) {
            case CONSTANTS.NUM_CONST:
            case CONSTANTS.LED_COLOR_CONST:
            case CONSTANTS.BOOL_CONST:
            case CONSTANTS.COLOR_CONST:
            case CONSTANTS.STRING_CONST:
            case CONSTANTS.IMAGE_CONST:
                val = expr.value;
                break;
            case CONSTANTS.NULL_CONST:
                val = null;
                break;
            case CONSTANTS.ARRAY_NUMBER:
            case CONSTANTS.ARRAY_STRING:
            case CONSTANTS.ARRAY_COLOR:
            case CONSTANTS.ARRAY_BOOLEAN:
            case CONSTANTS.ARRAY_IMAGE:
                val = evalArray(obj);
                break;
            case CONSTANTS.CREATE_LIST_WITH_ITEM:
                val = evalCreateArrayWithItem(obj);
                break;
            case CONSTANTS.CREATE_LIST_LENGTH:
                val = evalListLength(obj);
                break;
            case CONSTANTS.CREATE_LIST_IS_EMPTY:
                val = evalListIsEmpty(obj);
                break;
            case CONSTANTS.CREATE_LIST_FIND_ITEM:
                val = evalListFindItem(obj);
                break;
            case CONSTANTS.CREATE_LISTS_GET_INDEX:
                val = evalListsGetIndex(obj, expr.op, expr.position);
                break;
            case CONSTANTS.TEXT_JOIN:
                val = evalTextJoin(obj);
                break;
            case CONSTANTS.CREATE_LISTS_GET_SUBLIST:
                val = evalListsGetSubList(obj);
                break;
            case CONSTANTS.VAR:
                val = obj.memory.get(expr.name);
                break;
            case CONSTANTS.BINARY:
                val = evalBinary(obj, expr.op);
                break;
            case CONSTANTS.UNARY:
                val = evalUnary(obj, expr.op);
                break;
            case CONSTANTS.TERNARY_EXPR:
                val = evalTernaryExpr(obj);
                break;
            case CONSTANTS.SINGLE_FUNCTION:
                val = evalSingleFunction(obj, expr.op);
                break;
            case CONSTANTS.RANDOM_INT:
                val = evalRandInt(obj);
                break;
            case CONSTANTS.RANDOM_DOUBLE:
                val = evalRandDouble();
                break;
            case CONSTANTS.MATH_CONSTRAIN_FUNCTION:
                val = evalMathConstrainFunct(obj);
                break;
            case CONSTANTS.MATH_ON_LIST:
                val = evalMathOnList(obj, expr.op);
                break;
            case CONSTANTS.MATH_PROP_FUNCT:
                val = evalMathPropFunct(obj, expr.op);
                break;
            case CONSTANTS.MATH_CONST:
                val = evalMathConst(obj, expr.value);
                break;
            case CONSTANTS.GET_SAMPLE:
                val = evalSensor(obj);
                break;
            case CONSTANTS.PIN_TOUCH_SENSOR:
                val = evalPinTouchSensor(obj);
                break;
            case CONSTANTS.PIN_GET_VALUE_SENSOR:
                val = evalPinGetValueSensor(obj);
                break;
            case CONSTANTS.GET_GYRO_SENSOR_SAMPLE:
                val = evalGyroSensor(obj);
                break;
            case CONSTANTS.ENCODER_SENSOR_SAMPLE:
                val = evalEncoderSensor(obj);
                break;
            case CONSTANTS.MOTOR_GET_POWER:
                val = evalMotorGetPowerAction(obj);
                break;
            case CONSTANTS.GET_VOLUME:
                val = evalGetVolume(obj);
                break;
            case CONSTANTS.DISPLAY_GET_BRIGHTNESS_ACTION:
                val = evalDisplayGetBrightnessAction(obj);
                break;
            case CONSTANTS.DISPLAY_GET_PIXEL_ACTION:
                val = evalDisplayGetPixelAction(obj);
                break;
            case CONSTANTS.METHOD_CALL_RETURN:
                var value = evalMethodCallReturn(obj, expr.name, expr.parameters);
                UTIL.setObjectProperty(stmt, propName, value, arrayIndex);
                obj.modifiedStmt = true;
                val = value;
                break;
            case CONSTANTS.RGB_COLOR_CONST:
                val = evalRgbColorConst(obj);
                break;
            case CONSTANTS.IMAGE_SHIFT_ACTION:
                val = evalImageShiftAction(obj, expr.direction);
                break;
            case CONSTANTS.IMAGE_INVERT_ACTION:
                val = evalImageInvertAction(obj);
                break;
            default:
                throw "Invalid Expression Type!";
        }
        obj.currentStatement = stmt;
        return val;
    };

    var evalSensor = function (obj) {
        var sensorType = UTIL.getPropertyFromObject(obj.currentStatement, "sensorType");
        var sensorMode = UTIL.getPropertyFromObject(obj.currentStatement, "sensorMode");
        if (sensorMode) {
            return obj.simulationData[sensorType][sensorMode];
        }
        return obj.simulationData[sensorType];
    };

    var evalPinTouchSensor = function (obj) {
        var pinNumber = UTIL.getPropertyFromObject(obj.currentStatement, "pin");
        return obj.simulationData[pinNumber].touched;
    };

    var evalPinGetValueSensor = function (obj) {
        var valueType = UTIL.getPropertyFromObject(obj.currentStatement, "type");
        var pinNumber = UTIL.getPropertyFromObject(obj.currentStatement, "pin");
        return obj.simulationData[pinNumber][valueType];
    };

    var evalGyroSensor = function (obj) {
        var sensorMode = UTIL.getPropertyFromObject(obj.currentStatement, "sensorMode");
        switch (sensorMode) {
            case CONSTANTS.ANGLE:
                return obj.gyro.getAngle();
            case CONSTANTS.RATE:
                return obj.gyro.getRate();
            default:
                throw "Invalid Gyro Mode!";
        }
    };

    var evalEncoderSensor = function (obj) {
        var motorSide = UTIL.getPropertyFromObject(obj.currentStatement, "motorSide");
        var sensorMode = UTIL.getPropertyFromObject(obj.currentStatement, "sensorMode");
        var value = obj.simulationData.encoder.right / 360.0;
        if (motorSide == CONSTANTS.MOTOR_LEFT) {
            value = obj.simulationData.encoder.left / 360.0;
        }
        switch (sensorMode) {
            case CONSTANTS.ROTATION:
                return value;
            case CONSTANTS.DEGREE:
                return value * 360.0;
            case CONSTANTS.DISTANCE:
                return value * (CONSTANTS.WHEEL_DIAMETER * 3.14);
            default:
                throw "Invalid Encoder Mode!";
        }
    };

    var evalGetVolume = function (obj) {
        return obj.simulationData[CONSTANTS.VOLUME];
    };

    var evalRgbColorConst = function (obj) {
        var red = evalExpr(obj, "value", 0);
        var green = evalExpr(obj, "value", 1);
        var blue = evalExpr(obj, "value", 2);
        if (!isObject(red) && !isObject(green) && !isObject(blue) && !obj.modifiedStmt) {
            return [red, green, blue];
        }
    };

    var evalConsoleDebugAction = function (obj) {
        var text = evalExpr(obj, "text");
        console.log(text);
    };

    var evalAssertStmt = function (obj, stmt) {
        var test = evalExpr(obj, "test");
        var text = evalExpr(obj, "text");
        var left = evalExpr(obj, "left");
        var op = evalExpr(obj, "op");
        var right = evalExpr(obj, "right");
        console.assert(test, text + " " + left + " " + op + " " + right);
    }

    var evalBinary = function (obj, op) {
        var valLeft = evalExpr(obj, "left");
        var valRight = evalExpr(obj, "right");
        if (!isObject(valLeft) && !isObject(valRight) && !obj.modifiedStmt) {
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
                    valLeft = isNumber(valLeft) ? UTIL.round(valLeft, 2) : valLeft;
                    valRight = isNumber(valRight) ? UTIL.round(valRight, 2) : valRight;
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
        }
    };

    var evalUnary = function (obj, op) {
        var val = evalExpr(obj, "value");
        if (!isObject(val) && !obj.modifiedStmt)
            switch (op) {
                case CONSTANTS.NEG:
                    return -val;
                case CONSTANTS.NOT:
                    return !val;
                default:
                    throw "Invalid Unary Operator";
            }
    };

    var evalSingleFunction = function (obj, functName) {
        var val = evalExpr(obj, "value");
        if (!isObject(val) && !obj.modifiedStmt) {
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
    };

    var evalMathConst = function (obj, mathConst) {
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

    var evalMathOnList = function (obj, op) {
        var listVal = evalExpr(obj, "list");
        if (!isObject(listVal) && !obj.modifiedStmt)
            switch (op) {
                case CONSTANTS.SUM:
                    return listVal.reduce(function (x, y) {
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

    var evalMathConstrainFunct = function (obj) {
        var val_ = evalExpr(obj, "value");
        var min_ = evalExpr(obj, "min");
        var max_ = evalExpr(obj, "max");
        if (!isObject(val_) && !isObject(min_) && !isObject(max_) && !obj.modifiedStmt)
            return Math.min(Math.max(val_, min_), max_);
    };

    var evalMathPropFunct = function (obj, functionName) {
        var val1 = evalExpr(obj, "arg1");
        var val2;
        if (UTIL.getPropertyFromObject(obj.currentStatement, "arg2")) {
            val2 = evalExpr(obj, "arg2");
        }
        if (!isObject(val1) && !isObject(val2) && !obj.modifiedStmt) {
            switch (functionName) {
                case 'EVEN':
                    return val1 % 2 === 0;
                case 'ODD':
                    return val1 % 2 !== 0;
                case 'PRIME':
                    return isPrime(val1);
                case 'WHOLE':
                    return Number(val1) === val1 && val1 % 1 === 0;
                case 'POSITIVE':
                    return val1 >= 0;
                case 'NEGATIVE':
                    return val1 < 0;
                case 'DIVISIBLE_BY':
                    return val1 % val2 === 0;
                default:
                    throw "Invalid Math Property Function Name";
            }
        }
    };

    function evalRandInt(obj) {
        var min_ = evalExpr(obj, "min");
        var max_ = evalExpr(obj, "max");
        if (!isObject(min_) && !isObject(max_) && !obj.modifiedStmt) {
            return math_random_int(min_, max_);
        }
    }

    var evalRandDouble = function () {
        return Math.random();
    };

    var evalArray = function (obj) {
        var values = UTIL.getPropertyFromObject(obj.currentStatement, "value");
        var result = [];
        for (var i = 0; i < values.length; i++) {
            result.push(evalExpr(obj, "value", i));
        }
        return result;
    };

    var evalCreateArrayWithItem = function (obj) {
        var size = evalExpr(obj, "size");
        var val = evalExpr(obj, "value");
        if (!isObject(size) && !isObject(val) && !obj.modifiedStmt) {
            var a = [];
            for (var i = 0; i < size; i++) {
                a.push(val);
            }
        }
        return a;
    };

    var evalListLength = function (obj) {
        var val = evalExpr(obj, "list");
        if (!isObject(val) && !obj.modifiedStmt)
            return val.length;
    };

    var evalListIsEmpty = function (obj) {
        var val = evalExpr(obj, "list");
        if (!isObject(val) && !obj.modifiedStmt)
            return val.length === 0;
    };

    var evalListFindItem = function (obj) {
        var list = evalExpr(obj, "list");
        var ite = evalExpr(obj, "item");
        if (!isObject(list) && !isObject(ite) && !obj.modifiedStmt) {
            if (obj.currentStatement.position == CONSTANTS.FIRST) {
                return list.indexOf(ite);
            }
            return list.lastIndexOf(ite);
        }
    };

    var evalListsGetIndex = function (obj, op, position) {
        var list = evalExpr(obj, "list");
        var it;
        if (UTIL.getPropertyFromObject(obj.currentStatement, "item")) {
            it = evalExpr(obj, "item");
        }
        if (!isObject(list) && !isObject(it) && !obj.modifiedStmt) {
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
        }
    };

    var evalListsGetIndexStmt = function (obj, stmt) {
        var list = evalExpr(obj, "list");
        var it;
        if (stmt.item) {
            it = evalExpr(obj, "item");
        }
        if (!isObject(list) && !isObject(it) && !obj.modifiedStmt) {
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
        }
    };

    var evalListsSetIndex = function (obj, stmt) {
        var list = evalExpr(obj, "list");
        var it;
        if (stmt.item) {
            it = evalExpr(obj, "item");
        }
        var newValue = evalExpr(obj, "value");
        if (!isObject(list) && !isObject(it) && !isObject(newValue) && !obj.modifiedStmt) {
            var insert = stmt.op == CONSTANTS.INSERT;
            switch (stmt.position) {
                case CONSTANTS.FROM_START:
                    if (insert) {
                        list.splice(it, 0, newValue);
                        break;
                    }
                    list[it] = newValue;
                    break;
                case CONSTANTS.FROM_END:
                    if (insert) {
                        list.splice(list.length - it - 1, 0, newValue);
                        break;
                    }
                    list[list.length - it - 1] = newValue;
                    break;
                case CONSTANTS.FIRST:
                    if (insert) {
                        list.unshift(newValue);
                        break;
                    }
                    list[0] = newValue;
                    break;
                case CONSTANTS.LAST:
                    if (insert) {
                        list.push(newValue);
                        break;
                    }
                    list[list.length - 1] = newValue;
                    break;
                case CONSTANTS.RANDOM:
                    var tmp_x = Math.floor(Math.random() * list.length);
                    if (insert) {
                        list.splice(tmp_x, 0, newValue);
                        break;
                    }
                    list[tmp_x] = newValue;
                    break;
                default:
                    throw "Position on list is not supported!";
            }
        }
    };

    var evalListsGetSubList = function (obj) {
        var list = evalExpr(obj, "list");
        var at1 = 1;
        if (obj.currentStatement.at1) {
            at1 = evalExpr(obj, "at1");
        }
        var at2 = 1;
        if (obj.currentStatement.at2) {
            at2 = evalExpr(obj, "at2");
        }
        if (obj.currentStatement.where1 == CONSTANTS.FIRST && obj.currentStatement.where2 == CONSTANTS.LAST) {
            return list.concat();
        }
        if (!isObject(list) && !isObject(list) && !isObject(list) && !obj.modifiedStmt)
            return listsGetSubList(list, obj.currentStatement.where1, at1, obj.currentStatement.where2, at2);
    };

    var evalTernaryExpr = function (obj) {
        var condVal = evalExpr(obj, "exprList");
        if (condVal) {
            return evalExpr(obj, "thenList");
        }
        return evalExpr(obj, "elseStmts");
    };

    var isPrime = function (n) {
        if (isNaN(n) || !isFinite(n) || n % 1 || n < 2) {
            return false;
        }
        if (n == leastFactor(n)) {
            return true;
        }
        return false;
    };

    var listsRemoveFromEnd = function (list, x) {
        x = list.length - x;
        return list.splice(x, 1)[0];
    };

    var listsGetRandomItem = function (list, remove) {
        var x = Math.floor(Math.random() * list.length);
        if (remove) {
            return list.splice(x, 1)[0];
        } else {
            return list[x];
        }
    };

    var listsGetSubList = function (list, where1, at1, where2, at2) {
        function getAt(where, at) {
            if (where == CONSTANTS.FROM_START) {
                at = at;
            } else if (where == CONSTANTS.FROM_END) {
                at = list.length - 1 - at;
            } else if (where == CONSTANTS.FIRST) {
                at = 0;
            } else if (where == CONSTANTS.LAST) {
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

    var evalTextJoin = function (obj) {
        var values = UTIL.getPropertyFromObject(obj.currentStatement, "value");
        var result = "";
        for (var i = 0; i < values.length; i++) {
            var val = evalExpr(obj, "value", i);
            val = roundIfSensorData(val, values[i].expr);
            result += String(val);
        }
        return result;
    };

    var leastFactor = function (n) {
        if (isNaN(n) || !isFinite(n)) {
            return NaN;
        }
        if (n === 0) {
            return 0;
        }
        if (n % 1 || n * n < 2) {
            return 1;
        }
        if (n % 2 === 0) {
            return 2;
        }
        if (n % 3 === 0) {
            return 3;
        }
        if (n % 5 === 0) {
            return 5;
        }
        var m = Math.sqrt(n);
        for (var i = 7; i <= m; i += 30) {
            if (n % i === 0) {
                return i;
            }
            if (n % (i + 4) === 0) {
                return i + 4;
            }
            if (n % (i + 6) === 0) {
                return i + 6;
            }
            if (n % (i + 10) === 0) {
                return i + 10;
            }
            if (n % (i + 12) === 0) {
                return i + 12;
            }
            if (n % (i + 16) === 0) {
                return i + 16;
            }
            if (n % (i + 22) === 0) {
                return i + 22;
            }
            if (n % (i + 24) === 0) {
                return i + 24;
            }
        }
        return n;
    };

    var math_random_int = function (a, b) {
        if (a > b) {
            // Swap a and b to ensure a is smaller.
            var c = a;
            a = b;
            b = c;
        }
        return Math.floor(Math.random() * (b - a + 1) + a);
    };

    var mathMean = function (myList) {
        return myList.reduce(function (x, y) {
            return x + y;
        }) / myList.length;
    };

    var mathMedian = function (myList) {
        var localList = myList.filter(function (x) {
            return typeof x == 'number';
        });
        if (!localList.length) {
            return null;
        }
        localList.sort(function (a, b) {
            return b - a;
        });
        if (localList.length % 2 === 0) {
            return (localList[localList.length / 2 - 1] + localList[localList.length / 2]) / 2;
        } else {
            return localList[(localList.length - 1) / 2];
        }
    };

    var mathStandardDeviation = function (numbers) {
        var n = numbers.length;
        if (!n) {
            return null;
        }
        var mean = numbers.reduce(function (x, y) {
            return x + y;
        }) / n;
        var variance = 0;
        for (var j = 0; j < n; j++) {
            variance += Math.pow(numbers[j] - mean, 2);
        }
        variance = variance / n;
        return Math.sqrt(variance);
    };

    var mathRandomList = function (list) {
        var x = Math.floor(Math.random() * list.length);
        return list[x];
    };

    var isNumber = function (n) {
        return !isNaN(parseFloat(n)) && isFinite(n);
    };

    var invertImage = function (image) {
        for (var i = 0; i < image.length; i++) {
            for (var j = 0; j < image[i].length; j++) {
                image[i][j] = Math.abs(255 - image[i][j]);
            }
        }
        return image;
    };

    var shiftImage = function (image, direction, n) {
        n = Math.round(n);
        var shift = {
            down: function () {
                image.pop();
                image.unshift([0, 0, 0, 0, 0]);
            },
            up: function () {
                image.shift();
                image.push([0, 0, 0, 0, 0]);
            },
            right: function () {
                image.forEach(function (array) {
                    array.pop();
                    array.unshift(0);
                });
            },
            left: function () {
                image.forEach(function (array) {
                    array.shift();
                    array.push(0);
                });
            }
        };
        if (n < 0) {
            n *= -1;
            if (direction === "up") {
                direction = "down";
            } else if (direction === "down") {
                direction = "up";
            } else if (direction === "left") {
                direction = "right";
            } else if (direction === "right") {
                direction = "left";
            }
        }
        for (var i = 0; i < n; i++) {
            shift[direction]();
        }
        return image;
    };

    return ProgramEval;
});
