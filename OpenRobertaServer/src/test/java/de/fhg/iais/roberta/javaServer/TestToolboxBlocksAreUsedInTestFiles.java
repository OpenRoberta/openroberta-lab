package de.fhg.iais.roberta.javaServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.dbc.Assert;

public class TestToolboxBlocksAreUsedInTestFiles {
    private static Logger LOG = LoggerFactory.getLogger(TestToolboxBlocksAreUsedInTestFiles.class);
    private static final Pattern GET_NAME_INTOOLBOX_CONTENT = Pattern.compile("block[^>]+type=[\"']([^\"']*)[\"']");

    private static final Path commonResourcesPath = Paths.get("src/test/resources/crossCompilerTests/common");
    private static final Path specificResourcesPath = Paths.get("src/test/resources/crossCompilerTests/robotSpecific");
    private static final JSONObject testSpecification = Util.loadYAML("classpath:/crossCompilerTests/testSpec.yml");
    private static final JSONObject robots = testSpecification.getJSONObject("robots");

    private static Set<String> commonBlocksInTestFiles;

    /**
     * assemble all block names, that are used in the test suite. Do this once and reuse it in the tests for each robot
     *
     * @throws Exception
     */
    @BeforeClass
    public static void beforeClass() throws Exception {
        commonBlocksInTestFiles = parseResourceFilesFromPath(commonResourcesPath);
    }

    private static Set<String> parseResourceFilesFromPath(Path subDir) {
        try {
            return parseResourceFiles(Files.walk(subDir));
        } catch ( IOException e ) {
            throw new RuntimeException(e);
        }
    }

    private static Set<String> parseResourceFiles(Stream<Path> resources) throws IOException {
        return resources
            .filter(file -> file.getFileName().toString().endsWith(".xml"))
            .filter(file -> !file.getParent().startsWith("_"))
            .filter(file -> file.toFile().isFile())
            .map(file -> Util.readFileContent(file.toString()))
            .map(TestToolboxBlocksAreUsedInTestFiles::getBlockNamesFromContent)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
    }

    private static Set<String> getBlockNamesForAllToolboxes(RobotFactory robotFactory) {
        Set<String> blockNames = new HashSet<>();
        blockNames.addAll(getBlockNamesFromContent(robotFactory.getProgramToolboxBeginner()));
        blockNames.addAll(getBlockNamesFromContent(robotFactory.getProgramToolboxExpert()));
        return blockNames;
    }

    private static Set<String> getBlockNamesFromContent(String content) {
        Matcher matcher = GET_NAME_INTOOLBOX_CONTENT.matcher(content);
        Set<String> blocks = new HashSet<>();
        while ( matcher.find() ) {
            blocks.add(matcher.group(1));
        }
        return blocks;
    }

    @Test
    public void testGetBlockNamesFromContent() {
        String content1 =
            "        <block type='robSensors_record_begin'>\n" +
                "            <value name='FILENAME'>\n" +
                "                <block type='math_integer'>\n" +
                "                    <field name='NUM'>1</field>\n" +
                "                </block>\n" +
                "            </value>\n" +
                "        </block>\n" +
                "        <block type='robSensors_record_stop'/>";
        String content2 =
            "        <block type=\"robSensors_record_begin\">\n" +
                "            <value name=\"FILENAME\">\n" +
                "                <block type=\"math_integer\">\n" +
                "                    <field name=\"NUM\">1</field>\n" +
                "                </block>\n" +
                "            </value>\n" +
                "        </block>\n" +
                "        <block type=\"robSensors_record_stop\"/>";
        Assert.isTrue(getBlockNamesFromContent(content1).contains("robSensors_record_begin"), "block is missing");
        Assert.isTrue(getBlockNamesFromContent(content1).contains("robSensors_record_stop"), "block is missing");
        Assert.isTrue(getBlockNamesFromContent(content2).contains("robSensors_record_begin"), "block is missing");
        Assert.isTrue(getBlockNamesFromContent(content2).contains("robSensors_record_stop"), "block is missing");
    }

    @Test
    public void toolBoxBlockIsPresentInTestResources() {
        boolean hasGlobalErrors = false;
        for ( String robotName : robots.keySet() ) {
            Set<String> blockNamesFromCommonAndSpecificTestFiles = new HashSet<>();
            blockNamesFromCommonAndSpecificTestFiles.addAll(commonBlocksInTestFiles);
            RobotFactory factory = Util.configureRobotPlugin(robotName, "", "", new ArrayList<>());
            String dirOfRobotSpecificTestFiles = robots.getJSONObject(robotName).getString("dir");
            Set<String> blockNamesFromRobotSpecificTestFiles = parseResourceFilesFromPath(specificResourcesPath.resolve(dirOfRobotSpecificTestFiles));
            blockNamesFromCommonAndSpecificTestFiles.addAll(blockNamesFromRobotSpecificTestFiles);
            for ( String blockNameUsedInToolbox : getBlockNamesForAllToolboxes(factory) ) {
                if ( !blockNamesFromCommonAndSpecificTestFiles.contains(blockNameUsedInToolbox) ) {
                    LOG.error(String.format("block %s not found in common or specific tests for robot %s", blockNameUsedInToolbox, robotName));
                    hasGlobalErrors = true;
                }
            }
        }
        if ( hasGlobalErrors ) {
            Assert.fail("some blocks from toolboxes not found in the test files");
        } else {
            LOG.error("OK: all blocks found in toolboxes of all robots are used in the test files");
        }
    }
}
