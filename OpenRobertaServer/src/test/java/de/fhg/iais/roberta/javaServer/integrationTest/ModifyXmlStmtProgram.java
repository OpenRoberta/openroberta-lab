package de.fhg.iais.roberta.javaServer.integrationTest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.fhg.iais.roberta.util.Util;

public class ModifyXmlStmtProgram {
    public static void newProgram(String newExpression, String nameForXml) {

        String filePath = "/crossCompilerTests/robotSpecific/microbitv2/failCases/baseProgram.xml";
        String outputFilePath = "src/test/resources/crossCompilerTests/robotSpecific/microbitv2/failCases/" + nameForXml + ".xml";
        try {

            String xmlContent = Util.readResourceContent(filePath);

            String escapedExpression = escapeXmlSpecialCharacters(newExpression);

            Pattern pattern = Pattern.compile("<field name=\"EXPRESSION\">(.*?)</field>");
            Matcher matcher = pattern.matcher(xmlContent);

            String modifiedXML = matcher.replaceAll("<field name=\"EXPRESSION\">" + escapedExpression + "</field>");


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
