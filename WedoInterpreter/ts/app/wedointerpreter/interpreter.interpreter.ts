import { ARobotBehaviour } from "interpreter.aRobotBehaviour";
import { State } from "interpreter.state";
import * as C from "interpreter.constants";
import * as U from "interpreter.util";

declare var stackmachineJsHelper;

export class Interpreter {

    public breakpoints: any[];
    private terminated = false;
    private callbackOnTermination = undefined;

    private r: ARobotBehaviour;
    private s: State; // the state of the interpreter (ops, pc, bindings, stack, ...)

    private previousBlockId: any;
    private events: any;
    private stepBlock: any;

    /*
     * 
     * . @param generatedCode argument contains the operations and the function definitions
     * . @param r implementation of the ARobotBehaviour class
     * . @param cbOnTermination is called when the program has terminated
     * . @param simBreakpoints is an array containing the breakpoints
    */

    constructor(generatedCode: any, r: ARobotBehaviour, cbOnTermination: () => void, simBreakpoints: any[]) {
        this.terminated = false;
        this.callbackOnTermination = cbOnTermination;
        const stmts = generatedCode[C.OPS];
        const functions = generatedCode[C.FUNCTION_DECLARATION];
        this.r = r;

        this.breakpoints = simBreakpoints;

        this.events = {};
        this.events[C.DEBUG_STEP_INTO] = false;
        this.events[C.DEBUG_BREAKPOINT] = false;
        this.events[C.DEBUG_STEP_OVER] = false;
        this.stepBlock = null;
        this.previousBlockId = null;

        var stop = {};
        stop[C.OPCODE] = "stop";
        stmts.push(stop);
        this.s = new State(stmts, functions);

    }

    /**
     * run the operations.
     * . @param maxRunTime the time stamp at which the run method must have terminated. If 0 run as long as possible.
     */
    public run(maxRunTime: number): number {
        return this.evalOperation(maxRunTime);
    }

    /**
     * return true, if the program is terminated
     */
    public isTerminated() {
        return this.terminated;
    }

    /**
     * force the termination of the program. The termination takes place, when the NEXT operation should be executed. The delay is not significant.
     * The callbackOnTermination function is called
     */
    public terminate() {
        this.terminated = true;
        this.callbackOnTermination();
        this.r.close()
        this.s.removeHighlights([]);
    }

    public getRobotBehaviour() {
        return this.r;
    }

    /** Returns the map of interpreters variables */
    public getVariables() {
        return this.s.getVariables();
    }

    /** Removes all highlights from currently executing blocks*/
    public removeHighlights() {
        this.s.removeHighlights([]);
    }

    /** Sets the debug mode*/
    public setDebugMode(mode) {
        const s = this.s;
        s.setDebugMode(mode)
        if (mode) {
            stackmachineJsHelper.getJqueryObject("#blockly").addClass("debug");
            s.addHighlights(this.breakpoints);
        } else {
            s.removeHighlights(this.breakpoints);
            stackmachineJsHelper.getJqueryObject("#blockly").removeClass("debug");
        }
    }

    /** sets relevant event value to true */
    public addEvent(mode) {
        this.events[mode] = true;
    }

    /** sets relevant event value to false */
    public removeEvent(mode) {
        this.events[mode] = false;
    }


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
     *
     * Debugging functions:
     *
     * -StepOver will step over a given line. If the line contains a function the function will be executed and the result returned without debugging each line.
     * -StepInto If the line does not contain a function it behaves the same as “step over” but if it does the debugger will enter the called function
     * and continue line-by-line debugging there.
     * -BreakPoint will continue execution until the next breakpoint is reached or the program exits.

