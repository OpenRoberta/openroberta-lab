package de.fhg.iais.roberta.javaServer.integrationTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.FileUtils;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.basic.Pair;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.testsetup.IntegrationTest;
import static org.junit.Assert.fail;

@Category(IntegrationTest.class)
public class PythonLinterWorkflowRobotSpecificIT {
    private static final Logger LOG = LoggerFactory.getLogger("PYTHONLINTERSPECIFIC-IT");


    private static final String PYTHON_LINTER = "pylint ";
    private static final String[] PARAM = {
        "--output-format=json",
        "--disable=E1101,E0107"
        //disabling these error types because of false positives
    };
    private static final String SUFFIX = ".py";


    private static final boolean SHOW_SUCCESS = true;
    private static final boolean SHOW_WARNINGS = false;
    private static final List<String> results = new ArrayList<>();
    private static final String resourceBaseSpecific = "/crossCompilerTests/_expected/robotSpecific/targetLanguage/";
    private static final String resourceBaseCommon = "/crossCompilerTests/_expected/common/targetLanguage/";
    private static JSONObject robotsFromTestSpec;

    @BeforeClass
    public static void setupClass() throws Exception {
        if ( !isPylintInstalled() ) {
            LOG.error("Python 3.X and Pylint 3.X must be installed to run these tests - test fails");
            fail();
        }
        JSONObject testSpecification = Util.loadYAML("classpath:/crossCompilerTests/testSpec.yml");
        robotsFromTestSpec = testSpecification.getJSONObject("robots");

    }

    public static boolean isPylintInstalled() {
        boolean isInstalled = false;
        try {
            Process process = Runtime.getRuntime().exec("pylint --version");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output = reader.readLine();
            if ( output != null && output.contains("pylint 3") ) {
                isInstalled = true;
            }
            reader.close();
        } catch ( IOException e ) {
            LOG.error("Something went wrong while checking if the pylint module is installed - test fails");
            fail();
        }
        return isInstalled;
    }

    @AfterClass
    public static void tearDownAndPrintResults() {
        LOG.info("XXXXXXXXXX result of the python linter XXXXXXXXXX");
        for ( String result : results ) {
            LOG.info(result);
        }
    }

    @Test
    public void testCommonPythonPrograms() {
        LOG.info("XXXXXXXXXX START of the common Python linter XXXXXXXXXX");
        Set<String> robotDirectories = FileUtils.fileStreamOfResourceDirectory(resourceBaseCommon).collect(Collectors.toSet());
        if ( testPrograms(robotDirectories, resourceBaseCommon) ) {
            LOG.info("XXXXXXXXXX no error in the common Python programs detected XXXXXXXXXX");
        } else {
            LOG.error("XXXXXXXXXX at least one common Python program FAILED XXXXXXXXXX");
            fail();
        }
    }

    @Test
    public void testSpecificPythonPrograms() {
        LOG.info("XXXXXXXXXX START of the specific Python linter XXXXXXXXXX");
        final Set<String> robotList = robotsFromTestSpec.keySet();
        if ( testPrograms(robotList, resourceBaseSpecific) ) {
            LOG.info("XXXXXXXXXX no error in the Python programs detected XXXXXXXXXX");
        } else {
            LOG.error("XXXXXXXXXX at least one Python program FAILED XXXXXXXXXX");
            fail();
        }
    }

    private boolean testPrograms(Set<String> robotList, String resourceBase) {
        boolean resultAcc = true;
        for ( final String robotName : robotList ) {
            final JSONObject robot = robotsFromTestSpec.getJSONObject(robotName);
            if ( !robot.getString("suffix").equals(SUFFIX) ) {
                continue;
            }
            JSONArray jsonArray = robot.getJSONArray("pythonIgnoredModules");
            final String ignoredModules = IntStream.range(0, jsonArray.length()).mapToObj(jsonArray::get).map(Objects::toString).collect(Collectors.joining(","));
            final String resourceDirectory = resourceBase + "/" + robotName;
            boolean resultNext = FileUtils.fileStreamOfResourceDirectory(resourceDirectory).
                map(f -> compileNative(robotName, resourceBase, ignoredModules, f)).reduce(true, (a, b) -> a && b);
            resultAcc = resultAcc && resultNext;
        }
        return resultAcc;
    }

