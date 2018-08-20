import { Native } from "interpreter.nativeInterface";
import { State } from "interpreter.state";
import * as C from "interpreter.constants";
import * as U from "interpreter.util";

export class Interpreter {

    private terminated = false;
    private callbackOnTermination = undefined;

    private n: Native; // implementation of the NativeInterface to connect a real WeDo robot (or a test instance) to the interpreter
    private s: State; // the state of the interpreter (ops, pc, bindings, stack, ...)

    constructor() {
    }

    /**
     * run the operations.
     * 
     * . @param generatedCode argument contains the operations and the function definitions
     * . @param native implementation of the native interface Native to connect a real WeDo robot (or a test instance) to the interpreter
     * . @param cbOnTermination is called when the program has terminated
     */
    public run( generatedCode: any, native: Native, cbOnTermination: () => void ) {
        this.terminated = false;
        this.callbackOnTermination = cbOnTermination;
        const stmts = generatedCode[C.OPS];
        const functions = generatedCode[C.FUNCTION_DECLARATION];
        this.n = native;

        var stop = {};
        stop[C.OPCODE] = "stop";
        stmts.push( stop );
        this.s = new State( stmts, functions );
        this.timeout(() => { this.evalOperation() }, 0 ); // return to caller. Don't block the UI.
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
    }

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
    private evalOperation() {
        const s = this.s;
        const n = this.n;
        topLevelLoop: while ( !this.terminated ) {
            s.opLog( 'actual ops: ' );
            let stmt = s.getOp();
            if ( stmt === undefined ) {
                U.debug( 'PROGRAM TERMINATED. No ops remaining' );
                break topLevelLoop;
            }
            const opCode = stmt[C.OPCODE];
            switch ( opCode ) {
                case C.ASSIGN_STMT: {
                    const name = stmt[C.NAME];
                    s.setVar( name, s.pop() );
                    break;
                }
                case C.CLEAR_DISPLAY_ACTION: {
                    n.clearDisplay();
                    break;
                }
                case C.CREATE_DEBUG_ACTION: {
                    U.debug( 'NYI' );
                    break;
                }
                case C.DRIVE_ACTION: {
                    U.debug( 'NYI' );
                    break;
                }
                case C.EXPR:
                    this.evalExpr( stmt );
                    break;
                case C.FLOW_CONTROL: {
                    const conditional = stmt[C.CONDITIONAL];
                    const activatedBy: boolean = stmt[C.BOOLEAN] === undefined ? true : stmt[C.BOOLEAN];
                    const doIt: boolean = conditional ? ( s.pop() === activatedBy ) : true;
                    if ( doIt ) {
                        s.popOpsUntil( stmt[C.KIND] );
                        if ( stmt[C.BREAK] ) {
                            s.getOp();
                        }
                    }
                    break;
                }
                case C.GET_SAMPLE: {
                    n.getSample( s, stmt[C.NAME], stmt[C.PORT], stmt[C.GET_SAMPLE], stmt[C.SLOT] )
                    break;
                }
                case C.IF_STMT:
                    s.pushOps( stmt[C.STMT_LIST] )
                    break;
                case C.IF_TRUE_STMT:
                    if ( s.pop() ) {
                        s.pushOps( stmt[C.STMT_LIST] )
                    }
                    break;
                case C.IF_RETURN:
                    if ( s.pop() ) {
                        s.pushOps( stmt[C.STMT_LIST] )
                    }
                    break;
                case C.LED_ON_ACTION: {
                    const color = s.pop();
                    n.ledOnAction( stmt[C.NAME], stmt[C.PORT], color )
                    break;
                }
                case C.METHOD_CALL_VOID:
                case C.METHOD_CALL_RETURN: {
                    for ( let parameterName of stmt[C.NAMES] ) {
                        s.bindVar( parameterName, s.pop() )
                    }
                    const body = s.getFunction( stmt[C.NAME] )[C.STATEMENTS];
                    s.pushOps( body );
                    break;
                }
                case C.MOTOR_ON_ACTION: {
                    const duration = s.pop();
                    const speed = s.pop();
                    const name = stmt[C.NAME];
                    const port = stmt[C.PORT];
                    n.motorOnAction( name, port, duration, speed );
                    if ( duration >= 0 ) {
                        this.timeout(() => { n.motorStopAction( name, port ); this.evalOperation() }, duration );
                        return; // wait for handler being called
                    }
                    break;
                }
                case C.MOTOR_STOP: {
                    n.motorStopAction( stmt[C.NAME], stmt[C.PORT] );
                    break;
                }
                case C.REPEAT_STMT:
                    this.evalRepeat( stmt );
                    break;
                case C.REPEAT_STMT_CONTINUATION:
                    if ( stmt[C.MODE] === C.FOR || stmt[C.MODE] === C.TIMES ) {
                        const runVariableName = stmt[C.NAME];
                        const end = s.get1();
                        const incr = s.get0();
                        const value = s.getVar( runVariableName ) + incr;
                        if ( +value >= +end ) {
                            s.popOpsUntil( C.REPEAT_STMT );
                            s.getOp(); // the repeat has terminated
                        } else {
                            s.setVar( runVariableName, value );
                            s.pushOps( stmt[C.STMT_LIST] );
                        }
                    }
                    break;
                case C.SHOW_TEXT_ACTION: {
                    n.showTextAction( s.pop() );
                    break;
                }
                case C.STATUS_LIGHT_ACTION:
                    n.statusLightOffAction( stmt[C.NAME], stmt[C.PORT] )
                    break;
                case C.STOP:
                    U.debug( "PROGRAM TERMINATED. stop op" );
                    break topLevelLoop;
                case C.TEXT_JOIN:
                    const second = s.pop();
                    const first = s.pop();
                    s.push( '' + first + second );
                    break;
                case C.TIMER_SENSOR_RESET:
                    const port = stmt[C.PORT];
                    n.timerReset( port );
                    break;
                case C.TONE_ACTION: {
                    const duration = s.pop();
                    const frequency = s.pop();
                    n.toneAction( stmt[C.NAME], frequency, duration );
                    this.timeout(() => { this.evalOperation() }, duration );
                    return; // wait for handler being called
                }
                case C.VAR_DECLARATION: {
                    const name = stmt[C.NAME];
                    s.bindVar( name, s.pop() );
                    break;
                }
                case C.WAIT_STMT: {
                    U.debug( 'waitstmt started' );
                    s.pushOps( stmt[C.STMT_LIST] );
                    break;
                }
                case C.WAIT_TIME_STMT: {
                    const time = s.pop();
                    this.timeout(() => { this.evalOperation() }, time );
                    return; // wait for handler being called
                }
                default:
                    U.dbcException( "invalid stmt op: " + opCode );
            }
        }
        // termination either requested by the client or by executing 'stop' or after last statement
        this.terminated = true;
        n.close();
        this.callbackOnTermination();
    }