     */
    private evalOperation(maxRunTime: number) {
        const s = this.s;
        const n = this.r;

        while (maxRunTime >= new Date().getTime() && !n.getBlocking()) {
            let op = s.getOp();
            let results = this.evalSingleOperation(s, n, op);
            let result = results[0];
            let stop = results[1];

            if (s.getDebugMode()) {

                if (this.events[C.DEBUG_BREAKPOINT]) {
                    if (this.isPossibleBreakPoint(op)) {
                        for (let i = 0; i < this.breakpoints.length; i++) {
                            if (op[C.BLOCK_ID] === this.breakpoints[i]) {
                                stackmachineJsHelper.setSimBreak();
                                this.previousBlockId = op[C.BLOCK_ID];
                                this.events[C.DEBUG_BREAKPOINT] = false;
                                return result;
                            }
                        }
                    }
                }

                if (this.events[C.DEBUG_STEP_INTO]) {
                    if (this.isPossibleStepInto(op)) {
                        stackmachineJsHelper.setSimBreak();
                        this.previousBlockId = op[C.BLOCK_ID];
                        this.events[C.DEBUG_STEP_INTO] = false;
                        return result;
                    }
                }

                if (this.events[C.DEBUG_STEP_OVER]) {
                    if (this.stepBlock !== null && !s.beingExecuted(this.stepBlock) && this.isPossibleStepInto(op)) {
                        stackmachineJsHelper.setSimBreak();
                        this.previousBlockId = op[C.BLOCK_ID];
                        this.events[C.DEBUG_STEP_OVER] = false;
                        this.stepBlock = null;
                        return result;
                    } else if (this.stepBlock === null && this.isPossibleStepOver(op)) {
                        this.stepBlock = op;
                    } else if (this.stepBlock === null && this.isPossibleStepInto(op)) {
                        stackmachineJsHelper.setSimBreak();
                        this.previousBlockId = op[C.BLOCK_ID];
                        this.events[C.DEBUG_STEP_OVER] = false;
                        return result;
                    }
                }
            }

            this.previousBlockId = op[C.BLOCK_ID];

            if (result > 0 || stop) {
                return result;
            }
            if (this.terminated) {
                // termination either requested by the client or by executing 'stop' or after last statement
                n.close();
                this.callbackOnTermination()
                return 0;
            }
        }
        return 0;
    }

