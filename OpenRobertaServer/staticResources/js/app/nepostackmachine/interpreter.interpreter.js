define(["require", "exports", "./interpreter.state", "./interpreter.constants", "./interpreter.util", "neuralnetwork.playground"], function (require, exports, interpreter_state_1, C, U, PG) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.Interpreter = void 0;
    var Interpreter = /** @class */ (function () {
        /*
         *
         * . @param generatedCode argument contains the operations and the function definitions
         * . @param robotBehaviour implementation of the ARobotBehaviour class
         * . @param cbOnTermination is called when the program has terminated
        */
        function Interpreter(generatedCode, configuration, r, cbOnTermination, simBreakpoints) {
            this.terminated = false;
            this.callbackOnTermination = undefined;
            this.debugDelay = 2;
            this.terminated = false;
            this.callbackOnTermination = cbOnTermination;
            var stmts = generatedCode[C.OPS];
            this.robotBehaviour = r;
            r.setConfiguration(configuration);
            this.breakpoints = simBreakpoints;
            this.events = {};
            this.events[C.DEBUG_STEP_INTO] = false;
            this.events[C.DEBUG_BREAKPOINT] = false;
            this.events[C.DEBUG_STEP_OVER] = false;
            this.lastBlock = null;
            this.lastStoppedBlock = null;
            this.stepOverBlock = null;
            this.state = new interpreter_state_1.State(stmts);
        }
        /**
         * run the operations.
         * . @param maxRunTime the time stamp at which the run method must have terminated. If 0 run as long as possible.
         */
        Interpreter.prototype.run = function (maxRunTime) {
            return this.evalOperation(maxRunTime);
        };
        /**
         * return true, if the program is terminated
         */
        Interpreter.prototype.isTerminated = function () {
            return this.terminated;
        };
        /**
         * force the termination of the program. The termination takes place, when the NEXT operation should be executed. The delay is not significant.
         * The callbackOnTermination function is called
         */
        Interpreter.prototype.terminate = function () {
            this.terminated = true;
            this.callbackOnTermination();
            this.robotBehaviour.close();
            this.state.removeHighlights([]);
        };
        Interpreter.prototype.getRobotBehaviour = function () {
            return this.robotBehaviour;
        };
        /** Returns the map of interpreters variables */
        Interpreter.prototype.getVariables = function () {
            return this.state.getVariables();
        };
        /** Removes all highlights from currently executing blocks*/
        Interpreter.prototype.removeHighlights = function () {
            this.state.removeHighlights([]);
        };
        /** Sets the debug mode*/
        Interpreter.prototype.setDebugMode = function (mode) {
            this.state.setDebugMode(mode);
            if (mode) {
                stackmachineJsHelper.getJqueryObject('#blockly').addClass('debug');
                this.state.addHighlights(this.breakpoints);
            }
            else {
                this.state.removeHighlights(this.breakpoints);
                stackmachineJsHelper.getJqueryObject('#blockly').removeClass('debug');
            }
        };
        /** sets relevant event value to true */
        Interpreter.prototype.addEvent = function (mode) {
            this.events[mode] = true;
        };
        /** sets relevant event value to false */
        Interpreter.prototype.removeEvent = function (mode) {
            this.events[mode] = false;
        };
        /**
         * the central interpreter. It is a stack machine interpreting operations given as JSON objects. The operations are all IMMUTABLE. It
         * - uses the this.state component to store the state of the interpretation.
         * - uses the this.robotBehaviour component for accessing hardware sensors and actors
         *
         * if the program is not terminated, it will take one operation after the other and execute it. The property C.OPCODE contains the
         * operation code and is used for switching to the various operations implementations. For some operation codes the implementations is extracted to
         * special functions (repeat, expression) for better readability.
         *
         * The state of the interpreter consists of
         * - a stack of computed values
         * - the actual array of operations to be executed now, including a program counter as index into the array
         * - a hash map of bindings. A binding map a name as key to an array of values. This implements hiding of variables.
         *
         * Each operation code implementation may
         * - create new bindings of values to names (variable declaration)
         * - change the values of the binding (assign)
         * - push and pop values to the stack (expressions)
         *
         * Debugging functions:
         * -StepOver will step over a given line. If the line contains a function the function will be executed and the result returned without debugging each line.
         * -StepInto If the line does not contain a function it behaves the same as “step over” but if it does the debugger will enter the called function
         * and continue line-by-line debugging there.
         * -BreakPoint will continue execution until the next breakpoint is reached or the program exits.
         */
        Interpreter.prototype.evalOperation = function (maxRunTime) {
            while (maxRunTime >= new Date().getTime() && !this.robotBehaviour.getBlocking()) {
                var op = this.state.getOp();
                this.state.evalHighlightings(op, this.lastBlock);
                if (this.state.getDebugMode()) {
                    var canContinue = this.calculateDebugBehaviour(op);
                    if (!canContinue)
                        return 0;
                }
                var _a = this.evalSingleOperation(op), result = _a[0], stop_1 = _a[1];
                this.lastStoppedBlock = null;
                this.lastBlock = op;
                if (result > 0 || stop_1) {
                    return result;
                }
                if (this.terminated) {
                    // termination either requested by the client or by executing 'stop' or after last statement
                    this.robotBehaviour.close();
                    this.callbackOnTermination();
                    return 0;
                }
                if (this.state.getDebugMode()) {
                    return this.debugDelay;
                }
            }
            return 0;
        };
        /**
         * Is responsible for all debugging behavior
         * @param op
         * @return whether the interpreter can continue evaluating the operation
         * @private
         */
        Interpreter.prototype.calculateDebugBehaviour = function (op) {
            if (this.events[C.DEBUG_BREAKPOINT] && Interpreter.isBreakPoint(op, this.breakpoints) && op !== this.lastStoppedBlock) {
                this.breakPoint(op);
                return false;
            }
            if (this.events[C.DEBUG_STEP_INTO] && Interpreter.isPossibleStepInto(op) && op !== this.lastStoppedBlock) {
                this.stepInto(op);
                return false;
            }
            if (this.events[C.DEBUG_STEP_OVER]) {
                if (this.stepOverBlock !== null && !this.state.beingExecuted(this.stepOverBlock) && Interpreter.isPossibleStepInto(op)) {
                    this.stepOver(op);
                    return false;
                }
                else if (this.stepOverBlock === null && Interpreter.isPossibleStepOver(op)) {
                    this.stepOverBlock = op;
                }
                else if (this.stepOverBlock === null && this.lastStoppedBlock !== op && Interpreter.isPossibleStepInto(op)) {
                    this.stepOver(op);
                    return false;
                }
            }
            return true;
        };
        Interpreter.prototype.stepOver = function (op) {
            stackmachineJsHelper.setSimBreak();
            this.events[C.DEBUG_STEP_OVER] = false;
            this.stepOverBlock = null;
            this.lastStoppedBlock = op;
        };
        Interpreter.prototype.stepInto = function (op) {
            stackmachineJsHelper.setSimBreak();
            this.events[C.DEBUG_STEP_INTO] = false;
            this.lastStoppedBlock = op;
        };
        Interpreter.prototype.breakPoint = function (op) {
            stackmachineJsHelper.setSimBreak();
            this.events[C.DEBUG_BREAKPOINT] = false;
            this.lastStoppedBlock = op;
        };
        /**
         *  called from @see evalOperation() to evaluate all the operations
         *
         * @param stmt the operation to be evaluated
         * @returns [result,stop] result will be time required till next instruction and stop indicates if evalOperation should return result or not.
         */
        Interpreter.prototype.evalSingleOperation = function (stmt) {
            this.state.opLog('actual ops: ');
            this.state.incrementProgramCounter();
            if (stmt === undefined) {
                U.debug('PROGRAM TERMINATED. No ops remaining');
                this.terminated = true;
            }
            else {
                var opCode = stmt[C.OPCODE];
                switch (opCode) {
                    case C.JUMP: {
                        var condition = stmt[C.CONDITIONAL];
                        if (condition === C.ALWAYS || this.state.pop() === condition) {
                            this.state.pc = stmt[C.TARGET];
                        }
                        break;
                    }
                    case C.ASSIGN_STMT: {
                        var name_1 = stmt[C.NAME];
                        this.state.setVar(name_1, this.state.pop());
                        break;
                    }
                    case C.CLEAR_DISPLAY_ACTION: {
                        this.robotBehaviour.clearDisplay();
                        return [0, true];
                    }
                    case C.CREATE_DEBUG_ACTION: {
                        U.debug('NYI');
                        break;
                    }
                    case C.EXPR:
                        this.evalExpr(stmt);
                        break;
                    case C.GET_SAMPLE: {
                        this.robotBehaviour.getSample(this.state, stmt[C.NAME], stmt[C.GET_SAMPLE], stmt[C.PORT], stmt[C.MODE]);
                        break;
                    }
                    case C.NNSTEP_STMT:
                        this.evalNNStep();
                        break;
                    case C.LED_ON_ACTION: {
                        var color_1 = this.state.pop();
                        this.robotBehaviour.ledOnAction(stmt[C.NAME], stmt[C.PORT], color_1);
                        break;
                    }
                    case C.RETURN:
                        var returnValue = void 0;
                        if (stmt[C.VALUES])
                            returnValue = this.state.pop();
                        var returnAddress = this.state.pop();
                        this.state.pc = returnAddress;
                        if (stmt[C.VALUES])
                            this.state.push(returnValue);
                        break;
                    case C.MOTOR_ON_ACTION: {
                        var speedOnly = stmt[C.SPEED_ONLY];
                        var duration = speedOnly ? undefined : this.state.pop();
                        var speed = this.state.pop();
                        var name_2 = stmt[C.NAME];
                        var port = stmt[C.PORT];
                        var durationType = stmt[C.MOTOR_DURATION];
                        return [this.robotBehaviour.motorOnAction(name_2, port, duration, durationType, speed), true];
                    }
                    case C.DRIVE_ACTION: {
                        var speedOnly = stmt[C.SPEED_ONLY];
                        var setTime = stmt[C.SET_TIME];
                        var name_3 = stmt[C.NAME];
                        var time = void 0;
                        var distance = void 0;
                        if (setTime) {
                            distance = undefined;
                            time = setTime ? this.state.pop() : undefined;
                        }
                        else {
                            time = undefined;
                            distance = speedOnly ? undefined : this.state.pop();
                        }
                        var speed = this.state.pop();
                        var direction = stmt[C.DRIVE_DIRECTION];
                        var duration = this.robotBehaviour.driveAction(name_3, direction, speed, distance, time);
                        return [duration, true];
                    }
                    case C.TURN_ACTION: {
                        var speedOnly = stmt[C.SPEED_ONLY];
                        var setTime = stmt[C.SET_TIME];
                        var time = void 0;
                        var angle = void 0;
                        if (setTime) {
                            angle = undefined;
                            time = setTime ? this.state.pop() : undefined;
                        }
                        else {
                            time = undefined;
                            angle = speedOnly ? undefined : this.state.pop();
                        }
                        var speed = this.state.pop();
                        var name_4 = stmt[C.NAME];
                        var direction = stmt[C.TURN_DIRECTION];
                        var duration = this.robotBehaviour.turnAction(name_4, direction, speed, angle, time);
                        return [duration, true];
                    }
                    case C.CURVE_ACTION: {
                        var speedOnly = stmt[C.SPEED_ONLY];
                        var setTime = stmt[C.SET_TIME];
                        var time = void 0;
                        var distance = void 0;
                        if (setTime) {
                            distance = undefined;
                            time = setTime ? this.state.pop() : undefined;
                        }
                        else {
                            time = undefined;
                            distance = speedOnly ? undefined : this.state.pop();
                        }
                        var speedR = this.state.pop();
                        var speedL = this.state.pop();
                        var name_5 = stmt[C.NAME];
                        var direction = stmt[C.DRIVE_DIRECTION];
                        var duration = this.robotBehaviour.curveAction(name_5, direction, speedL, speedR, distance, time);
                        return [duration, true];
                    }
                    case C.STOP_DRIVE:
                        var name_6 = stmt[C.NAME];
                        this.robotBehaviour.driveStop(name_6);
                        return [0, true];
                    case C.BOTH_MOTORS_ON_ACTION: {
                        var duration = this.state.pop();
                        var speedB = this.state.pop();
                        var speedA = this.state.pop();
                        var portA = stmt[C.PORT_A];
                        var portB = stmt[C.PORT_B];
                        this.robotBehaviour.motorOnAction(portA, portA, duration, undefined, speedA);
                        this.robotBehaviour.motorOnAction(portB, portB, duration, undefined, speedB);
                        return [duration, true];
                    }
                    case C.MOTOR_STOP: {
                        this.robotBehaviour.motorStopAction(stmt[C.NAME], stmt[C.PORT]);
                        return [0, true];
                    }
                    case C.MOTOR_SET_POWER: {
                        var speed = this.state.pop();
                        var name_7 = stmt[C.NAME];
                        var port = stmt[C.PORT];
                        this.robotBehaviour.setMotorSpeed(name_7, port, speed);
                        return [0, true];
                    }
                    case C.MOTOR_GET_POWER: {
                        var port = stmt[C.PORT];
                        this.robotBehaviour.getMotorSpeed(this.state, name_6, port);
                        break;
                    }
                    case C.SHOW_TEXT_ACTION: {
                        var text = this.state.pop();
                        var name_8 = stmt[C.NAME];
                        if (name_8 === 'ev3') {
                            var x = this.state.pop();
                            var y = this.state.pop();
                            this.robotBehaviour.showTextActionPosition(text, x, y);
                            return [0, true];
                        }
                        return [this.robotBehaviour.showTextAction(text, stmt[C.MODE]), true];
                    }
                    case C.SHOW_IMAGE_ACTION: {
                        var image = void 0;
                        if (stmt[C.NAME] == 'ev3') {
                            image = stmt[C.IMAGE];
                        }
                        else {
                            image = this.state.pop();
                        }
                        return [this.robotBehaviour.showImageAction(image, stmt[C.MODE]), true];
                    }
                    case C.DISPLAY_SET_BRIGHTNESS_ACTION: {
                        var b = this.state.pop();
                        return [this.robotBehaviour.displaySetBrightnessAction(b), true];
                    }
                    case C.IMAGE_SHIFT_ACTION: {
                        var nShift = this.state.pop();
                        var image = this.state.pop();
                        if (stmt[C.NAME] === 'mbot') {
                            this.state.push(this.shiftImageActionMbot(image, stmt[C.DIRECTION], nShift));
                        }
                        else {
                            this.state.push(this.shiftImageAction(image, stmt[C.DIRECTION], nShift));
                        }
                        break;
                    }
                    case C.DISPLAY_SET_PIXEL_BRIGHTNESS_ACTION: {
                        var b = this.state.pop();
                        var y = this.state.pop();
                        var x = this.state.pop();
                        return [this.robotBehaviour.displaySetPixelBrightnessAction(x, y, b), true];
                    }
                    case C.DISPLAY_GET_PIXEL_BRIGHTNESS_ACTION: {
                        var y = this.state.pop();
                        var x = this.state.pop();
                        this.robotBehaviour.displayGetPixelBrightnessAction(this.state, x, y);
                        break;
                    }
                    case C.LIGHT_ACTION:
                        var color = void 0;
                        if (stmt[C.NAME] === 'mbot') {
                            var rgb = this.state.pop();
                            color = 'rgb(' + rgb[0] + ',' + rgb[1] + ',' + rgb[2] + ')';
                        }
                        else {
                            color = stmt[C.COLOR];
                        }
                        this.robotBehaviour.lightAction(stmt[C.MODE], color, stmt[C.PORT]);
                        return [0, true];
                    case C.STATUS_LIGHT_ACTION:
                        this.robotBehaviour.statusLightOffAction(stmt[C.NAME], stmt[C.PORT]);
                        return [0, true];
                    case C.STOP:
                        U.debug('PROGRAM TERMINATED. stop op');
                        this.terminated = true;
                        break;
                    case C.TEXT_JOIN: {
                        var n = stmt[C.NUMBER];
                        var result = new Array(n);
                        for (var i = 0; i < n; i++) {
                            var e = this.state.pop();
                            result[n - i - 1] = e;
                        }
                        this.state.push(result.join(''));
                        break;
                    }
                    case C.TIMER_SENSOR_RESET:
                        this.robotBehaviour.timerReset(stmt[C.PORT]);
                        break;
                    case C.ENCODER_SENSOR_RESET:
                        this.robotBehaviour.encoderReset(stmt[C.PORT]);
                        return [0, true];
                    case C.GYRO_SENSOR_RESET:
                        this.robotBehaviour.gyroReset(stmt[C.PORT]);
                        return [0, true];
                    case C.TONE_ACTION: {
                        var duration = this.state.pop();
                        var frequency = this.state.pop();
                        return [this.robotBehaviour.toneAction(stmt[C.NAME], frequency, duration), true];
                    }
                    case C.PLAY_FILE_ACTION:
                        return [this.robotBehaviour.playFileAction(stmt[C.FILE]), true];
                    case C.SET_VOLUME_ACTION:
                        this.robotBehaviour.setVolumeAction(this.state.pop());
                        return [0, true];
                    case C.GET_VOLUME:
                        this.robotBehaviour.getVolumeAction(this.state);
                        break;
                    case C.SET_LANGUAGE_ACTION:
                        this.robotBehaviour.setLanguage(stmt[C.LANGUAGE]);
                        break;
                    case C.SAY_TEXT_ACTION: {
                        var pitch = this.state.pop();
                        var speed = this.state.pop();
                        var text = this.state.pop();
                        return [this.robotBehaviour.sayTextAction(text, speed, pitch), true];
                    }
                    case C.UNBIND_VAR:
                        var variableToUnbind = stmt[C.NAME];
                        this.state.unbindVar(variableToUnbind);
                        break;
                    case C.VAR_DECLARATION: {
                        var name_9 = stmt[C.NAME];
                        this.state.bindVar(name_9, this.state.pop());
                        break;
                    }
                    case C.WAIT_TIME_STMT: {
                        var time = this.state.pop();
                        return [time, true]; // wait for handler being called
                    }
                    case C.WRITE_PIN_ACTION: {
                        var value = this.state.pop();
                        var mode = stmt[C.MODE];
                        var pin = stmt[C.PIN];
                        this.robotBehaviour.writePinAction(pin, mode, value);
                        return [0, true];
                    }
                    case C.LIST_OPERATION: {
                        var op = stmt[C.OP];
                        var loc = stmt[C.POSITION];
                        var ix = 0;
                        if (loc != C.LAST && loc != C.FIRST) {
                            ix = this.state.pop();
                        }
                        var value = this.state.pop();
                        var list = this.state.pop();
                        ix = this.getIndex(list, loc, ix);
                        if (op == C.SET) {
                            list[ix] = value;
                        }
                        else if (op == C.INSERT) {
                            if (loc === C.LAST) {
                                list.splice(ix + 1, 0, value);
                            }
                            else {
                                list.splice(ix, 0, value);
                            }
                        }
                        break;
                    }
                    case C.TEXT_APPEND:
                    case C.MATH_CHANGE: {
                        var value = this.state.pop();
                        var name_10 = stmt[C.NAME];
                        this.state.bindVar(name_10, this.state.pop() + value);
                        break;
                    }
                    case C.DEBUG_ACTION: {
                        var value = this.state.pop();
                        this.robotBehaviour.debugAction(value);
                        break;
                    }
                    case C.ASSERT_ACTION: {
                        var right = this.state.pop();
                        var left = this.state.pop();
                        var value = this.state.pop();
                        this.robotBehaviour.assertAction(stmt[C.MSG], left, stmt[C.OP], right, value);
                        break;
                    }
                    case C.COMMENT: {
                        break;
                    }
                    default:
                        U.dbcException('invalid stmt op: ' + opCode);
                }
            }
            return [0, false];
        };
        /**
         *  called from @see evalSingleOperation() to evaluate all kinds of expressions
         *
         * . @param expr to be evaluated
         */
        Interpreter.prototype.evalExpr = function (expr) {
            var _a;
            var kind = expr[C.EXPR];
            switch (kind) {
                case C.VAR:
                    this.state.push(this.state.getVar(expr[C.NAME]));
                    break;
                case C.NUM_CONST:
                    this.state.push(+expr[C.VALUE]);
                    break;
                case C.CREATE_LIST: {
                    var n = expr[C.NUMBER];
                    var arr = new Array(n);
                    for (var i = 0; i < n; i++) {
                        var e = this.state.pop();
                        arr[n - i - 1] = e;
                    }
                    this.state.push(arr);
                    break;
                }
                case C.CREATE_LIST_REPEAT: {
                    var rep = this.state.pop();
                    var val = this.state.pop();
                    var arr = new Array();
                    for (var i = 0; i < rep; i++) {
                        arr[i] = val;
                    }
                    this.state.push(arr);
                    break;
                }
                case C.BOOL_CONST:
                    this.state.push(expr[C.VALUE]);
                    break;
                case C.STRING_CONST:
                    this.state.push(expr[C.VALUE]);
                    break;
                case C.COLOR_CONST:
                    this.state.push(expr[C.VALUE]);
                    break;
                case C.IMAGE:
                    this.state.push(expr[C.VALUE]);
                    break;
                case C.RGB_COLOR_CONST: {
                    var b = this.state.pop();
                    var g = this.state.pop();
                    var r = this.state.pop();
                    this.state.push([r, g, b]);
                    break;
                }
                case C.UNARY: {
                    var subOp = expr[C.OP];
                    switch (subOp) {
                        case C.NOT:
                            var truthy;
                            var bool = this.state.pop();
                            if (bool === 'true') {
                                truthy = true;
                            }
                            else if (bool === 'false' || bool === '0' || bool === '') {
                                truthy = false;
                            }
                            else {
                                truthy = !!bool;
                            }
                            this.state.push(!truthy);
                            break;
                        case C.NEG:
                            var value_1 = this.state.pop();
                            this.state.push(-value_1);
                            break;
                        default:
                            U.dbcException('invalid unary expr subOp: ' + subOp);
                    }
                    break;
                }
                case C.MATH_CONST: {
                    var value_2 = expr[C.VALUE];
                    switch (value_2) {
                        case 'PI':
                            this.state.push(Math.PI);
                            break;
                        case 'E':
                            this.state.push(Math.E);
                            break;
                        case 'GOLDEN_RATIO':
                            this.state.push((1.0 + Math.sqrt(5.0)) / 2.0);
                            break;
                        case 'SQRT2':
                            this.state.push(Math.SQRT2);
                            break;
                        case 'SQRT1_2':
                            this.state.push(Math.SQRT1_2);
                            break;
                        case 'INFINITY':
                            this.state.push(Infinity);
                            break;
                        default:
                            throw 'Invalid Math Constant Name';
                    }
                    break;
                }
                case C.SINGLE_FUNCTION: {
                    var subOp = expr[C.OP];
                    var value_3 = this.state.pop();
                    U.debug('---------- ' + subOp + ' with ' + value_3);
                    switch (subOp) {
                        case 'SQUARE':
                            this.state.push(Math.pow(value_3, 2));
                            break;
                        case 'ROOT':
                            this.state.push(Math.sqrt(value_3));
                            break;
                        case 'ABS':
                            this.state.push(Math.abs(value_3));
                            break;
                        case 'LN':
                            this.state.push(Math.log(value_3));
                            break;
                        case 'LOG10':
                            this.state.push(Math.log(value_3) / Math.LN10);
                            break;
                        case 'EXP':
                            this.state.push(Math.exp(value_3));
                            break;
                        case 'POW10':
                            this.state.push(Math.pow(10, value_3));
                            break;
                        case 'SIN':
                            this.state.push(Math.sin(value_3));
                            break;
                        case 'COS':
                            this.state.push(Math.cos(value_3));
                            break;
                        case 'TAN':
                            this.state.push(Math.tan(value_3));
                            break;
                        case 'ASIN':
                            this.state.push(Math.asin(value_3));
                            break;
                        case 'ATAN':
                            this.state.push(Math.atan(value_3));
                            break;
                        case 'ACOS':
                            this.state.push(Math.acos(value_3));
                            break;
                        case 'ROUND':
                            this.state.push(Math.round(value_3));
                            break;
                        case 'ROUNDUP':
                            this.state.push(Math.ceil(value_3));
                            break;
                        case 'ROUNDDOWN':
                            this.state.push(Math.floor(value_3));
                            break;
                        case C.IMAGE_INVERT_ACTION:
                            this.state.push(this.invertImage(value_3));
                            break;
                        default:
                            throw 'Invalid Function Name';
                    }
                    break;
                }
                case C.MATH_CONSTRAIN_FUNCTION: {
                    var max_1 = this.state.pop();
                    var min_1 = this.state.pop();
                    var value_4 = this.state.pop();
                    this.state.push(Math.min(Math.max(value_4, min_1), max_1));
                    break;
                }
                case C.RANDOM_INT: {
                    var max = this.state.pop();
                    var min = this.state.pop();
                    if (min > max) {
                        _a = [max, min], min = _a[0], max = _a[1];
                    }
                    this.state.push(Math.floor(Math.random() * (max - min + 1) + min));
                    break;
                }
                case C.RANDOM_DOUBLE:
                    this.state.push(Math.random());
                    break;
                case C.MATH_PROP_FUNCT: {
                    var subOp = expr[C.OP];
                    var value_5 = this.state.pop();
                    switch (subOp) {
                        case 'EVEN':
                            this.state.push(this.isWhole(value_5) && value_5 % 2 === 0);
                            break;
                        case 'ODD':
                            this.state.push(this.isWhole(value_5) && value_5 % 2 !== 0);
                            break;
                        case 'PRIME':
                            this.state.push(this.isPrime(value_5));
                            break;
                        case 'WHOLE':
                            this.state.push(this.isWhole(value_5));
                            break;
                        case 'POSITIVE':
                            this.state.push(value_5 >= 0);
                            break;
                        case 'NEGATIVE':
                            this.state.push(value_5 < 0);
                            break;
                        case 'DIVISIBLE_BY':
                            var first = this.state.pop();
                            this.state.push(first % value_5 === 0);
                            break;
                        default:
                            throw 'Invalid Math Property Function Name';
                    }
                    break;
                }
                case C.MATH_ON_LIST: {
                    var subOp = expr[C.OP];
                    var value_6 = this.state.pop();
                    switch (subOp) {
                        case C.SUM:
                            this.state.push(this.sum(value_6));
                            break;
                        case C.MIN:
                            this.state.push(this.min(value_6));
                            break;
                        case C.MAX:
                            this.state.push(this.max(value_6));
                            break;
                        case C.AVERAGE:
                            this.state.push(this.mean(value_6));
                            break;
                        case C.MEDIAN:
                            this.state.push(this.median(value_6));
                            break;
                        case C.STD_DEV:
                            this.state.push(this.std(value_6));
                            break;
                        case C.RANDOM:
                            this.state.push(value_6[this.getRandomInt(value_6.length)]);
                            break;
                        default:
                            throw 'Invalid Math on List Function Name';
                    }
                    break;
                }
                case C.CAST_STRING: {
                    var num = this.state.pop();
                    this.state.push(num.toString());
                    break;
                }
                case C.CAST_CHAR: {
                    var num = this.state.pop();
                    this.state.push(String.fromCharCode(num));
                    break;
                }
                case C.CAST_STRING_NUMBER: {
                    var value = this.state.pop();
                    this.state.push(parseFloat(value));
                    break;
                }
                case C.CAST_CHAR_NUMBER: {
                    var index = this.state.pop();
                    var value = this.state.pop();
                    this.state.push(value.charCodeAt(index));
                    break;
                }
                case C.LIST_OPERATION: {
                    var subOp = expr[C.OP];
                    switch (subOp) {
                        case C.LIST_IS_EMPTY:
                            this.state.push(this.state.pop().length == 0);
                            break;
                        case C.LIST_LENGTH:
                            this.state.push(this.state.pop().length);
                            break;
                        case C.LIST_FIND_ITEM:
                            {
                                var item = this.state.pop();
                                var list = this.state.pop();
                                if (expr[C.POSITION] == C.FIRST) {
                                    this.state.push(list.indexOf(item));
                                }
                                else {
                                    this.state.push(list.lastIndexOf(item));
                                }
                            }
                            break;
                        case C.GET:
                        case C.REMOVE:
                        case C.GET_REMOVE:
                            {
                                var loc = expr[C.POSITION];
                                var ix = 0;
                                if (loc != C.LAST && loc != C.FIRST) {
                                    ix = this.state.pop();
                                }
                                var list = this.state.pop();
                                ix = this.getIndex(list, loc, ix);
                                var v = list[ix];
                                if (subOp == C.GET_REMOVE || subOp == C.GET) {
                                    this.state.push(v);
                                }
                                if (subOp == C.GET_REMOVE || subOp == C.REMOVE) {
                                    list.splice(ix, 1);
                                }
                            }
                            break;
                        case C.LIST_GET_SUBLIST:
                            {
                                var position = expr[C.POSITION];
                                var start_ix = void 0;
                                var end_ix = void 0;
                                if (position[1] != C.LAST) {
                                    end_ix = this.state.pop();
                                }
                                if (position[0] != C.FIRST) {
                                    start_ix = this.state.pop();
                                }
                                var list = this.state.pop();
                                start_ix = this.getIndex(list, position[0], start_ix);
                                end_ix = this.getIndex(list, position[1], end_ix) + 1;
                                this.state.push(list.slice(start_ix, end_ix));
                            }
                            break;
                        default:
                            throw 'Invalid Op on List Function Name';
                    }
                    break;
                }
                case C.BINARY: {
                    var subOp = expr[C.OP];
                    var right = this.state.pop();
                    var left = this.state.pop();
                    this.state.push(this.evalBinary(subOp, left, right));
                    break;
                }
                default:
                    U.dbcException('invalid expr op: ' + kind);
            }
        };
        Interpreter.prototype.evalBinary = function (subOp, left, right) {
            var leftIsArray = Array.isArray(left);
            var rightIsArray = Array.isArray(right);
            if (leftIsArray && rightIsArray) {
                var leftLen = left.length;
                var rightLen = right.length;
                switch (subOp) {
                    case C.EQ:
                        if (leftLen === rightLen) {
                            for (var i = 0; i < leftLen; i++) {
                                if (!this.evalBinary(subOp, left[i], right[i])) {
                                    return false;
                                }
                            }
                            return true;
                        }
                        else {
                            return false;
                        }
                    case C.NEQ:
                        if (leftLen === rightLen) {
                            for (var i = 0; i < leftLen; i++) {
                                if (this.evalBinary(subOp, left[i], right[i])) {
                                    return true;
                                }
                            }
                            return false;
                        }
                        else {
                            return true;
                        }
                    default:
                        U.dbcException('invalid binary expr supOp for array-like structures: ' + subOp);
                }
            }
            else if (leftIsArray || rightIsArray) {
                return false;
            }
            else {
                switch (subOp) {
                    case C.EQ:
                        return left == right;
                    case C.NEQ:
                        return left !== right;
                    case C.LT:
                        return left < right;
                    case C.LTE:
                        return left <= right;
                    case C.GT:
                        return left > right;
                    case C.GTE:
                        return left >= right;
                    case C.AND:
                        return left && right;
                    case C.OR:
                        return left || right;
                    case C.ADD:
                        return 0 + left + right;
                    case C.MINUS:
                        return 0 + left - right;
                    case C.MULTIPLY:
                        return 0 + left * right;
                    case C.DIVIDE:
                        return 0 + left / right;
                    case C.POWER:
                        return Math.pow(left, right);
                    case C.MOD:
                        return left % right;
                    default:
                        U.dbcException('invalid binary expr supOp: ' + subOp);
                }
            }
        };
        Interpreter.prototype.evalNNStep = function () {
            console.log('NNStep encountered');
            var s = this.state;
            var i2 = s.pop();
            var i1 = s.pop();
            var i0 = s.pop();
            var inputData = [i0, i1, i2];
            var outputData = PG.oneStep(inputData);
            for (var i = outputData.length - 1; i >= 0; i--) {
                s.push(outputData[i]);
            }
        };
        /**
         * return true if the parameter is prime
         *
         * . @param n to be checked for primality
         */
        Interpreter.prototype.isPrime = function (n) {
            if (n < 2) {
                return false;
            }
            if (n === 2) {
                return true;
            }
            if (n % 2 === 0) {
                return false;
            }
            for (var i = 3, s = Math.sqrt(n); i <= s; i += 2) {
                if (n % i === 0) {
                    return false;
                }
            }
            return true;
        };
        /**
         * return true if the value is whole number
         *
         * . @param value to be checked
         */
        Interpreter.prototype.isWhole = function (value) {
            return Number(value) === value && value % 1 === 0;
        };
        Interpreter.prototype.min = function (values) {
            return Math.min.apply(null, values);
        };
        Interpreter.prototype.max = function (values) {
            return Math.max.apply(null, values);
        };
        Interpreter.prototype.sum = function (values) {
            return values.reduce(function (a, b) { return a + b; }, 0);
        };
        Interpreter.prototype.mean = function (value) {
            var v = this.sum(value) / value.length;
            return Number(v.toFixed(2));
        };
        Interpreter.prototype.median = function (values) {
            values.sort(function (a, b) { return a - b; });
            var median = (values[(values.length - 1) >> 1] + values[values.length >> 1]) / 2;
            return Number(median.toFixed(2));
        };
        Interpreter.prototype.std = function (values) {
            var avg = this.mean(values);
            var diffs = values.map(function (value) { return value - avg; });
            var squareDiffs = diffs.map(function (diff) { return diff * diff; });
            var avgSquareDiff = this.mean(squareDiffs);
            return Number(Math.sqrt(avgSquareDiff).toFixed(2));
        };
        Interpreter.prototype.getRandomInt = function (max) {
            return Math.floor(Math.random() * Math.floor(max));
        };
        //    private round2precision( x: number, precision: number ): number {
        //        var y = +x + ( precision === undefined ? 0.5 : precision / 2 );
        //        return y - ( y % ( precision === undefined ? 1 : +precision ) );
        //    }
        Interpreter.prototype.getIndex = function (list, loc, ix) {
            if (loc == C.FROM_START) {
                return ix;
            }
            else if (loc == C.FROM_END) {
                return list.length - 1 - ix;
            }
            else if (loc == C.FIRST) {
                return 0;
            }
            else if (loc == C.LAST) {
                return list.length - 1;
            }
            else {
                throw 'Unhandled option (lists_getSublist).';
            }
        };
        Interpreter.prototype.invertImage = function (image) {
            for (var i = 0; i < image.length; i++) {
                for (var j = 0; j < image[i].length; j++) {
                    image[i][j] = Math.abs(255 - image[i][j]);
                }
            }
            return image;
        };
        Interpreter.prototype.shiftImageAction = function (image, direction, nShift) {
            nShift = Math.round(nShift);
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
                },
            };
            if (nShift < 0) {
                nShift *= -1;
                if (direction === 'up') {
                    direction = 'down';
                }
                else if (direction === 'down') {
                    direction = 'up';
                }
                else if (direction === 'left') {
                    direction = 'right';
                }
                else if (direction === 'right') {
                    direction = 'left';
                }
            }
            for (var i = 0; i < nShift; i++) {
                shift[direction]();
            }
            return image;
        };
        Interpreter.prototype.shiftImageActionMbot = function (image, direction, nShift) {
            nShift = Math.round(nShift);
            var shift = {
                left: function () {
                    image.pop();
                    image.unshift([0, 0, 0, 0, 0, 0, 0, 0]);
                },
                right: function () {
                    image.shift();
                    image.push([0, 0, 0, 0, 0, 0, 0, 0]);
                },
                up: function () {
                    image.forEach(function (array) {
                        array.pop();
                        array.unshift(0);
                    });
                },
                down: function () {
                    image.forEach(function (array) {
                        array.shift();
                        array.push(0);
                    });
                },
            };
            if (nShift < 0) {
                nShift *= -1;
                if (direction === 'up') {
                    direction = 'down';
                }
                else if (direction === 'down') {
                    direction = 'up';
                }
                else if (direction === 'left') {
                    direction = 'right';
                }
                else if (direction === 'right') {
                    direction = 'left';
                }
            }
            for (var i = 0; i < nShift; i++) {
                shift[direction]();
            }
            return image;
        };
        Interpreter.isPossibleStepInto = function (op) {
            var _a;
            if (((_a = op[C.POSSIBLE_DEBUG_STOP]) === null || _a === void 0 ? void 0 : _a.length) > 0) {
                return true;
            }
            return false;
        };
        Interpreter.isPossibleStepOver = function (op) {
            var isMethodCall = op[C.OPCODE] === C.COMMENT && op[C.TARGET] === C.METHOD_CALL;
            return op.hasOwnProperty(C.HIGHTLIGHT_PLUS) && isMethodCall;
        };
        Interpreter.isBreakPoint = function (op, breakpoints) {
            var _a, _b;
            if ((_a = op[C.POSSIBLE_DEBUG_STOP]) === null || _a === void 0 ? void 0 : _a.some(function (blockId) { return breakpoints.indexOf(blockId) >= 0; })) {
                return true;
            }
            if ((_b = op[C.HIGHTLIGHT_PLUS]) === null || _b === void 0 ? void 0 : _b.some(function (blockId) { return breakpoints.indexOf(blockId) >= 0; })) {
                return true;
            }
            return false;
        };
        return Interpreter;
    }());
    exports.Interpreter = Interpreter;
});
