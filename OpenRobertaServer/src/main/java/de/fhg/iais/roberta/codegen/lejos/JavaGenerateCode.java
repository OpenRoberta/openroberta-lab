package de.fhg.iais.roberta.codegen.lejos;

import java.util.ArrayList;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class is used to generate valid JAVA code from {@link Phrase} objects (<i>abstract syntax tree AST</i>).<br>
 * <br>
 * To create object of this class use {@link #JavaGenerateCode(BrickConfiguration, ArrayList)} constructor.
 * 
 * @author kcvejoski
 */
public class JavaGenerateCode {
    public static final String INDENT = "    ";

    private final String programName;
    private final BrickConfiguration brickConfiguration;
    private final ArrayList<Phrase> phrases;

    private final StringBuilder sb = new StringBuilder();

    /**
     * This constructor creates valid object of the class {@link JavaGenerateCode}.<br>
     * <br>
     * Client must provide valid brick configuration {@link BrickConfiguration} and valid AST.
     * 
     * @param brickConfiguration
     * @param phrases of the AST
     */
    public JavaGenerateCode(String programName, BrickConfiguration brickConfiguration, ArrayList<Phrase> phrases) {
        Assert.notNull(programName);
        Assert.notNull(brickConfiguration);
        Assert.isTrue(phrases.size() >= 1);

        this.programName = programName;
        this.brickConfiguration = brickConfiguration;
        this.phrases = phrases;
    }

    public void generate(boolean withWrapping) {
        generatePrefix(withWrapping);
        for ( Phrase phrase : this.phrases ) {
            this.sb.append("\n");
            JavaVisitor astVisitor = new JavaVisitor(this.sb, withWrapping ? 2 : 0, this.brickConfiguration);
            phrase.accept(astVisitor);
        }
        generateSuffix(withWrapping);
    }

    public String getCode() {
        return this.sb.toString();
    }

    private void generatePrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.sb.append("package generated.main;\n\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.HardwareComponent;\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.action.ActorPort;\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;\n");
        this.sb.append("import de.fhg.iais.roberta.codegen.lejos.Hal;\n\n");
        this.sb.append("public class " + this.programName + " {\n");
        this.sb.append(INDENT).append(this.brickConfiguration.generateRegenerate()).append("\n\n");
        this.sb.append(INDENT).append("public static void main(String[] args) {\n");
        this.sb.append(INDENT).append(INDENT).append("new ").append(this.programName).append("().run();\n");
        this.sb.append(INDENT).append("}\n\n");

        this.sb.append(INDENT).append("public void run() {\n");
        this.sb.append(INDENT).append(INDENT).append("Hal hal = new Hal(brickConfiguration);");
    }

    private void generateSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }

        this.sb.append("\n").append(INDENT).append("}\n}\n");
    }
}
