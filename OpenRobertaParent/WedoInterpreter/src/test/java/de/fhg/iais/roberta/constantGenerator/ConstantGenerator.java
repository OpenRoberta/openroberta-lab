package de.fhg.iais.roberta.constantGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import de.fhg.iais.roberta.util.Util1;

public class ConstantGenerator {
    private final List<String> javaConstants = new ArrayList<>();
    private final List<String> typescriptConstants = new ArrayList<>();

    @Test
    public void generateConstants() throws Exception {
        String in = "./constantsSource.txt";
        String javaOut = "../RobotWeDo/src/main/java/de/fhg/iais/roberta/visitor/C.java";
        String typescriptOut = "./ts/constants.ts";
        generateConstants(in, javaOut, typescriptOut);
    }

    private void generateConstants(String in, String javaOut, String typescriptOut) throws Exception {
        typescriptPrefix();
        javaPrefix();
        for ( String line : Util1.readFileLines(in) ) {
            line = line.trim();
            if ( line.equals("") ) {
                continue;
            }
            if ( line.startsWith("//") ) {
                this.javaConstants.add(line);
                this.typescriptConstants.add(line);
                continue;
            }
            String[] cmp = line.split("\\s*=\\s*");
            String name = cmp[0];
            String value = cmp[1].trim();
            boolean isString = value.startsWith("\"");
            boolean isEnum = value.startsWith("{");
            boolean isDouble = value.contains(".");
            if ( isString ) {
                this.javaConstants.add("    public static final String " + name + " = " + value + ";");
                this.typescriptConstants.add("export const " + name + ": string = " + value + ";");
            } else if ( isEnum ) {
                value = value.substring(1, value.length() - 1).trim();
                this.javaConstants.add("    public static enum " + name + " {");
                this.javaConstants.add("        " + value);
                this.javaConstants.add("    }");
                this.typescriptConstants.add("export const " + name + " = {");
                this.typescriptConstants
                    .add(Arrays.stream(value.split("\\s*,\\s*")).map(e -> "    " + e + ": \"" + e + "\"").collect(Collectors.joining(",\n")));
                this.typescriptConstants.add("}");
            } else if ( isDouble ) {
                this.javaConstants.add("    public static final double " + name + " = " + value + ";");
                this.typescriptConstants.add("export const " + name + ": number = " + value + ";");
            } else {
                this.javaConstants.add("    public static final int " + name + " = " + value + ";");
                this.typescriptConstants.add("export const " + name + ": number = " + value + ";");
            }
        }
        typescriptSuffix();
        javaSuffix();
        Util1.writeFile(javaOut, this.javaConstants.stream().collect(Collectors.joining("\n")));
        Util1.writeFile(typescriptOut, this.typescriptConstants.stream().collect(Collectors.joining("\n")));
    }

    private void typescriptPrefix() {
        this.typescriptConstants.add(""); // codelens doesn't work for the first line
    }

    private void typescriptSuffix() {
    }

    private void javaPrefix() {
        this.javaConstants.add("package de.fhg.iais.roberta.syntax.codegen.wedo;");
        this.javaConstants.add("");
        this.javaConstants.add("public class C {");
        this.javaConstants.add("");
    }

    private void javaSuffix() {
        this.javaConstants.add("}");
    }
}