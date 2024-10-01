package de.fhg.iais.roberta.exprEvaluator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class EvalExprStmtErrorListener extends BaseErrorListener {
    private final List<String> errors = new LinkedList<>();
    private List<String> stackRule = new LinkedList<>();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        String s = "";
        List<String> stack = ((Parser) recognizer).getRuleInvocationStack();
        Collections.reverse(stack);
        s += "rule stack: " + stack + "-> ";
        s += "line " + line + ":" + charPositionInLine + " at " + offendingSymbol + ": " + msg;
        //this.error.add(s);
        msg = msg.replace("<EOF>", "at the end of the line");
        msg = msg.replace("NAME", " a different option for this expression ");
        //TextlyError error = new TextlyError(line, charPositionInLine, offendingSymbol.toString(), msg);
        this.errors.add(msg);
        this.stackRule = stack;
    }

    public List<String> getError() {
        return this.errors;
    }

    public List<String> getStackRule() {
        return this.stackRule;
    }


}
