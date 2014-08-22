package de.fhg.iais.roberta.codegen.lejos;

import java.util.ArrayList;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.helper.StringManipulation;

/**
 * This class is used to generate valid JAVA code from {@link Phrase} objects (<i>abstract syntax tree AST</i>).<br>
 * <br>
 * To create object of this class use {@link #JavaGenerateCode(BrickConfiguration, ArrayList)} constructor.
 * 
 * @author kcvejoski
 */
public class JavaGenerateCode {
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
            JavaVisitor astVisitor = new JavaVisitor(this.sb, withWrapping ? 2 * PrettyPrintSettings.indentationSize : 0, this.brickConfiguration);
            phrase.accept(astVisitor);
        }
        generateSuffix(withWrapping);
    }

    public String getCode() {
        return this.sb.toString();
    }

    public void generatePrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        int next = PrettyPrintSettings.indentationSize;
        StringManipulation.appendCustomString(this.sb, 0, false, true, "package generated.main;");
        StringManipulation.appendCustomString(this.sb, 0, true, true, "import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;");
        StringManipulation.appendCustomString(this.sb, 0, false, true, "import de.fhg.iais.roberta.ast.syntax.HardwareComponent;");
        StringManipulation.appendCustomString(this.sb, 0, false, true, "import de.fhg.iais.roberta.ast.syntax.action.ActorPort;");
        StringManipulation.appendCustomString(this.sb, 0, false, true, "import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;");
        StringManipulation.appendCustomString(this.sb, 0, false, true, "import de.fhg.iais.roberta.codegen.lejos.Hal;");
        StringManipulation.appendCustomString(this.sb, 0, true, true, "public class " + this.programName + " {");

        StringManipulation.appendCustomString(this.sb, next, false, true, this.brickConfiguration.generateRegenerate());

        StringManipulation.appendCustomString(this.sb, next, true, false, "public static void main(String[] args) {");
        StringManipulation.appendCustomString(this.sb, next + PrettyPrintSettings.indentationSize, true, false, "new " + this.programName + "().run();");
        StringManipulation.appendCustomString(this.sb, next, true, true, "}");

        StringManipulation.appendCustomString(this.sb, next, true, false, "public void run() {");
        StringManipulation.appendCustomString(this.sb, next + PrettyPrintSettings.indentationSize, true, false, "Hal hal = new Hal(brickConfiguration);");
    }

    public void generateSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        StringManipulation.appendCustomString(this.sb, PrettyPrintSettings.indentationSize, true, true, "}");
        StringManipulation.appendCustomString(this.sb, 0, false, true, "}");
    }
}
