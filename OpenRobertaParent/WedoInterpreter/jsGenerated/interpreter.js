(function (factory) {
    if (typeof module === "object" && typeof module.exports === "object") {
        var v = factory(require, exports);
        if (v !== undefined) module.exports = v;
    }
    else if (typeof define === "function" && define.amd) {
        define(["require", "exports", "./constants", "./native", "./state", "./util"], factory);
    }
})(function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var C = require("./constants");
    var N = require("./native");
    var S = require("./state");
    var U = require("./util");
    var terminated = false;
    var callbackOnTermination = undefined;
    function run(generatedCode, cbOnTermination) {
        terminated = false;
        callbackOnTermination = cbOnTermination;
        var stmts = generatedCode[C.OPS];
        var functions = generatedCode[C.FUNCTION_DECLARATION];
        var stop = {};
        stop[C.OPCODE] = "stop";
        stmts.push(stop);
        S.storeCode(stmts, functions);
        setTimeout(function () { evalOperation(); }, 0); // return to caller. Don't block the UI.
    }
    exports.run = run;
    function isTerminated() {
        return terminated;
    }
    exports.isTerminated = isTerminated;
    function terminate() {
        terminated = true;
    }
    exports.terminate = terminate;
    function evalOperation() {
        var _loop_1 = function () {
            S.opLog('actual ops: ');
            var stmt = S.getOp();
            if (stmt === undefined) {
                U.p("PROGRAM TERMINATED. No ops remaining");
                terminated = true;
                return "break-topLevelLoop";
            }
            var opCode = stmt[C.OPCODE];
            switch (opCode) {
                case C.ASSIGN_STMT: {
                    var name_1 = stmt[C.NAME];
                    S.setVar(name_1, S.pop());
                    break;
                }
                case C.CLEAR_DISPLAY_ACTION: {
                    N.clearDisplay();
                    break;
                }
                case C.CREATE_DEBUG_ACTION: {
                    U.p('NYI');
                    break;
                }
                case C.DRIVE_ACTION: {
                    var distance = S.pop();
                    var speed = S.pop();
                    var driveDirection = stmt[C.DRIVE_DIRECTION];
                    N.driveAction(driveDirection, distance, speed);
                    break;
                }
                case C.EXPR:
                    evalExpr(stmt);
                    break;
                case C.FLOW_CONTROL: {
                    var conditional = stmt[C.CONDITIONAL];
                    var activatedBy = stmt[C.BOOLEAN] === undefined ? true : stmt[C.BOOLEAN];
                    var doIt = conditional ? (S.pop() === activatedBy) : true;
                    if (doIt) {
                        S.popOpsUntil(stmt[C.KIND]);
                        if (stmt[C.BREAK]) {
                            S.getOp();
                        }
                    }
                    break;
                }
                case C.GET_SAMPLE: {
                    N.getSample(stmt[C.NAME], stmt[C.PORT], stmt[C.GET_SAMPLE]);
                    break;
                }
                case C.IF_STMT:
                    S.pushOps(stmt[C.STMT_LIST]);
                    break;
                case C.IF_TRUE_STMT:
                    if (S.pop()) {
                        S.pushOps(stmt[C.STMT_LIST]);
                    }
                    break;
                case C.IF_RETURN:
                    if (S.pop()) {
                        S.pushOps(stmt[C.STMT_LIST]);
                    }
                    break;
                case C.LED_ON_ACTION: {
                    var color = S.pop();
                    N.ledOnAction(stmt[C.NAME], stmt[C.PORT], color);
                    break;
                }
                case C.METHOD_CALL_VOID:
                case C.METHOD_CALL_RETURN: {
                    for (var _i = 0, _a = stmt[C.NAMES]; _i < _a.length; _i++) {
                        var parameterName = _a[_i];
                        S.bindVar(parameterName, S.pop());
                    }
                    var body = S.getFunction(stmt[C.NAME])[C.STATEMENTS];
                    S.pushOps(body);
                    break;
                }
                case C.MOTOR_ON_ACTION: {
                    var duration = S.pop();
                    var speed = S.pop();
                    var name_2 = stmt[C.NAME];
                    var port_1 = stmt[C.PORT];
                    N.motorOnAction(name_2, port_1, duration, speed);
                    setTimeout(function () { N.motorStopAction(name_2, port_1); evalOperation(); }, duration);
                    return { value: void 0 };
                }
                case C.MOTOR_STOP: {
                    N.motorStopAction(stmt[C.NAME], stmt[C.PORT]);
                    break;
                }
                case C.REPEAT_STMT:
                    evalRepeat(stmt);
                    break;
                case C.REPEAT_STMT_CONTINUATION:
                    if (stmt[C.MODE] === C.FOR || stmt[C.MODE] === C.TIMES) {
                        var runVariableName = stmt[C.NAME];
                        var end = S.get1();
                        var incr = S.get0();
                        var value = S.getVar(runVariableName) + incr;
                        if (+value >= +end) {
                            S.popOpsUntil(C.REPEAT_STMT);
                            S.getOp(); // the repeat has terminated
                        }
                        else {
                            S.setVar(runVariableName, value);
                            S.pushOps(stmt[C.STMT_LIST]);
                        }
                    }
                    break;
                case C.SHOW_TEXT_ACTION: {
                    N.showTextAction(S.pop());
                    break;
                }
                case C.STATUS_LIGHT_ACTION:
                    N.statusLightOffAction('-', '-');
                    return { value: void 0 };
                case C.STOP:
                    U.p("PROGRAM TERMINATED. stop op");
                    terminated = true;
                    return "break-topLevelLoop";
                case C.TEXT_JOIN:
                    var second = S.pop();
                    var first = S.pop();
                    S.push('' + first + second);
                    break;
                case C.TIMER_SENSOR_RESET:
                    N.timerReset();
                    break;
                case C.TONE_ACTION: {
                    var duration = S.pop();
                    var frequency = S.pop();
                    N.toneAction(stmt[C.NAME], stmt[C.PORT], frequency, duration);
                    setTimeout(function () { evalOperation(); }, duration);
                    return { value: void 0 };
                }
                case C.VAR_DECLARATION: {
                    var name_3 = stmt[C.NAME];
                    S.bindVar(name_3, S.pop());
                    break;
                }
                case C.WAIT_STMT: {
                    U.p('waitstmt started');
                    S.pushOps(stmt[C.STMT_LIST]);
                    break;
                }
                case C.WAIT_TIME_STMT: {
                    var time = S.pop();
                    U.p('waiting ' + time + ' msec');
                    setTimeout(function () { evalOperation(); }, time);
                    return { value: void 0 };
                }
                default:
                    U.dbcException("invalid stmt op: " + opCode);
            }
        };
        topLevelLoop: while (!terminated) {
            var state_1 = _loop_1();
            if (typeof state_1 === "object")
                return state_1.value;
            switch (state_1) {
                case "break-topLevelLoop": break topLevelLoop;
            }
        }
        // termination either requested by the client or by executing 'stop' or after last statement
        callbackOnTermination();
    }
    exports.evalOperation = evalOperation;
    function evalExpr(expr) {
        var kind = expr[C.EXPR];
        switch (kind) {
            case C.VAR:
                S.push(S.getVar(expr[C.NAME]));
                break;
            case C.NUM_CONST:
                S.push(+expr[C.VALUE]);
                break;
            case C.STRING_CONST:
                S.push(expr[C.VALUE]);
                break;
            case C.COLOR_CONST:
                S.push(expr[C.VALUE]);
                break;
            case C.UNARY: {
                var subOp = expr[C.OP];
                switch (subOp) {
                    case C.NOT:
                        var bool = S.pop();
                        S.push(!bool);
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
                        S.push(Math.PI);
                        break;
                    case 'E':
                        S.push(Math.E);
                        break;
                    case 'GOLDEN_RATIO':
                        S.push((1.0 + Math.sqrt(5.0)) / 2.0);
                        break;
                    case 'SQRT2':
                        S.push(Math.SQRT2);
                        break;
                    case 'SQRT1_2':
                        S.push(Math.SQRT1_2);
                        break;
                    case 'INFINITY':
                        S.push(Infinity);
                        break;
                    default:
                        throw "Invalid Math Constant Name";
                }
            }
            case C.SINGLE_FUNCTION: {
                var subOp = expr[C.OP];
                var value = S.pop();
                switch (subOp) {
                    case 'ROOT':
                        S.push(Math.sqrt(value));
                        break;
                    case 'ABS':
                        S.push(Math.abs(value));
                        break;
                    case 'LN':
                        S.push(Math.log(value));
                        break;
                    case 'LOG10':
                        S.push(Math.log(value) / Math.LN10);
                        break;
                    case 'EXP':
                        S.push(Math.exp(value));
                        break;
                    case 'POW10':
                        S.push(Math.pow(10, value));
                        break;
                    case 'SIN':
                        S.push(Math.sin(value));
                        break;
                    case 'COS':
                        S.push(Math.cos(value));
                        break;
                    case 'TAN':
                        S.push(Math.tan(value));
                        break;
                    case 'ASIN':
                        S.push(Math.asin(value));
                        break;
                    case 'ATAN':
                        S.push(Math.atan(value));
                        break;
                    case 'ACOS':
                        S.push(Math.acos(value));
                        break;
                    case 'ROUND':
                        S.push(Math.round(value));
                        break;
                    case 'ROUNDUP':
                        S.push(Math.ceil(value));
                        break;
                    case 'ROUNDDOWN':
                        S.push(Math.floor(value));
                        break;
                    default:
                        throw "Invalid Function Name";
                }
            }
            case C.MATH_CONSTRAIN_FUNCTION: {
                var max_1 = S.pop();
                var min_1 = S.pop();
                var value = S.pop();
                S.push(Math.min(Math.max(value, min_1), max_1));
                break;
            }
            case C.RANDOM_INT: {
                var max = S.pop();
                var min = S.pop();
                if (min > max) {
                    _a = [max, min], min = _a[0], max = _a[1];
                }
                S.push(Math.floor(Math.random() * (max - min + 1) + min));
                break;
            }
            case C.RANDOM_DOUBLE:
                S.push(Math.random());
                break;
            case C.MATH_PROP_FUNCT: {
                var subOp = expr[C.OP];
                var value = S.pop();
                switch (subOp) {
                    case 'EVEN':
                        S.push(value % 2 === 0);
                        break;
                    case 'ODD':
                        S.push(value % 2 !== 0);
                        break;
                    case 'PRIME':
                        S.push(isPrime(value));
                        break;
                    case 'WHOLE':
                        S.push(Number(value) === value && value % 1 === 0);
                        break;
                    case 'POSITIVE':
                        S.push(value >= 0);
                        break;
                    case 'NEGATIVE':
                        S.push(value < 0);
                        break;
                    case 'DIVISIBLE_BY':
                        var first = S.pop();
                        S.push(first % value === 0);
                        break;
                    default:
                        throw "Invalid Math Property Function Name";
                }
            }
            case C.BINARY: {
                var subOp = expr[C.OP];
                var right = S.pop();
                var left = S.pop();
                switch (subOp) {
                    case C.EQ:
                        S.push(left === right);
                        break;
                    case C.NEQ:
                        S.push(left !== right);
                        break;
                    case C.LT:
                        S.push(left < right);
                        break;
                    case C.LTE:
                        S.push(left <= right);
                        break;
                    case C.GT:
                        S.push(left > right);
                        break;
                    case C.GTE:
                        S.push(left >= right);
                        break;
                    case C.AND:
                        S.push(left && right);
                        break;
                    case C.OR:
                        S.push(left || right);
                        break;
                    case C.ADD:
                        S.push(0 + left + right);
                        break;
                    case C.MINUS:
                        S.push(0 + left - right);
                        break;
                    case C.MULTIPLY:
                        S.push(0 + left * right);
                        break;
                    case C.DIVIDE:
                        S.push(0 + left / right);
                        break;
                    case C.POWER:
                        S.push(Math.pow(left, right));
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
    }
    function evalRepeat(stmt) {
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
                S.pushOps(contl);
                S.getOp(); // pseudo excution. Init is already done. Continuation is for termination only.
                S.pushOps(cont[C.STMT_LIST]);
                break;
            case C.TIMES:
            case C.FOR: {
                var runVariableName = stmt[C.NAME];
                var start = S.get2();
                var end = S.get1();
                if (+start >= +end) {
                    S.pop();
                    S.pop();
                    S.pop();
                }
                else {
                    S.bindVar(runVariableName, start);
                    S.pushOps(contl);
                    S.getOp(); // pseudo excution. Init is already done. Continuation is for termination only.
                    S.pushOps(cont[C.STMT_LIST]);
                    break;
                }
                break;
            }
            default:
                U.dbcException("invalid repeat mode: " + mode);
        }
    }
    function isPrime(n) {
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
    }
    ;
});
