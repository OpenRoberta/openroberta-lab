import * as C from "./constants";
import * as N from "./native";
import * as S from "./state";
import * as U from "./util";

var terminated = false;
var callbackOnTermination = undefined;

/**
 * run the operations.
 * 
 * . @param generatedCode argument contains the operations and the function definitions
 * . @param cbOnTermination is called when the program has terminated
 */
export function run( generatedCode: any, cbOnTermination: () => void ) {
    terminated = false;
    callbackOnTermination = cbOnTermination;
    const stmts = generatedCode[C.OPS];
    const functions = generatedCode[C.FUNCTION_DECLARATION];
    var stop = {};
    stop[C.OPCODE] = "stop";
    stmts.push( stop );
    S.storeCode( stmts, functions );
    timeout(() => { evalOperation() }, 0 ); // return to caller. Don't block the UI.
}

/**
 * return true, if the program is terminated
 */
export function isTerminated() {
    return terminated;
}

/**
 * force the termination of the program. The termination takes place, when the NEXT operation should be executed. The delay is not significant.
 * The callbackOnTermination function is called
 */
export function terminate() {
    terminated = true;
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
export function evalOperation() {
    topLevelLoop: while ( !terminated ) {
        S.opLog( 'actual ops: ' );
        let stmt = S.getOp();
        if ( stmt === undefined ) {
            U.p( 'PROGRAM TERMINATED. No ops remaining' );
            break topLevelLoop;
        }
        const opCode = stmt[C.OPCODE];
        switch ( opCode ) {
            case C.ASSIGN_STMT: {
                const name = stmt[C.NAME];
                S.setVar( name, S.pop() );
                break;
            }
            case C.CLEAR_DISPLAY_ACTION: {
                N.clearDisplay();
                break;
            }
            case C.CREATE_DEBUG_ACTION: {
                U.p( 'NYI' );
                break;
            }
            case C.DRIVE_ACTION: {
                const distance = S.pop();
                const speed = S.pop();
                const driveDirection = stmt[C.DRIVE_DIRECTION];
                N.driveAction( driveDirection, distance, speed );
                break;
            }
            case C.EXPR:
                evalExpr( stmt );
                break;
            case C.FLOW_CONTROL: {
                const conditional = stmt[C.CONDITIONAL];
                const activatedBy: boolean = stmt[C.BOOLEAN] === undefined ? true : stmt[C.BOOLEAN];
                const doIt: boolean = conditional ? ( S.pop() === activatedBy ) : true;
                if ( doIt ) {
                    S.popOpsUntil( stmt[C.KIND] );
                    if ( stmt[C.BREAK] ) {
                        S.getOp();
                    }
                }
                break;
            }
            case C.GET_SAMPLE: {
                N.getSample( stmt[C.NAME], stmt[C.PORT], stmt[C.GET_SAMPLE], stmt[C.SLOT] )
                break;
            }
            case C.IF_STMT:
                S.pushOps( stmt[C.STMT_LIST] )
                break;
            case C.IF_TRUE_STMT:
                if ( S.pop() ) {
                    S.pushOps( stmt[C.STMT_LIST] )
                }
                break;
            case C.IF_RETURN:
                if ( S.pop() ) {
                    S.pushOps( stmt[C.STMT_LIST] )
                }
                break;
            case C.LED_ON_ACTION: {
                const color = S.pop();
                N.ledOnAction( stmt[C.NAME], stmt[C.PORT], color )
                break;
            }
            case C.METHOD_CALL_VOID:
            case C.METHOD_CALL_RETURN: {
                for ( let parameterName of stmt[C.NAMES] ) {
                    S.bindVar( parameterName, S.pop() )
                }
                const body = S.getFunction( stmt[C.NAME] )[C.STATEMENTS];
                S.pushOps( body );
                break;
            }
            case C.MOTOR_ON_ACTION: {
                const duration = S.pop();
                const speed = S.pop();
                const name = stmt[C.NAME];
                const port = stmt[C.PORT];
                N.motorOnAction( name, port, duration, speed );
                if ( duration >= 0 ) {
                    timeout(() => { N.motorStopAction( name, port ); evalOperation() }, duration );
                    return; // wait for handler being called
                }
                break;
            }
            case C.MOTOR_STOP: {
                N.motorStopAction( stmt[C.NAME], stmt[C.PORT] );
                break;
            }
            case C.REPEAT_STMT:
                evalRepeat( stmt );
                break;
            case C.REPEAT_STMT_CONTINUATION:
                if ( stmt[C.MODE] === C.FOR || stmt[C.MODE] === C.TIMES ) {
                    const runVariableName = stmt[C.NAME];
                    const end = S.get1();
                    const incr = S.get0();
                    const value = S.getVar( runVariableName ) + incr;
                    if ( +value >= +end ) {
                        S.popOpsUntil( C.REPEAT_STMT );
                        S.getOp(); // the repeat has terminated
                    } else {
                        S.setVar( runVariableName, value );
                        S.pushOps( stmt[C.STMT_LIST] );
                    }
                }
                break;
            case C.SHOW_TEXT_ACTION: {
                N.showTextAction( S.pop() );
                break;
            }
            case C.STATUS_LIGHT_ACTION:
                N.statusLightOffAction( '-', '-' )
                return;
            case C.STOP:
                U.p( "PROGRAM TERMINATED. stop op" );
                break topLevelLoop;
            case C.TEXT_JOIN:
                const second = S.pop();
                const first = S.pop();
                S.push( '' + first + second );
                break;
            case C.TIMER_SENSOR_RESET:
                const port = stmt[C.PORT];
                N.timerReset( port );
                break;
            case C.TONE_ACTION: {
                const duration = S.pop();
                const frequency = S.pop();
                N.toneAction( stmt[C.NAME], frequency, duration );
                timeout(() => { evalOperation() }, duration );
                return; // wait for handler being called
            }
            case C.VAR_DECLARATION: {
                const name = stmt[C.NAME];
                S.bindVar( name, S.pop() );
                break;
            }
            case C.WAIT_STMT: {
                U.p( 'waitstmt started' );
                S.pushOps( stmt[C.STMT_LIST] );
                break;
            }
            case C.WAIT_TIME_STMT: {
                const time = S.pop();
                timeout(() => { evalOperation() }, time );
                return; // wait for handler being called
            }
            default:
                U.dbcException( "invalid stmt op: " + opCode );
        }
    }
    // termination either requested by the client or by executing 'stop' or after last statement
    terminated = true;
    N.close();
    callbackOnTermination();
}

/**
 *  called from @see evalOperation() to evaluate all kinds of expressions
 *  
 * . @param expr to be evaluated
 */
function evalExpr( expr ) {
    const kind = expr[C.EXPR];
    switch ( kind ) {
        case C.VAR:
            S.push( S.getVar( expr[C.NAME] ) );
            break;
        case C.NUM_CONST:
            S.push( +expr[C.VALUE] );
            break;
        case C.BOOL_CONST:
            S.push( +expr[C.VALUE] );
            break;
        case C.STRING_CONST:
            S.push( expr[C.VALUE] );
            break;
        case C.COLOR_CONST:
            S.push( expr[C.VALUE] );
            break;
        case C.UNARY: {
            const subOp = expr[C.OP];
            switch ( subOp ) {
                case C.NOT:
                    const bool = S.pop();
                    S.push( !bool );
                    break;
                default:
                    U.dbcException( "invalid unary expr subOp: " + subOp );
            }
            break;
        }
        case C.MATH_CONST: {
            const value = expr[C.VALUE];
            switch ( value ) {
                case 'PI': S.push( Math.PI ); break;
                case 'E': S.push( Math.E ); break;
                case 'GOLDEN_RATIO': S.push(( 1.0 + Math.sqrt( 5.0 ) ) / 2.0 ); break;
                case 'SQRT2': S.push( Math.SQRT2 ); break;
                case 'SQRT1_2': S.push( Math.SQRT1_2 ); break;
                case 'INFINITY': S.push( Infinity ); break;
                default:
                    throw "Invalid Math Constant Name";
            }
        }
        case C.SINGLE_FUNCTION: {
            const subOp = expr[C.OP];
            const value = S.pop();
            switch ( subOp ) {
                case 'ROOT': S.push( Math.sqrt( value ) ); break;
                case 'ABS': S.push( Math.abs( value ) ); break;
                case 'LN': S.push( Math.log( value ) ); break;
                case 'LOG10': S.push( Math.log( value ) / Math.LN10 ); break;
                case 'EXP': S.push( Math.exp( value ) ); break;
                case 'POW10': S.push( Math.pow( 10, value ) ); break;
                case 'SIN': S.push( Math.sin( value ) ); break;
                case 'COS': S.push( Math.cos( value ) ); break;
                case 'TAN': S.push( Math.tan( value ) ); break;
                case 'ASIN': S.push( Math.asin( value ) ); break;
                case 'ATAN': S.push( Math.atan( value ) ); break;
                case 'ACOS': S.push( Math.acos( value ) ); break;
                case 'ROUND': S.push( Math.round( value ) ); break;
                case 'ROUNDUP': S.push( Math.ceil( value ) ); break;
                case 'ROUNDDOWN': S.push( Math.floor( value ) ); break;
                default:
                    throw "Invalid Function Name";
            }
        }
        case C.MATH_CONSTRAIN_FUNCTION: {
            const max = S.pop();
            const min = S.pop();
            const value = S.pop();
            S.push( Math.min( Math.max( value, min ), max ) );
            break;
        }
        case C.RANDOM_INT: {
            var max = S.pop();
            var min = S.pop();
            if ( min > max ) {
                [min, max] = [max, min];
            }
            S.push( Math.floor( Math.random() * ( max - min + 1 ) + min ) );
            break;
        }
        case C.RANDOM_DOUBLE:
            S.push( Math.random() );
            break;
        case C.MATH_PROP_FUNCT: {
            const subOp = expr[C.OP];
            const value = S.pop();
            switch ( subOp ) {
                case 'EVEN': S.push( value % 2 === 0 ); break;
                case 'ODD': S.push( value % 2 !== 0 ); break;
                case 'PRIME': S.push( isPrime( value ) ); break;
                case 'WHOLE': S.push( Number( value ) === value && value % 1 === 0 ); break;
                case 'POSITIVE': S.push( value >= 0 ); break;
                case 'NEGATIVE': S.push( value < 0 ); break;
                case 'DIVISIBLE_BY':
                    const first = S.pop();
                    S.push( first % value === 0 ); break;
                default:
                    throw "Invalid Math Property Function Name";
            }
        }
        case C.BINARY: {
            const subOp = expr[C.OP];
            const right = S.pop();
            const left = S.pop();
            switch ( subOp ) {
                case C.EQ: S.push( left == right );
                case C.NEQ: S.push( left !== right ); break;
                case C.LT: S.push( left < right ); break;
                case C.LTE: S.push( left <= right ); break;
                case C.GT: S.push( left > right ); break;
                case C.GTE: S.push( left >= right ); break;
                case C.AND: S.push( left && right ); break;
                case C.OR: S.push( left || right ); break;
                case C.ADD: S.push( 0 + left + right ); break;
                case C.MINUS: S.push( 0 + left - right ); break;
                case C.MULTIPLY: S.push( 0 + left * right ); break;
                case C.DIVIDE: S.push( 0 + left / right ); break;
                case C.POWER: S.push( Math.pow( left, right ) ); break;
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
function evalRepeat( stmt: any ) {
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
            S.pushOps( contl ); S.getOp(); // pseudo execution. Init is already done. Continuation is for termination only.
            S.pushOps( cont[C.STMT_LIST] );
            break;
        case C.TIMES:
        case C.FOR: {
            const runVariableName = stmt[C.NAME];
            const start = S.get2();
            const end = S.get1();
            if ( +start >= +end ) {
                S.pop(); S.pop(); S.pop();
            } else {
                S.bindVar( runVariableName, start );
                S.pushOps( contl ); S.getOp(); // pseudo excution. Init is already done. Continuation is for termination only.
                S.pushOps( cont[C.STMT_LIST] );
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
function isPrime( n: number ) {
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
function timeout( callback: () => void, durationInMilliSec: number ) {
    if ( terminated ) {
        callback();
    } else
        if ( durationInMilliSec > 100 ) {
            // U.p( 'waiting for 100 msec from ' + durationInMilliSec + ' msec' );
            durationInMilliSec -= 100;
            setTimeout(() => { timeout( callback, durationInMilliSec ) }, 100 );
        } else {
            // U.p( 'waiting for ' + durationInMilliSec + ' msec' );
            setTimeout(() => { callback() }, durationInMilliSec );
        }
}