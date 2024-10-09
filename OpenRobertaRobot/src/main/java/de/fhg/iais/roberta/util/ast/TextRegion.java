package de.fhg.iais.roberta.util.ast;

public class TextRegion {
    public final int lineStart;
    public final int colStart;
    public final int lineStop;
    public final int colStop;

    public TextRegion(int lineStart, int colStart, int lineStop, int colStop) {
        this.lineStart = lineStart;
        this.colStart = colStart;
        this.lineStop = lineStop;
        this.colStop = colStop;
    }

    public int getLineStart() {
        return lineStart;
    }
    public int getColStart() {
        return colStart;
    }
}
