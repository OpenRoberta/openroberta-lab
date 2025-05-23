import { ARobotBehaviour } from './interpreter.aRobotBehaviour';
import { State } from './interpreter.state';
import { Network } from './neuralnetwork.nn';
import { getNetwork, setupNN } from './neuralnetwork.ui';
import * as C from './interpreter.constants';
import * as U from './interpreter.util';
import { SimulationRoberta } from 'simulation.roberta';
import * as UTIL from 'util.roberta';

export class Interpreter {
    public breakpoints: any[];
    private _name: string;
    private terminated = false;
    private callbackOnTermination = undefined;
    private robotBehaviour: ARobotBehaviour;
    private state: State; // the state of the interpreter (ops, pc, bindings, stack, ...)
    private events: any;
    private stepOverBlock: any;
    private lastStoppedBlock: any;
    private lastBlock: any;

    private _neuralNetwork: Network;
    private _updateNNView: boolean;

    private readonly debugDelay = 2;

    /*
     *
     * . @param generatedCode argument contains the operations and the function definitions
     * . @param robotBehaviour implementation of the ARobotBehaviour class
     * . @param cbOnTermination is called when the program has terminated
     */
    constructor(generatedCode: any, r: ARobotBehaviour, cbOnTermination: () => void, simBreakpoints: any[], name: string, updateNNView: boolean) {
        this.terminated = false;
        this.callbackOnTermination = cbOnTermination;
        const stmts = generatedCode[C.OPS];
        this.robotBehaviour = r;
        this.name = name;
        this.updateNNView = updateNNView;

        this.breakpoints = simBreakpoints;

        this.events = {};
        this.events[C.DEBUG_STEP_INTO] = false;
        this.events[C.DEBUG_BREAKPOINT] = false;
        this.events[C.DEBUG_STEP_OVER] = false;

        this.lastBlock = null;
        this.lastStoppedBlock = null;
        this.stepOverBlock = null;

        this.state = new State(stmts);
    }

    get name(): string {
        return this._name;
    }

    set name(value: string) {
        this._name = value;
    }

    get neuralNetwork(): Network {
        return this._neuralNetwork;
    }

    set neuralNetwork(network: Network) {
        this._neuralNetwork = network;
    }

    get updateNNView(): boolean {
        return this._updateNNView;
    }

    set updateNNView(value: boolean) {
        this._updateNNView = value;
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
        this.robotBehaviour.close();
        this.state.removeHighlights([]);
    }

    public getRobotBehaviour() {
        return this.robotBehaviour;
    }

    /** Returns the map of interpreters variables */
    public getVariables() {
        return this.state.getVariables();
    }

    /** Removes all highlights from currently executing blocks*/
    public removeHighlights() {
        this.state.removeHighlights([]);
    }

