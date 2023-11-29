package de.fhg.iais.roberta.javaServer.integrationTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
import de.fhg.iais.roberta.util.ast.AstFactory;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.visitor.validate.MicrobitV2TypecheckVisitor;

public class TestFailCasesTypecheck {
    private static final Logger LOG = LoggerFactory.getLogger(TestFailCasesTypecheck.class);
    private static RobotFactory testFactory;

    private static JSONObject robotsFromTestSpec;
    private static JSONObject progDeclsFromTestSpec;
    private static final String CROSS_COMPILER_TESTS = "/crossCompilerTests/";
    private static final String ROBOT_SPECIFIC = "robotSpecific/";
    public static final String TEST_SPEC_YML = "classpath:/crossCompilerTests/testSpec.yml";
    private int typecheckErrorCount = 0;
    private int parserErrorCount = 0;


    @BeforeClass
    public static void setup() {
        AstFactory.loadBlocks();
        Properties properties = Util.loadPropertiesRecursively("classpath:/microbitv2.properties");
        testFactory = new RobotFactory(new PluginProperties("microbitv2", "", "", properties));
        JSONObject testSpecification = Util.loadYAML(TEST_SPEC_YML);
        progDeclsFromTestSpec = testSpecification.getJSONObject("progs");
        robotsFromTestSpec = testSpecification.getJSONObject("robots");
    }

    private void setupRobotFactoryForRobot(String robotName) {
        List<String> pluginDefines = new ArrayList<>(); // maybe used later to add properties
        testFactory = Util.configureRobotPlugin(robotName, "", "", pluginDefines);
    }

    protected String setupRobotFactoryAndGetResourceDirForRobotSpecificTests(String robotName) {
        setupRobotFactoryForRobot(robotName);
        JSONObject robot = robotsFromTestSpec.getJSONObject(robotName);
        final String robotDir = robot.getString("dir");
        return CROSS_COMPILER_TESTS + ROBOT_SPECIFIC + robotDir + (robotDir.endsWith("/") ? "" : "/");
    }

    @Test
    public void testExpressions() throws Exception {
        String robotName = "microbitv2";
        final String resourceDirectory = setupRobotFactoryAndGetResourceDirForRobotSpecificTests(robotName);
        String folderPath = resourceDirectory + "failCases/";

        List<String> expressions = Arrays.asList(
            //STMTS
            "for ( Boolean i=1; i<10; i++){};",
            "for ( Number i=1; i<10; i=true){};",
            "for ( String i=1; i<10; i=i+2+4){};",
            "for each( Boolean item: listN){s=1;};",
            "if(5){s=1;} else if(false){s=2;} else{s=5;};",
            "while(bt==2){s=1;};",
            "while(bt==2+2){s=2;};",
            "while((bt==#black) && (bf==false)){s=2;};",
            "for (Number i = 0; i < 10; i = i + 2) { if (isEven(i+ #black)) {insertLast(listN, i * 2);} else {insertFirst(listN, i);}; };",
            "waitUntil(average(listN) == 5) {insertLast(listN, 7,9);} orWaitFor(average(listN) == 2) {insertFirst(listN, 666);};",
            "waitUntil(#black) {s=1;} orWaitFor(true) {s=2;}; ",
            "wait ms(false);",
            //FNAMESTMT:
            "set(listN, \"hola\",0);",
            "setFromEnd(listN, 1000,0,2);",
            "setFirst(listN,false);",
            "setLast(listN);",
            "insert(listN, true,0);",
            "insertFromEnd(listN, 1000);",
            "insertFirst(listN,666,9);",
            "insertLast(listN,666,true);",
            "remove(listN,0,0);",
            "removeFromEnd(listN);",
            "removeFirst(listN,true);",
            "removeLast();",

            //EXPRESSIONS:
            "s=sin(s+true)+cos(s)+tan(s)+asin(s)+acos(s)+atan(s);",
            "s=exp(2) + square(#black) + sqrt(9) + abs(-5) + log10(100) + log(2);",
            "s=randomInt(1,10,0)+ randomFloat(1);",
            "s= floor(3.7) + ceil(2.3) + round(true);",
            "bt = isEven(10) && isOdd(7,0) || isPrime(11,0) && isWhole(8) || isEmpty(listN) && isPositive(5) || isNegative(-3) && isDivisibleBy(10, 5);",
            "s = sum(listN) + max(listN) - min(listN) * average(listN) / median(listN) + stddev(listN) % size(listN+2) + randomItem(listN,0);",
            "s = indexOfFirst(listN);",
            "s = indexOfLast(listN, true);",
            "t = createTextWith(sin(true));",
            "s = getFirst(listN) +get(listN,1)+getFromEnd(listN,0,1)+getLast(listN,true);",
            "s = getAndRemoveFirst(listN,0) + getAndRemove(listN, 1,2) + getAndRemoveFromEnd(listN, 0,true) + getAndRemoveLast(listN,false);",

            "listN = subList(listN2,0,true);",
            "listN = subListFromIndexToEnd(listN2,#black,1);",
            "listN = subListFromEndToIndex(listN2,1,1,2);",
            "listN = subListFromEndToEnd(listN2,4,2,false);",
            "listN = subListFromIndexToLast(listN2,1,3);",
            "listN = subListFromFirstToIndex();",
            "listN = subListFromFirstToLast(listN2,2);",
            "listN = subListFromFirstToIndex(listN2,false);",
            "listN = subListFromEndToLast(listN2,3,3);"
        );

        for ( String expression : expressions ) {
            ModifyXmlStmtProgram.newProgram(expression, "testTextRep");
            getProgramAstFromExportXmlAndExpr(testFactory, folderPath + "testTextRep.xml");
            checkResults();
        }

    }

