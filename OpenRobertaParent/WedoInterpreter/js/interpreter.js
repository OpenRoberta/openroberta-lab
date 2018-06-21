const C = require('D:/temp/node/constants.js');

function evalStmts(bindings, stmts) {
	for (stmt of stmts) {
		evalStmt(bindings, stmt)
	}
}

exports.evalStmts = evalStmts;

function evalStmt(bindings, stmt) {
	var op = stmt[C.STMT];
	switch ( op ) {
		case "RepeatStmt": {
			var mode = stmt[C.MODE];
			if ( mode === "TIMES" ) {
				var head = stmt[C.EXPR];
				var variable = evalDecl(head[0]);
				var actual = evalExpr(bindings, head[1]);
				var end = evalExpr(bindings, head[2]);
				var step = evalExpr(bindings, head[3]);
				bindings[variable] = actual;
				while ( actual <= end ) {
					evalStmts(bindings, stmt[C.STMT_LIST]);
					actual += step;
					bindings[variable] = actual;
				}
				bindings[variable] = null;
				p("unbound " + variable);
			} else if ( mode === "UNTIL" ) {
				var condition = evalExpr(bindings, stmt[C.EXPR]);
				while ( condition ) {
					evalStmts(bindings, stmt[C.STMT_LIST]);
					condition = evalExpr(bindings, stmt[C.EXPR]);
				}
			} else if ( mode === "FOR" ) {
				var head = stmt[C.EXPR];
				var variable = evalDecl(head[0]);
				var actual = evalExpr(bindings, head[1]);
				var end = evalExpr(bindings, head[2]);
				var step = evalExpr(bindings, head[3]);
				bindings[variable] =  actual;
				p("bound " + variable + " = " + actual);
				while ( actual <= end ) {
					evalStmts(bindings, stmt[C.STMT_LIST]);
					actual += step;
					bindings[variable] =  actual;
					p("bound " + variable + " = " + actual);
				}
				bindings[variable] =  null;
				p("unbound " + variable);
			} else {
				throw "invalid repeat mode: " + mode;
			}
			break;
		}
		case "DriveAction": {
			var speed = evalExpr(bindings, stmt[C.SPEED]);
			var distance = evalExpr(bindings, stmt[C.DISTANCE]);
			var driveDirection = stmt[C.DRIVE_DIRECTION];
			p("drive " + driveDirection + " " + distance + "cm " + speed + "%");
			break;
		}
		case "ShowTextAction": {
			var showText = "" + evalExpr(bindings, stmt[C.TEXT]);
			var x = evalExpr(bindings, stmt[C.X]);
			var y = evalExpr(bindings, stmt[C.Y]);
			p("show \"" + showText + "\" at " + x + "," + y);
			break;
		}
		case "VarDeclaration": {
			var name = stmt[C.NAME];
			var value = evalExpr(bindings, stmt[C.VALUE]);
			bindings[name] = value;
			p("bound " + name + " = " + value);
			break;
		}
		case "AssignStmt": {
			var name = stmt[C.NAME];
			var value = evalExpr(bindings, stmt[C.EXPR]);
			bindings[name] = value;
			p("bound " + name + " = " + value);
			break;
		}
		default:
			throw "invalid stmt op: " + op;
	}
}

function evalDecl(decl) {
	return decl[C.NAME];
}

function evalExpr(bindings, expr) {
	var op = expr[C.EXPR];
	var subOp = null;
	switch ( op ) {
		case "Var":      return bindings[expr[C.NAME]];
		case "NumConst": return expr[C.VALUE];
		case "letConst": return expr[C.VALUE];
		
		case "Unary": {
			subOp = expr[C.OP];
			switch ( subOp ) {
				case "NOT":
					bool = evalExpr(bindings, expr[C.VALUE]);
					return !bool;
				default:
					throw "invalid unary expr subOp: " + op;
			}
		}
		case "Binary": {
			subOp = expr[C.OP];
			var left = evalExpr(bindings, expr[C.LEFT]);
			var right = evalExpr(bindings, expr[C.RIGHT]);
			switch ( subOp ) {
				case "EQ":  return left === right;
				case "NE":  return left !== right;
				case "ADD": return left + right;
				default:
					throw "invalid binary expr supOp: " + subOp;
			}
		}
		default:
			throw "invalid expr op: " + op;
	}
}

function p(s) {
	console.log(s);
}
