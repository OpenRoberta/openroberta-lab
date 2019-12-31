package de.fhg.iais.roberta.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.fhg.iais.roberta.factory.EV3Factory;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.dbc.Assert;
import joptsimple.OptionParser;
import joptsimple.OptionSpec;

public class HttpSessionStateTest {

    HttpSessionState httpSessionState;

    @Before
    public void setup() {
        OptionParser parser = new OptionParser();
        OptionSpec<String> serverDefineOpt = parser.acceptsAll(Arrays.asList("d", "server-property")).withRequiredArg().ofType(String.class);
        List<String> serverDefines = serverDefineOpt.options();

        Map<String, IRobotFactory> robotPluginMap = new HashMap<>();
        robotPluginMap.put("ev3lejosv1", new EV3Factory(new PluginProperties("ev3lejosv1", "", "", Util.loadProperties("classpath:/ev3lejosv1.properties"))));
        robotPluginMap.put("ev3lejosv0", new EV3Factory(new PluginProperties("ev3lejosv0", "", "", Util.loadProperties("classpath:/ev3lejosv0.properties"))));
        robotPluginMap.put("calliope2017", new RobotFactory(new PluginProperties("calliope2017", "", "", Util.loadProperties("classpath:/calliope2017.properties"))));
        robotPluginMap.put("calliope2016", new RobotFactory(new PluginProperties("calliope2016", "", "", Util.loadProperties("classpath:/calliope2016.properties"))));
        robotPluginMap.put("mbot", new RobotFactory(new PluginProperties("mbot", "", "", Util.loadProperties("classpath:/mbot.properties"))));
        ServerProperties serverProperties = new ServerProperties(Util.loadAndMergeProperties(null, serverDefines));

        httpSessionState = HttpSessionState.init(robotPluginMap, serverProperties, "en");
    }

    @Test
    public void getRobotFactoriesOfGroup_ShouldReturnMembers_WhenGivenValidGroup() {
        List<IRobotFactory> ev3Members = httpSessionState.getRobotFactoriesOfGroup("ev3");
        Assert.isTrue(ev3Members.stream().allMatch(factory -> factory.getGroup().equals("ev3")));
        List<IRobotFactory> calliopeMembers = httpSessionState.getRobotFactoriesOfGroup("calliope");
        Assert.isTrue(calliopeMembers.stream().allMatch(factory -> factory.getGroup().equals("calliope")));
    }

    @Test
    public void getRobotFactoriesOfGroup_ShouldReturnListWithRobot_WhenGivenNoGroupRobot() {
        List<IRobotFactory> mbotMembers = httpSessionState.getRobotFactoriesOfGroup("mbot");
        Assert.isTrue(mbotMembers.stream().allMatch(factory -> factory.getGroup().equals("mbot")));
    }

    @Test
    public void getRobotFactoriesOfGroup_ShouldReturnEmptyList_WhenGivenInvalidGroup() {
        List<IRobotFactory> imaginaryTestGroupMembers = httpSessionState.getRobotFactoriesOfGroup("imaginaryTestGroup");
        Assert.isTrue(imaginaryTestGroupMembers.isEmpty());
    }
}
