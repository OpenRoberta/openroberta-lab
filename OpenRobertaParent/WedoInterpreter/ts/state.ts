import * as C from "./constants";
import * as U from './util';

var functions = {};

var operations = [];
var pc: number = 0;
var operationsStack = [];

var bindings = {};
var stack = [];

export function getFunction( name: string ) {
    return functions[name];
}

export function bindVar( name: string, value ) {
    checkValidName( name );
    checkValidValue( value );
    var nameBindings = bindings[name];
    if ( nameBindings === undefined || nameBindings === null || nameBindings === [] ) {
        bindings[name] = [value];
        p( 'bind new ' + name + ' with ' + value + ' of type ' + typeof value );
    } else {
        nameBindings.unshift( value );
        p( 'bind&hide ' + name + ' with ' + value + ' of type ' + typeof value );
    }
}

export function unbindVar( name: string ) {
    checkValidName( name );
    var oldBindings = bindings[name];
    if ( oldBindings.length < 1 ) {
        U.dbcException( "unbind failed for: " + name );
    }
    oldBindings.shift();
    p( 'unbind ' + name + ' remaining bindings are ' + oldBindings.length );
}

export function getVar( name: string ) {
    checkValidName( name );
    var nameBindings = bindings[name];
    if ( nameBindings === undefined || nameBindings === null || nameBindings.length < 1 ) {
        U.dbcException( "getVar failed for: " + name );
    }
    // p( 'get ' + name + ': ' + nameBindings[0] );
    return nameBindings[0];
}

export function setVar( name: string, value: any ) {
    checkValidName( name );
    checkValidValue( value );
    if ( value === undefined || value === null ) {
        U.dbcException( "setVar value invalid" );
    }
    var nameBindings = bindings[name];
    if ( nameBindings === undefined || nameBindings === null || nameBindings.length < 1 ) {
        U.dbcException( "setVar failed for: " + name );
    }
    nameBindings[0] = value;
    // p( 'set ' + name + ': ' + nameBindings[0] );
}

export function push( value ) {
    checkValidValue( value );
    stack.push( value );
    p( 'push ' + value + ' of type ' + typeof value );
}

export function pop() {
    if ( stack.length < 1 ) {
        U.dbcException( "pop failed with empty stack" );
    }
    var value = stack.pop();
    // p( 'pop ' + value );
    return value;
}

function get( i: number ) {
    if ( stack.length === 0 ) {
        U.dbcException( "get failed with empty stack" );
    }
    return stack[stack.length - 1 - i];
}

export function get0() {
    return get( 0 );
}
export function get1() {
    return get( 1 );
}
export function get2() {
    return get( 2 );
}

export function storeCode( ops: any[], fct: any ) {
    functions = fct;
    operations = ops;
    pc = 0;
    operationsStack = [];
    bindings = {};
    stack = [];
    // p( 'storeCode with state reset' );
}

// only for debugging!
export function getOps() {
    const state = {};
    state[C.OPS] = operations;
    state[C.PC] = pc;

    return state;
}

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

export function getOp() {
    if ( operations !== undefined && pc >= operations.length ) {
        popOpsUntil();
    }
    return operations[pc++];
}

export function popOpsUntil( target?: string ) {
    while ( true ) {
        var opsWrapper = operationsStack.shift();
        if ( opsWrapper === undefined ) {
            throw "pop ops until " + target + "-stmt failed";
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
    p( msg + ' pc:' + pc + ' ' + opl );
}

function checkValidName( name ) {
    if ( name === undefined || name === null ) {
        U.dbcException( "invalid name" );
    }
}

function checkValidValue( value ) {
    if ( value === undefined || value === null ) {
        U.dbcException( "bindVar value invalid" );
    }
}


function p( s: any ) {
    U.p( s );
}