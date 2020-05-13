define(["require", "exports", "interpreter.state", "interpreter.constants", "interpreter.util"], function (require, exports, interpreter_state_1, C, U) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.Interpreter = void 0;
    var Interpreter = /** @class */ (function () {
        /*
         *
         * . @param generatedCode argument contains the operations and the function definitions
         * . @param r implementation of the ARobotBehaviour class
         * . @param cbOnTermination is called when the program has terminated
        */
        function Interpreter(generatedCode, r, cbOnTermination) {
            this.terminated = false;
            this.callbackOnTermination = undefined;
            this.terminated = false;
            this.callbackOnTermination = cbOnTermination;
            var stmts = generatedCode[C.OPS];
            var functions = generatedCode[C.FUNCTION_DECLARATION];
            this.r = r;
            var stop = {};
            stop[C.OPCODE] = "stop";
            stmts.push(stop);
            this.s = new interpreter_state_1.State(stmts, functions);
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
            this.r.close();
        };
        Interpreter.prototype.getRobotBehaviour = function () {
            return this.r;
        };
        /**
         * the central interpreter. It is a stack machine interpreting operations given as JSON objects. The operations are all IMMUTABLE. It
         * - uses the S (state) component to store the state of the interpretation.
         * - uses the R (robotBehaviour) component for accessing hardware sensors and actors
         *
         * if the program is not terminated, it will take one operation after the other and execute it. The property C.OPCODE contains the
         * operation code and is used for switching to the various operations implementations. For some operation codes the implementations is extracted to
         * special functions (repeat, expression) for better readability.
         *
         * The state of the interpreter consists of
         * - a stack of computed values
         * - the actual array of operations to be executed now, including a program counter as index into the array
         * - a stack of operations-arrays (including their program counters), that are actually frozen until the actual array has been interpreted.
         * - a hash map of bindings. A binding map a name as key to an array of values. This implements hiding of variables.
         *
         * The stack of operations-arrays is used to store the history of complex operation as
         *   - function call
         *   - if-then-else
         *   - repeat
         *   - wait
         * - If such an operation is executed, it pushes the actual array of operations (including itself) onto the stack of operations-arrays,
         *   set the actual array of operations to a new array of own operations (found at the property C.STMT_LIST) and set the program counter to 0
         * - The program counter of the pushed array of operations keeps pointing to the operation that effected the push. Thus some operations as break
         *   have to increase the program counter (to avoid an endless loop)
         * - if the actual array of operations is exhausted, the last array of operations pushed to the stack of operations-arrays is re-activated
         *
         * The statement C.FLOW_CONTROL is rather complex:
         * - it is used explicitly by 'continue' and 'break' and know about the repeat-statement / repeat-continuation structure (@see eval_repeat())
         * - it is used implicitly by if-then-else, if one branch is selected and is exhausted. It forces the continuation after the if-then-else
         *
         * Each operation code implementation may
         * - create new bindings of values to names (variable declaration)
         * - change the values of the binding (assign)
         * - push and pop values to the stack (expressions)
         * - push and pop to the stack of operations-arrays
         */
        Interpreter.prototype.evalOperation = function (maxRunTime) {
            var s = this.s;
            var n = this.r;
            while (maxRunTime >= new Date().getTime() && !n.getBlocking()) {
                s.opLog('actual ops: ');
                var stmt = s.getOp();
                if (stmt === undefined) {
                    U.debug('PROGRAM TERMINATED. No ops remaining');
                    this.terminated = true;
                }
                else {
                    var opCode = stmt[C.OPCODE];
                    switch (opCode) {
                        case C.ASSIGN_STMT: {
                            var name_1 = stmt[C.NAME];
                            s.setVar(name_1, s.pop());
                            break;
                        }
                        case C.CLEAR_DISPLAY_ACTION: {
                            n.clearDisplay();
                            return 0;
                        }
                        case C.CREATE_DEBUG_ACTION: {
                            U.debug('NYI');
                            break;
                        }
                        case C.EXPR:
                            this.evalExpr(stmt);
                            break;
                        case C.FLOW_CONTROL: {
                            var conditional = stmt[C.CONDITIONAL];
                            var activatedBy = stmt[C.BOOLEAN] === undefined ? true : stmt[C.BOOLEAN];
                            var doIt = conditional ? (s.pop() === activatedBy) : true;
                            if (doIt) {
                                s.popOpsUntil(stmt[C.KIND]);
                                if (stmt[C.BREAK]) {
                                    s.getOp();
                                }
                            }
                            break;
                        }
                        case C.GET_SAMPLE: {
                            n.getSample(s, stmt[C.NAME], stmt[C.GET_SAMPLE], stmt[C.PORT], stmt[C.MODE]);
                            break;
                        }
                        case C.IF_STMT:
                            s.pushOps(stmt[C.STMT_LIST]);
                            break;
                        case C.IF_TRUE_STMT:
                            if (s.pop()) {
                                s.pushOps(stmt[C.STMT_LIST]);
                            }
                            break;
                        case C.IF_RETURN:
                            if (s.pop()) {
                                s.pushOps(stmt[C.STMT_LIST]);
                            }
                            break;
                        case C.LED_ON_ACTION: {
                            var color = s.pop();
                            n.ledOnAction(stmt[C.NAME], stmt[C.PORT], color);
                            break;
                        }
                        case C.METHOD_CALL_VOID:
                        case C.METHOD_CALL_RETURN: {
                            for (var _i = 0, _a = stmt[C.NAMES]; _i < _a.length; _i++) {
                                var parameterName = _a[_i];
                                s.bindVar(parameterName, s.pop());
                            }
                            var body = s.getFunction(stmt[C.NAME])[C.STATEMENTS];
                            s.pushOps(body);
                            break;
                        }
                        case C.MOTOR_ON_ACTION: {
                            var speedOnly = stmt[C.SPEED_ONLY];
                            var duration = speedOnly ? undefined : s.pop();
                            var speed = s.pop();
                            var name_2 = stmt[C.NAME];
                            var port = stmt[C.PORT];
                            var durationType = stmt[C.MOTOR_DURATION];
                            if (durationType === C.DEGREE || durationType === C.DISTANCE || durationType === C.ROTATIONS) {
                                // if durationType is defined, then duration must be defined, too. Thus, it is never 'undefined' :-)
                                var rotationPerSecond = C.MAX_ROTATION * Math.abs(speed) / 100.0;
                                duration = duration / rotationPerSecond * 1000;
                                if (durationType === C.DEGREE) {
                                    duration /= 360.0;
                                }
                            }
                            n.motorOnAction(name_2, port, duration, speed);
                            return duration ? duration : 0;
                        }
                        case C.DRIVE_ACTION: {
                            var speedOnly = stmt[C.SPEED_ONLY];
                            var distance = speedOnly ? undefined : s.pop();
                            var speed = s.pop();
                            var name_3 = stmt[C.NAME];
                            var direction = stmt[C.DRIVE_DIRECTION];
                            var duration = n.driveAction(name_3, direction, speed, distance);
                            return duration;
                        }
                        case C.TURN_ACTION: {
                            var speedOnly = stmt[C.SPEED_ONLY];
                            var angle = speedOnly ? undefined : s.pop();
                            var speed = s.pop();
                            var name_4 = stmt[C.NAME];
                            var direction = stmt[C.TURN_DIRECTION];
                            var duration = n.turnAction(name_4, direction, speed, angle);
                            return duration;
                        }
                        case C.CURVE_ACTION: {
                            var speedOnly = stmt[C.SPEED_ONLY];
                            var distance = speedOnly ? undefined : s.pop();
                            var speedR = s.pop();
                            var speedL = s.pop();
                            var name_5 = stmt[C.NAME];
                            var direction = stmt[C.DRIVE_DIRECTION];
                            var duration = n.curveAction(name_5, direction, speedL, speedR, distance);
                            return duration;
                        }
                        case C.STOP_DRIVE:
                            var name_6 = stmt[C.NAME];
                            n.driveStop(name_6);
                            return 0;
                        case C.BOTH_MOTORS_ON_ACTION: {
                            var duration = s.pop();
                            var speedB = s.pop();
                            var speedA = s.pop();
                            var portA = stmt[C.PORT_A];
                            var portB = stmt[C.PORT_B];
                            n.motorOnAction(portA, portA, duration, speedA);
                            n.motorOnAction(portB, portB, duration, speedB);
                            return duration;
                        }
                        case C.MOTOR_STOP: {
                            n.motorStopAction(stmt[C.NAME], stmt[C.PORT]);
                            return 0;
                        }
                        case C.MOTOR_SET_POWER: {
                            var speed = s.pop();
                            var name_7 = stmt[C.NAME];
                            var port = stmt[C.PORT];
                            n.setMotorSpeed(name_7, port, speed);
                            return 0;
                        }
                        case C.MOTOR_GET_POWER: {
                            var port = stmt[C.PORT];
                            n.getMotorSpeed(s, name_6, port);
                            break;
                        }
                        case C.REPEAT_STMT:
                            this.evalRepeat(stmt);
                            break;
                        case C.REPEAT_STMT_CONTINUATION:
                            if (stmt[C.MODE] === C.FOR || stmt[C.MODE] === C.TIMES) {
                                var runVariableName = stmt[C.NAME];
                                var end = s.get1();
                                var incr = s.get0();
                                var value = s.getVar(runVariableName) + incr;
                                if (+value >= +end) {
                                    s.popOpsUntil(C.REPEAT_STMT);
                                    s.getOp(); // the repeat has terminated
                                }
                                else {
                                    s.setVar(runVariableName, value);
                                    s.pushOps(stmt[C.STMT_LIST]);
                                }
                            }
                            else if (stmt[C.MODE] === C.FOR_EACH) {
                                var runVariableName = stmt[C.EACH_COUNTER];
                                var varName = stmt[C.NAME];
                                var listName = stmt[C.LIST];
                                var list = s.getVar(listName);
                                var end = list.length;
                                var incr = s.get0();
                                var value = s.getVar(runVariableName) + incr;
                                if (+value >= +end) {
                                    s.popOpsUntil(C.REPEAT_STMT);
                                    s.getOp(); // the repeat has terminated
                                }
                                else {
                                    s.setVar(runVariableName, value);
                                    s.bindVar(varName, list[value]);
                                    s.pushOps(stmt[C.STMT_LIST]);
                                }
                            }
                            break;
                        case C.SHOW_TEXT_ACTION: {
                            var text = s.pop();
                            var name_8 = stmt[C.NAME];
                            if (name_8 === "ev3") {
                                var x = s.pop();
                                var y = s.pop();
                                n.showTextActionPosition(text, x, y);
                                return 0;
                            }
                            return n.showTextAction(text, stmt[C.MODE]);
                        }
                        case C.SHOW_IMAGE_ACTION: {
                            var image = void 0;
                            if (stmt[C.NAME] == "ev3") {
                                image = stmt[C.IMAGE];
                            }
                            else {
                                image = s.pop();
                            }
                            return n.showImageAction(image, stmt[C.MODE]);
                        }
                        case C.DISPLAY_SET_BRIGHTNESS_ACTION: {
                            var b = s.pop();
                            return n.displaySetBrightnessAction(b);
                        }
                        case C.IMAGE_SHIFT_ACTION: {
                            var nShift = s.pop();
                            var image = s.pop();
                            s.push(this.shiftImageAction(image, stmt[C.DIRECTION], nShift));
                            break;
                        }
                        case C.DISPLAY_SET_PIXEL_BRIGHTNESS_ACTION: {
                            var b = s.pop();
                            var y = s.pop();
                            var x = s.pop();
                            return n.displaySetPixelBrightnessAction(x, y, b);
                        }
                        case C.DISPLAY_GET_PIXEL_BRIGHTNESS_ACTION: {
                            var y = s.pop();
                            var x = s.pop();
                            n.displayGetPixelBrightnessAction(s, x, y);
                            break;
                        }
                        case C.LIGHT_ACTION:
                            n.lightAction(stmt[C.MODE], stmt[C.COLOR]);
                            return 0;
                        case C.STATUS_LIGHT_ACTION:
                            n.statusLightOffAction(stmt[C.NAME], stmt[C.PORT]);
                            return 0;
                        case C.STOP:
                            U.debug("PROGRAM TERMINATED. stop op");
                            this.terminated = true;
                            break;
                        case C.TEXT_JOIN: {
                            var n_1 = stmt[C.NUMBER];
                            var result = new Array(n_1);
                            for (var i = 0; i < n_1; i++) {
                                var e = s.pop();
                                result[n_1 - i - 1] = e;
                            }
                            s.push(result.join(""));
                            break;
                        }
                        case C.TIMER_SENSOR_RESET:
                            n.timerReset(stmt[C.PORT]);
                            break;
                        case C.ENCODER_SENSOR_RESET:
                            n.encoderReset(stmt[C.PORT]);
                            return 0;
                        case C.GYRO_SENSOR_RESET:
                            n.gyroReset(stmt[C.PORT]);
                            return 0;
                        case C.TONE_ACTION: {
                            var duration = s.pop();
                            var frequency = s.pop();
                            return n.toneAction(stmt[C.NAME], frequency, duration);
                        }
                        case C.PLAY_FILE_ACTION:
                            return n.playFileAction(stmt[C.FILE]);
                        case C.SET_VOLUME_ACTION:
                            n.setVolumeAction(s.pop());
                            return 0;
                        case C.GET_VOLUME:
                            n.getVolumeAction(s);
                            break;
                        case C.SET_LANGUAGE_ACTION:
                            n.setLanguage(stmt[C.LANGUAGE]);
                            break;
                        case C.SAY_TEXT_ACTION: {
                            var pitch = s.pop();
                            var speed = s.pop();
                            var text = s.pop();
                            return n.sayTextAction(text, speed, pitch);
                        }
                        case C.VAR_DECLARATION: {
                            var name_9 = stmt[C.NAME];
                            s.bindVar(name_9, s.pop());
                            break;
                        }
                        case C.WAIT_STMT: {
                            U.debug('waitstmt started');
                            s.pushOps(stmt[C.STMT_LIST]);
                            break;
                        }
                        case C.WAIT_TIME_STMT: {
                            var time = s.pop();
                            return time; // wait for handler being called
                        }
                        case C.WRITE_PIN_ACTION: {
                            var value = s.pop();
                            var mode = stmt[C.MODE];
                            var pin = stmt[C.PIN];
                            n.writePinAction(pin, mode, value);
                            return 0;
                        }
                        case C.LIST_OPERATION: {
                            var op = stmt[C.OP];
                            var loc = stmt[C.POSITION];
                            var ix = 0;
                            if (loc != C.LAST && loc != C.FIRST) {
                                ix = s.pop();
                            }
                            var value = s.pop();
                            var list = s.pop();
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
                            var value = s.pop();
                            var name_10 = stmt[C.NAME];
                            s.bindVar(name_10, s.pop() + value);
                            break;
                        }
                        case C.DEBUG_ACTION: {
                            var value = s.pop();
                            n.debugAction(value);
                            break;
                        }
                        case C.ASSERT_ACTION: {
                            var right = s.pop();
                            var left = s.pop();
                            var value = s.pop();
                            n.assertAction(stmt[C.MSG], left, stmt[C.OP], right, value);
                            break;
                        }
                        case C.COMMENT:
                            break;
                        default:
                            U.dbcException("invalid stmt op: " + opCode);
                    }
                }
                if (this.terminated) {
                    // termination either requested by the client or by executing 'stop' or after last statement
                    n.close();
                    this.callbackOnTermination();
                    return 0;
                }
            }
            return 0;
        };
        /**
         *  called from @see evalOperation() to evaluate all kinds of expressions
         *
         * . @param expr to be evaluated
         */
        Interpreter.prototype.evalExpr = function (expr) {
            var _a;
            var kind = expr[C.EXPR];
            var s = this.s;
            switch (kind) {
                case C.VAR:
                    s.push(s.getVar(expr[C.NAME]));
                    break;
                case C.NUM_CONST:
                    s.push(+expr[C.VALUE]);
                    break;
                case C.CREATE_LIST: {
                    var n = expr[C.NUMBER];
                    var arr = new Array(n);
                    for (var i = 0; i < n; i++) {
                        var e = s.pop();
                        arr[n - i - 1] = e;
                    }
                    s.push(arr);
                    break;
                }
                case C.CREATE_LIST_REPEAT: {
                    var rep = s.pop();
                    var val = s.pop();
                    var arr = new Array();
                    for (var i = 0; i < rep; i++) {
                        arr[i] = val;
                    }
                    s.push(arr);
                    break;
                }
                case C.BOOL_CONST:
                    s.push(expr[C.VALUE]);
                    break;
                case C.STRING_CONST:
                    s.push(expr[C.VALUE]);
                    break;
                case C.COLOR_CONST:
                    s.push(expr[C.VALUE]);
                    break;
                case C.IMAGE:
                    s.push(expr[C.VALUE]);
                    break;
                case C.RGB_COLOR_CONST: {
                    var b = s.pop();
                    var g = s.pop();
                    var r = s.pop();
                    s.push([r, g, b]);
                    break;
                }
                case C.UNARY: {
                    var subOp = expr[C.OP];
                    switch (subOp) {
                        case C.NOT:
                            var truthy;
                            var bool = s.pop();
                            if (bool === 'true') {
                                truthy = true;
                            }
                            else if (bool === 'false' || bool === '0' || bool === '') {
                                truthy = false;
                            }
                            else {
                                truthy = !!bool;
                            }
                            s.push(!truthy);
                            break;
                        case C.NEG:
                            var value = s.pop();
                            s.push(-value);
                            break;
                        default:
                            U.dbcException("invalid unary expr subOp: " + subOp);
                    }
                    break;
                }
                case C.MATH_CONST: {
                    var value = expr[C.VALUE];
                    switch (value) {
                        case 'PI':
                            s.push(Math.PI);
                            break;
                        case 'E':
                            s.push(Math.E);
                            break;
                        case 'GOLDEN_RATIO':
                            s.push((1.0 + Math.sqrt(5.0)) / 2.0);
                            break;
                        case 'SQRT2':
                            s.push(Math.SQRT2);
                            break;
                        case 'SQRT1_2':
                            s.push(Math.SQRT1_2);
                            break;
                        case 'INFINITY':
                            s.push(Infinity);
                            break;
                        default:
                            throw "Invalid Math Constant Name";
                    }
                    break;
                }
                case C.SINGLE_FUNCTION: {
                    var subOp = expr[C.OP];
                    var value = s.pop();
                    U.debug('---------- ' + subOp + ' with ' + value);
                    switch (subOp) {
                        case 'SQUARE':
                            s.push(Math.pow(value, 2));
                            break;
                        case 'ROOT':
                            s.push(Math.sqrt(value));
                            break;
                        case 'ABS':
                            s.push(Math.abs(value));
                            break;
                        case 'LN':
                            s.push(Math.log(value));
                            break;
                        case 'LOG10':
                            s.push(Math.log(value) / Math.LN10);
                            break;
                        case 'EXP':
                            s.push(Math.exp(value));
                            break;
                        case 'POW10':
                            s.push(Math.pow(10, value));
                            break;
                        case 'SIN':
                            s.push(Math.sin(value));
                            break;
                        case 'COS':
                            s.push(Math.cos(value));
                            break;
                        case 'TAN':
                            s.push(Math.tan(value));
                            break;
                        case 'ASIN':
                            s.push(Math.asin(value));
                            break;
                        case 'ATAN':
                            s.push(Math.atan(value));
                            break;
                        case 'ACOS':
                            s.push(Math.acos(value));
                            break;
                        case 'ROUND':
                            s.push(Math.round(value));
                            break;
                        case 'ROUNDUP':
                            s.push(Math.ceil(value));
                            break;
                        case 'ROUNDDOWN':
                            s.push(Math.floor(value));
                            break;
                        case C.IMAGE_INVERT_ACTION:
                            s.push(this.invertImage(value));
                            break;
                        default:
                            throw "Invalid Function Name";
                    }
                    break;
                }
                case C.MATH_CONSTRAIN_FUNCTION: {
                    var max_1 = s.pop();
                    var min_1 = s.pop();
                    var value = s.pop();
                    s.push(Math.min(Math.max(value, min_1), max_1));
                    break;
                }
                case C.RANDOM_INT: {
                    var max = s.pop();
                    var min = s.pop();
                    if (min > max) {
                        _a = [max, min], min = _a[0], max = _a[1];
                    }
                    s.push(Math.floor(Math.random() * (max - min + 1) + min));
                    break;
                }
                case C.RANDOM_DOUBLE:
                    s.push(Math.random());
                    break;
                case C.MATH_PROP_FUNCT: {
                    var subOp = expr[C.OP];
                    var value = s.pop();
                    switch (subOp) {
                        case 'EVEN':
                            s.push(this.isWhole(value) && value % 2 === 0);
                            break;
                        case 'ODD':
                            s.push(this.isWhole(value) && value % 2 !== 0);
                            break;
                        case 'PRIME':
                            s.push(this.isPrime(value));
                            break;
                        case 'WHOLE':
                            s.push(this.isWhole(value));
                            break;
                        case 'POSITIVE':
                            s.push(value >= 0);
                            break;
                        case 'NEGATIVE':
                            s.push(value < 0);
                            break;
                        case 'DIVISIBLE_BY':
                            var first = s.pop();
                            s.push(first % value === 0);
                            break;
                        default:
                            throw "Invalid Math Property Function Name";
                    }
                    break;
                }
                case C.MATH_ON_LIST: {
                    var subOp = expr[C.OP];
                    var value = s.pop();
                    switch (subOp) {
                        case C.SUM:
                            s.push(this.sum(value));
                            break;
                        case C.MIN:
                            s.push(this.min(value));
                            break;
                        case C.MAX:
                            s.push(this.max(value));
                            break;
                        case C.AVERAGE:
                            s.push(this.mean(value));
                            break;
                        case C.MEDIAN:
                            s.push(this.median(value));
                            break;
                        case C.STD_DEV:
                            s.push(this.std(value));
                            break;
                        case C.RANDOM:
                            s.push(value[this.getRandomInt(value.length)]);
                            break;
                        default:
                            throw "Invalid Math on List Function Name";
                    }
                    break;
                }
                case C.LIST_OPERATION: {
                    var subOp = expr[C.OP];
                    switch (subOp) {
                        case C.LIST_IS_EMPTY:
                            s.push(s.pop().length == 0);
                            break;
                        case C.LIST_LENGTH:
                            s.push(s.pop().length);
                            break;
                        case C.LIST_FIND_ITEM:
                            {
                                var item = s.pop();
                                var list = s.pop();
                                if (expr[C.POSITION] == C.FIRST) {
                                    s.push(list.indexOf(item));
                                }
                                else {
                                    s.push(list.lastIndexOf(item));
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
                                    ix = s.pop();
                                }
                                var list = s.pop();
                                ix = this.getIndex(list, loc, ix);
                                var v = list[ix];
                                if (subOp == C.GET_REMOVE || subOp == C.GET) {
                                    s.push(v);
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
                                    end_ix = s.pop();
                                }
                                if (position[0] != C.FIRST) {
                                    start_ix = s.pop();
                                }
                                var list = s.pop();
                                start_ix = this.getIndex(list, position[0], start_ix);
                                end_ix = this.getIndex(list, position[1], end_ix) + 1;
                                s.push(list.slice(start_ix, end_ix));
                            }
                            break;
                        default:
                            throw "Invalid Op on List Function Name";
                    }
                    break;
                }
                case C.BINARY: {
                    var subOp = expr[C.OP];
                    var right = s.pop();
                    var left = s.pop();
                    s.push(this.evalBinary(subOp, left, right));
                    break;
                }
                default:
                    U.dbcException("invalid expr op: " + kind);
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
                        U.dbcException("invalid binary expr supOp for array-like structures: " + subOp);
                }
            }
            else if (leftIsArray || rightIsArray) {
                return false;
            }
            else {
                switch (subOp) {
                    case C.EQ: return left == right;
                    case C.NEQ: return left !== right;
                    case C.LT: return left < right;
                    case C.LTE: return left <= right;
                    case C.GT: return left > right;
                    case C.GTE: return left >= right;
                    case C.AND: return left && right;
                    case C.OR: return left || right;
                    case C.ADD: return 0 + left + right;
                    case C.MINUS: return 0 + left - right;
                    case C.MULTIPLY: return 0 + left * right;
                    case C.DIVIDE: return 0 + left / right;
                    case C.POWER: return Math.pow(left, right);
                    case C.MOD: return left % right;
                    default:
                        U.dbcException("invalid binary expr supOp: " + subOp);
                }
            }
        };
        /**
         * called from @see evalOperation() to run a repeat statement
         *
         * a repeat-statement ALWAYS contains a single repeat-continuation statement. That in turn contains the body statements written by the programmer.
         * The repeat-statement does initialization of init, end, step and the run variable for the FOR and TIMES variant (other variants don't need that)
         * The repeat-continuation is for updating the run variable in the FOR and TIMES variant.
         *
         * A continue statement pops the stack until a repeat-continuation is found and re-executes it
         * A break statement pops the stack until a repeat-statement is found and skips that
         *
         * Have a look at the functions for push and pop of operations in the STATE component. The cleanup of the run variable is done there.
         * This is not optimal, as design decisions are distributed over two components.
         *
         * . @param stmt the repeat statement
         */
        Interpreter.prototype.evalRepeat = function (stmt) {
            var s = this.s;
            var mode = stmt[C.MODE];
            var contl = stmt[C.STMT_LIST];
            if (contl.length !== 1 || contl[0][C.OPCODE] !== C.REPEAT_STMT_CONTINUATION) {
                U.dbcException("repeat expects an embedded continuation statement");
            }
            var cont = contl[0];
            switch (mode) {
                case C.FOREVER:
                case C.UNTIL:
                case C.WHILE:
                    s.pushOps(contl);
                    s.getOp(); // pseudo execution. Init is already done. Continuation is for termination only.
                    s.pushOps(cont[C.STMT_LIST]);
                    break;
                case C.FOR_EACH: {
                    var runVariableName = stmt[C.EACH_COUNTER];
                    var varName = stmt[C.NAME];
                    var listName = stmt[C.LIST];
                    var start = s.get1();
                    var list = s.getVar(listName);
                    var end = list.length;
                    if (+start >= +end) {
                        s.pop();
                        s.pop();
                        s.pop();
                    }
                    else {
                        s.bindVar(runVariableName, start);
                        s.bindVar(varName, list[start]);
                        s.pushOps(contl);
                        s.getOp(); // pseudo excution. Init is already done. Continuation is for termination only.
                        s.pushOps(cont[C.STMT_LIST]);
                        break;
                    }
                    break;
                }
                case C.TIMES:
                case C.FOR: {
                    var runVariableName = stmt[C.NAME];
                    var start = s.get2();
                    var end = s.get1();
                    if (+start >= +end) {
                        s.pop();
                        s.pop();
                        s.pop();
                    }
                    else {
                        s.bindVar(runVariableName, start);
                        s.pushOps(contl);
                        s.getOp(); // pseudo excution. Init is already done. Continuation is for termination only.
                        s.pushOps(cont[C.STMT_LIST]);
                        break;
                    }
                    break;
                }
                default:
                    U.dbcException("invalid repeat mode: " + mode);
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
                }
            };
            if (nShift < 0) {
                nShift *= -1;
                if (direction === "up") {
                    direction = "down";
                }
                else if (direction === "down") {
                    direction = "up";
                }
                else if (direction === "left") {
                    direction = "right";
                }
                else if (direction === "right") {
                    direction = "left";
                }
            }
            for (var i = 0; i < nShift; i++) {
                shift[direction]();
            }
            return image;
        };
        return Interpreter;
    }());
    exports.Interpreter = Interpreter;
});
