package de.fhg.iais.roberta.codegen.lejos;

import java.util.ArrayList;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.Phrase;

/**
 * This class is used to generate valid JAVA code from {@link Phrase} objects (<i>abstract syntax tree AST</i>).<br>
 * <br>
 * To create object of this class use {@link #JavaGenerateCode(BrickConfiguration, ArrayList)} constructor.
 * 
 * @author kcvejoski
 */
public class JavaGenerateCode extends GenerateCode {

    /**
     * This constructor creates valid object of the class {@link JavaGenerateCode}.<br>
     * <br>
     * Client must provide valid brick configuration {@link BrickConfiguration} and valid AST.
     * 
     * @param brickConfiguration
     * @param phrases of the AST
     */
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
