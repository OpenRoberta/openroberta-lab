package de.fhg.iais.roberta.javaServer.integrationTest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.Util;

public class TestTypecheckUtilModifyXmlProgram {
    private static final Pattern STMTEXPRESSION_PATTERN = Pattern.compile("<field name=\"STMTEXPRESSION\">(.*?)</field>");
    private static final Pattern TYPE_PATTERN = Pattern.compile("--TYPE--");
    private static final Pattern VAR_NAME_PATTERN = Pattern.compile("--VAR-NAME--");
    private static final Pattern EVAL_PATTERN = Pattern.compile("--EVAL--");

    private static final Map<BlocklyType, String> VAR_NAME_MAP = new EnumMap<>(BlocklyType.class);

    static {
        VAR_NAME_MAP.put(BlocklyType.BOOLEAN, "boolT");
        VAR_NAME_MAP.put(BlocklyType.NUMBER, "num");
        VAR_NAME_MAP.put(BlocklyType.STRING, "str");
        VAR_NAME_MAP.put(BlocklyType.ARRAY_NUMBER, "listN2");
        VAR_NAME_MAP.put(BlocklyType.IMAGE, "img");
        VAR_NAME_MAP.put(BlocklyType.COLOR, "color");
    }

    public static void newProgramStmtExpr(String newExpression, String nameForXml, String robot) {

        String filePath = "/crossCompilerTests/robotSpecific/" + robot + "/textly/templateProgramStmtExpr.xml";
        String outputFilePath = "src/test/resources/crossCompilerTests/robotSpecific/" + robot + "/textly/" + nameForXml + ".xml";
        try {

            String xmlContent = Util.readResourceContent(filePath);
            String escapedExpression = escapeXmlSpecialCharacters(newExpression);
            Matcher matcher = STMTEXPRESSION_PATTERN.matcher(xmlContent);

            String modifiedXML = matcher.replaceAll("<field name=\"STMTEXPRESSION\">" + escapedExpression + "</field>");

            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
            writer.write(modifiedXML);
            writer.close();


        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public static void newProgramEvalExpr(BlocklyType newDataType, String evalExpression, String nameForXml) {
        String filePath = "/crossCompilerTests/robotSpecific/microbitv2/textly/templateProgramExprEval.xml";
        String outputFilePath = "src/test/resources/crossCompilerTests/robotSpecific/microbitv2/textly/" + nameForXml + ".xml";
        try {
            String xmlContent = Util.readResourceContent(filePath);


            String varName = VAR_NAME_MAP.getOrDefault(newDataType, "unknownVar");

            String varType = newDataType.getBlocklyName();
            String escapedVarType = escapeXmlSpecialCharacters(varType);
            String escapedVarName = escapeXmlSpecialCharacters(varName);
            String escapedEvalExpression = escapeXmlSpecialCharacters(evalExpression);

            Matcher typeMatcher = TYPE_PATTERN.matcher(xmlContent);
            String modifiedXML = typeMatcher.replaceAll(escapedVarType);

            Matcher varNameMatcher = VAR_NAME_PATTERN.matcher(modifiedXML);
            modifiedXML = varNameMatcher.replaceAll(escapedVarName);

            Matcher evalMatcher = EVAL_PATTERN.matcher(modifiedXML);
            modifiedXML = evalMatcher.replaceAll(escapedEvalExpression);

            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
            writer.write(modifiedXML);
            writer.close();

        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    private static String escapeXmlSpecialCharacters(String input) {
        return input
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;");
    }

}