    /** Sets the debug mode*/
    public setDebugMode(mode) {
        this.state.setDebugMode(mode);
        if (mode) {
            $('#blocklyDiv').addClass('debug');
            this.state.addHighlights(this.breakpoints);
        } else {
            this.state.removeHighlights(this.breakpoints);
            $('#blocklyDiv').removeClass('debug');
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
    private evalOperation(maxRunTime: number) {
        while (maxRunTime >= new Date().getTime() && !this.robotBehaviour.getBlocking()) {
            let op = this.state.getOp();
            this.state.evalHighlightings(op, this.lastBlock);

            if (this.state.getDebugMode()) {
                let canContinue = this.calculateDebugBehaviour(op);
                if (!canContinue) return 0;
            }

            let [result, stop] = this.evalSingleOperation(op);
            this.lastStoppedBlock = null;
            this.lastBlock = op;

            if (result > 0 || stop) {
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
    }

    /**
     * Is responsible for all debugging behavior
     * @param op
     * @return whether the interpreter can continue evaluating the operation
     * @private
     */
    private calculateDebugBehaviour(op): boolean {
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
            } else if (this.stepOverBlock === null && Interpreter.isPossibleStepOver(op)) {
                this.stepOverBlock = op;
            } else if (this.stepOverBlock === null && this.lastStoppedBlock !== op && Interpreter.isPossibleStepInto(op)) {
                this.stepOver(op);
                return false;
            }
        }

        return true;
    }

    private stepOver(op) {
        SimulationRoberta.Instance.setPause(true);
        this.events[C.DEBUG_STEP_OVER] = false;
        this.stepOverBlock = null;
        this.lastStoppedBlock = op;
    }

    private stepInto(op) {
        SimulationRoberta.Instance.setPause(true);
        this.events[C.DEBUG_STEP_INTO] = false;
        this.lastStoppedBlock = op;
    }

    private breakPoint(op) {
        SimulationRoberta.Instance.setPause(true);
        this.events[C.DEBUG_BREAKPOINT] = false;
        this.lastStoppedBlock = op;
    }

    /**
     *  called from @see evalOperation() to evaluate all the operations
     *
     * @param stmt the operation to be evaluated
     * @returns [result,stop] result will be time required till next instruction and stop indicates if evalOperation should return result or not.
     */
    private evalSingleOperation(stmt: any) {
        this.state.opLog('actual ops: ');
        this.state.incrementProgramCounter();
        if (stmt === undefined) {
            U.debug('PROGRAM TERMINATED. No ops remaining');
            this.terminated = true;
        } else {
            const opCode = stmt[C.OPCODE];
            switch (opCode) {
                case C.JUMP: {
                    const condition = stmt[C.CONDITIONAL];
                    if (condition === C.ALWAYS || this.state.pop() === condition) {
                        this.state.pc = stmt[C.TARGET];
                    }
                    break;
                }
                case C.CALL: {
                    this.state.pc = stmt[C.TARGET];
                    break;
                }
                case C.RETURN_ADDRESS:
                    this.state.push(+stmt[C.TARGET]);
                    break;
                case C.ASSIGN_STMT: {
                    const name = stmt[C.NAME];
                    this.state.setVar(name, this.state.pop());
                    break;
                }
                case C.POP: {
                    this.state.pop();
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
                    this.robotBehaviour.getSample(this.state, stmt[C.NAME], stmt[C.GET_SAMPLE], stmt[C.PORT], stmt[C.MODE], stmt[C.SLOT]);
                    break;
                }
                case C.COLOUR_COMPARE: {
                    const tolerance = this.state.pop();
                    const colour2 = this.state.pop();
                    const colour1 = this.state.pop();
                    this.state.push(this.robotBehaviour.colourCompare(colour1, colour2, tolerance));
                    break;
                }
                case C.MARKER:
                    const markerId = this.state.pop();
                    this.robotBehaviour.getSample(this.state, stmt[C.NAME], stmt[C.GET_SAMPLE], stmt[C.PORT], stmt[C.MODE], markerId);
                    break;
                case C.NEURAL_NETWORK:
                    setupNN(stmt);
                    this.neuralNetwork = getNetwork();
                    break;
                case C.NN_STEP_STMT:
                    this.neuralNetwork.forwardProp();
                    break;
                case C.NN_SETINPUTNEURON_STMT:
                    this.neuralNetwork.setInputNeuronVal(stmt[C.NAME], this.state.pop());
                    break;
                case C.NN_SETWEIGHT_STMT:
                    this.neuralNetwork.changeWeight(stmt[C.FROM], stmt[C.TO], this.state.pop());
                    break;
                case C.NN_SETBIAS_STMT:
                    this.neuralNetwork.changeBias(stmt[C.NAME], this.state.pop());
                    break;
                case C.RGBLED_ON_ACTION: {
                    const color = this.state.pop();
                    this.robotBehaviour.ledOnAction(stmt[C.NAME], stmt[C.PORT], color);
                    break;
                }
                case C.LED_ACTION: {
                    if (stmt[C.MODE] === 'ON') {
                        this.robotBehaviour.ledOnAction(stmt[C.NAME], stmt[C.PORT], null);
                    } else {
                        this.robotBehaviour.ledOffAction(stmt[C.NAME], stmt[C.PORT]);
                    }
                    return [0, true];
                }
                case C.REMEMBER: {
                    const num = this.state.pop();
                    this.robotBehaviour.remember(num);
                    break;
                }
                case C.RECALL: {
                    this.robotBehaviour.recall(this.state);
                    break;
                }
                case C.RETURN:
                    let returnValue: any;
                    if (stmt[C.VALUES]) returnValue = this.state.pop();

                    const returnAddress = this.state.pop();
                    this.state.pc = returnAddress;

                    if (stmt[C.VALUES]) this.state.push(returnValue);
                    break;
                case C.MOTOR_ON_ACTION: {
                    const speedOnly = stmt[C.SPEED_ONLY];
                    const setTime = stmt[C.SET_TIME];
                    let time;
                    let duration;
                    if (setTime) {
                        duration = undefined;
                        time = setTime ? this.state.pop() : undefined;
                    } else {
                        time = undefined;
                        duration = speedOnly ? undefined : this.state.pop();
                    }
                    const speed = this.state.pop();
                    const name = stmt[C.NAME];
                    const port = stmt[C.PORT];
                    const durationType = stmt[C.MOTOR_DURATION];
                    const durationA = this.robotBehaviour.motorOnAction(name, port, durationType, duration, speed, time);
                    return [durationA, true];
                }
                case C.DRIVE_ACTION: {
                    const speedOnly = stmt[C.SPEED_ONLY];
                    const setTime = stmt[C.SET_TIME];
                    const name = stmt[C.NAME];
                    let time;
                    let distance;

                    if (setTime) {
                        distance = undefined;
                        time = setTime ? this.state.pop() : undefined;
                    } else {
                        time = undefined;
                        distance = speedOnly ? undefined : this.state.pop();
                    }
                    const speed = this.state.pop();
                    const direction = stmt[C.DRIVE_DIRECTION];
                    const duration = this.robotBehaviour.driveAction(name, direction, speed, distance, time);
                    return [duration, true];
                }
                case C.TURN_ACTION: {
                    const speedOnly = stmt[C.SPEED_ONLY];
                    const setTime = stmt[C.SET_TIME];

                    let time;
                    let angle;

                    if (setTime) {
                        angle = undefined;
                        time = setTime ? this.state.pop() : undefined;
                    } else {
                        time = undefined;
                        angle = speedOnly ? undefined : this.state.pop();
                    }
                    const speed = this.state.pop();
                    const name = stmt[C.NAME];
                    const direction = stmt[C.TURN_DIRECTION];
                    const duration = this.robotBehaviour.turnAction(name, direction, speed, angle, time);
                    return [duration, true];
                }
                case C.CURVE_ACTION: {
                    const speedOnly = stmt[C.SPEED_ONLY];
                    const setTime = stmt[C.SET_TIME];
                    let time;
                    let distance;

                    if (setTime) {
                        distance = undefined;
                        time = setTime ? this.state.pop() : undefined;
                    } else {
                        time = undefined;
                        distance = speedOnly ? undefined : this.state.pop();
                    }
                    const speedR = this.state.pop();
                    const speedL = this.state.pop();
                    const name = stmt[C.NAME];
                    const direction = stmt[C.DRIVE_DIRECTION];
                    const duration = this.robotBehaviour.curveAction(name, direction, speedL, speedR, distance, time);
                    return [duration, true];
                }
                case C.STOP_DRIVE:
                    const name = stmt[C.NAME];
                    this.robotBehaviour.driveStop(name);
                    return [0, true];
                case C.BOTH_MOTORS_ON_ACTION: {
                    const speedB = this.state.pop();
                    const speedA = this.state.pop();
                    const portA = stmt[C.PORT_A];
                    const portB = stmt[C.PORT_B];
                    this.robotBehaviour.motorOnAction(portA, portA, '', 0, speedA, 0);
                    this.robotBehaviour.motorOnAction(portB, portB, '', 0, speedB, 0);
                    return [0, true];
                }
                case C.MOTOR_STOP: {
                    this.robotBehaviour.motorStopAction(stmt[C.NAME], stmt[C.PORT]);
                    return [0, true];
                }
                case C.MOTOR_SET_POWER: {
                    const speed = this.state.pop();
                    const name = stmt[C.NAME];
                    const port = stmt[C.PORT];
                    this.robotBehaviour.setMotorSpeed(name, port, speed);
                    return [0, true];
                }
                case C.MOTOR_GET_POWER: {
                    const port = stmt[C.PORT];
                    this.robotBehaviour.getMotorSpeed(this.state, name, port);
                    break;
                }
                case C.OMNI_DRIVE: {
                    const thetaVel = this.state.pop();
                    const yVel = this.state.pop();
                    const xVel = this.state.pop();
                    this.robotBehaviour.omniDriveAction(xVel, yVel, thetaVel);
                    return [0, true];
                    break;
                }
                case C.OMNI_DRIVE_DIST: {
                    const distance = this.state.pop();
                    const yVel = this.state.pop();
                    const xVel = this.state.pop();
                    return [this.robotBehaviour.omniDriveDistAction(xVel, yVel, distance), true];
                    break;
                }
                case C.OMNI_DRIVE_STOP: {
                    this.robotBehaviour.omniStopDriveAction();
                    return [0, true];
                }
                case C.OMNI_DRIVE_TURN: {
                    const angle = this.state.pop();
                    const thetaVel = this.state.pop();
                    const direction = stmt[C.TURN_DIRECTION];
                    return [this.robotBehaviour.omniDriveTurnAction(direction, thetaVel, angle), true];
                    break;
                }
                case C.OMNI_DRIVE_POSITION: {
                    const power = this.state.pop();
                    const y = this.state.pop();
                    const x = this.state.pop();
                    return [this.robotBehaviour.omniDrivePositionAction(power, x, y), true];
                    break;
                }
                case C.SHOW_TEXT_ACTION: {
                    const text = this.state.pop();
                    const name = stmt[C.NAME];
                    switch (name) {
                        case 'ev3':
                        case 'txt4':
                        case 'rcj':
                            let x = 0;
                            if (name === 'ev3') {
                                x = this.state.pop();
                            }
                            const y = this.state.pop();
                            this.robotBehaviour.showTextActionPosition(text, x, y);
                            return [0, true];
                        default:
                            return [this.robotBehaviour.showTextAction(text, stmt[C.MODE]), true];
                    }
                }
                case C.SHOW_IMAGE_ACTION: {
                    let image;
                    if (stmt[C.NAME] == 'ev3') {
                        image = stmt[C.IMAGE];
                    } else {
                        image = this.state.pop();
                    }
                    return [this.robotBehaviour.showImageAction(image, stmt[C.MODE]), true];
                }
                case C.DISPLAY_SET_BRIGHTNESS_ACTION: {
                    const b = this.state.pop();
                    return [this.robotBehaviour.displaySetBrightnessAction(b), true];
                }

                case C.IMAGE_SHIFT_ACTION: {
                    const nShift = this.state.pop();
                    const image = this.state.pop();
                    if (stmt[C.NAME] === 'mbot') {
                        this.state.push(this.shiftImageActionMbot(image, stmt[C.DIRECTION], nShift));
                    } else {
                        this.state.push(this.shiftImageAction(image, stmt[C.DIRECTION], nShift));
                    }
                    break;
                }

                case C.DISPLAY_SET_PIXEL_BRIGHTNESS_ACTION: {
                    const b = this.state.pop();
                    const y = this.state.pop();
                    const x = this.state.pop();
                    return [this.robotBehaviour.displaySetPixelBrightnessAction(x, y, b), true];
                }
                case C.DISPLAY_GET_PIXEL_BRIGHTNESS_ACTION: {
                    const y = this.state.pop();
                    const x = this.state.pop();
                    this.robotBehaviour.displayGetPixelBrightnessAction(this.state, x, y);
                    break;
                }
                case C.LIGHT_ACTION:
                    let color;
                    if (stmt[C.NAME] === 'mbot' || stmt[C.NAME] === 'txt4') {
                        const rgb = this.state.pop();
                        color = 'rgb(' + rgb[0] + ',' + rgb[1] + ',' + rgb[2] + ')';
                    } else {
                        color = stmt[C.COLOR];
                    }
                    this.robotBehaviour.lightAction(stmt[C.MODE], color, stmt[C.PORT]);
                    return [0, true];
                case C.RGBLED_OFF_ACTION:
                    this.robotBehaviour.ledOffAction(stmt[C.NAME], stmt[C.PORT]);
                    return [0, true];
                case C.CIRCLE_LED_ACTION:
                case C.PROXH_LED_ACTION:
                    var ledValues = new Array(8);
                    for (let i = 0; i < 8; i++) {
                        const val = this.state.pop();
                        ledValues[7 - i] = val;
                    }
                    if (opCode === C.CIRCLE_LED_ACTION) {
                        this.robotBehaviour.circleLedAction(ledValues);
                    } else {
                        this.robotBehaviour.proxHLedAction(ledValues);
                    }
                    return [0, true];
                case C.BUTTON_LED_ACTION:
                    var ledValues = new Array(4);
                    for (let i = 0; i < 4; i++) {
                        const val = this.state.pop();
                        ledValues[3 - i] = val;
                    }
                    this.robotBehaviour.buttonLedAction(ledValues);
                    return [0, true];
                case C.SOUND_LED_ACTION:
                    const val = this.state.pop();
                    this.robotBehaviour.soundLedAction(val);
                    return [0, true];
                case C.TEMPERATURE_LED_ACTION:
                    const red = this.state.pop();
                    const blue = this.state.pop();
                    this.robotBehaviour.temperatureLedAction(blue, red);
                    return [0, true];
                case C.STOP:
                    U.debug('PROGRAM TERMINATED. stop op');
                    this.terminated = true;
                    break;
                case C.TEXT_JOIN: {
                    const n = stmt[C.NUMBER];
                    var result = new Array(n);
                    for (let i = 0; i < n; i++) {
                        const e = this.state.pop();
                        result[n - i - 1] = e;
                    }
                    this.state.push(result.join(''));
                    break;
                }
                case C.TIMER_SENSOR_RESET:
                    this.robotBehaviour.timerReset(stmt[C.PORT]);
                    return [0, true];
                case C.ENCODER_SENSOR_RESET:
                    this.robotBehaviour.encoderReset(stmt[C.PORT]);
                    return [0, true];
                case C.GYRO_SENSOR_RESET:
                    this.robotBehaviour.gyroReset(stmt[C.PORT]);
                    return [0, true];
                case C.ODOMETRY_SENSOR_RESET:
                    this.robotBehaviour.odometryReset(stmt[C.SLOT]);
                    return [0, true];
                case C.TONE_ACTION: {
                    const duration = this.state.pop();
                    const frequency = this.state.pop();
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
                    const pitch = this.state.pop();
                    const speed = this.state.pop();
                    const text = this.state.pop();
                    return [this.robotBehaviour.sayTextAction(text, speed, pitch), true];
                }
                case C.UNBIND_VAR:
                    const variableToUnbind = stmt[C.NAME];
                    this.state.unbindVar(variableToUnbind);
                    break;
                case C.VAR_DECLARATION: {
                    const name = stmt[C.NAME];
                    this.state.bindVar(name, this.state.pop());
                    break;
                }
                case C.WAIT_TIME_STMT: {
                    const time = this.state.pop();
                    return [time, true]; // wait for handler being called
                }
                case C.WRITE_PIN_ACTION: {
                    const value = this.state.pop();
                    const mode = stmt[C.MODE];
                    const pin = stmt[C.PIN];
                    this.robotBehaviour.writePinAction(pin, mode, value);
                    return [0, true];
                }
                case C.LIST_OPERATION: {
                    const op = stmt[C.OP];
                    const loc = stmt[C.POSITION];
                    let ix = 0;
                    if (loc != C.LAST && loc != C.FIRST) {
                        ix = this.state.pop();
                    }
                    const value = this.state.pop();
                    let list = this.state.pop();
                    ix = this.getIndex(list, loc, ix);
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
                    const value = this.state.pop();
                    const name = stmt[C.NAME];
                    this.state.bindVar(name, this.state.pop() + value);
                    break;
                }
                case C.SERIAL_WRITE_ACTION: {
                    const value = this.state.pop();
                    this.robotBehaviour.debugAction(value);
                    break;
                }
                case C.DEBUG_ACTION: {
                    const value = this.state.pop();
                    this.robotBehaviour.debugAction(value);
                    break;
                }
                case C.ASSERT_ACTION: {
                    const right = this.state.pop();
                    const left = this.state.pop();
                    const value = this.state.pop();
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
    }

    /**
     *  called from @see evalSingleOperation() to evaluate all kinds of expressions
     *
     * . @param expr to be evaluated
     */
    private evalExpr(expr) {
        const kind = expr[C.EXPR];
        switch (kind) {
            case C.VAR:
                this.state.push(this.state.getVar(expr[C.NAME]));
                break;
            case C.NUM_CONST:
                this.state.push(+expr[C.VALUE]);
                break;
            case C.CREATE_LIST: {
                const n = expr[C.NUMBER];
                var arr = new Array(n);
                for (let i = 0; i < n; i++) {
                    const e = this.state.pop();
                    arr[n - i - 1] = e;
                }
                this.state.push(arr);
                break;
            }
            case C.CREATE_LIST_REPEAT: {
                const rep = this.state.pop();
                const val = this.state.pop();
                var arr = new Array();
                for (let i = 0; i < rep; i++) {
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
                const b = this.state.pop();
                const g = this.state.pop();
                const r = this.state.pop();
                this.state.push([r, g, b]);
                break;
            }
            case C.UNARY: {
                const subOp = expr[C.OP];
                switch (subOp) {
                    case C.NOT:
                        var truthy;
                        const bool = this.state.pop();
                        if (bool === 'true') {
                            truthy = true;
                        } else if (bool === 'false' || bool === '0' || bool === '') {
                            truthy = false;
                        } else {
                            truthy = !!bool;
                        }
                        this.state.push(!truthy);
                        break;
                    case C.NEG:
                        const value = this.state.pop();
                        this.state.push(-value);
                        break;
                    default:
                        U.dbcException('invalid unary expr subOp: ' + subOp);
                }
                break;
            }
            case C.MATH_CONST: {
                const value = expr[C.VALUE];
                switch (value) {
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
            case C.NN_GETWEIGHT: {
                this.state.push(this.neuralNetwork.getWeight(expr[C.FROM], expr[C.TO]));
                break;
            }
            case C.NN_GETBIAS: {
                this.state.push(this.neuralNetwork.getBias(expr[C.NAME]));
                break;
            }
            case C.NN_GETOUTPUTNEURON_VAL: {
                this.state.push(this.neuralNetwork.getNeuronVal(expr[C.NAME]));
                break;
            }
            case C.SINGLE_FUNCTION: {
                const subOp = expr[C.OP];
                const value = this.state.pop();
                U.debug('---------- ' + subOp + ' with ' + value);
                switch (subOp) {
                    case 'SQUARE':
                        this.state.push(Math.pow(value, 2));
                        break;
                    case 'ROOT':
                        this.state.push(Math.sqrt(value));
                        break;
                    case 'ABS':
                        this.state.push(Math.abs(value));
                        break;
                    case 'LN':
                        this.state.push(Math.log(value));
                        break;
                    case 'LOG10':
                        this.state.push(Math.log(value) / Math.LN10);
                        break;
                    case 'EXP':
                        this.state.push(Math.exp(value));
                        break;
                    case 'POW10':
                        this.state.push(Math.pow(10, value));
                        break;
                    case 'SIN':
                        this.state.push(Math.sin(value));
                        break;
                    case 'COS':
                        this.state.push(Math.cos(value));
                        break;
                    case 'TAN':
                        this.state.push(Math.tan(value));
                        break;
                    case 'ASIN':
                        this.state.push(Math.asin(value));
                        break;
                    case 'ATAN':
                        this.state.push(Math.atan(value));
                        break;
                    case 'ACOS':
                        this.state.push(Math.acos(value));
                        break;
                    case 'ROUND':
                        this.state.push(Math.round(value));
                        break;
                    case 'ROUNDUP':
                        this.state.push(Math.ceil(value));
                        break;
                    case 'ROUNDDOWN':
                        this.state.push(Math.floor(value));
                        break;
                    case C.IMAGE_INVERT_ACTION:
                        this.state.push(this.invertImage(value));
                        break;
                    default:
                        throw 'Invalid Function Name';
                }
                break;
            }
            case C.MATH_CONSTRAIN_FUNCTION: {
                const max = this.state.pop();
                const min = this.state.pop();
                const value = this.state.pop();
                this.state.push(Math.min(Math.max(value, min), max));
                break;
            }
            case C.RANDOM_INT: {
                var max = this.state.pop();
                var min = this.state.pop();
                if (min > max) {
                    [min, max] = [max, min];
                }
                this.state.push(Math.floor(Math.random() * (max - min + 1) + min));
                break;
            }
            case C.RANDOM_DOUBLE:
                this.state.push(Math.random());
                break;
            case C.MATH_PROP_FUNCT: {
                const subOp = expr[C.OP];
                const value = this.state.pop();
                switch (subOp) {
                    case 'EVEN':
                        this.state.push(this.isWhole(value) && value % 2 === 0);
                        break;
                    case 'ODD':
                        this.state.push(this.isWhole(value) && value % 2 !== 0);
                        break;
                    case 'PRIME':
                        this.state.push(this.isPrime(value));
                        break;
                    case 'WHOLE':
                        this.state.push(this.isWhole(value));
                        break;
                    case 'POSITIVE':
                        this.state.push(value >= 0);
                        break;
                    case 'NEGATIVE':
                        this.state.push(value < 0);
                        break;
                    case 'DIVISIBLE_BY':
                        const first = this.state.pop();
                        this.state.push(first % value === 0);
                        break;
                    default:
                        throw 'Invalid Math Property Function Name';
                }
                break;
            }
            case C.MATH_ON_LIST: {
                const subOp = expr[C.OP];
                const value = this.state.pop();
                switch (subOp) {
                    case C.SUM:
                        this.state.push(this.sum(value));
                        break;
                    case C.MIN:
                        this.state.push(this.min(value));
                        break;
                    case C.MAX:
                        this.state.push(this.max(value));
                        break;
                    case C.AVERAGE:
                        this.state.push(this.mean(value));
                        break;
                    case C.MEDIAN:
                        this.state.push(this.median(value));
                        break;
                    case C.STD_DEV:
                        this.state.push(this.std(value));
                        break;
                    case C.RANDOM:
                        this.state.push(value[this.getRandomInt(value.length)]);
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
                const subOp = expr[C.OP];
                switch (subOp) {
                    case C.LIST_IS_EMPTY:
                        this.state.push(this.state.pop().length == 0);
                        break;
                    case C.LIST_LENGTH:
                        this.state.push(this.state.pop().length);
                        break;
                    case C.LIST_FIND_ITEM:
                        {
                            const item = this.state.pop();
                            const list = this.state.pop();
                            let index = -1;
                            if (expr[C.POSITION] == C.FIRST) {
                                for (let i: number = 0; i < list.length; i++) {
                                    if (Array.isArray(list[i])) {
                                        index = i;
                                        for (let j: number = 0; j < list[i].length; j++) {
                                            if (list[i][j] != item[j]) {
                                                index = -1;
                                                break;
                                            }
                                        }
                                        if (index != -1) {
                                            break;
                                        }
                                    } else if (list[i] == item) {
                                        index = i;
                                        break;
                                    }
                                }
                            } else {
                                for (let i: number = list.length - 1; i >= 0; i--) {
                                    if (Array.isArray(list[i])) {
                                        index = i;
                                        for (let j: number = 0; j < list[i].length; j++) {
                                            if (list[i][j] != item[j]) {
                                                index = -1;
                                                break;
                                            }
                                        }
                                        if (index != -1) {
                                            break;
                                        }
                                    } else if (list[i] == item) {
                                        index = i;
                                        break;
                                    }
                                }
                            }
                            this.state.push(index);
                        }
                        break;
                    case C.GET:
                    case C.REMOVE:
                    case C.GET_REMOVE:
                        {
                            const loc = expr[C.POSITION];
                            let ix = 0;
                            if (loc != C.LAST && loc != C.FIRST) {
                                ix = this.state.pop();
                            }
                            let list = this.state.pop();
                            ix = this.getIndex(list, loc, ix);
                            let v = list[ix];
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
                            const position = expr[C.POSITION];
                            let start_ix;
                            let end_ix;
                            if (position[1] != C.LAST) {
                                end_ix = this.state.pop();
                            }
                            if (position[0] != C.FIRST) {
                                start_ix = this.state.pop();
                            }
                            let list = this.state.pop();
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
                const subOp = expr[C.OP];
                const right = this.state.pop();
                const left = this.state.pop();
                const dataType = expr[C.TYPE]; // used by robots with integer arithmetic only
                this.state.push(this.evalBinary(subOp, left, right, dataType));
                break;
            }
            case C.NULL_CONST: {
                this.state.push(null);
                break;
            }
            default:
                U.dbcException('invalid expr op: ' + kind);
        }
    }

    private evalBinary(subOp: string, left: any, right: any, dataType: string): any {
        let leftIsArray = Array.isArray(left);
        let rightIsArray = Array.isArray(right);

        if (leftIsArray && rightIsArray) {
            let leftLen = left.length;
            let rightLen = right.length;
            switch (subOp) {
                case C.EQ:
                    if (leftLen === rightLen) {
                        for (var i = 0; i < leftLen; i++) {
                            if (!this.evalBinary(subOp, left[i], right[i], dataType)) {
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
                            if (this.evalBinary(subOp, left[i], right[i], dataType)) {
                                return true;
                            }
                        }
                        return false;
                    } else {
                        return true;
                    }
                default:
                    U.dbcException('invalid binary expr supOp for array-like structures: ' + subOp);
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
                    if (dataType && dataType === 'INTEGER') {
                        return Math.floor(0 + left / right);
                    } else {
                        return 0 + left / right;
                    }
                case C.POWER:
                    return Math.pow(left, right);
                case C.MOD:
                    return left % right;
                default:
                    U.dbcException('invalid binary expr supOp: ' + subOp);
            }
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
        return Number(value) === value && value % 1 === 0;
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
        return Number(median.toFixed(2));
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

    private invertImage(imageToInvert: any): any {
        let image: number[][] = UTIL.clone(imageToInvert);
        for (var i = 0; i < image.length; i++) {
            for (var j = 0; j < image[i].length; j++) {
                image[i][j] = Math.abs(255 - image[i][j]);
            }
        }
        return image;
    }

    private shiftImageAction(imageToShift: number[][], direction: string, nShift: number): number[][] {
        let image: number[][] = UTIL.clone(imageToShift);
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
                image.forEach(function (array: number[]) {
                    array.pop();
                    array.unshift(0);
                });
            },
            left: function () {
                image.forEach(function (array: number[]) {
                    array.shift();
                    array.push(0);
                });
            },
        };
        if (nShift < 0) {
            nShift *= -1;
            if (direction === 'up') {
                direction = 'down';
            } else if (direction === 'down') {
                direction = 'up';
            } else if (direction === 'left') {
                direction = 'right';
            } else if (direction === 'right') {
                direction = 'left';
            }
        }
        for (var i = 0; i < nShift; i++) {
            shift[direction]();
        }
        return image;
    }

    private shiftImageActionMbot(image: number[][], direction: string, nShift: number): number[][] {
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
                image.forEach(function (array: number[]) {
                    array.pop();
                    array.unshift(0);
                });
            },
            down: function () {
                image.forEach(function (array: number[]) {
                    array.shift();
                    array.push(0);
                });
            },
        };
        if (nShift < 0) {
            nShift *= -1;
            if (direction === 'up') {
                direction = 'down';
            } else if (direction === 'down') {
                direction = 'up';
            } else if (direction === 'left') {
                direction = 'right';
            } else if (direction === 'right') {
                direction = 'left';
            }
        }
        for (var i = 0; i < nShift; i++) {
            shift[direction]();
        }
        return image;
    }

    private static isPossibleStepInto(op): boolean {
        if (op[C.POSSIBLE_DEBUG_STOP]?.length > 0) {
            return true;
        }
        return false;
    }

    private static isPossibleStepOver(op): boolean {
        let isMethodCall = op[C.OPCODE] === C.COMMENT && op[C.TARGET] === C.METHOD_CALL;
        return op.hasOwnProperty(C.HIGHTLIGHT_PLUS) && isMethodCall;
    }

    private static isBreakPoint(op: any, breakpoints: any[]): boolean {
        if (op[C.POSSIBLE_DEBUG_STOP]?.some((blockId) => breakpoints.indexOf(blockId) >= 0)) {
            return true;
        }
        if (op[C.HIGHTLIGHT_PLUS]?.some((blockId) => breakpoints.indexOf(blockId) >= 0)) {
            return true;
        }
        return false;
    }
}
