import * as C from './constants';
import * as U from './util';

/**
 * initialization of the state.
 * Gets the array of operations and the function definitions and resets the whole state
 * 
 * . @param ops the array of operations
 * . @param fct the function definitions
 */
export function storeCode( ops: any[], fct: any ) {
    functions = fct;
    operations = ops;
    pc = 0;
    operationsStack = [];
    bindings = {};
    stack = [];
    // p( 'storeCode with state reset' );
}

/**
 * ---------- the hash map of function definitions ----------
 */
var functions = {};

/**
 * returns the code block of a function. The code block contains formal parameter names and the array of operations implementing the function
 * 
 * . @param name the name of the function
 */
export function getFunction( name: string ) {
    return functions[name];
}

/**
 * ---------- the binding of values to names (the 'environment') ----------
 */
var bindings = {};

/**
 * introduce a new binding. An old binding (if it exists) is hidden, until an unbinding occurs.
 * 
 * . @param name the name to which a value is bound
 * . @param value the value that is bound to a name
 */
export function bindVar( name: string, value ) {
    checkValidName( name );
    checkValidValue( value );
    var nameBindings = bindings[name];
    if ( nameBindings === undefined || nameBindings === null || nameBindings === [] ) {
        bindings[name] = [value];
        U.p( 'bind new ' + name + ' with ' + value + ' of type ' + typeof value );
    } else {
        nameBindings.unshift( value );
        U.p( 'bind&hide ' + name + ' with ' + value + ' of type ' + typeof value );
    }
}

/**
 * remove a  binding. An old binding (if it exists) is re-established.
 * 
 * . @param name the name to be unbound
 */
export function unbindVar( name: string ) {
    checkValidName( name );
    var oldBindings = bindings[name];
    if ( oldBindings.length < 1 ) {
        U.dbcException( 'unbind failed for: ' + name );
    }
    oldBindings.shift();
    U.p( 'unbind ' + name + ' remaining bindings are ' + oldBindings.length );
}

/**
 * get the value of a binding.
 * 
 * . @param name the name whose value is requested
 */
export function getVar( name: string ) {
    checkValidName( name );
    var nameBindings = bindings[name];
    if ( nameBindings === undefined || nameBindings === null || nameBindings.length < 1 ) {
        U.dbcException( 'getVar failed for: ' + name );
    }
    // p( 'get ' + name + ': ' + nameBindings[0] );
    return nameBindings[0];
}

/**
 * update the value of a binding.
 * 
 * . @param name the name whose value is updated
 * . @param value the new value for that bindinf
 */
export function setVar( name: string, value: any ) {
    checkValidName( name );
    checkValidValue( value );
    if ( value === undefined || value === null ) {
        U.dbcException( 'setVar value invalid' );
    }
    var nameBindings = bindings[name];
    if ( nameBindings === undefined || nameBindings === null || nameBindings.length < 1 ) {
        U.dbcException( 'setVar failed for: ' + name );
    }
    nameBindings[0] = value;
    // p( 'set ' + name + ': ' + nameBindings[0] );
}

/**
 * ---------- the stack of values ----------
 */
var stack = [];

/**
 * push a value onto the stack
 * 
 * . @param value the value to be pushed
 */
export function push( value ) {
    checkValidValue( value );
    stack.push( value );
    U.p( 'push ' + value + ' of type ' + typeof value );
}

/**
 * pop a value from the stack:
 * - discard the value
 * - return the va
 */
export function pop() {
    if ( stack.length < 1 ) {
        U.dbcException( 'pop failed with empty stack' );
    }
    var value = stack.pop();
    // p( 'pop ' + value );
    return value;
}

/**
 * helper: get a value from the stack. Do not discard the value
 *
 * . @param i the i'th value (starting from 0) is requested
 */
function get( i: number ) {
    if ( stack.length === 0 ) {
        U.dbcException( 'get failed with empty stack' );
    }
    return stack[stack.length - 1 - i];
}

/**
 * get the first (top) value from the stack. Do not discard the value
 */
export function get0() {
    return get( 0 );
}
/**
 * get the second value from the stack. Do not discard the value
 */
export function get1() {
    return get( 1 );
}
/**
 * get the third value from the stack. Do not discard the value
 */
export function get2() {
    return get( 2 );
}

