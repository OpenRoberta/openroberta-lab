package de.fhg.iais.roberta.codegen.lejos;

import java.util.ArrayList;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.Phrase;

public class JavaGenerator {
    private final StringBuilder sb = new StringBuilder();
    private final String className;
    private final BrickConfiguration brickConfiguration;

    public JavaGenerator(String className, BrickConfiguration brickConfiguration) {
        this.className = className;
        this.brickConfiguration = brickConfiguration;
    }

    public void generate(ArrayList<Phrase> phrases) {
        generateCodeFromPhrases(phrases);
    }

    public void generateCodeFromPhrases(ArrayList<Phrase> phrases) {
        for ( Phrase phrase : phrases ) {
            this.sb.append("\n");
            JavaVisitor astVisitor = new JavaVisitor(this.sb, 0);
            phrase.accept(astVisitor);
        }
    }

    public String getClassName() {
        return this.className;
    }

    public BrickConfiguration getBrickConfiguration() {
        return this.brickConfiguration;
    }

    public StringBuilder getSb() {
        return this.sb;
    }
}
