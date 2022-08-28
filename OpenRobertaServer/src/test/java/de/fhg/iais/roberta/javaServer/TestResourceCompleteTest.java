package de.fhg.iais.roberta.javaServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.util.Util;

@RunWith(Parameterized.class)
public class TestResourceCompleteTest {

    private static final Path commonResourcesPath = Paths.get("src/test/resources/crossCompilerTests/common");
    private static final Path specificResourcesPath = Paths.get("src/test/resources/crossCompilerTests/robotSpecific");
    private static final JSONObject testSpecification = Util.loadYAML("classpath:/crossCompilerTests/testSpec.yml");
    private static final JSONObject robots = testSpecification.getJSONObject("robots");

    private static Set<String> commonBlocksInTestResources;
    private static Map<String, Set<String>> robotGroupToBlocksInTestResourcesMap;

    private String robot;
    private String block;

    public TestResourceCompleteTest(String robot, String block) {
        this.robot = robot;
        this.block = block;
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        commonBlocksInTestResources = parseResourceFilesFromPath(commonResourcesPath);
        robotGroupToBlocksInTestResourcesMap = robots.keySet().stream()
            .map(robotName -> new AbstractMap.SimpleEntry<>(Util.configureRobotPlugin(robotName, "", "", new ArrayList<>()).getGroup(), robots.getJSONObject(robotName).getString("dir")))
            .distinct()
            .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, entry -> parseResourceFilesFromPath(specificResourcesPath.resolve(entry.getValue())), (firstList, secondList) -> Stream.concat(firstList.stream(), secondList.stream()).collect(Collectors.toSet())));
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
            .map(TestResourceCompleteTest::parseBlockTypes)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
    }

    @Parameterized.Parameters(name = "{0} - {1}")
    public static Collection<Object[]> data() throws IOException {
        Comparator<List<String>> robotNameComparator = Comparator.comparing(s -> s.get(0));
        Comparator<List<String>> comparator = robotNameComparator.thenComparing(s -> s.get(1));

        return robots.keySet().stream()
            .map(robot -> Util.configureRobotPlugin(robot, "", "", new ArrayList<>()))
            .flatMap(TestResourceCompleteTest::generateTestDateForEachToolbox)
            .distinct()
            .sorted(comparator)
            .map(List::toArray)
            .collect(Collectors.toList());
    }

    private static Stream<List<String>> generateTestDateForEachToolbox(RobotFactory robotFactory) {
        return Stream.of(generateTestDataForRobotFactory(robotFactory, true), generateTestDataForRobotFactory(robotFactory, false)).flatMap(Collection::stream);
    }

    private static List<List<String>> generateTestDataForRobotFactory(RobotFactory robotFactory, boolean beginner) {
        Set<String> blocks = parseBlockTypes(beginner ? robotFactory.getProgramToolboxBeginner() : robotFactory.getProgramToolboxExpert());

        String robotName = robotFactory.getGroup();
        return blocks.stream()
            .map(blockType -> Arrays.asList(robotName, blockType))
            .collect(Collectors.toList());
    }

    private static Set<String> parseBlockTypes(String content) {
        Pattern pattern = Pattern.compile("block[^>]+type=\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(content);

        Set<String> blocks = new HashSet<>();

        while ( matcher.find() ) {
            blocks.add(matcher.group(1));
        }
        return blocks;
    }

    @Test
    public void toolBoxBlockIsPresentInTestResources() {
        Set<String> commonAndSpecificBlocks = new HashSet<>();
        commonAndSpecificBlocks.addAll(commonBlocksInTestResources);
        commonAndSpecificBlocks.addAll(robotGroupToBlocksInTestResourcesMap.get(this.robot));

        Assertions.assertThat(commonAndSpecificBlocks).withFailMessage("%s was neither found in common tests nor in %s specific tests", block, robot).contains(block);
    }
}
