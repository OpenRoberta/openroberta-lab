package de.fhg.iais.roberta.ast;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.util.Util;

public abstract class AstTest {
    private static final List<String> pluginDefines = new ArrayList<>();
    protected static IRobotFactory testFactory;

    @BeforeClass
    public static void setup() {
        String robotName = "test";
        String pwd = System.getProperty("user.dir");
        if ( pwd == null || pwd.isEmpty() || pwd.contains("OpenRobertaRobot") ) {
            robotName = "test";
            pluginDefines.add("test:robot.configuration.type = new");
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
        } else if ( pwd.contains("RobotVorwerk") ) {
            robotName = "vorwerk";
        } else if ( pwd.contains("RobotWeDo") ) {
            robotName = "wedo";
        }
        testFactory = Util.configureRobotPlugin(robotName, "", "", pluginDefines);
    }
}
