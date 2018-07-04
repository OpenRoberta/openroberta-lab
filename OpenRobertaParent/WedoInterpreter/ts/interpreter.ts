import * as C from "./constants";
import * as N from "./native";
import * as S from "./state";
import * as U from "./util";

var terminated = false;
var callbackOnTermination = undefined;

export function run( generatedCode: any, cbOnTermination: () => void ) {
    terminated = false;
    callbackOnTermination = cbOnTermination;
    S.reset();
    const stmts = generatedCode[C.OPS];
    const functions = generatedCode[C.FUNCTION_DECLARATION];
    var stop = {};
    stop[C.OPCODE] = "stop";
    stmts.push( stop );
    S.storeCode( stmts, functions );
    setTimeout(() => { evalOperation() }, 0 ); // return to caller. Don't block the UI.
}

export function isTerminated() {
    return terminated;
}

export function terminate() {
    terminated = true;
}

export function evalOperation() {
    topLevelLoop: while ( !terminated ) {
        S.opLog( 'actual ops: ' );
        let stmt = S.getOp();
        if ( stmt === undefined ) {
            U.p( "PROGRAM TERMINATED. No ops remaining" );
            terminated = true;
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
                const doIt = conditional ? S.pop() : true;
                if ( doIt ) {
                    S.popOpsUntil( stmt[C.KIND] );
                    if ( stmt[C.BREAK] ) {
                        S.getOp();
                    }
                }
                break;
            }
            case C.GET_SAMPLE: {
                N.getSample( stmt[C.NAME], stmt[C.PORT], stmt[C.GET_SAMPLE] )
                break;
            }
            case C.IF_STMT:
                S.pushOps( true, stmt[C.STMT_LIST] )
                break;
            case C.IF_TRUE_STMT:
                if ( S.pop() ) {
                    S.pushOps( false, stmt[C.STMT_LIST] )
                }
                break;
            case C.IF_RETURN:
                if ( S.pop() ) {
                    S.pushOps( false, stmt[C.STMT_LIST] )
                }
                break;
            case C.LED_ON_ACTION: {
                const color = S.pop();
                N.ledOnAction( stmt[C.NAME], stmt[C.PORT], color )
                break;
            }
            case C.METHOD_CALL_VOID:
            case C.METHOD_CALL_RETURN: {
                if ( stmt[C.RETURN] === undefined ) {
                    stmt[C.RETURN] = true;
                    for ( let parameterName of stmt[C.NAMES] ) {
                        S.bindVar( parameterName, S.pop() )
                    }
                    const body = S.getFunction( stmt[C.NAME] )[C.STATEMENTS];
                    S.pushOps( true, body );
                } else {
                    S.clearDangerousProperties( stmt );
                }
                break;
            }
            case C.MOTOR_ON_ACTION: {
                const duration = S.pop();
                const speed = S.pop();
                N.motorOnAction( stmt[C.NAME], stmt[C.PORT], duration, speed );
                break;
            }
            case C.MOTOR_STOP: {
                N.motorStopAction( stmt[C.NAME], stmt[C.PORT] );
                break;
            }
            case C.REPEAT_STMT:
                evalRepeat( stmt );
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
                terminated = true;
                break topLevelLoop;
            case C.TEXT_JOIN:
                const second = S.pop();
                const first = S.pop();
                S.push( '' + first + second );
                break;
            case C.TIMER_SENSOR_RESET:
                N.timerReset();
                break;
            case C.VAR_DECLARATION: {
                const name = stmt[C.NAME];
                S.bindVar( name, S.pop() );
                break;
            }
            case C.TONE_ACTION: {
                const duration = S.pop();
                const frequency = S.pop();
                U.p( "tone, duration: " + duration + ", frequency: " + frequency );
                break;
            }
            case C.WAIT_STMT: {
                U.p( 'waitstmt started' );
                S.pushOps( true, stmt[C.STMT_LIST] );
                break;
            }
            case C.WAIT_TIME_STMT: {
                const time = S.pop();
                U.p( 'waiting ' + time + ' msec' );
                setTimeout(() => { evalOperation() }, time );
                return; // wait for handler being called
            }
            default:
                U.dbcException( "invalid stmt op: " + opCode );
        }
    }
    // termination either requested by the client or by executing 'stop' or after last statement
    callbackOnTermination();
}

function evalExpr( expr ) {
    const kind = expr[C.EXPR];
    switch ( kind ) {
        case C.VAR:
            S.push( S.getVar( expr[C.NAME] ) );
            break;
        case C.NUM_CONST:
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
                case C.EQ: S.push( left === right ); break;
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

function evalRepeat( stmt: any ) {
    const mode = stmt[C.MODE];
    if ( mode === C.TIMES ) {
        if ( stmt[C.VALUE] === undefined ) {
            stmt[C.VALUE] = 0;
            stmt[C.END] = S.pop();
        }
        const value = stmt[C.VALUE] + 1;
        const end = [C.END];
        stmt[C.VALUE] = value;
        if ( value < end ) {
            S.pushOps( true, stmt[C.STMT_LIST] );
        } else {
            S.clearDangerousProperties( stmt );
        }
    } else if ( mode === C.UNTIL ) {
        if ( !S.pop() ) {
            S.pushOps( true, stmt[C.STMT_LIST] );
        }
    } else if ( mode === C.FOR ) {
        const variable = stmt[C.VAR];
        var actual = S.getVar( variable );
        var step;
        var end;
        if ( stmt[C.STEP] === undefined ) {
            step = S.pop();
            end = S.pop();
            stmt[C.STEP] = step;
            stmt[C.END] = end;
        } else {
            step = stmt[C.STEP];
            end = stmt[C.END];
            actual += step;
            S.setVar( variable, actual );
        }
        // U.p( 'actual:' + actual + ' step:' + step + ' end:' + end );
        if ( actual <= end ) {
            S.pushOps( true, stmt[C.STMT_LIST] );
        } else {
            S.unbindVar( variable );
            stmt[C.STEP] = undefined;
            stmt[C.END] = undefined;
        }
    } else {
        U.dbcException( "invalid repeat mode: " + mode );
    }
}

function isPrime( n: number ) {
    if ( n < 2 ) { return false; }
    if ( n === 2 ) { return true; }
    if ( n % 2 === 0 ) { return false; }
    for ( let i = 3, s = Math.sqrt( n ); i <= s; i += 2 ) {
        if ( n % i === 0 ) { return false; }
    }
    return true;
};