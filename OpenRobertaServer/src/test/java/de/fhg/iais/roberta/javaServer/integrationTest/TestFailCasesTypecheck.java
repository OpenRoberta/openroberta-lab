package de.fhg.iais.roberta.javaServer.integrationTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.typecheck.InfoCollector;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.visitor.validate.MicrobitV2TypecheckVisitor;

public class TestFailCasesTypecheck extends ReuseIntegrationAsUnitTest {
    private static final Logger LOG = LoggerFactory.getLogger(ReuseIntegrationAsUnitTest.class);
    private static RobotFactory testFactory;
    private static UsedHardwareBean usedHardwareBean;


    @BeforeClass
    public static void setup() {
        Properties properties = Util.loadPropertiesRecursively("classpath:/microbitv2.properties");
        testFactory = new RobotFactory(new PluginProperties("microbitv2", "", "", properties));
        UsedHardwareBean.Builder usedHardware = new UsedHardwareBean.Builder();
        usedHardwareBean = usedHardware.build();
    }

    @Test
    public void testOneForDebug() throws Exception {
        String robotName = "microbitv2";
        String programName = "repeatwhile";
        final String resourceDirectory = setupRobotFactoryAndGetResourceDirForRobotSpecificTests(robotName);

        String folderPath = resourceDirectory + "failCasesStmt";
        System.out.println("========= testing program " + programName + " =========");
        getProgramAstFromExportXmlAndExpr(testFactory, folderPath + "/" + programName + ".xml");
    }

    private static List<NepoInfo> typecheckAndCollectInfos(Phrase ast) {
        MicrobitV2TypecheckVisitor visitor = new MicrobitV2TypecheckVisitor(usedHardwareBean);
        ast.accept(visitor);
        return InfoCollector.collectInfos(ast);
    }

    public void getProgramAstFromExportXmlAndExpr(RobotFactory factory, String xml) {
        String exportedProgram = Util.readResourceContent(xml);
        Project.Builder builder = UnitTestHelper.setupWithExportXML(factory, exportedProgram);
        List<String> expression = extractExpression(factory, exportedProgram);
        Project project = builder.build();

        int errors = 0;
        System.out.println(" Evaluating the expression" + (expression.size() > 1 ? "s: " : ": ") + expression);
        for ( List<Phrase> listOfPhrases : project.getProgramAst().getTree() ) {
            for ( Phrase phrase : listOfPhrases ) {
                List<NepoInfo> infos = typecheckAndCollectInfos(phrase);
                int errorsOfThisPhrase = InfoCollector.collectInfos(phrase).size();
                errors += errorsOfThisPhrase;
                if ( infos.size() > 0 ) {
                    System.out.println("    Encountered " + infos.size() + " error" + (infos.size() > 1 ? "s: " : ": "));
                    for ( NepoInfo info : infos ) {
                        System.out.println("        " + info.toString());
                    }
                }
            }
        }
        if ( errors == 0 ) {
            System.out.println("No errors found");
        }
    }

    public static List<String> extractExpression(RobotFactory factory, String exportXmlAsString) {
        List<String> expressions = new ArrayList<>();
        String[] parts = exportXmlAsString.split("\\s*</program>\\s*<config>\\s*");
        String[] programParts = parts[0].split("<program>");
        String program = programParts[1];

        String regex = "<field\\s+name=\"EXPRESSION\">([^<]+)</field>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(program);

        // Find all EXPRESSION values
        while ( matcher.find() ) {
            String expressionValue = matcher.group(1);
            expressions.add(expressionValue);
        }

        if ( expressions.isEmpty() ) {
            System.out.println("Expression not found in the program");
        }
        return expressions;
    }

    private void testFailCases(String robotName) throws Exception {
        final String resourceDirectory = setupRobotFactoryAndGetResourceDirForRobotSpecificTests(robotName);
        String folderPath = resourceDirectory + "failCases/";
        File folder = new File("src/test/resources" + folderPath);
        File[] listOfFiles = folder.listFiles();
        for ( File file : listOfFiles ) {
            String programName = file.getName();
            System.out.println("========= testing program " + programName + " =========");
            getProgramAstFromExportXmlAndExpr(testFactory, folderPath + "/" + programName);
        }
    }

    @Test
    public void testAll() throws Exception {
        testFailCases("microbitv2");
    }

    private void testFailStmtCases(String robotName) throws Exception {
        final String resourceDirectory = setupRobotFactoryAndGetResourceDirForRobotSpecificTests(robotName);
        String folderPath = resourceDirectory + "failCasesStmt/";
        File folder = new File("src/test/resources" + folderPath);
        File[] listOfFiles = folder.listFiles();
        for ( File file : listOfFiles ) {
            String programName = file.getName();
            System.out.println("========= testing program " + programName + " =========");
            getProgramAstFromExportXmlAndExpr(testFactory, folderPath + "/" + programName);
        }
    }

    @Test
    public void testAllStmt() throws Exception {
        testFailStmtCases("microbitv2");
    }
}