    /**
     *  called from @see evalOperation() to evaluate all the operations
     *
     * @param s the S (state) component to store the state of the interpretation.
     * @param n the R (robotBehaviour) component for accessing hardware sensors and actors
     * @param stmt the operation to be evaluated
     * @returns [result,stop] result will be time required till next instruction and stop indicates if evalOperation should return result or not.
     */
    private evalSingleOperation(s: any, n: any, stmt: any) {
        s.opLog('actual ops: ');
        s.processBlock(stmt);
        if (stmt === undefined) {
            U.debug('PROGRAM TERMINATED. No ops remaining');
            this.terminated = true;
        } else {
            const opCode = stmt[C.OPCODE];
            switch (opCode) {
                case C.ASSIGN_STMT: {
                    const name = stmt[C.NAME];
                    s.setVar(name, s.pop());
                    break;
                }
                case C.CLEAR_DISPLAY_ACTION: {
                    n.clearDisplay();
                    return [0, true];
                }
                case C.CREATE_DEBUG_ACTION: {
                    U.debug('NYI');
                    break;
                }
                case C.EXPR:
                    this.evalExpr(stmt);
                    break;
                case C.FLOW_CONTROL: {
                    const conditional = stmt[C.CONDITIONAL];
                    const activatedBy: boolean = stmt[C.BOOLEAN] === undefined ? true : stmt[C.BOOLEAN];
                    const doIt: boolean = conditional ? (s.pop() === activatedBy) : true;
                    if (doIt) {
                        s.popOpsUntil(stmt[C.KIND]);
                        if (stmt[C.BREAK]) {
                            s.getOp();
                        }
                        s.terminateBlock(stmt);
                    }
                    break;
                }
                case C.GET_SAMPLE: {
                    n.getSample(s, stmt[C.NAME], stmt[C.GET_SAMPLE], stmt[C.PORT], stmt[C.MODE])
                    break;
                }
                case C.IF_STMT:
                    s.pushOps(stmt[C.STMT_LIST])
                    break;
                case C.NNSTEP_STMT:
                    this.evalNNStep();
                    break;
                case C.IF_TRUE_STMT:
                    if (s.pop()) {
                        s.pushOps(stmt[C.STMT_LIST])
                    }
                    break;
                case C.IF_RETURN:
                    if (s.pop()) {
                        s.pushOps(stmt[C.STMT_LIST])
                    }
                    break;
                case C.LED_ON_ACTION: {
                    const color = s.pop();
                    n.ledOnAction(stmt[C.NAME], stmt[C.PORT], color)
                    break;
                }
                case C.METHOD_CALL_VOID:
                case C.METHOD_CALL_RETURN: {
                    for (let parameterName of stmt[C.NAMES]) {
                        s.bindVar(parameterName, s.pop())
                    }
                    const body = s.getFunction(stmt[C.NAME])[C.STATEMENTS];
                    s.processBlock(body[body.length - 1]);
                    s.pushOps(body);
                    break;
                }
                case C.MOTOR_ON_ACTION: {
                    const speedOnly = stmt[C.SPEED_ONLY];
                    let duration = speedOnly ? undefined : s.pop();
                    const speed = s.pop();
                    const name = stmt[C.NAME];
                    const port = stmt[C.PORT];
                    const durationType = stmt[C.MOTOR_DURATION];
                    if (durationType === C.DEGREE || durationType === C.DISTANCE || durationType === C.ROTATIONS) {
                        // if durationType is defined, then duration must be defined, too. Thus, it is never 'undefined' :-)
                        let rotationPerSecond = C.MAX_ROTATION * Math.abs(speed) / 100.0;
                        duration = duration / rotationPerSecond * 1000;
                        if (durationType === C.DEGREE) {
                            duration /= 360.0;
                        }
                    }
                    n.motorOnAction(name, port, duration, speed);
                    return [duration ? duration : 0, true];
                }
                case C.DRIVE_ACTION: {
                    const speedOnly = stmt[C.SPEED_ONLY];
                    const distance = speedOnly ? undefined : s.pop();
                    const speed = s.pop();
                    const name = stmt[C.NAME];
                    const direction = stmt[C.DRIVE_DIRECTION];
                    const duration = n.driveAction(name, direction, speed, distance);
                    return [duration, true];
                }
                case C.TURN_ACTION: {
                    const speedOnly = stmt[C.SPEED_ONLY];
                    const angle = speedOnly ? undefined : s.pop();
                    const speed = s.pop();
                    const name = stmt[C.NAME];
                    const direction = stmt[C.TURN_DIRECTION];
                    const duration = n.turnAction(name, direction, speed, angle);
                    return [duration, true];
                }
                case C.CURVE_ACTION: {
                    const speedOnly = stmt[C.SPEED_ONLY];
                    const distance = speedOnly ? undefined : s.pop();
                    const speedR = s.pop();
                    const speedL = s.pop();
                    const name = stmt[C.NAME];
                    const direction = stmt[C.DRIVE_DIRECTION];
                    const duration = n.curveAction(name, direction, speedL, speedR, distance);
                    return [duration, true];
                }
                case C.STOP_DRIVE:
                    const name = stmt[C.NAME];
                    n.driveStop(name);
                    return [0, true];
                case C.BOTH_MOTORS_ON_ACTION: {
                    const duration = s.pop();
                    const speedB = s.pop();
                    const speedA = s.pop();
                    const portA = stmt[C.PORT_A];
                    const portB = stmt[C.PORT_B];
                    n.motorOnAction(portA, portA, duration, speedA);
                    n.motorOnAction(portB, portB, duration, speedB);
                    return [duration, true];
                }
                case C.MOTOR_STOP: {
                    n.motorStopAction(stmt[C.NAME], stmt[C.PORT]);
                    return [0, true];
                }
                case C.MOTOR_SET_POWER: {
                    const speed = s.pop();
                    const name = stmt[C.NAME];
                    const port = stmt[C.PORT];
                    n.setMotorSpeed(name, port, speed);
                    return [0, true];
                }
                case C.MOTOR_GET_POWER: {
                    const port = stmt[C.PORT];
                    n.getMotorSpeed(s, name, port);
                    break;
                }
                case C.REPEAT_STMT:
                    this.evalRepeat(stmt);
                    break;
                case C.REPEAT_STMT_CONTINUATION:
                    if (stmt[C.MODE] === C.FOR || stmt[C.MODE] === C.TIMES) {
                        const runVariableName = stmt[C.NAME];
                        const end = s.get1();
                        const incr = s.get0();
                        const value = s.getVar(runVariableName) + incr;
                        if (+value >= +end) {
                            s.popOpsUntil(C.REPEAT_STMT);
                            s.getOp(); // the repeat has terminated
                        } else {
                            s.setVar(runVariableName, value);
                            s.pushOps(stmt[C.STMT_LIST]);
                        }
                    } else if (stmt[C.MODE] === C.FOR_EACH) {
                        const runVariableName = stmt[C.EACH_COUNTER];
                        const varName = stmt[C.NAME];
                        const listName = stmt[C.LIST];
                        const list = s.getVar(listName);
                        const end = list.length;
                        const incr = s.get0();
                        const value = s.getVar(runVariableName) + incr;
                        if (+value >= +end) {
                            s.popOpsUntil(C.REPEAT_STMT);
                            s.getOp(); // the repeat has terminated
                        } else {
                            s.setVar(runVariableName, value);
                            s.bindVar(varName, list[value]);
                            s.pushOps(stmt[C.STMT_LIST]);
                        }
                    }
                    break;
                case C.SHOW_TEXT_ACTION: {
                    const text = s.pop();
                    const name = stmt[C.NAME];
                    if (name === "ev3") {
                        const x = s.pop();
                        const y = s.pop();
                        n.showTextActionPosition(text, x, y);
                        return [0, true];
                    }
                    return [n.showTextAction(text, stmt[C.MODE]), true];
                }
                case C.SHOW_IMAGE_ACTION: {
                    let image;
                    if (stmt[C.NAME] == "ev3") {
                        image = stmt[C.IMAGE];
                    } else {
                        image = s.pop();
                    }
                    return [n.showImageAction(image, stmt[C.MODE]), true];
                }
                case C.DISPLAY_SET_BRIGHTNESS_ACTION: {
                    const b = s.pop();
                    return [n.displaySetBrightnessAction(b), true];
                }

                case C.IMAGE_SHIFT_ACTION: {
                    const nShift = s.pop();
                    const image = s.pop();
                    s.push(this.shiftImageAction(image, stmt[C.DIRECTION], nShift));
                    break;
                }

                case C.DISPLAY_SET_PIXEL_BRIGHTNESS_ACTION: {
                    const b = s.pop();
                    const y = s.pop();
                    const x = s.pop();
                    return [n.displaySetPixelBrightnessAction(x, y, b), true];

                }
                case C.DISPLAY_GET_PIXEL_BRIGHTNESS_ACTION: {
                    const y = s.pop();
                    const x = s.pop();
                    n.displayGetPixelBrightnessAction(s, x, y);
                    break;
                }
                case C.LIGHT_ACTION:
                    n.lightAction(stmt[C.MODE], stmt[C.COLOR]);
                    return [0, true];
                case C.STATUS_LIGHT_ACTION:
                    n.statusLightOffAction(stmt[C.NAME], stmt[C.PORT])
                    return [0, true];
                case C.STOP:
                    U.debug("PROGRAM TERMINATED. stop op");
                    this.terminated = true;
                    break;
                case C.TEXT_JOIN: {
                    const n = stmt[C.NUMBER];
                    var result = new Array(n);
                    for (let i = 0; i < n; i++) {
                        const e = s.pop();
                        result[n - i - 1] = e;
                    }
                    s.push(result.join(""));
                    break;
                }
                case C.TIMER_SENSOR_RESET:
                    n.timerReset(stmt[C.PORT]);
                    break;
                case C.ENCODER_SENSOR_RESET:
                    n.encoderReset(stmt[C.PORT]);
                    return [0, true];
                case C.GYRO_SENSOR_RESET:
                    n.gyroReset(stmt[C.PORT]);
                    return [0, true];
                case C.TONE_ACTION: {
                    const duration = s.pop();
                    const frequency = s.pop();
                    return [n.toneAction(stmt[C.NAME], frequency, duration), true];
                }
                case C.PLAY_FILE_ACTION:
                    return [n.playFileAction(stmt[C.FILE]), true];
                case C.SET_VOLUME_ACTION:
                    n.setVolumeAction(s.pop());
                    return [0, true];
                case C.GET_VOLUME:
                    n.getVolumeAction(s);
                    break;
                case C.SET_LANGUAGE_ACTION:
                    n.setLanguage(stmt[C.LANGUAGE]);
                    break;
                case C.SAY_TEXT_ACTION: {
                    const pitch = s.pop();
                    const speed = s.pop();
                    const text = s.pop();
                    return [n.sayTextAction(text, speed, pitch), true];
                }
                case C.VAR_DECLARATION: {
                    const name = stmt[C.NAME];
                    s.bindVar(name, s.pop());
                    break;
                }
                case C.WAIT_STMT: {
                    U.debug('waitstmt started');
                    s.pushOps(stmt[C.STMT_LIST]);
                    break;
                }
                case C.WAIT_TIME_STMT: {
                    const time = s.pop();
                    return [time, true]; // wait for handler being called
                }
                case C.WRITE_PIN_ACTION: {
                    const value = s.pop();
                    const mode = stmt[C.MODE];
                    const pin = stmt[C.PIN];
                    n.writePinAction(pin, mode, value);
                    return [0, true];
                }
                case C.LIST_OPERATION: {
                    const op = stmt[C.OP];
                    const loc = stmt[C.POSITION];
                    let ix = 0;
                    if (loc != C.LAST && loc != C.FIRST) {
                        ix = s.pop();
                    }
                    const value = s.pop();
                    let list = s.pop();
                    ix = this.getIndex(list, loc, ix)
                    if (op == C.SET) {
                        list[ix] = value;
                    } else if (op == C.INSERT) {
                        if (loc === C.LAST) {
                            list.splice(ix + 1, 0, value);
                        } else {
                            list.splice(ix, 0, value);
                        }
                    }
                    break;
                }
                case C.TEXT_APPEND:
                case C.MATH_CHANGE: {
                    const value = s.pop();
                    const name = stmt[C.NAME];
                    s.bindVar(name, s.pop() + value);
                    break;
                }
                case C.DEBUG_ACTION: {
                    const value = s.pop();
                    n.debugAction(value);
                    break;
                }
                case C.ASSERT_ACTION: {
                    const right = s.pop();
                    const left = s.pop();
                    const value = s.pop();
                    n.assertAction(stmt[C.MSG], left, stmt[C.OP], right, value);
                    break;
                }
                case C.COMMENT: {
                    break;
                }
                case C.INITIATE_BLOCK: {
                    break;
                }
                case C.TERMINATE_BLOCK: {
                    s.terminateBlock(stmt);
                    break;
                }
                default:
                    U.dbcException("invalid stmt op: " + opCode);
            }
        }
        return [0, false];
    }

