package de.fhg.iais.roberta.javaServer.typecheck;

import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;

import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.Util;

public class TestTypecheckUtil {
    private static final Pattern TYPE_PATTERN = Pattern.compile("--TYPE--");
    private static final Pattern VAR_NAME_PATTERN = Pattern.compile("--VAR-NAME--");
    private static final Pattern TO_BE_TESTED_PATTERN = Pattern.compile("--TO-BE-TESTED--");

    private static final Map<BlocklyType, String> VAR_NAME_MAP = new EnumMap<>(BlocklyType.class);
    static {
        VAR_NAME_MAP.put(BlocklyType.BOOLEAN, "boolT");
        VAR_NAME_MAP.put(BlocklyType.NUMBER, "num");
        VAR_NAME_MAP.put(BlocklyType.STRING, "str");
        VAR_NAME_MAP.put(BlocklyType.ARRAY_NUMBER, "listN2");
        VAR_NAME_MAP.put(BlocklyType.IMAGE, "img");
        VAR_NAME_MAP.put(BlocklyType.COLOR, "color");
    }

    public static String getProgramUnderTestForEvalStmt(RobotFactory robotFactory, String stmt) {
        String pathPartForRobot = robotFactory.getPluginProperties().getStringProperty("robot.plugin.group");
        if (pathPartForRobot == null) {
            pathPartForRobot = robotFactory.getPluginProperties().getRobotName();
        }
        String filePath = "/crossCompilerTests/robotSpecific/" + pathPartForRobot + "/textly/templateProgramStmtExpr.xml";
        String xmlContent = Util.readResourceContent(filePath);

        Matcher matcher = TO_BE_TESTED_PATTERN.matcher(xmlContent);
        String modifiedXML = matcher.replaceAll(escapeXmlSpecialCharacters(stmt));
        return modifiedXML;
    }

    public static String getProgramUnderTestForEvalExpr(RobotFactory robotFactory, BlocklyType expectedType, String toBeTested) {
        String pathPartForRobot = robotFactory.getPluginProperties().getStringProperty("robot.plugin.group");
        if (pathPartForRobot == null) {
            pathPartForRobot = robotFactory.getPluginProperties().getRobotName();
        }
        String filePath = "/crossCompilerTests/robotSpecific/" + pathPartForRobot + "/textly/templateProgramExprEval.xml";
        String xmlContent = Util.readResourceContent(filePath);

        String varName = VAR_NAME_MAP.get(expectedType);
        Assert.assertNotNull(varName, "Variable of type " + expectedType + " not found");
        String varType = expectedType.getBlocklyName();
        String escapedVarType = escapeXmlSpecialCharacters(varType);
        String escapedVarName = escapeXmlSpecialCharacters(varName);
        String escapedtoBeTested = escapeXmlSpecialCharacters(toBeTested);

        Matcher matcher = TYPE_PATTERN.matcher(xmlContent);
        xmlContent = matcher.replaceAll(escapedVarType);

        matcher = VAR_NAME_PATTERN.matcher(xmlContent);
        xmlContent = matcher.replaceAll(escapedVarName);

        matcher = TO_BE_TESTED_PATTERN.matcher(xmlContent);
        xmlContent = matcher.replaceAll(escapedtoBeTested);
        return xmlContent;
    }

    public static String convertToStatement(String expression, BlocklyType expectedType) {
        String varName = VAR_NAME_MAP.get(expectedType);
        Assert.assertNotNull(varName, "Variable of type " + expectedType + " not found");
        return varName + " = " + expression + ";";
    }


    private static String escapeXmlSpecialCharacters(String input) {
        return input
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;");
    }
}
