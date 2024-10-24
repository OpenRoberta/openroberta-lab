package de.fhg.iais.roberta.exprEvaluator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.json.JSONObject;


public class EvalExprStmtErrorListener extends BaseErrorListener {
    private final List<JSONObject> errors = new LinkedList<>();
    private List<String> stackRule = new LinkedList<>();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        String s = "";
        List<String> stack = ((Parser) recognizer).getRuleInvocationStack();
        Collections.reverse(stack);
        s += "rule stack: " + stack + "-> ";
        s += "line " + line + ":" + charPositionInLine + " at " + offendingSymbol + ": " + msg;
        msg = msg.replace("<EOF>", "at the end of the line");
        msg = msg.replace("NAME", " a different option for this expression ");
        JSONObject errorObject = new JSONObject();
        errorObject.put("line", line);
        errorObject.put("charPositionInLine", charPositionInLine);
        errorObject.put("offendingSymbol", offendingSymbol.toString());
        errorObject.put("message", msg);
        this.errors.add(errorObject);
        this.stackRule = stack;
    }

    public List<JSONObject> getError() {
        return this.errors;
    }

    public List<String> getStackRule() {
        return this.stackRule;
    }
}