    @Ignore
    @Test
    public void testSingleExpression() throws Exception {
        String robotName = "microbitv2";
        final String resourceDirectory = setupRobotFactoryAndGetResourceDirForRobotSpecificTests(robotName);
        String folderPath = resourceDirectory + "failCases/";

        String expression = "s=isEven(7,2);";

        ModifyXmlStmtProgram.newProgram(expression, "testTextRep");
        getProgramAstFromExportXmlAndExpr(testFactory, folderPath + "testTextRep.xml");
        checkResults();
    }

    @Ignore
    @Test
    public void testOneForDebug() throws Exception {
        String robotName = "microbitv2";
        String programName = "repeatInf2";
        final String resourceDirectory = setupRobotFactoryAndGetResourceDirForRobotSpecificTests(robotName);

        String folderPath = resourceDirectory + "failCases";
        System.out.println("========= testing program " + programName + " =========");
        getProgramAstFromExportXmlAndExpr(testFactory, folderPath + "/" + programName + ".xml");
        checkResults();
    }

    private static List<NepoInfo> typecheckAndCollectInfos(Phrase ast, UsedHardwareBean usedHardwareBean) {
        MicrobitV2TypecheckVisitor visitor = new MicrobitV2TypecheckVisitor(usedHardwareBean);
        ast.accept(visitor);
        return InfoCollector.collectInfos(ast);
    }

    public void getProgramAstFromExportXmlAndExpr(RobotFactory factory, String xml) throws IOException {
        String exportedProgram = new String(Files.readAllBytes(Paths.get("src/test/resources/" + xml)));
        Project.Builder builder = UnitTestHelper.setupWithExportXML(factory, exportedProgram);

        List<String> expression = extractExpression(factory, exportedProgram);
        Project project = builder.build();

        UnitTestHelper.executeWorkflow("showsource", testFactory, project);
        UsedHardwareBean usedHardwareBean = project.getWorkerResult(UsedHardwareBean.class);

        LOG.info("========= Evaluating the expression\n " + expression);
        for ( List<Phrase> listOfPhrases : project.getProgramAst().getTree() ) {
            for ( Phrase phrase : listOfPhrases ) {
                List<NepoInfo> infos = typecheckAndCollectInfos(phrase, usedHardwareBean);
                for ( NepoInfo info : infos ) {
                    if ( info.getSeverity() == NepoInfo.Severity.ERROR ) {
                        if ( info.getMessage().contains("PARSE") ) {
                            parserErrorCount++;
                        } else {
                            typecheckErrorCount++;
                        }
                    }
                }
            }
        }
    }

    private void checkResults() {
        LOG.info("Typecheck errors: " + typecheckErrorCount);
        if ( parserErrorCount > 0 ) {
            Assert.fail("Parser errors found: " + parserErrorCount);
        }
        Assert.assertTrue("There should be typecheck errors", typecheckErrorCount > 0);
        typecheckErrorCount = 0;
        parserErrorCount = 0;
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

    @Deprecated
    private void testFailCases(String robotName) throws Exception {
        final String resourceDirectory = setupRobotFactoryAndGetResourceDirForRobotSpecificTests(robotName);
        String folderPath = resourceDirectory + "failCases/";
        File folder = new File("src/test/resources" + folderPath);
        File[] listOfFiles = folder.listFiles();
        for ( File file : listOfFiles ) {
            String programName = file.getName();
            System.out.println("========= testing program " + programName + " =========");
            getProgramAstFromExportXmlAndExpr(testFactory, folderPath + "/" + programName);
            checkResults();
        }
    }

    @Deprecated
    public void testAll() throws Exception {
        testFailCases("microbitv2");
    }

    @Deprecated
    private void testFailStmtCases(String robotName) throws Exception {
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

}
