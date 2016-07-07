package de.fhg.iais.roberta.persistence.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.dbc.Assert;

public class HttpSessionState {
    private static final Logger LOG = LoggerFactory.getLogger(HttpSessionState.class);
    public final static int NO_USER = -1;

    private int userId = HttpSessionState.NO_USER;
    private int robotId = 42;
    private String token = "1Q2W3E4R";
    private String programName;
    private String program;
    private String configurationName;
    private String configuration;
    private String toolboxName;
    private String toolbox;
    private final RobotCommunicator robotCommunicator;

    public HttpSessionState(RobotCommunicator robotCommunicator) {
        this.robotCommunicator = robotCommunicator;
    }

    public static HttpSessionState init(RobotCommunicator robotCommunicator) {
        return new HttpSessionState(robotCommunicator);
    }

    public int getUserId() {
        return this.userId;
    }

    public boolean isUserLoggedIn() {
        return this.userId >= 1;
    }

    public void setUserClearDataKeepTokenAndRobotId(int userId) {
        Assert.isTrue(userId >= 1 || userId == HttpSessionState.NO_USER);
        // token is not cleared. This would annoy the user.
        // robotId is not cleared. This would annoy the user.
        this.userId = userId;
        this.programName = null;
        this.program = null;
        this.configurationName = null;
        this.configuration = null;
    }

    public int getRobotId() {
        return this.robotId;
    }

    public void setRobotId(int robotId) {
        Assert.notNull(robotId);
        this.robotId = robotId;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        Assert.notNull(token);
        this.token = token;
    }

    public String getProgramName() {
        return this.programName;
    }

    public String getProgram() {
        return this.program;
    }

    public void setProgramNameAndProgramText(String programName, String program) {
        this.programName = programName;
        this.program = program;
    }

    public String getConfigurationName() {
        return this.configurationName;
    }

    public String getConfiguration() {
        return this.configuration;
    }

    public void setConfigurationNameAndConfiguration(String configurationName, String configuration) {
        this.configurationName = configurationName;
        this.configuration = configuration;
    }

    public String getToolboxName() {
        return this.toolboxName;
    }

    public String getToolbox() {
        return this.toolbox;
    }

    public void setToolboxNameAndToolbox(String toolboxName, String toolbox) {
        this.toolboxName = toolboxName;
        this.toolbox = toolbox;
    }

    public IRobotFactory getSimulationFactory() {
        try {
            return (IRobotFactory) this.getClass().getClassLoader().loadClass("de.fhg.iais.roberta.factory.SimFactory").newInstance();
        } catch ( InstantiationException | IllegalAccessException | ClassNotFoundException | SecurityException | IllegalArgumentException e ) {
            LOG.error("Simulation Factory Not Found!. Check the robot configuration property. System will crash!", e);
            return null;
        }
    }

    public IRobotFactory getRobotFactory() {
        try {
            Constructor<?> constructur =
                this.getClass().getClassLoader().loadClass("de.fhg.iais.roberta.factory.EV3Factory").getDeclaredConstructor(RobotCommunicator.class);

            if ( this.robotId == 42 ) {
                constructur =
                    this.getClass().getClassLoader().loadClass("de.fhg.iais.roberta.factory.EV3Factory").getDeclaredConstructor(RobotCommunicator.class);

            } else if ( this.robotId == 43 ) {
                constructur =
                    this.getClass().getClassLoader().loadClass("de.fhg.iais.roberta.factory.NxtFactory").getDeclaredConstructor(RobotCommunicator.class);
            } else {
                LOG.error("Invalide Robot Id" + this.robotId + "!. Returning EV3 factory.");
            }
            IRobotFactory factory = (IRobotFactory) constructur.newInstance(this.robotCommunicator);

            return factory;
        } catch ( InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | SecurityException
            | IllegalArgumentException | InvocationTargetException e ) {
            LOG.error("Robot Factory Not Found!. Check the robot configuration property. System will crash!");
            return null;
        }
    }
}