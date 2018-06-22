import * as UTIL from './util';

var bindings = {};
var stack = [];

export function reset() {
    bindings = {};
    stack = [];
    p( 'state reset' );
}

export function bindVar( name, value ) {
    if ( name === undefined || name === null ) {
        dbcException( "bindVar name invalid" );
    }
    if ( value === undefined || value === null ) {
        dbcException( "bindVar value invalid" );
    }
    var nameBindings = bindings[name];
    if ( nameBindings === undefined || nameBindings === null || nameBindings === [] ) {
        bindings[name] = [value];
        p( 'bind new ' + name + ' = ' + value );
    } else {
        nameBindings.unshift( value );
        p( 'hide ' + name + ' = ' + value );
    }
}
export function unbindVar( name ) {
    if ( name === undefined || name === null ) {
        dbcException( "unbindVar name invalid" );
    }
    var oldBindings = bindings[name];
    if ( oldBindings.length < 1 ) {
        dbcException( "unbind failed for: " + name );
    }
    oldBindings.shift();
    p( 'unbind ' + name + ' bindings remain: ' + oldBindings.length );
}

export function getVar( name ) {
    if ( name === undefined || name === null ) {
        dbcException( "getVar name invalid" );
    }
    var nameBindings = bindings[name];
    if ( nameBindings === undefined || nameBindings === null || nameBindings.length < 1 ) {
        dbcException( "getVar failed for: " + name );
    }
    p( 'get ' + name + ': ' + nameBindings[0] );
    return nameBindings[0];
}

export function setVar( name, value ) {
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
    p( 'set ' + name + ': ' + nameBindings[0] );
}

export function push( value ) {
    if ( value === undefined || value === null ) {
        dbcException( "push value invalid" );
    }
    stack.push( value );
    p( 'push ' + value );
}

export function pop() {
    if ( stack.length < 1 ) {
        dbcException( "pop failed with empty stack" );
    }
    var value = stack.pop();
    p( 'pop ' + value );
    return value;
}

function p( s ) {
    UTIL.p( s );
}

function dbcException( s ) {
    UTIL.dbcException( s );
}