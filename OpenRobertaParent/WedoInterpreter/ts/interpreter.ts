import * as C from "./constants";
import * as S from "./state";
import * as U from "./util";

export function run( stmts ) {
    S.reset();
    evalStmts( stmts );
}

function evalStmts( stmts ) {
    for ( let stmt of stmts ) {
        evalStmt( stmt );
    }
}

export function evalStmt( stmt ) {
    const op = stmt[C.STMT];
    switch ( op ) {
        case "RepeatStmt": {
            const mode = stmt[C.MODE];
            if ( mode === "TIMES" ) {
                const head = stmt[C.EXPR];
                const variable = evalDecl( head[0] );
                var actual = evalExpr( head[1] );
                const end = evalExpr( head[2] );
                const step = evalExpr( head[3] );
                S.bindVar( variable, actual );
                while ( actual <= end ) {
                    evalStmts( stmt[C.STMT_LIST] );
                    actual += step;
                    S.setVar( variable, actual );
                }
                S.unbindVar( variable );
            } else if ( mode === "UNTIL" ) {
                var condition = evalExpr( stmt[C.EXPR] );
                while ( condition ) {
                    evalStmts( stmt[C.STMT_LIST] );
                    condition = evalExpr( stmt[C.EXPR] );
                }
            } else if ( mode === "FOR" ) {
                const head = stmt[C.EXPR];
                const variable = evalDecl( head[0] );
                var actual = evalExpr( head[1] );
                const end = evalExpr( head[2] );
                const step = evalExpr( head[3] );
                S.bindVar( variable, actual );
                while ( actual <= end ) {
                    evalStmts( stmt[C.STMT_LIST] );
                    actual += step;
                    S.setVar( variable, actual );
                }
                S.unbindVar( variable );
            } else {
                U.dbcException( "invalid repeat mode: " + mode );
            }
            break;
        }
        case "DriveAction": {
            const speed = evalExpr( stmt[C.SPEED] );
            const distance = evalExpr( stmt[C.DISTANCE] );
            const driveDirection = stmt[C.DRIVE_DIRECTION];
            p( "drive " + driveDirection + " " + distance + "cm " + speed + "%" );
            break;
        }
        case "ShowTextAction": {
            const showText = "" + evalExpr( stmt[C.TEXT] );
            const x = evalExpr( stmt[C.X] );
            const y = evalExpr( stmt[C.Y] );
            p( "show \"" + showText + "\" at " + x + "," + y );
            break;
        }
        case "VarDeclaration": {
            const name = stmt[C.NAME];
            const value = evalExpr( stmt[C.VALUE] );
            S.bindVar( name, value );
            break;
        }
        case "AssignStmt": {
            const name = stmt[C.NAME];
            const value = evalExpr( stmt[C.EXPR] );
            S.setVar( name, value );
            break;
        }
        default:
            U.dbcException( "invalid stmt op: " + op );
    }
}

function evalDecl( decl ) {
    return decl[C.NAME];
}

function evalExpr( expr ) {
    const op = expr[C.EXPR];
    switch ( op ) {
        case "Var": return S.getVar( expr[C.NAME] );
        case "NumConst": return expr[C.VALUE];
        case "letConst": return expr[C.VALUE];

        case "Unary": {
            const subOp = expr[C.OP];
            switch ( subOp ) {
                case "NOT":
                    const bool = evalExpr( expr[C.VALUE] );
                    return !bool;
                default:
                    U.dbcException( "invalid unary expr subOp: " + op );
            }
        }
        case "Binary": {
            const subOp = expr[C.OP];
            const left = evalExpr( expr[C.LEFT] );
            const right = evalExpr( expr[C.RIGHT] );
            switch ( subOp ) {
                case "EQ": return left === right;
                case "NE": return left !== right;
                case "ADD": return left + right;
                default:
                    U.dbcException( "invalid binary expr supOp: " + subOp );
            }
        }
        default:
            U.dbcException( "invalid expr op: " + op );
    }
}

function p( s ) {
    console.log( s );
}
