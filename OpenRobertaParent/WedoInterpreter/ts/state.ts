import * as C from "./constants";
import * as U from './util';

var bindings = {};
var stack = [];
var operations = [];
var functions = {};
var pc: number = 0;
var operationsStack = [];

export function reset() {
    bindings = {};
    stack = [];
    operations = [];
    pc = 0;
    // p( 'state reset' );
}

export function getFunction( name: string ) {
    return functions[name];
}

export function bindVar( name: string, value ) {
    if ( name === undefined || name === null ) {
        dbcException( "bindVar name invalid" );
    }
    if ( value === undefined || value === null ) {
        dbcException( "bindVar value invalid" );
    }
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
    if ( name === undefined || name === null ) {
        dbcException( "unbindVar name invalid" );
    }
    var oldBindings = bindings[name];
    if ( oldBindings.length < 1 ) {
        dbcException( "unbind failed for: " + name );
    }
    oldBindings.shift();
    p( 'unbind ' + name + ' remaining bindings are ' + oldBindings.length );
}

export function getVar( name: string ) {
    if ( name === undefined || name === null ) {
        dbcException( "getVar name invalid" );
    }
    var nameBindings = bindings[name];
    if ( nameBindings === undefined || nameBindings === null || nameBindings.length < 1 ) {
        dbcException( "getVar failed for: " + name );
    }
    // p( 'get ' + name + ': ' + nameBindings[0] );
    return nameBindings[0];
}

export function setVar( name: string, value: any ) {
    if ( name === undefined || name === null ) {
        dbcException( "setVar name invalid" );
    }
    if ( value === undefined || value === null ) {
        dbcException( "setVar value invalid" );
    }
    var nameBindings = bindings[name];
    if ( nameBindings === undefined || nameBindings === null || nameBindings.length < 1 ) {
        dbcException( "setVar failed for: " + name );
    }
    nameBindings[0] = value;
    // p( 'set ' + name + ': ' + nameBindings[0] );
}

export function push( value ) {
    if ( value === undefined || value === null ) {
        dbcException( "push value invalid" );
    }
    stack.push( value );
    p( 'push ' + value + ' of type ' + typeof value );
}

export function pop() {
    if ( stack.length < 1 ) {
        dbcException( "pop failed with empty stack" );
    }
    var value = stack.pop();
    // p( 'pop ' + value );
    return value;
}

export function storeCode( ops: any[], fct: any ) {
    operations = ops;
    functions = fct;
    pc = 0;
}

// only for debugging!
export function getOps() {
    const state = {};
    state[C.OPS] = operations;
    state[C.PC] = pc;

    return state;
}

export function getOp() {
    while ( operations !== undefined && pc >= operations.length ) {
        popOps();
    }
    if ( operations === undefined ) {
        return undefined;
    } else {
        return operations[pc++];
    }
}

export function pushOps( reenable: boolean, ops: any[] ) {
    if ( reenable && pc > 0 ) {
        pc--;
    }
    const opsWrapper = {};
    opsWrapper[C.OPS] = operations;
    opsWrapper[C.PC] = pc;
    operationsStack.unshift( opsWrapper );
    operations = ops;
    pc = 0;
    opLog( 'PUSHING STMTS' );
}

export function popOps() {
    const opsWrapper = operationsStack.shift();
    operations = opsWrapper === undefined ? undefined : opsWrapper[C.OPS];
    pc = opsWrapper === undefined ? 0 : opsWrapper[C.PC];
}

export function popOpsUntil( target: string ) {
    while ( true ) {
        const opsWrapper = operationsStack.shift();
        if ( opsWrapper === undefined ) {
            throw "pop ops until " + target + "-stmt failed";
        }
        const suspendedStmt = opsWrapper[C.OPS][opsWrapper[C.PC]];
        clearDangerousProperties( suspendedStmt );
        if ( suspendedStmt[C.OPCODE] === target ) {
            operations = opsWrapper[C.OPS];
            pc = opsWrapper[C.PC];
            return;
        }
    }
}

export function clearDangerousProperties( stmt ) {
    const opc: string = stmt[C.OPCODE];
    if ( opc === C.REPEAT_STMT ) {
        stmt[C.VALUE] = undefined;
        stmt[C.END] = undefined;
    } else if ( opc === C.METHOD_CALL_VOID || opc === C.METHOD_CALL_RETURN ) {
        stmt[C.RETURN] = undefined;
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


function p( s: any ) {
    U.p( s );
}

function dbcException( s: string ) {
    U.dbcException( s );
}