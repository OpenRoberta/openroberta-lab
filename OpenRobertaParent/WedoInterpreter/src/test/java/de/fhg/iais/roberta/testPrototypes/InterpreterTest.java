package de.fhg.iais.roberta.testPrototypes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class InterpreterTest {
    private static final Logger LOG = LoggerFactory.getLogger(InterpreterTest.class);
    private static final String TEST_BASE = "simulatorTests/";

    @Test
    public void testInterpreter() throws Exception {
        String base = "simple";
        String joAsString = Util1.readFileContent(TEST_BASE + base + ".json");
        JSONObject program = new JSONObject(joAsString);
        JSONArray stmts = program.getJSONArray("programStmts");
        Map<String, Object> bindings = new HashMap<>();
        evalStmts(bindings, stmts);
    }

    private void evalStmts(Map<String, Object> bindings, JSONArray stmts) {
        stmts.iterator().forEachRemaining(stmt -> evalStmt(bindings, (JSONObject) stmt));
    }

    private void evalStmt(Map<String, Object> bindings, JSONObject stmt) {
        String op = stmt.getString("stmt");
        switch ( op ) {
            case "RepeatStmt": {
                String mode = stmt.getString("mode");
                if ( mode.equals("TIMES") ) {
                    JSONArray head = stmt.getJSONArray("expr");
                    String var = evalDecl((JSONObject) head.get(0));
                    float actual = (float) evalExpr(bindings, (JSONObject) head.get(1));
                    float end = (float) evalExpr(bindings, (JSONObject) head.get(2));
                    float step = (float) evalExpr(bindings, (JSONObject) head.get(3));
                    bindings.put(var, actual);
                    while ( actual <= end ) {
                        evalStmts(bindings, stmt.getJSONArray("stmtList"));
                        actual += step;
                        bindings.put(var, actual);
                    }
                    bindings.remove(var);
                    p("unbound " + var);
                } else if ( mode.equals("UNTIL") ) {
                    boolean condition = (boolean) evalExpr(bindings, stmt.getJSONObject("expr"));
                    while ( condition ) {
                        evalStmts(bindings, stmt.getJSONArray("stmtList"));
                        condition = (boolean) evalExpr(bindings, stmt.getJSONObject("expr"));
                    }
                } else if ( mode.equals("FOR") ) {
                    JSONArray head = stmt.getJSONArray("expr");
                    String var = evalDecl((JSONObject) head.get(0));
                    float actual = (float) evalExpr(bindings, (JSONObject) head.get(1));
                    float end = (float) evalExpr(bindings, (JSONObject) head.get(2));
                    float step = (float) evalExpr(bindings, (JSONObject) head.get(3));
                    bindings.put(var, actual);
                    p("bound " + var + " = " + actual);
                    while ( actual <= end ) {
                        evalStmts(bindings, stmt.getJSONArray("stmtList"));
                        actual += step;
                        bindings.put(var, actual);
                        p("bound " + var + " = " + actual);
                    }
                    bindings.remove(var);
                    p("unbound " + var);
                } else {
                    throw new DbcException("invalid repeat mode: " + mode);
                }
                break;
            }
            case "DriveAction": {
                float speed = (float) evalExpr(bindings, stmt.getJSONObject("speed"));
                float distance = (float) evalExpr(bindings, stmt.getJSONObject("distance"));
                String driveDirection = stmt.getString("driveDirection");
                p("drive " + driveDirection + " " + distance + "cm " + speed + "%");
                break;
            }
            case "ShowTextAction": {
                String text = evalExpr(bindings, stmt.getJSONObject("text")).toString();
                float x = (float) evalExpr(bindings, stmt.getJSONObject("x"));
                float y = (float) evalExpr(bindings, stmt.getJSONObject("y"));
                p("show \"" + text + "\" at " + x + "," + y);
                break;
            }
            case "VarDeclaration": {
                String name = stmt.getString("name");
                Object value = evalExpr(bindings, stmt.getJSONObject("value"));
                bindings.put(name, value);
                p("bound " + name + " = " + value);
                break;
            }
            case "AssignStmt": {
                String name = stmt.getString("name");
                Object value = evalExpr(bindings, stmt.getJSONObject("expr"));
                bindings.put(name, value);
                p("bound " + name + " = " + value);
                break;
            }
            default:
                throw new DbcException("invalid stmt op: " + op);
        }
    }

    private String evalDecl(JSONObject decl) {
        Assert.isTrue(decl.getString("expr").equals("Var"), "decl expects a variable");
        return decl.getString("name");
    }

    private Object evalExpr(Map<String, Object> bindings, JSONObject expr) {
        String op = expr.getString("expr");
        String subOp;
        switch ( op ) {
            case "Var":
                return bindings.get(expr.getString("name"));
            case "NumConst":
                return Float.parseFloat("" + expr.get("value"));
            case "StringConst":
                return expr.getString("value");
            case "Unary": {
                subOp = expr.getString("op");
                switch ( subOp ) {
                    case "NOT":
                        boolean bool = (boolean) evalExpr(bindings, expr.getJSONObject("value"));
                        return !bool;
                    default:
                        throw new DbcException("invalid unary expr subOp: " + op);
                }
            }
            case "Binary": {
                subOp = expr.getString("op");
                Object left = evalExpr(bindings, expr.getJSONObject("left"));
                Object right = evalExpr(bindings, expr.getJSONObject("right"));
                switch ( subOp ) {
                    case "EQ":
                        return Objects.equals(left, right);
                    case "NE":
                        return !Objects.equals(left, right);
                    case "ADD":
                        return (float) left + (float) right;
                    default:
                        throw new DbcException("invalid binary expr supOp: " + subOp);
                }
            }
            default:
                throw new DbcException("invalid expr op: " + op);
        }
    }

    private void p(String s) {
        System.out.println(s);
    }
}