package de.fhg.iais.roberta.javaServer.integrationTest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModifyXmlStmtProgram {
    public static void newProgram(String newExpression, String nameForXml) {

        String filePath = "OpenRobertaServer/src/test/resources/crossCompilerTests/robotSpecific/microbitv2/failCasesStmt/baseProgram.xml";

        try {

            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            StringBuilder xmlContent = new StringBuilder();
            String line;
            while ( (line = reader.readLine()) != null ) {
                xmlContent.append(line).append("\n");
            }
            reader.close();


            Pattern pattern = Pattern.compile("<field name=\"EXPRESSION\">(.*?)</field>");
            Matcher matcher = pattern.matcher(xmlContent.toString());


            String modifiedXML = matcher.replaceAll("<field name=\"EXPRESSION\">" + newExpression + "</field>");


            BufferedWriter writer = new BufferedWriter(new FileWriter("OpenRobertaServer/src/test/resources/crossCompilerTests/robotSpecific/microbitv2/failCasesStmt/" + nameForXml + ".xml"));
            writer.write(modifiedXML);
            writer.close();

            System.out.println("Modified XML program has been exported to " + nameForXml + ".xml");

        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        newProgram("waitMs(400,0);", "waitMs");
        newProgram("waitMs(true);", "waitMs2");
    }

}
