package de.fhg.iais.roberta.codegen.lejos;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.helper.StringManipulation;

/**
 * This class is used to generate valid JAVA code for wrapping generated code from {@link GenerateCode} object.<br>
 * <br>
 * To create object of this class use {@link #JavaWrappCode(String, BrickConfiguration)} constructor.
 * 
 * @author kcvejoski
 */
public class JavaWrappCode extends WrappCode {
    /**
     * This constructor creates valid object of the class {@link JavaWrappCode}.<br>
     * <br>
     * Client must provide name of the program (<i>this is used as a class name</i>) and valid brick configuration {@link BrickConfiguration}.
     * 
     * @param programName
     * @param brickConfiguration
     */
    public JavaWrappCode(String programName, BrickConfiguration brickConfiguration) {
        super(programName, brickConfiguration);
    }

    @Override
    public String generate(GenerateCode generateCode) {
        int next = PreattyPrintSettings.indentationSize;
        StringManipulation.appendCustomString(this.sb, 0, false, true, "import de.fhg.iais.roberta.codegen.lejos;");
        StringManipulation.appendCustomString(this.sb, 0, false, true, "import de.fhg.iais.roberta.ast.syntax;");

        StringManipulation.appendCustomString(this.sb, 0, true, true, "public class " + this.programName + " {");

        StringManipulation.appendCustomString(this.sb, next, false, true, this.brickConfiguration.generateRegenerate());

        StringManipulation.appendCustomString(this.sb, next, true, false, "public static void main(String[] args) {");
        StringManipulation.appendCustomString(this.sb, next + PreattyPrintSettings.indentationSize, true, false, this.programName + ".run();");
        StringManipulation.appendCustomString(this.sb, next, true, true, "}");

        StringManipulation.appendCustomString(this.sb, next, true, false, "public static void run() {");
        StringManipulation.appendCustomString(this.sb, next + PreattyPrintSettings.indentationSize, true, false, "Hal hal = new Hal(brickConfiguration);");
        generateCode.generate(next + PreattyPrintSettings.indentationSize);
        this.sb.append(generateCode.getSb().toString());
        StringManipulation.appendCustomString(this.sb, next, true, true, "}");

        this.sb.append("}");
        return this.sb.toString();
    }
}
