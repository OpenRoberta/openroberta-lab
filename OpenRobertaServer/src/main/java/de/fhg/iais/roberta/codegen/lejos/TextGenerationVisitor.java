package de.fhg.iais.roberta.codegen.lejos;


public abstract class TextGenerationVisitor implements Visitor {

    protected final StringBuilder sb;

    public TextGenerationVisitor(StringBuilder sb) {
        this.sb = sb;
    }

    protected TextGenerationVisitor addPhrase(Object... strings) {
        for ( Object string : strings ) {
            this.sb.append(string);
        }
        return this;
    }

    protected TextGenerationVisitor addWhitespace() {
        this.sb.append(" ");
        return this;
    }

}
