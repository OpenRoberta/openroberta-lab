package de.fhg.iais.roberta;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.ast.AstFactory;

public abstract class AstTest {
    private static final List<String> pluginDefines = new ArrayList<>();
    private static final List<String> pluginDefinesNewConf = new ArrayList<>();
    protected static RobotFactory testFactory;
    protected static RobotFactory testFactoryNewConf;

    @BeforeClass
    public static void setupForAllSubclasses() {
        AstFactory.loadBlocks();
        String robotName = "test";
        String pwd = System.getProperty("user.dir");
        if ( pwd == null || pwd.isEmpty() || pwd.contains("OpenRobertaRobot") ) {
            robotName = "test";
            pluginDefines.add("test:robot.configuration.type = old-S");
            pluginDefines.add("test:robot.configuration.old.toplevelblock = robBrick_EV3-Brick");
            pluginDefinesNewConf.add("test:robot.configuration.type = new");
        } else if ( pwd.contains("RobotArdu") ) {
            robotName = "nano";
        } else if ( pwd.contains("RobotEdison") ) {
            robotName = "edison";
        } else if ( pwd.contains("RobotEV3") ) {
            robotName = "ev3dev";
        } else if ( pwd.contains("RobotMbed") ) {
            robotName = "calliope2017NoBlue";
        } else if ( pwd.contains("RobotNAO") ) {
            robotName = "nao";
        } else if ( pwd.contains("RobotNXT") ) {
            robotName = "nxt";
        } else if ( pwd.contains("RobotRaspberryPi") ) {
            robotName = "nano";
        } else if ( pwd.contains("RobotWeDo") ) {
            robotName = "wedo";
        }
        testFactory = Util.configureRobotPlugin(robotName, "", "", pluginDefines);
        testFactoryNewConf = Util.configureRobotPlugin(robotName, "", "", pluginDefinesNewConf);
    }
}