    /**
     *  called from @see evalSingleOperation() to evaluate all kinds of expressions
     *
     * . @param expr to be evaluated
     */
    private evalExpr(expr) {
        const kind = expr[C.EXPR];
        const s = this.s;
        switch (kind) {
            case C.VAR:
                s.push(s.getVar(expr[C.NAME]));
                break;
            case C.NUM_CONST:
                s.push(+expr[C.VALUE]);
                break;
            case C.CREATE_LIST: {
                const n = expr[C.NUMBER];
                var arr = new Array(n);
                for (let i = 0; i < n; i++) {
                    const e = s.pop();
                    arr[n - i - 1] = e;
                }
                s.push(arr);
                break;
            }
            case C.CREATE_LIST_REPEAT: {
                const rep = s.pop();
                const val = s.pop();
                var arr = new Array();
                for (let i = 0; i < rep; i++) {
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
                const b = s.pop();
                const g = s.pop();
                const r = s.pop();
                s.push([r, g, b]);
                break;
            }
            case C.UNARY: {
                const subOp = expr[C.OP];
                switch (subOp) {
                    case C.NOT:
                        var truthy;
                        const bool = s.pop();
                        if (bool === 'true') {
                            truthy = true;
                        } else if (bool === 'false' || bool === '0' || bool === '') {
                            truthy = false;
                        } else {
                            truthy = !!bool
                        }
                        s.push(!truthy);
                        break;
                    case C.NEG:
                        const value = s.pop();
                        s.push(-value);
                        break;
                    default:
                        U.dbcException("invalid unary expr subOp: " + subOp);
                }
                break;
            }
            case C.MATH_CONST: {
                const value = expr[C.VALUE];
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
                const subOp = expr[C.OP];
                const value = s.pop();
                U.debug('---------- ' + subOp + ' with ' + value)
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
                const max = s.pop();
                const min = s.pop();
                const value = s.pop();
                s.push(Math.min(Math.max(value, min), max));
                break;
            }
            case C.RANDOM_INT: {
                var max = s.pop();
                var min = s.pop();
                if (min > max) {
                    [min, max] = [max, min];
                }
                s.push(Math.floor(Math.random() * (max - min + 1) + min));
                break;
            }
            case C.RANDOM_DOUBLE:
                s.push(Math.random());
                break;
            case C.MATH_PROP_FUNCT: {
                const subOp = expr[C.OP];
                const value = s.pop();
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
                        const first = s.pop();
                        s.push(first % value === 0);
                        break;
                    default:
                        throw "Invalid Math Property Function Name";
                }
                break;
            }
            case C.MATH_ON_LIST: {
                const subOp = expr[C.OP];
                const value = s.pop();
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
            case C.CAST_STRING: {
                var num = s.pop();
                s.push(num.toString());
                break;
            }
            case C.CAST_CHAR: {
                var num = s.pop();
                s.push(String.fromCharCode(num));
                break;
            }
            case C.CAST_STRING_NUMBER: {
                var value = s.pop();
                s.push(parseFloat(value));
                break;
            }
            case C.CAST_CHAR_NUMBER: {
                var index = s.pop();
                var value = s.pop();
                s.push(value.charCodeAt(index));
                break;
            }
            case C.LIST_OPERATION: {
                const subOp = expr[C.OP];
                switch (subOp) {
                    case C.LIST_IS_EMPTY:
                        s.push(s.pop().length == 0);
                        break;
                    case C.LIST_LENGTH:
                        s.push(s.pop().length);
                        break;
                    case C.LIST_FIND_ITEM: {
                        const item = s.pop();
                        const list = s.pop();
                        if (expr[C.POSITION] == C.FIRST) {
                            s.push(list.indexOf(item));
                        } else {
                            s.push(list.lastIndexOf(item));
                        }
                    }
                        break;
                    case C.GET:
                    case C.REMOVE:
                    case C.GET_REMOVE: {
                        const loc = expr[C.POSITION];
                        let ix = 0;
                        if (loc != C.LAST && loc != C.FIRST) {
                            ix = s.pop();
                        }
                        let list = s.pop();
                        ix = this.getIndex(list, loc, ix)
                        let v = list[ix];
                        if (subOp == C.GET_REMOVE || subOp == C.GET) {
                            s.push(v);
                        }
                        if (subOp == C.GET_REMOVE || subOp == C.REMOVE) {
                            list.splice(ix, 1);
                        }
                    }
                        break;
                    case C.LIST_GET_SUBLIST: {
                        const position = expr[C.POSITION];
                        let start_ix;
                        let end_ix;
                        if (position[1] != C.LAST) {
                            end_ix = s.pop();
                        }
                        if (position[0] != C.FIRST) {
                            start_ix = s.pop();
                        }
                        let list = s.pop();
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
                const subOp = expr[C.OP];
                const right = s.pop();
                const left = s.pop();
                s.push(this.evalBinary(subOp, left, right));
                break;
            }

            default:
                U.dbcException("invalid expr op: " + kind);
        }
    }

    private evalBinary(subOp: string, left: any, right: any): any {
        let leftIsArray = Array.isArray(left);
        let rightIsArray = Array.isArray(right);

        if (leftIsArray && rightIsArray) {
            let leftLen = left.length;
            let rightLen = right.length;
            switch (subOp) {
                case C.EQ:
                    if (leftLen === rightLen) {
                        for (var i = 0; i < leftLen; i++) {
                            if (!this.evalBinary(subOp, left[i], right[i])) {
                                return false;
                            }
                        }
                        return true;
                    } else {
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
                    } else {
                        return true;
                    }
                default:
                    U.dbcException("invalid binary expr supOp for array-like structures: " + subOp);
            }
        } else if (leftIsArray || rightIsArray) {
            return false;
        } else {
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
                    U.dbcException("invalid binary expr supOp: " + subOp);
            }
        }
    }

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
    private evalRepeat(stmt: any) {
        const s = this.s;
        const mode = stmt[C.MODE];
        const contl: any[] = stmt[C.STMT_LIST];
        if (contl.length !== 1 || contl[0][C.OPCODE] !== C.REPEAT_STMT_CONTINUATION) {
            U.dbcException("repeat expects an embedded continuation statement");
        }
        const cont = contl[0];
        switch (mode) {
            case C.FOREVER:
            case C.UNTIL:
            case C.WHILE:
                s.pushOps(contl);
                s.getOp(); // pseudo execution. Init is already done. Continuation is for termination only.
                s.pushOps(cont[C.STMT_LIST]);
                break;

            case C.FOR_EACH: {
                const runVariableName = stmt[C.EACH_COUNTER];
                const varName = stmt[C.NAME];
                const listName = stmt[C.LIST];
                const start = s.get1();
                const list = s.getVar(listName)
                const end = list.length;
                if (+start >= +end) {
                    s.pop();
                    s.pop();
                    s.pop();
                } else {
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
                const runVariableName = stmt[C.NAME];
                const start = s.get2();
                const end = s.get1();
                if (+start >= +end) {
                    s.pop();
                    s.pop();
                    s.pop();
                } else {
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
    }

    private evalNNStep() {
        console.log('NNStep encountered');
        const s = this.s;
        let i2 = s.pop();
        let i1 = s.pop();
        let i0 = s.pop();
        let inputData = [i0, i1, i2];
        let pg = require("playground");
        let outputData = pg.oneStep(inputData);
        for (let i = outputData.length - 1; i >= 0; i--) {
            s.push(outputData[i]);
        }
    }

    /**
     * return true if the parameter is prime
     *
     * . @param n to be checked for primality
     */
    private isPrime(n: number) {
        if (n < 2) {
            return false;
        }
        if (n === 2) {
            return true;
        }
        if (n % 2 === 0) {
            return false;
        }
        for (let i = 3, s = Math.sqrt(n); i <= s; i += 2) {
            if (n % i === 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * return true if the value is whole number
     *
     * . @param value to be checked
     */
    private isWhole(value: number) {
        return Number(value) === value && value % 1 === 0
    }


    private min(values: Array<number>): number {
        return Math.min.apply(null, values);
    }

    private max(values: Array<number>): number {
        return Math.max.apply(null, values);
    }

    private sum(values: Array<number>): number {
        return values.reduce((a, b) => a + b, 0);
    }

    private mean(value: Array<number>): number {
        const v = this.sum(value) / value.length;
        return Number(v.toFixed(2));
    }

    private median(values: Array<number>): number {
        values.sort((a, b) => a - b);
        const median = (values[(values.length - 1) >> 1] + values[values.length >> 1]) / 2;
        return Number(median.toFixed(2))
    }

    private std(values: Array<number>): number {
        const avg = this.mean(values);
        const diffs = values.map((value) => value - avg);
        const squareDiffs = diffs.map((diff) => diff * diff);
        const avgSquareDiff = this.mean(squareDiffs);
        return Number(Math.sqrt(avgSquareDiff).toFixed(2));

    }

    private getRandomInt(max: number): number {
        return Math.floor(Math.random() * Math.floor(max));
    }

    //    private round2precision( x: number, precision: number ): number {
    //        var y = +x + ( precision === undefined ? 0.5 : precision / 2 );
    //        return y - ( y % ( precision === undefined ? 1 : +precision ) );
    //    }

    private getIndex(list: Array<any>, loc: string, ix: number): number {
        if (loc == C.FROM_START) {
            return ix;
        } else if (loc == C.FROM_END) {
            return list.length - 1 - ix;
        } else if (loc == C.FIRST) {
            return 0;
        } else if (loc == C.LAST) {
            return list.length - 1;
        } else {
            throw 'Unhandled option (lists_getSublist).';
        }
    }

    private invertImage(image: any): any {
        for (var i = 0; i < image.length; i++) {
            for (var j = 0; j < image[i].length; j++) {
                image[i][j] = Math.abs(255 - image[i][j]);
            }
        }
        return image;
    }

    private shiftImageAction(image: number[][], direction: string, nShift: number): number[][] {
        nShift = Math.round(nShift);
        var shift = {
            down: function() {
                image.pop();
                image.unshift([0, 0, 0, 0, 0]);
            },
            up: function() {
                image.shift();
                image.push([0, 0, 0, 0, 0]);
            },
            right: function() {
                image.forEach(function(array: number[]) {
                    array.pop();
                    array.unshift(0);
                });
            },
            left: function() {
                image.forEach(function(array: number[]) {
                    array.shift();
                    array.push(0);
                });
            }
        };
        if (nShift < 0) {
            nShift *= -1;
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
        for (var i = 0; i < nShift; i++) {
            shift[direction]();
        }
        return image;
    }

    /** Returns true if the operation is a possible breakpoint*/
    private isPossibleBreakPoint(op) {
        if (op.hasOwnProperty(C.BLOCK_ID)) {
            if (op[C.BLOCK_ID] !== this.previousBlockId) {
                switch (op[C.OPCODE]) {
                    case C.INITIATE_BLOCK:
                    case C.REPEAT_STMT_CONTINUATION:
                    case C.REPEAT_STMT:
                    case C.METHOD_CALL_VOID:
                    case C.METHOD_CALL_RETURN:
                        return true;
                    default:
                        return false;
                }
            }
        }
        return false;
    }

    /** Returns true if the operation is a possible block where stepInto should stop*/
    private isPossibleStepInto(op) {
        if (op.hasOwnProperty(C.BLOCK_ID)) {
            if (this.previousBlockId == null || op[C.BLOCK_ID] !== this.previousBlockId) {
                switch (op[C.OPCODE]) {
                    case C.INITIATE_BLOCK: {
                        switch (op[C.OP]) {
                            case C.EXPR:
                            case C.GET_SAMPLE:
                            case C.VAR_DECLARATION:
                                return false;

                        }
                        return true;
                    }
                    case C.REPEAT_STMT:
                    case C.REPEAT_STMT_CONTINUATION:
                    case C.METHOD_CALL_VOID:
                    case C.METHOD_CALL_RETURN:
                        return true;
                    default: {
                        return false;
                    }
                }
            }
            return false;
        }
    }

    /** Returns true if the operation is a possible block where stepOver should stop*/
    private isPossibleStepOver(op) {
        if (op.hasOwnProperty(C.BLOCK_ID)) {
            switch (op[C.OPCODE]) {
                case C.METHOD_CALL_VOID:
                case C.METHOD_CALL_RETURN:
                    return true;
                case C.INITIATE_BLOCK: {
                    switch (op[C.OP]) {
                        case C.METHOD_CALL_VOID:
                        case C.METHOD_CALL_RETURN:
                            return true;
                    }
                    return false;
                }
                default: {
                    return false;
                }
            }
        }
        return false;
    }


}