/**
 * ---------- the management of the operations to be executed ----------
 * 
 * - operations contains the sequence of machine instructions that are executed sequentially
 * - pc, the program counter, is the index of the NEXT operation to be executed
 * - operationsStack is a stack of frozen (suspended) operations including their pc. Their execution is resumed
 *   if either the actual array of operations is exhausted or a C.FLOW_CONTROL operation is executed
 */
var operations = [];
var pc: number = 0;

var operationsStack = [];

/**
 * push the actual array of operations to the stack. 'ops' become the new actual array of operations.
 * The pc of the frozen array of operations is decremented by 1. This operation is typically called by
 * 'compound' statements as repeat, if, wait, but also for function calls.
 * 
 * . @param ops the new array of operations. Its 'pc' is set to 0
 */
export function pushOps( ops: any[] ) {
    if ( pc <= 0 ) {
        U.dbcException( 'pc must be > 0, but is ' + pc );
    }
    pc--;
    const opsWrapper = {};
    opsWrapper[C.OPS] = operations;
    opsWrapper[C.PC] = pc;
    operationsStack.unshift( opsWrapper );
    operations = ops;
    pc = 0;
    opLog( 'PUSHING STMTS' );
}

/**
 * get the next operation to be executed from the actual array of operations.
 * - If the 'pc' is less than the length of the actual array of operations, 'pc' is the index of
 *   the operation to be returned. The 'pc' is incremented by 1.
 * - Otherwise the stack of frozen operations is pop-ped until a not exhausted array of operations is found
 * 
 * NOTE: responsible for getting the new actual array of operations is @see popOpsUntil(). Here some cleanup of stack and binding
 * is done. Be VERY careful, if you change the implementation of @see popOpsUntil().
 */
export function getOp() {
    if ( operations !== undefined && pc >= operations.length ) {
        popOpsUntil();
    }
    return operations[pc++];
}

/**
 * unwind the stack of operation-arrays until
 * - if optional parameter is missing: executable operations are found, i.e. the 'pc' points INTO the array of operations
 * - if optional parameter is there: executable operations are found and C.OP_CODE of the operation with index 'pc' matches the value of 'target'.
 *   This is used by operations with C.OP_CODE 'C.FLOW_CONTROL' to unwind the stack until the statement list is found, that is the target of a 'continue'
 *   or 'break'. The 'if' statement uses it to skip behind the if after one of the 'then'-statement lists has been taken and is exhausted.
 *
 * . @param target optional parameter: if present, the unwinding of the stack will proceed until the C.OP_CODE of the operation matches 'target'
 */
export function popOpsUntil( target?: string ) {
    while ( true ) {
        var opsWrapper = operationsStack.shift();
        if ( opsWrapper === undefined ) {
            throw 'pop ops until ' + target + '-stmt failed';
        }
        const suspendedStmt = opsWrapper[C.OPS][opsWrapper[C.PC]];
        if ( suspendedStmt !== undefined ) {
            if ( suspendedStmt[C.OPCODE] === C.REPEAT_STMT && ( suspendedStmt[C.MODE] === C.TIMES || suspendedStmt[C.MODE] === C.FOR ) ) {
                unbindVar( suspendedStmt[C.NAME] );
                pop(); pop(); pop();
            }
            if ( target === undefined || suspendedStmt[C.OPCODE] === target ) {
                operations = opsWrapper[C.OPS];
                pc = opsWrapper[C.PC];
                return;
            }
        }
    }
}

/**
 * FOR DEBUGGING: write the actual array of operations to the 'console.log'. The actual operation is prefixed by '*'
 * 
 * . @param msg the prefix of the message (for easy reading of the logs)
 */
export function opLog( msg: string ) {
    var opl = '';
    var counter = 0;
    for ( let op of operations ) {
        var opc = op[C.OPCODE];
        if ( op[C.OPCODE] === C.EXPR ) {
            opc = opc + '[' + op[C.EXPR];
            if ( op[C.EXPR] === C.BINARY ) {
                opc = opc + '-' + op[C.OP];
            }
            opc = opc + ']';
        }
        opl = opl + ( counter++ == pc ? '*' : '' ) + opc + ' '
    }
    U.p( msg + ' pc:' + pc + ' ' + opl );
}

/**
 * for early error detection: assert, that a name given (for a binding) is valid
 */
function checkValidName( name ) {
    if ( name === undefined || name === null ) {
        U.dbcException( 'invalid name' );
    }
}

/**
 * for early error detection: assert, that a value given (for a binding) is valid
 */
function checkValidValue( value ) {
    if ( value === undefined || value === null ) {
        U.dbcException( 'bindVar value invalid' );
    }
}