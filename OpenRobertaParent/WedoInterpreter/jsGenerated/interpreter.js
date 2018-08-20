(function (factory) {
    if (typeof module === "object" && typeof module.exports === "object") {
        var v = factory(require, exports);
        if (v !== undefined) module.exports = v;
    }
    else if (typeof define === "function" && define.amd) {
        define(["require", "exports", "interpreter.state", "interpreter.constants", "interpreter.util"], factory);
    }
})(function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var interpreter_state_1 = require("interpreter.state");
    var C = require("interpreter.constants");
    var U = require("interpreter.util");
    var Interpreter = (function () {
        function Interpreter() {
            this.terminated = false;
            this.callbackOnTermination = undefined;
        }
        /**
         * run the operations.
         *
         * . @param generatedCode argument contains the operations and the function definitions
         * . @param native implementation of the native interface Native to connect a real WeDo robot (or a test instance) to the interpreter
         * . @param cbOnTermination is called when the program has terminated
         */
        Interpreter.prototype.run = function (generatedCode, native, cbOnTermination) {
            var _this = this;
            this.terminated = false;
            this.callbackOnTermination = cbOnTermination;
            var stmts = generatedCode[C.OPS];
            var functions = generatedCode[C.FUNCTION_DECLARATION];
            this.n = native;
            var stop = {};
            stop[C.OPCODE] = "stop";
            stmts.push(stop);
            this.s = new interpreter_state_1.State(stmts, functions);
            this.timeout(function () { _this.evalOperation(); }, 0); // return to caller. Don't block the UI.
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
        };
        /**
         * the central interpreter. It is a stack machine interpreting operations given as JSON objects. The operations are all IMMUTABLE. It
         * - uses the S (state) component to store the state of the interpretation.
         * - uses the N (native) component for accessing hardware sensors and actors
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
        Interpreter.prototype.evalOperation = function () {
            var _this = this;
            var s = this.s;
            var n = this.n;
            var _loop_1 = function () {
                s.opLog('actual ops: ');
                var stmt = s.getOp();
                if (stmt === undefined) {
                    U.debug('PROGRAM TERMINATED. No ops remaining');
                    return "break-topLevelLoop";
                }
                var opCode = stmt[C.OPCODE];
                switch (opCode) {
                    case C.ASSIGN_STMT: {
                        var name_1 = stmt[C.NAME];
                        s.setVar(name_1, s.pop());
                        break;
                    }
                    case C.CLEAR_DISPLAY_ACTION: {
                        n.clearDisplay();
                        break;
                    }
                    case C.CREATE_DEBUG_ACTION: {
                        U.debug('NYI');
                        break;
                    }
                    case C.DRIVE_ACTION: {
                        U.debug('NYI');
                        break;
                    }
                    case C.EXPR:
                        this_1.evalExpr(stmt);
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
                        n.getSample(s, stmt[C.NAME], stmt[C.PORT], stmt[C.GET_SAMPLE], stmt[C.SLOT]);
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
                        var duration = s.pop();
                        var speed = s.pop();
                        var name_2 = stmt[C.NAME];
                        var port_1 = stmt[C.PORT];
                        n.motorOnAction(name_2, port_1, duration, speed);
                        if (duration >= 0) {
                            this_1.timeout(function () { n.motorStopAction(name_2, port_1); _this.evalOperation(); }, duration);
                            return { value: void 0 };
                        }
                        break;
                    }
                    case C.MOTOR_STOP: {
                        n.motorStopAction(stmt[C.NAME], stmt[C.PORT]);
                        break;
                    }
                    case C.REPEAT_STMT:
                        this_1.evalRepeat(stmt);
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
                        break;
                    case C.SHOW_TEXT_ACTION: {
                        n.showTextAction(s.pop());
                        break;
                    }
                    case C.STATUS_LIGHT_ACTION:
                        n.statusLightOffAction(stmt[C.NAME], stmt[C.PORT]);
                        break;
                    case C.STOP:
                        U.debug("PROGRAM TERMINATED. stop op");
                        return "break-topLevelLoop";
                    case C.TEXT_JOIN:
                        var second = s.pop();
                        var first = s.pop();
                        s.push('' + first + second);
                        break;
                    case C.TIMER_SENSOR_RESET:
                        var port = stmt[C.PORT];
                        n.timerReset(port);
                        break;
                    case C.TONE_ACTION: {
                        var duration = s.pop();
                        var frequency = s.pop();
                        n.toneAction(stmt[C.NAME], frequency, duration);
                        this_1.timeout(function () { _this.evalOperation(); }, duration);
                        return { value: void 0 };
                    }
                    case C.VAR_DECLARATION: {
                        var name_3 = stmt[C.NAME];
                        s.bindVar(name_3, s.pop());
                        break;
                    }
                    case C.WAIT_STMT: {
                        U.debug('waitstmt started');
                        s.pushOps(stmt[C.STMT_LIST]);
                        break;
                    }
                    case C.WAIT_TIME_STMT: {
                        var time = s.pop();
                        this_1.timeout(function () { _this.evalOperation(); }, time);
                        return { value: void 0 };
                    }
                    default:
                        U.dbcException("invalid stmt op: " + opCode);
                }
            };
            var this_1 = this;
            topLevelLoop: while (!this.terminated) {
                var state_1 = _loop_1();
                if (typeof state_1 === "object")
                    return state_1.value;
                switch (state_1) {
                    case "break-topLevelLoop": break topLevelLoop;
                }
            }
            // termination either requested by the client or by executing 'stop' or after last statement
            this.terminated = true;
            n.close();
            this.callbackOnTermination();
        };
        /**
         *  called from @see evalOperation() to evaluate all kinds of expressions
         *
         * . @param expr to be evaluated
         */
        Interpreter.prototype.evalExpr = function (expr) {
            var kind = expr[C.EXPR];
            var s = this.s;
            switch (kind) {
                case C.VAR:
                    s.push(s.getVar(expr[C.NAME]));
                    break;
                case C.NUM_CONST:
                    s.push(+expr[C.VALUE]);
                    break;
                case C.BOOL_CONST:
                    s.push(expr[C.VALUE]);
                    break;
                case C.STRING_CONST:
                    s.push(expr[C.VALUE]);
                    break;
                case C.COLOR_CONST:
                    s.push(expr[C.VALUE]);
                    break;
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
                }
                case C.SINGLE_FUNCTION: {
                    var subOp = expr[C.OP];
                    var value = s.pop();
                    switch (subOp) {
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
                        default:
                            throw "Invalid Function Name";
                    }
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
                            s.push(value % 2 === 0);
                            break;
                        case 'ODD':
                            s.push(value % 2 !== 0);
                            break;
                        case 'PRIME':
                            s.push(this.isPrime(value));
                            break;
                        case 'WHOLE':
                            s.push(Number(value) === value && value % 1 === 0);
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
                }
                case C.BINARY: {
                    var subOp = expr[C.OP];
                    var right = s.pop();
                    var left = s.pop();
                    switch (subOp) {
                        case C.EQ:
                            s.push(left == right);
                            break;
                        case C.NEQ:
                            s.push(left !== right);
                            break;
                        case C.LT:
                            s.push(left < right);
                            break;
                        case C.LTE:
                            s.push(left <= right);
                            break;
                        case C.GT:
                            s.push(left > right);
                            break;
                        case C.GTE:
                            s.push(left >= right);
                            break;
                        case C.AND:
                            s.push(left && right);
                            break;
                        case C.OR:
                            s.push(left || right);
                            break;
                        case C.ADD:
                            s.push(0 + left + right);
                            break;
                        case C.MINUS:
                            s.push(0 + left - right);
                            break;
                        case C.MULTIPLY:
                            s.push(0 + left * right);
                            break;
                        case C.DIVIDE:
                            s.push(0 + left / right);
                            break;
                        case C.POWER:
                            s.push(Math.pow(left, right));
                            break;
                        default:
                            U.dbcException("invalid binary expr supOp: " + subOp);
                    }
                    break;
                }
                default:
                    U.dbcException("invalid expr op: " + kind);
            }
            var _a;
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
         * after the duration specified, call the callback function given. The duration is partitioned into 100 millisec intervals to allow termination of the running interpreter during
         * a timeout. Be careful: the termination is NOT effected here, but by the callback function (this should be @see evalOperation() in ALMOST ALL cases)
         *
         * . @param callback called when the time has elapsed
         *
         * . @param durationInMilliSec time that should elapse before the callback is called
         */
        Interpreter.prototype.timeout = function (callback, durationInMilliSec) {
            var _this = this;
            if (this.terminated) {
                callback();
            }
            else if (durationInMilliSec > 100) {
                // U.p( 'waiting for 100 msec from ' + durationInMilliSec + ' msec' );
                durationInMilliSec -= 100;
                setTimeout(function () { _this.timeout(callback, durationInMilliSec); }, 100);
            }
            else {
                // U.p( 'waiting for ' + durationInMilliSec + ' msec' );
                setTimeout(function () { callback(); }, durationInMilliSec);
            }
        };
        return Interpreter;
    }());
    exports.Interpreter = Interpreter;
});
