package de.fhg.iais.roberta.exprEvaluator;

public class TextlyError {
    private final int line;
    private final int charPositionInLine;
    private final String offendingSymbol;
    private final String message;

    public TextlyError(int line, int charPositionInLine, String offendingSymbol, String message) {
        this.line = line;
        this.charPositionInLine = charPositionInLine;
        this.offendingSymbol = offendingSymbol;
        this.message = message;
    }

    public int getLine() {
        return line;
    }

    public int getCharPositionInLine() {
        return charPositionInLine;
    }

    public String getOffendingSymbol() {
        return offendingSymbol;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Line " + line + ":" + charPositionInLine + " at '" + offendingSymbol + "': " + message;
    }
}