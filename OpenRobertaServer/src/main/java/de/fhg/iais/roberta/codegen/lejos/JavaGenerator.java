package de.fhg.iais.roberta.codegen.lejos;

import de.fhg.iais.roberta.ast.syntax.Phrase;

public class JavaGenerator {
    StringBuilder sb = new StringBuilder();

    public void generate(Phrase phrase) {
        JavaVisitor astVisitor = new JavaVisitor(this.sb, 0);
        phrase.accept(astVisitor);
    }

    public StringBuilder getSb() {
        return this.sb;
    }

    public void setSb(StringBuilder sb) {
        this.sb = sb;
    }
}