    private boolean compileNative(String robotName, String resourceBase, String ignoredModules, String resource) throws DbcException {
        if ( !resource.endsWith(SUFFIX) ) {
            return true;
        }
        String expectResult = resource.startsWith("error") ? "error" : "ok";
        String fullResource = resourceBase + robotName + "/" + resource;
        String output = "";
        Pair<Boolean, String> result = Pair.of(false, "");
        File tmp;
        try {
            logStart(robotName, fullResource);
            tmp = createTmpFile(resource, fullResource);
            String cmd = PYTHON_LINTER + Arrays.stream(PARAM).map(Objects::toString).collect(Collectors.joining(" ")) + " --ignored-modules=" + ignoredModules + " " + tmp.getAbsolutePath();

            Process pylintProc = Runtime.getRuntime().exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(pylintProc.getInputStream()));
            StringJoiner sj = new StringJoiner(System.getProperty("line.separator"));
            reader.lines().iterator().forEachRemaining(sj::add);
            output = sj.toString();
            pylintProc.destroy();
            tmp.delete();

            result = checkLinterOutput(output, expectResult);
        } catch ( IOException ioe ) {
            LOG.error("an error occurred while creating the tmp file", ioe);
        } catch ( Exception e ) {
            LOG.error("exception when executing pylint", e);
            return false;
        }
        log(result, robotName, fullResource, expectResult);
        return result.getFirst();
    }

    private void logStart(String name, String fullResource) {
        String format = "[[[[[[[[[[ Robot: %-15s check file with %s: %s";
        String msg = String.format(format, name, PYTHON_LINTER, fullResource);
        LOG.info(msg);
    }

    private void log(Pair<Boolean, String> result, String robotName, String fullResource, String expectedResult) {
        String format;
        if ( result.getFirst() || expectedResult.equals("error") ) {
            format = "++++++++++ Robot: %-15s succeeded checking file: %s\n\n";
            LOG.info(String.format(format, robotName, fullResource));
        } else {
            format = "---------- Robot: %-15s FAILED issues detected by python linter in file: %s\n\n";
            LOG.error(String.format(format, robotName, fullResource));
        }
        LOG.info(result.getSecond());
        LOG.info("]]]]]]]]]]");

        if ( result.getFirst() ) {
            if ( SHOW_SUCCESS ) {
                results.add(String.format("succ; %-15s; %-60s;", robotName, fullResource));
            }
        } else {
            results.add(String.format("FAIL; %-15s; %-60s;", robotName, fullResource));
        }
    }

    private File createTmpFile(String fileName, String fullResource) throws IOException {
        File tmp = File.createTempFile(fileName.substring(0, fileName.length() - SUFFIX.length()), SUFFIX);
        tmp.deleteOnExit();
        String fileContent = Util.readResourceContent(fullResource);
        if ( !tmp.exists() ) {
            throw new FileNotFoundException("File " + tmp.getAbsolutePath() + " does not exist.");
        }
        try (FileOutputStream os = new FileOutputStream(tmp)) {
            os.write(fileContent.getBytes());
        }
        return tmp;
    }

    private Pair<Boolean, String> checkLinterOutput(String output, String expectedResult) {
        Assert.assertTrue("expectedRc is invalid", "ok".equals(expectedResult) || "error".equals(expectedResult));
        StringBuilder errorMsg = new StringBuilder("\nMessages: fatal and error\n");
        StringBuilder otherMsg = SHOW_WARNINGS ? new StringBuilder("\nMessages: warning, convention, refactor and information\n") : new StringBuilder();
        boolean result = true;
        boolean shouldFail = expectedResult.equals("error");
        try {
            JSONArray jsonOutput = new JSONArray(output);
            for ( Object ob : jsonOutput ) {
                JSONObject json = (JSONObject) ob;
                String type = json.getString("message-id");
                if ( !shouldFail && (type.startsWith("F") || type.startsWith("E")) ) {
                    result = false;
                }
                if ( type.startsWith("F") || type.startsWith("E") ) {
                    errorMsg.append(String.format("\t\t%s: %d:%-4d: %s\n", type, json.get("line"), json.get("column"), json.getString("message")));
                } else if ( SHOW_WARNINGS ) {
                    otherMsg.append(String.format("\t\t%s: %d:%-4d: %s\n", type, json.get("line"), json.get("column"), json.getString("message")));
                }
            }
        } catch ( Exception e ) {
            LOG.error("Something went wrong while processing the output: " + e);
            return Pair.of(false, "");
        }

        return Pair.of(result, errorMsg + "\n" + otherMsg);
    }
}
