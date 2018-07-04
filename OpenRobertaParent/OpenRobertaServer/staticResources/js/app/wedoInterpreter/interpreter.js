define([ 'exports', 'log', 'message', 'constants.interpreter', 'native.interpreter', 'state.interpreter', 'util.interpreter' ], function(exports, LOG, MSG,
        CONST_I, NATIVE_I, STATE_I, UTIL_I) {

    var terminated = false;
    var callbackOnTermination = undefined;

    function run(generatedCode, cbOnTermination) {
        terminated = false;
        callbackOnTermination = cbOnTermination;
        STATE_I.reset();
        var stmts = generatedCode[CONST_I.OPS];
        var functions = generatedCode[CONST_I.FUNCTION_DECLARATION];
        var stop = {};
        stop[CONST_I.OPCODE] = "stop";
        stmts.push(stop);
        STATE_I.storeCode(stmts, functions);
        setTimeout(function() {
            evalOperation();
        }, 0); // return to caller. Don't block the UI.
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
        topLevelLoop: while (!terminated) {
            STATE_I.opLog('actual ops: ');
            var stmt = STATE_I.getOp();
            if (stmt === undefined) {
                UTIL_I.p("PROGRAM TERMINATED. No ops remaining");
                terminated = true;
                break topLevelLoop;
            }
            var opCode = stmt[CONST_I.OPCODE];
            switch (opCode) {
            case CONST_I.ASSIGN_STMT: {
                var name_1 = stmt[CONST_I.NAME];
                STATE_I.setVar(name_1, STATE_I.pop());
                break;
            }
            case CONST_I.CLEAR_DISPLAY_ACTION: {
                NATIVE_I.clearDisplay();
                break;
            }
            case CONST_I.CREATE_DEBUG_ACTION: {
                UTIL_I.p('NYI');
                break;
            }
            case CONST_I.DRIVE_ACTION: {
                var distance = STATE_I.pop();
                var speed = STATE_I.pop();
                var driveDirection = stmt[CONST_I.DRIVE_DIRECTION];
                NATIVE_I.driveAction(driveDirection, distance, speed);
                break;
            }
            case CONST_I.EXPR:
                evalExpr(stmt);
                break;
            case CONST_I.FLOW_CONTROL: {
                var conditional = stmt[CONST_I.CONDITIONAL];
                var doIt = conditional ? STATE_I.pop() : true;
                if (doIt) {
                    STATE_I.popOpsUntil(stmt[CONST_I.KIND]);
                    if (stmt[CONST_I.BREAK]) {
                        STATE_I.getOp();
                    }
                }
                break;
            }
            case CONST_I.GET_SAMPLE: {
                NATIVE_I.getSample(stmt[CONST_I.NAME], stmt[CONST_I.PORT], stmt[CONST_I.GET_SAMPLE]);
                break;
            }
            case CONST_I.IF_STMT:
                STATE_I.pushOps(true, stmt[CONST_I.STMT_LIST]);
                break;
            case CONST_I.IF_TRUE_STMT:
                if (STATE_I.pop()) {
                    STATE_I.pushOps(false, stmt[CONST_I.STMT_LIST]);
                }
                break;
            case CONST_I.IF_RETURN:
                if (STATE_I.pop()) {
                    STATE_I.pushOps(false, stmt[CONST_I.STMT_LIST]);
                }
                break;
            case CONST_I.LED_ON_ACTION: {
                var color = STATE_I.pop();
                NATIVE_I.ledOnAction(stmt[CONST_I.NAME], stmt[CONST_I.PORT], color);
                break;
            }
            case CONST_I.METHOD_CALL_VOID:
            case CONST_I.METHOD_CALL_RETURN: {
                if (stmt[CONST_I.RETURN] === undefined) {
                    stmt[CONST_I.RETURN] = true;
                    for (var _i = 0, _a = stmt[CONST_I.NAMES]; _i < _a.length; _i++) {
                        var parameterName = _a[_i];
                        STATE_I.bindVar(parameterName, STATE_I.pop());
                    }
                    var body = STATE_I.getFunction(stmt[CONST_I.NAME])[CONST_I.STATEMENTS];
                    STATE_I.pushOps(true, body);
                } else {
                    STATE_I.clearDangerousProperties(stmt);
                }
                break;
            }
            case CONST_I.MOTOR_ON_ACTION: {
                var duration = STATE_I.pop();
                var speed = STATE_I.pop();
                NATIVE_I.motorOnAction(stmt[CONST_I.NAME], stmt[CONST_I.PORT], duration, speed);
                break;
            }
            case CONST_I.MOTOR_STOP: {
                NATIVE_I.motorStopAction(stmt[CONST_I.NAME], stmt[CONST_I.PORT]);
                break;
            }
            case CONST_I.REPEAT_STMT:
                evalRepeat(stmt);
                break;
            case CONST_I.SHOW_TEXT_ACTION: {
                NATIVE_I.showTextAction(STATE_I.pop());
                break;
            }
            case CONST_I.STATUS_LIGHT_ACTION:
                NATIVE_I.statusLightOffAction('-', '-');
                break;
            case CONST_I.STOP:
                UTIL_I.p("PROGRAM TERMINATED. stop op");
                terminated = true;
                break topLevelLoop;
            case CONST_I.TEXT_JOIN:
                var second = STATE_I.pop();
                var first = STATE_I.pop();
                STATE_I.push('' + first + second);
                break;
            case CONST_I.TIMER_SENSOR_RESET:
                NATIVE_I.timerReset();
                break;
            case CONST_I.VAR_DECLARATION: {
                var name_2 = stmt[CONST_I.NAME];
                STATE_I.bindVar(name_2, STATE_I.pop());
                break;
            }
            case CONST_I.TONE_ACTION: {
                var duration = STATE_I.pop();
                var frequency = STATE_I.pop();
                UTIL_I.p("tone, duration: " + duration + ", frequency: " + frequency);
                break;
            }
            case CONST_I.WAIT_STMT: {
                UTIL_I.p('waitstmt started');
                STATE_I.pushOps(true, stmt[CONST_I.STMT_LIST]);
                break;
            }
            case CONST_I.WAIT_TIME_STMT: {
                var time = STATE_I.pop();
                UTIL_I.p('waiting ' + time + ' msec');
                setTimeout(function() {
                    evalOperation();
                }, time);
                return; // wait for handler being called
            }
            default:
                UTIL_I.dbcException("invalid stmt op: " + opCode);
            }
        }
        // termination either requested by the client or by executing 'stop' or after last statement
        callbackOnTermination();
    }
    exports.evalOperation = evalOperation;
    function evalExpr(expr) {
        var kind = expr[CONST_I.EXPR];
        switch (kind) {
        case CONST_I.VAR:
            STATE_I.push(STATE_I.getVar(expr[CONST_I.NAME]));
            break;
        case CONST_I.NUM_CONST:
            STATE_I.push(+expr[CONST_I.VALUE]);
            break;
        case CONST_I.STRING_CONST:
            STATE_I.push(expr[CONST_I.VALUE]);
            break;
        case CONST_I.COLOR_CONST:
            STATE_I.push(expr[CONST_I.VALUE]);
            break;
        case CONST_I.UNARY: {
            var subOp = expr[CONST_I.OP];
            switch (subOp) {
            case CONST_I.NOT:
                var bool = STATE_I.pop();
                STATE_I.push(!bool);
                break;
            default:
                UTIL_I.dbcException("invalid unary expr subOp: " + subOp);
            }
            break;
        }
        case CONST_I.MATH_CONST: {
            var value = expr[CONST_I.VALUE];
            switch (value) {
            case 'PI':
                STATE_I.push(Math.PI);
                break;
            case 'E':
                STATE_I.push(Math.E);
                break;
            case 'GOLDEN_RATIO':
                STATE_I.push((1.0 + Math.sqrt(5.0)) / 2.0);
                break;
            case 'SQRT2':
                STATE_I.push(Math.SQRT2);
                break;
            case 'SQRT1_2':
                STATE_I.push(Math.SQRT1_2);
                break;
            case 'INFINITY':
                STATE_I.push(Infinity);
                break;
            default:
                throw "Invalid Math Constant Name";
            }
        }
        case CONST_I.SINGLE_FUNCTION: {
            var subOp = expr[CONST_I.OP];
            var value = STATE_I.pop();
            switch (subOp) {
            case 'ROOT':
                STATE_I.push(Math.sqrt(value));
                break;
            case 'ABS':
                STATE_I.push(Math.abs(value));
                break;
            case 'LN':
                STATE_I.push(Math.log(value));
                break;
            case 'LOG10':
                STATE_I.push(Math.log(value) / Math.LN10);
                break;
            case 'EXP':
                STATE_I.push(Math.exp(value));
                break;
            case 'POW10':
                STATE_I.push(Math.pow(10, value));
                break;
            case 'SIN':
                STATE_I.push(Math.sin(value));
                break;
            case 'COS':
                STATE_I.push(Math.cos(value));
                break;
            case 'TAN':
                STATE_I.push(Math.tan(value));
                break;
            case 'ASIN':
                STATE_I.push(Math.asin(value));
                break;
            case 'ATAN':
                STATE_I.push(Math.atan(value));
                break;
            case 'ACOS':
                STATE_I.push(Math.acos(value));
                break;
            case 'ROUND':
                STATE_I.push(Math.round(value));
                break;
            case 'ROUNDUP':
                STATE_I.push(Math.ceil(value));
                break;
            case 'ROUNDDOWN':
                STATE_I.push(Math.floor(value));
                break;
            default:
                throw "Invalid Function Name";
            }
        }
        case CONST_I.MATH_CONSTRAIN_FUNCTION: {
            var max_1 = STATE_I.pop();
            var min_1 = STATE_I.pop();
            var value = STATE_I.pop();
            STATE_I.push(Math.min(Math.max(value, min_1), max_1));
            break;
        }
        case CONST_I.RANDOM_INT: {
            var max = STATE_I.pop();
            var min = STATE_I.pop();
            if (min > max) {
                _a = [ max, min ], min = _a[0], max = _a[1];
            }
            STATE_I.push(Math.floor(Math.random() * (max - min + 1) + min));
            break;
        }
        case CONST_I.RANDOM_DOUBLE:
            STATE_I.push(Math.random());
            break;
        case CONST_I.MATH_PROP_FUNCT: {
            var subOp = expr[CONST_I.OP];
            var value = STATE_I.pop();
            switch (subOp) {
            case 'EVEN':
                STATE_I.push(value % 2 === 0);
                break;
            case 'ODD':
                STATE_I.push(value % 2 !== 0);
                break;
            case 'PRIME':
                STATE_I.push(isPrime(value));
                break;
            case 'WHOLE':
                STATE_I.push(Number(value) === value && value % 1 === 0);
                break;
            case 'POSITIVE':
                STATE_I.push(value >= 0);
                break;
            case 'NEGATIVE':
                STATE_I.push(value < 0);
                break;
            case 'DIVISIBLE_BY':
                var first = STATE_I.pop();
                STATE_I.push(first % value === 0);
                break;
            default:
                throw "Invalid Math Property Function Name";
            }
        }
        case CONST_I.BINARY: {
            var subOp = expr[CONST_I.OP];
            var right = STATE_I.pop();
            var left = STATE_I.pop();
            switch (subOp) {
            case CONST_I.EQ:
                STATE_I.push(left === right);
                break;
            case CONST_I.NEQ:
                STATE_I.push(left !== right);
                break;
            case CONST_I.LT:
                STATE_I.push(left < right);
                break;
            case CONST_I.LTE:
                STATE_I.push(left <= right);
                break;
            case CONST_I.GT:
                STATE_I.push(left > right);
                break;
            case CONST_I.GTE:
                STATE_I.push(left >= right);
                break;
            case CONST_I.AND:
                STATE_I.push(left && right);
                break;
            case CONST_I.OR:
                STATE_I.push(left || right);
                break;
            case CONST_I.ADD:
                STATE_I.push(0 + left + right);
                break;
            case CONST_I.MINUS:
                STATE_I.push(0 + left - right);
                break;
            case CONST_I.MULTIPLY:
                STATE_I.push(0 + left * right);
                break;
            case CONST_I.DIVIDE:
                STATE_I.push(0 + left / right);
                break;
            case CONST_I.POWER:
                STATE_I.push(Math.pow(left, right));
                break;
            default:
                UTIL_I.dbcException("invalid binary expr supOp: " + subOp);
            }
            break;
        }
        default:
            UTIL_I.dbcException("invalid expr op: " + kind);
        }
        var _a;
    }
    function evalRepeat(stmt) {
        var mode = stmt[CONST_I.MODE];
        if (mode === CONST_I.TIMES) {
            if (stmt[CONST_I.VALUE] === undefined) {
                stmt[CONST_I.VALUE] = 0;
                stmt[CONST_I.END] = STATE_I.pop();
            }
            var value = stmt[CONST_I.VALUE] + 1;
            var end_1 = [ CONST_I.END ];
            stmt[CONST_I.VALUE] = value;
            if (value < end_1) {
                STATE_I.pushOps(true, stmt[CONST_I.STMT_LIST]);
            } else {
                STATE_I.clearDangerousProperties(stmt);
            }
        } else if (mode === CONST_I.UNTIL) {
            if (!STATE_I.pop()) {
                STATE_I.pushOps(true, stmt[CONST_I.STMT_LIST]);
            }
        } else if (mode === CONST_I.FOR) {
            var variable = stmt[CONST_I.VAR];
            var actual = STATE_I.getVar(variable);
            var step;
            var end;
            if (stmt[CONST_I.STEP] === undefined) {
                step = STATE_I.pop();
                end = STATE_I.pop();
                stmt[CONST_I.STEP] = step;
                stmt[CONST_I.END] = end;
            } else {
                step = stmt[CONST_I.STEP];
                end = stmt[CONST_I.END];
                actual += step;
                STATE_I.setVar(variable, actual);
            }
            // UTIL_I.p( 'actual:' + actual + ' step:' + step + ' end:' + end );
            if (actual <= end) {
                STATE_I.pushOps(true, stmt[CONST_I.STMT_LIST]);
            } else {
                STATE_I.unbindVar(variable);
                stmt[CONST_I.STEP] = undefined;
                stmt[CONST_I.END] = undefined;
            }
        } else {
            UTIL_I.dbcException("invalid repeat mode: " + mode);
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