    /**
     *  called from @see evalOperation() to evaluate all kinds of expressions
     *  
     * . @param expr to be evaluated
     */
    private evalExpr( expr ) {
        const kind = expr[C.EXPR];
        const s = this.s;
        switch ( kind ) {
            case C.VAR:
                s.push( s.getVar( expr[C.NAME] ) );
                break;
            case C.NUM_CONST:
                s.push( +expr[C.VALUE] );
                break;
            case C.BOOL_CONST:
                s.push( expr[C.VALUE] );
                break;
            case C.STRING_CONST:
                s.push( expr[C.VALUE] );
                break;
            case C.COLOR_CONST:
                s.push( expr[C.VALUE] );
                break;
            case C.UNARY: {
                const subOp = expr[C.OP];
                switch ( subOp ) {
                    case C.NOT:
                        var truthy;
                        const bool = s.pop();
                        if ( bool === 'true' ) {
                            truthy = true;
                        } else if ( bool === 'false' || bool === '0' || bool === '' ) {
                            truthy = false;
                        } else {
                            truthy = !!bool
                        }
                        s.push( !truthy );
                        break;
                    default:
                        U.dbcException( "invalid unary expr subOp: " + subOp );
                }
                break;
            }
            case C.MATH_CONST: {
                const value = expr[C.VALUE];
                switch ( value ) {
                    case 'PI': s.push( Math.PI ); break;
                    case 'E': s.push( Math.E ); break;
                    case 'GOLDEN_RATIO': s.push(( 1.0 + Math.sqrt( 5.0 ) ) / 2.0 ); break;
                    case 'SQRT2': s.push( Math.SQRT2 ); break;
                    case 'SQRT1_2': s.push( Math.SQRT1_2 ); break;
                    case 'INFINITY': s.push( Infinity ); break;
                    default:
                        throw "Invalid Math Constant Name";
                }
            }
            case C.SINGLE_FUNCTION: {
                const subOp = expr[C.OP];
                const value = s.pop();
                switch ( subOp ) {
                    case 'ROOT': s.push( Math.sqrt( value ) ); break;
                    case 'ABS': s.push( Math.abs( value ) ); break;
                    case 'LN': s.push( Math.log( value ) ); break;
                    case 'LOG10': s.push( Math.log( value ) / Math.LN10 ); break;
                    case 'EXP': s.push( Math.exp( value ) ); break;
                    case 'POW10': s.push( Math.pow( 10, value ) ); break;
                    case 'SIN': s.push( Math.sin( value ) ); break;
                    case 'COS': s.push( Math.cos( value ) ); break;
                    case 'TAN': s.push( Math.tan( value ) ); break;
                    case 'ASIN': s.push( Math.asin( value ) ); break;
                    case 'ATAN': s.push( Math.atan( value ) ); break;
                    case 'ACOS': s.push( Math.acos( value ) ); break;
                    case 'ROUND': s.push( Math.round( value ) ); break;
                    case 'ROUNDUP': s.push( Math.ceil( value ) ); break;
                    case 'ROUNDDOWN': s.push( Math.floor( value ) ); break;
                    default:
                        throw "Invalid Function Name";
                }
            }
            case C.MATH_CONSTRAIN_FUNCTION: {
                const max = s.pop();
                const min = s.pop();
                const value = s.pop();
                s.push( Math.min( Math.max( value, min ), max ) );
                break;
            }
            case C.RANDOM_INT: {
                var max = s.pop();
                var min = s.pop();
                if ( min > max ) {
                    [min, max] = [max, min];
                }
                s.push( Math.floor( Math.random() * ( max - min + 1 ) + min ) );
                break;
            }
            case C.RANDOM_DOUBLE:
                s.push( Math.random() );
                break;
            case C.MATH_PROP_FUNCT: {
                const subOp = expr[C.OP];
                const value = s.pop();
                switch ( subOp ) {
                    case 'EVEN': s.push( value % 2 === 0 ); break;
                    case 'ODD': s.push( value % 2 !== 0 ); break;
                    case 'PRIME': s.push( this.isPrime( value ) ); break;
                    case 'WHOLE': s.push( Number( value ) === value && value % 1 === 0 ); break;
                    case 'POSITIVE': s.push( value >= 0 ); break;
                    case 'NEGATIVE': s.push( value < 0 ); break;
                    case 'DIVISIBLE_BY':
                        const first = s.pop();
                        s.push( first % value === 0 ); break;
                    default:
                        throw "Invalid Math Property Function Name";
                }
            }
            case C.BINARY: {
                const subOp = expr[C.OP];
                const right = s.pop();
                const left = s.pop();
                switch ( subOp ) {
                    case C.EQ: s.push( left == right ); break;
                    case C.NEQ: s.push( left !== right ); break;
                    case C.LT: s.push( left < right ); break;
                    case C.LTE: s.push( left <= right ); break;
                    case C.GT: s.push( left > right ); break;
                    case C.GTE: s.push( left >= right ); break;
                    case C.AND: s.push( left && right ); break;
                    case C.OR: s.push( left || right ); break;
                    case C.ADD: s.push( 0 + left + right ); break;
                    case C.MINUS: s.push( 0 + left - right ); break;
                    case C.MULTIPLY: s.push( 0 + left * right ); break;
                    case C.DIVIDE: s.push( 0 + left / right ); break;
                    case C.POWER: s.push( Math.pow( left, right ) ); break;
                    default:
                        U.dbcException( "invalid binary expr supOp: " + subOp );
                }
                break;
            }
            default:
                U.dbcException( "invalid expr op: " + kind );
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
    private evalRepeat( stmt: any ) {
        const s = this.s;
        const mode = stmt[C.MODE];
        const contl: any[] = stmt[C.STMT_LIST];
        if ( contl.length !== 1 || contl[0][C.OPCODE] !== C.REPEAT_STMT_CONTINUATION ) {
            U.dbcException( "repeat expects an embedded continuation statement" );
        }
        const cont = contl[0];
        switch ( mode ) {
            case C.FOREVER:
            case C.UNTIL:
            case C.WHILE:
                s.pushOps( contl ); s.getOp(); // pseudo execution. Init is already done. Continuation is for termination only.
                s.pushOps( cont[C.STMT_LIST] );
                break;
            case C.TIMES:
            case C.FOR: {
                const runVariableName = stmt[C.NAME];
                const start = s.get2();
                const end = s.get1();
                if ( +start >= +end ) {
                    s.pop(); s.pop(); s.pop();
                } else {
                    s.bindVar( runVariableName, start );
                    s.pushOps( contl ); s.getOp(); // pseudo excution. Init is already done. Continuation is for termination only.
                    s.pushOps( cont[C.STMT_LIST] );
                    break;
                }
                break;
            }
            default:
                U.dbcException( "invalid repeat mode: " + mode );
        }
    }

    /**
     * return true if the parameter is prime
     * 
     * . @param n to be checked for primality
     */
    private isPrime( n: number ) {
        if ( n < 2 ) { return false; }
        if ( n === 2 ) { return true; }
        if ( n % 2 === 0 ) { return false; }
        for ( let i = 3, s = Math.sqrt( n ); i <= s; i += 2 ) {
            if ( n % i === 0 ) { return false; }
        }
        return true;
    }

    /**
     * after the duration specified, call the callback function given. The duration is partitioned into 100 millisec intervals to allow termination of the running interpreter during 
     * a timeout. Be careful: the termination is NOT effected here, but by the callback function (this should be @see evalOperation() in ALMOST ALL cases)
     * 
     * . @param callback called when the time has elapsed
     * 
     * . @param durationInMilliSec time that should elapse before the callback is called
     */
    private timeout( callback: () => void, durationInMilliSec: number ) {
        if ( this.terminated ) {
            callback();
        } else
            if ( durationInMilliSec > 100 ) {
                // U.p( 'waiting for 100 msec from ' + durationInMilliSec + ' msec' );
                durationInMilliSec -= 100;
                setTimeout(() => { this.timeout( callback, durationInMilliSec ) }, 100 );
            } else {
                // U.p( 'waiting for ' + durationInMilliSec + ' msec' );
                setTimeout(() => { callback() }, durationInMilliSec );
            }
    }
}