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
        String javaOut = "../RobotWeDo/src/main/java/de/fhg/iais/roberta/syntax/codegen/wedo/C.java";
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
                javaConstants.add(line);
                typescriptConstants.add(line);
                continue;
            }
            String[] cmp = line.split("\\s*=\\s*");
            String name = cmp[0];
            String value = cmp[1].trim();
            boolean isString = value.startsWith("\"");
            boolean isEnum = value.startsWith("{");
            boolean isDouble = value.contains(".");
            if ( isString ) {
                javaConstants.add("    public static final String " + name + " = " + value + ";");
                typescriptConstants.add("export const " + name + ": string = " + value + ";");
            } else if ( isEnum ) {
                value = value.substring(1, value.length() - 1).trim();
                javaConstants.add("    public static enum " + name + " {");
                javaConstants.add("        " + value);
                javaConstants.add("    }");
                typescriptConstants.add("export const " + name + " = {");
                typescriptConstants.add(Arrays.stream(value.split("\\s*,\\s*")).map(e -> "    " + e + ": \"" + e + "\"").collect(Collectors.joining(",\n")));
                typescriptConstants.add("}");
            } else if ( isDouble ) {
                javaConstants.add("    public static final double " + name + " = " + value + ";");
                typescriptConstants.add("export const " + name + ": number = " + value + ";");
            } else {
                javaConstants.add("    public static final int " + name + " = " + value + ";");
                typescriptConstants.add("export const " + name + ": number = " + value + ";");
            }
        }
        typescriptSuffix();
        javaSuffix();
        Util1.writeFile(javaOut, javaConstants.stream().collect(Collectors.joining("\n")));
        Util1.writeFile(typescriptOut, typescriptConstants.stream().collect(Collectors.joining("\n")));
    }

    private void typescriptPrefix() {
        typescriptConstants.add(""); // codelens doesn't work for the first line
    }

    private void typescriptSuffix() {
    }

    private void javaPrefix() {
        javaConstants.add("package de.fhg.iais.roberta.syntax.codegen.wedo;");
        javaConstants.add("");
        javaConstants.add("public class C {");
        javaConstants.add("");
    }

    private void javaSuffix() {
        javaConstants.add("}");
    }
}