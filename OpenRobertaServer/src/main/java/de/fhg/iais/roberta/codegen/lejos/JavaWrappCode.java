package de.fhg.iais.roberta.codegen.lejos;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.helper.StringManipulation;

public class JavaWrappCode extends WrappCode {

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
