package de.fhg.iais.roberta.codegen.lejos;

import java.util.ArrayList;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.Phrase;

public class JavaGenerateCode extends GenerateCode {

    public JavaGenerateCode(BrickConfiguration brickConfiguration, ArrayList<Phrase> phrases) {
        super(brickConfiguration, phrases);
    }

    @Override
    public void generate(int indentation) {
        for ( Phrase phrase : this.phrases ) {
            this.sb.append("\n");
            JavaVisitor astVisitor = new JavaVisitor(this.sb, indentation, this.brickConfiguration);
            phrase.accept(astVisitor);
        }
    }
}
