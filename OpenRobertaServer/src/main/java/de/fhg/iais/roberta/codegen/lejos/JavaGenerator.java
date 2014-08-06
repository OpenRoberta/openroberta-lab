package de.fhg.iais.roberta.codegen.lejos;

import java.util.ArrayList;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.Phrase;

public class JavaGenerator {
    private final StringBuilder sb = new StringBuilder();
    private final String className;
    private final BrickConfiguration brickConfiguration;
    private final int indentation = 4;

    public JavaGenerator(String className, BrickConfiguration brickConfiguration) {
        this.className = className;
        this.brickConfiguration = brickConfiguration;
    }

    public void generate(ArrayList<Phrase> phrases) {
        this.sb.append("import de.fhg.iais.roberta.codegen.lejos;\n\n");
        StringManipulation.appendCustomString(this.sb, 0, false, "public class " + this.className + " {");

        StringManipulation.appendCustomString(this.sb, this.indentation, true, "public static void main(String[] args) {");
        StringManipulation.appendCustomString(this.sb, 2 * this.indentation, true, this.className + ".run();");
        StringManipulation.appendCustomString(this.sb, this.indentation, true, "}\n");
        StringManipulation.appendCustomString(this.sb, this.indentation, true, "public static void run() {");
        StringManipulation.appendCustomString(this.sb, 2 * this.indentation, true, this.brickConfiguration.generateRegenerate());
        StringManipulation.appendCustomString(this.sb, 2 * this.indentation, true, "Hal hal = new Hal(brickConfiguration);");
        generateCodeFromPhrases(phrases);
        StringManipulation.appendCustomString(this.sb, this.indentation, true, "}");
        this.sb.append("\n}");
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
