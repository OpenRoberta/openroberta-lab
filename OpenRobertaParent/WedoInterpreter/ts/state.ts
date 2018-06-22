import * as U from './util';

var bindings = {};
var stack = [];
var operations = [];
var pc: number = 0;
var operationsStack = [];

export function reset() {
    bindings = {};
    stack = [];
    operations = [];
    pc = 0;
    // p( 'state reset' );
}

export function bindVar( name: string, value: any ) {
    if ( name === undefined || name === null ) {
        dbcException( "bindVar name invalid" );
    }
    if ( value === undefined || value === null ) {
        dbcException( "bindVar value invalid" );
    }
    var nameBindings = bindings[name];
    if ( nameBindings === undefined || nameBindings === null || nameBindings === [] ) {
        bindings[name] = [value];
        // p( 'bind new ' + name + ' = ' + value );
    } else {
        nameBindings.unshift( value );
        // p( 'hide old ' + name + ' = ' + value );
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
    // p( 'unbind ' + name + '. Number of remaining bindings: ' + oldBindings.length );
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

export function push( value: any ) {
    if ( value === undefined || value === null ) {
        dbcException( "push value invalid" );
    }
    stack.push( value );
    // p( 'push ' + value );
}

export function pop() {
    if ( stack.length < 1 ) {
        dbcException( "pop failed with empty stack" );
    }
    var value = stack.pop();
    // p( 'pop ' + value );
    return value;
}

export function storeOps( ops ) {
    operations = ops;
    pc = 0;
}

// only for debugging!
export function getOps() {
    return { 'ops': operations, 'pc': pc };
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

export function reEnableOp() {
    if ( pc-- <= 0 ) {
        pc++;
        U.dbcException( "No op to re-enable: " + pc );
    }
}

export function pushOps( ops ) {
    const opsWrapper = {};
    opsWrapper["ops"] = operations;
    opsWrapper["pc"] = pc;
    operationsStack.unshift( opsWrapper );
    operations = ops;
    pc = 0;
}

export function popOps() {
    const opsWrapper = operationsStack.shift();
    operations = opsWrapper === undefined ? undefined : opsWrapper["ops"];
    pc = opsWrapper === undefined ? 0 : opsWrapper["pc"];
}

export function opLog( msg: string ) {
    var opl = ''
    for ( let op of operations ) {
        opl = opl + op["opc"] + ' '
    }
    p( msg + ' ' + opl );
}


function p( s: any ) {
    U.p( s );
}

function dbcException( s: string ) {
    U.dbcException( s );
}